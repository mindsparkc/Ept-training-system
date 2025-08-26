package com.sinosoft.etp.cp.task.service;

import com.sinosoft.etp.cp.task.domain.TTaskCompanyWithCp;

import java.util.List;

/**
 * 【任务与企业关联】Service接口
 *
 *
 * @date 2021-12-08
 */
public interface ITTaskCompanyWithCpService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TTaskCompanyWithCp selectTTaskCompanyById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTaskCompany 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TTaskCompanyWithCp> selectTTaskCompanyList(TTaskCompanyWithCp tTaskCompany);

    /**
     * 新增【请填写功能名称】
     *
     * @param tTaskCompany 【请填写功能名称】
     * @return 结果
     */
    public int insertTTaskCompany(TTaskCompanyWithCp tTaskCompany);

    /**
     * 修改【请填写功能名称】
     *
     * @param tTaskCompany 【请填写功能名称】
     * @return 结果
     */
    public int updateTTaskCompany(TTaskCompanyWithCp tTaskCompany);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTTaskCompanyByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTTaskCompanyById(Long id);
}
