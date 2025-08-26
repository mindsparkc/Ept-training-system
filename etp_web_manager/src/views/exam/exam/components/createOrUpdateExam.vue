<template>
  <div class="app-container app-container-custome" :style="{pointerEvents: islook?'none':'auto'}">
<!--    <el-row :gutter="24">
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
    </el-row>-->
<!--    试卷基本信息-->
<!--    <div v-show="active==0" style="margin-top: 30px">
      <el-button plain size="small" type="primary" @click="getPapers">选择试卷</el-button>
      <div v-show="selectSj.mc!=null" class="examin-div">
        <div class="paper-title">
          <span>{{ selectSj.mc }}</span>
          <div class="paper-btn">
            <el-button type="text" @click="handleView">试卷预览</el-button>
&lt;!&ndash;            <el-button type="text" @click="editPaper">编辑试卷</el-button>&ndash;&gt;
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
            <el-row>
              <el-col :span="2" v-for="(item,index) in selectSj.tx">
                <div class="div-head" v-for="(value,key) in item"  v-if="key!='fs'">
                  <span>{{key}}({{value}})</span>
                </div>
                <div style="line-height: 28px;"> {{ item.fs }}分</div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>-->
<!--    第二步 选择试题-->
    <div v-show="active==1" style="margin-top: 20px;">

      <el-row :gutter="30">
        <el-col class="left" :span="17">
          <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="demo-form-inline">
            <el-row style="height: 36px;padding-top: 10px;padding-inline: 10px">
              <span style="font-size: 15px;font-weight: bold;">考试设置：</span>
            </el-row>
              <el-card>
                <el-row :gutter="30">
                  <el-col class="child-left" :span="16">
                    <el-form-item label="考试名称：" prop="name">
                      <el-input v-model="form.name" placeholder="请输入考试名称" maxlength="100" show-word-limit></el-input>
                    </el-form-item>
                    <el-form-item label="限制考试次数：">
                      <el-col :span="10">
                        <el-switch
                          v-model="flagForm.chanceFlag"
                          active-text="开启"
                          inactive-text="关闭"
                        @change="setDefault(1)">
                        </el-switch>
                      </el-col>
                      <el-col :span="14">
                        <el-input-number v-model="form.chance" :min="0" :max="9999" size="small" :disabled="!flagForm.chanceFlag"/> （次）
                        <el-tooltip content="开启后，限制每个考生最多考试次数" placement="top-start">
                          <i class="el-icon-question"></i>
                        </el-tooltip>
                      </el-col>
                    </el-form-item>
                    <el-form-item label="限制考试时长：">
                      <el-col :span="10">
                        <el-switch
                          v-model="flagForm.totalTimeFlag"
                          active-text="开启"
                          inactive-text="关闭"
                          @change="setDefault(2)">
                        </el-switch>
                      </el-col>
                      <el-col :span="14">
                        <el-input-number v-model="form.totalTime" :min="0" :max="9999" size="small" :disabled="!flagForm.totalTimeFlag"/> （分钟）
                        <el-tooltip content="开启后，限制考试时长" placement="top-start">
                          <i class="el-icon-question"></i>
                        </el-tooltip>
                      </el-col>
                    </el-form-item>
                    <el-form-item label="最低交卷时长：">
                      <el-col :span="10">
                        <el-switch
                          v-model="flagForm.handMinTimeFlag"
                          active-text="开启"
                          inactive-text="关闭"
                          @change="setDefault(3)">
                        </el-switch>
                      </el-col>
                      <el-col :span="14">
                        <el-input-number v-model="form.handMinTime" :min="0" :max="9999" size="small" :disabled="!flagForm.handMinTimeFlag"/> （分钟）
                        <el-tooltip content="开启后，限制超过最低交卷时长，才可交卷" placement="top-start">
                          <i class="el-icon-question"></i>
                        </el-tooltip>
                      </el-col>
                    </el-form-item>
                    <el-form-item label="考试发布时间：">
                      <el-col :span="10">
                        <el-switch
                          v-model="flagForm.publishTimeFlag"
                          active-text="自定义"
                          inactive-text="立即"
                          @change="setDefault(4)">
                        </el-switch>
                      </el-col>
                      <el-col :span="14">
                        <el-date-picker
                          v-model="form.publishTime"
                          type="datetime"
                          placeholder="选择发布时间"
                          size="small"
                          style="width: 190px"
                          :disabled="!flagForm.publishTimeFlag"
                          value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                      </el-col>
                    </el-form-item>
                    <el-form-item label="限制考试时间：">
                      <el-col>
                        <el-switch
                          v-model="form.timeLimit"
                          active-text="开启"
                          inactive-text="关闭"
                          :active-value="1"
                          :inactive-value="2"
                          @change="setDefault(5)">
                        </el-switch>
                      </el-col>
                    </el-form-item>
                    <el-form-item label="考试时间限制：" v-if="form.timeLimit==1" prop="dateRange" >
                      <el-date-picker
                        :clearable = "false"
                        v-model="form.dateRange"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="考试开始时间"
                        end-placeholder="考试结束时间"
                        value-format="yyyy-MM-dd HH:mm:ss">
                      </el-date-picker>
                    </el-form-item>
                    <el-form-item label="允许迟到时间：" v-if="form.timeLimit==1" prop="lateTime">
                      <el-input-number v-model="form.lateTime" :min="0" :max="999"></el-input-number> （分钟）
                      <el-tooltip content="0：不限制迟到时间，考试限制时间段内都可以进行考试" placement="top-start">
                        <i class="el-icon-question"></i>
                      </el-tooltip>
                    </el-form-item>
                    <el-form-item label="考试分类：" prop="categoryIds">
                      <el-cascader
                        :options="categoryOptions"
                        v-model="form.categoryIds"
                        :props="{ multiple: true, checkStrictly: true, value:'id'}"
                        clearable
                        style="width: 100%"
                      size="small"></el-cascader>
                    </el-form-item>
                    <el-form-item label="学员查看成绩：" prop="showScore">
                      <el-radio-group v-model="form.showScore">
<!--                        <el-radio :label="0">不允许查看</el-radio>-->
                        <el-radio :label="1">考试结束后立即显示成绩</el-radio>
                        <el-radio :label="2">批改后立即显示成绩</el-radio>
<!--                        <el-radio :label="3">定时发布成绩</el-radio>-->
<!--                        <el-radio :label="4">手动发布成绩</el-radio>-->
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="定时发布时间：" v-if="form.showScore==3">
                      <el-date-picker
                        v-model="form.showScoreTime"
                        type="datetime"
                        placeholder="选择日期时间"
                        value-format="yyyy-MM-DD HH:mm:ss">
                      </el-date-picker>
                    </el-form-item>
                    <el-form-item label="学员查看试卷：" prop="showExam">
                      <el-radio-group v-model="form.showExam">
<!--                        <el-radio :label="0">不允许查看</el-radio>-->
                        <el-radio :label="1">仅显示学员答题情况</el-radio>
                        <el-radio :label="2">显示学员答题情况 + 答案</el-radio>
                        <el-radio :label="3">显示学员答题情况 + 答案 + 解析</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="是否显示评语：" prop="showExam">
                      <el-radio-group v-model="form.showComment">
                        <el-radio :label="0">不显示</el-radio>
                        <el-radio :label="1">显示</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>

                  <el-col class="child-right" :span="8">
                    <el-form-item label-width="0px" style="margin-bottom: 43px">
<!--                      <imageUpload v-model="form.cover" :limit="1" :uploadUrl="'/commom/upload/fastdfs'"></imageUpload>-->
                      <singleImageUpload
                        v-model="form.cover"
                        :uploadUrl="'/commom/upload/fastdfs'"></singleImageUpload>

                    </el-form-item>
                    <el-form-item label="及格分:" prop="qualifyScore" label-width="100px" style="margin-top: -38px;">
                      <el-input-number v-model="form.qualifyScore" :min="0" :max="999" size="small"></el-input-number>
                    </el-form-item>
                  </el-col>

                </el-row>

                <el-row>
                  <div>
                    <el-col style="background-color: #F9F9F8">
                      <el-form-item label="考试权限" prop="openType" label-width="80px">
                        <el-radio-group v-model="form.openType" style="margin-left: 20px">
                          <el-radio :label="1" @click.native="">公开</el-radio>
                          <el-radio :label="2">部门开放</el-radio>
                          <el-radio :label="3">指定岗位</el-radio>
                          <el-radio :label="4">限定人员</el-radio>
                          <el-radio :label="5">口令密码</el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-col v-if="form.openType==2">
                        <AssigningDepts ref="assigningDepts" style="width: 100%;"/>
                      </el-col>

                      <el-form-item prop="password" label-width="10px">
                        <el-input v-if="form.openType==5" v-model="form.password" maxlength="100"
                                  style="width: 70%" size="small"
                                  placeholder="请输入口令密码" show-word-limit></el-input>
                      </el-form-item>
                      <el-col v-if="form.openType==3" style="margin-bottom: 10px">
                        <el-button
                          type="primary"
                          plain
                          icon="el-icon-plus"
                          size="mini"
                          @click="pointPost($event)"
                          style="margin-left: 10px"
                        >新增</el-button>

                        <el-button
                          type="danger"
                          plain
                          icon="el-icon-edit"
                          size="mini"
                          :disabled="multiple"
                          @click="deleteOpenType"
                        >删除</el-button>
                      </el-col>
                      <el-table :data="postArr" v-if="form.openType==3" border @selection-change="handleSelectionChange">
                        <el-table-column type="selection" width="55" align="center" />
                        <el-table-column label="岗位编号" align="center" prop="postId" />
                        <el-table-column label="岗位编码" align="center" prop="postCode" />
                        <el-table-column label="岗位名称" align="center" prop="postName" />
                        <el-table-column label="岗位排序" align="center" prop="postSort" />
                        <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
                      </el-table>
                      <el-col v-if="form.openType==4" style="margin-bottom: 10px">
                        <el-button
                          type="primary"
                          plain
                          icon="el-icon-plus"
                          size="mini"
                          @click="pointUser($event)"
                          style="margin-left: 10px"
                        >新增</el-button>

                        <el-button
                          type="danger"
                          plain
                          icon="el-icon-edit"
                          size="mini"
                          :disabled="multiple"
                          @click="deleteOpenType"
                        >删除</el-button>
                      </el-col>
                      <el-table :data="userArr" v-if="form.openType==4" border @selection-change="userSelectionChange">
                        <el-table-column type="selection" width="55" align="center" />
                        <el-table-column label="用户编号" align="center" prop="userId" />
                        <el-table-column label="用户名称" align="center" prop="userName" />
                        <el-table-column label="用户昵称" align="center" prop="nickName" />
                        <el-table-column label="手机号码" align="center" prop="phonenumber" />
                      </el-table>
                    </el-col>
                  </div>
                </el-row>
              </el-card>
            <el-row style="height: 36px;padding-top: 10px;padding-inline: 10px">
              <span style="font-size: 15px;font-weight: bold;">防作弊选项：</span>
            </el-row>
            <el-row>
              <el-card>
                <el-form-item label="切屏后强制交卷：" label-width="130px">
                  <el-col :span="6">
                    <el-switch
                      v-model="flagForm.leaveLimitFlag"
                      active-text="开启"
                      inactive-text="关闭"
                      @change="setDefault(6)">
                    </el-switch>
                  </el-col>
                  <el-col :span="10">
                    切屏超 <el-input-number v-model="form.leaveLimit" :min="0" :max="9999" size="small" :disabled="!flagForm.leaveLimitFlag"/> 次后，强制交卷
                  </el-col>
                </el-form-item>
                <el-form-item label="无操作强制交卷：" label-width="130px">
                  <el-col :span="6">
                    <el-switch
                      v-model="flagForm.noActLimitFlag"
                      active-text="开启"
                      inactive-text="关闭"
                      @change="setDefault(7)">
                    </el-switch>
                  </el-col>
                  <el-col :span="14">
                    答题时 <el-input-number v-model="form.noActLimit" :min="0" :max="9999" size="small" :disabled="!flagForm.noActLimitFlag"/> 分钟没有新操作，将会被强制交卷
                    <el-tooltip content="限制时间内，鼠标没有动或点击，将强制交卷" placement="top-start">
                      <i class="el-icon-question"></i>
                    </el-tooltip>
                  </el-col>
                </el-form-item>
              </el-card>
            </el-row>
          </el-form>
        </el-col>
        <el-col class="right" :span="7">
            <el-row>
              <el-col :span="18" style="margin-top: 10px;padding-inline: 10px">
                <span style="font-size: 15px;font-weight: bold">试卷信息：</span>
              </el-col>
              <el-col :span="6">
                <el-button type="text" @click="handleView">预览</el-button>
                <el-button type="text" @click="getPapers">更换</el-button>
              </el-col>
            </el-row>
            <el-card>
              <div class="paper-content">
                <el-row>
                  <el-col :span="9">
                    <div class="paper-text">试卷名称：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ selectSj.mc }}</div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="9">
                    <div class="paper-text">试卷总分：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ selectSj.fzNum }}分</div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="9">
                    <div class="paper-text">建议考试时长：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ selectSj.suggestTime }}分钟</div>
                  </el-col>
                </el-row>
                <el-row v-if="selectSj.qualifyScore!=null">
                  <el-col :span="9">
                    <div class="paper-text">建议及格分数：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ selectSj.qualifyScore }}分</div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="9">
                    <div class="paper-text">创建人：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ selectSj.createBy }}</div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="9">
                    <div class="paper-text">创建时间：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ parseTime(selectSj.createTime) }}</div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="9">
                    <div class="paper-text">更新时间：</div>
                  </el-col>
                  <el-col :span="15">
                    <div>{{ parseTime(selectSj.updateTime) }}</div>
                  </el-col>
                </el-row>
                <div style="margin-top: 20px;margin-bottom: 20px;width: 95%">
                  <el-row class="div-head" style="border-top: 1px solid #dddddd;">
                    <el-col :span="10">大题</el-col>
                    <el-col :span="8">题目数</el-col>
                    <el-col :span="6">分值</el-col>
                  </el-row>
                  <el-row v-for="(item,index) in selectSj.tx" class="div-head">
                    <el-col :span="10">
                      <div v-for="(value,key) in item">
                        <span v-if="key!='fs'">{{key}}</span>
                      </div>
                    </el-col>
                    <el-col :span="8">
                      <div v-for="(value,key) in item">
                        <span v-if="key!='fs'">{{value}}</span>
                      </div>
                    </el-col>
                    <el-col :span="6">
                      <div > {{ item.fs }}</div>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-card>
        </el-col>
      </el-row>


    </div>
    <div class="btns">
      <div class="btns-bt">
        <el-button v-show="buttonFlag!=0" @click="qx">取 消</el-button>
<!--        <el-button v-if="active > 0" @click="preStep">上一步</el-button>-->
        <el-button v-if="active < 1" @click="nextStep">下一步</el-button>
        <el-button v-show="buttonFlag!=0" v-if="active ==1" type="primary" @click="submitForm('1')">暂存</el-button>
        <el-button v-show="buttonFlag!=0" v-if="active ==1" type="primary" @click="submitForm('2')">发布</el-button>
      </div>
    </div>

<!--选择试卷-->
    <paperList
      ref="paper"
      @confirm="confirm"></paperList>

<!-- 限定用户    -->
    <el-dialog title="限定用户"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleUser"
               width="1000px">
      <AssigningUsers ref="assigningUsers" :xz_data="userArr"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUser = false">取 消</el-button>
        <el-button type="primary" @click="pointUserConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="限定岗位"
               :close-on-click-modal="false"
               :visible.sync="dialogVisiblePost"
               width="1000px">
      <AssigningPosts ref="assigningPosts" :xz_data="postArr"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisiblePost = false">取 消</el-button>
        <el-button type="primary" @click="pointPostConfirm">确 定</el-button>
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
      <div class="tip"><i class="el-icon-warning" style="color: #f3663a"></i>请确认是否发布考试？</div>
      <div class="ks-con">
        <span class="title">信息内容</span>
        <div class="con-body" >
          <div>考试名称:{{this.form.name}}</div>
          <div v-if="this.flagForm.publishTimeFlag==1">考试发布时间:{{this.form.publishTime}}</div>
          <div v-if="this.form.timeLimit==1">考试开始-结束时间:{{ this.form.startTime }}~{{this.form.endTime}}</div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="fbVisible=false">取 消</el-button>
        <el-button type="primary" @click="saveInfo('2')">确 定</el-button>
      </span>
    </el-dialog>
</div>
</template>

<script>
import { listPaper, getPaper } from '@/api/paper/paper';
import { getExam, addExam, updateExam,checkExistKS } from "@/api/exam/exam";
import Qrcg from "@/views/etp/qrcg/index";
/** @type update 2021-12-31 引入指定用户组件 */
import AssigningUsers from "@/components/AssigningUsers/index"
import AssigningPosts from "@/components/AssigningPosts/index"
import AssigningDepts from "@/components/AssigningDepts/index"
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import {getTreeselect} from '@/api/exam/examCategory'
import Template from '@/views/etp/certificateTemplate/index'
import {userList,postList} from '@/api/common/index'
import singleImageUpload from '@/components/singleImageUpload'
import paperList from "@/views/exam/exam/components/paperList";

export default {
  name: "editexamination",
  components: {
    Template,
    AssigningUsers,
    Qrcg,
    Treeselect,
    AssigningPosts,
    AssigningDepts,
    singleImageUpload,
    paperList
  },
  data() {
    var checkStartTime = (rule, value, callback) => {
      if(this.form.timeLimit==1){
        if (!value) {
          return callback(new Error('考试开始时间不能为空'));
        }else {
          let dateRange = this.form.dateRange
          let fbTime = new Date(dateRange[0]).getTime()
          let endTime = new Date(dateRange[1]).getTime();
          let rel = new Date(value).getTime()
          if(fbTime-rel>=0){
            this.form.startTime = null;
            return  callback(new Error('考试开始时间不能早于考试发布时间！'))
          }else if(endTime-rel<=0){
            this.form.startTime = null;
            return callback(new Error('考试开始时间不能晚于考试结束时间！'));
          }else{
            callback();
          }
        }
      }else{
        callback()
      }
    };
    var checkLateTime = (rule, value, callback) =>{
      if(this.form.timeLimit==1){
        if(!value){
          return callback(new Error('请填写考试允许迟到时间！'))
        }else{
          let dateRange = this.form.dateRange
          let startTime = new Date(this.form.startTime).getTime();
          let endTime = new Date(dateRange[1]).getTime();
          let lateTime = value*60*1000
          if((endTime-startTime)<=lateTime){
            callback(new Error('允许迟到时间不能晚于考试结束时间'))
            this.form.lateTime = 15;
            return
          }else{
            callback();
          }
        }
      }else{
        callback()
      }
    };
    var checkMc = (rule, value, callback) => {
      if (!value) {
        callback(new Error('考试名称不能为空'));
      }else{
        checkExistKS(this.form.name.toString(),this.form.examId==null?0:this.form.examId).then(response => {
          let flag = response.data
          if(flag){
            callback(this.$message.error("考试名称已存在，请修改！"));
          }else{
            callback()
          }
        });
      }
    };

    var checkShowScoreTime = (rule, value, callback) => {
      if(this.form.showScore == 3){
        if(!value){
          return callback(new Error('请选择考试成绩发布时间！'))
        }else{
          let date = this.form.dateRange;
          let endTime = new Date(date[1]).getTime();
          let showScoreTime = new Date(value).getTime();
          if((showScoreTime-endTime)<0){
            return callback('考试成绩发布时间不能早于考试结束时间！')
          }else{
            callback()
          }
        }
      }else{
        callback()
      }
    };
    var checkCategoryIds = (rule,value,callback) =>{
      if(value.length<=0){
        return callback(new Error('请选择考试分类！'))
      }else{
        callback()
      }
    };
    return {
      // 非多个禁用
      multiple: true,
      active:1,


      loading:false,
      zjclOptions:[],
      fbOptions:[],
      // 试卷表格数据
      testpaperList: [],
      paperUnit:[], //试卷详情

      //选择的试卷
      selectRow: {},
      selectSj:{
      },

      form:{
        /** @type update 2021-12-31 设置开放程度默认值为公开*/
        examId:null,
        categoryIds: [],
        name:'',
        totalTime:0,
        handMinTime:0,
        chance:0,
        leaveLimit:0,
        noActLimit:0,
        answerDriver:0,
        showComment:0,
        openType:1,
        lateTime:0,
        publishTime:null,
        endTime:'',
        dateRange:[],
        paperId:null,
        timeLimit:2,
        showScore:1,
        showExam:1,
        password:null,
        openRules:[],
        qualifyScore:60
      },

      radioDisable:false,
      fsdj_tmp:[],//[{key:'',value:[]}]

      dtcsRadio:0,
      rules:{
        name: [{
          required: true,
          validator: checkMc,
          trigger: 'blur'
        }],
        dateRange: [
          { required: true,
            validator: checkStartTime,
            type:'array',
            message: "请选择考试时间",
            trigger: 'blur' ,
            fields:{
               0:{required: true,message: "请选择考试发布时间",trigger: 'change'},
               1:{required: true,message: "请选择考试结束时间",trigger: 'change'}
            }
          }
        ],
        // //考试开始时间
        // startTime:[
        //   {required: true, validator: checkStartTime, trigger: 'blur'},
        // ],
        categoryIds: [
          {type:'array', required: true, validator: checkCategoryIds, message: "请选择考试分类", trigger: ["change","blur"]}
        ],
        chance: [
          {required: true, message: "请输入考试次数", trigger: "blur"}
        ],
        totalTime: [
          {required: true, trigger: "blur"}
        ],
        lateTime: [
          {required: true, validator:checkLateTime, trigger: ["blur","change"]}
        ],
        showScoreTime: [
          {required: true, validator:checkShowScoreTime, trigger: "blur"}
        ],
      },

      /**  @type update 开放程度指定数组 */
      showUserFlag:false,
      dialogVisibleUser:false,
      dialogVisiblePost:false,
      dialogVisibleDept:false,
      userArr:[],
      postArr:[],
      deptArr:[],
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
      // scoreSetRadio3:{
      //   day:3,
      //   time:9
      // },
      scoreSetTime:null,
      pushTimeSet:0,
      //发布
      fbVisible:false,
      ksInfo:null,

      categoryOptions:[],
      pwdFlag:false,
      activeNames:[],

      //新页面开关参数
      flagForm:{
        chanceFlag:false,
        totalTimeFlag:true,
        handMinTimeFlag:false,
        publishTimeFlag:false,
        // examTimeFlag:false,

        leaveLimitFlag:false,
        noActLimitFlag:false
      },

      statusOptions:[],
      postIds:[],
      userIds:[],
      allLoading:null,

      islook:false,

      buttonFlag:null
    };
  },
  activated() {

  },
  created() {
    this.allLoading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });

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
    debugger
    this.buttonFlag = this.$route.query.flag;
    if(this.buttonFlag == 0){//详情
      this.islook = true
      let route = Object.assign({}, this.$route, { title: `考试详情` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    }else{
      let route = Object.assign({}, this.$route, { title: `考试编辑` })
      this.$store.dispatch('tagsView/updateVisitedView',route)
      this.islook = false
    }
    this.form.examId = this.$route.query.examId;
    const examId = this.$route.query.examId;
    const paperId = this.$route.query.paperId;
    // this.form.fm = ETP_DEFAULT_IMAGE_URL
    if(examId){
      debugger
      //获取试卷信息回显
      getPaper(paperId).then(response =>{
        console.log(examId)
        this.selectSj = response.data
        // this.paperBack(response.data)
        this.nextStep()
        //试卷回显
        this.paperBack( this.selectSj);
      })
    }else {
      console.log("试卷回显")
      debugger
      this.selectSj = this.$route.query.sjData;
      this.form.name = this.selectSj.name;//新增
      if (typeof(this.form.name) === "undefined") {
        this.allLoading.close()
        this.$message("请选择试卷")
      }else {
        this.form.totalTime = this.selectSj.suggestTime//建议考试时长
        // this.form.totalTime = this.selectRow.suggestTime;
        if(this.selectSj.qualifyScore==null && this.selectSj.totalScore>2){
          this.form.qualifyScore = Math.ceil(this.selectSj.totalScore*0.6)
        }else {
          this.form.qualifyScore = this.selectSj.qualifyScore;//建议考试分数
        }
        this.form.cover =  this.form.cover == ""|| this.form.cover == null? require("@/assets/images/examine_cover.png") :  this.form.cover;
        //试卷回显
        this.paperBack( this.selectSj);
      }

    }
    getTreeselect().then(res => {
      this.categoryOptions = res.data
    })
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  watch:{
    //推送时间变为立即推送
    pushTimeSet(val){
      if(val == 0){
        this.$set(this.form,"pushTime",this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss'))
      }
    },
    //分数设置 类型改成 第三种
    // "form.scoreSet":{
    //   handler: function(val, oldVal) {
    //     if(val=='2'){
    //       this.getScoreShowTime()
    //     }else {
    //       this.$set(this.form,"showScoreTime",null)
    //     }
    //   },
    //   immediate: true
    // },
    //分数设置是第三种时， 展示时间根据填写数字变化
    // scoreSetRadio3:{
    //   handler: function(val, oldVal) {
    //     this.getScoreShowTime()
    //   },
    //   deep: true,
    //   immediate: true
    // },
    // "form.dateRange":{
    //   handler: function(val, oldVal) {
    //     this.getScoreShowTime()
    //   },
    //   deep: true,
    //   immediate: true
    // }
    "form.showScore":{
        handler: function(val, oldVal) {
          if(val !=3){
            this.form.showScoreTime = null
          }
        },
        deep: true,
    }
  },
  methods: {
    //关闭或开启对应开关，对相应的参数赋值或重置.
    setDefault(type){
      if(!this.flagForm.chanceFlag && type==1){
        this.form.chance = 0
      }

      if(!this.flagForm.handMinTimeFlag && type==3){
        this.form.handMinTime = 0
      }else if(this.flagForm.handMinTimeFlag && type==3){
        this.form.handMinTime = Math.ceil(this.selectSj.suggestTime /3)
      }

      if(this.flagForm.totalTimeFlag && type==2){
        this.form.totalTime = this.selectSj.suggestTime
      }else if(!this.flagForm.totalTimeFlag && type==2){
        this.form.totalTime = 0
      }
      if(this.flagForm.publishTimeFlag && type == 4){
        this.form.publishTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      }else if(!this.flagForm.publishTimeFlag && type == 4){
        this.form.publishTime = null
      }
      if(this.form.timeLimit ==1 && type == 5){ //限制
        //初始化考试时间
        let start = this.$moment().day(this.$moment().day() + 1).format('YYYY-MM-DD HH:mm:ss')
        let end = this.$moment().day(this.$moment().day() + 3).format('YYYY-MM-DD HH:mm:ss')
        this.$set(this.form,"dateRange",[start,end])
        // this.form.dateRange = [start,end]
      }else if(!this.form.timeLimit ==1 && type == 5){
        this.form.dateRange = []
        this.form.startTime = null
        this.form.endTime =null
        this.form.lateTime = 0

      }

      if(!this.flagForm.leaveLimitFlag && type == 6){
        this.form.leaveLimit = 0
      }else if(this.flagForm.leaveLimitFlag && type == 6){
        this.form.leaveLimit = 3
      }

      if(!this.flagForm.noActLimitFlag && type == 7){
        this.form.noActLimit = 0
      }else if(this.flagForm.noActLimitFlag && type == 7){
        this.form.noActLimit = 5
      }
    },

    //删除已选中的职位和人员信息
    deleteOpenType(){
      if(this.form.openType==3){
        if(this.postIds!=[]){
          for (let i = 0; i < this.postIds.length; i++) {
            this.postArr.forEach((item,key) =>{
              if(this.postIds[i]==item.postId){
                this.postArr.splice(key,1);
              }
            })
          }
        }
      }else{
        if(this.userIds != []){
          for (let i = 0; i < this.userIds.length; i++) {
            this.userArr.forEach((item,key) =>{
              if(this.userIds[i]==item.userId){
                this.userArr.splice(key,1);
              }
            })
          }
        }
      }
    },

    // 多选框选中数据userSelectionChange
    handleSelectionChange(selection) {
      this.postIds = selection.map(item => item.postId)
      this.multiple = !selection.length
    },

    // 多选框选中数据
    userSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId)
      this.multiple = !selection.length
    },

    //试卷预览
    handleView(){
      const id = this.selectSj.id
      // this.reset();
      let router = this.$router.resolve({
        path: "/paper-view/view",
        query: { paperId: id}
      })
      window.open(router.href,'_blank')
    },

    // 岗位状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },

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
      // if(this.form.dateRange!=undefined && this.form.dateRange.length>0){
      //   let end = this.form.dateRange[1]
      //   let showTime = this.$moment(end).add(this.scoreSetRadio3.day, 'days').format('YYYY-MM-DD')
      //   let time = this.scoreSetRadio3.time;
      //   if(time == undefined){
      //     time="00"
      //   }else{
      //     time = time > 9 ? time:`0${time}`;
      //   }
      //   showTime += ` ${time}:00:00`;
      //   this.$set(this.form,"showScoreTime",showTime)
      // }

    },
    /** 关闭二维码页面并跳转至查询页面 */
    closeQrcg(){
      this.dialogVisibleQrcg = false;
      this.$router.push({path: "/exam/exam", query: {t: Date.now()}})
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
        this.qrcgForm.value=this.qrcgUrl + this.qrcgModel +val.examId;
      }
      console.log(this.qrcgUrl,this.qrcgModel)
      this.dialogVisibleQrcg = true;
      this.qrcgForm.title = val.name==undefined?this.form.name:val.name;
      this.qrcgForm.size = 150;
      this.qrcgForm.startTime=val.startTime==undefined?this.form.startTime:val.startTime;
      this.qrcgForm.endTime=val.endTime==undefined?this.form.endTime:val.endTime;
    },

    qx(){
      if (this.$route.query.from && this.$route.query.from === 'index'){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/exam/exam", query: { t: Date.now() }});
        return
      }
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/exam/exam", query: {t: Date.now()}})
    },

    /**  @type update  2021-12-31 指定用户开始 */
    pointUserConfirm(){

      let arr = this.$refs.assigningUsers.xz_data;
      if (arr.length <= 0){
        this.$message.warning("未选择用户！");
        return
      }
      this.dialogVisibleUser = false;
      // this.userArr = this.mergeArray(this.userArr,arr,4);
      this.userArr = arr
      // this.userLoading = true;
    },
    pointUser(e){
      if (e.target.tagName === 'INPUT') return
      this.dialogVisibleUser = true;
      this.$nextTick(_=>{
        this.$refs.assigningUsers.getList();
      })
    },
    clearUser(e){
      if (e.target.tagName === 'INPUT') return
      this.userArr = [];
      this.showUserFlag = false;
    },
    handleClose(date,type){
      if(type == 4) this.userArr = this.userArr.filter(u => { return u.userId != date.userId })
      if(type == 3) this.postArr = this.postArr.filter(u => { return u.postId != date.postId })
      if(type == 2) this.deptArr = this.deptArr.filter(u => { return u.deptId != date.deptId })
    },
    mergeArray(arr1,arr2,type){
      let _arr = [];
      for(let i = 0;i < arr1.length;i++){
        _arr.push(arr1[i]);
      }
      for(let i = 0;i < arr2.length;i++){
        let flag = true;
        for(let j = 0;j < arr1.length;j++){
          if(type == 4){
            if(arr2[i]['userId'] == arr1[j]['userId']){
              flag = false;
              break;
            }
          }
          if(type == 3){
            if(arr2[i]['postId'] == arr1[j]['postId']){
              flag = false;
              break;
            }
          }
          if(type == 2){
            if(arr2[i]['children']!=null){

            }
            if(arr2[i]['deptId'] == arr1[j]['deptId']){
              flag = false;
              break;
            }
          }

        }
        if(flag){
          _arr.push(arr2[i]);
        }
      }
      return _arr;
    },

    /**  @type update  2021-12-31 指定岗位开始 */
    pointPostConfirm(){
      let arr = this.$refs.assigningPosts.xz_data;
      if (arr.length <= 0){
        this.$message.warning("未选择岗位！");
        return
      }
      this.dialogVisiblePost = false;
      // this.postArr = this.mergeArray(this.postArr,arr,3);
      this.postArr = arr
    },
    pointPost(e){
      if (e.target.tagName === 'INPUT') return
      this.dialogVisiblePost = true;
      this.$nextTick(_=>{
        this.$refs.assigningPosts.getList();
      })
    },

    /**  @type update  2021-12-31 指定部门开始 */
    pointDeptConfirm(){
      let arr = this.$refs.assigningDepts.getChoiceDept();
      if (arr.length <= 0){
        this.$message.warning("未选择部门！");
        return
      }
      this.dialogVisibleDept = false;
      this.deptArr = this.mergeArray(this.deptArr,arr,2);
      this.showUserFlag = true;
    },
    pointDept(e){
      if (e.target.tagName === 'INPUT') return
      this.dialogVisibleDept = true;
      this.$nextTick(_=>{
        this.$refs.assigningDepts.getList();
      })
    },

    /** 2021-12-31 指定用户结束 */

    //下一步
    nextStep() {
      //第一步
      if(this.selectSj.paperId){
        //回显考试信息@
        //修改
        if(this.$route.query.examId){
          getExam(this.$route.query.examId).then(response =>{
            //防止第一步换了试卷，response取到的还是原来的试卷id
            let newSjid = this.form.paperId
            this.form = response.data
            this.form.paperId = newSjid
            this.form.timeLimit = parseInt(response.data.timeLimit);
            this.form.openType = parseInt(response.data.openType);
            this.form.showScore = parseInt(response.data.showScore);
            this.form.showExam = parseInt(response.data.showExam);
            this.form.showComment = parseInt(response.data.showComment);
            this.form.answerDriver = parseInt(response.data.answerDriver);
            /** @type update 2021-12-31 设置开放程度回显 */
            // this.form.kfcd = parseInt(response.data.kfcd);
            // this.fsdj_tmp = JSON.parse(response.data.fsdj)

            // /** @type update 2021-12-31 限定用户回显*/
            // let sysUsers = response.data.data;
            // if(this.form.openType == 4){
            //   if (sysUsers.length > 0){
            //     this.showUserFlag = true;
            //     this.userArr = sysUsers;
            //   }
            // }

            //考试时间回显
            if(response.data.timeLimit==1){
              let date = [];
              date[0] = response.data.startTime;
              date[1] = response.data.endTime;
              this.$set(this.form,'dateRange',date);
            }
            // if(response.data.startTime){
            //   this.$set(this.form,'lateTime',response.data.lateTime);
            // }
            //图片回显
            this.form.cover =  this.form.cover == ""|| this.form.cover == null? require("@/assets/images/examine_cover.png") :  this.form.cover;

            //成绩设置回显
            // if(this.form.scoreSet =='2'){
            //   let showday = this.$moment(this.form.showScoreTime.substring(0,10))
            //   let endDay = this.$moment(this.form.ksjssj.substring(0,10))
            //   let days = showday.diff(endDay,'days')
            //   this.$set(this.scoreSetRadio3,"day",days)
            //   this.$set(this.scoreSetRadio3,"time",this.form.showScoreTime.substring(11,13))
            // }

            // //部门回显
            if(this.form.openType==2){
              this.$nextTick( _ => {
                this.$refs.assigningDepts.setCheckedKeys(this.form.openRules)
              })
            }else if(this.form.openType==3){ //岗位
              postList(this.form.openRules).then(res=>{
                this.postArr = res.data
              })
            }else if(this.form.openType==4){ //用户
              userList(this.form.openRules).then(res=>{
                this.userArr = res.data
              })
            }
            //控制开关 初始化开关
            if(this.form.chance>0){
              this.flagForm.chanceFlag =true
            }
            if(this.form.totalTime>0){
              this.flagForm.totalTimeFlag =true
            }
            if(this.form.handMinTime>0){
              this.flagForm.handMinTimeFlag =true
            }
            if(this.form.publishTime!=null){
              this.flagForm.publishTimeFlag =true
            }
            if(this.form.leaveLimit>0){
              this.flagForm.leaveLimitFlag =true
            }
            if(this.form.noActLimit>0){
              this.flagForm.noActLimitFlag =true
            }

            //判断selectRow 有没有数据，有的话 说明重新选择了试卷，修改及格分数 和限制考试时长
            if(this.selectRow.qualifyScore!=undefined){
              this.form.qualifyScore = this.selectRow.qualifyScore
              if(this.flagForm.totalTimeFlag){
                this.form.totalTime = this.selectRow.suggestTime
              }
            }
            this.allLoading.close()
          }).catch(error=>{
            this.allLoading.close()
          })
        }else{
          //初始化名称
          this.$set(this.form,"name",this.selectSj.mc)
          this.form.paperId = this.selectSj.id;

          //立即推送时间
          // this.$set(this.form,"pushTime",this.$moment().day(this.$moment().day()).format('YYYY-MM-DD HH:mm:ss'))
          //图片回显

          this.form.cover =  this.form.cover == ""|| this.form.cover == null? require("@/assets/images/examine_cover.png") :  this.form.cover;
          // this.form.categoryIds = this.selectSj.categoryIds
          // this.getScoreShowTime()
        }

        this.active = 1
      }else{
        this.allLoading.close()
        this.$message("请选择试卷")
      }

    },
    //上一步
    preStep() {
      this.active = 0
      if(null!= this.form.cover && this.form.cover.indexOf("/img/ex")>-1){
        this.form.cover = null
      }
    },
    getPapers(){
      //回显选中的试卷
      this.$refs.paper.getPapers(this.selectSj.paperId)

      // this.sj_open = true;
      // this.handleQuery();
    },

    confirm(val){
      // this.selectSj = this.selectRow
      // let paperId = this.selectRow.paperId
      this.selectSj = val
      this.selectRow = val
      this.form.name = this.selectRow.name;//新增
      // this.form.totalTime = this.selectRow.suggestTime;//建议考试时长
      // this.form.qualifyScore = this.selectRow.qualifyScore;//建议考试分数
      if(this.selectRow.qualifyScore==null && this.selectRow.totalScore>2){
        this.form.qualifyScore = Math.ceil(this.selectRow.totalScore*0.6)
      }else {
        this.form.qualifyScore = this.selectRow.qualifyScore;//建议考试分数
      }
      //试卷回显
      this.paperBack(val);
    },
    //试卷回显
    paperBack(selData){
      debugger
      let tx = [];
      if(selData.paperId!=null&&selData.paperId!=undefined){
        getPaper(selData.paperId).then(res =>{
          this.paperUnit = res.data.paperUnit
          for(const key in this.paperUnit){
            let test = {};
            let ruleCount = 0;
            if(this.paperUnit[key].quType == 1){
              if(this.paperUnit[key].unitRule!=null){
                let rule = this.paperUnit[key].unitRule
                for (const ruleKey in rule) {
                  if(rule[ruleKey].score!=null){
                    ruleCount += rule[ruleKey].score;
                  }
                }
              }
              test['单选题'] = this.paperUnit[key].quCount
              if(ruleCount == 0){
                test['fs'] = this.paperUnit[key].eachScore
              }else{
                test['fs'] = ruleCount
              }
              ruleCount = 0;
            }
            if(this.paperUnit[key].quType == 2){
              if(this.paperUnit[key].unitRule!=null){
                let rule = this.paperUnit[key].unitRule
                for (const ruleKey in rule) {
                  if(rule[ruleKey].score!=null){
                    ruleCount += rule[ruleKey].score;
                  }
                }
              }
              test['复选题'] = this.paperUnit[key].quCount
              if(ruleCount == 0){
                test['fs'] = this.paperUnit[key].eachScore
              }else{
                test['fs'] = ruleCount
              }
              ruleCount = 0;
            }
            if(this.paperUnit[key].quType == 3){
              if(this.paperUnit[key].unitRule!=null){
                let rule = this.paperUnit[key].unitRule
                for (const ruleKey in rule) {
                  if(rule[ruleKey].score!=null){
                    ruleCount += rule[ruleKey].score;
                  }
                }
              }
              test['判断题'] = this.paperUnit[key].quCount
              if(ruleCount == 0){
                test['fs'] = this.paperUnit[key].eachScore
              }else{
                test['fs'] = ruleCount
              }
              ruleCount = 0;
            }
            if(this.paperUnit[key].quType == 4){
              if(this.paperUnit[key].unitRule!=null){
                let rule = this.paperUnit[key].unitRule
                for (const ruleKey in rule) {
                  if(rule[ruleKey].score!=null){
                    ruleCount += rule[ruleKey].score;
                  }
                }
              }
              test['填空题'] = this.paperUnit[key].quCount
              if(ruleCount == 0){
                test['fs'] = this.paperUnit[key].eachScore
              }else{
                test['fs'] = ruleCount
              }
              ruleCount = 0;
            }
            if(this.paperUnit[key].quType == 5){
              if(this.paperUnit[key].unitRule!=null){
                let rule = this.paperUnit[key].unitRule
                for (const ruleKey in rule) {
                  if(rule[ruleKey].score!=null){
                    ruleCount += rule[ruleKey].score;
                  }
                }
              }
              test['问答题'] = this.paperUnit[key].quCount
              if(ruleCount == 0){
                test['fs'] = this.paperUnit[key].eachScore
              }else{
                test['fs'] = ruleCount
              }
              ruleCount = 0;
            }
            if(this.paperUnit[key].quType == 6){
              if(this.paperUnit[key].unitRule!=null){
                let rule = this.paperUnit[key].unitRule
                for (const ruleKey in rule) {
                  if(rule[ruleKey].score!=null){
                    ruleCount += rule[ruleKey].score;
                  }
                }
              }
              test['组合题'] = this.paperUnit[key].quCount
              if(ruleCount == 0){
                test['fs'] = this.paperUnit[key].eachScore
              }else{
                test['fs'] = ruleCount
              }
              ruleCount = 0;
            }
            tx.push(test);
          }
          this.allLoading.close()
        }).catch(error=>{
          this.allLoading.close()
        })
      }
      console.log(tx);
      this.$set(this.selectSj,'tx',tx)
      this.selectSj.id = selData.paperId//试卷id
      this.selectSj.mc = selData.name//试卷名称
      this.selectSj.createBy = selData.createBy
      this.selectSj.createTime = selData.createTime
      this.selectSj.updateTime = selData.updateTime
      this.selectSj.fzNum = selData.totalScore//试卷总分数
      this.selectSj.count = selData.quCount//试卷题目总数
      this.selectSj.qualifyScore = selData.qualifyScore//试卷建议及格分
      this.selectSj.suggestTime = selData.suggestTime//试卷建议考试时长
      this.form.paperId = selData.paperId
      // this.$forceUpdate();
    },

    getDepts(data){
      data.forEach((child,index) => {
        if(child.children!=null){
          return this.getDepts(child)
        }else{
          return child.deptId
        }
      })
    },

    //测试提交按钮
    submitForm(fb){
      if(this.form.timeLimit ==1 ){
        let dates = this.form.dateRange;
        this.form.startTime = dates[0];
        this.form.endTime = dates[1];
      }
      // if(fb=='2'&&this.form.timeLimit==1){
      //   /** 判断发布时 考试开始时间晚于当前时间 半小时*/
      //   console.log(new Date(this.form.kskssj).getTime(),new Date().getTime())
      //   let time = new Date(this.form.startTime).getTime()- new Date().getTime() >= 0
      //   if(!time){
      //     let options={
      //       message:"考试开始时间须晚于当前时间！",
      //       showClose:true,
      //       duration:10000
      //     }
      //     this.$message.warning(options)
      //     return;
      //   }
      // }
      //校验定时发布考试结果展示时间
      // if(this.form.showScore=='3'){
      //   let options={
      //     message:null,
      //     showClose:true,
      //     duration:10000
      //   }
      //   if(!this.form.showScoreTime){
      //     return this.$message.warning("请选择定时发布成绩时间 ！")
      //   }else{
      //     if(new Date(this.form.showScoreTime).getTime()<=new Date(this.form.endTime).getTime()){
      //       options.message = "定时成绩发布时间需要晚于考试结束时间 ！"
      //       return this.$message.warning(options)
      //     }
      //   }
      // }


      if(this.form.openType == 2){
        this.pointDeptConfirm()
        let ids = [];
        for (const key in this.deptArr) {
          ids[key] = this.deptArr[key].deptId
        }
        this.form.openRules = ids;
      }
      if(this.form.openType == 3){
        let ids = [];
        for (const key in this.postArr) {
          ids[key] = this.postArr[key].postId
        }
        this.form.openRules = ids;
      }
      if(this.form.openType == 4){
        let ids = [];
        for (const key in this.userArr) {
          ids[key] = this.userArr[key].userId
        }
        this.form.openRules = ids;
      }
      if(this.form.timeLimit==1){

      }
      if(fb=='2'){
        console.log(this.form)
        this.fbVisible = true
      }else{
        this.saveInfo(fb)
      }
    },

    //提交按钮
    saveInfo(fb){
      this.form.status = fb;
      let fm = this.form.cover
      if(null!= this.form.cover && this.form.cover.indexOf("/img/examine_cover")>-1){
        fm = null
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.examId != null) {
            updateExam({...this.form,...{cover:fm}}).then(response => {
              this.msgSuccess(response.msg);
              if(response.code ==200 && fb == '2'){
                console.log(this.form,'修改')
                this.createQrcg(this.form,'修改')
              }
            });
          } else {
            addExam({...this.form,...{cover:fm}}).then(response => {
              this.msgSuccess(response.msg);
              this.form.examId = response.data.examId
              if(null == this.form.cover){
                this.form.cover = require("@/assets/images/examine_cover.png")
              }
              console.log(response,'新增')
              if(response.code ==200 && fb=='2'){
                this.createQrcg(response.data.examId,'新增')
              }
            });
          }
        }else{
          if(this.form.categoryIds.length==0){
            this.$message.error("请选择考试分类！")
          }
        }
      });
    },

    back(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path:"/exam/exam/index"
        , query: {t: Date.now()}
      });
    },

    // 表单重置
    reset() {
      this.form = {
        categoryIds: [],
        name:'',
        totalTime:90,
        handMinTime:30,
        chance:0,
        leaveLimit:0,
        noActLimit:5,
        answerDriver:0,
        showComment:1,
        openType:1,
        lateTime:15
      };
      this.resetForm("form");
      // this.scoreSetRadio3 ={
      //   day:3,
      //   time:9
      // },
      this.fsdj_tmp = null;
      this.active = 1
    },
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
.btns-bt {
  margin-top: 30px;
  position: relative;
  height: 36px;
}
.btns-bt{
  //position: absolute;
  //right: 0px;
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
  //margin-top: 30px;
  p{
    margin-left: 20px;
  }
}
.paper-content > div{
  font-size: 13px;
  margin: 15px 5px;
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
  padding-top: 5px;
  padding-bottom: 5px;
  text-align: center;
}
.el-col-2 {
  border: 1px solid #dddddd;
  text-align: center;
}
.el-row div{
  //line-height: 32px;
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

::v-deep .el-collapse-item__header{
  font-size: 20px;
  padding-left: 40%;
}


::v-deep .el-radio-group{
  line-height: 50px;
}

::v-deep .el-form-item--medium .el-form-item__label{
  font-weight: normal;
}

.paper-text{
  text-align: right;
}
.right{
  margin-bottom: 60px;
}
.btns{
  position: fixed;
  bottom: 20px;
  right: 50px;
}
i{
  pointer-events: auto;
}
</style>
