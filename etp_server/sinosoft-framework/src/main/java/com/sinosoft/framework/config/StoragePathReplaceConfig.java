package com.sinosoft.framework.config;

import cn.jkingtools.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 判断替换哪些字段，以避免无效的遍历
 *
 * @author WANGLIJIE
 */
@Configuration
public class StoragePathReplaceConfig {
    @Value("${storage.enabled}")
    private boolean enabled;
    @Value("${storage.fields}")
    private String fields;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public boolean exists(String k) {
        if (StringUtils.isNotBlank(getFields()) && StringUtils.isNotBlank(k))
            return getFields().contains(k);

        return false;
    }
}
