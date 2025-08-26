package com.sinosoft.etp.cp.subject.domain;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 批量插入试题的返回信息
 *
 * @author WANGLIJIE
 */
public class TSubjectImportResponse {
    // 解析出的题目数量
    private Integer count = 0;
    // 解析出的题目数量详情
    private LinkedHashMap<String, Integer> detail;

    private List<TSubjectWithCp> subjects = new LinkedList<>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void addCount(Integer count) {
        this.count += count;
    }

    public LinkedHashMap<String, Integer> getDetail() {
        return detail;
    }

    public void setDetail(LinkedHashMap<String, Integer> detail) {
        this.detail = detail;
    }


    public void addDetail(String key, Integer i) {
        Integer integer = this.detail.get(key);
        if (integer == null) {
            integer = i;
        }else {
            integer += i;
        }
        this.detail.put(key, integer);
    }

    public List<TSubjectWithCp> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<TSubjectWithCp> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(TSubjectWithCp subject) {
        this.subjects.add(subject);
    }
}
