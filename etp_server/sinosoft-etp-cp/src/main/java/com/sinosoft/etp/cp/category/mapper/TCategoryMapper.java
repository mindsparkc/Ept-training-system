package com.sinosoft.etp.cp.category.mapper;


import com.sinosoft.etp.cp.category.domain.TCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类树Mapper接口
 *
 *
 * @date 2022-05-11
 */
public interface TCategoryMapper
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
     * 删除分类树
     *
     * @param id 分类树ID
     * @return 结果
     */
    public int deleteTCategoryById(Long id);

    /**
     * 批量删除分类树
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCategoryByIds(Long[] ids);


    /**
     * 校验分类名称是否唯一
     *
     * @param name 名称
     * @param parentId 父ID
     * @return 结果
     */
    public TCategory checkCategoryNameUnique(@Param("name") String name, @Param("parentId") Long parentId);

    /**
     * 是否存在子节点
     *
     * @param id 分类id
     * @return 结果
     */
    public int hasChildByCategoryId(Long id);
    /**
     * 是否存在课件
     * @param id 分类id
     * @return 结果
     */
    public int checkCategoryExistKJ(Long id);
    /**
     * 是否存在课程
     * @param id 分类id
     * @return 结果
     */
    public int checkCategoryExistKC(Long id);

    public int checkCategoryExistTM(Long id);

    public int checkCategoryExistSJ(Long id);

    public int checkCategoryExistKS(Long id);

}
