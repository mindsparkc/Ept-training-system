<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="课程名称:" prop="mc">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入课程名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程描述:" prop="ms">
        <el-input
          v-model="queryParams.ms"
          placeholder="请输入课程相关描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="课程来源:"  class="tags-div" prop="kjly" style="width: calc(100% - 256px);">
        <el-radio-group v-model="queryParams.kcly">
          <el-radio v-for="dict in kclyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <el-checkbox-group v-model="queryTag">
          <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
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
          v-hasPermi="['curriculumresource:curriculumresource:add']"
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
          v-hasPermi="['curriculumresource:curriculumresource:edit']"
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
          v-hasPermi="['curriculumresource:curriculumresource:remove']"
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
          v-hasPermi="['curriculumresource:curriculumresource:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="curriculumresourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="封面图" align="center" width="200" prop="fmt" >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fmt==null?require('@/assets/images/cover.jpg'):scope.row.fmt"
            :fit="fit"
            />
        </template>
      </el-table-column>
      <el-table-column label="课程名称" align="center" prop="mc" />
      <el-table-column label="课程描述" align="center" prop="ms"  >
        <template  slot-scope="scope">
          <el-tooltip effect="dark" :content="scope.row.ms" placement="bottom-end">
            <p class="desc">{{ scope.row.ms }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <!-- <el-table-column label="课程来源" align="center" prop="kcly" >
        <template slot-scope="scope">
          <p>{{ scope.row.kcly == '1' ? "用户服务中心" : "" }}</p>
          <p>{{ scope.row.kcly == '2' ? "培训管理中心" : "" }}</p>
        </template>
      </el-table-column> -->
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
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              type="text"
              icon="el-icon-view"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['curriculumresource:curriculumresource:edit']"
            ></el-button>
          </el-tooltip>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['curriculumresource:curriculumresource:remove']"
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

    <!-- 添加或修改课程资源管理对话框 -->
    <el-dialog
      :title="title"
      :close-on-click-modal="false"
      :visible.sync="open"
      width="500px" append-to-body>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCurriculum,delCurriculum,addCurriculum,exportCurriculum,pushBack } from "@/api/etp/curriculum/curriculum";

import {listTagRelate} from "@/api/etp/tag/tag";
export default {
  name: "Curriculumresource",
  data() {
    return {
      fit: 'cover',
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
      // 课程资源管理表格数据
      curriculumresourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fmt: null,
        kcly: null,
        tags:[]
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      kclyOptions:[],
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],

    };
  },
  created() {
    this.getTags()
    this.getList();
    this.getDicts("courseware_resource").then(res => {
      this.kclyOptions = res.data;
    });
  },
  methods: {
    /** 查询课程资源管理列表 */
    getList() {
      this.loading = true;
      listCurriculum(this.queryParams).then(response => {
        this.curriculumresourceList = response.rows;
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
        mc: null,
        ms: null,
        fmt: null,
        kcly: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "KC"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
    // 标签相关结束
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
      this.queryTag = [];
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
      this.title = "添加课程资源管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row.id)
      this.$router.push({
        path:"/etp/curriculumresource/info/",
        query:{
          id:row.id
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCurriculumresource(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCurriculumresource(this.form).then(response => {
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
      this.$confirm('是否确认删除课程资源管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCurriculumresource(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有课程资源管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportCurriculumresource(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
<style scoped>
.desc {
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
</style>
