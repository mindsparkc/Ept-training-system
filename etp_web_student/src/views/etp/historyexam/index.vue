<template>
  <div class="app-container">
    <el-form @submit.native.prevent :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" >
      <el-form-item label="考试名称" prop="ksmc">
        <el-input
          v-model="queryParams.ksmc"
          placeholder="请输入考试名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考试开始~结束时间">
        <el-date-picker
          value-format="yyyy-MM-dd HH:mm:ss"
          v-model="dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间" @change="handleQuery">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="studentList">
      <el-table-column label="封面图" align="center" prop="fmt" show-overflow-tooltip>
        <template   slot-scope="scope">
          <img :src="scope.row.fm"  min-width="70" height="70"  alt="X"/>
        </template>
      </el-table-column>
      <el-table-column label="考试名称" align="center" prop="ksmc" show-overflow-tooltip/>

      <el-table-column label="开始考试时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束考试时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.handTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="答卷时长" align="center" prop="djsc" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.djsc}}分钟</span>
        </template>
      </el-table-column>
      <el-table-column label="得分" align="center" prop="score" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.score ? scope.row.score : '0' }} 分</span>
        </template>
      </el-table-column>
      <el-table-column label="分数等级" align="center" prop="fsdj" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click="showInfo(scope.row)"
          >详情</el-button>
        </template>
      </el-table-column>
    </el-table>

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

import {getHistoryExam} from "@/api/etp/exam/exam";

export default {
  name: "HistoryExam",
  data() {
    return {
      // 遮罩层
      loading: true,
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
      queryParams: {
        ksmc: null,
        pageNum: 1,
        pageSize: 10,
        ksid: null,
        sjid: null,
        xyid: null,
        jd: null,
        snapshot: null,
        snapCreateTime: null,
        da: null,
        score: null,
        rate: null,
        createTime:null,
        handTime:null
      },
      dateRange:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    //跳转至历史试卷详情
    showInfo(row){

      if(row.handTime==null){
        this.$message.error("未交卷的考试无法查看详情！")
        return
      }
      // this.$router.push("/etp/historyexam/showTestPaper/" + row.id);
      this.$router.push("/etp/historyexam/components/paperInfo/" + row.id);
    },
    /** 查询试卷学员关联列表 */
    getList() {
      this.loading = true;
      getHistoryExam(this.queryParams).then(response => {
        console.log(response)
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if(list[i].fm == null || list[i].fm == '' || list[i].fm == undefined){
            list[i].fm = require("@/assets/images/cover.jpg");
          }
        }
        this.studentList = list;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        ksmc:null,
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
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      let date = this.dateRange;
      this.queryParams.createTime = date[0];
      this.queryParams.handTime = date[1];
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.dateRange = [];
      this.handleQuery();
    }
  }
};
</script>
