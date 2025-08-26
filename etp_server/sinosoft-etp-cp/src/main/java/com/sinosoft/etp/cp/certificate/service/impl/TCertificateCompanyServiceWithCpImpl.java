package com.sinosoft.etp.cp.certificate.service.impl;

import com.sinosoft.etp.cp.certificate.domain.TCertificateCompanyWithCp;
import com.sinosoft.etp.cp.certificate.mapper.TCertificateCompanyWithCpMapper;
import com.sinosoft.etp.cp.certificate.service.ITCertificateCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 证书企业关联Service业务层处理
 *
 *
 * @date 2022-02-08
 */
@Service
public class TCertificateCompanyServiceWithCpImpl implements ITCertificateCompanyWithCpService
{
    @Autowired
    private TCertificateCompanyWithCpMapper tCertificateCompanyMapper;

    /**
     * 查询证书企业关联
     *
     * @param id 证书企业关联ID
     * @return 证书企业关联
     */
    @Override
    public TCertificateCompanyWithCp selectTCertificateCompanyById(Long id)
    {
        return tCertificateCompanyMapper.selectTCertificateCompanyById(id);
    }

    /**
     * 查询证书企业关联列表
     *
     * @param tCertificateCompany 证书企业关联
     * @return 证书企业关联
     */
    @Override
    public List<TCertificateCompanyWithCp> selectTCertificateCompanyList(TCertificateCompanyWithCp tCertificateCompany)
    {
        return tCertificateCompanyMapper.selectTCertificateCompanyList(tCertificateCompany);
    }

    /**
     * 新增证书企业关联
     *
     * @param tCertificateCompany 证书企业关联
     * @return 结果
     */
    @Override
    public int insertTCertificateCompany(TCertificateCompanyWithCp tCertificateCompany)
    {
        return tCertificateCompanyMapper.insertTCertificateCompany(tCertificateCompany);
    }

    /**
     * 修改证书企业关联
     *
     * @param tCertificateCompany 证书企业关联
     * @return 结果
     */
    @Override
    public int updateTCertificateCompany(TCertificateCompanyWithCp tCertificateCompany)
    {
        return tCertificateCompanyMapper.updateTCertificateCompany(tCertificateCompany);
    }

    /**
     * 批量删除证书企业关联
     *
     * @param ids 需要删除的证书企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTCertificateCompanyByIds(Long[] ids)
    {
        return tCertificateCompanyMapper.deleteTCertificateCompanyByIds(ids);
    }

    /**
     * 删除证书企业关联信息
     *
     * @param id 证书企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTCertificateCompanyById(Long id)
    {
        return tCertificateCompanyMapper.deleteTCertificateCompanyById(id);
    }
}
