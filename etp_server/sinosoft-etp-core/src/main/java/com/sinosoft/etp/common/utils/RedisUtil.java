package com.sinosoft.etp.common.utils;


import com.sinosoft.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @Description 添加stream消息
 */
@Component
public class RedisUtil extends RedisCache{
    //private final RedisTemplate<String,String> redisTemplate;
    /** 点击课程 **/
    public final String ACT_KC = "CLI_CURR";
    /** 点击课件 **/
    public final String ACT_KJ = "CLI_COUR";
    /** 点击任务 **/
    public final String ACT_RW = "CLI_TASK";
    /** 点击考试 **/
    public final String ACT_KS = "CLI_EXAM";
    /** 点击交卷 **/
    public final String ACT_JJ = "CLI_EXAM_END";
    /** 点击数据筛选 **/
    public final String ACT_TAG = "CLI_TAG";
    /** 驻留事件（学习） **/
    public final String ACT_ING_XX = "ING_STUDY";


    @Autowired
    public RedisUtil(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * XADD 添加stream消息
     * @param key stream对应的key
     * @param message 要存储的消息数据
     */
    public RecordId addStream(String key, Map<String,Object> message){
        RecordId add = redisTemplate.opsForStream().add(key, message);
        System.out.println("添加成功："+add);
        return add;
    }

    public void addHash(String key,Map map) { redisTemplate.opsForHash().putAll(key,map); }

    public void addValue(String key,String value) { redisTemplate.opsForSet().add(key,value); }

    public void addGroup(String key, String groupName){
        redisTemplate.opsForStream().createGroup(key,groupName);
    }

    public void delField(String key, String fieldId){
        redisTemplate.opsForStream().delete(key,fieldId);
    }

    /**
     * 读取所有stream消息
     * @param key
     * @return
     */
    public List<MapRecord<String,Object,Object>> getAllStream(String key){
        List<MapRecord<String, Object, Object>> range = redisTemplate.opsForStream().range(key, Range.open("-", "+"));
        if(range == null){
            return null;
        }
        for(MapRecord<String,Object,Object> mapRecord : range){
            redisTemplate.opsForStream().delete(key,mapRecord.getId());
        }
        return range;
    }

    public void getStream(String key){
        List<MapRecord<String, Object, Object>> read = redisTemplate.opsForStream().read(StreamReadOptions.empty().block(Duration.ofMillis(1000*30)).count(2), StreamOffset.latest(key));
        System.out.println(read);
    }

    public void getStreamByGroup(String key, String groupName,String consumerName){
        List<MapRecord<String, Object, Object>> read = redisTemplate.opsForStream().read(Consumer.from(groupName, consumerName), StreamReadOptions.empty(), StreamOffset.create(key,ReadOffset.lastConsumed()));
        System.out.println(read);

    }

    public boolean hasKey(String key){
        Boolean aBoolean = redisTemplate.hasKey(key);
        return aBoolean==null?false:aBoolean;
    }

    public Object getMapValue(String key, String mKey){
        return redisTemplate.opsForHash().get(key,mKey);
    }

    public Map getHash(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    public void delStream(String key){
        redisTemplate.delete(key);
    }

    public void trimStream(String key,Long index){
        redisTemplate.opsForStream().trim(key, index);
    }

    public Long getStreamLen(String key){
        return redisTemplate.opsForStream().size(key);
    }

    public Set<String> getListKey(String prefix) {
        return redisTemplate.keys(prefix.concat("*"));
    }
}
