<template>
<!--试卷预览-->
  <div class="app-container">
    <div class="textBox pre-pad">
      <div style="position: relative;left:15px;height: 40px">
        <div class="pre-desc"><span style="margin-left:10px;font-size: 14px">考试详情</span></div>
      </div>
      <el-row>
        <el-col :span="4" style="text-align: center;">
          <el-image style="width: 200px; height: 150px;border-radius:5px;margin-top: calc(50% - 70px)"
                    :src="examForm.fm"
                    fit="cover"/>
        </el-col>
        <el-col :span="16" :offset="1">
          <el-form ref="form" label-width="100px" size="small">
            <el-row>
              <el-col :span="24">
                <el-form-item label="考试标签：">
                  <el-tag size="medium" :key="index" v-for="(item,index) in tags" style="margin-left:5px">{{ item }}</el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="考试时间：">
                  <span>{{ examForm.kskssj }} -- {{ examForm.ksjssj }}</span>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="考试名称：">
                  <span>{{ examForm.mc }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="答卷时长：">
                  <span>{{ examForm.djsc }}分钟</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="答题次数：">
                  <span>{{ examForm.dtcs=='-99'?'无限':examForm.dtcs }}次</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="开放程度：">
                  <span>{{ examForm.kfcd }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="考试描述：">
                  <span>{{ examForm.ms }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
        <el-col :span="1" :offset="1">
          <el-button size="small" @click="back">返 回</el-button>
        </el-col>
      </el-row>


      <el-divider/>
      <div class="paper pre-pad">
        <div style="position: relative;left:15px;height: 40px">
          <div class="pre-desc"><span style="margin-left:10px;font-size: 14px">试卷详情</span></div>
        </div>
        <el-form ref="testPaperForm" label-width="100px" size="small">
          <el-row :gutter="20">
            <el-col :span="4" :offset="1">
              <el-form-item label="试卷名称：">
                <span>{{ testPaperForm.sjmc }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="1">
              <el-form-item label="试卷题型：">
                <el-badge style="margin-left: 15px"
                          v-for="item in testPaperForm.tx" :value="item.value" class="item" type="primary">
                  <el-button size="small" @click="scrollToP(item)">{{ item.key }}</el-button>
                </el-badge>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="试卷策略：">
                <span>{{ testPaperForm.cl }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="23" :offset="1">
              <el-form-item label="试卷标签：">
                <el-tag size="medium" :key="index" v-for="(item,index) in testPaperForm.tags" style="margin-left:5px">{{ item }}</el-tag>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="23" :offset="1">
              <el-form-item label="试卷总分：">
                {{ total }} 分
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="23" :offset="1">
              <el-form-item label="评判等级：">
                  <span v-for="item in examForm.fsdj">
                    【{{ item.value[0]}} ~ {{ item.value[1] }}】 {{ item.key}}
                  </span>
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
          <div class="el-empty__image" v-if="testPaperForm.zjcl == '2'">
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
              <div class="desc">随机生成考卷不支持查看试题</div>
            </div>
          <div v-if="testPaperForm.zjcl != '2'" class="subjectBox" style="margin-left: 20px;padding:0 30px 30px">
            <div v-if="dxArr.length > 0" id="dx" style="padding-top: 10px">
              <el-divider content-position="left">单选题（{{dxArr.length}}）</el-divider>
              <div v-for="(item,index) in dxArr" :key="item.id" style="margin-left: 10px">
                <template v-if="item.xx">
                  <span>{{ index + 1 }}、{{ item.tg }}</span>
                  <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                    <span v-for="xx in JSON.parse(item.xx)" style="margin-left: 25px">
                    {{ xx.key }}、{{ xx.value }} <br/>
                  </span>
                    <span style="color: green;font-weight: bold;margin-left: 30px;" >
                    正确答案：{{ item.da }}
                  </span>
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
                    <span style="margin-left: 25px;color: green;font-weight: bold" >
                    正确答案：{{ item.da }}
                  </span>
                </template>
              </div>
            </div>
            <div v-if="pdArr.length > 0" id="pd"  style="padding-top: 10px">
              <el-divider content-position="left">判断题（{{pdArr.length}}）</el-divider>
              <div v-for="(item,index) in pdArr" :key="item.id" style="margin-left: 10px">
                <span>{{ index + 1 }}、{{ item.tg }}</span>
                <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：{{ item.da=='1'?'对':'错' }}
                </span>
              </div>
            </div>
            <div v-if="tkArr.length > 0" id="tk"  style="padding-top: 10px">
              <el-divider content-position="left">填空题（{{tkArr.length}}）</el-divider>
              <div v-for="(item,index) in tkArr" :key="item.id" style="margin-left: 10px">
                <span>{{ index + 1 }}、{{ item.tg }}</span>
                <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：{{ item.da }}
                </span>
              </div>
            </div>
            <div v-if="zgArr.length > 0" id="zg"  style="padding-top: 10px">
              <el-divider content-position="left">问答题（{{zgArr.length}}）</el-divider>
              <div v-for="(item,index) in zgArr" :key="item.id" style="margin-left: 10px">
                <span>{{ index + 1 }}、{{ item.tg }}</span>
                <span style="color: red;font-weight: bold">（ {{ item.score}} 分 ）</span> <br/>
                <span style="margin-left: 25px;color: green;font-weight: bold" >
                  正确答案：答案言之有理即可
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import {getTestpaper} from "@/api/etp/testpaper/testpaper";
import {getExamination} from "@/api/etp/examination/examination";
import {getSubjectByPaper} from "@/api/etp/testpaper/testpapersubject"
import Testpaper from "@/views/etp/testpaper";
export default {
  name: "previewExamation",
  components: {Testpaper},
  data(){
    return{
      examForm:{},
      testPaperForm:{},
      tags:[],
      subjects:null,

      dxArr:[],
      fxArr:[],
      pdArr:[],
      tkArr:[],
      zgArr:[]
    }
  },
  computed:{
    total(){
      let total = 0;
      if (this.dxArr.length > 0)
        this.dxArr.forEach(item => {if (item.score) total += item.score;})
      if (this.fxArr.length > 0)
        this.fxArr.forEach(item => {if (item.score) total += item.score;})
      if (this.pdArr.length > 0)
        this.pdArr.forEach(item => {if (item.score) total += item.score;})
      if (this.tkArr.length > 0)
        this.tkArr.forEach(item => {if (item.score) total += item.score;})
      if (this.zgArr.length > 0)
        this.zgArr.forEach(item => {if (item.score) total += item.score;})
      return total;
    }
  },
  mounted() {
    let id = this.$route.params.id;
    if (id !== undefined && id !== '') {
      this.getExamination(id)
    }
  },
  methods:{
    async getExamination(id){
      //获取考试信息回显
      await getExamination(id).then( response =>{
        if (response.code == '200'){
          this.examForm = response.data;
          if (this.examForm.tags)
            this.tags = this.examForm.tags.split(",")
          if (this.examForm.fsdj)
            this.examForm.fsdj = JSON.parse(this.examForm.fsdj)
          let kfcd = this.examForm.kfcd;
          if (kfcd == '0') this.examForm.kfcd = "完全公开";
          if (kfcd == '1') this.examForm.kfcd = "限定用户";
          if (kfcd == '2') this.examForm.kfcd = "不公开";
        }else {
          console.log("getExamination")
        }
      })

      // 获取试卷信息
      let txMap = new Map();
      txMap.set('1','单选题')
      txMap.set('2','复选题')
      txMap.set('3','判断题')
      txMap.set('4','填空题')
      txMap.set('5','问答题')
      await getTestpaper(this.examForm.sjid).then(res =>{
        if (res.code == '200'){
          this.testPaperForm = res.data;
          // 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷
          let zjcl = this.testPaperForm.zjcl;
          if (zjcl == '0') this.testPaperForm.cl = "自选题组卷";
          if (zjcl == '1') this.testPaperForm.cl = "随机选题组卷";
          if (zjcl == '2') this.testPaperForm.cl = "随机生成考卷";
          if (this.testPaperForm.tx){
            this.testPaperForm.tx = JSON.parse(this.testPaperForm.tx);
            this.testPaperForm.tx.map(item =>{ item.key = txMap.get(item.key)})
          }
          if(this.testPaperForm.tags)
            this.testPaperForm.tags = this.testPaperForm.tags.split(",")
        }
      })
      await getSubjectByPaper(this.testPaperForm.id).then(res => {
        if (res.code == '200'){
          this.subjects = res.data
          for (let key in this.subjects){
            switch (key + '') {
              case '1':
                this.dxArr = this.subjects[key + ''];
                break;
              case '2':
                this.fxArr = this.subjects[key + ''];
                break;
              case '3':
                this.pdArr = this.subjects[key + ''];
                break;
              case '4':
                this.tkArr = this.subjects[key + ''];
                break;
              case '5':
                this.zgArr = this.subjects[key + ''];
                break;
              default:break;
            }
          }
        }
      })
    },
    /** 返回*/
    back() {
      if (this.$route.query.where && this.$route.query.where == 'index' ){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/index", query: { t: Date.now() }});
        return;
      }
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: `/etp/task/addOrUpdateTask/${this.$route.query.id}`, query: {t: Date.now()}})
    },
    scrollToP(item){

      if (parseInt(item.value) > 0){
        let tagPoint = "";
        if (item.key == "单选题") tagPoint = "#dx"
        if (item.key == "复选题") tagPoint = "#fx"
        if (item.key == "判断题") tagPoint = "#pd"
        if (item.key == "填空题") tagPoint = "#tk"
        if (item.key == "问答题") tagPoint = "#zg"
        this.$el.querySelector(`${tagPoint}`).scrollIntoView({
          behavior: "smooth",  // 平滑过渡
          block:    "start"  // 上边框与视窗顶部平齐。默认值
        });
      }

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
