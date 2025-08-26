package com.sinosoft.etp.common.config;

import com.sinosoft.etp.common.message.ListenerMessage;
import com.sinosoft.etp.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 86151
 */
@Configuration
public class RedisStreamConfig {
    /** 监听类 */
    private final ListenerMessage streamListener;
    private final RedisUtil redisUtil;

    private String redisStreamName = "stream";
    private String group = "mygroup";

    @Autowired
    public RedisStreamConfig(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
        this.streamListener = new ListenerMessage(this.redisUtil);
    }

    @Bean
    public List<Subscription> subscription(RedisConnectionFactory factory){
        List<Subscription> resultList = new ArrayList<>();
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
            initStream(redisStreamName,group);
            StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer = StreamMessageListenerContainer.create(factory, options);
            Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from(group, this.getClass().getName()),
                    StreamOffset.create(redisStreamName, ReadOffset.lastConsumed()), streamListener);
            resultList.add(subscription);
            listenerContainer.start();
        return resultList;
    }

    private void initStream(String key, String group){
        boolean hasKey = redisUtil.hasKey(key);
        if(!hasKey){
            Map<String,Object> map = new HashMap<>();
            map.put("field","value");
            RecordId recordId = redisUtil.addStream(key, map);
            redisUtil.addGroup(key,group);
            //将初始化的值删除掉
            redisUtil.delField(key,recordId.getValue());
            redisUtil.trimStream(key, 10L);
            System.out.println("stream:{}-group:{} initialize success");
            System.out.println(key);
            System.out.println(group);
        }
    }
}
