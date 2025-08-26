<template>
<div class="app-container">
  <el-row :gutter="20">
    <el-col :span="4" :xs="24" class="span4-left">
      <Category ref="category"
                :menu="'paper'"
                @clickCategory="handleQuery"></Category>
    </el-col>
    <el-col :span="20" :xs="24" class="span20-right">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="试卷名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入试卷名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="试卷状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择试卷状态"
            clearable size="small">
            <el-option
              v-for="dict in fbOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
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
            v-hasPermi="['paper:paper:add']"
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
            v-hasPermi="['paper:paper:edit']"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['paper:paper:remove']"
          >删除</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="paperList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="试卷名称" align="left" prop="name" :show-overflow-tooltip="true" />
<!--        <el-table-column label="组卷策略" align="center" prop="strategy" :formatter="zjclFormat"/>-->
        <el-table-column label="试卷总分" align="center" prop="totalScore" />
        <el-table-column label="及格分" align="center" prop="qualifyScore" />
        <el-table-column label="题目数量" align="center" prop="quCount" />
        <el-table-column label="试卷状态" align="center" prop="status" >
          <template slot-scope="scope">
            <el-tag :type="scope.row.status == '0'?'info':'success'">{{ scope.row.status == '0' ? '暂存' : '已发布' }}</el-tag>
          </template>

        </el-table-column>
<!--        <el-table-column label="数据归属用户" align="center" prop="userId" />-->
<!--        <el-table-column label="版本号" align="center" prop="version" />-->
        <el-table-column label="操作" align="left" class-name="small-padding fixed-width" width="100">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="dark" content="详情" placement="top-start">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="编辑" placement="top-start">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                v-show="scope.row.status != 99"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['paper:paper:edit']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="发布" placement="top-start" v-show="scope.row.status==0">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-s-promotion"
                @click="handleFb(scope.row,99)"
                v-hasPermi="['paper:paper:edit']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="取消发布" placement="top-start" v-show="scope.row.status==99">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-position"
                @click="handleFb(scope.row,0)"
                v-hasPermi="['paper:paper:edit']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                style="color: red"
                @click="handleDelete(scope.row)"
                v-hasPermi="['paper:paper:remove']"
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

<!--  <el-dialog
    title="新增试卷"
    :visible.sync="dialogVisible"
    width="600px">
    <el-form :model="form" ref="form" :rules="rules"  label-width="110px">
      <el-form-item label="试卷名称" prop="name">
        <el-input
          v-model="form.name"
          placeholder="请输入试卷名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="试卷分类" prop="categoryIds" >
        <el-cascader
          style="width: 100%;"
          :options="categoryOptions"
          v-model="form.categoryIds"
          :props="{ multiple: true, checkStrictly: true ,value:'id'}"
          clearable></el-cascader>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button  @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="goEdit">下一步</el-button>
    </span>
  </el-dialog>-->

  <DelDialog :delVisible="delVisible"
             :delInfo="delInfo"
             :delInfoId="delInfoIds"
             @deleteInfo = "deleteInfo"
             @delClose="delVisible = false"></DelDialog>
</div>
</template>

<script>
import Category from "@/views/category/index"
import DelDialog from "@/components/DelDialog";
import { listPaper, getPaper, delPaper, addPaper,
  updatePaper, exportPaper, updateStatus } from "@/api/paper/paper";

export default {
  name: "Paper",
  components: {Category, DelDialog},
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
      // 试卷基础信息表格数据
      paperList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        version: null,
        name: null,
        content: null,
        strategy: null,
        totalScore: null,
        suggestTime: null,
        qualifyScore: null,
        quCount: null,
        status: null,
        userId: null,
        deptId: null,
        flag: 'exam',
      },
      // 表单参数
      form: {},
      // 表单校验

      fbOptions:[],
      zjclOptions:[],

      //=============新增弹窗===
      dialogVisible: false,


      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
      catIds:[],
    };
  },
  created() {
    this.getDicts("testpaper_fb").then(response => {
      this.fbOptions = response.data;
    });
    this.getDicts("paper_type").then(response => {
      this.zjclOptions = response.data;
    });
    this.getList();

  },
  methods: {
    // 发布状态：0-暂存，1-发布字典翻译
    fbFormat(row, column) {
      return this.selectDictLabel(this.fbOptions, row.status);
    },
    zjclFormat(row, column) {
      return this.selectDictLabel(this.zjclOptions, row.strategy);
    },
    /** 查询试卷基础信息列表 */
    getList() {
      this.loading = true;
      listPaper(this.queryParams).then(response => {
        this.paperList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        name: null,
        categoryIds: null,
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery(value) {
      if(value!=undefined &&typeof(value.catId) == 'number') {
        this.queryParams.categoryId = value.catId;
        this.catIds = value.catIds;
      }
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.$refs.category.$refs.tree.setCurrentKey(null)
      this.queryParams.categoryId = null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.paperId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      // this.dialogVisible = true;
      this.$router.push({
        path: "/paper-edit/edit",
        query: {
          catIds:this.catIds
        }
      })
    },
    handleView(row){
      const id = row.paperId
      this.reset();
      let router = this.$router.resolve({
        path: "/paper-view/view",
        query: { paperId: id}
      })
      window.open(router.href,'_blank')
    },

    handleUpdate(row){
      const id = row.paperId|| this.ids
      this.reset();
      this.$router.push({
        path: "/paper-edit/edit",
        query: {
          paperId: id,
          t: Date.now()
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.paperId || this.ids;
      if(!row.paperId){
        this.delInfo = {}
      }else{
        let info = {"试卷名称":row.name,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delPaper(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有试卷基础信息数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.exportLoading = true;
        return exportPaper(queryParams);
      }).then(response => {
        this.download(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },
    handleFb(row,status){
      let label = "发布"
      if(status==0){
        label = "取消发布"
      }
      let param ={
        paperId: row.paperId,
        status: status
      }
      this.$confirm('是否确认'+label+'试卷?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return updateStatus(param);
      }).then(response => {
        this.$message.success(label+"成功！")
        this.getList()
      }).catch(() => {});
    }
  }

}
</script>

<style scoped>

</style>
