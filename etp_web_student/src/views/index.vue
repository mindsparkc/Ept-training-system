<template>
  <div class="app-container-1200">
    <div class="top-nav">
      <div class="top-left">
<!--        <div class="left-tree">
          <ul>
            <li v-for="(item,index) in category" v-if="item.id !== 0"
                :class="{'category-selected':chooseCategoryFlag == index}"
                @mouseover="showExpand(1,item,index)" @mouseleave="showExpand(0)">
              {{ item.name }}
&lt;!&ndash;              v-show="item.children.length>0"&ndash;&gt;
              &lt;!&ndash; i class="el-icon-caret-right" ></i &ndash;&gt;
            </li>
          </ul>
          <div class="more-content" v-show="category[0].children!=undefined && category[0].children.length>8">
            <router-link to="/etp/curriculum/components/curriculumAll/">更多<i class="el-icon-arrow-right"></i></router-link>
          </div>

        </div>-->
        <!--              树展开-->
        <div class="tree-expand" v-show="isExpand" @mouseover="showExpand(1)" @mouseleave="showExpand(0)">
          <div class="expand-item" v-for="item in cateScond.children">
            <div @click="chooseCategory(item.id)">{{ item.name }} </div>
            <div>
<!--              <span>测试数据</span>-->
              <span v-for="tag in item.children" @click="chooseCategory(tag.id)">{{ tag.name }}</span>
            </div>
          </div>

        </div>
        <el-carousel arrow='never' height="420px">
          <el-carousel-item v-for="(item,index) in carouselUrls" :key="index">
            <el-image style="height: 100%; width: 100%;" alt="X" :src="item" >
              <div slot="error" style="height: 100%; width: 100%;" >
                <el-image style="height: 100%; width: 100%;" :src="require('@/assets/images/banner/20220309182352.jpg')">
                </el-image>
              </div>
            </el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <!--      右侧登录-->
      <div class="top-right">
        <div v-if="isLogin" style="padding: 0 20px">
          <div class="common-title-mini">
            最近学习
            <div class="line-title"></div>
            <div class="study-item" v-for="item in studyObj">
              <div style="width: 80%" @click="clickCurriculum(item)">
                <span>{{item.mc}}</span>
              </div>
              <el-progress :percentage="item.jd==null?'0.0':item.jd"></el-progress>
            </div>

            <router-link to="/curriculum">
              <el-button type="primary" plain style="margin: 0 auto;display: flex;margin-top: 35px;">我的课程</el-button>
            </router-link>
          </div>
        </div>

        <div class="right-login" v-if="!isLogin">
          <div style="margin-top: 20px;color: #333333;font-size: 18px;">考试培训系统</div>
          <el-image :src="require('@/assets/images/user-big.png')" fit="fill"></el-image>
          <router-link to="/login"><el-button class="login-btn">立即登录</el-button></router-link>
          <el-divider></el-divider>
          <div class="app-load" v-if="app_download_url!=null"><a @click="appOpen()" target="_blank">>> 下载app学习 <<</a></div>
        </div>
      </div>
    </div>
    <div class="bottom-content">
<!--      左侧内容区域-->
      <div class="bottom-left">
        <!--内容区域-->
        <div class="first-row" v-show="isLogin">

          <div class="task-item">
            <router-link to="/Task"><div class="common-title">当前任务 <i class="el-icon-arrow-right"></i></div></router-link>
            <el-card shadow="hover" class="task-exam-content" v-for="(item,index) in taskList" :key="index" @click.native="clickTask(item)">
              <div class="img-area">
                <el-image :src="item.fmt">
                  <div slot="error"  >
                    <el-image fit="cover" :src="require('@/assets/images/task_cover.png')">
                    </el-image>
                  </div>
                </el-image>
                <div class="mark">{{item.rwjd}}</div></div>
              <div class="desc-area">
                <div class="desc">{{ item.mc }}</div>
                <div class="desc-mini">结束时间：{{item.taskEndTime}}</div>
              </div>
            </el-card>
            <el-card class="task-exam-content" v-show="null==taskList || taskList.length==0" style="margin: 0 auto;">
              <dataEmpty :height="'110px'" :width="'100%'"></dataEmpty>
            </el-card>
          </div>

          <div class="exam-item">
            <router-link to="/examInfo/userExam"><div class="common-title">我的考试  <i class="el-icon-arrow-right"></i></div></router-link>
            <el-card shadow="hover" class="task-exam-content" v-for="(item,index) in homeExam" @click.native="clickExam(item)">
              <div class="img-area">
                <el-image :src="item.fm">
                  <div slot="error" style="height: 100%; width: 100%;">
                    <el-image :src="require('@/assets/images/examine_cover.png')">
                    </el-image>
                  </div>
                </el-image>
              </div>
              <div class="desc-area">
                <div class="desc">{{item.name}}</div>
                <div class="desc-mini">结束时间：{{item.endTime==null?'未限制':item.endTime}}</div>
              </div>
            </el-card>
            <el-card class="task-exam-content" v-show="homeExam==null||homeExam.length==0" style="margin: 0 auto;">
              <dataEmpty :height="'110px'" :width="'100%'"></dataEmpty>
            </el-card >
          </div>
        </div>

        <div class="second-row">
          <div class="curriculum-item">
<!--            <router-link to="/etp/curriculum/components/curriculumAll/">-->
<!--             -->
<!--            </router-link>-->
            <div class="common-title">推荐课程  <i class="el-icon-arrow-right"></i></div>
            <div>
              <el-card shadow="hover" class="curriculum-content" v-for="(item,index) in recommendedCourses" :key="index"  @click.native="clickCurriculumAll(item)">
                <el-image :src="item.fmt"  fit="fill">
                  <div slot="error" style="height: 100%; width: 100%;" >
                    <el-image fit="fill" :src="require('@/assets/images/cover.jpg')">
                    </el-image>
                  </div>
                </el-image>
                <div class="desc">
                  {{ item.mc }}
                </div>
                <div class="desc-mini">{{item.num}}人学习中</div>
                <div style="margin: 7px 24px 0">
                  <span v-for="(tag,index) in (item.tags||'').split(',')" v-if="item.tags!=null&&index<6" style="margin-right: 5px;">
                    <el-tag type="success" >{{ tag }}</el-tag>
                  </span>
                </div>
              </el-card>
              <div v-show="recommendedCourses==null||recommendedCourses.length==0||Object.keys(recommendedCourses).length==0" style="margin: 0 auto;">
                <dataEmpty :height="'184px'"></dataEmpty>
              </div>
            </div>
          </div>
        </div>

        <div class="second-row">
          <div class="curriculum-item" v-for="(data,key) in kcByCategoryMap" v-show="data.length>0">
            <div class="common-title" @click="chooseCategory(data[0].categoryId)">{{ key }}  <i class="el-icon-arrow-right"></i></div>
            <div>
              <el-card shadow="hover" class="curriculum-content" v-for="(item,index) in data" :key="index"  @click.native="clickCurriculumAll(item)">
                <el-image :src="item.fmt==null?require('@/assets/images/cover.jpg'):item.fmt"  fit="fill">
                  <div slot="error" style="height: 100%; width: 100%;" >
                    <el-image fit="fill" :src="require('@/assets/images/cover.jpg')">
                    </el-image>
                  </div>
                </el-image>
                <div class="desc">
                  {{ item.mc }}
                </div>
                <div class="desc-mini">{{item.num}}人学习中</div>
                <div style="margin: 7px 24px 0">
                  <span v-for="(tag,index) in (item.tags||'').split(',')" v-if="item.tags!=null&&index<6" style="margin-right: 5px;">
                    <el-tag type="success" >{{ tag }}</el-tag>
                  </span>
                </div>
              </el-card>
              <div v-show="recommendedCourses==null||recommendedCourses.length==0||Object.keys(recommendedCourses).length==0" style="margin: 0 auto;">
                <dataEmpty :height="'184px'"></dataEmpty>
              </div>
            </div>
          </div>
        </div>
      </div>

<!--      右侧公告和课程-->
      <div class="bottom-right">
        <div class="notice-area">
          <div class="common-title-mini">
            通知公告
            <div class="line-title"></div>
          </div>
          <div class="notice-item" v-for="(item,index) in notice"  @click="getNoticeInfo(notice[index])">
            <div>
             <el-tag type="danger" >{{ item.noticeType=='1'?'通知':'公告' }}</el-tag>
             <div>
               <span> {{ item.noticeTitle }}</span>

             </div>
            </div>
            <div>{{ item.createTime }}</div>
          </div>
          <div class="more-content"><router-link to="/system/notice">更多<i class="el-icon-arrow-right"></i></router-link></div>
        </div>
        <div class="hot-area">
          <div class="hot-img">
            <span>热门课程</span>
          </div>
          <div>
            <div v-for="(item,index) in hotKc"  class="hot-item" @click="clickCurriculumAll(item)">
              <div v-if="index==0" style="font-weight: bold;color: #FF6C00; font-style: italic;">TOP1</div>
              <div v-else-if="index==1" style="font-weight: bold;color: #F9CD1D; font-style: italic;">TOP2</div>
              <div v-else-if="index==2" style="font-weight: bold;color: #A8890F; font-style: italic;">TOP3</div>
              <div v-else style="font-weight: bold;color: #B2B2B2;">{{index}}</div>
              <span>{{item.mc}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

<!--    按照分类显示课程-->
<!--    <div class="btm-div">
      <div v-for="(data,key) in kcByCategoryMap" v-show="data.length>0">
        <div class="common-title">{{ key }}</div>
        <div class="btm-common">
          <el-card shadow="hover" v-for="item in data" @click.native="clickCurriculumAll(item)">
            <el-image :src="item.fmt==null?require('@/assets/images/cover.jpg'):item.fmt"  fit="fill"/>
            <div class="desc">
              {{ item.mc }}
            </div>
            <div class="desc-mini">{{item.num}}人学习中</div>
            <div style="margin: 7px 32px 0">
              <span v-for="(tag,index) in (item.tags||'').split(',')" v-if="item.tags!=null&&index<6" style="margin-right: 5px;">
                <el-tag type="success" >{{ tag }}</el-tag>
              </span>
            </div>
          </el-card>
        </div>
      </div>

    </div>-->

    <noticeDetail ref="noticeForm" :open="noticeOpen" :form="noticeform" @closeDialog="noticeOpen=false"></noticeDetail>
<!--    <el-row class="con-item">
      <el-row>
        <el-col>
          <div class="commonTitle">
            <span>我的任务</span>
            <span>
             <router-link to="/Task">
                更多 <i class="el-icon-d-arrow-right"/>
            </router-link>
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <div class="commonContentRow">
            <div class="ccrRowContent" v-for="(item,index) in taskList" :key="index"  @click="clickTask(item)">
              <el-image :src="item.fmt" />
              <div>
                <div>{{ item.mc }}</div>
                &lt;!&ndash;              <div v-show="false">&ndash;&gt;
                &lt;!&ndash;                已学习 35/113 课时&ndash;&gt;
                &lt;!&ndash;              </div>&ndash;&gt;
              </div>
              <div class="mark">{{item.rwjd}}</div>
            </div>
            <div v-show="null==taskList || taskList.length==0" style="margin: 0 auto;">
              <dataEmpty :height="'184px'"></dataEmpty>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-row>-->

<!--    **********************任务结束********************-->
<!--    <el-row class="con-item">
      <el-row>
        <el-col>
          <div class="commonTitle">
            <span>在线学习</span>
            <span>
            <router-link to="/curriculum">
                更多 <i class="el-icon-d-arrow-right"/>
            </router-link>
          </span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <div class="commonContentRow">
            <div class="ccrRowContent" v-for="(item,index) in studyObj" @click="clickCurriculum(item)">
              <el-image :src="item.fmt" />
              <div>
                <div>{{item.mc}}</div>
                &lt;!&ndash;              <div v-show="false">&ndash;&gt;
                &lt;!&ndash;                已学习 35/113 课时&ndash;&gt;
                &lt;!&ndash;              </div>&ndash;&gt;
              </div>
              <div class="mark">{{item.kcjd}}</div>
            </div>
            <div v-show="studyObj==null || studyObj.length==0" style="margin: 0 auto;">
              <dataEmpty :height="'184px'"></dataEmpty>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-row>
&lt;!&ndash;  *********************在线学习结束**********************&ndash;&gt;
    <el-row class="con-item">
      <el-row>
        <el-col>
          <div class="commonTitle">
            <span>考试测验</span>
            <span>
            <router-link to="/studyManagement/examManagement">
                更多 <i class="el-icon-d-arrow-right"/>
            </router-link>
          </span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <div class="commonContentRow">
            <div class="ccrRowContent" v-for="(item,index) in homeExam"  @click="clickExam(item)">
              <el-image :src="item.fm" />
              <div>
                <div>{{item.mc}}</div>
                &lt;!&ndash;              <div v-show="false">&ndash;&gt;
                &lt;!&ndash;                已完成 10%&ndash;&gt;
                &lt;!&ndash;              </div>&ndash;&gt;
              </div>
            </div>
            <div v-show="homeExam==null||homeExam.length==0" style="margin: 0 auto;">
              <dataEmpty :height="'184px'"></dataEmpty>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-row>
    &lt;!&ndash;   ************考试测验结束************************************* &ndash;&gt;
    <el-row class="con-item">
      <el-row>
        <el-col>
          <div class="commonTitle">
            <span>今日推荐</span>
            <span>
                <router-link to="/etp/curriculum/components/curriculumAll/">
                   更多 <i class="el-icon-d-arrow-right"/>
                </router-link>
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <div class="commonContentRow">
            <div class="ccrRowContent" v-for="(item,index) in recommendedCourses" :key="index"  @click="clickCurriculumAll(item)">
              <el-image :src="item.fmt" />
              <div>
                <div>
                  {{ item.mc }}
                </div>
              </div>

            </div>

            <div v-show="recommendedCourses==null||recommendedCourses.length==0" style="margin: 0 auto;">
              <dataEmpty :height="'184px'"></dataEmpty>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-row>-->
    <!--   ************今日推荐结束************************************* -->
    <el-dialog class="app-qrcg"
               title="App二维码"
               :visible.sync="dialogVisibleQr"
               width="20%"
               :before-close="closeQr"
               :close-on-click-modal=true>
      <div style="margin-left: 11%;margin-top: 7%">
        <vueQr :logo-src="adLogoUrl" :text="app_download_url" :size="270"></vueQr>
        <el-button type="success" style="margin-left: 24%;margin-top: 14%"><a :href="app_download_url" target="_self">安卓版下载</a></el-button>
      </div>
<!--      <div style="display: inline">-->
<!--        <vueQr :logo-src="iosLogoUrl" :text="app_download_url" :size="250"></vueQr>-->
<!--      </div>-->
    </el-dialog>

  </div>
</template>

<script>
import {listNotice} from "@/api/system/notice";
import detail from "@/views/sign/components/detail";
import noticeDetail from "@/views/system/notice/components/detail";
import dataEmpty from "@/components/empty"
import {clickSendRedis} from "@/api/buryPoint"
import {
  getHomeExam,
  getHomepageEventRegistration,
  getHomepageRecommendedCourses, getHomepageTask, getStudyObj,
  categoryShowData,
  queryCarousel,
  getTheHomepageQuestionnaire,
  queryCornerMark,homeHotKc,categoryList,getNewApp
} from "@/api/etp";
import dayjs from "dayjs";
import {getSign, studentSign} from "@/api/sign/sign";
import {getCurriculum, getKnowledgeInfo, tjlistCurriculum} from "@/api/etp/curriculum/curriculum";
import { getToken } from '@/utils/auth'
import vueQr from 'vue-qr/src/packages/vue-qr'

export default {
  name: 'index',
  components:{detail,noticeDetail,dataEmpty,vueQr},
  data() {
    return {
      // 活动报名
      open: false,
      form: {},

      //通知公告弹窗
      noticeOpen:false,
      noticeform:{},
      //轮播图
      carouselUrls: [require("@/assets/images/banner/20220309171001.jpg")],
      notice: [],
      //通知下标index
      noticeIndex: null,
      intervalId: null,
      playTime: 5000,
      //角标
      subscript: {},
      //问卷
      questionnaire: [],
      //活动报名
      studyRegistration: [],
      //推荐课程
      recommendedCourses: [],
      //首页任务
      taskList: [],
      //首页学习
      studyObj: [],
      //考试
      homeExam:[],
      isLogin:false,
      hotKc:["java学习基础","综合素质","ps基础入门","python","css从入门到实践","css从入门到实践","css从入门到实践","css从入门到实践","css从入门到实践"],
      swiperOption: {
        slidesPerView: 2,
        spaceBetween: 10,
        // centeredSlides: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
        },
      },

      //展示的1级
      category:['分类1','分类2','分类3','分类4','分类5','分类6','分类7'],
      //展示的2级
      cateScond:[],
      cateFirstIndex:null,
      chooseCategoryFlag:null,
      isExpand:false,
      kcByCategoryMap: null,
      app_download_url:null,
      dialogVisibleQr:false,
      adLogoUrl:require('@/assets/images/ewm/app1.png'),
      iosLogoUrl:require('@/assets/images/iOS.png')
    }
  },
  created() {
    /** 获取APP安卓平台最新版本信息 */
    let query = {'appCode':this.appVersionCode,'platform':'1101'}
    getNewApp(query).then( res => {
      console.log('获取app版本',res)
      if(res.code == 200 && res.data!=null){
        this.app_download_url = res.data.download;
      }else {
        this.message.error("获取APP版本失败")
      }
    })
    // this.getConfigKey("app.download").then(response => {
    //   this.app_download_url = response.msg;
    //   console.log(this.app_download_url)
    // });

    this.getAnnouncement();
    // this.queryCornerMark();
    // this.getTheHomepageQuestionnaire();
    // this.getHomepageEventRegistration();
    //推荐课程
    this.getHomepageRecommendedCourses();
    this.getStudyObj();
    this.getHomeHotKc();
    this.getCategoryList();
    //轮播图
    this.queryCarousel();
    //查询分类课程
    this.getKcByCategory();
    if(getToken()){
      this.isLogin = true
      this.getHomepageTask();
      this.getHomeExam();
    }else{
      this.isLogin = false
    }
  },
  methods: {
    appOpen(){
      if (!this.dialogVisibleQr) this.dialogVisibleQr = true
    },
    closeQr(){
      this.dialogVisibleQr = false
      console.log(this.dialogVisibleQr)
    },

    getDate(time) {
      return dayjs(time).format("YYYY-MM-DD");
    },
    queryCarousel() {
      queryCarousel().then(res => {
        const data = res.data;
        console.log(res)

        if(null!= data && data.length>0){
          // this.carouselUrls.length = 0;
          this.carouselUrls = []
          for (const datum of data) {
            const {imageurl} = datum;
            this.carouselUrls.push(imageurl);
          }
        }

      })
    },
    getCategoryList(){
      categoryList().then(res =>{
        this.category = res.data
      })
    },
    getHomeExam() {
      let params = {}
      params.pageNum = 1
      params.pageSize = 1
      getHomeExam(params).then(res => {
        console.log(res, "首页考试！");
        if (res.rows && res.rows.length > 0){
          let arr = res.rows;
          if(arr[0].ksjd == '已结束'|| arr[0].ksjd == '已完成'){
            this.homeExam = null;
          }else{
            for (let i = 0; i < arr.length; i++) {
              if(arr[i].fm == undefined || arr[i].fm == null || arr[i].fm == ''){
                arr[i].fm = require("@/assets/images/examine_cover.png");
              }
            }
            this.homeExam = arr;
          }

        }
      })
    },
    getStudyObj() {
      getStudyObj({pageNum:1,pageSize:4}).then(res => {
        console.log(res, "首页我的课程！");
        let arr = res.rows;
        for (let i = 0; i < arr.length; i++) {
          if(arr[i].fmt == undefined || arr[i].fmt == null || arr[i].fmt == ''){
            arr[i].fmt = require("@/assets/images/cover.jpg");
          }
        }
        this.studyObj = arr;
      })
    },
    getHomepageTask() {
      getHomepageTask({ pageNum: 1, pageSize: 1}).then(res => {
        if (res.rows && res.rows.length > 0){
          let arr = res.rows;
          for (let i = 0; i < arr.length; i++) {
            if(arr[i].fmt == undefined || arr[i].fmt == null || arr[i].fmt == ''){
              arr[i].fmt = require("@/assets/images/task_cover.png");
            }
          }
          this.taskList = arr
        }
        console.log(res, "首页任务！");
      })
    },
    //今日推荐
    getHomepageRecommendedCourses() {
      tjlistCurriculum({ pageNum: 1, pageSize: 6,sftj:"1"}).then(res => {
        let arr = res.rows;
        for (let i = 0; i < arr.length ; i++) {
          if(arr[i].fmt == undefined || arr[i].fmt == null || arr[i].fmt == ''){
            arr[i].fmt = require("@/assets/images/cover.jpg");
          }
        }
        this.recommendedCourses = arr;
        console.log(res, "今日推荐");
      })
    },
    getHomepageEventRegistration() {
      getHomepageEventRegistration().then(res => {
        this.studyRegistration = res.data ? res.data : {};
      })
    },
    getTheHomepageQuestionnaire() {
      getTheHomepageQuestionnaire().then(res => {
        /**
         * {
         *     "jssj": "2022-03-10T00:00:00.000+0800",
         *     "id": 52,
         *     "kssj": "2022-02-24T00:00:00.000+0800",
         *     "wjmc": "企业安全生产培训需求调查"
         * }
         */
        this.questionnaire = res.data;
        console.log(res)
      })
    },

    queryCornerMark() {
      queryCornerMark().then(res => {
        console.log(res)
        this.subscript = res.data;
      })
    },
    getAnnouncement() {
      listNotice({pageNum:1,pageSize:4}).then(res => {
        //createTime noticeContent
        this.notice = res.rows;
        this.intervalId = setInterval(() => {
          this.getText();
        }, this.playTime);
      })
    },
    getText() {
      const len = this.notice.length;
      if (len === 0) {
        return; // 当数组为空时，直接返回
      }
      if (!this.noticeIndex) {
        this.noticeIndex = 0;
      }
      if (this.noticeIndex === len - 1) { // 当前为最后一个时
        this.noticeIndex = 0; // 从第一个开始
      } else {
        this.noticeIndex++; // 自增
      }
    },
    destroyed() {
      clearInterval(this.intervalId); // 清除定时器
    },
    //跳转课程
    clickCurriculum(data){
      let jd = null
      getCurriculum(data.id).then(res=>{
        jd = res.data.kcjd
        if(jd){
          if(JSON.parse(jd).lastStudy==undefined){
            jd = null
          }else{
            jd =  JSON.parse(jd)["lastStudy"]["current"]
          }
        }
        let params = {}
        params.streamName = 'stream'
        params.eventType = 'CLI_CURR'
        params.id = data.id

        //点击埋点
        clickSendRedis(params).then(response =>{
          console.log("点击课程埋点",response)
        })

        let router = this.$router.resolve({
          path: "/etp/curriculum/previewCurriculum/",
          query: {cid: data.id, jd:jd}
        });
        window.open(router.href,'_blank')
      })
      // if(data.jd){
      //   if(JSON.parse(data.jd).lastStudy==undefined){
      //     jd = null
      //   }else{
      //     jd =  JSON.parse(data.jd)["lastStudy"]["current"]
      //   }
      //
      // }


    },
    //跳转全部课程（发现）详情
    clickCurriculumAll(data){
      let router = this.$router.resolve({
        path: "/etp/curriculum/previewCurriculum/",
        query: {cid: data.id,getStatus:null}
      });
      window.open(router.href,'_blank')
    },
    //跳转任务
    clickTask(data){
      let jd = null
      if(data.jd){
        if(JSON.parse(data.jd).lastStudy==undefined){
          jd = null
        }else{
          jd =  JSON.parse(data.jd)["lastStudy"]["current"]
        }
      }
      let params = {}
      params.streamName = 'stream'
      params.eventType = 'CLI_TASK'
      params.id = data.id
      //点击埋点
      clickSendRedis(params).then(response =>{
        console.log("点击首页任务埋点",response)
      })

      let router = this.$router.resolve({
        path: "/taskManagement/detail/",
        query: {rwid: data.id, jd:jd}
      });
      window.open(router.href,'_blank')
    },

    //考试
    clickExam(row) {
      const now = Date.parse(new Date());
      if(row.startTime!=null){
        const kssj = Date.parse(row.startTime);
        const ksjssj = Date.parse(row.endTime);
        if(kssj>now) {
          this.$message("考试还未开始！")
        }else if(ksjssj<now){
          this.$message("考试已结束！")
        }else if(row.chance!='0'&&row.chance<=row.usedChance){
          this.$message.error('您的考试次数已用完！')
        }else{
          let router = this.$router.resolve("/examStart/detail/" + row.examId);
          window.open(router.href,'_blank')
        }
      }else if(row.chance!='0'&&row.chance<=row.usedChance){
        this.$message.error('您的考试次数已用完！')
      }else{
        let router = this.$router.resolve("/examStart/detail/" + row.examId);
        window.open(router.href,'_blank')
      }
    },

    signHandleView(data) {
      if (data.id != undefined) {
        getSign(id).then(response => {
          this.form ={}
          this.form = response.data
          this.open = true
        });
      }else{
        this.$router.push({
          path: '/sign',
        });
      }
    },
    questionHandleView(data) {
      if (data!=undefined && data.id != undefined) {
        this.$router.push({
          path: '/etp/questionnaire/answer/',
          query: {id: data.id}
        });
      }else{
        this.$router.push({
          path: '/questionnaire',
        });
      }
    },
    /** 提交按钮 */
    signSubmitForm(type) {
      //活动报名校验
      let params = {"id":this.studyRegistration.id,"sfbm":type,"kfcd":this.studyRegistration.id.kfcd}
      if (this.form.id != null) {
        studentSign(params).then(response => {
          let message = type=='1'?'报名成功':'取消报名成功'
          this.msgSuccess(message);
          this.open = false
          this.getList();
        });
      }
    },

    //通知公告详情
    getNoticeInfo(row){
      this.noticeOpen=true
      let infoForm = {}
      infoForm["title"] = row.noticeTitle
      infoForm["content"] = row.noticeContent
      infoForm["createBy"] = row.createBy
      infoForm["createTime"] = row.createTime
      this.noticeform = infoForm
    },

    //热门课程
    getHomeHotKc(){
      homeHotKc().then(res =>{
        this.hotKc = res.data
      })
    },
    chooseCategory(categoryId){
      this.$router.push({
        path: "/etp/curriculum/components/curriculumAll/",
        query: {categoryId: categoryId}
      });
    },
    showExpand(flag,data,index){
      if(flag == 1){
        this.isExpand = true
        if(data!= undefined){
          this.cateScond = data
          this.cateFirstIndex = index
        }
        this.chooseCategoryFlag = this.cateFirstIndex
      }else{
        this.isExpand = false
        this.chooseCategoryFlag = null
      }
    },
    //分类展示课程
    getKcByCategory(){
      this.kcByCategoryMap = {}
      categoryShowData({sfxs:'1'}).then(res=>{
        let data = res.rows
        for(let i in data){
          tjlistCurriculum({pageNum:1,pageSize:3,categoryId:data[i].id}).then(response=>{
             this.$set(this.kcByCategoryMap,data[i].name,response.rows)
           })
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">

.slide-enter-active, .slide-leave-active {
  transition: all 0.5s ease;
}
.top-nav{
  display: flex;
  .top-left{
    width: 75%;
    height: 420px;
    border-radius: 6px;
    position: relative;
    box-shadow: 0 4px 30px 0 RGB(238 242 245 / 80%);
    .left-tree{
      height: 420px;
      width: 156px;
      background: rgba(245,240,240,0.8);
      //opacity: 0.8;
      position: absolute;
      z-index: 999;
      overflow: auto;
      ul{
        padding: 0;
      }
      .category-selected{
        background: #fbf5ee;
        color: #fa8919;
      }
      ul>li{
        height: 42px;
        line-height: 42px;
        list-style:none;
        width: 100%;
        text-align: center;
        cursor: pointer;
        position: relative;
        &:hover{
          background: #fbf5ee;
          color: #fa8919;
        }
        i{
          position: absolute;
          right: 4px;
          top: 15px;
          font-size: 12px;
        }
      }

    }

    .tree-expand{
      position: absolute;
      height: 420px;
      width: 50%;
      background: rgba(252, 247, 247, 0.8);
      left: 156px;
      z-index: 999;
      padding: 20px 30px ;
    }

    .expand-item{
      > div{
        &:nth-of-type(1){
          font-weight: bold;
          color: #333333;
          margin-top: 26px;
          &:hover{
            color: #1c84c6;
            cursor: pointer;
          }
        }
        &:nth-of-type(2){
          margin-top: 12px;
          span{
            color: #333333;
            font-weight: 400;
            margin-right: 30px;
            &:hover{
              color: #1c84c6;
              cursor: pointer;
            }
          }
        }
      }

    }
  }
  .top-right{
    margin-left: 20px;
    width: calc(25% - 20px);
    background: #ffffff;
    height: 420px;
    border-radius: 6px;
    box-shadow: 0 4px 30px 0 RGB(238 242 245 / 80%);
    .right-login{
      height: 100%;
      width: 100%;
      display: flex;
      align-items: center;
      flex-direction: column;
      .el-image{
        height: 43%;
        margin: 36px 0px 28px;
      }
      .el-button{
        background: #ff780c;
        border-radius: 20px;
        color: #ffffff;
        font-size: 20px;
        padding: 10px 80px;
        border-color: #ff780c;
      }
    }
    .app-load{
      color: #ff780c;
    }
    .study-item{
      margin-top: 28px;
      font-size: 16px;
      font-weight: 400;
      color: #303133;
      line-height: 24px;
      span{
        &:hover{
          cursor: pointer;
          color: #fa8919;
        }
      }
      >div{
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
      }
    }
  }
}
.el-card {
  border-radius: 4px;
  box-shadow: 0 4px 30px 0 RGB(238 242 245/80%);
  border: 0px !important;
  background-color: #FFFFFF;
  overflow: hidden;
  color: #303133;
  -webkit-transition: 0.3s;
  transition: 0.3s;
}
.bottom-content{
  display: flex;
  .bottom-left{
    width: 75%;
  }
  .bottom-right{
    width: calc(25% - 20px);
    margin-left: 20px;
    margin-top: 32px;
  }
  .first-row{

    display: flex;
    .task-item{
      width: calc((100%)/2 - 10px);
    }
    .exam-item{
      width: calc((100%)/2 - 10px);
      margin-left: 20px;
    }
    ::v-deep img{
      border-radius: 5px;
    }
  }
  .second-row {

    .curriculum-item{
      > div{
        &:nth-child(2){
          display: flex;
          flex-wrap: wrap;
          border-radius: 8px;
          > div{
            &:nth-child(1){
              margin-right: 20px;
            }
            &:nth-child(2){
              margin-right: 20px;
            }
            &:nth-child(4){
              margin-right: 20px;
              margin-top: 20px;
            }
            &:nth-child(5){
              margin-right: 20px;
              margin-top: 20px;
            }
            &:nth-child(6){
              margin-top: 20px;
            }
          }
        }
      }
      .desc{
        margin:0px 24px 0;
      }
      .desc-mini{
        margin:4px 24px;
      }
      .curriculum-content {
        width: calc((100%)/3 - 13.4px);
        height: 294px;
        background: #ffffff;
        border-radius: 8px;
        //box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%);
        cursor: pointer;

        .el-image{
          width: 100%;
          height: 178px;
          //border-top-left-radius: 8px;
          //border-top-right-radius: 8px;
        }
      }
    }

    ::v-deep img{
      padding: 20px 20px 10px;
      border-radius: 25px;
    }
  }
}
.desc{
  font-size: 18px;
  font-weight: 400;
  color: #303133;
  line-height: 24px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.desc-mini{
  font-size: 14px;
  font-weight: 400;
  color: #909199;
  margin-top: 4px;
}
::v-deep .el-tag--medium {
  height: 24px;
  line-height: 24px;
}
.common-title{
  padding: 32px 0px 20px;
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  cursor: pointer;
}
.slide-enter {
  transform: translateY(50px);
}

.slide-leave-to {
  transform: translateY(-50px);
}


.task-exam-content{
  width: 100%;
  height: 100%;
  position: relative;
  cursor: pointer;
  padding: 20px 20px;
  background: #ffffff;
  height: 162px;
  border-radius: 8px;

  ::v-deep .el-card__body{
    display: flex;
  }
  .img-area{
    width: 196px;
    height: 122px;
  }
  .el-image{
    width: 100%;
    height: 100%;
  }
  .desc-area{
    margin-left: 8px;
  }
}

::v-deep .el-progress-bar{
  width: 97%;
}

.mark{
  width: 68px;
  height: 30px;
  background-color: #ff780c;
  color: #ffffff;
  position: absolute;
  top: 20px;
  border-top-left-radius: 5px;
  border-bottom-right-radius: 26px;
  text-align: center;
  line-height: 30px;
}

.notice-area{
  height: 409px;
  background: #FFFFFF;
  border-radius: 6px;
  padding: 0px 20px;
  position: relative;
  box-shadow: 0 4px 30px 0 RGB(238 242 245/80%);
  .notice-item{
    span{
      cursor: pointer;
      &:hover{
        color: #fa8919!important;
      }
    }
    div{
      font-size: 14px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #999999;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    > div:nth-of-type(1){
      display: flex;
      padding: 20px 0px 8px;

      div{
        font-size: 18px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #303133;
        line-height: 24px;
        margin-left: 4px;
      }

    }
  }
}
.common-title-mini{
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #303133;
  line-height: 46px;
  height: 46px;
  border-bottom: 1px #e2e2e2 solid;
  padding-left: 10px;
  position: relative;
}
.line-title{
  width: 70px;
  height: 2px;
  background: #fa8919;
  border-radius: 2px;
  position: absolute;
  bottom: -2px;
}
.hot-area{
  margin-top: 32px;
  height: 452px;
  background: #FFFFFF;
  border-radius: 6px;
  box-shadow: 0 4px 30px 0 RGB(238 242 245/80%);
  // padding: 1px;

  .hot-img{
    background-image: url("../assets/images/bg_hotclass.png");
    background-size: cover;
    height: 40px;

    span{
      font-size: 18px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #007DFF;
      line-height: 40px;
      padding-left: 20px;
    }
  }

  .hot-item{
    padding-top: 20px;
    display: flex;

    div{
      width: 70px;
      text-align: center;
      align-self: flex-end;
    }
    span{
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      width: calc(100% - 70px);
      color: #303133;
      cursor: pointer;
      &:hover{
        color: #fa8919!important;
      }
    }
  }
}
.more-content{
  font-size: 16px;
  font-weight: 400;
  color: #999999;
  position: absolute;
  bottom: 20px;
  right: 20px;
}

::v-deep .el-card__body {
   padding: 0px;
}
.btm-div{
  width: 1200px;
}
.btm-common{
  display: flex;
  flex-wrap: wrap;
  .el-card{
    width: calc((100% - 60px)/4 );
    height: 294px;
    background: #ffffff;
    border-radius: 8px;
    cursor: pointer;
  }
  .el-card:not(:first-of-type){
    margin-left: 20px;
  }
  .el-image{
    width: 100%;
    height: 178px;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }
  .desc{
    margin: 16px 24px 0;
  }
  .desc-mini{
    margin: 4px 24px;
  }
}
.el-icon-arrow-right{
  &:hover{
    color:#fa8919;
  }
}
::v-deep .app-qrcg {
  .el-dialog{
    height: 55%;
    background-color: azure;
    background-image: url("../../src/assets/images/qr-bg.jpeg");
  }
}
</style>
