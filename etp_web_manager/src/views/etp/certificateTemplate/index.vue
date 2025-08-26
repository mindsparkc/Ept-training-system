<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="证书名称" prop="certName">
        <el-input
          v-model="queryParams.certName"
          placeholder="请输入证书名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="颁发机构" prop="authority">
        <el-input
          v-model="queryParams.authority"
          placeholder="请输入证书颁发机构"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="证书状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择证书状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>-->
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
          v-hasPermi="['api:template:add']"
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
          v-hasPermi="['api:template:edit']"
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
          v-hasPermi="['api:template:remove']"
        >删除</el-button>
      </el-col>
      <!-- el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
		  :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:template:export']"
        >导出</el-button>
      </el-col -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="证书名称" align="center" prop="certName" />
<!--      <el-table-column label="证书预览" align="center" prop="certName" >
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.previewFile"
            :preview-src-list="[scope.row.previewFile]">
          </el-image>
        </template>
      </el-table-column>-->
      <el-table-column label="证书颁发机构" align="center" prop="authority" />
      <el-table-column label="证书有效期(年)" align="center" prop="expireYear" />
      <el-table-column label="更新时间" align="center" prop="updateTime" />
      <el-table-column label="证书状态" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="证书描述" align="center" prop="content" width="200" :show-tooltip-when-overflow="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['api:template:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              v-show="scope.row.status==99"
              @click="viewPic(scope.row)"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="颁发" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-medal"
              @click="publish(scope.row)"
              v-hasPermi="['api:template:edit']"
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

    <!-- 添加或修改电子证书模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-row :gutter="20">
        <el-col :span="16">
          <div class="area">
            <div class="title">基础信息</div>
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
              <el-form-item label="证书名称" prop="certName">
                <el-input v-model="form.certName" placeholder="请输入证书名称" />
              </el-form-item>

              <el-form-item label="颁发机构" prop="authority">
                <el-input v-model="form.authority" placeholder="请输入证书颁发机构" />
              </el-form-item>
              <el-form-item label="有效期" prop="expireYear">
                <el-radio-group v-model="radio">
                  <el-radio :label="0" >终身有效</el-radio>
                  <el-radio :label="1">短期有效</el-radio>
                </el-radio-group>
                <el-input-number v-model="year" :min="1" style="width: 120px;margin-left: 15px;" v-show="radio==1"></el-input-number> <span v-show="radio==1" style="color: #1c1c1c"> 年</span>
              </el-form-item>
              <!--        <el-form-item label="颁发方式" prop="grantType">
                        <el-select v-model="form.grantType" placeholder="请选择颁发方式">
                          <el-option label="请选择字典生成" value="" />
                        </el-select>
                      </el-form-item>-->
              <el-form-item label="证书描述">
                <el-input v-model="form.content" placeholder="请输入证书描述" type="textarea" />
              </el-form-item>

              <el-form-item label="证书模板" prop="tempFile">
                <el-upload
                  class="upload-demo"
                  drag
                  accept=".docx"
                  :multiple="false"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :on-remove="handleUploadRemove"
                  :before-remove="beforeRemove"
                  :headers="upload.headers"
                  :action="upload.url"
                  :file-list="template"
                  :limit="1"
                  :on-exceed="handleExceed"
                >
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div class="el-upload__tip" slot="tip">限一个模板，只能上传docx文件</div>
                </el-upload>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="area">
            <div class="title">证书说明</div>
            <span><i class="el-icon-info"></i> 证书内容通过文本替换的方式，将上传的docx文件中的固定变量名称替换成具体的值，变量名称请严格参照以下声明的，区分大小写！</span>
            <p>${CERT_NAME}  证书名称</p>
            <p>${CERT_NO}  证书编号</p>
            <p>${USER_NAME}  用户姓名</p>
            <p>${CERT_AUTHORITY}  证书颁发机构</p>
            <p>${START_TIME}  证书颁发时间</p>
            <p>${CERT_EXPIRETIME}  证书有效期</p>
            <el-button type="primary" @click="downLoad" style="width: 100%;margin-top: 20px;">下载模板</el-button>
          </div>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" >确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 证书颁发 -->
    <el-dialog title="证书颁发" :visible.sync="userOpen" width="1000px" append-to-body>
      <el-form ref="userParams" :model="userParams" :inline="true" v-show="showSearch" label-width="100px">
        <el-form-item label="归属部门" prop="deptId">
          <treeselect  v-model="userParams.deptId"
                       :options="deptOptions"
                       :show-count="true"
                       style="width: 240px"
                       placeholder="请选择归属部门"
                       :flat="true"
          />
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input
            v-model="userParams.userName"
            placeholder="请输入用户名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="userHandleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="userResetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="userloading" :data="userList" @selection-change="userSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="55" align="center" />
        <el-table-column label="用户名称" align="center" key="userName" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName" :show-overflow-tooltip="true" />
        <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName" :show-overflow-tooltip="true" />
      </el-table>
      <pagination
        v-show="userTotal>0"
        :total="userTotal"
        :page.sync="userParams.pageNum"
        :limit.sync="userParams.pageSize"
        @pagination="getUserList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUser">确 定</el-button>
        <el-button @click="userCancel">取 消</el-button>
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
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate, exportTemplate, submitPublish } from "@/api/etp/certificateTemplate/template";
import {getToken} from "@/utils/auth";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listUser } from "@/api/system/user";
export default {
  name: "Template",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      fullscreenLoading: false,
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
      // 电子证书模板表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 证书状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        certName: null,
        authority: null,
        status: null,
      },
      userOpen: false,
      userloading: true,
      userTotal: 0,
      userList: [],
      selUsers: [],
      userParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        deptId: null,
      },
      // 表单参数
      form: {
        grantType: 1,
        grantDisplay: "人工颁发",
        expireYear:null
      },
      upload:{
        url: process.env.VUE_APP_BASE_API + "/common/upload",
        headers: {
          Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'
        },
      },
      template:[],
      radio: 1,
      year: 1,
      // 表单校验
      rules: {
        certName:[{ required: true, message: '证书名称不能为空', trigger: 'blur'}],
        authority:[{ required: true, message: '颁发机构不能为空', trigger: 'blur'}],
        expireYear:[{ required: true, message: '有效期不能为空', trigger: 'blur'}],
        tempFile:[{ required: true, message: '证书模板不能为空', trigger: 'blur'}],
      },

      // 部门树选项
      deptOptions: undefined,

      picOpen:false,
      pic_url:null,
    };
  },
  watch:{
    radio: {
      handler (val){
        if(val==0){
          this.form.expireYear = 0
        }
      },
      deep:true
    },
    year(val){
      this.form.expireYear = val
    }

  },

  created() {
    this.getList();
    this.getDicts("certificate_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getTreeselect()
  },
  methods: {
    viewPic(row){
      this.pic_url = row.previewFile
      this.picOpen = true
    },

    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        let parentData = response.data;
        this.deptOptions = parentData;
      });
    },
    /** 查询电子证书模板列表 */
    getList() {
      this.loading = true;
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 证书状态字典翻译
    statusFormat(row, column) {
      // let status = this.selectDictLabel(this.statusOptions, row.status);
      if(row.status == '99'){
        return "已生成";
      }else{
        return "生成中";
      }

    },
    userHandleQuery(){
      this.userParams.pageNum = 1;
      this.getUserList();
    },
    /** 查询用户列表 */
    getUserList() {
      this.userloading = true;
      listUser(this.userParams).then(response => {
          this.userList = response.rows;
          this.userTotal = response.total;
          this.userloading = false;
        }
      );
    },

    //用户 多选框选中数据
    userSelectionChange(selection) {
      this.selUsers = selection.map(item => item.userId)
    },
    //证书颁发
    publish(row){
      this.reset()
      this.getUserList()
      this.userOpen = true
      this.form.tempId = row.tempId
    },
    submitUser(){
      if(this.selUsers.length>0){
        let params={templateId:this.form.tempId,userIds:this.selUsers.join(",")}
        //提交证书颁发
        submitPublish(params).then(res => {
          this.userOpen = false
          this.$message.success("提交成功，证书构建中")
          this.userHandleQuery()
        })

      }else{
        this.$message.warning("请至少选择一个用户")
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tempId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加电子证书模板";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const tempId = row.tempId || this.ids
      getTemplate(tempId).then(response => {
        this.form = response.data;
        this.year = this.form.expireYear
        this.template = [{name:this.form.fileName,url:this.form.tempFile}]
        this.open = true;
        this.title = "修改电子证书模板";
      });
    },
    /** 提交按钮 */
    submitForm() {

      this.$refs["form"].validate(valid => {
        if (valid) {

          if (this.form.tempId != null) {
            updateTemplate(this.form).then(response => {
              this.$message.success("修改成功！证书生成中，请稍后刷新查看")
              this.open = false;
              this.getList();
            });
          } else {
            addTemplate(this.form).then(response => {
              this.$message.success("新增成功！证书生成中，请稍后刷新查看")
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const tempIds = row.tempId || this.ids;
      this.$confirm('是否确认删除电子证书模板编号为"' + tempIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTemplate(tempIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有电子证书模板数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportTemplate(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    handleUploadSuccess(res, file) {

      if(res.code==200){
        this.form.tempFile = res.url
        this.form.fileName = file.name
        this.$message.success("上传成功");
        //转化图片

      }else{
        this.$message.warning(res.msg);
      }

    },
    handleUploadError(){
      this.$message.error("上传失败, 请重试");
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    handleExceed(file, fileList) {
      this.$message.warning("限制只能上传一个文件，目前已经上传了")
    },
    handleUploadRemove(){
      this.form.tempFile=null
      this.clearFiles()
    },
    downLoad(){
      window.location.href = process.env.VUE_APP_BASE_API + "/api/v1/certificate/template"

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消按钮
    userCancel() {
      this.userOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        tempId: null,
        certName: null,
        content: null,
        authority: null,
        expireYear: 1,
        grantType: 1,
        grantDisplay: "人工颁发",
        tempFile: null,
        fileName:null,
        previewFile: null,
        status: "0",
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.template = []
      this.year = 1
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
    /** 重置按钮操作 */
    userResetQuery() {
      this.resetForm("userParams");
      this.userHandleQuery();
    },
  }
};
</script>
<style lang="scss" scoped>
.area{
  width: 100%;
  height: 600px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  span{
    color: #1890ff;
  }
}
::v-deep .title{
  margin: 0px auto 20px auto;
  text-align: left;
  font-size: 20px;
  color: #000;
  font-weight: 800;
}
</style>
