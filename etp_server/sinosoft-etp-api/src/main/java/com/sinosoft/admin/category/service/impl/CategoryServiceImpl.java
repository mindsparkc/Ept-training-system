package com.sinosoft.admin.category.service.impl;

import cn.jkingtools.utils.BeanUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.domain.CategoryVO;
import com.sinosoft.admin.category.mapper.CategoryMapper;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.TreeSelect;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用的分类表操作类
 *
 * @author wanglijie
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Resource
    CategoryMapper categoryMapper;

    private String insertColumns = "cat_id, name, parent_id, ancestors, sort, del_flag, create_by, create_time, update_by, update_time, remark, create_user_id,department_id,tenant_id,pub_flag";
    private String selectColumns = "cat_id as catId, name, parent_id as parentId, ancestors, sort, del_flag as delFlag, create_by as createBy, create_time as createTime, update_by as updateBy, update_time, remark";

    @EtpDataScope(index = 1)
    @Override
    public List<Category> select(CategoryType.CategoryEnum categoryEnum, Category category) {
        String dataScopeSql = "";
        // todo：这里会有隐含的bug跟业务逻辑有关，传空值null时到底要不要数据权限控制  目前如果null值则按所有权限处理
        if(category!=null) {
            if (category.getParams() != null && category.getParams().get(EtpDataScopeUtils.ETP_DATA_SCOPE) != null) {
                dataScopeSql = (String) category.getParams().get(EtpDataScopeUtils.ETP_DATA_SCOPE);
                if (StringUtils.isNotEmpty(dataScopeSql)) {
                    dataScopeSql = " where 1=1 " + dataScopeSql;
                }
            }
        }
        String sql = "select " + selectColumns + " from " + CategoryType.getTableName(categoryEnum)
                + dataScopeSql
                + " order by sort asc";
        return categoryMapper.selectCategory(sql);
    }



    @Override
    public Category selectById(CategoryType.CategoryEnum categoryEnum, Long catId){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select ").append(selectColumns).append(" from ")
                .append(CategoryType.getTableName(categoryEnum))
                .append(" where cat_id=").append(catId);
        List<Category> categories = categoryMapper.selectCategory(sqlBuilder.toString());
        if (categories.size() != 1) {
            throw new RuntimeException("获取分类信息出现异常，上级分类ID=【" + catId + "】");
        }
        return categories.get(0);
    }

    @Override
    public int insert(CategoryType.CategoryEnum categoryEnum, Category category) {

        EtpDataScopeUtils.setDataScope(category);// 20250519 lyd etp数据权限相关字段

        // 设置 ancestors
        StringBuilder sqlBuilder = new StringBuilder();
        if (category.getParentId() != null && category.getParentId() != 0) {
            try {
                Category cat = selectById(categoryEnum, category.getParentId());
                category.setAncestors(cat.getAncestors() == null ? "0," + cat.getCatId() : cat.getAncestors() + "," + cat.getCatId());
            } catch (RuntimeException e) {
                throw new RuntimeException("获取上级分类信息出现异常，上级分类ID=【" + category.getParentId() + "】");
            }
        } else {
            category.setAncestors("0");
        }

        sqlBuilder.delete(0, sqlBuilder.length());
        sqlBuilder.append("insert into ").append(CategoryType.getTableName(categoryEnum))
                .append("(").append(insertColumns).append(") values (")
                .append(IdUtil.nextId()).append(",'")   //cat_id
                .append(category.getName()).append("',")  //name
                .append(category.getParentId() == null ? 0L : category.getParentId()).append(",'") //parent_id
                .append(category.getAncestors()).append("',")  //ancestors
                .append(category.getSort())//sort
                .append(", 0, '") //del_flag
                .append(SecurityUtils.getUsername())   //create_by
                .append("', null, '', null, ''")// create_time, update_by, update_time, remark
                // 增加数据权限控制字段 20250519 lyd create_user_id,department_id,tenant_id,pub_flag
                .append(", ").append(category.getCreateUserId())
                .append(", ").append(category.getDepartmentId())
                .append(", '").append(category.getTenantId()).append("'")
                .append(", '").append(category.getPubFlag()).append("'")

                .append(")");  //结尾
        return categoryMapper.insertCatgory(sqlBuilder.toString());
    }

    @Override
    public int deleteById(CategoryType.CategoryEnum categoryEnum, Long catId) {
        String del = "delete from " +CategoryType.getTableName(categoryEnum)+" where cat_id=" + catId;
        return categoryMapper.deleteCategory(del);
    }

    @Override
    public int deleteByIds(CategoryType.CategoryEnum categoryEnum, List<Long> catIds) {
        if (catIds == null || catIds.size() == 0) {
            return 0;
        }

        StringBuilder delBuilder = new StringBuilder("delete from " +CategoryType.getTableName(categoryEnum)+" where cat_id in (");
        for (Long catId : catIds) {
            delBuilder.append(catId).append(",");
        }
        delBuilder.deleteCharAt(delBuilder.length()-1).append(")");

        return categoryMapper.deleteCategory(delBuilder.toString());
    }

    @Override
    public int updateCategory(CategoryType.CategoryEnum categoryEnum, Category category) {
        // TODO: 屏蔽以下代码，不允许更改父分类 START
//        if (category.getParentId() != null && category.getParentId() != 0) {
//            try {
//                Category cat = selectById(categoryEnum, category.getParentId());
//                category.setAncestors(cat.getAncestors() == null?"0,"+cat.getCatId():cat.getAncestors() + "," + cat.getCatId());
//            }catch (RuntimeException e) {
//                throw new RuntimeException("获取上级分类信息出现异常，上级分类ID=【" + category.getParentId() + "】");
//            }
//        }else {
//            category.setAncestors("0");
//        }

        if (category.getParentId() != null) {
            throw new RuntimeException("不允许更改分类节点所属的父级分类");
        }
        //  TODO: 屏蔽以上代码，不允许更改父分类 END

        StringBuilder updateBuilder = new StringBuilder("update ");
        updateBuilder.append(CategoryType.getTableName(categoryEnum)).append(" set name='").append(category.getName())
                .append("'");

//        if (category.getParentId() != null) {
//            updateBuilder.append(" , parent_id=").append(category.getParentId());
//        }
//        if (category.getAncestors() != null) {
//            updateBuilder.append(" , ancestors='").append(category.getAncestors()).append("'");
//        }
        if (category.getSort() != null) {
            updateBuilder.append(" , sort=").append(category.getSort());
        }
        if (category.getDelFlag() != null) {
            updateBuilder.append(" , del_flag=").append(category.getDelFlag());
        }
        if (category.getRemark() != null) {
            updateBuilder.append(" , remark='").append(category.getRemark()).append("'");
        }
        updateBuilder.append(" , update_by='").append(SecurityUtils.getUsername()).append("'");
        updateBuilder.append(" , update_time=sysdate()");
        updateBuilder.append(" where cat_id=").append(category.getCatId());
        return categoryMapper.updateCategory(updateBuilder.toString());
    }

    /**
     * 校验是否存在同名的子分类
     *
     * @return true/false，若返回 true 则是存在同名分类
     */
    @Override
    public boolean checkNameExists(CategoryType.CategoryEnum categoryEnum, Long catId, Long parentId, String name) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select ").append(selectColumns).append(" from ").append(CategoryType.getTableName(categoryEnum))
                .append(" where name='").append(name).append("'");
        if (parentId == null || parentId == 0L) {
            sqlBuilder.append(" and (parent_id is null || parent_id = 0)");
        }else {
            sqlBuilder.append(" and parent_id = ").append(parentId);
        }

        if (catId != null) {
            sqlBuilder.append(" and cat_id!=").append(catId);
        }

        return categoryMapper.selectCategory(sqlBuilder.toString()).size() > 0;
    }

    public boolean hasChild(CategoryType.CategoryEnum categoryEnum, List<Long> catIds) {
        StringBuilder selectBuilder = new StringBuilder("select ");
        selectBuilder.append(selectColumns).append(" from ").append(CategoryType.getTableName(categoryEnum))
                .append(" where parent_id in (");
        for (Long catId : catIds) {
            selectBuilder.append(catId).append(",");
        }
        selectBuilder.deleteCharAt(selectBuilder.length()-1).append(") ").append(" LIMIT 1");
        return categoryMapper.selectCategory(selectBuilder.toString()).size() > 0;
    }

    @Override
    public List<TreeSelect> buildCategoryTreeSelect(List<Category> categories)
    {
        List<CategoryVO> categoryTree = buildCategoryTree(categories);
        return categoryTree.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 生成返回给前端的分类树
     *
     * @param categories
     * @return
     */
    @Override
    public List<CategoryVO> buildCategoryTree(List<Category> categories) {
        List<CategoryVO> voList = categories.stream().map(category -> BeanUtils.map(category, CategoryVO.class)).collect(Collectors.toList());
        return buildCategoryTree1(voList);
    }


    public List<CategoryVO> buildCategoryTree1(List<CategoryVO> categoryVOS)
    {
        List<CategoryVO> returnList = new ArrayList<CategoryVO>();
        List<Long> tempList = new ArrayList<Long>();
        for (CategoryVO questionCategory : categoryVOS)
        {
            tempList.add(questionCategory.getCatId());
        }
        for (Iterator<CategoryVO> iterator = categoryVOS.iterator(); iterator.hasNext();)
        {
            CategoryVO questionCategory = (CategoryVO) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(questionCategory.getParentId()))
            {
                recursionFn(categoryVOS, questionCategory);
                returnList.add(BeanUtils.map(questionCategory, CategoryVO.class));
            }
        }
        if (returnList.isEmpty())
        {
            returnList = categoryVOS;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<CategoryVO> list, CategoryVO t)
    {
        // 得到子节点列表
        List<CategoryVO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CategoryVO tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<CategoryVO> getChildList(List<CategoryVO> list, CategoryVO t)
    {
        List<CategoryVO> tlist = new ArrayList<CategoryVO>();
        Iterator<CategoryVO> it = list.iterator();
        while (it.hasNext()) {
            CategoryVO n = (CategoryVO) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getCatId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<CategoryVO> list, CategoryVO t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }


}
