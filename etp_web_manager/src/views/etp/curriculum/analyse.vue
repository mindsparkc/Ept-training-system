<template>
<div class="app-container analy-div">
  <div style="font-weight: 600;">【{{kcmc}}】课程数据分析</div>
  <!--  上部分-->
  <el-row  :gutter="20" class="top">
    <el-col :span="18">
      <el-row :gutter="20" >
        <el-col :span="8">
          <div class="right-item">
            <div class="title">总计学习时长</div>
            <div class="desc">截止 {{lastDay}} 已学习</div>
            <div class="num">{{ kcLearnTime }}<span>分钟</span></div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="right-item">
            <div class="title">已领取课程的用户</div>
            <div class="desc">截止 {{nowDay}} 已领取</div>
            <div class="num">{{ receivedKcUser }}<span>人</span></div>
            <!--          <div class="desc">最后一次领取课程发生在 {{'2022-08-09'}} </div>-->
          </div>
        </el-col>
        <el-col :span="8">
          <div class="right-item">
            <div class="title">总计包含课件</div>
            <div class="desc">截止 {{nowDay}}</div>
            <div class="num">{{ courseNum }}<span>个</span></div>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="top-left">
            <div class="title">已完成用户</div>
            <div class="desc">截止 {{nowDay}} 已完成</div>
            <div class="num">{{ completedKcUser }}<span>人</span></div>
            <lineChart
              :id-name="'userLine'"
              :height="'220px'"
              :x-data="lineData.dataX"
              :y-data="lineData.dataY"
              ref="userLine"></lineChart>
          </div>
        </el-col>
      </el-row>
    </el-col>
    <el-col :span="6" style="height: 540px" >
      <div class="kj-sort">
        <div class="title">各课件学习时长</div>
        <div class="kj-item" v-for="item in kcListByKjTime">
          <div>{{item.mc}}</div>
          <div>{{timeTransfer(item.studyTime)}}</div>
        </div>
      </div>
    </el-col>
  </el-row>

  <!--  下半部分-->
  <el-row class="bottom">
    <div class="title">近七日课程的各课件完成人数</div>
    <el-table v-loading="loading" :data="tableData" width="100%"  border>
      <el-table-column prop="id"  label="课件编号"></el-table-column>
      <el-table-column prop="mc"  label="课件名称"></el-table-column>
      <el-table-column v-for="(item,index) in lineData.dataX" :key="index" :label="item" :property="item" align="center">
        <template slot-scope="scope">
          {{scope.row[scope.column.property]}}</template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getCompletedKjByDate"
    />
  </el-row>
</div>

</template>

<script>
import lineChart from '@/views/etp/curriculum/components/charts/lineChart'
import {allCompletedKcUser,completedCourse,completedKcUserByDate,completedKjByDate,
  getCourseNum,yesterdayStudyKc,receivedKcUser,getKcLearnTime,getKcListByKjTime}from "@/api/etp/curriculum/analyse";
export default {
  name: 'analyse',
  components:{
    lineChart,
  },
  data(){
    return{
      // 遮罩层
      loading: true,
      cid: null,//课程id
      kcmc: null,//课程名称
      nowDay: null,//今天
      lastDay:null,//昨天,
      completedKcUser:0,
      lineData: {
        dataX:['2022-4-11','2022-4-12','2022-4-13','2022-4-14','2022-4-15','2022-4-16','2022-4-17'],
        dataY:[0,0,0,0,0,0,0]
      },
      completedCourse:0,
      receivedKcUser:0,
      yesterdayStudyKc:0,
      courseNum:0,
      kcLearnTime:0,
      kcListByKjTime:[],
      // 总条数
      total: 0,
      tableData:[],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
      },
    }
  },
  created(){
    //获取路由课程id
    this.cid = this.$route.query.cid;
    this.kcmc = this.$route.query.mc;
    this.nowDay = this.$moment().day(this.$moment().day()).format('YYYY年MM月DD日')
    this.lastDay = this.$moment().day(this.$moment().day()-1).format('MM月DD日')
    this.getAllCompletedKcUser()
    this.getCompletedKcUserByDate()
    // this.getCompletedCourse()
    this.getReceivedKcUser()
    this.getYesterdayStudyKc()
    this.getCourseNum()
    this.getCompletedKjByDate()
    this.getKcLearnTime()
    this.getKcListByKjTime()
  },
  methods:{
    //已完成课程的用户数
    getAllCompletedKcUser(){
      allCompletedKcUser(this.cid).then(res => {
        this.completedKcUser=res.data
      })
    },

    //近七日课程完成人数
    getCompletedKcUserByDate(){
      completedKcUserByDate(this.cid).then(res => {
        let datax = new Array();
        let datay = new Array();
        datax =  Object.keys(res.data)
        datay =  Object.values(res.data)
        this.$set(this.lineData,"dataX",datax)
        this.$set(this.lineData,"dataY",datay)
        this.$refs["userLine"].initChart()
      })
    },

    //课件总计已完成
    getCompletedCourse(){
      completedCourse(this.cid).then(res => {
        this.completedCourse=res.data
      })
    },
    //课程学习总时长
    getKcLearnTime(){
      getKcLearnTime(this.cid).then(res => {
        if(res.code ==200){
          this.kcLearnTime = res.data
        }
      })
    },

    //已领取课程用户数
    getReceivedKcUser(){
      receivedKcUser(this.cid).then(res => {
        this.receivedKcUser=res.data
      })
    },

    //昨日学习课程用户数
    getYesterdayStudyKc(){
      yesterdayStudyKc(this.cid).then(res => {
        this.yesterdayStudyKc=res.data
      })
    },
    //课程总计包含课件数
    getCourseNum(){
      getCourseNum(this.cid).then(res => {
        this.courseNum=res.data
      })
    },
    //课程各课件完成情况
    getCompletedKjByDate(){
      this.queryParams.id = this.cid
      completedKjByDate(this.queryParams).then(res => {
        this.total = res.total;
        this.tableData=res.rows
        this.loading = false;
      })
    },
    //课程各课件学啥习时长排序
    getKcListByKjTime(){
      getKcListByKjTime(this.cid).then(res => {
        this.kcListByKjTime =res.data
      })
    },
    //学习时长单位转换
    timeTransfer(min){
      if(min>60){
        return (min/60).toFixed(2)+ " h"
      }else{
        return min+ " min"
      }
    }
  }
}
</script>

<style scoped lang="scss">
.el-col{
  padding-left: 14px;
}
.top{
  margin-top: 20px;
}
.bottom{
  margin-top: 20px;
  padding-left: 14px;
  min-height: calc(100vh - 84px - 61px - 360px - 20px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-bottom: 20px;
}

.right-item{
  height: 160px;
  padding: 10px 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.top-left{
  margin-top: 20px;
  height: 360px;
  padding: 10px 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.analy-div .title {
  position: relative;
  padding-left: 10px;
  margin: 12px 0;
  color: #4c4c4c;
  font-weight: 600;
  text-align: left;
}
.title:before {
  content: "";
  width: 4px;
  height: 16px;
  border-radius: 5px;
  position: absolute;
  top: 3px;
  left: 2px;
  background: blue;
}
.desc{
  font-size: 14px;
  color: #8d8d8d;
  margin-left: 2px;
}
.num{
  font-size: 26px;
  font-weight: 600;
  margin: 20px 6px 5px 4px;
  color: #5f5f5f;
  span{
    font-size: 14px;
    font-weight: 500;
    margin-left: 10px;
  }
}
.kj-sort{
  width: 100%;
  height: 100%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  color: #303133;
  padding: 10px 10px;
  overflow: auto;
  .kj-item {
    padding: 10px 10px 0px;
    display: flex;
    position: relative;
    div{
      &:nth-of-type(1){
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        width: 84%;
      }
      &:nth-of-type(2){
        position: absolute;
        right: 20px;
        color: #7a7b7d;
      }
    }
  }
}

</style>
