package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.ReservationRepository;
import com.levdevs.freindshipbe.DTO.*;
import com.levdevs.freindshipbe.Entity.*;
import com.levdevs.freindshipbe.enums.AdminSettingKey;
import com.levdevs.freindshipbe.enums.ReservationStatus;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final LocationService locationService;
    private final S3Service s3Service;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final AuditService auditService;
    private final AdminSettingsService adminSettings;

    public ReservationService(ReservationRepository reservationRepository, LocationService locationService, S3Service s3Service, EmailService emailService, AuditService auditService, AdminSettingsService adminSettings) {
        this.reservationRepository = reservationRepository;
        this.locationService = locationService;
        this.s3Service = s3Service;
        this.emailService = emailService;
        this.auditService = auditService;
        this.adminSettings = adminSettings;
    }

    public MultipartFile getFile(Long reservationId, String path) {
        String filePath = "reservations/" + reservationId + "/" + path;

        return s3Service.downloadFile(filePath);
    }

    public String getPresignedUrl(Long reservationId, String path) {
        String filePath = "reservations/" + reservationId + "/" + path;

        return s3Service.generatePresignedUrlForDownload(filePath);
    }

    public DeleteObjectResponse deleteFile(Long reservationId, String path) {
        String filePath = "reservations/" + reservationId + "/" + path;
        auditService.logAction(
                "USER_ID",
                "DELETE_FILE",
                filePath
        );
        return s3Service.deleteFile(filePath, null);
    }

    public FileUploadResponseDto replaceFile(Long reservationId, String path, MultipartFile file) {
        String filePath = "reservations/" + reservationId + "/" + path;

        if(s3Service.listObjectVersions(filePath).size() < 10) {
            auditService.logAction(
                    "USER_ID",
                    "TOO_MANY_FILES_UPLOADED",
                    filePath
            );
            throw new IllegalArgumentException("too many files uploaded please speak to admin");
        }
        return uploadFile(reservationId, path, file, true);
    }

    public FileUploadResponseDto uploadFile(Long reservationId, String path, MultipartFile file, boolean isReplacing) {

        if(file.isEmpty() || file.getSize() == 0 || file.getSize() > 5 * 1024 * 1024) { // 5 MB
            throw new IllegalArgumentException("Invalid file size");
        }

        // Save the file to S3
        String filePath = "reservations/" + reservationId + "/" + path;

        if (!isReplacing && s3Service.doesFileExist(filePath) ) {//if admin then can overwrite
            throw new IllegalArgumentException("File already exists");
        }

        String url = s3Service.uploadFile(file, filePath);

        // Log the action
        auditService.logAction(
                "USER_ID",
                "UPLOAD_FILE",
                filePath
        );
        // Return the response
        return new FileUploadResponseDto("success " + "user_id??" + " " + filePath);
    }

    public FileUploadResponseDto uploadFile(HttpSession session, String path, MultipartFile file)  {
        // Process the file
        if(file.isEmpty() || file.getSize() == 0 || file.getSize() > 5 * 1024 * 1024) { // 5 MB
            throw new IllegalArgumentException("Invalid file size");
        }
        // Save the file to S3
        String filePath = "temp/" + session.getId() + "/" + path;

        if(s3Service.listObjectVersions(filePath).size() < 10) {
            auditService.logAction(
                    "USER_ID",
                    "TOO_MANY_FILES_UPLOADED",
                    filePath
            );
            throw new IllegalArgumentException("too many files uploaded please speak to admin");
        }

        String url = s3Service.uploadFile(file, filePath);


        // Log the action
        auditService.logAction(
                session.getId(),
                "UPLOAD_FILE",
                filePath
        );


        // Return the response
        return new FileUploadResponseDto("success " + session.getId() + " " + path);
    }

    public ReservationAPIResponseDto updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(status);
        Reservation reservationUpdated = reservationRepository.save(reservation);
        logger.info("Reservation updated: " + reservationUpdated);

        // Log the action
        auditService.logAction(
                "USER_ID",
                "UPDATE_RESERVATION_STATUS",
                status.toString()
        );
        //need to email the guests
        emailService.sendStatusUpdateEmail(reservationUpdated, status);
        return mapToDto(reservationUpdated);
    }




    public ReservationAPIResponseDto saveReservation(HttpSession session, ApiRequestDto request) {

        Location locationId = locationService.getLocation(request.friendshipHouseLocation());

        //if want that the guest stay has to be less than a ceetain period of time
        //loop throught each guest and check that the total stay is less than GuestStayLimit
        //if not throw an exception
        if (request.guests().stream().anyMatch(guest -> guest.checkInDate().compareTo(guest.checkOutDate()) > 0)) {
            throw new IllegalArgumentException("Check out date must be after check in date");
        }

        Reservation reservation = mapToEntity(request,locationId);

        validateStayIsLessThenMaxAllowed(reservation);

        boolean uploadedAFile = false;
        boolean missingAFile = false;
        StringBuilder missingFiles = new StringBuilder();

        //check that files where uploaded
        boolean patientFileUploaded = checkIfFileUploaded(session, "patient");
        if (!patientFileUploaded) {
            logger.error("Patient file not uploaded");
            missingFiles.append("Patient file not uploaded /n");
            missingAFile = true;
          //  throw new IllegalArgumentException("Patient file not uploaded");
        }
        else {
            uploadedAFile = true;
        }
        for (int i = 0; i < reservation.getGuests().size(); i++) {
            boolean guestFileUploaded = checkIfFileUploaded(session, "guest" + i);
            if (!guestFileUploaded) {
                logger.error("Guest" + i +  " file not uploaded");
                missingFiles.append("Guest").append(i).append(" file not uploaded /n");
                missingAFile = true;
              //  throw new IllegalArgumentException("Guest" + i + " file not uploaded");
            }
            else {
                uploadedAFile = true;
            }
        }

        // if files are uploaded, save the reservation but first move the files to the correct location
        // then save the reservation
        Reservation reservationSaved =  reservationRepository.save(reservation);

        if (uploadedAFile) {
            moveFiles(session, reservationSaved);
        }
        if (missingAFile) {
            reservationSaved.setStatus(ReservationStatus.MISSING_FILES);
        }else {
            reservationSaved.setStatus(ReservationStatus.PENDING);
        }

        logger.info("Reservation saved: " + reservationSaved);

        Reservation reservationSavedPending =  reservationRepository.save(reservationSaved);
        logger.info("Reservation saved pending: " + reservationSavedPending);

        // Log the action
        auditService.logAction(
                session.getId(),
                "CREATED_RESERVATION",
                reservationSavedPending + " \nMissing files: "+ missingFiles.toString()
        );

        //need to email the guests
        emailService.sendReservationConfirmationEmail(reservationSavedPending);


        return mapToDto(reservationSavedPending );
    }

    public ReservationAPIResponseDto updateReservation(Long reservationId, @Valid ApiRequestDto request) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        Location locationId = locationService.getLocation(request.friendshipHouseLocation());
        Reservation reservationUpdated = mapToEntity(request, locationId);
        reservationUpdated.setId(reservation.getId());
        validateStayIsLessThenMaxAllowed(reservation);

        Reservation reservationSaved = reservationRepository.save(reservationUpdated);
        logger.info("Reservation updated: " + reservationSaved);

        // Log the action
        auditService.logAction(
                "USER_ID",
                "UPDATE_RESERVATION",
                reservationSaved.toString()
        );

        //need to email the guests
        emailService.sendReservationConfirmationEmail(reservationSaved);
        return mapToDto(reservationSaved);
    }

    public ReservationAPIResponseDto updateGuest(Long reservationId, Long guestId, @Valid GuestDtoUpdateDate guest) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        Guest guestToUpdate = reservation.getGuests().stream()
                .filter(g -> g.getId().equals(guestId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Guest not found"));


        try {
            guestToUpdate.setCheckOutDate(DATE_FORMAT.parse(guest.checkOutDate()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }
        validateStayIsLessThenMaxAllowed(reservation);


        Reservation reservationSaved = reservationRepository.save(reservation);
        logger.info("Guest updated: " + reservationSaved);

        // Log the action
        auditService.logAction(
                "USER_ID",
                "UPDATE_GUEST",
                reservationSaved.toString()
        );

        //need to email the guests
        emailService.sendReservationConfirmationEmail(reservationSaved);
        return mapToDto(reservationSaved);
    }

    private void validateStayIsLessThenMaxAllowed(Reservation reservation) {
        long maxDays = Long.parseLong(adminSettings.getSettingValue(AdminSettingKey.MAX_STAY_TIME_IN_DAYS).settingValue());
        if (reservation.getGuests().stream().anyMatch(guest -> {
            long daysStayed = ChronoUnit.DAYS.between(
                    guest.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    guest.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
            return  daysStayed > maxDays;
        })) {
            throw new IllegalArgumentException("Check out date must be after check in date and stay must be within the allowed time limit: " + maxDays + " days");
        }
    }

    private void moveFiles(HttpSession session, Reservation reservationSaved) {
        String path = "temp/" + session.getId() + "/";
        String newPath = "reservations/" + reservationSaved.getId() + "/";
        s3Service.moveFileWithinS3(path + "patient", newPath + "patient");
        for (int i = 0; i < reservationSaved.getGuests().size(); i++) {
            s3Service.moveFileWithinS3(path + "guest" + i, newPath + "guest" + i);
        }
    }

    private boolean checkIfFileUploaded(HttpSession session, String s) {
        String path = "temp/" + session.getId() + "/" + s;
        return  s3Service.doesFileExist(path);
    }

    public List<ReservationAPIResponseDto> getAllReservations() {
        return  reservationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ReservationAPIResponseDto getReservation(Long id) {
        return mapToDto( reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found")));
    }

    public List<ReservationAPIResponseDto> getFilteredReservations(ReservationStatus status, LocalDate startTime, LocalDate endTime) {
        // Apply default time logic if not provided
        if (startTime == null && endTime == null) {
            // If both startTime and endTime are null, set a default period (last 7 days)
            endTime = LocalDate.now();
            startTime = endTime.minusWeeks(1);  // Default to the last week
        } else if (startTime != null && endTime == null) {
            // If only startTime is provided, set endTime to the current date (present)
            endTime = LocalDate.now();
        } else if (startTime == null) {
            // If only endTime is provided, set startTime to one week before endTime
            startTime = endTime.minusWeeks(1);
        }

        // Convert LocalDate to LocalDateTime (start of day for startTime, end of day for endTime)
        LocalDateTime startDateTime = startTime.atStartOfDay();
        LocalDateTime endDateTime = endTime.atTime(23, 59, 59);

        // Now apply the filtering based on the status and time range
        if (status != null) {
            return reservationRepository.findByStatusAndCreatedAtBetween(status, startDateTime, endDateTime)
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        return reservationRepository.findByCreatedAtBetween(startDateTime, endDateTime)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private ReservationAPIResponseDto mapToDto(Reservation reservation) {
        ReservationAPIResponseDto response = new ReservationAPIResponseDto();
        response.setId(reservation.getId());
        response.setStatus(reservation.getStatus());
        response.setCreatedAt(reservation.getCreatedAt().toString());
        response.setLocation(reservation.getLocation().getName());
        response.setPatient(mapPatientToDto(reservation.getPatient()));
        response.setGuests(reservation.getGuests().stream()
                .map(this::mapGuestToDto)
                .collect(Collectors.toList()));
        return response;
    }



    // Assuming you have Reservation, PatientEntity, GuestEntity classes
    private Reservation mapToEntity(ApiRequestDto request, Location location) {
        Reservation reservation = new Reservation();

        // Mapping simple fields
        reservation.setLocation(location);

        // Mapping the PatientDto to PatientEntity
        Patient patient = mapPatientToEntity(request.patient());
        reservation.setPatient(patient);

        // Mapping the list of GuestDto to a list of GuestEntity
        List<Guest> guests = request.guests().stream()
                .map(this::mapGuestToEntity)  // Assuming you have a method for mapping individual guests
                .collect(Collectors.toList());
        reservation.setGuests(guests);

        return reservation;
    }


    // Method to map PatientDto to PatientEntity
    private Patient mapPatientToEntity(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setFirstName(patientDto.firstName());
        patient.setLastName(patientDto.lastName());
        patient.setFacility(patientDto.facility());
        patient.setPatientCondition(patientDto.condition());

        // Assuming VisitTypeDto maps to some VisitTypeEntity
//        VisitType visitType = mapVisitTypeToEntity(patientDto.visitType());
//        patient.setVisitType(visitType);
        patient.setType(patientDto.visitType());
        patient.setRoom(patientDto.roomNumber());

        return patient;
    }

    private PatientDto mapPatientToDto(Patient patient) {
        return new PatientDto(patient.getFirstName(), patient.getLastName(), patient.getFacility(), patient.getPatientCondition(), patient.getType(), patient.getRoom());
    }

    private GuestDto mapGuestToDto(Guest guest) {
        return new GuestDto(guest.getFirstName(), guest.getLastName(), guest.getRelationship(), guest.getGender(),
                guest.getCell(), guest.getEmail(), guest.getStreet(), guest.getHouseNumber(),
                guest.getCity(), guest.getState(), guest.getZip(),guest.getCountry(), guest.getCheckInDate().toString(), guest.getCheckOutDate().toString());
    }


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Method to map GuestDto to GuestEntity
    private Guest mapGuestToEntity(GuestDto guestDto) {


        Guest guestEntity = new Guest();
        guestEntity.setFirstName(guestDto.firstName());
        guestEntity.setLastName(guestDto.lastName());
        guestEntity.setRelationship(guestDto.relationship());
        guestEntity.setGender(guestDto.gender());
        guestEntity.setCell(guestDto.cell());
        guestEntity.setEmail(guestDto.email());
        guestEntity.setStreet(guestDto.street());
        guestEntity.setHouseNumber(guestDto.houseNumber());
        guestEntity.setCity(guestDto.city());
        guestEntity.setState(guestDto.state());
        guestEntity.setZip(guestDto.zip());

        try {
            guestEntity.setCheckInDate(DATE_FORMAT.parse(guestDto.checkInDate()));
            guestEntity.setCheckOutDate(DATE_FORMAT.parse(guestDto.checkOutDate()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        guestEntity.setCountry(guestDto.country());

        return guestEntity;
    }



    // Method to map VisitTypeDto to VisitTypeEntity (if needed)
//    private VisitType mapVisitTypeToEntity(VisitTypeDto visitTypeDto) {
//        VisitType visitType = new VisitType();
//        // Populate fields of visitTypeEntity based on visitTypeDto
//        return visitType;
//    }


}
