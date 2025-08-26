package com.sinosoft.framework.config.properties;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * druid 配置属性
 *
 *
 */
@Configuration
public class DruidProperties
{
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    /** 加密 */
    @Value("${spring.datasource.druid.filter.config.enable}")
    private boolean configFilterEnabled;

    @Value("${spring.datasource.druid.connectProperties.config.decrypt}")
    private String decryptEnabled;

    @Value("${spring.datasource.druid.connectProperties.config.decrypt.key}")
    private String decryptKey;

    public DruidDataSource dataSource(DruidDataSource datasource)
    {
        /** 配置初始化大小、最小、最大 */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** 配置获取连接等待超时的时间 */
        datasource.setMaxWait(maxWait);

        /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
         */
        datasource.setValidationQuery(validationQuery);
        /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);

        if (configFilterEnabled) {
            try {
                /* 启用数据库密码解密 */
                datasource.setFilters("config");
                Properties properties = new Properties();
                properties.put("config.decrypt", decryptEnabled);
                properties.put("config.decrypt.key", decryptKey);
                datasource.setConnectProperties(properties);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return datasource;
    }

    /**
     * 如果忘记密码，可以在此解密
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String password = "Xytn$L3t&";
        String[] arr = ConfigTools.genKeyPair(512);
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + ConfigTools.encrypt(arr[0], password));

        //password1=password
        String password1 = ConfigTools.decrypt(
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALizFQBZnHsPpj31Z8yOrrRL4R1jtrOnuEdW1Vt2vSKR/qRMqXjVeirWf8PT7srD33T8VuXzdwZpyhWVACDL1oUCAwEAAQ==",
                "gkYlljNHKe0/4z7bbJxD7v/txWJIFbiGWwsIPo176Q7fG0UjcSizNxuRUI2ll27ZPQf2ekiHFptus2/Rc4cmvA=="
        );

        //password2=Xytn$L3t&
        String password2 = ConfigTools.decrypt(
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK6P9upgzADzuUhdPKQ5wIyG6LqsnYlY7i6Dqz7PSY8udDaYMtESUjQpFSL4452AoTH3i54qR7ZqHN1KTvi6FUsCAwEAAQ==",
                "NQpKMxhWcpiMJX2os6Ri0rIDTclG5saU0B+31z50D2AwlOv1KPVGkGQRQ8AHwU64ohimqxfsN2EPqApaTXfrrQ=="
        );
        System.out.println("解密密码：" + password1);
    }
}
