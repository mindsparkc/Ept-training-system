package com.sinosoft.etp.up.subject.mapper;

import com.sinosoft.etp.up.subject.domain.TSubjectCompany;

import java.util.List;


/**
 * 题库企业关联Mapper接口
 *
 *
 * @date 2021-11-12
 */
public interface TSubjectCompanyMapper
{
    /**
     * 查询题库企业关联
     *
     * @param id 题库企业关联ID
     * @return 题库企业关联
     */
    public TSubjectCompany selectTSubjectCompanyById(Long id);

    /**
     * 查询题库企业关联列表
     *
     * @param tSubjectCompany 题库企业关联
     * @return 题库企业关联集合
     */
    public List<TSubjectCompany> selectTSubjectCompanyList(TSubjectCompany tSubjectCompany);

    /**
     * 新增题库企业关联
     *
     * @param tSubjectCompany 题库企业关联
     * @return 结果
     */
    public int insertTSubjectCompany(TSubjectCompany tSubjectCompany);

    /**
     * 修改题库企业关联
     *
     * @param tSubjectCompany 题库企业关联
     * @return 结果
     */
    public int updateTSubjectCompany(TSubjectCompany tSubjectCompany);

    /**
     * 删除题库企业关联
     *
     * @param id 题库企业关联ID
     * @return 结果
     */
    public int deleteTSubjectCompanyById(Long id);

    /**
     * 批量删除题库企业关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTSubjectCompanyByIds(Long[] ids);

    /**
     * 根据题库id删除题库企业关联
     *
     * @param tkid 题库企业关联ID
     * @return 结果
     */
    public int deleteSubjectCompanyByTkid(Long tkid);
}
