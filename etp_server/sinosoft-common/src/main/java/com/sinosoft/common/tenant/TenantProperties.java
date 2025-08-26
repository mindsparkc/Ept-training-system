package com.sinosoft.common.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 白名单配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 是否开启租户模式
     */
    private Boolean enable = true;

    /**
     * 多租户字段名称
     */
    private String column = "tenant_id";

    /**
     * 需要排除的多租户的表
     */
    private List<String> exclusionTable = Arrays.asList("sys_user", "sys_config","sys_dict_type","sys_dict_data"
    ,"sys_job");

}