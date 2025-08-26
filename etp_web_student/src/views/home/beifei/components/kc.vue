<!--
<template>
<div :style="{marginTop:top}">
  <div class="model-t">
    <span>{{title.name}}</span>
    <img src="../../../assets/home/btn_more.png">
  </div>
  <div class="model-c">
    <div class="model-item" v-for="i in (8)">
      <div class="fm">
        <img src="../../../assets/home/fm.png" >
      </div>
      <div>
        <div class="c-name line">
          C/C++气象数据中心实战，手把手教你做工业级项目
        </div>
        <div class="c-span">
          <span>次数 1020</span>
          <span>评论 201</span>
          <span>点赞 308</span>
        </div>
        <div class="c-btm">
          <img src="../../../assets/home/btn_dz.png">
          <span>308</span>
        </div>
      </div>
    </div>

  </div>
</div>
</template>

<script>
export default {
  name: "kc",
  props:{
    title:{
      name:"",
      url:""
    },
    top:{
      type:String,
      default:'32px'
    },
    data:{
      type:Array,
      default:[]
    }
  }
}
</script>

<style scoped lang="scss">
.model-t{
  span{
    font-size: 24px;
  }
  img{
    margin-left: 3px;
    vertical-align: sub;
  }
}
.model-c{
  margin-top: 25px;
  display: flex;
  flex-wrap: wrap;
}
.model-item{
  width: 285px;
  height: 270px;
  border-radius: 8px;

  .fm{
    height: 164px;
    width: 285px;
    img{
      width: 285px;
      height: 165px;
      border-radius: 5px;
    }
  }
  .c-name{
    font-size: 16px;
    margin-top: 15px;
  }
  .c-span{
    margin-top: 7px;

    span:not(:first-child){
      margin-left: 10px;
    }
  }
  span{
    font-size: 14px;
    color: #999999;
  }
  img{
    vertical-align: sub;
  }
  .c-btm{
    margin-top: 2px;
    span{
      margin-left: 7px;
    }
  }
}
.model-item:not(:nth-child(4n+1)){
  margin-left: 20px;
}
.model-item:nth-child(n+5){
  margin-top: 30px;
}
.line{
  text-overflow: -o-ellipsis-lastline;
  overflow: hidden;				//溢出内容隐藏
  text-overflow: ellipsis;		//文本溢出部分用省略号表示
  display: -webkit-box;			//特别显示模式
  -webkit-line-clamp: 2;			//行数
  line-clamp: 2;
  -webkit-box-orient: vertical;	//盒子中内容竖直排列
}
</style>
-->
