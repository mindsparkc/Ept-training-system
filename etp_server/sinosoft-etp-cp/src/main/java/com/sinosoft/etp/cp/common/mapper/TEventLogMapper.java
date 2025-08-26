package com.sinosoft.etp.cp.common.mapper;


import com.sinosoft.etp.common.domain.TEventLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 86151
 */
public interface TEventLogMapper {

    /**
     * 保存redis中事件信息
     * @param tEventLog
     * @return
     */
    public int insertEvent(TEventLog tEventLog);

    /**
     * 避免插入重复数据
     * @param date
     * @return
     */
    public void delEvent(String date);

    public List<TEventLog> getTeventLogList(Long userId,String eventType,String specificDate);
    /**
     * 查询某日所有课件点击次数
     * @param specificDate 统计日期
     * @param eventType 事件类型
     * @return
     */
    public List<TEventLog> selectByKj(@Param("specificDate") String specificDate, @Param("eventType") String eventType);

    /**
     * 查询某日所有课程点击次数
     * @param specificDate 统计日期
     * @param eventType 事件类型
     * @return
     */
    public List<TEventLog> selectByKc(@Param("specificDate") String specificDate,@Param("eventType") String eventType);

    /**
     * 查询某日所有考试点击次数
     * @param specificDate 统计日期
     * @param eventType 事件类型
     * @return
     */
    public List<TEventLog> selectByKs(@Param("specificDate") String specificDate,@Param("eventType") String eventType);

    /**
     * 查询某日所有任务点击次数
     * @param specificDate 统计日期
     * @param eventType 事件类型
     * @return
     */
    public List<TEventLog> selectByRw(@Param("specificDate") String specificDate,@Param("eventType") String eventType);


    public List<TEventLog> selectStudyByKj(@Param("specificDate") String specificDate,@Param("eventType") String eventType);
}
