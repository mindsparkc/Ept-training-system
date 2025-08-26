<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24" class="span4-left">
        <Category ref="category" @clickCategory="handleQuery"></Category>
      </el-col>
      <el-col :span="20" :xs="24" style="border-left: 1px solid #dddddd;" class="span20-right">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="90px">
          <el-form-item label="考试名称" prop="mc">
            <el-input
              v-model="queryParams.mc"
              clearable
              placeholder="请输入考试名称"
              size="small"
              @keyup.enter.native="handleQuery"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="发布状态" prop="fb">
            <el-select v-model="queryParams.fb" clearable placeholder="请选择发布状态" size="small">
              <el-option
                v-for="dict in fbOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="开放程度" prop="kfcd">
            <el-select v-model="queryParams.kfcd" clearable placeholder="请选择开放程度" size="small">
              <el-option
                v-for="dict in kfcdOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item class="tags-div" label="标签" prop="queryTag" style="width: calc(100% - 256px);">
            <div style="display: flex">
              <template style="position: relative">
                <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
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
            <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              v-hasPermi="['etp:examination:add']"
              icon="el-icon-plus"
              plain
              size="mini"
              type="primary"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              v-hasPermi="['etp:examination:remove']"
              :disabled="multiple"
              icon="el-icon-delete"
              plain
              size="mini"
              type="danger"
              @click="handleDelete"
            >删除</el-button>
          </el-col>

        </el-row>

        <el-table v-loading="loading" :data="examinationList"
                   @selection-change="handleSelectionChange">
          <el-table-column align="center" type="selection" width="45" />
<!--          <el-table-column :show-overflow-tooltip="true"  width="100" align="center" label="考试编号" prop="id" sortable/>-->
          <el-table-column align="center" label="封面" prop="fm" width="90">
            <template slot-scope="scope">
              <div class="fmt-item">
                <el-image
                  :src="scope.row.fm==null?require('@/assets/images/examine_cover.png'):scope.row.fm"
                  fit="cover"
                  style="width: 80px; height: 80px;"
                />
                <div :class="['tag-cir',{'fb':scope.row.fb == '99'}]">
                  <div>{{ scope.row.fb == '0' ? '暂存' : '已发布'}}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="left" label="考试名称" prop="mc" :show-overflow-tooltip="true"  />

          <el-table-column label="考试分类" align="left" prop="categoryName" />
          <el-table-column align="center" label="标签" prop="tags" width="80">
            <template slot-scope="scope">
              <MoreTagShow :tag-list="(scope.row.tags||'').split(',')" :limit="2"></MoreTagShow>
            </template>
          </el-table-column>
<!--          <el-table-column :formatter="fbFormat" align="center" label="发布状态" prop="fb" >-->
<!--            <template slot-scope="scope">-->
<!--              <el-tag :type="scope.row.fb == '0' ? 'info' : 'success'">{{fbFormat(scope.row)}}</el-tag>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column :formatter="kfcdFormat" align="center" label="开放程度" prop="kfcd" >
            <template slot-scope="scope">
              <el-tag v-if="scope.row.kfcd == '0'">完全公开</el-tag>
              <el-tag v-if="scope.row.kfcd == '1'" type="warning">限定用户</el-tag>
              <el-tag v-if="scope.row.kfcd == '2'" type="danger">不公开</el-tag>
            </template>
    <!--        <template slot-scope="scope">
              <el-tag :type="scope.row.kfcd == '0' ? 'primary' : 'warning'">{{kfcdFormat(scope.row)}}</el-tag>
            </template>-->
          </el-table-column>
          <el-table-column align="center" label="考试开始时间" prop="kskssj" width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.kskssj }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="考试结束时间" prop="ksjssj"  width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.ksjssj }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="已判卷/已考试" prop="kszt"  width="110"/>
    <!--      <el-table-column :show-overflow-tooltip="true" align="center" label="考试描述" prop="ms"/>-->
          <el-table-column align="left" class-name="small-padding fixed-width" label="操作" fixed="right" width="90">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="试卷预览" placement="top">
                <el-button
                  icon="el-icon-view"
                  type="text"
                  @click="previewPaper(scope.row)"
                ></el-button>
              </el-tooltip>
              <span v-show="scope.row.fb=='0'||new Date(scope.row.kskssj).getTime()>new Date().getTime()" style="margin-left: 10px">
               <el-tooltip class="item" effect="dark" content="取消发布" placement="top">
                <el-button
                  v-if="scope.row.fb == '99'"
                  v-hasPermi="['etp:examination:edit']"
                  icon="el-icon-s-promotion"
                  type="text"
                  @click="handleCancelFb(scope.row)"
                ></el-button>
               </el-tooltip>
                <el-tooltip class="item" effect="dark" content="修改" placement="top">
                  <el-button
                    v-if="scope.row.fb == '0'"
                    v-hasPermi="['etp:examination:edit']"
                    icon="el-icon-edit"
                    type="text"
                    @click="handleUpdate(scope.row)"
                  ></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="删除" placement="top">
                  <el-button
                    v-hasPermi="['etp:examination:remove']"
                    icon="el-icon-delete"
                    type="text"
                    @click="handleDelete(scope.row)"
                  ></el-button>
                </el-tooltip>
              </span>
              <el-tooltip class="item" effect="dark" content="详情" placement="top" style="margin-left: 10px">
                <el-button
                  v-if="scope.row.fb !== '0'"
                  v-hasPermi="['etp:examination:edit']"
                  icon="el-icon-tickets"
                  type="text"
                  @click="handleScore(scope.row)"
                ></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="分析" placement="top">
                <el-button
                  v-if="scope.row.kszt!=='0/0'"
                  type="text"
                  @click="showExamAnalysis(scope.row)"
                ><i class="iconfont icon-fenxi"></i></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="二维码" placement="top">
                <el-button
                  v-if="scope.row.fb =='99'"
                  type="text"
                  @click="createQrcg(scope.row)"
                ><i class="iconfont icon-ico"></i></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :limit.sync="queryParams.pageSize"
          :page.sync="queryParams.pageNum"
          :total="total"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 查看成绩   -->
    <el-dialog
      v-if="dialogTableVisible == true"
      :visible.sync="dialogTableVisible"
      :close-on-click-modal="false" width="1000px">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="成绩列表" name="first">
          <template v-if="First">
            <el-form>
              <el-form-item label="评卷状态:">
<!--                <el-radio-group v-model="pjzt" size="mini">-->
<!--                  <el-radio-button label="0" @click.native="getInfo(0)">未评卷</el-radio-button>-->
<!--                  <el-radio-button label="1" @click.native="getInfo(1)">已评卷</el-radio-button>-->
<!--                </el-radio-group>-->
                <el-button-group>
                  <el-button @click="getInfo('')">未评卷</el-button>
                  <el-button @click="getInfo(1)">已评卷</el-button>
                </el-button-group>
                <el-button @click="handleExport()" style="margin-left: 600px" type="primary">导出成绩</el-button>
              </el-form-item>
            </el-form>
            <el-table :data="gridData" style="margin-bottom: 30px;">
              <el-table-column property="xyid" align="center" label="学员ID"/>
              <el-table-column property="userName" align="center" label="学员姓名" />
              <el-table-column property="score" align="center" label="学员成绩" sortable />
              <el-table-column property="rate" align="center" label="正确率" sortable :formatter="rateFormate"/>
              <el-table-column property="pjzt" align="center" label="评卷状态">
                <template slot-scope="scope">
                  <span>{{scope.row.pjzt==1?'已判卷':'未判卷'}}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="考试开始时间" width="170px">
                <template slot-scope="scope">
                  <span>{{scope.row.createTime}}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="考试结束时间" width="170px">
                <template slot-scope="scope">
                  <span>{{ scope.row.handTime}}</span>
                </template>
              </el-table-column>
              <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                  <el-button
                    v-hasPermi="['etp:examination:edit']"
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
              v-show="gridData.length > 0"
              :limit.sync="gridForm.pageSize"
              :page.sync="gridForm.pageNum"
              :total="gridData2.length"
              @pagination="getGridData"
            />
          </template>
        </el-tab-pane>
        <el-tab-pane label="缺考人员" name="second">
          <template v-if="Second">
            <el-table :data="gridDataQk" style="margin-bottom: 30px;">
              <el-table-column property="userId" align="center" label="学员ID"/>
              <el-table-column property="userName" align="center" label="学员账户"/>
              <el-table-column property="nickName" align="center" label="学员昵称"/>
              <el-table-column property="sex" align="center" label="性别">
                <template slot-scope="scope">
                  {{scope.row.sex == 0?'男':'女'||'未知'}}
                </template>
              </el-table-column>
              <el-table-column property="phonenumber" align="center" label="联系电话" width="160px"/>
              <el-table-column property="email" align="center" label="邮箱" width="200px"/>
            </el-table>
            <pagination
              :autoScroll="false"
              v-show="gridDataQk.length > 0"
              :limit.sync="gridForm2.pageSize"
              :page.sync="gridForm2.pageNum"
              :total="gridDataQk2.length"
              @pagination="getGridDataQk"
            />
          </template>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
<!--    删除-->
    <DelDialog :delVisible="delVisible"
               :delInfo="delInfo"
               :delInfoId="delInfoIds"
               @deleteInfo = "deleteInfo"
               @delClose="delVisible = false"></DelDialog>
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
import {
  listExamination,
  getMissExam,
  delExamination,
  updateFb,
  addExamination,
  updateExamination,
  updateTExaminationFb,
  getExaminationInfo,
  exportExamination
} from '@/api/etp/examination/examination'
import { listTagRelate } from "@/api/etp/tag/tag";
import DelDialog from "@/components/DelDialog"
import moment from 'moment'
import Qrcg from "@/views/etp/qrcg";
import Category from "@/views/etp/category"
import MoreTagShow from "@/components/MoreTagShow"

export default {
  name: "Examination",
  components: {
    DelDialog,
    Qrcg,
    Category,
    MoreTagShow
  },
  data() {
    return {
      gridForm:{
        pageNum: 1,
        pageSize: 10,
      },
      gridData:[],   // 页面使用的数据
      gridData2:[],  // 总数据
      gridDataQk:[],
      gridDataQk2:[],
      gridForm2:{
        pageNum: 1,
        pageSize: 10,
      },
      dialogTableVisible:false,
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
      // 考试表格数据
      examinationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 发布状态：0-暂存，1-发布字典
      fbOptions: [],
      // 开放程度：完全公开-0，限定用户-1字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        mc: null,
        ms: null,
        fm: null,
        fb: null,
        kslj: null,
        kfcd: null,
        sjid: null,
        kskssj: null,
        ksjssj: null,
        djsc: null,
        dtcs: null,
        fsdj: null,
        zfs: null,
      },
      // 表单参数
      form: {},
      tagOptions:[],
      queryTag:[],

      height:'',
      openFlag:true,
      tipFlag:false,

      checkAll: true,
      xqId: '',//存储考试id，用于查询学员考试信息及缺考学员信息
      pjzt: '',
      //tabs标签渲染参数
      activeName: 'first',
      First: true,
      Second: false,

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
  activated() {
    this.getList();
  },
  created() {
    this.getList();
    this.getDicts("examination_fb").then(response => {
      this.fbOptions = response.data;
    });
    this.getDicts("examination_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
    this.getTags()
    this.getConfigKey("cp.qrcg.exam").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
  },
  methods: {

    //关闭二维码弹窗
    closeQrcg(){
      this.dialogVisibleQrcg = false;
    },
    //生成二维码
    createQrcg(row) {
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = row.mc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=row.kskssj;
      this.qrcgForm.endTime=row.ksjssj;
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl+this.qrcgModel+row.id+'';
    },

    /** 导出按钮操作 */
    handleExport() {
      this.$confirm('是否确认导出所有考试成绩数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.exportLoading = true;
        return exportExamination(this.xqId);
      }).then(response => {
        this.download(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    },

    //跳转至考试分析
    showExamAnalysis(row){
      console.log(row);
      this.$router.push({
      path:"/etp/examinationScoreAnalysis/AnalysisInfo/",
        query:{ id:row.id,sjid:row.sjid }
      });
    },
    //跳转至试卷预览
    previewPaper(row){
      this.$router.push({
        path:"/etp/examination/previewTestPaper/" + row.sjid,
        query:{ id:row.sjid }
      });
    },

    //跳转判卷页面
    toPaperInfo(row){
      // console.log(row);
      this.dialogTableVisible = false;
      this.$router.push("/etp/examination/paperInfo/" + row.id);
    },

    //tabs标签渲染方法
    handleClick(tab){
      if(tab != undefined && tab.name==='first'){
        this.First=true;
        this.Second=false;
      }else{
        this.First=false;
        this.Second=true;
        this.getMissExamByStu();
      }
    },
    /** 获取缺考人员信息 */
    getMissExamByStu(){
      getMissExam(this.xqId).then(res => {
        console.log(res);
        this.gridDataQk = [];
        this.gridDataQk2 = res.data.sysUsers;
        console.log(this.gridDataQk2)
        for (let i = 0; i < (this.gridForm2.pageSize > this.gridDataQk2.length
          ? this.gridDataQk2.length:this.gridForm2.pageSize); i++) {
          console.log(this.gridDataQk2[i]);
          this.gridDataQk.push(this.gridDataQk2[i]);
          console.log(this.gridDataQk);
        }
      })
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
    //处理学员考试详情
    getGridData(){
      let startIndex = (this.gridForm.pageNum - 1) * this.gridForm.pageSize;
      let endIndex =
        (this.gridForm.pageSize * this.gridForm.pageNum < this.gridData2.length)
          ? this.gridForm.pageSize * this.gridForm.pageNum:this.gridData2.length;
      this.gridData = [];
      for (let i = startIndex; i < endIndex; i++)
          this.gridData.push(this.gridData2[i]);
    },
    //处理缺考学员信息
    getGridDataQk(){
      let startIndex = (this.gridForm2.pageNum - 1) * this.gridForm2.pageSize;
      let endIndex =
        (this.gridForm2.pageSize * this.gridForm2.pageNum < this.gridDataQk2.length)
          ? this.gridForm2.pageSize * this.gridForm2.pageNum:this.gridDataQk2.length;
      this.gridDataQk = [];
      for (let i = startIndex; i < endIndex; i++)
        this.gridDataQk.push(this.gridDataQk2[i]);
    },

    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExamination(this.queryParams).then(response => {
        this.examinationList = response.rows;
        console.log(response);
        this.total = response.total;
        this.loading = false;
      });
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

    getTags(){
      listTagRelate({modelType:"KS"}).then(response => {
        this.tagOptions = response.data;
        this.$nextTick( _=> {
          if (this.$refs.tag.$el.clientHeight > 44){
            this.height = 45 + 'px';
            this.tipFlag = true;
          }
        })
      });
    },
    // 发布状态：0-暂存，1-发布字典翻译
    fbFormat(row, column) {
      return this.selectDictLabel(this.fbOptions, row.fb);
    },
    // 开放程度：完全公开-0，限定用户-1字典翻译
    kfcdFormat(row, column) {
      return this.selectDictLabel(this.kfcdOptions, row.kfcd);
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
      this.queryTag = []
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
      this.$router.push({
        path:"/etp/examination/editExamination",
        query:{ t: Date.now() }
      });
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids
      this.$router.push({
        path:"/etp/examination/editExamination/",
        query:{id:id,sjid:row.sjid,t: Date.now()}
      });
    },


    /** 查看成绩*/
    handleScore(row){
      this.xqId = row.id;
      this.getInfo(this.pjzt);
    },
    //获取学员考试信息
    getInfo(val){
      getExaminationInfo(this.xqId,val).then(res => {
        console.log(res);
        this.dialogTableVisible = true;
        this.gridData = [];
        this.gridData2 = res.data.examinationInfoWithCps;
        for(let i =0;i<this.gridData2.length;i++){
          let time = this.gridData2[i].handTime
          let handtime = this.$moment(time).format('YYYY-MM-DD HH:mm:ss');
          this.gridData2[i].handTime = handtime;
        }
        for (let i = 0; i < (this.gridForm.pageSize > this.gridData2.length
                              ? this.gridData2.length:this.gridForm.pageSize); i++) {
          this.gridData.push(this.gridData2[i]);
        }
      })
    },

    rateFormate(row){
      return  (row.rate * 100).toFixed(0) + "%"
      // parseFloat(row.rate) * 100 + "%"
      // (Number(row.rate).toFixed(2))
    },



    handleCancelFb(row){
      this.$confirm(`是否取消发布?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.fb = '0'
        updateFb(row).then(res => {
          this.$message.success("操作成功");
          this.getList();
        })
      }).catch();

    },
    // /** 删除按钮操作 */
    // handleDelete(row) {
    //   const ids = row.id || this.ids;
    //   this.$confirm('是否确认删除考试编号为【' + ids + '】的数据项?', "警告", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   }).then(function() {
    //     return delExamination(ids);
    //   }).then(response => {
    //     this.getList();
    //     this.msgSuccess(response.msg);
    //   }).catch(() => {});
    // },

    /** 删除按钮操作 */
    handleDelete(row) {
      this.delInfoIds = row.id || this.ids;
      if(!row.id){
        this.delInfo = {}
      }else{
        let info = {"考试标题":row.mc,"创建时间":row.createTime}
        this.delInfo = info
      }
      this.delVisible = true
    },
    deleteInfo(ids){
      delExamination(ids).then(response => {
        this.msgSuccess("删除成功");
        this.delVisible = false;
        this.getList();
      });
    },
  }
};
</script>
<style lang="scss" scoped lang="scss">
.tags-div ::v-deep .el-form-item__content{
  width: calc(100% - 120px)!important;
}

.el-checkbox-group {
  margin-left: 30px;
}

::v-deep .el-table .cell{
  white-space: normal;
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
