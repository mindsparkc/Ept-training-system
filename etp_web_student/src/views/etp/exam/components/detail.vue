<template>
  <div class="app-container" @touched="moveEvent" @click="moveEvent">
    <div v-if="infoShow">
      <el-row>
        <el-col :span="24">
          <el-alert
            title="点击`开始考试`后将自动进入考试，请诚信考试！"
            type="error">
          </el-alert>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-card>
            <div class="pre-exam">
              <div>
                <strong>考试名称：</strong>
                <span>{{examInfo.mc}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>试卷名称：</strong>
                <span>{{examInfo.sjmc}}</span>
              </div>
            </div>
            <div class="pre-exam" v-if="examInfo.ms!=undefined||examInfo.ms!=null">
              <div>
                <strong>考试描述：</strong>
                <span>{{examInfo.ms}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>组卷策略：</strong>
                <span>{{examInfo.zjcl}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>答题时间：</strong>
                <span>{{examInfo.djsc}}分钟</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>总分：</strong>
                <span>{{examInfo.totalScore}}分</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>分数等级：</strong>
                <span v-for="(value,key) of JSON.parse(examInfo.fsdj)"><span v-for="(val,k) of value">{{val}} </span></span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>题型及数量：</strong>
                <span v-for="(value,key) of examInfo.tx">
                  {{value}}
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" v-if="!this.showButton">
          <el-button type="primary" icon="el-icon-edit" @click="startTheExam">开始考试</el-button>
          <el-button @click="goBack">返回</el-button>
<!--          <el-button @click="dialogVisiblePaper = true">测试弹窗</el-button>-->
        </el-col>
      </el-row>
    </div>
    <div v-else>
      <el-row :gutter="20">
        <el-col :span="5">
          <div class="app-container-left">
            <div class="title">
              {{examInfo.mc}}
            </div>
            <div class="time">
              <span>总分：{{examInfo.djsc}}分 </span>
              <span>时长：{{examInfo.djsc}}分钟 </span>
            </div>
            <div class="tags" ref="tags">
              <template v-for="(item,key,index) in testQuestionDetails">
                <div style="width: 100%;margin-top: 5px">
                  <template v-if="typeItem[key]=='单选'">
                    <el-row :span="24">
                      <el-col>
                        <span>单选</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="(answerCollection[aItem.tkid] && answerCollection[aItem.tkid].length !== 0) ? 'success' : 'info'" v-if="aItem.lx==='1'" v-for="(aItem,aIndex) in allQuestion" :key="aIndex" @click="goAnchor(aItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{aIndex+1}}</el-tag>
                        </a>
                      </el-col>
                   </el-row>
                  </template>
                </div>
                <div style="width: 100%">
                  <template v-if="typeItem[key]=='复选'">
                    <el-row>
                      <el-col>
                        <span>复选</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="(answerCollection[aItem.tkid] && answerCollection[aItem.tkid].length !== 0) ? 'success' : 'info'" v-if="aItem.lx==='2'" v-for="(aItem,aIndex) in allQuestion" :key="aIndex" @click="goAnchor(aItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{aIndex+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%">
                  <template v-if="typeItem[key]=='判断'">
                    <el-row>
                      <el-col>
                        <span>判断</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="(answerCollection[aItem.tkid] && answerCollection[aItem.tkid].length !== 0) ? 'success' : 'info'" v-if="aItem.lx==='3'" v-for="(aItem,aIndex) in allQuestion" :key="aIndex" @click="goAnchor(aItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{aIndex+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%">
                  <template v-if="typeItem[key]=='填空'">
                    <el-row>
                      <el-col>
                        <span>填空</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="(answerCollection[aItem.tkid] && answerCollection[aItem.tkid].length !== 0) ? 'success' : 'info'" v-if="aItem.lx==='4'" v-for="(aItem,aIndex) in allQuestion" :key="aIndex" @click="goAnchor(aItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{aIndex+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%">
                  <template v-if="typeItem[key]=='主观'">
                    <el-row>
                      <el-col>
                        <span>主观</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="(answerCollection[aItem.tkid] && answerCollection[aItem.tkid].length !== 0) ? 'success' : 'info'" v-if="aItem.lx==='5'" v-for="(aItem,aIndex) in allQuestion" :key="aIndex" @click="goAnchor(aItem.tkid)" style="margin-right: 5px;margin-bottom: 5px;margin-top: 5px">{{aIndex+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
              </template>
            </div>
            <div class="theRemainingTime">
              <div>倒计时</div>
              <div>
                <vue-count-down :count="countdown" model="timer" @ended="downTime">
                  <template v-slot="time">
                    {{time.hour}}小时{{time.minute}}分{{time.second}}秒
                  </template>
                </vue-count-down>
              </div>
            </div>
            <div class="submit">
              <el-button type="primary" @click="submitTheTestPaper">提交试卷</el-button>
            </div>
          </div>
        </el-col>
        <el-col :span="19">
          <div class="app-container-right">
            <el-form label-position="top" :model="testQuestionDetails" ref="form" label-width="100px">
              <el-row>
                <el-card class="box-card" v-for="(item,key,index) in testQuestionDetails" :key="index">
                  <div slot="header" class="clearfix">
                    <span>{{typeItem[key]}}</span>
                  </div>
                  <div v-if="typeItem[key] === '单选'">
                    <el-form-item  v-for="(data,dIndex) in item" :key="dIndex"  :ref="'#task-'+ data.tkid">
                      <span v-html="(data.index) + '.(' + data.score + '分) ' + data.tg"></span><br/>
                      <el-radio-group v-model="singleAnswer[data.tkid]">
                        <el-radio v-for="(cData,cIndex) in data.xx" :key="cIndex" :label="cData.key">{{cData.key + '.' + cData.value}}</el-radio><br/>
                      </el-radio-group>
                    </el-form-item>
                  </div>
                  <div v-else-if="typeItem[key] === '复选'">
                    <el-form-item  v-for="(data,dIndex) in item" :key="dIndex"  :ref="'#task-'+ data.tkid">
                      <span v-html="(data.index) + '.(' + data.score + '分) ' + data.tg"></span><br/>
                      <el-checkbox-group v-model="multipleChoiceAnswer[data.tkid]">
                        <el-checkbox v-for="(cData,cIndex) in data.xx" :key="cIndex" :label="cData.key" :name="cData.tkid">{{cData.key + '.' + cData.value}}</el-checkbox><br/>
                      </el-checkbox-group>
                    </el-form-item>
                  </div>
                  <div v-else-if="typeItem[key] === '判断'">
                    <el-form-item  v-for="(data,dIndex) in item" :key="dIndex"  :ref="'#task-'+ data.tkid">
                      <span v-html="(data.index) + '.(' + data.score + '分) ' + data.tg"></span><br/>
                      <el-radio-group v-model="singleAnswer[data.tkid]">
                        <el-radio label="1">正确</el-radio>
                        <el-radio label="0">错误</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </div>
                  <div v-else-if="typeItem[key] === '填空'">
                    <el-form-item  v-for="(data,dIndex) in item" :key="dIndex" :ref="'#task-'+ data.tkid">
                      <span v-html="(data.index) + '.(' + data.score + '分) ' + data.tg"></span><br/>
                      <template v-for="(dDate,inputKey) in data.place">
                        <a>({{inputKey+1}})</a><el-input class="customStyle" :key="inputKey"  v-model="tkAnswer[data.tkid+'-'+inputKey]" size="nimi" style="width: 600px;padding: 10px"></el-input><br/>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-else-if="typeItem[key] === '主观'">
                    <el-form-item  v-for="(data,dIndex) in item" :key="dIndex"  :ref="'#task-'+ data.tkid">
                      <span v-html="(data.index) + '.(' + data.score + '分) ' + data.tg"></span><br/>
                      <el-input class="customStyle" type="textarea" v-model="singleAnswer[data.tkid]"></el-input>
                    </el-form-item>
                  </div>
                </el-card>
              </el-row>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog :visible="dialogVisible" width="500px" center :showClose="false" :close-on-click-modal="true">
      <span style="text-align: center;font-size: 30px;font-weight: bold;color: red">注意！</span><br/>
      <span style="font-size: 20px">答题时间已到，请停止答题，3s后将自动提交试卷，请不要关闭此页面！以防成绩无效！</span>
    </el-dialog>

    <el-dialog :visible="dialogVisiblePaper" class="paper-dialog" :show-close="false">
      <span style="height: 10px;font-size: 20px">提示！
        <el-divider/>
      </span>
      <span v-if="this.examInfo.showScoreTime != undefined" style="font-size: 15px">{{'考试结束，考试成绩请在'+ this.$moment(examInfo.showScoreTime).format('YYYY年MM月DD日 HH时mm分ss秒')+'之后查看！'}}</span>
      <span v-else style="font-size: 15px">{{'你的考试成绩为：' + paperScore + '分，成绩有效！'}}</span>

      <span slot="footer">
        <el-button type="primary" @click="closePaper()">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible="outTimeVisible" class="paper-dialog" :show-close="false">
      <span style="height: 10px;font-size: 20px;color: #C03639">提示！
        <el-divider/>
      </span>
        <span>您已超过{{this.examInfo.noactionTimeout}}分钟未进行任何操作，在倒计时结束后将强制交卷结束本次考试！</span>
<!--      <span>您已超过1分钟未进行任何操作，在倒计时结束后将强制交卷结束本次考试！</span>-->
      <span slot="footer">
        <el-button type="warning" @click="closeOutTime('0')">交卷
          <vue-count-down :count="outDown" model="timer" @ended="downOutTime">
            <template v-slot="time">
              {{time.second}}秒
            </template>
          </vue-count-down>
        </el-button>
        <el-button type="primary" @click="closeOutTime('1')">继续考试</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getExaminationInfo,
  getSubjectByPaper,
  resultInquiry,
  submitTheExam,
  heartBeat,
  isTrue,
} from "@/api/etp/exam/exam";
import vueCountDown from "@liuwave/vue-count-down";
import { delConfig } from "@/api/system/config";
import { parseTime } from "@/utils/ruoyi";

export default {
  name: "ExamDetail",
  props: {
    examId: {
      type: [Number, String],
      required: false,
    },
  },
  components: {
    vueCountDown,
  },
  data() {
    return {
      //倒计时
      countdown: 0,
      //考试详情
      examInfo: {},
      //是否显示详情
      infoShow: true,
      //分类
      testQuestionDetails: {},
      //类型
      typeItem: {},
      //单选答案
      singleAnswer: {},
      //多选答案
      multipleChoiceAnswer: {},
      //填空答案
      tkAnswer: {},
      //所有答案
      answerCollection: {},
      //所有问题
      allQuestion: [],
      examIdNow: undefined,
      //获取数据的json
      subData: {},
      //心跳检测参数
      heart: "",
      heartId: "0",
      // websock: null, //建立的连接
      // lockReconnect: false, //是否真正建立连接
      // timeout: 28 * 1000, //30秒一次心跳
      // timeoutObj: null, //外层心跳倒计时
      // serverTimeoutObj: null, //内层心跳检测
      // timeoutnum: null, //断开 重连倒计时
      dialogVisible: false,
      tkNumber: "", //记录填空题题号
      tkValue: [], //记录单个填空题的多个答案
      dxValue: [],
      dialogVisiblePaper: false,
      paperScore: "",
      isSubmitVisible: false,
      outCount: 0, //记录用户切出当前页面的次数
      outTime: null, //用户超时定时器
      outDown: 0, //用户超时倒计时30秒
      outTimeVisible: false,
      isOutDown: false,
      showButton: false,
    };
  },
  created() {
    this.examIdNow = this.$route.params && this.$route.params.examId;
    if (this.examId) {
      this.examIdNow = this.examId;
      this.getExamDetail(this.examId);
    } else if (this.examIdNow) {
      this.getExamDetail(this.examIdNow);
    }
  },
  mounted() {
    // this.heart = setInterval(this.getHeart,3000);
    // document.addEventListener('visibilitychange',this.handleVisiable);
  },
  destroyed() {
    clearInterval(this.heart);
    document.removeEventListener("visibilitychange", this.handleVisiable);
  },
  beforeDestroy() {
    clearTimeout(this.heart);
  },
  methods: {
    downOutTime() {
      if (this.isOutDown) {
        this.$message.error(
          "您已超过 " +
            this.examInfo.noactionTimeout +
            " 分钟未进行任何操作，已被系统自动交卷，本次考试已结束"
        );
        this.closeOutTime("0");
      }
      this.outTimeVisible = false;
    },

    //超时强制交卷方法
    closeOutTime(status) {
      if (status === "0") {
        this.isOutDown = true;
        this.outTimeVisible = false;
        this.submitTestPaper();
      } else if (status === "1") {
        this.outDown = 0;
        this.outTimeVisible = false;
      }
    },

    //监控鼠标移动和点击事件
    moveEvent() {
      clearTimeout(this.outTime);
      this.init();
    },
    //用户超时定时器方法
    init() {
      let time = parseInt(this.examInfo.noactionTimeout) * 60 * 1000;
      // let time = 60000
      this.outTime = setTimeout(() => {
        this.outTimeVisible = true;
        this.outDown = 30;
      }, time);
    },

    //监控用户切出当前页面
    handleVisiable(e) {
      let turnThreshold = parseInt(this.examInfo.turnThreshold);
      switch (e.target.visibilityState) {
        case "hidden":
          this.outCount += 1;
          console.log("内容不可见!");
          if (this.outCount === turnThreshold) {
            // this.$message.error("警告，您已离开考试页面3次，如再次离开则强制交卷！")
            alert(
              "警告，您已离开考试页面" +
                this.outCount +
                "次，如再次离开则强制交卷！"
            );
            break;
          } else if (this.outCount > turnThreshold) {
            alert(
              "警告，您已离开考试页面超过" +
                turnThreshold +
                "次，开始强制交卷！"
            );
            this.submitTestPaper();
            break;
          } else {
            alert("警告，您已离开考试页面" + this.outCount + "次！");
            break;
          }
          break;
        case "visible":
          console.log("正常展示页面！");
          break;
      }
    },

    /** 关闭dialog提示框 */
    closePaper() {
      this.dialogVisiblePaper = false;
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path: "/studyManagement/examManagement",
        query: { t: Date.now() },
      });
    },

    getHeart() {
      heartBeat(this.heartId).then(res => {
        if (res.code != "200") {
          this.$message.error("系统丢失与服务器连接，请重新登录！");
        } else {
          console.log("心跳连接正常！");
        }
      });
    },

    goAnchor(id) {
      this.$refs["#task-" + id][0].$el.scrollIntoView({
        block: "center",
        inline: "nearest",
      });
    },
    submitTheTestPaper() {
      // 1
      let no = 0;
      for (const cE of this.$refs.tags.children) {
        if (cE.className.indexOf("el-tag--success") === -1) {
          no++;
        }
      }
      //2
      let noE = 0;
      for (const aE of this.allQuestion) {
        if (
          !this.answerCollection[aE.tkid] ||
          this.answerCollection[aE.tkid].length === 0
        ) {
          noE++;
        }
      }
      this.$confirm(
        "是否提交考试！" + (noE === 0 ? "" : "你还有" + noE + "道题未答！"),
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          const loading = this.$loading({
            lock: true,
            text: "Loading",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          this.submitTestPaper(loading);
        })
        .catch(() => {});
      console.log(no, noE);
    },
    closeDialog() {
      if (this.dialogVisible) {
        this.dialogVisible = false;
        this.submitTestPaper();
      }
    },
    downTime() {
      console.log("22222");
      this.dialogVisible = true;
      setTimeout(this.closeDialog, 3000);
      // const loading = this.$loading({
      //   lock: true,
      //   text: 'Loading',
      //   spinner: 'el-icon-loading',
      //   background: 'rgba(0, 0, 0, 0.7)'
      // });
      // this.$notify.warning({
      //   title: '注意！',
      //   message: '答题时间已到，请停止答题，3s后将自动提交试卷，请不要关闭此页面！以防成绩无效！',
      //   showClose: false,
      //   duration: 3000,
      //   position: center,
      //   onClose: ()=>{
      //     this.submitTestPaper(loading);
      //   }
      // });
    },
    submitTestPaper(loading) {
      const dataArr = JSON.parse(JSON.stringify(this.subData));
      console.log(dataArr, "dataArr");
      const { sj } = dataArr;
      console.log(this.answerCollection);
      for (const aE of sj) {
        const { tkid } = aE;
        if (tkid) {
          aE["da"] = this.answerCollection[tkid];
        }
      }
      dataArr.sj = sj;
      submitTheExam(dataArr).then(res => {
        if (res.code == "200") {
          console.log("交卷成功！");
          if (this.examInfo.showScoreTime != undefined) {
            this.dialogVisiblePaper = true;
            setTimeout(() => {
              loading.close();
            }, 2000);
          } else {
            resultInquiry(dataArr.id).then(res => {
              this.paperScore = res.data.score;
              this.dialogVisiblePaper = true;
              setTimeout(() => {
                loading.close();
              }, 2000);
            });
          }
        }
      });
    },
    startTheExam() {
      if (this.examInfo.sjid) {
        const loading = this.$loading({
          lock: true,
          text: "正在创建试题！",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
        getSubjectByPaper({
          sjid: this.examInfo.sjid,
          ksid: this.examIdNow,
        }).then(res => {
          console.log(res, "res");
          if (res && res.data) {
            document.addEventListener("visibilitychange", this.handleVisiable);
            this.heart = setInterval(this.getHeart, 3000);
            this.subData = res.data;
            this.infoShow = false;
            //获取试题的数据
            const { sj } = res.data;
            if (sj && sj.length > 0) {
              this.allQuestion = sj;
              let index = 1;
              for (const datum of sj) {
                const { lx, lxbq, xx, tkid } = datum;
                if (xx && xx !== "") {
                  datum.xx = JSON.parse(xx);
                }
                this.typeItem[lx] = lxbq;
                if (!this.testQuestionDetails[lx]) {
                  this.testQuestionDetails[lx] = [];
                }
                /**
                 * 1: "单选"
                 * 2: "复选"
                 * 3: "判断"
                 * 4: "填空"
                 * 5: "主观"
                 */
                if (lx === "2") {
                  this.$set(this.multipleChoiceAnswer, tkid, []);
                }
                datum.index = index;
                index++;
                this.testQuestionDetails[lx].push(datum);
              }
              this.countdown =
                parseInt(this.examInfo.djsc) * 60
                  ? parseInt(this.examInfo.djsc) * 60
                  : 3600;
              this.$forceUpdate();
              setTimeout(() => {
                loading.close();
              }, 1000);
              console.log(this.testQuestionDetails, "testQuestionDetails");
              console.log(this.typeItem);
            }
          } else {
            this.$message.error(res.msg);
            loading.close();
            setTimeout(() => {
              this.goBack();
            }, 3000);
          }
        });
      } else {
        this.$message.error("创建试题失败！");
      }
    },
    goBack() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1);
    },
    getExamDetail(id) {
      getExaminationInfo(id).then(res => {
        if (res.data == undefined) {
          this.showButton = true;
        } else if (res && res.data) {
          // debugger
          let tx = JSON.parse(res.data.tx);
          let qa = [];
          for (let key in tx) {
            console.log(key);
            console.log(tx[key]);
            if (tx[key].key == 1) qa[key] = "单选题：" + tx[key].value + ";";
            if (tx[key].key == 2) qa[key] = "复选题：" + tx[key].value + ";";
            if (tx[key].key == 3) qa[key] = "判断题：" + tx[key].value + ";";
            if (tx[key].key == 4) qa[key] = "填空题：" + tx[key].value + ";";
            if (tx[key].key == 5) qa[key] = "主观题：" + tx[key].value;
          }

          this.examInfo = res.data;
          this.examInfo.tx = qa;
        }
      });
    },
  },
  watch: {
    tkAnswer: {
      handler(val) {
        for (const key in val) {
          let tkNum = key.split("-");
          let num = tkNum[1];
          let tkArr = {};
          if (this.tkNumber == "") {
            this.tkNumber = tkNum[0];
          } else if (this.tkNumber != "" && this.tkNumber != tkNum[0]) {
            this.tkNumber = tkNum[0];
            this.tkValue = [];
          }
          this.tkValue[num] = val[key];
          tkArr[this.tkNumber] = this.tkValue;
          Object.assign(this.answerCollection, tkArr);
          console.log(this.answerCollection);
        }
      },
      deep: true,
      immediate: true,
    },

    singleAnswer: {
      handler(val) {
        // console.log(val)
        Object.assign(this.answerCollection, val);
        // console.log(this.answerCollection);
      },
      deep: true,
      immediate: true,
    },
    multipleChoiceAnswer: {
      handler(val) {
        let dx = "";
        for (const key in val) {
          dx = val[key].toString();
          this.dxValue[key] = dx;
          console.log(dx);
        }
        Object.assign(this.answerCollection, this.dxValue);
        // console.log(this.answerCollection)
      },
      deep: true,
      immediate: true,
    },
  },
};
</script>
<style scoped lang="scss">
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.pre-exam {
  > div {
    line-height: 42px;
    color: #555;
  }
}
.app-container-left {
  border: 1px rgba(0, 0, 0, 0.08) solid;
  padding: 12px;
  > div {
    margin-bottom: 12px;
  }
  .title {
    font-size: 17px;
    font-weight: 400;
  }
  .time {
    height: 40px;
    font-size: 15px;
    font-weight: 400;
    border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
  }
  .theRemainingTime {
    padding-bottom: 20px;
    border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
    > div {
      text-align: center;
      margin-top: 16px;
      &:nth-child(2) {
        font-size: 20px;
        color: red;
      }
    }
  }
  .submit {
    text-align: center;
    padding-top: 20px;
  }
  .tags {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
    overflow-y: scroll;
    > a {
      > span {
        margin-right: 6px;
        margin-bottom: 6px;
      }
    }
  }
}
.app-container-right {
  border: 1px rgba(0, 0, 0, 0.08) solid;
  height: calc(100vh - 124px);
  overflow-y: scroll;
  .customStyle {
    border: 0;
    ::v-deep {
      .el-input__inner {
        border: 0 transparent solid;
        border-bottom: 1px solid red;
        border-radius: 0;
      }
    }
  }
  ::v-deep {
    .el-card {
      border-radius: 0;
      .el-card__header {
        background-color: rgba(250, 250, 250, 1);
      }
      .el-card__body {
        label {
          font-weight: 500;
          width: 100%;
          margin-left: 20px;
        }
      }
    }
  }
}

.paper-dialog {
  margin: auto;
  width: 70%;
}

::v-deep p:first-child {
  display: inline;
}

::v-deep el-checkbox-group {
  .el-checkbox {
    width: 100%;
  }
}
</style>
