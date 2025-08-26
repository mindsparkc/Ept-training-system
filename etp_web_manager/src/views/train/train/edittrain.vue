<template>
  <div class="app-container">
    <el-backtop :bottom="10" :right="15">

    </el-backtop>
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" class="span4-left">
        <div class="nav-area">
          <div class="tj-area">
            <div>习题总数：<span style="margin-right: 30px;">{{paper.quCount}}</span>习题总分：<span>{{paper.totalScore}}</span></div>

            <!--          <div>试卷总分：<span>{{paper.totalScore}}</span></div>-->
            <div><label style="color:red;">* </label> 及格分数：<el-input v-model="paper.qualifyScore" size="mini"
                                                                     onkeyup="value=value.replace(/[^\d]/g,'')"
                                                                     @blur="paper.qualifyScore = $event.target.value"
                                                                     style="width: 100px;"></el-input>
            </div>
            <div> <label style="color:red;">* </label>
              考试时长：<el-input v-model="paper.suggestTime"
                             onkeyup="value=value.replace(/[^\d]/g,'')"
                             @blur="paper.suggestTime = $event.target.value"
                             size="mini" style="width: 100px;"></el-input> 分钟
            </div>

            <!--          <div >简单<span> {{ 0}} </span>个 , 一般<span> {{ 0 }} </span>个</div>-->
            <!--          <div >较难<span> {{ 0 }} </span>个 , 困难<span> {{ 0 }} </span>个</div>-->
          </div>
          <el-divider></el-divider>
          <div class="card-item" v-for="(item,index) in paperUnit" @click="jumpClick(index)">
            <div>{{ item.display }}</div>
            <div>
              <el-button type="text" @click.stop="delUnit(index,item)">
                <i class="el-icon-delete"></i>删除
              </el-button>
            </div>
            <div class="tj-tm">
              共<span> {{item.quCount}} </span>题 , 共<span> {{item.totalScore}} </span>分
              <div v-show="item.quType!=6">默认每题
                <el-input size="mini"
                          v-model="item.eachScore"
                          @blur="disBlur(item)"
                          @input="validateScore(item)"></el-input> 分
              </div>

              <!--            <p v-if="item.lc!=undefined" v-for="type in item.lc">{{ type.level }}<span> {{ type.count }} </span>个   </p>-->
            </div>
            <el-divider></el-divider>
          </div>
        </div>
      </el-col>


      <el-col :span="20" :xs="24" class="span20-right">
        <el-form ref="paperForm" :model="paper" :rules="rules" label-width="100px">
          <el-card class="top-card" shadow="always">
            <el-form-item label="习题集名称" prop="name">
              <el-input
                v-model="paper.name"
                placeholder="请输入习题集名称"
                clearable
                size="small"
              />
            </el-form-item>
            <el-form-item label="习题集分类" prop="categoryIds" >
              <el-cascader
                style="width: 100%;"
                :options="categoryOptions"
                v-model="paper.categoryIds"
                :props="{ multiple: true, checkStrictly: true ,value:'id'}"
                clearable>
              </el-cascader>
            </el-form-item>
          </el-card>

          <div class="title-area">
            <div>题目列表</div>
            <el-button type="primary" v-show="this.paper.paperId!=null" @click="handleView">习题集预览</el-button>
            <el-dropdown>
              <el-button type="primary">
                添加新大题<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="item in tmTypeOptions" @click.native="selTmType(item)">{{item.dictLabel}}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button type="primary" @click="temporary(0)">暂存</el-button>
            <el-button type="primary" @click="temporary(99)">发布</el-button>
          </div>
          <!-- 题目列表    -->
          <tm-model
            ref="organizeUnit"
            :unit="paperUnit"
            :key="timeRefusr"
            @changeScore="changeScore"
            @sumCountByLevel="sumCountByLevel"
          >
          </tm-model>

          <div class="title-area" v-show="isShow">
            <div></div>
            <el-button type="primary" v-show="this.paper.paperId!=null" @click="handleView">习题集预览</el-button>
            <el-dropdown>
              <el-button type="primary">
                添加新大题<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="item in tmTypeOptions" @click.native="selTmType(item)">{{item.dictLabel}}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button type="primary" @click="temporary(0)">暂存</el-button>
            <el-button type="primary" @click="temporary(99)">发布</el-button>
          </div>
        </el-form>
      </el-col>
    </el-row>

    <tipDialog :tip-visible="tipVisible"
               :tipInfo="tipInfo"
               :infoTitle="infoTitle"
               :showQd="true"
               @clickSave="submit"
               @tipClose="tipVisible = false"></tipDialog>
  </div>
</template>

<script>
import {getPaperTreeselect, getTrainTreeselect} from "@/api/category/category";
import { getPaper, addPaper, updatePaper } from "@/api/paper/paper";
import TmModel from "@/views/paper/components/tm-model"
import singleImageUpload from "@/components/singleImageUpload";
import tipDialog from "@/components/tipDialog"

export default {
  name: "edittrain",
  components:{
    TmModel,singleImageUpload,tipDialog
  },
  data(){
    return{
      paper:{
        paperId:null,
        flag:'train',
        qualifyScore:0,
        suggestTime:60,//建议考试时长
        totalScore:0,
        quCount:0, //题目数量
        paperUnit:null,
        status: 0,
        lc:[]
      },

      categoryOptions:[],
      tmTypeOptions:[],
      //新增的题目类型
      sel_tm_Type:[],

      rules:{
        name: [
          { required: true, message: "习题集名称", trigger: "blur" }
        ],
        categoryIds: [
          { required: true, message: "习题集分类", trigger: "blur" }
        ],
      },

      paperUnit: null,
      timeRefusr: 1,
      //
      allLoading:null,
      isShow: false,
      scrolltop:0,

      tipVisible:false,
      tipInfo:{},
      infoTitle:"jdjdj"
    }
  },
  mounted() {
    window.addEventListener("scroll",this.scrollEvent,true)
  },
  created() {
    this.allLoading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });

    getTrainTreeselect().then(res => {
      this.categoryOptions = res.data
    })
    this.getDicts("question_type").then(response => {
      this.tmTypeOptions = response.data;
    });
    //编辑 回显
    this.paper.paperId = this.$route.query.paperId
    let catIds = this.$route.query.catIds
    if(undefined != catIds && catIds.length>0){
      this.paper.categoryIds = [catIds]
    }

    if(undefined != this.paper.paperId && null!=this.paper.paperId){
      this.getPaperInfo()
    }else{
      this.allLoading.close()
    }
  },
  methods:{
    selTmType(e){
      let len = 0
      if(this.paperUnit==null){
        this.paperUnit = []
      }else{
        len = this.paperUnit.length+1
      }
      let unit = {
        display: e.dictLabel, //单元名称
        quType: e.dictValue, //单元题型
        totalScore:0, //总分
        quCount:0, //总题目数量
        eachScore: 0, //每题默认分值如果 rules 中未设置，score 则使用该值作为题目分数
        quOrderFlag:0, //题目排序策略（0正序 1随机）枚举(2)
        answerOrderFlag:0, //候选答案排序策略（0正序 1随机）
        sort: len, //排序0 <= 值 <= 9
        unitRule:[],//组卷内容抽题、选题为题目内容，随机为规则内容
        //难度级别
        lc:[]
      }
      this.paperUnit.push(unit)
      this.$nextTick(_=>{
        window.scroll(0, document.querySelector('body').scrollHeight)
      })
    },
    disBlur(item){
      let reg =/\.$/
      if(reg.test(item.eachScore)){
        item.eachScore = item.eachScore+0
      }
    },
    validateScore(item){
      let num = item.eachScore
        .replace(/[^\d.]/g, "") //将非数字和点以外的字符替换成空
        .replace(/^\./g, "") //验证第一个字符是数字而不是点
        .replace(/\.{2,}/g, ".") //出现多个点时只保留第一个
        .replace(".", "$#$") // 1、将数字的点替换成复杂字符$#$
        .replace(/\./g, "") // 2、将字符串的点直接清掉
        .replace("$#$", ".") // 3、将复杂字符再转换回点
        .replace(/^(\-)*(\d+)\.(\d).*$/, "$1$2.$3"); //只能输入1个小数
      item.eachScore = num
      this.changeScore(item)
    },
    //分数变化统计
    changeScore(item){
      if(item!=null){
        item.totalScore = 0
        item.quCount = 0
        item.unitRule.forEach(que =>{
          if(item.quType ==6){//组合题，统计小题的分数
            item.quCount ++
            que.score = 0
            que.children.forEach(child =>{
              let score = Number(this.isTrue(child.score)?child.score:0)
              que.score =(que.score*10 +score*10)/10
              item.totalScore = (item.totalScore*10 +score*10)/10
            })

          }else{
            let score = Number(this.isTrue(que.score)?que.score:item.eachScore)
            if(this.isTrue(que.rule)){ //是规则
              que.rule.lc.forEach(r =>{
                let r_score = Number(r.count) * score
                item.totalScore = (item.totalScore*10 +r_score*10)/10
                item.quCount += Number(r.count)
              })
            }else{
              item.totalScore = (item.totalScore*10 +score*10)/10
              item.quCount ++
            }
          }

        })
      }

      this.paper.quCount=0
      this.paper.totalScore=0
      //统计总分 总题目个数
      this.paperUnit.forEach(unit =>{
        unit.unitRule.forEach(item =>{
          if(this.isTrue(item.rule)){
            item.rule.lc.forEach(r =>{
              this.paper.quCount += Number(r.count)
            })
          }else{
            this.paper.quCount ++
          }
        })

        // this.paper.total +=unit.unitRule.length
        this.paper.totalScore += unit.totalScore
      })
    },

    //统计各类型题目个数
    sumCountByLevel(item){

      item.lc =[]
      let json ={1:0,2:0,3:0,4:0}
      item.unitRule.forEach(que =>{
        if(this.isTrue(que.rule)){
          //规则循环
          que.rule.lc.forEach(r =>{
            json[r.level] += Number(r.count)

          })
        }else{
          json[que.level] += 1
        }
      })
      for(let i in json){
        item.lc.push({level:i,count:json[i]})
      }

      //计算各个level总的题目个数
      let that =this
      let level_obj={1:0,2:0,3:0,4:0}
      that.paperUnit.forEach(item=>{
        item.lc.forEach(lev=>{
          level_obj[lev.level] += lev.count
        })
      })
      for(let i in level_obj){
        this.paper.lc.push({level:i,count:level_obj[i]})
      }
    },

    delUnit(index,item){
      this.$confirm('是否确认删除【'+item.display+'】所有题目', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.paperUnit.splice(index,1);
        this.timeRefusr = new Date().getTime()
        this.changeScore(null)
      }).then(response => {

      }).catch(() => {});

    },
    //锚点
    jumpClick(index){
      document.querySelector(`#visit_${index}`).scrollIntoView({
        behavior: "smooth"
      });
    },
    //暂存
    temporary(status){
      this.$refs["paperForm"].validate(valid => {
        if (valid) {
          this.paper.status = status
          this.paper.flag = 'train'
          this.paper.paperUnit = this.$refs.organizeUnit.paperUnit
          if(status ==99){
            this.infoTitle= "是否确认发布习题集？"
            this.tipInfo ={
              "习题集名称":this.paper.name,
              "习题集总分":this.paper.totalScore,
              "及格分数":this.paper.qualifyScore,
              "测试时长":(this.paper.suggestTime==null?"":this.paper.suggestTime) + " 分钟",
              "题目个数":this.paper.quCount
            }
            this.tipVisible = true
          }else{
            this.submit()
          }

        }
      })
    },

    submit(){
      if(this.paper.qualifyScore==null || this.paper.qualifyScore ==''){
        this.$message.error("及格分数不能为空")
        return;
      } else if(this.paper.totalScore<this.paper.qualifyScore){
        this.$message.error("及格分数不能大于总分数")
        return;
      }

      if(this.paper.suggestTime==null || this.paper.suggestTime ==''){
        this.$message.error("考试时长不能为空")
        return;
      }
      if(this.paper.paperId==null){
        addPaper(this.paper).then(res =>{

          this.paper.paperId = res.data.paperId
          if(this.paper.status ==99 ){

            this.msgSuccess("发布成功");
            this.goBack()
          }else{
            this.msgSuccess("暂存成功");
          }
        }).catch(err=>{
          this.tipVisible = false
        })
      }else{
        updatePaper(this.paper).then(res =>{
          if(this.paper.status ==99 ){
            this.msgSuccess("发布成功");
            this.goBack()
          }else{
            this.msgSuccess("暂存成功");
          }
        }).catch(err=>{
          this.tipVisible = false
        })
      }
    },
    //回显
    getPaperInfo(){
      getPaper(this.paper.paperId).then(res =>{
        this.paper = res.data
        this.paperUnit = this.paper.paperUnit
        this.$nextTick(_=>{
          let len = this.paperUnit.length
          for(let i=0;i<len;i++){
            this.$refs.organizeUnit.backDropRow(i)
          }
          this.allLoading.close()
        })
      }).catch(error=>{
        this.allLoading.close()
      })
    },
    isTrue(value){
      if(value!=undefined && value!=null&&value!=""){
        return true
      }
      return false
    },
    goBack(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path:"/exam/testpaper" ,
      });
    },
    scrollEvent(e){

      if(e.srcElement.scrollingElement!==undefined){
        this.scrolltop= e.srcElement.scrollingElement.scrollTop; // 获取页面滚动高度
      }
      if ( this.scrolltop > 200) {
        this.isShow = true;
      }else{
        this.isShow = false;
      }
    },
    handleView(){
      const id = this.paper.paperId

      let router = this.$router.resolve({
        path: "/train-view/view",
        query: { paperId: id}
      })
      window.open(router.href,'_blank')

    },
  }

}
</script>

<style scoped lang="scss">
::v-deep .el-form-item {
  margin-bottom: 16px;
}
.top-card{
  ::v-deep .el-card__body{
    padding: 15px 20px 5px 20px;
  }
}
.title-area{
  position: relative;
  width: 100%;
  height: 56px;
  padding: 10px;
  div:first-of-type{
    width: 100px;
    position: absolute;
    font-weight: 700;
    line-height: 40px;
  }
  .el-dropdown{
    position: absolute;
    right: 160px;
    height: 36px;
  }
  > .el-button:nth-of-type(1){
    position: absolute;
    right: 300px;
  }
  >.el-button:nth-of-type(2){
    position: absolute;
    right: 80px;
  }
  >.el-button:nth-of-type(3){
    position: absolute;
    right: 0px;
  }
}
.nav-area{
  height: calc(100vh - 140px);
  overflow: auto;
}
.tj-area{
  font-size: 15px;
  font-weight: 600;
  >div{
    padding: 10px 10px 0px;
  }
  ::v-deep .el-form-item__label{
    font-size: 15px;
  }
  ::v-deep .el-form-item{
    margin-bottom: 0px;
  }
  span{
    color: #e35725;
    font-style: italic;
    font-size: 15px;
  }
  ::v-deep .el-input--mini {
    font-size: 15px;
  }

}
.card-item{
  padding: 10px;
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
  div:nth-of-type(2){
    float: right;
  }
  .el-input{
    width: 70px;
  }
}
::v-deep .el-divider--horizontal{
  margin: 10px 0px 14px;
}
.tj-tm{
  margin-top: 15px;
  div{
    margin-top: 4px;
  }
  span{
    color: #e35725;
    font-style: italic;
  }
  p{
    margin: 8px 0px;
  }
}
::v-deep .el-backtop{
  font-size: 15px;
  font-weight: 600;
  width: 32px;
  height: 32px;
  background-color: #f2f5f6;
}
</style>
