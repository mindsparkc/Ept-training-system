package com.sinosoft.admin.question.controller;

import com.sinosoft.admin.question.domain.FactByLevel;
import com.sinosoft.admin.question.domain.QuestionReqDTO;
import com.sinosoft.admin.question.domain.QuestionReqQuery;
import com.sinosoft.admin.question.service.IQuestionService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.DictUtils;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.core.domain.Question;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 题目信息Controller
 *
 * @author wanglijie
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/admin/v1/question")
public class QuestionController extends BaseController {
    @Resource
    private IQuestionService questionService;

    /**
     * 查询题目信息列表
     */
    @PreAuthorize("@ss.hasPermi('question:question:list')")
    @GetMapping("/list")
    public TableDataInfo list(QuestionReqQuery questionQuery)
    {
        startPage();
        List<Question> list = questionService.selectQuestionList(questionQuery);
        return getDataTable(list);
    }

    /**
     * 根据传入的分类，进行 group by 操作
     * 按难度等级统计题目数量
     * @param questionQuery
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:question:list')")
    @GetMapping("/fact/level")
    public AjaxResult factQuery(QuestionReqQuery questionQuery) {
        List<FactByLevel> factByLevels = questionService.factQuery(questionQuery);
        return AjaxResult.success(factByLevels);
    }

    /**
     * 随机取题
     *
     * @param questionQuery
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:question:list')")
    @GetMapping("/random")
    public AjaxResult randomQuery(QuestionReqQuery questionQuery)
    {
        List<Question> questions = questionService.randomQuery(questionQuery);
        // ？？？ get(0) 难道只是为了取出个提示？ 随便取第0个难度得名称进行提示 lyd
        String level = DictUtils.getDictLabel("question_level",questionQuery.getLevel().get(0));

        if(questionQuery.getN() > questions.size()){
            return AjaxResult.error("题库【"+level+"】类型题目数量不足，请选择其他题库");
        }
        return AjaxResult.success(questions);
    }

    /**
     * 获取题目信息详细信息
     */
    @PreProcessField(field = "analysis")
    @PreProcessField(field = "content")
    @PreAuthorize("@ss.hasPermi('question:question:query')")
    @GetMapping(value = "/{quId}")
    public AjaxResult getInfo(@PathVariable("quId") Long quId) {
        QuestionReqDTO reqDTO = questionService.selectQuestionById(quId);
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<QuestionReqDTO>(QuestionReqDTO.class);
        Object response = responseFieldUtil.response(reqDTO);
        return AjaxResult.success(response);
    }

    /**
     * 新增题目信息
     */
    @PreAuthorize("@ss.hasPermi('question:question:add')")
    @Log(title = "题目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QuestionReqDTO questionDTO) {
        Long quId = questionService.insertQuestion(questionDTO);
        Map<String, Long> qu = new HashMap<>();
        qu.put("quId", quId);
        return AjaxResult.success(qu);
    }

    /**
     * 修改题目信息
     */
    @PreAuthorize("@ss.hasPermi('question:question:edit')")
    @Log(title = "题目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QuestionReqDTO questionDto)
    {
        return toAjax(questionService.updateQuestion(questionDto));
    }

    /**
     * 删除题目信息
     */
    @PreAuthorize("@ss.hasPermi('question:question:remove')")
    @Log(title = "题目信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{quIds}")
    public AjaxResult remove(@PathVariable Long[] quIds)
    {
        return toAjax(questionService.deleteQuestionByIds(quIds));
    }
}
