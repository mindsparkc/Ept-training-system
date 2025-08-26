<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="证书编号" prop="certId">
        <el-input
          v-model="queryParams.certId"
          placeholder="请输入证书编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证书名称" prop="certName">
        <el-input
          v-model="queryParams.certName"
          placeholder="请输入证书名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon= "el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-close"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['api:record:remove']"
        >证书撤销</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
<!--    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:record:add']"
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
          v-hasPermi="['system:record:edit']"
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
          v-hasPermi="['system:record:remove']"
        >删除</el-button>
      </el-col>
      &lt;!&ndash; el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
		  :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:record:export']"
        >导出</el-button>
      </el-col &ndash;&gt;
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>-->

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="颁发记录编号" align="center" prop="recordId" />-->
<!--      <el-table-column label="证书模板编号" align="center" prop="tempId" />-->
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column label="证书编号" align="center" prop="certId" />
<!--      <el-table-column label="证书预览" align="center" prop="previewFile" >
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            v-show="scope.row.previewFile!=null"
            :src="scope.row.previewFile"
            :preview-src-list="[scope.row.previewFile]">
          </el-image>
        </template>
      </el-table-column>-->
      <el-table-column label="证书名称" align="center" prop="certName" />

      <el-table-column label="证书颁发时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="证书过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="证书所有者" align="center" prop="nickName" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status==99">已发布</span>
          <span v-else-if="scope.row.status==1">撤销</span>
          <span v-else>生成中...</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              v-show="scope.row.status==99"
              @click="viewPic(scope.row)"
            ></el-button>
          </el-tooltip>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['api:record:edit']"
          >修改</el-button>

          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['api:record:remove']"
            ></el-button>
          </el-tooltip>-->
<!--          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-medal"
              @click="viewPic(scope.row)"
              v-hasPermi="['api:template:edit']"
            ></el-button>
          </el-tooltip>-->
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

    <!-- 添加或修改电子证书颁发记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="证书模板编号" prop="tempId">
          <el-input v-model="form.tempId" placeholder="请输入证书模板编号" />
        </el-form-item>
        <el-form-item label="证书编号" prop="certId">
          <el-input v-model="form.certId" placeholder="请输入证书编号" />
        </el-form-item>
        <el-form-item label="证书名称" prop="certName">
          <el-input v-model="form.certName" placeholder="请输入证书名称" />
        </el-form-item>
        <el-form-item label="生成证书原文件">
          <fileUpload v-model="form.certFile"/>
        </el-form-item>
        <el-form-item label="证书预览地址">
          <fileUpload v-model="form.previewFile"/>
        </el-form-item>
        <el-form-item label="证书过期时间" prop="expireTime">
          <el-date-picker clearable size="small"
            v-model="form.expireTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择证书过期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="颁发方式" prop="grantType">
          <el-select v-model="form.grantType" placeholder="请选择颁发方式">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="颁发方式" prop="grantDisplay">
          <el-input v-model="form.grantDisplay" placeholder="请输入颁发方式" />
        </el-form-item>
        <el-form-item label="证书所有者" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入证书所有者" />
        </el-form-item>
        <el-form-item label="证书状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="证书预览" :visible.sync="picOpen" width="1000px" append-to-body>
      <el-image
        style="width: 100%; height: 100%"
        :src="pic_url"
        :preview-src-list="[pic_url]">
      </el-image>
      <div slot="footer" class="dialog-footer">
        <el-button @click="picOpen=false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord, exportRecord } from "@/api/etp/certRecord/record";
import Template from "@/views/etp/certificateTemplate";

export default {
  name: "Record",
  components: {Template},
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
      // 电子证书颁发记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tempId: null,
        certId: null,
        certName: null,
        expireTime: null,
        userId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      picOpen:false,
      pic_url:null,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    viewPic(row){
      this.pic_url = row.previewFile
      this.picOpen = true
    },

    /** 查询电子证书颁发记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
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
        recordId: null,
        tempId: null,
        certId: null,
        certName: null,
        certFile: null,
        previewFile: null,
        expireTime: null,
        grantType: null,
        grantDisplay: null,
        userId: null,
        status: "0",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加电子证书颁发记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids
      getRecord(recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改电子证书颁发记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids;
      this.$confirm('是否确认撤销电子证书颁发记录?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delRecord(recordIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("证书作废成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有电子证书颁发记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportRecord(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
