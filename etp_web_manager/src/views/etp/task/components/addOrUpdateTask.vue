<template>
  <div class="app-container app-container-custome">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-steps :active="active" simple finish-status="success">
          <el-step>
            <slot slot="title">第一步：登记任务信息</slot>
          </el-step>
          <el-step>
            <slot slot="title">第二步：编辑任务内容</slot>
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
    <br/>

    <!-- 登记任务信息    -->
    <transition name="el-fade-in">
      <div v-if="active === 0">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">

          <el-form-item label="任务名称" prop="mc">
            <el-input v-model="form.mc" placeholder="请输入任务名称"/>
          </el-form-item>
          <el-form-item label="任务标签" prop="tagValue">
            <tagsEdit widthSty="100%"
                      :parentValue="tagValue"
                      :selectOptions="tagOptions"
                      @selectTags="havaTags">

            </tagsEdit>
          </el-form-item>
          <el-form-item label="任务封面" prop="fmt">
            <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fmt"/>
          </el-form-item>
          <el-form-item label="开放程度" prop="kfcd">
            <el-radio-group v-model="form.kfcd">
<!--              <el-radio :label="0" @click.native="clearUser($event)">完全公开</el-radio>-->
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
          <el-row>
            <el-col :span="10">
              <el-form-item label="任务期限" prop="taskRangeTime">
                <el-date-picker
                  @input="daterangeChange"
                  v-model="form.taskRangeTime"
                  :picker-options="pickerOptions"
                  type="daterange"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  :default-time="['00:00:00', '23:59:59']">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="推送时间" prop="taskSendTime">
                <el-date-picker clearable size="small"
                                v-model="form.taskSendTime"
                                :picker-options="pickerOptions1"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择任务推送时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="任务描述" prop="ms">
            <el-input v-model="form.ms" placeholder="请输入任务描述"/>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
          </el-form-item>
        </el-form>
      </div>
    </transition>

    <!-- 编辑任务内容   -->
    <transition name="el-fade-in">
      <div v-if="active !== 0">
        <div>
          <el-button size="mini" type="primary" plain @click="addCourses()">+ 添加课程</el-button>
<!--          <el-button size="mini" type="primary" plain @click="addExams()">+ 添加考试</el-button>-->
        </div>
        <!--内容展示-->
        <el-divider content-position="left">任务内容</el-divider>
        <div>
          <el-table  :data="taskArr" style="width: 100%">
            <el-table-column  label="序号"
                              type="index"
                              width="100"
                              align="center"/>
            <el-table-column prop="source_name"
                             label="名称"
                             align="center">
              <template slot-scope="scope">
                <span style="cursor: pointer" @click="rowClick(scope.row)">{{scope.row.source_name}}</span>
              </template>
            </el-table-column>
            <el-table-column  prop="source_type"
                              align="center"
                              label="类型"
                              width="200">
              <template slot-scope="scope">
                <el-tag :type="scope.row.source_type == 'kc'?'success':''">
                  {{ scope.row.source_type == 'kc' ? '课程' : '考试' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="300">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="上移" placement="top-end">
                  <el-button type="text" style="padding:0"
                             :disabled="scope.$index == 0"
                             @click="moveUpward(taskArr,scope.row, scope.$index)">
                    <i class="el-icon-top  etp_font_size"></i></el-button>
                </el-tooltip>

                <el-tooltip class="item" effect="dark" content="上移" placement="top-end">
                  <el-button type="text" style="padding:0"
                             :disabled="(scope.$index + 1) == taskArr.length"
                             @click="moveDown(taskArr,scope.row, scope.$index)">
                    <i class="el-icon-bottom  etp_font_size"></i></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="删除" placement="top-end">
                  <el-button type="text" style="padding:0;color: red"
                             @click="delItem(taskArr,scope.row, scope.$index)">
                    <i class="el-icon-delete etp_font_color_red etp_font_size"></i>
                  </el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="预览" placement="top-end">
                  <el-button type="text" style="padding:0"
                             @click="rowClick(scope.row)">
                    <i class="el-icon-document  etp_font_size"></i></el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </transition>


    <div class="btns">
      <el-button @click="back">取 消</el-button>
      <el-button type="primary" @click="temStorage" v-if="active === 0">暂 存</el-button>
      <el-button @click="next" v-if="active === 0">下一步</el-button>
      <el-button @click="pre" v-if="active > 0">上一步</el-button>
      <el-button type="primary" @click="finish('0')" v-if="active > 0">暂 存</el-button>
      <el-button type="primary" @click="finish('99')" v-if="active > 0">发 布</el-button>
    </div>


    <el-dialog
      title="增加考试"
      :visible.sync="dialogVisibleKS"
      width="1000px"
      :close-on-click-modal="false">
      <addExamination ref="addExamination"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleKS = false">取 消</el-button>
        <el-button type="primary" @click="addExamConfirm">确 定</el-button>
      </span>

    </el-dialog>


    <el-dialog
      title="增加课程"
      :visible.sync="dialogVisibleKC"
      width="1000px"
      :close-on-click-modal="false">
      <addCurriculums ref="addCurriculums"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleKC = false">取 消</el-button>
        <el-button type="primary" @click="addCourseConfirm">确 定</el-button>
      </span>
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
    </el-dialog>
  </div>
</template>

<script>
import addCurriculums from "@/views/etp/task/components/addCurriculums";
import addExamination from "@/views/etp/task/components/addExamination";
import tagsEdit from "@/components/tags/tagsEdit"
import imageUpload from "@/components/ImageUpload";
import {listTagRelate} from "@/api/etp/tag/tag";
import {listTask, getTask, delTask, addTask, updateTask, exportTask, completeTask,checkExistRW} from "@/api/etp/task/index";
import AssigningUsers from "@/components/AssigningUsers"
import {ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import { completeTCurriculum } from '@/api/etp/curriculum/curriculum';
import Qrcg from "@/views/etp/qrcg";
export default {
  name: "addOrUpdateTask",
  components: {
    imageUpload,
    tagsEdit,
    addCurriculums,
    addExamination,
    AssigningUsers,
    Qrcg
  },
  data() {
    var checkMc = (rule, value, callback) => {
      if (!value) {
        callback(new Error('任务名称不能为空'));
      }else {
        checkExistRW({ id:this.form.id,mc:this.form.mc }).then(response => {
          let flag = response.data
          if(flag){
            callback(new Error('任务名称已经存在'));
          }else{
            callback()
          }
        });
      }
    };
    return {
      showUserFlag:false,
      dialogVisibleUser:false,
      modifyFlag: 0,
      dialogVisibleKC: false,
      dialogVisibleKS: false,
      active: 0,
      // 表单参数
      form: {
        taskSendTime:new Date(),
        kfcd:2
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
        taskRangeTime: [
          {required: true, message: "请设置任务期限", trigger: "blur"}
        ],
        taskSendTime: [
          {required: true, message: "请设置任务推送时间", trigger: "blur"}
        ]
      },
      // 标签用到的
      tagValue: [],
      tagOptions: [],
      // 任务内容数组
      taskArr: [],


      userArr:[],
      pickerOptions: {
        disabledDate: function(time){
          return time < Date.now()  - 8.64e7;
        }
      },
      pickerOptions1: {
        disabledDate: function(time){
          return time < Date.now()  - 8.64e7;
        }
      },
      xm_fmt:require("@/assets/images/task_cover.png"),
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
    }
  },
  watch: {
    form: {
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
    },
  },
  created() {
    this.getTags();
    const id = this.$route.params.id;
    this.form.fmt = require("@/assets/images/task_cover.png")
    if (id !== undefined && id !== '') {
      this.getTaskInfo(id);
    }
    //获取二维码地址
    this.getConfigKey("cp.qrcg.exam").then(response => {
      this.qrcgModel = response.msg;
      console.log(response,'二维码模块')
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
  },
  methods: {
    /** 关闭二维码页面并跳转至查询页面 */
    closeQrcg(){
      this.dialogVisibleQrcg = false;
      this.back()
      // this.$store.dispatch("tagsView/delView", this.$route);
      // this.$router.push({path: "/task", query: {t: Date.now()}})
    },

    /** 生成二维码 */
    createQrcg(form,val) {
      console.log(form.taskRangeTime[0]);
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = form.mc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime = form.taskRangeTime[0];
      this.qrcgForm.endTime = form.taskRangeTime[1];
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl + this.qrcgModel + val;
    },

    // 更新
    daterangeChange(e) {
      this.$nextTick(() => {
        this.$forceUpdate()
      })
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
    /** 任务内容项点击事件监听 */
    rowClick(row){
      if (row.source_type == 'kc'){
        window.open('/student/etp/curriculum/previewCurriculum?cid='+ row.id +'&requestFrom='+"admin" )

        // 课程预览
        // this.$router.push({
        //   path:"/etp/task/previewCurriculum/" + row.id,
        //   query:{ cid:row.id,id:this.$route.params.id }
        // });
      }
      if (row.source_type == 'ks'){
        // 考试预览
        this.$router.push({
          path:"/etp/task/previewExamination/" + row.id,
          query:{ cid:row.id,id:this.$route.params.id }
        });
      }
    },
    /** 添加课程 */
    addCourses() {
      this.dialogVisibleKC = true;
      this.$nextTick(_ => {
        this.$refs.addCurriculums.getList();
        this.$refs.addCurriculums.getTags();
      })
    },
    /** 添加课程确认操作 */
    addCourseConfirm() {
      this.dialogVisibleKC = false;
      let datas = this.$refs.addCurriculums.getCurriculumsByIds();
      this.delRepeat(this.taskArr, datas);
      this.taskArr = [...this.taskArr, ...datas]
    },
    /** 添加考试 */
    addExams() {
      this.dialogVisibleKS = true;
      this.$nextTick(_ => {
        this.$refs.addExamination.getList();
        this.$refs.addExamination.getTags();
      })
    },
    /** 添加考试确认操作 */
    addExamConfirm() {
      this.dialogVisibleKS = false;
      let datas = this.$refs.addExamination.getExamsByIds();
      this.delRepeat(this.taskArr, datas);
      this.taskArr = [...this.taskArr, ...datas]
    },
    /** 去重*/
    delRepeat(unitArr, arr) {
      if (unitArr.length > 0) {
        unitArr.forEach((item) => {
          arr.forEach((unit, i) => {
            if (item.id == unit.id && item.source_type == unit.source_type) {
              setTimeout(() => {
                this.$message.warning(item.source_name + " 已存在！");
              }, 150)
              arr.splice(i, 1);
            }
          })
        })
      }
    },
    /** 获取任务详情 */
    getTaskInfo(tid) {
      getTask(tid).then(res => {
        if (res.code == '200') {
          this.form = res.data;
          this.form.fmt =  this.form.fmt == ""|| this.form.fmt == null? this.xm_fmt:this.form.fmt;
          let {taskBeginTime, taskEndTime, kfcd, taskContentList} = res.data;
          this.form.taskRangeTime = [taskBeginTime, taskEndTime];
          let sysUsers = res.data.sysUsers;
          if (this.form.kfcd == 1){
            if (sysUsers.length > 0){
              this.showUserFlag = true;
              this.userArr = sysUsers;
            }
          }
          this.form.kfcd = parseInt(kfcd);
          if (null != res.data.tags) {
            this.tagValue = res.data.tags.split(",");
          } else {
            this.tagValue = [];
          }
          if (taskContentList.length > 0) {
            taskContentList.map(item => item.id = item.sid)
            this.taskArr = taskContentList;
          } else {
            // this.active++
          }
        }
      })
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
    /** 移除课程或者考试 */
    delItem(data, row, index) {
      this.$confirm(`是否移除【${row.source_name}】内容项?`, '提示', {
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
    /** 标签相关 */
    getTags() {
      listTagRelate({modelType: "RW"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
    /** 完成*/
    finish(states) {
      if (this.taskArr.length <= 0) {
        this.$message.warning("任务内容未设置！")
        return
      }
      if(null!= this.form.fmt && this.form.fmt.indexOf("/img/task_cover")>-1){
        this.form.fmt = null
      }
      this.addTags(this.form, this.tagValue)
      this.taskArr.map((item, index) => {
        item['sort'] = index + 1
        return item;
      })
      this.form.sysUsers =  this.userArr;
      this.form.states = states;
      this.form['taskContentList'] = this.taskArr;
      if(states == '0'){
        completeTask(this.form).then(res => {
          if (res.code == '200') {
            this.$message.success("任务暂存成功!")
          }
        })
      }else{
        this.$confirm('请确认是否发布当前任务信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          completeTask(this.form).then(res => {
            console.log(res);
            if (res.code == '200') {
              this.$message.success("任务发布成功!")
              this.createQrcg(this.form,res.data);
            }
          })
        }).catch(() => {});
      }

    },
    /** 返回 */
    back() {
      if (this.$route.query.from && this.$route.query.from === 'index'){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/index", query: { t: Date.now() }});
        return
      }
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/task", query: {t: Date.now()}})
    },
    /** 下一步 */
    next() {
      if(null!= this.form.fmt &&this.form.fmt.indexOf("/img/task_cover")>-1){
        this.form.fmt = null
      }
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("用户暂未指定!");
          return
        }
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          // if (this.modifyFlag > 3) {
          //   this.temStorage();
          // }
          this.temStorage();
          setTimeout(() => {
            ++this.active;
          }, 250)
          if (this.active >= 1) {
            this.active = 1;
          }
        }
      })
    },
    /** 上一步 */
    pre() {
      this.modifyFlag = 3;
      --this.active;


      if(null== this.form.fmt){
        this.form.fmt = this.xm_fmt
      }
      if (this.active <= 0) {
        this.active = 0;
      }
    },
    /** 暂存 */
    temStorage() {
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("用户暂未指定!");
          return
        }
      }

      if(null!= this.form.fmt && this.form.fmt.indexOf("/img/task_cover")>-1){
        this.form.fmt = null
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sysUsers =  this.userArr;
          this.addTags(this.form, this.tagValue)
          this.form.taskBeginTime = this.form.taskRangeTime[0]
          this.form.taskEndTime = this.form.taskRangeTime[1]
          this.form.states = "0"
          addTask(this.form).then(res => {
            if (res.code == '200') {
              // this.active++
              this.$message.success("任务基本信息保存成功!")
              this.form.id = res.data
              this.form.fmt = this.form.fmt==null? require("@/assets/images/task_cover.png"):this.form.fmt

            }
          })
        }
      });
    }
  }
}
</script>

<style scoped>
.app-container-custome {
  margin: 20px 5%;
  border-radius: 8px;
  border: 1px solid rgba(238, 227, 232, 0.15);
  background: rgba(249, 249, 249, 0.3);
  /*background: rgba(241, 245, 252, 0.3);*/
}

.btns {
  margin-top: 15px;
  margin-left: 10%;
}

::v-deep .el-steps--simple {
  padding: 15px 20%;
}
</style>
