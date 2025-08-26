<template>
<div class="app-container con-div">
  <el-tabs v-model="activeName"
           @tab-click="handleClick">
    <el-tab-pane label="统计分析" name="first">
      <div class="con-title">
        <span>{{ questionForm.wjmc }} </span>
        <span style="color: #ef1111" v-if="Date.parse(questionForm.endTime) < new Date()">(已结束)</span>
        <span style="color: #cea424" v-else-if="Date.parse(questionForm.beginTime) > new Date()">(未开始)</span>
        <span style="color: green" v-else>(正在收集)</span>
      </div>
      <div class="con-tags">
        <el-row style="pointer-events: none;">
          <el-col :span="8">
            <label>标签：</label>
            <span v-if="questionForm.tags==null">无</span>
            <span v-else><el-tag v-for="item in (questionForm.tags||'').split(',')">{{ item }}</el-tag></span>
          </el-col>
          <el-col :span="4"><el-checkbox v-model="questionForm.sfnm">匿名收集</el-checkbox></el-col>
          <el-col :span="5"><label>开放程度：</label><span>{{kfcdType(questionForm.kfcd)}}</span></el-col>
          <el-col :span="7"><label>截止时间：</label><span>{{questionForm.beginTime}} ~ {{ questionForm.endTime }}</span></el-col>
        </el-row>
        <el-divider></el-divider>
        <div class="con-analysis">截止 {{ nowDate }} 已收集 {{alltotal}} 份<el-button size="small" @click="refreshFirstData()">刷新</el-button></div>
      </div>
      <div v-show="alltotal<1" class="con-empty">
        <dataEmpty ></dataEmpty>
        <div style="margin-top: 30px;color: #5f5f5f">暂无数据</div>
      </div>
      <div v-show="alltotal>0" class="con-item" v-if="questionForm.nr && questionForm.nr.length > 0"
           v-for="(item,index) in questionForm.nr">
        <div>
          <p>{{ index +1 }} 、{{ item.tg }} <span style="color: green">【{{ typeDel(item.type)}}】</span></p>
          <div :class="`fx-table table${index}`">
<!--            非问答题表格 -->
            <el-table
              :data="tableData[`${item.id}`]"
              v-if="item.type!='3' && showFlag[`${item.id}`]==1"
              style="width: 100%">
              <el-table-column
                prop="optionName"
                label="选项"
                width="580">
              </el-table-column>
              <el-table-column
                prop="num"
                label="计数">
              </el-table-column>
              <el-table-column
                prop="bl"
                label="比例">
                <template slot-scope="scope">
                  {{getPercent(scope.row)}}
                </template>
              </el-table-column>
            </el-table>

<!--            问答题表格 -->
            <el-table
              :data="tableData[`${item.id}`]"
              v-if="item.type=='3'"
              style="width: 100%">
              <el-table-column
                type="index"
                label="序号"
                width="100">
              </el-table-column>
              <el-table-column
                prop="wdda"
                label="答案">
              </el-table-column>
              <el-table-column
                prop="num"
                label="计数">
              </el-table-column>
            </el-table>
          </div>
          <pagination
            v-show="(wdTotal[`${item.id}`]==undefined?0:wdTotal[`${item.id}`])>0"
            :total="wdTotal[`${item.id}`]==undefined?0:wdTotal[`${item.id}`]"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getWdAnalysis(id,item.id)"
          />


<!--          分析饼图   -->
          <div :class="`fx-pie pie${index}`" v-if="item.type!='3' && showFlag[`${item.id}`]==2">
            <pie-chart :class-name="`pie_${index}`" :data="pieData[`${item.id}`]" :width="'80%'"> </pie-chart>
          </div>

<!--          分析柱状图   -->
          <div :class="`fx-bar bar${index}`" v-if="item.type!='3' && showFlag[`${item.id}`]==3">
            <bar-chart :class-name="`bar_${index}`" :data="barData[`${item.id}`]" :width="'80%'"></bar-chart>
          </div>

          <div class="btn-group" v-if="item.type !=3">
            <el-button @click="getTableData(item.id)">表格</el-button>
            <el-button @click="getPieData(item.id)">饼状图</el-button>
            <el-button @click="getBarData(item.id)">柱状图</el-button>
          </div>
        </div>
      </div>
    </el-tab-pane>

<!--     第二页            -->
    <el-tab-pane label="详细" name="second">
      <div class="con-tags" style="margin-top: 0px;">
        <div class="con-analysis">截止 {{ nowDate }} 已收集 {{alltotal}} 份<el-button size="small" @click="refreshSecondData">刷新</el-button></div>
        <div v-show="alltotal<1" class="con-empty">
          <dataEmpty ></dataEmpty>
          <div style="margin-top: 30px;color: #5f5f5f">暂无数据</div>
        </div>
        <div v-show="alltotal>0">
<!--          <div>当前第<i class="el-icon-caret-left" @click="preData"></i>-->
<!--            <el-input @change="changePage" v-model="params.pageNum" style="width:50px"/>-->
<!--            <i class="el-icon-caret-right" @click="afterData"></i>份-->
<!--            <span class="more" @click="getMoreStudent">...查看更多</span>-->
<!--          </div>-->
          <span class="more" @click="handleQuery">...查看更多</span>
          <div class="con-title">
            <span>{{ questionnaireData.wjmc }} </span>
            <span style="color: #ef1111" v-if="Date.parse(questionnaireData.endTime) < new Date()">(已结束)</span>
            <span style="color: #cea424" v-else-if="Date.parse(questionnaireData.beginTime) > new Date()">(未开始)</span>
            <span style="color: green" v-else>(正在收集)</span>
            <div class="con-answer">提交者:
              <span v-if="!questionnaireData.sfnm">{{ questionnaireData.nickName }}</span>
              <span v-else>*****</span>
              <span>{{questionnaireData.updateTime}}</span>
            </div>
          </div>

          <el-row style="margin-top: 30px;pointer-events: none">
            <el-col :span="8">
              <label>标签：</label>
              <span v-if="questionnaireData.tags==null">无</span>
              <span v-else><el-tag v-for="item in (questionnaireData.tags||'').split(',')">{{ item }}</el-tag></span>
            </el-col>
            <el-col :span="4"><el-checkbox v-model="questionnaireData.sfnm">匿名收集</el-checkbox></el-col>
            <el-col :span="5"><label>开放程度：</label><span>{{kfcdType(questionnaireData.kfcd)}}</span></el-col>
            <el-col :span="7"><label>截止时间：</label><span>{{questionnaireData.beginTime}} ~ {{ questionnaireData.endTime }}</span></el-col>
          </el-row>
          <el-divider></el-divider>
          <!--        问卷内容回显-->
          <div v-if="questionnaireData.nr && questionnaireData.nr.length > 0"
               v-for="(item,index) in questionnaireData.nr">
            <p style="font-size: 18px;margin-left: 15px;margin-top: 15px">
              {{ index +1 }} 、{{ item.tg }} <span style="color: green">【{{ typeDel(item.type)}}】</span> </p>
            <div class="t-content">
              <template v-if="(item.type == '0') && item.xxList.length > 0">
                <el-radio-group v-model="item.da">
                  <el-radio v-for="t in item.xxList" :label="t.key" :key="t.key" >
                    {{ t.key }} 、{{ t.value }}
                  </el-radio>
                </el-radio-group>
              </template>
              <template v-if="(item.type == '1') && item.xxList.length > 0">
                <el-checkbox-group v-model="item.da">
                  <el-checkbox v-for="t in item.xxList" :label="t.key" :key="t.key">{{ t.key }} 、{{ t.value }}</el-checkbox>
                </el-checkbox-group>
              </template>
              <template v-if="(item.type == '2' || item.type == '3')">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 4}"
                  placeholder="请输入内容"
                  v-model="item.da">
                </el-input>
              </template>
            </div>
          </div>
        </div>
      </div>

      <el-dialog
        :title="dialog.title"
        :close-on-click-modal="false"
        :visible.sync="dialog.visible"
        width="50%">
        <el-form :model="dialog.queryParams" ref="queryForm" :inline="false"  label-width="90px">
          <el-form-item label="姓名:" prop="nickName" v-if="!questionnaireData.sfnm" style="width:330px">
            <el-input
              v-model="dialog.queryParams.nickName"
              placeholder="请输入名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="时间范围:">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button style="float: right" type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="dialog.tableData"
          v-loading="dialog.loading"
          style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="70">
          </el-table-column>
          <el-table-column
            prop="nickName"
            label="姓名">
            <template slot-scope="scope">
              <span v-if="!questionnaireData.sfnm">{{scope.row.nickName}}</span>
              <span v-if="questionnaireData.sfnm">******</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="updateTime"
            label="填写时间">
          </el-table-column>
          <el-table-column
            label="操作"
            width="180">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="getView(scope)"
              >查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="dialog.total>0"
          :total="dialog.total"
          :page.sync="dialog.queryParams.pageNum"
          :limit.sync="dialog.queryParams.pageSize"
          @pagination="getMoreStudent"
        />
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialog.visible = false">关 闭</el-button>
        </span>
      </el-dialog>

    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import { getQuestionnaire, totalByWjid, listQuestionnaireStudent,choseAnalysis,wdAnalysis } from '@/api/etp/questionnaire'
import dataEmpty from "@/components/empty"
import pieChart from '@/views/etp/questionnaire/components/pieChart'
import barChart from '@/views/etp/questionnaire/components/barChart'
export default {
  name: 'questionnaireDetail',
  components:{
    dataEmpty,
    pieChart,
    barChart
  },
  created() {
    this.id = this.$route.query.id;
    this.getQuestion(this.id)
    this.nowDate = this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss')
    this.getTotalByWjid(this.id)
    this.getChoseAnalysis(this.id)
  },
  data(){
    return{
      id: null,
      activeName: 'first',
      questionForm: {},
      nowDate: null,//当前时间
      alltotal: 0,//收集总试卷数
      //tab1 统计table 数据
      tableData:{},//{tmid:[选项list],tmid:[选项list]}
      wdTotal:{}, // 用于记录问答题 数据总数
      queryParams:{
        wjid:null,
        tmid:null,
        pageNum: 1,
        pageSize: 10,
      },
      showFlag:{},//展示标记
      pieData:{},//饼状图
      barData:{},//柱状图

      questionnaireData:{},
      params:{
        id: null,
        xyid: null,
        // pageNum: 1,
        // pageSize: 1,
      },
      total: 0,//分页用

      dialog:{
        title:"提示",
        visible: false,
        loading: false,
        tableData: [],
        total: 0,
        queryParams:{
          id:null,
          nickName:null,
          pageNum: 1,
          pageSize: 10,
        },
      },
      dateRange: null
    }
  },
  methods:{
    handleClick(tab, event) {
      if(tab.name=='second'){
        this.params.xyid = null
        this.getList(this.params)
        // this.handleQuery()
      }
      this.activeName = tab.name
    },
    typeDel(type){
      let t = '';
      switch (type) {
        case '0':
          t = '单选题';break;
        case '1':
          t = '复选题';break;
        case '2':
          t = '填空题';break;
        case '3':
          t = '问答题';break;
        default:
          break;
      }
      return t;
    },
    kfcdType(type){
      let t = '';
      switch (type) {
        case '0':
          t = '完全公开';break;
        case '1':
          t = '指定用户';break;
        case '2':
          t = '不公开';break;
        default:
          break;
      }
      return t;
    },
    //tab1 数据回显
    getQuestion(id){
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      getQuestionnaire(id).then(res => {
        this.questionForm = res.data;
        if(res.data.sfnm == 1){
          this.questionForm.sfnm = false
        }else{
          this.questionForm.sfnm = true
        }
        if(this.questionForm.nr){
          let nr = JSON.parse(this.questionForm.nr)
          this.$set(this.questionForm,"nr",nr)
        }
        //取 问答题数据
        this.questionForm.nr.forEach(item=>{
          if(item.type =='3'){
            this.getWdAnalysis(this.id,item.id)
          }
        })
        loading.close()
      })
    },

    //收集总数
    getTotalByWjid(id){
      totalByWjid(id).then(res => {
        this.alltotal = res.data
      })
    },
    //问卷各个选择题 试题的分析
    getChoseAnalysis(id){
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      choseAnalysis(id).then(res => {
        let data = res.data
        this.tableData = data
        //key = tmid
        for(let key in data){
          this.$set(this.showFlag,`${key}`,1)
          let xxlist = data[key]
          let pie_data = []
          let bar_label = []
          let bar_num = []
          //饼状图数据组合
          xxlist.forEach(item=>{
            let obj = {}
            obj.value = item.num
            obj.name = item.optionName
            pie_data.push(obj)

            bar_label.push(item.optionName)
            bar_num.push(item.num)
          })
          this.$set(this.pieData,key,pie_data)
          this.$set(this.barData,key, { dataLabel:bar_label,dataNum: bar_num})

        }
        loading.close()
      })
    },

    //问答题分析
    getWdAnalysis(wjid,tmid){
      this.queryParams.wjid = wjid
      this.queryParams.tmid = tmid
      wdAnalysis(this.queryParams).then(res => {
        debugger
        this.$set(this.wdTotal,tmid,res.total)
        this.$set(this.tableData,tmid,res.rows)
      })
    },
    //刷新
    refreshFirstData(){
      this.nowDate = this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss')
      this.getQuestion(this.id)
      this.getTotalByWjid(this.id)
      this.getChoseAnalysis(this.id)

    },
    //表格图
    getTableData(tmid){
      this.showFlag[tmid] = 1
    },
    //饼状图
    getPieData(tmid){
      this.showFlag[tmid] = 2
    },
    //柱状图
    getBarData(tmid){
      this.showFlag[tmid] = 3
    },

    refreshSecondData(){
      this.nowDate = this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss')
      this.getTotalByWjid(this.id)
      // this.getList(this.params)
    },
    reset(){
      this.dialog.queryParams.id = null
      this.dialog.queryParams.nickName = null
      this.dialog.queryParams.pageNum = 1;
    },

    //tab2 获取数据
    handleQuery() {
      this.reset()
      this.getMoreStudent();
    },

    //查看更多
    getMoreStudent(){
      this.dialog.loading = true;
      this.dialog.title = this.questionForm.wjmc
      this.dialog.visible = true
      this.dialog.queryParams.id = this.id
      if(this.questionnaireData.sfnm){
        this.dialog.queryParams.nickName=null
      }
      listQuestionnaireStudent(this.addDateRange(this.dialog.queryParams, this.dateRange)).then(response => {
        this.dialog.tableData = response.rows;
        this.dialog.total = response.total;
        this.dialog.loading = false;
      });
    },
    //查看详情
    getView(scope){
      this.dialog.visible=false
      this.params.id = this.id
      this.params.xyid = scope.row.xyid
      this.activeName = "second"
      // let pageData = this.dialog.queryParams
      // this.params.pageNum = (pageData.pageNum-1) *(pageData.pageSize) + scope.$index +1
      this.getList(this.params)

    },

    getList(params) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      params.id = this.id
      listQuestionnaireStudent(params).then(response => {

        if(response.rows.length>0){
          this.questionnaireData = response.rows[0];
          this.total = response.total;
          if(this.questionnaireData.nr){
            this.questionnaireData.nr = JSON.parse(this.questionnaireData.nr)
          }
          //复选题答案回显
          this.questionnaireData.nr.forEach((item,index) => {
            if(item.type=='1'){
              item.da = item.da.split(',')
            }
          })
          if(this.questionnaireData.sfnm == 1){
            this.questionnaireData.sfnm = false
          }else{
            this.questionnaireData.sfnm = true
          }
        }
        loading.close()
      });
    },

    //前一条数据
   /* preData(){
      let pageNum = this.params.pageNum
      if(pageNum>1){
        this.$set(this.params,"pageNum",--pageNum)
      }else{
        this.$set(this.params,"pageNum",1)
      }
      this.getList(this.params)
    },
    //后一条数据
    afterData(){
      let pageNum = this.params.pageNum
      if(pageNum <this.alltotal){
        this.$set(this.params,"pageNum",++pageNum)
      }else{
        this.$set(this.params,"pageNum",this.alltotal)
      }
      this.getList(this.params)
    },
    //页码校验
    changePage(){
      let pageNum = this.params.pageNum
      var regu = "^[0-9]*$";
      var re = new RegExp(regu);
      if(!re.test(pageNum)){
        this.$message("页码必需是数字")
        this.$set(this.params,"pageNum",1)
      }else if(pageNum>this.alltotal){
        this.$set(this.params,"pageNum",this.alltotal)
      }else if(pageNum<1){
        this.$set(this.params,"pageNum",1)
      }
      this.getList(this.params)
    },*/

    getPercent(row){
      let num = row.num
      let dataArr = this.tableData[row.tmid]
      let numAll = 0
      for(let key in dataArr){
        numAll+=dataArr[key].num
      }
      return numAll <= 0 ? "0%" : (Math.round(num / numAll * 10000) / 100.00)+"%";
    }
  }
}
</script>

<style scoped lang="scss">
.con-div{
  padding: 0px 150px 20px;
  color: #1c1c1c;
}
.con-title{
  font-size: 20px;
  font-weight: 600;
  text-align: center;

}
.con-tags{
  margin-top: 40px;
  label{
    font-weight: 500;
    color: #5f5f5f;
  }
  .el-row{
    font-size: 12px;
    line-height: 32px;
  }
  ::v-deep.el-checkbox__label{
    font-size: 12px;
  }
}

::v-deep.el-divider--horizontal{
  margin: 12px 0;
}
.con-item{
  padding: 0px 50px;
  p{
    font-size: 18px;
    margin-left: 15px;
    margin-top: 15px
  }
}
.con-analysis{
  color: #5f5f5f;
  button{
    margin-left: 15px;
  }
}
.el-tag{
  margin-right: 6px;
}
::v-deep .el-radio,.el-checkbox{
  display: block;
  line-height: 32px;
  white-space: normal;
  margin-right: 0;
}
.t-content{
  margin-left: 40px;
  pointer-events: none;
}
i{
  cursor: pointer;
}
.con-empty{
  display: flex;
  align-items: center;
  justify-content: center;
  vertical-align: middle;
  flex-direction: column;
}
.btn-group{
  margin: 18px 18px 28px;
  text-align: center;
}
#pane-first{
  margin-top: 15px;
}
.fx-pie{

}
.pagination-container{
  margin-bottom: 20px;
}
.more{
  color: #1c84c6;
  font-size: 16px;
  cursor: pointer;
}
.con-answer{
  font-weight: 500;
  font-size: 13px;
  margin-top: 10px;
  span:nth-of-type(2){
    margin-left: 10px;
  }
}
::v-deep.el-form-item {
  margin-bottom: 8px;
}
</style>
