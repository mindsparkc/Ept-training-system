<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24"  class="span4-left">
        <Category ref="category" @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="20" :xs="24" style="border-left: 1px solid #dddddd;" class="span20-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
          <el-form-item label="题干" prop="tgDisplat">
            <el-input
              v-model="queryParams.tgDisplat"
              placeholder="请输入题干"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="题目类型" prop="lx">
            <el-select v-model="queryParams.lx" placeholder="请选择类型：" clearable size="small">
              <el-option
                v-for="dict in lxOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>

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

          <el-form-item label="更新时间" prop="upadteTime">
            <el-date-picker
              v-model="queryParams.upadteTime"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
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
              v-hasPermi="['etp:subject:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['etp:subject:remove']"
            >批量删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['etp:subject:import']"
            >导入</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="subjectList"
                   @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="45" align="center" />
    <!--      <el-table-column  label="试题编号" :show-overflow-tooltip="true"  width="100" align="center" prop="id" sortable />-->
          <el-table-column width="60">
            <template slot-scope="scope">
              <div v-if="scope.row.sfks=='1'" class="kao-tag">考</div>
              <div v-if="scope.row.sflx=='1'" class="lian-tag">练</div>
            </template>
          </el-table-column>
          <el-table-column  label="题干" align="center" prop="tg" :show-overflow-tooltip="true" :align="'left'" >
            <template slot-scope="scope">
              <span v-if="scope.row.tgDisplat === null||scope.row.tgDisplat === ''">{{scope.row.tg}}</span>
              <span v-else>{{scope.row.tgDisplat}}</span>
            </template>
          </el-table-column>

          <el-table-column label="题目分类" align="left" prop="categoryName" />
          <el-table-column label="类型" align="center" prop="lx" :formatter="lxFormat" width="80"/>
          <el-table-column label="难易程度" align="center" prop="level" :formatter="levelFormat" width="80"/>
    <!--      <el-table-column label="是否考试" align="center" prop="sfks" :formatter="sfksFormat" />-->
    <!--      <el-table-column label="是否练习" align="center" prop="sflx" :formatter="sflxFormat" />-->
          <el-table-column label="标签" align="center" prop="tags" width="130">
            <template slot-scope="scope">
              <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right" width="60">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="修改" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['etp:subject:edit']"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['etp:subject:remove']"
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
      </el-col>
    </el-row>

    <!-- 添加或修改题库对话框 -->
<!--    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="类型：" prop="lx">
          <el-radio v-model="form.lx" :label="dict.dictValue" v-for="dict in lxOptions"> {{dict.dictLabel}}
          </el-radio>
        </el-form-item>
        <el-form-item label="标签：" prop="tagValue" >
          <tagsEdit widthSty="832px"
                    :parentValue=tagValue
                    :selectOptions="tagOptions"
                    @selectTags="havaTags">

          </tagsEdit>
        </el-form-item>
        <el-form-item label="题干：" prop="tg">
          <el-input v-model="form.tg" ref="tg" placeholder="请输入内容" />
        </el-form-item>

        <div  v-if="form.lx=='1'||form.lx=='2'">
          <el-form-item label="选项：" v-for="(item, index) in form.xxList" class="xx-div"
                        :key="item.key"
                        :prop="'xxList.' + index + '.value'"
                        :rules="{ required: true, message: '选项不能为空,不需要的选项请删除', trigger: 'blur'}"
                        :class="{'labNone':index!=0}">
            <el-input placeholder="请输入选项" v-model="item.value" :class="'input_' + index">
              <template slot="prepend">{{item.key}}</template>
            </el-input>
            <i class="el-icon-remove-outline" v-show="index!=0" @click="del_xx(index)"></i>
          </el-form-item>
          <el-button class="add-btn" @click="add_xx()">+ 新增选项</el-button>
        </div>

        <el-form-item label="正确答案：" prop="da" v-if="form.lx!='4'&&form.lx!='5'">
          <el-radio v-if="form.lx=='1'" v-model="dx_radio" :label="item.key" v-for="item in form.xxList"> {{item.key}}
          </el-radio>

          <el-checkbox-group v-model="da_checkList" v-if="form.lx=='2'">
            <el-checkbox :label="item.key" v-for="item in form.xxList"> {{item.key}}</el-checkbox>
          </el-checkbox-group>

          <el-radio-group v-if="form.lx=='3'" v-model="pd_radio">
            <el-radio  :label="'1'"> 对</el-radio>
            <el-radio  :label="'0'"> 错</el-radio>
          </el-radio-group>
        </el-form-item>

        &lt;!&ndash;          填空题&ndash;&gt;
        <div  v-if="form.lx=='4'">
          <el-form-item label="正确答案：" class="xx-div"
                        v-for="(item,index) in form.tk_list"
                        :key="index"
                        :prop="`tk_list[${index}]`"
                        :rules="tkRules"
                        :class="{'labNone':index!=0}">
&lt;!&ndash;            <el-input placeholder="请输入答案"  v-model="item.value"/>&ndash;&gt;
            <el-select
              v-model="form.tk_list[`${index}`]"
              multiple
              filterable
              allow-create
              default-first-option
              style="width: 832px;"
              :placeholder="`请输入第${index+1}个空的答案`">
              <el-option
                v-for="item in []"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <i class="el-icon-remove-outline" v-show="index!=0" @click="del_tk(index)"></i>
          </el-form-item>
          <el-button class="add-btn" @click="add_tk()">+ 新增填空答案</el-button>
        </div>

        <el-form-item label="可用范围：">
          <el-radio-group v-model="tmTypeRadio">
            <el-radio :label="'1'">考试</el-radio>
            <el-radio :label="'2'">练习</el-radio>
            <el-radio :label="'3'">考试与练习</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="答案解析：" prop="jx">
          <el-input v-model="form.jx" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>-->

    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>

    <!-- 题目导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".docx, .doc"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip" style="font-size: 15px">
          <!-- <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的题目数据 -->
          <el-link type="primary" style="font-size:14px"
                   @click="importTemplate"
                   v-hasPermi="['etp:subject:template']"
          >下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“docx”或“doc”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

<!--    导入结果提示窗口-->
    <tipDialog :tip-visible="tipVisible"
               :tipInfo="tipInfo"
               :infoTitle="infoTitle"
               @tipClose="tipVisible = false"></tipDialog>
  </div>
</template>

<script>
import { listSubject, getSubject, delSubject, addSubject, updateSubject, exportSubject, checkXx, importTemplate } from "@/api/etp/subject/subject";
import tagsEdit from "@/components/tags/tagsEdit";
import { listTagRelate } from "@/api/etp/tag/tag";
import DelDialog from "@/components/DelDialog"
import tipDialog from "@/components/tipDialog"
import { getToken } from '@/utils/auth'
import Category from "@/views/etp/category"
import MoreTagShow from "@/components/MoreTagShow"

export default {
  name: "Subject",
  components: {
    tagsEdit,DelDialog,tipDialog,Category,MoreTagShow
  },
  data() {
   /* var checkTk=(rule, value, callback) => {
      if (!value) {
        return callback(new Error('答案不能为空'));
      }else{
        var patrn = /["]/im;
        let checkFlag = true
        value.forEach(item=>{
          if (patrn.test(item)) {// 如果包含特殊字符返回false
            checkFlag = false
          }
        })
        if(checkFlag){
          return callback()
        }else{
          return callback(new Error('答案中不能存在英文双引号'));
        }
      }
    };*/
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
      // 题库表格数据
      subjectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型
      lxOptions: [],
      // 是否练习
      sflxOptions: [],
      // 是否考试
      sfksOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tgDisplat: null,
        lx: null,
        sflx: null,
        sfks: null,
        upadteTime: null,
        beginTime:null,
        endTime:null,
        tags:[]
      },
      // 表单参数
      form: {
        sfks:'1',
        xxList:[{key:'A',value:''},{key:'B',value:''},{key:'C',value:''},{key:'D',value:''}],
        //填空答案
        // tk_list:[{value:'' }],
        tk_list:[[]],
      },

      letterArr:[],
      //答案选项
      da_checkList:['A'],
      dx_radio: '',
      pd_radio: '1',//判断答案

      // 标签用到的
      tagValue: [],
      tagOptions:[],
      // 表单校验
      rules: {
        tg: [
          { required: true, message: "题干不能为空", trigger: "blur" }
        ]
      },
      // tkRules:[
      //   {required: true, message: "答案不能为空", trigger: "blur"},
      //   {validator: checkTk, trigger: 'blur'}
      // ],
      queryTag:[],

      height:'',
      openFlag:true,
      tipFlag:false,
      checkAll: true,

      tmTypeRadio:'3',

      //删除
      delVisible:false,
      delInfo:{},
      delInfoIds:null,

      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'  },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/etp/subjectWithCp/batch"
      },

      tipVisible: false,
      infoTitle:"",
      tipInfo:{},
      // 难易程度
      levelOptions: [],
    };
  },
  watch:{
    da_checkList(val, oldVal){
      this.form.da = val.join(',');
    },
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
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    });
    this.getDicts("subject_yes_no").then(response => {
      this.sflxOptions = response.data;
      this.sfksOptions = response.data;
    });
    this.getDicts("subject_level").then(response => {
      this.levelOptions = response.data;
    });
  },
  activated() {
    this.getList();
    this.getTags();
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

    /** 查询题库列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams)
      listSubject(this.queryParams).then(response => {
        this.subjectList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    havaTags(val){
      this.tagValue = val
    },

    // 类型：
    lxFormat(row, column) {
      return this.selectDictLabel(this.lxOptions, row.lx);
    },
    // 难易程度：
    levelFormat(row, column) {
      return this.selectDictLabel(this.levelOptions, row.level);
    },
    // 是否练习
    sflxFormat(row, column) {
      return this.selectDictLabel(this.sflxOptions, row.sflx);
    },
    // 是否考试
    sfksFormat(row, column) {
      return this.selectDictLabel(this.sfksOptions, row.sfks);
    },
    // 取消按钮
    /*cancel() {
      this.open = false;
      this.reset();
    },*/
    // 表单重置
    /*reset() {
      this.form = {
        id: null,
        tg: null,
        lx: null,
        xx: null,
        da: null,
        jx: null,
        sflx: '0',
        sfks: '1',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        xxList:[{key:'A',value:''},{key:'B',value:''},{key:'C',value:''},{key:'D',value:''}],
        //填空答案
        // tk_list:[{value: '' }],
        tk_list:[[]],
      };
      this.tmTypeRadio ='3'
      this.resetForm("form");
    },*/
    /** 搜索按钮操作 */
    handleQuery(categoryId) {
      this.queryParams.pageNum = 1;
      if(typeof(categoryId) == 'number'){
        this.queryParams.categoryId = categoryId
      }
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(',')
      }
      if(null !=  this.queryParams.upadteTime){
        this.queryParams.beginTime = this.queryParams.upadteTime[0]
        this.queryParams.endTime = this.queryParams.upadteTime[1]
      }

      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.$refs.category.$refs.tree.setCurrentKey(null)
      this.queryParams.categoryId = null
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
      // this.reset();
      // this.open = true;
      // this.form.lx = '1';
      // this.da_checkList=['A'];
      // this.title = "添加题目";
      // this.dx_radio = 'A';
      // this.tagValue = [];
      // this.$nextTick( _=>{
      //   this.$refs.tg.focus();
      // })
      this.$router.push({
        path:"/etp/subject/components/addOrUpdateSubject/",
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // this.reset();
      const id = row.id || this.ids
      this.$router.push({
        path:"/etp/subject/components/addOrUpdateSubject/" + id,
      });
     /* getSubject(id).then(response => {
        this.form = {...this.form,...response.data};
        this.open = true;
        this.title = "修改题目";
        if(null != response.data.tags ){
          this.tagValue = response.data.tags.split(",");
        }else{
          this.tagValue = [];
        }
        //题目可用范围回显
        if(this.form.sfks=='1'&&this.form.sflx=='1'){
          this.tmTypeRadio='3'
        }else if(this.form.sfks=='1'&&this.form.sflx!='1'){
          this.tmTypeRadio='1'
        }else{
          this.tmTypeRadio='2'
        }

        //选项回显
        if(this.form.lx=='1'){
          this.dx_radio = response.data.da;
          this.form.xxList = JSON.parse( response.data.xx);
        }
        if(this.form.lx=='2'){
          this.da_checkList = response.data.da.split(',');
          this.form.xxList = JSON.parse( response.data.xx);
        }
        //填空答案
        if(this.form.lx=='4'){
          let da = JSON.parse( response.data.da)
          this.form.tk_list = da;
        }
        this.$nextTick( _=>{
          this.$refs.tg.focus();
        })
      });*/
    },
    /** 提交按钮 */
   /* submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.lx == '1' || this.form.lx == '2'){
            this.form.xx = JSON.stringify(this.form.xxList);
            if(this.form.lx == '1'){
              this.form.da = this.dx_radio;
            }
          }else{
            this.form.xx = ""
          }
          if(this.form.lx == '3'){
            //判断
            this.form.da = this.pd_radio;
          }
          if(this.form.lx == '4'){
            //填空
            this.form.da =JSON.stringify(this.form.tk_list)
          }
          debugger
          if(this.tmTypeRadio=='1'){
            this.form.sfks ='1'
            this.form.sflx ='0'
          }else if(this.tmTypeRadio=='2'){
            this.form.sfks ='0'
            this.form.sflx ='1'
          }else{
            this.form.sfks ='1'
            this.form.sflx ='1'
          }

          if (this.form.id != null) {
            updateSubject(this.addTags(this.form,this.tagValue)).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              //更新一下标签列表
              this.getTags();
            });
          } else {
            addSubject(this.addTags(this.form,this.tagValue)).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              //更新一下标签列表
              this.getTags();
            });
          }

        }
      });
    },*/
   /* /!** 删除按钮操作 *!/
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除试题编号为【' + ids + '】的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSubject(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },*/

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"题干信息":row.tg,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delSubject(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
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
    getTags(){
      listTagRelate({modelType:"TK"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
   /* add_xx(){
      let arr = this.letterArr;
      this.form.xxList.push({key:'',value:''});
      this.form.xxList.forEach(function (item, index) {
       item.key=arr[index]
      })
      this.da_checkList = ['A'];
      this.dx_radio = 'A';
      // this.form.da = '';
      this.$nextTick(_=>{
        let index = this.form.xxList.length - 1;
        let elementNode = document.querySelector(".input_" + index);
        elementNode.children[1].focus()
      })

    },
    del_xx(index){
      let arr = this.letterArr;
      this.form.xxList.splice(index,1);
      this.form.xxList.forEach(function (item, index) {
        item.key=arr[index]
      })
      this.da_checkList = ['A'];
      this.dx_radio = 'A';
      // this.form.da = '';

    },
    add_tk(){
      this.form.tk_list.push([])

      this.$forceUpdate();
    },
    del_tk(index){
      this.form.tk_list.splice(index,1);
      this.$forceUpdate();
    },*/

    //导入题目
    handleImport(){
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      window.location.href = process.env.VUE_APP_BASE_API + "/etp/subjectWithCp/template"
      // importTemplate().then(res => {
      //   //const fileName = res.headers['content-disposition'].split(';')[1].split('filename=')[1];
      //   const fileName = "题库导入模版.docx"
      //   console.log(res)
      //
      //   const blob = new Blob([res], {type: `application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8`});
      //   if ("download" in document.createElement("a")) {
      //     console.log("非 IE 下载")
      //     // 非IE下载
      //     const elink = document.createElement("a");
      //     elink.download = fileName;
      //     elink.style.display = "none";
      //     elink.href = URL.createObjectURL(blob);
      //     document.body.appendChild(elink);
      //     elink.click();
      //     URL.revokeObjectURL(elink.href); // 释放URL 对象
      //     document.body.removeChild(elink);
      //   } else {
      //     // IE10+下载
      //     console.log("IE10+下载")
      //     navigator.msSaveBlob(blob, fileName);
      //   }
      // });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      debugger
      this.$refs.upload.clearFiles();
      if(response.code == 200){
        this.$alert(`${response.msg}，成功导入${response.data.count}条数据`, "导入结果", { dangerouslyUseHTMLString: true });
      }else{
        this.infoTitle = '导入失败'
        this.tipVisible = true
        this.tipInfo={"失败原因":response.msg}
      }
     this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};

</script>
<style lang="scss" scoped >

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
  width: calc(100% - 120px)!important;
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

.el-checkbox-group {
  margin-left: 30px;
}

</style>
