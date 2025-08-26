package com.sinosoft.etp.cp.analysis.mapper;

import com.sinosoft.etp.cp.analysis.domain.PeopleAndExaminationWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员分析
 */
public interface PeopleAnalysisWithCpMapper {
    public List<PeopleAndExaminationWithCp> selectPeopleAndExaminationResultByKsid(@Param(value = "ksid") Long ksid, @Param(value = "xyid") Long xyid);
}
