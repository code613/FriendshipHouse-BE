package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {}
