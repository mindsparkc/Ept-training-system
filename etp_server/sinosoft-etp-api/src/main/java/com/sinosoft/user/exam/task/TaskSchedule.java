package com.sinosoft.user.exam.task;

import com.sinosoft.user.exam.domain.UserExamRecord;
import com.sinosoft.user.exam.domain.UserExamRecordReqQuery;
import com.sinosoft.user.exam.mapper.UserExamRecordMapper;
import com.sinosoft.user.exam.service.IUserExamService;
import com.sinosoft.user.exam.task.core.MsgEvent;
import io.netty.util.HashedWheelTimer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮调动及处理结束考试的相关逻辑
 *
 * 作业调度，负责从数据库抽取考试记录并加入到时间轮中
 * 仅在程序启动时执行一次，也可配置成定时任务，在调度中手动触发执行
 *
 * 技术：
 * 1. EventBus
 * 2. HashedWheelTimer（Netty）
 *
 * @author wanglijie
 */
@Component("taskSchedule")
public class TaskSchedule {

    private static IUserExamService userExamService;

    @Resource
    public void setRedisService(IUserExamService userExamService) {
        TaskSchedule.userExamService = userExamService;
    }

    /** 时间轮 */
    private static volatile HashedWheelTimer timer = null;
    /** 设置每 30 秒 tick 一次 */
    private static long tickDuration = 30L;            // 时间刻度的长度，多久tick一次
    private static TimeUnit unit = TimeUnit.SECONDS;   // 时间刻度的长度，多久tick一次


    @Resource
    UserExamRecordMapper userExamRecordMapper;

    @PostConstruct
    public void init() {
        runSchedule();
        loadTask();
    }

    public static HashedWheelTimer getTimer() {
        HashedWheelTimer instance = timer;
        if (instance == null) {
            synchronized (TaskSchedule.class) {
                instance = TaskSchedule.timer;
                if (instance == null) {
                    instance = TaskSchedule.timer = new HashedWheelTimer(tickDuration, unit);
                }
            }
        }
        return instance;
    }

    public void loadTask() {
        loadTask(null, false);
    }

    /**
     * 注册事件监听处理和实例化时间轮
     */
    public void runSchedule() {
        EventBus.getDefault().register(new TaskSchedule());
    }

    /**
     * 自数据库加载未交卷的考试，需要注意的是，如果不限制考试时间，则无需定时强制交卷
     * 但同样可以调用该函数，仅加载某一场考试的考试记录，并全部强制交卷
     *
     * @param examId       选填，如果填写该值，则仅加载该场考试的记录
     * @param forceSubmit  填 true
     */
    public void loadTask(Long examId, boolean forceSubmit) {
        // 如果 examId 有值，则只查询该 examId 指定的考试记录；否则，加载全部考试记录
        UserExamRecordReqQuery query = new UserExamRecordReqQuery();
        query.setExamId(examId);
        query.setStatus("0"); // 只查询未交卷的
        List<UserExamRecord> userExamRecords = userExamRecordMapper.selectUserExamRecord(query);

        for (UserExamRecord userExamRecord : userExamRecords) {
            // 若 forceSubmit 为 true，则以当前系统时间为准，强制结束所有考试
            if (forceSubmit) {
                EventBus.getDefault().post(new MsgEvent(userExamRecord.getRecordId(), "强制交卷", 5L));
            }else {
                Date logicEndTime = userExamRecord.getLogicEndTime();
                if (logicEndTime == null) continue;
                long delay = (logicEndTime.getTime()-(new Date()).getTime())/1000 + 10; // 获取距离强制交卷相差的秒数， +10秒，以冗余时间差
                if (delay <=0) {
                    EventBus.getDefault().post(new MsgEvent(userExamRecord.getRecordId(), "强制交卷", 5L));
                }else {
                    EventBus.getDefault().post(new MsgEvent(userExamRecord.getRecordId(), "到达交卷时间，强制交卷", delay));
                }
            }
        }
    }



    /**
     * 处理交卷
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMsgEvent(MsgEvent event) {
        // 将定时作业加到时间轮中，定时触发执行交卷
        System.out.println(event);
        TaskSchedule.getTimer().newTimeout(timeout -> {
            System.out.println("启动" + event);
            TaskSchedule.userExamService.endExam(event.getRecordId(), "2", event.getRemark());
        }, event.getDelay(), event.getUnit());
    }
}
