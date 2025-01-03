package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.LocationRepository;
import com.levdevs.freindshipbe.Entity.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocation(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public long count() {
        return locationRepository.count();
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }
}
