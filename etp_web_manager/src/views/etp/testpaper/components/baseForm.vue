<template>
<div>
  <el-form ref="form" :model="form" :rules="rules" label-width="100px">
    <el-form-item label="试卷名称：" prop="sjmc">
      <el-input v-model="form.sjmc" placeholder="请输入试卷名称" />
    </el-form-item>
    <el-form-item label="标签：" prop="bj">
      <tagsEdit widthSty="100%"
                :parentValue=tagValue
                :selectOptions="tagOptions"
                @selectTags="havaTags">

      </tagsEdit>
    </el-form-item>
    <el-form-item label="试卷分类" class="category" prop="categoryId">
      <treeselect v-model="form.categoryId" :options="categoryOptions"
                  :normalizer="normalizer" placeholder="选择分类"
      />
    </el-form-item>
    <el-form-item label="组卷策略：" prop="zjcl">
      <el-radio v-model="form.zjcl" :label="item.dictValue" v-for="item in zjclOptions"> {{item.dictLabel}}</el-radio>
    </el-form-item>
    <el-form-item label="封面：" prop="fm">
      <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fm"></imageUpload>
    </el-form-item>
    <el-form-item label="设置题型：" >
      <el-checkbox-group v-model="xztx">
        <span v-for="(item,index) in lxOptions">
          <el-checkbox :label="item.dictValue" style="margin-left: 10px;"  @change="checkChange($event,index,item.dictValue)">
            <span>{{item.dictLabel}}</span>
          </el-checkbox>
          <span v-if="xztx.indexOf(item.dictValue)>-1"  style="margin-left: 5px;">
            <el-input-number v-model="txgs[index].value" :min="0"  label="题目个数"></el-input-number>
          </span>
          <br>
        </span>


      </el-checkbox-group>
    </el-form-item>


  </el-form>
</div>
</template>

<script>

import tagsEdit from "@/components/tags/tagsEdit";
import { listTagRelate } from "@/api/etp/tag/tag";
import {ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import { getTreeselect } from '@/api/etp/category/category'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkExistSJ } from "@/api/etp/testpaper/testpaper";


export default {
  name: "paperForm",
  components: {
    tagsEdit,
    Treeselect
  },
  data() {
    var checkMc = (rule, value, callback) => {
      if (!value) {
        callback(new Error('试卷名称不能为空'));
      }else {
        checkExistSJ({ id:this.form.id,sjmc:this.form.sjmc }).then(response => {
          let flag = response.data
          if(flag){
            callback(new Error('试卷名称已经存在'));
          }else{
            callback()
          }
        });
      }
    };
    return {
      // 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷字典
      zjclOptions: [],
      // 类型
      lxOptions: [],

      // 标签用到的
      tagValue: [],
      tagOptions:[],

      // 表单参数
      form: {
        zjcl:"0",
        fm:require("@/assets/images/cover.jpg")
      },
      //题型个数
      txgs: [],
      //选中题型
      xztx:[],
      display:"none",
      //题型和数量
      // tx: [],
      // 表单校验
      rules: {
        sjmc:[
          {required: true, validator: checkMc, trigger: "blur"}
        ],
        categoryId: [
          {required: true,  message: "请选择试卷分类", trigger: "blur"}
        ],
      },
      categoryOptions:[],
    };
  },
  created() {
    this.getDicts("testpaper_zjcl").then(response => {
      this.zjclOptions = response.data;
    });
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    });
    this.getTags();

    this.getTreeselect()
  },
  watch:{
    //监听题型个数变化
    // txgs:{
    //   handler(val){
    //     this.tx =[];
    //     for(let key in val){
    //       let json = {}
    //       json.key = key
    //       json.value = val[key]
    //       this.tx.push(json)
    //     }
    //   },
    //   deep:true
    // },
    //监听复选框变化,不选复选框时，数字变为0
    xztx:{
      handler(val){
        let txgs = this.txgs
        txgs.filter((item,index) =>{
          if(!val.includes(item.key)){
            this.$set(this.txgs,index, {key:item.key,value:0})
          }
        })
      },
      deep:true
    }
  },
  methods: {
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
    havaTags(val){
      this.tagValue = val
    },
    getTags(){
      listTagRelate({modelType:"SJ"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    checkChange(val,index,lx){
      if(val){
        if(lx=='1'||lx=='2'){
          this.$set(this.txgs,index, {key:lx,value:10})
        }else if(lx=='3'||lx=='4'){
          this.$set(this.txgs,index, {key:lx,value:5})
        }else{
          this.$set(this.txgs,index, {key:lx,value:2})
        }
      }

      debugger
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sjmc: null,
        bj: null,
        zjcl: "0",
        fm: null,
        fb: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
    }
  }
}
</script>

<style scoped>
.numcheck{
  left:68px!important;
  display: none;
}
.isBad{
  display: block;
}
</style>
