<template>
  <div class="app-container">
    <el-form @submit.native.prevent :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="问卷名称" prop="wjmc">
        <el-input
          v-model="queryParams.wjmc"
          placeholder="请输入问卷名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <div style="display: flex">
          <template style="position: relative">
            <el-checkbox v-model="checkAll" @change="handleCheckAllChange" >全部数据</el-checkbox>
            <el-checkbox-group v-model="queryTag" ref="tag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
              <el-checkbox :label="item.id" v-for="item in tagOptions" name="tag" @change="handleQuery">{{item.name}}</el-checkbox>
            </el-checkbox-group>
            <div @click="openFop" v-if="tipFlag"  style="position: absolute;z-index: 9999;right: -5%;width: 100px;top:0;cursor: pointer;color: #1890ff">
              {{ openFlag?'展开':'收起' }}
            </div>
          </template>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="questionnaireList" @selection-change="handleSelectionChange">
      <!-- el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center"/ -->
      <el-table-column label="封面图" align="center" prop="fm">
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fm"
            :fit="fit"/>
        </template>
      </el-table-column>
      <el-table-column label="问卷名称" align="center" prop="wjmc" :show-overflow-tooltip="true"/>
      <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tags" >
        <template slot-scope="scope">
          <span v-for="(item,index) in (scope.row.tags||'').split(',').slice(0,3)">
             <el-tag v-if="item!=''" style="margin-right: 3px;margin-bottom:2px">
               {{item}}
             </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="wjStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '未开始'">{{scope.row.status}}</el-tag>
          <el-tag v-if="scope.row.status === '进行中'" type="warning">{{scope.row.status}}</el-tag>
          <el-tag v-if="scope.row.status === '已完成'" type="success">{{scope.row.status}}</el-tag>
          <el-tag v-if="scope.row.status === '已结束'" type="info">{{scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status=='已完成'||scope.row.status=='已结束'"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="getDetail(scope.row)"
          >详情</el-button>

          <el-button
            v-if="scope.row.status!='进行中'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="answer(scope.row)"
            disabled
          >填卷</el-button>
          <el-button
            v-if="scope.row.status==='进行中'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="answer(scope.row)"
          >填卷</el-button>
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

    <!-- 添加或修改问卷调查对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="问卷名称" prop="wjmc">
          <el-input v-model="form.wjmc" placeholder="请输入问卷名称" />
        </el-form-item>
        <el-form-item label="启用状态 0 正常 1 禁用" prop="qyzt">
          <el-select v-model="form.qyzt" placeholder="请选择启用状态 0 正常 1 禁用">
            <el-option
              v-for="dict in qyztOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="封面图">
          <imageUpload v-model="form.fm"/>
        </el-form-item>
        <el-form-item label="问卷描述" prop="ms">
          <el-input v-model="form.ms" placeholder="请输入问卷描述" />
        </el-form-item>
        <el-form-item label="发布状态 0 暂存 99 已发布" prop="fb">
          <el-input v-model="form.fb" placeholder="请输入发布状态 0 暂存 99 已发布" />
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker clearable size="small"
            v-model="form.beginTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable size="small"
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开放程度0公开，1指定用户，2不公开" prop="kfcd">
          <el-select v-model="form.kfcd" placeholder="请选择开放程度0公开，1指定用户，2不公开">
            <el-option
              v-for="dict in kfcdOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否匿名0否，1公开" prop="sfnm">
          <el-input v-model="form.sfnm" placeholder="请输入是否匿名0否，1公开" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import {listTagRelate} from "@/api/etp/tag/tag";
import { listQuestionnaire,
  getQuestionnaire,
  delQuestionnaire,
  addQuestionnaire,
  updateQuestionnaire,
  exportQuestionnaire
} from "@/api/etp/questionnaire/questionnaire";

export default {
  name: "Questionnaire",
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
      // 问卷调查表格数据
      questionnaireList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 启用状态 0 正常 1 禁用字典
      qyztOptions: [],
      // 开放程度0公开，1指定用户，2不公开字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wjmc: null,
        qyzt: null,
        ms: null,
        fb: null,
        nr: null,
        beginTime: null,
        endTime: null,
        kfcd: null,
        sfnm: null,
        tags:[]
      },
      queryTag:[],
      tagOptions:[],
      tagValue:[],
      isIndeterminate: true,
      checkAll: true,
      openFlag:true,
      tipFlag:false,
      height:'',
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  activated() {
    this.getList();
  },
  created() {
    this.getList();
    this.getTags();
    this.getDicts("question_status").then(response => {
      this.qyztOptions = response.data;
    });
    this.getDicts("question_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
  },
  methods: {
    handleCheckAllChange(val) {
      this.queryTag = []
      this.handleQuery()
    },

    handleCheckedTagsChange(value) {
      // let checkedCount = value.length;
      this.checkAll = false
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
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

    /** 跳转至答题页面*/
    answer(data) {
      this.$router.push({
        path: "/etp/questionnaire/answer/",
        query: { id: data.id ,tags:data.tags}
      });
    },
    getDetail(row){
      this.$router.push({
        path: "/etp/questionnaire/detail/",
        query: { id: row.id,tags:row.tags}
      });
    },
    /** 查询问卷调查列表 */
    getList() {
      this.loading = true;
      listQuestionnaire(this.queryParams).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if(list[i].fm == null || list[i].fm == '' || list[i].fm == undefined){
            list[i].fm = require("@/assets/images/cover.jpg");
          }
        }
        this.questionnaireList = list;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 启用状态 0 正常 1 禁用字典翻译
    qyztFormat(row, column) {
      return this.selectDictLabel(this.qyztOptions, row.qyzt);
    },
    // 开放程度0公开，1指定用户，2不公开字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
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
        wjmc: null,
        qyzt: null,
        fm: null,
        ms: null,
        fb: null,
        nr: null,
        beginTime: null,
        endTime: null,
        kfcd: null,
        sfnm: null,
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
      this.title = "添加问卷调查";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path:"/etp/questionnaire/"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateQuestionnaire(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addQuestionnaire(this.form).then(response => {
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
      this.$confirm('是否确认删除问卷调查编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delQuestionnaire(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有问卷调查数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportQuestionnaire(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "WJ"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    }
  }
};
</script>

<style scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

</style>
