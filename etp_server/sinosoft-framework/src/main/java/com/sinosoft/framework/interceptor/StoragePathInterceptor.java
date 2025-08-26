package com.sinosoft.framework.interceptor;

import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.filter.RepeatedlyRequestWrapper;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.framework.config.ServerConfig;
import com.sinosoft.framework.config.StoragePathReplaceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * 处理存储路径
 *
 * @author WANGLIJIE
 */
 @Component
public class StoragePathInterceptor implements HandlerInterceptor {
    @Autowired
    StoragePathReplaceConfig storagePathReplaceConfig;

    @Autowired
    ServerConfig serverConfig;

    /**
     * 进入 Controller 之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (storagePathReplaceConfig.isEnabled()) {
            if (request instanceof RepeatedlyRequestWrapper && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                RepeatedlyRequestWrapper repeatedlyRequestWrapper = (RepeatedlyRequestWrapper) request;
                //String body = repeatedlyRequestWrapper.getBody().replace(serverConfig.getUrl(), "{storage.path.url}")
                //        .replace("/dev-api", "/prod-api").replace("", "");
                // repeatedlyRequestWrapper.setBody(body);
//                Object parse = JSONObject.parse(binaryReader(request));
                Object parse = JSONObject.parse(repeatedlyRequestWrapper.getBody());
                repeatedlyRequestWrapper.setBody(JSONObject.toJSONString(analysisJson(parse, null)));

            }
        }
        return true;
    }

    public static String binaryReader(HttpServletRequest request) throws IOException {
        int len = request.getContentLength();
        if (len > 0) {
                ServletInputStream iii = request.getInputStream();
                byte[] buffer1 = new byte[len];
                iii.read(buffer1, 0, len);

            BufferedReader br = request.getReader();
            String str, wholeStr = "";
            while ((str = br.readLine()) != null) {
                wholeStr += str;
            }



            RepeatedlyRequestWrapper repeatedlyRequestWrapper = (RepeatedlyRequestWrapper) request;
            byte[] buffer = repeatedlyRequestWrapper.getBody().getBytes();

            System.out.println("****************************");
            System.out.println(len);
            System.out.println(buffer1.length);
            System.out.println(buffer.length);
            System.out.println(new String(buffer, StandardCharsets.UTF_8).getBytes().length);
            System.out.println("1_______");
            System.out.println(new String(buffer1, StandardCharsets.UTF_8));
            System.out.println("2_______");
            System.out.println(new String(buffer1, StandardCharsets.ISO_8859_1));
            System.out.println("3_______");
            System.out.println( wholeStr);
            System.out.println("4_______");
            System.out.println(new String(buffer));
            System.out.println("5_______");
            System.out.println(new String(buffer, StandardCharsets.UTF_8));
            System.out.println("********************************");
            //转换成UTF-8格式

            return new String(buffer, StandardCharsets.UTF_8);
        }
        return "";
    }

//    /**
//     * Controller 结束之后调用
//     */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
//            throws Exception {
//        // 判断是否本地调试模式，如是则替换其中的 {storage.path.url} 为 127.0.0.1
////        if (MainConfig.isLocalDev()) {
////            ServletOutputStream outputStream = response.getOutputStream();
////        }
//        super.postHandle(request, response, handler, modelAndView);
//    }

    private Object analysisJson(Object object, String key) {

        Object resultObj = null;
        if (object instanceof JSONArray) {
            // JSONArray 数组 - List
            resultObj = new JSONArray();
            JSONArray jsonArray = (JSONArray)object;
            for (Object o : jsonArray) {
                ((JSONArray)resultObj).add(analysisJson(o, null));
            }
        } else if (object instanceof JSONObject) {
            // JSONObject - Map
            resultObj = new JSONObject();
            JSONObject jsonObject = (JSONObject)object;
            Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                ((JSONObject)resultObj).put(entry.getKey(), analysisJson(entry.getValue(), entry.getKey()));
            }
        } else {
            // JSON 是基本对象
            // System.out.println(key + ":" + object);
            if (object instanceof String) {
                Object o = analysisJson((String) object);
                if (o != null) {
                    resultObj = JSON.toJSONString(analysisJson(o, null));
                }else {
                    // 转换失败
                    if (storagePathReplaceConfig.exists(key)) {
                        if (isResUrl((String) object)) {
                            URL url = null;
                            try {
                                url = new URL((String) object);
                                resultObj = "{storage.path.url}" + url.getPath().replace("/dev-api", "")
                                        .replace("/prod-api", "");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }else {
                            // 如果是富文本，提取 src 的值
                            String url = ResponseFieldUtil.getUrl();
                            if (object instanceof String) {
                                resultObj = ((String) object).replace("/dev-api", "")
                                        .replace("/prod-api", "").replace(url, "{storage.path.url}");
                            }
                        }
                    }else {
                        resultObj = object;
                    }
                }
            }else {
                resultObj = object;
            }

        }
        return resultObj;
    }

    private Object analysisJson(String string) {
        Object object = null;
        try {
            object = JSON.parse(string);
        }catch (JSONException e) {
            return null;
        }
        return object;
    }

    public boolean isResUrl(String str){
        //转换为小写
        str = str.toLowerCase();
        String regex = "^(http|https).*(\\.png|\\.jpg|\\.jpeg|\\.doc|\\.docx|\\.pdf|\\.txt|\\.ppt|\\.pptx|\\.mp4|\\.apk)$";
        return  str.matches(regex);
    }

}
