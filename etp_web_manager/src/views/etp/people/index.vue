<template>
  <div class="app-container custom_box">
    <div>
      <el-button type="success" @click="chosePeople">选择人员和考试</el-button>
      <!-- <el-button type="success" @click="choseExamination">选择考试</el-button> -->
    </div>
    <div ref="charts" style="height: 500px" class="charts"></div>
    <!-- 选择人员 -->
    <el-dialog
      :title="chosePeopleTitle"
      :visible.sync="chosePeopleOpen"
      :close-on-click-modal="false"
      width="1000px"
      append-to-body
    >
      <div style="overflow: hidden">
        <div
          style="
            float: left;
            width: 25%;
            border-right: 1px solid #cccccc;
            height: 100%;
          "
        >
          <div style="width: 90%; margin: 0 auto">
            <el-input placeholder="输入关键字进行过滤" v-model="filterText">
            </el-input>
            <el-tree
              :data="deptOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              default-expand-all
              @node-click="handleNodeClick"
            />
          </div>
        </div>
        <div style="float: right; width: 74%; height: 100%">
          <el-form
            :model="peopleQueryParams"
            ref="peopleQueryForm"
            :inline="true"
            v-show="showSearch"
            label-width="68px"
          >
            <el-input
              placeholder="请输入人员名称"
              clearable
              v-model="peopleQueryParams.userName"
              style="width: 300px; float: right"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="peopleHandleQuery"
              ></el-button>
            </el-input>
            <div style="clear: both"></div>
          </el-form>
          <el-table
            v-loading="peopleListloading"
            border
            :data="peopleList"
            @selection-change="handleSelectionChange"
            style="margin-top: 10px"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column
              label="序号"
              type="index"
              width="55"
              align="center"
            />
            <el-table-column label="账号" align="center" prop="userName" />
            <el-table-column label="用户名" align="center" prop="nickName" />
            <el-table-column label="部门" align="center" prop="dept.deptName" />
            <el-table-column
              label="创建时间"
              align="center"
              prop="createTime"
            />
            <el-table-column label="状态" align="center" prop="status" >
              <template slot-scope="scope">
                <font  v-if="scope.row.status=== '0'">正常</font>
                <font  v-if="scope.row.status=== '1'">停用</font>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="peopletotal > 0"
            :total="peopletotal"
            :page.sync="peopleQueryParams.pageNum"
            :limit.sync="peopleQueryParams.pageSize"
            @pagination="getPeopleList"
          />
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPeople">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 选择考试 -->
    <el-dialog
      :title="choseexaminationTitle"
      :visible.sync="choseexaminationOpen"

      width="800px"
      append-to-body
    >
    <el-form
            :model="examinationQueryParams"
            ref="examinationQueryForm"
            :inline="true"
            v-show="showexaminationSearch"
            label-width="68px"
          >
            <el-input
              placeholder="请输入试卷名称"
              clearable
              v-model="examinationQueryParams.mc"
              style="width: 300px; float: right"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="examinationHandleQuery"
              ></el-button>
            </el-input>
            <div style="clear: both"></div>
          </el-form>
          <el-table
            v-loading="examinationListloading"
            border
            :data="examinationList"
            @selection-change="handleSelectionChangeTwo"
            style="margin-top: 10px"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column
              label="序号"
              type="index"
              width="55"
              align="center"
            />
            <el-table-column label="试卷名称" align="center" prop="mc" />
            <el-table-column
              label="创建时间"
              align="center"
              prop="createTime"
            />
          </el-table>
          <pagination
            v-show="examinationtotal > 0"
            :total="examinationtotal"
            :page.sync="examinationQueryParams.pageNum"
            :limit.sync="examinationQueryParams.pageSize"
            @pagination="getexaminationList"
          />
        <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitexamination">确 定</el-button>
        <el-button @click="examinationCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  exportUser,
  resetUserPwd,
  changeUserStatus,
  importTemplate,
} from "@/api/system/user";
import { listexamination, getexamination, delexamination, addexamination, updateexamination, exportexamination } from "@/api/etp/examination/examination";
import { listExamination, getExamination, delExamination, addExamination, updateExamination, updateTExaminationFb } from "@/api/etp/examination/examination";
import { processingPersonnelAnalyzeData } from "@/api/etp/analysis/peopleAnalysis";
import { listGroup } from "@/api/system/userGroup";
import { getToken } from "@/utils/auth";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import echarts from "echarts";
export default {
  name: "peopleAnalysis",
  components: { Treeselect },
  data() {
    return {
      lineEcharts: null,
      chosePeopleTitle: "选择人员",
      chosePeopleOpen: false,
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 部门树选项
      deptOptions: undefined,
      filterText: "",
      peopleQueryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
      },
      showSearch: true,
      peopleListloading: true,
      peopleList: [],
      //选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      peopletotal: 0,
      // 日期范围
      dateRange: [],

      choseexaminationTitle:'选择试卷',
      choseexaminationOpen:false,
      examinationQueryParams:{
        pageNum: 1,
        pageSize: 10,
        sjmc: null,
      },
      showexaminationSearch:true,
      examinationListloading:true,
      examinationList:[],
      examinationtotal:0,
      examinationIds:[],
      examinationArray:['试卷A','试卷B','试卷C','试卷D','试卷E','试卷F',],

      examinationNames:[],
      sourceData:[]
    };
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
  },

  methods: {
    getPeopleList() {
      this.peopleListloading = true;
      listUser(this.addDateRange(this.peopleQueryParams, this.dateRange)).then(
        (response) => {
          this.peopleList = response.rows;
          this.peopletotal = response.total;
          this.peopleListloading = false;
        }
      );
    },
    getexaminationList() {
      // listexamination(this.examinationQueryParams).then(response => {
      //   this.examinationList = response.rows;
      //   this.examinationtotal = response.total;
      //   this.examinationListloading = false;
      // });
      listExamination(this.examinationQueryParams).then(response => {
        this.examinationList = response.rows;
        this.examinationtotal = response.total;
        this.examinationListloading = false;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    chosePeople() {
      this.chosePeopleOpen = true;
      this.getTreeselect();
      this.getPeopleList();

    },
    choseExamination(){
      this.choseexaminationOpen = true;
      this.getexaminationList();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleSelectionChangeTwo(selection){
      this.examinationIds = selection.map((item) => item.id);
      this.examinationArray = selection.map((item) => item.mc);
      console.log(this.examinationArray)
    },
    handleNodeClick(data) {
      this.peopleQueryParams.deptId = data.id;
      this.getPeopleList();
    },
    peopleHandleQuery() {
      this.getPeopleList();
    },
    getTreeselect() {
      treeselect().then((response) => {
        let parentData = response.data;
        let temp = parentData[0];
        temp["isDisabled"] = true;
        this.deptOptions = parentData;
      });
    },
    submitPeople(){
      console.log(this.ids)
      if(this.ids.length <=0 ) {
        this.$message.error('请选择人员！');
        return;
      }
      this.chosePeopleOpen = false
      this.choseExamination();
    },
    submitexamination(){
      if(this.examinationIds.length <= 0){
        this.$message.error('请选择考试！');
        return;
      }
      processingPersonnelAnalyzeData(this.examinationIds,this.ids).then(response => {
        this.sourceData = response;
              this.drawLine()

        console.log(response)
      });

      this.choseexaminationOpen = false;
    },
    cancel(){
      this.chosePeopleOpen = false
    },
    examinationCancel(){
      this.choseexaminationOpen = false;
    },
    examinationHandleQuery(){
      this.getexaminationList();
    },
    drawLine() {
      this.lineEcharts = echarts.init(this.$refs.charts, "macarons");
      // 基于准备好的dom，初始化echarts实例
      // 绘制图表
      this.lineEcharts.setOption({
        title: {
          text: "人员分析",
          subtext: "People analysis",
        },
          legend: {},
  // color: ['#ffdf25', '#36a9ce', '#d0e17d','#ccc'],
  tooltip: {},
  dataset: {
    source:this.sourceData
    // source:[
    //   ['name','104','105','107','110'],
    //   ['42',80.0,70.0,60.0,0],
    //   ['43',60.0,33,22,0]
    // ]
    // source: [
    //   ['name', '张三', '李四', '王五'],
    //   ['考试1', '43.3', '85.8', '93.7'],
    //   ['考试2', "83.1", "73.4", "55.1"],
    //   ['考试3', "86.4", "65.2", "82.5"],
    //   ['考试4', "72.4", "53.9", "39.1"]
    // ]
  },
  xAxis: { type: 'category' },
  yAxis: {},
  // Declare several bar series, each will be mapped
  // to a column of dataset.source by default.
  series: [
  { type: 'bar',barWidth: "16" },
  { type: 'bar' ,barWidth: "16"},
  { type: 'bar',barWidth: "16" },
  { type: 'bar',barWidth: "16" },
  { type: 'bar',barWidth: "16" },
  { type: 'bar',barWidth: "16" },
  { type: 'bar',barWidth: "16" },
  { type: 'bar',barWidth: "16" },
  { type: 'bar',barWidth: "16" },
   { type: 'bar',barWidth: "16" },
    { type: 'bar',barWidth: "16" }
  , { type: 'bar',barWidth: "16" }
  , { type: 'bar',barWidth: "16" }
  ]
        // Declare several bar series, each will be mapped
        // to a column of dataset.source by default.
        // series: [
        //   {name:'张三', type: "bar",barWidth: "16",data:[320, 332, 301, 334, 390, 360] },
        //   {name:'李四', type: "bar",barWidth: "16",data:[220, 182, 191, 234, 290, 320] },
        //   {name:'王五', type: "bar",barWidth: "16",data:[150, 232, 201, 154, 190, 150] }
        //   ],
      });
    },
  },
  mounted() {
    this.drawLine();
  },
};
</script>
<style scoped>
.custom_box {
  margin-top: 5px;
}
.charts {
  height: 500px;
  border: 1px solid #ccc;
  margin-top: 10px;
}
</style>
