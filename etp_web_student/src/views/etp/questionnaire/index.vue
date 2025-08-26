<template>
  <div class="app-container" style="overflow-x: hidden;">
    <div class="font-size-xl font-color-black" style="margin-bottom: 28px">我的问卷</div>

    <el-form @submit.native.prevent :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="问卷名称" prop="wjmc">
        <el-input
          v-model="queryParams.wjmc"
          placeholder="请输入问卷名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <div style="display: flex">
          <template style="position: relative">
            <el-checkbox v-model="checkAll" @change="handleCheckAllChange" >全部数据</el-checkbox>
            <el-checkbox-group v-model="queryTag" ref="tag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
              <el-checkbox :label="item.id" v-for="item in tagOptions" name="tag" @change="handleQuery">{{item.name}}</el-checkbox>
            </el-checkbox-group>
            <div @click="openFop" v-if="tipFlag"  style="position: absolute;z-index: 9999;right: -5%;width: 100px;top:0;cursor: pointer;color: #1890ff">
              {{ openFlag?'展开':'收起' }}
            </div>
          </template>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div
      v-for="(item, index) in questionnaireList"
      :key="index"
      class="question-item"
    >
      <el-image
        v-if="item && item.fm"
        style="width: 180px; height: 90px; border-radius: 8px;flex-shrink: 0;"
        :src="item.fm"
        fit="cover"
      ></el-image>
      <img src="../../../assets/images/examine_cover.png"  style="width: 180px; height: 90px; border-radius: 8px" v-else />

      <div style="width: 100%; margin-left: 20px; cursor: pointer" >
        <div
          class="font-size-l font-color-black text-title"

        >
          {{ item.wjmc }}
        </div>

        <div style="margin-top: 12px">
          <span class="font-size-m font-color-light-grey">开始时间：</span>
          <span class="font-color-black font-size-m">{{item.beginTime}}</span>

          <span class="font-size-m font-color-light-grey" style="margin-left: 20px">结束时间：</span>
          <span class="font-color-black font-size-m">{{ item.endTime}}</span>
        </div>

        <div style="margin-top: 12px">
          <span class="font-size-m font-color-light-grey" >标签：</span>
          <span class="font-color-black font-size-m">
               <span v-for="(tag,i) in (item.tags||'').split(',').slice(0,3)">
               <el-tag v-if="tag!=''" style="margin-right: 3px;margin-bottom:2px">
                 {{tag}}
               </el-tag>
            </span>
          </span>

          <span class="font-size-m font-color-light-grey" style="margin-left: 20px">状态：
            <span class="font-color-black font-size-m">
              <el-tag v-if="item.status === '未开始'">{{item.status}}</el-tag>
              <el-tag v-if="item.status === '进行中'" type="warning">{{item.status}}</el-tag>
              <el-tag v-if="item.status === '已完成'" type="success">{{item.status}}</el-tag>
              <el-tag v-if="item.status === '已结束'" type="info">{{item.status}}</el-tag>
            </span>
          </span>

        </div>
      </div>

      <el-button class="oper-btn" v-if="item.status=='已完成'||item.status=='已结束'" size="medium" type="primary" @click="getDetail(item)" style=" right: 82px;">查看详情</el-button>
      <el-button class="oper-btn" v-if="item.status!='进行中'" disabled size="medium" type="primary" @click="answer(item)" style=" right: 2px;">填卷</el-button>
      <el-button class="oper-btn"  v-if="item.status==='进行中'" size="medium" type="primary" @click="answer(item)" style=" right: 2px;">填卷</el-button>
    </div>
    <p v-if="loading" class="text-p">下拉加载更多数据</p>
    <p v-if="noMore" class="text-p">暂无更多数据</p>


    <!-- 添加或修改问卷调查对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="问卷名称" prop="wjmc">
          <el-input v-model="form.wjmc" placeholder="请输入问卷名称" />
        </el-form-item>
        <el-form-item label="启用状态 0 正常 1 禁用" prop="qyzt">
          <el-select v-model="form.qyzt" placeholder="请选择启用状态 0 正常 1 禁用">
            <el-option
              v-for="dict in qyztOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="封面图">
          <imageUpload v-model="form.fm"/>
        </el-form-item>
        <el-form-item label="问卷描述" prop="ms">
          <el-input v-model="form.ms" placeholder="请输入问卷描述" />
        </el-form-item>
        <el-form-item label="发布状态 0 暂存 99 已发布" prop="fb">
          <el-input v-model="form.fb" placeholder="请输入发布状态 0 暂存 99 已发布" />
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker clearable size="small"
            v-model="form.beginTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable size="small"
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开放程度0公开，1指定用户，2不公开" prop="kfcd">
          <el-select v-model="form.kfcd" placeholder="请选择开放程度0公开，1指定用户，2不公开">
            <el-option
              v-for="dict in kfcdOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否匿名0否，1公开" prop="sfnm">
          <el-input v-model="form.sfnm" placeholder="请输入是否匿名0否，1公开" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import {listTagRelate} from "@/api/etp/tag/tag";
import { listQuestionnaire,
  getQuestionnaire,
  delQuestionnaire,
  addQuestionnaire,
  updateQuestionnaire,
  exportQuestionnaire
} from "@/api/etp/questionnaire/questionnaire";

export default {
  name: "Questionnaire",
  data() {
    return {
      fit: 'cover',
      // 遮罩层
      loading: true,
      noMore:false,
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
      // 问卷调查表格数据
      questionnaireList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 启用状态 0 正常 1 禁用字典
      qyztOptions: [],
      // 开放程度0公开，1指定用户，2不公开字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wjmc: null,
        qyzt: null,
        ms: null,
        fb: null,
        nr: null,
        beginTime: null,
        endTime: null,
        kfcd: null,
        sfnm: null,
        tags:[]
      },
      queryTag:[],
      tagOptions:[],
      tagValue:[],
      isIndeterminate: true,
      checkAll: true,
      openFlag:true,
      tipFlag:false,
      height:'',
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  activated() {
    this.getList();
  },
  created() {
    this.getList();
    this.getTags();
    this.getDicts("question_status").then(response => {
      this.qyztOptions = response.data;
    });
    this.getDicts("question_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
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
        if (this.questionnaireList.length >= this.total) {
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
    handleCheckAllChange(val) {
      this.queryTag = []
      this.handleQuery()
    },

    handleCheckedTagsChange(value) {
      // let checkedCount = value.length;
      this.checkAll = false
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },

    openFop(){
      this.openFlag = !this.openFlag;
      if (!this.openFlag){ // 展开
        this.height = 'auto'
      }else {
        this.height = 45 + 'px'
      }
      // this.$forceUpdate()
    },

    /** 跳转至答题页面*/
    answer(data) {
      this.$router.push({
        path: "/etp/questionnaire/answer/",
        query: { id: data.id ,tags:data.tags}
      });
    },
    getDetail(row){
      this.$router.push({
        path: "/etp/questionnaire/detail/",
        query: { id: row.id,tags:row.tags}
      });
    },
    /** 查询问卷调查列表 */
    getList() {
      this.loading = true;
      listQuestionnaire(this.queryParams).then(response => {
        let list = response.rows;
        if (list && list.length > 0) {
          for (let i = 0; i < list.length; i++) {
            if(list[i].fm == null || list[i].fm == '' || list[i].fm == undefined){
              list[i].fm = require("@/assets/images/cover.jpg");
            }
          }
          this.questionnaireList = list;
          this.total = response.total;

          if (this.questionnaireList.length >= this.total) {
            //若本次请求列表长度大于等于列表总数 则认为没有更多数据了
            this.noMore = true;
          }
        } else {
        this.noMore = true;
      }
      this.loading = false;
      });
    },
    // 启用状态 0 正常 1 禁用字典翻译
    qyztFormat(row, column) {
      return this.selectDictLabel(this.qyztOptions, row.qyzt);
    },
    // 开放程度0公开，1指定用户，2不公开字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
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
        wjmc: null,
        qyzt: null,
        fm: null,
        ms: null,
        fb: null,
        nr: null,
        beginTime: null,
        endTime: null,
        kfcd: null,
        sfnm: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加问卷调查";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path:"/etp/questionnaire/"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateQuestionnaire(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addQuestionnaire(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除问卷调查编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delQuestionnaire(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有问卷调查数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.exportLoading = true;
          return exportQuestionnaire(queryParams);
        }).then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        }).catch(() => {});
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "WJ"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    }
  }
};
</script>

<style scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 100px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
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

.font-color-black {
  color: #333333;
}
.font-color-grey {
  color: #666666;
}
.font-color-light-grey {
  color: #999999;
}
.question-item {
/*cursor: pointer;*/
  display: flex;
  flex-direction: row;
  position: relative;
  margin-top: 18px;
  padding-bottom: 12px;

  border-bottom: 1px solid hsla(0,0%,91.4%,.6);
}
.text-p {
  text-align: center;
  color: #999999;
  font-size: 12px;
}
::v-deep .el-button--primary {
  background-color: #0045c5;
  border-color: #0045c5;
  border-radius: 6px;
}
.oper-btn{
  position: absolute;
  bottom: 20%;
}
::v-deep .is-disabled{
  background-color: #7d9bd2;
  border-color: #7d9bd2;
}
::v-deep .is-disabled:hover{
  background-color: #7d9bd2;
  border-color: #7d9bd2;
}
</style>
