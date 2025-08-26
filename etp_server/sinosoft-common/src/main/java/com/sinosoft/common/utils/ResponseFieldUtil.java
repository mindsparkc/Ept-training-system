package com.sinosoft.common.utils;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.utils.reflect.ReflectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理 @PreProcessField 注解，替换 URL 中的固定字符串
 *
 * @param <T>
 *
 * @author WANGLIJIE
 */
public class ResponseFieldUtil<T> {
    public Class<T> clazz;

    public ResponseFieldUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> response(List<T> list) {
        List<T> l = new ArrayList<>();
        for (T t : list) {
            l.add(response(t));
        }
        return l;
    }

    public T response(T response) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (String.class != f.getType()) continue;
            PreProcessField[] annotationsByType = f.getDeclaredAnnotationsByType(PreProcessField.class);
            if (annotationsByType.length > 0) {
                String value = ReflectUtils.invokeGetter(response, f.getName());
                if (StringUtils.isNotBlank(value))
                    ReflectUtils.invokeSetter(response, f.getName(), value.replace("{storage.path.url}", getUrl()));
            }
        }
        return response;
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
