<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="是否考试" prop="sfks">
        <el-select v-model="queryParams.sfks" placeholder="请选择是否考试" clearable size="small">
          <el-option
            v-for="dict in sfksOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否练习" prop="sflx">
        <el-select v-model="queryParams.sflx" placeholder="请选择是否练习" clearable size="small">
          <el-option
            v-for="dict in sflxOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="题型" prop="lx" class="tags-div" style="width: calc(100% - 256px);">
        <el-radio-group v-model="queryParams.lx">
          <el-radio v-for="dict in lxOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="试卷来源:"  class="tags-div" prop="kjly" style="width: calc(100% - 256px);">
        <el-radio-group v-model="queryParams.kcly">
          <el-radio v-for="dict in tklyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
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
          v-hasPermi="['subjectResource:subjectResource:add']"
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
          v-hasPermi="['subjectResource:subjectResource:edit']"
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
          v-hasPermi="['subjectResource:subjectResource:remove']"
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
          v-hasPermi="['subjectResource:subjectResource:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="subjectResourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column width="60">
        <template slot-scope="scope">
          <div v-if="scope.row.sfks=='1'" class="kao-tag">考</div>
          <div v-if="scope.row.sflx=='1'" class="lian-tag">练</div>
        </template>
      </el-table-column>
      <el-table-column label="题干" align="center" prop="tg" />
      <el-table-column label="题型" align="center" prop="lx" :formatter="lxFormat"/>
      <el-table-column label="标签" align="center" prop="tags" >
        <template slot-scope="scope">
          <span v-for="(item,index) in (scope.row.tags||'').split(',')">
             <el-tag v-if="item!=''" style="margin-right: 3px;margin-bottom:2px">
               {{item}}
             </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="试卷来源" align="center" prop="ly" >
        <template slot-scope="scope">
          <p>{{ scope.row.ly == '1' ? "用户服务中心" : "" }}</p>
          <p>{{ scope.row.ly == '2' ? "培训管理中心" : "" }}</p>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['subjectResource:subjectResource:edit']"
          >查看</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['subjectResource:subjectResource:remove']"
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

    <!-- 添加或修改题库资源管理对话框 -->
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
import { listSubjectResource, getSubjectResource, delSubjectResource, addSubjectResource, updateSubjectResource, exportSubjectResource } from "@/api/etp/subjectResource/subjectResource";
import { listTagRelate } from "@/api/etp/tag/tag";
export default {
  name: "SubjectResource",
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
      // 题库资源管理表格数据
      subjectResourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tg: null,
        lx: null,
        xx: null,
        da: null,
        jx: null,
        sflx: null,
        sfks: null,
        ly: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      lxOptions:[],
      sflxOptions:[],
      sfksOptions:[],
      tklyOptions:[],
      queryTag:[],
      tagOptions:[]
    };
  },
  created() {
    this.getTags();
    this.getList();
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    });
    this.getDicts("subject_yes_no").then(response => {
      this.sflxOptions = response.data;
      this.sfksOptions = response.data;
    });
    this.getDicts("courseware_resource").then(res => {
      this.tklyOptions = res.data;
    });
  },
  methods: {
    /** 查询题库资源管理列表 */
    getList() {
      this.loading = true;
      listSubjectResource(this.queryParams).then(response => {
        this.subjectResourceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getTags(){
      listTagRelate({modelType:"TK"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
        // 类型：
    lxFormat(row, column) {
      return this.selectDictLabel(this.lxOptions, row.lx);
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        tg: null,
        lx: null,
        xx: null,
        da: null,
        jx: null,
        sflx: null,
        sfks: null,
        ly: null,
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
      this.title = "添加题库资源管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSubjectResource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改题库资源管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSubjectResource(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSubjectResource(this.form).then(response => {
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
      this.$confirm('是否确认删除题库资源管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSubjectResource(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有题库资源管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportSubjectResource(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
<style  lang="scss" scoped >
.el-dialog .el-input{
  width: 832px!important;
}
.el-icon-remove-outline{
  margin-left: 10px;
  color: red;
  cursor: pointer;
}
.add-btn{
  width: 832px;
  border: 1px dashed #DCDFE6;
  margin: -10px 0px 22px 100px;
}

.labNone ::v-deep .el-form-item__label ,.labNone ::v-deep .el-form-item__label:before{
    color:#fff!important;
}
.el-textarea{
  width: 832px!important;
}

.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
.kao-tag{
  text-align: center;
  border: 1px solid #d1e9ff;
  height: 20px;
  width: 20px;
  border-radius: 50%;
  background: #18abff;
  color: #ffffff;
  line-height: 20px;
  display: inline-block;
}
.lian-tag{
  text-align: center;
  border: 1px solid #ace5c6;
  height: 20px;
  width: 20px;
  border-radius: 50%;
  background: #13ce66;
  color: #ffffff;
  line-height: 20px;
  display: inline-block;
}
</style>
