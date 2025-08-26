<template>
  <div class="navbar">
    <!-- 启用 topNav 时,将点击展开左侧边菜单的按钮隐藏 -->
    <hamburger v-if="!topNav" id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
<!--    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>-->

    <div style="max-width: 1500px; height: 100%; margin: auto">
      <div class="left-logo">

        <router-link to="/index">
          <img src="../../assets/home/logo.png" />
        </router-link>
      </div>

      <!--    <div class="center-menu">-->
      <!--      <el-input placeholder="搜索你感兴趣的内容" v-model="selectValue" class="input-with-select">-->
      <!--        <el-button slot="append" icon="el-icon-search" @click="selectSearch()"></el-button>-->
      <!--      </el-input>-->
      <!--    </div>-->

      <div class="right-menu">
        <div class="toggle" @click="toggleManage">
          <img src="@/assets/images/d.png" />
          <div class="t-text">管理端入口</div>
        </div>
        <template v-if="device!=='mobile'">
          <!--        <search id="header-search" class="right-menu-item" />-->

          <!-- el-tooltip content="源码地址" effect="dark" placement="bottom">
            <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
          </el-tooltip>

          <el-tooltip content="文档地址" effect="dark" placement="bottom">
            <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
          </el-tooltip -->

          <screenfull id="screenfull" class="right-menu-item hover-effect" />

          <!-- el-tooltip content="布局大小" effect="dark" placement="bottom">
            <size-select id="size-select" class="right-menu-item hover-effect" />
          </el-tooltip -->

          <div class="right-menu-item">
          <span v-if="showAvatar"
                style="color: white;text-align: center;">{{nickName}}</span>
          </div>
          <!-- 这里是头像，修改为仅登录状态下显示头像 -->
          <el-dropdown class="avatar-container right-menu-item hover-effect" v-if="showAvatar" trigger="click">
            <div class="avatar-wrapper">
              <!--            <img :src="avatar" class="user-avatar">-->
              <el-avatar :src="avatar" class="user-avatar">
                <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
              </el-avatar>
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown">
              <router-link to="/curriculum">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <!-- 仅开发模式下允许启动布局设置 -->
              <!--            <el-dropdown-item v-if="showSetting"  @click.native="setting = true" >-->
              <!--              <span>布局设置</span>-->
              <!--            </el-dropdown-item>-->
              <!--            <router-link to="/errorbook">-->
              <!--              <el-dropdown-item divided><span>错题本</span></el-dropdown-item>-->
              <!--            </router-link>-->
              <router-link to="/sign">
                <el-dropdown-item ><span>活动报名</span></el-dropdown-item>
              </router-link>
              <router-link to="/stuCert">
                <el-dropdown-item ><span>我的证书</span></el-dropdown-item>
              </router-link>
              <router-link to="/opinion">
                <el-dropdown-item ><span>意见反馈</span></el-dropdown-item>
              </router-link>
              <el-dropdown-item divided @click.native="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

          <!-- 显示登录和注册按钮 -->
          <el-link v-else type="primary" class="right-menu-item hover-effect" style="font-size: 16px;"
                   href="/student/login">登录</el-link>
          <!-- div class="right-menu-item" style="padding: 0px">|</div -->
          <!-- el-link type="primary" class="right-menu-item hover-effect" style="font-size: 16px">注册</el-link -->
        </template>

      </div>

    </div>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import TopNav from "@/components/TopNav";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import SizeSelect from "@/components/SizeSelect";
import Search from "@/components/HeaderSearch";
import RuoYiGit from "@/components/RuoYi/Git";
import RuoYiDoc from "@/components/RuoYi/Doc";
import { getToken } from "@/utils/auth";
import defaultAvatar from "@/assets/images/user-big.png";

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
  },
  data() {
    return {
      selectValue: "",
      defaultAvatar,
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "device", "name", "nickName"]),
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
  methods: {
    selectSearch() {
      alert("查询成功");
    },
    //链接管理端登录
    toggleManage() {
      let url = window.location.href;
      const index = url.indexOf('/', url.indexOf('://') + 3) // 跳过协议部分的斜杠
      if(index != -1){
        let url_ = url.substring(0,index);
        window.open(url_ + '/admin');
      }else{
        window.open(url + '/admin');
      }
    },

    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "/student/index";
          });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 56px;
  overflow: hidden;
  padding: 0 0px;
  position: relative;
  background: #282827;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    // left: 50px;
    height: 100%;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .left-logo {
    float: left;
    margin-top: 10px;
    margin-left: 1%;
  }
  .left-logo img {
    height: 36px;
  }
  .center-menu {
    width: 350px;
    display: inline-block;
    line-height: 65px;
    margin-left: 44%;
  }
  .right-menu {
    float: right;
    height: 100%;
    line-height: 56px;
    padding-right: 1%;
    display: flex;
    .toggle{
      color:#ffffff;
      margin:0 20px 0 0px;
      cursor: pointer;
      display: flex;
      align-items: center;
      img{
        width: 22px;
        height: 22px;
        margin-right: 5px;
      }
      .t-text{
        margin-top:4px;
      }
    }

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 14px;
      color: #d6dde9;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 36px;
          height: 36px;
          border-radius: 18px;
          margin-top: 5px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 16px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
