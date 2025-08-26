package com.sinosoft.common.utils;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.config.MainConfig;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.common.utils.http.HttpUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DocxTemplateTractorUtils {

    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    public static List<String>patternList=new ArrayList();
    //需要处理的节点名称
    static {
        patternList.add("mc:AlternateContent");
        patternList.add("mc:Choice");
        patternList.add("w:drawing");
        patternList.add("wp:anchor");
        patternList.add("a:graphic");
        patternList.add("a:graphicData");
        patternList.add("wps:wsp");
        patternList.add("wps:txbx");
        patternList.add("w:txbxContent");
        patternList.add("w:p");
        patternList.add("w:r");
        patternList.add("w:t");
    }


    /**
     * word 转图片
     * @param viewUrl
     * @return
     */
    public static String wordTransPic(String viewUrl){
        String result = null;
        try {
            System.out.println("============================"+MainConfig.getParsrFileServer()+"/file/picturesPreview");
            String res = HttpUtils.sendGet(MainConfig.getParsrFileServer()+"/file/picturesPreview","filePath="+ URLEncoder.encode(viewUrl,"utf-8") );

            if(res.split(",").length>1){
                result = res.split(",")[1];
                result = result.replace(MainConfig.getProfile(),"{storage.path.url}" + Constants.RESOURCE_PREFIX);
            }
            System.out.println("===================图片地址========="+result);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    /**
     * 模板内容替换
     * @param file 文件
     * @param map 替换内容
    //     * @param exportFile 文件生成路径
     */
    public  static String build(String file, Map<String,String> map){
        XWPFDocument xwpfDocument =null;

        try{
            //替换内容
            InputStream inputStream = new FileInputStream(file);
            xwpfDocument = new XWPFDocument(inputStream);
            replaceText(xwpfDocument,map);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            xwpfDocument.write(byteArrayOutputStream);

            /*byte[] barray = byteArrayOutputStream.toByteArray();
            //byte[] 转 InputStream
            InputStream is = new ByteArrayInputStream(barray);
            //InputStream 转 MultipartFile
            MultipartFile multipartFile = new MockMultipartFile("cret.docx","cret.svg.docx",ContentType.APPLICATION_OCTET_STREAM.toString(), is);

            //文件上传
            String filePath = MainConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, multipartFile);*/
            //生成文件的路径
//            String url = getUrl() + fileName;
//            String url = getUrl() + /profile/;
//            String exportFile =  getUrl() + "/profile/"+ new Date().getTime()+".docx";
            String date = DateUtils.getDate();
            date = date.replace("-","/")+"/";
            String exportFile =  MainConfig.getWordTransPath()+ date+ new Date().getTime()+".docx";
            File desc = new File(exportFile);
            if (!desc.exists())
            {
                if (!desc.getParentFile().exists())
                {
                    desc.getParentFile().mkdirs();
                }
            }
//            String exportFile ="d:\\"+ new Date().getTime()+".docx";
            System.out.println("***************************************"+exportFile);
            OutputStream outputStream = new FileOutputStream(exportFile);
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.close();
            return exportFile;

        }catch (IOException e){
            logger.error("错误信息："+e);
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param xwpfDocument
     * @param textMap  需要替换的文本入参
     */
    public static void  replaceText(XWPFDocument xwpfDocument, Map<String, String> textMap){
        List<XWPFParagraph> paras=xwpfDocument.getParagraphs();
        for (XWPFParagraph para : paras) {
            for(XmlObject object:para.getCTP().getRArray())
            {
                XmlCursor cursor = object.newCursor();
                eachchild(cursor, 0,textMap);
            }
        }
    }

    public static void eachchild(XmlCursor cursor,int start,Map<String, String>map) {
        //预计子节点个数应该是小于10个节点
        for(int i=0;i<10;i++)
        {
            //如果可以移动到子节点i
            if(cursor.toChild(i)) {
                //如果移动到达的子节点正好是按照顺序是需要的节点 则继续前往下一层
                if(cursor.getDomNode().getNodeName().equals(patternList.get(start))) {
                    if(start==patternList.size()-1) {
                        String reString=cursor.getTextValue();
                        for(String e:map.keySet()) {
                            if(reString.contains(e)) {
                                //    执行替换
                                reString=reString.replace(e, map.get(e));
                            }
                        }
                        //bingo  设置替换节点内容
                        cursor.setTextValue(reString);
                    }
                    //继续下一层  遍历
                    eachchild(cursor,start+1,map);
                }else {
                    cursor.toParent();
                }
            }
        }

        //  此处很重要，如果命中或者未命中都需要 遍历其他节点
        cursor.toParent();
    }

    public static String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
