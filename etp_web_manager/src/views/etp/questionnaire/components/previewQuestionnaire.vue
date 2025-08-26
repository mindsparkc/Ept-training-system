<template>
  <div class="app-container">
    <div class="textBox pre-pad">
      <div style="margin-left: 20px;padding:0 30px 30px">
        <!--  // 0 单选题 1 复选题 2 填空题 3 问答题-->
        <div id="dx" style="padding-top: 10px">
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
                <span v-if="questionForm.tags==null">无</span>
                <span v-else><el-tag v-for="item in (questionForm.tags||'').split(',')">{{ item }}</el-tag></span>
              </el-col>
              <el-col :span="4"><el-checkbox v-model="questionForm.sfnm">匿名收集</el-checkbox></el-col>
              <el-col :span="5"><label>开放程度：</label><span>{{kfcdType(questionForm.kfcd)}}</span></el-col>
              <el-col :span="7"><label>截止时间：</label><span>{{questionForm.beginTime}} ~ {{ questionForm.endTime }}</span></el-col>
            </el-row>
          </div>
          <el-divider></el-divider>

<!--          <div style="text-align: center"><h1> {{ questionForm.wjmc }}</h1></div>-->
          <div v-if="questionForm.nr && questionForm.nr.length > 0"
               v-for="(item,index) in questionForm.nr">
            <p style="font-size: 18px;margin-left: 15px;margin-top: 15px">
              {{ index +1 }} 、{{ item.tg }} <span style="color: green">【{{ typeDel(item.type)}}】</span> </p>
            <div class="t-content">
              <template v-if="(item.type == '0') && item.xxList.length > 0">

                <el-radio-group v-model="item.da">
                  <el-radio v-for="t in item.xxList" :label="t.key" :key="t.key"  :rules="{ required: true, message: '选项不能为空', trigger: 'blur'}">
                    {{ t.key }} 、{{ t.value }}
                  </el-radio>
                </el-radio-group>

              </template>

              <template v-if="(item.type == '1') && item.xxList.length > 0">
                <el-checkbox-group v-model="dxs">
                  <el-checkbox v-for="t in item.xxList" :label="t.key" :key="t.key"  :rules="{ required: true, message: '选项不能为空', trigger: 'blur'}">{{ t.key }} 、{{ t.value }}</el-checkbox>
                </el-checkbox-group>
              </template>

              <template v-if="(item.type == '2' || item.type == '3')">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  placeholder="请输入内容"
                  v-model="item.da">
                </el-input>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getQuestionnaire } from "@/api/etp/questionnaire";
export default {
  name: "previewQuestionnaire",
  data(){
    return{
      questionForm:{},
      tags:[],
      dxs:[]
    }
  },
  created() {
    let id = this.$route.query.id;
    if (id !== undefined) {
      this.getQuestion(id);
    }
  },
  methods:{
    back(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path:"/questionnaire",
      });
    },
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
        if (this.questionForm.tags)
          this.tags = this.questionForm.tags.split(",")
        if(this.questionForm.nr){
          this.questionForm.nr = JSON.parse(this.questionForm.nr)
        }
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
  overflow-y: auto;
  height: calc(100vh - 125px);
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
