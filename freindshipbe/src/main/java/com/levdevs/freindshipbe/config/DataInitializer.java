package com.levdevs.freindshipbe.config;

import com.levdevs.freindshipbe.DAO.ReservationRepository;
import com.levdevs.freindshipbe.DTO.LocationWithoutIdDTO;
import com.levdevs.freindshipbe.DTO.SubLocationWithoutIdDTO;
import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Entity.Reservation;
import com.levdevs.freindshipbe.Service.LocationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LocationService locationService;

    public DataInitializer(LocationService locationService) {
        this.locationService = locationService;
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
    }
}

