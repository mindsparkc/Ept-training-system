<template>
<div>
  <el-form ref="form" :model="question" label-width="80px" :rules="rules">
    <el-form-item label="题目" prop="content">
      <div :id="`tg${edit_index}`" v-model="question.content"  @input="changetg"/>
    </el-form-item>

    <div  v-if="question.type=='1'||question.type=='2'||question.type=='3'" class="xx-area">
      <el-form-item label="选项" prop="answers" >
        <el-table
          :data="question.answers"
          border
          ref="tmTable"
          class="typeTable"
          style="width: 100%">
          <el-table-column
            label="是否答案"
            align="center"
            width="100"
          >
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.rightFlag" @change="handleSelectionChange(scope.$index)">
              </el-checkbox>
            </template>
          </el-table-column>
          <el-table-column
            prop="content"
            label="选项内容">
            <template slot-scope="scope">
              <el-form-item
                :prop="'answers.' + scope.$index + '.' + 'content'"
                :rules="selRules">
                <el-input
                  autosize
                  v-model="scope.row.content"
                  type="textarea">
                </el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            align="center"
            v-if="question.type!=3"
            label="操作"
            width="120">
            <template slot-scope="scope">
              <el-button
                @click.native.prevent="del_xx(scope.$index)"
                type="text"
                icon="el-icon-delete"
                size="small">
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-button class="add-btn" @click="add_xx()" type="primary" plain  v-if="question.type!=3">+ 新增选项</el-button>
    </div>

    <!--          填空题-->
    <div  v-if="question.type=='4'" class="tk-da">
      <el-form-item label="答案" class="xx-div"
                    v-for="(item,index) in question.answers"
                    :key="index"
                    :prop="`answers[${index}].contents`"
                    :rules="tkRules"
                    :class="{'labNone':index!=0}">
        <el-select
          v-model="question.answers[`${index}`].contents"
          multiple
          filterable
          popper-class="select-popper"
          allow-create
          default-first-option
          :popper-append-to-body="false"
          style="width: 95%;"
          :placeholder="`请输入第${index+1}个空的答案`">
          <el-option
            v-for="item in []"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <i class="el-icon-delete" v-show="index!=0" @click="del_tk(index)" style="margin-left: 8px;"></i>
      </el-form-item>
      <div class="tips"><i class="el-icon-warning"></i> 每个空可以输入多个答案，输入完成后按“回车键”对答案进行确认</div>
      <el-button class="add-btn" @click="add_tk()" type="primary" plain>+ 新增填空答案 </el-button>
      <!-- el-tooltip content="每个空可以输入多个答案，输入完成后按回车键确定" placement="right-start" effect="light">
        <i class="el-icon-warning" style="color: #fa9443;margin-left: 10px;"></i>
      </el-tooltip -->


    </div>
    <!--  组合题没有解析-->
    <el-form-item label="解析" prop="analysis" v-show="question.type !='6'">
      <div :id="`jx${edit_index}`" v-model="question.analysis"  @input="changejx"/>
    </el-form-item>
  </el-form>
</div>
</template>

<script>
import E from 'wangeditor';
import musicEditor from "@/api/etp/musicEditor";
import {getToken} from "@/utils/auth";
import {exportApp} from "@/api/app/app/app";

export default {
  name: "tm",
  props:{
    question:{
      type:Object,
      default:{
      }
    },
    //子题目 索引
    edit_index:{
      default: ""
    }
  },
  watch:{
    question: {
      handler (newVal, oldVal) { // newVal是获取的是新的值
        this.$forceUpdate()
      },
      deep: true // deep属性是深度监听formData对象里的属性的值是否改变。
    }
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
    var checkXx = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('选项不能为空'));
      }else{
        return callback()
      }
    };
    return{
      pd_radio: '1',
      // 表单校验
      rules: {
        content: [
          { required: true, message: "题目内容不能为空", trigger: "blur" }
        ],
        answers:[
          {required:true,trigger:'blur'}
        ],
        type: [
          { required: true, message: "题目类型不能为空", trigger: "change" }
        ],
        level: [
          { required: true, message: "难度等级不能为空", trigger: "change" }
        ],
        categoryIds: [
          { required: true, message: "题目分类不能为空", trigger: "change", type: "array" }
        ]
      },
      tkRules:[
        {required: true, message: "答案不能为空", trigger: "blur"},
        {validator: checkTk, trigger: 'blur'}
      ],
      selRules:[
        {required: true, validator: checkXx, trigger: "blur"},
      ],
      editor:{}
    }

  },
  mounted() {
    if(this.edit_index!=""){
      this.initSubEdit()
    }
  },
  beforeDestroy() {
    for (var key in this.editor) {
      this.editor[key].destroy()
      // console.log('值 - '+key); /*值*/
      // console.log('值 - '+this.editor[x]); /*值*/
    }

  },
  methods: {

    add_xx(){
      this.$emit('addXx')
    },
    del_xx(index){
      this.$confirm('是否确认删除该选项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$emit('delXx', index)
      }).catch(() => {});

    },
    add_tk(){
      this.$emit('addTk')
    },
    del_tk(index){
      this.$confirm('是否确认删除该填空的答案?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$emit('delTk', index)
      }).catch(() => {});
    },

    textHtmlChanged(e){
      this.question.da=e.target.innerHTML
    },

    // 选中数据
    //原理就是取消所有选择，再选中点击的，达到单选效果
    handleSelectionChange(index) {
      if(this.question.type=='1'||this.question.type=='3'){
        //单选
        this.question.answers.forEach((item,i)=>{
          if(index!=i){
            item.rightFlag = false
          }
        })
      }else{
        //多选
      }

    },
    //***********************************富文本********************************
    initSubEdit(){
      this.editor[`tg${this.edit_index}`] = null
      this.editor[`jx${this.edit_index}`] = null
      //edit 初始化
      this.initEdit(this.question.content,`tg${this.edit_index}`);
      this.initEdit(this.question.analysis,`jx${this.edit_index}`);

    },

    changetg(e){
      this.question.content = e.target.innerHTML
    },
    changejx(e){
      this.question.analysis = e.target.innerHTML
    },
    /**初始化富文本编辑器*/
    initEdit(txt,id_ele) {

      this.editor[id_ele] = new E("#"+id_ele);
      this.editor[id_ele].menus.extend('musicEditor',musicEditor);
      this.editor[id_ele].config.menus = this.editor[id_ele].config.menus.concat('musicEditor');
      this.editor[id_ele].config.uploadFileName = 'file';
      this.editor[id_ele].config.uploadVideoName = 'file';

      this.editor[id_ele].config.height = 200;
      this.editor[id_ele].zIndex.baseZIndex = 1000
      this.editor[id_ele].config.uploadImgHeaders = {
        Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'
      }
      this.editor[id_ele].config.uploadVideoHeaders = {
        Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'
      }
      // 配置图片上传地址
      this.editor[id_ele].config.uploadImgServer = process.env.VUE_APP_BASE_API + '/commom/upload/fastdfs';
      // 配置视频上传地址
      this.editor[id_ele].config.uploadVideoServer = process.env.VUE_APP_BASE_API + '/commom/upload/fastdfs';
      // 图片上传回调
      this.editor[id_ele].config.uploadImgHooks = {
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
      this.editor[id_ele].config.uploadVideoHooks = {
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
      this.editor[id_ele].create()
      this.$nextTick(_ => {
        if (txt) this.editor[id_ele].txt.html(txt);
      })
    },
  }
}
</script>

<style scoped lang="scss">

.xx-area{
  margin-top: 20px;
  ::v-deep .el-form-item__content{
    display: flex;
  }
}
.el-icon-remove-outline{
  margin-left: 10px;
  color: red;
  cursor: pointer;
}
.add-btn{
  //width: 120px;
  margin: -10px 0px 22px 80px;
  //border: 1px dashed #DCDFE6;
}
.labNone{
  ::v-deep .el-form-item__label{
    display: none;
  }
  //
}
::v-deep .el-icon-delete{
  color:red;
  cursor: pointer;
}
//隐藏全选框
.typeTable {
  ::v-deep .el-table__header-wrapper {
    .el-table-column--selection {
      .el-checkbox {
        display: none !important;
      }
      .cell{
        &:after{
          content: '是否选中';
        }
      }
    }
  }

  ::v-deep .el-form-item {
    margin: 10px 0 20px;
  }
}
.tk-da{
  ::v-deep .select-popper{
    display: none;
    background-color: red;
    color: red;
  }
}
::v-deep video{
  width: 285px;
  height: 130px;
}
.tips{
  font-size: 14px;
  color: #fa9443;
  margin-bottom: 20px;
  i{
    color: #fa9443;
  }
  margin-left: 80px;
  margin-top: -10px;
}
</style>
