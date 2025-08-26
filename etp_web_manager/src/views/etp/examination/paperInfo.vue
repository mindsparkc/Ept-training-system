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
                <span>{{examInfo.totalScore}}</span>
              </el-form-item>
              <el-form-item label="最终得分：" style="margin-bottom: 0px">
                <span>{{ examInfo.score }}</span>
              </el-form-item>
              <!--                  <el-form-item label="分数等级：" style="margin-bottom: 0px">-->
              <!--                    {{examInfo.level}}-->
              <!--                  </el-form-item>-->
              <el-form-item v-if="this.pjzt == '1'" label="判卷状态：" style="margin-bottom: 0px">
                <span>已判卷</span>
              </el-form-item>
              <el-form-item v-if="this.pjzt=='1'" label="判卷老师：" style="margin-bottom: 0px">
                <span>{{this.pjTeacher}}</span>
              </el-form-item>
              <el-form-item v-if="this.pjzt=='1'" label="判卷时间：" style="margin-bottom: 0px">
                <span>{{this.pjTime}}</span>
              </el-form-item>
              <el-form-item  label="显示答案：" style="margin-bottom: 0px">
                <el-radio-group v-model="daFlag">
                  <el-radio label="0">否</el-radio>
                  <el-radio label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="显示解析：" style="margin-bottom: 0px">
                <el-radio-group v-model="jxFlag">
                  <el-radio label="0">否</el-radio>
                  <el-radio label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
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
                  <div class="div_tb">
                    <div class="div_tb_left">
                      <div class="div_tb_true"></div>
                      <el-form-item label="正 确"/>
                    </div>
                    <div class="div_tb_right">
                      <div class="div_tb_false"></div>
                      <el-form-item label="错 误"/>
                    </div>
                  </div>
                </el-form>
              </el-col>
              <el-col>
                <el-button type="success" style="margin-top: 20px;margin-left: 40px;" @click="submitTestPaper()">提交判卷</el-button>
                <el-button type="primary" style="margin-left: 30px" @click="back()">返回</el-button>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="19">
          <div class="app-container-right">
            <el-form label-position="top" ref="form" label-width="100px">
              <el-row>
                <el-card class="box-card" v-for="(item,index) of allQuestion" :key="index">
                  <div v-if="index === '单选' && item!=null">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="dxIndex" :ref="'#task-'+ dxItem.tkid" v-for="(dxItem,dxIndex,count) of item">
                      <span class="span_tg" v-html="(count+1) + '.（'+ + dxItem.score + '分）' + dxItem.tg"></span>
                      <template v-for="xx in JSON.parse(dxItem.xx)" v-if="dxItem.xx!=undefined">
                        <span style="margin-left: 30px">
                          {{xx.key+'.'+xx.value}}
                        </span><br/>
                      </template>
                      <template>
                        <span v-if="dxItem.isRight==false" class="span_da_false">
                          学员答案：{{ dxItem.answered == 'null'||dxItem.answered == undefined?'未作答':dxItem.answered}}
                        </span>
                        <span v-if="dxItem.isRight==true" class="span_da_true">
                          学员答案：{{ dxItem.answered == 'null'?'未作答':dxItem.answered}}
                        </span>
                      </template><br/>

                      <template>
                        <span class="span_da_true" v-if="daFlag=='1'">
                          正确答案：{{ dxItem.da }}
                        </span><br/>
                        <span class="span_da_true" v-if="jxFlag=='1'">
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
                      <span class="span_tg" v-html="(count+1) + '.（'+ + fxItem.score + '分）' + fxItem.tg"></span>
                      <template v-if="fxItem.xx!=undefined">
                        <el-col v-for="xx in JSON.parse(fxItem.xx)">
                          <span style="margin-left: 30px">
                            {{xx.key+'.'+xx.value}}
                          </span><br/>
                        </el-col>
                      </template>
                      <template>
                        <span v-if="fxItem.isRight==false" class="span_da_false">
                          学员答案：{{ fxItem.answered == 'null'||fxItem.answered == '[]'?'未作答':fxItem.answered}}
                        </span>
                        <span v-else class="span_da_true">
                          学员答案：{{ fxItem.answered == 'null'||fxItem.answered == '[]'?'未作答':fxItem.answered}}
                        </span>
                      </template><br/>

                      <template>
                        <span class="span_da_true" v-if="daFlag=='1'">
                          正确答案：{{ fxItem.da }}
                        </span><br/>
                        <span class="span_da_true" v-if="jxFlag=='1'">
                          答案解析：{{ fxItem.jx ==undefined ? '无': fxItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '判断' && item!=null">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="pdIndex" :ref="'#task-'+ pdItem.tkid" v-for="(pdItem,pdIndex,count) of item">
                      <span class="span_tg" v-html="(count+1) + '.（'+ + pdItem.score + '分）' + pdItem.tg"></span>
                      <template v-if="pdItem.isRight==false">
                        <span class="span_da_false" v-if="pdItem.answered == 1">
                          学员答案：正确
                        </span>
                        <span  class="span_da_false" v-else-if="pdItem.answered == 0">
                          您的答案：错误
                        </span>
                        <span  class="span_da_false" v-else>
                          填写答案：未作答
                        </span>
                      </template>
                      <template v-if="pdItem.isRight==true">
                        <span class="span_da_true" v-if="pdItem.answered == 1">
                          学员答案：正确
                        </span>
                        <span class="span_da_true" v-else-if="pdItem.answered == 0">
                          您的答案：错误
                        </span>
                        <span class="span_da_true" v-else>
                          填写答案：未作答
                        </span>
                      </template><br/>

                      <template>
                        <span class="span_da_true" v-if="daFlag=='1'">
                          正确答案：{{ pdItem.da == 1?'正确':'错误' }}
                        </span><br/>
                        <span class="span_da_true" v-if="jxFlag=='1'">
                          答案解析：{{ pdItem.jx ==undefined ? '无': pdItem.jx}}
                        </span>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '填空' && item!=null">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="tkIndex" :ref="'#task-'+ tkItem.tkid" v-for="(tkItem,tkIndex,count) of item">
                      <span class="span_tg" v-html="(count+1) + '.（'+ + tkItem.score + '分）' + tkItem.tg"></span>
                      <template>
                        <span v-if="tkItem.answered!=null && tkItem.isRight == true" class="span_da_true">
                          学员答案：<span v-for="(val,key) in JSON.parse(tkItem.answered)">
                            <span v-for="(v,k) in val">{{v}}</span><a>,</a>
                          </span>
                        </span>
                        <span v-if="tkItem.answered!=null && tkItem.isRight == false"  class="span_da_false">
                          学员答案：<span v-for="(val,key) in JSON.parse(tkItem.answered)">
                            <span v-for="(v,k) in val">{{v}}</span><a>,</a>
                          </span>
                        </span>
                        <span v-if="tkItem.answered==null"  class="span_da_false">
                          学员答案：未作答
                        </span>
                      </template><br/>
                      <template>
                        <template>
                          <span class="span_da_true" v-if="daFlag=='1'">
                           正确答案：<span v-for="(val,key) in JSON.parse(tkItem.da)">
                            <span v-for="(v,k) in val">{{v}}</span><a>,</a>
                          </span>
                         </span><br/>
                        </template>
                        <span class="span_da_true" v-if="jxFlag=='1'">
                          答案解析：{{ tkItem.jx ==undefined ? '无': tkItem.jx}}
                        </span><br/>
                        <template v-if="pjzt=='1'">
                          <span class="span_pj">
                          评分：<el-input size="mini" v-model="tkItem.logicalScore" placeholder="请填写评分"
                                       style="width: 100px" @input="checkVal(tkItem.logicalScore,tkItem.score,tkIndex)"/> 分
                        </span><br/>
                          <span class="span_pj">
                          评语：<el-input type="textarea" :rows="1" size="small"
                                       v-model="tkItem.py"
                                       placeholder="请填写评语" style="width: 500px;"/>
                        </span>
                        </template>
                        <template v-else>
                          <span class="span_pj">
                          评分：<el-input size="mini" v-model="tkPfArr[tkIndex]" placeholder="请填写评分"
                                       style="width: 100px" @input="checkVal(tkPfArr[tkIndex],tkItem.score,tkIndex)"/> 分
                        </span><br/>
                          <span class="span_pj">
                          评语：<el-input type="textarea" :rows="1" size="small"
                                       v-model="pyArr[tkIndex]"
                                       placeholder="请填写评语" style="width: 500px;"/>
                        </span>
                        </template>
                      </template>
                    </el-form-item>
                  </div>
                  <div v-if="index === '主观' && item!=undefined">
                    <div>
                      <span style="font-size: 23px">{{index}}</span>
                      <el-divider/>
                    </div>
                    <el-form-item  :key="zgIndex" :ref="'#task-'+ zgItem.tkid" v-for="(zgItem,zgIndex,count) of item">
                      <span class="span_tg" v-html="(count+1) + '.（'+ + zgItem.score + '分）' + zgItem.tg"></span>
                      <template>
                        <span v-if="zgItem.isRight==true" class="span_da_true">
                          学员答案：{{ zgItem.answered =='null'||zgItem.answered == undefined?'未作答':zgItem.answered}}
                        </span>
                        <span v-if="zgItem.isRight==false"  class="span_da_false">
                          学员答案：{{ zgItem.answered =='null'||zgItem.answered == undefined?'未作答':zgItem.answered}}
                        </span>
                      </template><br/>

                      <template>
                        <span class="span_da_true" v-if="daFlag=='1'">
                          正确答案：{{ zgItem.da }}
                        </span><br/>
                        <span class="span_da_true" v-if="jxFlag=='1'">
                          答案解析：{{ zgItem.jx ==undefined ? '无': zgItem.jx}}
                        </span><br/>
                        <template v-if="pjzt=='1'">
                          <span class="span_pj">
                          评分：<el-input size="mini" v-model="zgItem.logicalScore" placeholder="请填写评分"
                                       style="width: 100px" @input="checkVal(zgItem.logicalScore,zgItem.score,zgIndex)"/> 分
                        </span><br/>
                          <span class="span_pj">
                          评语：<el-input type="textarea" :rows="1" size="small"
                                       v-model="zgItem.py"
                                       placeholder="请填写评语" style="width: 500px;"/>
                        </span>
                        </template>
                        <template v-else>
                          <span class="span_pj">
                          评分：<el-input size="mini" v-model="zgPfArr[zgIndex]" placeholder="请填写评分" :type="number"
                                       style="width: 100px" @input="checkVal(zgPfArr[zgIndex],zgItem.score,zgIndex)"/> 分
                        </span><br/>
                          <span class="span_pj">
                          评语：<el-input type="textarea" :rows="1" size="small"
                                       v-model="pyArr[zgIndex]"
                                       placeholder="请填写评语" style="width: 500px;"/>
                        </span>
                        </template>
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
  selectAnswerInfo,
  updateDa,
} from '@/api/etp/examination/examination'
import vueCountDown from "@liuwave/vue-count-down"
import {delConfig} from "@/api/system/config";

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
      //考试详情
      examInfo: {},
      //考试答题详情
      testPaperInfo: {},
      //所有试题
      allQuestion: {},
      daArr: [],
      jxArr: [],
      typeItem: [],
      uid: '',
      mc: '',
      tkPfArr:{},
      zgPfArr:{},
      pyArr:{},
      dataArr:{},
      pjzt: '',
      pjTeacher:'',
      pjTime:'',
      daFlag:'0',
      jxFlag:'0'
    }
  },
  created() {
    this.uid = this.$route.params.id;
    if (this.uid !== undefined && this.uid !== '') {
      this.getInfo();
    }
  },
  methods: {
    //提交判卷
    submitTestPaper(){
      //校验
      debugger
      if(this.pjzt=='1'){
        for (let i in this.allQuestion){
          if(i == '填空' || i == '主观'){
            let children = this.allQuestion[i];
            for(let childId in children){
              this.tkPfArr[childId] = children[childId].logicalScore;
              this.zgPfArr[childId] = children[childId].logicalScore;
              this.pyArr[childId] = children[childId].py;
            }
          }
        }
      }else{
        for(let i in this.allQuestion){
          console.log(i);
          if(i == '主观'){
            let pf = false;
            let py = false;
            for (let c in this.allQuestion[i]){
              let num = c;
              for(let k in this.zgPfArr){
                if(num == k && this.zgPfArr[k]!=''){
                  pf = true;
                }
              }
              for(let e in this.pyArr){
                if(num == e && this.pyArr[e]!=''){
                  py = true;
                }
              }
            }
            if(pf == false){
              this.$message.error("您有题目未填写评分！")
              return
            }else if(py == false){
              this.$message.error("您有题目未填写评语！")
              return
            }
          }
        }
      }

      this.$confirm(`是否确定提交判卷?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        let params = {'tkpf':this.tkPfArr,'zgpf':this.zgPfArr,'py':this.pyArr,'id':this.uid};
        updateDa(params).then(res =>{
          if(res.code == 200){
            this.$message.success("判卷提交成功！")
            this.back();
          }
        })
      })
    },

    //校验评分填写是否规范
    checkVal(pf,score,index){
      debugger
      let reg = /^\d+$/
      if(!reg.test(pf)){
        this.$message.error("评分只能填写正整数！")
        if(this.pjzt==="1"){
          for(let key in this.allQuestion){
            if(index == key){
              this.allQuestion[key].logicalScore = null;
            }
          }
        }else{
          this.tkPfArr[index] = '';
          this.zgPfArr[index] = '';
        }
      }else if(pf > score){
        this.$message.error("填写的评分不能大于当前题目的分数！")
        if(this.pjzt==='1'){
          for(let key in this.allQuestion){
            if(index == key){
              this.allQuestion[key].logicalScore = null;
            }
          }
        }else{
          this.tkPfArr[index] = '';
          this.zgPfArr[index] = '';
        }
      }
    },

    //获取试卷详情
    getInfo(){
      selectAnswerInfo(this.uid).then(response => {
        if (response.code == '200') {
          console.log(response);
          debugger
          this.pjzt = response.data.examination.pjzt;
          this.pjTeacher = response.data.examination.pjTeacher;
          this.pjTime = response.data.examination.pjTime;
          this.examInfo = response.data.da.detail;
          this.mc = response.data.examination.mc;
          let allQuestion = response.data.da.sj;

          let dx = {}
          let fx = {}
          let pd = {}
          let tk = {}
          let zg = {}
          for(let i in allQuestion){
            if(allQuestion[i].lx == 1){
              dx[allQuestion[i].id] = allQuestion[i];
            }else if(allQuestion[i].lx == 2){
              fx[allQuestion[i].id] = allQuestion[i];
            }else if(allQuestion[i].lx == 3){
              pd[allQuestion[i].id] = allQuestion[i];
            }else if(allQuestion[i].lx == 4){
              tk[allQuestion[i].id] = allQuestion[i];
            }else if(allQuestion[i].lx == 5){
              zg[allQuestion[i].id] = allQuestion[i];
            }
          }
          if(Object.getOwnPropertyNames(dx).length>0) {this.allQuestion['单选'] = dx}
          if(Object.getOwnPropertyNames(fx).length>0) {this.allQuestion['复选'] = fx}
          if(Object.getOwnPropertyNames(pd).length>0) {this.allQuestion['判断'] = pd}
          if(Object.getOwnPropertyNames(tk).length>0) {this.allQuestion['填空'] = tk}
          if(Object.getOwnPropertyNames(zg).length>0) {this.allQuestion['主观'] = zg}

          // this.allQuestion = allQuestion;
          let da = '';
          for(let i in allQuestion){
            if(allQuestion[i].lx ==4){
              da = eval(allQuestion[i].da);
              this.allQuestion[i].da = da;
            }
          }
        }
      })
    },
    //返回上一个页面
    back() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/etp/examination", query: {t: Date.now()}})
    },

    goAnchor(id){
      debugger
      this.$refs["#task-" + id][0].$el.scrollIntoView({ block: 'start', inline: 'nearest' })
    },

  },
}
</script>
<style scoped lang="scss">
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.pre-exam {
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
    height: 80%;
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
  ::v-deep .tags{
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    overflow-y: scroll;
    height: calc(49vh - 124px);
    > a {
      >span{
        margin-right: 6px;
        margin-bottom: 6px;
        width: 40px;
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
      border-radius: 1px;
      .el-card__header{
        background-color: rgba(250,250,250,1);
      }
     .el-card__body{
       .el-form-item{
         margin-bottom: 0px;
         label{
           font-weight: 500;
         }
       }
     }
    }
  }
}

::v-deep .clearfix {
  font-size: 25px;
  border-bottom: 1px solid #AAAAAA;
  margin-bottom: 10px;
  width: 110%;
}
::v-deep .span_tg{
  display: block;
}


::v-deep .div_tb_left{
  width: 50%;
  float: left;
  > .el-form-item{
    margin-left: 80px;
  }
}

::v-deep .div_tb_true{
  width: 20px;
  height: 20px;
  background-color: #13ce66;
  float: left;
  border-radius: 3px;
  margin-top: 7px;
  margin-left: 50px;
}

::v-deep .div_tb{
  width: 100%;
  height: 100%;
  }
  ::v-deep .div_tb_right{
    width: 50%;
    float: right;
    > .el-form-item{
      margin-left: 50px;
    }
  }

  ::v-deep .div_tb_false{
    width: 20px;
    height: 20px;
    float: left;
    background-color: #ff4949;
    border-radius: 3px;
    margin-top: 7px;
    margin-left: 20px;
  }

  ::v-deep .span_da_false{
    color: #f56c6c;
    font-weight: bold;
    margin-left: 30px;
  }

  ::v-deep .span_da_true{
    color: #67c23a;
    font-weight: bold;
    margin-left: 30px;
  }

  ::v-deep .span_pj{
    color: black;
    font-weight: bold;
    margin-left: 60px;
  }

</style>
