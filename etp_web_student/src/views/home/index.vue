<template>
  <div style=" background:#f7f7f7;">
    <div style="height: 361px; background: #f7f7f7;">
      <div class="app-container-1240 ">
        <el-row :gutter="20" class="home_banner">
          <el-col :span="14" style="padding-right: 0;">
            <el-carousel arrow='never' height="351px">
              <el-carousel-item v-for="(item,index) in carouselUrls" :key="index">
                <el-image style="height: 100%; width: 100%; border-radius: 10px" alt="X" :src="item">
                  <div slot="error" style="height: 100%; width: 100%;">
                    <el-image style="height: 100%; width: 100%;"
                      :src="require('@/assets/images/banner/20220309182352.jpg')">
                    </el-image>
                  </div>
                </el-image>
              </el-carousel-item>
            </el-carousel>
          </el-col>
          <el-col :span="10">
            <div>
              <el-row :gutter="10">
                <el-col :span="12">
                  <div class="t-t-div">
                    <div class="t-t-item">
                      <div class="t-title"><span>课程</span></div>
                      <!--                      <div @mouseenter="changeImg(1)" @mouseleave="changeImg(0)"> <router-link to="/curriculum"><span>查看 </span>-->
                      <!--                        <img :src="img_index==1?img_url_b:img_url_h"></router-link>-->
                      <!--                      </div>-->
                    </div>
                    <div class="two-item">
                      <div class="eve-item">
                        <div>
                          {{ kctj.wwc }}
                        </div>
                        <div>未完成</div>
                      </div>
                      <div class="eve-item">
                        <div>
                          {{ kctj.ylq }}
                        </div>
                        <div>已领取</div>
                      </div>
                    </div>
                    <div class="three-item-test" v-if="studyKc.mc!=''">
                      <div class="eve-item" style="width:24%;text-align: center;">
                        <img src="@/assets/home/KC_sign.png" style="width: 38px; height: 38px; border-radius: 50%;">
                      </div>
                      <div class="eve-item-button" style="width: 52%;text-align:center;border-radius: 4px;"
                        onselectstart="return false">
                        <div style="width: 48%; font-size: 12px; text-align: center;">上次学习</div>
                        <div style="text-overflow:ellipsis;white-space:nowrap;overflow:hidden; text-align:left;">{{ studyKc.mc }}</div>
                      </div>
                      <div class="eve-item" style="width: 21%">
                        <el-button round type="primary" size="mini" @click="clickCurriculumAll">继续</el-button>
                      </div>
                    </div>
                    <div v-else class="one-item" style="border-top: 1px solid #EEEEEE;">
                      <div style="color: #999999;font-size: 14px">暂无学习记录</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="t-t-div">
                    <div class="t-t-item">
                      <div class="t-title"><span>考试</span></div>
                      <div @mouseenter="changeImg(2)" @mouseleave="changeImg(0)">
                        <!--                        <router-link to="/studyManagement/examManagement"> <span>查看 </span>-->
                        <!--                          <img :src="img_index==2?img_url_b:img_url_h">-->
                        <!--                        </router-link>-->
                      </div>
                    </div>
                    <div class="two-item">
                      <div class="eve-item">
                        <div>
                          {{ kstj.jjs }}
                        </div>
                        <div style="color: #F44336">将结束</div>
                      </div>
                      <div class="eve-item">
                        <div>
                          {{ kstj.jks }}
                        </div>
                        <div>将开始</div>
                      </div>
                      <!--                      <div class="eve-item" style="align-self: start">-->
                      <!--                        <div style="margin-bottom: 3px">-->
                      <!--                          <div class="jp">-->
                      <!--                            2-->
                      <!--                          </div>-->
                      <!--                        </div>-->
                      <!--                        &lt;!&ndash;             <div>&ndash;&gt;-->
                      <!--                        &lt;!&ndash;               2&ndash;&gt;-->
                      <!--                        &lt;!&ndash;             </div>&ndash;&gt;-->
                      <!--                        <div>上次排名</div>-->
                      <!--                      </div>-->
                    </div>
                    <div class="three-item-test" v-if="comeEndExam!=undefined">
                      <div class="eve-item" style="width: 24%; text-align: center;">
                        <img src="@/assets/home/KC_sign.png" style="width: 38px; height: 38px; border-radius: 50%;">
                      </div>
                      <div class="eve-item-button" style="width: 50%;text-align:left" onselectstart="return false">
                        <div style="width: 48%">即将结束</div>
                        <div style="text-overflow:ellipsis;white-space:nowrap;overflow:hidden;">{{ comeEndExam.name }}
                        </div>
                      </div>
                      <div class="eve-item" style="width: 30%">
                        <el-button round type="primary" size="mini" @click="toExam">考试</el-button>
                      </div>
                    </div>
                    <div v-else class="one-item" style="border-top: 1px solid #EEEEEE;">
                      <div style="color: #999999;font-size: 14px">暂无即将结束的考试</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="10" style="margin-top: 10px">
                <el-col :span="12">
                  <div class="t-t-div">
                    <div class="t-t-item">
                      <div class="t-title"><span>任务</span></div>
                      <div @mouseenter="changeImg(3)" @mouseleave="changeImg(0)">
                        <!--                        <router-link to="/Task"><span>查看 </span>-->
                        <!--                          <img :src="img_index==3?img_url_b:img_url_h">-->
                        <!--                        </router-link>-->
                      </div>
                    </div>
                    <div class="two-item">
                      <div class="eve-item">
                        <div>
                          {{ rwtj.wwc }}
                        </div>
                        <div>未完成</div>
                      </div>
                      <!--                      <div class="eve-item">-->
                      <!--                        <div><router-link to="/Task">{{ rwtj.all }}</router-link></div>-->
                      <!--                        <div>总任务</div>-->
                      <!--                      </div>-->
                      <div class="eve-item">
                        <div>
                          {{ rwtj.jgq }}
                        </div>
                        <div style="color: #F44336">将结束</div>
                      </div>
                    </div>
                    <div class="one-item">
                      <div class="eve-item">
                        <div>
<!--                          <router-link :to="{path:'/Task',query:{jd:2}}">{{ rwtj.jgq }}</router-link>-->
                          {{ rwtj.jgq }}
                        </div>
                        <div>累计完成</div>
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="t-t-div">
                    <div class="t-t-item">
                      <div class="t-title"><span>证书</span></div>
                    </div>
                    <div class="one-item-zs">
                      <div class="eve-item">
                        <div>
                          {{ zstj }}
                        </div>
                        <div>已获得证书</div>
                      </div>
                    </div>
                    <!--                      <div class="two-item">-->
                    <!--                        <div class="eve-item">-->
                    <!--                          <div>223</div>-->
                    <!--                          <div>昨日学习时长</div>-->
                    <!--                        </div>-->
                    <!--                        <div class="eve-item">-->
                    <!--                          <div>54</div>-->
                    <!--                          <div>累计学习时长</div>-->
                    <!--                        </div>-->
                    <!--                      </div>-->
                    <!--                      <div class="two-item">-->
                    <!--                        <div class="eve-item">-->
                    <!--                          <div>12</div>-->
                    <!--                          <div>上周学习时长</div>-->
                    <!--                        </div>-->
                    <!--                        <div class="eve-item">-->
                    <!--                          <div>12</div>-->
                    <!--                          <div>上周排名</div>-->
                    <!--                        </div>-->
                    <!--                      </div>-->
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="app-container-1240">
      <el-row>
        <allkcView id="visit1" :title="{name:'推荐课程',url:'/etp/curriculum/components/curriculumAll/'}"
          :data="allCurriculumList"></allkcView>
        <kcView id="visit2" :title="{name:'我的课程',url:'/curriculum'}" :data="curriculumList"></kcView>
        <ksView id="visit3" :title="{name:'我的考试',url:'/examInfo'}" :data="examinationList"></ksView>
        <rwView id="visit4" :title="{name:'我的任务',url:'/task'}" :data="taskList"></rwView>
        <!--        <rwView id="visit5"-->
        <!--                :title="{name:'我的练习',url:''}"-->
        <!--                :data="[]"-->
        <!--        ></rwView>-->
        <!--        <wjView id="visit6"-->
        <!--                :title="{name:'我的问卷',url:'/questionnaire'}"-->
        <!--                :data="questionnaireList"-->
        <!--        ></wjView>-->
      </el-row>
    </div>
    <div id='testNavBar'>
      <div class="nav-area" >
        <ul>
          <li :class="{'activeMenuCss':active_menu==1}" @click="activeLi(1)"
            style="text-align: center;padding-right: 20px;">推荐课程
          </li>
          <!--              <img :src="require('@/assets/home/'+(active_menu==1?'btn_kc_ac.png':'btn_kc_zc.png'))">-->
          <li :class="{'activeMenuCss':active_menu==2}" @click="activeLi(2)">
            <img :src="require('@/assets/home/'+(active_menu==2?'btn_kc_ac.png':'btn_kc_zc.png'))">我的课程
            <!--                <el-badge :value="curriculumList.length" style="padding-top: 8px"></el-badge>-->
          </li>
          <li :class="{'activeMenuCss':active_menu==3}" @click="activeLi(3)">
            <img :src="require('@/assets/home/'+(active_menu==3?'btn_ks_ac.png':'btn_ks_zc.png'))">我的考试
            <!--                <el-badge :value="examinationList.length" style="padding-top: 8px"></el-badge>-->
          </li>
          <li :class="{'activeMenuCss':active_menu==4}" @click="activeLi(4)">
            <img :src="require('@/assets/home/'+(active_menu==4?'btn_rw_ac.png':'btn_rw_zc.png'))">我的任务
            <!--                <el-badge :value="taskList.length" style="padding-top: 8px"></el-badge>-->
          </li>
          <!--            <li :class="{'activeMenuCss':active_menu==4}" @click="activeLi(5)">-->
          <!--              <img :src="require('@/assets/home/'+(active_menu==5?'btn_lx_ac.png':'btn_lx_zc.png'))">我的练习</li>-->
          <!--            <li :class="{'activeMenuCss':active_menu==5}" @click="activeLi(6)">-->
          <!--              <img :src="require('@/assets/home/'+(active_menu==6?'btn_wj_ac.png':'btn_wj_zc.png'))">我的问卷</li>-->
        </ul>
      </div>
    </div>
    <div class="ele-right">
      <div>
        <img src="../../assets/home/icon_ewm.png">
        <div class="ewm">
          <div>
            <div class="app" :style="{color:isapp?'#282827':'#999999'}" @mouseenter="changeImageSrc(1, 'hover')">
              app下载 </div>
            <div class="gzh" :style="{color:!isapp?'#282827':'#999999'}" @mouseenter="changeImageSrc(2, 'hover')"> 小程序
            </div>
          </div>
          <el-divider></el-divider>
          <div>
            <div class="app-img">
              <vueQr :text="app_download_url" :size="270" v-show="isapp"></vueQr>
              <vueQr :text="wx_program_url" :size="270" v-show="!isapp"></vueQr>
<!--              <img src="../../assets/images/ewm/xcx1.jpg" v-show="!isapp">-->
            </div>
            <div class="app-img">
              <span v-show="isapp">扫码下载APP 开始学习吧！</span>
              <span v-show="!isapp">扫小程序 开始学习吧！</span>
            </div>
          </div>
        </div>
      </div>
      <div @click="openDeepseek">
<!--        对话-->
        <img src="../../assets/home/icon_dh.png" style="margin-top: 12px;">
      </div>
      <!--        <div>-->
      <!--          <img src="../../assets/home/icon_bz.png">-->
      <!--        </div>-->
      <!--        <div>-->
      <!--          <img src="../../assets/home/icon_sz.png">-->
      <!--        </div>-->
      <div @click="toTop">
        <img src="../../assets/home/icon_top.png">
      </div>
    </div>
    <div style="  width: 100%;  height: 90px;  background: #282928; margin-top:20px;">
        <div class="botm-footer">
          <div class="info">
            <div>
              <!--            <span>关于我们</span>-->
              <!--            <span>联系我们</span>-->
              <!--            <span>帮助中心</span>-->
            </div>
            <!--          <div>-->
            <!--            Copyright©2019 Sinosoft.com.cn 京ICP备05055577号-1 京公网安备11010802024207号-->
            <!--          </div>-->
          </div>
            <div  class="info">
              <span>推荐课程</span>
              <span>我的课程</span>
              <span>我的考试</span>
              <span>我的任务</span>
            </div>
            <div class="app-img">
              <div class="app1-img" v-show="seen">
                <vueQr :text="app_download_url" :size="270"></vueQr>
<!--                <img src="../../assets/images/ewm/app2.png">-->
              </div>
              <div class="bottom-app-img" @mouseenter="enter" @mouseleave="leave"> app 下载</div>
            </div>
            <div class="app-img">
              <div class="app1-img" v-show="seen1">
                <vueQr :text="wx_program_url" :size="270"></vueQr>
<!--                <img src="../../assets/images/ewm/xcx1.jpg">-->
              </div>
              <div class="bottom-app-img"  @mouseenter="enter1" @mouseleave="leave1" > 小程序</div>
            </div>

        </div>
      </div>

    <dh-view
    :dialog-visible="openDeepseekDiaLog"
    @close="closeDeepseekDiaLog"></dh-view>
  </div>


</template>

<script>
import allkcView from "./components/allKc";
import kcView from "./components/kc";
import ksView from "./components/ks";
import rwView from "./components/rw";
import wjView from "./components/wj";
import dhView from "./components/dh.vue"
import {
  getKnowledgeInfo,
  listCurriculum,
} from "@/api/etp/curriculum/curriculum";
import { listTask } from "@/api/etp/task/task";
import { listExam } from "@/api/etp/exam/exam";
import fm from "@/assets/images/examine_cover.png";
import { listQuestionnaire } from "@/api/etp/questionnaire/questionnaire";
import {
  kctjNum,
  rwtjNum,
  kstjNum,
  queryCarousel,
  zstjNum,
  lastStudy,
  comingEndExam,
} from "@/api/home";
import {getNewApp} from "@/api/etp";
import vueQr from 'vue-qr/src/packages/vue-qr'
import {getConfigKey} from "@/api/system/config";
export default {
  name: "index",
  components: {
    allkcView,
    kcView,
    ksView,
    rwView,
    wjView,
    queryCarousel,
    dhView,
    vueQr
  },
  data() {
    return {
      // 鼠标移入显示
      seen1: false,
      seen: false,
      active_menu: 1,
      //全部课程
      allCurriculumList: [],
      //我的课程
      curriculumList: [],
      //我的考试
      examinationList: [],
      //我的任务
      taskList: [],
      questionnaireList: [],
      comeEndExam: {},
      kctj: {},
      rwtj: {},
      kstj: {},
      zstj: 0,
      studyKc: {
        mc: "",
      },
      isapp: true,
      img_url_b: require("@/assets/home/icon_more_b.png"),
      img_url_h: require("@/assets/home/icon_more_q.png"),
      img_index: 0,
      //轮播图
      carouselUrls: [require("@/assets/images/banner/20220309171001.jpg")],
      ksParams: {
        pageNum: 1,
        pageSize: 8,
        name: null,
        content: null,
        orders: "update_time,desc",
      },
      isnavshow: false,
      cateList: [],
      cateInfo: [],
      config_list: {},
      isfixTab: false,
      openDeepseekDiaLog: false,
      app_download_url: null, // 最新版本app下载地址
      wx_program_url: null , // 微信小程序地址
    };
  },
  created() {
    this.getKctjNum();
    this.getRwtjNum();
    this.getKstjNum();
    this.getZstjNum();
    this.getAllCurriculumList();
    this.getCurriculumList();
    this.getExamList();
    this.getTaskList();
    this.getQuestionnaireList();
    //轮播图
    this.queryCarousel();
    this.getLastStudyKc();
    this.getEndExam();
    // 查询最新版本的app下载地址
    this.queryAppDownUrl();
    // 查询微信小程序地址
    this.queryWxProgram();
  },
  // 监听页面滚动
  mounted() {
    window.addEventListener("scroll", this.scrollHandle, true); //绑定页面滚动事件
  },
  methods: {
    //离开当前组件前一定要清除滚动监听，否则进入其他路由会报错
    // beforeRouteLeave(to, from, next) {
    //   window.removeEventListener('scroll', this.handleTabFix, true)
    //   next()
    // },
    enter() {
      this.seen = true;
    },
    leave() {
      this.seen = false;
    },
    enter1() {
      this.seen1 = true;
    },
    leave1() {
      this.seen1 = false;
    },

    queryAppDownUrl() {
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
    },
    queryWxProgram() {
      getConfigKey('wx.program.url').then(res => {
        this.wx_program_url = res.msg;
      }).catch(error => {
        this.message.error("获取微信小程序地址失败")
      })
    },

    scrollHandle(e) {
      this.scrolltop = e.srcElement.scrollingElement.scrollTop; // 获取页面滚动高度
      if (this.scrolltop > 454) {
        this.isfixTab = true;
      } else {
        this.isfixTab = false;
      }
    },
    // 鼠标移入移出显示

    toExam() {
      if (
        this.comeEndExam.chance != "0" &&
        this.comeEndExam.chance <= this.comeEndExam.usedChance
      ) {
        this.$message.error("您的考试次数已用完！");
      } else {
        let router = this.$router.resolve(
          "examStart/detail/" + this.comeEndExam.examId
        );
        window.open(router.href, "_blank");
      }
    },

    //跳转至课程学习页面
    clickCurriculumAll() {
      let router = this.$router.resolve({
        path: "/etp/curriculum/previewCurriculum/",
        query: {
          cid: this.studyKc.id,
          getStatus: null,
        },
      });
      window.open(router.href, "_blank");
    },

    //回到浏览器顶部
    toTop() {
      // console.log(document.querySelector(".app-container-1240").scrollTop);
      let scrollTop =
        document.body.scrollTop || document.documentElement.scrollTop;
      // if(document.body.scrollTop!=0) document.body.scrollTop = 0
      if (document.documentElement.scrollTop != 0)
        document.documentElement.scrollTop = 0;
    },

    queryCarousel() {
      queryCarousel({
        pageNum: 1,
        pageSize: 4,
      }).then(res => {
        const data = res.data;
        if (null != data && data.length > 0) {
          this.carouselUrls = [];
          for (const datum of data) {
            const { imageurl } = datum;
            this.carouselUrls.push(imageurl);
          }
        }
      });
    },

    activeLi(index) {
      this.active_menu = index;
      this.jumpClick(index);
    },
    //锚点
    jumpClick(index) {
      document.querySelector(`#visit${index}`).scrollIntoView({
        behavior: "smooth",
        block: "center",
      });
    },
    getKctjNum() {
      kctjNum().then(res => {
        this.kctj = res.data;
      });
    },
    getRwtjNum() {
      rwtjNum().then(res => {
        this.rwtj = res.data;
      });
    },
    getKstjNum() {
      kstjNum().then(res => {
        this.kstj = res.data;
      });
    },
    getZstjNum() {
      zstjNum().then(res => {
        // console.log(res)
        this.zstj = res.data;
      });
    },
    getLastStudyKc() {
      lastStudy().then(res => {
        this.studyKc = res.data;
      });
    },

    getAllCurriculumList() {
      this.loading = true;
      getKnowledgeInfo({
        pageNum: 1,
        pageSize: 4,
      }).then(response => {
        let list = response.rows;

        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fmt == null ||
            list[i].fmt == "" ||
            list[i].fmt == undefined
          ) {
            list[i].fmt = require("@/assets/images/cover.jpg");
          }
        }
        this.allCurriculumList = list;

        this.loading = false;
      });
    },
    /** 查询curriculum列表 */
    getCurriculumList() {
      this.loading = true;
      listCurriculum({
        pageNum: 1,
        pageSize: 8,
        jd: "最近学习",
      }).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fmt == null ||
            list[i].fmt == "" ||
            list[i].fmt == undefined
          ) {
            list[i].fmt = require("@/assets/images/cover.png");
          }
        }
        this.curriculumList = list;
      });
    },
    /** 查询考试列表 */
    getExamList() {
      listExam(this.ksParams).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].cover == null ||
            list[i].cover == "" ||
            list[i].cover == undefined
          ) {
            let fmt = require("@/assets/images/examine_cover.png");
            list[i].fmt = fmt;
          } else {
            list[i].fmt = list[i].cover;
          }
          let now = new Date().getTime();
          if (list[i].startTime != null && list[i].startTime != "") {
            let start = Date.parse(list[i].startTime);
            let end = Date.parse(list[i].endTime);
            if (now > start && now < end) {
              list[i].examStatus = "进行中";
            } else if (now < start) {
              list[i].examStatus = "未开始";
            } else if (now > end) {
              list[i].examStatus = "已结束";
            }
          } else {
            list[i].examStatus = "进行中";
          }
        }
        this.examinationList = list;
      });
    },

    /** 查询即将结束的考试 */
    getEndExam() {
      let params = {
        orders: "end_time,desc",
      };
      comingEndExam(params).then(response => {
        console.log(this.comeEndExam);
        this.comeEndExam = response.data;
        console.log(this.comeEndExam);
      });
    },

    /** 查询任务列表 */
    getTaskList() {
      listTask({
        pageNum: 1,
        pageSize: 8,
      }).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fmt == null ||
            list[i].fmt == "" ||
            list[i].fmt == undefined
          ) {
            list[i].fmt = require("@/assets/images/task_cover.png");
          }
        }
        this.taskList = list;
      });
    },
    /** 查询问卷调查列表 */
    getQuestionnaireList() {
      listQuestionnaire(this.queryParams).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fm == null ||
            list[i].fm == "" ||
            list[i].fm == undefined
          ) {
            list[i].fm = require("@/assets/images/cover.jpg");
          }
        }
        this.questionnaireList = list;
      });
    },
    changeImageSrc(key, way) {
      if (way === "hover" && key == 1) {
        this.isapp = true;
      } else {
        this.isapp = false;
      }
    },
    changeImg(index) {
      this.img_index = index;
    },
    openDeepseek(){
      this.openDeepseekDiaLog =true
    },
    closeDeepseekDiaLog(){
      this.openDeepseekDiaLog =false
    }
  },
};
</script>

<style scoped lang="scss">
.background-div {
  background: linear-gradient(
    to bottom,
    #f3f3f3 0%,
    #f3f3f3 20%,
    white 20%,
    white 100%
  );
  background-size: 350px, 550px;
}
.app-main {
  width: 100% !important;
}
.app-container-1240 {
  width: 1240px;
  padding-top: 10px;
  padding-left: 20px;
  padding-right: 20px;
  margin: 0 auto;
  overflow: auto;
  color: #333333;
  min-height: 800px;
  overflow: hidden;
}
.top {
  // display: grid;
  grid-gap: 20px;
  grid-template-columns: 285px 285px 285px 285px;
  height: 142px;
  > div {
    height: 176px;
    box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.15);
    border-radius: 8px;
  }
}
.t-t-div {
  background-color: white;
  border-radius: 8px;
  height: 170px;
}
.t-t-item {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: 16px;
  font-weight: 500;
  line-height: 38px;
  padding-right: 16px;
  > div:nth-of-type(2) {
    cursor: pointer;
    &:hover span {
      color: #0045c5;
    }
  }
  .t-title {
    width: 86px;
    height: 38px;
    background: #e5ecf9;
    border-top-left-radius: 8px;
    border-bottom-right-radius: 26px;
    > span {
      color: #0045c5;
      margin-left: 24px;
      font-weight: 600;
    }
  }
  span {
    color: #999999;
    line-height: 20px;
  }
  img {
    vertical-align: sub;
    width: 24px;
    height: 24px;
  }
}
.one-item {
  display: flex;
  text-align: center;
  height: 60px;
  align-items: center;
  > div {
    width: 100%;
  }
}
.one-item-zs {
  display: flex;
  text-align: center;
  height: 100px;
  align-items: center;
  > div {
    width: 100%;
  }
}
.two-item {
  display: flex;
  text-align: center;
  height: 66px;
  align-items: center;
  > div:nth-of-type(1) {
    border-right: 1px solid #eeeeee;
  }
  > div {
    width: 50%;
  }
}
.three-item {
  display: flex;
  text-align: center;
  height: 60px;
  align-items: center;
  > div:not(:nth-of-type(3)) {
    border-right: 1px solid #eeeeee;
  }
  > div {
    width: calc(100% / 3);
  }
}
.three-item-test {
  display: flex;
  height: 66px;
  align-items: center;
  border-top: 1px solid #eeeeee;
  > div {
    width: calc(100% / 3);
  }
}
.eve-item {
  > div:nth-of-type(1) {
    font-size: 25px;
    line-height: 20px;
    margin-bottom: 3px;
    font-weight: 500; // cursor: pointer;
    &:hover {
      color: #0045c5;
    }
  }
  div:nth-of-type(2) {
    font-size: 14px;
    color: #999999;
  }
  .jp {
    background-image: url("../../assets/home/btn_jp.png");
    background-repeat: no-repeat;
    height: 48px;
    width: 48px;
    color: #fff;
    line-height: 55px;
    margin: 0 auto;
  }
}
.eve-item-button {
  > div:nth-of-type(1) {
    font-size: 12px;
    line-height: 20px;
    margin-bottom: 3px;
    background-color: #ff5558;
    color: white;
    border-radius: 4px;
    text-align: center;
    // cursor: pointer;
    // &:hover{
    // color: #0045C5;
    // }
  }
  div:nth-of-type(2) {
    font-size: 14px;
  }
  .jp {
    background-image: url("../../assets/home/btn_jp.png");
    background-repeat: no-repeat;
    height: 48px;
    width: 48px;
    color: #fff;
    line-height: 55px;
    margin: 0 auto;
  }
}
.nav-area {
  width: 138px;
  position: fixed;
  top: 46vh;
  // left: calc((100% - 1200px) / 2 - 185px);
  left: 8px;
  @media screen and (max-width: 1600px) {
    left: 25px;
  }
  font-size: 16px;
  font-weight: 600;
  color: #999999;
  line-height: 48px;
  background-color: white;
  box-shadow: 0px 1px 15px 5px rgba(0, 0, 0, 0.03);
  border-radius: 8px;
  margin-left: -10px;
  z-index: 9999999;
  ul {
    padding: 0;
  }
  li {
    list-style-type: none;
    cursor: pointer;
    padding-left: 15px;
    img {
      margin-right: 10px;
      vertical-align: sub;
    }
    &:hover {
      color: #333333;
    }
  }
  .activeMenuCss {
    background: #eeeeee;
    color: #333333;
  }
}
.botm-footer {
  width: 1240px;
  padding-left: 20px;
  margin: 0 auto;
  display: flex;
  color: #999999;
  font-size: 16px;
  .info {
    // margin-left: calc((100% - 1200px) / 2);
    margin-top: 36px;
    span {
      color: #fff;
      margin-right: 30px;
    }
    > div:nth-of-type(2) {
      width: 400px;
      margin-top: 20px;
      line-height: 20px;
    }
  }
  .app-img {
    margin-top: 30px;
    // margin-right: 20px;
    text-align: center;
  }
  .app1-img {
    width: 100px;
    height: 100px;
    background: #fff;
    border-radius: 8px;
    position: absolute;
    bottom: 70px;
    margin-left: 20px;
    box-shadow: 0 4px 8px 0 rgb(7 17 27 / 10%);
  }
}
.app-img {
  img {
    width: 90px;
    height: 90px;
    margin-top: 5px;
  }
}
.bottom-app-img {
  border: 1px solid #999;
  background: #000;
  height: 36px;
  line-height: 34px;
  padding: 0 20px;
  border-radius: 18px;
  width: 120px;
  margin-left: 10px;
}
.ele-right {
  position: fixed;
  top: 55%;
  right: 0;
  width: 56px;
  background: #fff;
  box-shadow: 0 4px 8px 0 rgb(7 17 27 / 10%);
  border-radius: 8px 0 0 8px;
  > div {
    margin: 0 auto;
    position: relative;
    width: 100%;
    height: 56px;
    color: #b5b9bc;
    text-align: center;
    cursor: pointer;
    &:hover {
      background-color: #cde2f6;
    }
    > img {
      margin-top: 18px;
    }
  }
  > div:nth-of-type(1) {
    &:hover .ewm {
      display: block;
    }
  }
  .ewm {
    display: none;
    width: 226px;
    height: 160px;
    position: absolute;
    right: 58px;
    top: 5px;
    background: #ffffff;
    box-shadow: 0px 1px 20px 0px rgba(0, 0, 0, 0.15);
    border-radius: 8px;
    padding: 10px 10px;
    > div {
      display: flex;
      div {
        width: 50%;
      }
    }
    .app-img {
      text-align: left;
    }
    span {
      color: #282827;
    }
    ::v-deep .el-divider--horizontal {
      margin: 10px 0;
    }
  }
}
::v-deep .el-button--mini.is-round {
  background-color: #0045c5;
  border: none;
  font-size: 14px;
  padding: 5px 10px;
}
::v-deep .el-tabs__item {
  padding: 0 10px;
}
::v-deep .el-tabs__item.is-active {
  color: #0045c5;
}
::v-deep .el-tabs__nav-wrap::after {
  background-color: #fff;
}
::v-deep .el-tabs__active-bar {
  background-color: #0045c5;
}
::v-deep .el-tabs__nav {
  padding-top: 2px;
}
.fixedNavbar {
  position: fixed;
  z-index: 2032;
  top: 20px;
}
</style>
