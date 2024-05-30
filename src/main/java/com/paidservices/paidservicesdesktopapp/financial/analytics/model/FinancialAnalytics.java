package com.paidservices.paidservicesdesktopapp.financial.analytics.model;

import com.paidservices.paidservicesdesktopapp.model.MedicalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FinancialAnalytics {
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<MedicalService, BigDecimal> medicalServiceSum = new HashMap<>();
    private BigDecimal totalSum;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Map<MedicalService, BigDecimal> getMedicalServiceSum() {
        return medicalServiceSum;
    }

    public void setMedicalServiceSum(Map<MedicalService, BigDecimal> medicalServiceSum) {
        this.medicalServiceSum = medicalServiceSum;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }
}
