<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" class="span4-left">
        <Category ref="category" @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="20" :xs="24" style="border-left: 1px solid #dddddd;" class="span20-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
          <el-form-item label="课程名称" prop="mc">
            <el-input
              v-model="queryParams.mc"
              placeholder="请输入课程名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="课程描述" prop="ms">
            <el-input
              v-model="queryParams.ms"
              placeholder="请输入课程相关描述"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="300"
              show-word-limit
            />
          </el-form-item>
    <!--      <el-form-item label="开放程度" prop="kfcd">
            <el-select v-model="queryParams.kfcd" placeholder="请选择开放程度" clearable size="small">
              <el-option
                v-for="dict in [{dictValue:'0',dictLabel:'完全公开'},{dictValue:'1',dictLabel:'限定用户'}]"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>-->
          <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
            <div style="display: flex">
              <template style="position: relative">
              <el-checkbox  v-model="checkAll" @change="handleCheckAllChange">全部数据</el-checkbox>
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
              v-hasPermi="['etp:curriculum:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['etp:curriculum:remove']"
            >删除
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="curriculumList"
                   @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="30" align="center"/>
          <el-table-column label="封面图" align="center" prop="fmt" width="90">
            <template slot-scope="scope">
              <div class="fmt-item">
                <el-image
                  style="width: 80px; height: 60px;border-radius: 5px"
                  :src="scope.row.fmt==null?require('@/assets/images/cover.jpg'):scope.row.fmt"
                  :fit="fit"/>
                <div :class="['tag-cir',{'fb':scope.row.states == '99'}]">
                  <div>{{ scope.row.states == '0' ? '暂存' : '已发布'}}</div>
                </div>
              </div>

            </template>
          </el-table-column>
          <el-table-column label="课程名称" align="left" prop="mc"/>
          <el-table-column label="课程分类" align="left" prop="categoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="开放程度" align="center" prop="kfcd" width="80">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.kfcd == '0'">完全公开</el-tag>
              <el-tag v-if="scope.row.kfcd == '1'" type="warning">限定用户</el-tag>
              <el-tag v-if="scope.row.kfcd == '2'" type="danger">不公开</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="标签" align="center" prop="tags" width="100">
            <template slot-scope="scope">
              <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
    <!--          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null"-->
    <!--                  v-for="(item,index) in (scope.row.tags||'').split(',')"-->
    <!--                  :key="index"-->
    <!--                  style="margin-right: 3px;margin-bottom:8px">-->
    <!--            {{item}}-->
    <!--          </el-tag>-->
    <!--          <el-tag v-if="scope.row.tags !== '' && scope.row.tags !== null && (scope.row.tags||'').split(',').length > 3"-->
    <!--                  style="margin-right: 3px;margin-bottom:8px">-->
    <!--            ...-->
    <!--          </el-tag>-->
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" width="95" prop="updateTime"/>
          <el-table-column label="课程描述" align="left"  prop="ms" :show-overflow-tooltip="true">
            <template  slot-scope="scope">
                <p class="desc">{{ scope.row.ms }}</p>
            </template>
          </el-table-column>
          <el-table-column label="推荐" align="center" key="sftj" width="60">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.sftj"
                active-value="1"
                inactive-value="0"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
<!--          <el-table-column label="状态" align="center" prop="states" width="80">-->
<!--            <template slot-scope="scope">-->
<!--              <el-tag :type="scope.row.states == '0'?'info':'success'">{{ scope.row.states == '0' ? '暂存' : '已发布' }}</el-tag>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="操作" align="left" class-name="small-padding fixed-width" width="90" fixed="right">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="预览" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-view"
                  @click="rowClick(scope.row)">
                </el-button>
              </el-tooltip>

              <el-tooltip class="item" effect="dark" content="修改" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['etp:curriculum:edit']"
                  v-if="scope.row.states == '0'"
                >
                </el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除" placement="top">
                <el-button
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['etp:curriculum:remove']"
                >
                </el-button>
              </el-tooltip>
<!--              <el-tooltip class="item" effect="dark" content="取消发布" placement="top">-->
<!--                <el-button-->
<!--                  type="text"-->
<!--                  icon="el-icon-s-promotion"-->
<!--                  @click="pushBack(scope.row)"-->
<!--                  v-hasPermi="['etp:curriculum:edit']"-->
<!--                  v-if="scope.row.states != '0'">-->
<!--                </el-button>-->
<!--              </el-tooltip>-->
              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-show="scope.row.states == '99'">
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="pushBack" icon="el-icon-s-promotion"
                                    v-hasPermi="['etp:curriculum:edit']">取消发布</el-dropdown-item>
                  <el-dropdown-item command="analyse" icon="iconfont icon-fenxi"
                                    >分析</el-dropdown-item>
                  <el-dropdown-item command="createQrcg" icon="iconfont icon-ico"
                                    >二维码</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
<!--              <el-tooltip class="item" effect="dark" content="分析" placement="top">-->
<!--                <el-button-->
<!--                  type="text"-->
<!--                  @click="analyse(scope.row)"-->
<!--                  v-if="scope.row.states != '0'"-->
<!--                ><i class="iconfont icon-fenxi"></i>-->
<!--                </el-button>-->
<!--              </el-tooltip>-->
<!--              <el-tooltip class="item" effect="dark" content="二维码" placement="top">-->
<!--                <el-button-->
<!--                  type="text"-->
<!--                  @click="createQrcg(scope.row)"-->
<!--                  v-if="scope.row.states == '99'"-->
<!--                ><i class="iconfont icon-ico"></i>-->
<!--                </el-button>-->
<!--              </el-tooltip>-->
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
    <!-- 添加或修改curriculum对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open" width="1000px"
      :close-on-click-modal="false" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名称" prop="mc">
          <el-input v-model="form.mc" placeholder="请输入课程名称" maxlength="100" show-word-limit/>
        </el-form-item>
        <el-form-item label="封面图" prop="fmt">
          <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fmt"/>
        </el-form-item>

        <el-form-item label="标签" prop="tag">
          <tagsEdit widthSty="100%"
                    :parentValue=tagValue
                    :selectOptions="tagOptions"
                    @selectTags="havaTags">

          </tagsEdit>
        </el-form-item>

        <el-form-item label="开放程度" prop="kfcd">
          <el-radio-group v-model="form.kfcd">
            <el-radio :label="0">完全公开</el-radio>
            <el-radio :label="1">限定用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="课程描述" prop="ms">
          <el-input v-model="form.ms" type="textarea" placeholder="请输入课程描述" maxlength="300" show-word-limit/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" maxlength="300" show-word-limit/>
        </el-form-item>
      </el-form>
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
               @delClose="delVisible = false">

    </DelDialog>
    <el-dialog title="生成二维码"
               :visible.sync="dialogVisibleQrcg"
               width="70%"
               :before-close="closeQrcg"
               :close-on-click-modal="false">
      <div style="height: 550px">
        <Qrcg :data="qrcgForm" @closeQrcg="closeQrcg"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import imageUpload from "@/components/ImageUpload";
import tagsEdit from "@/components/tags/tagsEdit"
import { listCurriculum,delCurriculum,addCurriculum,updateTjzt,exportCurriculum,pushBack } from "@/api/etp/curriculum/curriculum";
import { listTagRelate } from "@/api/etp/tag/tag";
import MoreTagShow from "@/components/MoreTagShow"
import DelDialog from "@/components/DelDialog"
import Qrcg from "@/views/etp/qrcg";
import Category from "./category/index"
import { changeUserStatus } from '@/api/system/user'

export default {
  name: "Curriculum",
  components: {
    imageUpload,
    tagsEdit,
    MoreTagShow,
    DelDialog,
    Qrcg,
    Category
  },
  data() {
    return {
      fit: 'cover',
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      open: false,
      title: '新增课程',
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
        tags:[]
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        mc: [
          {required: true, message: "请输入表名称", trigger: "blur"}
        ],
        fmt: [
          {required: true, message: "请设置封面图", trigger: "blur"}
        ],
        kfcd: [
          {required: true, message: "请设置开放程度", trigger: "blur"}
        ],
      },
      // 标签用到的
      tagValue: [],
      tagOptions:[],
      queryTag:[],

      height:'',
      openFlag:true,
      tipFlag:false,
      isIndeterminate: false,
      checkAll: true,

      //删除参数
      delVisible:false,
      delInfo:{},
      delInfoIds:null,

      //二维码参数
      qrcgUrl:'',
      qrcgModel:'',
      dialogVisibleQrcg:false,
      qrcgForm:{
        title:'',
        value:'',
        size:'',
        startTime:'',
        endTime:''
      },
      catIds:null,
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
    this.getTags();
    this.getConfigKey("cp.qrcg.curriculum").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
  },
  activated() {
    this.getList();
    this.getTags();
  },
  methods: {
    //关闭二维码弹窗
    closeQrcg(){
      this.dialogVisibleQrcg = false;
    },
    //生成二维码
    createQrcg(row) {
      debugger
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = row.mc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=null;
      this.qrcgForm.endTime=null;
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl+this.qrcgModel+row.id+'';
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
    /** 标签相关 */
    getTags() {
      listTagRelate({modelType: "KC"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "pushBack":
          this.pushBack(row);
          break;
        case "analyse":
          this.analyse(row);
          break;
        case "createQrcg":
          this.createQrcg(row);
          break;
        default:
          break;
      }
    },
    /** 取消发布 */
    pushBack(row) {
      let id = row.id;
      this.$confirm(`是否取消发布课程【${row.mc}】?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        pushBack(id).then(res => {
          this.$message.success("操作成功");
          this.getList();
        })
      }).catch();

    },
    // 状态修改
    handleStatusChange(row) {
      let text = row.sftj === "0" ? "取消推荐" : "推荐";
      this.$confirm('确认"' + text + '"【' + row.mc + '】课程吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return updateTjzt({id:row.id,sftj:row.sftj});
      }).then(() => {
        this.msgSuccess(text + "成功");
      }).catch(function() {
        row.sftj = row.sftj == "0" ? "1" : "0";
      });
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
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery(value) {
      this.queryParams.pageNum = 1;
      if(value!=undefined &&typeof(value.catId) == 'number') {
        this.queryParams.categoryId = value.catId;
        this.catIds = value.catIds
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
      this.queryTag = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        path:"/etp/curriculum/addOrUpdateCurriculum/",
        query: {
          catIds:this.catIds
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path:"/etp/curriculum/addOrUpdateCurriculum/" + row.id,
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addCurriculum(this.addTags(this.form,this.tagValue)).then(response => {
            this.msgSuccess("新增成功");
            this.open = false;
            this.getList();
            //更新一下标签列表
            this.getTags();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"课程名称":row.mc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delCurriculum(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },
    rowClick(row) {
      // this.$router.push({
      //   path: "/etp/curriculum/previewCurriculum/",
      //   query: { cid: row.id }
      // });
      window.open('/student/etp/curriculum/previewCurriculum?cid='+ row.id+'&requestFrom='+"admin" )
    },
    //课程分析
    analyse(row){
      this.$router.push({
        path: "/etp/curriculum/analyse/",
        query: { cid: row.id,mc:row.mc }
      });
    }
  }
};
</script>
<style scoped lang="scss">
.desc {
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}
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
