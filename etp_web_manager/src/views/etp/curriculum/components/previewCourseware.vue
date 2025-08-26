<template>
  <!-- 课件预览 -->
  <div class="app-container1">
    <iframe v-if="courseWare.type == '1' || courseWare.type == '2' || courseWare.type == '4' || courseWare.type == '5'"
            :src="courseWare.address" style="border: 0;height: 600px;width: 100%;"/>
    <div v-if="courseWare.type == '3'" v-html="courseWare.content" />
  </div>

</template>

<script>
export default {
  name: "previewCourseware",
  props:{
    courseWare:Object,
    default: {
      /** 预览 Office：1  视频：2  富文本：3  外部链接：4  其他文件：5 */
      type:'',
      /** 课件地址 */
      address:'',
      /** 课件内容 */
      content:'',
    }
  }
}
</script>

<style scoped>

</style>
