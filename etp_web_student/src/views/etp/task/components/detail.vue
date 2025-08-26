<template>
  <div class="app-container-xq">
    <div style="display: flex;height: 100vh">
      <div class="head-container">
        <div class="course" v-if="isActive" >
          <!--            <div class="title">课程</div>-->

          <div class="content">
            <div class="desc-area">
              <div class="text-mc" >{{ rwmc}}</div>
              <div class="text-ms">{{ rwms }}</div>
              <div class="text-ms xx"><i class="el-icon-alarm-clock"></i>结束时间：{{ taskEndTime }} </div>
            </div>
            <el-divider></el-divider>
            <div class="ml-title"><div></div>任务目录</div>
            <el-tree
              :data="taskOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :default-expanded-keys="expandedKey"
              :filter-node-method="filterNode"
              ref="tree"
              highlight-current
              node-key="pid"
              @node-click="openNewCourse"
            >
                <span class="custom-tree-node" :style="{color:''}" slot-scope="{ node, data }">
                  <span v-if="data.type === 'kj'">
                    <i :class="{'el-icon-wc':data.rate==100,'el-icon-jd':(data.rate>0&&data.rate<100)}"></i>
                    {{ node.label }}
                  </span>
                  <span v-else-if="data.type === 'st'">
                    <span style="font-size: 12px;color:#888b8e">【题】 {{ node.label }}</span>
                  </span>
                  <span v-else>
                    {{ node.label }}
                  </span>
                </span>
            </el-tree>
          </div>
        </div>
        <!--          <div class="exam">-->
        <!--            <div class="title">考试</div>-->
        <!--            <div class="content">-->
        <!--              <ul>-->
        <!--                <li v-for="(item,index) in examData" :class="{clickData: examIndex === index}"-->
        <!--                    @click="clickExamData(item,index)">{{ item.name }}-->
        <!--                </li>-->
        <!--              </ul>-->
        <!--            </div>-->
        <!--          </div>-->
      </div>
      <el-divider direction="vertical" v-if="isActive"></el-divider>
      <div class="showContent">
        <!--          <myIframe :src="src" ref="myIframe" v-if="src"/>-->
        <Screenfull id="screenfull" class="right-menu-item hover-effect" />
        <Hamburger id="hamburger-container" :title="rwmc" :is-active="isActive" class="hamburger-container" @toggleClick="toggleSideBar" />
        <el-divider></el-divider>
        <!-- 这里是头像，修改为仅登录状态下显示头像 -->
        <el-dropdown class="avatar-container right-menu-item hover-effect" v-if="showAvatar" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatar" class="user-avatar">
            <i class="el-icon-caret-bottom" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/profile">
              <el-dropdown-item>个人中心</el-dropdown-item>
            </router-link>
            <!-- 仅开发模式下允许启动布局设置 -->
            <!--            <el-dropdown-item v-if="showSetting"  @click.native="setting = true" >-->
            <!--              <span>布局设置</span>-->
            <!--            </el-dropdown-item>-->
            <el-dropdown-item divided @click.native="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <exam-detail :exam-id="examId" v-if="examId"/>
        <study v-else :subject-val="subjectVal" :request-from="requestFrom"/>
      </div>

    </div>
  </div>
</template>

<script>

import {getCourseware, getTaskInfo,updateTaskStatus} from '@/api/etp/task/task'
import myIframe from '@/components/iFrame'
import ExamDetail from "@/views/etp/exam/components/detail";
import Study from "@/views/etp/curriculum/components/study";
import {clickSendRedis} from "@/api/buryPoint";
import {mapGetters} from "vuex";
import {getToken} from "@/utils/auth";
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'

export default {
  name: 'TaskDetail',
  components: {Study, ExamDetail, myIframe,Hamburger,Screenfull},
  data() {
    return {
      kkFileViewPath: 'http://10.153.3.13:8012/onlinePreview?url=',
      src: '',
      examId: "",
      taskOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      examIndex: null,
      examData: [],
      subjectVal: {
      },
      rwid:null,
      kjid:null,
      expandedKey:null,

      rwmc:null,
      rwms:null,
      taskEndTime:null,
      isActive:true,
      requestFrom:null,
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ]),

    showAvatar: {
      get() {
        return getToken()
      }
    }
  },
  created() {
    this.requestFrom = this.$route.query.requestFrom;
    if( this.requestFrom != 'admin'){
      document.title = "学员端 · 培训考试系统"
    }else{
      document.title = "管理端 · 培训考试系统"
    }
    this.rwid = this.$route.query.rwid;
    //进度
    this.kjid = this.$route.query.jd;
    this.getTaskDetail(this.rwid,this.kjid)
  },
  methods: {
    toggleSideBar(){
      this.isActive = !this.isActive
    },
    clickExamData(item, index) {
      this.examIndex = index;
      //销毁组件播放，展示考试组件
      this.src = '';
      this.examId = item.id;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.examIndex = null;
      this.examId = "";
      if (data) {
        const {type, id, pid,p_label} = data;
        this.expandedKey = [pid]
        this.$nextTick(()=>{
          this.$refs.tree.setCurrentKey(pid);
        })
        if(!(pid.indexOf('kc') > -1)){
          this.kjid = id
          console.log(data)
          this.subjectVal = {
            lx: "任务",
            sid: id,
            taskId: this.rwid,
            source_type: type,
            p_label: p_label
          }

          let params = {}
          params.streamName = 'stream'
          params.eventType = 'CLI_COUR'
          params.id = this.kjid
          //点击埋点
          if(this.requestFrom !='admin'){
            clickSendRedis(params).then(response =>{
              console.log("点击任务课件埋点",response)
            })
            if(type == 'kj'){
              //更新进度
              updateTaskStatus(this.rwid,this.kjid).then(res=>{
                console.log(res.code,"进度更新!!!")
              });
            }
          }
        }

      }
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    async getTaskDetail(dictId,kjid) {
      await getTaskInfo(dictId).then((res) => {
        this.rwmc = res.data.mc;
        this.rwms = res.data.ms;
        this.taskEndTime = res.data.taskEndTime;

        this.taskOptions.length = 0;
        this.examData.length = 0;
        if (res && res.data) {
          const {sources} = res.data;
          if (sources && sources.length !== 0) {
            for (const source of sources) {
              if (source) {
                const {type, name, sources: sourcesChild,id} = source;
                if (type === 'kc') {
                  //存放课程课件
                  let children = [];
                  let kjIndex = -1
                  if (sourcesChild) {
                    //对课程 进行重组，将课件后面的习题，放到课件下面
                    let temp = [];
                    for (const [index,sE] of sourcesChild.entries()) {
                      const {type: cType, name: sName, id: kjId,rate:cjRate} = sE;
                      if(cType=="kj"){
                        let json = {
                          label: index+1+"\xa0\xa0\xa0 | \xa0【课件】"+ sName,
                          type: cType,
                          id: kjId,
                          rate:cjRate,
                          pid:"kj_"+kjId,
                          p_label:name
                        }
                        kjIndex =  temp.push(json)-1

                      }else{
                        let json ={
                          label: sName.replace(/<[^>]+>/g, ''),
                          type: cType,
                          id: kjId,
                          rate:cjRate,
                          pid:"st_"+kjId,
                          p_label:name
                        }
                        if(kjIndex != -1){//有课件节点，加到课件节点下面
                          if(temp[kjIndex].children==null||temp[kjIndex].children=='undefined'){
                            temp[kjIndex].children=[];
                          }
                          temp[kjIndex].children.push(json)
                        }else{//没有课件节点，习题为主节点
                          temp.push(json);
                        }
                      }

                    }
                    children = temp
                  }
                  this.taskOptions.push({
                    children,
                    label: name,
                    pid:"kc"+id
                  });
                } else if (type === 'ks') {
                  this.examData.push(source);
                }
              }
            }
          }
        }
      });
      //打开课件内容
      this.openContent(dictId,kjid)
    },

    //打开课件内容
    openContent(rwid,kjid){
      //如果进度是null 打开第一个
      let tasks = this.taskOptions;
      if(kjid == null && tasks.length>0){
        this.handleNodeClick(tasks[0].children[0]);
      }else if(kjid != null && tasks.length>0){
        for(let i in tasks){
          if(tasks[i].children!=null&&tasks[i].children!=undefined){
            for(let j in tasks[i].children){
              if(tasks[i].children[j].id==kjid){
                this.handleNodeClick(tasks[i].children[j])
                return;
              }
            }
          }
        }
      }
    },
    openNewCourse(data){
      /** 打开树形结构 对应课件*/
      const {id} = data;
      // 打开新的浏览器标签页进行学习
      let router =  this.$router.resolve({
        path: "/taskManagement/detail/",
        query: {rwid:this.rwid, jd:id}
      });
      window.open(router.href,"_self")
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/student/index';
        })
      }).catch(() => {});
    }
  }
}
</script>
<style scoped lang="scss">
.app-container-xq {
  position: relative;
  padding: 0px;
}
.head-container {
  > div {
    height: 90vh;
    font-size: 19px;
    padding: 15px 8px 8px;
    width: calc(20vw);
    .title {
      height: 10%;
      padding: 2%;
      border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
    }

    .content {
      height: 100%;
      overflow-y: auto;
      ul {
        margin: 0;
        padding: 0;

        li {
          height: 25px;
          line-height: 25px;
          font-size: 15px;
          list-style: none;
          margin: 5px auto 0;
          padding-left: 20px;
          border-radius: 10px;
          overflow: hidden;
          width: 95%;
          white-space: nowrap;
          text-overflow: ellipsis;
          vertical-align: bottom;

          &:hover {
            background-color: rgba(0, 0, 0, 0.08);
          }

          &.clickData {
            background-color: rgba(0, 0, 0, 0.08);
          }
        }
      }
    }
  }
  .custom-tree-node{
    font-size: 14px;
    overflow: hidden;
    //width: 88%;
    //white-space: nowrap;
    //text-overflow: ellipsis;
    //vertical-align: bottom;
  }
}
.right-menu-item{
  position: absolute;
  top: 25px;
  right: 80px;
  cursor: pointer;
}
.showContent {
  width: 100%;
  //height: 90vh;
  overflow-y: auto;
  //box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%);
}
.desc-area{
  padding-left: 10px;
  height: 100px;
  position: relative;
  .text-mc{
    width: 100%;
    overflow: hidden;
    display: block;
    font-size: 18px;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-weight: bold;
  }
  .text-ms{
    color: #9a9595;
    margin-top: 2px;
    overflow: hidden;				//溢出内容隐藏
    text-overflow: ellipsis;		//文本溢出部分用省略号表示
    display: -webkit-box;			//特别显示模式
    -webkit-line-clamp: 2;			//行数
    line-clamp: 2;
    -webkit-box-orient: vertical;
    font-size: 14px;
  }

  .xx{
    position: absolute;
    bottom: 0px;
    i{
      margin-right: 5px;
    }
  }
}
.ml-title{
  display: flex;
  align-items: center;
  margin-bottom: 14px;
  font-size: 16px;
  padding-left: 10px;
  padding-top: 10px;
  div{
    height: 14px;
    width: 3px;
    background: #fa8919;
    margin-right: 4px;
  }

}
::v-deep .el-tree-node {
  margin-top: 8px;
  margin-bottom: 5px;
  width: 100%;
  white-space: normal;
  .el-tree-node__content{
    height: 100%;
    padding: 2px 70px 2px 0px;
    align-items: start;
    position: relative;
  }
}
::v-deep .el-divider--horizontal {
  margin: 0px;
}
::v-deep .el-divider--vertical {
  height: calc(100% - 10px);
  margin-top: 5px;
}
.avatar-container {
  margin-right: 30px;
  position: absolute;
  right: 0px;
  top: 0px;
  .avatar-wrapper {
    margin-top: 5px;
    position: relative;

    .user-avatar {
      cursor: pointer;
      width: 40px;
      height: 40px;
      border-radius: 10px;
      margin-top: 7px;
    }

    .el-icon-caret-bottom {
      cursor: pointer;
      position: absolute;
      right: -20px;
      top: 25px;
      font-size: 12px;
    }
  }
}
::v-deep .hamburger{
  cursor: pointer;
  height: 65px;
}

::-webkit-scrollbar-thumb{
  width: 5px;
  background-color: #e5e5e5;
}
::-webkit-scrollbar{
  width: 8px;
}

::-webkit-scrollbar-track {
  background-color: #ffffff;
}
.desc-btom{
  width: 96%;
  margin: 0 auto;
  .line-heng{
    height: 1px;
    width: 100%;
    background: #e0dcdc;
    margin: 40px 0 20px;
  }
}
::v-deep .el-icon-wc{
  background: url("~@/assets/images/icon_gkk_wc.png") center no-repeat;
  width: 16px;
  height: 16px;
  position: absolute;
  right: 10px;
  //top: 7px;
}
::v-deep .el-icon-jd{
  background: url("~@/assets/images/icon_kcxq_jd.png") center no-repeat;
  width: 16px;
  height: 16px;
  position: absolute;
  right: 10px;
  //top: 7px;
}
::v-deep .is-current {
  color: blue;
}
::v-deep .el-tree >.el-tree-node >.el-tree-node__content{
  font-weight: 700;
  font-size: 16px;
  padding-left: 8px!important;

}
::v-deep .el-icon-caret-right{
  position: absolute;
  right: 6px;
  font-size: 16px;
  top: 0px;
}
</style>
