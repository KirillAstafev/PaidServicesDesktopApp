package com.paidservices.paidservicesdesktopapp.financial.analytics;

import com.paidservices.paidservicesdesktopapp.financial.analytics.model.FinancialAnalytics;
import com.paidservices.paidservicesdesktopapp.model.Visitation;

import java.util.List;

public interface AnalyticsCreator {
    FinancialAnalytics createAnalytics(List<Visitation> analyticsData);
}
