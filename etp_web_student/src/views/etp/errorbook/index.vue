<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-row>
        <el-form-item label="题干" prop="tg">
          <el-input
            v-model="queryParams.tg"
            placeholder="请输入题干"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="题目类型">
          <el-radio-group v-model="queryParams.stlx" placeholder="请选择回复状态" clearable size="small" @change="handleQuery">
            <el-radio v-if="dict.dictLabel!='主观题'"
              v-for="dict in stlxOptions"
              :key="dict.dictValue"
              :label="dict.dictValue">
              {{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-row>
      <el-row>
        <el-col :span="19">
          <el-form-item label="错题来源">
            <el-radio-group v-model="queryParams.ctlx" @change="handleQuery">
              <el-radio label="">全部</el-radio>
              <el-radio label="1">考试</el-radio>
              <el-radio label="2">练习</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['etp:errorbook:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['etp:errorbook:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['etp:errorbook:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--		  :loading="exportLoading"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['etp:errorbook:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="errorbookList" @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="right" inline class="demo-table-expand">
            <el-form-item label="选项：" style="width: 100%">
              <template v-if="props.row.stlx=='1'||props.row.stlx=='2'">
                <el-row v-for="(item) in JSON.parse(props.row.xx)">
                  <el-col :span="24">
                    <span>{{item.key}}、{{item.value}}</span>
                  </el-col>
                </el-row>
              </template>
              <template v-else>
                <el-row>
                  <el-col :span="24">
                    <span>1、正确</span>
                  </el-col>
                  <el-col :span="24">
                    <span>2、错误</span>
                  </el-col>
                </el-row>
              </template>
            </el-form-item>
            <el-form-item label="学员答案：">
              <span v-if="props.row.stlx=='3'">
                {{props.row.answered=='0'?'错误':'正确'}}
              </span>
              <span v-else-if="props.row.stlx=='4'" v-for="(val,key) in JSON.parse(props.row.answered)">
                <span>{{val}}，</span>
              </span>
              <span v-else>{{props.row.answered}}</span>
            </el-form-item>
            <el-form-item label="标准答案：">
              <span v-if="props.row.stlx=='3'">
                {{props.row.da=='0'?'错误':'正确'}}
              </span>
              <span v-else-if="props.row.stlx=='4'" v-for="(val,key) in JSON.parse(props.row.da)">
                <span v-for="(v,k) in val">{{v}}，</span>
              </span>
              <span v-else>
                {{props.row.da}}
              </span>
            </el-form-item>
            <el-form-item label="解析：">
              <span>{{props.row.jx}}</span>
            </el-form-item>
            <el-form-item label="备注：">
              <span>{{props.row.remake}}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <!-- el-table-column type="selection" width="55" align="center" / -->
<!--      <el-table-column label="主键id" align="center" prop="id" />-->
      <el-table-column label="序号" type="index" align="center"/>
      <el-table-column label="题干" align="left" prop="tg" min-width="300px"/>
      <el-table-column label="题目类型" align="center" prop="stlx" :formatter="stlxFormat" />
      <el-table-column label="错题来源" align="center" prop="ctlx">
        <template slot-scope="scope">
          {{scope.row.ctlx=='1'?'考试':'练习'}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['etp:errorbook:remove']"
          >移除</el-button>
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

    <!-- 添加或修改错题集对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="试题id" prop="stid">
          <el-input v-model="form.stid" placeholder="请输入试题id" />
        </el-form-item>
        <el-form-item label="课程id" prop="kcid">
          <el-input v-model="form.kcid" placeholder="请输入课程id" />
        </el-form-item>
        <el-form-item label="考试id" prop="ksid">
          <el-input v-model="form.ksid" placeholder="请输入考试id" />
        </el-form-item>
        <el-form-item label="学员id" prop="xyid">
          <el-input v-model="form.xyid" placeholder="请输入学员id" />
        </el-form-item>
        <el-form-item label="题干" prop="tg">
          <el-input v-model="form.tg" placeholder="请输入题干" />
        </el-form-item>
        <el-form-item label="选项 ：仅单选、复选时有值。可以存json" prop="xx">
          <el-input v-model="form.xx" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="题目类型">
          <el-checkbox-group v-model="form.stlx">
            <el-checkbox
              v-for="dict in stlxOptions"
              :key="dict.dictValue"
              :label="dict.dictValue">
              {{dict.dictLabel}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="正确答案" prop="da">
          <el-input v-model="form.da" placeholder="请输入正确答案" />
        </el-form-item>
        <el-form-item label="学员答案" prop="answered">
          <el-input v-model="form.answered" placeholder="请输入学员答案" />
        </el-form-item>
        <el-form-item label="题目解析" prop="jx">
          <el-input v-model="form.jx" placeholder="请输入题目解析" />
        </el-form-item>
        <el-form-item label="错题类型；1-考试，2-练习">
          <el-checkbox-group v-model="form.ctlx">
            <el-checkbox>请选择字典生成</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="备注" prop="remake">
          <el-input v-model="form.remake" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
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
import {
  listErrorbook,
  getErrorbook,
  delErrorbook,
  addErrorbook,
  updateErrorbook,
  exportErrorbook,
} from "@/api/etp/errorbook/errorbook";
import DelDialog from "@/components/DelDialog";

export default {
  name: "Errorbook",
  components: {
    DelDialog,
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
      // 错题集表格数据
      errorbookList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 题目类型字典
      stlxOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tg: null,
        stlx: null,
        ctlx: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      //删除参数
      delVisible: false,
      delInfo: {},
      delInfoIds: null,
    };
  },
  created() {
    this.getList();
    this.getDicts("subject_lx").then(response => {
      this.stlxOptions = response.data;
    });
  },
  methods: {
    /** 查询错题集列表 */
    getList() {
      this.loading = true;
      listErrorbook(this.queryParams).then(response => {
        // debugger
        let lists = response.rows;
        for (let i = 0; i < lists.length; i++) {
          lists[i].tg = lists[i].tg.replace(/<[^>]+>/g, "");
        }
        this.errorbookList = lists;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 题目类型字典翻译
    stlxFormat(row, column) {
      return this.selectDictLabels(this.stlxOptions, row.stlx);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        stid: null,
        kcid: null,
        ksid: null,
        xyid: null,
        tg: null,
        xx: null,
        stlx: [],
        da: null,
        answered: null,
        jx: null,
        ctlx: [],
        remake: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.stlx = null;
      this.queryParams.ctlx = null;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加错题集";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getErrorbook(id).then(response => {
        this.form = response.data;
        this.form.stlx = this.form.stlx.split(",");
        this.form.ctlx = this.form.ctlx.split(",");
        this.open = true;
        this.title = "修改错题集";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.stlx = this.form.stlx.join(",");
          this.form.ctlx = this.form.ctlx.join(",");
          if (this.form.id != null) {
            updateErrorbook(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addErrorbook(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if (!row.id) {
        this.delInfo = {};
      } else {
        let ctlx = "";
        if (row.ctlx == "1") ctlx = "考试";
        else ctlx = "练习";
        let info = { 题干: row.tg, 错题来源: ctlx };
        this.delInfo = info;
      }
      this.delVisible = true;
    },
    deleteInfo(ids) {
      delErrorbook(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有错题集数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.exportLoading = true;
          return exportErrorbook(queryParams);
        })
        .then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
