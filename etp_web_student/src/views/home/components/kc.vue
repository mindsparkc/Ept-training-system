<template>
<div :style="{marginTop:top}">
  <div class="model-t">
    <el-row>
      <el-col>
        <span  style="line-height: 50px; font-weight: 600; font-size: 24px">{{title.name}}
          <!-- <img src="@/assets/home/icon_right.png"/> -->
        </span>
        <div class="more-span">
          <router-link :to="title.url">
            <span style="font-size: 14px;color:#999999;line-height: 50px; margin-left:16px;">更多</span>
            <img src="../../../assets/home/icon_more_q.png">
          </router-link>
        </div>
        <el-tag size="small" :type="tagStudy" @click="tagClick('1')"><a>最近学习</a></el-tag>
        <el-tag size="small" style="margin-left: 10px" :type="tagGet" @click="tagClick(2)"><a>最近领取</a></el-tag>
<!--        <el-tag size="small" style="margin-left: 10px" :type="tagUpdate" @click="tagClick(3)"><a>最近更新</a></el-tag>-->

      </el-col>
<!--      <el-col>-->
<!--        <el-tag>-->

<!--        </el-tag>-->
<!--      </el-col>-->
    </el-row>
  </div>
  <div class="model-c"  v-if="data.length>0" >
    <div class="model-item" v-for="item in data" @click="clickCurriculumAll(item)">
      <div class="fm">
        <img :src="item.fmt" @error="e => { e.target.src = defaultImg }">
      </div>
      <div>
        <div class="c-name line">
          {{ item.mc | ellipsis }}
        </div>
        <div class="c-span">
<!--          <span>次数 1020</span>-->
<!--          <span>评论 201</span>-->
<!--          <span>点赞 308</span>-->
          <span>{{ item.num }} 人学习中</span>
        </div>
        <div class="c-btm">
<!--          <img src="../../../assets/home/btn_dz.png">-->
<!--          <span>308</span>-->
          <el-tag v-if="item.kcjd === '未开始'">{{item.kcjd}}</el-tag>
          <el-tag v-if="item.kcjd === '进行中'" type="warning">{{item.kcjd}}</el-tag>
          <el-tag v-if="item.kcjd === '已完成'" type="success">{{item.kcjd}}</el-tag>
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
import { listCurriculum } from "@/api/etp/curriculum/curriculum";
export default {
  name: "kc",
  data() {
    return {
      defaultImg,
      tagStudy: "",
      tagGet: "info",
      tagUpdate: "info",
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
      default: "21px",
    },
    data: {
      type: Array,
      default: [],
    },
  },
  methods: {
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
      console.log(tab, event);
    },
    //tag点击事件
    tagClick(type) {
      this.loading = true;
      let item = { pageNum: 1, pageSize: 8, jd: "" };
      if (type == 1) {
        this.tagStudy = "";
        this.tagGet = "info";
        this.tagUpdate = "info";
        item.jd = "最近学习";
      } else if (type == 2) {
        this.tagStudy = "info";
        this.tagGet = "";
        this.tagUpdate = "info";
        item.jd = "最近领取";
      } else if (type == 3) {
        this.tagStudy = "info";
        this.tagGet = "info";
        this.tagUpdate = "";
      }

      /** 查询curriculum列表 */
      listCurriculum(item).then(response => {
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fmt == null ||
            list[i].fmt == "" ||
            list[i].fmt == undefined
          ) {
            list[i].fmt = require("@/assets/images/cover.png");
          }
        }
        this.data = list;
        this.loading = false;
      });
    },
  },
};
</script>

<style scoped lang="scss" src="../../../assets/styles/home-css.scss">
</style>
