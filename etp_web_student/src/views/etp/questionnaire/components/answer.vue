<template>
  <div class="app-container">
      <div class="paper pre-pad">
        <div style="margin-left: 20px;padding:0 30px 30px">
          <!--  // 0 单选题 1 复选题 2 填空题 3 问答题-->
          <div id="dx" style="padding-top: 10px">
            <el-tag type="danger" style="width: 100%" v-if="questionForm.wjnr!=null">问卷已经作答，请查看详情，不能再次提交</el-tag>
            <el-tag type="danger" v-else-if="Date.parse(questionForm.beginTime) >new Date()">问卷还未开始，不能提交答题</el-tag>
            <el-tag type="danger" v-else-if="Date.parse(questionForm.endTime) < new Date()">问卷已经结束，不能提交答题</el-tag>

            <div class="con-title">
              <span>{{ questionForm.wjmc }} </span>
              <span style="color: #ef1111" v-if="Date.parse(questionForm.endTime) < new Date()">(已结束)</span>
              <span style="color: #cea424" v-else-if="Date.parse(questionForm.beginTime) > new Date()">(未开始)</span>
              <span style="color: green" v-else>(正在收集)</span>
            </div>
            <div class="con-tags">
              <el-row style="margin-top: 30px;pointer-events: none">
                <el-col :span="8">
                  <label>标签：</label>
                  <span v-if="tags==null">无</span>
                  <span v-else><el-tag v-for="item in (tags||'').split(',')">{{ item }}</el-tag></span>
                </el-col>
                <el-col :span="4"><el-checkbox v-model="questionForm.sfnm">匿名收集</el-checkbox></el-col>
                <el-col :span="5"><label>开放程度：</label><span>{{kfcdType(questionForm.kfcd)}}</span></el-col>
                <el-col :span="7"><label>截止时间：</label><span>{{questionForm.beginTime}} ~ {{ questionForm.endTime }}</span></el-col>
              </el-row>
            </div>
            <el-divider></el-divider>

<!--            <div style="text-align: center"><h1> {{ questionForm.wjmc }}</h1></div>-->
            <div v-if="questionForm.nr && questionForm.nr.length > 0"
                 v-for="(item,index) in questionForm.nr">
              <p style="font-size: 18px;margin-left: 15px;margin-top: 15px">
                {{ index +1 }} 、{{ item.tg }} <span style="color: green">【{{ typeDel(item.type)}}】</span> </p>
              <div class="t-content">
                <template v-if="(item.type == '0') && item.xxList.length > 0">

                  <el-radio-group v-model="item.da">
                    <el-radio v-for="t in item.xxList" :label="t.key" :key="t.key" @change="onInput()" :rules="{ required: true, message: '选项不能为空', trigger: 'blur'}">
                      {{ t.key }} 、{{ t.value }}
                    </el-radio>
                  </el-radio-group>

                </template>

                <template v-if="(item.type == '1') && item.xxList.length > 0">
                  <el-checkbox-group v-model="dxs">
                    <el-checkbox v-for="t in item.xxList" :label="t.key" :key="t.key" @change="onInput(),setDxs(index)" :rules="{ required: true, message: '选项不能为空', trigger: 'blur'}">{{ t.key }} 、{{ t.value }}</el-checkbox>
                  </el-checkbox-group>
                </template>

                <template v-if="(item.type == '2' || item.type == '3')">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 4}"
                    placeholder="请输入内容"
                    v-model="item.da"
                    @input="onInput()">
                  </el-input>
                </template>
              </div>

            </div>
          </div>
<!--          <button @click="confirm1">queding</button>-->
          <div align="center" style="padding: 10px;">

            <el-button type="primary" v-if="questionForm.wjnr==null && new Date()>Date.parse(questionForm.beginTime) && new Date()< Date.parse(questionForm.endTime)"
                       v-hasPermi="['etp:questionnaire:edit']"
                       @click="submitAnswer()">提交</el-button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
import {
  getQuestionnaire,
  updateQuestionnaire
} from '@/api/etp/questionnaire/questionnaire'
export default {
  name: "answer",
  data(){
    return{
      questionForm:{},
      tags:null,
      dxs:[]
    }
  },
  created() {
    let id = this.$route.query.id;
    this.tags = this.$route.query.tags;
    if (id !== undefined) {
      this.getQuestion(id);
    }
  },
  methods:{
    kfcdType(type){
      let t = '';
      switch (type) {
        case '0':
          t = '完全公开';break;
        case '1':
          t = '指定用户';break;
        case '2':
          t = '不公开';break;
        default:
          break;
      }
      return t;
    },
    //强制刷新方法
    onInput(){
      this.$forceUpdate();
    },

    // confirm1(){
    //   console.log(this.dxs)
    //   console.log(this.questionForm)
    // },
    //返回上级页面
    back(){
      this.$store.dispatch("tagsView/delView", this.$route);
      // this.$router.push({
      //   path:"/system/questionnaire",
      // });
      this.$router.go(-1)
    },
    typeDel(type){
      let t = '';
      switch (type) {
        case '0':
          t = '单选题';break;
        case '1':
          t = '复选题';break;
        case '2':
          t = '填空题';break;
        case '3':
          t = '问答题';break;
        default:
          break;
      }
      return t;
    },
    getQuestion(id){
      getQuestionnaire(id).then(res => {

        this.questionForm = res.data;

        if(this.questionForm.nr){
          this.questionForm.nr = JSON.parse(this.questionForm.nr)
          this.questionForm.nr.forEach(item =>{
            item['da'] = ''
          })
          // console.log(this.questionForm)
        }
      })
    },
    setDxs(val){
      this.questionForm.nr.forEach((item,index) => {
        console.log(JSON.stringify(this.dxs))
        if(index == val){
          let dx = ''
          for (let i=0;i<this.dxs.length;i++){
           dx +=this.dxs[i] + ",";
          }
          item['da'] = dx;
        }
      })
    },
    submitAnswer(){
      // this.questionForm.nr = JSON.stringify(this.questionForm.nr)
      let form = JSON.parse(JSON.stringify(this.questionForm))
      form.nr = JSON.stringify(this.questionForm.nr)
      updateQuestionnaire(form).then(response =>{
        this.msgSuccess("提交成功");
        this.back()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.subjectBox span{
  line-height: 40px;
  font-size: 15px;
}
.pre-desc:before{
  content: "";
  width: 3px;
  height:20px;
  border-radius: 5px;
  position: absolute;
  top: 0;
  left: 0;
  background: blue;
}
.pre-pad {
  padding: 15px 8px 8px;
}
.textBox{
  border-radius: 5px;
  /*height: 260px;*/
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.paper{
  height: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  border-radius: 5px;
  margin-top: 15px;
}
.pre-pad {
  padding: 15px 8px 8px;
}
.radio-xz {
  padding-left: 50px;
}
::v-deep .el-radio,.el-checkbox{
  display: block;
  line-height: 32px;
  white-space: normal;
  margin-right: 0;
}
.t-content{
  margin-left: 40px;
}
.con-title{
  font-size: 20px;
  font-weight: 600;
  text-align: center;

}
.con-tags {
  margin-top: 40px;
  label {
    font-weight: 500;
    color: #5f5f5f;
  }

  .el-row {
    font-size: 12px;
    line-height: 32px;
  }

  ::v-deep.el-checkbox__label {
    font-size: 12px;
  }
}
::v-deep.el-divider--horizontal{
  margin: 8px 0 20px;
}
.el-tag{
  margin-right: 6px;
}
</style>
