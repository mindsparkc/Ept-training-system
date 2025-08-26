package com.sinosoft.admin.certificate.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.admin.certificate.domain.CertificateGrantRecord;
import com.sinosoft.admin.certificate.domain.CertificateTemplate;
import com.sinosoft.admin.certificate.mapper.CertificateGrantRecordMapper;
import com.sinosoft.admin.certificate.mapper.CertificateTemplateMapper;
import com.sinosoft.admin.certificate.service.ICertificateTemplateService;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.config.MainConfig;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.common.utils.DocxTemplateTractorUtils;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.framework.config.ServerConfig;
import com.sinosoft.framework.manager.AsyncManager;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电子证书模板Service业务层处理
 *
 *
 * @date 2022-07-15
 */
@Service
public class CertificateTemplateServiceImpl implements ICertificateTemplateService
{
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @Autowired
    private CertificateTemplateMapper certificateTemplateMapper;
    @Autowired
    private CertificateGrantRecordMapper certificateGrantRecordMapper;
    @Resource
    private ServerConfig serverConfig;


    /**
     * 查询电子证书模板
     *
     * @param tempId 电子证书模板ID
     * @return 电子证书模板
     */
    @Override
    public CertificateTemplate selectCertificateTemplateById(Long tempId)
    {
        return certificateTemplateMapper.selectCertificateTemplateById(tempId);
    }

    /**
     * 查询电子证书模板列表
     *
     * @param certificateTemplate 电子证书模板
     * @return 电子证书模板
     */
    @EtpDataScope
    @Override
    public List<CertificateTemplate> selectCertificateTemplateList(CertificateTemplate certificateTemplate)
    {
        return certificateTemplateMapper.selectCertificateTemplateList(certificateTemplate);
    }

    /**
     * 新增电子证书模板
     *
     * @param certificateTemplate 电子证书模板
     * @return 结果
     */
    @Override
    public int insertCertificateTemplate(CertificateTemplate certificateTemplate)
    {
        EtpDataScopeUtils.setDataScope(certificateTemplate);// 20250520 lyd etp数据权限相关字段

        certificateTemplate.setCreateTime(DateUtils.getNowDate());
        certificateTemplate.setDelFlag("0");
        AsyncManager.me().execute(templateTrans(certificateTemplate));
        return certificateTemplateMapper.insertCertificateTemplate(certificateTemplate);
    }

    /**
     * 修改电子证书模板
     *
     * @param certificateTemplate 电子证书模板
     * @return 结果
     */
    @Override
    public int updateCertificateTemplate(CertificateTemplate certificateTemplate)
    {
        certificateTemplate.setUpdateTime(DateUtils.getNowDate());
        certificateTemplate.setStatus("0");
        AsyncManager.me().execute(templateTrans(certificateTemplate));
        return certificateTemplateMapper.updateCertificateTemplate(certificateTemplate);
    }


    /**
     * 上传模板转换
     * @param certificateTemplate
     */
    public TimerTask templateTrans(CertificateTemplate certificateTemplate){
        String url= serverConfig.getUrl();
        System.out.println("url++++++:"+url);
        return new TimerTask(){
            @Override
            public void run(){
                String filePath = certificateTemplate.getTempFile();
                filePath = filePath.replace("{storage.path.url}"+Constants.RESOURCE_PREFIX,MainConfig.getProfile());
                System.out.println("==========文件上传之后的路径替换"+filePath);
                //模板 文字替换 之后文件的地址
                String wordUrl = templateTrans(filePath,certificateTemplate);
                logger.info("----------模板 文字替换 之后文件的地址--------------"+wordUrl);
                //word 转图片
                String result = DocxTemplateTractorUtils.wordTransPic(wordUrl);
                certificateTemplate.setPreviewFile(result);
                certificateTemplate.setStatus("99");
                certificateTemplateMapper.updateCertificateTemplate(certificateTemplate);
            }
        };

    }
    /**
     * 批量删除电子证书模板
     *
     * @param tempIds 需要删除的电子证书模板ID
     * @return 结果
     */
    @Override
    public int deleteCertificateTemplateByIds(Long[] tempIds)
    {
        return certificateTemplateMapper.deleteCertificateTemplateByIds(tempIds);
    }

    /**
     * 删除电子证书模板信息
     * @param tempId 电子证书模板ID
     * @return 结果
     */
    @Override
    public int deleteCertificateTemplateById(Long tempId)
    {
        return certificateTemplateMapper.deleteCertificateTemplateById(tempId);
    }

    /**
     * 发布证书
     * @param tempId
     * @param userIds
     * @return
     */
    public int submitPublish(Long tempId,String userIds[])
    {
        List<CertificateGrantRecord> data = new ArrayList<>();
        Date now = new Date();
        //查询模板信息
        CertificateTemplate template = certificateTemplateMapper.selectCertificateTemplateById(tempId);
        for(String userId:userIds){
            Long user_id = Long.parseLong(userId);
            CertificateGrantRecord record = new CertificateGrantRecord();
            record.setTempId(tempId);
            //证书编号--规则需要定义
            record.setCertId("");
            record.setCertName(template.getCertName());

            Date expireTime = null;
            int year = template.getExpireYear().intValue();
            if(year !=0){//0-永久有效
                expireTime = DateUtils.getNyearsAfter(year);
            }
            record.setExpireTime(expireTime);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

//            //模板链接
//            String tempFile = template.getTempFile();
//            String certFile = templateTrans(tempFile,template,user_id,sdf1.format(expireTime),record.getCertId());
//            //替换文字-生成证书原文件--word类型
//            record.setCertFile(certFile);
//            //证书预览地址--调用word转图片的接口
//            String picUrl = wordTransPic(certFile);
//            if(StringUtils.isNotEmpty(picUrl)){
//                record.setPreviewFile(picUrl.split(",")[1]);
//            }
            //暂存
            record.setStatus("0");
            record.setCertId(String.valueOf(IdUtil.nextId()));
            record.setGrantType(template.getGrantType());
            record.setGrantDisplay(template.getGrantDisplay());
            record.setUserId(user_id);
            record.setCreateBy(SecurityUtils.getUsername());
            record.setUpdateBy(SecurityUtils.getUsername());
            data.add(record);
        }
        return certificateGrantRecordMapper.submitPublish(data);
    }
    public String templateTrans(String fileUrl, CertificateTemplate certificateTemplate) {
//        fileUrl = "D:\\cret_template.docx";
        logger.info("===================开始转换模板======================");
        Map<String, String> map = new HashMap<>();
        map.put("${CERT_NAME}", certificateTemplate.getCertName());
        map.put("${CERT_AUTHORITY}", certificateTemplate.getAuthority());
        //********************************缺证书编号 *********************
        //新增的预览
        map.put("${CERT_NO}", "12345678");
        map.put("${USER_NAME}", "张三");
        map.put("${USER_NAME}", "张三");
        map.put("${START_TIME}", DateUtils.getDate());
        int year = certificateTemplate.getExpireYear().intValue();
        if (year != 0) {//0-永久有效
            Date expire_Time = DateUtils.getNyearsAfter(year);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            map.put("${CERT_EXPIRETIME}", sdf1.format(expire_Time));
        }

        //模板文字替换 上传之后返回的路径
        String viewUrl = DocxTemplateTractorUtils.build(fileUrl, map);
        System.out.println("view=模板文字替换之后保存的====:" + viewUrl);
        logger.info("===================结束转换模板======================："+viewUrl);
        return viewUrl;
    }

}

