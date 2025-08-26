<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="90px">
      <el-form-item label="考试名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          clearable
          placeholder="请输入考试名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布状态" prop="fb">
        <el-select v-model="queryParams.fb" clearable placeholder="请选择发布状态" size="small">
          <el-option
            v-for="dict in fbOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开放程度" prop="kfcd">
        <el-select v-model="queryParams.kfcd" clearable placeholder="请选择开放程度" size="small">
          <el-option
            v-for="dict in kfcdOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item class="tags-div" label="标签" prop="queryTag" style="width: calc(100% - 256px);">
        <template style="position: relative">
          <el-checkbox-group ref="tag" v-model="queryTag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
          </el-checkbox-group>
          <div @click="openFop" v-if="tipFlag"  style="position: absolute;right: -3%;width: 50px;top:0;cursor: pointer;color: #1890ff">
            {{ openFlag?'展开':'收起' }}
          </div>
        </template>
      </el-form-item>

      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="examinationList"
               @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column :show-overflow-tooltip="true"  width="100" align="center" label="考试编号" prop="id" sortable/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="考试名称" prop="mc" />
      <el-table-column align="center" label="封面" prop="fm" >
        <template slot-scope="scope">
          <el-image
            :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
            fit="cover"
            style="width: 80px; height: 80px;"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="标签" prop="tags" >
        <template slot-scope="scope">
          <span v-for="(item,index) in (scope.row.tags||'').split(',').slice(0,3)">
             <el-tag v-if="item!=''" style="margin-right: 3px;margin-bottom:2px">
               {{item}}
             </el-tag>
          </span>
          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null && (scope.row.tags||'').split(',').length > 3"
                  style="margin-right: 3px;margin-bottom:8px">
            ...
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :formatter="fbFormat" align="center" label="发布状态" prop="fb" >
        <template slot-scope="scope">
          <el-tag :type="scope.row.fb == '0' ? 'info' : 'success'">{{fbFormat(scope.row)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :formatter="kfcdFormat" align="center" label="开放程度" prop="kfcd" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.kfcd == '0'">完全公开</el-tag>
          <el-tag v-if="scope.row.kfcd == '1'" type="warning">限定用户</el-tag>
          <el-tag v-if="scope.row.kfcd == '2'" type="danger">不公开</el-tag>
        </template>
<!--        <template slot-scope="scope">
          <el-tag :type="scope.row.kfcd == '0' ? 'primary' : 'warning'">{{kfcdFormat(scope.row)}}</el-tag>
        </template>-->
      </el-table-column>
      <el-table-column align="center" label="考试开始时间" prop="kskssj">
        <template slot-scope="scope">
          <span>{{ scope.row.kskssj }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="考试结束时间" prop="ksjssj">
        <template slot-scope="scope">
          <span>{{ scope.row.ksjssj }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="已判卷/已考试" prop="kszt"/>
<!--      <el-table-column :show-overflow-tooltip="true" align="center" label="考试描述" prop="ms"/>-->
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.kszt!=='0/0'"
            icon="el-icon-data-analysis"
            size="mini"
            type="text"
            @click="showExamAnalysis(scope.row)"
          >考试分析</el-button>
          <el-button
            v-else
            icon="el-icon-data-analysis"
            size="mini"
            type="text"
            @click="showExamAnalysis(scope.row)"
            disabled
          >考试分析</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listExamination, getMissExam, delExamination,updateFb, addExamination, updateExamination, updateTExaminationFb,getExaminationInfo } from "@/api/etp/examination/examination";
import { listTagRelate } from "@/api/etp/tag/tag";
import DelDialog from "@/components/DelDialog"
import moment from 'moment'

export default {
  name: "Examination",
  components: {
    DelDialog
  },
  data() {
    return {
      gridForm:{
        pageNum: 1,
        pageSize: 10,
      },
      gridData:[],   // 页面使用的数据
      gridData2:[],  // 总数据
      gridDataQk:[],
      gridDataQk2:[],
      gridForm2:{
        pageNum: 1,
        pageSize: 10,
      },
      dialogTableVisible:false,
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
      // 考试表格数据
      examinationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 发布状态：0-暂存，1-发布字典
      fbOptions: [],
      // 开放程度：完全公开-0，限定用户-1字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fm: null,
        fb: null,
        kslj: null,
        kfcd: null,
        sjid: null,
        kskssj: null,
        ksjssj: null,
        djsc: null,
        dtcs: null,
        fsdj: null,
        zfs: null,
      },
      // 表单参数
      form: {},
      tagOptions:[],
      queryTag:[],

      height:'',
      openFlag:true,
      tipFlag:false,

      isIndeterminate: true,
      checkAll: false,
      xqId: '',//存储考试id，用于查询学员考试信息及缺考学员信息
      pjzt: '',
      //tabs标签渲染参数
      activeName: 'first',
      First: true,
      Second: false,

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
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
  },
  created() {
    this.getList();
    this.getDicts("examination_fb").then(response => {
      this.fbOptions = response.data;
    });
    this.getDicts("examination_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
    this.getTags()
  },
  methods: {
    //跳转至考试分析
    showExamAnalysis(row){
      this.$router.push({
      path:"/etp/examinationScoreAnalysis/AnalysisInfo/",
        query:{ id:row.id,sjid:row.sjid }
      });
    },


    //tabs标签渲染方法
    handleClick(tab){
      if(tab != undefined && tab.name==='first'){
        this.First=true;
        this.Second=false;
      }else{
        this.First=false;
        this.Second=true;
        this.getMissExamByStu();
      }
    },


    handleCheckAllChange(val) {
      if (val){
        this.tagOptions.forEach(item => {
          this.queryTag.push(item.id)
        })
      }else{
        this.queryTag = []
      }
      this.isIndeterminate = false;
    },

    handleCheckedTagsChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.tagOptions.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },

    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExamination(this.queryParams).then(response => {
        this.examinationList = response.rows;
        console.log(response);
        this.total = response.total;
        this.loading = false;
      });
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

    getTags(){
      listTagRelate({modelType:"KS"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
    // 发布状态：0-暂存，1-发布字典翻译
    fbFormat(row, column) {
      return this.selectDictLabel(this.fbOptions, row.fb);
    },
    // 开放程度：完全公开-0，限定用户-1字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
    },


    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = []
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },


    /** 查看成绩*/
    handleScore(row){
      this.xqId = row.id;
      this.getInfo(this.pjzt);
    },
    //获取学员考试信息
    getInfo(val){
      getExaminationInfo(this.xqId,val).then(res => {
        this.dialogTableVisible = true;
        this.gridData = [];
        this.gridData2 = res.data.examinationInfoWithCps;
        for(let i =0;i<this.gridData2.length;i++){
          let time = this.gridData2[i].handTime
          let handtime = this.$moment(time).format('YYYY-MM-DD HH:mm:ss');
          this.gridData2[i].handTime = handtime;
        }
        for (let i = 0; i < (this.gridForm.pageSize > this.gridData2.length
                              ? this.gridData2.length:this.gridForm.pageSize); i++) {
          this.gridData.push(this.gridData2[i]);
        }
      })
    },

    rateFormate(row){
      return  (row.rate * 100).toFixed(0) + "%"
      // parseFloat(row.rate) * 100 + "%"
      // (Number(row.rate).toFixed(2))
    },

  }
};
</script>
<style lang="scss" scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
</style>
