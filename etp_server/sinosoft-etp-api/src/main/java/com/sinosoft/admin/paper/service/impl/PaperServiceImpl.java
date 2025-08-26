package com.sinosoft.admin.paper.service.impl;

import cn.jkingtools.utils.BeanUtils;
import cn.jkingtools.utils.DateUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.admin.exam.mapper.ExamMapper;
import com.sinosoft.admin.paper.domain.PaperPreview;
import com.sinosoft.admin.paper.domain.PaperQuery;
import com.sinosoft.admin.paper.domain.PaperReqDTO;
import com.sinosoft.admin.paper.domain.PaperRespDTO;
import com.sinosoft.admin.paper.mapper.PaperMapper;
import com.sinosoft.admin.paper.mapper.PaperPreviewMapper;
import com.sinosoft.admin.paper.mapper.PaperUnitMapper;
import com.sinosoft.admin.paper.service.IPaperService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.*;
import com.sinosoft.core.enums.PaperStrategyEnum;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.user.exam.domain.UserExam;
import com.sinosoft.user.exam.service.IUserExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 试卷基础信息Service业务层处理
 *
 *
 * @date 2022-08-24
 */
@Service
public class PaperServiceImpl implements IPaperService
{
    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.PAPER_CATEGORY;
    private static CategoryType.CategoryEnum trainCategoryEnum = CategoryType.CategoryEnum.TRAIN_CATEGORY;

    @Resource
    private PaperMapper paperMapper;
    @Resource
    private PaperUnitMapper paperUnitMapper;
    @Resource
    private PaperPreviewMapper paperPreviewMapper;
    @Resource
    private IObjectInterCategoryService objectInterCategoryService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    IUserExamService userExamService;

    @Resource
    ExamMapper examMapper;

    /**
     * 查询试卷基础信息
     *
     * @param paperId 试卷基础信息ID
     * @return 试卷基础信息
     */
    @Override
    public PaperRespDTO selectPaperById(Long paperId) {
        Paper paper = paperMapper.selectPaperById(paperId);

        if (paper == null) throw new RuntimeException("获取试卷信息失败");

        PaperRespDTO paperDto = new PaperRespDTO();
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(paperDto, paper);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // 补充试卷规则
        List<PaperUnit> paperUnits = paperUnitMapper.selectPaperUnitByPaperId(paperId);

        // 补充随机组卷中题目分类名
        Map<Long, Category> categoryMap = categoryService.select(CategoryType.CategoryEnum.QUESTION_CATEGORY, null)
                .stream().collect(Collectors.toMap(Category::getCatId, category -> category));
        for (PaperUnit paperUnit : paperUnits) {
            for (PaperUnitItem unitRule : paperUnit.getUnitRule()) {
                // 只处理随机组卷
                if (!unitRule.getStrategy().equals(PaperStrategyEnum.RANDOM.getCode())) continue;
                PaperUnitItemRule rule = unitRule.getRule();
                List<Long> catId = rule.getCatId();
                StringBuilder catDispaly = new StringBuilder();
                for (Long l : catId) {
                    catDispaly.append(categoryMap.get(l).getName()).append("/");
                }
                rule.setCatDisplay(catDispaly.toString());
            }
        }
        paperDto.setPaperUnit(paperUnits);

        // 补充分类信息
        List<ObjectInterCategory> interCategories = null;
        if("train".equals(paper.getFlag())){//习题集
            interCategories = objectInterCategoryService.selectObjectInterCategory(trainCategoryEnum, Arrays.asList(paperId));
        }else{
            interCategories = objectInterCategoryService.selectObjectInterCategory(categoryEnum, Arrays.asList(paperId));
        }
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
        paperDto.setCategoryIds(categories);

        return paperDto;
    }

    /**
     * 预览试卷
     * @param paperId
     */
    @Override
    public UserExam previewPaperById(Long paperId) {
        UserExam userExam = new UserExam();
        userExam.setPaperId(paperId);
        userExam.setExamId(0L);

        PaperPreview preview = paperPreviewMapper.selectPaperPreviewById(paperId);

        if (preview == null) {
            // 保存预览试卷快照
            UserExam exam = userExamService.makeupExamContent0(userExam);
            preview = new PaperPreview();
            preview.setPaperId(paperId);
            preview.setVersion(0L);
            preview.setContent(exam);
            paperPreviewMapper.insertPaperPreview(preview);
        }

        return preview.getContent();
    }

    /**
     * 查询试卷基础信息列表
     *
     * @param paper 试卷基础信息
     * @return 试卷基础信息
     */
    @EtpDataScope(alias = "p")
    @Override
    public List<Paper> selectPaperList(PaperQuery paper)
    {
        return paperMapper.selectPaperList(paper);
    }

    /**
     * 新增试卷基础信息
     *
     * @param paperDTO 试卷基础信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long insertPaper(PaperReqDTO paperDTO) throws InvocationTargetException, IllegalAccessException {
        paperDTO.setPaperId(IdUtil.nextId());

        Paper paper = new Paper();
        org.apache.commons.beanutils.BeanUtils.copyProperties(paper, paperDTO);
        paper.setVersion(DateUtils.getNowDate().getTime());
        paper.setCreateTime(DateUtils.getNowDate());
        paper.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        paper.setCreateBy(SecurityUtils.getUsername());

        if (paperDTO.getStatus()==99 && (paperDTO.getPaperUnit() == null || paperDTO.getPaperUnit().size() == 0)) {
            throw new RuntimeException("不能保存或发布空白试卷，请完善试卷信息！");
        }

        // 遍历试卷各单元，设置额外信息
        if (paperDTO.getPaperUnit() != null) {
            for (PaperUnit paperUnit : paperDTO.getPaperUnit()) {
                if (paperDTO.getStatus()==99 && (paperUnit.getUnitRule()==null || paperUnit.getUnitRule().size() == 0 )) {
                    throw new RuntimeException("请完善大题“" + paperUnit.getDisplay() + "”信息！");
                }

                paperUnit.setPaperId(paper.getPaperId());
                paperUnit.setPaperUnitId(IdUtil.nextId());
                paperUnit.setPaperVer(paper.getVersion());

                if(paperUnit.getUnitRule() != null) {
                    for (PaperUnitItem unitRule : paperUnit.getUnitRule()) {
                        unitRule.setPaperId(paper.getPaperId());
                        unitRule.setPaperUnitId(paperUnit.getPaperUnitId());
                        unitRule.setRuleId(IdUtil.nextId());
                    }
                }
            }

            // 插入试卷单元
            if (paperDTO.getPaperUnit().size()>0) paperUnitMapper.batchInsertPaperUnit(paperDTO.getPaperUnit());
        }


        // 试卷归属分类
        if (paperDTO.hasCategoryIds()) {
            List<ObjectInterCategory> categories = getPaperInterCategory(paperDTO);
            // 批量插入试卷归属分类
            if("train".equals(paperDTO.getFlag())){
                objectInterCategoryService.batchInsertObjectInterCategory(trainCategoryEnum, categories);//习题集
            }else{
                objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories); //试卷
            }

        }

        EtpDataScopeUtils.setDataScope(paper);// 20250520 lyd etp数据权限相关字段
        int i = paperMapper.insertPaper(paper);
        if (i<=0) throw new RuntimeException("保存试卷失败");

        // 保存预览试卷快照
        UserExam userExam = new UserExam();
        userExam.setPaperId(paper.getPaperId());
        userExam.setExamId(0L);

        UserExam exam = userExamService.makeupExamContent0(userExam);
        PaperPreview preview = new PaperPreview();
        preview.setPaperId(paper.getPaperId());
        preview.setVersion(paper.getVersion());
        preview.setContent(exam);
        paperPreviewMapper.insertPaperPreview(preview);

        return paper.getPaperId();
    }

    /**
     * 获取试卷/习题集的关联分类
     *
     * @param paperDTO
     * @return
     */
    private List<ObjectInterCategory> getPaperInterCategory(PaperReqDTO paperDTO) {
        return paperDTO.getCategoryIds().stream().map(ancestors -> {
            ObjectInterCategory interCategory = new ObjectInterCategory();
            Long catId = ancestors.get(ancestors.size()-1);
            interCategory.setCatId(catId);
            interCategory.setObjId(paperDTO.getPaperId());
            // 设置祖级列表
            Category category = null;
            if("train".equals(paperDTO.getFlag())){
                category = categoryService.selectById(trainCategoryEnum, catId);
            }else{
                category = categoryService.selectById(categoryEnum, catId);
            }
            interCategory.setAncestors(category.getAncestors()==null||category.getAncestors().equals("0")||category.getAncestors().length()==0?
                    String.valueOf(catId):category.getAncestors()+","+catId);
            return interCategory;
        }).collect(Collectors.toList());
    }

    /**
     * 修改试卷状态
     *
     * @param paperDTO 试卷基础信息
     * @return 结果
     */
    public int updatePaperStatus(PaperReqDTO paperDTO) {
        // 若是发布试卷，则需要提取试卷快照，并
        if (paperDTO.getStatus() == 99) {
            List<PaperUnit> paperUnits = paperUnitMapper.selectPaperUnitByPaperId(paperDTO.getPaperId());
            if (paperUnits == null || paperUnits.size() == 0) {
                throw new RuntimeException("不能保存或发布空白试卷，请完善试卷信息！");
            }
            for (PaperUnit paperUnit : paperUnits) {
                if (paperUnit.getUnitRule() == null || paperUnit.getUnitRule().size() == 0) {
                    throw new RuntimeException("请完善大题“" + paperUnit.getDisplay() + "”信息！");
                }
            }

//            UserExam exam = userExamService.makeupExamContent0(userExam);
//            PaperPreview preview = new PaperPreview();
//            preview.setPaperId(paper.getPaperId());
//            preview.setVersion(paper.getVersion());
//            preview.setContent(exam);
//            paperPreviewMapper.deletePaperPreviewById(paper.getPaperId());
//            paperPreviewMapper.insertPaperPreview(preview);
        }

        Paper paper = new Paper();
        paper.setPaperId(paperDTO.getPaperId());
        paper.setStatus(paperDTO.getStatus());
        return paperMapper.updatePaperStatus(paper);
    }

    /**
     * 修改试卷基础信息
     *
     * @param paperDTO 试卷基础信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updatePaper(PaperReqDTO paperDTO) {
        Paper paper = BeanUtils.map(paperDTO, Paper.class);
        paper.setVersion(DateUtils.getNowDate().getTime());
        paper.setUpdateTime(DateUtils.getNowDate());
        paper.setUpdateBy(SecurityUtils.getUsername());

        int updatePaper = paperMapper.updatePaper(paper);
        if (updatePaper != 1) {
            throw new RuntimeException("更新试卷内容出错，请联系管理员解决！");
        }

        if (paperDTO.getStatus()==99 && (paperDTO.getPaperUnit() == null || paperDTO.getPaperUnit().size() == 0)) {
            throw new RuntimeException("不能保存或发布空白试卷，请完善试卷信息！");
        }

        // 遍历试卷各单元，设置额外信息
        if (paperDTO.getPaperUnit() != null) {
            for (PaperUnit paperUnit : paperDTO.getPaperUnit()) {
                if (paperDTO.getStatus()==99 && (paperUnit.getUnitRule()==null || paperUnit.getUnitRule().size() == 0 )) {
                    throw new RuntimeException("请完善大题“" + paperUnit.getDisplay() + "”信息！");
                }
                paperUnit.setPaperId(paper.getPaperId());
                paperUnit.setPaperUnitId(IdUtil.nextId());
                paperUnit.setPaperVer(paper.getVersion());
                if (paperUnit.getUnitRule() != null) {
                    for (PaperUnitItem unitRule : paperUnit.getUnitRule()) {
                        unitRule.setPaperId(paper.getPaperId());
                        unitRule.setPaperUnitId(paperUnit.getPaperUnitId());
                        unitRule.setRuleId(IdUtil.nextId());
                    }
                }
            }
        }

        // 删除老的规则，插入新规则
        paperUnitMapper.deletePaperUnitByPaperId(paperDTO.getPaperId());
        if (paperDTO.getPaperUnit().size()>0) paperUnitMapper.batchInsertPaperUnit(paperDTO.getPaperUnit());

        // 分类
        // 删除关联分类 && 插入关联分类
        if("train".equals(paperDTO.getFlag())){
            objectInterCategoryService.deleteByObjIds(trainCategoryEnum, Arrays.asList(paperDTO.getPaperId()));
        }else{
            objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(paperDTO.getPaperId()));
        }

        if (paperDTO.hasCategoryIds()) {
            List<ObjectInterCategory> interCategory = getPaperInterCategory(paperDTO);
            if("train".equals(paperDTO.getFlag())){
                objectInterCategoryService.batchInsertObjectInterCategory(trainCategoryEnum, interCategory);
            }else{
                objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, interCategory);
            }

        }

        // 保存预览试卷快照
        UserExam userExam = new UserExam();
        userExam.setPaperId(paper.getPaperId());
        userExam.setExamId(0L);

        UserExam exam = userExamService.makeupExamContent0(userExam);
        PaperPreview preview = new PaperPreview();
        preview.setPaperId(paper.getPaperId());
        preview.setVersion(paper.getVersion());
        preview.setContent(exam);
        paperPreviewMapper.deletePaperPreviewById(paper.getPaperId());
        paperPreviewMapper.insertPaperPreview(preview);

        return updatePaper;
    }

    /**
     * 批量删除试卷基础信息
     *
     * @param paperIds 需要删除的试卷基础信息ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deletePaperByIds(Long[] paperIds) {
        // 监测试卷是否已被考试引用，若被引用则不允许删除
        // 需要提示用户哪几份试卷不能删除
//        List<Paper> paperList = paperMapper.selectPaperByIds(paperIds);
//        StringBuilder result = new StringBuilder("如下试卷存在被考试引用的情况，不能被删除【");
//        boolean r = false;
//        for (Paper paper : paperList) {
//            if (paper.getRelateNum()!=null && paper.getRelateNum() > 0) {
//                r = true;
//                result.append(paper.getName()).append("、");
//            }
//        }
//
//        if (r) {
//            result.deleteCharAt(result.length()-1).append("】");
//            throw new RuntimeException(result.toString());
//        }
        List<Exam> exams = examMapper.selectExamByPaperIds(paperIds);
        if (exams.size() > 0) {
            StringBuilder result = new StringBuilder("试卷已被考试");
            for (Exam exam : exams) {
                result.append("【").append(exam.getName()).append("】");
            }
            result.deleteCharAt(result.length()-1).append("引用，不能删除");
            throw new RuntimeException(result.toString());
        }

        paperUnitMapper.deletePaperUnitByPaperIds(paperIds);
        return paperMapper.deletePaperByIds(paperIds);
    }

    /**
     * 删除试卷基础信息信息
     *
     * @param paperId 试卷基础信息ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deletePaperById(Long paperId)
    {
        Exam exam = examMapper.selectExamByPaperId(paperId);
        if (exam != null) {
            StringBuilder result = new StringBuilder("试卷已被考试【").append(exam.getName())
                    .append("】引用，不能删除");
            throw new RuntimeException(result.toString());
        }
        paperUnitMapper.deletePaperUnitByPaperId(paperId);
        return paperMapper.deletePaperById(paperId);
    }

    @Override
    public List<Paper> getSelectedXtjByKc(Long kcid) {
        return paperMapper.getSelectedXtjByKc(kcid);
    }
}
