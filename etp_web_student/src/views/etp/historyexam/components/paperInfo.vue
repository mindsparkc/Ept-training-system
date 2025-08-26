<template>
  <div class="app-container">
    <div>
      <el-row :gutter="20">
        <el-col :span="5">
          <div class="app-container-left">
            <el-form>
                  <el-form-item label="试卷名称：" style="margin-bottom: 0px">
                    <span>{{ mc }}</span>
                  </el-form-item>
                  <el-form-item label="总分数：" style="margin-bottom: 0px">
                    <span>{{examInfo.totalScore}} 分</span>
                  </el-form-item>
                  <el-form-item label="最终得分：" style="margin-bottom: 0px">
                    <span>{{ examInfo.score }} 分</span>
                  </el-form-item>
<!--                  <el-form-item label="分数等级：" style="margin-bottom: 0px">-->
<!--                    {{examInfo.level}}-->
<!--                  </el-form-item>-->
            </el-form>
            <el-divider/>
            <div class="tags" ref="tags">
              <template v-for="(item,index) in allQuestion">
                <div style="width: 100%;margin-top: 5px" v-if="index==='单选'">
                  <template>
                    <el-row :span="24">
                      <el-col>
                        <span>单选</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type=" dxItem.isRight == true ? 'success' : 'danger'"  v-for="(dxItem,dxIndex,count) in item" :key="dxIndex" @click="goAnchor(dxItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{count+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%" v-if="index==='复选'">
                  <template>
                    <el-row>
                      <el-col>
                        <span>复选</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type=" fxItem.isRight == true ? 'success' : 'danger'" v-for="(fxItem,fxIndex,count) in item" :key="fxIndex" @click="goAnchor(fxItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{count+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%" v-if="index==='判断'">
                  <template>
                    <el-row>
                      <el-col>
                        <span>判断</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="pdItem.isRight == true ? 'success' : 'danger'" v-for="(pdItem,pdIndex,count) in item" :key="pdIndex" @click="goAnchor(pdItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{count+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%" v-if="index==='填空'">
                  <template>
                    <el-row>
                      <el-col>
                        <span>填空</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="tkItem.isRight == true ? 'success' : 'danger'" v-for="(tkItem,tkIndex,count) in item" :key="tkIndex" @click="goAnchor(tkItem.tkid)" style="margin-right: 5px;margin-top: 5px">{{count+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
                <div style="width: 100%" v-if="index==='主观'">
                  <template>
                    <el-row>
                      <el-col>
                        <span>主观</span>
                      </el-col>
                      <el-col>
                        <a>
                          <el-tag :type="zgItem.isRight == true ? 'success' : 'danger'" v-for="(zgItem,zgIndex,count) in item" :key="zgIndex" @click="goAnchor(zgItem.tkid)" style="margin-right: 5px;margin-bottom: 5px;margin-top: 5px">{{count+1}}</el-tag>
                        </a>
                      </el-col>
                    </el-row>
                  </template>
                </div>
              </template>
            </div>
            <br/>
            <el-row/>
            <el-divider/>
            <el-row>
              <el-col>
                <el-form>
                  <div style="width: 100%;height: 100%">
                    <div style="width: 50%;float: left;">
                      <div style="width: 20px;height: 20px;background-color: #13ce66;float: left;border-radius: 3px;margin-top: 7px;margin-left: 40%"></div>
                      <el-form-item label="正 确" style="margin-left: 60px"/>
                    </div>
                    <div style="width: 50%;float: right;">
                      <div style="width: 20px;height: 20px;float: left;background-color: #ff4949;border-radius: 3px;margin-top: 7px;margin-left: 30%"></div>
                      <el-form-item label="错 误" style="margin-left: 50px"/>
                    </div>
                  </div>
                </el-form>
              </el-col>
              <el-col>
<!--                <el-button type="success" style="margin-top: 30%">再考一次</el-button>-->
                <el-button type="primary" style="margin-top: 15%;margin-left: 70%" @click="back()">返回</el-button>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="19">
          <div class="app-container-right">
            <el-form label-position="top" ref="form" label-width="100px">
              <el-row>
                <el-card class="box-card" v-for="(item,index) in allQuestion" :key="index">
                  <div v-if="index === '单选' && item!=null">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>

                    <el-form-item  :key="dxIndex" :ref="'#task-'+ dxItem.tkid" v-for="(dxItem,dxIndex,count) of item">
                      <span v-html="(count+1) + '.（'+ + dxItem.score + '分）'+dxItem.tg"></span><br/>
                      <template v-for="xx in JSON.parse(dxItem.xx)"  v-if="dxItem.xx!=undefined">
                        <span style="margin-left: 30px">
                          {{xx.key+'.'+xx.value}}
                        </span><br/>
                      </template>
                      <template>
                        <span v-if="dxItem.isRight ==false" style="color: #f56c6c;font-weight: bold;margin-left: 30px;">
                          您的答案：{{ dxItem.answered == 'null'?'未作答':dxItem.answered}}
                        </span>
                        <span v-if="dxItem.isRight ==true" style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          您的答案：{{ dxItem.answered == 'null'?'未作答':dxItem.answered}}
                        </span>
                      </template><br/>
                      <template>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          正确答案：{{ dxItem.da }}
                        </span><br/>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          答案解析：{{ dxItem.jx ==undefined ? '无': dxItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '复选' && item!=null">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="fxIndex" :ref="'#task-'+ fxItem.tkid" v-for="(fxItem,fxIndex,count) of item">
                      <span v-html="(count+1) + '.（'+ + fxItem.score + '分）' + fxItem.tg"></span><br/>
                      <template v-for="xx in JSON.parse(fxItem.xx)">
                        <span style="margin-left: 30px">
                          {{xx.key+'.'+xx.value}}
                        </span><br/>
                      </template>
                      <template>
                        <span v-if="fxItem.isRight ==false && fxItem.answered!=undefined" style="color: #f56c6c;font-weight: bold;margin-left: 30px;">
                          您的答案：{{fxItem.answered}}
                        </span>
                        <span v-if="fxItem.isRight ==true && fxItem.answered!=undefined" style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          您的答案：{{fxItem.answered}}
                        </span>
                        <span v-if="fxItem.isRight ==false &&fxItem.answered==undefined" style="color: #f56c6c;font-weight: bold;margin-left: 30px;">
                          您的答案：未作答
                        </span>
                      </template><br/>
                      <template>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          正确答案：{{ fxItem.da }}
                        </span><br/>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          答案解析：{{ fxItem.jx ==undefined ? '无': fxItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '判断' && item!=null" >
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="pdIndex" :ref="'#task-'+ pdItem.tkid" v-for="(pdItem,pdIndex,count) of item">
                      <span v-html="(count+1) + '.（'+ + pdItem.score + '分）' + pdItem.tg"></span><br/>
                      <template v-if="pdItem.isRight ==true">
                        <span  style="color: #67c23a;font-weight: bold;margin-left: 30px;" v-if="pdItem.answered == 1">
                          您的答案：正确
                        </span>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;" v-else-if="pdItem.answered == 0">
                          您的答案：错误
                        </span>
                      </template>
                      <template v-if="pdItem.isRight ==false">
                        <span  style="color: #f56c6c;font-weight: bold;margin-left: 30px;" v-if="pdItem.answered == 1">
                          您的答案：正确
                        </span>
                        <span style="color: #f56c6c;font-weight: bold;margin-left: 30px;" v-else-if="pdItem.answered == 0">
                          您的答案：错误
                        </span>
                        <span style="color: #f56c6c;font-weight: bold;margin-left: 30px;" v-else>
                          您的答案：未作答
                        </span>
                      </template><br/>
                      <template>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          正确答案：{{ pdItem.da == 1?'正确':'错误' }}
                        </span><br/>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          答案解析：{{ pdItem.jx ==undefined ? '无': pdItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '填空'">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="tkIndex" :ref="'#task-'+ tkItem.tkid" v-for="(tkItem,tkIndex,count) of item">
                      <span v-html="(count+1) + '.（'+ + tkItem.score + '分）'+tkItem.tg"></span><br/>
                      <template>
                        <span v-if="tkItem.isRight ==false && tkItem.answered!=undefined" style="color: #f56c6c;font-weight: bold;margin-left: 30px;">
                          您的答案：<span v-for="(val,key) in JSON.parse(tkItem.answered)">
                              {{val}}
                          </span>
                        </span>
                        <span v-if="tkItem.isRight ==true && tkItem.answered!=undefined" style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          您的答案：<span v-for="(val,key) in JSON.parse(tkItem.answered)">
                            {{val}}
                          </span>
                        </span>
                        <span v-if="tkItem.answered==undefined" style="color: #f56c6c;font-weight: bold;margin-left: 30px;">
                          您的答案：未作答
                        </span>
                      </template><br/>
                      <template>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          正确答案：<span v-for="(val,key) in JSON.parse(tkItem.da)">
                            <span v-for="(v,k) in val">{{v}}</span>
                          </span>
                        </span><br/>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          答案解析：{{ tkItem.jx ==undefined ? '无': tkItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '主观'">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="zgIndex" :ref="'#task-'+ zgItem.tkid" v-for="(zgItem,zgIndex,count) of item">
                      <span v-html="(count+1) + '.（'+ + zgItem.score + '分）'+zgItem.tg"></span><br/>
                      <template>
                        <span v-if="zgItem.isRight ==false" style="color: #f56c6c;font-weight: bold;margin-left: 30px;">
                          您的答案：{{ zgItem.answered == 'null'?'未作答':zgItem.answered}}
                        </span>
                        <span v-if="zgItem.isRight ==true" style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          您的答案：{{ zgItem.answered == 'null'?'未作答':zgItem.answered}}
                        </span>
                      </template><br/>
                      <template>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          正确答案：{{ zgItem.da }}
                        </span><br/>
                        <span style="color: #67c23a;font-weight: bold;margin-left: 30px;">
                          答案解析：{{ zgItem.jx ==undefined ? '无': zgItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                </el-card>
              </el-row>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {
  getExaminationInfo,
  getHistoryTestPaper,
  getSubjectByPaper,
  resultInquiry,
  selectAnswerInfo,
  submitTheExam,
} from "@/api/etp/exam/exam";
import vueCountDown from "@liuwave/vue-count-down";
import { delConfig } from "@/api/system/config";

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
      //考试详情
      examInfo: {},
      //考试答题详情
      testPaperInfo: {},
      //所有试题
      allQuestion: {},
      daArr: [],
      jxArr: [],
      typeItem: [],
      uid: "",
      mc: "",
    };
  },
  created() {
    this.uid = this.$route.params.tid;
    if (this.uid !== undefined && this.uid !== "") {
      this.getInfo();
    }
  },
  methods: {
    async getInfo() {
      await getHistoryTestPaper(this.uid).then(response => {
        if (response.code == "200") {
          this.examInfo = response.data;
          console.log(this.examInfo);
        }
      }),
        await selectAnswerInfo(this.uid).then(response => {
          if (response.code == "200") {
            console.log(response);
            // debugger
            this.examInfo = response.data.da.detail;
            this.mc = response.data.examination.mc;
            let allQuestion = response.data.da.sj;

            let dx = {};
            let fx = {};
            let pd = {};
            let tk = {};
            let zg = {};

            for (let i in allQuestion) {
              if (allQuestion[i].lx == 1) {
                dx[allQuestion[i].id] = allQuestion[i];
              } else if (allQuestion[i].lx == 2) {
                fx[allQuestion[i].id] = allQuestion[i];
                // let fxda = '';
                // if(allQuestion[i].answered != undefined){
                //   let da = JSON.parse(allQuestion[i].answered)
                //   for (const key in da) {
                //     console.log(key)
                //     console.log(da[key])
                //     let nextKey = parseInt(key) + 1;
                //     if(da[nextKey]!=undefined){
                //       fxda += da[key]+','
                //     }else{
                //       fxda += da[key]
                //     }
                //   }
                // allQuestion[i].answered = fxda;
                // }
              } else if (allQuestion[i].lx == 3) {
                pd[allQuestion[i].id] = allQuestion[i];
              } else if (allQuestion[i].lx == 4) {
                tk[allQuestion[i].id] = allQuestion[i];
              } else if (allQuestion[i].lx == 5) {
                zg[allQuestion[i].id] = allQuestion[i];
              }
            }

            if (Object.getOwnPropertyNames(dx).length > 0) {
              this.allQuestion["单选"] = dx;
            }
            if (Object.getOwnPropertyNames(fx).length > 0) {
              this.allQuestion["复选"] = fx;
            }
            if (Object.getOwnPropertyNames(pd).length > 0) {
              this.allQuestion["判断"] = pd;
            }
            if (Object.getOwnPropertyNames(tk).length > 0) {
              this.allQuestion["填空"] = tk;
            }
            if (Object.getOwnPropertyNames(zg).length > 0) {
              this.allQuestion["主观"] = zg;
            }

            // this.allQuestion = allQuestion;
            // for(let key  in allQuestion){
            //   console.log(allQuestion[key].answerd);
            //   for(let k in allQuestion[key].answerd){
            //     console.log(allQuestion[key].answerd[k]);
            //   }
            // }
          }
        });
    },
    //返回上一个页面
    back() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path: "/studyManagement/historyExam",
        query: { t: Date.now() },
      });
    },

    goAnchor(id) {
      this.$refs["#task-" + id][0].$el.scrollIntoView({
        block: "start",
        inline: "nearest",
      });
    },
  },
  watch: {
    // singleAnswer: {
    //   handler (val) {
    //     Object.assign(this.answerCollection,val);
    //   },
    //   deep: true,
    //   immediate: true
    // },
    // multipleChoiceAnswer: {
    //   handler (val) {
    //     Object.assign(this.answerCollection,val);
    //   },
    //   deep: true,
    //   immediate: true
    // }
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
    height: 80%;
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
    }
  }
  ::v-deep p:first-child {
    display: inline;
  }
}
</style>
