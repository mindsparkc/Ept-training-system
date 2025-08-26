<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="false" v-show="showSearch" label-width="68px">
      <el-form-item label="意见描述" prop="yjms">
        <el-input style="width: 30%;"
          v-model="queryParams.yjms"
          placeholder="请输入意见描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery" show-word-limit maxlength="100"
        />
      </el-form-item>
      <el-form-item label="回复内容" prop="hf">
        <el-input style="width: 30%;"
          v-model="queryParams.hf"
          placeholder="请输入回复内容"
          clearable
          size="small"
          @keyup.enter.native="handleQuery" show-word-limit maxlength="300"
        />
      </el-form-item>
      <el-form-item label="回复状态" prop="hfStatus">
        <el-radio-group v-model="queryParams.hfStatus">
          <el-radio v-for="dict in hfStatusOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>

<!--        <el-select v-model="queryParams.hfStatus" placeholder="请选择回复状态" clearable size="small">
          <el-option
            v-for="dict in hfStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>-->
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="opinionList"
               @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="活动编号" :show-overflow-tooltip="true"  width="100" align="center" prop="id" sortable/>-->
      <el-table-column label="意见描述" align="left" :show-overflow-tooltip="true"  width="380" prop="yjms" />
      <el-table-column label="回复内容" align="left" :show-overflow-tooltip="true"  width="180" prop="hf" />
      <el-table-column label="学员姓名" align="center" prop="xyname" />
      <el-table-column label="回复状态" align="center" prop="hfStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.hfStatus == '0'" type="info">未回复</el-tag>
          <el-tag v-if="scope.row.hfStatus !== '0'" type="success">已回复</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="回复" placement="top">
            <el-button
              type="text"
              icon="el-icon-chat-line-square"
              @click="preview(scope.row)"
            >
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
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


    <!-- 添加或修改意见反馈对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="800px" append-to-body>
      <div style="margin: 5px 20px 20px 20px">
        <p style="font-size: 16px"><span style="font-weight: bold">学员姓名：</span>{{ opinionForm.xyname }}</p>
        <p style="font-size: 16px"><span style="font-weight: bold">提问时间：</span>{{ opinionForm.createTime }}</p>
        <p style="font-size: 16px"><span style="font-weight: bold">意见描述：</span>{{ opinionForm.yjms }}</p>
        <div style="font-size: 16px">
          <span style="font-weight: bold">附件：<br></span>
          <template v-for="(item,index) in opinionForm.fj" v-if="opinionForm.fj">
            <el-link :href="item.url" :underline="true" target="_blank">
              <span style="margin-left: 10px">
                {{ index+1 }}><span style="margin: 8px" class="el-icon-document"> {{ item.name }} </span>
              </span>
            </el-link>
            <br />
          </template>
<!--          <template v-if="opinionForm.fj" v-for="item in opinionForm.fj" style="margin-left: 15px">
            <el-image style="width: 100px; height: 100px;border-radius: 10px;margin: 10px"
                      :src="item.url"
                      fit="cover" />
          </template>-->
        </div>
        <div>
          <p style="font-size: 16px"><span style="font-weight: bold">意见回复：</span></p>
          <el-input
            type="textarea"
            :rows="5"
            placeholder="请输入回复内容"
            v-model="opinionForm.hf" show-word-limit maxlength="300">
          </el-input>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">回 复</el-button>
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
import { listOpinion, getOpinion, delOpinion, addOpinion, updateOpinion, exportOpinion }
  from "@/api/etp/opinion";
import DelDialog from "@/components/DelDialog"
export default {
  name: "Opinion",
  components: {DelDialog},
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
      // 意见反馈表格数据
      opinionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 回复状态 0 未回复 1已回复字典
      hfStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        yjms: null,
        hf: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },

      opinionForm:{},

      delVisible:false,
      delInfo:{},
      delInfoIds:null
    };
  },
  watch:{
    queryParams:{
      handler(){
        this.getList();
      },
      deep:true
    },
  },
  created() {
    this.getList();
    this.getDicts("opinion_status").then(response => {
      this.hfStatusOptions.push({dictLabel:"全部",dictValue:undefined})
      response.data.forEach(item => {
        this.hfStatusOptions.push(item)
      })


    });
  },
  methods: {
    preview(row){
      this.title = "查看意见"
      this.opinionForm = null;
      this.open = true;
      this.opinionForm = row
      this.opinionForm.fj = JSON.parse(row.fj);

    },
    /** 查询意见反馈列表 */
    getList() {
      this.loading = true;
      listOpinion(this.queryParams).then(response => {
        this.opinionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        yjms: null,
        fj: null,
        hf: null,
        xyid: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        hfStatus: null
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
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    /** 提交按钮 */
    submitForm() {
      this.open = false;
      this.opinionForm.hfStatus = '1';
      this.opinionForm.fj = JSON.stringify(this.opinionForm.fj)
      updateOpinion(this.opinionForm).then(response => {
        this.msgSuccess("回复成功");
        this.open = false;
        this.getList();
      });
      /*this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOpinion(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOpinion(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });*/
    },
    /** 删除按钮操作 */
    // handleDelete(row) {
    //   const ids = row.id || this.ids;
    //   this.$confirm('是否确认删除意见反馈编号为【' + ids + '】的数据项?', "警告", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   }).then(function() {
    //     return delOpinion(ids);
    //   }).then(() => {
    //     this.getList();
    //     this.msgSuccess("删除成功");
    //   }).catch(() => {});
    // },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"意见描述":row.yjms,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delOpinion(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有意见反馈数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.exportLoading = true;
        return exportOpinion(queryParams);
      }).then(response => {
        this.download(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
