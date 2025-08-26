<template>
<!--增加课程-->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入课程名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程描述" prop="ms">
        <el-input
          v-model="queryParams.ms"
          placeholder="请输入课程相关描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <el-checkbox-group v-model="queryTag">
          <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{item.name }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading" :data="curriculumList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"/>
      <el-table-column label="序号" type="index" align="center"/>
      <el-table-column label="封面图" align="center" prop="fmt">
        <template slot-scope="scope">
          <el-image
            style="width: 80px; height: 60px;border-radius: 5px"
            :src="scope.row.fmt==null?require('@/assets/images/cover.jpg'):scope.row.fmt"
            fit="cover"
            :preview-src-list="new Array(scope.row.fmt)"/>
        </template>
      </el-table-column>
      <el-table-column label="课程名称" align="center" prop="mc"/>
<!--      <el-table-column label="开放程度" align="center" prop="kfcd">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.kfcd == '0'">完全公开</el-tag>
          <el-tag v-if="scope.row.kfcd == '1'" type="warning">限定用户</el-tag>
          <el-tag v-if="scope.row.kfcd == '2'" type="danger">不公开</el-tag>
        </template>
      </el-table-column>-->
      <el-table-column label="标签" align="center" prop="tags" width="100px" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null"
                  v-for="(item,index) in (scope.row.tags||'').split(',')"
                  :key="index"
                  style="margin-right: 3px;margin-bottom:2px">
            {{item}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="states">
        <template slot-scope="scope">
          <el-tag :type="scope.row.states == '0'?'info':'success'">{{ scope.row.states == '0' ? '暂存' : '已发布' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="课程描述" align="center" width="150px"  prop="ms">
        <template  slot-scope="scope">
          <el-tooltip effect="dark" :content="scope.row.ms" placement="bottom-end">
            <p class="desc">{{ scope.row.ms }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" width="180" prop="updateTime"/>
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
import { listCurriculum } from "@/api/etp/curriculum/curriculum";
import {listTagRelate} from "@/api/etp/tag/tag";

export default {
  name: "addCurriculums",

  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // curriculum表格数据
      curriculumList: [],
      ids:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        states: '99',
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        tags:[]
      },
      // 标签用到的
      tagValue: [],
      tagOptions:[],
      queryTag:[]
    }
  },

  methods:{

    getCurriculumsByIds(){
      let datas = [];
      this.ids.forEach(item => {
        this.curriculumList.forEach(curr => {
          if (item == curr.id) {
            curr['source_name'] = curr.mc
            curr['source_type'] = 'kc';
            datas.push(curr)
          }
        })
      })
      return datas;
    },

    /** 标签相关 */
    getTags() {
      listTagRelate({modelType: "KC"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },

    /** 查询curriculum列表 */
    getList() {
      this.loading = true;
      listCurriculum(this.queryParams).then(response => {
        this.curriculumList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if(null != this.queryTag){
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
