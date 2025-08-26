package com.sinosoft.etp.cp.subject.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.DocTractorUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.subject.domain.TSubjectImportResponse;
import com.sinosoft.etp.cp.subject.domain.TSubjectWithCp;
import com.sinosoft.etp.cp.subject.mapper.TSubjectCompanyWithCpMapper;
import com.sinosoft.etp.cp.subject.mapper.TSubjectWithCpMapper;
import com.sinosoft.etp.cp.subject.service.ITSubjectWithCpService;
import com.sinosoft.etp.cp.subject.utils.LineType;
import com.sinosoft.etp.cp.subject.utils.LineTypeRuling;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 题库Service业务层处理
 *
 *
 * @date 2021-11-08
 */
@Service
public class TSubjectServiceWithCpImpl implements ITSubjectWithCpService
{
    @Autowired
    private TSubjectWithCpMapper tSubjectMapper;

    @Autowired
    private TSubjectCompanyWithCpMapper tSubjectCompanyMapper;

    @Autowired
    private ITTagWithCpService iTTagService;

    private static final String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

    /**
     * 查询题库
     *
     * @param id 题库ID
     * @return 题库
     */
    @Override
    public TSubjectWithCp selectTSubjectById(Long id)
    {
        return tSubjectMapper.selectTSubjectById(id);
    }

    /**
     * 查询题库列表
     *
     * @param tSubject 题库
     * @return 题库
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TSubjectWithCp> selectTSubjectList(TSubjectWithCp tSubject)
    {
        return tSubjectMapper.selectTSubjectList(tSubject);
    }

    /**
     * 新增题库
     *
     * @param tSubject 题库
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTSubject(TSubjectWithCp tSubject)
    {
        tSubject.setCreateTime(DateUtils.getNowDate());
        tSubject.setCreateBy(SecurityUtils.getUsername());
        tSubject.setUpdateTime(DateUtils.getNowDate());
//        String text = getText(tSubject.getTg());
        tSubject.setTgDisplat(StringUtils.getText(tSubject.getTg()));
        int i = tSubjectMapper.insertTSubject(tSubject);

        //插入题库企业关联表
        //TSubjectCompanyWithCp tSubjectCompany = new TSubjectCompanyWithCp();
        //tSubjectCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tSubjectCompany.setTkid(tSubject.getId());
        //tSubjectCompany.setCreateTime(DateUtils.getNowDate());
        //tSubjectCompany.setCreateBy(SecurityUtils.getUsername());
        //tSubjectCompany.setUpdateTime(DateUtils.getNowDate());
        //tSubjectCompanyMapper.insertTSubjectCompany(tSubjectCompany);

        //插入标签表
        if(null != tSubject.getParams().get("tagStr")){
            //企业插入标签
            iTTagService.commpanyInsertTag(tSubject.getParams().get("tagStr").toString());
            //插入题库标签关联表
            iTTagService.insertRelateTag(tSubject.getId(),tSubject.getParams().get("tagStr").toString(),"TK");
        };

        return i;
    }


    /**
     * 修改题库
     *
     * @param tSubject 题库
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTSubject(TSubjectWithCp tSubject)
    {
        tSubject.setUpdateTime(DateUtils.getNowDate());
        tSubject.setUpdateBy(SecurityUtils.getUsername());
//        String text = getText(tSubject.getTg());
        tSubject.setTgDisplat(StringUtils.getText(tSubject.getTg()));
        //删除关联关系
        iTTagService.deleteRelateTag(tSubject.getId(),"TK");
        int i = tSubjectMapper.updateTSubject(tSubject);
        //插入标签表
        if(null != tSubject.getParams().get("tagStr")){
            iTTagService.commpanyInsertTag(tSubject.getParams().get("tagStr").toString());
            //插入题库标签关联表
            iTTagService.insertRelateTag(tSubject.getId(),tSubject.getParams().get("tagStr").toString(),"TK");
        };
        return i;
    }

    /**
     * 批量删除题库
     *
     * @param ids 需要删除的题库ID
     * @return 结果
     */
    @Override
    public int deleteTSubjectByIds(Long[] ids)
    {
        return tSubjectMapper.deleteTSubjectByIds(ids);
    }

    /**
     * 删除题库信息
     *
     * @param id 题库ID
     * @return 结果
     */
    @Override
    public int deleteTSubjectById(Long id)
    {
        return tSubjectMapper.deleteTSubjectById(id);
    }

    /**
     * 随机选择试题
     * @param ids
     * @param num
     * @return
     */
    @Override
    public List<TSubjectWithCp> getSubjectRand(String ids, int num){
        return tSubjectMapper.getSubjectRand(ids,num);
    }

    /**
     * 批量上传题目
     * @param filePath
     * @return
     */
    @Override
    public TSubjectImportResponse batchSubjects(String filePath) throws IOException {
        TSubjectImportResponse response;
        try {
            response = parseBatchDoc(filePath);
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("系统发生错误，请联系管理员解决！");
        }

        return response;
    }

    @Override
    public List<TSubjectWithCp> getSelectedStByKc(Long kcid) {
        return tSubjectMapper.getSelectedStByKc(kcid);
    }

    /**
     * 解析题目
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    private TSubjectImportResponse parseBatchDoc(String filePath)
            throws IOException {

        TSubjectImportResponse response = new TSubjectImportResponse();

        AtomicReference<String> currentType = new AtomicReference<>();
        AtomicReference<String> currentTypeCode = new AtomicReference<>();
        AtomicReference<TSubjectWithCp> subject = new AtomicReference<>();
        AtomicReference<List<Map<String, String>>> options = new AtomicReference<>();
        // 逐行处理文件
        DocTractorUtils.lineByLine(filePath, line -> {
            LineTypeRuling lineTypeRuling = new LineTypeRuling(line);
            LineType lineType = lineTypeRuling.checkLineType();
            if (lineType == LineType.notes || lineType == LineType.undefined) {
                return; // 忽略注释行及未识别的
            }else if (lineType == LineType.type) {
                currentType.set(lineTypeRuling.getText());
                currentTypeCode.set(LineTypeRuling.getSubjectTypeCode(lineTypeRuling.getText()));
            }else if (lineType == LineType.stem) {
                // 如果有选项，则先把选项保存到上一个题目中
                if (options.get() != null && subject.get() != null) {
                    subject.get().setXx(JSONObject.toJSONString(options.get()));
                    options.set(null);
                }
                // 判断上一个题目是否正常
                if (subject.get() != null) {
                    checkSubject(subject.get());
                }

                subject.set(new TSubjectWithCp());
                subject.get().setTg(lineTypeRuling.getText());
                subject.get().setLx(currentTypeCode.get());
                subject.get().setRemark("手工导入" + filePath);
                subject.get().setCreateBy(SecurityUtils.getUsername());
                response.addSubject(subject.get());
                subject.get().setSflx("1"); // 默认仅练习
            }else if (lineType == LineType.options) {
                if (options.get() == null) {
                    options.set(new LinkedList<>());
                }
                Map<String, String> xx = new HashMap<>();
                xx.put("key", lineTypeRuling.getSourceText().substring(0, 1));
                xx.put("value", lineTypeRuling.getSourceText().substring(2));
                options.get().add(xx);
            }else if (lineType == LineType.answer) {
                String da = lineTypeRuling.getText();
                if (currentTypeCode.get().equals("2")) { // 复选题
                    if (da.contains(",") || da.contains("，")) {
                        da = da.replace("，", ",");
                    }else {
                        da = StringUtils.join(da.split(""), ",");
                    }
                    subject.get().setDa(da);
                }else if (currentTypeCode.get().equals("3")) {
                    // 判断题
                    da = da.trim();
                    if (da.equals("对") || da.equals("正确")) {
                        subject.get().setDa("1");
                    }else if (da.equals("错") || da.equals("错误")) {
                        subject.get().setDa("0");
                    }else {
                        throw new RuntimeException("题目：\"" + subject.get().getTg() + "\"的答案错误。判断题的答案" +
                                "只能为\"对\"、\"正确\"或\"错\"、\"错误。\"");
                    }
                } else if (currentTypeCode.get().equals("4")) {// 填空题
                    // 需要处理为 JSON [["空1答案","空1答案"],["空2答案","空2答案"]]
                    // 模版中的答案样式为：红楼梦&石头记|水浒传|三国演义|西游记
                    List<List<String>> das = new ArrayList<>();
                    String[] spaces = da.split("\\|");
                    for(int i=0; i<spaces.length; i++) {
                        String[] items = spaces[i].split("&");
                        das.add(Arrays.asList(items));
                    }
                    subject.get().setDa(JSONObject.toJSONString(das));
                }else {
                    subject.get().setDa(da);
                }
                checkSubject(da, subject.get());
            }else if (lineType == LineType.parse) {
                subject.get().setJx(lineTypeRuling.getText());
            }else if (lineType == LineType.label) {
                Map<String, Object> params = new HashMap<>();
                params.put("tagStr", lineTypeRuling.getText().replace("|", ","));
                subject.get().setParams(params);
                // subject.get().setTags(lineTypeRuling.getText().replace("|", ","));
            }else if (lineType == LineType.scope) {
                if (StringUtils.isNotBlank(lineTypeRuling.getText())) {
                    subject.get().setSfks("0");
                    subject.get().setSflx("0");
                    String[] split = lineTypeRuling.getText().split("\\|");
                    for (int i=0; i< split.length; i++) {
                        if (split[i].equals("考试"))
                            subject.get().setSfks("1");
                        else if (split[i].equals("练习"))
                            subject.get().setSflx("1");
                    }
                }
            }
        });

        // 1. 判断最后一个题目是否正常
        // 2. 若最后一个题目存在选项，则进行赋值
        if (subject.get() != null) {
            checkSubject(subject.get());
            if (options.get() != null)  subject.get().setXx(JSONObject.toJSONString(options.get()));
        }
        for (TSubjectWithCp subject1 : response.getSubjects()) {
            response.addCount(1);
            insertTSubject(subject1);
        }

        response.setSubjects(null);

        return response;
    }

    private void checkSubject(TSubjectWithCp subjectWithCp) {
        if (StringUtils.isBlank(subjectWithCp.getDa())) {
            throw new RuntimeException("题目：\"" + subjectWithCp.getTg() + "\"的答案不能为空！");
        }
    }

    private void checkSubject(String da, TSubjectWithCp subjectWithCp) {
        if (StringUtils.isBlank(da)) {
            throw new RuntimeException("题目：\"" + subjectWithCp.getTg() + "\"的答案不能为空！");
        }
    }

    private String getText(String str) {
        return str.replaceAll(regEx_style,"").replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "").replaceAll("<!--[\\w\\W\\r\\n]*?-->","");
    }
}
