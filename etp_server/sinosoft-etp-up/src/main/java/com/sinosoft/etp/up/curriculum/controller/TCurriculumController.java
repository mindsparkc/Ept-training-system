package com.sinosoft.etp.up.curriculum.controller;

import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.etp.up.curriculum.domain.TCurriculum;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumContent;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumStudent;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumSubject;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumService;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumStudentService;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumSubjectService;
import com.sinosoft.etp.up.errorbook.domain.TErrorbook;
import com.sinosoft.etp.up.errorbook.service.ITErrorbookService;
import com.sinosoft.etp.up.subject.domain.TSubject;
import com.sinosoft.etp.up.subject.service.ITSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * curriculumController
 *
 *
 * @date 2021-11-15
 */
@RestController
@RequestMapping("/etp/curriculum")
public class TCurriculumController extends BaseController
{
    @Autowired
    private ITCurriculumService tCurriculumService;
    @Autowired
    private ITCurriculumStudentService tCurriculumStudentService;

    @Resource
    private ITCurriculumSubjectService tCurriculumSubjectService;

    @Resource
    private ITErrorbookService itErrorbookService;

    @Resource
    private ITSubjectService itSubjectService;

    /**
     * 查询curriculum列表
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCurriculum tCurriculum)
    {

        String tags = tCurriculum.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tCurriculum.getParams().put("tags", tags.split(","));
            tCurriculum.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TCurriculum> list = tCurriculumService.selectTCurriculumList(tCurriculum);
        list.forEach(item->{
            int num = tCurriculumStudentService.countJoinCurriculumById(item.getId());
            item.setNum(num);
        });
        return getDataTable(list, TCurriculum.class);
    }


    /**
     * 获取curriculum详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:curriculum:query')")
    @Log(title = "查询课程目录信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tCurriculumService.selectTCurriculumById(id));
    }


    /**
     * 获取课程详情内容项
     * @param id 课程id
     */
    @GetMapping("/getTCurriculumContentDetailByCid/{id}")
    public AjaxResult getTCurriculumContentDetailByCid(@PathVariable("id") Long id){
        List<TCurriculumContent> contents = tCurriculumService.getTCurriculumContentDetailByCid(id);
        return AjaxResult.success(contents);
    }



    @GetMapping("getCurriculumTags")
    public AjaxResult getCurriculumTags(){
        List<Map<String,Object>>list = tCurriculumService.getCurriculumTags();
        return AjaxResult.success(list);
    }

    /**
     * 获取知识库信息
     * @return
     */
    @GetMapping("getKnowledgeInfo")
    public TableDataInfo getKnowledgeInfo(TCurriculum tCurriculum) {
        String tags = tCurriculum.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tCurriculum.getParams().put("tags", tags.split(","));
            tCurriculum.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TCurriculum> list = tCurriculumService.getKnowledgeInfo(tCurriculum);
        list.forEach(item->{
            int num = tCurriculumStudentService.countJoinCurriculumById(item.getId());
            item.setNum(num);
            // 封面图特殊处理
//            item.put("fmt", ((String)item.get("fmt")).replace("{storage.path.url}", getUrl()));
        });
        return getDataTable(list, TCurriculum.class);
    }

    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

    /**
     * 查询课程及课件、试题详情
     * app端用
     */
    @GetMapping("/selectTCurriculumByIds")
    public  AjaxResult selectTCurriculumByIds(Long id,Long rwid){
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TCurriculum>(TCurriculum.class);

        return AjaxResult.success(responseFieldUtil.response(tCurriculumService.selectTCurriculumByIds(id,rwid)));
    }


    /**
     * 领取课程
     */
    @Log(title = "curriculum", businessType = BusinessType .INSERT)
    @PostMapping("/receiveCurriculum")
    public AjaxResult receiveCurriculum(@RequestBody TCurriculumStudent tCurriculumStudent)
    {
        return AjaxResult.success(tCurriculumStudentService.insertTCurriculumStudent(tCurriculumStudent));
    }


    /**
     * id 课程id
     * jd 课件id
     * contentIds 课程包含的所有课件id
     * 修改课程学习进度
     */
    @PostMapping("/updateStatus")
    public  AjaxResult updateStatus(@RequestBody Map<String,Object> params){
        Long id = Long.parseLong(params.get("id").toString());
        Long jd = Long.parseLong(params.get("jd").toString());
        String contentIds = params.get("contentIds").toString();
        return AjaxResult.success(tCurriculumService.updateStatus(id,jd,contentIds));
    }

    /**
     * 进度=null时，课件可能包含在其他课程，已经有进度
     * 查询进度=100，sort最大的课件，---如果查到kj，返回前端，展示这个课件内容
     *                          ---没有查到kj，返回null,前端展示树结构第一条
     * @param id
     * @return
     */
    @GetMapping("/showInitContent/{id}")
    public AjaxResult showInitContent(@PathVariable("id") Long id){
        //查找课程,需要打开的课件
        Long kjid = tCurriculumService.showInitContent(id);
        return AjaxResult.success(kjid);
    }


    /**
     * 获取习题记录详细信息
     */
    @GetMapping(value = "/getSubjectInfo/{kcid}/{stid}")
    public AjaxResult getSubjectInfo(@PathVariable("kcid") Long kcid,@PathVariable("stid")Long stid)
    {
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        return AjaxResult.success(tCurriculumSubjectService.selectTCurriculumSubjectById(kcid,stid,xyid));
    }

    /**
     * 新增习题记录
     */
    @Log(title = "存储习题记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addSubject")
    public AjaxResult add(@RequestBody TCurriculumSubject tCurriculumSubject)
    {
        //根据课件id获取课件信息
        TSubject tSubject = itSubjectService.selectTSubjectById(tCurriculumSubject.getStid());
        //判断用户答案是否与正确答案一致
        int num = insertErrorSubject(tCurriculumSubject,tSubject);
        //判断习题之前有没有被记录，无则新增，有则覆盖
        List<TCurriculumSubject> list = tCurriculumSubjectService.selectTCurriculumSubjectList(tCurriculumSubject);
        if(list.size()>0){
            tCurriculumSubject.setId(list.get(0).getId());
            return toAjax(tCurriculumSubjectService.updateTCurriculumSubject(tCurriculumSubject));
        }else{
            return toAjax(tCurriculumSubjectService.insertTCurriculumSubject(tCurriculumSubject));
        }
    }

    /**
     * 推荐课程
     * @param tCurriculum
     * @return
     */
    @GetMapping("/tjList")
    public TableDataInfo tjList(TCurriculum tCurriculum)
    {

        String tags = tCurriculum.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tCurriculum.getParams().put("tags", tags.split(","));
            tCurriculum.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TCurriculum> list = tCurriculumService.selectTjCurriculumList(tCurriculum);
        list.forEach(item->{
            int num = tCurriculumStudentService.countJoinCurriculumById(item.getId());
            item.setNum(num);
        });
        return getDataTable(list, TCurriculum.class);
    }


    public int insertErrorSubject(TCurriculumSubject tCurriculumSubject, TSubject tSubject){
        Boolean bl = false;
        int count = 0;
        if("1".equals(tSubject.getLx())||"3".equals(tSubject.getLx())){
            if("正确".equals(tCurriculumSubject.getXyda())){
                tCurriculumSubject.setXyda("1");
            }else if("错误".equals(tCurriculumSubject.getXyda())){
                tCurriculumSubject.setXyda("0");
            }
            if(!tCurriculumSubject.getXyda().equals(tSubject.getDa())){
                bl = true;
            }
        }else if("2".equals(tSubject.getLx())){
            String[] da = tSubject.getDa().split(",");
            String[] yhDa = tCurriculumSubject.getXyda().split(",");
            int num = 0;
            if (da.length == yhDa.length){
                for (int i = 0; i < da.length; i++) {
                    for (int j = 0; j < yhDa.length; j++) {
                        if(da[i].equals(yhDa[j])){
                            num +=1;
                        }
                    }
                }
                if(num<da.length){
                    bl = true;
                }
            }else{
               bl = true;
            }
        }else if("4".equals(tSubject.getLx())){

            //List yhDa = JSONObject.parseArray(tCurriculumSubject.getXyda(),List.class);
            List yhDa = (List) JSON.parse(tCurriculumSubject.getXyda());
            List da = JSONObject.parseArray(tSubject.getDa(),List.class);
            if(da.size()==yhDa.size()){
                int num = 0;
                for (int i = 0; i < da.size(); i++) {
                    for (int j = 0; j < yhDa.size(); j++) {
                        if(da.get(i).equals(yhDa.get(j))){
                            num += 1;
                        }
                    }
                }
                if(num<da.size()){
                    bl = true;
                }
            }else{
                bl = true;
            }

        }
        if(bl){
            TErrorbook tErrorbook = new TErrorbook();
            tErrorbook.setAnswered(tCurriculumSubject.getXyda());
            tErrorbook.setStid(tSubject.getId());
            tErrorbook.setTg(tSubject.getTg());
            tErrorbook.setXx(tSubject.getXx());
            tErrorbook.setJx(tSubject.getJx());
            tErrorbook.setStlx(tSubject.getLx());
            tErrorbook.setDa(tSubject.getDa());
            tErrorbook.setKcid(tCurriculumSubject.getKcid());
            tErrorbook.setCtlx("2");
            tErrorbook.setRemake("\""+ tCurriculumSubject.getRemake() +"\"课程中答错");
            count = itErrorbookService.insertTErrorbook(tErrorbook);
        }
        return count;
    }
}
