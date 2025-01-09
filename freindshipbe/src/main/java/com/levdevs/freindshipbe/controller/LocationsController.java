package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.DTO.LocationDTO;
import com.levdevs.freindshipbe.DTO.LocationWithoutIdDTO;
import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
public class LocationsController {

    private final LocationService locationService;

    private static final Logger logger = LoggerFactory.getLogger(LocationsController.class);


    public LocationsController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Get all locations as Location objects
//    @GetMapping
//    public ResponseEntity<List<Location>> getAllLocations() {
//        List<Location> locations = locationService.getAllLocations();
//        return ResponseEntity.ok(locations);
//    }

    // Get all distinct location names as Strings
    //this needs to be fixed.. to add a sublist of all the sublocations
    @GetMapping
    public ResponseEntity<List<LocationWithoutIdDTO>> getAllLocationNames() {
        List<LocationWithoutIdDTO> locationNames = locationService.getAllLocationsNames();
        return ResponseEntity.ok(locationNames);
    }

    // Get a location by name
    @GetMapping("/{name}")
    public ResponseEntity<LocationWithoutIdDTO> getLocation(@PathVariable String name) {
        logger.info("Received request for location: {}", name);
//        try {
            LocationWithoutIdDTO location = locationService.getLocationByName(name);
            logger.info("Fetched location: {}", location);
            return ResponseEntity.ok(location); // Return 200 OK with the location details
//        } catch (IllegalArgumentException e) {
//            logger.error("Location not found for name: {}", name, e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
    }

    // Create a new location
//    @PostMapping
//    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
//        Location savedLocation = locationService.save(location);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
//    }
    @PostMapping
    public ResponseEntity<LocationWithoutIdDTO> createLocation(@RequestBody LocationWithoutIdDTO location) {
   //     Location newLocation = new Location(location);  // Convert the string to Location
//        Location savedLocation = locationService.save(newLocation);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
        System.out.println("location POST: " + location);
        logger.info("Received request to create a new {entity}: {}", location);
//        try {
            LocationWithoutIdDTO savedLocation = locationService.save(location);
            System.out.println("location POST: " + savedLocation);
            logger.info("Successfully created {entity}: {}", savedLocation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
//        } catch (IllegalArgumentException e) {
//            logger.error("Failed to create {entity}. Error: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(null); // You can return a custom error message here
//        }
    }


    // Update an existing location
    @PutMapping("/{name}")
    public ResponseEntity<LocationWithoutIdDTO> updateLocation(@PathVariable String name, @RequestBody LocationWithoutIdDTO updatedLocation) {
        System.out.println("name PUT: " + updatedLocation);
        logger.info("Received PUT request to {} with name: {}", name,updatedLocation);
        logger.debug("PUT New data for {entity}: {}", updatedLocation);
//        try {
            LocationWithoutIdDTO newlyUpdatedLocation = locationService.updateLocation(name, updatedLocation);
            logger.info("PUT Successfully updated : {}", newlyUpdatedLocation);
            return ResponseEntity.ok(newlyUpdatedLocation); // Return 200 OK with the updated location
//        } catch (IllegalArgumentException e) {
//            logger.error("Failed to update {entity} with name: {}. Error: {}", name, e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////        }
////        Optional<Location> existingLocation = Optional.ofNullable(locationService.getLocation(id));
////        if (existingLocation.isPresent()) {
////            updatedLocation.setId(id); // Ensure the correct ID is set for updating
////            Location savedLocation = locationService.save(updatedLocation);
////            return ResponseEntity.ok(savedLocation);
////        }
////        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String name) {
        logger.info("Received request to delete {entity} with name: {}", name);
//        try {
            locationService.deleteLocation(name); // Let the service handle the deletion
            logger.info("Successfully deleted {entity} with name: {}", name);
            return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
//        } catch (IllegalArgumentException e) {
//            // Handle the case where the location is not found
//            logger.error("Failed to delete {entity} with name: {}. Error: {}", name, e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
    }

}
