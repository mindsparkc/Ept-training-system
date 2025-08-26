package com.sinosoft.uploadfile.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Content-Type 与文件扩展名对照表
 *
 * @author wanglijie
 */
public class ContentTypeUtils {
    private static Map<String, String> map = new HashMap<>();

    /**
     * 根据文件后缀获取
     *
     * @param fileExt
     * @return
     */
    public static String getContentType(String fileExt) {
        fileExt = fileExt.contains(".")? fileExt : "."+fileExt;

        String result = map.get(fileExt.toLowerCase());
        if (result == null) {
            result = map.get("anno");
        }

        return result;
    }

    static{
        map.put("anno","application/octet-stream");
        map.put(".tif","image/tiff");
        map.put("0.001","application/x-001");
        map.put("0.301","application/x-301");
        map.put("0.323","text/h323");
        map.put("0.906","application/x-906");
        map.put("0.907","drawing/907");
        map.put(".a11","application/x-a11");
        map.put(".acp","audio/x-mei-aac");
        map.put(".ai","application/postscript");
        map.put(".aif","audio/aiff");
        map.put(".aifc","audio/aiff");
        map.put(".aiff","audio/aiff");
        map.put(".anv","application/x-anv");
        map.put(".asa","text/asa");
        map.put(".asf","video/x-ms-asf");
        map.put(".asp","text/asp");
        map.put(".asx","video/x-ms-asf");
        map.put(".au","audio/basic");
        map.put(".avi","video/avi");
        map.put(".awf","application/vnd.adobe.workflow");
        map.put(".biz","text/xml");
        map.put(".bmp","application/x-bmp");
        map.put(".bot","application/x-bot");
        map.put(".c4t","application/x-c4t");
        map.put(".c90","application/x-c90");
        map.put(".cal","application/x-cals");
        map.put(".cat","application/vnd.ms-pki.seccat");
        map.put(".cdf","application/x-netcdf");
        map.put(".cdr","application/x-cdr");
        map.put(".cel","application/x-cel");
        map.put(".cer","application/x-x509-ca-cert");
        map.put(".cg4","application/x-g4");
        map.put(".cgm","application/x-cgm");
        map.put(".cit","application/x-cit");
        map.put(".class","java/");
        map.put(".cml","text/xml");
        map.put(".cmp","application/x-cmp");
        map.put(".odc","text/x-ms-odc");
        map.put(".out","application/x-out");
        map.put(".p10","application/pkcs10");
        map.put(".p12","application/x-pkcs12");
        map.put(".p7b","application/x-pkcs7-certificates");
        map.put(".p7c","application/pkcs7-mime");
        map.put(".p7m","application/pkcs7-mime");
        map.put(".p7r","application/x-pkcs7-certreqresp");
        map.put(".p7s","application/pkcs7-signature");
        map.put(".pc5","application/x-pc5");
        map.put(".pci","application/x-pci");
        map.put(".pcl","application/x-pcl");
        map.put(".pcx","application/x-pcx");
        map.put(".pdf","application/pdf");
        map.put(".pdx","application/vnd.adobe.pdx");
        map.put(".pfx","application/x-pkcs12");
        map.put(".pgl","application/x-pgl");
        map.put(".pic","application/x-pic");
        map.put(".pko","application/vnd.ms-pki.pko");
        map.put(".pl","application/x-perl");
        map.put(".plg","text/html");
        map.put(".pls","audio/scpls");
        map.put(".plt","application/x-plt");
        map.put(".pot","application/vnd.ms-powerpoint");
        map.put(".ppa","application/vnd.ms-powerpoint");
        map.put(".ppm","application/x-ppm");
        map.put(".pps","application/vnd.ms-powerpoint");
        map.put(".ppt","application/vnd.ms-powerpoint");
        map.put(".ppt","application/x-ppt");
        map.put(".pr","application/x-pr");
        map.put(".prf","application/pics-rules");
        map.put(".prn","application/x-prn");
        map.put(".prt","application/x-prt");
        map.put(".ps","application/x-ps");
        map.put(".ps","application/postscript");
        map.put(".ptn","application/x-ptn");
        map.put(".pwz","application/vnd.ms-powerpoint");
        map.put(".r3t","text/vnd.rn-realtext3d");
        map.put(".ra","audio/vnd.rn-realaudio");
        map.put(".ram","audio/x-pn-realaudio");
        map.put(".ras","application/x-ras");
        map.put(".rat","application/rat-file");
        map.put(".rdf","text/xml");
        map.put(".rec","application/vnd.rn-recording");
        map.put(".red","application/x-red");
        map.put(".rgb","application/x-rgb");
        map.put(".rjs","application/vnd.rn-realsystem-rjs");
        map.put(".rjt","application/vnd.rn-realsystem-rjt");
        map.put(".rlc","application/x-rlc");
        map.put(".rle","application/x-rle");
        map.put(".rm","application/vnd.rn-realmedia");
        map.put(".rmf","application/vnd.adobe.rmf");
        map.put(".rmi","audio/mid");
        map.put(".rmj","application/vnd.rn-realsystem-rmj");
        map.put(".rmm","audio/x-pn-realaudio");
        map.put(".rmp","application/vnd.rn-rn_music_package");
        map.put(".rms","application/vnd.rn-realmedia-secure");
        map.put(".rmvb","application/vnd.rn-realmedia-vbr");
        map.put(".rmx","application/vnd.rn-realsystem-rmx");
        map.put(".rnx","application/vnd.rn-realplayer");
        map.put(".rp","image/vnd.rn-realpix");
        map.put(".rpm","audio/x-pn-realaudio-plugin");
        map.put(".rsml","application/vnd.rn-rsml");
        map.put(".wks","application/x-wks");
        map.put(".wm","video/x-ms-wm");
        map.put(".wma","audio/x-ms-wma");
        map.put(".wmd","application/x-ms-wmd");
        map.put(".wmf","application/x-wmf");
        map.put(".wml","text/vnd.wap.wml");
        map.put(".wmv","video/x-ms-wmv");
        map.put(".wmx","video/x-ms-wmx");
        map.put(".wmz","application/x-ms-wmz");
        map.put(".wp6","application/x-wp6");
        map.put(".wpd","application/x-wpd");
        map.put(".wpg","application/x-wpg");
        map.put(".wpl","application/vnd.ms-wpl");
        map.put(".wq1","application/x-wq1");
        map.put(".wr1","application/x-wr1");
        map.put(".wri","application/x-wri");
        map.put(".wrk","application/x-wrk");
        map.put(".ws","application/x-ws");
        map.put(".ws2","application/x-ws");
        map.put(".wsc","text/scriptlet");
        map.put(".wsdl","text/xml");
        map.put(".wvx","video/x-ms-wvx");
        map.put(".xdp","application/vnd.adobe.xdp");
        map.put(".xdr","text/xml");
        map.put(".xfd","application/vnd.adobe.xfd");
        map.put(".xfdf","application/vnd.adobe.xfdf");
        map.put(".xhtml","text/html");
        map.put(".xls","application/vnd.ms-excel");
        map.put(".xls","application/x-xls");
        map.put(".xlw","application/x-xlw");
        map.put(".xml","text/xml");
        map.put(".xpl","audio/scpls");
        map.put(".xq","text/xml");
        map.put(".xql","text/xml");
        map.put(".xquery","text/xml");
        map.put(".xsd","text/xml");
        map.put(".xsl","text/xml");
        map.put(".xslt","text/xml");
        map.put(".xwd","application/x-xwd");
        map.put(".x_b","application/x-x_b");
        map.put(".sis","application/vnd.symbian.install");
        map.put(".sisx","application/vnd.symbian.install");
        map.put(".x_t","application/x-x_t");
        map.put(".ipa","application/vnd.iphone");
        map.put(".apk","application/vnd.android.package-archive");
        map.put(".xap","application/x-silverlight-app");

        map.put(".mp4","video/mp4");
        map.put(".svg","image/svg+xml");
        map.put(".png","image/png");
    }
}
