<template>
  <div class="app-container custom_box">
    <el-form
      v-show="true"
      ref="examinationQueryForm"
      :inline="true"
      :model="coursewareQueryParams"
      label-width="90px"
    >
      <el-form-item label="课件名称" prop="mc">
        <el-input
          v-model="coursewareQueryParams.mc"
          clearable
          placeholder="请输入课件名称"
          size="small"
          @keyup.enter.native="coursewareHandleQuery"
        />
      </el-form-item>
      <!-- <el-form-item
        class="tags-div"
        label="标签"
        prop="queryTag"
        style="width: calc(100% - 256px)"
      >
        <el-checkbox-group v-model="queryTag">
          <el-checkbox v-for="item in tagOptions" :key="item.id" :label="item.id">
            {{ item.name }}</el-checkbox
          >
        </el-checkbox-group>
      </el-form-item> -->
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
            <el-checkbox :label="item.id"  v-for="(item, index) in tagOptions"  name="tag">
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
          @click="coursewareHandleQuery"
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
          <el-button>课件列表</el-button>
        </el-col>
      </el-row>
      <el-table
        v-loading="coursewareloading"
        ref="firstTable"
        :data="coursewareList"
        @current-change="handleCurrentChange1"
      >
        <el-table-column align="center" label="序号" prop="xh">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template></el-table-column
        >
        <el-table-column
          :show-overflow-tooltip="true"
          align="center"
          label="课件名称"
          prop="mc"
        />
        <el-table-column label="课件形式" align="center" prop="xs">
          <template slot-scope="scope">
            <font>{{ scope.row.xs == "1" ? "OFFICE" : "" }}</font>
            <font>{{ scope.row.xs == "2" ? "视频" : "" }}</font>
            <font>{{ scope.row.xs == "3" ? "富文本" : "" }}</font>
            <font>{{ scope.row.xs == "4" ? "外部链接" : "" }}</font>
            <font>{{ scope.row.xs == "5" ? "其他文件" : "" }}</font>
          </template>
        </el-table-column>
        <el-table-column align="center" label="标签" prop="tags">
          <template slot-scope="scope">
            <span v-for="(item, index) in (scope.row.tags || '').split(',')">
              <el-tag
                size="mini"
                v-if="item != ''"
                style="margin-right: 3px; margin-bottom: 2px"
              >
                {{ item }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="coursewaretotal > 0"
        :total="coursewaretotal"
        :page-sizes="[5, 10, 20]"
        :page.sync="coursewareQueryParams.pageNum"
        :limit.sync="coursewareQueryParams.pageSize"
        @pagination="getCoursewareList"
      />
    </div>
    <div style="border: 3px solid #027cff">
      <el-container>
        <el-header style="height: 50px">
          <el-col :span="1.5">
            <div>课件浏览分析</div>
          </el-col>
        </el-header>
        <el-main style="color: #eef1f6">
          <div
            id="myChart1"
            ref="myChart1"
            class="myChart1"
            style="width: 49%; height: 400px; float: left"
          ></div>
          <div
            id="myChart2"
            ref="myChart2"
            class="myChart2"
            style="width: 49%; height: 400px; float: right"
          ></div>
        </el-main>
      </el-container>
    </div>
    <!-- <div>
      <el-button type="success" @click="choseCourseware">选择课件</el-button>
    </div>
    <div ref="charts" style="height: 500px" class="charts"></div>
    <el-dialog
      :title="choseCoursewareTitle"
      :visible.sync="choseCoursewareOpen"
      width="1000px"
      append-to-body
    >
        <el-form
            :model="coursewareQueryParams"
            ref="examinationQueryForm"
            :inline="true"
            v-show="true"
            label-width="68px"
          >
            <el-input
              placeholder="请输入课件名称"
              clearable
              v-model="coursewareQueryParams.mc"
              style="width: 300px; float: right"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="coursewareHandleQuery"
              ></el-button>
            </el-input>
            <div style="clear: both"></div>
          </el-form>

            <el-table
            v-loading="coursewareloading"
            border
            :data="coursewareList"
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
            <el-table-column label="课件名称" align="center" prop="mc" />
            <el-table-column
              label="创建时间"
              align="center"
              prop="createTime"
            />
          </el-table>
          <pagination
            v-show="coursewaretotal > 0"
            :total="coursewaretotal"
            :page.sync="coursewareQueryParams.pageNum"
            :limit.sync="coursewareQueryParams.pageSize"
            @pagination="getCoursewareList"
          />
                  <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCourseware">确 定</el-button>
        <el-button @click="coursewareCancel">取 消</el-button>
      </div>
    </el-dialog> -->
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
  name: "coursewareBrowsing",
  components: { Treeselect },
  data() {
    return {
      lineEcharts: null,
      choseCoursewareTitle: "选择课件",
      choseCoursewareOpen: false,
      coursewareQueryParams: {
        pageNum: 1,
        pageSize: 5,
        tags:null,
        mc: null,
      },
      coursewareloading: true,
      coursewareList: [],
      //选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      coursewaretotal: 0,
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],

      height: "",
      openFlag: true,
      tipFlag: false,

      isIndeterminate: true,
      checkAll: false,
    };
  },
  watch: {
    coursewareQueryParams:{
      handler(){
        this.getCoursewareList();
      },
      deep:true
    },
  },
  created() {
    this.getTags();
    this.getCoursewareList();
  },

  methods: {
    /** 搜索按钮操作 */
    coursewareHandleQuery() {
      this.coursewareQueryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.coursewareQueryParams.tags = this.queryTag.join(',')
      }
      this.getCoursewareList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.coursewareQueryParams.mc = null;
      this.queryTag = [];
      this.coursewareHandleQuery();
    },
    getCoursewareList() {
      listCourseware(this.coursewareQueryParams).then((response) => {
        this.coursewareList = response.rows;
        this.coursewaretotal = response.total;
        this.coursewareloading = false;
      });
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "KJ" }).then((response) => {
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
    choseCourseware() {
      this.choseCoursewareOpen = true;
      this.getCoursewareList();
    },
    submitCourseware() {
      this.choseCoursewareOpen = false;
    },
    coursewareCancel() {
      this.choseCoursewareOpen = false;
    },
    handleCurrentChange1() {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    drawLineCharts1() {
      this.lineEcharts1 = echarts.init(this.$refs.myChart1, "macarons");
      this.lineEcharts1.setOption({
        tooltip: {
          trigger: "axis",
        },
        xAxis: {
          type: "category",
          data: ["2/26", "2/27", "2/28", "3/1", "3/2", "3/3", "3/4"],
        },
        yAxis: {
          type: "value",
          name: '浏览次数',
        },
        series: [
          {
            data: [150, 230, 224, 218, 135, 147, 260],
            type: "line",
          },
        ],
      });
    },
    drawLineCharts2() {
      this.lineEcharts1 = echarts.init(this.$refs.myChart2, "macarons");
      this.lineEcharts1.setOption({
        xAxis: {
          type: "category",
          data: ["研发部", "销售部", "运维部", "工程部"],
        },
        tooltip: {
          trigger: "axis",
        },
        yAxis: {
          type: "value",
          name: '浏览次数',
        },
        series: [
          {
            data: [120, 200, 150, 80],
            type: "bar",
            barWidth:16,
            showBackground: true,
            backgroundStyle: {
              color: "rgba(180, 180, 180, 0.2)",
            },
          },
        ],
      });
    },
    drawLine() {
      this.lineEcharts = echarts.init(this.$refs.charts, "macarons");
      // 基于准备好的dom，初始化echarts实例
      // 绘制图表
      this.lineEcharts.setOption({
        title: {
          text: "课件浏览分析",
          subtext: "Courseware browsing analysis",
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["视频演示", "线性代数", "Java实战之路", "vue3教程文档"],
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
          boundaryGap: false,
          data: ["2月12", "2月13", "2月14", "2月15", "2月16", "2月17", "2月18"],
        },
        yAxis: {
          type: "value",
          //写在yAxis里面
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
        },
        series: [
          {
            name: "视频演示",
            type: "line",
            stack: "Total",
            data: [120, 132, 101, 134, 90, 230, 210],
          },
          {
            name: "线性代数",
            type: "line",
            stack: "Total",
            data: [220, 182, 191, 234, 290, 330, 310],
          },
          {
            name: "Java实战之路",
            type: "line",
            stack: "Total",
            data: [150, 232, 201, 154, 190, 330, 410],
          },
          {
            name: "vue3教程文档",
            type: "line",
            stack: "Total",
            data: [320, 332, 301, 334, 390, 330, 320],
          },
        ],
      });
    },
  },
  mounted() {
    //this.drawLine();
    this.drawLineCharts1();
    this.drawLineCharts2();
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
