package com.sinosoft.etp.cp.certificate.service.impl;

import com.sinosoft.etp.cp.certificate.domain.TCertificateTagWithCp;
import com.sinosoft.etp.cp.certificate.mapper.TCertificateTagWithCpMapper;
import com.sinosoft.etp.cp.certificate.service.ITCertificateTagWithCpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理证书标签关联Service业务层处理
 *
 *
 * @date 2022-02-08
 */
@Service
public class TCertificateTagServiceWithCpImpl implements ITCertificateTagWithCpService
{
    @Resource
    private TCertificateTagWithCpMapper tCertificateTagMapper;

    /**
     * 查询管理证书标签关联
     *
     * @param id 管理证书标签关联ID
     * @return 管理证书标签关联
     */
    @Override
    public TCertificateTagWithCp selectTCertificateTagById(Long id)
    {
        return tCertificateTagMapper.selectTCertificateTagById(id);
    }

    /**
     * 查询管理证书标签关联列表
     *
     * @param tCertificateTag 管理证书标签关联
     * @return 管理证书标签关联
     */
    @Override
    public List<TCertificateTagWithCp> selectTCertificateTagList(TCertificateTagWithCp tCertificateTag)
    {
        return tCertificateTagMapper.selectTCertificateTagList(tCertificateTag);
    }

    /**
     * 新增管理证书标签关联
     *
     * @param tCertificateTag 管理证书标签关联
     * @return 结果
     */
    @Override
    public int insertTCertificateTag(TCertificateTagWithCp tCertificateTag)
    {
        return tCertificateTagMapper.insertTCertificateTag(tCertificateTag);
    }

    /**
     * 修改管理证书标签关联
     *
     * @param tCertificateTag 管理证书标签关联
     * @return 结果
     */
    @Override
    public int updateTCertificateTag(TCertificateTagWithCp tCertificateTag)
    {
        return tCertificateTagMapper.updateTCertificateTag(tCertificateTag);
    }

    /**
     * 批量删除管理证书标签关联
     *
     * @param ids 需要删除的管理证书标签关联ID
     * @return 结果
     */
    @Override
    public int deleteTCertificateTagByIds(Long[] ids)
    {
        return tCertificateTagMapper.deleteTCertificateTagByIds(ids);
    }

    /**
     * 删除管理证书标签关联信息
     *
     * @param id 管理证书标签关联ID
     * @return 结果
     */
    @Override
    public int deleteTCertificateTagById(Long id)
    {
        return tCertificateTagMapper.deleteTCertificateTagById(id);
    }
}
