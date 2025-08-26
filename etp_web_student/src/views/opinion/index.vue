<template>
  <div class="app-container" style="overflow-x: hidden">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="意见描述" prop="yjms">
        <el-input
          v-model="queryParams.yjms"
          placeholder="请输入意见描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="回复内容" prop="hf">
        <el-input
          v-model="queryParams.hf"
          placeholder="请输入回复内容"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
<!--      <el-form-item label="回复状态">-->
<!--        <el-radio-group v-model="queryParams.hfStatus" placeholder="请选择回复状态" clearable size="small">-->
<!--          <el-radio-->
<!--            v-for="dict in hfStatusOptions"-->
<!--            :key="dict.dictValue"-->
<!--            :label="dict.dictValue">-->
<!--            {{dict.dictLabel}}-->
<!--          </el-radio>-->
<!--        </el-radio-group>-->
<!--      </el-form-item>-->
      <el-form-item style="float: right">
        <el-button icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['etp:opinion:add']"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-row v-for="(item,index) in tableData" style="border-bottom: 1px solid #EEEEEE">
      <el-col>
        <el-row>
          <el-col>
            <span class="col-top">
              意见描述：{{item.yjms}}
            </span>
            <el-tag :type="item.hfStatus === '0'?'warning':'success'">{{hfStatusFormat(item)}}</el-tag>
          </el-col>
        </el-row>
        <el-row style="">
          <el-col>
            <span class="col-center">
              回复内容：{{item.hf==null?'未回复':item.hf}}
            </span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;margin-bottom: 10px">
          <el-col :span="20" class="col-bottom">
            <span>
              {{item.createTime}}
            </span>
          </el-col>
          <el-col :span="4">
            <el-button size="medium" @click="preview(item)" type="primary" v-hasPermi="['etp:opinion:query']">查看详情</el-button>
            <el-button v-if="item.hfStatus ==0" type="primary" @click="handleDelete(item)"
                       v-hasPermi="['etp:opinion:remove']">删除</el-button>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :offset="10">
        <p v-if="loading" style="color:#999999;">下拉加载更多数据</p>
        <p v-if="noMore" style="color:#999999;">暂无更多数据</p>
      </el-col>
    </el-row>

    <!-- 添加或修改意见反馈对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="意见描述" prop="yjms">
          <el-input  type="textarea" :rows="2" v-model="form.yjms" placeholder="请输入意见描述" />
        </el-form-item>
        <el-form-item label="附件地址">
          <imageUpload
            ref="imgCompent"
            :limit="6"
            v-model="form.fj" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <div align="center"><el-button @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button></div>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open1" :rules="rules" width="800px" append-to-body>
      <div style="margin: 5px 20px 20px 20px">
        <p style="font-size: 16px">意见描述：{{ opinionForm.yjms }}</p>
        <div style="font-size: 16px">附件名称：
          <div v-for="item in opinionForm.fj" style="margin-left: 78px;margin-bottom: 5px;" >
            <el-link :href="item.url" :underline="true" target="_blank">
<!--              <div><el-image-->
<!--                style="width: 80px; height: 60px;border-radius: 5px"-->
<!--                :src="item.url"-->
<!--                fit="fit"/></div>-->
              <div class="el-icon-picture"> {{ item.name }} </div>
            </el-link>
          </div>


        </div>
        <p style="font-size: 16px">回复内容：{{ opinionForm.hf }}</p>
      </div>
      <div slot="footer" class="dialog-footer">
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
        <div align="center"><el-button @click="cancel">关 闭</el-button></div>
      </div>
    </el-dialog>

    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
  </div>
</template>

<script>
import { listOpinion, getOpinion, delOpinion, addOpinion, updateOpinion } from "@/api/opinion/opinion";
import DelDialog from "@/components/DelDialog"
export default {
  name: "Opinion",
  components: {DelDialog},
  data() {
    return {
      fit:'cover',
      // 遮罩层
      loading: true,
      noMore: false,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 意见反馈表格数据
      opinionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层
      open1: false,
      // 创建时间时间范围
      daterangeCreateTime: [],
      // 回复状态 0 未回复 1已回复字典
      hfStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderByColumn:"update_time",
        isAsc:"desc",
        yjms: null,
        hf: null,
        createTime: null,
        remark: null,
        hfStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        yjms: [
          { required: true, message: "意见描述不能为空", trigger: "blur" }
        ],
      },
      opinionForm:{},
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
      //--------------------
      tableData: [],
    };
  },
  created() {
    this.getList();
    this.getDicts("opinion_status").then(response => {
      this.hfStatusOptions = response.data;
      console.log(response.data)

    });
  },

  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },

  methods: {

    //判断滚动条是否到底
    handleScroll(){
      var scrollTop = Math.floor(document.body.scrollTop || document.documentElement.scrollTop || window.pageYOffset)
      var clientHeight = Math.floor(document.body.clientHeight || document.documentElement.clientHeight)
      var scrollHeight = Math.floor(document.body.scrollHeight || document.documentElement.scrollHeight)

      if(scrollTop + clientHeight >= scrollHeight){
        this.lastScrollTop = scrollTop;
        if(this.tableData.length>=this.total){
          this.content = '暂无更多数据'
          this.noMore = true;
          this.loading = false;
        }else{
          this.content = '下拉加载更多数据'
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum += 1;
          this.getList();
        }
      }
    },

    preview(row){
      this.title = "查看意见及反馈"
      this.open1 = true;
      this.opinionForm = row
      this.opinionForm.fj = JSON.parse(row.fj)
    },
    /** 查询意见反馈列表 */
    getList(flag) {
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listOpinion(this.queryParams).then(response => {
        this.opinionList = response.rows;
        this.total = response.total;
        if(this.opinionList.length>0){
          if(flag){
            this.tableData = []
          }
          this.opinionList.forEach((item,index)=>{
            this.tableData.push(item)
          })
        }
        if(this.tableData.length<=this.total){
          this.noMore = true;
          this.loading = false;
        }
      });
    },
    // 回复状态 0 未回复 1已回复字典翻译
    hfStatusFormat(row, column) {
      return this.selectDictLabel(this.hfStatusOptions, row.hfStatus);
    },
    // 取消按钮
    cancel() {
      this.open1 = false;
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        yjms: null,
        fj: null,
        hf: null,
        xyid: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        hfStatus: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.tableData = [];
      this.getList(true);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加意见反馈";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOpinion(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改意见反馈";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.fj = JSON.stringify(this.$refs.imgCompent.fileList);
          if (this.form.id != null) {
            updateOpinion(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOpinion(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
          this.open = false;
          this.getList();
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let info = {"意见描述":row.yjms,"创建时间":row.createTime}
      this.delInfo = info
      this.delInfoIds = row.id || this.ids;
      this.delVisible = true
      // const ids = row.id || this.ids;
      // this.$confirm('是否确认删除意见反馈编号为"' + ids + '"的数据项?', "警告", {
      //     confirmButtonText: "确定",
      //     cancelButtonText: "取消",
      //     type: "warning"
      //   }).then(function() {
      //     return delOpinion(ids);
      //   }).then(() => {
      //     this.getList();
      //     this.msgSuccess("删除成功");
      //   }).catch(() => {});

    },
    deleteInfo(ids){
      delOpinion(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    }

  }
};
</script>

<style scoped>
.col-bottom {
  height: 13px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 38px;
}
.col-center {
  height: 15px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #666666;
  line-height: 20px;
}
.col-top {
  height: 22px;
  font-size: 16px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 52px;
}

::v-deep .el-button--primary{
  background-color: #0045C5;
  border-radius: 6px;
}
</style>
