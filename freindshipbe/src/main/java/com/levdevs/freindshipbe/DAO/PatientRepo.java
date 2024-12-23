package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<Patient,Integer> {
}
