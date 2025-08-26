<template>
<div :style="{marginTop:top}">
  <div class="model-t">
    <el-row>
      <el-col>
        <span style="line-height: 50px; font-weight: 600; font-size: 24px">{{title.name}}
          <!-- <img src="@/assets/home/icon_right.png"/> -->
        </span>
        <div class="more-span">
          <router-link :to="title.url">
            <span style="font-size: 14px;color:#999999;line-height: 50px; margin-left:16px;">更多</span>
            <img src="../../../assets/home/icon_more_q.png">
          </router-link>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="model-c" v-if="data.length>0">
    <div class="model-item" v-for="item in data" @click="clickTask(item)">
      <div class="fm">
        <img :src="item.fmt" @error="e => { e.target.src = defaultImg }">
      </div>
      <div>
        <div class="c-name line">
          {{ item.mc | ellipsis }}
        </div>
        <div class="c-span">
          <span>结束时间：{{item.taskEndTime}}</span>
        </div>
        <div class="c-btm">
          <el-tag v-if="item.rwjd === '未开始'">{{item.rwjd}}</el-tag>
          <el-tag v-if="item.rwjd === '进行中'" type="warning">{{item.rwjd}}</el-tag>
          <el-tag v-if="item.rwjd === '已完成'" type="success">{{item.rwjd}}</el-tag>
          <el-tag v-if="item.rwjd === '已结束'" type="info">{{item.rwjd}}</el-tag>
        </div>
        <div>
          <el-button type="primary">开始任务</el-button>
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
import { clickSendRedis } from "@/api/buryPoint";
import defaultImg from "@/assets/home/fm.png";
export default {
  name: "rw",
  data() {
    return {
      defaultImg,
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
    //跳转任务
    clickTask(data) {
      if (data.rwjd == "已结束") {
        this.$message.error("任务已结束！");
      } else {
        let jd = null;
        if (data.jd) {
          if (JSON.parse(data.jd).lastStudy == undefined) {
            jd = null;
          } else {
            jd = JSON.parse(data.jd)["lastStudy"]["current"];
          }
        }
        let params = {};
        params.streamName = "stream";
        params.eventType = "CLI_TASK";
        params.id = data.id;
        //点击埋点0
        clickSendRedis(params).then(response => {
          console.log("点击首页任务埋点", response);
        });

        let router = this.$router.resolve({
          path: "/taskManagement/detail/",
          query: { rwid: data.id, jd: jd },
        });
        window.open(router.href, "_blank");
      }
    },
  },
};
</script>

<style scoped lang="scss" src="../../../assets/styles/home-css.scss">
</style>
