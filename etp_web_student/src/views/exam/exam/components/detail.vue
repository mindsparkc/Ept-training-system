<template>
  <div class="app-container-center" >
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
        <el-card>
          <el-row>
            <div class="pre-exam">
              <div>
                <img :src="examInfo.cover==null?require('@/assets/images/examine_cover.png'):examInfo.cover" @error="setDefaultImage" width="200" height="150"  alt="X"/>
              </div>
            </div>
          </el-row>
          <el-row>
            <div class="pre-exam">
              <div>
                <strong>考试名称：</strong>
                <span>{{examInfo.name}}</span>
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
                <strong>及格分：</strong>
                <span>{{examInfo.qualifyScore==null?'暂无':examInfo.qualifyScore+'分'}}</span>
              </div>
            </div>
            <div class="pre-exam" v-show="examInfo.startTime!=null">
              <div>
                <strong>考试开始时间：</strong>
                <span>{{examInfo.startTime}}</span>
              </div>
            </div>
            <div class="pre-exam" v-show="examInfo.endTime!=null">
              <div>
                <strong>考试结束时间：</strong>
                <span>{{examInfo.endTime}}</span>
              </div>
            </div>
            <div class="pre-exam" >
              <div>
                <strong>允许迟到时间：</strong>
                <span>{{examInfo.lateTime>0?examInfo.lateTime+'分钟':'不限制'}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>考试时长：</strong>
                <span>{{examInfo.totalTime>0?examInfo.totalTime+'分钟':'不限制'}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>最短交卷时长：</strong>
                <span>{{examInfo.handMinTime>0?examInfo.handMinTime+'分钟':'不限制'}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>允许页面无操作最长时间：</strong>
                <span>{{examInfo.noActLimit>0?examInfo.noActLimit+'分钟':'不限制'}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>显示成绩策略：</strong>
                <span v-if="examInfo.showScore == 0">不显示成绩</span>
                <span v-if="examInfo.showScore == 1">立即显示</span>
                <span v-if="examInfo.showScore == 2">批改后显示</span>
                <span v-if="examInfo.showScore == 3">定时发布</span>
                <span v-if="examInfo.showScore == 4">人工发布</span>
              </div>
            </div>
            <div v-if="examInfo.openType=='5'" class="pre-exam">
              <div>
                <el-col>
                  <strong style="display:inline;">口令密码：</strong><el-input v-model="password" placeholder="请输入口令密码" style="width: 200px"></el-input>
                </el-col>
              </div>
            </div>
            <div class="pre-exam" v-if="examInfo.showScore == 3">
              <div>
                <strong>公布成绩时间：</strong>
                <span>{{examInfo.showScoreTime}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>允许离开考试页面次数：</strong>
                <span>{{examInfo.leaveLimit>0?examInfo.leaveLimit+'次 (超过限制次数，将会强制交卷)':'不限制'}}</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>考试次数：</strong>
                <span>已考{{examInfo.usedChance}}次  </span>
                <span v-if="examInfo.chance ==0"> (考试总次数不限制)</span>
                <span v-else>(限制考试{{examInfo.chance}}次)</span>
              </div>
            </div>
            <div class="pre-exam">
              <div>
                <strong>考试描述：</strong>
                <span>{{examInfo.content==null?'无':examInfo.content}}</span>
              </div>
            </div>
          </el-row>
          <el-row v-show="examInfo.records!=null && examInfo.records.length>0">
            <div v-for="(item) in examInfo.records" class="jx-exam">
              <div>
                <img src="../../../../assets/images/djs.png">
              </div>
              <div>
                <div style="color: #ff9429">正在进行中的考试</div>
                <div style="font-size: 14px">考试时间：{{item.startTime}}</div>
              </div>
              <div>
                <i class="el-icon-circle-close" @click="giveUp(item.recordId)"></i>
                <i class="el-icon-arrow-right" @click="beginExam(item)"></i>
              </div>
            </div>
          </el-row>
        </el-card>
      </el-row>
      <el-row>
        <el-col :span="24" v-if="!this.showButton">
          <el-button type="primary" icon="el-icon-edit" v-has-permi="['user:exam:room']" @click="startTheExam">开始考试</el-button>
          <el-button @click="goBack">返回</el-button>
<!--          <el-button @click="dialogVisiblePaper = true">测试弹窗</el-button>-->
        </el-col>
      </el-row>
    </div>
    <div v-else @mousemove="moveEvent" @click="moveEvent">

      <el-row :gutter="20">
        <el-col :span="4" :xs="24" class="box-show">
          <div class="nav-area">
            <div class="card-item" v-for="(item,u_index) in allQuestion" @click="jumpClick(u_index)">
              <div>{{ item.display }}</div>
              <div class="tj-tm">
                共<span class="j-italic"> {{item.quCount}} </span>题 , 共<span class="j-italic"> {{item.totalScore}} </span>分
              </div>
              <div class="bh-item">
                <el-tag :type="(que.answeredFlag==true?'success' : 'info')" v-for="(que,t_index) in item.questions" @click.stop="jumpTmClick(u_index,t_index)">
                  {{t_index+1}}
                </el-tag>
              </div>
              <el-divider></el-divider>
            </div>
          </div>
        </el-col>
        <el-col :span="16" :xs="24" class="nav-area">
          <el-form label-position="top"  ref="form" label-width="100px">
          <div class="content" id="print" ref="print">
            <div v-for="(item,k) in allQuestion">
              <div :id="`visit_${k}`" class="unit-title">
                【{{item.display}}】 共{{item.quCount}}题，总计{{item.totalScore}}分
              </div>
              <div v-for="(question,i) in item.questions" style="margin-bottom: 30px;">
                <el-row :id="`tm_${k}_${i}`">
                  <el-col :span="20">
                    <div :class="['tm-item']">
                      <div style="display: flex">
                        <span><div class="xh" >{{i+1}}</div> </span>
                        <div class="tm-title" v-html="` (${typeFormat(question.type)})  ${question.content}`"></div>
                      </div>
                      <!--    单选题 多选题 选项-->
                      <div class="tm-sel" v-if="question.type==1||question.type==2">
                        <div v-for="(item,j) in question.answers">
                          <el-checkbox v-model="item.selectFlag" @change="selectChange(question,j)">{{letterArr[j]}}. {{item.content}}</el-checkbox>
                        </div>
                      </div>
                      <!--   判断题 -->
                      <div class="tm-sel" v-if="question.type==3">
                        <div v-for="(item,j) in question.answers">
                          <el-checkbox v-model="item.selectFlag" @change="selectChange(question,j)">{{item.content}}</el-checkbox>
                        </div>
                      </div>
                      <!--  填空题 -->
                      <div class="tm-sel" v-if="question.type==4">
                        <div v-for="(item,j) in question.answers">
                          <el-input v-model="item.answered" @change="selectChange(question,j)"></el-input>
                        </div>
                      </div>
                      <!--  问答题 -->
                      <div class="tm-sel" v-if="question.type==5">
                        <el-input type="textarea" v-model="question.answered" @change="selectChange(question,i)"></el-input>
                      </div>
                      <slot></slot>
                    </div>
                  </el-col>
                  <el-col :span="4" v-show="question.type!=6" style="text-align: center">
                    本题 <span class="j-italic">{{question.score==null?item.eachScore:question.score}}</span> 分
                  </el-col>
                </el-row>
                <div class="divider"></div>
                <div class="box-card" v-if="question!=null && question.children!=null">
                  <div class="child-tm" v-for="(child,j) in question.children" >
                    <el-row>
                      <el-col :span="20">
                        <div style="display: flex">
                          <span><div class="child-xh" >{{j+1}}</div> </span>
                          <div class="tm-title" v-html="` (${typeFormat(child.type)})  ${child.content}`"></div>
                        </div>
                        <!--    单选题 多选题 选项-->
                        <div class="tm-sel" v-if="child.type==1||child.type==2">
                          <div v-for="(item,k) in child.answers">
                            <el-checkbox v-model="item.selectFlag" @change="selectChangeBySix(child,k,question)">{{letterArr[j]}}. {{item.content}}</el-checkbox>
                          </div>
                        </div>
                        <!--   判断题 -->
                        <div class="tm-sel" v-if="child.type==3">
                          <div v-for="(item,k) in child.answers">
                            <el-checkbox v-model="item.selectFlag" @change="selectChangeBySix(child,k,question)">{{item.content}}</el-checkbox>
                          </div>
                        </div>
                        <!--  填空题 -->
                        <div class="tm-sel" v-if="child.type==4">
                          <div v-for="(item,k) in child.answers">
                            <el-input v-model="item.answered" @change="selectChangeBySix(child,k,question)"></el-input>
                          </div>
                        </div>
                        <!--  问答题 -->
                        <div class="tm-sel" v-if="child.type==5">
                          <el-input type="textarea" v-model="child.answered" @change="selectChangeBySix(child,j,question)"></el-input>
                        </div>
                      </el-col>
                      <el-col :span="4" style="text-align: center">
                        本题 <span class="j-italic"> {{child.score==null?item.eachScore:child.score}}</span> 分
                      </el-col>
                    </el-row>
                    <div class="divider"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          </el-form>
        </el-col>

        <el-col :span="4" :xs="24" >
          <div class="nav-area">
            <div class="top1">
              <div>考试名称：{{subData.name}}</div>
              <div>考试总分：{{subData.totalScore}} 分</div>
              <div>考试时长：{{subData.totalTime}} 分钟</div>
              <div>最短交卷时长：{{subData.handMinTime}} 分钟</div>
              <el-divider></el-divider>
              <!--          <el-checkbox v-model="isShowAnswer">显示答案和解析</el-checkbox>-->
            </div>
            <div class="print-btn">
              <div v-if="subData.totalTime>0">
                <div style="text-align: center;margin-top: 16px;">倒计时</div>
                <div>
                  <vue-count-down :count="countdown" model="timer" @ended="downTime" style="font-size: 20px;color: red;">
                    <template v-slot="time">
                      {{time.hour}}小时{{time.minute}}分{{time.second}}秒
                    </template>
                  </vue-count-down>
                </div>
              </div>
              <el-button type="primary" v-has-permi="['user:exam:room']" @click="submitTheTestPaper" style="margin-top: 40px">提交试卷</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog :visible="dialogVisible" width="500px" center :showClose="false" :close-on-click-modal="true">
      <span style="text-align: center;font-size: 30px;font-weight: bold;color: red">注意！</span><br/>
      <span style="font-size: 20px">答题时间已到，请停止答题，3s后将自动提交试卷，请不要关闭此页面！以防成绩无效！</span>
    </el-dialog>

<!--    <el-dialog :visible="dialogVisiblePaper" class="paper-dialog" :show-close="false">-->
<!--      <span style="height: 10px;font-size: 20px">提示！-->
<!--        <el-divider/>-->
<!--      </span>-->
<!--      <span v-if="this.examInfo.showScore ==3 " style="font-size: 15px">{{'考试结束，考试成绩请在'+ this.$moment(examInfo.showScoreTime).format('YYYY年MM月DD日 HH时mm分ss秒')+'之后查看！'}}</span>-->
<!--      <span v-else-if="this.examInfo.showScore ==1" style="font-size: 15px">{{'你的考试成绩为：' + paperScore + '分，成绩有效！'}}</span>-->

<!--      <span slot="footer">-->
<!--        <el-button type="primary" @click="goBack">确 定</el-button>-->
<!--      </span>-->
<!--    </el-dialog>-->

    <el-dialog :visible="outTimeVisible" class="paper-dialog" :show-close="false">
      <span style="height: 10px;font-size: 20px;color: #C03639">提示！
        <el-divider/>
      </span>
        <span>您已超过{{this.examInfo.noActLimit}}分钟未进行任何操作，在倒计时结束后将强制交卷结束本次考试！</span>
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

    <el-dialog
      title="提示"
      :close-on-click-modal="false"
      :visible.sync="showVisible"
      width="30%">
      <div class="tip"><i class="el-icon-star-off" style="color: red"></i>本次考试已结束！</div>
      <div class="del-con">
        <span class="title">提示：</span>
        <div class="con-body" >
          <div class="del-item" v-for="(val,key) in delInfo">
            <span>{{ key }}：</span>
            <span>{{ val }}</span>
          </div>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
<!--      <el-button @click="delClose">取 消</el-button>-->
      <el-button type="primary" @click="goBack">确 定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>

import {
  submitTheExam,
  getExamInfo,
  startExam,
  getHistoryExam,
  giveUpExam,
  submitAnswer
} from "@/api/exam/exam";
import vueCountDown from "@liuwave/vue-count-down"
import {delConfig} from "@/api/system/config";
import {parseTime} from "@/utils/ruoyi";

export default {
  name: 'ExamDetail',
  props: {
    examId: {
      type: [Number,String],
      required: false
    }
  },
  components: {
    vueCountDown
  },
  data() {
    return {
      //倒计时
      countdown: 0,
      //考试详情
      examInfo: {},
      //是否显示详情
      infoShow: true,
      //所有问题
      allQuestion: [],
      examIdNow: undefined,
      //获取数据的json
      subData: {},
      //心跳检测参数
      heart:'',
      heartId:'0',
      dialogVisible:false,
      tkNumber : '',//记录填空题题号
      tkValue : [],//记录单个填空题的多个答案
      dxValue: [],
      dialogVisiblePaper:false,
      paperScore:'',
      isSubmitVisible:false,
      outCount:0,//记录用户切出当前页面的次数
      outTime:null,//用户超时定时器
      outDown:0,//用户超时倒计时30秒
      outTimeVisible:false,
      isOutDown:false,
      showButton:false,
      //选择题ABCD
      letterArr:[],
      //记录用户开始考试时间
      startExamTime:null,
      password:null,//考试口令密码

      typeOptions:[],
      isShowAnswer:false,
      showVisible:false,
      delInfo:[],
      answersRecordArr:[],
      answersDs: null,
      examStatus:false,
    }
  },
  created() {
    this.examIdNow = this.$route.params && this.$route.params.examId;
    if (this.examId){
      this.examIdNow = this.examId;
      this.getExamDetail(this.examId)
    }else if (this.examIdNow) {
      this.getExamDetail(this.examIdNow)
    }
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  mounted() {
    // this.heart = setInterval(this.getHeart,3000);
    // document.addEventListener('visibilitychange',this.handleVisiable);
  },
  destroyed() {
    clearInterval(this.heart);
    clearInterval(this.answersDs)
    document.removeEventListener('visibilitychange',this.handleVisiable);
  },
  beforeDestroy() {
    clearTimeout(this.heart);
    clearTimeout(this.answersDs);
  },
  methods: {

    // delClose(){
    //   this.$emit("delClose")
    // },

    //处理组合题答题状态
    selectChangeBySix(child,index,fatherData){
      if(child.type == '1' || child.type == '3'){
        child.answers.forEach((item,i)=>{
          if(index!=i){
            item.selectFlag = false;
          }else{
            item.selectFlag = true;
          }
        })
        child.answeredFlag = true;

      }else if(child.type=='4'){
        child.answers.forEach((item,i)=>{
          if(item.answered!=null&&item.answered!=''){
            child.answeredFlag = true;
            return
          }else{
            child.answeredFlag = false;
          }
        })
      }else if(child.type == '5'){
        if(child.answered != null && child.answered != ''){
          child.answeredFlag = true;
        }else{
          child.answeredFlag = false;
        }
      }else if(child.type == '2'){
        let tag = 0;
        child.answers.forEach((item,i)=>{
          if(!item.selectFlag){
            tag +=1
          }
        })
        if(child.answers.length == tag){
          child.answeredFlag = false;
        }

      }

      let num = 0;
      fatherData.children.forEach((item,i)=>{
        if(item.answeredFlag){
          num+=1;
        }
      })
      if(fatherData.children.length==num){
        fatherData.answeredFlag = true
      }else{
        fatherData.answeredFlag = false
      }
      let obj={
        quId:null,
        answers:{
          answerId:null,
          selectFlag:null,
          answered:null
        },
        answered:null,
        answeredFlag:null,
        paperUnitId:null
      }
      obj.quId = child.quId
      obj.answers = child.answers
      obj.answered = child.answered
      obj.answeredFlag = child.answeredFlag
      obj.paperUnitId = child.paperUnitId
      this.answersRecordArr.push(obj)
    },

    //处理答题状态
    selectChange(data,cIndex){
      data.answeredFlag = true;
      if(data.type == '1' || data.type == '3'){
        data.answers.forEach((item,i)=>{
          if(cIndex!=i){
            item.selectFlag = false;
          }else{
            item.selectFlag = true;
          }
        })
        data.answeredFlag = true;
      }else if(data.type=='4'){
        data.answers.forEach((item,i)=>{
          if(item.answered!=null&&item.answered!=''){
            data.answeredFlag = true;
            return
          }else{
            data.answeredFlag = false;
          }
        })
      }else if(data.type == '5'){
        if(data.answered != null && data.answered != ''){
          data.answeredFlag = true;
        }else{
          data.answeredFlag = false;
        }
      }else if(data.type == '2'){
        let tag = 0;
        data.answers.forEach((item,i)=>{
          if(!item.selectFlag){
            tag +=1
          }
        })
        if(data.answers.length == tag){
          data.answeredFlag = false;
        }
      }
      let obj={
        quId:null,
        answers:{
          answerId:null,
          selectFlag:null,
          answered:null
        },
        answered:null,
        answeredFlag:null,
        paperUnitId:null
      }
      obj.quId = data.quId
      obj.answers = data.answers
      obj.answered = data.answered
      obj.answeredFlag = data.answeredFlag
      obj.paperUnitId = data.paperUnitId
      this.answersRecordArr.push(obj)
    },


    submitAnswer(){
      let arr = this.answersRecordArr
      this.answersRecordArr.forEach((item, index) => {
        submitAnswer(this.subData.recordId,item).then(res=>{
          this.answersRecordArr.splice(index, 1);
        })
      })


    },
    setDefaultImage(e) {
      e.target.src = require("@/assets/images/examine_cover.png");
    },
    /** 超时未操作倒计时结束触发方法 */
    downOutTime(){
      // if(this.isOutDown){
      //   this.$message.error("您已超过 "+ this.examInfo.noActLimit +" 分钟未进行任何操作，已被系统自动交卷，本次考试已结束")
      //   this.closeOutTime('0');
      // }
      this.$message.error("您已超过 "+ this.examInfo.noActLimit +" 分钟未进行任何操作，已被系统自动交卷，本次考试已结束")
      this.closeOutTime('0');
      this.outTimeVisible = false;
    },

    //超时强制交卷方法
    closeOutTime(status){
      if(status==='0'){
        this.isOutDown = true;
        this.outTimeVisible = false
        this.submitTestPaper();
      }else if(status==='1'){
        this.outDown = 0;
        this.outTimeVisible = false
        this.moveEvent()//鼠标监听
      }
    },

    //监控鼠标移动和点击事件
    moveEvent(){
      console.log("监听开始。。。。")
      if(!this.examStatus){
        if(this.examInfo.noActLimit>0){
          clearTimeout(this.outTime);
          this.init();
        }
      }
    },
    //用户超时定时器方法
    init(){
      let time = parseInt(this.examInfo.noActLimit)*1000*60
      // let time = 60000
      if(time > 0){
        this.outTime = setTimeout(()=>{
          this.outTimeVisible = true;
          this.outDown = 30;
        },time);
      }
    },

    //监控用户切出当前页面
    handleVisiable(e){
      let turnThreshold = parseInt(this.subData.leaveLimit);
      if(turnThreshold > 0){
        switch (e.target.visibilityState){
          case "hidden":
            this.outCount+=1;
            console.log("内容不可见!");
            if(this.outCount===turnThreshold){
              // this.$message.error("警告，您已离开考试页面3次，如再次离开则强制交卷！")
              alert("警告，您已离开考试页面"+ this.outCount +"次，如再次离开则强制交卷！")
              break;
            }else if(this.outCount>turnThreshold){
              // alert("警告，您已离开考试页面超过"+ turnThreshold +"次，开始强制交卷！");
              this.submitTestPaper();
              break;
            }else{
              alert("警告，您已离开考试页面"+ this.outCount +"次！");
              break;
            }
            break;
          case "visible":
            console.log("正常展示页面！");
            break;
        }
      }
    },


    /** 关闭dialog提示框 */
    // closePaper(){
    //   this.dialogVisiblePaper = false;
    //   this.$store.dispatch("tagsView/delView", this.$route);
    //   this.$router.push({path:"/examInfo",query: {t: Date.now()}});
    // },

    submitTheTestPaper(){
      //判断是否到提前交卷时间
      if(this.subData.handMinTime>0){
        let nowTime = new Date().getTime();
        let usedTime = (nowTime - this.startExamTime)/1000/60
        if(this.subData.handMinTime>usedTime){
          this.$message.error("还未到达最短交卷时长，请认真答题！")
          return
        }
      }
     //遍历所有题目状态是否为已答题
      let answeredCount = 0;
      this.allQuestion.forEach((item,i) => {
        item.questions.forEach((cItem,j) => {
          console.log(cItem,i)
          if(cItem.answeredFlag==false){
            answeredCount++;
          }
        })
      })

      this.$confirm("是否提交考试！" + ((answeredCount === 0) ? "" : "你还有" + answeredCount + "道题未答！"), "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        this.submitTestPaper(loading);
      }).catch(() => {});
      console.log(no,noE)
    },
    closeDialog() {
      if(this.dialogVisible){
        this.dialogVisible=false
        this.submitTestPaper();
      }
    },
    downTime(){
      console.log('22222')
      this.dialogVisible = true;
      setTimeout(this.closeDialog,3000);
    },
    /** 提交考试 */
    submitTestPaper(loading){
      this.examStatus = true;
      document.removeEventListener('visibilitychange',this.handleVisiable);
      this.subData.paperUnit = this.allQuestion;
      submitTheExam(this.subData).then((res)=>{
        if(res.code == '200'){
          console.log("交卷成功！")
          if(this.examInfo.showScore == 0){
            this.delInfo = {'本次考试成绩':'不显示！'}
            this.showVisible = true;
          }
          if(this.examInfo.showScore != 0&& this.examInfo.showScore != 1){
            // this.dialogVisiblePaper = true;
            let info = {}
            if(this.examInfo.showScore==2) info = {"本次考试成绩":'请等待试卷批改完成后在历史考试中查看！'}
            if(this.examInfo.showScore==3) info = {"本次考试成绩":'请在'+this.$moment(this.examInfo.showScoreTime).format('YYYY年MM月DD日 HH时mm分ss秒')+'之后查看！'}
            if(this.examInfo.showScore==4) info = {"本次考试成绩":'请等待管理员人工发布！'}
            this.delInfo = info;
            this.showVisible = true;
            setTimeout(() => {
              loading.close();
              this.goBack();
            }, 3000);
          }else if(this.examInfo.showScore==1){
            getHistoryExam(this.subData.recordId).then((res)=>{
              console.log(res);
              this.paperScore = res.data.userScore;
              let info = {"本次考试成绩":this.paperScore+'分！'}
              this.delInfo = info;
              this.showVisible = true;
              setTimeout(() => {
                loading.close();
                this.goBack();
              }, 3000);
            })
          }
        }
      }).catch(
        setTimeout(() => {
          loading.close();
          this.goBack();
        }, 3000)
      )
    },
    startTheExam(){
      if(this.examInfo.records.length>0){
        const h = this.$createElement;
        this.$msgbox({
          title: '温馨提示',
          message: h('p', null, [
            h('p', null, '本场考试中有您未完成的考试记录 '),
            h('p', null, '考试时间:'+this.examInfo.records[0].startTime),
            h('p', null, '放弃正在进行的考试将会消耗掉一次考试机会，若考试机会用尽则不可再开始新的考试，请您慎重选择。')
          ]),
          showCancelButton: true,
          confirmButtonText: '继续考试',
          cancelButtonText: '放弃重考',
          type: 'warning'
        }).then(() => {
          this.beginExam(this.examInfo.records[0])
        }).catch(() => {
          giveUpExam(this.examInfo.records[0].recordId)
          this.beginExam()
        });
      }else{
        this.beginExam()
      }

    },

    //开始考试考试
    beginExam(record){

      if(this.subData.openType==5){
        if(this.password==null||this.password==''){
          this.$message.error("请输入口令密码！")
          return
        }
      }
      if (this.examInfo.examId){
        const loading = this.$loading({
          lock: true,
          text: '正在创建试题！',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        let params={
          examId:this.examIdNow,
          password:this.password,
          recordId:record==undefined?null:record.recordId
        }
        startExam(params).then(res=>{
          console.log(res,"res")
          if (res && res.data){
            this.startExamTime = new Date().getTime();
            document.addEventListener('visibilitychange',this.handleVisiable);
            // this.heart = setInterval(this.getHeart,3000);
            this.subData = res.data;
            this.infoShow = false;

            //获取试题的数据
            const { paperUnit } = res.data;
            if (paperUnit && paperUnit.length > 0){
              this.allQuestion = paperUnit;
              if(this.subData.totalTime>0){
                if(record==undefined){
                  this.countdown = (parseInt(this.subData.totalTime) * 60) ? (parseInt(this.subData.totalTime) * 60) : 3600;
                }else{
                  //继续考试
                  this.countdown = parseInt((new Date(record.logicEndTime) - new Date()) / 1000)
                }
              }

              this.moveEvent()//开始监听鼠标是否移动
              this.$forceUpdate();
              setTimeout(() => {
                loading.close();
              }, 1000);

            }
           this.answersDs = setInterval(this.submitAnswer,3000)
          }else{
            this.$message.error(res.msg);
            loading.close();
            setTimeout(() => {
              this.goBack();
            }, 3000);
          }
        }).catch(loading.close())
      }
    },
    goBack(){
      this.$store.dispatch('tagsView/delView', this.$route);
      window.open('','_self').close();
      // this.$router.go(-1);
    },
    getExamDetail(id) {
      getExamInfo(id).then(res => {
        if(res.data == undefined){
          this.showButton = true;
        }else if (res && res.data){
          this.examInfo = res.data;
        }
      })
    },

    // 题目类型字典翻译
    typeFormat(lx, column) {
      return this.selectDictLabel(this.typeOptions, lx);
    },

    //锚点
    jumpClick(index){
      document.querySelector(`#visit_${index}`).scrollIntoView({
        behavior: "smooth"
      });
    },

    jumpTmClick(u_index,t_index){
      document.querySelector(`#tm_${u_index}_${t_index}`).scrollIntoView({
        behavior: "smooth"
      });
    },
    giveUp(recordId){
      this.$confirm('是够确认放弃考试?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return giveUpExam(recordId)
      }).then(() =>{
        this.getExamDetail(this.examIdNow)
      }).catch(() => {
      });
    }
  }

}
</script>
<style scoped lang="scss">
.app-container-xq{
  padding: 20px;
  margin: 0 auto;
}
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.pre-exam {
  display: inline-block;
  width: 50%;
  > div{
    line-height: 42px;
    color: #555;
  }
}

.app-container-left{
  border: 1px rgba(0, 0, 0, 0.08) solid;
  padding: 12px;
  >div{
    margin-bottom: 12px;
  }
  .title{
    font-size: 17px;
    font-weight: 400;
  }
  .time{
    height: 40px;
    font-size: 15px;
    font-weight: 400;
    border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
  }
  .theRemainingTime{
    padding-bottom: 20px;
    border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
    >div{
      text-align: center;
      margin-top: 16px;
      &:nth-child(2){
        font-size: 20px;
        color: red;
      }
    }
  }
  .submit{
    text-align: center;
    padding-top: 20px;
  }
  .tags{
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
    > a {
      >span{
        margin-right: 6px;
        margin-bottom: 6px;
      }
    }
  }
}
.app-container-right{
  border: 1px rgba(0, 0, 0, 0.08) solid;
  height: calc(100vh - 124px);
  overflow-y: scroll;
  .customStyle{
    border: 0;
    ::v-deep{
      .el-input__inner{
        border: 0 transparent solid;
        border-bottom: 1px solid red;
        border-radius: 0;
      }
    }
  }
  ::v-deep{
    .el-card{
      border-radius: 0;
      .el-card__header{
        background-color: rgba(250,250,250,1);
      }
      .el-card__body{
        label{
          font-weight: 500;
          width: 100%;
          margin-left: 15px;
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

//-------------------------------分割线---------------------------
.top1{
  padding: 0 10px;
  font-size: 14px;
  font-weight: 600;
  div{
    line-height: 40px;
  }
}
.content{

}
.tj-tm{
  margin-top: 15px;
  div{
    margin-top: 4px;
  }
}
.j-italic{
  color: #e35725;
  font-style: italic;
}
.card-item{
  padding: 0px 10px;
  font-size: 14px;
  div:first-child{
    color: #e35725;
    font-weight: 600;
    font-size: 15px;
    cursor: pointer;
    &:hover{
      color: #53a2eb;
    }
  }
}
.nav-area{
  height: calc(100vh - 45px);
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  overflow: auto;
  padding: 10px 0px;
}
.bh-item{
  margin-top: 10px;
  .el-tag{
    margin-right: 10px;
    margin-top: 10px;
    width: 40px;
    text-align: center;
  }
}
.span20-right{
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}
.unit-title{
  padding: 20px 0px;
  font-size: 18px;
  color:#e35725;
  font-weight: 600;
  margin-left: -4px;
}
::v-deep .tm-sel{
  margin-left: 30px;
}
.child-tm{
  padding-left: 30px;
  margin-bottom: 30px;
}
::v-deep .answer-item{
  margin-left: 30px;
}
.span16-right{
  //width: calc(100% - 280px - 280px - 10px - 10px) !important;
}
.print-btn{
  text-align: center;
  margin-top: 44px;
  margin-bottom: 50px;
}
.divider{
  margin-top: 20px;
  border-bottom: #eee 2px dotted;
  margin-bottom: 20px;
}
.el-tag{
  cursor: pointer;
}

//------------------------------子题目分割线-------------------------------
.tm-item{
  font-size: 16px;
}
.tm-title{
  display: inline-block;
  margin-left: 0px;
  margin-bottom: 20px;
  ::v-deep p:nth-of-type(1){
    display: inline;
  }
}
.tm-sel{
  margin-bottom: 30px;
  & > div{
    padding:18px 0px 0px 5px;
  }
  .el-checkbox{
    margin-right: 5px;
  }
}
.tm-da-item{
  display: flex;
  flex-wrap: wrap;
  color: #6b6b6b;
}
.tm-da{
  margin-top: 6px;
}
.tm-jx{
  margin-top: 6px;
}
.tm-tk{
  > span{
    padding-bottom: 2px;
    span:not(:first-child){
      &:before{
        content: " / ";
      }
    }
  }
}
.answer-item{
  border: #eee 1px solid;
  border-radius: 5px;
  line-height: 24px;
  margin: 5px 0px 10px 0px;
  padding: 10px;
  font-size: 14px;
  color: #555;
  background: #fcfcfc;
}
.xh{
  height: 30px;
  width: 30px;
  background: url("../../../../assets/images/tag.png") 100% 100% no-repeat;
  color: #fff;
  text-align: center;
  line-height: 25px;
  margin-right: 5px;
}
.child-xh{
  height: 20px;
  width: 20px;
  background: url("../../../../assets/images/fang20.png") 100% 100% no-repeat;
  color: #fff;
  margin-right: 10px;
  text-align: center;
  line-height: 20px;
}
.rightStyle{
  color: #67c23a
}
.errorStyle{
  color: #f56c6c;
}

//----------------------------分割线  显示成绩弹窗-----------------------------
::v-deep .el-dialog__body {
  padding: 8px 20px 20px;
}
.tip{
  font-size: 16px;
  font-weight: 600;
  i{
    margin-right: 5px;
  }
}
.del-con{
  border:1px solid #dddddd;
  padding: 6px 14px;
  margin-top: 24px;
  position: relative;
}
span.title{
  display:block;
  width:70px;
  position:absolute;
  top:-9px;
  text-align: center;
  background: white;
}
.con-body{
  margin-top: 15px;
}
.del-item{
  padding: 0px 8px 8px;
}
.jx-exam{
  display: flex;
  min-height: 20px;
  width: 100%;
  border:1px solid #eeeeee;
  padding: 10px;
  margin-bottom: 20px;
  >div:nth-child(1){
    margin-right: 10px;
  }
  >div:nth-child(2){
    div{
      line-height: 30px;
    }
    width: calc(100% - 140px);
  }
  >div:nth-child(3){
    width: 60px;
    i{
      cursor: pointer;
    }
    i:first-child{
      font-size: 18px;
      color: red;
      float: right;
    }
    i:nth-of-type(2){
      font-size: 30px;
      font-weight: 700;
      margin-top: 20px;
      color: #6c6c72;
    }
  }
  img{
    width: 60px;
    height: 60px;
  }
}
</style>
