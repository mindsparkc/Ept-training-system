<template>
  <div>
    <el-dialog
      title="deepseek"
      :visible.sync="dialogVisible"
      width="1200px"
      :before-close="colse">
      <div>

        <!--  右侧对话栏   -->
        <div  class="right-menu">

          <div class="right-area">

            <div class="dh-area">
              <div v-for="(item,index) in dataList">
                <div class="question-right" v-if="item['key']=='user'" >
                  <div>{{item.value}}</div><img :src="require('@/assets/images/deepseek.png')" alt="" >
                </div>

                <div  class="answer" v-if="item['key']=='assistant'">
                  <img :src="require('@/assets/images/deepseek.png')" alt="" style="margin-right: 3px;height: 32px">
                  <div style="white-space: pre-wrap;" v-html="item.value"></div>
                </div>

              </div>
              <div class="loading" v-if="isLoading">加载中... <i class="el-icon-loading"></i></div>
            </div>

            <div class="input-item">
              <div class="reset-btn" @click="reset"><i class="el-icon-chat-dot-square"></i> 开始新对话</div>
              <el-input v-model="input"  type="textarea"
                        :rows="3"
                        @keyup.enter.native="send"
                        placeholder="给 Deepseek 发送消息"
              >
              </el-input>
              <div class="up" @click="send">
                <i class="el-icon-top" ></i>
              </div>
            </div>
          </div>


        </div>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="colse">关 闭</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import {streamChat,deleteRedisCache} from "@/api/home/dh"
export default {
  name: "dh",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },

  },
  data() {
    return {
      input: "",
      dataList:[],
      isLoading: false
    };
  },
  methods: {
    colse(){
      //清空对话
      this.reset()
      this.$emit('close')

    },

    send(){
      this.dataList.push({key:"user",value:this.input})

      this.isLoading = true
      streamChat(this.input).then(res => {

        if(res.code==200){

          let data = JSON.parse(res.msg)
          this.isLoading = false
          this.dataList.push({key:"assistant",value:data.choices[0].message.content})

        }else{
          this.isLoading = false
          this.$modal.msgError(res.msg)

        }

      });
      this.input = ""

    },
    reset(){
      deleteRedisCache().then(res => {
        this.dataList = []
      })
    }
  }
}

</script>
<style scoped lang="scss">

.right-menu{
  display: inline-block;
  width: 100%;
  height: calc(85vh - 84px);
  padding-top: 20px;

  .right-area{
    width: 70%;
    margin: 0 auto;
    height: 100%;
    position: relative;

  }
  .dh-area{
    overflow: auto;
    height: calc(100% - 150px);
    width: 100%;
    position: absolute;
  }
}

.input-item{
  margin: 20px;
  position: absolute;
  bottom: 0px;
  width: calc(100% - 40px);
}
::v-deep .el-textarea__inner{
  padding-right: 80px;
}
.up{
  width: 38px;
  height: 38px;
  border-radius: 50px;
  background: #1079e1;
  position: absolute;
  right: 25px;
  bottom: 10px;
  cursor: pointer;
}
.up:hover{
  box-shadow: 0 0 10px #1079e1;
}
.el-icon-top{
  color: #fff;
  font-size: 20px;
  position: absolute;
  font-weight: 600;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
}
.reset-btn{
  background-color: rgb(219 234 254);
  color: #4d6bfe;
  cursor: pointer;
  z-index: 1;
  border-radius: 12px;
  width: fit-content;
  padding: 2px 14px;
  font-size: 14px;
  line-height: 28px;
  margin: 20px auto;
}
.question-right{

  display: flex;
  justify-content: flex-end;
  >div{
    height: 40px;
    line-height: 40px;
    font-size: 18px;
    background: #eff6ff;
    padding: 0 20px;
    border-radius: 14px;
  }

  >img{
    margin-left: 3px;
    height: 32px;
    margin-top: 4px;
  }
}
.answer{
  padding: 20px;
  display: flex;
  justify-content: flex-start;
}

.loading{
  background-color: rgb(219 234 254);
  width: fit-content;
  padding: 10px 20px;
  border-radius: 14px;
  color: blue;
  font-weight: 600;
  margin: 20px;
  i{
    color: blue;
  }
}
::v-deep .el-dialog__body{
  padding: 0px;
}
</style>
