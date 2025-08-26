package com.sinosoft.etp.cp.examination.service.impl;

import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.examination.domain.TExaminationStudentWithCp;
import com.sinosoft.etp.cp.examination.mapper.TExaminationStudentWithCpMapper;
import com.sinosoft.etp.cp.examination.service.ITExaminationStudentWithCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @type create
 * @auther cfl
 * 2021-12-31
 * 批量插入考试与学员关联关系业务实现层
 */
@Service
public class TExaminationStudentWithCpServiceImpl implements ITExaminationStudentWithCpService {

    @Resource
    private TExaminationStudentWithCpMapper tExaminationStudentMapper;

    @Override
    public Long insert(List<TExaminationStudentWithCp> sysUsers, Long ksid) {
        List<TExaminationStudentWithCp> list = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            TExaminationStudentWithCp student = new TExaminationStudentWithCp();
            student.setKsid(ksid);
            student.setXyid(sysUser.getUserId());
            student.setCreateBy(SecurityUtils.getUsername());
            student.setUpdateBy(SecurityUtils.getUsername());
            list.add(student);
        });
        return tExaminationStudentMapper.insert(list);
    }
}
