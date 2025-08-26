package com.sinosoft.etp.cp.curriculum.service.impl;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumCompanyWithCp;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumCompanyWithCpMapper;
import com.sinosoft.etp.cp.curriculum.service.ITCurriculumCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【课程与企业关联】Service业务层处理
 *
 *
 * @date 2021-11-22
 */
@Service
public class TCurriculumCompanyWithCpServiceImpl implements ITCurriculumCompanyWithCpService {

    @Autowired
    private TCurriculumCompanyWithCpMapper tCurriculumCompanyMapper;

    /**
     * 新增【请填写功能名称】
     *
     * @param tCurriculumCompany 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTCurriculumCompany(TCurriculumCompanyWithCp tCurriculumCompany)
    {
        return tCurriculumCompanyMapper.insertTCurriculumCompany(tCurriculumCompany);
    }
}
