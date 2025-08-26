<template>
<div :style="{marginTop:top} ">
  <div class="model-all">
    <el-row>
      <el-col>
        <span style="line-height: 50px; font-weight: 600; font-size: 24px">{{title.name}}  </span>
        <div class="more-span">
            <router-link :to="{ path:title.url, query: {tab:this.activeName} }">
              <span style="font-size: 14px;color:#999999;line-height: 50px; margin-left:16px;">更多</span>
              <img src="../../../assets/home/icon_more_q.png">
            </router-link>
        </div>
        <el-tag @click="tagClick" size="small" :type="typeData"><a>最新更新</a></el-tag>
          <!-- <img src="@/assets/home/icon_right.png"/> -->
        

      </el-col>
      <!-- <el-col :span="20">
        <div style="display: inline-block">
          <el-tabs v-model="activeName" @tab-click="handleClick" :tabBarStyle="{ borderBottom: 'unset' }">
            <el-tab-pane label="全部" name="first"></el-tab-pane>
            <el-tab-pane label="推荐" name="second"></el-tab-pane>
          </el-tabs>
            
       
        </div>
      </el-col> -->
      <el-col>

      </el-col>
    </el-row>
    <!-- <el-row style="padding-top: -20px;">
      <el-tag @click="tagClick" size="small" :type="typeData"><a>最新更新</a></el-tag>
    </el-row> -->
  </div>
  <div class="model-c"  v-if="data.length>0" >
    <div class="model-item" v-for="(item,index) in data" @click="clickCurriculumAll(item)" style="background:#fff;">
      <div class="fm">
        <img :src="item.fmt" @error="e => { e.target.src = defaultImg }">
      </div>
      <div>
        <div class="c-name line">
          {{ item.mc  | ellipsis}}
        </div>
        <div class="c-span">
<!--          <span>次数 1020</span>-->
<!--          <span>评论 201</span>-->
<!--          <span>点赞 308</span>-->
          <span>{{ item.num }} 人学习中</span>
        </div>
      </div>
    </div>
  </div>
  <div v-else style="margin-left: 540px">
    <div>
      <img src="@/assets/home/null.png">
    </div>
    <span style="margin-left: 35px">暂无数据</span>
  </div>
</div>
</template>

<script>
import defaultImg from "@/assets/home/fm.png";
import { getKnowledgeInfo } from "@/api/etp/curriculum/curriculum";
export default {
  name: "kc",
  data() {
    return {
      defaultImg,
      typeData: "",
      activeName: "first",
      loading: false,
    };
  },
  filters: {
    //超过20位显示 ...
    ellipsis: function(value) {
      if (!value) return "";
      if (value.length > 28) {
        return value.slice(0, 28) + "...";
      }
      return value;
    },
  },
  props: {
    title: {
      name: "",
      url: "",
    },
    top: {
      type: String,
      default: "11px",
    },
    data: {
      type: Array,
      default: [],
    },
  },
  methods: {
    getList(type) {
      this.loading = true;
      getKnowledgeInfo({ pageNum: 1, pageSize: 4, sftj: type }).then(
        response => {
          let list = response.rows;
          for (let i = 0; i < list.length; i++) {
            if (
              list[i].fmt == null ||
              list[i].fmt == "" ||
              list[i].fmt == undefined
            ) {
              list[i].fmt = require("@/assets/images/cover.jpg");
            }
          }
          this.data = list;
          this.loading = false;
          console.log(this.data);
        }
      );
    },

    //跳转全部课程（发现）详情
    clickCurriculumAll(data) {
      let router = this.$router.resolve({
        path: "/etp/curriculum/previewCurriculum/",
        query: { cid: data.id, getStatus: null },
      });
      window.open(router.href, "_blank");
    },
    //tab页点击事件
    handleClick(tab, event) {
      console.log(tab._props.name);
      if (tab._props.name == "first") {
        this.getList("");
      } else if (tab._props.name == "second") {
        this.getList("1");
      }
    },
    //获取标签对应数据
    tagClick() {
      // if(this.typeData==''){
      //   this.typeData = 'info';
      // }else{
      //   this.typeData = '';
      // }
    },
  },
};
</script>

<style scoped lang="scss" src="../../../assets/styles/home-css.scss">
</style>
