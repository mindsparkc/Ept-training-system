<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="false" v-show="showSearch" label-width="68px">
      <el-form-item label="活动名称" prop="hdmc">
        <el-input style="width: 30%;"
          v-model="queryParams.hdmc"
          placeholder="请输入活动名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
                  show-word-limit
                  maxlength="100"

        />
      </el-form-item>
      <el-form-item label="活动状态" prop="status">

        <el-radio-group v-model="queryParams.status">
          <el-radio v-for="dict in hdztOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
<!--        <el-select v-model="queryParams.status" placeholder="请选择活动状态" clearable size="small">
          <el-option
            v-for="dict in hdztOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>-->
      </el-form-item>
      <el-form-item label="开放程度" prop="kfcd">

        <el-radio-group v-model="queryParams.kfcd">
          <el-radio v-for="dict in kfcdOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>
<!--        <el-select v-model="queryParams.kfcd" placeholder="请选择开放程度" clearable size="small">
          <el-option
            v-for="dict in kfcdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>-->
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
          v-hasPermi="['etp:sign:add']"
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
<!--          v-hasPermi="['etp:sign:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['etp:sign:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['etp:sign:export']"
        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="signList"
               @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="活动编号" :show-overflow-tooltip="true"  width="100" align="center" prop="id" sortable/>-->
      <el-table-column label="活动封面" align="center" prop="hdfm" >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.hdfm==null?require('@/assets/images/cover.jpg'):scope.row.hdfm"
            fit="fit"/>
        </template>
      </el-table-column>
      <el-table-column label="活动标题" align="center" prop="hdmc" :show-overflow-tooltip="true"/>
      <el-table-column label="活动副标题" align="center" prop="hdfmc" :show-overflow-tooltip="true"/>
      <el-table-column label="标签" align="center" prop="tags">
        <template slot-scope="scope">
          <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="kssj" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.kssj, '{y}-{m}-{d} {hh}:{mm}:{ss}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="jssj" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.jssj, '{y}-{m}-{d} {hh}:{mm}:{ss}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开放程度" align="center" prop="kfcd" :formatter="kfcdFormat" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == '-1'" type="info"
                  effect="dark">
            {{ statusFormat(scope.row) }}
          </el-tag>
          <el-tag v-if="scope.row.status == '0'" type="warning"
                  effect="dark">
            {{ statusFormat(scope.row) }}
          </el-tag>
          <el-tag v-if="scope.row.status == '1'" type="success"
                  effect="dark">
            {{ statusFormat(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row)"
              v-hasPermi="['etp:sign:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['etp:sign:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['etp:sign:remove']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="报名人员" placement="top">
            <el-button
              type="text"
              icon="el-icon-user"
              @click="members(scope.row)"
              v-hasPermi="['etp:sign:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="二维码" placement="top">
            <el-button
              type="text"
              @click="createQrcg(scope.row)"
              v-hasPermi="['etp:sign:edit']"
            ><i class="iconfont icon-ico"></i></el-button>
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

    <!-- 添加或修改sign对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="活动标题" prop="hdmc">
          <el-input v-model="form.hdmc" type="textarea" placeholder="请输入活动标题" show-word-limit maxlength="100"/>
        </el-form-item>
        <el-form-item label="活动副标题" prop="hdfmc">
          <el-input v-model="form.hdfmc" type="textarea" placeholder="请输入活动副标题" show-word-limit maxlength="100"/>
        </el-form-item>
        <el-form-item label="设置标签" prop="tagValue">
          <tagsEdit widthSty="100%"
                    :parentValue="tagValue"
                    :selectOptions="tagOptions"
                    @selectTags="havaTags">

          </tagsEdit>
        </el-form-item>
        <el-form-item label="活动封面" prop="hdfm">
          <imageUpload v-model="form.hdfm" :limit="1" :upload-url="'/commom/upload/fastdfs'"/>
        </el-form-item>
        <el-form-item label="活动内容" prop="hdnr">
          <editor v-model="form.hdnr" :upload-url1="'/commom/upload/fastdfs'" :min-height="192"/>
        </el-form-item>
        <el-form-item label="时间范围" prop="rangeTime">
          <el-date-picker clearable  size="small"
                          v-model="form.rangeTime"
                          @input="daterangeChange"
                          type="daterange"
                          value-format="yyyy-MM-dd"
                          range-separator="至"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期"
                          :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="开放程度" prop="kfcd">
          <el-radio-group v-model="form.kfcd">
            <el-radio :label="'0'" @click.native="clearUser($event)">完全公开</el-radio>
            <el-radio :label="'1'" @click.native="pointUser($event)">限定用户</el-radio>
            <el-radio :label="'2'" @click.native="clearUser($event)">不公开</el-radio>
          </el-radio-group>
<!--          <el-radio-group v-model="form.kfcd">-->
<!--            <el-radio @click.native="pointUser(dict.dictValue,$event)" v-for="dict in kfcdOptions" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>-->
<!--          </el-radio-group>-->
        </el-form-item>

        <!-- @type  update
             @desc: 展示指定的用户
        -->
        <el-form-item  v-if="form.kfcd && form.kfcd == 1 && showUserFlag">
          <el-tag closable style="margin: 5px"
                  :key="user.userId" v-for="user in userArr"
                  :disable-transitions="false"
                  @close="handleClose(user)">
            {{ user.userName }}
          </el-tag>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" show-word-limit maxlength="300"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>

      </div>
    </el-dialog>


    <!-- 限定用户    -->
    <el-dialog title="限定用户"
               :visible.sync="dialogVisibleUser"
               :close-on-click-modal="false"
               width="1000px">
      <AssigningUsers
        :xz_data="userArr"
        ref="assigningUsers"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUser = false">取 消</el-button>
        <el-button type="primary" @click="pointUserConfirm">确 定</el-button>
      </span>
    </el-dialog>

  <!--  查看用户是否完成报名   -->
    <el-dialog title="活动报名"
               :close-on-click-modal="false"
               :visible.sync="dialogTableVisible">
      <el-form>
        <el-form-item>
          <el-button @click="exportXy()" type="primary">导出</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="gridData" style="margin-bottom: 30px;">
        <el-table-column property="xyid" align="center" label="学员ID"/>
        <el-table-column property="userName" align="center" label="学员姓名" />
        <el-table-column property="sfbm" align="center" label="是否报名">
          <template slot-scope="scope">
            <el-tag :type="scope.row.sfbm == '0'?'warning':'success'"
              effect="dark">
              {{ scope.row.sfbm == '0'?'未报名':'已报名' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :autoScroll="false"
        v-show="gridData.length > 0"
        :limit.sync="gridForm.pageSize"
        :page.sync="gridForm.pageNum"
        :total="gridData2.length"
        @pagination="getGridData"
      />
    </el-dialog>

    <el-dialog title="生成二维码"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleQrcg"
               width="70%">
      <div style="height: 550px">
        <Qrcg :data="qrcgForm" @closeQrcg="closeQrcg"/>
      </div>
<!--      <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="closeQrcg()">关 闭</el-button>-->
<!--      </span>-->
    </el-dialog>
<!--    删除弹窗-->
    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>

    <!--预览详情-->
    <detail ref="signForm" :open="signOpen" :form="signForm"  @closeDialog="signOpen=false"></detail>


  </div>
</template>

<script>
/** @type update  引入指定用户组件 */
import AssigningUsers from "@/components/AssigningUsers"
import Qrcg from "@/views/etp/qrcg";

import { listSign, getSign, delSign, addSign, updateSign, exportByStudent } from "@/api/etp/sign";
import tagsEdit from "@/components/tags/tagsEdit"
import {listTagRelate} from "@/api/etp/tag/tag";
import {ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import DelDialog from "@/components/DelDialog"
import detail from "@/views/etp/sign/components/detail";
import MoreTagShow from "@/components/MoreTagShow"
export default {
  name: "Sign",
  components:{tagsEdit,AssigningUsers,Qrcg,DelDialog,detail,MoreTagShow},
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
      // sign表格数据
      signList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 开放程度字典
      kfcdOptions: [],
      // 活动状态字典
      hdztOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        hdmc: null,
        hdfmc: null,
        hdnr: null,
      },
      // 表单参数
      form: {
        rangeTime:[],
        kfcd:'0'
      },
      // 表单校验
      rules: {
        hdmc: [
          { required: true, message: "活动标题不能为空", trigger: "blur" }
        ],
        // hdfm: [
        //   { required: true, message: "活动封面不能为空", trigger: "blur" }
        // ],
        hdnr: [
          { required: true, message: "活动内容不能为空", trigger: "blur" }
        ],
       /* kssj: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        jssj: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],*/
        rangeTime:[
          {required: true, message: "请设置时间范围", trigger: "blur"}
        ],
      },
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],

      /**限定用户*/
      dialogVisibleUser:false,
      showUserFlag:false,
      userArr:[],

      /** 查看用户是否完成报名*/
      dialogTableVisible:false,

      gridData:[],   // 页面使用的数据
      gridData2:[],  // 总数据
      gridForm:{
        pageNum: 1,
        pageSize: 10,
      },
      pickerOptions: {
        disabledDate: function(time){
          return time < Date.now() - 8.64e7;
        }
      },

      height:'',
      openFlag:true,
      tipFlag:false,

      // isIndeterminate: true,
      checkAll: true,

      //二维码参数
      dialogVisibleQrcg: false,
      qrcgForm:{
        title:'',
        value:'',
        size:'',
        startTime:'',
        endTime:''
      },
      qrcgUrl:'',

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,

      //活动预览
      signOpen:false,
      signForm:{},
      //导出id
      exportId:'',
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
        if (null != this.queryTag) {
          this.queryParams.tags = this.queryTag.join(',')
        }
        this.getList();
      },
      deep:true
    },
  },
  created() {
    this.getTags();
    this.getList();
    this.getDicts("hdbm_kfcd").then(response => {
      this.kfcdOptions.push({dictLabel:"全部",dictValue:undefined})
      response.data.forEach(item => {
        this.kfcdOptions.push(item)
      })
      // this.kfcdOptions = response.data;
    });
    this.getDicts("hdbm_status").then(res =>{
      this.hdztOptions.push({dictLabel:"全部",dictValue:undefined})
      res.data.forEach(item => {
        this.hdztOptions.push(item)
      })
      // this.hdztOptions = res.data;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });

  },
  methods: {

    closeQrcg(){
      this.dialogVisibleQrcg = false;
    },

    /** 生成二维码 */
    createQrcg(val) {
      debugger
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      if(val!=''&& val!=undefined&&val!=null){
        this.dialogVisibleQrcg = true;
        this.qrcgForm.title = val.hdmc;
        this.qrcgForm.size = 150;
        this.qrcgForm.startTime = val.kssj;
        this.qrcgForm.endTime = val.jssj;
        this.qrcgForm.value=this.qrcgUrl+'sign';
      }else{
        this.dialogVisibleQrcg = true;
        this.qrcgForm.title = this.form.hdmc;
        this.qrcgForm.size = 150;
        this.qrcgForm.startTime=this.form.kssj;
        this.qrcgForm.endTime=this.form.jssj;
        this.qrcgForm.value=this.qrcgUrl+'sign';
      }

    },

    handleDetail(row){
      // this.$router.push({
      //   path:"/etp/sign/components/signDetail/" + row.id,
      // });
      getSign(row.id).then(response => {
        this.signForm ={}
        this.signForm = response.data
        this.signOpen = true
      });
    },

    handleCheckAllChange(val) {
      this.queryTag = []
    },

    handleCheckedTagsChange(value) {
      this.checkAll = false
      // let checkedCount = value.length;
      // this.checkAll = checkedCount === this.tagOptions.length;
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },

    // 更新
    daterangeChange(e) {
      this.$nextTick(() => {
        this.$forceUpdate()
      })
    },

    getGridData(){
      let startIndex = (this.gridForm.pageNum - 1) * this.gridForm.pageSize;
      let endIndex =
        (this.gridForm.pageSize * this.gridForm.pageNum < this.gridData2.length)
          ? this.gridForm.pageSize * this.gridForm.pageNum:this.gridData2.length;
      this.gridData = [];
      for (let i = startIndex; i < endIndex; i++)
        this.gridData.push(this.gridData2[i]);
    },


    members(row){
      this.exportId = row.id;
      getSign(row.id).then(res => {
        this.dialogTableVisible = true;

        //  this.gridData = res.data.sysUsers;
        this.gridData2 = res.data.sysUsers;
        this.gridData = [];
        for (let i = 0; i < (this.gridForm.pageSize > this.gridData2.length
          ? this.gridData2.length:this.gridForm.pageSize); i++) {
          this.gridData.push(this.gridData2[i]);
        }
      })
    },
    clearUser(e){
      if (e.target.tagName === 'INPUT') return
      this.userArr = [];
      this.showUserFlag = false;
    },
    pointUser(e){
      if (e.target.tagName === 'INPUT') return
      this.dialogVisibleUser = true;
      this.$nextTick(_=>{
        this.$refs.assigningUsers.handleQuery();
      })
    },
    // pointUser(labelValue,e){
    //   if (e.target.tagName === 'INPUT') return
    //   // 弹框
    //   if (labelValue == '1'){
    //     this.dialogVisibleUser = true;
    //     this.$nextTick(_=>{
    //       this.$refs.assigningUsers.getList();
    //     })
    //   }
    //
    // },
    pointUserConfirm(){
      let arr = this.$refs.assigningUsers.getChoiceUser();
      if (arr.length <= 0){
        this.$message.warning("未选择用户！");
        return
      }
      this.userArr = this.mergeArray(this.userArr,arr);
      this.showUserFlag = true;
      this.dialogVisibleUser = false;
    },
    handleClose(user){
      this.userArr = this.userArr.filter(u => { return u.userId != user.userId })
    },
    mergeArray(arr1,arr2){
      let _arr = [];
      for(let i = 0;i < arr1.length;i++){
        _arr.push(arr1[i]);
      }
      for(let i = 0;i < arr2.length;i++){
        let flag = true;
        for(let j = 0;j < arr1.length;j++){
          if(arr2[i]['userId'] == arr1[j]['userId']){
            flag = false;
            break;
          }
        }
        if(flag){
          _arr.push(arr2[i]);
        }
      }
      return _arr;
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

    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "HD"}).then(response => {
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
    },
    // 标签相关结束

    /** 查询sign列表 */
    getList() {
      this.loading = true;
      listSign(this.queryParams).then(response => {
        this.signList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 开放程度字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
    },
    statusFormat(row, column){
      return this.selectDictLabel(this.hdztOptions, row.status)
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
        hdmc: null,
        hdfmc: null,
        hdfm: null,
        hdnr: null,
        kssj: null,
        jssj: null,
        kfcd: '0',
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加";
      this.tagValue = [];
      this.form.hdfm = require("@/assets/images/cover.jpg")
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids

      getSign(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改";
        if (null != response.data.tags) {
          this.tagValue = response.data.tags.split(",");
        } else {
          this.tagValue = [];
        }
        this.form.rangeTime = [];
        this.form.rangeTime.push(this.form.kssj,this.form.jssj)

        //图片回显
        this.form.hdfm =  this.form.hdfm == ""|| this.form.hdfm == null? require("@/assets/images/cover.jpg") : this.form.hdfm;
        /** @type update 限定用户回显*/
        let sysUsers = response.data.sysUsers;
        if(this.form.kfcd == 1){
          if (sysUsers.length > 0){
            this.showUserFlag = true;
            this.userArr = sysUsers;
          }
        }
      });
    },
    /** 提交按钮 */
    submitForm() {


      /** @type update 2021-12-31 限定用户提交前校验开始*/
      this.form.sysUsers = this.userArr;
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("请指定用户！")
          return
        }
      }
      /** 校验指定用户逻辑结束*/

      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.kssj = this.form.rangeTime[0];
          this.form.jssj =  this.form.rangeTime[1];
          if(null!= this.form.hdfm && this.form.hdfm.indexOf("/img/cover")>-1){
            this.form.hdfm = null
          }
          this.$confirm("请确认是否保存当前活动信息？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.form.id != null) {
              updateSign(this.addTags(this.form, this.tagValue)).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.createQrcg();
                this.getList();
              });
            } else {
              addSign(this.addTags(this.form, this.tagValue)).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.createQrcg();
                this.getList();
              });
            }
          }).catch(() => {
          });

          //更新一下标签列表
          this.getTags();
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"活动标题":row.hdmc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delSign(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },

    /** 导出按钮操作 */
    exportXy() {
      this.$confirm('是否确认导出所有报名学员数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.exportLoading = true;
        return exportByStudent(this.exportId);
      }).then(response => {
        this.download(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
<style lang="scss" scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

</style>
