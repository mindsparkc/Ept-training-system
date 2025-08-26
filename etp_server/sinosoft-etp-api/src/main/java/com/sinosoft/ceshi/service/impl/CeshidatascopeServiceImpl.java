package com.sinosoft.ceshi.service.impl;

import java.util.List;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.ServletUtils;
import org.springframework.stereotype.Service;
import com.sinosoft.ceshi.mapper.CeshidatascopeMapper;
import com.sinosoft.ceshi.domain.Ceshidatascope;
import com.sinosoft.ceshi.service.ICeshidatascopeService;

import javax.annotation.Resource;

/**
 * 数据权限校验Service业务层处理
 *
 * @author sinosoft
 * @date 2022-10-25
 */
@Service
public class CeshidatascopeServiceImpl implements ICeshidatascopeService
{
    @Resource
    private CeshidatascopeMapper ceshidatascopeMapper;

    /**
     * 查询数据权限校验
     *
     * @param name 数据权限校验ID
     * @return 数据权限校验
     */
    @Override
    public Ceshidatascope selectCeshidatascopeById(String name)
    {
        return ceshidatascopeMapper.selectCeshidatascopeById(name);
    }

    /**
     * 查询数据权限校验列表
     *
     * @param ceshidatascope 数据权限校验
     * @return 数据权限校验
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "d")
    public List<Ceshidatascope> selectCeshidatascopeList(Ceshidatascope ceshidatascope)
    {
        Object sss = ServletUtils.getRequest().getAttribute("sss");
        System.out.println(sss);
        return ceshidatascopeMapper.selectCeshidatascopeList(ceshidatascope);
    }

    /**
     * 查询数据权限校验列表
     *
     * @param names 需要查询的数据权限校验ID
     * @return 数据权限校验集合
     */
    public List<Ceshidatascope> selectCeshidatascopeByIds(String[] names) {
        return ceshidatascopeMapper.selectCeshidatascopeByIds(names);
    }

    /**
     * 新增数据权限校验
     *
     * @param ceshidatascope 数据权限校验
     * @return 结果
     */
    @Override
    public int insertCeshidatascope(Ceshidatascope ceshidatascope)
    {
        return ceshidatascopeMapper.insertCeshidatascope(ceshidatascope);
    }

    /**
     * 修改数据权限校验
     *
     * @param ceshidatascope 数据权限校验
     * @return 结果
     */
    @Override
    public int updateCeshidatascope(Ceshidatascope ceshidatascope)
    {
        return ceshidatascopeMapper.updateCeshidatascope(ceshidatascope);
    }

    /**
     * 批量删除数据权限校验
     *
     * @param names 需要删除的数据权限校验ID
     * @return 结果
     */
    @Override
    public int deleteCeshidatascopeByIds(String[] names)
    {
        return ceshidatascopeMapper.deleteCeshidatascopeByIds(names);
    }

    /**
     * 删除数据权限校验信息
     *
     * @param name 数据权限校验ID
     * @return 结果
     */
    @Override
    public int deleteCeshidatascopeById(String name)
    {
        return ceshidatascopeMapper.deleteCeshidatascopeById(name);
    }
}
