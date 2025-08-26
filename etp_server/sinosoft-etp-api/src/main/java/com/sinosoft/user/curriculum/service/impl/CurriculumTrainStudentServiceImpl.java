package com.sinosoft.user.curriculum.service.impl;

import cn.jkingtools.utils.ArrayUtils;
import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.redis.RedisCache;
import com.sinosoft.common.exception.CustomException;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.*;
import com.sinosoft.core.enums.*;
import com.sinosoft.user.curriculum.domain.CurriculumTrainStudent;
import com.sinosoft.user.curriculum.mapper.CurriculumTrainStudentMapper;
import com.sinosoft.user.curriculum.service.ICurriculumTrainStudentService;
import com.sinosoft.user.exam.domain.*;
import com.sinosoft.user.exam.exception.UserExamException;
import com.sinosoft.user.exam.mapper.*;
import com.sinosoft.user.exam.service.IUserExamService;
import com.sinosoft.user.exam.task.core.MsgEvent;
import org.apache.commons.beanutils.BeanUtils;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 学员用户查询课程习题集信息
 *
 */
@Service
public class CurriculumTrainStudentServiceImpl implements ICurriculumTrainStudentService {
    private static final Logger log = LoggerFactory.getLogger(CurriculumTrainStudentServiceImpl.class);

    @Resource
    CurriculumTrainStudentMapper curriculumTrainStudentMapper;

    @Override
    public CurriculumTrainStudent selectCTrainInfo(CurriculumTrainStudent curriculumTrainStudent) {
        return curriculumTrainStudentMapper.selectCTrainInfo(curriculumTrainStudent);
    }

    @Override
    public int insertCTrainInfo(CurriculumTrainStudent curriculumTrainStudent) {
        return curriculumTrainStudentMapper.insertCTrainInfo(curriculumTrainStudent);
    }

    @Override
    public int deleteCTrainInfo(CurriculumTrainStudent curriculumTrainStudent) {
        return curriculumTrainStudentMapper.deleteCTrainInfo(curriculumTrainStudent);
    }
}
