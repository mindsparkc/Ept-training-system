<template>
  <div class="app-container">
    <el-form label-width="90px">
      <el-card>
        <el-row>
          <el-col :span="24" style="height: 36px;">
            <span style="margin-left: 7px;float: left;font-size: 25px; font-weight: bolder">{{this.examInfo.mc}}
              <a v-if="examInfo.examStatus=='0'" style="color: #909399">(未开始)</a>
              <a v-if="examInfo.examStatus=='1'" style="color: #67c23a">(进行中)</a>
              <a v-if="examInfo.examStatus=='2'" style="color: #f56c6c">(已结束)</a>
            </span>
          </el-col>
        </el-row>
        <el-divider style="margin: 5px 0"/>
        <el-row>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="总分：">{{this.examInfo.zfs}} 分</el-form-item>
          </el-col>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="平均分：">{{this.examInfo.averageScore}} 分</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="答题时长：">{{this.examInfo.djsc}} 分钟</el-form-item>
          </el-col>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="最高分：">{{this.examInfo.topScore}} 分</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="应考人数：">{{this.examInfo.peopleNum}} 人</el-form-item>
          </el-col>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="最低分：" prop="lastScore">{{this.examInfo.lastScore}} 分</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" style="height: 36px">
            <el-form-item label="考试时间：">
              {{this.examInfo.kskssj}}~{{this.examInfo.ksjssj}}
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>
    <p></p>
    <div style="border:3px">
      <el-container>
        <el-header style="height: 50px">
          <el-col :span="1.5">
            <div style="font-size: 20px;font-weight: bolder ">考试成绩分析</div>
          </el-col>
        </el-header>
        <el-main style="padding: 2px">
          <el-card>
          <el-table v-loading="loading"  ref="secondTable" :data="examinationList"
                    row-key="deptId"
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                    default-expand-all highlight-current-row
                    >
<!--            @current-change="handleCurrentChange2"-->
          <el-table-column :show-overflow-tooltip="true" align="center" label="部门名称" prop="deptName" />
          <el-table-column align="center" label="应考人数" prop="peopleNum" >
            <template slot-scope="scope">
              {{scope.row.peopleNum == null?'无':scope.row.peopleNum+'人'}}
            </template>
          </el-table-column>
          <el-table-column align="center" label="实考人数" prop="practiceNum" >
            <template slot-scope="scope">
              {{scope.row.practiceNum == null?'无':scope.row.practiceNum+'人'}}
            </template>
          </el-table-column>
            <el-table-column align="center" label="参考率" prop="ratio" >
              <template slot-scope="scope">
                {{scope.row.ratio == null?'无':scope.row.ratio*100+'%'}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="最高分" prop="topScore" >
              <template slot-scope="scope">
                {{scope.row.topScore == null?'无':scope.row.topScore+'分'}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="最低分" prop="lastScore" >
              <template slot-scope="scope">
                {{scope.row.lastScore == null?'无':scope.row.lastScore+'分'}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="平均分" prop="averageScore" >
              <template slot-scope="scope">
                {{scope.row.averageScore == null?'无':scope.row.averageScore+'分'}}
              </template>
            </el-table-column>
            <el-table-column align="center" label="平均时长" prop="averageTime" >
              <template slot-scope="scope">
                {{scope.row.averageTime == null?'无':scope.row.averageTime+'分钟'}}
              </template>
            </el-table-column>
        </el-table>
          <pagination style="height: 50px"
            v-show="total>0"
            :limit.sync="queryParams.pageSize"
            :page.sync="queryParams.pageNum"
            :total="total"
            @pagination="getList"
          /></el-card></el-main>
        <el-container>
          <el-aside width="860px"  >
            <div class="test2" style="width:810px;height: 400px;">
              <div id="myChart1" style="width:100%;height:100%;"></div>
            </div>
          </el-aside>
          <el-main style="color: #eef1f6;height: 416px;width: 100%;">
            <el-card style="height:370px">
              <div id="myChart2" style="width:100%;height:380px;"></div>
            </el-card>
          </el-main>
        </el-container>
        <el-header style="height: 50px">
          <el-col :span="1.5">
            <div style="font-size: 20px;font-weight: bolder ">试题分析</div>
          </el-col>
        </el-header>
        <el-main style="padding: 2px">
          <el-card>
            <el-table v-loading="loading" ref="expandTable" :data="subjectList" :expand-row-keys="expands"
                :row-key="getRowKeys" @expand-change="expandSelelct" @row-click="clickRow">
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item label="选项：" style="width: 100%">
                      <template v-if="props.row.lx!=='3'">
                        <el-row v-for="(item) in JSON.parse(props.row.xx)">
                          <el-col :span="24">
                            <span>{{item.key}}、{{item.value}}</span>
                          </el-col>
                          <el-col :span="24">
                            <el-progress v-if="item.key === index && props.row.lx==='1'" v-for="(value,index) in subjectData.dxArr" :percentage="value" :format="format" stroke-width='10'/>
                          </el-col>
                          <el-col :span="24">
                            <el-progress v-if="item.key === index && props.row.lx==='2'" v-for="(value,index) in subjectData.fxArr" :percentage="value" :format="format" stroke-width='10'/>
                          </el-col>
                        </el-row>
                      </template>
                      <template v-else>
                        <el-row>
                          <el-col :span="24">
                            <span>1、正确</span>
                          </el-col>
                          <el-col :span="24">
                            <el-progress v-if="index == '1'" v-for="(value,index) in subjectData.pdArr" :percentage="value" :format="format" stroke-width='10'/>
                          </el-col>
                          <el-col :span="24">
                            <span>2、错误</span>
                          </el-col>
                          <el-col :span="24">
                            <el-progress v-if="index == '0'" v-for="(value,index) in subjectData.pdArr" :percentage="value" :format="format" stroke-width='10'/>
                          </el-col>
                        </el-row>
                      </template>
                    </el-form-item>
                    <el-form-item label="正确答案：">
                      <span v-if="props.row.lx!=='3'">{{props.row.da}}</span>
                      <span v-else>{{props.row.da=='0'?'错误':'正确'}}</span>
                    </el-form-item>
                    <el-form-item label="解析：">
                      <span>{{props.row.jx==undefined||props.row.jx==null?'无':props.row.jx}}</span>
                    </el-form-item>
                  </el-form>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" align="center" label="题目ID" prop="id" width="100px" />
              <el-table-column label="题目类型：" prop="lx" width="100px">
                <template slot-scope="scope">
                  <span>{{scope.row.lx==='1'?'单选题':scope.row.lx==='2'?'复选题':'判断题'}}</span>
                </template>
              </el-table-column>
              <el-table-column align="left" label="题干" prop="tg"/>
            </el-table>
          </el-card>
        </el-main>
      </el-container>
    </div>
  </div>
</template>
<script>
import { listExamination, getExamination, getSubjectInfo, getSubjectAnalysis} from "@/api/etp/analysis/examinationScoreAnalysis";
import { listTagRelate } from "@/api/etp/tag/tag";
import echarts from "echarts";
import { now } from 'moment'
export default {
  name: "Examination",
  data() {
    return {
      gridData:[],
      dialogTableVisible:false,
      // 遮罩层
      loading: false,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 考试表格数据
      examinationList: [],
      //试题数据
      subjectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 发布状态：0-暂存，1-发布字典
      fbOptions: [],
      // 开放程度：完全公开-0，限定用户-1字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ksid:'',
      },

      // 表单参数
      form: {},
      tagOptions:[],
      queryTag:[],
      currentRow: null,
      myChart1: '',
      myChart2: '',
      //图表参数
      opinion: [],//饼状图数据
      djDate: ['product'],//柱状图按钮数据
      djValue: [],//柱状图展示数据
      examInfo: [],//考试详细信息
      //试题分析参数
      subjectData: [],
      expands:[],//要展开的行，值为row的key值
      getRowKeys(row){
        return row.id
      },
      dxArr: '',
      fxArr: '',
      pdArr: '',
      fullPath:'',
    }
  },
  created() {
    const ksid = this.$route.query.id;
    const sjid = this.$route.query.sjid;
    console.log("ksid="+ksid,"sjid="+sjid);
    this.queryParams.ksid = ksid;
    this.getList();
    this.getExamInfo(ksid);
    this.getSubject(sjid);
  },
  beforeRouteUpdate(to,from,next){
    console.log("to="+to);
    console.log("from="+from);
    next();
  },

  activated(){

      const ksid = this.$route.query.id;
      const sjid = this.$route.query.sjid;
      console.log("ksid="+ksid,"sjid="+sjid);
      this.queryParams.ksid = ksid;
      this.getList();
      this.getExamInfo(ksid);
      this.getSubject(sjid);

  },

  methods: {
    //进度条后面展示的文字
    format(percentage){
      return percentage==undefined?0:percentage+'人次';
    },

    //每次只展示一行
    expandSelelct(row,expandedRows){
      //当没有行展开时
      if(expandedRows.length>0){
        //清空展开行数
        this.expands = [];
        //把当前行数id放到展开行数中
        this.expands.push(row.id);
        this.getSubjectAnalysisInfo(row.id,row.lx)
      }else{
        //清空展开行数
        this.expands = [];
      }
    },
    //点击某行任意位置展开或收起行
    clickRow(row){
      this.$refs.expandTable.toggleRowExpansion(row);
    },

    /** 试题分析详情查询 */
    getSubjectAnalysisInfo(id,lx){
      getSubjectAnalysis(id,this.queryParams.ksid,lx).then(res =>{
        if(res.code == 200){
          console.log(res);
          this.subjectData = res.data;
        }
      })
    },


    /** 试题查询 */
    getSubject(sjid){
      getSubjectInfo(sjid).then( res =>{
        if(res.code == 200){
          this.subjectList = res.data;
        }
      })
    },

    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExamination(this.queryParams).then(response => {
        this.opinion = [];
        this.djDate = ['product'];
        this.djValue = [];
        this.examinationList = this.handleTree(response.rows,"deptId");
        let rows = response.rows;
        for (let i = 0; i < rows.length; i++) {
          let data = {};
          data['value'] = rows[i].practiceNum==null?0:rows[i].practiceNum;
          data['name'] = rows[i].deptName
          this.opinion.push(data);
          let data1 = [];
          data1.push(rows[i].deptName);
          let cj = rows[i].params
          // this.series = rows[i].deptName
          //解析考试等级及对应人数
          for (const cjKey in cj) {
            this.djDate.push(cjKey);
            data1.push(cj[cjKey]==undefined?0:cj[cjKey]);
          }
          this.djValue.push(data1);
        }
        this.drawLine1();
        this.drawLine2();
        this.loading = false;
      });
    },
    getTags(){
      listTagRelate({modelType:"KS"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    // 开放程度：完全公开-0，限定用户-1字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
    },
    //画出柱状图
    drawLine1 () {
      let data = [];
      data.push(this.djDate);
      for (const dataKey in this.djValue) {
        data.push(this.djValue[dataKey])
      }
      this.myChart1 = echarts.init(document.getElementById('myChart1'))
      this.myChart1.setOption({
        title: {
          text: '部门及格率分析'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: this.djDate
        },
        dataset: {
          source: data
        },
        xAxis: { type: 'category', axisLabel:{interval:0}, rotate:-25 },
        yAxis: {},
        series: [{ type: 'bar' }, { type: 'bar' }]
      })
    },
    drawLine2 () {
      // 基于准备好的dom，初始化echarts实例
      this.myChart2 = echarts.init(document.getElementById('myChart2'))
      // 绘制图表
      this.myChart2.setOption({
        title: {
          text: '各部门考试人数占比',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'horizontal',
          bottom: 'bottom',
          data: this.opinion,
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: this.opinion,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    getExamInfo(val) {
      //获取行数据，根据部门ID，考试ID，查询出该部门所有参加考试人员的考试详情
      getExamination(val).then(res => {
        this.examInfo = res.data
      })
    },

    /** 查看成绩*/
    // handleScore(row){
    //   debugger
    //   getExamination(row).then(res => {
    //     this.dialogTableVisible = true;
    //     this.gridData = res.data.sysUsers
    //   })
    // },

    rateFormate(row){
      return  (row.rate * 100).toFixed(0) + "%"
      // parseFloat(row.rate) * 100 + "%"
      // (Number(row.rate).toFixed(2))
    },

  }
};
</script>
<style>
.el-header {
  /*background-color: #B3C0D1;*/
  color: #333;
  text-align: left;
  line-height: 40px;
}

.el-aside {
  /*background-color: #D3DCE6;*/
  /*color: #333;*/
  text-align: center;
  line-height: 220px;
}

.el-main {
  background-color: #eef1f6;
  color: #333;
  text-align: center;
  /*line-height: 220px;*/
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

</style>
