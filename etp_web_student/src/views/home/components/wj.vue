<template>
<div :style="{marginTop:top}">
  <div class="model-t">
    <el-row>
      <el-col>
        <span>{{title.name}}</span>
        <div>
          <span>更多</span>
          <router-link :to="title.url"> <img src="../../../assets/home/icon_more_q.png"></router-link>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="model-c" v-if="data.length>0">
    <div class="model-item" v-for="item in data" @click="getDetail(item)">
      <div class="fm">
        <img :src="item.fm" >
      </div>
      <div>
        <div class="c-name line">
          {{ item.wjmc }}
        </div>
        <div class="c-span">
          <span>结束时间：{{item.endTime}}</span>
        </div>
        <div class="c-btm">
          <el-tag v-if="item.status === '未开始'">{{item.status}}</el-tag>
          <el-tag v-if="item.status === '进行中'" type="warning">{{item.status}}</el-tag>
          <el-tag v-if="item.status === '已完成'" type="success">{{item.status}}</el-tag>
          <el-tag v-if="item.status === '已结束'" type="info">{{item.status}}</el-tag>
        </div>
      </div>
    </div>

  </div>
</div>
</template>

<script>
import {clickSendRedis} from "@/api/buryPoint";

export default {
  name: "wj",
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
  },
  methods:{

    getDetail(row){
      let router = this.$router.resolve({
        path: "/etp/questionnaire/detail/",
        query: { id: row.id,tags:row.tags}
      });
      window.open(router.href,'_blank')
    },
  }


}
</script>

<style scoped lang="scss" src="../../../assets/styles/home-css.scss">

</style>
