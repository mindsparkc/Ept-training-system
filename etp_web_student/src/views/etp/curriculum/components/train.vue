<template>
<div>
  <el-form label-position="top"  ref="form" label-width="100px">
    <el-row>
      <div class="box-card" v-for="(item,key,index) in trainSubjects" :key="index">
        <div slot="header" class="unit-title">
          <span>{{item.display}} </span>
        </div>
        <div v-for="(question,i) in item.questions" style="margin-bottom: 30px;">
          <el-row :id="`tm_${k}_${i}`">
            <el-col :span="20">
              <questionView
                :data="question"
                :bqShow="false"
                :is-show-answer="isShowAnswer"
                @selectChange="selectChange"
                :xh="i+1">
              </questionView>
            </el-col>
          </el-row>


          <div class="divider"></div>
        </div>

      </div>
      <el-button type="primary" style="margin-top: 10px" @click="submitDa">提交答案</el-button>
      <el-button  style="margin-top: 10px" @click="clearDa">清空答案</el-button>
    </el-row>
  </el-form>
</div>
</template>

<script>
import {getTrainInfo} from "@/api/train/train";
import questionView from "./questionView"
import {insertCTrainInfo,selectCTrainInfo,deleteCTrainInfo} from "@/api/curriculm/curriculum";

export default {
  name: "train",
  components: {questionView},
  props:{

    cid:{
      type:Number
    },
    trainId:{
      type:Number
    }

  },
  data(){
    return {
      trainDetails:[],
      trainSubjects:[],

      isShowAnswer: false,
      answersRecord: {}
    }
  },

  methods:{

    async getTrainDetails(paperId){
      await getTrainInfo(paperId).then(res=>{
        this.trainDetails = res.data
        this.trainSubjects = res.data.paperUnit

      })
      this.getCTrainStudent()

    },
    //获取习题集答案
    getCTrainStudent(){
      let params ={
        cid:this.cid,
        trainId:this.trainId,
      }
      selectCTrainInfo(params).then(res=>{
        let data = res.data
        if(null != data){
          this.answersRecord = JSON.parse(data.da)
          this.setSubjectDa()
        }
      })
    },
    //答案赋值
    setSubjectDa(){
      this.trainSubjects.forEach(item=>{
        let  questions = item.questions
        let paperUnitId = item.paperUnitId
        questions.forEach(q=> {
          if(item.quType != 6){
            let quid = q.quId
            let da = this.answersRecord[paperUnitId + "_" + quid]
            this.setDaByType(q,da)
          }else{
            q.children.forEach(c=>{
              let quid = c.quId
              let da = this.answersRecord[paperUnitId + "_" + quid]
              this.setDaByType(c,da)
            })
          }
        })
      })

    },
    setDaByType(q,da){
      if(da!=undefined){

        if(q.type == 1||q.type == 2||q.type == 3){
          q.answers.forEach((a,index)=>{
            this.$set(a,'selectFlag',da.answers[index].selectFlag)
          })
        }
        if(q.type == 4){
          q.answers.forEach((a,index)=>{
            this.$set(a,'answered',da.answers[index].answered)
          })
        }
        if(q.type == 5){
          this.$set(q,'answered',da.answered)
        }
      }
    },
    //处理答题状态
    selectChange(params){
      let {data,cIndex,fatherData} =params
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
      this.answersRecord[data.paperUnitId+"_"+data.quId] = obj
    },


    //提交习题集答案
    submitDa(){
      let params ={
        cid:this.cid,
        trainId:this.trainId,
        da:JSON.stringify(this.answersRecord),
      }
      insertCTrainInfo(params).then(res=>{
        this.$message.success("提交成功")
      })
    },
    clearDa(){
      this.$confirm('清空答案清除答题记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params ={
          cid:this.cid,
          trainId:this.trainId,
        }
        deleteCTrainInfo(params).then(res=>{
          this.$message({
            type: 'success',
            message: '清空成功!'
          });
          location.reload();
        })

      }).catch(() => {

      });

    }
  }
}
</script>

<style scoped lang="scss">
.unit-title{
  padding: 20px 0px;
  font-size: 18px;
  color:#e35725;
  font-weight: 600;
  margin-left: -4px;
}
.divider{
  margin-top: 20px;
  border-bottom: #eee 2px dotted;
  margin-bottom: 20px;
}
</style>
