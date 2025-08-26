<template>
 <!-- 分配用户 -->
  <div class="app-container">
    <el-row :gutter="24" style="margin-top: -35px">
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="用户名称" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="用户状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="用户组" prop="status">
            <el-select
              v-model="queryParams.groupId"
              placeholder="用户组"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in groupOptions"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="12" class="mb8">
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList"
                  ref="multipleTable"
                  @select-all="selectAll"
                  @select="handleSelect"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="用户编号" align="center" key="userId" prop="userId" />
          <el-table-column label="用户名称" align="center" key="userName" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName" :show-overflow-tooltip="true" />
          <el-table-column label="用户组" align="center" key="groupId" prop="groupId"  :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ groupToLabel(scope.row) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName" :show-overflow-tooltip="true" />
          <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" width="120" />
          <el-table-column label="状态" align="center" key="status" >
            <template slot-scope="scope">
              {{ scope.row.status == '0'?'正常':'停用' }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime"  width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
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
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { listUser, } from "@/api/system/user";
import { listGroup } from "@/api/system/userGroup";
export default {
  name: "index",
  props:{
    xz_data:{
      default:[]
    }
  },
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
      // 用户表格数据
      userList: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined,
        groupId:undefined,
      },
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      groupOptions:[],

    };
  },

  created() {
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then(response => {
      this.sexOptions = response.data;
    });
    this.listGroup();
  },
  methods: {

    groupToLabel(val){
      let map = new Map();
      this.groupOptions.forEach(item => {
        map.set(item.value,item.label)
      })
      let groupid = val.groupId
      if (groupid)
        return map.get(val.groupId)
      else
        return "--";
    },
    /** 获取用户组别 */
    listGroup(){
      listGroup().then( res => {
        let rows = res.rows;
        let arr = [];
        rows.forEach( row => {
          let tmpObj = {
            value: row.groupId,
            label: row.groupName
          }
          arr.push(tmpObj);
        })
        this.groupOptions = arr;
      })

    },

    getChoiceUser(){
      let userArr = [];
      this.ids.forEach(id => {
        this.userList.forEach(user => {
          if (id == user.userId) userArr.push(user)
        })
      })
      return userArr;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(',')
      }
      this.getList();
    },
    /** 查询用户列表 */
    async getList() {
      this.queryParams.deptId = this.$store.getters.qyid;
      this.loading = true;
      await listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.userList = this.userList.filter( item => {
            return !item.admin
          })
          this.total = response.total;
          this.loading = false;
        }
      );

      await this.toggleSelection(this.xz_data)
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.groupId = undefined
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    handleSelect(list,item){
      debugger
      if(list.includes(item)){
        this.xz_data.push(item)
      }else{
        this.xz_data = this.xz_data.filter((data) => {
          if(data.userId != item.userId){
            return data
          }
        });
      }
    },
    selectAll(selection){
      let data = this.userList
      if(selection.length>0){
        //全选
        data.forEach(item =>{
          //查看选中的data中是否有这条数据
          let result = this.xz_data.find(xz => xz.userId == item.userId)
          if(result == undefined){
            this.xz_data.push(item)
          }
        })
      }else{
        //取消全选
        data.forEach(item =>{
          this.xz_data = this.xz_data.filter((xz) => {
            if(xz.userId != item.userId){
              return data
            }
          });
        })
      }

    },
    //设置选中
    toggleSelection(rows) {
      let data_list =this.userList
      if (rows) {
        this.$nextTick(()=>{
          rows.forEach((row) => {
            if(row != undefined){
              let data = data_list.find(item => row.userId == item.userId)
              if(data!= undefined){
                this.$refs.multipleTable.toggleRowSelection(data, true);
              }
            }
          });
        })
      }

    },
  }
};
</script>

<style scoped>

</style>
