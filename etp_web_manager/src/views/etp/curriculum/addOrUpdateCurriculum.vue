<template>
  <div class="app-container app-container-custome">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-steps :active="active" simple finish-status="success">
          <el-step>
            <slot slot="title">第一步：课程信息</slot>
          </el-step>
          <el-step>
            <slot slot="title">第二步：课程内容</slot>
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
    <br/>

    <!-- 课程基本信息    -->
    <transition name="el-fade-in-linear">
      <div v-if="active === 0">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="课程名称" prop="mc">
            <el-input v-model="form.mc" placeholder="请输入课程名称" maxlength="100" show-word-limit/>
          </el-form-item>
          <el-form-item label="封面图片" prop="fmt">
            <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fmt"/>
          </el-form-item>

          <el-form-item label="课程标签" prop="tagValue">
            <tagsEdit widthSty="100%"
                      :parentValue="tagValue"
                      :selectOptions="tagOptions"
                      @selectTags="havaTags">

            </tagsEdit>
          </el-form-item>
          <el-form-item label="课程分类" class="category" prop="categoryIds">
<!--            <treeselect v-model="form.categoryId" :options="categoryOptions"-->
<!--                        :normalizer="normalizer" placeholder="选择分类"-->
<!--            />-->
            <el-cascader
              style="width: 100%;"
              :options="categoryOptions"
              v-model="form.categoryIds"
              :props="{ multiple: true, checkStrictly: true ,value:'id'}"
              clearable></el-cascader>
          </el-form-item>

          <el-form-item label="开放程度" prop="kfcd">
            <el-radio-group v-model="form.kfcd">
              <el-radio :label="0" @click.native="clearUser($event)">完全公开</el-radio>
              <el-radio :label="1" @click.native="pointUser($event)">限定用户</el-radio>
              <el-radio :label="2" @click.native="clearUser($event)">不公开</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item  v-if="form.kfcd && form.kfcd == 1 && showUserFlag">
              <el-tag closable style="margin: 5px"
                      :key="user.userId" v-for="user in userArr"
                      :disable-transitions="false"
                      @close="handleClose(user)">
                {{ user.userName }}
              </el-tag>
          </el-form-item>
          <el-form-item label="课程描述" prop="ms">
            <el-input v-model="form.ms" type="textarea" placeholder="请输入课程描述" maxlength="300" show-word-limit/>
          </el-form-item>
          <el-form-item label="备注信息" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" maxlength="300" show-word-limit/>
          </el-form-item>
        </el-form>
      </div>
    </transition>

    <!-- 组织课程内容  -->
    <transition name="el-fade-in-linear">
      <div v-if="active !== 0">
        <div>
          <el-button size="mini" type="primary" plain @click="addKJ()">+ 添加课件</el-button>
          <el-button size="mini" type="primary" plain @click="addXt()">+ 添加习题集</el-button>
<!--          <el-button size="mini" type="primary" plain @click="addLx()">+ 添加练习</el-button>-->
        </div>
        <!--内容展示-->
        <el-divider content-position="left">课程章节内容</el-divider>
        <div>
          <el-table :row-key="getKey" :data="unitArr" style="width: 100%;">
            <el-table-column  label="序号"
                              type="index"
                              width="100"
                              align="center"/>
            <el-table-column  prop="source_name"
                              label="名称"
                              align="left">
              <template slot-scope="scope">
                <span style="cursor: pointer;" :style="scope.row.source_type=='kj'?'font-weight: bold;font-size: 16px':'color:gray'"
                      @click="rowClick(scope.row)"> {{scope.row.source_name}} </span>
              </template>
            </el-table-column>
            <el-table-column  prop="source_type"
                              align="center"
                              label="类型"
                              width="200">
              <template slot-scope="scope">
                <el-tag :type="scope.row.source_type == 'kj'?'success':''">
                  {{ scope.row.source_type == 'kj' ? '课件' : '练习' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="300">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="上移" placement="top-end">
                  <el-button type="text" style="padding:0"
                             :disabled="scope.$index == 0"
                             @click="moveUpward(unitArr,scope.row, scope.$index)">
                    <i class="el-icon-top  etp_font_size"></i></el-button>
                </el-tooltip>

                <el-tooltip class="item" effect="dark" content="上移" placement="top-end">
                  <el-button type="text" style="padding:0"
                             :disabled="(scope.$index + 1) == unitArr.length"
                             @click="moveDown(unitArr,scope.row, scope.$index)">
                    <i class="el-icon-bottom  etp_font_size"></i></el-button>
                </el-tooltip>
<!--                <el-tooltip class="item" effect="dark" content="删除" placement="top-end">-->
<!--                  <el-button type="text" style="padding:0;color: red"-->
<!--                             @click="delItem(unitArr,scope.row, scope.$index)">-->
<!--                    <i class="el-icon-delete etp_font_color_red etp_font_size"></i>-->
<!--                  </el-button>-->
<!--                </el-tooltip>-->
                <el-tooltip class="item" effect="dark" content="预览" placement="top-end">
                  <el-button type="text" style="padding:0"
                             @click="rowClick(scope.row)">
                    <i class="el-icon-document  etp_font_size"></i></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="拖拽" placement="top-start">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-s-grid"
                    class="move"
                    @click=""
                  ></el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </transition>


    <div class="btns">
      <el-button @click="qx">取 消</el-button>
      <el-button type="primary" @click="tempStore" v-if="active === 0">暂 存</el-button>
      <el-button @click="next" v-if="active === 0">下一步</el-button>
      <el-button @click="pre" v-if="active > 0">上一步</el-button>
      <el-button type="primary" @click="submit('0')" v-if="active === 1">暂 存</el-button>
      <el-button type="primary" @click="submit('99')" v-if="active === 1">发 布</el-button>


    </div>

    <el-dialog
      title="增加课件"
      :visible.sync="dialogVisibleKJ"
      width="1000px"
      :before-close="addKJConfirm"
      :close-on-click-modal="false">
      <addKJ ref="addKJ"/>
      <span slot="footer" class="dialog-footer">
<!--        <el-button @click="dialogVisibleKJ = false">取 消</el-button>-->
        <el-button type="primary" @click="addKJConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="增加练习"
      :visible.sync="dialogVisibleST"
      :before-close="addLXConfirm"
      width="1000px"
      :close-on-click-modal="false">
      <addST ref="addST"/>
      <span slot="footer" class="dialog-footer">
<!--        <el-button @click="dialogVisibleST = false">取 消</el-button>-->
        <el-button type="primary" @click="addLXConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="增加习题集"
      :visible.sync="dialogVisibleXt"
      :before-close="addXtjConfirm"
      width="1000px"
      :close-on-click-modal="false">
      <addXtj ref="addXtj"/>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addXtjConfirm">确 定</el-button>
      </span>
    </el-dialog>


    <el-dialog title="课件预览"
               :close-on-click-modal="false"
               :before-close="closeKj"
               :visible.sync="dialogVisiblePreview"
               width="1200px">
      <previewCourseware :courseWare="courseWare"/>
    </el-dialog>

    <el-dialog title="试题预览"
               :close-on-click-modal="false"
               :visible.sync="dialogVisiblePreviewSubject"
               width="1000px">
      <previewSubject ref="subPre" :form="subjectForm"/>
    </el-dialog>

    <el-dialog title="限定用户"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleUser"
               width="1000px">
      <AssigningUsers
        :xz_data="userArr"
        ref="assigningUsers"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUser = false">取 消</el-button>
        <el-button type="primary" @click="pointUserConfirm" style="margin-left: 50px">确 定</el-button>
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
  </div>
</template>

<script>
import tagsEdit from "@/components/tags/tagsEdit"
import { listTagRelate } from "@/api/etp/tag/tag";
import imageUpload from "@/components/ImageUpload";
import { addCurriculum,completeTCurriculum,getCurriculum,getTCurriculumContentDetail,checkExistKC } from "@/api/etp/curriculum/curriculum";
import { getSubject,selectedStByKc } from "@/api/etp/subject/subject"
import {addCourseware, getCourseware, selectedKjByKc, selectedXtjByKc} from '@/api/etp/courseware/courseware'
import addKJ from "@/views/etp/curriculum/components/addKJ";
import addST from "@/views/etp/curriculum/components/addST";
import previewCourseware from "@/views/etp/curriculum/components/previewCourseware";
import previewSubject from "@/views/etp/curriculum/components/perviewSubject"
import {FILE_PREVIEW_URL,ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import AssigningUsers from "@/components/AssigningUsers"
import Qrcg from "@/views/etp/qrcg";
import { getTreeselect } from '@/api/etp/category/category'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { checkPermi } from '@/utils/permission'
import Sortable from 'sortablejs'
import {getCurriculumTreeselect} from "@/api/category/category";
import addXtj from "@/views/etp/curriculum/components/addXtj";

export default {
  name: "addOrUpdateCurriculum",
  components: {
    imageUpload,
    tagsEdit,
    addKJ,
    addST,
    previewCourseware,
    previewSubject,
    AssigningUsers,
    Qrcg,
    Treeselect,
    addXtj
  },

  data() {
    var checkMc = (rule, value, callback) => {
      if (!value) {
         callback(new Error('课程名称不能为空'));
      }else {
        checkExistKC({ id:this.form.id,mc:this.form.mc }).then(response => {
          let flag = response.data
          if(flag){
             callback(new Error('课程名称已经存在'));
          }else{
            callback()
          }
        });
      }
    };
    return {
      showUserFlag:false,
      dialogVisibleUser:false,
      subjectForm:{},
      dialogVisiblePreviewSubject:false,
      courseWare:'',
      dialogVisiblePreview:false,
      dialogVisibleKJ: false,
      dialogVisibleST: false,
      dialogVisibleXt: false,
      modifyFlag: 0,
      type: '',
      active: 0,
      //二维码参数
      dialogVisibleQrcg: false,
      qrcgForm:{
        title:'',
        value:'',
        size:'',
        startTime:'',
        endTime:''
      },
      qrcgUrl:'',
      qrcgModel:'',
      form: {
        kfcd: 0,
      },
      // 表单校验
      rules: {
        mc: [
          {required: true, validator: checkMc, trigger: "blur"}
        ],
        // fmt: [
        //   {required: true, message: "请设置封面图", trigger: "blur"}
        // ],
        kfcd: [
          {required: true, message: "请设置开放程度", trigger: "blur"}
        ],
        userArr:[
          { type: Array, required: true, message: "请指定用户", trigger: "blur"  }
        ],
        categoryIds: [
          {required: true,  message: "请选择课程分类", trigger: "blur"}
        ],
      },
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      unitArr: [],
      userArr:[],
      categoryOptions:[],
      tableFlag: false
    }
  },
  watch: {
    /*form: {
      handler() {
        this.modifyFlag++;
      },
      deep: true // 深度监视，适用于当一个对象内部属性被改变时
    },
    tagValue: {
      handler() {
        this.modifyFlag++;
      },
      deep: true // 深度监视，适用于当一个对象内部属性被改变时
    },
    userArr:{
      handler(){
        this.modifyFlag++;
      },
      deep: true // 深度监视，适用于当一个对象内部属性被改变时
    },*/
    tableFlag:{
      handler(){
        debugger
        if(this.tableFlag){
          this.rowDrop()
        }
      },
      deep: true
    }

  },
  mounted() {

  },
  created() {
    this.getTags();
    this.tableFlag=false
    const id = this.$route.params.id
    this.form.fmt = require("@/assets/images/cover.jpg")
    let catIds = this.$route.query.catIds
    if(typeof catIds ==='string'){
      catIds = [catIds]
    }
    if(undefined != catIds && catIds.length>0){
      this.form.categoryIds = [catIds]
    }
    if (id !== undefined && id !== '') {
      this.getCurriculum(id);
    }
    this.getConfigKey("cp.qrcg.curriculum").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
    this.treeSelect()
  },
  methods: {

    getKey(row){
      return row.id+"-"+row.source_type
    },
    // 行拖拽
    rowDrop () {
      let tbody = document.querySelector('.el-table__body-wrapper tbody')
      let _this = this
      Sortable.create(tbody, {

        // animation: 150, // ms, number 单位：ms，定义排序动画的时间
        // onAdd: function (evt) { // 拖拽时候添加有新的节点的时候发生该事件
        //   console.log('onAdd.foo:', [evt.item, evt.from])
        // },
        // onUpdate: function (evt) { // 拖拽更新节点位置发生该事件
        //   console.log('onUpdate.foo:', [evt.item, evt.from])
        // },
        // onRemove: function (evt) { // 删除拖拽节点的时候促发该事件
        //   console.log('onRemove.foo:', [evt.item, evt.from])
        // },
        // onStart: function (evt) { // 开始拖拽出发该函数
        //   console.log('onStart.foo:', [evt.item, evt.from])
        // },
        // onSort: function (evt) { // 发生排序发生该事件
        //   console.log('onUpdate.foo:', [evt.item, evt.from])
        // },
        onEnd ({ newIndex, oldIndex }) { // 结束拖拽
          let currRow = _this.unitArr.splice(oldIndex, 1)[0]
          _this.unitArr.splice(newIndex, 0, currRow)
        }
      })
    },
    treeSelect(){
      getCurriculumTreeselect().then(res => {
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

    /** 关闭二维码页面并跳转至查询页面 */
    closeQrcg(){
      this.dialogVisibleQrcg = false;
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/curriculum", query: {t: Date.now()}})
    },
    /** 生成二维码 */
    createQrcg(val) {
      debugger
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = this.form.mc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=null;
      this.qrcgForm.endTime=null;
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl+this.qrcgModel+val+'';
    },

    /** 指定用户 */
    pointUserConfirm(){
      let arr = this.$refs.assigningUsers.getChoiceUser();
      if (arr.length <= 0){
        this.$message.warning("未选择用户！");
        return
      }
      this.userArr = this.mergeArray(this.userArr,arr);
      this.showUserFlag = true;
      this.dialogVisibleUser = false;
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
    closeKj(){
      this.dialogVisiblePreview = false;
      this.courseWare={ type:'',address:'',content:'' }
    },
    /** 课程内容预览*/
    async rowClick(row){
      let { id } = row;
      if (row.source_type == 'kj'){
        getCourseware(id).then(res => {
          this.dialogVisiblePreview = false;
          /** 预览 Office：1  视频：2  富文本：3  外部链接：4  其他文件：5 */
          console.log(res.data)
          let { kjdz,xs,nr,lj, id } = res.data;
          let router = this.$router.resolve({
            path: "/etp/course/previewCourse",
            query: {kjid: id}
          })
          window.open(router.href,'_blank')
          // if (xs == '1' || xs == '2' || xs == '5' ) this.courseWare = { type:xs,address:FILE_PREVIEW_URL + '?url=' + encodeURIComponent(window.btoa(kjdz)) }
          // if (xs == '3') this.courseWare = { type:xs,content:nr }
          // if (xs == '4') this.courseWare = { type:xs,address:lj }
        })
      }
      if(row.source_type == 'st'){
        this.dialogVisiblePreviewSubject = true;
        await getSubject(id).then(res => {
          this.$nextTick(_=>{
            this.subjectForm = res.data;
            //选项回显
            if(res.data.lx == '1' ){
              this.$refs.subPre.dx_radio = res.data.da;
              this.subjectForm.xxList = JSON.parse( res.data.xx);
            }
            if(res.data.lx == '2' ){
              this.$refs.subPre.da_checkList = res.data.da.split(',');
              this.subjectForm.xxList = JSON.parse( res.data.xx);
            }
            //填空答案
            if(res.data.lx=='4'){
              let da = res.data.da.split(',');
              let list = [];
              da.forEach(function(item){
                list.push({value:item})
              })
              this.subjectForm.tk_list = list;
            }
          })
        })
      }
      if (row.source_type == 'xtj'){
        const id = row.id
        let router = this.$router.resolve({
          path: "/train-view/view",
          query: { paperId: id}
        })
        window.open(router.href,'_blank')
      }
    },
    /** 获取课程基本信息*/
    getCurriculum(id) {
      getCurriculum(id).then(res => {
        this.form = res.data;
        let sysUsers = res.data.sysUsers;

        //图片回显
        this.form.fmt =  this.form.fmt == ""|| this.form.fmt == null? require("@/assets/images/cover.jpg") :  this.form.fmt;

        if (this.form.kfcd == 1){
          if (sysUsers.length > 0){
            this.showUserFlag = true;
            this.userArr = sysUsers;
          }
        }

        let tCurriculumContents = res.data.tCurriculumContents;
        if (tCurriculumContents.length > 0) {
          tCurriculumContents.forEach(item => {
              item.id = item.sid
            if(item.source_type=='st'){
              item.source_name = item.source_name.replace(/<[^>]+>/g, '');
            }

          })
          this.unitArr = tCurriculumContents;

        } else {
          this.active++;
        }

        this.form.kfcd = parseInt(res.data.kfcd);
        if (null != res.data.tags) {
          this.tagValue = res.data.tags.split(",");
        } else {
          this.tagValue = [];
        }
      })
    },

    /** 增加课件*/
    addKJ() {
      this.type = 'kj';
      this.dialogVisibleKJ = true;
      this.$nextTick(_ => {
        //课件选中数据
        if(this.$refs.addKJ.xz_data.length==0){
          selectedKjByKc(this.form.id).then(res=>{
            this.$refs.addKJ.xz_data = res.data
            this.$refs.addKJ.handleQuery();
          })
        }else{
          this.$refs.addKJ.handleQuery();
        }
        this.$refs.addKJ.getTags();
        this.$refs.addKJ.tabsActiveName = 'first'
      })
    },
    /** 增加课件确认按钮 */
    addKJConfirm() {
      let arr = this.$refs.addKJ.collectDatasByIdsArr();
      if (arr.length > 0) {

        let allData = [...this.unitArr,...arr]
        this.delRepeat(allData, arr,'kj');
      }

      this.dialogVisibleKJ = false;
    },
    /** 增加练习*/
    addLx() {
      this.type = 'lx';
      this.dialogVisibleST = true;
      this.$nextTick(_ => {
        //课件选中数据
        if(this.$refs.addST.xz_data.length==0){
          selectedStByKc(this.form.id).then(res=>{
            this.$refs.addST.xz_data = res.data
            this.$refs.addST.handleQuery();
          })
        }else{
          this.$refs.addST.handleQuery();
        }
        this.$refs.addST.getTags();
        this.$refs.addST.tabsActiveName = 'first'
      })
    },
    /** 增加练习确认按钮 */
    addLXConfirm() {
      let arr = this.$refs.addST.collectDatasByIdsArr();
      if (arr.length > 0) {
        let allData = [...this.unitArr,...arr]
        this.delRepeat(allData, arr,'st');
      }
      this.dialogVisibleST = false
    },
    /** 去重*/
    delRepeat(allData, arr,type) {
      //unitArr 包含原来的和新改变的所有的
      let data_new = []
      if (allData.length > 0 && arr.length==0) {
        this.unitArr = allData.filter(item => {
          return item.source_type != type
        })
      }else if(allData.length == 0 && arr.length >0){
        this.unitArr = arr
      }else{
        allData.forEach(u =>{
          let data = arr.find(a=>{
            return a.id==u.id && a.source_type==u.source_type
          })
          let data2 = data_new.find(a=>{
            return a.id==u.id && a.source_type==u.source_type
          })
          //data_new里没有这一条数据
          if(data != undefined && data2==undefined){
            data_new.push(u)
          }else if(u.source_type != type){
            data_new.push(u)
          }
        })
        this.unitArr = data_new
        if(null!=this.unitArr && this.unitArr.length>0){
          this.tableFlag = true
        }
      }
    },

    /** 下一步 */
    next() {
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("用户暂未指定!");
          return
        }
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          /*if (this.modifyFlag > 3) {
            this.tempStore();
          }*/
          this.tempStore();

        }
      })
    },
    /** 取消 */
    qx(){
      if (this.$route.query.from && this.$route.query.from === 'index'){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/index", query: { t: Date.now() }});
        return
      }
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/curriculum", query: {t: Date.now()}})
    },
    /** 上一步 */
    pre() {
      // this.modifyFlag = 3;
      --this.active;
      if(null== this.form.fmt){
        this.form.fmt = require("@/assets/images/cover.jpg")
      }
      if (this.active <= 0) {
        this.active = 0;
      }
    },
    /** 标签相关 */
    getTags() {
      listTagRelate({modelType: "KC"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
    /** 暂存 */
    tempStore() {
      this.form.states = '0'
      this.form.sysUsers =  this.userArr;
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("用户暂未指定!");
          return
        }
      }
      if(null!= this.form.fmt && this.form.fmt.indexOf("/img/cover")>-1){
        this.form.fmt = null
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          addCurriculum(this.addTags(this.form, this.tagValue)).then(response => {
            if (response.code == 200) {
              this.$message.success("课程基本信息保存成功!")
              this.form.id = response.data;
              ++this.active;
              setTimeout(() => {
                if (this.active >= 1) {
                  this.active = 1;
                  if(this.unitArr!=null && this.unitArr.length>0){
                    this.tableFlag = true
                  }
                }
              }, 250)

            }
            this.form.fmt = this.form.fmt==null? require("@/assets/images/cover.jpg"):this.form.fmt
          });
        }
      });
    },
    /** 提交 */
    submit(states) {
      if (this.unitArr.length <= 0) {
        this.$message.warning("课程内容未设置！")
        return
      }
      this.form.states = states;
      this.addTags(this.form, this.tagValue)
      this.unitArr.map((item, index) => {
        item['sort'] = index + 1
        return item;
      })
      this.form.sysUsers =  this.userArr;
      this.form['tCurriculumContents'] = this.unitArr;

      if(null!=this.form.fmt&&this.form.fmt.indexOf("/img/cover")>-1){
        this.form.fmt = null
      }
      if(states == '0'){
        completeTCurriculum(this.form).then(res => {
          if (res.code == 200) {
            this.$message.success("课程暂存成功！");
          }
        })
      }else{
        this.$confirm('请确认是否发布当前课程信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          completeTCurriculum(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success("课程发布成功！");
              this.createQrcg(res.data);
            }
          })
        }).catch(() => {});
      }
      /*completeTCurriculum(this.form).then(res => {
        if (res.code == 200) {
          if (states == '0') {
            this.$message.success("课程暂存成功！");
            setTimeout(() => {
              this.$store.dispatch("tagsView/delView", this.$route);
              this.$router.push({path: "/curriculum", query: {t: Date.now()}})
            }, 240)
            return
          }
          this.$message.success("课程发布成功！");
          this.createQrcg(res.data);
          // setTimeout(() => {
          //   this.$store.dispatch("tagsView/delView", this.$route);
          //   this.$router.push({path: "/curriculum", query: {t: Date.now()}})
          // }, 240)
        }
      })*/
    },
    /** 上移*/
    moveUpward(data, row, index) {
      if (index > 0) {
        let upData = data[index - 1];
        data.splice(index - 1, 1);
        data.splice(index, 0, upData);
      } else {
        this.$message({
          message: '已经是第一条，上移失败',
          type: 'warning'
        });
      }
    },
    /** 下移*/
    moveDown(data, row, index) {
      if ((index + 1) == data.length) {
        this.$message({
          message: '已经是最后一条，下移失败',
          type: 'warning'
        });
      } else {
        let downData = data[index + 1];
        data.splice(index + 1, 1);
        data.splice(index, 0, downData);
      }
    },
    /** 删除课件或者试题*/
    delItem(data, row, index) {
      this.$confirm(`是否删除【${row.source_name}】内容项?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        data.splice(index, 1);
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch();
    },

    /** 增加习题集*/
    addXt(){
      this.dialogVisibleXt = true;
      this.$nextTick(_ => {
        //习题集选中数据
        if(this.$refs.addXtj.xz_data.length==0){
          selectedXtjByKc(this.form.id).then(res=>{
            this.$refs.addXtj.xz_data = res.data
            this.$refs.addXtj.handleQuery();
          })
        }else{
          this.$refs.addXtj.handleQuery();
        }
        this.$refs.addXtj.tabsActiveName = 'first'
      })
    },
    /** 增加习题集确认按钮 */
    addXtjConfirm() {
      let arr = this.$refs.addXtj.collectDatasByIdsArr();
      if (arr.length > 0) {
        let allData = [...this.unitArr,...arr]
        this.delRepeat(allData, arr,'xtj');
      }
      this.dialogVisibleXt = false
    },
  }
}
</script>

<style scoped>
.app-container-custome {
  margin: 20px 5%;
  border-radius: 8px;
  border: 1px solid rgba(238, 227, 232, 0.15);
  background: rgba(249, 249, 249, 0.3);
}

.btns {
  margin-top: 15px;
  margin-left: 10%;
}

::v-deep .el-steps--simple {
  padding: 15px 20%;
}

.qrcg-div {
  height: 100%;

}
::v-deep .el-dialog__body {
  padding: 0px 20px;
}
</style>
