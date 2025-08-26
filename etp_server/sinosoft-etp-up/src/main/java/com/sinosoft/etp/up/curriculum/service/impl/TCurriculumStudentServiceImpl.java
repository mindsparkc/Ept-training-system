package com.sinosoft.etp.up.curriculum.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumStudent;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumStudentMapper;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TCurriculumStudentServiceImpl implements ITCurriculumStudentService {

    @Resource
    private TCurriculumStudentMapper tCurriculumStudentMapper;

    /**
     * 新增【请填写功能名称】
     *
     * @param tCurriculumStudent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTCurriculumStudent(TCurriculumStudent tCurriculumStudent)
    {
        tCurriculumStudent.setCreateTime(DateUtils.getNowDate());
        tCurriculumStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());

        // TODO: 先检查是否已领取过课程
        int curriculumById = tCurriculumStudentMapper.countCurriculumById(tCurriculumStudent.getXyid(), tCurriculumStudent.getKcid());
        if (curriculumById > 0)
            return 1;

        return tCurriculumStudentMapper.insertTCurriculumStudent(tCurriculumStudent);
    }

    @Override
    public int countJoinCurriculumById(Long id) {
        return tCurriculumStudentMapper.countJoinCurriculumById(id);
    }

    /**
     * 获取包含cid的任务
     * @param cid
     * @return
     */
    @Override
    public List<Long> getTaskContainCurriculum(Long cid){
        return tCurriculumStudentMapper.getTaskContainCurriculum(SecurityUtils.getLoginUser().getUser().getUserId(),cid);
    }

}
