package com.sinosoft.etp.cp.questionnaire.service.impl;

import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireStudentWithCp;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireStudentWithCpMapper;
import com.sinosoft.etp.cp.questionnaire.service.ITQuestionnaireStudentWithCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TQuestionnaireStudentWithCpServiceImpl implements ITQuestionnaireStudentWithCpService {

    @Resource
    private TQuestionnaireStudentWithCpMapper tQuestionnaireStudentMapper;

    @Override
    public Long insert(List<TQuestionnaireStudentWithCp> sysUsers, Long sid) {
        List<TQuestionnaireStudentWithCp> list = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            TQuestionnaireStudentWithCp student = new TQuestionnaireStudentWithCp();
            student.setWjid(sid);
            student.setXyid(sysUser.getUserId());
            student.setCreateBy(SecurityUtils.getUsername());
            student.setUpdateBy(SecurityUtils.getUsername());
            list.add(student);
        });
        return tQuestionnaireStudentMapper.insert(list);
    }
}
