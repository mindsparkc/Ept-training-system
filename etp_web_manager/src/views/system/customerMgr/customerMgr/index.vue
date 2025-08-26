<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="企业名称" prop="tenantName">
        <el-input
          v-model="queryParams.tenantName"
          placeholder="请输入企业名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系人" prop="linkman">
        <el-input
          v-model="queryParams.linkman"
          placeholder="请输入联系人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="vip等级" prop="vipGrade">
        <el-select style="width: 100%" v-model="queryParams.vipGrade"  placeholder="请选择" clearable>
          <el-option v-for="item in listVipOption" :key="item.vipCode" :label="item.vipName" :value="item.vipCode"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="企业状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          v-hasPermi="['system:tenant:add']"
        >新增企业</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="deptList"
      row-key="deptId"
      default-expand-all
    >
      <el-table-column prop="tenantName" label="企业名称"></el-table-column>
      <el-table-column prop="linkman" label="企业联系人"></el-table-column>
      <el-table-column prop="userName" label="登录账号"></el-table-column>
      <el-table-column prop="nickName" label="账号昵称"></el-table-column>
      <el-table-column prop="vipGrade" label="vip等级"></el-table-column>
      <el-table-column prop="vipStartTime" label="vip开始时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.vipStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="vipEndTime" label="vip结束时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.vipEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="registerTime" label="注册时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="注册说明"></el-table-column>
      <el-table-column prop="status" label="状态" width="120" :formatter="statusFormat"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tenant:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tenant:remove']"
          >删除</el-button>
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

    <!-- 添加或修改企业对话框 -->
    <el-dialog :title="title"
               :visible.sync="open"
               :close-on-click-modal="false"
               width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="tenantName">
              <el-input v-model="form.tenantName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="linkman">
              <el-input v-model="form.linkman" placeholder="联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.id == null">
            <el-form-item label="登录账号" prop="userName">
              <el-input v-model="form.userName" placeholder="登录账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.id == null">
            <el-form-item label="账号昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="账号昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.id == null">
            <el-form-item label="登录密码" prop="password">
              <el-input v-model="form.password" placeholder="登录密码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="vip等级" prop="vipGrade">
              <el-select style="width: 100%" v-model="form.vipGrade"  placeholder="请选择" clearable>
                <el-option v-for="item in listVipOption" :key="item.vipCode" :label="item.vipName" :value="item.vipCode"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listDept
} from "@/api/system/dept";
import {addCustomer, delCustomer, updateCustomer} from "@/api/system/tenant"
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {listVip} from "@/api/system/vip";
import {getTenant, listTenant} from "@/api/system/tenant";

export default {
  name: "Dept",
  components: { },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 总条数
      total: 0,
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        tenantName: undefined,
        linkman: undefined,
        vipGrade: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantName: [
          { required: true, message: "企业名称不能为空", trigger: "blur" }
        ],
        userName: [
          { required: true, message: "登录账号不允许为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "登录密码不允许为空", trigger: "blur" }
        ],

        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      listVipOption: []
    };
  },
  created() {
    this.getList();
    this.getVipGrade();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    // 获取vip等级
    getVipGrade() {
      this.listVipOption.splice(0,this.listVipOption.length )
      listVip().then(res => {
        this.listVipOption.push(...res.rows)
      })
    },
    /** 查询部门列表 */
    getList() {
      this.loading = true;
      this.deptList.splice(0, this.deptList.length)
      listTenant(this.queryParams).then(response => {
        this.deptList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      })
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children,
        // isDisabled: node.parentId == 0
      };
    },
    // 字典状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    initForm() {
      return {
        tenantName: null,
        phonenumber: null,
        email: null,
        linkman: null,
        userName: null,
        nickName: null,
        password: null,
        vipGrade: null,
        vipStartTime: null,
        vipEndTime: null,
        status: '0'
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = this.initForm();
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentId = row.deptId;
      }
      this.open = true;
      this.title = "添加部门";
      listDept({rootNodeType: 'tenant'}).then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getTenant(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$confirm('请确认是否保存当前企业信息?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.form.deptId != undefined) {
              updateCustomer(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCustomer(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }).catch(() => {

          });

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.tenantName + '"的企业?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delCustomer(row.id);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
