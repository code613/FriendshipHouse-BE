package com.levdevs.freindshipbe.config;

import com.levdevs.freindshipbe.DAO.ReservationRepository;
import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Entity.Reservation;
import com.levdevs.freindshipbe.Service.LocationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LocationService locationService;

    public DataInitializer(LocationService locationService) {
        this.locationService = locationService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (locationService.count() == 0) {
            locationService.save(new Location("House A"));
            locationService.save(new Location("House B"));
            locationService.save(new Location("House C"));
            locationService.save(new Location("Friendship House"));
            System.out.println("Sample locations added to the database.");
        }
    }
}

