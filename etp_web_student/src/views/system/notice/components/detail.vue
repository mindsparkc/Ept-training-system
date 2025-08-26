<template>
  <el-dialog
    :title="title"
    :visible.sync="open"
    :before-close="close"
    width="900px"
    append-to-body>
    <el-form ref="form" :model="form" label-width="80px">
      <div class="con-title">{{form.title}}</div>
      <div class="con-base">
        <span>发布时间：{{form.createTime}}</span>
<!--        <span>创建人：{{form.createBy}}</span>-->
      </div>
      <el-divider></el-divider>
      <div class="con-content ql-editor" v-html="form.content"></div>
    </el-form>
    <div slot="footer" class="dialog-footer" >
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "detail.vue",
  props:{
    title:{
      type:String,
      default:"通知公告详情"
    },
    open:{
      type:Boolean,
      default: false
    },
    form:{
      type:Object,
      default:function (){
        return {}
      }
    }

  },
  methods:{
    close(){
      this.$emit('closeDialog')
    }
  }
}
</script>

<style scoped lang="scss">
.con-title{
  font-size:24px ;
  font-weight: 700!important;
  text-align: center;
  padding-bottom: 10px;
}
.con-content{
  margin-top: 20px;
}
::v-deep .el-dialog{
  height: 86vh;
}
::v-deep .el-dialog__footer {
  bottom: 0px;
  position: absolute;
  right: 18px;
}
::v-deep  .el-dialog__body {
  padding: 10px 110px;
  overflow-y: auto;
  height: 80vh;
}
.con-base{
  text-align: center;
  color: #abaaaa;
  span:nth-child(2) {
    margin-left: 20px;
  }
}
</style>
