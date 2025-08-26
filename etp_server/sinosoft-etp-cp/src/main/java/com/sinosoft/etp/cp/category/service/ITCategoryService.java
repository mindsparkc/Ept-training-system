package com.sinosoft.etp.cp.category.service;

import com.sinosoft.etp.cp.category.domain.TCategory;

import java.util.List;

/**
 * 分类树Service接口
 *
 *
 * @date 2022-05-11
 */
public interface ITCategoryService
{
    /**
     * 查询分类树
     *
     * @param id 分类树ID
     * @return 分类树
     */
    public TCategory selectTCategoryById(Long id);

    /**
     * 查询分类树列表
     *
     * @param tCategory 分类树
     * @return 分类树集合
     */
    public List<TCategory> selectTCategoryList(TCategory tCategory);

    /**
     * 新增分类树
     *
     * @param tCategory 分类树
     * @return 结果
     */
    public int insertTCategory(TCategory tCategory);

    /**
     * 修改分类树
     *
     * @param tCategory 分类树
     * @return 结果
     */
    public int updateTCategory(TCategory tCategory);

    /**
     * 批量删除分类树
     *
     * @param ids 需要删除的分类树ID
     * @return 结果
     */
    public int deleteTCategoryByIds(Long[] ids);

    /**
     * 删除分类树信息
     *
     * @param id 分类树ID
     * @return 结果
     */
    public int deleteTCategoryById(Long id);


    /**
     * 校验分类名称是否唯一
     *
     * @param tCategory
     * @return 结果
     */
    public String checkCategoryNameUnique(TCategory tCategory);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param datas 分类列表
     * @return 下拉树结构列表
     */
    public List<TCategory> buildCategoryTreeSelect(List<TCategory> datas);

    /**
     * 是否存在子节点
     *
     * @param id 分类id
     * @return 结果
     */
    public boolean hasChildByCategoryId(Long id);

    /**
     * 是否存在课件
     * @param id 分类id
     * @return 结果
     */
    public boolean checkCategoryExistKJ(Long id);
    /**
     * 是否存在课程
     * @param id 分类id
     * @return 结果
     */
    public boolean checkCategoryExistKC(Long id);

    public boolean checkCategoryExistTM(Long id);

    public boolean checkCategoryExistSJ(Long id);

    public boolean checkCategoryExistKS(Long id);
}
