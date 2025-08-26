<template>
  <div>
    <div class="tag" v-for="(item,index) in tagList">
      <span v-if="showFlag &&limit!=null && index<limit">
        <el-tag v-show="item!=''">
         {{item}}
        </el-tag>
      </span>
      <span v-else-if="(!showFlag||limit==null)">
        <el-tag v-show="item!=''">
         {{item}}
        </el-tag>
      </span>
    </div>
    <div v-show="limit!=null&&tagList.length>limit && showFlag"><i class="el-icon-arrow-down" @click="showTag"></i></div>
    <div v-show="!showFlag"><i class="el-icon-arrow-up" @click="showTag"></i></div>
  </div>

</template>

<script>
export default {
  name: 'index',
  props:{
    tagList:{
      type:Array,
      default:[]
    },
    limit:{
      type:Number,
      default:null
    }

  },
  data(){
    return{
      showFlag:true
    }
  },
  methods:{
    showTag(){
       this.showFlag = !this.showFlag
    }
  }
}
</script>

<style scoped>
.tag{
  margin-top: 4px;
}
i{
  cursor: pointer;
}
</style>
