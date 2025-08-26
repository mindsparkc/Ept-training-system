<!--增加试题-->
<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" @submit.native.prevent :inline="true" v-show="showSearch" label-width="90px">
      <el-row>
        <el-form-item label="题目名称：" prop="tg">
          <el-input
            v-model="queryParams.tg"
            placeholder="请输入题干"

            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-col :span="24">
          <el-form-item label="标签：" class="tags-div" prop="queryTag">
            <el-checkbox-group v-model="queryTag">
              <el-checkbox :label="item.id" v-for="item in tagOptions"> {{ item.name }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row style="padding-left: 20px;color: #1c84c6">
<!--        <el-col :span="5">-->
<!--          <label>题目总数：{{total}}</label>-->
<!--        </el-col>-->
        <el-col :span="10">
          <label>已选中：{{xz_data.length}}</label>
        </el-col>
<!--        <el-col :span="3">-->
<!--          单选题-{{xz_count.dx}}-->
<!--        </el-col>-->
<!--        <el-col :span="3">-->
<!--          复选题-{{xz_count.fx}}-->
<!--        </el-col>-->
<!--        <el-col :span="3">-->
<!--          判断题-{{xz_count.pd}}-->
<!--        </el-col>-->
<!--        <el-col :span="3">-->
<!--          填空题-{{xz_count.tk}}-->
<!--        </el-col>-->
<!--        <el-col :span="3">-->
<!--          辨析题-{{xz_count.zg}}-->
<!--        </el-col>-->
        <el-col :span="6" :offset="8">
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table v-loading="loading"
              ref="multipleTable"
              :data="subjectList"
              height="350"
              @select-all="selectAll"
              @select="handleSelect">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column
        fixed
        type="index"
        width="50">
      </el-table-column>
      <el-table-column label="题干" align="center" prop="tg" width="300" :show-overflow-tooltip="true" :align="'left'">
        <template slot-scope="scope">
          <span v-if="scope.row.tgDisplat === null||scope.row.tgDisplat === ''">{{scope.row.tg}}</span>
          <span v-else>{{scope.row.tgDisplat}}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="lx" :formatter="lxFormat"/>
      <el-table-column label="难易程度" align="center" prop="level" :formatter="levelFormat"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
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

  </div>
</template>

<script>
import {listSubject,} from "@/api/etp/subject/subject";
import tagsEdit from "@/components/tags/tagsEdit";
import {listTagRelate} from "@/api/etp/tag/tag";

export default {
  name: "addSTHand",
  components: {
    tagsEdit
  },
  props:{
    lxOptions:{
      type:Array,
      default:[]
    },
    tm_type:{
      type:String,
      default:""
    },
    zjcl:{
      type:String,
      default:"0"
    },
    //设置的题目个数
    szgs:{
      type:Number,
      default:0
    }
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 题库表格数据
      subjectList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        lx: null,
        tg: null,
        tags: []
      },
      queryTag: [],
      // 标签用到的
      tagValue: [],
      tagOptions: [],

      open:false,
      xz_lx:[],
      xz_data:[],//选中数据
      //选中计数
      xz_count:{dx:0,fx:0,pd:0,tk:0,zg:0},
      multipleSelection: [],
      levelOptions: []
    };
  },
  created() {
    this.getDicts("subject_level").then(response => {
      this.levelOptions = response.data;
    });
    this.getTags();
    this.getList()
  },
  watch:{
    queryParams:{
      handler(){
        this.getList();
      },
      deep:true
    },
    queryTag:{
      handler(){
        if (null != this.queryTag) {
          this.queryParams.tags = this.queryTag.join(',')
        }
        this.getList();
      },
      deep:true
    }
  },
  methods: {
    // 难易程度：
    levelFormat(row, column) {
      return this.selectDictLabel(this.levelOptions, row.level);
    },
    /** 查询题库列表 */
    async getList() {
      this.loading = true;
      this.queryParams.lx = this.tm_type
      await listSubject(this.queryParams).then(response => {
        this.subjectList = response.rows;
        this.total = response.total;
        this.loading = false;
      });

     await this.toggleSelection(this.xz_data)
    },
    havaTags(val) {
      this.tagValue = val
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
      this.xz_lx = [];
      this.multipleSelection = [];
      this.ids = [];
      this.handleQuery();
    },
    handleSelect(list,item){
      //自选题组卷，选中个数大于设置个数，则不能选中
      if(list.includes(item)){
        if(this.zjcl == '0' && this.xz_data.length >= this.szgs){

          let options={duration:10000,message:"选中题目个数已达到设置个数，不能继续选择"}
          this.$message.warning(options)
          this.$refs.multipleTable.toggleRowSelection(item, false);
        }else{
          this.xz_data.push(item)
        }

      }else{
        this.xz_data = this.xz_data.filter((data) => {
          if(data.id != item.id){
            return data
          }
        });
      }
    },
    selectAll(selection){
      let data = this.subjectList
      if(selection.length>0){
        //全选
        data.forEach(item =>{
          //查看选中的data中是否有这条数据
          let result = this.xz_data.find(xz => xz.id == item.id)
          if(result == undefined){
            this.xz_data.push(item)
          }
        })
      }else{
        //取消全选
        data.forEach(item =>{
          this.xz_data = this.xz_data.filter((xz) => {
            if(xz.id != item.id){
              return data
            }
          });
        })
      }

    },
    // 多选框选中数据
    handleSelectionChange(selection) {

      // selection.map(function (item) {
      //   if(item != undefined){
      //    let flag = this.multipleSelection.find(sel => {
      //       return  item.id == sel.id
      //     })
      //
      //   }
      // })
      //this.multipleSelection = selection;
    },

    // 类型：
    lxFormat(row, column) {
      return this.selectDictLabel(this.lxOptions, row.lx);
    },
    getTags() {
      listTagRelate({modelType: "TK"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    //选中
    getRowKeys(row){
      return row.id;
    },

    //设置选中
    toggleSelection(rows) {
      let data_list =this.subjectList
      if (rows) {
        this.$nextTick(()=>{
          rows.forEach(row => {
            if(row != undefined){
              let data = data_list.find(item => row.id == item.id)
              if(data!= undefined){
                this.$refs.multipleTable.toggleRowSelection(data, true);
              }

            }
          });
        })
      }

    },

  }
}
</script>

<style scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
::v-deep .el-form-item {
  margin-bottom: 0px;
}
::v-deep .el-dialog__body {
  padding: 0px 20px;
}
</style>
