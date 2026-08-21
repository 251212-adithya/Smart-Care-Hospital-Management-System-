package com.smartcare.hms.service.report;

import java.util.Map;

/** ABSTRACTION: contract for anything that can produce a hospital report. */
public interface ReportService {
    Map<String, Object> generateReport();
    String getReportName();
}
