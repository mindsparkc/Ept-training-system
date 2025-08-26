package com.sinosoft.etp.cp.curriculum.service;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumContentWithCp;

/**
 * curriculumService接口
 *
 *
 * @date 2021-11-25
 */
public interface ITCurriculumContentWithCpService {
    /**
     * 新增curriculum
     *
     * @param tCurriculumContent curriculum
     * @return 结果
     */
    public int insertTCurriculumContent(TCurriculumContentWithCp tCurriculumContent);
}
