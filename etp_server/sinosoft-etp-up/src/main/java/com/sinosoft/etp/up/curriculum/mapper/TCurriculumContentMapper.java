package com.sinosoft.etp.up.curriculum.mapper;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * curriculumMapper接口
 *
 * 
 * @date 2021-11-25
 */
public interface TCurriculumContentMapper {

    /**
     * 新增curriculum
     *
     * @param tCurriculumContent curriculum
     * @return 结果
     */
    public int insertTCurriculumContent(TCurriculumContent tCurriculumContent);

    /**
     * 根据课程ID获取课程内容
     * @param id 课程ID
     * @return
     */
    List<TCurriculumContent> selectTurriculumContentByCid(@Param("id") Long id,@Param("xyid") Long xyid);

}
