<template>
<div class="app-container-xq">
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
      <div class="content" id="print" ref="print">
        <div v-for="(item,k) in paperUnit">
          <div :id="`visit_${k}`" class="unit-title">
            【{{item.display}}】 共{{item.quCount}}题，总计{{item.totalScore}}分
          </div>
          <div v-for="(question,i) in item.questions" style="margin-bottom: 30px;">
            <el-row :id="`tm_${k}_${i}`">
              <el-col :span="20">
                <qusetion-view
                  :data="question"
                  :bqShow="false"
                  :is-show-answer="isShowAnswer"
                  :each-score="question.eachScore"
                  :xh="i+1">
                </qusetion-view>
              </el-col>
              <el-col :span="4" v-show="question.type!=6" style="text-align: center">
                本题 <span class="j-italic">{{question.score==null?item.eachScore:question.score}}</span> 分
              </el-col>
            </el-row>
            <div class="divider"></div>
            <div class="box-card" v-if="question!=null && question.children!=null">
              <div class="child-tm" v-for="(child,j) in question.children" >
                <el-row>
                  <el-col :span="20">
                    <qusetion-view
                      :bqShow="false"
                      :xh="j+1"
                      :child="true"
                      :is-show-answer="isShowAnswer"
                      :data="child">
                    </qusetion-view>
                  </el-col>
                  <el-col :span="4" style="text-align: center">
                    本题 <span class="j-italic"> {{child.score==null?item.eachScore:child.score}}</span> 分
                  </el-col>
                </el-row>
                <div class="divider"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="4" :xs="24" >
      <div class="nav-area">
        <div class="top1">
          <div>考试名称：{{examForm.name}}</div>
          <div>考试总分：{{examForm.totalScore}}</div>
          <div>学员得分：{{examForm.userScore}}</div>
          <div>试题数量：{{examForm.quCount}}</div>
          <div>考试时长：{{examForm.doTime[0]}}分{{examForm.doTime[1]}}秒</div>
          <el-divider></el-divider>
<!--          <el-checkbox v-model="isShowAnswer">显示答案和解析</el-checkbox>-->
        </div>
        <div class="print-btn">
<!--          <el-button type="primary" v-print="printContent" @click="prints" >打印试卷</el-button>-->
          <el-button type="primary" @click="back" >返回</el-button>
        </div>
      </div>
    </el-col>
  </el-row>
</div>
</template>

<script>
import { getHistoryExam } from "@/api/exam/exam";
import qusetionView from "@/views/components/question/questionView";

export default {
  name: "viewpaper",
  components: { qusetionView },
  data() {
    return {
      recordId: null,
      examForm: null,
      paperUnit: [],
      isShowAnswer: true,
    };
  },
  created() {
    this.recordId = this.$route.query.recordId;
    this.getView();
  },
  methods: {
    back() {
      this.$router.go(-1);
    },

    getView() {
      getHistoryExam(this.recordId).then(res => {
        // debugger
        console.log(res.data);
        this.examForm = res.data;
        let m = parseInt((res.data.doTime / 60) % 60);
        let s = parseInt(res.data.doTime % 60);
        let date = [];
        date[0] = m;
        date[1] = s;
        this.examForm.doTime = date;
        this.paperUnit = res.data.exam.paperUnit;
      });
    },
    //锚点
    jumpClick(index) {
      document.querySelector(`#visit_${index}`).scrollIntoView({
        behavior: "smooth",
      });
    },
    jumpTmClick(u_index, t_index) {
      document.querySelector(`#tm_${u_index}_${t_index}`).scrollIntoView({
        behavior: "smooth",
      });
    },
    // prints(){
    //   this.printContent.popTitle = this.examForm.name
    // }
  },
};
</script>

<style scoped lang="scss">
.top1 {
  padding: 0 10px;
  font-size: 14px;
  font-weight: 600;
  div {
    line-height: 40px;
  }
}
.content {
}
.tj-tm {
  margin-top: 15px;
  div {
    margin-top: 4px;
  }
}
.j-italic {
  color: #e35725;
  font-style: italic;
}
.card-item {
  padding: 0px 10px;
  font-size: 14px;
  div:first-child {
    color: #e35725;
    font-weight: 600;
    font-size: 15px;
    cursor: pointer;
    &:hover {
      color: #53a2eb;
    }
  }
}
.nav-area {
  height: calc(100vh - 130px);
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  overflow: auto;
  padding: 10px 0px;
}
.bh-item {
  margin-top: 10px;
  .el-tag {
    margin-right: 10px;
    margin-top: 10px;
    width: 40px;
    text-align: center;
  }
}
.span20-right {
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}
.unit-title {
  padding: 20px 0px;
  font-size: 18px;
  color: #e35725;
  font-weight: 600;
  margin-left: -4px;
}
::v-deep .tm-sel {
  margin-left: 30px;
}
.child-tm {
  padding-left: 30px;
  margin-bottom: 30px;
}
::v-deep .answer-item {
  margin-left: 30px;
}
.span16-right {
  //width: calc(100% - 280px - 280px - 10px - 10px) !important;
}
.print-btn {
  text-align: center;
  margin-top: 44px;
}
.divider {
  margin-top: 20px;
  border-bottom: #eee 2px dotted;
  margin-bottom: 20px;
}
.el-tag {
  cursor: pointer;
}
</style>
