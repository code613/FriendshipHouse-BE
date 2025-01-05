package com.levdevs.freindshipbe.DAO;


import com.levdevs.freindshipbe.Entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAll();

    // If you want to fetch distinct locations (optional)
    List<Location> findDistinctByName(String name);
    boolean existsByName(String name); // Custom query to check for duplicate names
}
