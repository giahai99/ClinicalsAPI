package com.bharath.clinicals.clinicalsapi.repos;

import com.bharath.clinicals.clinicalsapi.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
