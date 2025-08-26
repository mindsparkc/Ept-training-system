<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" class="span4-left">
        <Category  ref="category" @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="20" :xs="24" style="border-left: 1px solid #dddddd;" class="span20-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
          <el-form-item label="课件名称" prop="mc">
            <el-input
              v-model="queryParams.mc"
              placeholder="请输入课件名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="课件描述" prop="ms">
            <el-input
              v-model="queryParams.ms"
              placeholder="请输入课件相关描述"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="300"
              show-word-limit
            />
          </el-form-item>
          <!--         <el-form-item label="课件形式" prop="xs">
                <el-select v-model="queryParams.xs" placeholder="请输入课件形式" clearable size="small">
                  <el-option
                    v-for="dict in xsOptions"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  />
                </el-select>
              </el-form-item>

          <el-form-item label="类型" class="tags-div" prop="xs" style="width: calc(100% - 256px);">
            <el-radio-group v-model="queryParams.xs">
              <el-radio v-for="dict in xsOptions"
                        :key="dict.dictValue"
                        :label="dict.dictValue"
              >{{ dict.dictLabel }}</el-radio>
            </el-radio-group>
          </el-form-item> -->
          <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
            <div style="display: flex">
             <template style="position: relative">
               <el-checkbox  v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>
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
              @click="handleAdd()"
              v-hasPermi="['etp:courseware:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['etp:courseware:remove']"
            >删除
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="coursewareList"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="45" align="center"/>
          <el-table-column label="封面图片" align="center" prop="fm" width="90">
            <template slot-scope="scope">
              <el-image
                style="width: 80px; height: 60px;border-radius: 5px"
                :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
                :fit="fit"
              />
            </template>
          </el-table-column>
          <el-table-column label="课件名称" align="left" prop="mc" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span style="cursor: pointer" @click="preview(scope.row)">{{scope.row.mc}}</span>
            </template>
          </el-table-column>
          <el-table-column label="课件分类" align="left" prop="categoryName" />
          <el-table-column label="课件形式" align="center" prop="xs">
            <template slot-scope="scope">
              <p>{{ scope.row.xs == '1' ? "OFFICE" : "" }}</p>
              <p>{{ scope.row.xs == '2' ? "视频" : "" }}</p>
              <p>{{ scope.row.xs == '3' ? "富文本" : "" }}</p>
              <p>{{ scope.row.xs == '4' ? "外部链接" : "" }}</p>
              <p>{{ scope.row.xs == '5' ? "其他文件" : "" }}</p>
            </template>
          </el-table-column>

          <!--      <el-table-column label="开放程度" align="center" prop="kfcd">
                  <template slot-scope="scope">
                    <el-tag v-if="scope.row.kfcd == '0'">完全公开</el-tag>
                    <el-tag v-if="scope.row.kfcd == '1'" type="warning">限定用户</el-tag>
                    <el-tag v-if="scope.row.kfcd == '2'" type="danger">不公开</el-tag>
                  </template>
                </el-table-column>-->
          <el-table-column label="标签" align="center" prop="tags">
            <template slot-scope="scope">
              <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
<!--              <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null"-->
<!--                      v-for="(item,index) in (scope.row.tags||'').split(',').slice(0,3)"-->
<!--                      :key="index"-->
<!--                      style="margin-right: 3px;margin-bottom:8px">-->
<!--                {{ item }}-->
<!--              </el-tag>-->
              <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null && (scope.row.tags||'').split(',').length > 3"
                      style="margin-right: 3px;margin-bottom:8px">
                ...
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="学习时长" align="center" prop="learnTime" width="80">
            <template  slot-scope="scope">
              {{ scope.row.learnTime }} min
            </template>
          </el-table-column>
          <el-table-column label="课件描述" align="left"  prop="ms" :show-overflow-tooltip="true">
            <template  slot-scope="scope">
              {{ scope.row.ms }}
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" width="100"/>
          <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right" width="80">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="预览" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-view"
                  @click="preview(scope.row)"
                  v-hasPermi="['etp:courseware:remove']"
                >
                </el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="修改" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['etp:courseware:edit']"
                >
                </el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['etp:courseware:remove']"
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
      </el-col>
    </el-row>


    <!-- 添加或修改课件管理对话框 -->
<!--
    <el-dialog :title="title" :visible.sync="open" width="1000px"
               :before-close="handleClose" :close-on-click-modal="false" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课件名称" prop="mc">
          <el-input v-model="form.mc" placeholder="请输入课件名称" maxlength="100" show-word-limit/>
        </el-form-item>
        <el-form-item label="设置标签" prop="tagValue">
          <tagsEdit widthSty="100%"
                    :parentValue="tagValue"
                    :selectOptions="tagOptions"
                    @selectTags="havaTags">

          </tagsEdit>
        </el-form-item>
        <el-form-item label="封面图片" prop="fm">
          <imageUpload :limit="1"
                       :uploadUrl="'/commom/upload/fastdfs'"
                       v-model="form.fm"></imageUpload>
        </el-form-item>

        <el-form-item label="课件形式" prop="xs">
          <el-radio-group v-model="courseType">
            <el-radio :disabled="radioFlag" :label="1">链接</el-radio>
            <el-radio :disabled="radioFlag" :label="2">文件</el-radio>
            <el-radio :disabled="radioFlag" :label="3">富文本</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="课件链接" prop="lj" v-show="courseType === 1" >
          <el-input v-model="form.lj" placeholder="请输入课件链接"/>
        </el-form-item>
        <el-form-item label="课件内容" prop="nr" v-show="courseType === 3" >
          <div id="edit" v-model="form.nr"></div>
        </el-form-item>
        <el-form-item label="选择课件" prop="kjdz" v-show="courseType === 2" >
          <fileUpload ref="fileUpload" :limit="1" :uploadUrl="'/commom/upload/fastdfs'"
                      :accept="'.doc,.docx,.pdf,.txt,.jpg,.jpeg,.png,.mp4'"
                      :file-type="['doc','docx','pdf','txt','jpg','jpeg','png','mp4']"
                      v-model="form.kjdz" @getFileType="getFileType"/>

        </el-form-item>

        <el-form-item label="学习时长" prop="learnTime" v-if="courseType !== 1" :key="103">
          <el-input v-model.number="form.learnTime"  placeholder="请输入课件最低学习时长（分钟）">
            <template slot="append">分钟</template>
          </el-input>
        </el-form-item>

        <el-form-item label="课件描述" prop="ms">
          <el-input v-model="form.ms" type="textarea" placeholder="请输入课件描述" maxlength="300" show-word-limit/>
        </el-form-item>
&lt;!&ndash;        <el-form-item label="开放程度" prop="kfcd">
          <el-radio-group v-model="kfcd">
            <el-radio :label="0">完全公开</el-radio>
            <el-radio :label="1">限定用户</el-radio>
            <el-radio :label="2">不公开</el-radio>
          </el-radio-group>
        </el-form-item>&ndash;&gt;
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" maxlength="300" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
-->


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

    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
  </div>
</template>

<script>
import imageUpload from "@/components/ImageUpload";
import fileUpload from "@/components/FileUpload";
import RichTextPreview from "./components/index"
import E from 'wangeditor'
import {getToken} from "@/utils/auth";
import {getExt} from "@/utils/getExt";
import {
  listCourseware,
  getCourseware,
  delCourseware,
  addCourseware,
  updateCourseware,
  exportCourseware
} from "@/api/etp/courseware/courseware";
import tagsEdit from "@/components/tags/tagsEdit"
import {listTagRelate} from "@/api/etp/tag/tag";
import {FILE_PREVIEW_URL, ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import DelDialog from "@/components/DelDialog"
import MoreTagShow from "@/components/MoreTagShow"
import Category from "./category/index"

export default {
  name: "Courseware",
  components: {
    imageUpload,
    fileUpload,
    tagsEdit,
    RichTextPreview,
    DelDialog,
    MoreTagShow,
    Category
  },
  data() {
    var validateLj = (rule, value, callback) => {
      if(this.courseType == 1){
        if (null== value || value === '') {
          callback(new Error('课件链接不能为空'));
        } else {
          callback();
        }
      }else{
        callback()
      }

    };
    var validateNr = (rule, value, callback) => {
      if(this.courseType == 3){
        if (null== value || value === '') {
          callback(new Error('课件内容不能为空'));
        } else {
          callback();
        }
      }else{
        callback()
      }

    };
    var validateWj = (rule, value, callback) => {
      if(this.courseType != 3 && this.courseType != 1){
        if (null== value || value === '') {
          callback(new Error('请选择文件'));
        } else {
          callback();
        }
      }else{
        callback()
      }

    };
    return {
      isFullscreen:false,
      infoObj:"",
      contentTextVisible:false,
      fit: "cover",
      editor: null,
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
      // 课件管理表格数据
      coursewareList: [],
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
        lj: null,
        nr: null,
        kjdz: null,
        kfcd: null,
        tags: [],
        categoryId:null
      },
      fmt:null,
      // 表单参数
      form: {
        learnTime: 0
      },
      // 课件形式
      courseType: 1,
      // 开放程度
      kfcd: 0,
      // 表单校验
      rules: {
        mc: [
          {required: true, message: "课件名称不能为空", trigger: "blur"}
        ],
        // fm: [
        //   {required: true, message: "课件封面还未上传", trigger: "blur"}
        // ],
        ms: [
          {required: true, message: "课件描述不能为空", trigger: "blur"}
        ],
        nr: [
          {required: true,  validator: validateNr, trigger: "blur"}
        ],
        lj: [
          {required: true,  validator: validateLj, trigger: "blur"},
        ],
        kjdz: [
          {required: true,  validator: validateWj, trigger: "blur"}
        ],
        learnTime: [
          {required: true, message: "学习时长不能为空", trigger: "blur"},
          {type: 'number', message: "学习时长需要是数字", trigger: "blur"}
        ]
      },
      xsOptions: [],
      kfcdOptions: [],


      optype: '',
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],


      height:'',
      openFlag:true,
      tipFlag:false,

      checkAll: true,

      //控制课件形式是否可选
      radioFlag:false,

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
      catIds:null
    };
  },
  watch: {
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

    courseType: {
      handler(newValue, oldValue) {
        // 只要每次更换就讲其他形式的课件置空
        // if (this.$refs.fileUpload) this.$refs.fileUpload.fileList = [];
        // if (this.form.lj) this.form.lj = '';
        // if (newValue === 1){
        //   this.resetFlag();
        // }
        if (newValue === 3) {
          this.$nextTick(() => {
            // if (this.editor != null) {
            //   this.editor.destroy();
            //   this.editor = null;
            // }
            if (this.editor == null) {
              this.initEdit('');
            }

          })
        }
      },
    }
  },
  created() {
    this.getTags();
    this.getList();
    this.getDicts("courseware_type").then(res => {
      this.xsOptions.push({dictLabel:"全部",dictValue:undefined})
      res.data.forEach(item => {
        this.xsOptions.push(item)
      })
      // this.xsOptions = res.data;
    });
    this.getDicts("courseware_open_close").then(res => {
      this.kfcdOptions = res.data;
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
      let checkedCount = value.length;
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


    handleContentClose(){
      this.contentTextVisible = false;
      this.isFullscreen = false;
    },
    /** 预览 Office：1  视频：2  富文本：3  外部链接：4  其他文件：5 */
    preview(obj){
      // if(obj.xs == '1' || obj.xs == '5' || obj.xs == '2'){
      //   let url = obj.kjdz;
      //   console.log(this.previewUrl() + '?url=' + encodeURIComponent(window.btoa(url)))
      //   console.log(url)
      //   window.open(this.previewUrl() + '?url=' + encodeURIComponent(window.btoa(url)) )
      // }
      // if (obj.xs == '3'){
      //   this.contentTextVisible = true;
      //   this.infoObj = {
      //     type:"richText",
      //     content:obj.nr
      //   };
      // }
      // if (obj.xs == '4'){
      //   window.open(obj.lj)
      // }
      let router = this.$router.resolve({
        path: "/etp/course/previewCourse",
        query: {kjid: obj.id}
      })
      window.open(router.href,'_blank')

    },

    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "KJ" }).then(response => {
       this.tagOptions = response.data;
       //  this.tagOptions = Object.assign(this.tagOptions,response.data);
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

    /** 查询课件管理列表 */
    getList() {
      this.loading = true;
      listCourseware(this.queryParams).then(response => {
        this.coursewareList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        mc: null,
        ms: null,
        fm: null,
        xs: null,
        lj: null,
        nr: null,
        learnTime: 0,
        kjwjmc: null,
        kfcd: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        kjdz: null,
        remark: null
      };
      this.resetForm("form");
      this.radioFlag = false;
      if (this.editor != null) {
        this.editor.destroy();
        this.editor = null;
      }
      this.getTags();
    },
    /** 搜索按钮操作 */
    handleQuery(value) {
      if(value!=undefined &&typeof(value.catId) == 'number') {
        this.queryParams.categoryId = value.catId;
        this.catIds = value.catIds
      }
      this.queryParams.pageNum = 1;

      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(',')
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
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        path:"/etp/courseware/components/addOrUpdateCourseware/",
        query: {
          catIds:this.catIds
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // this.reset();
      const id = row.id || this.ids
      this.$router.push({
        path:"/etp/courseware/components/addOrUpdateCourseware/" + id,
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"课件名称":row.mc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delCourseware(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },
// --------------下面方法不用----------------

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
    /**初始化富文本编辑器*/
    initEdit(txt) {
      this.editor = new E('#edit');
      this.editor.config.uploadFileName = 'file';
      this.editor.config.uploadVideoName = 'file';
      this.editor.config.height = 200;
      this.editor.config.uploadImgHeaders = {
        Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'
      }
      this.editor.config.uploadVideoHeaders = {
        Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'
      }
      // 配置图片上传地址
      this.editor.config.uploadImgServer = process.env.VUE_APP_BASE_API + '/commom/upload/fastdfs';
      // 配置视频上传地址
      this.editor.config.uploadVideoServer = process.env.VUE_APP_BASE_API + '/commom/upload/fastdfs';
      // 图片上传回调
      this.editor.config.uploadImgHooks = {
        // 上传图片之前
        before: function (xhr) {
        },
        // 图片上传并返回了结果，图片插入已成功
        success: function (xhr) {
          this.$message("上传成功");
        },
        // 图片上传并返回了结果，但图片插入时出错了
        fail: function (xhr, editor, resData) {
          this.$message(resData);
        },
        // 上传图片出错，一般为 http 请求的错误
        error: function (xhr, editor, resData) {
          this.$message(resData);
        },
        // 上传图片超时
        timeout: function (xhr) {
          this.$message("上传图片超时");
        },
        // 图片上传并返回了结果，想要自己把图片插入到编辑器中
        // 例如服务器端返回的不是 { errno: 0, data: [...] } 这种格式，可使用 customInsert
        customInsert: function (insertImgFn, result) {
          // result 即服务端返回的接口
          // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
          insertImgFn(result.url)
        }
      };
      // 视频上传回调
      this.editor.config.uploadVideoHooks = {
        // 上传视频之前
        before: function (xhr) {
        },
        // 视频上传并返回了结果，视频插入已成功
        success: function (xhr) {
          this.$message("上传成功");
        },
        // 图片上传并返回了结果，但图片插入时出错了
        fail: function (xhr, editor, resData) {
          this.$message(resData);
        },
        // 上传图片出错，一般为 http 请求的错误
        error: function (xhr, editor, resData) {
          this.$message(resData);
        },
        // 上传图片超时
        timeout: function (xhr) {
          this.$message("上传图片超时");
        },
        // 图片上传并返回了结果，想要自己把图片插入到编辑器中
        // 例如服务器端返回的不是 { errno: 0, data: [...] } 这种格式，可使用 customInsert
        customInsert: function (insertImgFn, result) {
          // result 即服务端返回的接口
          // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
          insertImgFn(result.url)
        }
      }
      this.editor.create()
      this.$nextTick(_ => {
        if (txt) this.editor.txt.html(txt);
      })
    },

    /** 取消按钮 */
    cancel() {
      this.handleClose();
    },
    /** 弹框叉号回调 */
    handleClose() {
      this.courseType = 1;
      this.kfcd = 0;
      this.open = false;
      this.tagValue = [];
      this.tagOptions = [];
      this.reset();
    },

    /** 新增按钮操作 */
    handleAdd1() {
      this.handleClose();
      this.open = true;
      this.title = "添加课件";
      // this.form.fm = ETP_DEFAULT_IMAGE_URL
      this.form.fm =require("@/assets/images/cover.jpg") ;

    },
    /** 修改按钮操作 */
    handleUpdate1(row) {
      this.reset();
      this.radioFlag = true
      const id = row.id || this.ids
      getCourseware(id).then(response => {
        this.open = true;
        this.title = "修改课件";
        if (null != response.data.tags) {
          this.tagValue = response.data.tags.split(",");
        } else {
          this.tagValue = [];
        }
        /**
         * 1,链接 2,文件 3,富文本  // 课件形式
         * Office：1             //  数据库中类型说明
         * 视频：2
         * 其他文件：5
         * 富文本：3
         * 外部链接：4
         */
        this.form = response.data;
        //图片回显
        this.form.fm =  this.form.fm == ""|| this.form.fm == null? require("@/assets/images/cover.jpg") : this.form.fm;

        this.form.learnTime = parseInt(response.data.learnTime)
        this.kfcd = parseInt(this.form.kfcd);
        let xs1 = response.data.xs;
        if (xs1 == "1" || xs1 == "2" || xs1 == "5") {
          this.courseType = 2;
          this.$nextTick(_ => {
            this.$refs.fileUpload.fileList = [
              {
                name: this.form.kjwjmc,
                url: this.form.kjdz,
                fileName: this.form.kjwjmc
              }
            ];
          })
        }

        if (xs1 == "3") {
          this.courseType = 3;
          this.$nextTick(_ => {
            if (this.editor != null) {
              this.editor.destroy();
              this.editor = null;
            }
            this.initEdit(this.form.nr);
          })
        }

        if (xs1 == "4") {
          this.courseType = 1;
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      if (this.courseType === 1) { // 链接
        this.form.xs = "4";
        this.form.kjdz = '';
        this.form.kjwjmc = '';
        this.form.nr = '';
      }
      if (this.courseType === 2) { // 文件
        let type = getExt(this.form.kjdz);

        this.form.xs = type + "";

        this.form.lj = ''
        this.form.nr = ''
      }

      if (this.courseType === 3) { // 富文本
        this.form.nr = this.editor.txt.html();
        this.form.xs = 3 + "";
        this.form.lj = ''
        this.form.kjdz = ''
        this.form.kjwjmc = ''
      }
      this.form.kfcd = this.kfcd;
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.fm.indexOf("/img/cover")>-1){
            this.form.fm = null
          }
          if (this.courseType === 2) { // 文件
            let kjwjmc = this.$refs.fileUpload.fileList[0].fileName;
            this.form.kjwjmc = kjwjmc;
          }
          if (this.form.id != null) {
            if (this.editor != null) this.form.nr = this.editor.txt.html();
            updateCourseware(this.addTags(this.form, this.tagValue)).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              //更新一下标签列表
              this.getTags();
            });
          } else {
            addCourseware(this.addTags(this.form, this.tagValue)).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              //更新一下标签列表
              this.getTags();
            });
          }
        }
      });
    },


  },
  /** 页面销毁 */
  beforeDestroy() {
    // 调用销毁 API 对当前编辑器实例进行销毁
    if (this.editor != null) {
      this.editor.destroy()
      this.editor = null
    }
  }
};
</script>

<style scoped>

::v-deep .el-dialog__body{
  padding: 10px 20px 30px 20px;
}
::v-deep .el-dialog__header {
  padding: 15px 20px 15px;
}
::v-deep .el-dialog__headerbtn{
  top: 17px;
  font-size: 20px;
}
::v-deep .avue-crud__dialog__header {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
}
::v-deep .el-dialog__title {
  color: rgba(0,0,0,.85);
  font-weight: 500;
  word-wrap: break-word;
}
::v-deep .avue-crud__dialog__menu {
  padding-right: 30px;
  float: left;
}
::v-deep .avue-crud__dialog__menu i {
  color: #909399;
  font-size: 15px;
}
::v-deep .el-icon-full-screen{
  cursor: pointer;
}
::v-deep .el-icon-full-screen:before {
  content: "\e719";
}
.desc {
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}
.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.el-form-item__error{
  display: none;
}
.el-checkbox-group {
  margin-left: 30px;
}
</style>
