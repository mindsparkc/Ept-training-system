<template>
  <!-- 课程预览-->
  <div class="app-container-xq">
<!--    <div v-if="this.getStatus=='0'">
      <el-button @click="receiveCurriculum()" type="primary" style="margin-left: 84%">领取课程</el-button>
    </div>-->

    <div class="btm-content">
      <div v-if="isActive" class="pre-list pre-pad">
        <div class="desc-area">
          <div class="text-mc" :title="kcmc">{{ kcmc }}</div>
          <div class="text-ms">{{ kcms }}</div>
          <div class="text-ms xx"><i class="el-icon-user-solid"></i>{{ xxrs }} 人在学习</div>
          <div v-if="this.getStatus=='0'" class="rece-btn">
            <el-button v-if="this.requestFrom!='admin'" @click="receiveCurriculum()" type="primary" size="mini">领取课程</el-button>
          </div>
        </div>
        <el-divider></el-divider>
        <div class="ml-title"><div></div>课程目录</div>
        <el-tree :data="treeData" :props="defaultProps"
                 highlight-current
                 node-key="sid"
                 :render-after-expand="false"
                 :render-content="renderContent"
                 :filter-node-method="filterNode"
                 ref="tree" @node-click="openContent">

        </el-tree>
      </div>
      <el-divider direction="vertical" v-if="isActive"></el-divider>
      <!-- 课程内容  -->
      <div class="pre-content pre-pad" :style="{width:(isActive?'80%':'100%')}">
        <Screenfull id="screenfull" class="right-menu-item hover-effect" />
        <Hamburger id="hamburger-container" :title="kcmc" :is-active="isActive" class="hamburger-container" @toggleClick="toggleSideBar" />
        <!-- 这里是头像，修改为仅登录状态下显示头像 -->
        <el-dropdown class="avatar-container right-menu-item hover-effect" v-if="showAvatar" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatar" class="user-avatar">
            <i class="el-icon-caret-bottom" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <!-- 仅开发模式下允许启动布局设置 -->
<!--            <el-dropdown-item v-if="showSetting"  @click.native="setting = true" >-->
<!--              <span>布局设置</span>-->
<!--            </el-dropdown-item>-->
            <el-dropdown-item divided @click.native="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <el-divider></el-divider>
        <div class="el-empty__image" v-show="tipFlag">
          <svg viewBox="0 0 79 86" version="1.1" xmlns="http://www.w3.org/2000/svg"
               xmlns:xlink="http://www.w3.org/1999/xlink">
            <defs>
              <linearGradient id="linearGradient-1-10" x1="38.8503086%" y1="0%" x2="61.1496914%" y2="100%">
                <stop stop-color="#FCFCFD" offset="0%"></stop>
                <stop stop-color="#EEEFF3" offset="100%"></stop>
              </linearGradient>
              <linearGradient id="linearGradient-2-10" x1="0%" y1="9.5%" x2="100%" y2="90.5%">
                <stop stop-color="#FCFCFD" offset="0%"></stop>
                <stop stop-color="#E9EBEF" offset="100%"></stop>
              </linearGradient>
              <rect id="path-3-10" x="0" y="0" width="17" height="36"></rect>
            </defs>
            <g id="Illustrations" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
              <g id="B-type" transform="translate(-1268.000000, -535.000000)">
                <g id="Group-2" transform="translate(1268.000000, 535.000000)">
                  <path id="Oval-Copy-2"
                        d="M39.5,86 C61.3152476,86 79,83.9106622 79,81.3333333 C79,78.7560045 57.3152476,78 35.5,78 C13.6847524,78 0,78.7560045 0,81.3333333 C0,83.9106622 17.6847524,86 39.5,86 Z"
                        fill="#F7F8FC"></path>
                  <polygon id="Rectangle-Copy-14" fill="#E5E7E9"
                           transform="translate(27.500000, 51.500000) scale(1, -1) translate(-27.500000, -51.500000) "
                           points="13 58 53 58 42 45 2 45"></polygon>
                  <g id="Group-Copy"
                     transform="translate(34.500000, 31.500000) scale(-1, 1) rotate(-25.000000) translate(-34.500000, -31.500000) translate(7.000000, 10.000000)">
                    <polygon id="Rectangle-Copy-10" fill="#E5E7E9"
                             transform="translate(11.500000, 5.000000) scale(1, -1) translate(-11.500000, -5.000000) "
                             points="2.84078316e-14 3 18 3 23 7 5 7"></polygon>
                    <polygon id="Rectangle-Copy-11" fill="#EDEEF2"
                             points="-3.69149156e-15 7 38 7 38 43 -3.69149156e-15 43"></polygon>
                    <rect id="Rectangle-Copy-12" fill="url(#linearGradient-1-10)"
                          transform="translate(46.500000, 25.000000) scale(-1, 1) translate(-46.500000, -25.000000) "
                          x="38" y="7" width="17" height="36"></rect>
                    <polygon id="Rectangle-Copy-13" fill="#F8F9FB"
                             transform="translate(39.500000, 3.500000) scale(-1, 1) translate(-39.500000, -3.500000) "
                             points="24 7 41 7 55 -3.63806207e-12 38 -3.63806207e-12"></polygon>
                  </g>
                  <rect id="Rectangle-Copy-15" fill="url(#linearGradient-2-10)" x="13" y="45" width="40"
                        height="36"></rect>
                  <g id="Rectangle-Copy-17" transform="translate(53.000000, 45.000000)">
                    <mask id="mask-4-10" fill="white">
                      <use xlink:href="#path-3-10"></use>
                    </mask>
                    <use id="Mask" fill="#E0E3E9"
                         transform="translate(8.500000, 18.000000) scale(-1, 1) translate(-8.500000, -18.000000) "
                         xlink:href="#path-3-10"></use>
                    <polygon id="Rectangle-Copy" fill="#D5D7DE" mask="url(#mask-4-10)"
                             transform="translate(12.000000, 9.000000) scale(-1, 1) translate(-12.000000, -9.000000) "
                             points="7 0 24 0 20 18 -1.70530257e-13 16"></polygon>
                  </g>
                  <polygon id="Rectangle-Copy-18" fill="#F8F9FB"
                           transform="translate(66.000000, 51.500000) scale(-1, 1) translate(-66.000000, -51.500000) "
                           points="62 45 79 45 70 58 53 58"></polygon>
                </g>
              </g>
            </g>
          </svg>
          <div class="desc">课程学习</div>
        </div>
        <!-- 课件预览 -->
        <div class="kj-view"
             v-show="!tipFlag && kjFlag">
          <div class="kj-title"><i class="el-icon-notebook-2"></i>{{kjmc}}
            <span v-if="this.requestFrom!='admin'" style="margin-left: 20px;color: #868686;font-size: 14px">总需学习时长：{{learnTime}} 分钟</span>
            <span v-else style="margin-left: 20px;color: #868686;font-size: 14px">总需学习时长：-- 分钟</span>
            <span v-if="this.requestFrom!='admin'" style="margin-left: 38px;color: #868686;font-size: 14px">历史已学习：{{lastStudyTime}} 分钟</span>
            <span v-else style="margin-left: 38px;color: #868686;font-size: 14px">历史已学习：-- 分钟</span>
            <span v-if="this.requestFrom!='admin'" style="margin-left: 50px;color: #868686;font-size: 14px">学习中：{{studyTime}}</span>
            <span v-else style="margin-left: 50px;color: #868686;font-size: 14px">学习中：--:--:--</span>
          </div>
          <video width="96%" controls autoplay v-if="kjlx=='2'" style="display: block;margin: 10px auto 0px;height: calc(100% - 200px);" id="myVideo" ref="videoPlay">
            <source :src="url" >
          </video>
<!--          视频下面加课件描述-->
          <div class="desc-btom" v-if="kjlx=='2'">
            <div class="line-heng"></div>
            <span>{{kjms}}</span>
          </div>
          <div style="height: 100%;height:calc(100% - 80px);" v-if="iframeFlag">
            <scrollLoad :list="picList" :total="picTotal" :host="picHost" ref="pdfSvg"></scrollLoad>
          </div>

          <div v-show="richFlag" style="padding: 20px 50px;height: 90%;overflow:auto;" v-html="nr"></div>
          <div class="a-item" v-show="aHref.flag" >
            <!--            <div class="a-title">即将离开本站</div>-->
            <div class="a-content">课件名称：{{aHref.name}}</div>
            <div class="a-content">课件地址：{{aHref.address}}</div>
            <div class="a-content">点击【继续访问】即将离开本站，在新的页面打开课件内容</div>
            <el-divider></el-divider>
            <el-button type="primary" @click="visitUrl(aHref.address)">继续访问</el-button>
          </div>
        </div>
        <!--  试题预览-->
        <div style="width: 100%;height: 100%;overflow:auto;padding: 15px" v-show="!tipFlag && stFlag">
          <el-form ref="form" :model="subjectForm" label-width="100px" size="small">
            <el-form-item label="试题名称：">
              <span v-html="subjectForm.tg"></span>
            </el-form-item>
            <el-form-item label="试题类型：">
              <!-- 单选题：1 复选题：2 判断题：3 填空题：4 主观题：5   -->
              <span v-if="subjectForm.lx == '1'">单选题</span>
              <span v-if="subjectForm.lx == '2'">复选题</span>
              <span v-if="subjectForm.lx == '3'">判断题</span>
              <span v-if="subjectForm.lx == '4'">填空题</span>
              <span v-if="subjectForm.lx == '5'">主观题</span>
            </el-form-item>
            <el-form-item label="请选择：" v-if="subjectForm.lx == '3'">
              <el-radio-group v-model="pd">
                <el-radio label="正确"></el-radio>
                <el-radio label="错误"></el-radio>
              </el-radio-group>
              <el-row>
                <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
              </el-row>
            </el-form-item>
            <el-form-item label="请填写：" v-if="subjectForm.lx =='4'">
              <template  v-for="(data,dIndex) in subjectForm.place">
                <a>({{dIndex+1}})</a><el-input class="customStyle" :key="dIndex"  v-model="tk[dIndex]" size="nimi" style="width: 600px;padding: 10px"></el-input><br/>
              </template>
<!--              <el-input v-model="tk" placeholder="请输入答案" v-if="subjectForm.lx =='4'" style="width: 60%"/>-->
              <el-row>
                <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
              </el-row>
            </el-form-item>
            <el-form-item label="请填写：" v-if="subjectForm.lx == '5'">
              <el-input type="textarea" :rows="2" placeholder="请输入答案" v-model="zg" v-if="subjectForm.lx == '5'" style="width: 60%"/>
              <el-row>
                <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
              </el-row>
            </el-form-item>
            <el-form-item label="选项内容：" v-if="subjectForm.lx != '3' && subjectForm.lx !='5' && subjectForm.lx != '4'">
              <template v-if="subjectForm.lx == '1'">
                <el-radio-group v-model="dx">
                  <el-radio v-for="(item,index) in subjectForm.xxList" :label="item.key" :key="item.key" @change="onInput()">
                    {{ item.key }} 、{{ item.value }}
                  </el-radio>
                </el-radio-group>
                <el-row>
                  <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
                </el-row>
              </template>
              <template v-if="subjectForm.lx == '2'">
                <!--              <span :key="index" v-for="(item,index) in subjectForm.xxList">{{ item.key }} 、 {{-->
                <!--                  item.value-->
                <!--                }} <br/></span>-->

                <el-checkbox-group v-model="duo">
                  <el-checkbox v-for="(item,index) in subjectForm.xxList" :label="item.key" :key="item.key" @change="onInput()">{{ item.key }} 、{{ item.value }}</el-checkbox>
                </el-checkbox-group>
                <el-button type="success" @click="showDa()">提交</el-button>
              </template>
            </el-form-item>
            <el-form-item label="试题答案：" v-if="dxt || dxts || pdt || tkt || zgt">
              <template v-if="subjectForm.lx == '3'">
                <span>{{ subjectForm.da == '1' ? '正确' : '错误' }}</span>
              </template>
              <template v-if="subjectForm.lx == '4'" >
                <span v-for="(val,key) in JSON.parse(subjectForm.da)">
                   <span v-for="(v,k) in val">{{v}}</span><br/>
                </span>
              </template>
              <template v-if="subjectForm.lx != '3' && subjectForm.lx!='4'">
                <span>{{ subjectForm.da }}</span>
              </template>
            </el-form-item>
            <el-form-item label="试题解析：" v-if="dxt || dxts || pdt || tkt || zgt">
              <span>{{ subjectForm.jx }}</span>
            </el-form-item>
          </el-form>
        </div>

        <!--  习题集    -->
        <div style="width: 100%; height: calc(100% - 67px);overflow:auto;padding: 15px" v-show="!tipFlag && xtjFlag">
          <train ref="train"
          :cid="cid"
          :train-id="trainId"></train>
        </div>



      </div>
    </div>
  </div>
</template>

<script>
import {
  selectTCurriculumByIds,
  updateStatus,
  showInitContent,
  receiveCurriculum,
  getCurriculumSubject,
  addCurriculumSubject,
  getKnowledgeInfo,
} from "@/api/etp/curriculum/curriculum";
import { getCourseware } from "@/api/etp/courseware/courseware";
import { getSubject } from "@/api/etp/subject/subject";
import { FILE_PREVIEW_URL } from "@/utils/ruoyi";
import scrollLoad from "@/components/scrollLoad/index";
import { clickSendRedis, stopSendRedis } from "@/api/buryPoint";
import Hamburger from "@/components/Hamburger";
import { getToken } from "@/utils/auth";
import { mapGetters } from "vuex";
import Screenfull from "@/components/Screenfull";
import train from "@/views/etp/curriculum/components/train";

export default {
  name: "previewCurriculum",
  components: {
    scrollLoad,
    Hamburger,
    Screenfull,
    train,
  },
  computed: {
    ...mapGetters(["sidebar", "avatar"]),

    showAvatar: {
      get() {
        return getToken();
      },
    },
  },
  data() {
    return {
      tipFlag: true,
      kjFlag: false,
      stFlag: false,
      xtjFlag: false,
      iframeFlag: false,
      richFlag: false,
      dxt: false,
      dxts: false,
      pdt: false,
      tkt: false,
      zgt: false,
      nr: "",
      url: "",
      filterText: "",
      curriculum: {},
      treeData: [
        { label: "一级 1" },
        // {label : '1、js公开课'}
      ],
      defaultProps: {
        children: "children",
        label: "label",
      },
      subjectForm: {},
      dx: "",
      duo: [],
      pd: "",
      tk: {},
      zg: "",
      tags: [],

      cid: "", //课程id
      kjlx: "",
      jd: null, //课程进度
      contentIds: null, //记录课程中的所有课件
      update: true, //更新进度标记
      aHref: {
        flag: false,
        name: null,
        address: null,
      },
      getStatus: null,
      subjectArr: {}, //课程习题对象

      //文件图片
      picList: [],
      picTotal: 0,
      picHost: "",

      timer: null,
      isActive: true,

      kcmc: "", //课程名称
      kcms: "", //课程描述
      xxrs: "", //学习人数
      //学习的课件名称
      kjmc: "",
      kjms: "",
      requestFrom: null,
      learnTime: 0,
      lastStudyTime: 0,

      selectRow: {}, //记录当前点击课件id
      //计时器参数
      flag: null,
      hour: 0,
      minute: 0,
      second: 0,
      studyTime: "00:00:00",
      videoStatus: null,
      trainId: null,
    };
  },
  destroyed() {
    clearInterval(this.timer);
    document.removeEventListener("visibilitychange", this.handleVisiable);
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  mounted() {},
  created() {
    this.requestFrom = this.$route.query.requestFrom;
    if (this.requestFrom != "admin") {
      document.title = "学员端 · 培训考试系统";
    } else {
      document.title = "管理端 · 培训考试系统";
    }
    this.cid = this.$route.query.cid;
    //进度
    this.jd = this.$route.query.jd;
    //类型
    this.getStatus = this.$route.query.getStatus;
    if (null == this.getStatus) {
      //查看领取状态
      getKnowledgeInfo({ id: this.cid }).then(res => {
        if (res.rows[0].getStatus != null) {
          this.getStatus = res.rows[0].getStatus;
        } else {
          this.getStatus = 0;
        }
      });
    }

    if (this.getStatus == "0") this.update = false;
    console.log(this.getStatus);
    if (this.cid !== undefined) {
      this.getCurriculum(this.cid);
    }
  },

  methods: {
    //查询当前节点的下个同级节点
    selectNextId() {
      for (let i = 0; i < this.treeData.length; i++) {
        // debugger;
        if (this.treeData[i].id == this.selectRow.id) {
          if (i < this.treeData.length - 1) {
            //重置计时器
            this.reset();
            //调用树结构点击事件，加载下个视频课件
            this.handleNodeClick(this.treeData[i + 1]);
            return;
          } else {
            //后面没有节点，则清除视频监听
            this.videoStatus.removeEventListener(
              "ended",
              this.selectNextId,
              false
            );
            return;
          }
        }
      }
    },

    //开始计时
    startTime() {
      this.flag = setInterval(() => {
        this.second = this.second + 1;
        if (this.second >= 60) {
          this.lastStudyTime = parseInt(this.lastStudyTime) + 1;
          this.second = 0;
          this.minute = this.minute + 1;
        }

        if (this.minute >= 60) {
          this.minute = 0;
          this.hour = this.hour + 1;
        }
        this.studyTime =
          this.complZero(this.hour) +
          ":" +
          this.complZero(this.minute) +
          ":" +
          this.complZero(this.second);
      }, 1000);
    },
    //重新计时
    reset() {
      window.clearInterval(this.flag);
      this.hour = 0;
      this.minute = 0;
      this.second = 0;
      this.studyTime = "00:00:00";
    },
    //暂停计时
    end() {
      this.flag = clearInterval(this.flag);
    },
    //补零
    complZero(n) {
      return n < 10 ? "0" + n : "" + n;
    },

    //监控用户切出当前页面
    handleVisiable(e) {
      switch (e.target.visibilityState) {
        case "hidden":
          console.log("内容不可见!");
          this.end();
          clearInterval(this.timer);
          this.$refs["videoPlay"].pause();
          break;
        case "visible":
          console.log("正常展示页面！");
          this.handleNodeClick(this.selectRow);
          this.reset();
          break;
      }
    },

    toggleSideBar() {
      this.isActive = !this.isActive;
    },

    /** 保存用户练习题答题记录 */
    submitSubject() {
      addCurriculumSubject(this.subjectArr).then(res => {
        if (res.code === 200) {
          console.log("保存成功");
        } else {
          console.log("保存失败");
        }
      });
    },

    /** 领取课程 */
    receiveCurriculum() {
      let json = { kcid: this.cid };
      receiveCurriculum(json).then(res => {
        this.$message.success("课程领取成功！");
      });
      // this.back();
    },

    showDa() {
      if (
        JSON.stringify(this.tk) != JSON.stringify({}) ||
        this.zg != "" ||
        this.duo != "" ||
        this.pd != "" ||
        this.dx != ""
      ) {
        this.$nextTick(_ => {
          this.dxts = true;
          this.pdt = true;
          this.tkt = true;
          this.zgt = true;
          this.dxt = true;
        });
        this.subjectArr["kcid"] = this.cid;
        this.subjectArr["stid"] = this.subjectForm.id;
        this.subjectArr["remake"] = this.kcmc;
        if (this.subjectForm.lx === "1") {
          this.subjectArr["xyda"] = this.dx;
        } else if (this.subjectForm.lx === "2") {
          this.subjectArr["xyda"] = this.duo.toString();
        } else if (this.subjectForm.lx === "3") {
          this.subjectArr["xyda"] = this.pd;
        } else if (this.subjectForm.lx === "4") {
          let tkValue = [];
          for (let key in this.tk) {
            tkValue.push(this.tk[key]);
          }
          console.log(JSON.stringify(tkValue));
          this.subjectArr["xyda"] = JSON.stringify(tkValue);
        } else {
          this.subjectArr["xyda"] = this.zg;
        }
        this.submitSubject();
      } else {
        this.$message.error("请答题后提交！");
      }
    },

    //强制刷新方法
    onInput() {
      this.$forceUpdate();
    },

    /** 返回*/
    back() {
      if (this.getStatus == null) {
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/curriculum", query: { t: Date.now() } });
      } else {
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({
          path: "/etp/curriculum/components/curriculumAll",
          query: { t: Date.now() },
        });
      }
    },
    /** 搜索过滤*/
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 标志位重置*/
    flagR() {
      this.kjFlag = false;
      this.stFlag = false;
      this.aFlag = false;
      this.xtjFlag = false;
      this.iframeFlag = false;
      this.richFlag = false;
      this.dxt = false;
      this.dxts = false;
      this.pdt = false;
      this.tkt = false;
      this.zgt = false;
      this.aHref.flag = false;
    },
    /** 每次选择试题时，清空试题答案 */
    clearSt() {
      this.dx = "";
      this.duo = [];
      this.pd = "";
      this.tk = {};
      this.zg = "";
    },

    renderContent(h, { node, data, store }) {
      // debugger;
      if (data.source_type == "kj") {
        if (data.xs == 1) {
          if (data.suffix == ".pdf") {
            return (
              <span>
                <span class="mlxx">pdf</span>
                {node.label}
              </span>
            );
          } else if (data.suffix == ".ppt") {
            return (
              <span>
                <span class="mlxx">ppt</span>
                {node.label}
              </span>
            );
          } else {
            return (
              <span>
                <span class="mlxx">office</span>
                {node.label}
              </span>
            );
          }
        } else if (data.xs == 2) {
          return (
            <span>
              <span class="mlxx">视频</span>
              {node.label}
            </span>
          );
        } else if (data.xs == 3) {
          return (
            <span class="mlxx">
              <a>富文本</a>
              {node.label}
            </span>
          );
        } else if (data.xs == 4) {
          return (
            <span class="mlxx">
              <a>外部链接</a>
              {node.label}
            </span>
          );
        } else {
          return (
            <span class="mlxx">
              <span>其他文件</span>
              {node.label}
            </span>
          );
        }

        if (data.rate == 100) {
          return (
            <span>
              <i class="el-icon-wc" />
              {node.label}
            </span>
          );
        } else if (data.rate > 0 && data.rate < 100) {
          return (
            <span>
              <i class="el-icon-jd" />
              {node.label}
            </span>
          );
        } else {
          return <span>{node.label}</span>;
        }
      } else {
        return <span style="font-size: 14px;">{node.label}</span>;
      }
    },
    /** 依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身*/
    handleNodeClick(row) {
      let { sid, source_type } = row;
      this.selectRow = row;
      this.flagR();
      this.clearSt();
      this.jd = sid;
      this.kjlx = "";
      this.kjmc = "";
      this.$nextTick(() => {
        this.$refs.tree.setCurrentKey(sid);
      });
      this.tipFlag = false;
      if (source_type == "kj") {
        // 预览课件
        this.kjFlag = true;
        getCourseware(sid).then(res => {
          /**课件形式Office：1 视频：2 富文本：3 外部链接：4 其他文件：5*/
          let {
            xs,
            kjdz,
            nr,
            lj,
            mc,
            ms,
            metaData,
            learnTime,
            lastStudyTime,
          } = res.data;
          this.kjmc = mc;
          this.kjlx = xs;
          this.kjms = ms;
          this.learnTime = learnTime;
          this.lastStudyTime = lastStudyTime;
          if (xs == "1" || xs == "5") {
            this.iframeFlag = true;
            // this.$nextTick(_ => {
            //   this.url = this.previewUrl() + '?url=' + encodeURIComponent(window.btoa(kjdz));
            // })
            let { list, size } = JSON.parse(metaData);
            this.picList = list;
            this.picTotal = size;
            // this.picHost = 'http://10.153.3.12'+kjdz.substring(21,kjdz.indexOf("."))+'/'
            this.picHost = kjdz.substring(0, kjdz.lastIndexOf(".")) + "/";
          }
          if (xs == "2") {
            this.iframeFlag = false;
            this.$nextTick(_ => {
              this.url = kjdz;
              //监控视频状态是否为结束状态
              this.videoStatus = document.getElementById("myVideo");
              this.videoStatus.addEventListener(
                "ended",
                this.selectNextId,
                false
              );
            });
          }
          if (xs == "4") {
            this.aHref.flag = true;
            this.aHref.name = mc;
            this.aHref.address = lj;
          }
          if (xs == "3") {
            this.richFlag = true;
            this.nr = nr;
          }

          if (this.requestFrom != "admin") {
            //驻留事件
            let stopParams = {};
            let time = 1 * 30 * 1000;
            // let time = 1000*60
            // let time = 5分钟
            stopParams.streamName = "stream";
            stopParams.eventType = "ING_STUDY";
            stopParams.id = sid; //课件id
            stopParams.pt = "0"; //课程类型
            stopParams.pid = this.cid; //课程id
            this.timer = setInterval(() => {
              if (this.kjlx == "1" || this.kjlx == "5") {
                //获取页码
                stopParams.offset = this.$refs.pdfSvg.pageNo;
              } else if (this.kjlx == "2") {
                //视频
                let videotime = document.getElementById("myVideo");
                stopParams.offset = videotime.currentTime;
                // this.$refs["videoPlay"].play();
              } else if (this.kjlx == "3") {
                //富文本
                stopParams.offset = 0;
              }
              //驻留埋点
              stopSendRedis(stopParams).then(response => {
                console.log("课件驻留埋点" + Date.now(), response);
              });
            }, time);
            document.addEventListener("visibilitychange", this.handleVisiable);
            this.startTime();
          }
        });
        let params = {};
        params.streamName = "stream";
        params.eventType = "CLI_COUR";
        params.id = sid;

        if (this.requestFrom != "admin") {
          //点击埋点
          clickSendRedis(params).then(response => {
            console.log("点击课件埋点", response);
          });
          //更新进度
          if (this.update && this.getStatus == "1") {
            //更新进度
            updateStatus(this.cid, this.jd, this.contentIds).then(res => {
              if (res.code == "200") {
                console.log("进度更新成功");
              }
            });
          }
          this.update = true;
        }
      } else if (source_type == "st") {
        //预览试题
        this.stFlag = true;
        getSubject(sid).then(res => {
          if (res.code == "200") {
            this.subjectForm = res.data;
            getCurriculumSubject(this.cid, this.subjectForm.id).then(res => {
              console.log(res, "返回");
              if (res.code == "200") {
                if (this.subjectForm.lx == "1") this.dx = res.data.xyda;
                if (this.subjectForm.lx == "2")
                  this.duo = res.data.xyda.split(",");
                if (this.subjectForm.lx == "3") this.pd = res.data.xyda;
                if (this.subjectForm.lx == "4")
                  this.tk = JSON.parse(res.data.xyda);
                if (this.subjectForm.lx == "5") this.zg = res.data.xyda;
              }
            });
            console.log(this.subjectForm);
            if (this.subjectForm.jx == "" || this.subjectForm.jx == null) {
              this.subjectForm.jx = "无";
            }
            // console.log(this.subjectForm.jx)
            // console.log(this.subjectForm)
            /** 类型：单选题：1 复选题：2 判断题：3 填空题：4 主观题：5*/
            //选项回显
            if (this.subjectForm.lx == "1")
              this.subjectForm.xxList = JSON.parse(res.data.xx);
            if (this.subjectForm.lx == "2")
              this.subjectForm.xxList = JSON.parse(res.data.xx);
          }
        });
      } else if (source_type == "xtj") {
        this.trainId = sid;
        this.xtjFlag = true;
        this.$refs.train.getTrainDetails(sid);
      }
    },
    /** 点击课程学习, 获取课程详情*/
    async getCurriculum(id) {
      // debugger;
      let contents = null;
      let kjid = this.jd;
      let kjArr = []; //记录所有课件id
      this.kcmc = "";
      this.kcms = "";
      this.xxrs = 0;
      await selectTCurriculumByIds(id).then(res => {
        if (res.code == "200") {
          console.log(res);
          this.kcmc = res.data.mc;
          this.kcms = res.data.ms;
          this.xxrs = res.data.num;
          contents = res.data.tCurriculumContents; //所有课件数据
          //对list 进行重组，将课件后面的习题，放到课件下面
          let temp = [];
          // debugger;
          //内容不是空
          if (null != contents) {
            let kjIndex = -1;
            contents.forEach((item, index) => {
              if (item.source_type == "kj") {
                item["label"] =
                  index + 1 + "\xa0\xa0" + " | \xa0" + item.source_name;
                kjIndex = temp.push(item) - 1;
                kjArr.push(item.sid);
              } else {
                //习题
                item["label"] =
                  index +
                  1 +
                  "\xa0\xa0" +
                  " | \xa0" +
                  ("【题】 " + item.source_name.replace(/<[^>]+>/g, ""));
                // if(kjIndex != -1){//有课件节点，加到课件节点下面
                //   if(temp[kjIndex].children==null||temp[kjIndex].children=='undefined'){
                //     temp[kjIndex].children=[];
                //   }
                //   temp[kjIndex].children.push(item)
                // }else{//没有课件节点，习题为主节点
                //   temp.push(item);
                // }
                temp.push(item);
              }
            });
            this.contentIds = kjArr.join(",");
          }
          this.$nextTick(() => {
            this.treeData = temp;
          });
        }
      });

      //如果进度是null ，打开第一个课件
      if (this.jd == null) {
        //jd==null 时，根据课程id 获取打开课程要展示的内容
        // kjid =await showInitContent(id).then(res =>{
        //   return  res.data
        // })
        if (this.getStatus == null) {
          this.update = true; //更新进度
        }
        this.handleNodeClick(contents[0]);
      } else {
        //打开对应课件
        this.update = true;
        // this.openContent(kjid)
        this.treeData.forEach((item, index) => {
          if (kjid == item.sid) {
            let row = item;
            this.handleNodeClick(row);
          }
        });
      }
    },
    /** 打开树形结构 对应课件*/
    openContent(row) {
      let { sid, source_type } = row;
      // 打开新的浏览器标签页进行学习
      let router = this.$router.resolve({
        path: "/etp/curriculum/previewCurriculum/",
        query: { cid: this.cid, jd: sid },
      });
      window.open(router.href, "_self");
    },

    //访问外链地址
    visitUrl(address) {
      this.$confirm("将在新的标签页打开课件?", "即将离开本站", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          window.open(address, "_blank");
        })
        .catch(() => {});
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "/student/index";
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.app-container-xq {
  //height: calc(100vh - 75px);
  position: relative;
  padding: 0px;
}

.el-empty__image {
  width: 300px;
  margin: 0 auto;
  text-align: center;
}
.pre-desc:before {
  content: "";
  width: 3px;
  height: 20px;
  border-radius: 5px;
  position: absolute;
  top: 0;
  left: 0;
  background: blue;
}
.desc {
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 4px;
}

.pre-font {
  font-size: 16px;
  color: rgb(56, 58, 62);
  line-height: 30px;
}

::v-deep .pre-list {
  overflow: auto;
  padding-left: 10px;
}

::v-deep .pre-list .el-tree-node {
  margin-top: 8px;
  margin-bottom: 5px;
  width: 100%;
  white-space: normal;
  .el-tree-node__content {
    height: 100%;
    padding: 2px 70px 2px 0px;
    align-items: start;
    .mlxx {
      padding: 2px 5px;
      background: #fa8919;
      color: #fff;
      margin-right: 10px;
      border-radius: 6px;
    }
    font-size: 14px;
  }
}

::v-deep .pre-list .el-tree-node .el-tree-node__label {
  font-size: 14px;
}
.btm-content {
  display: flex;
  height: calc(100vh);
}
/** 列表 */
.pre-list {
  width: calc(20% - 20px);
  height: 100%;
  padding-bottom: 30px;
  border-radius: 5px;
  padding-top: 15px;
}
/** 内容*/
.pre-content {
  height: 100%;
  width: calc(80% - 20px);
  position: relative;
}
/** 基本信息 */
.pre-basic {
  border-radius: 5px;
  height: 280px;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

.a-item {
  width: 100%;
  height: 100%;
  padding: 0px 30px;
  position: relative;
  .el-button--medium {
    position: absolute;
    right: 90px;
  }
}
.a-title {
  font-size: 18px;
  font-weight: 600;
  margin: 15px 0px;
}
.a-content:not(:nth-child(3)) {
  margin: 0px 0px 10px;
}
.a-item .a-content:nth-child(3) {
  margin: 20px 0px 0px;
  color: #606266;
}
.a-item .a-content:nth-child(1) {
  margin: 20px 0px 10px;
}
.text-mc {
  width: 100%;
  overflow: hidden;
  display: block;
  font-size: 18px;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-weight: bold;
}

.el-divider--horizontal {
  margin: 0px;
}
.desc-area {
  padding-left: 10px;
  height: 100px;
  position: relative;

  .text-ms {
    color: #9a9595;
    margin-top: 2px;
    overflow: hidden; //溢出内容隐藏
    text-overflow: ellipsis; //文本溢出部分用省略号表示
    display: -webkit-box; //特别显示模式
    -webkit-line-clamp: 2; //行数
    line-clamp: 2;
    -webkit-box-orient: vertical;
    font-size: 14px;
  }

  .xx {
    position: absolute;
    bottom: 0px;
    i {
      margin-right: 5px;
    }
  }
}
.ml-title {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
  margin-left: 10px;
  padding-top: 10px;
  div {
    height: 14px;
    width: 3px;
    background: #fa8919;
    margin-right: 4px;
  }
}
.kj-view {
  width: 100%;
  height: calc(100% - 76px);
  -webkit-overflow-scrolling: auto;
  overflow: hidden;
  margin-top: 10px;
}
::v-deep .hamburger {
  cursor: pointer;
  height: 65px;
}
.kj-title {
  font-size: 18px;
  margin-left: 16px;
  line-height: 40px;
  i {
    margin-right: 4px;
  }
}
.right-menu-item {
  position: absolute;
  top: 25px;
  right: 80px;
  cursor: pointer;
}
.avatar-container {
  margin-right: 30px;
  position: absolute;
  right: 0px;
  top: 0px;
  .avatar-wrapper {
    margin-top: 5px;
    position: relative;

    .user-avatar {
      cursor: pointer;
      width: 40px;
      height: 40px;
      border-radius: 10px;
      margin-top: 7px;
    }

    .el-icon-caret-bottom {
      cursor: pointer;
      position: absolute;
      right: -20px;
      top: 25px;
      font-size: 12px;
    }
  }
}
.mlxx {
  padding: 2px 5px;
  background-color: #fa8919;
  color: #ffffff;
}
::v-deep .el-divider--vertical {
  height: calc(100% - 10px);
  margin-top: 5px;
}

::v-deep p:first-child {
  display: inline;
}
.rece-btn {
  position: absolute;
  bottom: 3px;
  right: 6px;
}
::-webkit-scrollbar-thumb {
  width: 5px;
  background-color: #e5e5e5;
}
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background-color: #ffffff;
}
.desc-btom {
  width: 96%;
  margin: 0 auto;
  .line-heng {
    height: 1px;
    width: 100%;
    background: #e0dcdc;
    margin: 40px 0 20px;
  }
}
::v-deep .el-icon-wc {
  background: url("~@/assets/images/icon_gkk_wc.png") center no-repeat;
  width: 16px;
  height: 16px;
  position: absolute;
  right: 10px;
  //top: 7px;
}
::v-deep .el-icon-jd {
  background: url("~@/assets/images/icon_kcxq_jd.png") center no-repeat;
  width: 16px;
  height: 16px;
  position: absolute;
  right: 10px;
  //top: 7px;
}
::v-deep .is-current {
  color: blue;
}
</style>
