package com.sinosoft.etp.cp.certificate.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.certificate.domain.TCertificateWithCp;
import com.sinosoft.etp.cp.certificate.mapper.TCertificateCompanyWithCpMapper;
import com.sinosoft.etp.cp.certificate.mapper.TCertificateTagWithCpMapper;
import com.sinosoft.etp.cp.certificate.mapper.TCertificateWithCpMapper;
import com.sinosoft.etp.cp.certificate.service.ITCertificateWithCpService;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 证书管理Service业务层处理
 *
 *
 * @date 2022-02-07
 */
@Service
public class TCertificateServiceWithCpImpl implements ITCertificateWithCpService
{
    @Resource
    private TCertificateWithCpMapper tCertificateMapper;

    @Resource
    private TCertificateCompanyWithCpMapper tCertificateCompanyMapper;

    @Resource
    private ITTagWithCpService itTagService;

    @Resource
    private TCertificateTagWithCpMapper tCertificateTagMapper;

    /**
     * 查询证书管理
     *
     * @param id 证书管理ID
     * @return 证书管理
     */
    @Override
    public TCertificateWithCp selectTCertificateById(Long id)
    {
        return tCertificateMapper.selectTCertificateById(id);
    }

    /**
     * 查询证书管理列表
     *
     * @param tCertificate 证书管理
     * @return 证书管理
     */
    @Override
    @DataScope(deptAlias = "d") // bug 部门关联已删除
    @EtpDataScope(alias = "tc")
    public List<TCertificateWithCp> selectTCertificateList(TCertificateWithCp tCertificate)
    {
        return tCertificateMapper.selectTCertificateList(tCertificate);
    }

    /**
     * 新增证书管理
     *
     * @param tCertificate 证书管理
     * @return 结果
     */
    @Override
    public int insertTCertificate(TCertificateWithCp tCertificate)
    {
        EtpDataScopeUtils.setDataScope(tCertificate);// 20250520 lyd etp数据权限相关字段
        //插入新增证书数据
        tCertificate.setCreateTime(DateUtils.getNowDate());
        tCertificate.setUpdateTime(DateUtils.getNowDate());
        tCertificate.setCreateBy(SecurityUtils.getUsername());
        int result = tCertificateMapper.insertTCertificate(tCertificate);
        //插入证书企业关联数据
        //TCertificateCompanyWithCp tCertificateCompany = new TCertificateCompanyWithCp();
        //tCertificateCompany.setZsid(tCertificate.getId());
        //tCertificateCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tCertificateCompanyMapper.insertTCertificateCompany(tCertificateCompany);

        //插入证书标签关联数据
        if(tCertificate.getParams().get("tagStr") != null){
            String tagStr = tCertificate.getParams().get("tagStr").toString();
            itTagService.commpanyInsertTag(tagStr);
            itTagService.insertRelateTag(tCertificate.getId(),tagStr,"ZS");
        }
        return result;
    }

    /**
     * 修改证书管理
     *
     * @param tCertificate 证书管理
     * @return 结果
     */
    @Override
    public int updateTCertificate(TCertificateWithCp tCertificate)
    {
        //删除证书标签关联关系
        itTagService.deleteRelateTag(tCertificate.getId(),"ZS");
        //修改证书信息
        tCertificate.setUpdateTime(DateUtils.getNowDate());
        tCertificate.setUpdateBy(SecurityUtils.getUsername());
        int row = tCertificateMapper.updateTCertificate(tCertificate);
        //新增修改后的证书标签关联信息
        if(tCertificate.getParams().get("tagStr") != null){
            String tagStr = tCertificate.getParams().get("tagStr").toString();
            itTagService.commpanyInsertTag(tagStr);
            itTagService.insertRelateTag(tCertificate.getId(),tagStr,"ZS");
        }
        return row;
    }

    /**
     * 批量删除证书管理
     *
     * @param ids 需要删除的证书管理ID
     * @return 结果
     */
    @Override
    public int deleteTCertificateByIds(Long[] ids)
    {
        //批量删除证书企业关联信息
        //tCertificateCompanyMapper.deleteTCertificateCompanyByIds(ids);
        //批量删除证书标签关联信息
        tCertificateTagMapper.deleteTCertificateTagByIds(ids);
        //批量逻辑删除证书信息
        int row = tCertificateMapper.deleteTCertificateByIds(ids);
        return row;
    }

    /**
     * 删除证书管理信息
     *
     * @param id 证书管理ID
     * @return 结果
     */
    @Override
    public int deleteTCertificateById(Long id)
    {
        //删除证书企业关联信息
        //tCertificateCompanyMapper.deleteTCertificateCompanyById(id);
        //删除证书标签关联信息
        itTagService.deleteRelateTag(id,"ZS");
        //逻辑删除证书信息
        int row = tCertificateMapper.deleteTCertificateById(id);
        return row;
    }
}
