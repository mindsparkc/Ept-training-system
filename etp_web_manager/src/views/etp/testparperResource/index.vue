<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="试卷名称:" prop="sjmc">
        <el-input
          v-model="queryParams.sjmc"
          placeholder="请输入试卷名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="组卷策略:" prop="zjcl" class="tags-div" style="width: calc(100% - 256px);">
        <!-- <el-select v-model="queryParams.zjcl" placeholder="请选择组卷策略" clearable size="small">
          <el-option
            v-for="dict in zjclOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select> -->
        <el-radio-group v-model="queryParams.zjcl">
          <el-radio v-for="dict in zjclOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="试卷来源:"  class="tags-div" prop="kjly" style="width: calc(100% - 256px);">
        <el-radio-group v-model="queryParams.kcly">
          <el-radio v-for="dict in sjlyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item> -->
            <el-form-item label="标签:" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <el-checkbox-group v-model="queryTag">
          <el-checkbox :label="item.id" v-for="item in tagOptions"> {{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['testparperResource:testparperResource:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['testparperResource:testparperResource:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['testparperResource:testparperResource:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
		  :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['testparperResource:testparperResource:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="testparperResourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="封面" align="center" prop="fm" >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fm"
            :fit="fit"
            :preview-src-list="new Array(scope.row.fm)"/>

        </template>
      </el-table-column>
      <el-table-column label="试卷名称" align="center" prop="sjmc" />
      <el-table-column label="组卷策略" align="center" prop="zjcl" :formatter="zjclFormat"/>
      <!-- <el-table-column label="试卷来源" align="center" prop="sjly" >
        <template slot-scope="scope">
          <p>{{ scope.row.sjly == '1' ? "用户服务中心" : "" }}</p>
          <p>{{ scope.row.sjly == '2' ? "培训管理中心" : "" }}</p>
        </template>
      </el-table-column> -->
      <el-table-column label="标签" align="center" prop="tags" >
        <template slot-scope="scope">
          <span v-for="(item,index) in (scope.row.tags||'').split(',')">
             <el-tag v-if="item!=''" style="margin-right: 3px;margin-bottom:2px">
               {{item}}
             </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['testparperResource:testparperResource:edit']"
          >查看</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['testparperResource:testparperResource:remove']"
          >删除</el-button> -->
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

    <!-- 添加或修改试卷资源管理对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="500px" append-to-body>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//import { listTestparperResource, getTestparperResource, delTestparperResource, addTestparperResource, updateTestparperResource, exportTestparperResource } from "@/api/etp/testparperResource/testparperResource";
import { listTestpaper, getTestpaper, delTestpaper, addTestpaper, updateTestpaper, exportTestpaper } from "@/api/etp/testpaper/testpaper";

import { listTagRelate } from "@/api/etp/tag/tag";
export default {
  name: "TestparperResource",
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
      // 试卷资源管理表格数据
      testparperResourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sjmc: null,
        zjcl: null,
        fm: null,
        tx: null,
        tags:[]
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      tagOptions:[],
      queryTag:[],
      fit:'fill',
      zjclOptions:[],
      sjlyOptions:[]

    };
  },
  created() {
    this.getList();
    this.getTags();
    this.getDicts("testpaper_zjcl").then(response => {
      this.zjclOptions = response.data;
    });
    this.getDicts("courseware_resource").then(res => {
      this.sjlyOptions = res.data;
    });
  },
  methods: {
    /** 查询试卷资源管理列表 */
    getList() {
      this.loading = true;
      listTestpaper(this.queryParams).then(response => {
        this.testparperResourceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sjmc: null,
        zjcl: null,
        fm: null,
        tx: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    // 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷字典翻译
    zjclFormat(row, column) {
      return this.selectDictLabel(this.zjclOptions, row.zjcl);
    },
    getTags(){
      listTagRelate({modelType:"SJ"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if(null != this.queryTag){
        this.queryParams.tags = this.queryTag.join(',')

      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag=[]
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加试卷资源管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTestparperResource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改试卷资源管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTestparperResource(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTestparperResource(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除试卷资源管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTestparperResource(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有试卷资源管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportTestparperResource(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
