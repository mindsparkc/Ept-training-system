package com.sinosoft.user.curriculum.controller;

import com.sinosoft.admin.paper.service.IPaperService;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.user.curriculum.domain.CurriculumTrainStudent;
import com.sinosoft.user.curriculum.service.ICurriculumTrainStudentService;
import com.sinosoft.user.train.controller.UserTrainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户 课程接口
 */
@RestController
@RequestMapping("/user/v1/curriculum")
public class UserCurriculumController  extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(UserCurriculumController.class);

    @Resource
    private ICurriculumTrainStudentService iCurriculumTrainStudentService;

    /**
     * 用户课程-习题集-答案 查询
     */
    @GetMapping(value = "/selectCTrainInfo")
    public AjaxResult selectCTrainInfo(CurriculumTrainStudent curriculumTrainStudent){
        curriculumTrainStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        return AjaxResult.success(iCurriculumTrainStudentService.selectCTrainInfo(curriculumTrainStudent));
    }


    /**
     * 用户课程-习题集-答案 保存
     */
    @PostMapping(value = "/insertCTrainInfo")
    public AjaxResult insertCTrainInfo(@RequestBody  CurriculumTrainStudent curriculumTrainStudent){
        curriculumTrainStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        deleteCTrainInfo(curriculumTrainStudent);
        return AjaxResult.success(iCurriculumTrainStudentService.insertCTrainInfo(curriculumTrainStudent));
    }



    /**
     * 用户课程-习题集-答案 删除
     */
    @DeleteMapping("/deleteCTrainInfo")
    public AjaxResult deleteCTrainInfo(@RequestBody CurriculumTrainStudent curriculumTrainStudent){
        curriculumTrainStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        return AjaxResult.success(iCurriculumTrainStudentService.deleteCTrainInfo(curriculumTrainStudent));
    }
}
