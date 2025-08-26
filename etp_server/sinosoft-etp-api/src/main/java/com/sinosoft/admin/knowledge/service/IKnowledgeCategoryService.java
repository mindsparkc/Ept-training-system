package com.sinosoft.admin.knowledge.service;

import com.sinosoft.admin.knowledge.domain.KnowledgeCategory;

import java.util.List;

/**
 * 知识点分类树Service接口
 *
 *
 * @date 2022-08-10
 */
public interface IKnowledgeCategoryService
{
    /**
     * 查询知识点分类树
     *
     * @param catId 知识点分类树ID
     * @return 知识点分类树
     */
    public KnowledgeCategory selectKnowledgeCategoryById(Long catId);

    /**
     * 查询知识点分类树列表
     *
     * @param knowledgeCategory 知识点分类树
     * @return 知识点分类树集合
     */
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory);

    /**
     * 查询知识点分类树列表
     *
     * @param catIds 需要查询的知识点分类树ID
     * @return 知识点分类树集合
     */
    public List<KnowledgeCategory> selectKnowledgeCategoryByIds(Long[] catIds);

    /**
     * 新增知识点分类树
     *
     * @param knowledgeCategory 知识点分类树
     * @return 结果
     */
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 修改知识点分类树
     *
     * @param knowledgeCategory 知识点分类树
     * @return 结果
     */
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory);

    /**
     * 批量删除知识点分类树
     *
     * @param catIds 需要删除的知识点分类树ID
     * @return 结果
     */
    public int deleteKnowledgeCategoryByIds(Long[] catIds);

    /**
     * 删除知识点分类树信息
     *
     * @param catId 知识点分类树ID
     * @return 结果
     */
    public int deleteKnowledgeCategoryById(Long catId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param datas 分类列表
     * @return 下拉树结构列表
     */
    public List<KnowledgeCategory> buildCategoryTreeSelect(List<KnowledgeCategory> datas);

    /**
     * 检验分类名称在父类中是否存在
     * @param parentId 父类ID
     * @param name 分类名称
     * @return
     */
    public int checkName(Long parentId,String name);
}
