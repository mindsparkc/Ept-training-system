<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="代码" prop="vipCode">
        <el-input
          v-model="queryParams.vipCode"
          placeholder="请输入代码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="vipName">
        <el-input
          v-model="queryParams.vipName"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['etp:vip:add']"
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
          v-hasPermi="['etp:vip:edit']"
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
          v-hasPermi="['etp:vip:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vipList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="代码" align="center" prop="vipCode" />
      <el-table-column label="名称" align="center" prop="vipName" />
      <el-table-column label="部门数量" align="center" prop="maxDept" />
      <el-table-column label="人员数量" align="center" prop="maxUser" />
      <el-table-column label="课件数量" align="center" prop="maxCourseware" />
      <el-table-column label="课程数量" align="center" prop="maxCurriculum" />
      <el-table-column label="考试数量" align="center" prop="maxExam" />
      <el-table-column label="文件数量" align="center" prop="maxFile" />
      <el-table-column label="文件大小(G)" align="center" prop="maxFileSize" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['etp:vip:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['etp:vip:remove']"
          >删除</el-button>
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

    <!-- 添加或修改租户vip资源控制对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="代码" prop="vipCode">
          <el-input v-model="form.vipCode" placeholder="请输入代码" />
        </el-form-item>
        <el-form-item label="名称" prop="vipName">
          <el-input v-model="form.vipName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="部门数量" prop="maxDept">
          <el-input v-model="form.maxDept" placeholder="请输入允许最大部门数量(0表示不限制)" />
        </el-form-item>
        <el-form-item label="人员数量" prop="maxUser">
          <el-input v-model="form.maxUser" placeholder="请输入允许最大人员数量(0表示不限制)" />
        </el-form-item>
        <el-form-item label="课件数量" prop="maxCourseware">
          <el-input v-model="form.maxCourseware" placeholder="请输入允许最大课件数量(0表示不限制)" />
        </el-form-item>
        <el-form-item label="课程数量" prop="maxCurriculum">
          <el-input v-model="form.maxCurriculum" placeholder="请输入允许最大课程数量(0表示不限制)" />
        </el-form-item>
        <el-form-item label="考试数量" prop="maxExam">
          <el-input v-model="form.maxExam" placeholder="请输入允许最大考试数量(0表示不限制)" />
        </el-form-item>
        <el-form-item label="文件数量">
          <el-input v-model="form.maxFile" placeholder="请输入允许最大文件数量(0表示不限制)" />
        </el-form-item>
        <el-form-item label="文件大小(G)" prop="maxFileSize">
          <el-input v-model="form.maxFileSize" placeholder="请输入允许最大文件大小(G,0表示不限制)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addVip, delVip, exportVip, getVip, listVip, updateVip} from "@/api/system/vip";

  export default {
  name: "Vip",
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
      // 租户vip资源控制表格数据
      vipList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vipCode: null,
        vipName: null,
        maxDept: null,
        maxUser: null,
        maxCourseware: null,
        maxCurriculum: null,
        maxExam: null,
        maxFile: null,
        maxFileSize: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        delFlag: [],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询租户vip资源控制列表 */
    getList() {
      this.loading = true;
      listVip(this.queryParams).then(response => {
        this.vipList = response.rows;
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
        vipCode: null,
        vipName: null,
        maxDept: null,
        maxUser: null,
        maxCourseware: null,
        maxCurriculum: null,
        maxExam: null,
        maxFile: null,
        maxFileSize: null
      };
      this.resetForm("form");
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
      this.title = "添加vip等级";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getVip(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改vip等级";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateVip(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVip(this.form).then(response => {
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
      this.$confirm('是否确认vip等级id为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delVip(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出vip等级数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportVip(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
