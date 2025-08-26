<!-- 适配个人中心 -->
<template>
  <section class="app-main">
    <div style="width: 290px; height: calc(100vh - 66px); float: left; position: fixed; z-index: 9999">
      <navbar2/>
    </div>

    <div style="float: right">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="cachedViews">
          <router-view :key="key" />
        </keep-alive>
      </transition>
    </div>

  </section>
</template>

<script>
import Navbar2 from "@/layout/components/Navbar2";
export default {
  name: 'AppMain2',
  components: {Navbar2},
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 66px);
  width: 1500px;
  position: relative;
  //overflow: auto;
  /*width: 78vw;*/
  margin: 15px auto 120px;
  scroll-behavior: smooth;
}

.fixed-header+.app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header+.app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 17px;
  }
}
</style>
