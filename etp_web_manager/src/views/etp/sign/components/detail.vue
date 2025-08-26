<template>
  <el-dialog
    :title="title"
    :visible.sync="open"
    :close-on-click-modal="false"
    :before-close="close"
    width="900px"
    append-to-body>
    <el-form ref="form" :model="form" label-width="80px">
      <div class="con-title">{{form.hdmc}}</div>
      <div class="con-f-title" v-if="form.hdfmc">{{form.hdfmc}}</div>
      <div class="con-desc">
        <el-image
          style="width: 100px; height: 100px;border-radius: 5px"
          :src="form.hdfm==null?require('@/assets/images/cover.jpg'):form.hdfm"
          fit="fit"/>
        <div class="con-base">
          <div>发布时间：{{form.createTime}}</div>
          <div>报名时间：{{form.kssj}} ~ {{form.jssj}}</div>
          <div style="margin-top: 2px;">
            <el-tag
              :key="index"
              size="medium"
              v-for="(item,index) in (form.tags||'').split(',')"
              v-if="item!=''"
              style="margin-right:5px">
              {{ item }}
            </el-tag>
          </div>
        </div>
      </div>

      <el-divider></el-divider>
      <div class="con-content ql-editor" v-html="form.hdnr"></div>
    </el-form>
    <div slot="footer" class="dialog-footer">
<!--      <el-button @click="cancel">关 闭</el-button>-->
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "detail.vue",
  props:{
    title:{
      type:String,
      default:"活动详情"
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
  data(){
    return{

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
  margin-bottom: 10px;
}
.con-f-title{
  text-align: center;
  font-size: 18px ;
  font-weight: 600;
}
.con-content{
  margin-top: 20px;
  overflow-y: auto;
  height: calc(80vh - 264px);
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
  height: 80vh;
}
.con-base{
  //color: #abaaaa;
  margin-left: 20px;
}
.con-desc{
  display: flex;
  margin-top: 22px;
  position: relative;
  .img-tag{
    background: #f28514;
    border-color: #f28514;
    color: #fff;
    position: absolute;
    border-radius: 2px;
    border-bottom-right-radius: 16px;
  }
  .sign-btn{
    position: absolute;
    right: 0px;
    bottom: 0px;
  }
}
.con-base{
  div{
    padding:4px
  }
}
</style>
