<template>
  <div class="container category-container">
    <div class="head-container">
      <el-input
        v-model="categoryName"
        placeholder="请输入分类名称"
        clearable
        size="small"
        prefix-icon="el-icon-search"
        style="margin-bottom: 20px"
      />
<!--      <el-button class="add-btn" size="mini" @click="addCategory()">新增</el-button>-->
    </div>
    <el-button type="primary" style="width: 100%;margin-bottom: 10px" @click="addRoot()">增加根级分类</el-button>
    <div class="tips"><i class="el-icon-warning"></i> 在分类节点上点击鼠标右键可进行操作</div>
    <div class="head-container">
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
<!--          <span class="edit-tree" v-show="data.catId != 0">-->
<!--            <i class="el-icon-circle-plus-outline" @click.stop="() => addCategory(data)"></i>-->
<!--            <i class="el-icon-edit" @click.stop="editCategory(data)"></i>-->
<!--            <i class="el-icon-delete" @click.stop="() => remove(data)" ></i>-->
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
      width="600px">
      <el-form ref="form" :model="form" :rules="rules"  label-width="110px" >
        <el-form-item label="上级分类:" prop="parentId"  v-if="!isRoot">
          <treeselect v-model="form.parentId" :options="categoryOptions"
                      :disabled="form.catId==null?false:true"
                      :normalizer="normalizer" placeholder="选择上级分类"
          >
          </treeselect>
        </el-form-item>
        <el-form-item label="分类名称:" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

<!--        <el-form-item label="首页是否显示:" prop="sfxs">-->
<!--          <el-radio-group v-model="form.sfxs">-->
<!--            <el-radio label="0" >不显示</el-radio>-->
<!--            <el-radio label="1" >显示</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {addKnowledgeCategory,updateKnowledgeCategory,delKnowledgeCategory,getTreeSelect,checkByName} from "@/api/etp/knowledge/knowledgeCategory"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: 'index',
  components: { Treeselect },
  data() {
    let checkMc = (rule,value,callback) => {
      if(!value){
        callback(new Error("请填写分类名称"))
      }else{
        let query = {'parentId':this.form.parentId,'name':value};
        checkByName(query).then(res =>{
          let flag = res.data
          if(flag){
            callback(new Error(res.data))
          }else{
            callback();
          }
        })
      }
    };
    return{
      categoryName:null,
      categoryOptions:[],
      defaultProps: {
        children: "children",
        label: "label"
      },
      //弹窗
      title:"",
      open:false,
      form:{

      },

      rules: {
        name: [
          { required: true, message: '请输入分类名称', validator : checkMc, trigger: 'blur' },
        ],
        parentId: [
          { required: true, message: '请选择上级分类', trigger: 'blur' },
        ],
      },
      isRoot:false,
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
      return data.name.indexOf(value) !== -1;
    },
    handleNodeClick(data,node){
      this.$refs.tree.setCurrentKey(data.id)
      this.catIds =[]
      this.getIds(node)
      this.menuVisible = false
      this.$emit("clickCategory",{catId:data.id,catIds:this.catIds})
      // this.$emit("clickCategory",data.catId)
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
        // isDisabled: node.parentId == 0
      };
    },
    getTreeselect(){
      getTreeSelect().then(res => {
        this.categoryOptions = res.data
        this.categoryOptions.unshift({id:-1,label:"未分类"})
        this.categoryOptions.unshift({id:0,label:"全部试知识点"})
        console.log(this.categoryOptions)
      })
    },
    addRoot(){
      this.reset()
      this.isRoot = true
      this.title="新增根级分类"
      this.open = true
    },
    addCategory(){
      // this.getTreeselect()
      this.reset()
      let data = this.currentData

      if(data != undefined){
        this.form.parentId = data.id
      }
      if(data.id==0){
        this.addRoot()
      }else{
        this.title="新增分类"
        this.open = true
      }

    },
    editCategory(){
      this.reset()
      let data = this.currentData
      this.form.id = data.id
      this.form.parentId = data.parentId
      this.form.name = data.name
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
            if (this.form.id != undefined) {
              updateKnowledgeCategory(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getTreeselect();
              });
            } else {
              addKnowledgeCategory(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getTreeselect();
              });
            }
          }).catch(() => {});

        }
      });
    },
    remove(){
      let data = this.currentData
        this.$confirm('是否确认名称为【'+data.label+'】的分类?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delKnowledgeCategory(data.id);
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
