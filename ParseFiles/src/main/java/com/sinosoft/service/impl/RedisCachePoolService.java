package com.sinosoft.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.entity.Task;
import com.sinosoft.service.ICachePoolService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class RedisCachePoolService implements ICachePoolService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void addQueueTask(String task) {
        redisTemplate.opsForSet().add(PDF_FILE_POOL_KEY, task);
    }

    /**
     * 将文件从待处理队列拿出来，放入处理中的队列
     * @return
     */
    @Override
    public String getTask() {
        String task = (String) redisTemplate.opsForSet().pop(PDF_FILE_POOL_KEY);
        if (StringUtils.isEmpty(task)) return null;

        Task object = JSONObject.parseObject(task, Task.class);
        redisTemplate.opsForHash().putIfAbsent(PDF_FILE_PARSE_KEY, object.getRequestId(), String.valueOf(new Date().getTime()));
        return task;
    }

    /**
     * 文件处理完成后，移除处理中的队列
     * @param task
     */
    @Override
    public void finishTask(String task) {
        // Task object = JSONObject.parseObject(task, Task.class);
        redisTemplate.opsForHash().delete(PDF_FILE_PARSE_KEY, task);
    }

    /**
     * 判断一个任务在已处理队列中是否存在，若存在则返回其加入队列的时间
     *
     * @param task
     * @return
     */
    @Override
    public String existsTask(String task) {
        // Task object = JSONObject.parseObject(task, Task.class);
        Object o = redisTemplate.opsForHash().get(PDF_FILE_PARSE_KEY, task);
        return o.toString();
    }
}
