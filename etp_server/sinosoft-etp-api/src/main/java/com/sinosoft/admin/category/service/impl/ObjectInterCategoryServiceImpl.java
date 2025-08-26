package com.sinosoft.admin.category.service.impl;

import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.ObjectInterCategory;
import com.sinosoft.admin.category.mapper.ObjectInterCategoryMapper;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObjectInterCategoryServiceImpl implements IObjectInterCategoryService {

    @Resource
    private ObjectInterCategoryMapper objectInterCategoryMapper;

    @Resource
    private ICategoryService categoryService;

    private static String insertColumns = "obj_id, cat_id, ancestors";
    private static String selectColumns = "obj_id as objId, cat_id catId, ancestors";

    /**
     * 根据题目 ID 获取分类信息
     *
     * @param categoryEnum
     * @param quIds
     * @return
     */
    @Override
    public List<ObjectInterCategory> selectObjectInterCategory(CategoryType.CategoryEnum categoryEnum, List<Long> quIds) {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("select ").append(selectColumns).append(" from ")
                .append(CategoryType.getInterTableName(categoryEnum)).append(" where obj_id in (");
        for (Long quId : quIds) {
            selectBuilder.append(quId).append(",");
        }
        selectBuilder.deleteCharAt(selectBuilder.length()-1).append(")");

        return objectInterCategoryMapper.selectInter(selectBuilder.toString());
    }

    @Override
    public int batchInsertObjectInterCategory(CategoryType.CategoryEnum categoryEnum, List<ObjectInterCategory> interCategories) {
        if (interCategories == null || interCategories.size() == 0) {
            return 0;
        }

        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into ").append(CategoryType.getInterTableName(categoryEnum)).append("(")
                .append(insertColumns).append(") values ");
        for (ObjectInterCategory interCategory:interCategories) {
            insertSql.append("(").append(interCategory.getObjId()).append(",")
                    .append(interCategory.getCatId()).append(",'").append(interCategory.getAncestors())
                    .append("')").append(",");
        }

        insertSql.deleteCharAt(insertSql.length()-1);
        return objectInterCategoryMapper.insertInter(insertSql.toString());
    }

    // 根据对象ID（List<Long>）删除与分类的关联
    @Override
    public int deleteByObjIds(CategoryType.CategoryEnum categoryEnum, List<Long> objIds) {
        if (objIds == null || objIds.size() == 0) return 0;

        StringBuilder deleteBuilder = new StringBuilder();
        deleteBuilder.append("delete from ").append(CategoryType.getInterTableName(categoryEnum))
                .append(" where obj_id in (");
        for (Long objId : objIds) {
            deleteBuilder.append(objId).append(",");
        }
        deleteBuilder.deleteCharAt(deleteBuilder.length()-1).append(")");
        return objectInterCategoryMapper.deleteInter(deleteBuilder.toString());
    }

    // 清空某分类 ID 下的数据
    @Override
    public int deleteByCatIds(CategoryType.CategoryEnum categoryEnum, List<Long> catIds) {
        if (catIds == null || catIds.size() == 0) return 0;

        StringBuilder deleteBuilder = new StringBuilder();
        deleteBuilder.append("delete from ").append(CategoryType.getInterTableName(categoryEnum))
                .append("where cat_id in (");
        for (Long catId : catIds) {
            deleteBuilder.append(catId).append(",");
        }
        deleteBuilder.deleteCharAt(deleteBuilder.length()-1).append(")");
        return objectInterCategoryMapper.deleteInter(deleteBuilder.toString());
    }

    // 将某些对象 ID 更新为指定分类 ID
    @Override
    public int updateByObjIds(CategoryType.CategoryEnum categoryEnum, List<Long> objIds, Long catId) {
        if (objIds == null || objIds.size() == 0) return 0;

        // 先根据删除，再新增
        deleteByObjIds(categoryEnum, objIds);
        List<ObjectInterCategory> objectInterCategories = objIds.stream().map(objId -> {
            ObjectInterCategory interCategory = new ObjectInterCategory();
            interCategory.setObjId(objId);
            interCategory.setCatId(catId);
            Category category = categoryService.selectById(categoryEnum, catId);
            interCategory.setAncestors(category.getAncestors() == null || category.getAncestors().equals("0") || category.getAncestors().length() == 0 ?
                    String.valueOf(catId) : category.getAncestors() + "," + catId);
            return interCategory;
        }).collect(Collectors.toList());
        batchInsertObjectInterCategory(categoryEnum, objectInterCategories);
        return 0;
    }

    //  校验某分类下是否为空
    @Override
    public boolean hasObj(CategoryType.CategoryEnum categoryEnum, List<Long> catIds) {
        if (catIds == null || catIds.size() == 0) return false;

        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("select ").append(selectColumns).append(" from ")
                .append(CategoryType.getInterTableName(categoryEnum)).append(" where cat_id in (");

        for (Long catId : catIds) {
            selectBuilder.append(catId).append(",");
        }
        selectBuilder.deleteCharAt(selectBuilder.length() - 1).append(") LIMIT 1");

        return objectInterCategoryMapper.selectInter(selectBuilder.toString()).size() > 0;
    }
}
