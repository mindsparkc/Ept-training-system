package com.sinosoft.etp.up.curriculum.service.impl;


import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.progress.mapper.ProgressMapper;
import com.sinosoft.etp.up.courseware.domain.TCoursewareStudent;
import com.sinosoft.etp.up.courseware.mapper.TCoursewareStudentMapper;
import com.sinosoft.etp.up.curriculum.domain.TCurriculum;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumContent;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumContentMapper;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumMapper;
import com.sinosoft.etp.up.curriculum.mapper.TCurriculumStudentMapper;
import com.sinosoft.etp.up.curriculum.service.ITCurriculumService;
import com.sinosoft.etp.up.task.mapper.TTaskStudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * curriculumService业务层处理
 *
 *
 * @date 2021-11-15
 */
@Service
public class TCurriculumServiceImpl implements ITCurriculumService {
    @Resource
    private TCurriculumMapper tCurriculumMapper;
    @Resource
    private TCurriculumContentMapper tCurriculumContentMapper;
    @Resource
    private TCurriculumStudentMapper tCurriculumStudentMapper;
    @Resource
    private TCoursewareStudentMapper tCoursewareStudentMapper;
    @Resource
    private ProgressMapper progressMapper;
    @Resource
    private TTaskStudentMapper tTaskStudentMapper;
    /**
     * 查询curriculum
     * @param id curriculumID·
     * @return curriculum
     */
    @Override
    public TCurriculum selectTCurriculumById(Long id) {

        TCurriculum tCurriculum = tCurriculumMapper.selectTCurriculumById(id,SecurityUtils.getLoginUser().getUser().getUserId());
        //学习人数
        int num = tCurriculumStudentMapper.countJoinCurriculumById(tCurriculum.getId());
        tCurriculum.setNum(num);
        //目录信息
        List<TCurriculumContent> curriculumContents = tCurriculumContentMapper.selectTurriculumContentByCid(tCurriculum.getId(),SecurityUtils.getLoginUser().getUser().getUserId());
        tCurriculum.settCurriculumContents(curriculumContents);
        return tCurriculum;
    }

    /**
     * 查询curriculum列表
     * @param tCurriculum curriculum
     * @return curriculum
     */
    @Override
    public List<TCurriculum> selectTCurriculumList(TCurriculum tCurriculum) {
        tCurriculum.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        //tCurriculum.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        List<TCurriculum> list = new ArrayList<>();
        if("最近学习".equals(tCurriculum.getJd()) || tCurriculum.getJd()==null || "100".equals(tCurriculum.getJd()) || "未学完".equals(tCurriculum.getJd())){
            list = tCurriculumMapper.selectTCurriculumList(tCurriculum);
        }else if("最近领取".equals(tCurriculum.getJd())) {
            list = tCurriculumMapper.selectTCurriculumByGet(tCurriculum);
        }
        return list;
    }
    @Override
    public List<TCurriculum> selectTjCurriculumList(TCurriculum tCurriculum) {
        return tCurriculumMapper.selectTjCurriculumList(tCurriculum);
    }

    /**
     * 根据课程ID获取课程内容
     */
    @Override
    public List<TCurriculumContent> getTCurriculumContentDetailByCid(Long id) {
        return tCurriculumContentMapper.selectTurriculumContentByCid(id,SecurityUtils.getLoginUser().getUser().getUserId());
    }

    @Override
    public List<Map<String, Object>> getCurriculumTags() {
        //Long qyid = SecurityUtils.getLoginUser().getUser().getDeptId();
        return tCurriculumMapper.getCurriculumTags();
    }

    @EtpDataScope(alias = "t")
    @Override
    public List<TCurriculum> getKnowledgeInfo(TCurriculum tCurriculum) {
        try{
            tCurriculum.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        }catch (Exception e){
            tCurriculum.setXyid(null);
        }

        //tCurriculum.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        return tCurriculumMapper.getKnowledgeInfo(tCurriculum);
    }


    @Override
    public TCurriculum selectTCurriculumByIds(Long id,Long rwid) {


        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        TCurriculum tCurriculum = tCurriculumMapper.selectTCurriculumById(id,SecurityUtils.getLoginUser().getUser().getUserId());

        //学习人数
        int num = tCurriculumStudentMapper.countJoinCurriculumById(tCurriculum.getId());
        tCurriculum.setNum(num);

        List<TCurriculumContent> curriculumContents = tCurriculumContentMapper.selectTurriculumContentByCid(tCurriculum.getId(),xyid);
        List<Long> kjids = new ArrayList<>();
        if(null != curriculumContents){
            //任务进度更新的时候，没有更新课程的进度，从任务进课程的时候，看不到任务学的课程进度
            //获取 任务记录的当前学习的课件（判断一下任务记录的这个课件是否在这个课程中）（目前给app用2022-4-7）
            if(null != rwid){
                curriculumContents.forEach(item ->{
                    if("kj".equals(item.getSource_type())){
                        kjids.add(item.getSid());
                    }
                });
                if(kjids.size()>0){
                    Long currentId = tTaskStudentMapper.getTaskCurrentId(rwid,xyid,kjids);
                    tCurriculum.setCurrentId(currentId);
                }
            }
        }
        tCurriculum.settCurriculumContents(curriculumContents);
        return tCurriculum;
    }

    @Override
    public Long showInitContent(Long kcid){

        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        Long kjid= tCurriculumStudentMapper.getInitCourseware(xyid,kcid);
        return kjid;
    }

    /**
     * 更新进度
     * @param cid 课程id
     * @param kjid 课件id
     * @return
     */
    @Override
    @Transactional
    public Double updateStatus(Long cid,Long kjid,String contentIds) {

        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        String[] kjids = contentIds.split(",");
        //1. 更新课件进度100，先查t_courseware_student 有没有这个数据，没有插入
        TCoursewareStudent data = tCoursewareStudentMapper.selectTCoursewareStudentById(kjid,xyid);
        if(null==data){
            TCoursewareStudent tCoursewareStudent = new TCoursewareStudent();
            tCoursewareStudent.setKjid(kjid);
            tCoursewareStudent.setXyid(xyid);
//            tCoursewareStudent.setJd("{\"rate\":100}");
            tCoursewareStudent.setDelFlag("0");
            tCoursewareStudentMapper.insertTCoursewareStudent(tCoursewareStudent);
        }
        //2.更新课程进度 只更新当前这个课程的，只更新当前看的哪个课件
        tCurriculumStudentMapper.updateCurriculumJd(xyid,cid,kjid);
        //总进度
      /*  Double rate = 0.0;
        TCurriculumStudent tCurriculumStudent = new TCurriculumStudent();
        //查询目前的jd 课件完成数量
        TCurriculum tCurriculum = tCurriculumMapper.selectTCurriculumById(cid,SecurityUtils.getLoginUser().getUser().getUserId());
        ProgressVo preJdInfo = JSONObject.parseObject(tCurriculum.getKcjd(), ProgressVo.class);

        ProgressVo jdInfo = JSONObject.parseObject(JSON.toJSONString(progressMapper.getRateByXyid(xyid,kjids)),ProgressVo.class);
        //判断 之前的课件完成数 与课件进度更新之后是否一致，一致说明已经更新了，不需要再更新
        if(null != jdInfo ){
            if(null== preJdInfo || (null!=preJdInfo && !preJdInfo.getCompleted().equals(jdInfo.getCompleted()))){
                //原来的的进度  跟 新进度 完成个数不一致才更新
                Double allRate = jdInfo.getRate();
                BigDecimal bg = new BigDecimal(allRate/(kjids.length*100)*100);
                rate = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                jdInfo.setRate(rate);
                jdInfo.setTime(System.currentTimeMillis());
                JSONObject json = new JSONObject();
                json.put("current",kjid);
                json.put("source_type","kj");
                jdInfo.setLastStudy(json);
                //更新进度字段
                tCurriculumStudent.setJd(JSONObject.toJSONString(jdInfo));
                tCurriculumStudent.setKcid(cid);
                tCurriculumStudent.setXyid(xyid);
                tCurriculumStudentMapper.updateTCurriculumStudent(tCurriculumStudent);

                //3. 更新任务的进度
                updateTaskProgress(cid,xyid);
            }else if(preJdInfo.getCompleted().equals(jdInfo.getCompleted())){
                //只更新当前课件
                tCurriculumStudentMapper.updateCurriculumJd(xyid,cid,kjid);
            }
        }
*/
        return 0.0;
    }


}
