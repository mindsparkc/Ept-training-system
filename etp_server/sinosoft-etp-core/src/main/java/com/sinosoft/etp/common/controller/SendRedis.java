package com.sinosoft.etp.common.controller;

import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 86151
 */
@Controller
@RequestMapping("/etp/redisStream")
@ResponseBody
public class SendRedis {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 前端埋点
     * @return
     */
    @GetMapping("/sendRedis")
    public AjaxResult addStream(String streamName,String eventType,String id){
        Map<String,Object> message = new HashMap<>();
        String userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
        message.put("eventType",eventType);
        message.put("id",id);
        message.put("userId",userId);
        String msg = redisUtil.addStream(streamName, message).getValue();
        return AjaxResult.success(msg);
    }


    /**
     * 前端埋点,单独处理驻留事件
     * @param streamName stream Key
     * @param eventType 事件类型
     * @param id id
     * @param pt 课件入口类型
     * @param pid 课件入口id
     * @param offset pdf记录
     * @return
     */
    @GetMapping("/sendRedisByIng")
    public AjaxResult addStreamByIng(String streamName, String eventType, String id, String pt, String pid,String offset,String lx){
        Map<String,Object> message = new HashMap<>();
        String userId = SecurityUtils.getLoginUser().getUser().getUserId().toString();
        System.out.println("用户id="+userId);
        message.put("eventType",eventType);
        message.put("id",id);
        message.put("userId",userId);
        message.put("pt",pt);
        message.put("pid",pid);
        message.put("offset",offset);
        String msg = redisUtil.addStream(streamName, message).getValue();
        return AjaxResult.success(msg);
    }

}
