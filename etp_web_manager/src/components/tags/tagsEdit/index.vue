<template>
  <div>
    <el-select
      v-model="value"
      multiple
      filterable
      allow-create
      default-first-option
      placeholder="请选择标签" :style="{width:widthSty}">
      <el-option
        v-for="(item,index) in selectOptions" v-if="item.id"
        :key="index"
        :label="item.name"
        :value="item.name">
      </el-option>
    </el-select>
  </div>
</template>

<script>
import { listTagRelate } from "@/api/etp/tag/tag";
export default {
  name: 'tagsEdit',
  props:{
    widthSty:{
      type: String,
      default:'300px'
    },
    parentValue:{
      type: Array,
      default: []
    },
    selectOptions:{
      type: Array,
      default: []
    },
  },
  data() {
    return{
      value: this.parentValue,
      options: [],
    }
  },
  created() {

  },
  watch:{
    value(val){
      this.$emit('selectTags', val);
    },
    parentValue(val){
      this.value = val;
    }
  },
  methods: {

  }

}
</script>

<style scoped>

</style>
