<!--增加试题-->
<template>
  <div class="app-container">
    <el-tabs v-model="tabsActiveName" type="card" @tab-click="handleClick">
      <el-tab-pane label="全部" name="first">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">

        <el-form-item label="习题集名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入习题集名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="习题集状态" prop="status">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择习题集状态"
            :disabled="true"
            clearable size="small">
            <el-option
              v-for="dict in fbOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue" />
          </el-select>
        </el-form-item>

        <el-form-item style="float: right;margin-top: 25px">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>


      </el-form>
      <el-table v-loading="loading"
                ref="multipleTable"
                :data="paperList"
                @select-all="selectAll"
                @select="handleSelect"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="习题集名称" align="left" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="习题集总分" align="center" prop="totalScore" />
        <el-table-column label="及格分" align="center" prop="qualifyScore" />
        <el-table-column label="题目数量" align="center" prop="quCount" />
      </el-table>
      </el-tab-pane>

      <el-tab-pane label="已选择" name="second">
        <el-table
          v-loading="loading"
          ref="xzTable"
          :data="xz_data">
          <el-table-column label="习题集名称" align="left" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="习题集总分" align="center" prop="totalScore" />
          <el-table-column label="及格分" align="center" prop="qualifyScore" />
          <el-table-column label="题目数量" align="center" prop="quCount" />
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
import {listPaper} from "@/api/paper/paper";

export default {
  name: "addXtj",
  components: {

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

      paperList: [],
      isIndeterminate: true,
      checkAll: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        content: null,
        status: '99',
        flag: 'train',
      },

      fbOptions:[],

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

  },
  created() {
    this.getDicts("testpaper_fb").then(response => {
      this.fbOptions = response.data;
    });

  },
  methods: {

    /** 选择试题*/
    collectDatasByIdsArr() {
      let datas = [];
      this.xz_data.forEach(item => {
        let name = item.name
        if(name==""||name==null){
          name= item.name
        }
        item['source_name'] = name
        item['source_type'] = 'xtj';
        item['id'] = item.paperId
        datas.push(item)
      })
      return datas;
    },
    /** 查询习题集列表 */
    async getList() {
      this.loading = true;
      await listPaper(this.queryParams).then(response => {
        this.paperList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      await this.toggleSelection(this.xz_data)
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
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
      this.handleQuery();
    },
    /** 多选框选中数据*/
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.paperId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },


    handleSelect(list,item){
      if(list.includes(item)){
        item['id'] = item.paperId
        this.xz_data.push(item)
      }else{
        this.xz_data = this.xz_data.filter((data) => {
          if(data.paperId != item.paperId){
            return data
          }
        });
      }
    },
    selectAll(selection){
      let data = this.paperList
      if(selection.length>0){
        //全选
        data.forEach(item =>{
          //查看选中的data中是否有这条数据
          let result = this.xz_data.find(xz => xz.paperId == item.paperId)
          if(result == undefined){
            item['id'] = item.paperId
            this.xz_data.push(item)
          }
        })
      }else{
        //取消全选
        data.forEach(item =>{
          this.xz_data = this.xz_data.filter((xz) => {
            if(xz.paperId != item.paperId){
              return data
            }
          });
        })
      }

    },
    //设置选中
    toggleSelection(rows) {
      let data_list =this.paperList
      if (rows) {
        this.$nextTick(()=>{
          rows.forEach((row) => {
            if(row != undefined){
              let data = data_list.find(item => row.paperId == item.paperId)
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
      this.$confirm(`是否删除【${row.name}】内容项?`, '提示', {
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
