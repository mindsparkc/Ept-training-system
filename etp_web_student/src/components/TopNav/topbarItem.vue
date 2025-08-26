<template>
  <!-- 仅包含一级菜单 -->
  <el-menu-item  :index="item.path" :key="item.path"
                v-if="hasOneShowingChild(item.children,item)">
    <svg-icon :icon-class="item.meta.icon" />{{ item.meta.title }}
  </el-menu-item>
  <!-- 有二级菜单的项 -->
  <el-submenu :index="item.path" v-else-if="!hasOneShowingChild(item.children,item)" popper-append-to-body>
    <template slot="title">
      <svg-icon :icon-class="item.meta.icon" />{{ item.meta.title }}
    </template>

    <topbar-item v-for="child in item.children"
                 :key="child.path"
                 :base-path="child.path"
                 :item="child">
    </topbar-item>
  </el-submenu>
</template>

<script>
export default {
  name: "TopbarItem",
  props: {
    item: {
      type: Object,
      require: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      if (!children) {
        children = [];
      }
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    }
  }
}
</script>

<style scoped>

</style>
