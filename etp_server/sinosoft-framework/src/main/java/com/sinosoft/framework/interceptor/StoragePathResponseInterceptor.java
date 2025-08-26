package com.sinosoft.framework.interceptor;

import com.alibaba.fastjson.JSON;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.annotation.PreProcessFields;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 借助该类重写 URL
 * @author WANGLIJIE
 */
@RestControllerAdvice
public class StoragePathResponseInterceptor implements ResponseBodyAdvice {
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (o instanceof AjaxResult && mediaType.equals(MediaType.APPLICATION_JSON)) {
            AjaxResult object = (AjaxResult)o;
            PreProcessField preProcessField = methodParameter.getMethodAnnotation(PreProcessField.class);
            if (preProcessField != null) {
                object = preProcessField(object, preProcessField.field());
            }else {
                PreProcessFields preProcessFields = methodParameter.getMethodAnnotation(PreProcessFields.class);
                if (preProcessFields != null) {

                    for (PreProcessField field : preProcessFields.value()) {
                        object = preProcessField(object, field.field());
                    }
                    o = object;
                }
            }
        }
        return o;
    }

    private AjaxResult preProcessField(AjaxResult object, String field) {
        Object value = object.get(field);
        if (value instanceof String) {
            object.put(field, ((String)value).replace("{storage.path.url}", serverConfig.getUrl()));
        }
        return object;
    }

    /**
     * 校验是否是 JSON
     *
     * @param o
     * @return
     */
    private Object checkJson(Object o) {
        Object obj = null;
        try {
            obj = JSON.parse((String) o);
        }catch (Exception e) {
            return null;
        }
        return obj;
    }
}
