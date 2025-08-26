package com.sinosoft.admin.question.service.impl;

//import cn.jkingtools.utils.BeanUtils;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.admin.question.domain.FactByLevel;
import com.sinosoft.admin.question.domain.QuestionReqDTO;
import com.sinosoft.admin.question.domain.QuestionReqQuery;
import com.sinosoft.admin.question.mapper.QuestionMapper;
import com.sinosoft.admin.question.service.IQuestionService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.exception.CustomException;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.Question;
import com.sinosoft.core.domain.QuestionAnswer;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目信息Service业务层处理
 *
 * @author wanglijie
 * @date 2022-08-09
 */
// @Transactional
@Service
public class QuestionServiceImpl implements IQuestionService
{
    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.QUESTION_CATEGORY;

    @Resource
    private QuestionMapper questionMapper;

//    @Resource
//    private QuestionInterCategoryMapper questionInterCategoryMapper;

    @Resource
    private IObjectInterCategoryService objectInterCategoryService;

    @Resource
    private ICategoryService categoryService;

    /**
     * 查询题目信息
     *
     * @param quId 题目信息ID
     * @return 题目信息
     */
    @Override
    public QuestionReqDTO selectQuestionById(Long quId) {
        // 获取主题目
        Question question = questionMapper.selectQuestionById(quId);

        if (question == null) {
            throw new RuntimeException("获取题目详细信息出错");
        }

        QuestionReqDTO masterDto = cn.jkingtools.utils.BeanUtils.map(question, QuestionReqDTO.class);

        // 补充分类信息
        List<ObjectInterCategory> interCategories = objectInterCategoryService.selectObjectInterCategory(categoryEnum, Arrays.asList(quId));
        List<List<Long>> categories = new ArrayList<>();
        for (ObjectInterCategory interCategory : interCategories) {
            String[] split = interCategory.getAncestors().split(",");
            categories.add(Arrays.stream(split).filter(s->{
                if (s.equals("0")) {
                    return false;
                }
                return true;
                }).map(s -> Long.valueOf(s)).collect(Collectors.toList()));
        }

        masterDto.setCategoryIds(categories);

        return masterDto;
    }

    /**
     * 查询题目信息列表
     *
     * @param questionQuery 题目信息
     * @return 题目信息
     */
    @EtpDataScope(alias = "qu")
    @Override
    public List<Question> selectQuestionList(QuestionReqQuery questionQuery) {
        return questionMapper.selectQuestionList(questionQuery);
    }

    //按难度等级统计题目数量
    @Override
    public List<FactByLevel> factQuery(QuestionReqQuery questionQuery) {
        return questionMapper.factQuery(questionQuery);
    }

    @EtpDataScope(alias = "qu")
    @Override
    public List<Question> randomQuery(QuestionReqQuery questionQuery) {
        if(questionQuery.getN() <= 0) return null;
        return questionMapper.randomQuery(questionQuery);
    }

    /**
     * 查询题目信息列表
     *
     * @param quIds 需要查询的题目信息ID
     * @return 题目信息集合
     */
    public List<Question> selectQuestionByIds(Long[] quIds) {
        return questionMapper.selectQuestionByIds(quIds);
    }

    /**
     * 新增题目信息
     *
     * @param questionDto 题目信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertQuestion(QuestionReqDTO questionDto) {

        questionDto.setQuId(IdUtil.nextId());

        if (questionDto.getAnswers() != null) {
            for (QuestionAnswer answer : questionDto.getAnswers()) {
                answer.setQuId(questionDto.getQuId());
                answer.setAnswerId(IdUtil.nextId());
            }
        }

        if (questionDto.getChildren() != null) {
            for (Question child : questionDto.getChildren()) {
                child.setQuId(IdUtil.nextId());
                child.setParentId(questionDto.getQuId());
                for (QuestionAnswer answer : child.getAnswers()) {
                    answer.setQuId(child.getQuId());
                    answer.setAnswerId(IdUtil.nextId());
                }
            }
        }

        // 插入关联分类
        if (questionDto.hasCategoryIds()) {
            List<ObjectInterCategory> categories = getQuestionInterCategory(questionDto, questionDto);
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories);
        }

//        Question question = BeanUtils.map(questionDto, Question.class);
        Question question = new Question();
        try {
            BeanUtils.copyProperties(question, questionDto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        question.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        question.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        question.setCreateBy(SecurityUtils.getUsername());
        question.setCreateTime(DateUtils.getNowDate());

        question.setVersion(DateUtils.getNowDate().getTime());
        question.setDisplay(StringUtils.getText(questionDto.getContent()));
        EtpDataScopeUtils.setDataScope(question);// 20250520 lyd etp数据权限相关字段
        int i = questionMapper.insertQuestion(question);
        if (i <=0 ) throw new RuntimeException("保存题目信息失败");

        return question.getQuId();
    }

    /**
     * 获取主题目的关联分类
     *
     * @param questionDto
     * @param master
     * @return
     */
    private List<ObjectInterCategory> getQuestionInterCategory(QuestionReqDTO questionDto, QuestionReqDTO master) {
        return questionDto.getCategoryIds().stream().map(ancestors -> {
            ObjectInterCategory interCategory = new ObjectInterCategory();
            Long catId = ancestors.get(ancestors.size()-1);
            interCategory.setCatId(catId);
            interCategory.setObjId(master.getQuId());
            // 设置祖级列表
            Category category = categoryService.selectById(categoryEnum, catId);
            interCategory.setAncestors(category.getAncestors()==null||category.getAncestors().equals("0")||category.getAncestors().length()==0?
                    String.valueOf(catId):category.getAncestors()+","+catId);
            return interCategory;
        }).collect(Collectors.toList());
    }

    /**
     * 修改题目信息
     *
     * @param questionDto 题目信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateQuestion(QuestionReqDTO questionDto) {
        if (questionDto.getAnswers() != null) {
            for (QuestionAnswer answer : questionDto.getAnswers()) {
                if (answer.getAnswerId() == null) {
                    answer.setAnswerId(IdUtil.nextId());
                }
            }
        }

        if (questionDto.getChildren() != null) {
            for (Question child : questionDto.getChildren()) {
                if (child.getAnswers() == null) continue;
                for (QuestionAnswer answer : child.getAnswers()) {
                    if (answer.getAnswerId() == null) {
                        answer.setAnswerId(IdUtil.nextId());
                    }
                }
            }
        }

        Question question = new Question();
        try {
            BeanUtils.copyProperties(question, questionDto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        question.setDisplay(StringUtils.getText(questionDto.getContent()));
        int row = questionMapper.updateQuestion(question);

        if (row != 1) {
            throw new CustomException("操作异常，请练习管理员！");
        }

        // 删除关联分类 && 插入关联分类
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(questionDto.getQuId()));
        if (questionDto.hasCategoryIds()) {
            List<ObjectInterCategory> interCategory = getQuestionInterCategory(questionDto, questionDto);
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, interCategory);
        }

        return row;
    }

    /**
     * 批量删除题目信息
     *
     * @param quIds 需要删除的题目信息ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteQuestionByIds(Long[] quIds) {
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(quIds));
        return questionMapper.logicalDeleteQuestionByIds(quIds);
    }

    /**
     * 删除题目信息信息
     *
     * @param quId 题目信息ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteQuestionById(Long quId) {
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(quId));
        return questionMapper.logicalDeleteQuestionByIds(Arrays.asList(quId).toArray(new Long[1]));
    }
}
