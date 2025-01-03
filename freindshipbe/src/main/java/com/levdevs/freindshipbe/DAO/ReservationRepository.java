package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
