<template>
  <div class="app-container custom_box">
    <el-form
      v-show="true"
      ref="courseQueryForm"
      :inline="true"
      :model="courseQueryParams"
      label-width="90px"
    >
      <el-form-item label="课程名称" prop="mc">
        <el-input
          v-model="courseQueryParams.mc"
          clearable
          placeholder="请输入课程名称"
          size="small"
          @keyup.enter.native="courseHandleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <template style="position: relative">
            <el-checkbox-group ref="tag" v-model="queryTag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
              <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
            </el-checkbox-group>
          <div @click="openFop" v-if="tipFlag"  style="position: absolute;right: -5%;width: 100px;top:0;cursor: pointer;color: #1890ff">
            {{ openFlag?'展开':'收起' }}
          </div>
        </template>

      </el-form-item>

      <el-form-item>
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="courseHandleQuery"
          >搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div style="border: 2px solid #ddd; overflow: hidden; padding-bottom: 10px">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button>课程列表</el-button>
        </el-col>
      </el-row>
      <el-table
        v-loading="courseloading"
        ref="firstTable"
        :data="courseList"
      >
        <el-table-column label="课程编号" :show-overflow-tooltip="true" width="100" align="center" prop="id" sortable/>
        <el-table-column label="课程名称" align="center" prop="mc" />
        <el-table-column label="标签" align="center" prop="tags" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null"
                  v-for="(item,index) in (scope.row.tags||'').split(',')"
                  :key="index"
                  style="margin-right: 3px;margin-bottom:8px">
            {{item}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" width="180" prop="updateTime"/>
        </el-table>
        <pagination
            v-show="coursetotal > 0"
            :total="coursetotal"
            :page-sizes="[5, 10, 20]"
            :page.sync="courseQueryParams.pageNum"
            :limit.sync="courseQueryParams.pageSize"
            @pagination="getCourseList"
          />
    </div>
    <div style="border: 3px solid #027cff">
      <el-container>
        <el-header style="height: 50px">
          <el-col :span="1.5">
            <div>课程进度分析</div>
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
  </div>
</template>
<script>
import {
  listCourseware,
  getCourseware,
  delCourseware,
  addCourseware,
  updateCourseware,
  exportCourseware,
} from "@/api/etp/courseware/courseware";
import { listGroup } from "@/api/system/userGroup";
import { getToken } from "@/utils/auth";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listTagRelate } from "@/api/etp/tag/tag";
import echarts from "echarts";
export default {
  name: "courseProgress",
  components: { Treeselect },
  data() {
    return {
      lineEcharts: null,
      choseCourseTitle: "选择课程",
      choseCourseOpen: false,
      courseQueryParams: {
        pageNum: 1,
        pageSize: 5,
        mc: null,
      },
      courseloading: true,
      courseList: [],
      //选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      coursetotal: 0,

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
    courseQueryParams:{
      handler(){
        this.getCourseList();
      },
      deep:true
    },
  },
  created() {
    this.getTags();
    this.getCourseList();
  },

  methods: {
    handleCheckAllChange(val) {
      if (val){
        this.tagOptions.forEach(item => {
          this.queryTag.push(item.id)
        })
      }else{
        this.queryTag = []
      }
      this.isIndeterminate = false;
    },

    handleCheckedTagsChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.tagOptions.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },
        /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "KC" }).then((response) => {
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
    havaTags(val) {
      this.tagValue = val
    },
    courseHandleQuery(){
      this.courseQueryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.courseQueryParams.tags = this.queryTag.join(',')
      }
      this.getCourseList();
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
    getCourseList() {
      listCourseware(this.courseQueryParams).then((response) => {
        this.courseList = response.rows;
        this.coursetotal = response.total;
        this.courseloading = false;
      });
    },
    resetQuery() {
      this.resetForm("courseQueryForm");
      this.queryTag = [];
      this.courseHandleQuery();
    },
    choseCourse() {
      this.choseCourseOpen = true;
      this.getCourseList();
    },
    coursewareHandleQuery() {},
    submitCourseware() {},
    courseCancel() {
      this.choseCourseOpen = false;
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
    type: 'category',
    data: ['0%~10%', '10%~20%', '20%~30%', '30%~40%', '40%~50%', '50%~60%', '60%~70%', '70%~80%', '80%~90%', '90%~100%']
  },
  yAxis: {
    type: 'value',
    name:"人数（个）"
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130, 130, 10, 30],
      type: 'bar',
      barWidth:16,
    }
  ]
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
