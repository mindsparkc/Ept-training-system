<template>
  <div class="app-container app-container-custome">
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
      <el-form-item label="课件分类" class="category" prop="categoryIds">
<!--        <treeselect v-model="form.categoryId" :options="categoryOptions"-->
<!--                    :normalizer="normalizer" placeholder="选择分类"-->
<!--        />-->
        <el-cascader
          style="width: 100%;"
          :options="categoryOptions"
          v-model="form.categoryIds"
          :props="{ multiple: true, checkStrictly: true ,value:'id'}"
          clearable></el-cascader>
      </el-form-item>
      <el-form-item label="封面图片" prop="fm">
        <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fm"></imageUpload>
      </el-form-item>

      <el-form-item label="课件形式" prop="xs">
        <el-radio-group v-model="courseType">
<!--          <el-radio :disabled="radioFlag" :label="1">链接</el-radio>-->
          <el-radio :disabled="radioFlag" :label="2">文件</el-radio>
          <el-radio :disabled="radioFlag" :label="3">富文本</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="课件链接" prop="lj" v-show="courseType === 1">
        <el-input v-model="form.lj" placeholder="请输入课件链接"/>
      </el-form-item>
      <el-form-item label="课件内容" prop="nr" v-show="courseType === 3">
        <div id="edit" v-model="form.nr" ></div>
      </el-form-item>
      <el-form-item label="选择课件" prop="kjdz" v-show="courseType === 2" >

        <fileUpload ref="fileUpload" :limit="1" :uploadUrl="'/commom/upload/fastdfs'"
                    :accept="'.pdf,.mp4'"
                    :file-type="['pdf','mp4']"
                    v-model="form.kjdz" @getFileType="getFileType" style="display:inline-block;"/>
        <i class="el-icon-info" style="margin-left: 15px;color: #f19054"></i> <span style="color: #868585">仅能上传PDF、MP4类型文件</span>
        <br>
        <el-checkbox v-model="form.suffix"
          :true-label="'1'"
          :false-label="'0'"
        >ppt文件</el-checkbox><i class="el-icon-info" style="margin-left: 15px;color: #f19054"></i> <span style="color: #868585">如果是PPT转换的PDF文件，请选中</span>

      </el-form-item>

      <el-form-item label="学习时长" prop="learnTime" v-if="courseType !== 1" :key="103">
        <el-input v-model.number="form.learnTime"  placeholder="请输入课件最低学习时长（分钟）">
          <template slot="append">分钟</template>
        </el-input>
      </el-form-item>

      <el-form-item label="课件描述" prop="ms">
        <el-input v-model="form.ms" type="textarea" placeholder="请输入课件描述" maxlength="300" show-word-limit/>
      </el-form-item>
<!--      <el-form-item label="开放程度" prop="kfcd">-->
<!--        <el-radio-group v-model="kfcd">-->
<!--          <el-radio :label="0">完全公开</el-radio>-->
<!--          <el-radio :label="1">限定用户</el-radio>-->
<!--          <el-radio :label="2">不公开</el-radio>-->
<!--        </el-radio-group>-->
<!--      </el-form-item>-->
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" maxlength="300" show-word-limit/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer btns">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>

<script>
import {getExt} from "@/utils/getExt";
import { addCourseware, getCourseware, updateCourseware,checkExistKJ } from '@/api/etp/courseware/courseware'
import E from "wangeditor";
import {getToken} from "@/utils/auth";
import {listTagRelate} from "@/api/etp/tag/tag";
import tagsEdit from "@/components/tags/tagsEdit"
import imageUpload from "@/components/ImageUpload";
import fileUpload from "@/components/FileUpload";
import { ETP_DEFAULT_IMAGE_URL } from "@/utils/ruoyi";
import { getTreeselect } from '@/api/etp/category/category'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { checkExistKC } from '@/api/etp/curriculum/curriculum'
import {getCoursewareTreeselect} from "@/api/category/category";
export default {
  name: "addCourseware",
  components:{
    tagsEdit,imageUpload,fileUpload,Treeselect
  },
  data(){
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
    var checkMc = (rule, value, callback) => {
      if (!value) {
        callback(new Error('课件名称不能为空'));
      }else {
        checkExistKJ({ id:this.form.id,mc:this.form.mc }).then(response => {
          let flag = response.data
          if(flag){
            callback(new Error('课件名称已经存在'));
          }else{
            callback()
          }
        });
      }
    };
    return{
      form:{
        learnTime: 0
      },
      // 表单校验
      rules: {
        mc: [
          {required: true, validator: checkMc, trigger: "blur"}
        ],
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
        categoryIds: [
          {required: true,  message: "请选择课件分类", trigger: "blur"}
        ],
        learnTime: [
          {required: true, message: "学习时长不能为空", trigger: "blur"},
          {type: 'number', message: "学习时长需要是数字", trigger: "blur"}
        ]
      },
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      // 课件形式
      courseType: 2,
      learnFlag:false,
      // 开放程度
      kfcd: 0,
      //控制课件形式是否可选
      radioFlag:false,

      categoryOptions:[],

    }
  },
  watch:{
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
    let catIds = this.$route.query.catIds
    if(typeof catIds ==='string'){
      catIds = [catIds]
    }
    if(undefined != catIds && catIds.length>0){
      this.form.categoryIds = [catIds]
    }
    this.form.fm = require("@/assets/images/cover.jpg")
    const id = this.$route.params.id
    if (id !== undefined && id !== '') {
      this.getCourseInfo(id);
    }
    this.getTreeselect()
  },
  methods:{
    getTreeselect(){
      // getTreeselect().then(res => {
      //   this.categoryOptions = res.data
      // })
      getCoursewareTreeselect().then(res => {
        this.categoryOptions = res.data
      })
    },
    /** 转换分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children,
        // isDisabled: node.parentId == 0
      };
    },
    /**初始化富文本编辑器*/
    initEdit(txt) {
      this.editor = new E('#edit');
      this.editor.config.uploadFileName = 'file';
      this.editor.config.uploadVideoName = 'file';
      this.editor.config.height = 200;
      this.editor.zIndex.baseZIndex = 1000
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
    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "KJ"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },

    getFileType(file){
      // this.learnFlag = false;
      let url = URL.createObjectURL(file);
      let audioElement = new Audio(url);
      let duration;
      duration = audioElement.duration;
      audioElement.addEventListener("loadedmetadata",  () => {
        duration = audioElement.duration;
        this.$set(this.form,"learnTime",Math.ceil(duration / 60))
        // this.learnFlag = true;
      })
    },

    getCourseInfo(id){
      getCourseware(id).then(response => {
        this.radioFlag = true
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
        this.form = response.data
        if(response.data.suffix == 'ppt'){
          this.form.suffix = '1'
        }else{
          this.form.suffix = '0'
        }
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

          if (this.courseType === 2) { // 文件
            let kjwjmc = this.$refs.fileUpload.fileList[0].fileName;
            this.form.kjwjmc = kjwjmc;
          }
          this.$confirm('请确认是否保存当前课件信息?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if(null!=this.form.fm && this.form.fm.indexOf("/img/cover")>-1){
              this.form.fm = null
            }
            if (this.form.id != null) {
              if (this.editor != null) this.form.nr = this.editor.txt.html();
              updateCourseware(this.addTags(this.form, this.tagValue)).then(response => {
                this.msgSuccess("修改成功");
                this.cancel();
                if(this.form.fm ==null){
                  this.form.fm = require("@/assets/images/cover.jpg")
                }
              });

            } else {

              addCourseware(this.addTags(this.form, this.tagValue)).then(response => {
                this.msgSuccess("新增成功");
                this.cancel();
                if(this.form.fm ==null){
                  this.form.fm = require("@/assets/images/cover.jpg")
                }
              });
            }
          }).catch(() => {
          });

        }
      });
    },
    cancel(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1);
      // this.$router.push({path: "/index", query: {t: Date.now()}})
    },
    reset() {
      this.form = {
        id: null,
        mc: null,
        ms: null,
        fm: null,
        categoryIds: null,
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
  }
}
</script>

<style scoped>
.app-container-custome {
  margin: 20px 5%;
  border-radius: 8px;
  border: 1px solid rgba(238, 227, 232, 0.15);
  background: rgba(249, 249, 249, 0.3);
}
.btns {
  margin-top: 15px;
  margin-left: 10%;
}
/*/deep/ .w-e-toolbar{*/
/*  z-index: 0;*/
/*}*/
/*.category /deep/ .w-e-text-container{*/
/*  z-index: 1;*/
/*}*/

</style>
