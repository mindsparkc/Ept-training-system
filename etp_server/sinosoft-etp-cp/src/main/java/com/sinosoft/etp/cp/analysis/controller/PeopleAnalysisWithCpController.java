package com.sinosoft.etp.cp.analysis.controller;

import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.etp.cp.analysis.service.PeopleAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PeopleAnalysisController
 *
 *
 * @date 2022-02-15
 */
@RestController
@RequestMapping("/etp/peopleAnalysisWithCp")
public class PeopleAnalysisWithCpController extends BaseController {

    @Autowired
    private PeopleAnalysisService peopleAnalysisService;

    @GetMapping(value = "/getData/{ksids}/{xyids}")
    public List<Object> processingPersonnelAnalyzeData(@PathVariable Long[] ksids, @PathVariable Long[] xyids){

        return peopleAnalysisService.processingPersonnelAnalyzeData(ksids, xyids);
    }

}
