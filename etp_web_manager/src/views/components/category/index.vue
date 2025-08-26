<template>
  <div class="container">
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
    <div class="head-container">
      <el-tree
        :data="categoryOptions"
        :props="defaultProps"
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        ref="tree"
        node-key="id"
        highlight-current
        default-expand-all
        @node-click="handleNodeClick"
        >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
          <span class="edit-tree" >
            <i class="el-icon-circle-plus-outline" @click.stop="() => addCategory(data)"></i>
            <i class="el-icon-edit" @click.stop="editCategory(data)"></i>
            <i class="el-icon-delete" @click.stop="() => remove(data)" ></i>
          </span>
        </span>
      </el-tree>
    </div>


    <el-dialog
      :title="title"
      :close-on-click-modal="false"
      :visible.sync="open"
      width="600px">
      <el-form ref="form" :model="form" :rules="rules"  label-width="110px" >
        <el-form-item label="上级分类:" prop="parentId">
          <treeselect v-model="form.parentId" :options="categoryOptions"
                      :disabled="form.id==null?false:true"
                      :normalizer="normalizer" placeholder="选择上级分类">
          </treeselect>
        </el-form-item>
        <el-form-item label="分类名称:" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item label="首页是否显示:" prop="sfxs">
          <el-radio-group v-model="form.sfxs">
            <el-radio label="0" >不显示</el-radio>
            <el-radio label="1" >显示</el-radio>
          </el-radio-group>
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
import {updateCategory, addCategory, delCategory} from "@/api/category"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: 'Category',
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
        sfxs:'0',
        type: '0',
      },

      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' },
        ],
        parentId: [
          { required: true, message: '请选择上级分类', trigger: 'blur' },
        ],
      }
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
    filterNode(value, data){
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleNodeClick(data){
      this.$refs.tree.setCurrentKey(data.id)
      this.$emit("clickCategory",data.id)
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
      let tree = null;
      this.$emit('categoryTree', val => {
        console.log("--------------------------")
        console.log(val)
        this.categoryOptions = val
      })
      // this.categoryTreeselect().then(res => {
      //   this.categoryOptions = res.data
      // })
      console.log(tree)
    },

    addCategory(data){
      // this.getTreeselect()
      this.reset()

      if(data != undefined){
        this.form.parentId = data.id
      }

      this.title="新增分类"
      this.open = true
    },
    editCategory(data){
      this.reset()
      this.form.id = data.id
      this.form.parentId = data.parentId
      this.form.name = data.name
      this.form.sfxs = data.sfxs
      this.form.type = data.type
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
    remove(data){
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
