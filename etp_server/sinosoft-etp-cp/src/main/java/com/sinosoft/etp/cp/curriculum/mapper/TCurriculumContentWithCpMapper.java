package com.sinosoft.etp.cp.curriculum.mapper;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumContentWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * curriculumMapper接口
 *
 *
 * @date 2021-11-25
 */
public interface TCurriculumContentWithCpMapper {

    /**
     * 新增curriculum
     *
     * @param tCurriculumContent curriculum
     * @return 结果
     */
    public int insertTCurriculumContent(TCurriculumContentWithCp tCurriculumContent);

    /**
     * 根据课程ID获取课程内容
     * @param id 课程ID
     * @return
     */
    List<TCurriculumContentWithCp> selectTurriculumContentByCid(@Param("id") Long id);

    /**
     * 根据课程ID删除与课程相关的内容
     * @param id
     */
    void deleteTCurriculumContentByCid(Long id);
}
