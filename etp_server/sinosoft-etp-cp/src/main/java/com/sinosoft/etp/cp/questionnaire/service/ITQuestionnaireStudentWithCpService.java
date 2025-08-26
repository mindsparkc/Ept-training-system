package com.sinosoft.etp.cp.questionnaire.service;

import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireStudentWithCp;

import java.util.List;

public interface ITQuestionnaireStudentWithCpService {

    /**
     * 批量插入调查问卷与学员记录表记录
     * @param sysUsers  学员
     * @param sid      调查问卷id
     * @return Long
     */
    Long insert(List<TQuestionnaireStudentWithCp> sysUsers, Long sid);

}
