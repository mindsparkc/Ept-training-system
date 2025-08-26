<template>
  <div class="app-container" style="overflow-x: hidden">
    <div class="title-text">我的任务</div>

    <el-tabs v-model="activeName" @tab-click="handleClick" :tabBarStyle="{ borderBottom: 'unset' }"style="margin-top: 28px">
      <el-tab-pane label="全部" name="1"></el-tab-pane>
      <el-tab-pane label="已完成" name="2"></el-tab-pane>
      <el-tab-pane label="进行中" name="3"></el-tab-pane>
      <el-tab-pane label="将过期" name="4"></el-tab-pane>
    </el-tabs>

    <el-row v-for="(item,index) in tableData" style="border-bottom: 1px solid #EEEEEE">
      <el-col style="margin-top: 17px;margin-bottom: 17px" :span="4">
        <el-image style="width: 180px; height: 90px;border-radius: 8px" :src="item.fmt" :fit="fit"/>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col>
            <span class="col-top">
              {{item.mc}}
            </span>
          </el-col>
        </el-row>
        <el-row style="">
          <el-col>
            <span v-if="item.ms!=null" class="col-center">
              {{item.ms}}
            </span>
            <span v-else style="color: white">
              {{0}}
            </span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
<!--          <el-col :span="3" class="col-bottom">-->
<!--            <span>-->
<!--              {{item.createBy}}-->
<!--            </span>-->
<!--          </el-col>-->
          <el-col :span="20" class="col-bottom">
            <span>任务开始~结束时间：{{item.taskBeginTime}}~{{item.taskEndTime}}</span>
          </el-col>
          <el-col :span="4">
            <el-button v-if="item.getStatus!='0'" size="medium" type="primary" @click="handleDetail(item)">执行任务</el-button>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :offset="10">
        <p v-if="loading" style="color: #999999">下拉加载更多数据</p>
        <p v-if="noMore" style="color: #999999">暂无更多数据</p>
      </el-col>
    </el-row>

<!--    <el-form @submit.native.prevent :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">-->
<!--      <el-form-item label="任务名称" prop="mc">-->
<!--        <el-input-->
<!--          v-model="queryParams.mc"-->
<!--          placeholder="请输入任务名称"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">-->
<!--        <div style="display: flex">-->
<!--          <template style="position: relative">-->
<!--            <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>-->
<!--            <el-checkbox-group v-model="queryTag" ref="tag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">-->
<!--              <el-checkbox :label="item.id" v-for="item in tagOptions" name="tag" @change="handleQuery">{{item.name}}</el-checkbox>-->
<!--            </el-checkbox-group>-->
<!--            <div @click="openFop" v-if="tipFlag"  style="position: absolute;z-index: 9999;right: -5%;width: 100px;top:0;cursor: pointer;color: #1890ff">-->
<!--              {{ openFlag?'展开':'收起' }}-->
<!--            </div>-->
<!--          </template>-->
<!--        </div>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->




<!--    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">-->
<!--      <el-table-column label="封面图" align="center" prop="fmt" >-->
<!--        <template   slot-scope="scope">-->
<!--          <img :src="scope.row.fmt"  width="90" height="70"  alt="X"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="任务名称" align="left" prop="mc" show-overflow-tooltip />-->

<!--      <el-table-column label="标签" align="center" prop="tags" show-overflow-tooltip >-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null"-->
<!--                  v-for="(item,index) in (scope.row.tags||'').split(',')"-->
<!--                  :key="index"-->
<!--                  style="margin-right: 3px;margin-bottom:8px">-->
<!--            {{item}}-->
<!--          </el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="任务开始时间" align="center" prop="taskBeginTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{scope.row.taskBeginTime}}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="任务结束时间" align="center" prop="taskEndTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.taskEndTime}}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="状态" align="center" prop="rwjd" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.rwjd === '未开始'">{{scope.row.rwjd}}</el-tag>-->
<!--          <el-tag v-if="scope.row.rwjd === '进行中'" type="warning">{{scope.row.rwjd}}</el-tag>-->
<!--          <el-tag v-if="scope.row.rwjd === '已完成'" type="success">{{scope.row.rwjd}}</el-tag>-->
<!--          <el-tag v-if="scope.row.rwjd === '已结束'" type="info">{{scope.row.rwjd}}</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="进度" align="center" prop="jd" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span v-if="scope.row.jd==null"> 0%</span>-->
<!--          <span v-else>{{JSON.parse(scope.row.jd).rate==null?0:JSON.parse(scope.row.jd).rate}}%</span>-->

<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-reading"-->
<!--            @click="handleDetail(scope.row)"-->
<!--          >执行</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->

<!--    <pagination-->
<!--      v-show="total>0"-->
<!--      :total="total"-->
<!--      :page.sync="queryParams.pageNum"-->
<!--      :limit.sync="queryParams.pageSize"-->
<!--      @pagination="getList"-->
<!--    />-->
  </div>
</template>

<script>
import {getTaskInfoDetail, listTask} from "@/api/etp/task/task";
import {listTagRelate} from "@/api/etp/tag/tag";
import {clickSendRedis} from "@/api/buryPoint";

export default {
  name: "Task",
  data() {
    return {
      // 遮罩层
      loading: true,
      noMore: false,
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
        jd:null,
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
      isIndeterminate: true,
      checkAll: true,
      openFlag:true,
      tipFlag:false,
      height:'',
      //-----------------------
      activeName:'1',
      tableData:[],
    };
  },
  created() {
    let jd = this.$route.query.jd;
    if(jd!=undefined){
      this.queryParams.jd = jd
    }
    this.getList();
    this.getTags();
  },
  activated() {
    this.getList();
    this.getTags();
  },

  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },

  methods: {

    handleScroll(){

      var scrollTop = Math.floor(document.body.scrollTop || document.documentElement.scrollTop || window.pageYOffset)
      if(this.lastScrollTop == scrollTop){
        return
      }
      // console.log( '获取距离顶部的距离' + scrollTop )
      var clientHeight = Math.floor(document.body.clientHeight || document.documentElement.clientHeight)
      // console.log( '获取可视区的高度' + clientHeight )
      var scrollHeight = Math.floor(document.body.scrollHeight || document.documentElement.scrollHeight)
      // console.log( '获取滚动条的总高度' + scrollHeight )

      if(scrollTop + clientHeight >= scrollHeight){
        if(this.tableData.length>=this.total){
          this.noMore = true;
          this.loading = false;
        }else{
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum += 1;
          this.getList();
        }
      }
    },

    handleClick(tab, event) {
      this.tableData = []
      if(tab.name=='1'){
        this.queryParams.pageNum = 1
        this.queryParams.jd = null
        if(this.noMore) this.noMore = false;
        this.getList()
      }else if(tab.name == '2'){
        this.queryParams.pageNum = 1
        this.queryParams.jd = '3'
        if(this.noMore) this.noMore = false;
        this.getList();
      }else if (tab.name == '3'){
        this.queryParams.pageNum = 1
        this.queryParams.jd = '1'
        if(this.noMore) this.noMore = false;
        this.getList();
      }else if(tab.name == '4'){
        this.queryParams.pageNum = 1
        this.queryParams.jd = '2'
        if(this.noMore) this.noMore = false;
        this.getList();
      }
    },


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

    /** 查询任务列表 */
    getList() {
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(',')
      }
      this.loading = true;
      listTask(this.queryParams).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if(list[i].fmt == null || list[i].fmt == '' || list[i].fmt == undefined){
            list[i].fmt = require("@/assets/images/task_cover.png");
          }
        }
        this.taskList = list;
        if(this.taskList.length>0){
          this.taskList.forEach((item,index) => {
            this.tableData.push(item);
          })
        }
        this.total = response.total;
        if(this.taskList.length<=this.total){
          this.loading = false;
          this.noMore = true
        }
      });
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
      this.queryParams.jd = null
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
      this.reset();
      this.open = true;
      this.title = "添加任务";
    },
    /** 查看详情 */
    handleDetail(row) {
      let jd = null
      getTaskInfoDetail(row.id).then(res=>{
        jd = res.data.jd
        if(jd){
          console.log(JSON.parse(jd).lastStudy)
          if(JSON.parse(jd).lastStudy==undefined){
            jd = null
          }else{
            jd =  JSON.parse(jd)["lastStudy"]["current"]
          }
        }
        let params = {}
        params.streamName = 'stream'
        params.eventType = 'CLI_TASK'
        params.id = row.id
        //点击埋点
        clickSendRedis(params).then(response =>{
          console.log("点击任务执行埋点",response)
        })

        // this.$router.push("/taskManagement/detail/" + row.id);
        let router =  this.$router.resolve({
          path: "/taskManagement/detail/",
          query: {rwid: row.id, jd:jd}
        });
        window.open(router.href,'_blank')
      })


    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "RW" }).then(response => {
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
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

.title-text{
  font-size: 26px;
  color: #333333;
}

::v-deep .el-tabs__nav-wrap::after {
  /*el-tabs组件隐藏下划线*/
  position: static !important;
}

::v-deep .el-tabs__item {
  font-size: 18px;
  color: #999999;
}
::v-deep .el-tabs__item.is-active {
  color: black;
}

.col-top {
  height: 22px;
  font-size: 20px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 52px;
}

.col-center {
  height: 15px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #666666;
  line-height: 20px;
}

.col-bottom {
  height: 13px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 38px;
}

::v-deep .el-button--primary{
  background-color: #0045C5;
  border-radius: 6px;
}

</style>
