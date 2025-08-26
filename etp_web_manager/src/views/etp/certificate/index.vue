<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="证书中文名" prop="mc" label-width="90px">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入证书中文名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="证书英文名" prop="enmc" label-width="90px">
        <el-input
          v-model="queryParams.enmc"
          placeholder="请输入证书英文名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="证书类型" prop="lx" label-width="80px">
        <el-select v-model="queryParams.lx" placeholder="请选择证书类型" clearable size="small">
          <el-option
            v-for="dict in lxOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <div style="display: flex">
          <template style="position: relative">
            <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>
            <el-checkbox-group ref="tag" v-model="queryTag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
              <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
            </el-checkbox-group>
            <div @click="openFop" v-if="tipFlag"  style="position: absolute;right: -3%;width: 50px;top:0;cursor: pointer;color: #1890ff">
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['etp:certificate:add']"
        >新增</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['etp:certificate:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['etp:certificate:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--		  :loading="exportLoading"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['etp:certificate:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="certificateList"
               @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="证书编号" :show-overflow-tooltip="true"  width="100" align="center" prop="id" />
<!--      <el-table-column label="序号" align="center" type="index" />-->
      <el-table-column label="证书中文名" align="center" prop="mc" />
      <el-table-column label="证书英文名" align="center" prop="enmc" />
      <el-table-column label="证书描述" align="center" prop="ms" :show-overflow-tooltip="true"/>
      <el-table-column label="证书底图" align="center" prop="dt">
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.dt"
            :fit="fit"
          />
        </template>
      </el-table-column>
      <el-table-column label="证书类型" align="center" prop="lx" :formatter="lxFormat" />

      <el-table-column label="标签" align="center" prop="tags" >
        <template slot-scope="scope">
          <span v-for="(item,index) in (scope.row.tags||'').split(',').slice(0,3)">
             <el-tag v-if="item!=''" style="margin-right: 3px;margin-bottom:2px">
               {{item}}
             </el-tag>
          </span>
          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null && (scope.row.tags||'').split(',').length > 3"
                  style="margin-right: 3px;margin-bottom:8px">
            ...
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
          <el-button
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['etp:certificate:edit']"
          ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['etp:certificate:remove']"
            ></el-button>
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

    <!-- 添加或修改证书管理对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="证书中文名" prop="mc" label-width="100px">
          <el-input v-model="form.mc" placeholder="请输入证书中文名" maxlength="100" show-word-limit/>
        </el-form-item>
        <el-form-item label="证书英文名" prop="enmc" label-width="100px">
          <el-input v-model="form.enmc" placeholder="请输入证书英文名" maxlength="100" show-word-limit/>
        </el-form-item>
        <el-form-item label="证书描述" prop="ms" label-width="100px">
          <el-input v-model="form.ms" placeholder="请输入证书描述" maxlength="300" show-word-limit/>
        </el-form-item>
        <el-form-item label="设置标签" prop="tagValue" label-width="100px">
          <tagsEdit widthSty="100%"
                    :parentValue="tagValue"
                    :selectOptions="tagOptions"
                    @selectTags="havaTags">

          </tagsEdit>
        </el-form-item>
        <el-form-item label="证书底图" label-width="100px">
          <imageUpload :uploadUrl="'/commom/upload/fastdfs'" v-model="form.dt"/>
        </el-form-item>
        <el-form-item label="证书类型" prop="lx" label-width="100px">
          <el-select v-model="form.lx" placeholder="请选择证书类型">
            <el-option
              v-for="dict in lxOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="删除标志" prop="delFlag">-->
<!--          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />-->
<!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark" label-width="100px">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" maxlength="300" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import tagsEdit from "@/components/tags/tagsEdit";
import {listTagRelate} from "@/api/etp/tag/tag";
import {
  listCertificate,
  getCertificate,
  delCertificate,
  addCertificate,
  updateCertificate,
  exportCertificate
} from "@/api/etp/certificate/certificate";

export default {
  name: "Certificate",
  components:{
    tagsEdit
  },
  data() {
    return {
      fit: "cover",
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
      // 证书管理表格数据
      certificateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 证书类型字典
      lxOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        enmc: null,
        lx: null,
        tags:[]
      },
      queryTag:[],
      tagOptions:[],
      tagValue:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        mc: [
          { required: true, message: "证书中文名不能为空", trigger: "blur" }
        ],
        enmc: [
          { required: true, message: "证书英文名不能为空", trigger: "blur" }
        ],
        ms: [
          { required: true, message: "证书描述不能为空", trigger: "blur" }
        ],
        lx: [
          { required: true, message: "证书类型不能为空", trigger: "change" }
        ],
      },

      height:'',
      openFlag:true,
      tipFlag:false,
      // isIndeterminate: true,
      checkAll: true,
    };
  },
  watch:{
    queryTag:{
      handler(){
        if (null != this.queryTag) {
          this.queryParams.tags = this.queryTag.join(',')
        }
        this.getList();
      },
      deep:true
    },
    queryParams:{
      handler(){
        this.getList();
      },
      deep:true
    },
  },
  created() {
    this.getList();
    this.getTags();
    this.getDicts("certificate_type").then(response => {
      this.lxOptions = response.data;
    });
  },
  methods: {
    handleCheckAllChange(val) {
      this.queryTag = []
    },

    handleCheckedTagsChange(value) {
      this.checkAll = false
      // let checkedCount = value.length;
      // this.checkAll = checkedCount === this.tagOptions.length;
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
    /** 查询证书管理列表 */
    getList() {
      this.loading = true;
      listCertificate(this.queryParams).then(response => {
        this.certificateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 证书类型字典翻译
    lxFormat(row, column) {
      return this.selectDictLabel(this.lxOptions, row.lx);
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
        enmc: null,
        ms: null,
        dt: null,
        lx: null,
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
      this.title = "添加证书管理";
      this.tagValue = [];
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCertificate(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改证书管理";
        if(null != response.data.tags ){
          this.tagValue = response.data.tags.split(",");
        }else{
          this.tagValue = [];
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$confirm('请确认是否保存当前证书信息?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.form.id != null) {
              updateCertificate(this.addTags(this.form,this.tagValue)).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCertificate(this.addTags(this.form,this.tagValue)).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }).catch(() => {});

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除证书名称为【' + row.mc + '】的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCertificate(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有证书管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportCertificate(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "ZS"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
    havaTags(val) {
      this.tagValue = val
      if(this.tagValue.length>5){
        this.$message.error("标签最多只能存在5个！");
        let data = this.tagValue.slice(0,5);
        this.tagValue = data;
        console.log(this.tagValue);
      }
    },
  }
};
</script>

<style scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

</style>
