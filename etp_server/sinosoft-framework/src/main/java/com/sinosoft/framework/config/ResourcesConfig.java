package com.sinosoft.framework.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.common.config.MainConfig;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.framework.interceptor.PlatformInterceptor;
import com.sinosoft.framework.interceptor.RepeatSubmitInterceptor;
import com.sinosoft.framework.interceptor.StoragePathInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * 通用配置
 *
 *
 */
@EnableWebMvc
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Resource
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Resource
    private StoragePathInterceptor storagePathInterceptor;

    @Resource
    private PlatformInterceptor platformInterceptor;

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + MainConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(platformInterceptor).addPathPatterns("/**");
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        registry.addInterceptor(storagePathInterceptor).addPathPatterns("/**");
    }

    /**
     * web跨域访问配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许证书
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS));
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 解决controller返回字符串中文乱码问题
////        for (HttpMessageConverter<?> converter : converters) {
////            if (converter instanceof StringHttpMessageConverter) {
////                ((StringHttpMessageConverter)converter).setDefaultCharset(StandardCharsets.UTF_8);
////            }
////        }
//        super.configureMessageConverters(converters);
//            converters.add(responseBodyConverter());
//    }
//
//    @Bean
//    public HttpMessageConverter<String> responseBodyConverter() {
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(
//                Charset.forName("UTF-8"));
//        return converter;
//    }

    //    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 解决controller返回字符串中文乱码问题
//        for (HttpMessageConverter<?> converter : converters) {
//            if (converter instanceof StringHttpMessageConverter) {
//                ((StringHttpMessageConverter)converter).setDefaultCharset(StandardCharsets.UTF_8);
//            }
//        }
//    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        // UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        // config.addAllowedOrigin("*");
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 对接口配置跨域设置
        // source.registerCorsConfiguration("/**", config);
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);

    }
}
