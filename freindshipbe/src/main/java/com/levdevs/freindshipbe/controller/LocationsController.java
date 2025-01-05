package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Service.LocationService;
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

    public LocationsController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Get all locations as Location objects
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    // Get all distinct location names as Strings
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllLocationNames() {
        List<String> locationNames = locationService.getAllLocationsNames();
        return ResponseEntity.ok(locationNames);
    }

    // Get a location by ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocation(id);
        if (location != null) {
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Create a new location
//    @PostMapping
//    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
//        Location savedLocation = locationService.save(location);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
//    }
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody String location) {
        Location newLocation = new Location(location);  // Convert the string to Location
//        Location savedLocation = locationService.save(newLocation);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
        try {
            Location savedLocation = locationService.save(newLocation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // You can return a custom error message here
        }
    }


    // Update an existing location
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location updatedLocation) {
        Optional<Location> existingLocation = Optional.ofNullable(locationService.getLocation(id));
        if (existingLocation.isPresent()) {
            updatedLocation.setId(id); // Ensure the correct ID is set for updating
            Location savedLocation = locationService.save(updatedLocation);
            return ResponseEntity.ok(savedLocation);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Delete a location by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        Optional<Location> existingLocation = Optional.ofNullable(locationService.getLocation(id));
        if (existingLocation.isPresent()) {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
