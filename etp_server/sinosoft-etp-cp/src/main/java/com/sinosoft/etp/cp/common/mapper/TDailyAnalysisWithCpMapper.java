package com.sinosoft.etp.cp.common.mapper;

import com.sinosoft.etp.cp.common.domain.TDailyAnalysisWithCp;

import java.util.List;
import java.util.Map;

/**
 * @author 86151
 */
public interface TDailyAnalysisWithCpMapper {

    /**
     * 查询每日统计
     *
     * @param id
     * @return
     */
    public TDailyAnalysisWithCp selectTDailyAnalysisById(Long id);

    /**
     * @param tDailyAnalysisWithCp
     * @return
     */
    public List<TDailyAnalysisWithCp> selectTDailyAnalysisList(TDailyAnalysisWithCp tDailyAnalysisWithCp);

    /**
     *
     * @param tDailyAnalysisWithCp
     * @return
     */
    public int insertTDailyAnalysis(TDailyAnalysisWithCp tDailyAnalysisWithCp);

    /**
     *
     * @param tDailyAnalysisWithCp
     * @return 结果
     */
    public int updateTDailyAnalysis(TDailyAnalysisWithCp tDailyAnalysisWithCp);

    /**
     *
     * @param id
     * @return
     */
    public int deleteTDailyAnalysisById(Long id);

    /**
     *
     * @param ids
     * @return
     */
    public int deleteTDailyAnalysisByIds(Long[] ids);

    /**
     * 统计近七天课件点击次数
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByKj(Map<String,Object> map);

    /**
     * 统计近七天课程点击次数
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByKc(Map<String,Object> map);

    /**
     * 统计近七天任务点击次数
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByRw(Map<String,Object> map);

    /**
     * 统计近七天学习时间
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByStudy(Map<String,Object> map);

    /**
     * 统计近七天视频类学习时间
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByVideo(Map<String,Object> map);

    /**
     * 统计近七天富文本类学习时间
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByText(Map<String,Object> map);

    /**
     * 统计近七天文件类学习时间
     * @param map
     * @return
     */
    public List<TDailyAnalysisWithCp> selectWeekByFile(Map<String,Object> map);

}
