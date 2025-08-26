<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="false" v-show="showSearch" label-width="90px">
      <el-form-item label="问卷名称" prop="wjmc">
        <el-input style="width: 30%;"
          v-model="queryParams.wjmc"
          placeholder="请输入问卷名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
                  maxlength="100"
                  show-word-limit
        />
      </el-form-item>

      <el-form-item label="启用状态" prop="qyzt">
        <el-radio-group v-model="queryParams.qyzt">
          <el-radio v-for="dict in qyztOptions"
                    :key="dict.dictValue"
                    :label="dict.dictValue"
          >{{ dict.dictLabel }}</el-radio>
        </el-radio-group>

<!--        <el-select v-model="queryParams.qyzt" placeholder="请选择启用状态" clearable size="small">
          <el-option
            v-for="dict in qyztOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>-->
      </el-form-item>

      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
       <div style="display:flex;">
         <template style="position: relative">
           <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>
           <el-checkbox-group ref="tag" v-model="queryTag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
             <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
           </el-checkbox-group>
           <div @click="openFop" v-if="tipFlag"  style="position: absolute;right: -3%;width: 50px;top:0;cursor: pointer;color: #1890ff">
             {{ openFlag?'展开':'收起' }}
           </div>
         </template>
       </div>
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
          v-hasPermi="['etp:questionnaire:add']"
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
          v-hasPermi="['etp:questionnaire:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="questionnaireList"
              :default-sort ="{prop:'id'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection"  width="55" align="center" />
      <el-table-column label="问卷名称" align="center" prop="wjmc" :show-overflow-tooltip="true"/>
      <el-table-column label="封面图" align="center" prop="fm" >
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
            fit="fit"/>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tags">
        <template slot-scope="scope">
          <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="qyzt" :formatter="qyztFormat" width="80px"/>
      <el-table-column label="发布状态" align="center" prop="fb" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fb == '0'" type="info">暂存</el-tag>
          <el-tag v-if="scope.row.fb !== '0'" type="success">已发布</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="beginTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开放程度" align="center" prop="kfcd" :formatter="kfcdFormat" />
      <el-table-column label="是否匿名" align="center" prop="sfnm" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sfnm == '0'" type="success">匿名</el-tag>
          <el-tag v-if="scope.row.sfnm !== '0'" type="warning">公开</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="问卷描述" align="center" prop="ms" :show-overflow-tooltip="true"/>-->
      <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              type="text"
              icon="el-icon-view"
              @click="preview(scope.row)"
            >
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="撤回" placement="top">
            <el-button
              v-if="scope.row.fb !== '0'"
              type="text"
              icon="el-icon-s-promotion"
              @click="handleBack(scope.row)"
              v-hasPermi="['etp:questionnaire:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="修改" placement="top">
            <el-button
              v-if="scope.row.fb == '0'"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['etp:questionnaire:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top">
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['etp:questionnaire:remove']"
            ></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="详情" placement="top">
            <el-button
              type="text"
              icon="el-icon-tickets"
              @click="toDetail(scope.row)"
            >
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="二维码" placement="top">
            <el-button
              type="text"
              @click="createQrcg(scope.row)"
            ><i class="iconfont icon-ico"></i>
            </el-button>
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

    <!--    删除弹窗-->
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
import { listQuestionnaire, getQuestionnaire, delQuestionnaire,
          addQuestionnaire, updateQuestionnaire,backQuestionnaire,
          exportQuestionnaire } from "@/api/etp/questionnaire";
import {listTagRelate} from "@/api/etp/tag/tag";
import DelDialog from "@/components/DelDialog"
import Qrcg from "@/views/etp/qrcg";
import MoreTagShow from "@/components/MoreTagShow"

export default {
  name: "Questionnaire",
  components:{DelDialog,Qrcg,MoreTagShow },
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
      // 活动报名表格数据
      questionnaireList: [],

      // 启用状态 0 正常 1 禁用字典
      qyztOptions: [],
      // 开放程度0公开，1指定用户，2不公开字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wjmc: null,
        ms: null,
        fb: null,
        nr: null,
        beginTime: null,
        endTime: null,
        kfcd: null,
        sfnm: null,
        tags: []
      },

      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],

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
  activated() {
    this.getList();
    this.getTags();
  },
  created() {
    this.getList();
    this.getDicts("question_status").then(response => {

      this.qyztOptions.push({dictLabel:"全部",dictValue:undefined})
      response.data.forEach(item => {
        this.qyztOptions.push(item)
      })

      // this.qyztOptions = response.data;
    });
    this.getDicts("question_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
    this.getTags();
    //获取二维码地址
    this.getConfigKey("cp.qrcg.question").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
  },
  methods: {

    //关闭二维码弹窗
    closeQrcg(){
      this.dialogVisibleQrcg = false;
    },
    //生成二维码
    createQrcg(row) {
      debugger
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = row.wjmc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=row.beginTime;
      this.qrcgForm.endTime=row.endTime;
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl+this.qrcgModel+row.id+'';
    },

    handleCheckAllChange(val) {
      this.queryTag = []
    },

    handleCheckedTagsChange(value) {
      // let checkedCount = value.length;
      this.checkAll = false
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
    preview(data){
      this.$router.push({
        path:"/etp/questionnaire/previewQuestionnaire/",
        query:{ id:data.id }
      });

    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "WJ"}).then(response => {
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
    // 标签相关结束

    /** 查询活动报名列表 */
    getList() {
      this.loading = true;
      listQuestionnaire(this.queryParams).then(response => {
        this.questionnaireList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 启用状态 0 正常 1 禁用字典翻译
    qyztFormat(row, column) {
      return this.selectDictLabel(this.qyztOptions, row.qyzt);
    },
    // 开放程度0公开，1指定用户，2不公开字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
    },

    // 表单重置
    reset() {
      this.form = {
        id: null,
        wjmc: null,
        qyzt: null,
        fm: null,
        ms: null,
        fb: null,
        nr: null,
        beginTime: null,
        endTime: null,
        kfcd: null,
        sfnm: null,
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
      this.$router.push({
        path:"/etp/questionnaire/addOrUpdateQuestionnaire/",
      });
     /* this.reset();
      this.open = true;
      this.title = "添加活动报名";*/
    },
    /** 撤回操作*/
    handleBack(row){
      console.log(row)
      this.$confirm('撤回此问卷调查, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        backQuestionnaire({id:row.id}).then(res => {
          this.$message(res.msg)
          this.getList();
        })
      }).catch(() => {});
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.$router.push({
        path:"/etp/questionnaire/addOrUpdateQuestionnaire/",
        query:{ id:id }
      });

    },

    /** 删除按钮操作 */
    // handleDelete(row) {
    //   const ids = row.id || this.ids;
    //   this.$confirm('是否确认删除活动报名编号为【' + ids + '】的数据项?', "警告", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   }).then(function() {
    //     return delQuestionnaire(ids);
    //   }).then(() => {
    //     this.getList();
    //     this.msgSuccess("删除成功");
    //   }).catch(() => {});
    // },
    // /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"问卷名称":row.wjmc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delQuestionnaire(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有活动报名数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.exportLoading = true;
        return exportQuestionnaire(queryParams);
      }).then(response => {
        this.download(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },

    /** 查看详情*/
    toDetail(row){
      let id = row.id
      this.$router.push({
        path: "/etp/questionnaire/components/detail",
        query: { id: id }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

</style>
