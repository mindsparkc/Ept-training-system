<template>
  <!--试卷预览-->
  <div class="app-container">
    <div class="textBox pre-pad">
      <div style="position: relative;left:15px;height: 40px">
        <div class="pre-desc"><span style="margin-left:10px;font-size: 14px">历史考试详情</span>
<!--          <el-button size="mini" type="success" @click="" style="margin-left: 75%">再考一次</el-button>-->
          <el-button size="mini" type="primary" @click="back()" style="margin-left: 80%">返回</el-button>
        </div>
      </div>
        <el-form ref="testPaperForm" label-width="100px" size="small">
          <el-row :gutter="0">
            <el-col :offset="1">
              <el-form-item label="试卷名称：">
                <span>{{ mc }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="5" :offset="1">
              <el-form-item label="总分数：">
                <span>{{examForm.totalScore}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="最终得分：">
                <span>{{ examForm.score }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="3" :offset="1">
              <el-form-item label="分数等级：">
                {{examForm.level}}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-divider/>
        <div class="paper pre-pad">
          <div style="position: relative;left:15px;height: 40px">
            <div class="pre-desc">
              <span style="margin-left:10px;font-size: 14px">试题详情</span>
            </div>
          </div>
          <div class="subjectBox" style="margin-left: 20px;padding:0 30px 30px">
            <div v-if="dxArr.length > 0" id="dx" style="padding-top: 10px">
              <el-divider content-position="left">单选题（{{dxArr.length}}）</el-divider>
              <div v-for="(item,index) in dxArr" :key="item.id" style="margin-left: 10px">
                <template v-if="item.xx">
                  <span>{{ index + 1 }}、{{ item.tg }}</span>
                  <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                  <span v-for="xx in JSON.parse(item.xx)" style="margin-left: 25px">
                    {{ xx.key }}、{{ xx.value }} <br/>
                  </span>
                  <template v-for="(da,index) in daArr">
                    <span v-if="index == item.tkid" style="color: red;font-weight: bold;margin-left: 30px;">
                      您的答案：{{ da }}
                    </span>
                  </template><br/>
                  <template  v-for="(val,index) in jxArr" v-if="val.tkid == item.tkid">
                    <span style="color: green;font-weight: bold;margin-left: 30px;">
                      正确答案：{{ val.da }}
                    </span><br/>
                    <span style="color: green;font-weight: bold;margin-left: 30px;">
                      答案解析：{{ val.jx ==undefined ? '无': item.jx}}
                    </span>
                  </template>
                </template>
              </div>
            </div>
            <div v-if="fxArr.length > 0" id="fx"  style="padding-top: 10px">
              <el-divider content-position="left">复选题（{{fxArr.length}}）</el-divider>
              <div v-for="(item,index) in fxArr" :key="item.id" style="margin-left: 10px">
                <template v-if="item.xx">
                  <span>{{ index + 1 }}、{{ item.tg }}</span>
                  <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                  <span v-for="xx in JSON.parse(item.xx)" style="margin-left: 25px">
                    {{ xx.key }}、{{ xx.value }} <br/>
                  </span>
                </template>
                <template v-for="(da,index) in daArr">
                    <span v-if="index == item.tkid" style="color: red;font-weight: bold;margin-left: 30px;">
                      您的答案：{{da}}
                    </span>
                </template><br/>
                <template  v-for="(val,index) in jxArr" v-if="val.tkid == item.tkid">
                    <span style="color: green;font-weight: bold;margin-left: 30px;">
                      正确答案：{{ val.da }}
                    </span><br/>
                  <span style="color: green;font-weight: bold;margin-left: 30px;">
                      答案解析：{{ val.jx ==undefined ? '无': item.jx}}
                    </span>
                </template>
              </div>
            </div>
            <div v-if="pdArr.length > 0" id="pd"  style="padding-top: 10px">
              <el-divider content-position="left">判断题（{{pdArr.length}}）</el-divider>
              <div v-for="(item,index) in pdArr" :key="item.id" style="margin-left: 10px">
                <span>{{ index + 1 }}、{{ item.tg }}</span>
                <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                <template v-for="(da,index) in daArr">
                    <span v-if="index == item.tkid" style="color: red;font-weight: bold;margin-left: 30px;">
                      您的答案：{{da=='1'?'正确':'错误'}}
                    </span>
                </template><br/>
                <template  v-for="(val,index) in jxArr" v-if="val.tkid == item.tkid">
                    <span style="color: green;font-weight: bold;margin-left: 30px;">
                      正确答案：{{ val.da=='1' ? '正确':'错误' }}
                    </span><br/>
                  <span style="color: green;font-weight: bold;margin-left: 30px;">
                      答案解析：{{ val.jx ==undefined ? '无': item.jx}}
                    </span>
                </template>
              </div>
            </div>
            <div v-if="tkArr.length > 0" id="tk"  style="padding-top: 10px">
              <el-divider content-position="left">填空题（{{tkArr.length}}）</el-divider>
              <div v-for="(item,index) in tkArr" :key="item.id" style="margin-left: 10px">
                <span>{{ index + 1 }}、{{ item.tg }}</span>
                <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                <template v-for="(da,index) in daArr">
                    <span v-if="index == item.tkid" style="color: red;font-weight: bold;margin-left: 30px;">
                      您的答案：{{da}}
                    </span>
                </template><br>
                <template  v-for="(val,index) in jxArr" v-if="val.tkid == item.tkid">
                    <span style="color: green;font-weight: bold;margin-left: 30px;">
                      正确答案：{{ val.da }}
                    </span><br>
                  <span style="color: green;font-weight: bold;margin-left: 30px;">
                      答案解析：{{ val.jx ==undefined ? '无': item.jx}}
                    </span>
                </template>
              </div>
            </div>
            <div v-if="zgArr.length > 0" id="zg"  style="padding-top: 10px">
              <el-divider content-position="left">主观题（{{zgArr.length}}）</el-divider>
              <div v-for="(item,index) in zgArr" :key="item.id" style="margin-left: 10px">
                <span>{{ index + 1 }}、{{ item.tg }}</span>
                <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                <template v-for="(da,index) in daArr">
                    <span v-if="index == item.tkid" style="color: red;font-weight: bold;margin-left: 30px;">
                      您的答案：{{da}}
                    </span>
                </template><br>
                <template  v-for="(val,index) in jxArr" v-if="val.tkid == item.tkid">
                    <span style="color: green;font-weight: bold;margin-left: 30px;">
                      正确答案：{{ val.da }}
                    </span><br>
                  <span style="color: green;font-weight: bold;margin-left: 30px;">
                      答案解析：{{ val.jx ==undefined ? '无': item.jx}}
                    </span>
                </template>
              </div>
            </div>
          </div>
        </div>
      </div>
  </div>

</template>

<script>
import {getHistoryTestPaper,selectAnswerInfo} from "@/api/etp/exam/exam";

export default {
  name: "showTestPaper",
  data() {
    return {
      examForm: {},
      testPaperForm: {},
      tags: [],
      subjects: null,

      dxArr: [],
      fxArr: [],
      pdArr: [],
      tkArr: [],
      zgArr: [],
      daArr: [],
      jxArr: [],
      uid: '',
      mc: '',

    }
  },

  created() {
    // console.log(this.$route.params.tid);
    this.uid = this.$route.params.tid;
    // console.log(this.uid);
    if (this.uid !== undefined && this.uid !== '') {
      this.getInfo();
    }
  },
  computed: {

  },
  mounted() {

  },

  methods:{
    async getInfo(){
      await getHistoryTestPaper(this.uid).then(response => {
        if (response.code == '200') {
          this.examForm = response.data
          console.log(response.data)
        }
      }),
        await selectAnswerInfo(this.uid).then(response => {
        if (response.code == '200') {
          this.mc = response.data.examination.mc;
          this.testPaperForm = response.data
          console.log(this.testPaperForm)
          this.testPaperForm.sj.forEach((item,index) => {
            if (item.lxbq == '单选') {this.dxArr[this.dxArr.length++] = item;}
            if (item.lxbq == '复选') {this.fxArr[this.fxArr.length++] = item;}
            if (item.lxbq == '判断') {this.pdArr[this.pdArr.length++] = item;}
            if (item.lxbq == '填空') {this.tkArr[this.tkArr.length++] = item;}
            if (item.lxbq == '主观') {this.zgArr[this.zgArr.length++] = item;}
          })

          // console.log(this.testPaperForm)
          this.daArr = response.data.da;
          // console.log(this.daArr);
          this.jxArr = response.data.jx;
          // console.log(this.jxArr);
          // console.log(this.daArr[6])
        }
      })
    },
    //返回上一个页面
    back() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/studyManagement/historyExam", query: {t: Date.now()}})
    },

    //进入考试页面
    toExam(){
      this.$confirm("是否重新考试","警告",{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"warning"
      })
      this.$store.dispatch("tagsView/delView",this.$route);

    }
  }

}
</script>

<style scoped>
.paper{
  height: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  border-radius: 5px;
  margin-top: 15px;
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
.el-empty__image {
  width: 260px;
  margin: 0 auto;
  text-align: center;
}
.desc {
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 2px;
}
.subjectBox span{
  line-height: 40px;
  font-size: 15px;
}

.el-divider__text.is-left{
  font-size: 18px;
}
</style>
