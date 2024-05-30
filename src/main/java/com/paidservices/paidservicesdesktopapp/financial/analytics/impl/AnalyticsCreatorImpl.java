package com.paidservices.paidservicesdesktopapp.financial.analytics.impl;

import com.paidservices.paidservicesdesktopapp.financial.analytics.AnalyticsCreator;
import com.paidservices.paidservicesdesktopapp.financial.analytics.model.FinancialAnalytics;
import com.paidservices.paidservicesdesktopapp.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.model.Visitation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class AnalyticsCreatorImpl implements AnalyticsCreator {
    @Override
    public FinancialAnalytics createAnalytics(List<Visitation> analyticsData) {
        FinancialAnalytics analytics = new FinancialAnalytics();

        analytics.setStartDate(calculateStartDate(analyticsData));
        analytics.setEndDate(calculateEndDate(analyticsData));
        analytics.setMedicalServiceSum(calculateServiceSum(analyticsData));
        analytics.setTotalSum(calculateTotalSum(analyticsData));

        return analytics;
    }

    private LocalDate calculateStartDate(List<Visitation> analyticsData) {
        Optional<LocalDate> startDateOptional = analyticsData.stream()
                .map(visitation -> visitation.getDateTime().toLocalDate())
                .min(Comparator.naturalOrder());

        return startDateOptional.orElseThrow(() -> {
            return new RuntimeException("Ошибка! Не удалось найти начальную дату для анализа");
        });
    }

    private LocalDate calculateEndDate(List<Visitation> analyticsData) {
        Optional<LocalDate> endDateOptional = analyticsData.stream()
                .map(visitation -> visitation.getDateTime().toLocalDate())
                .max(Comparator.naturalOrder());

        return endDateOptional.orElseThrow(() -> {
            return new RuntimeException("Ошибка! Не удалось найти конечную дату для анализа");
        });
    }

    private Map<MedicalService, BigDecimal> calculateServiceSum(List<Visitation> analyticsData) {
        Map<MedicalService, BigDecimal> serviceSumMap = new HashMap<>();

        analyticsData.forEach(visitation -> {
            MedicalService service = visitation.getMedicalService();

            if (serviceSumMap.containsKey(service)) {
                BigDecimal currentServiceSum = serviceSumMap.get(service);
                serviceSumMap.put(service, currentServiceSum.add(service.getPrice()));
            } else {
                serviceSumMap.put(service, service.getPrice());
            }
        });

        return serviceSumMap;
    }

    private BigDecimal calculateTotalSum(List<Visitation> analyticsData) {
        return analyticsData.stream()
                .map(visitation -> visitation.getMedicalService().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
