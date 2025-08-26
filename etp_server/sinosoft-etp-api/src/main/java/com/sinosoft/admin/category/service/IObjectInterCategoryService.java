package com.sinosoft.admin.category.service;

import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.ObjectInterCategory;

import java.util.List;

public interface IObjectInterCategoryService {
    // 根据题目 ID 获取分类
    public List<ObjectInterCategory> selectObjectInterCategory(CategoryType.CategoryEnum categoryEnum, List<Long> quIds);

    // 批量插入分类与对象关联关系
    public int batchInsertObjectInterCategory(CategoryType.CategoryEnum categoryEnum, List<ObjectInterCategory> objectInterCategory);

    // 根据对象ID（List<Long>）删除与分类的关联
    public int deleteByObjIds(CategoryType.CategoryEnum categoryEnum, List<Long> ids);

    // 清空某分类 ID 下的数据
    public int deleteByCatIds(CategoryType.CategoryEnum categoryEnum, List<Long> ids);

    // 将某些对象 ID 更新为指定分类 ID
    public int updateByObjIds(CategoryType.CategoryEnum categoryEnum, List<Long> objIds, Long catId);

    //  校验某分类下是否为空
    public boolean hasObj(CategoryType.CategoryEnum categoryEnum, List<Long> ids);
}
