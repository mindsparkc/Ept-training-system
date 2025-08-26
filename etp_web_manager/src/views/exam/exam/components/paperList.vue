<template>
<div>
  <el-dialog
    :visible.sync="sj_open"
    :close-on-click-modal="false"
    append-to-body
    title="选择试卷" width="1000px" >
    <el-form ref="queryForm" :inline="true" :model="queryParams" label-width="98px">
      <el-form-item label="试卷名称" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入试卷名称"
          size="small"
          @keyup.enter.native="handleQuery"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="试卷描述" prop="content">
        <el-input
          v-model="queryParams.content"
          clearable
          placeholder="请输入试卷名称"
          size="small"
          @keyup.enter.native="handleQuery"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="paperTable"
      v-loading="loading"
      :data="testpaperList"
      highlight-current-row
      @current-change="handleCurrentChange" >
      <el-table-column align="center" label="序号" type="index"/>
      <el-table-column align="center" label="试卷名称" prop="name"/>
      <el-table-column align="center" label="试卷描述" prop="content"/>
      <el-table-column align="center" label="发布状态" prop="status">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status=='99'">发布</el-tag>
          <el-tag type="info" v-if="scope.row.status=='0'">暂存</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-row style="margin-bottom: 2%">
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-row>
      <el-row>
        <el-button type="primary" @click="confirm()">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </el-row>
    </div>
  </el-dialog>
</div>
</template>

<script>
import {listPaper} from "@/api/paper/paper";

export default {
  name: "paperList",
  data(){
    return{
      sj_open:false,
      loading:false,
      total:0,
      testpaperList:[],
      selectRow:{},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        content: null,
        status:99
      },
      sjid:null
    }
  },
  methods: {
    getPapers(sjid){
      this.sj_open = true;
      this.handleQuery();
      this.sjid = sjid
    },
    /** 查询试卷列表 */
    getList() {
      this.loading = true;
      listPaper(this.queryParams).then(response => {
        this.testpaperList = response.rows;
        this.total = response.total;
        this.setCurrent(this.sjid)
        this.loading = false;

      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    confirm(){
      this.sj_open = false
      this.$emit('confirm',this.selectRow)
    },
    handleCurrentChange(val) {
      this.selectRow = val;
    },
    // 取消按钮
    cancel() {
      this.sj_open = false;
      this.reset();
    },

    //选中试卷
    setCurrent(sjid) {
      let data = this.testpaperList.find(item => sjid == item.paperId)
      this.$nextTick(_=>{
        if(sjid == undefined){
          this.$refs.paperTable.setCurrentRow(this.testpaperList[0]);
        }else{
          this.$refs.paperTable.setCurrentRow(data);
        }

      })
    },
  }
}
</script>

<style scoped>

</style>
