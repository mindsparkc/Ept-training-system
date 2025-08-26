<template>
  <!-- 课程预览-->
  <div class="app-container">
<!--    <div>-->
<!--      <el-button @click="back()" type="primary" style="margin-left: 94%">返回</el-button>-->
<!--    </div>-->

    <div class="pre-list pre-pad">
      <div style="width: 95%">
        <span class="text-mc" :title="kcmc">{{ kcmc }}</span>
      </div>
      <el-divider></el-divider>
      <el-tree :data="treeData" :props="defaultProps"
               highlight-current
               :filter-node-method="filterNode"
               ref="tree" @node-click="handleNodeClick">
      </el-tree>
    </div>
    <!-- 课程内容  -->
    <div class="pre-content pre-pad">
        <div class="el-empty__image" v-show="tipFlag">
          <svg viewBox="0 0 79 86" version="1.1" xmlns="http://www.w3.org/2000/svg"
               xmlns:xlink="http://www.w3.org/1999/xlink">
            <defs>
              <linearGradient id="linearGradient-1-10" x1="38.8503086%" y1="0%" x2="61.1496914%" y2="100%">
                <stop stop-color="#FCFCFD" offset="0%"></stop>
                <stop stop-color="#EEEFF3" offset="100%"></stop>
              </linearGradient>
              <linearGradient id="linearGradient-2-10" x1="0%" y1="9.5%" x2="100%" y2="90.5%">
                <stop stop-color="#FCFCFD" offset="0%"></stop>
                <stop stop-color="#E9EBEF" offset="100%"></stop>
              </linearGradient>
              <rect id="path-3-10" x="0" y="0" width="17" height="36"></rect>
            </defs>
            <g id="Illustrations" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
              <g id="B-type" transform="translate(-1268.000000, -535.000000)">
                <g id="Group-2" transform="translate(1268.000000, 535.000000)">
                  <path id="Oval-Copy-2"
                        d="M39.5,86 C61.3152476,86 79,83.9106622 79,81.3333333 C79,78.7560045 57.3152476,78 35.5,78 C13.6847524,78 0,78.7560045 0,81.3333333 C0,83.9106622 17.6847524,86 39.5,86 Z"
                        fill="#F7F8FC"></path>
                  <polygon id="Rectangle-Copy-14" fill="#E5E7E9"
                           transform="translate(27.500000, 51.500000) scale(1, -1) translate(-27.500000, -51.500000) "
                           points="13 58 53 58 42 45 2 45"></polygon>
                  <g id="Group-Copy"
                     transform="translate(34.500000, 31.500000) scale(-1, 1) rotate(-25.000000) translate(-34.500000, -31.500000) translate(7.000000, 10.000000)">
                    <polygon id="Rectangle-Copy-10" fill="#E5E7E9"
                             transform="translate(11.500000, 5.000000) scale(1, -1) translate(-11.500000, -5.000000) "
                             points="2.84078316e-14 3 18 3 23 7 5 7"></polygon>
                    <polygon id="Rectangle-Copy-11" fill="#EDEEF2"
                             points="-3.69149156e-15 7 38 7 38 43 -3.69149156e-15 43"></polygon>
                    <rect id="Rectangle-Copy-12" fill="url(#linearGradient-1-10)"
                          transform="translate(46.500000, 25.000000) scale(-1, 1) translate(-46.500000, -25.000000) "
                          x="38" y="7" width="17" height="36"></rect>
                    <polygon id="Rectangle-Copy-13" fill="#F8F9FB"
                             transform="translate(39.500000, 3.500000) scale(-1, 1) translate(-39.500000, -3.500000) "
                             points="24 7 41 7 55 -3.63806207e-12 38 -3.63806207e-12"></polygon>
                  </g>
                  <rect id="Rectangle-Copy-15" fill="url(#linearGradient-2-10)" x="13" y="45" width="40"
                        height="36"></rect>
                  <g id="Rectangle-Copy-17" transform="translate(53.000000, 45.000000)">
                    <mask id="mask-4-10" fill="white">
                      <use xlink:href="#path-3-10"></use>
                    </mask>
                    <use id="Mask" fill="#E0E3E9"
                         transform="translate(8.500000, 18.000000) scale(-1, 1) translate(-8.500000, -18.000000) "
                         xlink:href="#path-3-10"></use>
                    <polygon id="Rectangle-Copy" fill="#D5D7DE" mask="url(#mask-4-10)"
                             transform="translate(12.000000, 9.000000) scale(-1, 1) translate(-12.000000, -9.000000) "
                             points="7 0 24 0 20 18 -1.70530257e-13 16"></polygon>
                  </g>
                  <polygon id="Rectangle-Copy-18" fill="#F8F9FB"
                           transform="translate(66.000000, 51.500000) scale(-1, 1) translate(-66.000000, -51.500000) "
                           points="62 45 79 45 70 58 53 58"></polygon>
                </g>
              </g>
            </g>
          </svg>
          <div class="desc">课程学习</div>
        </div>
        <!-- 课件预览 -->
        <div style="width: 100%;height: calc(100% - 5px);overflow:hidden;"
             v-show="!tipFlag && kjFlag">
<!--          <iframe ref="iframe" style="width: 100%;height: 100%;overflow:scroll;border:0;" v-if="iframeFlag"-->
<!--                  :src="url" sandbox="allow-top-navigation allow-same-origin allow-forms allow-scripts"/>-->
<!--          office-->
          <div style="height: 100%" v-if="iframeFlag">
            <scrollLoad :list="picList" :total="picTotal" :host="picHost"></scrollLoad>
          </div>
<!--          视频-->
          <video height="100%" controls autoplay v-if="kjlx=='2'" style="display: block;margin: 0 auto;">
            <source :src="url" >
          </video>
          <div v-show="richFlag" style="height: 100%;overflow:auto;" v-html="nr"></div>
<!--          外链-->
          <div class="a-item" v-show="aHref.flag" >
            <div class="a-content">课件名称：{{aHref.name}}</div>
            <div class="a-content">课件地址：{{aHref.address}}</div>
            <div class="a-content">点击【继续访问】即将离开本站，在新的页面打开课件内容</div>
            <el-divider></el-divider>
            <el-button type="primary" @click="visitUrl(aHref.address)">继续访问</el-button>
          </div>
        </div>
        <!--  试题预览-->
        <div style="width: 100%;height: 100%;overflow:auto;padding: 15px" v-show="!tipFlag && stFlag">
          <el-form ref="form" :model="subjectForm" label-width="100px" size="small">
            <el-form-item label="试题名称：">
              <span>{{ subjectForm.tg }}</span>
            </el-form-item>
            <el-form-item label="试题类型：">
              <!-- 单选题：1 复选题：2 判断题：3 填空题：4 问答题：5   -->
              <span v-if="subjectForm.lx == '1'">单选题</span>
              <span v-if="subjectForm.lx == '2'">复选题</span>
              <span v-if="subjectForm.lx == '3'">判断题</span>
              <span v-if="subjectForm.lx == '4'">填空题</span>
              <span v-if="subjectForm.lx == '5'">问答题</span>
            </el-form-item>
            <el-form-item label="请选择：" v-if="subjectForm.lx == '3'">
              <el-radio-group v-model="pd">
                <el-radio label="正确"></el-radio>
                <el-radio label="错误"></el-radio>
              </el-radio-group>
              <el-row>
                <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
              </el-row>
            </el-form-item>


            <el-form-item label="请填写：" v-if="subjectForm.lx =='4'">
              <el-input v-model="tk" placeholder="请输入答案" v-if="subjectForm.lx =='4'" style="width: 60%"/>
              <el-row>
                <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
              </el-row>
            </el-form-item>
            <el-form-item label="请填写：" v-if="subjectForm.lx == '5'">
              <el-input type="textarea" :rows="2" placeholder="请输入答案" v-model="zg" v-if="subjectForm.lx == '5'" style="width: 60%"/>
              <el-row>
                <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
              </el-row>
            </el-form-item>
            <el-form-item label="选项内容：" v-if="subjectForm.lx != '3' && subjectForm.lx !='5' && subjectForm.lx != '4'">
              <template v-if="subjectForm.lx == '1'">
<!--              <span :key="index" v-for="(item,index) in subjectForm.xxList">{{ item.key }} 、 {{-->
<!--                  item.value-->
<!--                }} <br/></span>-->
                <el-radio-group v-model="dx">
                  <el-radio v-for="(item,index) in subjectForm.xxList" :label="item.key" :key="item.key" @change="onInput()">
                    {{ item.key }} 、{{ item.value }}
                  </el-radio>
                </el-radio-group>
                <el-row>
                  <el-button @click="showDa()" type="success" style="margin-top: 2%">提交</el-button>
                </el-row>
              </template>
              <template v-if="subjectForm.lx == '2'">
<!--              <span :key="index" v-for="(item,index) in subjectForm.xxList">{{ item.key }} 、 {{-->
<!--                  item.value-->
<!--                }} <br/></span>-->

                <el-checkbox-group v-model="duo">
                  <el-checkbox v-for="(item,index) in subjectForm.xxList" :label="item.key" :key="item.key" @change="onInput()">{{ item.key }} 、{{ item.value }}</el-checkbox>
                </el-checkbox-group>
                <el-button type="success" @click="showDa()">提交</el-button>
              </template>
            </el-form-item>
            <el-form-item label="试题答案：" v-if="dxt || dxts || pdt || tkt || zgt">
              <template v-if="subjectForm.lx == '3'">
                <span>{{ subjectForm.da == '1' ? '正确' : '错误' }}</span>
              </template>
              <template v-if="subjectForm.lx != '3'">
                <span>{{ subjectForm.da }}</span>
              </template>

            </el-form-item>
            <el-form-item label="试题解析：" v-if="dxt || dxts || pdt || tkt || zgt">
              <span>{{ subjectForm.jx }}</span>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
</template>

<script>
import { getCurriculum } from '@/api/etp/curriculum/curriculum'
import {getCourseware} from "@/api/etp/courseware/courseware";
import {getSubject} from "@/api/etp/subject/subject";
import {FILE_PREVIEW_URL} from "@/utils/ruoyi";
import scrollLoad from "@/components/scrollLoad/index"

export default {
  name: "previewCurriculum",
  components:{
    scrollLoad
  },
  data() {
    return {
      tipFlag: true,
      kjFlag: false,
      stFlag: false,
      iframeFlag: false,
      richFlag: false,
      dxt: false,
      dxts: false,
      pdt: false,
      tkt: false,
      zgt: false,
      nr: '',
      url: '',
      filterText: '',
      curriculum: {},
      treeData: [
        {label: '一级 1',}
        // {label : '1、js公开课'}
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      subjectForm: {},
      dx: '',
      duo:[],
      pd: '',
      tk: '',
      zg: '',
      tags:[],
      cid:'',
      kjlx: '',
      aHref: {
        flag:false,
        name:null,
        address:null
      },
      kcmc:'',
      //文件图片
      picList:[],
      picTotal:0,
      picHost:""
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {

    this.cid = this.$route.query.cid;
    // console.log("id="+this.cid);
    if (this.cid !== undefined) {
      this.getCurriculum(this.cid)
    }
  },

  methods: {
    showDa(){
      if( this.tk != '' || this.zg != '' || this.duo != '' || this.pd != '' || this.dx !=''){
        this.dxts = true;
        this.pdt = true;
        this.tkt = true;
        this.zgt = true;
        this.dxt = true;
      }else{
        this.$message.error("请答题后提交！")
      }
    },
    //强制刷新方法
    onInput(){
      this.$forceUpdate();
    },

    /** 返回*/
    back() {
      this.$store.dispatch("tagsView/delView", this.$route);
      // this.$router.push({path: '/etp/curriculum/',query: {t: Date.now()}})
      this.$router.back();
    },
    /** 搜索过滤*/
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 标志位重置*/
    flagR() {
      this.kjFlag = false
      this.stFlag = false
      this.iframeFlag = false
      this.richFlag = false
      this.dxt = false
      this.dxts = false
      this.pdt = false
      this.tkt = false
      this.zgt = false
      this.aHref.flag = false;
    },
    /** 每次选择试题时，清空试题答案 */
    clearSt(){
      this.dx = ''
      this.duo = []
      this.pd = ''
      this.tk = ''
      this.zg = ''
    },

    /** 依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身*/
    handleNodeClick(row, node, self) {
      let {sid, source_type} = row;
      this.flagR();
      this.clearSt();
      this.tipFlag = false;
      this.kjlx = null;
      if(source_type =='kj'){
        // 预览课件
        this.kjFlag = true;
        getCourseware(sid).then(res => {
          /**课件形式 Office：1 ,视频：2 ,富文本：3 ,外部链接：4 ,其他文件：5*/
          let {xs, kjdz, nr, lj,mc,metaData} = res.data;
          this.kjlx = xs;
          if (xs == '1' || xs == '5') {
            this.iframeFlag = true;
            // this.$nextTick(_ => {
            //   this.url = FILE_PREVIEW_URL + '?url=' + encodeURIComponent(window.btoa(kjdz));
            // })
            let { list,size } = JSON.parse(metaData)

            this.picList = list
            this.picTotal = size
            // this.picHost = "http://10.153.3.12/profile/upload/2022/05/19/b3e72a4c-cd18-4bf2-bfed-2b04953409a4/"
            // this.picHost = 'http://10.153.3.12'+kjdz.substring(21,kjdz.indexOf("."))+'/'
            this.picHost = kjdz.substring(0,kjdz.lastIndexOf(".")) +"/"
          }

          if(xs == '2'){
            this.iframeFlag = false;
            this.$nextTick(_ => {
              this.url = kjdz;
            })
          }
          if (xs == '4') {

            this.aHref.flag = true;
            this.aHref.name = mc
            this.aHref.address = lj
          }
          if (xs == '3') {
            this.richFlag = true;
            this.nr = nr;
          }
        })
      }else if(source_type == 'st'){
        //预览试题
        this.stFlag = true;
        getSubject(sid).then(res => {
          if (res.code == '200') {
            this.subjectForm = res.data;
            if(this.subjectForm.jx == '' || this.subjectForm.jx == null){
              this.subjectForm.jx = '无'
            }
            /** 类型：单选题：1 复选题：2 判断题：3 填空题：4 问答题：5*/
            //选项回显
            if (this.subjectForm.lx == '1') this.subjectForm.xxList = JSON.parse(res.data.xx);
            if (this.subjectForm.lx == '2') this.subjectForm.xxList = JSON.parse(res.data.xx);
          }
        })
      }

    },
    /** 获取课程详情*/
    getCurriculum(id) {
      getCurriculum(id).then(res => {
        // console.log(res)
        if (res.code == '200') {
          this.curriculum = res.data
          this.kcmc = res.data.mc
          let contents = res.data.tCurriculumContents;
          contents.map((item,index) => item['label'] = ((index+1) + "." + (item.source_type == 'kj' ? ' 【课件】' : ' 【试题】') + item.source_name ))
          this.treeData = contents
          if(null != contents){
            this.handleNodeClick(contents[0])
          }
        }
      })
    },
//访问外链地址
    visitUrl(address){
      this.$confirm('将在新的标签页打开课件?', '即将离开本站', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        window.open(address,'_blank')
      }).catch(() => {
      });
    }

  }
}
</script>

<style scoped lang="scss">
.app-container {
  height: calc(100vh - 84px);
  position: relative;
}

.el-empty__image {
  width: 300px;
  margin: 0 auto;
  text-align: center;
}
.pre-desc:before{
  content: "";
  width: 3px;
  height:20px;
  border-radius: 5px;
  position: absolute;
  top: 0;
  left: 0;
  background: blue;
}
.desc {
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 4px;
}

.pre-font {
  font-size: 16px;
  color: rgb(56, 58, 62);
  line-height: 30px;
}

.pre-pad {
  padding: 15px 8px 8px;
}

::v-deep .pre-list {
  overflow: auto;
}

::v-deep .pre-list .el-tree-node {
  margin-top: 8px;
  margin-bottom: 5px;
  width: 100%;
  white-space: nowrap !important;
  text-overflow:ellipsis !important;
  overflow:hidden !important;
}

::v-deep .pre-list .el-tree-node .el-tree-node__label {
  font-size: 14px;
}

/** 列表 */
.pre-list {
  position: absolute;
  width: calc(100% - 80% - 10px);
  right: 20px;
  /*height: auto;*/
  /*top: 10.5%;*/
  margin-top: 15px;
  height: calc(84.5%);
  padding-bottom: 30px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

/** 基本信息 */
.pre-basic {
  border-radius: 5px;
  height: 280px;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

/** 内容*/
.pre-content {
  border-radius: 5px;
  height: calc(90%);
  margin-top: 15px;
  width: calc(80% - 15px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}
.a-item{
  width: 100%;
  height: 100%;
  padding: 0px 30px;
  position: relative;
.el-button--medium{
  position: absolute;
  right: 90px;
}
}
.a-title{
  font-size: 18px;
  font-weight: 600;
  margin: 15px 0px;

}
.a-content:not(:nth-child(3)){
  margin: 0px 0px 10px;
}
.a-item .a-content:nth-child(3){
  margin: 20px 0px 0px;
  color: #606266;
}
.a-item .a-content:nth-child(1){
  margin: 20px 0px 10px;
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
