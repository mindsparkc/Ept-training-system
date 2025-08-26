<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="APP 名称" prop="appName">
        <el-input
          v-model="queryParams.appName"
          placeholder="请输入APP名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="APP描述" prop="appDescribe">
        <el-input
          v-model="queryParams.appDescribe"
          placeholder="请输入APP描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <br>
      <el-form-item label="平台标识">
        <el-radio-group v-model="queryParams.platform" @change="handleQuery">
          <el-radio label="">全部</el-radio>
          <el-radio label="1101">安卓</el-radio>
          <el-radio label="1102">ios</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item style="margin-left: 60%">
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
          v-hasPermi="['app:app:add']"
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
          v-hasPermi="['app:app:edit']"
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
          v-hasPermi="['app:app:remove']"
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
<!--          v-hasPermi="['app:app:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    </el-row>

    <el-table v-loading="loading" :data="appList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键id" align="center" prop="id" />-->
<!--      <el-table-column label="" align="center" prop="" />-->
      <el-table-column label="APP图标" align="center" prop="appIcon">
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.appIcon==null?require('@/assets/images/cover.jpg'):scope.row.appIcon"
            :fit="fit"
          />
        </template>
      </el-table-column>
      <el-table-column label="APP标识" align="center" prop="appCode" />
      <el-table-column label="APP名称" align="center" prop="appName" />
      <el-table-column label="APP描述" align="center" prop="appDescribe" />
      <el-table-column label="平台标识" align="center" prop="platform">
        <template slot-scope="scope">
          {{scope.row.platform==1101?'安卓':'ios'}}
        </template>
      </el-table-column>
      <el-table-column label="最新版本号" align="center" prop="versionCode" />
      <el-table-column label="最新版本描述" align="center" prop="versionDescribe" />
<!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180">&ndash;&gt;-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {hh}:{mm}:{ss}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="创建者" align="center" prop="createBy" />-->
<!--      <el-table-column label="修改者" align="center" prop="updateBy" />-->
<!--      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="APP更新" placement="top">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-plus-outline"
            @click="updateAppInfo(scope.row)"
          ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="详情" placement="top">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-folder-opened"
            @click="handleInfo(scope.row)"
          ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:app:edit']"
          ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:app:remove']"
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

    <!-- 添加或修改app管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="APP标识" prop="appCode" >
          <el-input v-model="form.appCode" placeholder="请输入APP标识" />
        </el-form-item>
        <el-form-item label="APP名称" prop="appName" >
          <el-input v-model="form.appName" placeholder="请输入APP名称" />
        </el-form-item>
        <el-form-item label="APP描述" prop="appDescribe">
          <el-input v-model="form.appDescribe" placeholder="请输入APP描述" />
        </el-form-item>
        <el-form-item label="APP图标" prop="appIcon">
          <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.appIcon"></imageUpload>
        </el-form-item>
        <el-form-item label="平台标识">
          <el-radio-group v-model="form.platform">
            <el-radio :label="'1101'">安卓</el-radio>
            <el-radio :label="'1102'">ios</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm(0)">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="updateOpen" width="700px" append-to-body>
      <el-form ref="updateForm" :model="updateForm" :rules="updateRules" label-width="80px">
        <!--        <el-form-item label="APP主键" prop="appId">-->
        <!--          <el-input v-model="form.appId" placeholder="请输入APP 主键" />-->
        <!--        </el-form-item>-->
        <el-form-item label="版本号" prop="versionCode">
          <el-input v-model="updateForm.versionCode" type="number" oninput="value=value.replace(/[^0-9]/g,'')" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="updateForm.versionName"  placeholder="请输入版本名称" />
        </el-form-item>
        <el-form-item label="版本描述" prop="versionDescribe" >
          <el-input v-model="updateForm.versionDescribe" type="textarea" placeholder="请输入版本升级描述" rows="4"/>
        </el-form-item>
        <el-form-item label="app文件" prop="download">
          <el-input v-if="updateForm.platform=='1102'" v-model="updateForm.download" placeholder="请输入链接地址" />
          <fileUpload v-else ref="fileUpload" :limit="1" :uploadUrl="'/commom/upload/fastdfs'"
                      :accept="'.apk'"
                      :file-type="['apk']"
                      v-model="updateForm.download" @getFileType="getFileType"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="updateForm.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm(1)">确 定</el-button>
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
import { listApp, getApp, delApp, addApp, updateApp, exportApp, getAppName, getVersionCode } from "@/api/app/app/app";
import imageUpload from "@/components/ImageUpload";
import DelDialog from "@/components/DelDialog"
import { addAppVersion } from '@/api/app/app/appVersion'


export default {
  name: "App",
  components:{
    imageUpload,
    DelDialog
  },
  data() {
    //校验app标识是否存在
    var checkCode = (rule, value, callback) => {
      debugger
      if (!value) {
        callback(new Error('APP标识不能为空'));
      }else if(this.ckFlag){
        //校验app标识是否重复
        getAppName(value).then(res =>{
          if(res.data!==0){
            callback(new Error("APP标识已存在！"))
          }else{
            callback()
          }
        })
      }else {
        callback()
      }
    };
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
      // app管理表格数据
      appList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appName: null,
        appDescribe: null,
        platform: '',
      },
      // 表单参数
      form: {},
      // 新增APP详情参数
      updateForm: {appId:'',platform:'',lastVersionCode:'',fileName:''},
      //新增APP详情弹窗
      updateOpen: false,
      platform:'1101',
      ckFlag : false,
      // 表单校验
      rules: {
        appCode: [
          { required: true, validator: checkCode, trigger: 'blur' },
        ],
        appName: [
          { required: true, message: '请输入APP名称', trigger: 'blur' },
        ],
        appDescribe: [
          { required: true, message: '请输入APP描述', trigger: 'blur' },
        ],
        platform: [
          { required: true, trigger: 'blur' },
        ],
      },
      //新增APP详情表单校验
      updateRules:{
        versionCode:[
          { required: true, message: '请输入APP版本号，版本号需大于当前版本！', trigger: 'blur'}
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
      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    //删除方法
    deleteInfo(ids){
      delApp(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },

    /** 父子通信 */
    getFileType(file){
      let url = URL.createObjectURL(file);
      let audioElement = new Audio(url);
      let duration;
      duration = audioElement.duration;
      audioElement.addEventListener("loadedmetadata",  () => {
        duration = audioElement.duration;
        this.$set(this.form,"learnTime",Math.ceil(duration / 60))
      })
    },

    /** 新增按钮操作 */
    updateAppInfo(row) {
      this.updateReset()
      this.updateForm.appId = row.id
      this.updateForm.platform = row.platform
      this.updateForm.lastVersionCode = row.versionCode;
      this.updateOpen = true;
      this.title = "更新app版本";
    },

    /** 跳转至app详情页面 **/
    handleInfo(row) {
      console.log(row);
      this.$router.push({
        path:"/etp/app/appVersion",
        query:{appId:row.id,platform:row.platform}
      })
    },

    /** 查询app管理列表 */
    getList() {
      this.loading = true;
      listApp(this.queryParams).then(response => {
        this.appList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.updateOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        describe: null,
        icon: null,
        platform: null,
        delFlag: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },

    // 表单重置
    updateReset() {
      this.updateForm = {
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
      this.ckFlag = true;
      this.reset();
      this.form.platform = this.platform;
      this.open = true;
      this.title = "添加app管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getApp(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改app管理";
      });
    },
    /** 提交按钮 */
    submitForm(val) {
      if(val==0){
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateApp(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApp(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
      }else{
        getVersionCode(this.updateForm.appId,this.updateForm.versionCode).then(res => {
          if(res.data>0){
            this.msgError("APP版本号已存在!")
            this.updateForm.versionCode = '';
            return
          }else if(this.updateForm.versionCode<this.updateForm.lastVersionCode){
            this.msgError("输入版本号需大于当前版本号！");
            this.updateForm.versionCode = '';
            return
          }else{
            this.$refs["updateForm"].validate(valid => {
              if (valid) {
                this.updateForm.fileName = this.$refs.fileUpload.fileList[0].fileName;
                addAppVersion(this.updateForm).then(response => {
                  this.msgSuccess("更新App版本成功");
                  this.updateOpen = false;
                  this.getList();
                });
              }

            });
          }
        })
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"APP名称":row.appName,"APP标识":row.appCode}
        this.delInfo = info
      }
      this.delVisible = true
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有app管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportApp(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
