package com.sinosoft.etp.cp.subject.service;

import com.sinosoft.etp.cp.subject.domain.TSubjectCompanyWithCp;

import java.util.List;

/**
 * 题库企业关联Service接口
 *
 *
 * @date 2021-11-12
 */
public interface ITSubjectCompanyWithCpService
{
    /**
     * 查询题库企业关联
     *
     * @param id 题库企业关联ID
     * @return 题库企业关联
     */
    public TSubjectCompanyWithCp selectTSubjectCompanyById(Long id);

    /**
     * 查询题库企业关联列表
     *
     * @param tSubjectCompany 题库企业关联
     * @return 题库企业关联集合
     */
    public List<TSubjectCompanyWithCp> selectTSubjectCompanyList(TSubjectCompanyWithCp tSubjectCompany);

    /**
     * 新增题库企业关联
     *
     * @param tSubjectCompany 题库企业关联
     * @return 结果
     */
    public int insertTSubjectCompany(TSubjectCompanyWithCp tSubjectCompany);

    /**
     * 修改题库企业关联
     *
     * @param tSubjectCompany 题库企业关联
     * @return 结果
     */
    public int updateTSubjectCompany(TSubjectCompanyWithCp tSubjectCompany);

    /**
     * 批量删除题库企业关联
     *
     * @param ids 需要删除的题库企业关联ID
     * @return 结果
     */
    public int deleteTSubjectCompanyByIds(Long[] ids);

    /**
     * 删除题库企业关联信息
     *
     * @param id 题库企业关联ID
     * @return 结果
     */
    public int deleteTSubjectCompanyById(Long id);
}
