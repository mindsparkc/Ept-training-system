<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="版本号" prop="versionCode">
        <el-input
          v-model="queryParams.versionCode"
          placeholder="请输入版本号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号名称" prop="versionName">
        <el-input
          v-model="queryParams.versionName"
          placeholder="请输入版本号名称"
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
          v-hasPermi="['app:appVersion:add']"
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
          v-hasPermi="['app:appVersion:edit']"
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
          v-hasPermi="['app:appVersion:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--		  :loading="exportLoading"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['app:appVersion:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appVersionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键id" align="center" prop="id" />-->
<!--      <el-table-column label="APP 主键" align="center" prop="appId" />-->
      <el-table-column label="版本号" align="center" prop="versionCode" />
      <el-table-column label="版本号名称" align="center" prop="versionName" />
      <el-table-column label="版本升级描述" align="center" prop="versionDescribe" />
      <el-table-column label="下载地址" align="center" prop="download" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['app:appVersion:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['app:appVersion:remove']"
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

    <!-- 添加或修改app版本管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="APP主键" prop="appId">-->
<!--          <el-input v-model="form.appId" placeholder="请输入APP 主键" />-->
<!--        </el-form-item>-->
        <el-form-item label="版本号" prop="versionCode">
          <el-input v-model="form.versionCode" type="number" oninput="value=value.replace(/[^0-9]/g,'')" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="form.versionName"  placeholder="请输入版本名称" />
        </el-form-item>
        <el-form-item label="版本描述" prop="versionDescribe" >
          <el-input v-model="form.versionDescribe" type="textarea" placeholder="请输入版本升级描述" rows="4"/>
        </el-form-item>
        <el-form-item label="app文件" prop="download">
          <el-input v-if="this.platform=='1102'" v-model="form.download" placeholder="请输入链接地址" />
          <fileUpload v-else ref="fileUpload" :limit="1" :uploadUrl="'/commom/upload/fastdfs'"
                      :accept="'.apk'"
                      :file-type="['apk']"
                      v-model="form.download" @getFileType="getFileType"/>
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

    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
  </div>
</template>

<script>
import { listAppVersion, getAppVersion, delAppVersion, addAppVersion, updateAppVersion, exportAppVersion ,getNewApp} from "@/api/app/app/appVersion";
import fileUpload from "@/components/FileUpload";
import DelDialog from "@/components/DelDialog"
import { delApp } from '@/api/app/app/app'

export default {
  name: "AppVersion",
  components:{
    fileUpload,
    DelDialog
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
      // app版本管理表格数据
      appVersionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        versionCode: null,
        versionName: null,
        appId:this.appId,
      },
      // 表单参数
      form: {appId:'',fileName:''},
      // 表单校验
      rules: {
        versionCode:[
          { required: true, message: '请输入APP版本号', trigger: 'blur'}
        ],
        versionName:[
          { required: true, message: '请输入APP版本名称', trigger: 'blur'}
        ],
        versionDescribe:[
          { required: true, message: '请输入APP版本升级描述', trigger: 'blur'}
        ],
        download:[
          { required: true, message: '请输入下载地址', trigger: 'blur'}
        ]
      },
      platform:'',
      appId:'',
      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
    };
  },
  created() {
    this.appId = this.$route.query.appId;
    this.platform = this.$route.query.platform;
    this.getList();
  },
  methods: {

    //删除方法
    deleteInfo(ids){
      delAppVersion(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },

    /** 父子通信 */
    getFileType(file){
      debugger
      let url = URL.createObjectURL(file);
      let audioElement = new Audio(url);
      let duration;
      duration = audioElement.duration;
      audioElement.addEventListener("loadedmetadata",  () => {
        duration = audioElement.duration;
        this.$set(this.form,"learnTime",Math.ceil(duration / 60))
      })
    },

    /** 查询app版本管理列表 */
    getList() {
      this.loading = true;
      this.queryParams['appId'] = this.appId;
      this.queryParams['platform'] = this.platform;
      console.log(this.queryParams);
      listAppVersion(this.queryParams).then(response => {
        this.appVersionList = response.rows;
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
        appId: null,
        versionCode: null,
        versionName: null,
        versionDescribe: null,
        download: null,
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
      this.title = "添加app版本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAppVersion(id).then(response => {
        this.form = response.data;
        this.$nextTick(_ => {
          this.$refs.fileUpload.fileList = [
            {
              name: this.form.fileName,
              url: this.form.download,
              fileName: this.form.fileName
            }
          ];
        })
        this.open = true;
        this.title = "修改app版本";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.fileName = this.$refs.fileUpload.fileList[0].fileName;
          if (this.form.id != null) {
            updateAppVersion(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appId = this.appId;
            this.form.fileName = this.$refs.fileUpload.fileList[0].fileName;
            addAppVersion(this.form).then(response => {
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
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"版本号":row.versionCode,"版本号名称":row.versionName}
        this.delInfo = info
      }
      this.delVisible = true
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有app版本管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportAppVersion(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
