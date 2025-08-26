<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入任务名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务描述" prop="ms">
        <el-input
          v-model="queryParams.ms"
          placeholder="请输入任务描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <div style="display: flex">
          <template style="position: relative">
          <el-checkbox  v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>
          <el-checkbox-group ref="tag" v-model="queryTag" :style="{height: height,overflow: 'hidden'}"  @change="handleCheckedTagsChange">
            <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
          </el-checkbox-group>
          <div @click="openFop" v-if="tipFlag"  style="position: absolute;right: -3%;width: 50px;top:0;cursor: pointer;color: #1890ff">
            {{ openFlag?'展开':'收起' }}
          </div>
        </template>
        </div>
      </el-form-item>
<!--
      <el-form-item label="任务开始时间" prop="taskBeginTime">
        <el-date-picker clearable size="small"
                        v-model="queryParams.taskBeginTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择任务开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="任务结束时间" prop="taskEndTime">
        <el-date-picker clearable size="small"
                        v-model="queryParams.taskEndTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择任务结束时间">
        </el-date-picker>
      </el-form-item>-->
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
          v-hasPermi="['etp:task:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['etp:task:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 展现时需要体现：序号、标签、任务名称、任务描述、封面图和开放程度（公开、指定用户、不公开）、更新时间。   -->
    <el-table v-loading="loading" :data="taskList"
               @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center" />
      <el-table-column label="封面图" align="center" prop="fmt" width="100">
        <template slot-scope="scope">
          <div class="fmt-item">
            <el-image
              style="width: 80px; height: 60px;border-radius: 5px"
              :src="scope.row.fmt==null?require('@/assets/images/task_cover.png'):scope.row.fmt"
              fit="cover"
              />
            <div :class="['tag-cir',{'fb':scope.row.states == '99'}]">
              <div>{{ scope.row.states == '0' ? '暂存' : '已发布'}}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="left" prop="mc" />
      <el-table-column label="任务描述" align="left" prop="ms" :show-overflow-tooltip="true">
        <template  slot-scope="scope">
          {{ scope.row.ms }}
        </template>
      </el-table-column>
      <el-table-column label="开放程度" align="center" prop="kfcd" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.kfcd == '0'">完全公开</el-tag>
          <el-tag v-if="scope.row.kfcd == '1'" type="warning">限定用户</el-tag>
          <el-tag v-if="scope.row.kfcd == '2'" type="danger">不公开</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tags" width="120" >
        <template slot-scope="scope">
          <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
        </template>
      </el-table-column>
      <el-table-column label="任务开始时间" align="center" prop="taskBeginTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskBeginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务结束时间" align="center" prop="taskEndTime" width="110">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!-- el-table-column label="任务推送时间" align="center" prop="taskSendTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskSendTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column -->

<!--      <el-table-column label="状态" align="center" prop="states">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag :type="scope.row.states == '0'?'info':'success'">{{ scope.row.states == '0' ? '暂存' : '已发布' }}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="left" class-name="small-padding fixed-width" width="120px" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              type="text"
              icon="el-icon-view"
              @click="goDetail(scope.row)"
            >
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="取消发布" placement="top">
            <el-button
              type="text"
              icon="el-icon-s-promotion"
              @click="pushBack(scope.row)"
              v-hasPermi="['etp:curriculum:edit']"
              v-if="scope.row.states != '0'"
            >
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['etp:task:edit']"
              v-if="scope.row.states == '0'"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['etp:task:remove']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="二维码" placement="top">
            <el-button
              v-if="scope.row.states == '99'"
              type="text"
              @click="createQrcg(scope.row)"
              v-hasPermi="['etp:task:remove']"
            ><i class="iconfont icon-ico"></i></el-button>
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

<!--    删除-->
    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>

    <el-dialog title="生成二维码"
               :visible.sync="dialogVisibleQrcg"
               width="70%"
               :before-close="closeQrcg"
               :close-on-click-modal="false">
      <div style="height: 550px">
        <Qrcg :data="qrcgForm" @closeQrcg="closeQrcg"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTask, getTask, delTask, addTask, updateTask, exportTask, pushBack} from "@/api/etp/task/index";
import {listTagRelate} from "@/api/etp/tag/tag";
import DelDialog from "@/components/DelDialog"
import Qrcg from "@/views/etp/qrcg";
import MoreTagShow from "@/components/MoreTagShow"

export default {
  name: "Task",
  components: {
    DelDialog,
    Qrcg,
    MoreTagShow
  },
  data() {
    return {
      zkFlag:false,

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
      // 任务表格数据
      taskList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        taskBeginTime: null,
        taskEndTime: null,
        tags:[]
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 标签用到的
      tagValue: [],
      tagOptions:[],
      queryTag:[],

      height:'',
      openFlag:true,
      tipFlag:false,

      checkAll: true,

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,

      //二维码参数
      qrcgUrl:'',
      qrcgModel:'',
      dialogVisibleQrcg:false,
      qrcgForm:{
        title:'',
        value:'',
        size:'',
        startTime:'',
        endTime:''
      },
    };
  },
  created() {
    this.getList();
    this.getTags();
    this.getConfigKey("cp.qrcg.task").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
  },
  activated() {
    this.getList();
    this.getTags();
    this.getConfigKey("cp.qrcg.task").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
  },
  watch:{
    queryTag:{
      handler(){
        if (null != this.queryTag) {
          this.queryParams.tags = this.queryTag.join(',')
        }
        this.getList();
      },
      deep:true
    },
    queryParams:{
      handler(){
        this.getList();
      },
      deep:true
    },
  },
  methods: {

    //关闭二维码弹窗
    closeQrcg(){
      this.dialogVisibleQrcg = false;
    },
    //生成二维码
    createQrcg(row) {
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = row.mc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=row.taskBeginTime;
      this.qrcgForm.endTime=row.taskEndTime;
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl+this.qrcgModel+row.id+'&jd';
    },

    //跳转任务预览页面
    goDetail(row){
      // this.$router.push({
      //   path: "/task/previewTask/",
      //   query: {rwid: row.id}
      // });
      window.open('/student/taskManagement/detail?rwid='+ row.id +'&requestFrom='+"admin"  )
    },

    handleCheckAllChange(val) {
      this.queryTag = []
    },

    handleCheckedTagsChange(value) {
      this.checkAll = false
      // let checkedCount = value.length;
      // this.checkAll = checkedCount === this.tagOptions.length;
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },
    openFop(){
      this.openFlag = !this.openFlag;
      if (!this.openFlag){ // 展开
        this.height = 'auto'
      }else {
        this.height = 45 + 'px'
      }
      // this.$forceUpdate()
    },
    /** 标签相关 */
    getTags() {
      listTagRelate({modelType: "RW"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
    /** 查询任务列表 */
    getList() {
      this.loading = true;
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows;
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
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        taskBeginTime: null,
        taskEndTime: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(',')
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path:"/etp/task/addOrUpdateTask/",query:{t:Date.now()}});
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path:"/etp/task/addOrUpdateTask/" + row.id,
        query:{t:Date.now()}
      });
    },
    /** 任务退回*/
    pushBack(row){
      let id = row.id;
      this.$confirm(`是否取消发布任务【${row.mc}】?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        pushBack(id).then(res => {
          this.$message.success("任务撤销成功!");
          this.getList();
        })
      }).catch();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTask(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTask(this.form).then(response => {
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
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"任务名称":row.mc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delTask(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },

  }
};
</script>
<style  scoped lang="scss">
.desc {
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

.fmt-item{
  position: relative;
}
.tag-cir {
  width: 44px;
  height: 23px;
  background-color: #f1580b;
  color: #ffffff;
  position: absolute;
  top: 4px;
  border-radius: 3px 4px 4px 0px;
  font-size: 8px;
  left: -1px;
}
.fb{
  background-color: #0b830a!important;
}

.tag-cir::before{
  content: '';
  position: absolute;
  width: 4px;
  height: 4px;
  left: 0;
  bottom: -3px;
  background: #f1580b;
  clip-path: polygon(0 0, 100% 0, 100% 100%); /*三角*/
}
.fb::before{
  background-color: #0b830a!important;
}
</style>
