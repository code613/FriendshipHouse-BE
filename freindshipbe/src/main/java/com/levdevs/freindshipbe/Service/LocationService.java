package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.SubLocationRepository;
import com.levdevs.freindshipbe.controller.LocationsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import com.levdevs.freindshipbe.DAO.LocationRepository;
import com.levdevs.freindshipbe.DTO.*;
import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Entity.SubLocation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final SubLocationRepository subLocationRepository;
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final AuditService auditService;



    public LocationService(LocationRepository locationRepository, SubLocationRepository subLocationRepository, AuditService auditService) {
        this.locationRepository = locationRepository;
        this.subLocationRepository = subLocationRepository;
        this.auditService = auditService;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

//    public List<String> getAllLocationsNames() {
//        return locationRepository.findAll()
//                .stream()
//                .map(Location::getName) // Extract the name field
//                .distinct()            // Remove duplicates
//                .collect(Collectors.toList()); // Convert to a list
//    }
//    public List<LocationWithoutIdDTO> getAllLocationsNames() {
//        return locationRepository.findAll()
//                .stream()
//                .map(location -> new LocationWithoutIdDTO(
//                        location.getName(),  // Map the name to the DTO
//                        location.getSubLocations()  // Extract sublocations and map to their names
//                                .stream()
//                                .map( l -> new SubLocationWithoutIdDTO(l.getName())) // Assuming SubLocation has a getName method
//                                .collect(Collectors.toList()) // Collect sublocation names into a list
//                ))
//                .collect(Collectors.toList()); // Collect into a list
//    }

    public List<LocationWithoutIdDTO> getAllLocationsNames() {
        // Step 1: Get all locations from the repository
        List<Location> locations = locationRepository.findAll();

        // Step 2: Create a list to hold the LocationWithoutIdDTOs
        List<LocationWithoutIdDTO> locationDTOs = new ArrayList<>();

        // Step 3: Loop through each location and map it to a DTO
        for (Location location : locations) {
            // Step 3.1: Map the sublocations to their DTOs
            List<SubLocationWithoutIdDTO> subLocationDTOs = new ArrayList<>();
            for (SubLocation subLocation : location.getSubLocations()) {
                SubLocationWithoutIdDTO subLocationDTO = new SubLocationWithoutIdDTO(subLocation.getName());
                subLocationDTOs.add(subLocationDTO);
            }

            // Step 3.2: Create the LocationWithoutIdDTO and add it to the list
            LocationWithoutIdDTO locationDTO = new LocationWithoutIdDTO(location.getName(), subLocationDTOs);
            locationDTOs.add(locationDTO);
        }

        // Step 4: Return the final list of DTOs
        return locationDTOs;
    }




//    public Location getLocation(String name) {
//        Optional<Location> locationOptional = locationRepository.findByName(name);
//        if (locationOptional.isPresent()) {
//            Location location = locationOptional.get();
//            return locationRepository.findById(location.getId()).orElse(null);
//        } else {
//            // Handle the case when the location is not found
//            System.out.printf("Location: %s not found!\n", name);
//            throw new IllegalArgumentException("Location not found: " + name);
//        }
//    }

    public long count() {
        return locationRepository.count();
    }

//    public LocationWithoutIdDTO save(LocationWithoutIdDTO location) {
//        // Check if a Location with the same name already exists
//        if (locationRepository.existsByName(location.name())) {
//            throw new IllegalArgumentException("Location with this name already exists");
//        }
//        Location temp = locationRepository.save(mapDTOToLocation(location));
//
//        return mapLocationToDTO(temp);
//    }

    @Transactional
    public LocationWithoutIdDTO save(LocationWithoutIdDTO location) {
        // Check if a Location with the same name already exists
        if (locationRepository.existsByName(location.name())) {
            throw new IllegalArgumentException("Location with this name already exists");
        }

        // Map Location DTO to entity
        Location locationEntity = new Location();
        locationEntity.setName(location.name());

        // Check for existing SubLocations
        List<SubLocation> subLocations = location.facility().stream()
                .map(subLocationDTO -> {
                    // Check if SubLocation already exists
                    Optional<SubLocation> existingSubLocation = subLocationRepository.findByName(subLocationDTO.name());
                    return existingSubLocation.orElseGet(() -> {
                        // If not found, create a new SubLocation
                        SubLocation newSubLocation = new SubLocation();
                        newSubLocation.setName(subLocationDTO.name());
                        return newSubLocation;
                    });
                })
                .collect(Collectors.toList());

        locationEntity.setSubLocations(subLocations);

        // Save the Location entity
        Location savedLocation = locationRepository.save(locationEntity);

        // Log the action
        auditService.logAction(
                "Session ID",  // Assuming you have a session ID
                "CREATED_LOCATION",
                "Location name: " + location.name()
        );

        // Map saved entity back to DTO
        return mapLocationToDTO(savedLocation);
    }

    @Transactional
    public LocationWithoutIdDTO updateLocation(String name, LocationWithoutIdDTO updatedLocation) {
        logger.info("Received request to update location: {}", name);
        // Find the existing location
        Location location = locationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + name));

        logger.info("Found location: {}", location);
        // Update the location name
        location.setName(updatedLocation.name());

        // Validate and process sublocations
        List<SubLocation> updatedSubLocations = updatedLocation.facility()
                .stream()
                .map(subLocationDTO -> {
                    if (subLocationDTO.name() == null || subLocationDTO.name().isEmpty()) {
                        throw new IllegalArgumentException("SubLocation name cannot be null or empty");
                    }

                    // Find existing sublocation by name, or create a new one if not found
                    Optional<SubLocation> existingSubLocation = subLocationRepository.findByName(subLocationDTO.name());
                    if (existingSubLocation.isPresent()) {
                        // If the sublocation already exists, return it
                        return existingSubLocation.get();
                    } else {
                        // If not found, create a new SubLocation and set its name
                        SubLocation newSubLocation = new SubLocation();
                        newSubLocation.setName(subLocationDTO.name());
                        // Save the new sublocation
                        return subLocationRepository.save(newSubLocation);
                    }
                })
                .collect(Collectors.toList());


        // Get IDs of updated sublocations
        Set<Long> updatedSubLocationIds = updatedSubLocations.stream()
                .map(SubLocation::getId)
                .collect(Collectors.toSet());



        logger.info("Updated sublocations: {}", updatedSubLocations);
        // Clear existing sublocations and add updated ones
        location.getSubLocations().clear();
        location.getSubLocations().addAll(updatedSubLocations);

        deleteUnusedSubLocations(location, updatedSubLocationIds);

        logger.info("Updated location: {}", location);
        // Save the updated location
        Location temp = locationRepository.save(location);
        logger.info("Saved location: {}", temp);

        // Log the action
        auditService.logAction(
                "Session ID",  // Assuming you have a session ID
                "UPDATED_LOCATION",
                "Location name: " + name
        );



        // Map and return the updated DTO
        return mapLocationToDTO(temp);
    }

    private void deleteUnusedSubLocations(Location location, Set<Long> updatedSubLocationIds) {
        // Get IDs of existing sublocations
        Set<Long> existingSubLocationIds = location.getSubLocations().stream()
                .map(SubLocation::getId)
                .collect(Collectors.toSet());

        // Find sublocations that are no longer associated with this location
        Set<Long> sublocationsToDelete = new HashSet<>(existingSubLocationIds);
        sublocationsToDelete.removeAll(updatedSubLocationIds);

        logger.info("Sublocations to delete: {}", sublocationsToDelete);
        List<SubLocation> subLocations = location.getSubLocations();
        logger.info("Sublocations before deletion: {}", subLocations);

        // Delete sublocations that are not in the updated list and are not used by other locations
        sublocationsToDelete.forEach(subLocationId -> {
            SubLocation subLocation = subLocationRepository.findById(subLocationId)
                    .orElseThrow(() -> new IllegalArgumentException("SubLocation not found for deletion"));

            if (!isSubLocationUsedByOtherLocation(subLocation)) {
                logger.info("Deleting sublocation: {}", subLocation);
                subLocationRepository.delete(subLocation);
            }
        });

        List<SubLocation> subLocationsAfter = location.getSubLocations();
        logger.info("Sublocations after deletion: {}", subLocationsAfter);
    }

    // Helper method to check if a sublocation is used by other locations
    private boolean isSubLocationUsedByOtherLocation(SubLocation subLocation) {
        // Check if there are any other locations using this sublocation
        return locationRepository.existsBySubLocations(subLocation);  // Assuming such a method exists in your repository
    }



//    @Transactional
//    public LocationWithoutIdDTO updateLocation(String name, LocationWithoutIdDTO updatedLocation) {
//        // Find the existing location
//        Location location = locationRepository.findByName(name)
//                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + name));
//
//        // Update the location name
//        location.setName(updatedLocation.name());
//
//        // Update sublocations
//        List<SubLocation> updatedSubLocations = updatedLocation.subLocations()
//                .stream()
//                .map(subLocationDTO -> subLocationRepository.findByName(subLocationDTO.name())
//                        .orElseGet(() -> {
//                            // Create a new SubLocation if it doesn't exist
//                            SubLocation newSubLocation = new SubLocation();
//                            newSubLocation.setName(subLocationDTO.name());
//                            return newSubLocation;
//                        }))
//                .collect(Collectors.toList());
//
//        // Clear existing sublocations and add updated ones
//        location.getSubLocations().clear();
//        location.getSubLocations().addAll(updatedSubLocations);
//
//        // Save the updated location
//        locationRepository.save(location);
//
//        // Map and return the updated DTO
//        return mapLocationToDTO(location);
//    }


//    public LocationWithoutIdDTO updateLocation(String name, LocationWithoutIdDTO updatedLocation) {
//        Optional<Location> locationOptional = locationRepository.findByName(name);
//        if (locationOptional.isPresent()) {
//            Location location = locationOptional.get();
//            location.setName(updatedLocation.name());
//            List<SubLocation> subLocations = updatedLocation.subLocations()
//                    .stream()
//                    .map(subLocationDTO -> {
//                        SubLocation subLocation = new SubLocation();
//                        subLocation.setName(subLocationDTO.name());
//                        return subLocation;
//                    })
//                    .collect(Collectors.toList());
//            location.setSubLocations(subLocations);
//            locationRepository.save(location);
//            return updatedLocation;
//        } else {
//            // Handle the case when the location is not found
//            System.out.printf("Location: %s not found!\n", name);
//            throw new IllegalArgumentException("Location not found: " + name);
//        }
//    }

    @Transactional
    public void deleteLocation(String name) {
        Optional<Location> locationOptional = locationRepository.findByName(name);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            locationRepository.deleteById(location.getId());
            // Log the action
            auditService.logAction(
                    "Session ID",  // Assuming you have a session ID
                    "DELETE_LOCATION",
                    "Location name: " + name
            );
          //  deleteUnusedSubLocations(location, Collections.emptySet());
        } else {
            // Handle the case when the location is not found
            System.out.printf("Location: %s not found!\n", name);
            throw new IllegalArgumentException("Location not found: " + name);
        }
    }

    public Location getLocation(String name) {
        Optional<Location> locationOptional = locationRepository.findByName(name);
        if (locationOptional.isPresent()) {
            return locationOptional.get();
        } else {
            // Handle the case when the location is not found
            System.out.printf("Location: %s not found!\n", name);
            throw new IllegalArgumentException("Location not found: " + name);
        }
    }


    public LocationWithoutIdDTO getLocationByName(String name) {
        var locationOptional = locationRepository.findByName(name);
        LocationWithoutIdDTO locationDTO;
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            List<SubLocationWithoutIdDTO> subLocationDTOs = new ArrayList<>();
            for (SubLocation subLocation : location.getSubLocations()) {
                SubLocationWithoutIdDTO subLocationDTO = new SubLocationWithoutIdDTO(subLocation.getName());
                subLocationDTOs.add(subLocationDTO);
            }
            locationDTO = new LocationWithoutIdDTO(location.getName(), subLocationDTOs);
        } else {
            // Handle the case where the location was not found
            System.out.printf("Location : %s not found!\n",name);
            throw new IllegalArgumentException("Location not found: " + name);
        }
        return locationDTO;

//                .map(Location::getId) // Extract the ID if the location exists
//                .orElse(0L); // Return 0 if no location is found
    }

    private LocationWithoutIdDTO mapLocationToDTO(Location location) {
        List<SubLocationWithoutIdDTO> subLocationDTOs = location.getSubLocations()
                .stream()
                .map(subLocation -> new SubLocationWithoutIdDTO(subLocation.getName()))
                .collect(Collectors.toList());
        return new LocationWithoutIdDTO(location.getName(), subLocationDTOs);
    }

    private Location mapDTOToLocation(LocationWithoutIdDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.name());
        List<SubLocation> subLocations = locationDTO.facility()
                .stream()
                .map(subLocationDTO -> {
                    SubLocation subLocation = new SubLocation();
                    subLocation.setName(subLocationDTO.name());
                    return subLocation;
                })
                .collect(Collectors.toList());
        location.setSubLocations(subLocations);
        return location;
    }

}
