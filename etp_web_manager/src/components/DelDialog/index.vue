<template>
  <el-dialog
    title="提示"
    :close-on-click-modal="false"
    :visible.sync="delVisible"
    :before-close="delClose"
    width="30%">
    <div class="tip"><i class="el-icon-delete-solid" style="color: red"></i>确定要删除此信息？</div>
    <div class="del-con" v-show="Object.keys(delInfo).length>0">
      <span class="title">信息内容</span>
      <div class="con-body" >
        <div class="del-item" v-for="(val,key) in delInfo">
          <span>{{ key }}：</span>
          <span>{{ val }}</span>
        </div>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="delClose">取 消</el-button>
      <el-button type="primary" @click="deleteInfo">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "index",
  props:{
    delVisible:{
      type:Boolean,
      default:false
    },
    delInfo:{
      type:Object,
      default: {}
    },
    delInfoId:{
      default: null
    }
  },
  data() {
    return {

    };
  },
  methods: {
    delClose(){
      this.$emit("delClose")
    },
    deleteInfo(){
      this.$emit("deleteInfo",this.delInfoId)
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
