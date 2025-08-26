package com.sinosoft.etp.cp.certificate.service;

import com.sinosoft.etp.cp.certificate.domain.TCertificateWithCp;

import java.util.List;

/**
 * 证书管理Service接口
 *
 *
 * @date 2022-02-07
 */
public interface ITCertificateWithCpService
{
    /**
     * 查询证书管理
     *
     * @param id 证书管理ID
     * @return 证书管理
     */
    public TCertificateWithCp selectTCertificateById(Long id);

    /**
     * 查询证书管理列表
     *
     * @param tCertificate 证书管理
     * @return 证书管理集合
     */
    public List<TCertificateWithCp> selectTCertificateList(TCertificateWithCp tCertificate);

    /**
     * 新增证书管理
     *
     * @param tCertificate 证书管理
     * @return 结果
     */
    public int insertTCertificate(TCertificateWithCp tCertificate);

    /**
     * 修改证书管理
     *
     * @param tCertificate 证书管理
     * @return 结果
     */
    public int updateTCertificate(TCertificateWithCp tCertificate);

    /**
     * 批量删除证书管理
     *
     * @param ids 需要删除的证书管理ID
     * @return 结果
     */
    public int deleteTCertificateByIds(Long[] ids);

    /**
     * 删除证书管理信息
     *
     * @param id 证书管理ID
     * @return 结果
     */
    public int deleteTCertificateById(Long id);
}
