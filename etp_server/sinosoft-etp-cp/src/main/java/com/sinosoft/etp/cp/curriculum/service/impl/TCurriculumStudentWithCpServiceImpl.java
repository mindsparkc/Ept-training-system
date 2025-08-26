package com.sinosoft.etp.cp.curriculum.service.impl;

import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumStudentWithCp;
import com.sinosoft.etp.cp.curriculum.mapper.TCurriculumStudentWithCpMapper;
import com.sinosoft.etp.cp.curriculum.service.ITCurriculumStudentWithCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TCurriculumStudentWithCpServiceImpl implements ITCurriculumStudentWithCpService {

    @Resource
    private TCurriculumStudentWithCpMapper tCurriculumStudentMapper;

    @Override
    public Long insert(List<TCurriculumStudentWithCp> sysUsers, Long kcid) {
        List<TCurriculumStudentWithCp> list = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            TCurriculumStudentWithCp student = new TCurriculumStudentWithCp();
            student.setKcid(kcid);
            student.setXyid(sysUser.getUserId());
            student.setCreateBy(SecurityUtils.getUsername());
            student.setUpdateBy(SecurityUtils.getUsername());
            // 如果存在用户就不插入，不存在就插入
            List<TCurriculumStudentWithCp> flag = tCurriculumStudentMapper.selectStudnetByKcIdAndXYid(student.getKcid(), student.getXyid());
            if (!(flag.size() > 0)){
                list.add(student);
            }
        });
        if (list.size()>0){
            return tCurriculumStudentMapper.insert(list);
        }
        return 0L;
    }
}
