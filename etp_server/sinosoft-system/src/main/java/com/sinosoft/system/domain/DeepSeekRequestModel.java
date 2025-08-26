package com.sinosoft.system.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author： bgl
 * @Date： 2025/4/16 9:34
 * @Describe：
 */

@Data
@Builder
public class DeepSeekRequestModel
{
    /**
     * 所用DeepSeek模型
     */
    private String model;
    private List<Message> messages;
    private Map<String, String> response_format ;

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }

}
