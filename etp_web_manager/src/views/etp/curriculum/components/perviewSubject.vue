<template>
  <!-- 预览试题 -->
  <div>
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="类型：" prop="lx">
        <el-radio disabled v-model="form.lx"
                  :label="dict.dictValue" v-for="dict in lxOptions">
          {{ dict.dictLabel }}
        </el-radio>
      </el-form-item>
      <el-form-item label="题干：" prop="tgDisplat">
        <el-input disabled v-model="form.tgDisplat" placeholder="请输入内容"/>
      </el-form-item>
      <div v-if="form.lx=='1'||form.lx=='2'">
        <el-form-item label="选项：" v-for="(item, index) in form.xxList" class="xx-div"
                      :key="item.key"
                      :prop="'xxList.' + index + '.value'"
                      :class="{'labNone':index!=0}">
          <el-input disabled placeholder="请输入选项" v-model="item.value">
            <template slot="prepend">{{ item.key }}</template>
          </el-input>
        </el-form-item>
      </div>

      <el-form-item label="正确答案：" prop="da" v-if="form.lx!='4'&&form.lx!='5'">
        <el-radio v-if="form.lx=='1'"  v-model="dx_radio" :label="item.key" v-for="item in form.xxList"> {{ item.key }}</el-radio>

        <el-checkbox-group v-model="da_checkList" v-if="form.lx=='2'">
          <el-checkbox  :label="item.key" v-for="item in form.xxList"> {{item.key}}</el-checkbox>
        </el-checkbox-group>

        <el-radio-group v-if="form.lx=='3'" v-model="pd_radio">
          <el-radio  :label="'1'"> 对</el-radio>
          <el-radio  :label="'0'"> 错</el-radio>
        </el-radio-group>
      </el-form-item>

      <!--          填空题-->
      <div  v-if="form.lx=='4'">
        <el-form-item label="正确答案：" class="xx-div"
                      :prop="`tk_list.${index}.value`"
                      v-for="(item,index) in form.tk_list"
                      :key="index"
                      :rules="{ required: true, message: '答案不能为空', trigger: 'blur'}"
                      :class="{'labNone':index!=0}">
          <el-input placeholder="请输入答案"  v-model="item.value"/>
        </el-form-item>
      </div>
      <el-form-item label="答案解析：" prop="jx" v-if="form.sflx == '1' && form.lx!='5'">
        <el-input v-model="form.jx" type="textarea" placeholder="请输入内容"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "perviewSubject",
  props: {
    form: Object,
    default: {}
  },
  mounted() {
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;
    });
  },
  data() {
    return {
      // 类型
      lxOptions: [],
      //答案选项
      da_checkList:['A'],
      dx_radio: '',
      pd_radio: '1',//判断答案
    }
  },
}
</script>

<style scoped>

</style>
