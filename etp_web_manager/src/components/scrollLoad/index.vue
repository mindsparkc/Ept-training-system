<template>
  <div id="scrollCon" class="scroll-item" ref="Box" @scroll="divScroll">
    <div style="/*height: 800px; */ width: 100%; text-align: center;">
      <!--      <div id="svgItem" ></div>-->
      <el-image v-for="(url, index) in list" style="width: 970px;"
                :key="url"
                :class='`my-photo photo_${index}`'
                :src='`${host}${url}`'
                lazy/>
    </div>


    <div class="page-item">
      <span>总共{{pageNo}}/{{total}}页</span>
      <span>前往
        <el-input v-model.number="jump" style="width: 80px;" @change="changePage"></el-input>页
      </span>

    </div>
    <div v-loading.fullscreen.lock="fullscreenLoading"></div>

  </div>
</template>

<script>
import _ from 'lodash'


function isInSight(el) { // TODO: 判断元素是否在可现实的范围内
  var bound = el.getBoundingClientRect();
  var clientHeight = window.innerHeight;
  //只考虑向下滚动加载
  //const clientWidth=window.innerWeight;
  return bound.top <= clientHeight + 100;
}

export default {
  name: 'index',
  props:{
    list:{
      type: Array,
      default: [],
    },
    total:{
      type: Number,
      default: null,
    },
    host:{
      type: String,
      default: "",
    }
  },
//定义变量
  data(){
    return {
      fullscreenLoading: false,
      //图片列表
      // list:[],
      pageNo:1,
      //跳页
      jump: null,
      //用于存放预加载的图片
      data: null,
      //总页数
      // total:0,
      //单页高度
      svgHeight:0
    }
  },
  created() {

  },
  mounted () {
    //初始化数据
    this.initList()
  },
  methods:{

    //监听滚动操作 节流
    divScroll:_.throttle(function (){
      let that = this
      this.throttle = false
      let scrollTop = this.$refs.Box.scrollTop;
      // console.log('距离顶部'+scrollTop);
      let scrollHeight = this.$refs.Box.scrollHeight;
      // console.log('滚动条高度'+scrollHeight);
      //可视区域的高度
      let windowHeight = this.$refs.Box.offsetHeight;

      // console.log(this.$refs.Box);
      //
      // console.log('可视区域的高度'+windowHeight);

      // this.pageNo = Math.floor(scrollTop / windowHeight) + 1

      var imgs = document.querySelectorAll('.my-photo');

      for (var i = 0; i < imgs.length; i++) {
        if (isInSight(imgs[i])) {
          this.pageNo = i+1;
          // console.log('页码'+this.pageNo);
        }
      }

      // console.log(Math.floor((scrollTop + windowHeight / 2)/that.svgHeight  ))
      // if(Math.floor((scrollTop + windowHeight / 2)/that.svgHeight  )>=1){
      //   // if(scrollTop+windowHeight >=scrollHeight){
      //
      //   console.log("到答底部")
      //   this.pageNo++ ;
      //   if(this.pageNo > this.total){
      //     this.pageNo =this.total
      //     return false
      //   }else{
      //     // this.getList("down")
      //   }
      // }else if(scrollTop ==0){//到达顶部
      //   this.pageNo--
      //   if(this.pageNo <1){
      //     this.pageNo=1
      //     return false
      //   }else{
      //     // this.getList("up")
      //   }
      // }
    },600),

    //从n页开始加载，支持一开始从第n页开始看
    initList(){
      // let that = this
      // this.data = new Array(11);
      // //当前页
      let no = this.pageNo - 1;

      let anchor = document.querySelector(".photo_"+no)
      // console.log(anchor)
      // console.log("photo_"+no)
      anchor.scrollIntoView();

      // let windowHeight = this.$refs.Box.offsetHeight;
      // this.$refs.Box.scrollTo(0, windowHeight * no)
      // for(let i=0;i<11 && i+(no-6)<this.total;i++)
      // {
      //   if(6-no<=i)
      //   {
      //     // that.fullscreenLoading =true
      //     let imgobj = new Image();
      //     imgobj.onload=function (){
      //       // that.fullscreenLoading =false
      //       that.svgHeight = imgobj.height
      //     }
      //     imgobj.src = this.host+this.list[no-6+i]
      //
      //     this.data[i] = imgobj
      //   }
      // }
      // let svgItem = document.getElementById("svgItem")
      // if(undefined != svgItem.childNodes[1]){
      //   svgItem.removeChild(svgItem.childNodes[1])
      // }
      // if(undefined != svgItem.childNodes[0]){
      //   svgItem.removeChild(svgItem.childNodes[0])
      // }
      //
      // if(undefined!= this.data[5]){
      //   svgItem.appendChild(this.data[5])
      //
      // }
      // if(undefined!= this.data[6]){
      //   svgItem.appendChild(this.data[6])
      // }
    },

    //获取数据
    // getList(moveFlag){
    //
    //   let no = this.pageNo;
    //   this.jump = null
    //   let svgItem = document.getElementById("svgItem")
    //   if(moveFlag == "down")
    //   {
    //     //向下滑动 尾部添加,头部删除
    //     if(no+4<this.total)
    //     {
    //       let imgobj = new Image();
    //       imgobj.src = this.host+this.list[no+4]
    //       // this.data.push(this.list[no+4]);
    //       this.data.push(imgobj);
    //
    //     }else{
    //       this.data.push(undefined);
    //     }
    //     this.data.shift();
    //
    //     if(undefined!= this.data[5]){
    //
    //       if(undefined != svgItem.childNodes[0]){
    //         svgItem.removeChild(svgItem.childNodes[0])
    //       }
    //       svgItem.appendChild(this.data[6])
    //
    //     }
    //
    //   }else if(moveFlag == "up")
    //   {
    //     //向上滑动 头部添加,尾部删除
    //
    //     if(no > 5)
    //     {
    //       // this.data.unshift(this.list[no-6]);
    //       let imgobj = new Image();
    //       imgobj.src = this.host+this.list[no-6]
    //       this.data.unshift(imgobj);
    //     }else{
    //       this.data.unshift(undefined);
    //     }
    //     this.data.pop();
    //
    //     if(undefined!= this.data[5]){
    //       if(undefined != svgItem.childNodes[1]){
    //         svgItem.removeChild(svgItem.childNodes[1])
    //       }
    //       svgItem.insertBefore(this.data[5],svgItem.childNodes[0])
    //     }
    //
    //
    //   }
    //
    //   //加载之后 滚动条放到顶部 稍微往下一点
    //   this.$refs.Box.scrollTo(0,10)
    //
    // },

    //页码校验
    changePage(){
      let pageNum = this.jump
      var regu = "^[0-9]*$";
      var re = new RegExp(regu);
      if(!re.test(pageNum)){
        this.$message.error("页码必需是数字")
        this.jump = null
      }else if(pageNum>this.total){
        this.$message.error("跳页不能超过最大页数")
        this.jump = null
      }else if(pageNum<1){
        this.$message.error("跳页不能小于1")
        this.jump = null
      }else{
        this.pageNo = pageNum
        this.initList()
      }

    },
  }
}
</script>

<style scoped lang="scss">
.scroll-item{
  //height: calc(100% + 11px);
  height: 100%;
  overflow: auto;
}
#svgItem{
  text-align: center;
}
.page-item{
  position: fixed;
  top: 92%;
  right: 2vw;
}
.page-item{
  span{
    margin-right: 10px;
  }
}

.el-image {
  height: 100%;
  width: 94%;
}
</style>
