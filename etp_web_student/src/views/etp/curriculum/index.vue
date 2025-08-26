<template>
  <div class="app-container" style="overflow-x: hidden;">
    <div class="title-text">我的课程</div>

    <el-tabs v-model="activeName" @tab-click="handleClick" :tabBarStyle="{ borderBottom: 'unset' }"style="margin-top: 10px">
      <el-tab-pane label="全部" name="1"></el-tab-pane>
      <el-tab-pane label="已学完" name="2"></el-tab-pane>
      <el-tab-pane label="未学完" name="3"></el-tab-pane>
    </el-tabs>

    <el-row v-for="(item,index) in tableData" style="border-bottom: 1px solid #EEEEEE;margin-top:15px; ">
      <el-col style="margin-bottom: 15px" :span="4">
        <!--        <span>{{ item }}</span>-->
        <el-image style="width: 180px; height: 120px;border-radius: 8px" :src="item.fmt" :fit="fit"/>
      </el-col>
      <el-col :span="20" style="padding-left:10px;">
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
        <el-row style="margin-top: 24px">
          <el-col :span="3" class="col-bottom">
            <span>
              {{item.createBy}}
            </span>
          </el-col>
          <el-col :span="17" class="col-bottom">
            <span>{{item.num}}</span><span>人学习中</span>
          </el-col>
          <el-col :span="4">
            <el-button v-if="item.getStatus!='0'" size="medium" type="primary" @click="rowClick(item)">立即学习</el-button>
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

    <!-- 添加或修改curriculum对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" :close-on-click-modal="false" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名称" prop="mc">
          <el-input v-model="form.mc" placeholder="请输入课程名称"/>
        </el-form-item>
        <el-form-item label="封面图" prop="fmt">
          <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fmt"/>
        </el-form-item>

        <el-form-item label="开放程度" prop="kfcd">
          <el-radio-group v-model="form.kfcd">
            <el-radio :label="0">完全公开</el-radio>
            <el-radio :label="1">限定用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程描述" prop="ms">
          <el-input v-model="form.ms" type="textarea" placeholder="请输入课程描述"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import imageUpload from "@/components/ImageUpload";
import tagsEdit from "@/components/tags/tagsEdit";
import {
  listCurriculum,
  delCurriculum,
  addCurriculum,
  pushBack,
  getCurriculum,
} from "@/api/etp/curriculum/curriculum";
import { listTagRelate } from "@/api/etp/tag/tag";
import { clickSendRedis } from "@/api/buryPoint";

export default {
  name: "Curriculum",
  components: {
    imageUpload,
    tagsEdit,
    listTagRelate,
  },
  data() {
    return {
      fit: "cover",
      // 遮罩层
      loading: true,
      noMore: false,
      // 选中数组
      ids: [],
      open: false,
      title: "新增课程",
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // curriculum表格数据
      curriculumList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        jd: null,
        tags: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        mc: [{ required: true, message: "请输入表名称", trigger: "blur" }],
        fmt: [{ required: true, message: "请设置封面图", trigger: "blur" }],
        kfcd: [{ required: true, message: "请设置开放程度", trigger: "blur" }],
      },
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],
      isIndeterminate: true,
      checkAll: true,
      openFlag: true,
      tipFlag: false,
      height: "",
      //  -----------
      activeName: "1",
      tableData: [],
    };
  },
  created() {
    let jd = this.$route.query.jd;
    if (jd != undefined) {
      this.queryParams.jd = jd;
    }
    this.getList();
    this.getTags();
    //分类id
    let categoryId = this.$route.query.categoryId;
  },
  activated() {
    this.getList();
    this.getTags();
  },

  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },

  methods: {
    handleScroll() {
      var scrollTop = Math.floor(
        document.body.scrollTop ||
          document.documentElement.scrollTop ||
          window.pageYOffset
      );
      var clientHeight = Math.floor(
        document.body.clientHeight || document.documentElement.clientHeight
      );
      var scrollHeight = Math.floor(
        document.body.scrollHeight || document.documentElement.scrollHeight
      );

      if (scrollTop + clientHeight >= scrollHeight) {
        if (this.tableData.length >= this.total) {
          this.noMore = true;
          this.loading = false;
        } else {
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum += 1;
          this.getList();
        }
      }
    },

    handleClick(tab, event) {
      this.tableData = [];
      if (tab.name == "1") {
        this.queryParams.pageNum = 1;
        this.queryParams.jd = null;
        if (this.noMore) this.noMore = false;
        this.getList(true);
      } else if (tab.name == "2") {
        this.queryParams.pageNum = 1;
        this.queryParams.jd = "100";
        if (this.noMore) this.noMore = false;
        this.getList(true);
      } else if (tab.name == "3") {
        this.queryParams.pageNum = 1;
        this.queryParams.jd = "未学完";
        if (this.noMore) this.noMore = false;
        this.getList(true);
      }
    },

    //------------------------------------------------------------

    /** 跳转知识库页面 */
    knowledgeInfo() {
      this.$router.push({
        path: "/etp/curriculum/components/curriculumAll/",
      });
    },

    handleCheckAllChange(val) {
      this.queryTag = [];
      this.handleQuery();
    },

    handleCheckedTagsChange(value) {
      // let checkedCount = value.length;
      this.checkAll = false;
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },

    openFop() {
      this.openFlag = !this.openFlag;
      if (!this.openFlag) {
        // 展开
        this.height = "auto";
      } else {
        this.height = 45 + "px";
      }
      // this.$forceUpdate()
    },

    /** */
    rowClick(row) {
      let jd = null;
      getCurriculum(row.id).then(res => {
        jd = res.data.kcjd;
        if (jd) {
          if (JSON.parse(jd).lastStudy == undefined) {
            jd = null;
          } else {
            jd = JSON.parse(jd)["lastStudy"]["current"];
          }
        }
        let params = {};
        params.streamName = "stream";
        params.eventType = "CLI_CURR";
        params.id = row.id;

        //点击埋点
        clickSendRedis(params).then(response => {
          console.log("点击课程埋点", response);
        });

        // 打开新的浏览器标签页进行学习
        let router = this.$router.resolve({
          path: "/etp/curriculum/previewCurriculum/",
          query: { cid: row.id, jd: jd },
        });
        window.open(router.href, "_blank");
      });
    },
    /** 取消发布 */
    pushBack(row) {
      let id = row.id;
      this.$confirm(`是否取消发布课程【${row.mc}】?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          pushBack(id).then(res => {
            this.$message.success("操作成功");
            this.getList();
          });
        })
        .catch();
    },
    /** 查询curriculum列表 */
    getList(flag) {
      this.loading = true;
      listCurriculum(this.queryParams).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fmt == null ||
            list[i].fmt == "" ||
            list[i].fmt == undefined
          ) {
            list[i].fmt = require("@/assets/images/cover.jpg");
          }
        }
        this.curriculumList = list;
        if (this.curriculumList.length > 0) {
          if (flag) {
            this.tableData = [];
          }
          this.curriculumList.forEach((item, index) => {
            this.tableData.push(item);
          });
        }
        this.total = response.total;
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
        id: null,
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(",");
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = [];
      this.queryParams.jd = null;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path: "/etp/curriculum/addOrUpdateCurriculum/",
        query: {
          id: row.id,
        },
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addCurriculum(this.addTags(this.form, this.tagValue)).then(
            response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              //更新一下标签列表
              this.getTags();
            }
          );
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除curriculum编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function() {
          return delCurriculum(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
          //更新一下标签列表
          this.getTags();
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有curriculum数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.exportLoading = true;
          return exportCurriculum(queryParams);
        })
        .then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "KC" }).then(response => {
        this.tagOptions = response.data;
        this.$nextTick(_ => {
          if (this.$refs.tag.$el.clientHeight > 44) {
            this.height = 45 + "px";
            this.tipFlag = true;
          }
        });
      });
    },
  },
};
</script>
<style scoped>
.title-text {
  font-size: 24px;
  color: #333333;
  font-weight: 600;
}
::v-deep .el-tabs__nav-wrap::after {
  /*el-tabs组件隐藏下划线*/
  position: static !important;
}

::v-deep .el-tabs__item {
  font-size: 14px;
  color: #999999;
}
::v-deep .el-tabs__item.is-active {
  color: #0045c5;
}

.col-top {
  height: 24px;
  font-size: 16px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 36px;
}

.col-center {
  height: 24px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #666666;
}

.col-bottom {
  height: 13px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 38px;
}

::v-deep .el-button--primary {
  /* background-color: #0045c5; */
  background-color: #cde2f6;
  color: #0045c5;
  border-radius: 20px;
  border: #cde2f6;
  position: absolute;
  right: 0;
}

/*---------------------------------*/
.desc {
  white-space: nowrap !important;
  text-overflow: ellipsis !important;
  overflow: hidden !important;
}
.tags-div ::v-deep .el-form-item__content {
  width: calc(100% - 100px) !important;
}
.el-checkbox-group {
  margin-left: 30px;
}
</style>
