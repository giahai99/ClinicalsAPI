package com.bharath.clinicals.clinicalsapi.controllers;

import com.bharath.clinicals.clinicalsapi.dto.ClinicalDataRequest;
import com.bharath.clinicals.clinicalsapi.model.ClinicalData;
import com.bharath.clinicals.clinicalsapi.model.Patient;
import com.bharath.clinicals.clinicalsapi.repos.ClinicalDataRepository;
import com.bharath.clinicals.clinicalsapi.repos.PatientRepository;
import com.bharath.clinicals.clinicalsapi.util.BMICalculator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin
public class ClinicalDataController {
    private ClinicalDataRepository clinicalDataRepository;
    private PatientRepository patientRepository;

    ClinicalDataController(ClinicalDataRepository clinicalDataRepository, PatientRepository patientRepository) {
        this.clinicalDataRepository = clinicalDataRepository;
        this.patientRepository = patientRepository;
    }

    @RequestMapping(value = "/clinicals", method = RequestMethod.POST)
    public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request){
        Patient patient = patientRepository.findById(request.getPatientId()).get();
        ClinicalData clinicalData = new ClinicalData();
        clinicalData.setComponentName(request.getComponentName());
        clinicalData.setComponentValue(request.getComponentValue());
        clinicalData.setPatient(patient);
        return clinicalDataRepository.save(clinicalData);
    }

    @RequestMapping(value = "/clinicals/{patientId}/{componentName}", method = RequestMethod.GET)
    public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId, @PathVariable("componentName") String componentName) {
        if (componentName.equals("bmi")) {
            componentName = "hw";
        }
        List<ClinicalData> clinicalData = clinicalDataRepository.findByPatientIdAndComponentNameOrderByMeasuredDateTime(patientId, componentName);
        ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
        for(ClinicalData eachEntry:duplicateClinicalData) {
            BMICalculator.calculateBMI(clinicalData, eachEntry);
        }
        return clinicalData;
    }

}
