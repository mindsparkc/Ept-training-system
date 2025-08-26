package com.sinosoft.etp.common.controller;

import cn.jkingtools.utils.BeanUtils;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.etp.common.constant.StreamConstant;
import com.sinosoft.etp.common.domain.UserActive;
import com.sinosoft.etp.common.utils.RedisUtil;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 点击事件及页面驻留事件
 *
 * @author wanglijie
 */
@Controller
@RequestMapping("/active")
public class UserActivityLogController {

    @Resource
    private RedisUtil redisUtil;

    @PostMapping
    public AjaxResult UserActiveLog(UserActive userActive) {
        Map<String, Object> message = BeanUtils.map(userActive, Map.class);
        RecordId recordId = redisUtil.addStream(StreamConstant.MESSAGE_KEY, message);
        return AjaxResult.success();
    }
}
