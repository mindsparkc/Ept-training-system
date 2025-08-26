package com.sinosoft.etp.cp.index.controller;

import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;
import com.sinosoft.etp.cp.index.service.IIndexWithCpService;
import com.sinosoft.etp.cp.opinion.domain.TOpinionWithCp;
import com.sinosoft.system.domain.SysNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/etp/indexWithCp")
public class IndexWithCpController extends BaseController {

    @Autowired
    private IIndexWithCpService indexService;

    /** 首页统计 */
    @GetMapping("/total")
    public AjaxResult getTotal(){
        return AjaxResult.success(indexService.getTotal());
    }

    /** 当前课程 */
    @GetMapping("/getCurrentCourse")
    public AjaxResult getCurrentCourse(){
        List<TCurriculumWithCp> list = indexService.getCurrentCourse();
        List<TCurriculumWithCp> result = new ArrayList<>();
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TCurriculumWithCp>(TCurriculumWithCp.class);
        for (TCurriculumWithCp l : list) {
            result.add((TCurriculumWithCp)responseFieldUtil.response(l));
        }
        return AjaxResult.success(result);
    }
    /** 当前考试*/
    @GetMapping("/getCurrentTest")
    public AjaxResult getCurrentTest(){
        List<TExaminationWithCp> list = indexService.getCurrentExaminations();
        List<TExaminationWithCp> result = new ArrayList<>();
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TExaminationWithCp>(TExaminationWithCp.class);
        for (TExaminationWithCp l : list) {
            result.add((TExaminationWithCp)responseFieldUtil.response(l));
        }
        return AjaxResult.success(result);
    }
    /** 热门课件*/
    @GetMapping("/getCurrentCourseware")
    public AjaxResult getCurrentCourseware(){
        List<TCoursewareWithCp> list = indexService.getCurrentCourseware();
        List<TCoursewareWithCp> result = new ArrayList<>();
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TCoursewareWithCp>(TCoursewareWithCp.class);
        for (TCoursewareWithCp l : list) {
            result.add((TCoursewareWithCp)responseFieldUtil.response(l));
        }
        return AjaxResult.success(result);
    }
    /** 活跃学员*/
    @GetMapping("/getHotMembers")
    public AjaxResult getHotMembers(){
        // todo 活跃学员
        return AjaxResult.success();
    }

    /** 通知公告*/
    @GetMapping("/getNotices")
    public AjaxResult getNotices(){
        List<SysNotice> list = indexService.getNotices();
        List<SysNotice> result = new ArrayList<>();
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<SysNotice>(SysNotice.class);
        for (SysNotice l : list) {
            result.add((SysNotice)responseFieldUtil.response(l));
        }
        return AjaxResult.success(result);
    }

    /** 本企业学员待反馈意见*/
    @GetMapping("/getOpinions")
    public AjaxResult getOpinions(){
        List<TOpinionWithCp> list = indexService.getOpinions(SecurityUtils.getLoginUser().getUser().getDeptId());
        List<TOpinionWithCp> result = new ArrayList<>();
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TOpinionWithCp>(TOpinionWithCp.class);
        for (TOpinionWithCp l : list) {
            result.add((TOpinionWithCp)responseFieldUtil.response(l));
        }
        return AjaxResult.success(result);
    }

}
