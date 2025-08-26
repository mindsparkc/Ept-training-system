package com.sinosoft.etp.cp.analysis.service.impl;

import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.etp.cp.analysis.domain.PeopleAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.mapper.PeopleAnalysisWithCpMapper;
import com.sinosoft.etp.cp.analysis.service.PeopleAnalysisService;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.examination.service.ITExaminationWithCpService;
import com.sinosoft.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeopleAnalysisServiceImpl implements PeopleAnalysisService {
    @Resource
    private PeopleAnalysisWithCpMapper peopleAnalysisMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ITExaminationWithCpService tExaminationService;

    @Override
    public List<Object> processingPersonnelAnalyzeData(Long[] ksids, Long[] xyids) {

        List<Object> array = new ArrayList<>();
        List<Object> nameArray = new ArrayList<>();
        nameArray.add("name");
        for (Long xyid:xyids) {
            SysUser user = userService.selectUserById(xyid);
            nameArray.add(user.getNickName());
        }
        array.add(nameArray);
        for (Long ksid:ksids) {
            TExaminationWithCp tExamination =  tExaminationService.selectTExaminationById(ksid);
            List<Object> scoreArray = new ArrayList<>();
            scoreArray.add(tExamination.getMc());
            for(Long xyid:xyids){
                List<PeopleAndExaminationWithCp> peopleAndExaminationList = peopleAnalysisMapper.selectPeopleAndExaminationResultByKsid(ksid,xyid);

                if (peopleAndExaminationList == null || peopleAndExaminationList.size()==0) {
                    scoreArray.add(0);
                }else {
                    scoreArray.add(peopleAndExaminationList.get(0).getScore());
                }
            }

            array.add(scoreArray);
        }

        return array;
    }

}
