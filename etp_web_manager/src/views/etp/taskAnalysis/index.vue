<template>
  <div class="app-container custom_box">
    <el-form
      v-show="true"
      ref="taskQueryForm"
      :inline="true"
      :model="taskQueryParams"
      label-width="90px"
    >
      <el-form-item label="任务名称" prop="mc">
        <el-input
          v-model="taskQueryParams.mc"
          clearable
          placeholder="请输入任务名称"
          size="small"
          @keyup.enter.native="taskHandleQuery()"
        />
      </el-form-item>
      <el-form-item
        label="标签"
        class="tags-div"
        prop="queryTag"
        style="width: calc(100% - 256px)"
      >
        <template style="position: relative">
          <el-checkbox-group
            ref="tag"
            v-model="queryTag"
            :style="{ height: height, overflow: 'hidden' }"
            @change="handleCheckedTagsChange"
          >
            <el-checkbox
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
              >全选</el-checkbox
            >
            <el-checkbox :label="item.id" v-for="(item, index) in tagOptions" name="tag">
              {{ item.name }}</el-checkbox>
          </el-checkbox-group>
          <div
            @click="openFop"
            v-if="tipFlag"
            style="
              position: absolute;
              right: -5%;
              width: 100px;
              top: 0;
              cursor: pointer;
              color: #1890ff;
            "
          >
            {{ openFlag ? "展开" : "收起" }}
          </div>
        </template>
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="taskHandleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <div style="border: 2px solid #ddd; overflow: hidden; padding-bottom: 10px">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button>任务列表</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="Taskloading" ref="firstTable" :data="TaskList">
        <el-table-column
          label="任务编号"
          :show-overflow-tooltip="true"
          width="100"
          align="center"
          prop="id"
          sortable
        />
        <el-table-column label="任务名称" align="center" prop="mc" />
        <el-table-column label="标签" align="center" prop="tags">
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.tags !== '' && scope.row.tags !== null"
              v-for="(item, index) in (scope.row.tags || '')
                .split(',')
                .slice(0, 3)"
              :key="index"
              style="margin-right: 3px; margin-bottom: 8px"
            >
              {{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="states">
          <template slot-scope="scope">
            <el-tag :type="scope.row.states == '0' ? 'info' : 'success'">{{
              scope.row.states == "0" ? "暂存" : "已发布"
            }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="Tasktotal > 0"
        :total="Tasktotal"
        :page-sizes="[5, 10, 20]"
        :page.sync="taskQueryParams.pageNum"
        :limit.sync="taskQueryParams.pageSize"
        @pagination="getTaskList"
      />
    </div>
    <div style="border: 3px solid #027cff">
      <el-container>
        <el-header style="height: 50px">
          <el-col :span="1.5">
            <div>任务进度分析</div>
          </el-col>
        </el-header>
      </el-container>
      <el-main style="color: #eef1f6">
        <div
          id="myChart1"
          ref="myChart1"
          class="myChart1"
          style="width: 100%; height: 400px; float: left"
        ></div>
      </el-main>
    </div>
    <!-- <div>
      <el-button type="success" @click="choseTask">选择任务和学员</el-button>
    </div>
    <div ref="charts" style="height: 500px" class="charts"></div>
    <el-dialog
      :title="choseTaskTitle"
      :visible.sync="choseTaskOpen"
      width="1000px"
      append-to-body
    >
      <el-form
        :model="TaskQueryParams"
        ref="examinationQueryForm"
        :inline="true"
        v-show="true"
        label-width="68px"
      >
        <el-input
          placeholder="请输入任务名称"
          clearable
          v-model="TaskQueryParams.mc"
          style="width: 300px; float: right"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="TaskHandleQuery"
          ></el-button>
        </el-input>
        <div style="clear: both"></div>
      </el-form>

      <el-table
        v-loading="Taskloading"
        border
        :data="TaskList"
        @selection-change="handleSelectionChange"
        style="margin-top: 10px"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="任务名称" align="center" prop="mc" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
      </el-table>
      <pagination
        v-show="Tasktotal > 0"
        :total="Tasktotal"
        :page.sync="TaskQueryParams.pageNum"
        :limit.sync="TaskQueryParams.pageSize"
        @pagination="getTaskList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTask">确 定</el-button>
        <el-button @click="TaskCancel">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>
<script>
import {
  listTask,
  getTask,
  delTask,
  addTask,
  updateTask,
  exportTask,
  pushBack,
} from "@/api/etp/task/index";

import { listGroup } from "@/api/system/userGroup";
import { getToken } from "@/utils/auth";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listTagRelate } from "@/api/etp/tag/tag";
import echarts from "echarts";
export default {
  name: "taskAnalysis",
  components: { Treeselect },
  data() {
    return {
      lineEcharts: null,
      choseTaskTitle: "选择课件",
      choseTaskOpen: false,
      taskQueryParams: {
        pageNum: 1,
        pageSize: 5,
        mc: null,
      },
      Taskloading: true,
      TaskList: [],
      //选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      Tasktotal: 0,
      //人员
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

      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],

      height:'',
      openFlag:true,
      tipFlag:false,

      isIndeterminate: true,
      checkAll: false,
    };
  },
  watch: {
    taskQueryParams:{
      handler(){
        this.getTaskList();
      },
      deep:true
    },
  },
  created() {
    this.getTags();
    this.getTaskList();
  },

  methods: {
    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "RW" }).then((response) => {
        response.data.forEach((item) => {
          this.tagOptions.push(item);
        });
        //  this.tagOptions = Object.assign(this.tagOptions,response.data);
        this.$nextTick((_) => {
          if (this.$refs.tag.$el.clientHeight > 44) {
            this.height = 45 + "px";
            this.tipFlag = true;
          }
        });
      });
    },
    taskHandleQuery() {
      this.taskQueryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.taskQueryParams.tags = this.queryTag.join(',')
      }
      this.getTaskList();
    },
    getTaskList() {
      listTask(this.taskQueryParams).then((response) => {
        this.TaskList = response.rows;
        this.Tasktotal = response.total;
        this.Taskloading = false;
      });
    },
    resetQuery() {
      this.resetForm("taskQueryForm");
      this.queryTag = [];
      this.taskHandleQuery();
    },
    handleCheckAllChange(val) {
      if (val) {
        this.tagOptions.forEach((item) => {
          this.queryTag.push(item.id);
        });
      } else {
        this.queryTag = [];
      }
      this.isIndeterminate = false;
    },
    handleCheckedTagsChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.tagOptions.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },
        openFop(){
      this.openFlag = !this.openFlag;
      if (!this.openFlag){ // 展开
        this.height = 'auto'
      }else {
        this.height = 45 + 'px'
      }
      // this.$forceUpdate()
    },
    handleSelectionChange() {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    drawLine() {
      this.lineEcharts = echarts.init(this.$refs.myChart1, "macarons");
      // 基于准备好的dom，初始化echarts实例
      // 绘制图表
      this.lineEcharts.setOption({
        tooltip: {
          trigger: "axis",
        },

        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },

        xAxis: {
          type: "category",
          data: [
            "0%~10%",
            "10%~20%",
            "20%~30%",
            "30%~40%",
            "40%~50%",
            "50%~60%",
            "60%~70%",
            "70%~80%",
            "80%~90%",
            "90%~100%",
          ],
        },
        yAxis: {
          type: "value",
          name: "人数（个）",
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130, 130, 10, 30],
            type: "bar",
            barWidth: 16,
          },
        ],
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
  width: 100%;
  height: 500px;
  border: 1px solid #ccc;
  margin-top: 10px;
}
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
.tags-div ::v-deep .el-form-item__content {
  width: calc(100% - 120px) !important;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
