<template>
  <div class="app-container" style="overflow-x: hidden;">
    <div class="font-size-xl font-color-black">考试记录</div>
    <div style="margin: 28px 0 20px 0">
      <span
        v-for="(item, index) in examTypes"
        :key="index"
        :class="[item.isChecked ? 'tab-check' : 'tab-uncheck']"
        @click="hisExamTypeOnClick(index)"
      >
        {{ item.name }}
      </span>
    </div>
    <el-form
      @submit.native.prevent
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="考试名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入考试名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="yyyyMMdd"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="考试开始时间"
          end-placeholder="考试结束时间"
          @change="handleQuery"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery" :loading="loading&&!isReset"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" @click="resetQuery" :loading="loading&&isReset">重置</el-button>
      </el-form-item>
    </el-form>

    <span class="font-size-m font-color-light-grey">
      共找到
      <span class="font-color-red font-size-m">{{ total ? total : "0" }}</span>
      条考试记录</span
    >

    <div
      v-for="(historyExamItem, historyExamIndex) in studentList"
      :key="historyExamIndex"
      class="exam-item"  @click="showInfo(historyExamItem)"
    >
      <div>
        <span
          class="font-size-l font-color-black text-title"
         style="margin-right:12px"
        >
          {{ historyExamItem.name }}
        </span>
        <el-tag v-if="historyExamItem.status == 0" type="info">未交卷</el-tag>
        <el-tag v-if="historyExamItem.status == 1" type="warning"
          >未判卷</el-tag
        >
        <el-tag v-if="historyExamItem.status == 3" type="warning"
          >未发布</el-tag
        >
        <el-tag v-if="historyExamItem.status == 8" type="info">已放弃</el-tag>
        <el-tag v-if="historyExamItem.status == 9" type="success"
          >已完成</el-tag
        >
      </div>
      <div style="margin-top: 12px">
        <span class="font-size-m font-color-light-grey" >考试成绩：</span>
        <span class="font-color-red font-size-m" >
          {{historyExamItem.status != null && historyExamItem.status == 9?
            historyExamItem.userScore+'分':'未出成绩'
          }}</span
        >

        <span
          v-if="historyExamItem.status != null && historyExamItem.status != 0"
          class="font-size-m font-color-light-grey"
          style="margin-left: 20px"
          >考试时间：</span
        >
        <span
          class="font-color-black font-size-m"
          v-if="historyExamItem.status != null && historyExamItem.status != 0"
          >{{ historyExamItem.doTime }}</span
        >
        <span
          class="font-size-m font-color-light-grey"
          style="margin-left: 20px"
          >试卷总分：</span
        >
        <span class="font-color-black font-size-m"
          >{{
            historyExamItem.totalScore ? historyExamItem.totalScore : "0"
          }}分</span
        >

        <span
          class="font-size-m font-color-light-grey"
          style="margin-left: 20px"
          v-if="
            historyExamItem.status != null &&
            historyExamItem.status != 0 &&
            historyExamItem.status != 8
          "
          >交卷方式：</span
        >
        <span
          class="font-color-black font-size-m"
          v-if="
            historyExamItem.status != null &&
            historyExamItem.status != 0 &&
            historyExamItem.status != 8
          "
          >{{ handType(historyExamItem.handType) }}
        </span>
      </div>
      <div style="margin-top: 12px">
        <span class="font-size-m font-color-light-grey">考试时间：</span>
        <span class="font-color-black font-size-m">{{
          historyExamItem.startTime ? historyExamItem.startTime : ""
        }}</span>

        <span
          class="font-size-m font-color-light-grey"
          style="margin-left: 20px"
          v-if="
            historyExamItem.endTime &&
            historyExamItem.status != null &&
            historyExamItem.status != 8
          "
          >交卷时间：</span
        >
        <span
          class="font-color-black font-size-m"
          v-if="
            historyExamItem.endTime &&
            historyExamItem.status != null &&
            historyExamItem.status != 8
          "
          >{{ historyExamItem.endTime ? historyExamItem.endTime : "" }}</span
        >
        <span
          class="font-size-m font-color-light-grey"
          v-if="historyExamItem.logicEndTime && historyExamItem.status == 0"
          style="margin-left: 20px"
          >预计结束时间：</span
        >
        <span
          class="font-color-black font-size-m"
          v-if="historyExamItem.logicEndTime && historyExamItem.status == 0"
          >{{ historyExamItem.logicEndTime ? item.logicEndTime : "" }}</span
        >
        <span
          class="font-size-m font-color-light-grey"
          style="margin-left: 20px"
          v-if="historyExamItem.endTime && historyExamItem.status == 8"
          >放弃考试时间：</span
        >
        <span
          class="font-color-black font-size-m"
          v-if="historyExamItem.endTime && historyExamItem.status == 8"
          >{{ historyExamItem.endTime ? historyExamItem.endTime : "" }}</span
        >
      </div>

      <el-button
        :type="examStatus(historyExamItem.status).type"
        style="position: absolute; bottom: 20%; right: 12px"
        @click="showInfo(historyExamItem)"
        >{{ examStatus(historyExamItem.status).btnText }}</el-button
      >
    </div>
    <p v-if="loading" class="text-p">下拉加载更多数据</p>
    <p v-if="noMore" class="text-p">暂无更多数据</p>

    <el-table v-loading="loading" :data="studentList" v-if="false">
      <el-table-column
        label="封面图"
        align="center"
        prop="cover"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <img :src="scope.row.cover" min-width="70" height="70" alt="X" />
        </template>
      </el-table-column>
      <el-table-column
        label="考试名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />

      <el-table-column
        label="开始考试时间"
        align="center"
        prop="startTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="结束考试时间"
        align="center"
        prop="endTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="答卷时长"
        align="center"
        prop="doTime"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="scope.row.doTime != null"
            >{{ scope.row.doTime[0] }}分{{ scope.row.doTime[1] }}秒</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="交卷方式"
        align="center"
        prop="handType"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.handType == 0" type="info">未交卷</el-tag>
          <el-tag v-if="scope.row.handType == 1" type="success"
            >人工交卷</el-tag
          >
          <el-tag v-if="scope.row.handType == 2" type="danger">强制交卷</el-tag>
          <el-tag v-if="scope.row.handType == 3" type="danger">放弃</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="考试状态"
        align="center"
        prop="status"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0" type="info">未交卷</el-tag>
          <el-tag v-if="scope.row.status == 1" type="success">未判卷</el-tag>
          <el-tag v-if="scope.row.status == 4" type="warning">未发布</el-tag>
          <el-tag v-if="scope.row.status == 9" type="success">已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="得分"
        align="center"
        prop="userScore"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ scope.row.userScore ? scope.row.userScore : "0" }} 分</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click="showInfo(scope.row)"
            >详情</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-if="false"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getHistoryList, getHistoryExam, startExam } from "@/api/exam/exam";
import { isFulfilled } from "q";

export default {
  name: "HistoryExam",
  data() {
    return {
      examTypes: [
        { name: "所有形式", type: "0", isChecked: true },
        // { name: "公开的", type: "1", isChecked: false },
        // { name: "指派的", type: "2", isChecked: false },
      ],

      // 遮罩层
      loading: true,
      noMore: false,
      isReset:false,//是否点击重置
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
      // 试卷学员关联表格数据
      studentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      dateRange:[],//时间区间
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        examId: null,
        range: '',
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },

  methods: {
    handleScroll() {
      // 获取滚动的距离
      let scrollTop = document.documentElement.scrollTop;
      // 获取滚动的高度（获取整个html的高度）
      let scrollHeight = document.documentElement.scrollHeight;
      // 获取屏幕(浏览器)高度
      let clientHeight = document.documentElement.clientHeight;
      // 滚动的距离 + 屏幕高度 - 内容高度 >= 0 表示滚动到底部了      (下拉加载判断条件)
      if (scrollTop + clientHeight - scrollHeight >= 0) {
        //判断是否需要获取数据
        if (this.studentList.length >= this.total) {
          this.loading = false;
          this.noMore = true;
        } else {
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum++;
          this.getList();
        }
      }
    },
    //考试类型 所有形式  公开  指派
    examTypeOnClick(index) {
      this.examTypes[index].isChecked = true;
      this.examTypes.forEach((item, itemIndex) => {
        if (index == itemIndex) {
          item.isChecked = true;
        } else {
          item.isChecked = false;
        }
      });

      //重新发起请求 FIXME
      // this.queryParams.xx
      this.queryParams.pageNum = 1;
      this.getList();
    },
    secondToMinutes(second) {
      let minute = Math.floor(second / 60);
      let sec = second % 60;
      return `${minute}分${sec}秒`;
    },
    handType(type) {
      switch (type) {
        case "0":
          return "尚未交卷";
        case "1":
          return "人工交卷";
        case "2":
          return "强制交卷";
        case "3":
          return "已放弃";
        default:
          return "暂无";
      }
    },
    //考试状态 /** 试卷状态（0未交卷 1未判卷 3未发布 8已放弃 9已完成） */ 当状态为未交卷时可以重新进入考试 需要特殊判断
    examStatus(status) {
      let statusInfo = {};
      switch (status) {
        case "0": //未交卷
          statusInfo.type = "success";
          statusInfo.btnText = "继续考试";
          return statusInfo;
        case "1":
          statusInfo.type = "warning";
          statusInfo.btnText = "等待判卷";
          return statusInfo;

        case "3":
          statusInfo.type = "warning";
          statusInfo.btnText = "等待公布";
          return statusInfo;
        case "8":
          statusInfo.type = "info";
          statusInfo.btnText = "已放弃";
          return statusInfo;
        case "9":
          statusInfo.type = "primary";
          statusInfo.btnText = "查看成绩";
          return statusInfo;

        default:
          return statusInfo;
      }
    },

    //跳转至历史试卷详情
    showInfo(row) {

      // if (row.status == 0) {
      //   this.$message.error("未交卷的考试无法查看详情！");
      //   return;
      // }
      // this.$router.push({
      //   path: "/showHistoryExam/view",
      //   query: {
      //     recordId: row.recordId,
      //   },
      // });
      let status = row.status;

      switch (
        status //试卷状态（0未交卷 1未判卷 3未发布 8已放弃 9已完成） */
      ) {
        case "0": //继续考试 跳转到考试详情页面
          let router = this.$router.resolve({
            path: "/examStart/detail/" + row.examId,
          });
          window.open(router.href, "_blank");
          break;
        case "1":
          this.$message.error("考试正在判卷，请您耐心等待");

          break;
        case "3":
          this.$message.error("考试成绩尚未公布，请您耐心等待");

          break;
        case "8":
          this.$message.error("您已主动放弃本次考试，无法查看试卷");

          break;
        case "9":
          this.$router.push({
            path: "/showHistoryExam/view",
            query: {
              recordId: row.recordId,
            },
          });

          break;
        default:
          this.$message.error("您已主动放弃本次考试");
      }
    },
    /** 查询试卷学员关联列表 */
    getList() {
      this.loading = true;
      getHistoryList(this.queryParams).then((response) => {
        let list = response.rows;
        if (list && list.length > 0) {
          for (let i = 0; i < list.length; i++) {
            // if (
            //   list[i].cover == null ||
            //   list[i].cover == "" ||
            //   list[i].cover == undefined
            // ) {
            //   list[i].cover = require("@/assets/images/cover.jpg");
            // }
            if (list[i].doTime != null) {
              list[i].doTime = this.secondToMinutes(list[i].doTime);
              // let m = parseInt((list[i].doTime / 60) % 60);
              // let s = parseInt(list[i].doTime % 60);
              // let date = [];
              // date[0] = m;
              // date[1] = s;
              // list[i].doTime = date;
            }
          }
          this.studentList = this.studentList.concat(list);
          this.total = response.total;
          if (this.studentList.length >= this.total) {
            //若本次请求列表长度大于等于列表总数 则认为没有更多数据了
            this.noMore = true;
          }
        } else {
          this.noMore = true;
        }

        this.loading = false;
        this.isReset = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        ksmc: null,
        id: null,
        ksid: null,
        sjid: null,
        xyid: null,
        jd: null,
        snapshot: null,
        snapCreateTime: null,
        da: null,
        score: null,
        rate: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.total = 0;
      this.queryParams.pageNum = 1;
      if(this.dateRange&&this.dateRange.length>0){
        this.queryParams.range = this.dateRange.join(',');
      }else{
        this.queryParams.range='';
      }

      this.studentList = [];
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {

      this.isReset = true;
      this.dateRange = [];
      this.resetForm("queryForm");

      this.handleQuery();

    },
  },
};
</script>

<style scoped>
.font-size-xl {
  font-size: 26px;
}
.font-size-l {
  font-size: 16px;
}
.font-size-m {
  font-size: 14px;
}

.font-color-black {
  color: #333333;
}
.font-color-grey {
  color: #666666;
}
.font-color-light-grey {
  color: #999999;
}
.font-color-red {
  color: #ff0000;
}

.tab-check {
  cursor: pointer;
  color: #0045c5;
  background-color: rgba(0, 69, 197, 0.05);
  border-radius: 4px;
  font-size: 14px;
  padding: 5px 10px;
  margin-right: 10px;
}
.tab-uncheck {
  cursor: pointer;
  color: #333333;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-size: 14px;
  padding: 5px 10px;
  margin-right: 10px;
}
.flex-row {
  display: flex;
  flex-direction: row;
}
.flex-between {
  justify-content: space-between;
}
.flex-col-center {
  align-items: center;
}

.exam-item {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  position: relative;
  margin-top: 18px;
  padding-bottom: 12px;

  border-bottom-style: ridge;
  border-bottom-width: thin;
  border-bottom-color: #eeeeee;
}

.text-p {
  text-align: center;
  color: #999999;
  font-size: 12px;
}

.exam-item:hover .text-title {
  color: #0045c5;
}

::v-deep .el-button--primary {
  background-color: #0045c5;
  border-color:  #0045c5;
  border-radius: 6px;
}
</style>
