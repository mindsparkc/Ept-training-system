package  com.sinosoft.admin.certificate.task;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.admin.certificate.domain.CertificateGrantRecord;
import com.sinosoft.admin.certificate.domain.CertificateTemplate;
import com.sinosoft.admin.certificate.mapper.CertificateGrantRecordMapper;
import com.sinosoft.admin.certificate.mapper.CertificateTemplateMapper;
import com.sinosoft.common.config.MainConfig;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.utils.DocxTemplateTractorUtils;
import com.sinosoft.system.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证书转化 定时任务
 */
@Component("certficataAward")
public class CertficataAward {
    private static final Logger logger = LoggerFactory.getLogger("sys-user");
    @Resource
    private CertificateTemplateMapper certificateTemplateMapper;
    @Resource
    private CertificateGrantRecordMapper certificateGrantRecordMapper;
    @Resource
    private SysUserMapper sysUserMapper;


    public Integer wordTransPic(){

        //查询未转换的word

        CertificateGrantRecord record = new CertificateGrantRecord();
        record.setStatus("0");

        List<CertificateGrantRecord> list = certificateGrantRecordMapper.selectCertificateGrantRecordList(record);

        List<CertificateGrantRecord> records = new ArrayList<>();
        list.forEach(data -> {
            //查询模板信息
            CertificateTemplate template = certificateTemplateMapper.selectCertificateTemplateById(data.getTempId());
            CertificateGrantRecord obj = new CertificateGrantRecord();
            obj.setRecordId(data.getRecordId());

            //模板文字替换 上传之后返回的路径
            String wordUrl = templateTrans(template,data);
            logger.info("----定时任务------转换url:"+wordUrl);
            String docxUrl = wordUrl.replace(MainConfig.getProfile(),"{storage.path.url}"+ Constants.RESOURCE_PREFIX);
            obj.setCertFile(docxUrl);
//            obj.setCertFile("wordUrl");
            //word 转图片
            String result = DocxTemplateTractorUtils.wordTransPic(wordUrl);
            obj.setPreviewFile(result);
            records.add(obj);
        });

        if(null !=records && records.size()>0){
            return certificateGrantRecordMapper.updateRecords(records);
        }
        return null;
    }
    /**
     * 生成预览证书图片
     * expiretime 有效时间
     * certId 证书编号
     * fileUrl
     */
    public String templateTrans(CertificateTemplate certificateTemplate, CertificateGrantRecord record){
        //模板地址
//        String fileUrl = certificateTemplate.getTempFile();
//        fileUrl = "D:\\cret_template.docx";
//        String filePath = fileUrl.substring(fileUrl.indexOf(Constants.RESOURCE_PREFIX));
//        filePath = filePath.replace(Constants.RESOURCE_PREFIX, MainConfig.getProfile());
        String filePath = certificateTemplate.getTempFile();
        filePath = filePath.replace("{storage.path.url}"+Constants.RESOURCE_PREFIX,MainConfig.getProfile());
        System.out.println("获取模板文件路径==================："+filePath);

        Map<String,String> map = new HashMap<>();
        map.put("${CERT_NAME}",certificateTemplate.getCertName());
        map.put("${CERT_AUTHORITY}",certificateTemplate.getAuthority());
        //********************************缺证书编号 *********************
        //查询用户信息
        SysUser user = sysUserMapper.selectUserById(record.getUserId());
        map.put("${USER_NAME}",user.getUserName());
        map.put("${CERT_NO}",record.getCertId());
        map.put("${START_TIME}", DateUtils.getDate());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (record.getExpireTime() != null) {
            map.put("${CERT_EXPIRETIME}",sdf1.format(record.getExpireTime()));
        }else {
            map.put("${CERT_EXPIRETIME}","永久有效");
        }


        //模板文字替换 上传之后返回的路径
        String viewUrl = DocxTemplateTractorUtils.build(filePath,map);
        System.out.println("view=模板文字替换之后保存的====:"+viewUrl);

        return viewUrl;
    }



}
