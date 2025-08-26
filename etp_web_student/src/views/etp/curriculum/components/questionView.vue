<template>
<div>
  <div :class="['tm-item']">
    <div style="display: flex">
      <span v-if="!bqShow &&!child"><div class="xh" >{{xh}}、</div> </span>
      <span v-if="child"><div class="child-xh" v-if="!bqShow">{{xh}}、</div> </span>
      <div class="tm-title" v-html="` ${data.content}`"></div>
    </div>

<!--    单选题 选项-->
    <div class="tm-sel" v-if="data.type==1||data.type==2">
      <div v-for="(item,j) in data.answers">
        <el-checkbox v-model="item.selectFlag" @change="selectChange(data,j)">{{letterArr[j]}}. {{item.content}}</el-checkbox>
      </div>
    </div>
<!--   判断题 -->
    <div class="tm-sel" v-if="data.type==3" >
      <div v-for="(item,j) in data.answers">
        <el-checkbox v-model="item.selectFlag" @change="selectChange(data,j)">{{item.content}}</el-checkbox>
      </div>
    </div>

    <!--  填空题 -->
    <div class="tm-sel" v-if="data.type==4">
      <div v-for="(item,j) in data.answers">
        <el-input v-model="item.answered" @change="selectChange(data,j)"></el-input>
      </div>
    </div>
    <!--  问答题 -->
    <div class="tm-sel" v-if="data.type==5">
      <el-input type="textarea" v-model="data.answered" @change="selectChange(data)"></el-input>
    </div>

    <!--  组合题 -->
    <div class="tm-sel" v-if="data.type==6">
      <div class="child-tm" v-for="(child,j) in data.children" >
        <el-row>
          <el-col :span="20">
            <div style="display: flex">
              <span><div class="child-xh" >{{j+1}}</div> </span>
              <div class="tm-title" v-html="` (${typeFormat(child.type)})  ${child.content}`"></div>
            </div>
            <!--    单选题 多选题 选项-->
            <div class="tm-sel" v-if="child.type==1||child.type==2">
              <div v-for="(item,k) in child.answers">
                <el-checkbox v-model="item.selectFlag" @change="selectChange(child,k,data)">{{letterArr[j]}}. {{item.content}}</el-checkbox>
              </div>
            </div>
            <!--   判断题 -->
            <div class="tm-sel" v-if="child.type==3">
              <div v-for="(item,k) in child.answers">
                <el-checkbox v-model="item.selectFlag" @change="selectChange(child,k,data)">{{item.content}}</el-checkbox>
              </div>
            </div>
            <!--  填空题 -->
            <div class="tm-sel" v-if="child.type==4">
              <div v-for="(item,k) in child.answers">
                <el-input v-model="item.answered" @change="selectChange(child,k,data)"></el-input>
              </div>
            </div>
            <!--  问答题 -->
            <div class="tm-sel" v-if="child.type==5">
              <el-input type="textarea" v-model="child.answered" @change="selectChange(child,j,data)"></el-input>
            </div>
          </el-col>
        </el-row>
        <div class="divider"></div>
      </div>
    </div>
    <slot></slot>
  </div>

</div>
</template>

<script>
export default {
  name: "questionView",
  props:{
    data:{
      type: Object
    },
    bqShow:{
      type: Boolean,
      default: true
    },
    xh:{},
    tm_index:{
      type: Number,
      default: 0
    },
    child:{
      default:false
    },
    isShowAnswer:{
      default: true
    }
  },
  created() {
    this.getDicts("question_level").then(response => {
      this.levelOptions = response.data;
    });
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
  },
  data(){
    return{
      levelOptions:[],
      letterArr:[],
      typeOptions:[],
    }
  },
  methods:{
    // 难度等级字典翻译
    levelFormat(level, column) {
      return this.selectDictLabel(this.levelOptions, level);
    },
    // 题目类型字典翻译
    typeFormat(lx, column) {
      return this.selectDictLabel(this.typeOptions, lx);
    },

    selectChange(data,index,fatherData){
      let params= {data:data,cIndex:index,fatherData:fatherData}
      this.$emit('selectChange',params)
    },

  }

}
</script>

<style scoped lang="scss">
.tm-item{
  //pointer-events: none;
  font-size: 16px;
}
.tm-title{
  display: inline-block;
  margin-left: 0px;
  margin-bottom: 20px;
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
  //background: url("../../../assets/images/tag.png") 100% 100% no-repeat;
  //color: #fff;
  text-align: center;
  line-height: 25px;
  margin-right: 5px;
}
.child-xh{
  height: 20px;
  width: 20px;
  //background: url("../../../assets/images/fang20.png") 100% 100% no-repeat;
  color: #fff;
  margin-right: 10px;
  text-align: center;
  line-height: 20px;
}
::v-deep video{
  width: 285px;
  height: 130px;
}
</style>
