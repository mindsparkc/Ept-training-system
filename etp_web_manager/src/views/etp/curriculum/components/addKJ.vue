<!--增加课件-->
<template>
  <div class="app-container">

    <el-tabs v-model="tabsActiveName" type="card" @tab-click="handleClick">
      <el-tab-pane label="全部" name="first">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="课件名称" prop="mc">
            <el-input
              v-model="queryParams.mc"
              placeholder="请输入课件名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="课件描述" prop="ms">
            <el-input
              v-model="queryParams.ms"
              placeholder="请输入课件描述"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="300"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="课件类型" class="tags-div" prop="xs" style="width: calc(100% - 256px);">
            <el-radio-group v-model="queryParams.xs">
              <el-radio
                        :key="null"
                        :label="null"
              >全部</el-radio>
              <el-radio v-for="dict in xsOptions"
                        :key="dict.dictValue"
                        :label="dict.dictValue"
              >{{ dict.dictLabel }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
            <el-checkbox-group v-model="queryTag">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
              <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item style="float: right;margin-top: 25px">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading"
                  ref="multipleTable"
                  :data="coursewareList"
                  @select-all="selectAll"
                  @select="handleSelect"
                 >
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="封面图片" align="center" prop="fm">
            <template slot-scope="scope">
              <el-image
                style="width: 80px; height: 60px;border-radius: 5px"
                :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
                :fit="fit"
                :preview-src-list="new Array(scope.row.fm)"/>
            </template>
          </el-table-column>
          <el-table-column label="课件名称" align="center" prop="mc"/>
          <el-table-column label="课件形式" align="center" prop="xs">
            <template slot-scope="scope">
              <p>{{ scope.row.xs == '1' ? "OFFICE" : "" }}</p>
              <p>{{ scope.row.xs == '2' ? "视频" : "" }}</p>
              <p>{{ scope.row.xs == '3' ? "富文本" : "" }}</p>
              <p>{{ scope.row.xs == '4' ? "外部链接" : "" }}</p>
              <p>{{ scope.row.xs == '5' ? "其他文件" : "" }}</p>
            </template>
          </el-table-column>
          <el-table-column label="课件描述" align="center" width="150px"  prop="ms">
            <template  slot-scope="scope">
              <el-tooltip effect="dark" :content="scope.row.ms" placement="bottom-end">
                <p class="desc">{{ scope.row.ms }}</p>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="开放程度" align="center" prop="kfcd">
            <template slot-scope="scope">
              <el-tag :type="scope.row.kfcd == '1'?'warning':''">{{ scope.row.kfcd == '1' ? '限定用户' : '完全公开' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" width="180px"/>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
      <el-tab-pane label="已选择" name="second">
        <el-table v-loading="loading"
                  ref="xzTable"
                  :data="xz_data">
          <el-table-column label="封面图片" align="center" prop="fm">
            <template slot-scope="scope">
              <el-image
                style="width: 80px; height: 60px;border-radius: 5px"
                :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
                :fit="fit"
                :preview-src-list="new Array(scope.row.fm)"/>
            </template>
          </el-table-column>
          <el-table-column label="课件名称" align="center" prop="mc"/>
          <el-table-column label="课件形式" align="center" prop="xs">
            <template slot-scope="scope">
              <p>{{ scope.row.xs == '1' ? "OFFICE" : "" }}</p>
              <p>{{ scope.row.xs == '2' ? "视频" : "" }}</p>
              <p>{{ scope.row.xs == '3' ? "富文本" : "" }}</p>
              <p>{{ scope.row.xs == '4' ? "外部链接" : "" }}</p>
              <p>{{ scope.row.xs == '5' ? "其他文件" : "" }}</p>
            </template>
          </el-table-column>
          <el-table-column label="课件描述" align="center" width="150px"  prop="ms">
            <template  slot-scope="scope">
              <el-tooltip effect="dark" :content="scope.row.ms" placement="bottom-end">
                <p class="desc">{{ scope.row.ms }}</p>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="开放程度" align="center" prop="kfcd">
            <template slot-scope="scope">
              <el-tag :type="scope.row.kfcd == '1'?'warning':''">{{ scope.row.kfcd == '1' ? '限定用户' : '完全公开' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" width="180px"/>
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
import { listCourseware, } from "@/api/etp/courseware/courseware";
import { listTagRelate } from "@/api/etp/tag/tag";

export default {
  name: "addKJ",
  data() {
    return {
      fit: "cover",
      queryTag: [],
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      isIndeterminate: true,
      checkAll: false,
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
      // 课件管理表格数据
      coursewareList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fm: null,
        xs: null,
        lj: null,
        nr: null,
        kjdz: null,
        kfcd: null,
        tags: [],
      },
      xsOptions:[],
      kfcdOptions:[],

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
    this.getDicts("courseware_type").then(res => {
      this.xsOptions = res.data;
    });
    this.getDicts("courseware_open_close").then(res => {
      this.kfcdOptions = res.data;
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
    /** 选择课件*/
    collectDatasByIdsArr() {
      let datas = [];
      this.xz_data.forEach(item => {
        item['source_name'] = item.mc
        item['source_type'] = 'kj'
        datas.push(item)
      })
      return datas;
    },
    /** 查询课件管理列表 */
    async getList() {
      this.loading = true;
      await listCourseware(this.queryParams).then(response => {
        this.coursewareList = response.rows;
        this.total = response.total;
        this.loading = false;
      });

      await this.toggleSelection(this.xz_data)
    },
    /** 获取标签 */
    getTags() {
      listTagRelate({modelType: "KJ"}).then(response => {
        this.tagOptions = response.data;
      });
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
    /** 多选框选中数据 */
 /*   handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },*/

    handleClick(tab, event) {
      console.log(tab, event);

      if(tab.name == 'first'){
        //更新选中状态
        this.getList()
      }
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
      let data = this.coursewareList
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
      let data_list =this.coursewareList
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
      this.$confirm(`是否删除【${row.mc}】内容项?`, '提示', {
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
.desc {
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}
.tags-div ::v-deep .el-form-item__content {
  width: calc(100% - 100px) !important;
}
</style>
