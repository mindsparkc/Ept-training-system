package com.sinosoft.etp.cp.examination.task;

import com.sinosoft.etp.cp.examination.mapper.TExaminationWithCpMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 86151
 */
@Component("examinationTask")
public class ExaminationTask {

    @Resource
    private TExaminationWithCpMapper tExaminationWithCpMapper;

    public void updateExamStatus(){
        int i = tExaminationWithCpMapper.updateExamStatus();
        System.out.println("本次强制提交"+i+"条试卷");
    }
}
