package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {}
