<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" class="span4-left">
        <Category ref="category"
                  menu="exam"
                  @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="4" :xs="24" class="span20-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
          <el-form-item label="考试名称：" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入考试名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="考试状态：" prop="status">
            <el-radio-group v-model="queryParams.status" @change="handleQuery">
              <el-radio label="">全部</el-radio>
              <el-radio label="1">暂存</el-radio>
              <el-radio label="2">发布</el-radio>
              <el-radio label="3">结束</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item style="padding-left: 20%">
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
            v-hasPermi="['exam:exam:add']"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['exam:exam:edit']"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['exam:exam:remove']"
          >删除</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="examList" @selection-change="handleSelectionChange">
<!--        <el-table-column label="考试编号" align="center" prop="examId" />-->
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="封面" align="center" width="100">
          <template slot-scope="scope">
            <div class="fmt-item">
              <el-image
                :src="scope.row.cover==null?require('@/assets/images/examine_cover.png'):scope.row.cover"
                :fit="fit"
                style="width: 80px; height: 60px;border-radius: 5px"
              />
              <div :class="['tag-cir',{'fb':scope.row.status == 2},{'stop':scope.row.status == 3||scope.row.status == 4}]">
                <div v-if="scope.row.status == 1">暂存</div>
                <div v-if="scope.row.status == 2">发布</div>
                <div v-if="scope.row.status == 3">结束</div>
                <div v-if="scope.row.status == 4">强制结束</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="考试名称" align="center" prop="name" show-overflow-tooltip width="150"/>
        <el-table-column label="考试描述" align="center" prop="content" show-overflow-tooltip width="150"/>
        <el-table-column label="开放策略" align="center" prop="openType">
          <template slot-scope="scope">
            <span v-if="scope.row.openType == '1'">公开</span>
            <span v-else-if="scope.row.openType == '2'">限定部门</span>
            <span v-else-if="scope.row.openType == '3'">限定岗位</span>
            <span v-else-if="scope.row.openType == '4'">限定成员</span>
            <span v-else-if="scope.row.openType == '5'">口令密码</span>
          </template>
        </el-table-column>
<!--        <el-table-column label="考试口令密码" align="center" prop="pwdFlag"/>-->
<!--        <el-table-column label="考试状态" align="center" prop="status" width="100">-->
<!--          <template slot-scope="scope">-->
<!--            <el-tag type="info" v-if="scope.row.status == 1">暂存</el-tag>-->
<!--            <el-tag type="success" v-else-if="scope.row.status == 2">发布</el-tag>-->
<!--            <el-tag type="danger" v-else-if="scope.row.status == 3">结束</el-tag>-->
<!--            <el-tag type="danger" v-else-if="scope.row.status == 4">强制结束</el-tag>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="考试发布时间" align="center" prop="publishTime">
          <template slot-scope="scope">
            <span>{{ scope.row.publishTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="考试开始时间" align="center" prop="startTime">
          <template slot-scope="scope">
            <span>{{ scope.row.startTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="考试结束时间" align="center" prop="endTime">
          <template slot-scope="scope">
            <span>{{ scope.row.endTime }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column label="备注" align="center" prop="remark" />-->
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="dark" content="详情" placement="top-start">
              <el-button
                         size="mini"
                         type="text"
                         icon="el-icon-view"
                         @click="handleLook(scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="修改" placement="top-start">
              <el-button v-if="scope.row.status=='1'"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['exam:exam:edit']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="取消发布" placement="top-start">
              <el-button v-if="scope.row.status=='2'"
                size="mini"
                type="text"
                icon="el-icon-s-promotion"
                @click="qxStatus(scope.row)"
                v-hasPermi="['exam:exam:edit']"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="评卷" placement="top-start">
              <el-button v-if="scope.row.status == '2'"
                         size="mini"
                         type="text"
                         icon="el-icon-document-checked"
                         @click="handleSelectStudent(scope.row)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['exam:exam:remove']"
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

      <DelDialog :delVisible="delVisible"
                 :delInfo="delInfo"
                 :delInfoId="delInfoIds"
                 @deleteInfo = "deleteInfo"
                 @delClose="delVisible = false">

      </DelDialog>

    <el-dialog
      v-if="dialogTableVisible == true"
      :visible.sync="dialogTableVisible"
      :close-on-click-modal="false" width="1000px">
            <el-form>
              <el-form-item label="评卷状态:">
                <el-button-group>
                  <el-button @click="handleSelectStudent(0)">未评卷</el-button>
                  <el-button @click="handleSelectStudent('1,4,9')">已评卷</el-button>
                </el-button-group>
<!--                <el-button @click="handleExport()" style="margin-left: 600px" type="primary">导出成绩</el-button>-->
              </el-form-item>
            </el-form>
            <el-table :data="gridData" style="margin-bottom: 30px;">
              <el-table-column property="userId" align="center" label="学员ID"/>
              <el-table-column property="nickName" align="center" label="学员姓名" />
              <el-table-column property="userScore" align="center" label="学员成绩" sortable />
              <el-table-column property="rate" align="center" label="正确率" sortable/>
              <el-table-column property="status" align="center" label="评卷状态">
                <template slot-scope="scope">
                  <el-tag :type="(scope.row.status==0?'info':'success')">{{ scope.row.status==0?'未评卷':'已评卷' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column align="center" label="考试开始时间" width="170px">
                <template slot-scope="scope">
                  <span>{{scope.row.startTime}}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="考试结束时间" width="170px">
                <template slot-scope="scope">
                  <span>{{ scope.row.endTime}}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.status==0"
                    icon="el-icon-document-checked"
                    size="mini"
                    type="text"
                    @click="toPaperInfo(scope.row)"
                  >去评卷</el-button>
                  <el-button v-if="scope.row.status!=0"
                             icon="el-icon-document-checked"
                             size="mini"
                             type="text"
                             @click="toPaperInfo(scope.row)"
                  >去评卷</el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              :autoScroll="false"
              v-show="studentTotal > 0"
              :limit.sync="studentParams.pageSize"
              :page.sync="studentParams.pageNum"
              :total="studentTotal"
              @pagination="gridDatas()"
            />
    </el-dialog>

    <!--选择试卷-->
    <paperList
      ref="paper"
      @confirm="confirm"></paperList>
  </div>
</template>

<script>
import {
  addExam,
  delExam,
  getExam,
  listExam,
  updateExam,
  updateStatus,
  selectStudentList,
} from '@/api/exam/exam'
  import Category from "@/views/category/index";
  import Template from '@/views/etp/certificateTemplate/index'
  import DelDialog from "@/components/DelDialog"
  import paperList from "@/views/exam/exam/components/paperList";

  export default {
    name: "Exam",
    components:{
      Template,
      Category,
      DelDialog,
      paperList
    },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      fit:'cover',
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
      // 考试信息表格数据
      examList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        category: null,
        name:null,
        status:"",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        paperId: [
          { required: true, message: "试卷编号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "考试状态不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "删除标记不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建者不能为空", trigger: "blur" }
        ],
        updateBy: [
          { required: true, message: "更新者不能为空", trigger: "blur" }
        ]
      },
      delVisible:false,
      delInfo:{},
      delInfoIds:null,
      catIds:[],

      //评卷参数
      dialogTableVisible:false,
      // 查询参数
      studentParams: {
        pageNum: 1,
        pageSize: 10,
        userId:null,
        examId:null,
        range:[],
        status:null,
      },
      gridData:[],   // 学员考试信息数据
      studentTotal:0,
      buttonData:false
    };
  },
  created() {
    this.getList();
  },
  activated() {
    this.getList();
  },
    watch:{
      dialogTableVisible(newValue,oldValue){
        if(!newValue){
          this.studentParams = {
            pageNum: 1,
            pageSize: 10,
            userId:null,
            examId:null,
            range:[],
            status:null,
          }
        }
      }
    },
    methods: {
      //跳转至评卷页面
      toPaperInfo(data){
        if(data.endTime!=null&data.endTime!=''){
          let router = this.$router.resolve({
            path: "/record-paper/record",
            query: { recordId: data.recordId}
          })
          window.open(router.href,'_blank')
        }else{
         this.$message.error("未交卷的考试无法进行评卷！")
        }
      },

      //获取分页信息
      gridDatas(){
        this.selectStudent(this.studentParams);
      },

      //获取学员考试列表
      handleSelectStudent(val){
        if(val.examId!=undefined){
          this.studentParams.examId = val.examId
        }else{
          this.studentParams.status = val
        }
        this.selectStudent(this.studentParams);
      },

      selectStudent(val){
        selectStudentList(val).then(res => {
          console.log(res);
          this.dialogTableVisible = true;
          this.gridData = res.rows;
          this.studentTotal = res.total;
        })
      },

      //取消发布
      qxStatus(row){
        // let flag = false
        // if(row.startTime!=null){
        //   let startTime = new Date(row.startTime).getTime();
        //   let now = new Date().getTime();
        //   if(startTime<now){
        //     this.$message.error("考试已经开始，无法取消发布！")
        //   }else{
        //     flag = true
        //   }
        // }else{
        //   //当前考试已经有学员考试记录，则无法取消发布
        // }
        //当前考试已经有学员考试记录，则无法取消发布
        let flag = true
        if(flag){
          this.$confirm('是否取消发布本条考试?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let data = { examId: row.examId, status: 1 }
            return updateStatus(data)
          }).then(() =>{
            this.getList();
            this.$message.success("取消成功！")
          }).catch(() => {});
        }
      },

      /** 查询考试信息列表 */
      getList() {
        this.loading = true;
        listExam(this.queryParams).then(response => {
          this.examList = response.rows;
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
          examId: null,
          name: null,
          content: null,
          categoryId:null,
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery(value) {
        if (typeof(value) == 'object') {
          this.queryParams.categoryId = value.catId;
          this.catIds = value.catIds
        }
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.categoryId = null
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.examId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.$refs.paper.getPapers()
      },
      confirm(val) {
        this.$router.push({
          path:"/exam-edit/edit/",
          query:{ sjData: val }
        });
      },
      /** 详情按钮操作 */
      handleLook(row) {
        const id = row.examId || this.ids
        this.$router.push({
          path:"/exam-edit/edit/",
          query:{examId:id,paperId:row.paperId,flag:0,t: Date.now()}
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        const id = row.examId || this.ids
        this.$router.push({
          path:"/exam-edit/edit/",
          query:{examId:id,paperId:row.paperId,flag:1,t: Date.now()}
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.examId != null) {
              updateExam(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addExam(this.form).then(response => {
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
        this.delInfoIds = row.examId || this.ids;
        if(!row.examId){
          this.delInfo = {}
        }else{
          let info = {"考试":row.name,"创建时间":row.createTime}
          this.delInfo = info
        }
        this.delVisible = true
      },
      deleteInfo(ids){
        delExam(ids).then(response => {
          this.msgSuccess("删除成功");
          this.delVisible = false;
          this.getList();
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有考试信息数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            this.exportLoading = true;
            return exportExam(queryParams);
          }).then(response => {
            this.download(response.msg);
            this.exportLoading = false;
        }).catch(() => {});
      },


      getPapers(){
        this.sj_open = true;
        this.handleQuery();
      },
    }
};
</script>

<style scoped lang="scss">
.tag-cir {
  width: 44px;
  height: 23px;
  background-color: #f1580b;
  color: #ffffff;
  position: absolute;
  top: 10px;
  border-radius: 3px 4px 4px 0px;
  font-size: 8px;
  left: 6px;
}

.fb{
  background-color: #0b830a!important;
}

.stop{
  background-color: #909399!important;
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
