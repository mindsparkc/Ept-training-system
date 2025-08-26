<template>
  <div class="app-container app-container-custome">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="类型：" prop="lx">
        <el-radio v-model="form.lx" :label="dict.dictValue" v-for="dict in lxOptions"> {{dict.dictLabel}}
        </el-radio>
      </el-form-item>
      <el-form-item label="难易程度：" prop="level">
        <el-radio v-model="form.level" :label="dict.dictValue" v-for="dict in levelOptions"> {{dict.dictLabel}}
        </el-radio>
      </el-form-item>
      <el-form-item label="题干：" prop="tg">
        <div id="edit" v-model="form.tg" ref="tg" />
      </el-form-item>
      <el-form-item label="标签：" prop="tagValue" >
        <tagsEdit widthSty="832px"
                  :parentValue=tagValue
                  :selectOptions="tagOptions"
                  @selectTags="havaTags">

        </tagsEdit>
      </el-form-item>
      <el-form-item label="题目分类" class="category" prop="categoryId">
        <treeselect v-model="form.categoryId" :options="categoryOptions"
                    :normalizer="normalizer" placeholder="选择分类"
        />
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

      <el-form-item label="正确答案：" prop="da" v-if="form.lx!='4'">
        <el-radio v-if="form.lx=='1'" v-model="dx_radio" :label="item.key" v-for="item in form.xxList"> {{item.key}}
        </el-radio>

        <el-checkbox-group v-model="da_checkList" v-if="form.lx=='2'">
          <el-checkbox :label="item.key" v-for="item in form.xxList"> {{item.key}}</el-checkbox>
        </el-checkbox-group>

        <el-radio-group v-if="form.lx=='3'" v-model="pd_radio">
          <el-radio  :label="'1'"> 对</el-radio>
          <el-radio  :label="'0'"> 错</el-radio>
        </el-radio-group>
        <div class="el-textarea__inner show-result" contenteditable="true" v-if="form.lx=='5'"
             v-html="form.da" @blur="textHtmlChanged"></div>
      </el-form-item>

      <!--          填空题-->
      <div  v-if="form.lx=='4'">
        <el-form-item label="正确答案：" class="xx-div"
                      v-for="(item,index) in form.tk_list"
                      :key="index"
                      :prop="`tk_list[${index}]`"
                      :rules="tkRules"
                      :class="{'labNone':index!=0}">
          <!--            <el-input placeholder="请输入答案"  v-model="item.value"/>-->
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
<!--        <div id="edit2" v-model="form.jx" ref="tg" />-->
        <el-input v-model="form.jx" type="textarea" placeholder="请输入内容" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>

<script>
import tagsEdit from "@/components/tags/tagsEdit";
import { addSubject, getSubject, updateSubject } from '@/api/etp/subject/subject'
import {listTagRelate} from "@/api/etp/tag/tag";
import { getTreeselect } from '@/api/etp/category/category'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import E from 'wangeditor';
import { getToken } from '@/utils/auth'
import musicEditor from '@/api/etp/musicEditor'

export default {
  name: "addSubject",
  components:{tagsEdit,Treeselect},
  created() {
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    });

    this.getDicts("subject_level").then(response => {
      this.levelOptions = response.data;
    });
    this.getTags();
    const id = this.$route.params.id
    if (id !== undefined && id !== '') {
      this.getSubjectInfo(id);
    }else{
      this.form.lx = '1';
      this.da_checkList=['A'];
      this.dx_radio = 'A';
      this.tagValue = [];
    }
    this.getTreeselect()
  },
  mounted() {
    const id = this.$route.params.id
    if (id == undefined || id == '') {
      this.initEdit('');
    }
  },
  watch:{
    da_checkList(val, oldVal){
      this.form.da = val.join(',');
    },
  },
  data(){
    var checkTk=(rule, value, callback) => {
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
    };
    return{
      //答案选项
      pd_radio: '1',//判断答案
      da_checkList:['A'],
      dx_radio: '',
      // 标签用到的
      tagValue: [],
      tagOptions:[],
      // 类型
      lxOptions: [],
      // 表单参数
      form: {
        sfks:'1',
        lx:'1',
        xxList:[{key:'A',value:''},{key:'B',value:''},{key:'C',value:''},{key:'D',value:''}],
        //填空答案
        // tk_list:[{value:'' }],
        tk_list:[[]],
        level:'1',
      },
      tmTypeRadio:'3',

      // 表单校验
      rules: {
        tg: [
          { required: true, message: "题干不能为空", trigger: "blur" }
        ],
        categoryId: [
          {required: true,  message: "请选择题目分类", trigger: "blur"}
        ],
      },
      tkRules:[
        {required: true, message: "答案不能为空", trigger: "blur"},
        {validator: checkTk, trigger: 'blur'}
      ],
      categoryOptions:[],
      // 难易程度
      levelOptions: [],
    }
  },
  methods:{
    getTreeselect(){
      getTreeselect().then(res => {
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
    textHtmlChanged(e){
      this.form.da=e.target.innerHTML
    },
    getTags(){
      listTagRelate({modelType:"TK"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val){
      this.tagValue = val
    },
    getSubjectInfo(id) {
      getSubject(id).then(response => {
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
          if (this.editor != null) {
            this.editor.destroy();
            this.editor = null;
          }
          this.initEdit(response.data.tg);
        })
      });
      console.log(this.form)
    },
    add_xx(){
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
    },
    /** 提交按钮 */
    submitForm() {
      this.form.tg = this.editor.txt.html();
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
          this.$confirm('请确认是否保存当前试题信息?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.form.id != null) {
              updateSubject(this.addTags(this.form,this.tagValue)).then(response => {
                this.msgSuccess("修改成功");
                this.cancel()
              });
            } else {
              addSubject(this.addTags(this.form,this.tagValue)).then(response => {
                this.msgSuccess("新增成功");
                this.cancel()
              });
            }
          }).catch(() => {});


        }
      });
    },
    // 取消按钮
    cancel() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1);
      // this.$router.push({path: "/index", query: {t: Date.now()}})
    },
    reset() {
      this.form = {
        id: null,
        tg: null,
        lx: null,
        xx: null,
        da: null,
        jx: null,
        sflx: '0',
        sfks: '1',
        level:'1',
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
    },

    /**初始化富文本编辑器*/
    initEdit(txt) {
      debugger

        this.editor = new E('#edit');
        this.editor.menus.extend('musicEditor',musicEditor);
        this.editor.config.menus = this.editor.config.menus.concat('musicEditor');
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
  }
}
</script>

<style lang="scss" scoped>
.el-icon-remove-outline{
  margin-left: 10px;
  color: red;
  cursor: pointer;
}
.app-container-custome {
  margin: 20px 5%;
  border-radius: 8px;
  border: 1px solid rgba(238, 227, 232, 0.15);
  background: rgba(249, 249, 249, 0.3);
  .add-btn{
    width: calc(100% - 140px);
    border: 1px dashed #DCDFE6;
    margin: -10px 0px 22px 100px;
  }
  .dialog-footer{
    margin-top: 15px;
    margin-left: 10%;
  }
  .el-input-group{
    width: calc(100% - 40px)
  }
}
::v-deep .vue-treeselect__menu-container{
  z-index: 9999!important;
}
</style>
