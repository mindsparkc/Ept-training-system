package com.sinosoft.etp.up.curriculum.service;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumContent;

/**
 * curriculumService接口
 *
 * 
 * @date 2021-11-25
 */
public interface ITCurriculumContentService {
    /**
     * 新增curriculum
     *
     * @param tCurriculumContent curriculum
     * @return 结果
     */
    public int insertTCurriculumContent(TCurriculumContent tCurriculumContent);
}
