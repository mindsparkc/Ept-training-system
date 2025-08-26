package com.sinosoft.user.train.controller;

import com.sinosoft.admin.paper.service.IPaperService;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 供学员端请求，查询习题集
 *
 * @author buguoli
 */
@RestController
@RequestMapping("/user/v1/train")
public class UserTrainController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserTrainController.class);

    @Resource
    private IPaperService paperService;

    /**
     * 获取试卷基础信息详细信息
     */
    @GetMapping(value = "/{paperId}")
    public AjaxResult getInfo(@PathVariable("paperId") Long paperId)
    {
        return AjaxResult.success(paperService.previewPaperById(paperId));
    }


}
