<template>
<div class="app-container">
  <el-row :gutter="20">
    <el-col :span="4" :xs="24" class="box-show">
      <div class="nav-area">
        <div class="card-item" v-for="(item,u_index) in paperUnit" @click="jumpClick(u_index)">
          <div>{{ item.display }}</div>
          <div class="tj-tm">
            共<span class="j-italic"> {{item.quCount}} </span>题 , 总计<span class="j-italic"> {{item.totalScore}} </span>分
          </div>
          <div class="bh-item">
            <el-tag v-for="(que,t_index) in item.questions" @click.stop="jumpTmClick(u_index,t_index)">
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
          <div>习题集名称：{{paperForm.paper.name}}</div>
          <div>习题集总分：{{paperForm.paper.totalScore}}</div>
          <div>及格分数：{{ paperForm.paper.qualifyScore }} </div>
          <div>试题数量：{{paperForm.paper.quCount}}</div>
          <div>考试时长：{{paperForm.paper.suggestTime}} 分钟</div>
<!--          <div>试卷分类：付付付付付付付付</div>-->
          <div>创建时间：{{paperForm.paper.createTime}}</div>
          <div>更新时间：{{paperForm.paper.updateTime}}</div>
          <el-divider></el-divider>
          <el-checkbox v-model="isShowAnswer">显示答案和解析</el-checkbox>
        </div>
        <div class="print-btn">
          <el-button type="primary" v-print="printContent" @click="prints" >打印习题集</el-button>
        </div>
      </div>
    </el-col>
  </el-row>
</div>
</template>

<script>
import {paperView} from "@/api/paper/paper"
import qusetionView from "@/views/components/question/questionView"

export default {
  name: "viewpaper",
  components: {qusetionView},
  data(){
    return{
      paperId:null,
      paperForm:null,
      paperUnit:[],
      isShowAnswer:true,
      printContent: {
        id: "print", // 打印的区域
        preview: false, // 预览工具是否启用
        previewTitle: '这是预览标题', // 预览页面的标题
        popTitle: null, // 打印页面的页眉
      }
    }
  },
  created() {
    this.paperId = this.$route.query.paperId
    this.getView()
  },
  methods: {
    getView(){
      paperView( this.paperId).then(res =>{
        this.paperForm = res.data
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
    prints(){
      this.printContent.popTitle = this.paperForm.name
    }

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
  height: calc(100vh - 50px);
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
</style>
