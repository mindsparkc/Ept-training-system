package com.sinosoft.admin.knowledge.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.knowledge.domain.KnowledgeCategory;
import com.sinosoft.admin.knowledge.mapper.KnowledgeCategoryMapper;
import com.sinosoft.admin.knowledge.service.IKnowledgeCategoryService;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 知识点分类树Service业务层处理
 *
 *
 * @date 2022-08-10
 */
@Service
public class KnowledgeCategoryServiceImpl implements IKnowledgeCategoryService
{
    @Resource
    private KnowledgeCategoryMapper knowledgeCategoryMapper;

    /**
     * 查询知识点分类树
     *
     * @param catId 知识点分类树ID
     * @return 知识点分类树
     */
    @Override
    public KnowledgeCategory selectKnowledgeCategoryById(Long catId)
    {
        return knowledgeCategoryMapper.selectKnowledgeCategoryById(catId);
    }

    /**
     * 查询知识点分类树列表
     *
     * @param knowledgeCategory 知识点分类树
     * @return 知识点分类树
     */
    @Override
    public List<KnowledgeCategory> selectKnowledgeCategoryList(KnowledgeCategory knowledgeCategory)
    {
        return knowledgeCategoryMapper.selectKnowledgeCategoryList(knowledgeCategory);
    }

    /**
     * 查询知识点分类树列表
     *
     * @param catIds 需要查询的知识点分类树ID
     * @return 知识点分类树集合
     */
    @Override
    public List<KnowledgeCategory> selectKnowledgeCategoryByIds(Long[] catIds) {
        return knowledgeCategoryMapper.selectKnowledgeCategoryByIds(catIds);
    }

    /**
     * 新增知识点分类树
     *
     * @param knowledgeCategory 知识点分类树
     * @return 结果
     */
    @Override
    public int insertKnowledgeCategory(KnowledgeCategory knowledgeCategory)
    {
        knowledgeCategory.setCatId(IdUtil.nextId());
        knowledgeCategory.setCreateTime(DateUtils.getNowDate());
        knowledgeCategory.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        return knowledgeCategoryMapper.insertKnowledgeCategory(knowledgeCategory);
    }

    /**
     * 修改知识点分类树
     *
     * @param knowledgeCategory 知识点分类树
     * @return 结果
     */
    @Override
    public int updateKnowledgeCategory(KnowledgeCategory knowledgeCategory)
    {
        knowledgeCategory.setUpdateTime(DateUtils.getNowDate());
        knowledgeCategory.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        return knowledgeCategoryMapper.updateKnowledgeCategory(knowledgeCategory);
    }

    /**
     * 批量删除知识点分类树
     *
     * @param catIds 需要删除的知识点分类树ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCategoryByIds(Long[] catIds)
    {
        return knowledgeCategoryMapper.deleteKnowledgeCategoryByIds(catIds);
    }

    /**
     * 删除知识点分类树信息
     *
     * @param catId 知识点分类树ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeCategoryById(Long catId)
    {
        return knowledgeCategoryMapper.deleteKnowledgeCategoryById(catId);
    }

    /**
     * 构建树结构
     * @param datas 分类列表
     * @return
     */
    @Override
    public List<KnowledgeCategory> buildCategoryTreeSelect(List<KnowledgeCategory> datas) {
        List<KnowledgeCategory> list = buildCategoryTree(datas);
        return list;
    }

    /**
     * 检验分类名称在父类中是否存在
     * @param parentId 父类ID
     * @param name 分类名称
     * @return
     */
    @Override
    public int checkName(Long parentId,String name) {
        return knowledgeCategoryMapper.checkName(parentId,name);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param datas 列表
     * @return 树结构列表
     */
    public List<KnowledgeCategory> buildCategoryTree(List<KnowledgeCategory> datas)
    {
        List<KnowledgeCategory> returnList = new ArrayList<KnowledgeCategory>();
        List<Long> tempList = new ArrayList<Long>();
        for (KnowledgeCategory category : datas) {
            tempList.add(category.getCatId());
        }
        for (Iterator<KnowledgeCategory> iterator = datas.iterator(); iterator.hasNext();) {
            KnowledgeCategory data = (KnowledgeCategory) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(data.getParentId()) || data.getParentId()==1L) {
                if (data.getCatId() != 1L) {
                    recursionFn(datas, data);
                }
                returnList.add(data);

            }
        }
        if (returnList.isEmpty()) {
            returnList = datas;
        }
        return returnList;
    }
    /**
     * 递归列表
     */
    private void recursionFn(List<KnowledgeCategory> list, KnowledgeCategory t)
    {
        // 得到子节点列表
        List<KnowledgeCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (KnowledgeCategory tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<KnowledgeCategory> list, KnowledgeCategory t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    /**
     * 得到子节点列表
     */
    private List<KnowledgeCategory> getChildList(List<KnowledgeCategory> list, KnowledgeCategory t)
    {
        List<KnowledgeCategory> tlist = new ArrayList<KnowledgeCategory>();
        Iterator<KnowledgeCategory> it = list.iterator();
        while (it.hasNext())
        {
            KnowledgeCategory n = (KnowledgeCategory) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getCatId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }
}
