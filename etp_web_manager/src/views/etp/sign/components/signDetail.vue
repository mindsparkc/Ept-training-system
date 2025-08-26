<template>
  <div class="app-container">
    <div class="textBox pre-pad">
      <div style="position: relative;left:15px;height: 40px">
        <div class="pre-desc"><span style="margin-left:10px;font-size: 14px">活动报名基本信息</span></div>
      </div>
      <el-row>
        <el-col :span="4" style="text-align: center;">
          <el-image style="width: 200px; height: 150px;border-radius:5px;margin-top: calc(50% - 100px)"
                    :src="signForm.hdfm"
                    fit="cover"/>
        </el-col>
        <el-col :span="16" :offset="1">
          <el-form ref="form" label-width="150px" size="small">
            <el-row>
              <el-col :span="12">
                <el-form-item label="活动报名标题：">
                  <span>{{ signForm.hdmc }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动报名副标题：">
                  <span>{{ signForm.hdfmc?signForm.hdfmc:'暂无' }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="活动报名时间范围：">
                  <span>{{ signForm.kssj  }} ~ {{ signForm.jssj  }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="活动报名标签：">
                  <el-tag size="medium" :key="index" v-for="(item,index) in tags" style="margin-left:5px">{{ item }}</el-tag>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
        <el-col :span="1" :offset="1">
          <el-button size="small" @click="back">返 回</el-button>
        </el-col>
      </el-row>

      <el-divider/>
      <div class="paper pre-pad">
        <div style="position: relative;left:15px;height: 40px">
          <div class="pre-desc">
            <span style="margin-left:10px;font-size: 14px">活动报名内容</span>
          </div>
        </div>
        <div style="margin-left: 20px;padding:0 30px 30px">
            <div v-html="signForm.hdnr"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getSign } from "@/api/etp/sign";

export default {
  name: "signDetail",
  data(){
    return{
      signForm:{},
      tags:[],
    }
  },
  created() {
    let id = this.$route.params.id;
    if (id !== undefined) {
      this.getSign(id);
    }
  },
  methods:{

    getSign(id){
      getSign(id).then(res => {
        this.signForm = res.data;
        if (null != res.data.tags) {
          this.tags = res.data.tags.split(",");
        } else {
          this.tags = [];
        }
        console.log(res);
      })
    },

    back(){
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path:"/sign",
      });
    },
  }
}
</script>

<style scoped>
.subjectBox span{
  line-height: 40px;
  font-size: 15px;
}
.pre-desc:before{
  content: "";
  width: 3px;
  height:20px;
  border-radius: 5px;
  position: absolute;
  top: 0;
  left: 0;
  background: blue;
}
.pre-pad {
  padding: 15px 8px 8px;
}
.textBox{
  border-radius: 5px;
  /*height: 260px;*/
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.paper{
  height: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  border-radius: 5px;
  margin-top: 15px;
}
.pre-pad {
  padding: 15px 8px 8px;
}
</style>
