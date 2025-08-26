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
        <el-tag size="small"><a>最近考试</a></el-tag>

      </el-col>
    </el-row>
  </div>
  <div class="model-c"  v-if="data.length>0">
    <div class="model-item-ks" v-for="(item,index) in data" @click="clickExam(item)">
      <div class="fm">
        <img :src="item.fmt" @error="e => { e.target.src = defaultImg }">
        <div class="tag-cir">
          <el-tag v-if="item.status =='2'" type="warning">进行中</el-tag>
          <el-tag v-else-if="item.status =='3'||item.status=='4'" type="info">已结束</el-tag>
<!--          <el-tag v-else>{{item.ksjd}}</el-tag>-->
        </div>
      </div>
      <div>
        <div class="c-name line">
          {{ item.name | ellipsis }}
        </div>
        <div class="c-span">
          <span v-if="item.totalTime>0">时长：{{item.totalTime}}分钟</span>
          <span v-else>时长：不限</span>
        </div>
        <div class="c-span">
          <span v-if="item.startTime!=null">{{item.startTime}}---{{item.endTime}}</span>
        </div>
        <div>
          <el-button type="primary">开始考试</el-button>
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
export default {
  name: "ks",
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
    //考试
    clickExam(row) {
      if (row.status == "4" || row.status == "5") {
        this.$message("考试已结束！");
      } else if (row.chance != "0" && row.chance <= row.usedChance) {
        this.$message.error("您的考试次数已用完！");
      } else {
        let router = this.$router.resolve("/examStart/detail/" + row.examId);
        window.open(router.href, "_blank");
      }
    },
  },
};
</script>

<style scoped lang="scss" src="../../../assets/styles/home-css.scss">
</style>
