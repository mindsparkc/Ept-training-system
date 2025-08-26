<template>
  <div class="app-container">
    <el-row :gutter="20" style="display: flex">
      <div class="showContent">
        <exam-detail :exam-id="examId" v-if="examId"/>
        <study v-else :subject-val="subjectVal"/>
      </div>
      <div class="head-container">
        <div class="course">
          <!--            <div class="title">课程</div>-->
          <div class="content">
            <div style="width: 95%">
              <span class="text-mc" :title="rwmc">{{ rwmc }}</span>
            </div>
            <el-divider></el-divider>
            <el-tree
              :data="taskOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :default-expanded-keys="expandedKey"
              :filter-node-method="filterNode"
              ref="tree"
              highlight-current
              node-key="pid"
              @node-click="handleNodeClick"
            >
                <span class="custom-tree-node" :style="{color:''}" slot-scope="{ node, data }">
                  <span v-if="data.type === 'kj'">
<!--                    <i class="el-icon-success" style="color:green" v-if="data.rate==100"></i>-->
                    【课件】{{ node.label }}
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
      </div>
    </el-row>
  </div>
</template>

<script>

import { getTaskInfo,} from '@/api/etp/task'
import Study from "@/views/etp/curriculum/components/study";

export default {
  name: 'TaskDetail',
  components: {Study},
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
      rwmc:''
    }
  },
  created() {
    this.rwid = this.$route.query.rwid;
    //进度
    this.kjid = this.$route.query.jd;
    this.getTaskDetail(this.rwid,this.kjid)
  },
  methods: {
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
        const {type, id, pid} = data;
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
            source_type: type
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
        this.taskOptions.length = 0;
        this.examData.length = 0;
        console.log(res);
        if (res && res.data) {
          this.rwmc = res.data.mc;
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
                    for (const sE of sourcesChild) {
                      const {type: cType, name: sName, id: kjId,rate:cjRate} = sE;
                      if(cType=="kj"){
                        let json = {
                          label: sName,
                          type: cType,
                          id: kjId,
                          rate:cjRate,
                          pid:"kj_"+kjId
                        }
                        kjIndex =  temp.push(json)-1
                      }else{
                        let json ={
                          label: sName,
                          type: cType,
                          id: kjId,
                          rate:cjRate,
                          pid:"st_"+kjId
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
    }
  }
}
</script>
<style scoped lang="scss">
.head-container {
  > div {
    height: calc(86vh - 62px);
    font-size: 19px;
    box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%);

    width: calc(20vw - 15px);
    margin-left: 15px;
    .title {
      height: 10%;
      padding: 2%;
      border-bottom: 1px rgba(0, 0, 0, 0.08) solid;
    }

    .content {
      height: 100%;
      overflow-y: auto;
      padding-top: 15px;
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
    width: 88%;
    white-space: nowrap;
    text-overflow: ellipsis;
    vertical-align: bottom;
  }
}

.showContent {
  width: 100%;
  height: calc(86vh - 64px);
  overflow-y: auto;
  box-shadow: 0 2px 4px rgb(0 0 0 / 12%), 0 0 6px rgb(0 0 0 / 4%);
}

.text-mc{
  width: 100%;
  overflow: hidden;
  display: block;
  font-size: 16px;
  margin-left: 10px;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.el-divider--horizontal{
  margin: 10px 0;
}
</style>
