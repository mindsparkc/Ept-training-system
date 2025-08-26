<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24">
        <div class="app-container-right">
          <el-form :model="testQuestionDetails" label-position="top" ref="form" label-width="100px">
            <el-card class="box-card" v-for="(item,key,index) in testQuestionDetails" :key="index">
              <div slot="header" class="clearfix">
                <span>{{typeItem[key]}}</span>
              </div>
              <div v-if="typeItem[key] === '单选'">
                <el-form-item  v-for="(data,dIndex) in item" :key="dIndex" :label="(data.index) + '.' + data.tg" :ref="'#task-'+ data.tkid">
                  <el-radio-group v-model="singleAnswer[data.tkid]">
                    <el-radio v-for="(cData,cIndex) in data.xx" :key="cIndex" :label="cData.key">{{cData.key + '.' + cData.value}}</el-radio>
                  </el-radio-group>
                  <el-row>
                    <el-col :span="24">
                      <div class="standardAnswer">标准答案：{{data.da}}</div>
                    </el-col>
                  </el-row>
                </el-form-item>
              </div>
              <div v-else-if="typeItem[key] === '复选'">
                <el-form-item  v-for="(data,dIndex) in item" :key="dIndex" :label="(data.index) + '.' + data.tg" :ref="'#task-'+ data.tkid">
                  <el-checkbox-group v-model="multipleChoiceAnswer[data.tkid]">
                    <el-checkbox v-for="(cData,cIndex) in data.xx" :key="cIndex" :label="cData.key" :name="cData.tkid">{{cData.key + '.' + cData.value}}</el-checkbox>
                  </el-checkbox-group>
                  <el-row>
                    <el-col :span="24">
                      <div class="standardAnswer">标准答案：{{data.da}}</div>
                    </el-col>
                  </el-row>
                </el-form-item>
              </div>
              <div v-else-if="typeItem[key] === '判断'">
                <el-form-item  v-for="(data,dIndex) in item" :key="dIndex" :label="(data.index) + '.' + data.tg" :ref="'#task-'+ data.tkid">
                  <el-radio-group v-model="singleAnswer[data.tkid]">
                    <el-radio label="1">正确</el-radio>
                    <el-radio label="2">错误</el-radio>
                  </el-radio-group>
                  <el-row>
                    <el-col :span="24">
                      <div class="standardAnswer">标准答案：{{data.da}}</div>
                    </el-col>
                  </el-row>
                </el-form-item>
              </div>
              <div v-else-if="typeItem[key] === '填空'">
                <el-form-item  v-for="(data,dIndex) in item" :key="dIndex" :label="(data.index) + '.' + data.tg" :ref="'#task-'+ data.tkid">
                  <el-input class="customStyle" v-model="singleAnswer[data.tkid]"></el-input>
                  <el-row>
                    <el-col :span="24">
                      <div class="standardAnswer">标准答案：{{data.da}}</div>
                    </el-col>
                  </el-row>
                </el-form-item>
              </div>
              <div v-else-if="typeItem[key] === '主观'">
                <el-form-item  v-for="(data,dIndex) in item" :key="dIndex" :label="(data.index) + '.' + data.tg" :ref="'#task-'+ data.tkid">
                  <el-input class="customStyle" type="textarea" v-model="singleAnswer[data.tkid]"></el-input>
                  <el-row>
                    <el-col :span="24">
                      <div class="standardAnswer">标准答案：{{data.da}}</div>
                    </el-col>
                  </el-row>
                </el-form-item>
              </div>
            </el-card>
          </el-form>
        </div>
      </el-col>
    </el-row>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>

import {getSubjectByPaper} from "@/api/etp/exam/exam";

export default {
  name: "WrongQuestionBook",
  data() {
    return {
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      testQuestionDetails: {},
      typeItem:{},
      //单选答案
      singleAnswer: {},
      //多选答案
      multipleChoiceAnswer: {},
      //所有答案
      answerCollection:{},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList(){
      //添加分页参数
      getSubjectByPaper({sjid:40}).then(res=>{
        if (res && res.data){
          let index = 1;
          for (const datum of res.data) {
            const { lx,lxbq,xx,tkid } = datum;
            if (xx && xx !== ""){
              datum.xx = JSON.parse(xx);
            }
            this.typeItem[lx] = lxbq;
            if (!this.testQuestionDetails[lx]){
              this.testQuestionDetails[lx] = [];
            }
            /**
             * 1: "单选"
             * 2: "复选"
             * 3: "判断"
             * 4: "填空"
             * 5: "主观"
             */
            if (lx === "2"){
              this.$set(this.multipleChoiceAnswer,tkid,[]);
            }
            datum.index = index;
            index++;
            this.testQuestionDetails[lx].push(datum);
          }
          this.$forceUpdate();
          console.log(this.testQuestionDetails,"testQuestionDetails")
          console.log(this.typeItem,"typeItem")
        }
      })
    }
  },
  watch: {
    singleAnswer: {
      handler (val) {
        Object.assign(this.answerCollection,val);
      },
      deep: true,
      immediate: true
    },
    multipleChoiceAnswer: {
      handler (val) {
        Object.assign(this.answerCollection,val);
      },
      deep: true,
      immediate: true
    }
  }
};
</script>
<style scoped lang="scss">
.app-container-right{
  border: 1px rgba(0, 0, 0, 0.08) solid;
  .standardAnswer{
    margin-top: 6px;
  }
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
      border-radius: 0;
      .el-card__header{
        background-color: rgba(250,250,250,1);
      }
    }
  }
}
</style>
