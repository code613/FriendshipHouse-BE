package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.SubLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubLocationRepository extends JpaRepository<SubLocation, Long> {
    Optional<SubLocation> findByName(String name);
}

