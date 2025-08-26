<template>
  <div class="container category-container">
    <div class="head-container">
      <el-input
        v-model="categoryName"
        placeholder="请输入分类名称"
        clearable
        size="small"
        prefix-icon="el-icon-search"
        style="margin-bottom: 10px"
      />
    </div>
    <el-button type="primary" style="width: 100%;margin-bottom: 10px" @click="addRoot()">增加根级分类</el-button>
    <div class="tips"><i class="el-icon-warning"></i> 在分类节点上点击鼠标右键可进行操作</div>
    <div class="tree-div">
      <el-tree
        :data="categoryOptions"
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        :props="defaultProps"
        ref="tree"
        node-key="id"
        highlight-current
        default-expand-all
        @node-click="handleNodeClick"
        @node-contextmenu="rightClick"
        >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>
            <svg-icon v-show="data.id==0 || data.id==-1" icon-class="point" style="margin-left: -18px;"></svg-icon>&nbsp;&nbsp;
            {{ node.label }}</span>
<!--          <span class="edit-tree" v-show="data.id != 0">-->
<!--            <i class="el-icon-circle-plus-outline" @click.stop="() => addCategory(node,data)"></i>-->
<!--            <i class="el-icon-edit" @click.stop="editCategory(node,data)"></i>-->
<!--            <i class="el-icon-delete" @click.stop="() => remove(node,data)" ></i>-->
<!--          </span>-->
        </span>
      </el-tree>
    </div>
    <el-card class="box-card" ref="card" v-show="menuVisible">
      <div class="add" @click="addCategory">
        <i class="el-icon-circle-plus-outline"></i>&nbsp;&nbsp;新增分类
      </div>
      <div class="delete" @click="editCategory" v-show="rootVisible">
        <i class="el-icon-edit"></i>&nbsp;&nbsp;修改分类
      </div>
      <div class="edit" @click="remove" v-show="rootVisible">
        <i class="el-icon-delete"></i>&nbsp;&nbsp;删除分类
      </div>
    </el-card>

    <el-dialog
      :title="title"
      :close-on-click-modal="false"
      :visible.sync="open"
      :append-to-body="true"
      width="600px">
      <el-form ref="form" :model="form" :rules="rules"  label-width="110px" >
        <el-form-item label="上级分类:" prop="parentId" v-if="!isRoot">
          <treeselect v-model="form.parentId" :options="categoryOptions"
                      :disabled="form.catId==null?false:true"
                      :normalizer="normalizer" placeholder="选择上级分类"
          >
          </treeselect>
        </el-form-item>
        <el-form-item label="分类名称:" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item label="显示排序:" prop="sort"  v-show="!isRoot">
          <el-input-number v-model="form.sort" controls-position="right" :min="0" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {addPaperCategory,updatePaperCategory,getPaperCategory,getPaperTreeselect,delPaperCategory,
      addExamCategory,updateExamCategory,getExamTreeselect,getExamCategory,delExamCategory} from "@/api/category/category"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: 'index',
  components: { Treeselect },
  props:{
    menu:{
      type:String,
      default:"question"
    }
  },
  data() {
    return{
      categoryName:null,
      categoryOptions:[],
      defaultProps: {
        id: "id",
        children: "children",
        label: "label"
      },
      //弹窗
      title:"",
      open:false,
      form:{
      },

      isRoot:false,
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
        ],
        parentId: [
          { required: true, message: '请选择上级分类', trigger: 'blur' },
        ],
      },
      // =====右键菜单相关
      menuVisible: false,
      rootVisible:false,//全部
      currentData:'',
      catIds:[]
    }
  },
  created() {
    this.getTreeselect()

  },
  watch: {
    // 根据名称筛选分类树
    categoryName(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods:{
    // 鼠标右击事件
    rightClick(event, object, Node, element) {
      this.currentData = object
      // this.currentNode = Node
      this.menuVisible = true
      //全部题目
      if(object.id ==0){
        this.rootVisible = false
      }else{
        this.rootVisible = true
      }

      let ele = document.getElementsByClassName("hideSidebar")[0]
      if(ele == undefined){
        if(event.clientX - 200 >200){
          this.$refs.card.$el.style.left = event.clientX - 150 -200 + 'px'
          this.$refs.card.$el.style.top = event.clientY - 90 + 'px'
        }else{
          this.$refs.card.$el.style.left = event.clientX - 200 + 'px'
          this.$refs.card.$el.style.top = event.clientY - 90 + 'px'
        }
      }else{
        if(event.clientX - 54 >200){
          this.$refs.card.$el.style.left = event.clientX- 150 - 54 + 'px'
          this.$refs.card.$el.style.top = event.clientY - 90 + 'px'
        }else{
          this.$refs.card.$el.style.left = event.clientX - 54 + 'px'
          this.$refs.card.$el.style.top = event.clientY - 90 + 'px'
        }
      }
      document.addEventListener('click', this.foo)
    },
    //  取消鼠标监听事件 菜单栏
    foo() {
      this.menuVisible = false
      //  要及时关掉监听，不关掉的是一个坑，不信你试试，虽然前台显示的时候没有啥毛病，加一个alert你就知道了
      document.removeEventListener('click', this.foo)
    },
    filterNode(value, data){
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleNodeClick(data,node){
      this.menuVisible = false
      this.catIds =[]
      this.getIds(node)
      this.$refs.tree.setCurrentKey(data.id)
      this.$emit("clickCategory",{catId:data.id,catIds:this.catIds})
    },
    // 递归函数
    getIds(node) {
      if (!node.parent) {// 若无父节点，则直接返回
        return
      }
      this.catIds.unshift(node.data.id)// 将value保存起来
      //调用递归
      this.getIds(node.parent)
    },
    /** 转换分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.label,
        children: node.children,
      };
    },
    getTreeselect(){
      if(this.menu =='paper'){
        getPaperTreeselect().then(res => {
          this.categoryOptions = res.data
          this.categoryOptions.unshift({id:-1,label:"未分类"})
          this.categoryOptions.unshift({id:0,label:"全部试卷"})
        })
      }else if(this.menu =='exam'){
        getExamTreeselect().then(res => {
          this.categoryOptions = res.data
          this.categoryOptions.unshift({id:-1,label:"未分类"})
          this.categoryOptions.unshift({id:0,label:"全部考试"})
        })
      }

    },
    addRoot(){
      this.reset()
      this.isRoot = true
      this.title="新增根级分类"
      this.open = true
    },
    addCategory(){
      this.reset()
      let data = this.currentData
      if(data != undefined){
        this.form.parentId = data.id
      }
      if(data.id==0){
        this.addRoot()
      }else{
        this.title="新增题库"
        this.open = true
      }
    },
    editCategory(){
      let data = this.currentData
      this.reset()
      if(this.menu=="paper"){
        getPaperCategory(data.id).then(res=>{
          this.form = res.data
        })
      }else if(this.menu=="exam"){
        getExamCategory(data.id).then(res=>{
          this.form = res.data
        })
      }
      this.title="修改分类"
      this.open = true
    },
    submitForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$confirm('请确认是否保存当前分类信息?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.form.catId != undefined) {
              delete this.form.parentId
              if(this.menu=="paper"){
                updatePaperCategory(this.form).then(response => {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getTreeselect();
                });
              }else if(this.menu=="exam"){
                updateExamCategory(this.form).then(response => {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getTreeselect();
                });
              }

            } else {
              if(this.menu=="paper"){
                addPaperCategory(this.form).then(response => {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getTreeselect();
                });
              }else if(this.menu=="exam"){
                addExamCategory(this.form).then(response => {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getTreeselect();
                });
              }

            }
          }).catch(() => {});

        }
      });
    },
    remove(){
      let data = this.currentData
      let that = this
      this.$confirm('是否确认名称为【'+data.label+'】的分类?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        if(that.menu=="paper"){
          return delPaperCategory(data.id);
        }else if(that.menu=="exam"){
          return delExamCategory(data.id);
        }
      }).then(response => {
        this.getTreeselect();
        this.msgSuccess(response.msg);
      }).catch(() => {});
    },
    reset(){
      this.form={
        catId:null,
        name:null,
        parentId:null,
        sort: null,
      }
      this.isRoot = false
      this.resetForm("form");
    }
  }
}
</script>

<style scoped lang="scss">
.head-container:first-of-type{
  display: flex;
}
.add-btn{
  margin-bottom: 20px;
  margin-left: 6px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  span:nth-of-type(2){
    color: #1c84c6;
    i{
      margin-left: 5px;
      cursor: pointer;
    }
    i:nth-of-type(3){
      color: red;
    }
  }
  &:hover{
    .edit-tree{
      display: inline-block;
    }
  }
}
.edit-tree{
  display: none;
}
</style>
