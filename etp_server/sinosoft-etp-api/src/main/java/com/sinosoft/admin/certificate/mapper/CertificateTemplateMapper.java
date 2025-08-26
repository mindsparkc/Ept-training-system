package com.sinosoft.admin.certificate.mapper;


import com.sinosoft.admin.certificate.domain.CertificateTemplate;

import java.util.List;

/**
 * 电子证书模板Mapper接口
 *
 *
 * @date 2022-07-15
 */
public interface CertificateTemplateMapper
{
    /**
     * 查询电子证书模板
     *
     * @param tempId 电子证书模板ID
     * @return 电子证书模板
     */
    public CertificateTemplate selectCertificateTemplateById(Long tempId);

    /**
     * 查询电子证书模板列表
     *
     * @param certificateTemplate 电子证书模板
     * @return 电子证书模板集合
     */
    public List<CertificateTemplate> selectCertificateTemplateList(CertificateTemplate certificateTemplate);

    /**
     * 新增电子证书模板
     *
     * @param certificateTemplate 电子证书模板
     * @return 结果
     */
    public int insertCertificateTemplate(CertificateTemplate certificateTemplate);

    /**
     * 修改电子证书模板
     *
     * @param certificateTemplate 电子证书模板
     * @return 结果
     */
    public int updateCertificateTemplate(CertificateTemplate certificateTemplate);

    /**
     * 删除电子证书模板
     *
     * @param tempId 电子证书模板ID
     * @return 结果
     */
    public int deleteCertificateTemplateById(Long tempId);

    /**
     * 批量删除电子证书模板
     *
     * @param tempIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCertificateTemplateByIds(Long[] tempIds);
}
