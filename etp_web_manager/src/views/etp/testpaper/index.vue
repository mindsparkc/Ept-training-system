<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" class="span4-left">
        <Category ref="category" @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="20" :xs="24" style="border-left: 1px solid #dddddd;" class="span20-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="98px">
          <el-form-item label="试卷名称" prop="sjmc">
            <el-input
              v-model="queryParams.sjmc"
              placeholder="请输入试卷名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="组卷策略" prop="zjcl">
            <el-select v-model="queryParams.zjcl" placeholder="请选择组卷策略" clearable size="small">
              <el-option
                v-for="dict in zjclOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="发布状态" prop="fb">
            <el-select v-model="queryParams.fb" placeholder="请选择发布状态" clearable size="small">
              <el-option
                v-for="dict in fbOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
    <!--      <el-form-item label="更新时间" prop="updateTime">-->
    <!--        <el-date-picker clearable size="small"-->
    <!--          v-model="queryParams.updateTime"-->
    <!--          type="date"-->
    <!--          value-format="yyyy-MM-dd"-->
    <!--          placeholder="选择更新时间">-->
    <!--        </el-date-picker>-->
    <!--      </el-form-item>-->
          <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
            <div style="display: flex">
              <template style="position: relative">
                <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>
                <el-checkbox-group ref="tag" v-model="queryTag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
                  <el-checkbox :label="item.id" v-for="(item,index) in tagOptions" name="tag"> {{ item.name }}</el-checkbox>
                </el-checkbox-group>
              <div @click="openFop" v-if="tipFlag"  style="position: absolute;right: -3%;width: 50px;top:0;cursor: pointer;color: #1890ff">
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

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['etp:testpaper:add']"
            >新增</el-button>
          </el-col>

          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['etp:testpaper:remove']"
            >删除</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="testpaperList"
                   @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="封面" align="center" prop="fm"  width="90">
            <template slot-scope="scope">
              <el-image
                style="width: 60px; height: 60px"
                :src="scope.row.fm==null?require('@/assets/images/cover.jpg'):scope.row.fm"
                :fit="fit"
                :preview-src-list="new Array(scope.row.fm)"/>
            </template>
          </el-table-column>
          <el-table-column label="试卷名称" align="center" prop="sjmc" :show-overflow-tooltip="true"/>
          <el-table-column label="试卷分类" align="center" prop="categoryName"/>
          <el-table-column label="组卷策略" align="center" prop="zjcl" :formatter="zjclFormat" />
          <el-table-column label="标签" align="center" prop="tags" >
            <template slot-scope="scope">
              <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
            </template>
          </el-table-column>
          <el-table-column label="发布状态" align="center" prop="fb" :formatter="fbFormat">
            <template slot-scope="scope">
              <el-tag type="success">{{fbFormat(scope.row)}}</el-tag>
            </template>

          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" width="100">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {hh}:{mm}:{ss}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="left" class-name="small-padding fixed-width" fixed="right" width="90">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="预览" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-view"
                  @click="previewPaper(scope.row)"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="取消发布" placement="top">
                <el-button
                  v-if="scope.row.fb != '0'"
                  v-hasPermi="['etp:curriculum:edit']"
                  icon="el-icon-s-promotion"
                  type="text"
                  @click="pushBack(scope.row)"
                >
                </el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="修改" placement="top">
                <el-button
                  v-if="scope.row.fb == '0'"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['etp:testpaper:edit']"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['etp:testpaper:remove']"
                ></el-button>
              </el-tooltip>
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

    <!-- 添加或修改试卷对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="700px" append-to-body>
      <baseform ref="baseForm"></baseform>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

<!--    删除-->
    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
  </div>
</template>

<script>
import imageUpload from "@/components/ImageUpload";
import baseform from "./components/baseForm";
import { listTestpaper, getTestpaper, delTestpaper, addTestpaper, updateTestpaper, pushBack } from "@/api/etp/testpaper/testpaper";
import { listTagRelate } from "@/api/etp/tag/tag";
import DelDialog from "@/components/DelDialog"
import Category from "@/views/etp/category"
import MoreTagShow from "@/components/MoreTagShow"

export default {
  name: "Testpaper",
  components: {
    imageUpload,
    baseform,
    DelDialog,
    Category,
    MoreTagShow
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
      // 试卷表格数据
      testpaperList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷字典
      zjclOptions: [],
      // 发布状态：0-暂存，1-发布字典
      fbOptions: [],
      fit:'fill',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sjmc: null,
        zjcl: null,
        fb: null,
        updateTime: null,
      },
      tagOptions:[],
      queryTag:[],

      height:'',
      openFlag:true,
      tipFlag:false,
      checkAll: true,

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
    };
  },
  watch:{
    queryTag:{
      handler(){
        if (null != this.queryTag) {
          this.queryParams.tags = this.queryTag.join(',')
        }
        this.getList();
      },
      deep:true
    },
    queryParams:{
      handler(){
        this.getList();
      },
      deep:true
    },
  },
  created() {
    this.getList();
    this.getDicts("testpaper_zjcl").then(response => {
      this.zjclOptions = response.data;
    });
    this.getDicts("testpaper_fb").then(response => {
      this.fbOptions = response.data;
    });
   this.getTags()
  },
  activated() {
    this.getList();
    this.getTags();
  },
  methods: {

    //跳转至试卷预览
    previewPaper(row){
      this.$router.push({
        path:"/etp/examination/previewTestPaper/" + row.id,
        query:{ id:row.id }
      });
    },

    handleCheckAllChange(val) {
        this.queryTag = []
    },

    handleCheckedTagsChange(value) {
      this.checkAll = false
      // let checkedCount = value.length;
      // this.checkAll = checkedCount === this.tagOptions.length;
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

    /** 查询试卷列表 */
    getList() {
      this.loading = true;
      listTestpaper(this.queryParams).then(response => {
        this.testpaperList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷字典翻译
    zjclFormat(row, column) {
      return this.selectDictLabel(this.zjclOptions, row.zjcl);
    },
    // 发布状态：0-暂存，1-发布字典翻译
    fbFormat(row, column) {
      return this.selectDictLabel(this.fbOptions, row.fb);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.$refs.baseForm.reset();
    },
    getTags(){
      listTagRelate({modelType:"SJ"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
    /** 搜索按钮操作 */
    handleQuery(categoryId) {
      this.queryParams.pageNum = 1;
      if(typeof(categoryId) == 'number'){
        this.queryParams.categoryId = categoryId
      }
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(',')
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.$refs.category.$refs.tree.setCurrentKey(null)
      this.queryParams.categoryId = null
      this.queryTag=[]
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
      this.$router.push('/etp/testpaper/paper/')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids
      this.$router.push('/etp/testpaper/paper/'+id)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.baseForm.$refs["form"].validate(valid => {
        if (valid) {
          this.form = this.$refs.baseForm.form;

          if (this.form.id != null) {
            updateTestpaper(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.getList()
              this.open = false;
            });
          } else {
            addTestpaper(this.form).then(response => {
              this.msgSuccess("基本信息保存");
              this.open = false;
              this.getList()
              this.$router.push('/etp/testpaper/paper/'+response.data)
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"试卷名称":row.sjmc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delTestpaper(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },
    /** 取消发布 */
    pushBack(row) {
      let id = row.id;
      this.$confirm(`是否取消发布?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        pushBack(id).then(res => {
          this.$message.success("操作成功");
          this.getList();
        })
      }).catch();

    }

  }
};
</script>
<style lang="scss" scoped>
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}
.el-checkbox-group {
  margin-left: 30px;
}

.fmt-item{
  position: relative;
}
.tag-cir {
  width: 44px;
  height: 23px;
  background-color: #f1580b;
  color: #ffffff;
  position: absolute;
  top: 4px;
  border-radius: 3px 4px 4px 0px;
  font-size: 8px;
  left: -1px;
}
.fb{
  background-color: #0b830a!important;
}

.tag-cir::before{
  content: '';
  position: absolute;
  width: 4px;
  height: 4px;
  left: 0;
  bottom: -3px;
  background: #f1580b;
  clip-path: polygon(0 0, 100% 0, 100% 100%); /*三角*/
}
.fb::before{
  background-color: #0b830a!important;
}
</style>
