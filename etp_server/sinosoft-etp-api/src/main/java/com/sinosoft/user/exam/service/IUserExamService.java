package com.sinosoft.user.exam.service;

import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.core.domain.PaperUnitItem;
import com.sinosoft.user.exam.domain.*;

import java.util.List;

/**
 * 学员用户查询考试信息的Service层接口
 *
 * @author wanglijie
 */
public interface IUserExamService {
    /**
     * 查询考试列表
     *
     * @param examQuery
     * @return
     */
    public List<UserSimpleExam> getExams(UserExamReqQuery examQuery);

    /**
     * 获取考试详情
     *
     * @param examId
     * @return
     */
    public UserSimpleExam getExamInfo(Long examId);

    /**
     * 获取即将结束的考试
     * @return
     */
    public UserSimpleExam getComingEnd();

    /**
     * 校验是否有权限开始考试
     */
    public UserSimpleExam checkAbleToStartTheExam(Long examId, String password);


    /**
     * 组织试卷
     */
    public UserExam makeupExamContent(UserExam userExam);
    public UserExam makeupExamContent0(UserExam userExam);

    /**
     * 将学员提交的答案与试卷快照合并
     */
    public UserExam makeupUserExam(UserExam userExam, String handType, String remark);

    /**
     * 根据考试记录 ID，查询考试记录详情
     * @param recordId
     * @return
     */
    public UserExamRecordResp queryExamByRecordId(Long recordId);

    /**
     * 根据 recordId 获取未结束的考试的答题记录
     * @param recordId
     * @return
     */
    public UserExam queryUserExamByRecordId(Long recordId);

    /**
     * 查询考试记录列表
     *
     * @param examId
     * @return
     */
    public List<UserExamRecord> queryExamRecord(Long examId, String name, String status, List<String> range);
    public List<UserExamRecord> queryExamRecord(UserExamRecordReqQuery query);

    /**
     * 提交一个题目的答案
     */
    public void submitAnswer(Long recordId, PaperUnitItem unitItem);

    /**
     * 放弃考试
     * @param recordId
     */
    public void endExam(Long recordId, String handType, String remark);

    /**
     * 根据类型查询将开始或将结束的考试数量
     * @param type
     * @return
     */
    public int getCountNum(String type);
}
