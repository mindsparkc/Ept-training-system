<template>
  <div class="app-container app-container-custome">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-steps :active="active" finish-status="success" simple>
          <el-step>
            <slot slot="title">第一步：选择试卷</slot>
          </el-step>
          <el-step>
            <slot slot="title">第二步：发布考试</slot>
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
<!--    试卷基本信息-->
    <div v-show="active==0" style="margin-top: 30px">
      <el-button plain size="small" type="primary" @click="getPaper">选择试卷</el-button>
      <div v-show="selectSj.mc!=null" class="examin-div">
        <div class="paper-title">
          <span>{{ selectSj.mc }}（{{zjclFormat(selectSj)}}）</span>
          <div class="paper-btn">
            <el-button type="text" @click="previewPaper()">试卷预览</el-button>
<!--            <el-button type="text" @click="editPaper">编辑试卷</el-button>-->
          </div>
        </div>
        <div class="paper-content">
          <div>试卷分类：{{ selectSj.categoryName }}</div>
          <div>总分：{{ selectSj.fzNum }}</div>
          <div>总题数：{{ selectSj.count }}</div>
          <div v-if="selectSj.zjcl=='2'">取题范围：
            <span v-for="(item,key) in selectSj.qtfw" style="margin-right: 10px;">{{ selectDictLabel(lxOptions,key) }} ({{item.length}}) ,</span>
          </div>
          <div>创建人：{{ selectSj.createBy }}</div>
          <div>创建时间：{{ parseTime(selectSj.createTime) }}</div>
          <div>更新时间：{{ parseTime(selectSj.updateTime) }}</div>
          <div style="margin-top: 10px;margin-bottom: 10px">
            <!--            <span v-for="(item,index) in selectSj.tx" style="margin-right: 10px;">-->
            <!--              {{  selectDictLabel(lxOptions,item.key) }}：{{ item.value }}个,共{{ item.fs }}分;-->
            <!--            </span>-->
            <el-row>
              <el-col :span="2" v-for="(item,index) in selectSj.tx">
                <div class="div-head"> {{  selectDictLabel(lxOptions,item.key) }}({{ item.value }})</div>
                <div > {{ item.fs }}分</div>
              </el-col>

            </el-row>
          </div>
        </div>
      </div>
    </div>
<!--    第二步 选择试题-->
    <div v-show="active==1" style="margin-top: 20px;">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="考试名称：" prop="mc">
          <el-input v-model="form.mc" placeholder="请输入考试名称" style="width: 400px" maxlength="100" show-word-limit/>
        </el-form-item>
        <el-form-item label="考试标签：" prop="tagValue">
          <tagsEdit :parentValue="tagValue"
                    :selectOptions="tagOptions"
                    widthSty="400px"
                    @selectTags="havaTags">
          </tagsEdit>
        </el-form-item>
        <el-form-item label="考试分类" class="category" prop="categoryId">
          <treeselect v-model="form.categoryId" :options="categoryOptions"
                      :normalizer="normalizer" placeholder="选择分类"
                      style="width: 400px"
          />
        </el-form-item>
        <el-form-item label="考试时间：" prop="dateRange">
          <el-date-picker
            v-model="form.dateRange"
            :picker-options="pickerOptions"
            value-format="yyyy-MM-dd HH:mm:ss"
            end-placeholder="结束日期"
            range-separator="至"
            start-placeholder="开始日期"
            type="datetimerange">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="推送时间：" prop="pushTime">
          <el-radio-group v-model="pushTimeSet" >
            <el-radio :label="0">立即推送</el-radio>
            <el-radio :label="1">自定义推送时间</el-radio>
            <span v-if="pushTimeSet==1">
              <el-date-picker
                value-format="yyyy-MM-dd HH:mm:ss"
                v-model="form.pushTime"
                type="datetime"
                placeholder="选择推送时间">
            </el-date-picker>
            <el-tooltip content="注意：推送时间需不能晚于考试开始时间" placement="right-start" effect="light">
              <i class="el-icon-question"></i>
            </el-tooltip>
            </span>

          </el-radio-group>

        </el-form-item>
        <el-form-item label="答卷时长：" prop="djsc">
<!--          <el-input v-model="form.djsc" placeholder="请输入" style="width: 80px"/> 分钟-->
          <el-input-number v-model="form.djsc" :min="1" label="答卷时长"  style="width: 128px"></el-input-number> 分钟
        </el-form-item>
        <el-form-item label="答题次数：" prop="dtcs">
          <el-radio-group v-model="dtcsRadio">
            <el-radio :label="0">无限次</el-radio>
            <el-radio :label="1">
              有限次
              <span v-show="dtcsRadio==1" style="color: #1e1e1e">
                 <el-input v-model.number="form.dtcs" placeholder="请输入" style="width:80px"/> 次
              </span>
            </el-radio>
          </el-radio-group>

        </el-form-item>
        <el-form-item label="分数等级：" prop="fsdj" class="fsdj">
          <div v-for="(item,index) in fsdj_tmp" class="dj-div">
            成绩大于 <el-input-number v-model="item.value[0]" :min="0" label="分数"></el-input-number> 分，为 <el-input class="dj-input" v-model="item.key" placeholder="分数等级" />

            <i v-show="index<fsdj_tmp.length && index>0" class="el-icon-remove-outline" @click="del_dj(index)"></i>
          </div>
          <el-button plain size="small" style="width: 420px" type="primary" @click="add_dj()">+ 新增等级</el-button>
        </el-form-item>

        <el-form-item label="开放程度：" prop="kfcd">
          <el-radio-group v-model="form.kfcd">
            <el-radio :label="0" @click.native="clearUser($event)">完全公开</el-radio>
            <el-radio :label="1" @click.native="pointUser($event)">限定用户</el-radio>
            <el-radio :label="2" @click.native="clearUser($event)">不公开</el-radio>
          </el-radio-group>

        </el-form-item>

        <!-- @type  update
             @time: 2021-12-31
             @desc: 展示指定的用户
        -->
        <el-form-item  v-if="form.kfcd && form.kfcd == 1 && showUserFlag">
          <el-tag closable style="margin: 5px"
                  :key="user.userId" v-for="user in userArr"
                  :disable-transitions="false"
                  @close="handleClose(user)">
            {{ user.userName }}
          </el-tag>
        </el-form-item>

        <el-form-item label="成绩设置：" prop="scoreSet">
          <el-radio-group v-model="form.scoreSet" >
            <div class="radio-set">
              <el-radio :disabled="radioDisable" label="0">交卷后立即显示成绩</el-radio>
            </div>
            <div class="radio-set">
              <el-radio  label="1">定时发布成绩</el-radio>
              <span v-show="form.scoreSet=='1'">
              <el-date-picker
                v-model="form.showScoreTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
              <el-tooltip content="注意：若存在问答题则会在判卷后且到达该时间时发布成绩,定时发布时间需要在结束考试之后" placement="right-start" effect="light">
                <i class="el-icon-question"></i>
              </el-tooltip>
            </span>
            </div>
            <div class="radio-set">
              <el-radio label="2">
                交卷后第<el-input-number :disabled="form.scoreSet!='2'" :min=0 v-model="scoreSetRadio3.day"/>天
                <el-input-number :disabled="form.scoreSet!='2'" :min=0 v-model="scoreSetRadio3.time"/>点发布成绩
                <span v-show="form.scoreSet=='2'" style="color: #1f1f1f">(将于{{form.showScoreTime}}发布成绩)</span>
              </el-radio>
            </div>
          </el-radio-group>

        </el-form-item>
        <el-form-item label="封面：" prop="fm">
          <imageUpload v-model="form.fm" :limit="1" :uploadUrl="'/commom/upload/fastdfs'"></imageUpload>
        </el-form-item>
        <el-form-item label="考试描述：" prop="ms">
          <el-input  v-model="form.ms" placeholder="请输入考试描述" style="width: 400px" type="textarea" maxlength="300" show-word-limit/>
        </el-form-item>

      </el-form>
    </div>
    <div class="btns">
      <div class="btns-bt">
        <el-button @click="qx">取 消</el-button>
        <el-button v-if="active > 0" @click="preStep">上一步</el-button>
        <el-button v-if="active < 1" @click="nextStep">下一步</el-button>
        <el-button v-if="active ==1" type="primary" @click="submitForm(0)">暂存</el-button>
        <el-button v-if="active ==1" type="primary" @click="submitForm(99)">发布</el-button>
      </div>
    </div>

<!--选择试卷-->
    <el-dialog
      :visible.sync="sj_open"
      :close-on-click-modal="false"
      append-to-body
      title="选择试卷" width="1000px" >
      <el-form ref="queryForm" :inline="true" :model="queryParams" label-width="98px">
        <el-form-item label="试卷名称" prop="sjmc">
          <el-input
            v-model="queryParams.sjmc"
            clearable
            placeholder="请输入试卷名称"
            size="small"
            @keyup.enter.native="handleQuery"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="组卷策略" prop="zjcl">
          <el-select v-model="queryParams.zjcl" clearable placeholder="请选择组卷策略" size="small">
            <el-option
              v-for="dict in zjclOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="tags-div" label="标签" prop="queryTag" style="width: calc(100% - 256px);">
          <el-checkbox-group v-model="queryTag">
            <el-checkbox v-for="item in tagOptionsSj" :label="item.id"> {{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="singleTable"
        v-loading="loading"
        :data="testpaperList"
        highlight-current-row
        @current-change="handleCurrentChange" >
        <el-table-column align="center" label="序号" type="index"/>
        <el-table-column align="center" label="试卷名称" prop="sjmc" width="360px"/>
        <el-table-column :formatter="zjclFormat" align="center" label="组卷策略" prop="zjcl" />
        <el-table-column :formatter="fbFormat" align="center" label="发布状态" prop="fb">
          <template slot-scope="scope">
            {{fbFormat(scope.row)}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="更新时间" prop="updateTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirm()">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

<!-- 限定用户    -->
    <el-dialog title="限定用户"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleUser"
               width="1000px">
      <AssigningUsers
        :xz_data="userArr"
        ref="assigningUsers"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUser = false">取 消</el-button>
        <el-button type="primary" @click="pointUserConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="生成二维码"
               :visible.sync="dialogVisibleQrcg"
               width="70%"
               :before-close="closeQrcg"
               :close-on-click-modal="false">
      <div style="height: 550px">
        <Qrcg :data="qrcgForm" @closeQrcg="closeQrcg"/>
      </div>
<!--      <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="closeQrcg()">关 闭</el-button>-->
<!--      </span>-->
    </el-dialog>

<!--    发布提示窗口-->
    <el-dialog
      title="提示"
      :close-on-click-modal="false"
      :visible.sync="fbVisible"
      width="30%">
      <div class="tip"><i class="el-icon-warning" style="color: #f3663a"></i>请确认是否发布【{{this.form.mc}}】考试？</div>
      <div class="ks-con">
        <span class="title">信息内容</span>
        <div class="con-body" >
          <div>考试时间:{{ this.form.kskssj }} ~ {{ this.form.ksjssj }}</div>
          <div>推送时间:{{ this.form.pushTime }}</div>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
      <el-button @click="fbVisible=false">取 消</el-button>
      <el-button type="primary" @click="saveInfo('99')">确 定</el-button>
    </span>
    </el-dialog>

    <el-drawer
      title="预览试卷"
      :visible.sync="openTestPaper"
      size="50%"
      direction="rtl">

      <previewTestPaper ref="paper"></previewTestPaper>

    </el-drawer>
</div>
</template>

<script>
import { listTestpaper, getTestpaper} from "@/api/etp/testpaper/testpaper";
import { listTagRelate } from "@/api/etp/tag/tag";
import { selectScoreByType,getSubjectByPaper} from "@/api/etp/testpaper/testpapersubject";
import { getExamination, addExamination, updateExamination,checkExistKS } from "@/api/etp/examination/examination";
import tagsEdit from "@/components/tags/tagsEdit";
import Qrcg from "@/views/etp/qrcg";
import previewTestPaper from "@/views/etp/examination/previewTestPaper";
/** @type update 2021-12-31 引入指定用户组件 */
import AssigningUsers from "@/components/AssigningUsers"
import {ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import { getTreeselect } from '@/api/etp/category/category'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { checkExistKJ } from '@/api/etp/courseware/courseware'


export default {
  name: "editexamination",
  components: {
    tagsEdit,AssigningUsers,Qrcg,previewTestPaper,Treeselect
  },
  data() {
    var checkDtsc = (rule, value, callback) => {
      if (this.dtcsRadio == '1') {
        if (!value) {
          return callback(new Error('答题次数不能为空'));
        }else {
          callback();
        }
      }else {
        callback();
      }
    };
    var checkPushTime = (rule, value, callback) => {

      if (!value) {
        return callback(new Error('推送时间不能为空'));
      }else {
        let dateRange = this.form.dateRange
        let rel = new Date(dateRange[0]).getTime()-new Date(value).getTime()>=0
        if(rel){
          callback();
        }else{
          return callback(new Error('推送时间不能晚于考试开始时间'));
        }
      }
    };
    var checkMc = (rule, value, callback) => {
      if (!value) {
        callback(new Error('考试名称不能为空'));
      }else {
        checkExistKS({ id:this.form.id,mc:this.form.mc }).then(response => {
          let flag = response.data
          if(flag){
            callback(new Error('考试名称已经存在'));
          }else{
            callback()
          }
        });
      }
    };
    return {
      openTestPaper:false,
      active:0,
      sj_open:false,
      loading:false,
      zjclOptions:[],
      fbOptions:[],
      // 试卷表格数据
      testpaperList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sjmc: null,
        zjcl: null,
        fb: null
      },
      tagOptions:[],
      tagOptionsSj:[],
      queryTag:[],
      //选择的试卷
      selectRow:[],
      selectSj:{
      },

      // 开放程度：完全公开-0，限定用户-1 不公开 2
      kfcdOptions: [],
      lxOptions:[],
      form:{
        /** @type update 2021-12-31 设置开放程度默认值为公开*/
        kfcd:0,
        djsc:60,
        fm: null,
        dateRange:[],
        scoreSet:'2',
      },

      radioDisable:false,
      fsdj_tmp:[],//[{key:'',value:[]}]

      dtcsRadio:0,
      rules:{
        mc: [{
          required: true,
          validator: checkMc,
          trigger: 'blur'
        }],
        dateRange: [
          { required: true,
            type:'array',
            message: "请选择考试时间",
            trigger: 'blur' ,
            fields:{
               0:{required: true,message: "请选择考试时间",trigger: 'change'},
               1:{required: true,message: "请选择考试时间",trigger: 'change'}
            }
          }
        ],
        dtcs:[
          { validator: checkDtsc, trigger: 'blur' }
        ],
        //推送时间
        pushTime:[
          {required: true,message: '请选择推送时间',trigger: 'blur'},
          {validator: checkPushTime,trigger: 'blur'}
        ],
        categoryId: [
          {required: true,  message: "请选择考试分类", trigger: "blur"}
        ],
      },
      pickerOptions: {
        disabledDate: function(time){
          return time < Date.now()  - 8.64e7;
        }
      },
      // timingPickerOptions: {
      //   disabledDate: function(time){
      //     let that = this
      //     return time > that.form.dateRange[1].getTime()
      //   }
      // },
      // 标签用到的
      tagValue: [],

      /**  @type update 指定用户数组 */
      showUserFlag:false,
      dialogVisibleUser:false,
      userArr:[],
      //二维码参数
      dialogVisibleQrcg:false,
      qrcgForm:{
        title:'',
        value:'',
        size:'',
        startTime:'',
        endTime:''
      },
      qrcgUrl:'',
      qrcgModel:'',
      scoreSetRadio3:{
        day:3,
        time:9
      },
      scoreSetTime:null,
      pushTimeSet:0,
      //发布
      fbVisible:false,
      ksInfo:null,

      categoryOptions:[],
    };
  },
  created() {
    this.getDicts("testpaper_zjcl").then(response => {
      this.zjclOptions = response.data;
    });
    this.getDicts("testpaper_fb").then(response => {
      this.fbOptions = response.data;
    });
    this.getTags();
    this.getDicts("examination_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    })
    //获取二维码地址
    this.getConfigKey("cp.qrcg.exam").then(response => {
      this.qrcgModel = response.msg;
      console.log(response,'二维码模块')
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
      console.log(response,'二维码地址')
    });
    //判断是否是修改
    const id = this.$route.query.id;
    const sjid = this.$route.query.sjid;
    // this.form.fm = ETP_DEFAULT_IMAGE_URL
    if(id){
      //获取试卷信息回显
      getTestpaper(sjid).then(response =>{
        this.paperBack(response.data)
      })
    }
    this.getTreeselect()
  },
  watch:{
    //推送时间变为立即推送
    pushTimeSet(val){
      if(val == 0){
        this.$set(this.form,"pushTime",this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss'))
      }
    },
    //分数设置 类型改成 第三种
    "form.scoreSet":{
      handler: function(val, oldVal) {
        if(val=='2'){
          this.getScoreShowTime()
        }else {
          this.$set(this.form,"showScoreTime",null)
        }
      },
      immediate: true
    },
    //分数设置是第三种时， 展示时间根据填写数字变化
    scoreSetRadio3:{
      handler: function(val, oldVal) {
        this.getScoreShowTime()
      },
      deep: true,
      immediate: true
    },
    "form.dateRange":{
      handler: function(val, oldVal) {
        this.getScoreShowTime()
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    getTreeselect(){
      getTreeselect().then(res => {
        this.categoryOptions = res.data
      })
    },
    /** 转换分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children,
        // isDisabled: node.parentId == 0
      };
    },
    //计算成绩显示时间
    getScoreShowTime(){
      let end = this.form.dateRange[1]
      let showTime = this.$moment(end).add(this.scoreSetRadio3.day, 'days').format('YYYY-MM-DD')
      let time = this.scoreSetRadio3.time;
      if(time == undefined){
        time="00"
      }else{
        time = time > 9 ? time:`0${time}`;
      }
      showTime += ` ${time}:00:00`;
      this.$set(this.form,"showScoreTime",showTime)
    },
    /** 关闭二维码页面并跳转至查询页面 */
    closeQrcg(){
      this.dialogVisibleQrcg = false;
      this.back()
    },

    /** 生成二维码 */
    createQrcg(val,remark) {
      console.log(val);
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      if(remark=='新增'){
        this.qrcgForm.value=this.qrcgUrl + this.qrcgModel +val;
      }else if(remark=='修改'){
        this.qrcgForm.value=this.qrcgUrl + this.qrcgModel +val.id;
      }
      console.log(this.qrcgUrl,this.qrcgModel)
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = val.mc==undefined?this.form.mc:val.mc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=val.kskssj==undefined?this.form.kskssj:val.kskssj;
      this.qrcgForm.endTime=val.ksjssj==undefined?this.form.ksjssj:val.ksjssj;
    },

    qx(){
      if (this.$route.query.from && this.$route.query.from === 'index'){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/index", query: { t: Date.now() }});
        return
      }
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/etp/testpaper", query: {t: Date.now()}})
    },

    /**  @type update  2021-12-31 指定用户开始 */
    pointUserConfirm(){
      let arr = this.$refs.assigningUsers.getChoiceUser();
      if (arr.length <= 0){
        this.$message.warning("未选择用户！");
        return
      }
      this.dialogVisibleUser = false;
      this.userArr = this.mergeArray(this.userArr,arr);
      this.showUserFlag = true;
    },
    pointUser(e){
      if (e.target.tagName === 'INPUT') return
      this.dialogVisibleUser = true;
      this.$nextTick(_=>{
        this.$refs.assigningUsers.handleQuery();
      })
    },
    clearUser(e){
      if (e.target.tagName === 'INPUT') return
      this.userArr = [];
      this.showUserFlag = false;
    },
    handleClose(user){
      this.userArr = this.userArr.filter(u => { return u.userId != user.userId })
    },
    mergeArray(arr1,arr2){
      let _arr = [];
      for(let i = 0;i < arr1.length;i++){
        _arr.push(arr1[i]);
      }
      for(let i = 0;i < arr2.length;i++){
        let flag = true;
        for(let j = 0;j < arr1.length;j++){
          if(arr2[i]['userId'] == arr1[j]['userId']){
            flag = false;
            break;
          }
        }
        if(flag){
          _arr.push(arr2[i]);
        }
      }
      return _arr;
    },
    /** 2021-12-31 指定用户结束 */

    // 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷字典翻译
    zjclFormat(row, column) {
      return this.selectDictLabel(this.zjclOptions, row.zjcl);
    },
    // 发布状态：0-暂存，1-发布字典翻译
    fbFormat(row, column) {
      return this.selectDictLabel(this.fbOptions, row.fb);
    },
    getTags(){
      listTagRelate({modelType:"KS"}).then(response => {
        this.tagOptions = response.data;
      });
      listTagRelate({modelType:"SJ"}).then(response => {
        this.tagOptionsSj = response.data;
      });
    },
    //下一步
    nextStep() {
      //第一步
      if(this.selectSj.id){
        //回显考试信息
        //修改
        if(this.$route.query.id){
          getExamination(this.$route.query.id).then(response =>{
            //防止第一步换了试卷，response取到的还是原来的试卷id
            let newSjid = this.form.sjid
            this.form = response.data
            this.form.sjid = newSjid
            /** @type update 2021-12-31 设置开放程度回显 */
            this.form.kfcd = parseInt(response.data.kfcd);

            this.fsdj_tmp = JSON.parse(response.data.fsdj)

            /** @type update 2021-12-31 限定用户回显*/
            let sysUsers = response.data.sysUsers;
            if(this.form.kfcd == 1){
              if (sysUsers.length > 0){
                this.showUserFlag = true;
                this.userArr = sysUsers;
              }
            }

            //答题次数回显
            if(response.data.dtcs=="-99"){
              this.dtcsRadio = 0
              this.form.dtcs=""
            }else{
              this.dtcsRadio = 1
            }
            //标签回显
            if (null != response.data.tags) {
              this.tagValue = response.data.tags.split(",");
            }
            //考试时间回显
            if(response.data.kskssj){
              this.$set(this.form,'dateRange',[response.data.kskssj,response.data.ksjssj])
            }
            //推送时间回显
            this.pushTimeSet= 1
            //图片回显
            this.form.fm =  this.form.fm == ""|| this.form.fm == null? require("@/assets/images/examine_cover.png") :  this.form.fm;

            //成绩设置回显
            if(this.form.scoreSet =='2'){
              let showday = this.$moment(this.form.showScoreTime.substring(0,10))
              let endDay = this.$moment(this.form.ksjssj.substring(0,10))
              let days = showday.diff(endDay,'days')
              this.$set(this.scoreSetRadio3,"day",days)
              this.$set(this.scoreSetRadio3,"time",this.form.showScoreTime.substring(11,13))
            }
          })
        }else{
          //初始化名称
          this.$set(this.form,"mc",this.selectSj.mc)

          //新增 初始化分数等级
          let zfs = this.form.zfs
          let no_max = Math.floor(zfs*0.6)
          debugger
          this.fsdj_tmp = []
          this.fsdj_tmp.push({key:'不及格',value:[0,null]})
          this.fsdj_tmp.push({key:'及格',value:[no_max,null]})

          //初始化考试时间
          let start = this.$moment().day(this.$moment().day() + 1).format('YYYY-MM-DD HH:mm:ss')
          let end = this.$moment().day(this.$moment().day() + 3).format('YYYY-MM-DD HH:mm:ss')
          this.form.dateRange=[start,end]
          //立即推送时间
          this.$set(this.form,"pushTime",this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss'))
          //图片回显
          this.form.fm =  this.form.fm == ""|| this.form.fm == null? require("@/assets/images/examine_cover.png") :  this.form.fm;

          this.form.categoryId = this.selectSj.categoryId
          this.getScoreShowTime()
        }

        /**  成绩设置按钮展示*/
        let zgRel = this.selectSj.tx.find(item =>{
          return item.key =='5'&&item.value>0
        })
        if(zgRel){//有问答题
          // this.$set(this.form,"scoreSet",'1')
          this.radioDisable = true
        }else{
          this.$set(this.form,"scoreSet",'0')
          this.radioDisable = false
        }

        this.active = 1
      }else{
        this.$message("请选择试卷")
      }

    },
    //上一步
    preStep() {
      this.active = 0
      if(null!= this.form.fm && this.form.fm.indexOf("/img/ex")>-1){
        this.form.fm = null
      }
    },
    // radioChange(value){
    //   // this.$message(value)
    // },
    getPaper(){
      this.sj_open = true;
      this.handleQuery();
    },
    /** 查询试卷列表 */
    getList() {
      this.loading = true;
      this.queryParams.fb = '99'
      listTestpaper(this.queryParams).then(response => {
        this.testpaperList = response.rows;
        this.total = response.total;
        this.setCurrent(this.selectSj.id)
        this.form.sjid = this.selectSj.id
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if(null != this.queryTag){
        this.queryParams.tags = this.queryTag.join(',')
      }
      this.getList();
    },
    handleCurrentChange(val) {
      this.selectRow = val;
    },
    setCurrent(sjid) {
      let data = this.testpaperList.find(item => sjid == item.id)
      this.$refs.singleTable.setCurrentRow(data);
    },
    confirm(){
      this.sj_open = false
      let selData = this.selectRow
      //试卷回显
      this.paperBack(selData);
    },
    //试卷回显
    paperBack(selData){
      let tx = JSON.parse(selData.tx)
      this.selectSj.id = selData.id
      this.selectSj.mc = selData.sjmc
      this.selectSj.zjcl = selData.zjcl
      this.selectSj.createBy = selData.createBy
      this.selectSj.createTime = selData.createTime
      this.selectSj.updateTime = selData.updateTime
      this.selectSj.categoryName = selData.categoryName
      this.selectSj.categoryId = selData.categoryId
      this.form.sjid = selData.id
      let tm_num = 0 //总题数
      let fz_num = 0 //总分数
      //回显分值
      selectScoreByType(selData.id).then(response => {
        let fzMap = response.data
        for(let i in tx){
          tm_num += parseInt(tx[i].value)
          let key = tx[i].key
          let fz = fzMap[key]==undefined?0:fzMap[key]
          let fz_lx = fz *parseInt(tx[i].value)
          this.$set(tx[i],'fs',fz_lx)
          fz_num += fz_lx
        }
        this.$set(this.selectSj ,'count',tm_num)
        this.$set(this.selectSj ,'fzNum',fz_num)
        this.$set(this.selectSj ,'tx',tx)
        this.form.zfs = fz_num
      })
      //随机生成组卷需要 回显 取题范围
      if(this.selectSj.zjcl =='2'){
        getSubjectByPaper(this.form.sjid).then(response => {
          let list = response.data;
          this.$set(this.selectSj,"qtfw",list)
        })
      }
    },
    editPaper(){
      let sjid = this.form.sjid
      this.$router.push('/etp/testpaper/paper/'+sjid)
    },

    isArrRepeat(_arr){
      let is_repeat=false;
      _arr.map((item,index)=>{
        if(_arr.indexOf(item)!=index){
          is_repeat=true;
        }
      });
      return is_repeat;
    },

    /** 提交按钮 */
    submitForm(fb) {
      console.log(this.form);
      // if(this.form.fb=='99'){
      //   this.$message.error("该考试已发布，无法进行修改操作！");
      //   return
      // }
      if(this.form.dateRange.length>0){
        let date = this.form.dateRange
        this.form.kskssj = date[0]
        this.form.ksjssj = date[1]
      }
      this.form.fb = fb
      this.form.sjid = this.selectSj.id
      /** 校验分数等级*/
      let fsdj = JSON.parse(JSON.stringify(this.fsdj_tmp));
      //从小到大排序
      fsdj = this.compare(fsdj)
      let array= []//用于校验有没有重复
      fsdj.forEach((item,index)=>{

        if(item.value[0]==null||""==item.key){
          this.$message.warning("分数等级内容不能为空！")
          return
        }
        array.push(item.value[0])
        if(index < fsdj.length-1){
          fsdj[index].value[1] = fsdj[index+1].value[0]-1
        }else{
          fsdj[index].value[1] = this.selectSj.fzNum
        }
      })
      if(this.isArrRepeat(array)){
        this.$message.warning("分数等级不能重叠！")
        return
      }
      if(fsdj[fsdj.length-1].value[0]>this.selectSj.fzNum)
      {
        let options={
          message:`最大分数不能大于总分数(${this.selectSj.fzNum}分)！`,
          showClose:true,
          duration:10000
        }
        this.$message.warning(options)
        return
      }
      this.form.fsdj = JSON.stringify(fsdj)

      /** @type update 2021-12-31 限定用户提交前校验开始*/
      this.form.sysUsers = this.userArr;
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("请指定用户！")
          return
        }
      }

      if(fb=='99'){
        /** 判断发布时 考试开始时间晚于当前时间 半小时*/
          // let time = new Date(this.form.kskssj).getTime()- new Date().getTime()>30*60*1000
          debugger
        console.log(new Date(this.form.kskssj).getTime(),new Date().getTime())
        let time = new Date(this.form.kskssj).getTime()- new Date().getTime() >= 0
        if(!time){
          let options={
            message:"考试开始时间须晚于当前时间！",
            showClose:true,
            duration:10000
          }
          this.$message.warning(options)
          return;
        }
      }
      /** 校验指定用户逻辑结束*/
      this.$refs["form"].validate(valid => {

        if (valid) {
          if(this.dtcsRadio == 0){
            this.form.dtcs = "-99"
          }

          //校验定时考试结果展示时间
          if(this.form.scoreSet!='0'){
            let options={
              message:null,
              showClose:true,
              duration:10000
            }
            if(!this.form.showScoreTime){

              return this.$message.warning("请选择定时发布成绩时间 ！")
            }else{
              if(new Date(this.form.showScoreTime).getTime()<=new Date(this.form.ksjssj).getTime()){
                options.message = "定时成绩发布时间需要晚于考试结束时间 ！"
                return this.$message.warning(options)
              }
            }
          }else{
            this.form.showScoreTime = null //交卷后显示成绩，清空时间
          }

          if(fb=='99'){
            // this.$confirm(`请确认是否发布【${this.form.mc}】考试？`, '提示', {
            //   confirmButtonText: '确定',
            //   cancelButtonText: '取消',
            //   type: 'warning'
            // }).then(() => {
            //   this.saveInfo(fb)
            // }).catch(() => {
            //   this.$message("取消发布")
            // });
            this.fbVisible = true

          }else{
            this.saveInfo(fb)
          }


        }
      });
    },
    saveInfo(fb){
      let fm = this.form.fm
      if(null!= this.form.fm && this.form.fm.indexOf("/img/examine_cover")>-1){
        fm = null
      }
      if (this.form.id != null) {
        updateExamination( this.addTags({...this.form,...{fm:fm}}, this.tagValue)).then(response => {
          this.msgSuccess(response.msg);
          if(response.code ==200 && fb == '99'){
            console.log(this.form,'修改')
            this.createQrcg(this.form,'修改')
          }
        });
      } else {
        addExamination( this.addTags({...this.form,...{fm:fm}}, this.tagValue)).then(response => {
          this.msgSuccess(response.msg);
          this.form.id = response.data
          if(null == this.form.fm){
            this.form.fm = require("@/assets/images/examine_cover.png")
          }
          console.log(response,'新增')
          if(response.code ==200 && fb=='99'){
            this.createQrcg(response.data,'新增')
          }
        });
      }
    },

    //数组排序
    compare(array) {
      return array.sort(function (a, b) {
        const x = a.value[0];
        const y = b.value[0];
        return x-y;
      });
    },
    //新增等级
    add_dj(){
      var fsdj = this.fsdj_tmp
      //试卷分数
      // var score = this.selectSj.fzNum;
      // var dj_min = fsdj[fsdj.length-1].value[1]+1
      this.fsdj_tmp.push({key:'',value:[]})

    },
    del_dj(index){
      this.fsdj_tmp.splice(index,1)
    },
    back(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path:"/etp/examination"
        , query: {t: Date.now()}
      });
    },
    // 取消按钮
    cancel() {
      this.sj_open = false;
      this.queryTag=[]
      this.reset();
    },
    havaTags(val) {
      this.tagValue = val
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        mc: null,
        ms: null,
        fm: null,
        fb: null,
        kslj: null,
        kfcd: 0,
        sjid: null,
        kskssj: null,
        ksjssj: null,
        djsc: 60,//答卷时长
        dtcs: null,//答卷次数
        fsdj: null,
        zfs: null,
        dateRange:[],
        scoreSet:'2',
        showScoreTime:null,
        pushTimeSet:0,
      };
      this.resetForm("form");
      this.scoreSetRadio3 ={
        day:3,
        time:9
      },
      this.fsdj_tmp = null;
      this.active = 0
    },
    previewPaper(){
      this.openTestPaper=true;
      let id = this.selectSj.id
      this.$nextTick(()=>{
        this.$refs.paper.getTestpaper(id)
      })
      // 试卷预览
      // this.$router.push({
      //   path:"/etp/examination/previewTestPaper/" + id,
      //   query:{ id:id }
      // });
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container-custome {
  margin: 20px 30px;
  border-radius: 8px;
  border: 1px solid rgba(167, 171, 183, 0.25);
  background: rgba(241, 245, 252, 0.3);
}
.btns {
  margin-top: 30px;
  position: relative;
  height: 36px;
}
.btns-bt{
  position: absolute;
  right: 0px;
}
.examin-div{
  min-height: 200px;
  width: 100%;
  margin-top: 20px;
  border: 1px solid #dddddd;
  border-radius: 4px;
}
.paper-title{
  height: 36px;
  line-height: 36px;
  border-bottom: 1px solid #dddddd;
  padding-left: 20px;
  position: relative;
}
.paper-btn{
  position: absolute;
  top: 0px;
  right: 15px;
}
.paper-content{
  margin-top: 30px;
  p{
    //margin-left: 40px;
  }
}
.paper-content > div{
  font-size: 15px;
  margin: 6px 22px;
  color: #7f7b7b;
}
.dj-div{
  display: flex;
  ::v-deep .dj-input{
    width: 100px;
    margin: 5px 5px;
  }
  ::v-deep .el-input-number{
    width: 140px;
    margin: 5px 5px;
  }
  .el-slider{
    width: 300px;
    margin-left: 20px;
  }
}
.el-icon-remove-outline{
  line-height: 38px;
  color: red;
  margin-left: 10px;
  cursor: pointer;
}
//.el-picker-panel__footer .el-button--text{
//  display: none!important;
//}
.el-icon-question{
  color:#f29303;
  font-size: 20px;
  margin: 0px 6px 0px 18px;
}
.div-head{
  border-bottom: 1px solid #dddddd;
}
.el-col-2 {
  border: 1px solid #dddddd;
  text-align: center;
}
.el-row div{
  line-height: 32px;
}
::v-deep .fsdj .el-form-item__label:before, .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap > .el-form-item__label:before {
  content: "*";
  color: #ff4949;
  margin-right: 4px;
}
.radio-set{
  margin-top: 10px;
  ::v-deep .el-input-number--medium {
    width: 120px;
    line-height: 34px;
    margin: 0 6px;
  }
}


::v-deep .el-dialog__body {
  padding: 8px 20px 20px;
}
.tip{
  font-size: 16px;
  font-weight: 600;
  i{
    margin-right: 5px;
  }
}
.ks-con{
  border:1px solid #dddddd;
  padding: 6px 14px;
  margin-top: 24px;
  position: relative;
}
span.title{
  display:block;
  width:70px;
  position:absolute;
  top:-9px;
  text-align: center;
  background: white;
}
.con-body{
  margin-top: 15px;
  div{
    padding: 0px 8px 8px;
  }
}
::v-deep .el-drawer__header{
  margin-bottom: 5px!important;
}
</style>
