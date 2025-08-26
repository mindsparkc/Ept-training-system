<template>
  <div class="app-container">
    <el-form @submit.native.prevent :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="考试名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入考试名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考试开始~结束时间">
        <el-date-picker
          value-format="yyyy-MM-dd HH:mm:ss"
          v-model="dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间" @change="handleQuery">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <div style="display: flex">
          <template style="position: relative">
            <el-checkbox v-model="checkAll" @change="handleCheckAllChange" >全部数据</el-checkbox>
            <el-checkbox-group v-model="queryTag" ref="tag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
              <el-checkbox :label="item.id" v-for="item in tagOptions" name="tag" @change="handleQuery">{{item.name}}</el-checkbox>
            </el-checkbox-group>
            <div @click="openFop" v-if="tipFlag"  style="position: absolute;z-index: 9999;right: -5%;width: 100px;top:0;cursor: pointer;color: #1890ff">
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

    <el-table v-loading="loading" :data="examinationList">
      <el-table-column label="封面" align="center" prop="fm">
        <template  slot-scope="scope">
          <img :src="scope.row.fm"  width="90" height="70"  alt="X"/>
        </template>
      </el-table-column>
      <el-table-column label="考试名称" align="center" prop="mc" show-overflow-tooltip/>
<!--      <el-table-column label="考试描述" align="center" prop="ms" show-overflow-tooltip/>-->
      <el-table-column label="标签" align="center" prop="tags" show-overflow-tooltip >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null"
                  v-for="(item,index) in (scope.row.tags||'').split(',')"
                  :key="index"
                  style="margin-right: 3px;margin-bottom:8px">
            {{item}}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="答题次数" align="center" prop="dtcs" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.dtcs == '-99'">{{scope.row.yhdtcs}}/-</span>
          <span v-else>{{scope.row.yhdtcs}}/{{scope.row.dtcs}}</span>
        </template>
      </el-table-column>
      <el-table-column label="考试开始时间" align="center" prop="kskssj" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.kskssj }}</span>
        </template>
      </el-table-column>
      <el-table-column label="考试结束时间" align="center" prop="ksjssj" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.ksjssj }}</span>
        </template>
      </el-table-column>
      <el-table-column label="答卷时长" align="center" prop="djsc" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{scope.row.djsc}}分钟</span>
        </template>
      </el-table-column>
      <el-table-column label="总分数" align="center" prop="zfs" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.zfs ? scope.row.zfs : '——'}} 分</span>
        </template>
      </el-table-column>
      <el-table-column label="考试状态" align="center" prop="ksjd" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-tag v-if="scope.row.ksjd =='进行中'" type="success">{{scope.row.ksjd}}</el-tag>
          <el-tag v-else-if="scope.row.ksjd =='已结束'" type="info">{{scope.row.ksjd}}</el-tag>
          <el-tag v-else>{{scope.row.ksjd}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.ksjd!='已结束'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleToExam(scope.row)"
          >{{ scope.row.isSubmit=='0'?'继续考试':'考试' }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-tickets"
            @click="showInfo(scope.row)"
          >详情</el-button>
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


    <el-dialog title="查看成绩" :visible.sync="dialogTableVisible">
      <el-table :data="gridData" style="margin-bottom: 30px;">
        <el-table-column property="createTime" align="center" label="考试时间" sortable />
        <el-table-column property="score" align="center" label="成绩" sortable>
          <template slot-scope="scope">
            {{scope.row.score?scope.row.score:0}}分
          </template>
        </el-table-column>
        <el-table-column property="zfs" align="center" label="总分数" sortable>
          <template slot-scope="scope">
            {{scope.row.zfs?scope.row.zfs:'——'}}分
          </template>
        </el-table-column>
<!--        <el-table-column property="rate" align="center" label="正确率" sortable :formatter="rateFormate"/>-->
      </el-table>
      <pagination
        :autoScroll="false"
        v-show="gridData.length > 0"
        :limit.sync="gridForm.pageSize"
        :page.sync="gridForm.pageNum"
        :total="gridData2.length"
        @pagination="getGridData"
      />
    </el-dialog>
    <el-dialog :visible="djsTableVisible" width="700px" center
               :show-close="false"
               :close-on-click-modal="true">
      <div class="djs-div">
        <div class="djs-ms"> <span>距离开始考试还剩:</span></div>
        <div class="djs-time"><span v-if="day!='0'">{{day}}天</span><span>{{hr}}时{{min}}分{{sec}}秒</span></div>
        <div class="djs-button"><el-button @click="djsTableVisible = false" type="primary">关闭</el-button></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getHisInfo, listExam} from "@/api/etp/exam/exam";
import {listTagRelate} from "@/api/etp/tag/tag";

export default {
  name: "Examination",
  data() {
    return {
      nowTime: '',
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
        ksjd: null,
      },
      dateRange:[],
      gridForm:{
        pageNum: 1,
        pageSize: 10,
      },
      gridData:[],   // 页面使用的数据
      gridData2:[],  // 总数据
      dialogTableVisible:false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      //倒计时参数
      djsTableVisible:false,
      day:0,
      hr:0,
      min:0,
      sec:0,

      // 标签用到的
      tagValue: [],
      tagOptions:[],
      queryTag:[],
      isIndeterminate: true,
      checkAll: true,
      openFlag:true,
      tipFlag:false,
      height:'',

      djsFlag:false,
    };
  },
  created() {
    let jd = this.$route.query.ksjd;
    if(jd!=undefined){
      this.queryParams.ksjd = jd
    }
    this.getList();
    this.getTags();
  },
  methods: {

    handleCheckAllChange(val) {
      this.queryTag = []
      this.handleQuery()
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
    //倒计时方法
    countdown(row){
      const endTime = Date.parse(row.kskssj);
      const nowTime = Date.parse(new Date());
      const msec = endTime-nowTime;

      let day = parseInt(msec / 1000 / 60 / 60 / 24);
      let hr = parseInt(msec /1000 / 60 / 60 % 24);
      let min = parseInt(msec /1000 / 60 % 60);
      let sec = parseInt(msec /1000 % 60);
      this.day = day;
      this.hr = hr>9 ? hr:'0'+hr;
      this.min = min>9 ? min:'0'+min;
      this.sec = sec>9 ? sec:'0'+sec;
      const that = this;
      if(hr==0 & min==0 & sec==0){
        this.handleToExam(row);
      }else{
        if(this.djsTableVisible){
          setTimeout(function (){
            that.countdown(row);
          },1000)
        }
      }
    },



    getGridData(){
      let startIndex = (this.gridForm.pageNum - 1) * this.gridForm.pageSize;
      let endIndex =
        (this.gridForm.pageSize * this.gridForm.pageNum < this.gridData2.length)
          ? this.gridForm.pageSize * this.gridForm.pageNum:this.gridData2.length;
      this.gridData = [];
      for (let i = startIndex; i < endIndex; i++)
        this.gridData.push(this.gridData2[i]);
    },

    rateFormate(row){
      // return  (row.rate * 100).toFixed(0) + "%"
      // parseFloat(row.rate) * 100 + '%'
      // (Number(row.rate).toFixed(2))

    },

    /** 查看成绩*/
    showInfo(row){
      getHisInfo(row.id).then(res => {
        this.dialogTableVisible = true;
        console.log(res);
        this.gridData = [];
        this.gridData2 = res.data.tTestpaperStudent;
        for (let i = 0; i < (this.gridForm.pageSize > this.gridData2.length
          ? this.gridData2.length:this.gridForm.pageSize); i++) {
          this.gridData.push(this.gridData2[i]);
          this.gridData[i]['zfs'] = res.data.zfs;
        }
      })
    },

    /** 查看详情 */
    handleToExam(row) {
      const kssj = Date.parse(row.kskssj);
      const now = Date.parse(new Date());
      if(kssj>now){
        this.djsTableVisible = true;

        this.countdown(row);
      }else if(row.dtcs!='-99'&&row.dtcs<=row.yhdtcs){
        this.$message.error('您的考试次数已用完！')
      }else{
        this.djsTableVisible = false;
        let router = this.$router.resolve({
          path:"/examManagement/detail/" + row.id,
        })
        window.open(router.href,'_blank')
        // this.$router.push("/examManagement/detail/" + row.id);
      }
    },
    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExam(this.queryParams).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if(list[i].fm == null || list[i].fm == '' || list[i].fm == undefined){
            console.log(list[i].fm)
            let fm = require("@/assets/images/examine_cover.png");
            list[i].fm = fm;
          }
        }
        this.examinationList = list;
        console.log(this.examinationList);
        this.total = response.total;
        this.loading = false;
      });
    },
    reset() {
      this.form = {
        id: null,
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
      let date = this.dateRange;
      this.queryParams.kskssj = date[0];
      this.queryParams.ksjssj = date[1];
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = [];
      this.dateRange = [];
      this.queryParams.ksjd = null
      this.handleQuery();
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "KS" }).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    }
  }
};
</script>
<style scoped lang="scss">
  .djs-div{
    height: 200px;
    width: 100%;
    .djs-ms{
      font-size: 40px;
      flood-color: #121315;
      text-align: center;
      padding: 20px;
      font-weight: bold;
    }
    .djs-time{
      font-size: 30px;
      flood-color: #121315;
      text-align: center;
      padding: 10px;
    }
    .djs-button{
     position: absolute;
      right: 40px;
      bottom: 20px;
    }
  }
  .el-checkbox-group {
    margin-left: 30px;
  }

</style>
