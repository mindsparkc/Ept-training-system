<!--增加试题-->
<template>
  <div class="app-container">
    <el-tabs v-model="tabsActiveName" type="card" @tab-click="handleClick">
      <el-tab-pane label="全部" name="first">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
        <el-form-item label="题目名称：" prop="tgDisplat">
          <el-input
            v-model="queryParams.tgDisplat"
            placeholder="请输入题干"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
            maxlength="300"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="题目类型" prop="lx">
          <el-select v-model="queryParams.lx" placeholder="请选择类型：" clearable size="small">
            <el-option
              v-for="dict in lxOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="更新时间" prop="upadteTime">
          <el-date-picker
            v-model="queryParams.upadteTime"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
          <el-checkbox-group v-model="queryTag">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            <el-checkbox :label="item.id" v-for="item in tagOptions"> {{ item.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item style="float: right;margin-top: 25px">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>


      </el-form>
      <el-table v-loading="loading"
                ref="multipleTable"
                :data="subjectList"
                @select-all="selectAll"
                @select="handleSelect"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="题干" align="center" prop="tg" width="300" :show-overflow-tooltip="true" :align="'left'" >
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
        @pagination="getList"/>
      </el-tab-pane>

      <el-tab-pane label="已选择" name="second">
        <el-table
          v-loading="loading"
          ref="xzTable"
          :data="xz_data">
          <el-table-column label="题干" align="center" prop="tg" width="300" :show-overflow-tooltip="true" :align="'left'" >
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
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="删除" placement="top-end">
                <el-button type="text" style="padding:0;color: red"
                           @click="delItem(scope.row, scope.$index)">
                  <i class="el-icon-delete etp_font_color_red etp_font_size"></i>
                </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { listSubject, } from "@/api/etp/subject/subject";
import { listTagRelate } from "@/api/etp/tag/tag";
import tagsEdit from "@/components/tags/tagsEdit";

export default {
  name: "addST",
  components: {
    tagsEdit
  },
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 题库表格数据
      subjectList: [],
      isIndeterminate: true,
      checkAll: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tags: []
      },
      queryTag: [],
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      // 类型
      lxOptions: [],
      levelOptions: [],
      sflxOptions: [],
      // 是否考试
      sfksOptions: [],


      //tabs
      tabsActiveName:'first',
      xz_data:[],//选中的数据
    };
  },
  watch: {
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
  created() {
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    });
    this.getDicts("subject_level").then(response => {
      this.levelOptions = response.data;
    });
    this.getDicts("subject_yes_no").then(response => {
      this.queryParams.sflx = '1' // 只能选择练习题
      this.sflxOptions = response.data;
      this.sfksOptions = response.data;
    });
  },
  methods: {
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
    /** 选择试题*/
    collectDatasByIdsArr() {
      let datas = [];
      this.xz_data.forEach(item => {
        let tg = item.tgDisplat
        if(tg==""||tg==null){
          tg= item.tg
        }
        item['source_name'] = tg
        item['source_type'] = 'st';
        datas.push(item)
      })
      return datas;
    },
    /** 查询题库列表 */
    async getList() {
      this.loading = true;
      this.queryParams.sflx = '1' // 只能选择练习题
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
      if (null != this.queryParams.upadteTime) {
        this.queryParams.beginTime = this.queryParams.upadteTime[0]
        this.queryParams.endTime = this.queryParams.upadteTime[1]
      }else{
        this.queryParams.beginTime = null
        this.queryParams.endTime = null
      }

      this.getList();
    },

    handleClick(tab, event) {
      console.log(tab, event);

      if(tab.name == 'first'){
        //更新选中状态
        this.getList()
      }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = [];
      this.handleQuery();
    },
    /** 多选框选中数据*/
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 类型格式转换*/
    lxFormat(row, column) {
      return this.selectDictLabel(this.lxOptions, row.lx);
    },
    // 难易程度：
    levelFormat(row, column) {
      return this.selectDictLabel(this.levelOptions, row.level);
    },
    /** 获取标签*/
    getTags() {
      listTagRelate({modelType: "TK"}).then(response => {
        this.tagOptions = response.data;
      });
    },

    handleSelect(list,item){
      if(list.includes(item)){
        this.xz_data.push(item)
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
    //设置选中
    toggleSelection(rows) {
      let data_list =this.subjectList
      if (rows) {
        this.$nextTick(()=>{
          rows.forEach((row) => {
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
    /** 删除课件或者试题*/
    delItem( row, index) {
      this.$confirm(`是否删除【${row.tg}】内容项?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.xz_data.splice(index, 1);
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch();
    }
  }

}
</script>

<style scoped>
.tags-div ::v-deep .el-form-item__content {
  width: calc(100% - 100px) !important;
}
</style>
