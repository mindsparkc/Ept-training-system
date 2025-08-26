<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="课件名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入课件名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课件描述" prop="ms">
        <el-input
          v-model="queryParams.ms"
          placeholder="请输入课件描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="课件来源:"  class="tags-div" prop="kjly" style="width: calc(100% - 256px);">
        <el-radio-group v-model="queryParams.kjly">
          <el-radio v-for="dict in kjlyOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="课件形式" class="tags-div" prop="xs" style="width: calc(100% - 256px);">
        <el-radio-group v-model="queryParams.xs">
          <el-radio v-for="dict in xsOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
      </el-form-item>

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
          v-hasPermi="['etp:coursewareresource:add']"
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
          v-hasPermi="['etp:coursewareresource:edit']"
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
          v-hasPermi="['etp:coursewareresource:remove']"
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
          v-hasPermi="['etp:coursewareresource:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="coursewareresourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课件封面" align="center" prop="fm" >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
            :fit="fit"
            />
        </template>
      </el-table-column>
      <el-table-column label="课件名称" align="center" prop="mc" />
      <el-table-column label="课件形式" align="center" prop="xs" >
        <template slot-scope="scope">
          <p>{{ scope.row.xs == '1' ? "OFFICE" : "" }}</p>
          <p>{{ scope.row.xs == '2' ? "视频" : "" }}</p>
          <p>{{ scope.row.xs == '3' ? "富文本" : "" }}</p>
          <p>{{ scope.row.xs == '4' ? "外部链接" : "" }}</p>
          <p>{{ scope.row.xs == '5' ? "其他文件" : "" }}</p>
        </template>
      </el-table-column>
      <!-- <el-table-column label="课件来源" align="center" prop="kjly" >
        <template slot-scope="scope">
          <p>{{ scope.row.kjly == '1' ? "用户服务中心" : "" }}</p>
          <p>{{ scope.row.kjly == '2' ? "培训管理中心" : "" }}</p>
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
      <el-table-column label="课件描述" align="center" prop="ms" >
        <template  slot-scope="scope">
          <el-tooltip effect="dark" :content="scope.row.ms" placement="bottom-end">
            <p class="desc">{{ scope.row.ms }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" />
      <!-- <el-table-column label="仅“课件形式”为3时，该字段有值" align="center" prop="nr" />
      <el-table-column label="1:" align="center" prop="kjly" />
      <el-table-column label="完全公开：0限定用户：1" align="center" prop="kfcd" /> -->
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['etp:coursewareresource:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['etp:coursewareresource:remove']"
          >删除</el-button> -->
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              type="text"
              icon="el-icon-view"
              @click="preview(scope.row)"
              >
            </el-button>
          </el-tooltip>
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

    <!-- 添加或修改课件资源管理对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课件名称" prop="mc">
          <el-input v-model="form.mc" placeholder="请输入课件名称" />
        </el-form-item>
        <el-form-item label="课件描述" prop="ms">
          <el-input v-model="form.ms" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="课件封面" prop="fm">
          <el-input v-model="form.fm" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="课件形式 Office：1
视频：2
富文本：3
外部链接：4
其他文件：5
" prop="xs">
          <el-input v-model="form.xs" placeholder="请输入课件形式 Office：1
视频：2
富文本：3
外部链接：4
其他文件：5
" />
        </el-form-item>
        <el-form-item label="“课件形式”不为3时，该字段有值" prop="lj">
          <el-input v-model="form.lj" placeholder="请输入“课件形式”不为3时，该字段有值" />
        </el-form-item>
        <el-form-item label="仅“课件形式”为3时，该字段有值" prop="nr">
          <el-input v-model="form.nr" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="1:" prop="kjly">
          <el-input v-model="form.kjly" placeholder="请输入1:" />
        </el-form-item>
        <el-form-item label="完全公开：0
限定用户：1
" prop="kfcd">
          <el-input v-model="form.kfcd" placeholder="请输入完全公开：0
限定用户：1
" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
        <el-dialog :visible.sync="contentTextVisible" width="1000px"
               :close-on-click-modal="false"
               :fullscreen="isFullscreen"
               :before-close="handleContentClose" append-to-body>
      <template slot="title">
        <div class="avue-crud__dialog__header">
            <span class="el-dialog__title">
            <span style="display:inline-block;background-color: #3478f5;width:3px;height:20px;margin-right:5px; float: left;margin-top:2px"></span>
              课件预览
            </span>
          <div class="avue-crud__dialog__menu" @click="isFullscreen? isFullscreen = false: isFullscreen = true">
            <i class="el-icon-full-screen"></i>
          </div>
        </div>
      </template>
      <RichTextPreview :infoObj="infoObj"/>
    </el-dialog>
  </div>
</template>

<script>
//import { listCoursewareresource, getCoursewareresource, delCoursewareresource, addCoursewareresource, updateCoursewareresource, exportCoursewareresource } from "@/api/etp/coursewareresource/coursewareresource";
import {
  listCourseware,
  getCourseware,
  delCourseware,
  addCourseware,
  updateCourseware,
  exportCourseware
} from "@/api/etp/courseware/courseware";
import {listTagRelate} from "@/api/etp/tag/tag";
import RichTextPreview from "./components/index"
import {FILE_PREVIEW_URL} from "@/utils/ruoyi";
import imageUpload from "@/components/ImageUpload";
export default {
  name: "Coursewareresource",
    components: {
    imageUpload,
    RichTextPreview
  },
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
      // 课件资源管理表格数据
      coursewareresourceList: [],
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
        fm: null,
        xs: null,
        lj: null,
        nr: null,
        kjly: null,
        kfcd: null,
        tags: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      xsOptions: [],

      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],
      contentTextVisible:false,
      isFullscreen:false,
      infoObj:"",
      fit: "cover",
      kjlyOptions:[]
    };
  },
  created() {
    this.getTags()
    this.getList();
    this.getDicts("courseware_type").then(res => {
      this.xsOptions = res.data;
    });
    this.getDicts("courseware_resource").then(res => {
      this.kjlyOptions = res.data;
    });
  },
  methods: {
    /** 查询课件资源管理列表 */
    getList() {
      this.loading = true;
      listCourseware(this.queryParams).then(response => {
        this.coursewareresourceList = response.rows;
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
        fm: null,
        xs: null,
        lj: null,
        nr: null,
        kjly: null,
        kfcd: null,
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
      listTagRelate({modelType: "KJ"}).then(response => {
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
      if (null != this.queryTag) {
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
    handleContentClose(){
      this.contentTextVisible = false;
      this.isFullscreen = false;
    },
    /** 预览 Office：1  视频：2  富文本：3  外部链接：4  其他文件：5 */
    preview(obj){
      if(obj.xs == '1' || obj.xs == '5' || obj.xs == '2'){
        let url = obj.kjdz;
        window.open(FILE_PREVIEW_URL + '?url=' + encodeURIComponent(window.btoa(url)) )
      }
      if (obj.xs == '3'){
        this.contentTextVisible = true;
        this.infoObj = {
          type:"richText",
          content:obj.nr
        };
      }
      if (obj.xs == '4'){
        window.open(obj.lj)
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加课件资源管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCoursewareresource(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改课件资源管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCoursewareresource(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCoursewareresource(this.form).then(response => {
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
      this.$confirm('是否确认删除课件资源管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCoursewareresource(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有课件资源管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportCoursewareresource(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
<style scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
.desc {
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}
</style>
