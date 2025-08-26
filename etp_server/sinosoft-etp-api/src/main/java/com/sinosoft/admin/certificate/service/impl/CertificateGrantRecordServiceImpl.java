package com.sinosoft.admin.certificate.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.admin.certificate.domain.CertificateGrantRecord;
import com.sinosoft.admin.certificate.mapper.CertificateGrantRecordMapper;
import com.sinosoft.admin.certificate.mapper.CertificateTemplateMapper;
import com.sinosoft.admin.certificate.service.ICertificateGrantRecordService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 电子证书颁发记录Service业务层处理
 *
 *
 * @date 2022-07-20
 */
@Service
public class CertificateGrantRecordServiceImpl implements ICertificateGrantRecordService
{
    @Resource
    private CertificateGrantRecordMapper certificateGrantRecordMapper;
    @Resource
    private CertificateTemplateMapper certificateTemplateMapper;

    /**
     * 查询电子证书颁发记录
     *
     * @param recordId 电子证书颁发记录ID
     * @return 电子证书颁发记录
     */
    @Override
    public CertificateGrantRecord selectCertificateGrantRecordById(Long recordId)
    {
        return certificateGrantRecordMapper.selectCertificateGrantRecordById(recordId);
    }

    /**
     * 查询电子证书颁发记录列表
     *
     * @param certificateGrantRecord 电子证书颁发记录
     * @return 电子证书颁发记录
     */
    @EtpDataScope(alias = "r")
    @Override
    public List<CertificateGrantRecord> selectCertificateGrantRecordList(CertificateGrantRecord certificateGrantRecord)
    {
        return certificateGrantRecordMapper.selectCertificateGrantRecordList(certificateGrantRecord);
    }

    /**
     * 新增电子证书颁发记录
     *
     * @param certificateGrantRecord 电子证书颁发记录
     * @return 结果
     */
    @Override
    public int insertCertificateGrantRecord(CertificateGrantRecord certificateGrantRecord)
    {
        EtpDataScopeUtils.setDataScope(certificateGrantRecord);// 20250520 lyd etp数据权限相关字段
        certificateGrantRecord.setCreateTime(DateUtils.getNowDate());
        return certificateGrantRecordMapper.insertCertificateGrantRecord(certificateGrantRecord);
    }

    /**
     * 修改电子证书颁发记录
     *
     * @param certificateGrantRecord 电子证书颁发记录
     * @return 结果
     */
    @Override
    public int updateCertificateGrantRecord(CertificateGrantRecord certificateGrantRecord)
    {
        certificateGrantRecord.setUpdateTime(DateUtils.getNowDate());
        return certificateGrantRecordMapper.updateCertificateGrantRecord(certificateGrantRecord);
    }

    /**
     * 批量删除电子证书颁发记录
     *
     * @param recordIds 需要删除的电子证书颁发记录ID
     * @return 结果
     */
    @Override
    public int deleteCertificateGrantRecordByIds(Long[] recordIds)
    {
        return certificateGrantRecordMapper.deleteCertificateGrantRecordByIds(recordIds);
    }

    /**
     * 删除电子证书颁发记录信息
     *
     * @param recordId 电子证书颁发记录ID
     * @return 结果
     */
    @Override
    public int deleteCertificateGrantRecordById(Long recordId)
    {
        return certificateGrantRecordMapper.deleteCertificateGrantRecordById(recordId);
    }


}
