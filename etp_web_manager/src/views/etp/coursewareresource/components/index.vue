<template>
  <!-- 富文本预览 -->
  <div class="app-container">
    <div v-if="infoObj.type == 'richText'" v-html="infoObj.content" />
  </div>
</template>

<script>
export default {
  name: "RichTextPreview",
  props:{
    infoObj:Object,
    default: {
      type:'richText',// linked or richText
      content:'内容项'
    }
  }
}
</script>

<style scoped>

</style>
