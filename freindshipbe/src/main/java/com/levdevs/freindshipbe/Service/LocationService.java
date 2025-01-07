package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.LocationRepository;
import com.levdevs.freindshipbe.Entity.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<String> getAllLocationsNames() {
        return locationRepository.findAll()
                .stream()
                .map(Location::getName) // Extract the name field
                .distinct()            // Remove duplicates
                .collect(Collectors.toList()); // Convert to a list
    }


    public Location getLocation(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public long count() {
        return locationRepository.count();
    }

    public Location save(Location location) {
        // Check if a Location with the same name already exists
        if (locationRepository.existsByName(location.getName())) {
            throw new IllegalArgumentException("Location with this name already exists");
        }
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public Location getLocationIdByName(String name) {
        var locationOptional = locationRepository.findByName(name);
        Location location = new Location();
        if (locationOptional.isPresent()) {
            location = locationOptional.get(); // Safe to get the value now
            // Use the location
        } else {
            // Handle the case where the location was not found
            System.out.println("Location not found!");
        }
        return location;

//                .map(Location::getId) // Extract the ID if the location exists
//                .orElse(0L); // Return 0 if no location is found
    }

}
