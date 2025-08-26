<template>
  <div class="pre-content pre-pad" >
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
    <div style="width: 100%;height: calc(100% - 5px);-webkit-overflow-scrolling: auto;overflow:hidden;"
         v-show="!tipFlag">
      <div class="kj-title"><i class="el-icon-notebook-2"></i>{{kjmc}}</div>
      <div style="height:calc(100% - 60px);" v-if="iframeFlag ">
        <scrollLoad :list="picList" :total="picTotal" :host="picHost" ref="pdfSvg"></scrollLoad>
      </div>
      <video controls autoplay width="96%" v-if="kjlx=='2'" style="display: block;margin: 0 auto;height: calc(100% - 70px);" id="myVideo">
        <source :src="url" >
      </video>
      <div v-show="richFlag" style="padding: 20px 50px;height: 90%;overflow:auto;" v-html="nr"></div>
      <div class="a-item" v-show="aHref.flag" >
<!--        <div class="a-title">即将离开本站</div>-->

        <div class="a-content">课件名称：{{aHref.name}}</div>
        <div class="a-content">课件地址：{{aHref.address}}</div>
        <div class="a-content">点击【继续访问】即将离开本站，在新的页面打开课件内容</div>
        <el-divider></el-divider>
        <el-button type="primary" @click="visitUrl(aHref.address)">继续访问</el-button>
      </div>
    </div>

  </div>
</template>

<script>
 import { getCourseware } from '@/api/etp/courseware/courseware'
 import scrollLoad from "@/components/scrollLoad/index"

 export default {
   name: 'study',
   components:{
     scrollLoad
   },
   data(){
     return {
       tipFlag: true,
       iframeFlag: false,
       richFlag: false,
       kjlx: '',
       nr: '',
       url: '',
       aHref: {
         flag:false,
         name:null,
         address:null
       },

       //文件图片
       picList:[],
       picTotal:0,
       picHost:"",

       kcmc:'',//课程名称
       kcms:'',//课程描述
       //学习的课件名称
       kjmc:"",
       kjid:null

     }
   },
   created() {

     this.kjid = this.$route.query.kjid
   },

   watch: {
     kjid: {
       handler (val, oldValue) {
         this.showCj();
       },
       deep: true,
     }
   },
   methods: {

     /** 标志位重置*/
     flagR() {
       this.iframeFlag = false
       this.richFlag = false
       this.aHref.flag = false;
     },

     /** 依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身*/
     showCj() {
       this.flagR();
       this.tipFlag = false;
       this.kjlx = ''
       this.kjmc = '';
       getCourseware(this.kjid).then(res => {
         /**课件形式Office：1 视频：2 富文本：3 外部链接：4 其他文件：5*/
         let {xs, kjdz, nr, lj,mc,metaData} = res.data;
         this.kjmc = mc
         this.kjlx = xs;
         if (xs == '1' || xs == '5') {
           this.iframeFlag = true;
           // this.$nextTick(_ => {
           //   this.url = this.previewUrl() + '?url=' + encodeURIComponent(window.btoa(kjdz));
           // })
           if(metaData != null){
             let { list,size } = JSON.parse(metaData)
             this.picList = list
             this.picTotal = size
             this.picHost = kjdz.substring(0,kjdz.lastIndexOf(".")) +"/"
           }
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

<style lang="scss" scoped>
.el-empty__image {
  width: 300px;
  margin: 0 auto;
  text-align: center;
}

.desc {
  text-align: center;
  font-size: 18px;
  color: #ccb1ea;
  letter-spacing: 4px;
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

/** 内容*/
.pre-content {
  height: calc(100% - 75px);
  border-radius: 5px;
  padding-top: 15px;
}
.a-item{
  width: 100%;
  height: 100%;
  padding: 0px 30px;
  position: relative;
  .el-button--medium {
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
.kj-title{
  font-size: 18px;
  margin-left: 16px;
  line-height: 40px;
  i{
    margin-right: 4px;
  }
}
</style>
