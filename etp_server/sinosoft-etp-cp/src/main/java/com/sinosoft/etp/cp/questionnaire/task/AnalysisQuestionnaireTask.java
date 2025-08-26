package com.sinosoft.etp.cp.questionnaire.task;

import com.alibaba.fastjson.JSONArray;
import com.sinosoft.etp.cp.questionnaire.domain.QuestionnaireTask;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireTaskMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("questionnaireTask")
public class AnalysisQuestionnaireTask {
    @Resource
    private TQuestionnaireTaskMapper tQuestionnaireTaskMapper;

    public void analysis(){
        //选择题汇总
        List<Map<String,Object>> dataList = tQuestionnaireTaskMapper.getChoiceSummary();
        Date now = new Date();
        List<QuestionnaireTask> saveData = new ArrayList<>();
        dataList.forEach(item->{
            //sql 汇总的答案形式 A,B,B,C...
            String da = item.get("da").toString();
            Map<String,Integer> daNumMap = letterCount(da);

            //遍历选项 获取每个选项有多少人选择
            List<Map<String,String>> xxlist = JSONArray.parseObject(item.get("xxList").toString(),List.class);
            xxlist.forEach(xx ->{
                QuestionnaireTask task = new QuestionnaireTask();
                task.setWjid(Long.parseLong(item.get("wjid").toString()));
                task.setTmid(item.get("tmid").toString());
                task.setType(item.get("type").toString());
                task.setTg(item.get("tg").toString());
                task.setUpdateTime(now);
                task.setOption(xx.get("key"));
                task.setOptionName(xx.get("value"));
                int num = 0;
                if(null != daNumMap.get(xx.get("key"))){
                    num = daNumMap.get(xx.get("key"));
                }
                task.setNum(num);
                saveData.add(task);
            });
        });
        List<QuestionnaireTask> wdList = tQuestionnaireTaskMapper.getWdSummany();
        wdList.forEach(item->{
            item.setUpdateTime(now);
        });
        saveData.addAll(wdList);
        if(saveData.size()!=0){
            tQuestionnaireTaskMapper.addQuestionnaireSum(saveData);
        }

    }

    /**
     * 统计各个答案的数量
     * @param da
     */
    public Map<String,Integer>  letterCount(String da) {
        da = da.replace(",","");
        Stream<String> words = Arrays.stream(da.split(""));
        Map<String,Integer> wordsCount = words.collect(Collectors.toMap(k->k,v->1,(i,j)->i+j));
        return wordsCount;
    }

}
