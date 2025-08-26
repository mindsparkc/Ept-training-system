package com.sinosoft.etp.cp.analysis.service;

import java.util.List;

public interface PeopleAnalysisService {
    public List<Object> processingPersonnelAnalyzeData(Long[] ksids, Long[] xyids);
}
