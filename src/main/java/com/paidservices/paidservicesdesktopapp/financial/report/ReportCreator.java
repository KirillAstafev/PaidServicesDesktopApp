package com.paidservices.paidservicesdesktopapp.financial.report;

import com.paidservices.paidservicesdesktopapp.financial.analytics.model.FinancialAnalytics;

public interface ReportCreator {
    void createReport(FinancialAnalytics reportData, String absoluteFilePath);
}
