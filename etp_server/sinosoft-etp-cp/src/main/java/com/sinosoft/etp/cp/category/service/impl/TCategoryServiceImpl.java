package com.sinosoft.etp.cp.category.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.category.domain.TCategory;
import com.sinosoft.etp.cp.category.mapper.TCategoryMapper;
import com.sinosoft.etp.cp.category.service.ITCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 分类树Service业务层处理
 *
 *
 * @date 2022-05-11
 */
@Service
public class TCategoryServiceImpl implements ITCategoryService
{
    @Autowired
    private TCategoryMapper tCategoryMapper;

    /**
     * 查询分类树
     *
     * @param id 分类树ID
     * @return 分类树
     */
    @Override
    public TCategory selectTCategoryById(Long id)
    {
        return tCategoryMapper.selectTCategoryById(id);
    }

    /**
     * 查询分类树列表
     *
     * @param tCategory 分类树
     * @return 分类树
     */
    @Override
    public List<TCategory> selectTCategoryList(TCategory tCategory)
    {
        return tCategoryMapper.selectTCategoryList(tCategory);
    }

    /**
     * 新增分类树
     *
     * @param tCategory 分类树
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public int insertTCategory(TCategory tCategory)
    {
        tCategory.setCreateTime(DateUtils.getNowDate());
        tCategory.setCreateBy(SecurityUtils.getUsername());
        //tCategory.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //如果父节点是null
        if(null == tCategory.getParentId() || tCategory.getParentId()==1){
            tCategory.setParentId(0L);
            tCategory.setAncestors("0");
        }else{
            TCategory info = tCategoryMapper.selectTCategoryById(tCategory.getParentId());
            if(info != null) {
                tCategory.setAncestors(info.getAncestors()+"," + tCategory.getParentId());
            }else {
                tCategory.setAncestors("0," + tCategory.getParentId());
            }

        }

        return tCategoryMapper.insertTCategory(tCategory);
    }

    /**
     * 修改分类树
     *
     * @param tCategory 分类树
     * @return 结果
     */
    @Override
    public int updateTCategory(TCategory tCategory)
    {
        tCategory.setUpdateTime(DateUtils.getNowDate());
        return tCategoryMapper.updateTCategory(tCategory);
    }

    /**
     * 批量删除分类树
     *
     * @param ids 需要删除的分类树ID
     * @return 结果
     */
    @Override
    public int deleteTCategoryByIds(Long[] ids)
    {
        return tCategoryMapper.deleteTCategoryByIds(ids);
    }

    /**
     * 删除分类树信息
     *
     * @param id 分类树ID
     * @return 结果
     */
    @Override
    public int deleteTCategoryById(Long id)
    {
        return tCategoryMapper.deleteTCategoryById(id);
    }


    @Override
    public String checkCategoryNameUnique(TCategory tCategory) {

        Long id = StringUtils.isNull(tCategory.getId()) ? -1L : tCategory.getId();
        if(null == tCategory.getParentId()){
            tCategory.setParentId(0L);
        }
        TCategory info = tCategoryMapper.checkCategoryNameUnique(tCategory.getName(), tCategory.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            //已经有了
            return "1";
        }
        return "0";
    }

    /**
     * 构建前端所需要分类树结构
     *
     * @param datas 分类列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TCategory> buildCategoryTreeSelect(List<TCategory> datas)
    {
        List<TCategory> trees = buildCategoryTree(datas);
        return trees;
    }

    @Override
    public boolean hasChildByCategoryId(Long id) {
        int result =  tCategoryMapper.hasChildByCategoryId(id);
        return result > 0 ? true : false;
    }

    @Override
    public boolean checkCategoryExistKJ(Long id) {
        int result =  tCategoryMapper.checkCategoryExistKJ(id);
        return result > 0 ? true : false;
    }

    @Override
    public boolean checkCategoryExistKC(Long id) {
        int result =  tCategoryMapper.checkCategoryExistKC(id);
        return result > 0 ? true : false;
    }

    @Override
    public boolean checkCategoryExistTM(Long id) {
        int result =  tCategoryMapper.checkCategoryExistTM(id);
        return result > 0 ? true : false;
    }

    @Override
    public boolean checkCategoryExistSJ(Long id) {
        int result =  tCategoryMapper.checkCategoryExistSJ(id);
        return result > 0 ? true : false;
    }

    @Override
    public boolean checkCategoryExistKS(Long id) {
        int result =  tCategoryMapper.checkCategoryExistKS(id);
        return result > 0 ? true : false;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param datas 列表
     * @return 树结构列表
     */
    public List<TCategory> buildCategoryTree(List<TCategory> datas)
    {
        List<TCategory> returnList = new ArrayList<TCategory>();
        List<Long> tempList = new ArrayList<Long>();
        for (TCategory category : datas) {
            tempList.add(category.getId());
        }
        for (Iterator<TCategory> iterator = datas.iterator(); iterator.hasNext();) {
            TCategory data = (TCategory) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(data.getParentId()) || data.getParentId()==1L) {
                if (data.getId() != 1L) {
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
    private void recursionFn(List<TCategory> list, TCategory t)
    {
        // 得到子节点列表
        List<TCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TCategory tChild : childList)
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
    private boolean hasChild(List<TCategory> list, TCategory t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    /**
     * 得到子节点列表
     */
    private List<TCategory> getChildList(List<TCategory> list, TCategory t)
    {
        List<TCategory> tlist = new ArrayList<TCategory>();
        Iterator<TCategory> it = list.iterator();
        while (it.hasNext())
        {
            TCategory n = (TCategory) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }


}
