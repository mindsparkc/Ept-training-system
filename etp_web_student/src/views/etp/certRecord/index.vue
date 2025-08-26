<template>
  <div class="app-container" style="overflow-x: hidden;">
    <div style="color: #333333; font-size: 26px; margin-bottom: 28px">
      我的证书
    </div>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="证书编号" prop="certId">
        <el-input
          v-model="queryParams.certId"
          placeholder="请输入证书编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证书名称" prop="certName">
        <el-input
          v-model="queryParams.certName"
          placeholder="请输入证书名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery" :loading="loading&&!isReset"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery" :loading="loading&&isReset"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <span style="color: #999999; font-size: 14px" v-if="total && total > 0">
      共找到
      <span style="color: #ff0000; font-size: 14px">{{
        total ? total : "0"
      }}</span>
      张证书</span
    >
    <!-- <span style="color: #999999; font-size: 14px" v-else>暂未获得证书</span> -->

    <el-row :gutter="50" style="margin-top: 20px">
      <el-col :span="6" v-for="(item, index) in recordList" :key="index">
        <div class="item-cert-content" @click="viewPic(item)">
          <span class="font-size-xl">{{ item.certName }}</span>

          <span class="font-size-l margin-l text-ell">{{ item.content }}</span>
          <span class="font-size-l margin-xl"
            >特颁此证 以资鼓励</span
          >
          <span class="font-size-m margin-xl">NO.{{ item.certId }}</span>
          <span v-if="item.status == 99" class="font-size-m "
            >颁发时间 {{ item.updateTime }}</span
          >
          <span v-if="item.status == 99" class="font-size-m "
            >有效期至 {{ item.expireTime }}</span
          >

          <span v-else>制证中</span>
        </div>
      </el-col>
    </el-row>

    <p v-if="loading" class="text-p">下拉加载更多数据</p>
    <p v-if="noMore" class="text-p">暂无更多数据</p>
    <el-table
      v-if="false"
      v-loading="loading"
      :data="recordList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!--      <el-table-column label="颁发记录编号" align="center" prop="recordId" />-->
      <!--      <el-table-column label="证书模板编号" align="center" prop="tempId" />-->
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column label="证书编号" align="center" prop="certId" />
      <!--      <el-table-column label="证书预览" align="center" prop="previewFile" >-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-image-->
      <!--            style="width: 100px; height: 100px"-->
      <!--            v-show="scope.row.previewFile!=null"-->
      <!--            :src="scope.row.previewFile"-->
      <!--            :preview-src-list="[scope.row.previewFile]">-->
      <!--          </el-image>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="证书名称" align="center" prop="certName" />
      <el-table-column
        label="证书过期时间"
        align="center"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="证书所有者" align="center" prop="nickName" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 99">已发布</span>
          <span v-else>生成中...</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="content" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="预览" placement="top">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-medal"
              @click="viewPic(scope.row)"
            ></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-if="false"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      title="证书预览"
      :visible.sync="picOpen"
      width="540px"
      append-to-body
    >
      <el-image
        style="width: 100%; height: 100%;"
        :src="pic_url"
        :preview-src-list="[pic_url]"
      >
      </el-image>
      <div slot="footer" class="dialog-footer">
        <el-button @click="picOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord } from "@/api/etp/certRecord/record";

export default {
  name: "Record",
  data() {
    return {
      // 遮罩层

      loading: true,
      isReset:false,
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
      // 电子证书颁发记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tempId: null,
        certId: null,
        certName: null,
        expireTime: null,
        userId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      picOpen: false,
      pic_url: null,
    };
  },
  created() {
    this.getList();
  },

  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  methods: {
    handleScroll() {
      // 获取滚动的距离
      let scrollTop = document.documentElement.scrollTop;
      // 获取滚动的高度（获取整个html的高度）
      let scrollHeight = document.documentElement.scrollHeight;
      // 获取屏幕(浏览器)高度
      let clientHeight = document.documentElement.clientHeight;
      // 滚动的距离 + 屏幕高度 - 内容高度 >= 0 表示滚动到底部了      (下拉加载判断条件)
      if (scrollTop + clientHeight - scrollHeight >= 0) {
        //判断是否需要获取数据
        if (this.recordList.length >= this.total) {
          this.loading = false;
          this.noMore = true;
        } else {
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum++;
          this.getList();
        }
      }
    },
    viewPic(row) {
      if (row.status == 99) {
        this.pic_url = row.previewFile;
        this.picOpen = true;
      } else {
        this.$message.error("制证中，请稍候查看");
      }
    },

    /** 查询电子证书颁发记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then((response) => {
        this.loading = false;

        let list = response.rows;
        if (list && list.length > 0) {
          this.recordList = this.recordList.concat(list);
          this.total = response.total;
          if (this.recordList.length >= this.total) {
            //若本次请求列表长度大于等于列表总数 则认为没有更多数据了
            this.noMore = true;
          }


        } else {
          this.noMore = true;
        }

        this.loading = false;
        this.isReset = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        recordId: null,
        tempId: null,
        certId: null,
        certName: null,
        certFile: null,
        previewFile: null,
        expireTime: null,
        grantType: null,
        grantDisplay: null,
        userId: null,
        status: "0",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.total = 0;
      this.queryParams.pageNum = 1;
      this.recordList = []
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.isReset = true;

      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.recordId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加电子证书颁发记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids;
      getRecord(recordId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改电子证书颁发记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.recordId != null) {
            updateRecord(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then((response) => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const recordIds = row.recordId || this.ids;
      this.$confirm(
        '是否确认删除电子证书颁发记录编号为"' + recordIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delRecord(recordIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有电子证书颁发记录数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.exportLoading = true;
          return exportRecord(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>

<style scoped>
.item-cert-content {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background-image: url("../../../assets/images/cert/bg-cert.png");
  background-size: cover;

  color: white;
  height: 410px;
  margin-bottom: 20px;
  padding: 12px;
  text-align: center;
}

.text-p {
  text-align: center;
  color: #999999;
  font-size: 12px;
}
.text-ell {
  text-overflow: ellipsis;
  word-break: break-all;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}

::v-deep .el-button--primary {
  background-color: #0045c5;
  border-color: #0045c5;
  border-radius: 6px;
}

.font-size-xl {
  font-size: 26px;
}
.font-size-l {
  font-size: 16px;
}
.font-size-m {
  font-size: 14px;
}

.margin-xl {
  margin-top: 24px;
}
.margin-l {
  margin-top: 12px;
}
</style>
