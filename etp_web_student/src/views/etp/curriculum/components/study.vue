<template>
  <div class="pre-content pre-pad" >
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
    <div style="width: 100%;height: calc(100% - 5px);-webkit-overflow-scrolling: auto;overflow:hidden;"
         v-show="!tipFlag && kjFlag">
      <div class="kj-title"><i class="el-icon-notebook-2"></i>{{subjectVal.p_label}} / {{kjmc}}
        <span v-if="this.requestFrom!='admin'" style="margin-left: 20px;color: #868686;font-size: 14px">总需学习时长：{{learnTime}} 分钟</span>
        <span v-else style="margin-left: 20px;color: #868686;font-size: 14px">总需学习时长：-- 分钟</span>
        <span v-if="this.requestFrom!='admin'" style="margin-left: 38px;color: #868686;font-size: 14px">历史已学习：{{lastStudyTime}} 分钟</span>
        <span v-else style="margin-left: 38px;color: #868686;font-size: 14px">历史已学习：-- 分钟</span>
        <span v-if="this.requestFrom!='admin'" style="margin-left: 56px;color: #868686;font-size: 14px">学习中：{{studyTime}}</span>
        <span v-else style="margin-left: 56px;color: #868686;font-size: 14px">学习中：--:--:--</span>
      </div>
      <div style="height:calc(100% - 60px);" v-if="iframeFlag ">
        <scrollLoad :list="picList" :total="picTotal" :host="picHost" ref="pdfSvg"></scrollLoad>
      </div>
      <video controls autoplay width="96%" v-if="kjlx=='2'" style="display: block;margin: 0 auto;height: calc(100% - 200px);" id="myVideo" ref="videoPlay">
        <source :src="url" >
      </video>
      <!--          视频下面加课件描述-->
      <div class="desc-btom" v-if="kjlx=='2'">
        <div class="line-heng"></div>
        <span>{{kjms}}</span>
      </div>
      <div v-show="richFlag" style="padding: 20px 50px;height: 90%;overflow:auto;" v-html="nr"></div>
      <div class="a-item" v-show="aHref.flag" >
<!--        <div class="a-title">即将离开本站</div>-->

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
            <el-radio label="正确" @change="showDa()"></el-radio>
            <el-radio label="错误" @change="showDa()"></el-radio>
          </el-radio-group>
        </el-form-item>


        <el-form-item label="请填写：" v-if="subjectForm.lx =='4'">
          <template  v-for="(data,dIndex) in subjectForm.place">
            <a>({{dIndex+1}})</a><el-input class="customStyle" :key="dIndex"  v-model="tk[dIndex]" size="nimi" style="width: 600px;padding: 10px"></el-input><br/>
          </template>
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
  </div>
</template>

<script>
import { getCourseware } from "@/api/etp/courseware/courseware";
import { FILE_PREVIEW_URL, handleTree } from "@/utils/ruoyi";
import { getSubject } from "@/api/etp/subject/subject";
import scrollLoad from "@/components/scrollLoad/index";
import { clickSendRedis, stopSendRedis } from "@/api/buryPoint";
import { addCurriculumSubject } from "@/api/etp/curriculum/curriculum";

export default {
  name: "study",
  props: {
    subjectVal: {
      type: Object,
    },
    requestFrom: {
      default: null,
    },
  },

  components: {
    scrollLoad,
  },
  data() {
    return {
      tipFlag: true,
      kjFlag: false,
      stFlag: false,
      iframeFlag: false,
      richFlag: false,
      dxt: false,
      dxts: false,
      pdt: false,
      tkt: false,
      zgt: false,
      kjlx: "",
      nr: "",
      url: "",
      subjectForm: {},
      dx: "",
      duo: [],
      pd: "",
      tk: {},
      zg: "",
      aHref: {
        flag: false,
        name: null,
        address: null,
      },

      //文件图片
      picList: [],
      picTotal: 0,
      picHost: "",

      kcmc: "", //课程名称
      kcms: "", //课程描述
      xxrs: "", //学习人数
      //学习的课件名称
      kjmc: "",
      kjms: "",
      learnTime: 0,
      lastStudyTime: 0,

      kjId: null, //记录当前点击课件id
      //计时器参数
      flag: null,
      hour: 0,
      minute: 0,
      second: 0,
      studyTime: "00:00:00",
    };
  },
  created() {},
  destroyed() {
    clearInterval(this.timer);
    document.removeEventListener("visibilitychange", this.handleVisiable);
  },
  watch: {
    subjectVal: {
      handler(val, oldValue) {
        this.showCj();
      },
      deep: true,
      immediate: true,
    },
  },
  methods: {
    //开始计时
    startTime() {
      this.flag = setInterval(() => {
        this.second = this.second + 1;
        if (this.second >= 60) {
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
          this.showCj();
          break;
      }
    },

    //学员做题后展示答案及解析
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
      /** 根据试题类型及参数是否有值进行修改状态 */
      if (this.dx != "") (this.dxt = true), (this.subjectForm["xxjd"] = "");
      else if (this.pd != "")
        (this.pdt = true), (this.subjectForm["xxjd"] = "");
      else if (this.tk != "")
        (this.tkt = true), (this.subjectForm["xxjd"] = "");
      else if (this.zg != "")
        (this.zgt = true), (this.subjectForm["xxjd"] = "");
      this.$forceUpdate();
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

    /** 标志位重置*/
    flagR() {
      this.kjFlag = false;
      this.stFlag = false;
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
      this.tk = "";
      this.zg = "";
    },
    /** 依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身*/
    showCj() {
      //  debugger
      this.flagR();
      this.clearSt();
      this.tipFlag = false;
      this.kjlx = "";
      this.kjmc = "";

      if (this.subjectVal.source_type == "kj") {
        // 预览课件
        this.kjId = this.subjectVal.sid;
        this.kjFlag = true;
        getCourseware(this.subjectVal.sid).then(res => {
          /**课件形式Office：1 视频：2 富文本：3 外部链接：4 其他文件：5*/
          let {
            xs,
            kjdz,
            nr,
            lj,
            mc,
            metaData,
            learnTime,
            lastStudyTime,
            ms,
          } = res.data;
          this.kjmc = mc;
          this.kjlx = xs;
          this.learnTime = learnTime;
          this.lastStudyTime = lastStudyTime;
          this.kjms = ms;
          if (xs == "1" || xs == "5") {
            this.iframeFlag = true;
            // this.$nextTick(_ => {
            //   this.url = this.previewUrl() + '?url=' + encodeURIComponent(window.btoa(kjdz));
            // })
            if (metaData != null) {
              let { list, size } = JSON.parse(metaData);
              this.picList = list;
              this.picTotal = size;
              // this.picHost = 'http://10.153.3.12'+kjdz.substring(21,kjdz.indexOf("."))+'/'
              this.picHost = kjdz.substring(0, kjdz.lastIndexOf(".")) + "/";
            }
          }
          if (xs == "2") {
            this.iframeFlag = false;
            this.$nextTick(_ => {
              this.url = kjdz;
            });
          }
          if (xs == "4") {
            // this.iframeFlag = true;
            this.aHref.flag = true;
            this.aHref.name = mc;
            this.aHref.address = lj;
          }
          if (xs == "3") {
            this.richFlag = true;
            this.nr = nr;
          }
          document.addEventListener("visibilitychange", this.handleVisiable);
          this.startTime();
        });

        //驻留事件
        let stopParams = {};
        let time = 1 * 30 * 1000;
        // let time = 1分钟
        stopParams.streamName = "stream";
        stopParams.eventType = "ING_STUDY";
        stopParams.id = this.subjectVal.sid; //课件id
        stopParams.pt = "1"; //任务类型
        stopParams.pid = this.subjectVal.taskId; //任务id
        if (this.requestFrom != "admin") {
          this.timer = setInterval(() => {
            if (this.kjlx == "1" || this.kjlx == "5") {
              //获取页码
              stopParams.offset = this.$refs.pdfSvg.pageNo;
            } else if (this.kjlx == "2") {
              //视频
              let videotime = document.getElementById("myVideo");
              stopParams.offset = videotime.currentTime;
            } else if (this.kjlx == "3") {
              //富文本
              stopParams.offset = 0;
            }
            // console.log("//任务id",this.subjectVal.taskId )
            //点击埋点
            stopSendRedis(stopParams).then(response => {
              console.log("任务课件驻留埋点", response);
            });
          }, time);
        }
      } else if (this.subjectVal.source_type == "st") {
        //预览试题
        this.stFlag = true;
        getSubject(this.subjectVal.sid).then(res => {
          if (res.code == "200") {
            this.subjectForm = res.data;
            if (this.subjectForm.jx == "" || this.subjectForm.jx == null) {
              this.subjectForm.jx = "无";
            }
            /** 类型：单选题：1 复选题：2 判断题：3 填空题：4 主观题：5*/
            //选项回显
            if (this.subjectForm.lx == "1")
              this.subjectForm.xxList = JSON.parse(res.data.xx);
            if (this.subjectForm.lx == "2")
              this.subjectForm.xxList = JSON.parse(res.data.xx);
          }
        });
      }
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
  },
};
</script>

<style lang="scss" scoped>
.el-empty__image {
  width: 300px;
  margin: 0 auto;
  text-align: center;
}

.desc {
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 4px;
}

::v-deep .pre-list {
  overflow: auto;
}

::v-deep .pre-list .el-tree-node .el-tree-node__label {
  font-size: 14px;
}

/** 内容*/
.pre-content {
  height: calc(100% - 75px);
  border-radius: 5px;
  padding-top: 15px;
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
.kj-title {
  font-size: 18px;
  margin-left: 16px;
  line-height: 40px;
  i {
    margin-right: 4px;
  }
}

::v-deep p:first-child {
  display: inline;
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
</style>
