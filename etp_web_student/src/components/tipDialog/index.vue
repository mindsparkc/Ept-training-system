<template>
  <el-dialog
    title="提示"
    :visible.sync="tipVisible"
    :close-on-click-modal="false"
    width="30%">
    <div class="tip"><i class="el-icon-warning" style="color: #f1501f"></i>{{ infoTitle }}</div>
    <div class="del-con" v-show="Object.keys(tipInfo).length>0">
      <span class="title">信息内容</span>
      <div class="con-body" >
        <div class="del-item" v-for="(val,key) in tipInfo">
          <span>{{ key }}：</span>
          <span>{{ val }}</span>
        </div>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="tipClose">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "index",
  props:{
    tipVisible:{
      type:Boolean,
      default:false
    },
    tipInfo:{
      type:Object,
      default: {}
    },

    infoTitle:{
      default:"操作结果"
    }
  },
  data() {
    return {

    };
  },
  methods: {
    tipClose(){
      this.$emit("tipClose")
    },
    deleteInfo(){
      this.$emit("tipClose")
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-dialog__body {
  padding: 8px 20px 20px;
}
.tip{
  font-size: 16px;
  font-weight: 600;
  i{
    margin-right: 5px;
  }
}
.del-con{
  border:1px solid #dddddd;
  padding: 6px 14px;
  margin-top: 24px;
  position: relative;
}
span.title{
  display:block;
  width:70px;
  position:absolute;
  top:-9px;
  text-align: center;
  background: white;
}
.con-body{
  margin-top: 15px;
}
.del-item{
  padding: 0px 8px 8px;
}
</style>
