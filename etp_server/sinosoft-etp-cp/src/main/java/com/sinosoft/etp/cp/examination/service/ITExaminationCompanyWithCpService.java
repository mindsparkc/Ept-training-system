package com.sinosoft.etp.cp.examination.service;

import com.sinosoft.etp.cp.examination.domain.TExaminationCompanyWithCp;

import java.util.List;

/**
 * 考试企业关联Service接口
 *
 *
 * @date 2021-12-28
 */
public interface ITExaminationCompanyWithCpService
{
    /**
     * 查询考试企业关联
     *
     * @param id 考试企业关联ID
     * @return 考试企业关联
     */
    public TExaminationCompanyWithCp selectTExaminationCompanyById(Long id);

    /**
     * 查询考试企业关联列表
     *
     * @param tExaminationCompany 考试企业关联
     * @return 考试企业关联集合
     */
    public List<TExaminationCompanyWithCp> selectTExaminationCompanyList(TExaminationCompanyWithCp tExaminationCompany);

    /**
     * 新增考试企业关联
     *
     * @param tExaminationCompany 考试企业关联
     * @return 结果
     */
    public int insertTExaminationCompany(TExaminationCompanyWithCp tExaminationCompany);

    /**
     * 修改考试企业关联
     *
     * @param tExaminationCompany 考试企业关联
     * @return 结果
     */
    public int updateTExaminationCompany(TExaminationCompanyWithCp tExaminationCompany);

    /**
     * 批量删除考试企业关联
     *
     * @param ids 需要删除的考试企业关联ID
     * @return 结果
     */
    public int deleteTExaminationCompanyByIds(Long[] ids);

    /**
     * 删除考试企业关联信息
     *
     * @param id 考试企业关联ID
     * @return 结果
     */
    public int deleteTExaminationCompanyById(Long id);
}
