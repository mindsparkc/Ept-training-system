package com.sinosoft.etp.up.curriculum.service.impl;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumContent;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumContentMapper;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * curriculumService业务层处理
 *
 * 
 * @date 2021-11-25
 */
@Service
public class TCurriculumContentServiceImpl  implements ITCurriculumContentService {

    @Autowired
    private TCurriculumContentMapper tCurriculumContentMapper;

    /**
     * 新增curriculum
     * @param tCurriculumContent curriculum
     * @return 结果
     */
    @Override
    public int insertTCurriculumContent(TCurriculumContent tCurriculumContent)
    {
        return tCurriculumContentMapper.insertTCurriculumContent(tCurriculumContent);
    }
}
