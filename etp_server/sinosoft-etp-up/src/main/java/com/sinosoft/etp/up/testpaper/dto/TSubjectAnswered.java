package com.sinosoft.etp.up.testpaper.dto;

/**
 * TSubject 的扩展类，包括学生的答案
 *
 * @author WANGLIJIE
 */
public class TSubjectAnswered extends TSubjectExt {
    /**
     * 学生答案
     */
    private String answered;


    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }


}
