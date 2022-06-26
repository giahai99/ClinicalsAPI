package com.bharath.clinicals.clinicalsapi.controllers;

import com.bharath.clinicals.clinicalsapi.model.ClinicalData;
import com.bharath.clinicals.clinicalsapi.model.Patient;
import com.bharath.clinicals.clinicalsapi.repos.PatientRepository;
import com.bharath.clinicals.clinicalsapi.util.BMICalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

    private PatientRepository repository;
    Map<String,String> filters = new HashMap<>();

    @Autowired
    PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    public List<Patient> getPatients() {
        return repository.findAll();
    }

    @RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
    public Patient getPatient(@PathVariable("id") int id) {
        return repository.findById(id).get();
    }

    @RequestMapping(value = "/patients", method = RequestMethod.POST)
    public Patient savePatient(@RequestBody Patient patient) {
        return repository.save(patient);
    }

    @RequestMapping(value = "/patients/analyze/{id}", method = RequestMethod.GET)
    public Patient anylize(@PathVariable("id") int id) {
        Patient patient = repository.findById(id).get();
        List<ClinicalData> clinicalData = patient.getClinicalData();
        ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
        for(ClinicalData eachEntry:duplicateClinicalData) {
            if (filters.containsKey(eachEntry.getComponentName())) {
                clinicalData.remove(eachEntry);
                continue;
            } else {
                filters.put(eachEntry.getComponentName(), null);
            }

            BMICalculator.calculateBMI(clinicalData, eachEntry);
        }
        filters.clear();
        return patient;
    }



}
