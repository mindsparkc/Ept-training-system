package com.sinosoft.etp.cp.sign.service.impl;

import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCp;
import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCpExport;
import com.sinosoft.etp.cp.sign.mapper.TSignStudentWithCpMapper;
import com.sinosoft.etp.cp.sign.service.ITSignStudentWithCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TSignStudentWithCpServiceImpl implements ITSignStudentWithCpService {

    @Resource
    private TSignStudentWithCpMapper tSignStudentMapper;

    @Override
    public Long insert(List<TSignStudentWithCp> sysUsers, Long sid) {
        List<TSignStudentWithCp> list = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            TSignStudentWithCp student = new TSignStudentWithCp();
            student.setHdid(sid);
            student.setXyid(sysUser.getUserId());
            student.setCreateBy(SecurityUtils.getUsername());
            student.setUpdateBy(SecurityUtils.getUsername());
            student.setSfbm("0");
            list.add(student);
        });
        return tSignStudentMapper.insert(list);
    }

    @Override
    public List<TSignStudentWithCpExport> selectBysId(Long sid) {
        List<TSignStudentWithCp> list = tSignStudentMapper.selectBysId(sid);
        List<TSignStudentWithCpExport> lists = new ArrayList<>();
        TSignStudentWithCpExport t = new TSignStudentWithCpExport();
        for (TSignStudentWithCp tSignStudentWithCp: list) {
            t.setXyid(tSignStudentWithCp.getXyid());
            t.setXyName(tSignStudentWithCp.getUserName());
            t.setSfbm("1".equals(tSignStudentWithCp.getSfbm())?"已报名":"未报名");
            lists.add(t);
        }
        return lists;
    }
}
