package com.levdevs.freindshipbe.config;

import com.levdevs.freindshipbe.DAO.AdminSettingsRepository;
import com.levdevs.freindshipbe.DTO.LocationWithoutIdDTO;
import com.levdevs.freindshipbe.DTO.SubLocationWithoutIdDTO;
import com.levdevs.freindshipbe.Entity.AdminSettings;
import com.levdevs.freindshipbe.Service.LocationService;
import com.levdevs.freindshipbe.controller.AdminSettingsController;
import com.levdevs.freindshipbe.enums.AdminSettingKey;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LocationService locationService;
    private final AdminSettingsController adminSettingsService;
    private final AdminSettingsRepository adminSettingsRepository;

    public DataInitializer(LocationService locationService, AdminSettingsController adminSettingsService, AdminSettingsRepository adminSettingsRepository) {
        this.locationService = locationService;
        this.adminSettingsService = adminSettingsService;
        this.adminSettingsRepository = adminSettingsRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (locationService.count() == 0) {
            locationService.save(new LocationWithoutIdDTO("House A", new ArrayList<SubLocationWithoutIdDTO>()));
            locationService.save(new LocationWithoutIdDTO("House B", new ArrayList<SubLocationWithoutIdDTO>()));
            locationService.save(new LocationWithoutIdDTO("House C", new ArrayList<SubLocationWithoutIdDTO>()));
            locationService.save(new LocationWithoutIdDTO("Friendship House", new ArrayList<SubLocationWithoutIdDTO>()));
            System.out.println("Sample locations added to the database.");
        }

        // Check if settings are already initialized
        if (!adminSettingsRepository.existsBySettingKey(AdminSettingKey.MAX_STAY_TIME_IN_DAYS)) {
            // Initialize default settings
            adminSettingsRepository.save(new AdminSettings(AdminSettingKey.MAX_STAY_TIME_IN_DAYS, "30"));
            System.out.println("Default maxStayTimeInDays initialized to 30 days.");

            System.out.println("Admin settings initialized.");
        } else {
            System.out.println("Admin settings already initialized.");
        }

//        // Initialize the data for GuestStayTimeLimit
//        if (guestStayTimeLimitService.getGuestStayTimeLimit() == null) {
//            guestStayTimeLimitService.saveOrUpdateGuestStayTimeLimit(new GuestStayTimeLimit(30));  // Example: max stay time 30 days
//            System.out.println("Guest stay time limit initialized.");
//        }
    }
}

