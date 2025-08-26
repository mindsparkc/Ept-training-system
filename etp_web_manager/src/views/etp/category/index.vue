<template>
  <div class="container">

    <div class="head-container">
      <el-input
        v-model="categoryName"
        placeholder="请输入分类名称"
        clearable
        size="small"
        prefix-icon="el-icon-search"
        style="margin-bottom: 10px"
      />
<!--      <el-button class="add-btn" size="mini" @click="addCategory()">新增</el-button>-->
    </div>
    <div class="tips"><i class="el-icon-warning"></i> 在分类节点上点击鼠标右键可进行操作</div>
    <div class="tree-div">
      <el-tree
        class="cate-tree"
        :data="categoryOptions"
        :props="defaultProps"
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        ref="tree"
        node-key="id"
        highlight-current
        default-expand-all
        @node-click="handleNodeClick"
        @node-contextmenu="rightClick"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">

          <span><svg-icon v-show="data.id==1" icon-class="point" style="margin-left: -18px;"></svg-icon>&nbsp;&nbsp;
            <el-tooltip class="item" effect="dark" :content="node.label" placement="top-start">
              <span>{{ node.label }}</span>
            </el-tooltip>

          </span>
<!--          <span class="edit-tree" v-show="data.id != 0">-->
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
      <div class="delete" @click="editCategory">
        <i class="el-icon-edit"></i>&nbsp;&nbsp;修改分类
      </div>
      <div class="edit" @click="remove">
        <i class="el-icon-delete"></i>&nbsp;&nbsp;删除分类
      </div>
    </el-card>
    <el-dialog
      :title="title"
      :close-on-click-modal="false"
      :append-to-body="true"
      :visible.sync="open"
      width="600px">
      <el-form ref="form" :model="form" :rules="rules"  label-width="110px" >
        <el-form-item label="上级分类:" prop="parentId" v-show="this.form.parentId!=0">
          <treeselect v-model="form.parentId" :options="categoryOptions"
                      :disabled="form.id==null?false:true"
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
import {listCategory,addCategory,updateCategory,delCategory,getTreeselect} from "@/api/etp/category/category"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: 'index',
  components: { Treeselect },
  data() {
    return{
      categoryName:null,
      categoryOptions:[],
      defaultProps: {
        children: "children",
        label: "name"
      },
      //弹窗
      title:"",
      open:false,
      form:{
        sfxs:'0'
      },
      isScroll:false,
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
      currentData:''
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
    // 题目类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    filterNode(value, data){
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleNodeClick(data){
      this.menuVisible = false
      this.$refs.tree.setCurrentKey(data.id)
      this.$emit("clickCategory",data.id)
    },
    // 鼠标右击事件
    rightClick(event, object, Node, element) {
      // console.log(event, object)
      this.currentData = object
      this.currentNode = Node

      this.menuVisible = true
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
    getTreeselect(){
      getTreeselect().then(res => {
        this.categoryOptions = res.data
      })
    },

    addCategory(){
      let data = this.currentData
      // this.getTreeselect()
      this.reset()

      if(data != undefined){
        this.form.parentId = data.id
      }

      this.title="新增分类"
      this.open = true
    },
    editCategory(){
      let data = this.currentData
      this.reset()
      this.form.id = data.id
      this.form.parentId = data.parentId
      this.form.name = data.name
      this.form.sfxs = data.sfxs
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
              updateCategory(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getTreeselect();
              });
            } else {
              addCategory(this.form).then(response => {
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
        this.$confirm('是否确认名称为【'+data.name+'】的分类?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCategory(data.id);
        }).then(response => {
          this.getTreeselect();
          this.msgSuccess(response.msg);
        }).catch(() => {});
    },
    reset(){
      this.form={
        id:null,
        name:null,
        parentId:null,
        sfxs:'0'
      }
      this.resetForm("form");
    }
  }
}
</script>

<style scoped lang="scss">
.container{
  width: 100%;

}
.head-container{
  display: flex;
}
.tree-div{
  height: calc(100vh - 100px - 52px - 50px);
  width: 100%;
  overflow: auto;
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
    color: #0696f1;
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
i{
  color: #0f7cc2;
}
.el-icon-delete{
  color: red;
}
.edit-tree{
  display: inline-block;
  position: absolute;
  left: 220px;
}

.cate-tree {
  min-width: 100%;
  display: inline-block;

}

::v-deep .el-tree-node {
  position: relative;
}
.box-card {
  width: 140px;
  position: absolute;
  z-index: 100000;
  div{
    line-height: 40px;
    border-bottom: 1px solid #ddd;
  }
}
/* 点击节点时的选中颜色 */
::v-deep .el-tree-node.is-current > .el-tree-node__content {
  color: blue !important;
}

::v-deep .el-card__body {
  padding: 10px !important;
  > div {
    &:hover {
      color: blue;
      cursor: pointer;
    }
  }
}
.tips{
  font-size: 14px;
  color: #fa9443;
  margin-bottom: 10px;
  i{
    color: #fa9443;
  }

}
</style>
