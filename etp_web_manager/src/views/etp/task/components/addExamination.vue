<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="90px">
      <el-form-item label="考试名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          clearable
          placeholder="请输入考试名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布状态" prop="fb">
        <el-select v-model="queryParams.fb" clearable placeholder="请选择发布状态" size="small">
          <el-option
            v-for="dict in fbOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="开放程度" prop="kfcd">
        <el-select v-model="queryParams.kfcd" clearable placeholder="请选择开放程度" size="small">
          <el-option
            v-for="dict in kfcdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>-->
      <el-form-item class="tags-div" label="标签" prop="queryTag" style="width: calc(100% - 256px);">
        <el-checkbox-group v-model="queryTag">
          <el-checkbox v-for="item in tagOptions" :label="item.id"> {{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="examinationList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column :show-overflow-tooltip="true" align="center" label="考试名称" prop="mc" />
      <el-table-column align="center" label="封面" prop="fm" >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="标签" prop="tags" >
        <template slot-scope="scope">
          <span v-for="(item,index) in (scope.row.tags||'').split(',')">
             <el-tag v-if="item!=''" style="margin-right: 3px;margin-bottom:2px">
               {{item}}
             </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column :formatter="fbFormat" align="center" label="发布状态" prop="fb" >
        <template slot-scope="scope">
          <el-tag :type="scope.row.fb == '0' ? 'info' : 'success'">{{fbFormat(scope.row)}}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column :formatter="kfcdFormat" align="center" label="开放程度" prop="kfcd" >
        <template slot-scope="scope">
          <el-tag :type="scope.row.kfcd == '0' ? 'primary' : 'warning'">{{kfcdFormat(scope.row)}}</el-tag>
        </template>
      </el-table-column>-->
      <el-table-column align="center" label="更新时间" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" align="center" label="考试描述" prop="ms"/>
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { listExamination, getExamination, delExamination, addExamination, updateExamination, exportExamination } from "@/api/etp/examination/examination";
import { listTagRelate } from "@/api/etp/tag/tag";
export default {
  name: "addExamination",
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
      // 考试表格数据
      examinationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 发布状态：0-暂存，1-发布字典
      fbOptions: [],
      // 开放程度：完全公开-0，限定用户-1字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fm: null,
        fb: null,
        kslj: null,
        kfcd: null,
        sjid: null,
        kskssj: null,
        ksjssj: null,
        djsc: null,
        dtcs: null,
        fsdj: null,
        zfs: null,
      },
      // 表单参数
      form: {},
      tagOptions:[],
      queryTag:[]
    };
  },
  created() {
    this.getDicts("examination_fb").then(response => {
      this.fbOptions = response.data;
    });
    this.getDicts("examination_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
  },
  methods: {

    getExamsByIds(){
      let datas = [];
      this.ids.forEach(item => {
        this.examinationList.forEach(exam => {
          if (item == exam.id) {
            exam['source_name'] = exam.mc
            exam['source_type'] = 'ks';
            datas.push(exam)
          }
        })
      })
      return datas;
    },

    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExamination(this.queryParams).then(response => {
        this.examinationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getTags(){
      listTagRelate({modelType:"KS"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    // 发布状态：0-暂存，1-发布字典翻译
    fbFormat(row, column) {
      return this.selectDictLabel(this.fbOptions, row.fb);
    },
    // 开放程度：完全公开-0，限定用户-1字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
    },


    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = []
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },


  }
};
</script>
<style lang="scss" scoped>
.tags-div ::v-deep .el-form-item__content {
  width: calc(100% - 100px) !important;
}
</style>
