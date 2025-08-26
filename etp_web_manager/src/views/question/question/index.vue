<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24"  class="span4-left">
        <Category ref="category"  @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="20" :xs="24"  class="span20-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="题目内容" prop="content">
            <el-input v-model="queryParams.content" placeholder="请输入题目内容" clearable size="small">
            </el-input>
          </el-form-item>
          <el-form-item label="题目类型" prop="type">
            <el-select v-model="queryParams.type" placeholder="请选择题目类型" clearable size="small">
              <el-option
                v-for="dict in typeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="难度等级" prop="level">
            <el-select v-model="queryParams.level" placeholder="请选择难度等级" clearable size="small">
              <el-option
                v-for="dict in levelOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
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
              v-hasPermi="['question:question:add']"
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
              v-hasPermi="['question:question:edit']"
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
              v-hasPermi="['question:question:remove']"
            >删除</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="questionList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="题目内容" align="left" prop="display"  :show-overflow-tooltip="true" />
          <el-table-column label="题目类型" align="center" prop="type" :formatter="typeFormat" />
          <el-table-column label="难度等级" align="center" prop="level" :formatter="levelFormat" width="120">
            <template slot-scope="scope">
              <el-tag size="small" v-show="scope.row.level!=null">{{levelFormat(scope.row)}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="是否练习" align="center" prop="tarinFlag" :formatter="isTrainFormat" width="100"/>
          <el-table-column label="是否考试" align="center" prop="examFlag" :formatter="isExamFormat" width="100"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="预览" placement="top">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleView(scope.row)"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="修改" placement="top">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['question:question:edit']"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['question:question:remove']"
                ></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0"
                    :total="total"
                    :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize"
                    @pagination="getList"
        />
      </el-col>
    </el-row>

    <el-dialog
      title=""
      :visible.sync="dialogVisible"
      width="60%">
      <div class="base-div" v-if="questionDetial!=null">
        <el-row>
          <el-col :span="6">
            题目类型：{{typeFormat({type:questionDetial.type})}}
          </el-col>
          <el-col :span="6">
            难易程度：{{levelFormat({level:questionDetial.level})}}
          </el-col>
          <el-col :span="6">
            更新时间：{{questionDetial.updateTime}}
          </el-col>
        </el-row>
      </div>
      <el-divider></el-divider>
      <div>
        <el-card style="margin-bottom: 20px;">
          <qusetion-view :data="questionDetial">
          </qusetion-view>
        </el-card>
        <el-card class="box-card" v-if="questionDetial!=null && questionDetial.children!=null && questionDetial.children.length >0">
          <div class="child-tm" v-for="(item,index) in questionDetial.children">
            <qusetion-view
              :bqShow="false"
              :tm_index="index"
              :xh="index+1"
              :child="true"
              :data="item"></qusetion-view>
          </div>
        </el-card>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
  </div>
</template>

<script>
import { listQuestion, getQuestion, delQuestion, addQuestion, updateQuestion } from "@/api/question/question";
import Category from "@/views/question/question/category/index"
import DelDialog from "@/components/DelDialog"
import qusetionView from "@/views/components/question/questionView"

export default {
  name: "Question",
  components: {qusetionView, Category, DelDialog},
  data() {
    return {
      dialogVisible: false,
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
      // // 题目库表格数据
      questionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 题目类型字典
      typeOptions: [],
      // 难度等级字典
      levelOptions: [],
      // 是否练习字典
      isTrainOptions: [],
      // 是否考试字典
      isExamOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: null,
        type: null,
        level: null,
        isTrain: null,
        isExam: null,
        category: null,
      },

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,

      questionDetial:null,
      catIds:[] //点击树选中的节点
    };
  },
  activated() {
    this.getList();
  },
  created() {

    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("question_level").then(response => {
      this.levelOptions = response.data;
    });
    this.getDicts("subject_yes_no").then(response => {
      this.isTrainOptions = response.data;
      this.isExamOptions = response.data;
    });
    this.getList();
  },
  methods: {
    /** 查询题目库列表 */
    getList() {
      this.loading = true;
      listQuestion(this.queryParams).then(response => {
        this.questionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 题目类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 难度等级字典翻译
    levelFormat(row, column) {
      return this.selectDictLabel(this.levelOptions, row.level);
    },
    // 是否练习字典翻译
    isTrainFormat(row, column) {
      return this.selectDictLabels(this.isTrainOptions, row.tarinFlag);
    },
    // 是否考试字典翻译
    isExamFormat(row, column) {
      return this.selectDictLabels(this.isExamOptions, row.examFlag);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        type: "1",
        level: "1",
        content: "",
        isTrain: "1",
        isExam: "0",
        categoryId:0,
        questionChildList: []
      };
    },
    /** 搜索按钮操作 */
    handleQuery(value) {
      if(value!=undefined &&typeof(value.catId) == 'number') {
        this.queryParams.categoryId = value.catId;
        this.catIds = value.catIds
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
      this.ids = selection.map(item => item.quId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      // this.open = true;
      // this.title = "添加题目库";
      this.$router.push({
        path: "/question-edit/edit",
        query: {
          catIds:this.catIds
        }})
    },
    handleView(row){
      const id = row.quId
      getQuestion(id).then(res => {
        this.questionDetial = res.data
      })
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.quId|| this.ids
      this.$router.push({path: "/question-edit/edit",
        query: {
          quId: id,
          t: Date.now()
        }
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.quId || this.ids;
      if(!row.quId){
        this.delInfo = {}
      }else{
        let info = {"题目":row.display,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delQuestion(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    }
  }
};
</script>
<style scoped lang="scss">
::v-deep .child-tm{
  margin-top: 50px;
  &:first-child{
    margin-top: 20px!important;
  }
}
.base-div{
  padding: 0px 20px;
  color: #9a9a9a;
  font-weight: 300;
}
::v-deep .el-divider{
  margin-top: 5px;
}
::v-deep .el-dialog__body{
  padding-top: 10px;
}
</style>
