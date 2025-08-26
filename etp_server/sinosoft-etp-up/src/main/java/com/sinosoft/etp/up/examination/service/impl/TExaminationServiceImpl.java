package com.sinosoft.etp.up.examination.service.impl;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.examination.domain.TExamination;
import com.sinosoft.etp.up.examination.mapper.TExaminationMapper;
import com.sinosoft.etp.up.examination.service.ITExaminationService;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperStudentMapper;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperSubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 考试Service业务层处理
 *
 *
 * @date 2021-12-24
 */
@Service
public class TExaminationServiceImpl implements ITExaminationService
{
    @Autowired
    private TExaminationMapper tExaminationMapper;
    @Autowired
    private TTestpaperSubjectMapper tTestpaperSubjectMapper;
    @Resource
    private TTestpaperStudentMapper tTestpaperStudentMapper;

    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    @Override
    public TExamination selectTExaminationById(Long id)
    {
            //Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();
            TExamination tExamination = tExaminationMapper.selectTExaminationById(id);
            // 获取试卷总分数
            Long sjid = Long.valueOf(tExamination.getSjid().toString());

            Long ksid = id;
            Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
            //获取用户考试次数
            List<TTestpaperStudent> tTestpaperStudent = tTestpaperStudentMapper.selectTTestpaperStudentInfoByIds(ksid,sjid,xyid);
            Integer num = tTestpaperStudent.size();
            Double totalScore = tTestpaperSubjectMapper.selectScore(sjid);
            //tExamination.put("userName",SecurityUtils.getUsername());
            //tExamination.put("totalScore",totalScore);
            //tExamination.put("yhdtcs",num);
            tExamination.setUserName(SecurityUtils.getUsername());
            tExamination.setTotalScore(totalScore);
            tExamination.setYhdtcs(num.toString());
        return tExamination;
    }

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TExamination> selectTExaminationList(TExamination tExamination)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        tExamination.setXyid(xyid);
        //tExamination.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        List<TExamination> tExaminations = tExaminationMapper.selectTExaminationList(tExamination);
        for (int i=0;i<tExaminations.size();i++) {
            Long ksid = tExaminations.get(i).getId();
            Long sjid = tExaminations.get(i).getSjid();
            //可优化为count查询
            List<TTestpaperStudent> tTestpaperStudent = tTestpaperStudentMapper.selectTTestpaperStudentInfoByIds(ksid,sjid,xyid);
            Integer size = tTestpaperStudent.size();
            tExaminations.get(i).setYhdtcs(size.toString());
            TTestpaperStudent testpaperStudents = tTestpaperStudentMapper.selectIsSubmit(ksid);
            if(testpaperStudents!=null){
                if(testpaperStudents.getHandTime()==null||"".equals(testpaperStudents.getHandTime())){
                    tExaminations.get(i).setIsSubmit("0");
                }else{
                    tExaminations.get(i).setIsSubmit("1");
                }
            }
        }
        return tExaminations;
    }

    @Override
    public Integer getExaminationNum(Long xyid) {
        return tExaminationMapper.getExaminationNum(xyid);
    }

    @Override
    public TExamination getTExaminationById(Long id) {
        TExamination tExamination = tExaminationMapper.selectTExaminationByKSID(id);
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        Long ksid = tExamination.getId();
        Long sjid = tExamination.getSjid();

        List<TTestpaperStudent> tTestpaperStudent = tTestpaperStudentMapper.selectTTestpaperStudentInfoByIds(ksid,sjid,xyid);
        Integer size = tTestpaperStudent.size();
        tExamination.setYhdtcs(size.toString());
        tExamination.settTestpaperStudent(tTestpaperStudent);
        return tExamination;
    }

}
