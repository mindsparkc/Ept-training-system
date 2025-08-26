<template>
  <div class="app-container app-container-custome">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-steps :active="active" simple finish-status="success">
          <el-step>
            <slot slot="title">第一步：问卷基本信息</slot>
          </el-step>
          <el-step>
            <slot slot="title">第二步：组织问卷内容</slot>
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
    <br/>

    <!--问卷基本信息-->
    <transition name="el-fade-in-linear">
      <div v-if="active === 0">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="问卷名称" prop="wjmc">
            <el-input v-model="form.wjmc" placeholder="请输入问卷名称" maxlength="100" show-word-limit/>
          </el-form-item>
          <el-form-item label="设置标签" prop="tagValue">
            <tagsEdit widthSty="100%"
                      :parentValue="tagValue"
                      :selectOptions="tagOptions"
                      @selectTags="havaTags">

            </tagsEdit>
          </el-form-item>
          <el-form-item label="启用状态" prop="qyzt">
            <el-radio-group v-model="form.qyzt">
              <el-radio v-for="dict in qyztOptions" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="封面图"  prop="fm">
            <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="form.fm"/>
          </el-form-item>
          <el-form-item label="问卷描述" prop="ms">
            <el-input type="textarea" v-model="form.ms" placeholder="请输入问卷描述" maxlength="300" show-word-limit/>
          </el-form-item>

          <el-form-item label="时间范围" prop="rangeTime">

            <el-date-picker clearable  size="small"
                v-model="form.rangeTime"
                @input="daterangeChange"
                type="daterange"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
                >
            </el-date-picker>

<!--            <el-date-picker clearable size="small"
                            v-model="form.beginTime"
                            type="date"
                            :picker-options="pickerOptions"
                            value-format="yyyy-MM-dd"
                            placeholder="选择开始时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker clearable size="small"
                            v-model="form.endTime"
                            type="date"
                            :picker-options="pickerOptions1"
                            value-format="yyyy-MM-dd"
                            placeholder="选择结束时间">
            </el-date-picker>-->
          </el-form-item>
          <el-form-item label="开放程度" prop="kfcd">
            <el-radio-group v-model="form.kfcd">
              <el-radio @click.native="pointUser(dict.dictValue,$event)" v-for="dict in kfcdOptions" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- @type  update
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

          <el-form-item label="是否匿名" prop="sfnm">
            <el-radio-group v-model="form.sfnm">
              <el-radio v-for="dict in sfnmOptions" :label="dict.dictValue">{{ dict.dictLabel }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input  type="textarea" v-model="form.remark" placeholder="请输入备注" maxlength="300" show-word-limit/>
          </el-form-item>
        </el-form>
      </div>
    </transition>

    <!--组织问卷内容-->
    <transition name="el-fade-in-linear">
      <div v-if="active !== 0">
        <template v-if="questionContents.length <= 0">
          <el-alert
            title="问卷内容为空，请先组织问卷相关内容！"
            type="warning"
            >
          </el-alert>
        </template>
        <template v-if="questionContents.length > 0">
          <el-alert
            title="鼠标拖动可更改顺序"
            type="warning"
            effect="dark">
          </el-alert>
          <vuedraggable v-model="questionContents">
            <transition-group>
              <div v-for="(item,index) in questionContents" :key="index">
                <div style="font-size:18px;margin-top: 15px;position: relative">
                  {{ index +1 }} 、{{ item.tg }} 【{{ typeDel(item.type) }}】
                  <div style="position: absolute;top: 0;right: 50px">
                    <i @click="editQ(item,index)" class="el-icon-edit" style="margin-right: 20px;cursor: pointer;color: skyblue" />
                    <i @click="delQ(item,index)" class="el-icon-delete" style="cursor: pointer;color:red;" />
                  </div>
                </div>
                <p style="margin-left: 20px;font-size:16px;"
                   v-if="item.xxList && item.xxList.length > 0"
                   v-for="(option,i) in item.xxList" :key="i">
                  {{ option.key }} 、{{ option.value }}
                </p>
              </div>
            </transition-group>
          </vuedraggable>
        </template>
        <div style="margin-top: 20px">
          <el-button type="primary" @click="addQuestion('0')" plain icon="el-icon-plus" size="mini">单选题</el-button>
          <el-button type="primary" @click="addQuestion('1')" plain icon="el-icon-plus" size="mini">复选题</el-button>
<!--          <el-button type="primary" @click="addQuestion('2')" plain icon="el-icon-plus" size="mini">填空题</el-button>-->
          <el-button type="primary" @click="addQuestion('3')" plain icon="el-icon-plus" size="mini">问答题</el-button>
        </div>
        <div>
        </div>
      </div>
    </transition>

    <div class="btns">
      <el-button @click="back" v-if="active === 0">取 消</el-button>
      <el-button @click="tempStore" type="primary"  v-if="active === 0">暂 存</el-button>
      <el-button @click="nextStep"  v-if="active === 0">下一步</el-button>
      <el-button @click="preStep" v-if="active > 0">上一步</el-button>
      <el-button @click="submit('0')" type="primary" v-if="active === 1">暂 存</el-button>
      <el-button @click="submit('99')" type="primary" v-if="active === 1">发 布</el-button>
    </div>


    <el-dialog
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="1000px" append-to-body>
      <el-form ref="questionform" :model="questionform" :rules="qrules"  label-width="100px">

        <el-form-item label="题干：" prop="tg">
          <el-input v-model="questionform.tg" ref="tg" placeholder="请输入内容" />
        </el-form-item>

        <div v-if="questionform.type =='0'|| questionform.type == '1'">
          <el-form-item label="选项：" v-for="(item, index) in questionform.xxList" class="xx-div"
                        :key="item.key"
                        :prop="'xxList.' + index + '.value'"
                        :rules="{ required: true, message: '选项不能为空', trigger: 'blur'}"
                        :class="{'labNone':index != 0}">
            <el-input placeholder="请输入选项" v-model="item.value" :class="'input_' + index">
              <template slot="prepend">{{item.key}}</template>
            </el-input>
            <i class="el-icon-remove-outline" v-show="index != 0" @click="del_xx(index)"></i>
          </el-form-item>
          <el-button class="add-btn" @click="add_xx()">+ 新增选项</el-button>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="yes">确 定</el-button>
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

  </div>
</template>

<script>
/** @type update  引入指定用户组件 */
import AssigningUsers from "@/components/AssigningUsers"
import {addQuestionnaire, getQuestionnaire, updateQuestionnaire} from "@/api/etp/questionnaire";
import vuedraggable from 'vuedraggable';
import {uuid} from "@/utils/ruoyi";
import tagsEdit from "@/components/tags/tagsEdit"
import {listTagRelate} from "@/api/etp/tag/tag";
import {ETP_DEFAULT_IMAGE_URL} from "@/utils/ruoyi";
import Qrcg from "@/views/etp/qrcg";
import { completeTask } from '@/api/etp/task'
export default {
  name: "addOrUpdateQuestionnaire",
  components:{vuedraggable,tagsEdit,AssigningUsers,Qrcg },
  data() {
    return {
      tempStoreFlag:false,
      title:'',
      open:false,
      active: 0,
      form:{
        sfnm: '1',
        qyzt: '0',
        kfcd: '0',
        rangeTime:[],
      },
      kfcdOptions:null,
      qyztOptions:null,
      sfnmOptions:null,
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
      // 表单校验
      rules: {
        wjmc: [
          {required: true, message: "请输入问卷名称", trigger: "blur"}
        ],
        // fm: [
        //   {required: true, message: "请设置封面", trigger: "blur"}
        // ],
        qyzt: [
          {required: true, message: "请设置启用状态", trigger: "blur"}
        ],
        /*beginTime: [
          {required: true, message: "请设置开始时间", trigger: "blur"}
        ],
        endTime: [
          {required: true, message: "请设置结束时间", trigger: "blur"}
        ],*/
        rangeTime:[
          {required: true, message: "请设置时间范围", trigger: "blur"}
        ],
        kfcd: [
          {required: true, message: "请设置开放程度", trigger: "blur"}
        ],
      },
      questionform:{
        xxList:[{ key:'A',value:'' }],
      },
      qrules:{
        tg:[
          { required: true, message: "题干不能为空", trigger: "blur" }
        ],
      },
      letterArr:[],

      // 存放问卷内容
      questionContents:[],

      globalId:'',

      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],


      /**限定用户*/
      dialogVisibleUser:false,
      showUserFlag:false,
      userArr:[],

      pickerOptions: {
        disabledDate: function(time){
          return time < Date.now()  - 8.64e7;
        }
      },
    }
  },

  created() {
    this.getTags();
    this.getDicts("question_status").then(response => {
      this.qyztOptions = response.data;
    });
    this.getDicts("question_kfcd").then(response => {
      this.kfcdOptions = response.data;
    });
    this.getDicts("question_sfnm").then(response => {
      this.sfnmOptions = response.data;
    });
    //获取二维码地址
    this.getConfigKey("cp.qrcg.question").then(response => {
      this.qrcgModel = response.msg;
    });
    this.getConfigKey("cp.qrcg.url").then(response => {
      this.qrcgUrl = response.msg;
    });
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');

    this.form.fm = require("@/assets/images/cover.jpg")
    const id = this.$route.query.id;
    if (id !== undefined) {
      this.getQuestion(id);
    }
  },
  methods:{

    /** 关闭二维码页面并跳转至查询页面 */
    closeQrcg(){
      this.dialogVisibleQrcg = false;
      this.back();
    },
    /** 生成二维码 */
    createQrcg(val) {
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = val.wjmc;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=val.beginTime;
      this.qrcgForm.endTime=val.endTime;
      if(this.qrcgUrl.search('http') == -1){
        this.qrcgUrl = window.location.protocol+'//'+window.location.host+ this.qrcgUrl;
      }
      this.qrcgForm.value=this.qrcgUrl + this.qrcgModel + val.id + '';
    },

    pointUser(labelValue,e){
      if (e.target.tagName === 'INPUT') return
      // 弹框
      if (labelValue == '1'){
        this.dialogVisibleUser = true;
        this.$nextTick(_=>{
          this.$refs.assigningUsers.handleQuery();
        })
      }

    },
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

    /**标签相关开始*/
    getTags() {
      listTagRelate({modelType: "WJ"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
    // 标签相关结束

    editQ(data,index){
      data['op'] = 'edit'
      this.questionform = data;
      this.open = true;
    },

    delQ(data,index){
      let tg = data.tg
      this.$confirm('是否删除'+ tg +'?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.questionContents.splice(index,1);
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {});
    },

    getQuestion(id){
      getQuestionnaire(id).then(response => {
        this.form = response.data;
        if (this.form.nr != '' && this.form.nr != null) {
          this.questionContents = JSON.parse(this.form.nr);
        }
        if (null != response.data.tags) {
          this.tagValue = response.data.tags.split(",");
        } else {
          this.tagValue = [];
        }
        //this.form.beginTime   this.form.endTime
        this.form.rangeTime = [];
        this.form.rangeTime.push(this.form.beginTime,this.form.endTime)
        // this.$set(this.form.rangeTime,'daterange',[this.form.beginTime,this.form.endTime])
        /** @type update 限定用户回显*/
        let sysUsers = response.data.sysUsers;
        //图片回显
        this.form.fm =  this.form.fm == ""|| this.form.fm == null? require("@/assets/images/cover.jpg") :  this.form.fm;

        if(this.form.kfcd == 1){
          if (sysUsers.length > 0){
            this.showUserFlag = true;
            this.userArr = sysUsers;
          }
        }
      });
    },
    /** 下一步 */
    nextStep(){
      this.tempStoreFlag = true;
      this.$refs["form"].validate( validate => {
        if (validate){
          this.tempStore();
          this.active ++;
        }
      })
    },
    /** 上一步*/
    preStep(){
      this.active --
      if(null== this.form.fm){
        this.form.fm = require("@/assets/images/cover.jpg")
      }
    },
    tempStore(){

      this.form.beginTime = this.form.rangeTime[0];
      this.form.endTime =  this.form.rangeTime[1];
      this.form.fb = '0';
      /** @type update  限定用户提交前校验开始*/
      this.form.sysUsers = this.userArr;
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("请指定用户！")
          return
        }
      }
      if(null!= this.form.fm && this.form.fm.indexOf("/img/cover")>-1){
        this.form.fm = null
      }
      /** 校验指定用户逻辑结束*/
      this.$refs["form"].validate(flag => {
        if (flag) {
          if (this.form.id != null) {
            updateQuestionnaire(this.addTags(this.form, this.tagValue)).then(response => {
              if(this.tempStoreFlag == false){
                this.msgSuccess("暂存成功!");
              }
            });
          } else {
            addQuestionnaire(this.addTags(this.form, this.tagValue)).then(response => {
              this.form.id = response.data;
              if(this.tempStoreFlag==false){
                this.msgSuccess("暂存成功!")
              }
            });
          }

        }
      })
    },
    submit(status){

      this.form.beginTime = this.form.rangeTime[0];
      this.form.endTime =  this.form.rangeTime[1];

      if (this.questionContents.length <= 0){
        this.$message.warning("问卷内容不能为空！");
        return
      }
      /** @type update  限定用户提交前校验开始*/
      this.form.sysUsers = this.userArr;
      if (this.form.kfcd == '1'){
        if (this.userArr.length <= 0){
          this.$message.warning("请指定用户！")
          return
        }
      }
      /** 校验指定用户逻辑结束*/
      let jsonStr = JSON.stringify(this.questionContents)
      this.form.nr = jsonStr;
      this.form.fb = status;
      if(null!= this.form.fm && this.form.fm.indexOf("/img/cover")>-1){
        this.form.fm = null
      }
      if(status == '99'){
        this.$confirm('请确认是否发布当前问卷信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          updateQuestionnaire(this.addTags(this.form, this.tagValue)).then(response => {
            this.msgSuccess("发布成功");
            this.createQrcg(this.form)
          });
        }).catch(() => {});
      }else{
        updateQuestionnaire(this.addTags(this.form, this.tagValue)).then(response => {
          this.msgSuccess("暂存成功");
        });
      }
    },

    typeDel(type){
      let t = '';
      switch (type) {
        case '0':
          t = '单选题';break;
        case '1':
          t = '复选题';break;
        case '2':
          t = '填空题';break;
        case '3':
          t = '问答题';break;
        default:
          break;
      }
      return t;
    },
    addQuestion(type){
      // 0 单选题 1 复选题 2 填空题 3 问答题
      this.questionform.type = type;
      this.open = true;
      let t = '';
      switch (type) {
          case '0':
            t = '单选题';break;
          case '1':
            t = '复选题';break;
          case '2':
            t = '填空题';break;
          case '3':
            t = '问答题';break;
          default:
            break;
      }
      this.title = `添加问卷题目【${t}】`;
      this.$nextTick(_=>{
        this.$refs.tg.focus()
      })

    },
    add_xx(){
      let arr = this.letterArr;
      this.questionform.xxList.push({key:'',value:''});
      this.questionform.xxList.forEach(function (item, index) {
        item.key = arr[index]
      });

      this.$nextTick(_=>{
        let index = this.questionform.xxList.length - 1;
        let elementNode = document.querySelector(".input_" + index);
        elementNode.children[1].focus()
      })
    },
    del_xx(index){
      let arr = this.letterArr;
      this.questionform.xxList.splice(index,1);
      this.questionform.xxList.forEach(function (item, index) {
        item.key = arr[index]
      })
    },
    yes(){
      this.$refs["questionform"].validate(valid => {
        if (valid){
          let type = this.questionform.type;
          if (type == 2 || type == 3){
            if (this.questionform.op && this.questionform.op == 'edit'){
              this.questionContents.forEach((item,index) => {
                if (this.questionform.id == item.id){
                  this.questionContents.splice(index,0,this.questionform);
                  this.questionContents.splice(index + 1,1);
                }
              })
            }else {
              let temp = { tg:this.questionform.tg,type:type,id:uuid() }
              this.questionContents.push(temp);
            }
          }else {
            if (this.questionform.op && this.questionform.op == 'edit'){
              this.questionContents.forEach((item,index) => {
                if (this.questionform.id == item.id){
                  this.questionContents.splice(index,0,this.questionform);
                  this.questionContents.splice(index + 1,1);
                }
              })
            }else {
              let temp = { tg:this.questionform.tg,type:type,xxList:this.questionform.xxList,id:uuid() };
              this.questionContents.push(temp);
            }
          }
          this.open = false;
          this.questionform = {
            xxList:[{ key:'A',value:'' }],
          };
        }
      });
    },
    cancel(){
      this.open = false;
      this.questionform = {
        xxList:[{ key:'A',value:'' }],
      };
    },
    back(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/questionnaire", query: {t: Date.now()}})
    },
    // 更新
    daterangeChange(e) {
      this.$nextTick(() => {
        this.$forceUpdate()
      })
    }
  }
}
</script>

<style scoped>
.el-dialog .el-input{
  width:95%  !important;
}
.el-icon-remove-outline{
  margin-left: 10px;
  color: red;
  cursor: pointer;
}
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
.xx-div{
  color: #a7a6a6;
}
.add-btn{
  width: 30%;
  border: 1px dashed #DCDFE6;
  margin: 10px 15px 22px 100px;
}
</style>
