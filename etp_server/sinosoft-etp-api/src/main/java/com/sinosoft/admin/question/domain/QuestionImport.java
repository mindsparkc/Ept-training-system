package com.sinosoft.admin.question.domain;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.utils.IdUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.core.domain.Question;
import com.sinosoft.core.domain.QuestionAnswer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 供批量导入题目使用
 */
public class QuestionImport {

    @Excel(row = true)
    private Long row;

    @Excel(name="序号")
    private String sn;

    @Excel(name = "题目（必填）")
    private String content;

    @Excel(name = "题型（必填）")
    private String type;

    /**
     * 单选、多选、判断，会有正确答案字段
     * 形式如 A 或 ABCD
     */
    @Excel(name = "正确答案（必填）")
    private String rightAnswer;

    @Excel(name = "解析")
    private String analysis;

    /**
     * 1简单 2一般 3较难 4困难
     */
    @Excel(name = "难易程度（默认简单）")
    private String level;

    /**
     * 考试、练习、考试||练习
     */
    @Excel(name = "考试或练习（默认仅练习）")
    private String flag;

    @Excel(name = "选项A")
    private String optionA;

    @Excel(name = "选项B")
    private String optionB;

    @Excel(name = "选项C")
    private String optionC;

    @Excel(name = "选项D")
    private String optionD;

    @Excel(name = "选项E")
    private String optionE;

    @Excel(name = "选项F")
    private String optionF;

    @Excel(name = "选项G")
    private String optionG;

    @Excel(name = "选项H")
    private String optionH;

    @Excel(name = "选项I")
    private String optionI;

    @Excel(name = "选项J")
    private String optionJ;

    @Excel(name = "选项K")
    private String optionK;
    @Excel(name = "选项L")
    private String optionL;
    @Excel(name = "选项M")
    private String optionM;
    @Excel(name = "选项N")
    private String optionN;
    @Excel(name = "选项O")
    private String optionO;
    @Excel(name = "选项P")
    private String optionP;
    @Excel(name = "选项Q")
    private String optionQ;
    @Excel(name = "选项R")
    private String optionR;
    @Excel(name = "选项S")
    private String optionS;
    @Excel(name = "选项T")
    private String optionT;
    @Excel(name = "选项U")
    private String optionU;
    @Excel(name = "选项V")
    private String optionV;
    @Excel(name = "选项W")
    private String optionW;
    @Excel(name = "选项X")
    private String optionX;
    @Excel(name = "选项Y")
    private String optionY;
    @Excel(name = "选项Z")
    private String optionZ;
    //记录题目导入失败原因
    private String errorMsg;

    public QuestionImportData question() {
        if ("示例".equals(sn)) return null;
        QuestionImportData question = new QuestionImportData();
        question.setQuId(IdUtil.nextId());
        question.setContent(content);
        question.setDisplay(content);
        question.setVersion(DateUtils.getNowDate().getTime());
        question.setType(typeCode());
        question.setLevel(levelCode());
        question.setAnalysis(analysis);
        question.setStatus("0");
        question.setSn(Long.valueOf(sn));
        question.setRowNum(row);

        // 是否考试、是否练习
        String lookup = StringUtils.lookup(flag, "练习", "1", "考试", "2", "练习||考试", "3");
        if (StringUtils.equalsAny(lookup, "1", "3")) question.setTarinFlag(true);
        if (StringUtils.equalsAny(lookup, "2", "3")) question.setExamFlag(true);

        // 仅单选、多选、判断、填空题会需要设置候选答案
        if (StringUtils.equalsAny(question.getType(), "1", "2", "3", "4"))
            question.setAnswers(getAnswers(question.getQuId())); // 候选答案
        question.setRemark(JSON.toJSONString(this)); // 将自身保存在 remark 中
        return question;
    }

    // ：1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题
    public String typeCode() {
        return type.equals("单选题")?"1":
                type.equals("复选题")?"2":
                        type.equals("判断题")?"3":
                                type.equals("填空题")?"4":
                                        type.equals("问答题")?"5":
                                                type.equals("组合体")?"6":"0";
    }

    // 简单 1 一般 2 较难 3 困难 4
    public String levelCode() {
        return level.equals("简单")?"1":
                level.equals("一般")?"2":
                        level.equals("较难")?"3":
                                level.equals("困难")?"4":"0";
    }

    // 获取候选答案
    private List<QuestionAnswer> getAnswers(Long quId) {
        List<QuestionAnswer> answers = new ArrayList<>();

        // 处理正确答案
        List<String> rightA = new ArrayList<>();
        rightA = Arrays.asList(rightAnswer.split(""));

        // getOptionA-Z 进行赋值
        Class<?> clazz = this.getClass();
        char star = 'A';
        while (true) {
            try {
                Method method = clazz.getMethod("getOption" + star);
                String o = (String)method.invoke(this);
                if (o == null || "".equals(o)) break;
                QuestionAnswer answer = new QuestionAnswer();
                answer.setAnswerId(IdUtil.nextId());
                answer.setQuId(quId);
                if (StringUtils.equalsAny(typeCode(), "4")) {
                    // 填空题，则认为每个空可以有多个答案，答案间用 || 分割，每个空可以有多个正确答案
                    answer.setContents(Arrays.asList(o.split("||")));
                }else { // 如果是选择、判断，则直接将选项值赋予 Content
                    answer.setContent(o);
                }

                // 如果是单选题、多选、判断，授予正确答案
                if (rightA.contains(String.valueOf(star)))
                    answer.setRightFlag(true);
                else
                    answer.setRightFlag(false);

                answers.add(answer);
            } catch (NoSuchMethodException e) {
                break;
            } catch (InvocationTargetException e) {
                break;
            } catch (IllegalAccessException e) {
                break;
            }
            star++;
        }


        return answers;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getOptionF() {
        return optionF;
    }

    public void setOptionF(String optionF) {
        this.optionF = optionF;
    }

    public String getOptionG() {
        return optionG;
    }

    public void setOptionG(String optionG) {
        this.optionG = optionG;
    }

    public String getOptionH() {
        return optionH;
    }

    public void setOptionH(String optionH) {
        this.optionH = optionH;
    }

    public String getOptionI() {
        return optionI;
    }

    public void setOptionI(String optionI) {
        this.optionI = optionI;
    }

    public String getOptionJ() {
        return optionJ;
    }

    public void setOptionJ(String optionJ) {
        this.optionJ = optionJ;
    }

    public String getOptionK() {
        return optionK;
    }

    public void setOptionK(String optionK) {
        this.optionK = optionK;
    }

    public String getOptionL() {
        return optionL;
    }

    public void setOptionL(String optionL) {
        this.optionL = optionL;
    }

    public String getOptionM() {
        return optionM;
    }

    public void setOptionM(String optionM) {
        this.optionM = optionM;
    }

    public String getOptionN() {
        return optionN;
    }

    public void setOptionN(String optionN) {
        this.optionN = optionN;
    }

    public String getOptionO() {
        return optionO;
    }

    public void setOptionO(String optionO) {
        this.optionO = optionO;
    }

    public String getOptionP() {
        return optionP;
    }

    public void setOptionP(String optionP) {
        this.optionP = optionP;
    }

    public String getOptionQ() {
        return optionQ;
    }

    public void setOptionQ(String optionQ) {
        this.optionQ = optionQ;
    }

    public String getOptionR() {
        return optionR;
    }

    public void setOptionR(String optionR) {
        this.optionR = optionR;
    }

    public String getOptionS() {
        return optionS;
    }

    public void setOptionS(String optionS) {
        this.optionS = optionS;
    }

    public String getOptionT() {
        return optionT;
    }

    public void setOptionT(String optionT) {
        this.optionT = optionT;
    }

    public String getOptionU() {
        return optionU;
    }

    public void setOptionU(String optionU) {
        this.optionU = optionU;
    }

    public String getOptionV() {
        return optionV;
    }

    public void setOptionV(String optionV) {
        this.optionV = optionV;
    }

    public String getOptionW() {
        return optionW;
    }

    public void setOptionW(String optionW) {
        this.optionW = optionW;
    }

    public String getOptionX() {
        return optionX;
    }

    public void setOptionX(String optionX) {
        this.optionX = optionX;
    }

    public String getOptionY() {
        return optionY;
    }

    public void setOptionY(String optionY) {
        this.optionY = optionY;
    }

    public String getOptionZ() {
        return optionZ;
    }

    public void setOptionZ(String optionZ) {
        this.optionZ = optionZ;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
