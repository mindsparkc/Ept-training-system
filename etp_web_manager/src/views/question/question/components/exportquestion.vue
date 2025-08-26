<template>
  <div class="app-container app-container-custome">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-steps :active="active" simple finish-status="success" style="width: 100%">
          <el-step>
            <slot slot="title">第一步：题目上传</slot>
          </el-step>
          <el-step>
            <slot slot="title">第二步：题目检查</slot>
          </el-step>
          <el-step>
            <slot slot="title">第三步：题目导入</slot>
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
    <br/>

    <!-- 课程基本信息    -->
    <transition name="el-fade-in-linear">
      <div v-if="active === 0" style="margin-top: 10%">
        <el-row :gutter="24" style="margin: 0 auto;text-align: center;">
          <el-col>
            <el-button
              size="big"
              icon="el-icon-upload"
              type="primary"
              @click="handleImport"
              v-hasPermi="['question:import:import']">点击上传题目文件</el-button>
          </el-col>
          <el-col>
            <span style="color:red;font-size: 2px">*提示：仅允许导入“xls”或“xlsx”格式文件！</span>
          </el-col>
            <el-col style="margin-top: 2%;margin-bottom: 5%">
              <el-card style="width: 30%;margin-left: 35%;margin-top: 5%;height:150px">
                <el-col>
                  <img src="@/assets/etp/execl-2.png"/>
                </el-col>
                <el-col style="margin-top: 20px">
                  <el-button size="small" v-hasPermi="['question:import:download']" @click="downloadFile" round>下载Execl文档模板</el-button>
                </el-col>
              </el-card>
            </el-col>
        </el-row>
      </div>
    </transition>

    <!-- 检查题目  -->
    <transition name="el-fade-in-linear">
      <div v-if="active == 1">
        <el-row :gutter="20">
          <el-col :span="12">
            <span style="text-align: center;font-weight: bold">输入区</span>
          </el-col>
          <el-col :span="12">
            <span style="text-align: center;font-weight: bold">检查区</span>
          </el-col>
          <el-divider></el-divider>
        </el-row>
        <div>
          <el-row :gutter="20" v-for="(item,index) in questionList">
            <el-col :span="12">
              <el-col style="margin-top: 10px">
                <span>
                  {{JSON.parse(item.remark)['sn']}}.{{'['+JSON.parse(item.remark)['type']+']'}} {{JSON.parse(item.remark)['content']}}
                </span>
              </el-col>
              <el-col v-for="(data,key) of JSON.parse(item.remark)" style="margin-bottom: 2px">
                <span v-if="key.includes('option') && data!=''">{{key.substring(6,7)}} . {{data}}</span>
              </el-col>
              <el-col style="margin-bottom: 10px">
                <span>{{'[答案]'+JSON.parse(item.remark)['rightAnswer']}}</span>
              </el-col>
              <el-col>
                <span>{{'[解析]'+JSON.parse(item.remark)['analysis']}}</span>
              </el-col>
              <el-col style="margin-top: 10px">
                [导入]：
                <el-switch
                  v-model="item.delFlag"
                  active-text="是"
                  inactive-text="否"
                :active-value="'0'"
                :inactive-value="'2'">
                </el-switch>
              </el-col>
              <el-col style="margin-top: 10px" v-if="JSON.parse(item.remark)['errorMsg']!=null">
                <span style="color: red">
                  {{'解析错误:'+JSON.parse(item.remark)['errorMsg']}}
                </span>
              </el-col>
            </el-col>
            <el-col :span="12">
              <el-card>
                <qusetion-view
                  :data="item"
                  :bqShow="false"
                  :is-show-answer="isShowAnswer"
                  :each-score="item.eachScore"
                  :xh="index+1">
                </qusetion-view>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </transition>

    <!-- 导入情况  -->
    <transition name="el-fade-in-linear">
      <div v-if="active == 2">
        <el-card>
          <el-row>
            <el-col>
                <el-col :span="10" :offset="10">
                  <img src="@/assets/images/importSuccess.png" width="200" height="200">
                </el-col>
                <el-col :span="10" :offset="11" style="font-weight: bolder;color: #13ce66">
                  <span>题目导入成功</span>
                </el-col>
              <el-col :offset="5" style="margin-top: 3%;margin-bottom: 2%">
                <el-col :span="3" v-for="(item,key) in questionType">
                  <div class="div-head-1"> {{ key }}</div>
                  <div class="div-head"> {{ item }}题</div>
                </el-col>
              </el-col>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </transition>


    <div class="btns">
<!--      <el-button @click="getImport" v-if="active===1">获取题目</el-button>-->

<!--      <el-button @click="next" v-if="active === 0">下一步</el-button>-->
<!--      <el-button type="warning" v-hasPermi="['question:import:submit']" @click="deleteImport" v-if="active === 1">取 消</el-button>-->
      <el-button type="danger" v-hasPermi="['question:import:submit']" @click="qx" v-if="active === 1">取消</el-button>
      <el-button v-hasPermi="['question:import:submit']" type="primary" @click="submit" v-if="active === 1">导 入</el-button>
    </div>


    <el-dialog :title="title" :visible.sync="open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xls,.xlsx"
        :headers="headers"
        :action="baseUrl"
        :disabled="isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件模板拖到此处，或
          <em>点击上传</em>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import qusetionView from "@/views/components/question/questionView"
import { getToken } from '@/utils/auth'
import { getImportQuestion,delImport,submitImport,giveupImport } from "@/api/question/question"

export default {
  name: "exportquestion",
  components: {
    qusetionView,
    getImportQuestion,
    delImport,
    submitImport,
    giveupImport
  },

  data() {
    return {
      active:0,
      form: {},
      // 表单校验
      rules: {},
      //文件上传参数
      baseUrl: process.env.VUE_APP_BASE_API + "/admin/v1/question/importData",
      isUploading:false,
      title:'',
      open:false,
      headers: { Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'  },
      //后台返回的题目列表
      questionList:[],
      letterArr:[],
      isShowAnswer:true,
      typeOptions:[],
      recordId:null,
      questionType:{'单选题':1,'复选题':2,'判断题':3,'填空题':4,'问答题':5}
    }
  },

  created() {
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
  },
  methods: {

    //处理提交结束的回显页面参数
    getType(){
      let dx = 0;
      let fx = 0;
      let pd = 0;
      let tk = 0;
      let wd = 0;
      this.questionList.forEach((item,index) =>{
        if(item.delFlag=='0' && item.type=='1'){
          dx++;
        }else if(item.delFlag=='0' && item.type=='2'){
          fx++;
        }else if(item.delFlag=='0' && item.type=='3'){
          pd++;
        }else if(item.delFlag=='0' && item.type=='4'){
          tk++;
        }else if(item.delFlag=='0' && item.type=='5'){
          wd++;
        }
      })
      this.questionType['单选题'] = dx;
      this.questionType['复选题'] = fx;
      this.questionType['判断题'] = pd;
      this.questionType['填空题'] = tk;
      this.questionType['问答题'] = wd;
    },

    deleteImport(){
      let ids = [];
      this.questionList.forEach((item,key)=>{
        debugger
        if(item.delFlag=='2'){
          ids.push(item.quId)
        }
      })
      if(ids.length>0){
        delImport(ids).then(response => {
          console.log(response)
        }).catch(()=>{
          this.msgError("导入失败，请联系管理员！")
        })
      }
    },


    //忽略导入按钮
    // updateFlag(item){
    //   if(!item.importFlag){
    //     item.delFlag = '2'
    //     console.log(this.questionList)
    //   }else{
    //     item.delFlag = '0'
    //     console.log(this.questionList)
    //   }
    // },

    //测试获取数据
    // getImport(){
    //   getImportQuestion('1516474780876834').then(res => {
    //     debugger
    //     if(res.code == 200){
    //       this.questionList = res.data
    //       console.log(res);
    //     }
    //   })
    // },
    //将下面两个方法同步依次执行
    async submit(){
      await this.deleteImport();
      await this.submitImport();
    },

    //提交方法
    submitImport(){
      this.$confirm('是否提交导入题目','警告',{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(()=>{
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        submitImport(this.recordId).then(res =>{
          if(res.code == 200){
          console.log(res);
          this.getType();
          loading.close();
          ++this.active;
          }
        })
      }).catch(()=>{})

    },

    //上一步
    pre(){
      --this.active;
      this.questionList = [];
    },

    //下一步
    next(){
      ++this.active;
    },

    //取消导入
    qx(){
      this.$confirm('是否放弃本次导入？','警告',{
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
      }).then(() => {
        giveupImport(this.recordId).then(res=>{
          this.msgSuccess(res.msg);
          this.pre();
        })
      }).catch(() => {})
    },

    //试题导入方法
    handleImport(){
      this.title = "题目导入";
      this.open = true;
    },

    //文件提交方法
    submitFileForm() {
      this.$refs.upload.submit();
    },

    //模板上传加载状态
    handleFileUploadProgress(event, file, fileList) {
      this.isUploading = true;
    },

    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.isUploading = false;
      debugger
      this.$refs.upload.clearFiles();
      if(response.code == 200){
        // this.$alert('上传成功');
        this.msgSuccess("上传成功！")
        this.open = false
        this.recordId = response.data.recordId;
        getImportQuestion(this.recordId).then(res=>{
          if(res.code == 200){
            this.questionList = res.data
            console.log(res);
          }
        })
        ++this.active;
      }else{
        this.infoTitle = '上传失败'
      }
    },


    typeFormat(lx, column) {
      return this.selectDictLabel(this.typeOptions, lx);
    },

    downloadFile(){
      window.location.href = process.env.VUE_APP_BASE_API + "/admin/v1/question/importData/template";
    }

  }
}
</script>

<style scoped lang="scss">

::v-deep div.el-steps.el-steps--simple{
  padding:2px 1%;
}

.tm-title{
  display: inline-block;
  margin-left: 0px;
  margin-bottom: 20px;
  ::v-deep p:nth-of-type(1){
    display: inline;
  }
}

.div-head{
  border: 1px solid #dddddd;
  text-align: center;
}

.div-head-1{
  border: 1px solid #dddddd;
  text-align: center;
  background-color: #bebfc3;
}


/*-------------------------------*/
.app-container-custome {
  margin: 20px 5%;
  border-radius: 8px;
  border: 1px solid rgba(238, 227, 232, 0.15);
  background: rgba(249, 249, 249, 0.3);
}

.btns {
  margin-top: 15px;
  margin-left: 80%;
}

::v-deep .el-steps--simple {
  padding: 15px 20%;
}

.qrcg-div {
  height: 100%;

}
::v-deep .el-dialog__body {
  padding: 0px 20px;
}
</style>
