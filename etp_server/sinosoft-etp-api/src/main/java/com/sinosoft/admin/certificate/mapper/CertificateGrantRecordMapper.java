package com.sinosoft.admin.certificate.mapper;


import com.sinosoft.admin.certificate.domain.CertificateGrantRecord;

import java.util.List;

/**
 * 电子证书颁发记录Mapper接口
 *
 *
 * @date 2022-07-20
 */
public interface CertificateGrantRecordMapper
{
    /**
     * 查询电子证书颁发记录
     *
     * @param recordId 电子证书颁发记录ID
     * @return 电子证书颁发记录
     */
    public CertificateGrantRecord selectCertificateGrantRecordById(Long recordId);

    /**
     * 查询电子证书颁发记录列表
     *
     * @param certificateGrantRecord 电子证书颁发记录
     * @return 电子证书颁发记录集合
     */
    public List<CertificateGrantRecord> selectCertificateGrantRecordList(CertificateGrantRecord certificateGrantRecord);

    /**
     * 新增电子证书颁发记录
     *
     * @param certificateGrantRecord 电子证书颁发记录
     * @return 结果
     */
    public int insertCertificateGrantRecord(CertificateGrantRecord certificateGrantRecord);

    /**
     * 修改电子证书颁发记录
     *
     * @param certificateGrantRecord 电子证书颁发记录
     * @return 结果
     */
    public int updateCertificateGrantRecord(CertificateGrantRecord certificateGrantRecord);

    /**
     * 删除电子证书颁发记录
     *
     * @param recordId 电子证书颁发记录ID
     * @return 结果
     */
    public int deleteCertificateGrantRecordById(Long recordId);

    /**
     * 批量删除电子证书颁发记录
     *
     * @param recordIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCertificateGrantRecordByIds(Long[] recordIds);

    public int submitPublish(List<CertificateGrantRecord> list);
    public int updateRecords(List<CertificateGrantRecord> list);
}
