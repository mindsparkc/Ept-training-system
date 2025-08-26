package com.sinosoft.etp.cp.certificate.mapper;

import com.sinosoft.etp.cp.certificate.domain.TCertificateCompanyWithCp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 证书企业关联Mapper接口
 *
 *
 * @date 2022-02-08
 */
@Mapper
public interface TCertificateCompanyWithCpMapper
{
    /**
     * 查询证书企业关联
     *
     * @param id 证书企业关联ID
     * @return 证书企业关联
     */
    public TCertificateCompanyWithCp selectTCertificateCompanyById(Long id);

    /**
     * 查询证书企业关联列表
     *
     * @param tCertificateCompany 证书企业关联
     * @return 证书企业关联集合
     */
    public List<TCertificateCompanyWithCp> selectTCertificateCompanyList(TCertificateCompanyWithCp tCertificateCompany);

    /**
     * 新增证书企业关联
     *
     * @param tCertificateCompany 证书企业关联
     * @return 结果
     */
    public int insertTCertificateCompany(TCertificateCompanyWithCp tCertificateCompany);

    /**
     * 修改证书企业关联
     *
     * @param tCertificateCompany 证书企业关联
     * @return 结果
     */
    public int updateTCertificateCompany(TCertificateCompanyWithCp tCertificateCompany);

    /**
     * 删除证书企业关联
     *
     * @param id 证书企业关联ID
     * @return 结果
     */
    public int deleteTCertificateCompanyById(Long id);

    /**
     * 批量删除证书企业关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCertificateCompanyByIds(Long[] ids);
}
