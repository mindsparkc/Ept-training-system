<template>
  <div style="width: 100%; height: 100%; border-radius: 5px" >
    <div style="width: 100%; padding-top: 20px">
      <div style="width: 60px; position: relative; margin: auto;">
        <el-avatar :size="60" :src="avatar" >
          <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
        </el-avatar>
      </div>
      <div class="nick-name">
        <span>{{nickName}}</span>
      </div>

      <!-- 菜单 -->
      <ul class="menu" style="padding-top: 40px;">
        <li style="padding-bottom: 10px"><span class="menu-title">学习·考试</span></li>
        <li :key="1" :class="[activeIndex===1?'menu-item-active':'menu-item']" @click="clickMenu(1)">
          <router-link to="/curriculum">
            <div>我的课程</div>
          </router-link>
        </li>
        <li :key="2" :class="[activeIndex===2?'menu-item-active':'menu-item']"  @click="clickMenu(2)">
          <router-link to="/examInfo">
            <div>我的考试</div>
          </router-link>
        </li>
        <li :key="3" :class="[activeIndex===3?'menu-item-active':'menu-item']"  @click="clickMenu(3)">
          <router-link to="/examRecord">
            <div>考试记录</div>
          </router-link>
        </li>
        <li :key="4" :class="[activeIndex===6?'menu-item-active':'menu-item']"  @click="clickMenu(6)">
          <router-link to="/task">
            <div>我的任务</div>
          </router-link>
        </li>
      </ul>

      <ul class="menu" style="padding-top: 15px">
        <li style="padding-bottom: 10px"><span class="menu-title">数据</span></li>
        <li class="menu-item" :class="[activeIndex===4?'menu-item-active':'menu-item']"  @click="clickMenu(4)">
          <router-link to="/stuCert">
            <div>我的证书</div>
          </router-link>
        </li>

      </ul>
      <ul class="menu" style="padding-top: 15px">
        <li style="padding-bottom: 10px"><span class="menu-title">问卷</span></li>
        <li class="menu-item" :class="[activeIndex===7?'menu-item-active':'menu-item']"  @click="clickMenu(7)">
          <router-link to="/questionnaire">
            <div>调查问卷</div>
          </router-link>
        </li>
      </ul>
      <ul class="menu" style="padding-top: 15px">
        <li style="padding-bottom: 10px"><span class="menu-title">个人配置</span></li>
        <li class="menu-item" :class="[activeIndex===5?'menu-item-active':'menu-item']"  @click="clickMenu(5)">
          <router-link to="/user/profile">
            <div>信息修改</div>
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getToken } from "@/utils/auth";

export default {
  data() {
    return {
      activeIndex: 1,
    };
  },
  computed: {
    ...mapGetters(["avatar", "nickName"]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings;
      },
      set(val) {
        this.$store.dispatch("settings/changeSetting", {
          key: "showSettings",
          value: val,
        });
      },
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
    showSetting() {
      return process.env.NODE_ENV === "development";
    },
    showAvatar: {
      get() {
        return getToken();
      },
    },
  },
  mounted() {
    let path = this.$route.path;
    if (path == "/curriculum") this.activeIndex = 1;
    else if (path == "/examInfo") this.activeIndex = 2;
    else if (path == "/examRecord") this.activeIndex = 3;
    else if (path == "/task") this.activeIndex = 6;
    else if (path == "/stuCert") this.activeIndex = 4;
    else if (path == "/questionnaire") this.activeIndex = 7;
  },

  // watch: {
  //   $route: {
  //     handler: function(val, oldVal){
  //       console.log(val);
  //       console.log(oldVal);
  //       if(val.path == 'curriculum'){
  //         this.activeIndex = 1
  //       }
  //
  //       else if(val.path == 'examInfo'){
  //         this.activeIndex = 2
  //         this.clickMenu(2)
  //       }
  //
  //       else if(val.path == 'examRecord'){
  //         this.activeIndex = 3
  //       }
  //
  //       else if(val.path == 'task'){
  //         this.activeIndex = 6
  //       }
  //
  //       else if(val.path == 'stuCert'){
  //         this.activeIndex = 4
  //       }
  //
  //     },
  //     // 深度观察监听
  //     deep: true
  //   }
  // },

  methods: {
    clickMenu: function(index) {
      this.activeIndex = index;
    },
  },
};
</script>

<style lang="scss" scoped>
.nick-name {
  display: block;
  width: 100%;
  text-align: center;
  font-size: 20px;
  padding-top: 10px;
}

.menu {
  width: 100%;
  margin: 0px;
  list-style: none;
  padding-left: 0px;
  user-select: none;

  span {
    padding-left: 108px;
  }

  .menu-title {
    color: #333333;
    font-size: 16px;
    margin-bottom: 10px;
    font-weight: 600;
  }

  .menu-item {
    text-align: left;
    height: 48px;
    line-height: 48px;
    font-size: 14px;
    color: #666666;
    list-style: none;
    position: relative;
    cursor: pointer;
    box-sizing: border-box;
    white-space: nowrap;

    div {
      padding-left: 108px;
    }
  }

  .menu-item-active {
    text-align: left;
    height: 48px;
    line-height: 48px;
    font-size: 14px;
    color: #0045c5;
    background-color: #ffffff;
    list-style: none;
    position: relative;
    cursor: pointer;
    box-sizing: border-box;
    white-space: nowrap;
    // border-left: 3px solid #0045c5;

    div {
      padding-left: 108px;
    }
  }
}
</style>
