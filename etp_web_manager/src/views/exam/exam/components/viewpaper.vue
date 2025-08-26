<template>
<div class="app-container">
  <el-row :gutter="20">
    <el-col :span="4" :xs="24" class="box-show">
      <div class="nav-area">
        <div class="card-item" v-for="(item,u_index) in paperUnit" @click="jumpClick(u_index)">
          <div>{{ item.display }}</div>
          <div class="tj-tm">
            共<span class="j-italic"> {{item.quCount}} </span>题 , 共<span class="j-italic"> {{item.totalScore}} </span>分
          </div>
          <div class="bh-item">
            <el-tag :type="(que.rightFlag==true?'success' : 'danger')" v-for="(que,t_index) in item.questions" @click.stop="jumpTmClick(u_index,t_index)">
              {{t_index+1}}
            </el-tag>
          </div>
          <el-divider></el-divider>
        </div>
      </div>
    </el-col>
    <el-col :span="16" :xs="24" class="nav-area">
      <el-form label-position="top" :model="paperUnit" ref="form" label-width="100px">
        <div class="content" id="print" ref="print">
          <div v-for="(item,k) in paperUnit">
            <div :id="`visit_${k}`" class="unit-title">
              【{{item.display}}】 共{{item.quCount}}题，总计{{item.totalScore}}分
            </div>
            <div v-for="(question,i) in item.questions" style="margin-bottom: 30px;">
              <el-row :id="`tm_${k}_${i}`" :gutter="20">
                <el-col :span="17">
                  <div :class="['tm-item']">
                    <div style="display: flex">
                      <span><div class="xh" >{{i+1}}</div> </span>
                      <div class="tm-title" v-html="` (${typeFormat(question.type)})  ${question.content}`"></div>
                    </div>
                    <!--    单选题 多选题 选项-->
                    <div class="tm-sel" v-if="question.type==1||question.type==2">
                      <div v-for="(item,j) in question.answers">
                        <el-checkbox v-model="item.selectFlag">{{letterArr[j]}}. {{item.content}}</el-checkbox>
                      </div>
                    </div>
                    <!--   判断题 -->
                    <div class="tm-sel" v-if="question.type==3">
                      <div v-for="(item,j) in question.answers">
                        <el-checkbox v-model="item.selectFlag">{{item.content}}</el-checkbox>
                      </div>
                    </div>
                    <!--  填空题 -->
                    <div class="tm-sel" v-if="question.type==4">
                      <div v-for="(item,j) in question.answers">
                        <el-input v-model="item.answered"></el-input>
                      </div>
                    </div>
                    <!--  问答题 -->
                    <div class="tm-sel" v-if="question.type==5">
                      <el-input type="textarea" v-model="question.answered"></el-input>
                    </div>

                    <div class="answer-item"  v-if="question.type!=6 && isShowAnswer">
                      <div class="tm-da-item tm-da" v-if="question.type!=6 && question.type!=5">
                        <span>答案： </span>
                        <span v-if="question.type==1||question.type==2">
                          <span v-for="(da,i) in question.answers" v-if="da.rightFlag">
                            {{letterArr[i]}}
                          </span>
                        </span>
                        <span v-if="question.type==3">
                          <span v-for="(da,i) in question.answers" v-if="da.rightFlag">
                            {{da.content}}
                          </span>
                        </span>
                        <!--     填空题 -->
                        <span class="tm-tk" v-if="question.type==4">
                          <span v-for="(item,i) in question.answers">
                            {{i+1}}. <span v-for="tk in item.contents">{{tk}}</span> <br/>
                          </span>
                        </span>
                      </div>
                      <div class="tm-da-item tm-jx" v-if="question.type!=6">
                        <span>解析：
                          <span v-if="question.analysis==null">暂无</span>
                          <span v-else v-html="question.analysis"></span>
                        </span>
                      </div>
                    </div>

                    <slot></slot>
                  </div>
                </el-col>
                <el-col :span="6" v-show="question.type!=6" style="text-align: left;background-color: #d3dce6">
                  <el-row class="pj-show">
                    本题 <span class="j-italic">{{question.score==null?item.eachScore:question.score}}</span> 分
                  </el-row>
                  <el-row class="pj-show">
                    <el-row class="pj-show">
                      <el-col :span="7" style="padding-top: 5px">得分</el-col>
                      <el-col :span="15">
                        <el-input v-model="question.recordScore" :style="inputStyle"
                                  @change="checkVal(question.recordScore,question.score)"
                                  oninput="value=value.replace(/^0+(\d)|[^\d]+/g,'')"
                                  :placeholder="question.useScore"></el-input>
                      </el-col>
                    </el-row>
                    <el-row class="pj-show">
                      <el-col :span="7" style="padding-top: 5px">评语</el-col>
                      <el-col :span="17"><el-input type="textarea" v-model="question.review"></el-input></el-col>
                    </el-row>
                  </el-row>
                </el-col>
              </el-row>
              <div class="divider"></div>
              <div class="box-card" v-if="question!=null && question.children!=null">
                <div class="child-tm" v-for="(child,j) in question.children" >
                  <el-row :gutter="20">
                    <el-col :span="17" :class="['tm-item']">
                      <div style="display: flex">
                        <span><div class="child-xh" >{{j+1}}</div> </span>
                        <div class="tm-title" v-html="` (${typeFormat(child.type)})  ${child.content}`"></div>
                      </div>
                      <!--    单选题 多选题 选项-->
                      <div class="tm-sel" v-if="child.type==1||child.type==2">
                        <div v-for="(item,k) in child.answers">
                          <el-checkbox v-model="item.selectFlag">{{letterArr[j]}}. {{item.content}}</el-checkbox>
                        </div>
                      </div>
                      <!--   判断题 -->
                      <div class="tm-sel" v-if="child.type==3">
                        <div v-for="(item,k) in child.answers">
                          <el-checkbox v-model="item.selectFlag">{{item.content}}</el-checkbox>
                        </div>
                      </div>
                      <!--  填空题 -->
                      <div class="tm-sel" v-if="child.type==4">
                        <div v-for="(item,k) in child.answers">
                          <el-input v-model="item.answered"></el-input>
                        </div>
                      </div>
                      <!--  问答题 -->
                      <div class="tm-sel" v-if="child.type==5">
                        <el-input type="textarea" v-model="child.answered"></el-input>
                      </div>

                      <div class="answer-item"  v-if="child.type!=6 && isShowAnswer">
                        <div class="tm-da-item tm-da" v-if="child.type!=6&&child.type!=5">
                          <span>答案： </span>
                          <span v-if="child.type==1||child.type==2">
                          <span v-for="(da,i) in child.answers" v-if="da.rightFlag">
                            {{letterArr[i]}}
                          </span>
                        </span>
                          <span v-if="child.type==3">
                          <span v-for="(da,i) in child.answers" v-if="da.rightFlag">
                            {{da.content}}
                          </span>
                        </span>
                          <!--     填空题 -->
                          <span class="tm-tk" v-if="child.type==4">
                          <span v-for="(item,i) in child.answers">
                            {{i+1}}. <span v-for="tk in item.contents">{{tk}}</span> <br/>
                          </span>
                        </span>
                        </div>
                        <div class="tm-da-item tm-jx" v-if="child.type!=6">
                        <span>解析：
                          <span v-if="child.analysis==null">暂无</span>
                          <span v-else v-html="child.analysis"></span>
                        </span>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="6" v-show="question.type==6" style="text-align: left;background-color: #d3dce6">
                      <el-row class="pj-show">
                        本题 <span class="j-italic">{{child.score==null?item.eachScore:child.score}}</span> 分
                      </el-row>
                      <el-row class="pj-show">
                        <el-row class="pj-show">
                          <el-col :span="7" style="padding-top: 5px">得分</el-col>
                          <el-col :span="15">
                            <el-input v-model="child.recordScore" style="width: 80px;" :placeholder="child.useScore"
                                      @change="checkVal(child.recordScore,child.score)"
                                      oninput="value=value.replace(/^0+(\d)|[^\d]+/g,'')"></el-input>
                          </el-col>
                        </el-row>
                        <el-row class="pj-show">
                          <el-col :span="7" style="padding-top: 5px">评语</el-col>
                          <el-col :span="17"><el-input type="textarea" v-model="child.review"></el-input></el-col>
                        </el-row>
                      </el-row>
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
          <div>试卷名称：{{examForm.name}}</div>
          <div>考试总分：{{examForm.totalScore}}</div>
          <div>学员得分：{{examForm.userScore}}</div>
          <div>试题数量：{{examForm.quCount}}</div>
          <el-divider></el-divider>
          <el-checkbox v-model="isShowAnswer">显示答案和解析</el-checkbox>
        </div>
        <div class="print-btn">
          <el-button type="primary" @click="checkRecord" >提交判卷</el-button>
          <el-button type="primary" @click="closeWindow" >返回</el-button>
        </div>
      </div>
    </el-col>
  </el-row>
</div>
</template>

<script>
import {getHistoryExam,reviewSubmit} from "@/api/exam/exam"

export default {
  name: "viewpaper",
  components: {},
  data(){
    return{
      recordId:null,
      examForm: {},
      paperUnit: {},
      isShowAnswer:false,
      typeOptions:[],
      inputStyle:{
        width:'80px',
      },
    }
  },
  created() {
    this.recordId = this.$route.query.recordId
    console.log(this.recordId)
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
    this.getView()
  },
  methods: {

    //校验评分填写是否规范
    checkVal(recordScore,score){
      if(recordScore > score){
        this.$message.error("填写的评分不能大于当前题目的分数！")
      }
    },

    //校验用户是否评卷
    checkRecord(){
      let flag = false
      this.paperUnit.forEach((data,uIndex) => {
        data.questions.forEach((item,kIndex) =>{
          if(item.type===5){
            if(item.useScore==null&&item.recordScore==undefined){
              this.$message.error("问答题第"+ (kIndex+1) +"题未评卷，请评卷后提交！")
              flag = false
              return
            }else if(item.recordScore>item.score){
              this.$message.error("问答题第"+ (kIndex+1) +"题评卷分数不能大于题目分数，请修改后提交！")
              flag = false
              return
            }else{
              item.useScore = item.recordScore;
              flag = true
            }
          }else if(item.type == 6){
            item.children.forEach((child,cIndex) => {
              if(child.type == 5){
                if(child.useScore==null&child.recordScore==undefined){
                  this.$message.error("组合题中的问答题第"+ (cIndex+1) +"题未评卷，请评卷后提交！")
                  flag = false
                  return
                }else if(child.recordScore>child.score){
                  this.$message.error("组合题中的问答题第"+ (kIndex+1) +"题评卷分数不能大于题目分数，请修改后提交！")
                  flag = false
                  return
                }else{
                  child.useScore = child.recordScore
                  flag = true
                }
              }
            })
          }else if(item.type!=5&&item.type!=6){
            item.useScore = item.recordScore;
            flag = true
          }
        })
      })
      if(flag){
        this.submitMarkExam()
      }
    },


    //提交判卷方法
    submitMarkExam(){
      this.examForm.paperUnit = this.paperUnit;
      reviewSubmit(this.examForm).then(res => {
        if(res.code==200){
          this.$message.success(res.msg);
          setTimeout(()=>{
            this.closeWindow()
          }, 2000)
        }
      })
    },

    closeWindow(){
      this.$store.dispatch('tagsView/delView', this.$route);
      window.open('','_self').close();
    },

    back(){
      this.$router.go(-1)
    },

    getView(){
      getHistoryExam(this.recordId).then(res =>{
        console.log(res.data)
        this.examForm = res.data
        let m = parseInt(res.data.doTime / 60 % 60)
        let s = parseInt(res.data.doTime % 60)
        let date = []
        date[0] = m
        date[1] = s
        this.examForm.doTime = date
        this.paperUnit = res.data.paperUnit
      })
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
    // prints(){
    //   this.printContent.popTitle = this.examForm.name
    // }

    // 题目类型字典翻译
    typeFormat(lx, column) {
      return this.selectDictLabel(this.typeOptions, lx);
    },

  }
}
</script>

<style scoped lang="scss">
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
}
.divider{
  margin-top: 20px;
  border-bottom: #eee 2px dotted;
  margin-bottom: 20px;
}
.el-tag{
  cursor: pointer;
}

.tm-item{
  pointer-events: none;
  font-size: 16px;
}
.tm-title{
  display: inline-block;
  margin-left: 0px;
  margin-bottom: 20px;
  font-size:16px;
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
    font-size: 16px;
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
.pj-show{
  padding: 6px;
}
</style>
