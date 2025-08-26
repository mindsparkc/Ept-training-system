package com.sinosoft.etp.cp.task.service.impl;

import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.task.domain.TTaskStudentWithCp;
import com.sinosoft.etp.cp.task.mapper.TTaskStudentWithCpMapper;
import com.sinosoft.etp.cp.task.service.ITaskStudentWithCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TTaskStudentWithCpServiceImpl implements ITaskStudentWithCpService {

    @Resource
    private TTaskStudentWithCpMapper tTaskStudentMapper;

    @Override
    public Long insert(List<TTaskStudentWithCp> sysUsers, Long rwid) {
        List<TTaskStudentWithCp> list = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            TTaskStudentWithCp student = new TTaskStudentWithCp();
            student.setRwid(rwid);
            student.setXyid(sysUser.getUserId());
            student.setCreateBy(SecurityUtils.getUsername());
            student.setUpdateBy(SecurityUtils.getUsername());
            List<TTaskStudentWithCp> flag = tTaskStudentMapper.selectStudentByrwIdAndXyId(student.getRwid(),student.getXyid());
            if (!(flag.size()>0)){
                list.add(student);
            }
        });
        if (list.size() > 0){
            return tTaskStudentMapper.insert(list);
        }
        return 0L;
    }
}
