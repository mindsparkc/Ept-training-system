<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default  {
  name:  'App',
    metaInfo() {
        return {
            title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
            titleTemplate: title => {
                return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
            }
        }
    },
  mounted() {
    window.addEventListener("scroll",this.scrollHandle,true)

  },
  methods: {
    scrollHandle(e) {
      if(e.srcElement.scrollingElement!=undefined){
        this.scrolltop = e.srcElement.scrollingElement.scrollTop; // 获取页面滚动高度

        let ele = document.getElementsByClassName('span4-left')
        let treeArea = document.getElementsByClassName('tree-div')
        let navArea = document.getElementsByClassName('nav-area')
        if(ele.length>0){
          if (this.scrolltop > 60) {
            ele[0].style.cssText = 'top: 20px !important';
          } else {
            ele[0].style.cssText = 'top: 104px !important';
          }
        }
        if(treeArea.length>0){
          if (this.scrolltop > 60) {
            treeArea[0].style.cssText = 'height: calc(100vh - 100px - 22px);'
          } else {
            treeArea[0].style.cssText = 'height: calc(100vh - 100px - 52px - 50px);'
          }
        }
        if(navArea.length>0){
          if (this.scrolltop > 60) {
            navArea[0].style.cssText = 'height: calc(100vh - 60px) !important;'
          } else {
            navArea[0].style.cssText = 'height: calc(100vh -140px) !important;'
          }
        }
      }

    }
  }
}
</script>
