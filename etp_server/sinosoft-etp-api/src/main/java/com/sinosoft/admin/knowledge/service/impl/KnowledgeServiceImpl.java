package com.sinosoft.admin.knowledge.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.admin.knowledge.domain.Knowledge;
import com.sinosoft.admin.knowledge.mapper.KnowledgeMapper;
import com.sinosoft.admin.knowledge.service.IKnowledgeService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识点Service业务层处理
 *
 *
 * @date 2022-08-10
 */
@Service
public class KnowledgeServiceImpl implements IKnowledgeService
{
    @Resource
    private KnowledgeMapper knowledgeMapper;
    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.KNOWLEDGE_CATEGORY;

    @Resource
    private IObjectInterCategoryService objectInterCategoryService;
    @Resource
    private ICategoryService categoryService;
    /**
     * 查询知识点
     *
     * @param knoledgeId 知识点ID
     * @return 知识点
     */
    @Override
    public Knowledge selectKnowledgeById(Long knoledgeId)
    {
        Knowledge knowledge = knowledgeMapper.selectKnowledgeById(knoledgeId);
        // 补充分类信息
        List<ObjectInterCategory> interCategories = objectInterCategoryService.selectObjectInterCategory(categoryEnum, Arrays.asList(knoledgeId));
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
        knowledge.setCategoryIds(categories);
        return knowledge;
    }

    /**
     * 查询知识点列表
     *
     * @param knowledge 知识点
     * @return 知识点
     */
    @EtpDataScope(alias = "k")
    @Override
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge)
    {
        List<Knowledge> knowledgeList =  knowledgeMapper.selectKnowledgeList(knowledge);
        for (Knowledge k:knowledgeList) {
            int num = knowledgeMapper.selectChildById(k.getKnoledgeId());
            if(num>0){
                k.setHasChildren(true);
            }else{
                k.setHasChildren(false);
            }
        }
        return knowledgeList;
    }

    /**
     * 查询知识点列表
     *
     * @param knoledgeIds 需要查询的知识点ID
     * @return 知识点集合
     */
    @Override
    public List<Knowledge> selectKnowledgeByIds(Long[] knoledgeIds) {
        return knowledgeMapper.selectKnowledgeByIds(knoledgeIds);
    }

    /**
     * 新增知识点
     *
     * @param knowledge 知识点
     * @return 结果
     */
    @Override
    public int insertKnowledge(Knowledge knowledge)
    {
        EtpDataScopeUtils.setDataScope(knowledge);// 20250520 lyd etp数据权限相关字段

        knowledge.setContentDisplay(knowledge.getContent());
        knowledge.setContent(StringUtils.getText(knowledge.getContent()));
        knowledge.setKnoledgeId(IdUtil.nextId());
        knowledge.setCreateTime(DateUtils.getNowDate());
        knowledge.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        knowledge.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        knowledge.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        // 试卷归属分类
        if (knowledge.hasCategoryIds()) {
            List<ObjectInterCategory> categories = getKnowledgeInterCategory(knowledge);
            // 批量插入试卷归属分类
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, categories);
        }

        return knowledgeMapper.insertKnowledge(knowledge);
    }

    /**
     * 修改知识点
     *
     * @param knowledge 知识点
     * @return 结果
     */
    @Override
    public int updateKnowledge(Knowledge knowledge)
    {
        knowledge.setContentDisplay(knowledge.getContent());
        knowledge.setContent(StringUtils.getText(knowledge.getContent()));
        knowledge.setUpdateTime(DateUtils.getNowDate());
        knowledge.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        // 分类
        // 删除关联分类 && 插入关联分类
        objectInterCategoryService.deleteByObjIds(categoryEnum, Arrays.asList(knowledge.getKnoledgeId()));
        if (knowledge.hasCategoryIds()) {
            List<ObjectInterCategory> interCategory = getKnowledgeInterCategory(knowledge);
            objectInterCategoryService.batchInsertObjectInterCategory(categoryEnum, interCategory);
        }

        return knowledgeMapper.updateKnowledge(knowledge);
    }

    /**
     * 批量删除知识点
     *
     * @param knoledgeIds 需要删除的知识点ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeByIds(Long[] knoledgeIds)
    {
        return knowledgeMapper.deleteKnowledgeByIds(knoledgeIds);
    }

    /**
     * 删除知识点信息
     *
     * @param knoledgeId 知识点ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeById(Long knoledgeId)
    {
        return knowledgeMapper.deleteKnowledgeById(knoledgeId);
    }

    /**
     * 获取下级知识点
     * @param parentId 父类id
     * @return 结果
     */
    @Override
    public List<Knowledge> getChildList(Long parentId) {
        List<Knowledge> list = knowledgeMapper.getChildList(parentId);
        for (Knowledge k : list) {
            int num = knowledgeMapper.selectChildById(k.getKnoledgeId());
            if(num > 0){
                k.setHasChildren(true);
            }else{
                k.setHasChildren(false);
            }
        }
        return list;
    }

    /**
     * 获取知识点的关联分类
     *
     * @param knowledge
     * @return
     */
    private List<ObjectInterCategory> getKnowledgeInterCategory(Knowledge knowledge) {
        return knowledge.getCategoryIds().stream().map(ancestors -> {
            ObjectInterCategory interCategory = new ObjectInterCategory();
            Long catId = ancestors.get(ancestors.size()-1);
            interCategory.setCatId(catId);
            interCategory.setObjId(knowledge.getKnoledgeId());
            // 设置祖级列表
            Category category = categoryService.selectById(categoryEnum, catId);
            interCategory.setAncestors(category.getAncestors()==null||category.getAncestors().equals("0")||category.getAncestors().length()==0?
                    String.valueOf(catId):category.getAncestors()+","+catId);
            return interCategory;
        }).collect(Collectors.toList());
    }
}
