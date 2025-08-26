<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <KnowledgeCategory ref="category" @clickCategory="handleQuery"></KnowledgeCategory>
      </el-col>
      <el-col :span="20" :xs="24" style="border-left: 1px solid #dddddd;">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px" @submit.native.prevent>
          <el-form-item label="知识点描述" prop="content">
            <el-input
              v-model="queryParams.content"
              placeholder="请输入知识点描述"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="100"
              show-word-limit
            />
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
              v-hasPermi="['knowledge:knowledge:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['knowledge:knowledge:edit']"
            >修改</el-button>
          </el-col>
<!--          <el-col :span="1.5">-->
<!--            <el-button-->
<!--              type="danger"-->
<!--              plain-->
<!--              icon="el-icon-delete"-->
<!--              size="mini"-->
<!--              :disabled="multiple"-->
<!--              @click="handleDelete"-->
<!--              v-hasPermi="['knowledge:knowledge:remove']"-->
<!--            >删除</el-button>-->
<!--          </el-col>-->
    <!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
        </el-row>

<!--        <el-table v-loading="loading" :data="knowledgeList" @selection-change="handleSelectionChange">-->
        <el-table v-loading="loading" :data="knowledgeList" row-key="knoledgeId" lazy
                  style="width: 100%;margin-bottom: 20px;"
                  :tree-props="{children:'children',hasChildren:'hasChildren'}"
                  @select="select" @select-all="selectAll" ref="multipleTable"
                  :load="getChildrens" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
<!--          <el-table-column label="知识点ID" align="center" prop="knoledgeId" />-->
          <el-table-column label="知识点描述" align="left" prop="content" :show-overflow-tooltip="true"/>
    <!--      <el-table-column label="分类ID" align="center" prop="catId" />-->
    <!--      <el-table-column label="数据归属用户" align="center" prop="userId" />-->
    <!--      <el-table-column label="数据归属部门" align="center" prop="deptId" />-->
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="修改" placement="top-start">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['knowledge:knowledge:edit']"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['knowledge:knowledge:remove']"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="新增下级知识点" placement="top-start">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-plus"
                  @click="handleChildAdd(scope.row)"
                  v-hasPermi="['knowledge:knowledge:add']"
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

    <!-- 添加或修改知识点对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="知识点描述">
          <div id="edit" v-model="form.content" ref="content" />
        </el-form-item>
        <el-form-item label="知识点分类" prop="categoryIds">
<!--          <treeselect z-index=9999 v-model="form.catId" :options="categoryOptions"-->
<!--                      :normalizer="normalizer" placeholder="选择分类"-->
<!--          />-->
          <el-cascader
            style="width: 100%;"
            :options="categoryOptions"
            v-model="form.categoryIds"
            :props="{ multiple: true, checkStrictly: true ,value:'id'}"
            clearable></el-cascader>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm(null)">确 定</el-button>
        <el-button @click="cancel(null)">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="childTitle" :visible.sync="childOpen" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="知识点描述">
          <div id="childEdit" v-model="form.content" ref="content" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('child')">确 定</el-button>
        <el-button @click="cancel('child')">取 消</el-button>
      </div>
    </el-dialog>

    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
  </div>
</template>

<script>
import { listKnowledge, getKnowledge, delKnowledge, addKnowledge, updateKnowledge, exportKnowledge, getChildList} from "@/api/etp/knowledge/knowledge";
import E from 'wangeditor';
import musicEditor from '@/api/etp/musicEditor'
import { getToken } from '@/utils/auth'
import KnowledgeCategory from '@/views/etp/knowledge/components/knowledgeCategory'
import {getTreeSelect} from "@/api/etp/knowledge/knowledgeCategory"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import DelDialog from "@/components/DelDialog"

export default {
  name: "Knowledge",
  components:{
    KnowledgeCategory,
    Treeselect,
    DelDialog
  },
  data() {
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
      // 知识点表格数据
      knowledgeList: [],
      // 弹出层标题
      title: "",
      //下级弹出层标题
      childTitle:"",
      // 是否显示弹出层
      open: false,
      // 是否显示下级弹出层
      childOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: null,
        catId: null,
      },
      // 表单参数
      form: {},
      //
      maps: new Map(),
      // 表单校验
      rules: {
        content:[
          {required:true, message:"请输入知识点描述", trigger:"blur"}
        ],
        catId: [
          {required: true,  message: "请选择知识点分类", trigger: "blur"}
        ],
      },
      categoryOptions:[],
      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
      delParentId:null,
      catIds:null,
    };
  },
  created() {
    this.getList();
    this.treeSelect();
  },

  methods: {

    // 重新触发树形表格的loadSearch函数
    refreshLoadTree(parentId) {
      debugger
      // 根据父级id取出对应节点数据
      const { tree, treeNode, resolve } = this.maps.get(parentId)
      this.$set(
        this.$refs.multipleTable.store.states.lazyTreeNodeMap, parentId, [])
      if (tree) {
        this.getChildrens(tree, treeNode, resolve)
      }
    },

    selectAll(selection) {
      // knowledgeList第一层只要有在selection里面就是全选
      const isSelect = selection.some(el => {
        const knowledgeIds = this.knowledgeList.map(j => j.knoledgeId)
        return knowledgeIds.includes(el.knoledgeId)
      })
      // tableDate第一层只要有不在selection里面就是全不选
      const isCancel = !this.knowledgeList.every(el => {
        const knowledgeIds = selection.map(j => j.knoledgeId)
        return knowledgeIds.includes(el.knoledgeId)
      })
      if (isSelect) {
        selection.map(el => {
          if (el.children) {
            // 解决子组件没有被勾选到
            this.setChildren(el.children, true)
          }
        })}
      if (isCancel) {
        this.knowledgeList.map(el => {
          if (el.children) {
            // 解决子组件没有被勾选到
            this.setChildren(el.children, false)
          }
        })
      }
    },

    toggleSelection(tree, select) {
      debugger
      if (tree) {
        this.$nextTick(() => {
          this.$refs.multipleTable &&
          this.$refs.multipleTable.toggleRowSelection(tree, select)
        })
      }
    },

    setChildren(children, type) {
      debugger
      // 编辑多个子层级
      children.map(j => {
        this.toggleSelection(j, type)
        if (j.children) {
          this.setChildren(j.children, type)
        }
      })
    },

    // 选中父节点时，子节点一起选中取消
    select(selection, tree) {
      debugger
      const hasSelect = selection.some(el => {
        return tree.knoledgeId === el.knoledgeId
      })
      if (hasSelect) {
        if (tree.children) {
          // 解决子组件没有被勾选到
          this.setChildren(tree.children, true)
        }
      } else {
        if (tree.children) {
          this.setChildren(tree.children, false)
        }
      }
    },

    //懒加载方法：获取当前节点的子节点数据
    getChildrens(tree,treeNode,resolve){
      if(!tree.hasChildren){
        tree.hasChildren = true
      }
      this.maps.set(tree.knoledgeId, { tree, treeNode, resolve })
      getChildList(tree.knoledgeId).then(res=>{
        debugger
        if(res.data.length && res.data.length>0){
          tree.children = res.data
          resolve(res.data);
        }else{
          tree.hasChildren = false;
          resolve();
        }
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

    treeSelect(){
      console.log("刷新分类树")
      getTreeSelect().then(res => {
        this.categoryOptions = res.data
      })
    },

    destroyEdit(){
      this.editor.txt.html('');
      this.editor.destroy();
    },
    /**初始化富文本编辑器*/
    initEdit(txt,child) {
      if(child != null){
        this.editor = new E('#childEdit');
      }else{
        this.editor = new E('#edit');
      }
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


    /** 查询知识点列表 */
    getList() {
      this.loading = true;
      listKnowledge(this.queryParams).then(response => {
        this.knowledgeList = response.rows;
        // response.rows.forEach((tree,index)=>{
        //   // this.maps.set(tree.knoledgeId,{ tree })
        //   // this.refreshLoadTree(tree.knoledgeId)
        // })
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel(child) {
      if(child != null){
        this.childOpen = false;
        if(this.editor != null){
          this.editor.destroy();
          this.editor = null;
        }
      }else{
        this.open = false;
        if(this.editor != null){
          this.editor.destroy();
          this.editor = null
        }
      }
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        knoledgeId: null,
        content: null,
        catId: null,
        userId: null,
        deptId: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
      this.queryParams.catId = null;
      this.$refs.category.$refs.tree.setCurrentKey(null)
    },
    /** 搜索按钮操作 */
    handleQuery(value) {
      this.queryParams.pageNum = 1;
      if(value!=undefined &&typeof(value.catId) == 'number') {
        this.queryParams.catId = value.catId;
        this.catIds = value.catIds
      }

      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.knoledgeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      console.log("handleAdd()")
      this.reset();
      this.treeSelect(); // 每次点击新增按钮，都刷新分类树
      this.open = true;
      this.title = "添加知识点";
        // this.createDel = true;
      this.$nextTick(_=>{
        this.initEdit('',null);
      })
    },
    /** 新增下级按钮操作 */
    handleChildAdd(row) {
      debugger
      this.reset();
      if(this.maps.get(row.knoledgeId)==undefined){
        let tree = row;
        this.maps.set(row.knoledgeId, { tree })
      }
      this.form.parentId = row.knoledgeId;
      this.form.catId = row.catId;
      this.childOpen = true;
      this.childTitle = "添加下级知识点";
      if(this.editor!=null){
        this.editor.destroy();
        this.editor = null;
      }
      this.$nextTick(_=>{
        this.initEdit('','child');
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row)
      this.reset();
      const knoledgeId = row.knoledgeId || this.ids
      getKnowledge(knoledgeId).then(response => {
        debugger
        this.form = response.data;
        if(this.form.parentId){
          this.childOpen = true;
          this.childTitle = "修改知识点";
          if(this.editor != null){
            this.editor.destroy();
            this.editor = null;
          }
          this.$nextTick(_=>{
            this.initEdit(response.data.content,'child');
          })
        }else{
          this.open = true;
          this.title = "修改知识点";
          if(this.editor != null){
            this.editor.destroy();
            this.editor = null;
          }
          this.$nextTick(_=>{
            this.initEdit(response.data.content,null);
          })
        }
      });
    },
    /** 提交按钮 */
    submitForm(child) {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.content = this.editor.txt.html();
          if (this.form.knoledgeId != null) {
            updateKnowledge(this.form).then(response => {
              this.msgSuccess("修改成功");
              if(child != null){
                this.childOpen = false;
                this.refreshLoadTree(this.form.parentId)
              }else{
                this.open = false;
              }
              this.getList();
            });
          } else {
            addKnowledge(this.form).then(response => {
              this.msgSuccess("新增成功");
              if(child != null){
                this.childOpen = false;
                this.refreshLoadTree(this.form.parentId)
              }else{
                this.open = false;
              }
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      debugger
      // if(row.hasChildren){
      //   this.msgError("有下级列表时，无法进行删除操作！")
      // }else{
      //   const knoledgeIds = row.knoledgeId || this.ids;
      //   this.$confirm('是否确认删除知识点编号为"' + knoledgeIds + '"的数据项?', "警告", {
      //     confirmButtonText: "确定",
      //     cancelButtonText: "取消",
      //     type: "warning"
      //   }).then(function() {
      //     return delKnowledge(knoledgeIds);
      //   }).then(() => {
      //     debugger
      //     this.getList();
      //     if(row.parentId != null){
      //       this.refreshLoadTree(row.parentId);
      //     }
      //     this.msgSuccess("删除成功");
      //   }).catch(() => {});
      // }

      debugger
      if(row.hasChildren){
        this.msgError("有下级列表时，无法进行删除操作！")
      }else{
        this.delInfoIds = row.knoledgeId || this.ids;
        if(!row.knoledgeId){
          this.delInfo = {};
        }else{
          let info = {"知识点描述：":row.content};
          this.delInfo = info;
        }
        if(row.parentId != null){
          this.delParentId = row.parentId;
        }
        this.delVisible = true;
      }
    },

    deleteInfo(ids){
      delKnowledge(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
        if(this.delParentId!=null){
          this.refreshLoadTree(this.delParentId);
          this.delParentId = null;
        }
      });
    },



    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有知识点数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportKnowledge(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
