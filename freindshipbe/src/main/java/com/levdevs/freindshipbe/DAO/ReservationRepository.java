package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.Reservation;
import com.levdevs.freindshipbe.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Custom query method with filters for status and createdAt (now LocalDateTime)
    List<Reservation> findByStatusAndCreatedAtBetween(ReservationStatus status, LocalDateTime startTime, LocalDateTime endTime);

    // Custom query method with optional createdAt filter
    List<Reservation> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}
