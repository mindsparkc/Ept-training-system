package com.sinosoft.admin.question.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.question.domain.QuestionForOld;
import com.sinosoft.admin.question.domain.QuestionForOldCategory;
import com.sinosoft.admin.question.domain.QuestionReqDTO;
import com.sinosoft.admin.question.mapper.QuestionMapper;
import com.sinosoft.admin.question.service.IQuestionService;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.QuestionAnswer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 86151
 */
@Component("questionCopyTask")
public class QuestionCopyTask {

    @Resource
    private IQuestionService iQuestionService;

    @Resource
    private QuestionMapper questionMapper;

    @Scheduled(cron = "0 00 01 ? * *" )
    public void CopySubject(){

        //获取旧题库分类数据并写入新表
        //List<QuestionForOldCategory> categoryList = questionMapper.selectCategoryAll();
        //for (QuestionForOldCategory odlCategory : categoryList) {
        //    Category category = new Category();
        //    category.setCatId(odlCategory.getId());
        //    category.setAncestors(odlCategory.getAncestors());
        //    category.setName(odlCategory.getName());
        //    category.setParentId(odlCategory.getParentId());
        //    int i = questionMapper.insertOldCategory(category);
        //    System.out.println("-------------写入分类数据共："+i+"条-------------");
        //}

        //1.获取旧题库所有题目 2.遍历题目并更改为新版本结构 3.调用新版新增接口将数据写进去
        List<QuestionForOld> list = questionMapper.selectAll();
        for (QuestionForOld questionForOld : list) {

            QuestionReqDTO questionReqDTO = new QuestionReqDTO();

            questionReqDTO.setContent(questionForOld.getTg());
            questionReqDTO.setDisplay(questionForOld.getTgDisplat());
            questionReqDTO.setTarinFlag("1".equals(questionForOld.getSflx())?true:false);
            questionReqDTO.setExamFlag("1".equals(questionForOld.getSfks())?true:false);
            questionReqDTO.setLevel(questionForOld.getLevel());
            questionReqDTO.setType(questionForOld.getLx());
            if(questionForOld.getCategoryId()!=null && questionForOld.getCategoryId()!=0){
                ArrayList<Long> longArrayList = new ArrayList<>();
                longArrayList.add(questionForOld.getCategoryId());
                questionReqDTO.setCategoryIds(Collections.singletonList(longArrayList));
            }else{
                questionReqDTO.setCategoryIds(null);
            }
            if("1".equals(questionForOld.getLx())){
                if(questionForOld.getXx()!=null) {
                    JSONArray xx = JSONArray.parseArray(questionForOld.getXx());
                    ArrayList<QuestionAnswer> arrayList = new ArrayList<>();
                    for (int i = 0; i < xx.size(); i++) {
                        QuestionAnswer questionAnswer = new QuestionAnswer();

                        String value = getValue(xx.get(i).toString());
                        String key = getKey(xx.get(i).toString());
                        if(key.equals(questionForOld.getDa())){
                            questionAnswer.setRightFlag(true);
                        }else{
                            questionAnswer.setRightFlag(false);
                        }
                        questionAnswer.setSelectFlag(false);
                        questionAnswer.setContent(value);
                        questionAnswer.setSort(i+1);
                        questionAnswer.setAnalysis(questionForOld.getJx());
                        arrayList.add(questionAnswer);
                    }
                    questionReqDTO.setAnswers(arrayList);
                }else{
                    questionReqDTO.setAnswers(null);
                }
                iQuestionService.insertQuestion(questionReqDTO);
            }else if("2".equals(questionForOld.getLx())){
                if(questionForOld.getXx()!=null){
                    JSONArray xx = JSONArray.parseArray(questionForOld.getXx());
                    ArrayList<QuestionAnswer> arrayList = new ArrayList<>();
                    for (int i = 0; i < xx.size(); i++) {
                        QuestionAnswer questionAnswer = new QuestionAnswer();
                        String value = getValue(xx.get(i).toString());
                        String key = getKey(xx.get(i).toString());
                        if(questionForOld.getDa().contains(key)){
                            questionAnswer.setRightFlag(true);
                        }else{
                            questionAnswer.setRightFlag(false);
                        }
                        questionAnswer.setSelectFlag(false);
                        questionAnswer.setContent(value);
                        questionAnswer.setSort(i+1);
                        questionAnswer.setAnalysis(questionForOld.getJx());
                        arrayList.add(questionAnswer);
                    }
                    questionReqDTO.setAnswers(arrayList);
                }
                iQuestionService.insertQuestion(questionReqDTO);
            }else if("3".equals(questionForOld.getLx())){
                ArrayList<QuestionAnswer> arrayList = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    if(i==0){
                        questionAnswer.setContent("正确");
                        if("1".equals(questionForOld.getDa())){
                            questionAnswer.setRightFlag(true);
                        }else{
                            questionAnswer.setRightFlag(false);
                        }
                        questionAnswer.setSelectFlag(false);
                        questionAnswer.setSort(i+1);
                        questionAnswer.setAnalysis(questionForOld.getJx());
                        arrayList.add(questionAnswer);
                    }else{
                        questionAnswer.setContent("错误");
                        if("0".equals(questionForOld.getDa())){
                            questionAnswer.setRightFlag(true);
                        }else{
                            questionAnswer.setRightFlag(false);
                        }
                        questionAnswer.setSelectFlag(false);
                        questionAnswer.setSort(i+1);
                        questionAnswer.setAnalysis(questionForOld.getJx());
                        arrayList.add(questionAnswer);
                    }
                }
                questionReqDTO.setAnswers(arrayList);
                iQuestionService.insertQuestion(questionReqDTO);
            }else if("4".equals(questionForOld.getLx())){
                List<List<String>> r = JSONObject.parseObject(questionForOld.getDa().replace(" ",""), List.class);
                ArrayList<QuestionAnswer> arrayList = new ArrayList<>();
                questionReqDTO.setQuId(IdUtil.nextId());
                for (int i = 0; i < r.size(); i++) {
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    String rr = r.get(i).toString();
                    questionAnswer.setContent("["+rr.substring(2,rr.length()-2)+"]");
                    questionAnswer.setContents(r.get(i));
                    questionAnswer.setRightFlag(false);
                    questionAnswer.setSelectFlag(false);
                    questionAnswer.setSort(i+1);
                    arrayList.add(questionAnswer);
                }
                questionReqDTO.setAnswers(arrayList);

                iQuestionService.insertQuestion(questionReqDTO);
            }else if("5".equals(questionForOld.getLx())){
                questionReqDTO.setAnswers(new ArrayList<>());
                iQuestionService.insertQuestion(questionReqDTO);
            }
        }
    }

    public String getValue(String xxValue){
        String value = null;
        JSONObject obj = JSONObject.parseObject(xxValue);
        for (Map.Entry<String,Object> entry:obj.entrySet() ) {
            if("value".equals(entry.getKey())){
                value = entry.getValue().toString();
                System.out.println("value==========="+value);
            }
        }
        return value;
    }

    public String getKey(String xxValue){
        String key = null;
        JSONObject obj = JSONObject.parseObject(xxValue);
        for (Map.Entry<String,Object> entry:obj.entrySet() ) {
            if("key".equals(entry.getKey())){
                key = entry.getValue().toString();
                System.out.println("value==========="+key);
            }
        }
        return key;
    }
}
