package com.sinosoft.ceshi.mapper;

import java.util.List;
import com.sinosoft.ceshi.domain.Ceshidatascope;

/**
 * 数据权限校验Mapper接口
 * 
 * @author sinosoft
 * @date 2022-10-25
 */
public interface CeshidatascopeMapper 
{
    /**
     * 查询数据权限校验
     * 
     * @param name 数据权限校验ID
     * @return 数据权限校验
     */
    public Ceshidatascope selectCeshidatascopeById(String name);

    /**
     * 查询数据权限校验列表
     * 
     * @param ceshidatascope 数据权限校验
     * @return 数据权限校验集合
     */
    public List<Ceshidatascope> selectCeshidatascopeList(Ceshidatascope ceshidatascope);

    /**
     * 批量查询数据权限校验列表
     *
     * @param names 需要查询的数据权限校验ID
     * @return 数据权限校验集合
     */
    public List<Ceshidatascope> selectCeshidatascopeByIds(String[] names);

    /**
     * 新增数据权限校验
     * 
     * @param ceshidatascope 数据权限校验
     * @return 结果
     */
    public int insertCeshidatascope(Ceshidatascope ceshidatascope);

    /**
     * 修改数据权限校验
     * 
     * @param ceshidatascope 数据权限校验
     * @return 结果
     */
    public int updateCeshidatascope(Ceshidatascope ceshidatascope);

    /**
     * 删除数据权限校验
     * 
     * @param name 数据权限校验ID
     * @return 结果
     */
    public int deleteCeshidatascopeById(String name);

    /**
     * 批量删除数据权限校验
     * 
     * @param names 需要删除的数据ID
     * @return 结果
     */
    public int deleteCeshidatascopeByIds(String[] names);
}
