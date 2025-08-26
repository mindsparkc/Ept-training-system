package com.sinosoft.web.controller.system;


import com.google.gson.Gson;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.redis.RedisCache;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.domain.DeepSeekRequestModel;

import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author： bgl
 * @Date： 2025/4/16 10:12
 * @Describe：
 */
@RestController
@RequestMapping("/system/deepseek")
public class DeepseekController extends BaseController {

    @Autowired
    private RedisCache redisCache;
    @Value("${deepseek.apiKey}")
    private String apiKey;


    private static final String API_URL = "https://api.deepseek.com/chat/completions";
//    private static String API_KEY = "sk-3d00127cc4554500a5e97095d465bfda";
    private List<DeepSeekRequestModel.Message> conversationHistory = new ArrayList<>();
    private final Gson gson = new Gson();
    private final OkHttpClient deepSeekHttpClient ;
    private static String userId = null;
    public DeepseekController() {


        this.deepSeekHttpClient = new OkHttpClient.Builder()
                .readTimeout(300, TimeUnit.SECONDS) // 设置读取超时时间为300秒
                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间为30秒
                .writeTimeout(300, TimeUnit.SECONDS) // 设置写入超时时间为300秒
                .build();
    }
    @GetMapping(path = "/stream")
    public AjaxResult streamChat(@RequestParam String message) {
        this.userId = String.valueOf(SecurityUtils.getLoginUser().getUser().getUserId());

        try {
            // 调用DeepSeek流式API
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(createRequestBody(message))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            try (Response response =  executeRequest(request)) {
                // 记录响应状态码
                int statusCode = response.code();
                if(statusCode != 200){
                    return AjaxResult.error("请求失败：错误码"+statusCode);
                }
                System.out.println("Response Status Code: " + statusCode);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.body().byteStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                String responseBody = result.toString();
                //存储到redis
                if (result != null) {
                    conversationHistory =redisCache.getCacheList(userId);
                    conversationHistory.add(DeepSeekRequestModel.Message.builder().role("assistant").content(responseBody).build());
                    redisCache.setCacheList(userId, conversationHistory);
                }
                return AjaxResult.success(responseBody);
            }

        } catch (Exception e) {
            return AjaxResult.error("请求报错："+e.toString());
        }
    }
    private RequestBody createRequestBody(String message) {
        if(null!= redisCache.getCacheList(userId)){
            conversationHistory =redisCache.getCacheList(userId);
        }

        // 添加用户消息到对话历史
        conversationHistory.add(DeepSeekRequestModel.Message.builder().role("user").content(message).build());
        redisCache.setCacheList(userId, conversationHistory);
        redisCache.expire(userId, 3600, TimeUnit.SECONDS);
        Map<String, String> json_type = new HashMap<>();
        json_type.put("type", "json_object");
        // 构建请求体
        DeepSeekRequestModel requestBody = DeepSeekRequestModel.builder()
                .model("deepseek-chat")
                .messages(conversationHistory)
                .build();

        // 构建请求体json
        String jsonBody = gson.toJson(requestBody);
        return RequestBody.create(jsonBody, MediaType.parse("application/json"));
    }

    /**
     *
     * 在请求失败时增加重试机制，以提高系统的可靠性。
     * @param request
     * @return
     * @throws IOException
     */
    private Response executeRequest(Request request) throws IOException {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                return deepSeekHttpClient.newCall(request).execute();
            } catch (IOException e) {
                if (attempt == maxRetries - 1) {
                    throw e;
                }
                System.out.println("Attempt " + (attempt + 1) + " failed, retrying...");
                try {
                    Thread.sleep(1000); // 等待1秒后重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Thread interrupted", ie);
                }
            }
        }
        return null;
    }
    /**
     * 清空redis缓存
     */
    @PostMapping(path = "/reset")
    public AjaxResult deleteRedisCache(){
        System.out.println("清空对话");
        redisCache.deleteObject(String.valueOf(SecurityUtils.getLoginUser().getUser().getUserId()));
        return AjaxResult.success();
    }

}
