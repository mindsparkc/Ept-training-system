package com.sinosoft.etp.cp.questionnaire.mapper;

import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireStudentWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TQuestionnaireStudentWithCpMapper {

    /**
     * 插入调查问卷与学员关联关系
     * @param sysUsers 学员
     */
    Long insert(@Param("list") List<TQuestionnaireStudentWithCp> sysUsers);

    /**
     * 根据调查问卷ID查询用户
     * @param sid 调查问卷ID
     * @return  List<TQuestionnaireStudent>
     */
    List<TQuestionnaireStudentWithCp> selectBysId(@Param("sid") Long sid);


    /**
     * 根据调查问卷ID删除活动与学员记录表记录
     * @param sid 调查问卷ID
     */
    void delRealtionBysId(@Param("sid") Long sid);

    Integer getTotalByWjid(@Param("wjid") Long wjid);


}
