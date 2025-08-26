package com.sinosoft.etp.cp.certificate.mapper;

import com.sinosoft.etp.cp.certificate.domain.TCertificateTagWithCp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 管理证书标签关联Mapper接口
 *
 *
 * @date 2022-02-08
 */
@Mapper
public interface TCertificateTagWithCpMapper
{
    /**
     * 查询管理证书标签关联
     *
     * @param id 管理证书标签关联ID
     * @return 管理证书标签关联
     */
    public TCertificateTagWithCp selectTCertificateTagById(Long id);

    /**
     * 查询管理证书标签关联列表
     *
     * @param tCertificateTag 管理证书标签关联
     * @return 管理证书标签关联集合
     */
    public List<TCertificateTagWithCp> selectTCertificateTagList(TCertificateTagWithCp tCertificateTag);

    /**
     * 新增管理证书标签关联
     *
     * @param tCertificateTag 管理证书标签关联
     * @return 结果
     */
    public int insertTCertificateTag(TCertificateTagWithCp tCertificateTag);

    /**
     * 修改管理证书标签关联
     *
     * @param tCertificateTag 管理证书标签关联
     * @return 结果
     */
    public int updateTCertificateTag(TCertificateTagWithCp tCertificateTag);

    /**
     * 删除管理证书标签关联
     *
     * @param id 管理证书标签关联ID
     * @return 结果
     */
    public int deleteTCertificateTagById(Long id);

    /**
     * 批量删除管理证书标签关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCertificateTagByIds(Long[] ids);
}
