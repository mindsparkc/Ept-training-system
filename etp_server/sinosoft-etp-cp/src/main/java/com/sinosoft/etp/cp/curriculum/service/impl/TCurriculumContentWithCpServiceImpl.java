package com.sinosoft.etp.cp.curriculum.service.impl;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumContentWithCp;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumContentWithCpMapper;
import com.sinosoft.etp.cp.curriculum.service.ITCurriculumContentWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * curriculumService业务层处理
 *
 *
 * @date 2021-11-25
 */
@Service
public class TCurriculumContentWithCpServiceImpl implements ITCurriculumContentWithCpService {

    @Autowired
    private TCurriculumContentWithCpMapper tCurriculumContentMapper;

    /**
     * 新增curriculum
     * @param tCurriculumContent curriculum
     * @return 结果
     */
    @Override
    public int insertTCurriculumContent(TCurriculumContentWithCp tCurriculumContent)
    {
        return tCurriculumContentMapper.insertTCurriculumContent(tCurriculumContent);
    }
}
