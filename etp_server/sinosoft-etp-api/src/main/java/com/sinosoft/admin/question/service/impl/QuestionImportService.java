package com.sinosoft.admin.question.service.impl;

import com.sinosoft.admin.question.domain.QuestionImportData;
import com.sinosoft.admin.question.mapper.QuestionMapper;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.admin.question.domain.QuestionImport;
import com.sinosoft.admin.question.domain.QuestionImportRecord;
import com.sinosoft.admin.question.mapper.QuestionImportDataMapper;
import com.sinosoft.admin.question.mapper.QuestionImportRecordMapper;
import com.sinosoft.admin.question.service.IQuestionImport;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.Question;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class QuestionImportService implements IQuestionImport {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    private QuestionImportRecordMapper questionImportRecordMapper;
    @Resource
    private QuestionImportDataMapper questionImportDataMapper;

    @Resource
    private QuestionMapper questionMapper;

    /**
     * TODO: 批量导入题目，将批量导入的题目写入临时表
     *
     * @param questions
     * @return 返回一个 ID，为该次导入数据的批号。可根据该批号查询导入的题目
     */
    @Override
    public Long importQuestion(List<QuestionImport> questions, String fileName) {
        // TODO: 写入记录表
        QuestionImportRecord record = new QuestionImportRecord();
        record.setRecordId(IdUtil.nextId());
        record.setFileName(fileName);
        record.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        record.setImportDate(new Date());
        record.setStatus("1");
        record.setTotal(questions.size());
        questionImportRecordMapper.insertImportRecord(record);

        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);

        try {
            QuestionImportDataMapper mapper = sqlSession.getMapper(QuestionImportDataMapper.class);

            Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
            Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
            Integer i = 0;
            for (QuestionImport questionImport :questions) {
                //判断导入题目是否有问题
                if(!"问答题".equals(questionImport.getType()) && questionImport.getRightAnswer().isEmpty()){
                    questionImport.setErrorMsg("题目解析出错，未解析出题目答案。");
                }
                if(!"填空题".equals(questionImport.getType())&& !"问答题".equals(questionImport.getType())&&questionImport.getOptionA().isEmpty()){
                    questionImport.setErrorMsg("题目解析出错，未解析出题目选项。");
                }

                QuestionImportData question = questionImport.question();
                if (question == null) continue;

                question.setCreateBy(SecurityUtils.getUsername());
                question.setUserId(userId);
                question.setDeptId(deptId);
                question.setRecordId(record.getRecordId());
                mapper.insertQuestion(question);
                i++;
                if (i % 400 == 0 || i == questions.size()) {
                    // 每 400 条提交一次，提交后无法回滚
                    sqlSession.commit();
                    sqlSession.clearCache();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
        questionImportRecordMapper.updateRecordStatus(record.getRecordId(), "2");
        return record.getRecordId();
    }

    /**
     * TODO: 查询指定人员的导入记录，若不指定，则查询全部导入记录
     * @param userId   根据用户 ID 查询
     * @param status   根据记录的状态查询
     * @param fileName 根据文件名查询
     * @return
     */
    @Override
    public List<QuestionImportRecord> selectImportRecord(Long userId, String status, String fileName) {
        // TODO: 查询导入记录列表
        return questionImportRecordMapper.selectImportRecord(userId,status,fileName);
    }

    /**
     * TODO: 查询导入记录详情
     * @param recordId
     * @return
     */
    @Override
    public List<Question> selectImportData(Long recordId) {
        return questionImportDataMapper.selectImportData(recordId);
    }

    /**
     * TODO: 放弃导入
     * @param recordId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void giveupImportRecord(Long recordId) {
        questionImportRecordMapper.updateRecordStatus(recordId,"8");
    }

    /**
     * TODO: 提交导入记录，将题目导入题库
     * @param recordId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int submitImportRecord(Long recordId) {
        List<Question> questionImportData = questionImportDataMapper.selectImportData(recordId);
        int num = 0;
        for (Question q : questionImportData) {
            if(!"2".equals(q.getDelFlag())){
                q.setDisplay(q.getContent());
                questionMapper.insertQuestion(q);
                num++;
            }
        }
        questionImportRecordMapper.updateRecordStatus(recordId,"9");
        return num;
    }

    /**
     * TODO: 编辑导入的单个题目
     * @param question
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editImportData(Question question) {

    }

    /**
     * TODO: 将导入的某条题目标记为删除，这样这条题目在导入时就不会导入到题库中了
     * @param quIds
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteImportData(Long[] quIds) {
       questionImportDataMapper.deleteImportData(quIds);
    }
}
