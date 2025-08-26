package com.sinosoft.task;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.config.ThreadPoolConfig;
import com.sinosoft.entity.Courseware;
import com.sinosoft.entity.Task;
import com.sinosoft.service.ICachePoolService;
import com.sinosoft.service.ICoursewareService;
import com.sinosoft.service.impl.FilePathToLocalService;
import com.sinosoft.utils.IpUtils;
import com.sinosoft.utils.PdfConvertUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 文件解析队列维护
 *
 * 该类实现一个常驻线程
 * 线程-A：用以从数据库获取待处理的文件，并放入队列
 * 线程-B：用以从队列获取数据，并判断线程池是否已饱和，启动线程对文件进行处理。
 *
 * @author WANGLIJIE
 */
@Component
public class FileParseQueueManager {

    @Resource
    private ThreadPoolConfig threadPoolConfig;

    @Resource
    private ICoursewareService coursewareService;

    @Resource
    private ICachePoolService cachePoolService;

    @Resource
    private FilePathToLocalService filePathToLocalService;

    private static String remark = null;

    static {
        try {
            remark = IpUtils.getIpAddress().toString();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 作用：
     * 1. 一次性从数据库获取的待处理文件的条数
     * 2. 缓存待处理文件队列的长度
     */
    private Integer LIMIT = 500;
    private LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue(LIMIT);

    @PostConstruct
    public void init() {
        // 生产者线程
         new Thread(new Producer()).start();
        // 消费者线程
        // new Thread(new Consumer()).start();
    }

    /**
     * 生产者线程
     *
     * @author WANGLIJIE
     */
    private class Producer implements Runnable {

        @Override
        public void run() {
            System.out.println("生产者线程启动……");

            while (true) {
                try {
                    executor();
                    Thread.sleep(12000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void executor() {
            // 从数据库获取一批待处理的文件
            List<Courseware> coursewares = coursewareService.list();
            for (Courseware a : coursewares) {
                System.out.println("准备处理文件：" + a.getId() + ":" + a.getKjdz() + ":" + filePathToLocalService.localPath(a.getKjdz()));
                // 放入队列成功，更新数据状态
                Task task = new Task();
                task.setRequestId(String.valueOf(a.getId()));
                task.setFile(filePathToLocalService.localPath(a.getKjdz()));
//                cachePoolService.addQueueTask(JSON.toJSONString(task));

                a.setStep("1");
                a.setStepMark(remark);
                coursewareService.update(a);

                executor0(JSON.toJSONString(task));
            }
        }
    }

    /**
     * 消费者线程
     *
     * @author WANGLIJIE
     */
    public class Consumer implements Runnable {

        @Override
        public void run() {
            System.out.println("消费者线程启动……");
            int i = 0;
            while (true) {
                try {
                    Thread.sleep(300);
                    if (i%100 == 0) {
                        System.out.println(new Date() + "横在");
                        i = 0;
                    }
                    executor();
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void executor() {
            String task = cachePoolService.getTask();

            if (StringUtils.isEmpty(task)) return;
            /**
             * 启动线程池，若线程池已满则等待稍后尝试
             */
            executor0(task);
        }
    }

    public void executor0(String task) {
        Task object = JSONObject.parseObject(task, Task.class);
        System.out.println("即将启动作业……" + object.getFile());
        // 测试代码：单个文件转换
        // TODO: 多线程，频繁 GC，先用单线程跑任务
        // PdfConvertUtils.pdfSplitAndConvertSvg(new File(object.getFile()));

        Courseware courseware = new Courseware();
        courseware.setId(Integer.valueOf(object.getRequestId()));
        courseware.setStep("2");
        // 更新作业处理中
        coursewareService.update(courseware);
        threadPoolConfig.threadPoolTaskExecutor().execute(new FileParseThread(object.getFile(), new FileParseThread.CallbackAndMetadata() {
            @Override
            public void call(String arg, String step) {
                cachePoolService.finishTask(task);
                System.out.println("作业完成" + task);
                // update 更新作业状态
                Task parseObject = JSONObject.parseObject(task, Task.class);
                Courseware courseware = new Courseware();
                courseware.setId(Integer.valueOf(parseObject.getRequestId()));
                courseware.setStep(step);
                courseware.setMetaData(arg);
                coursewareService.update(courseware);
            }
        }));
    }
}
