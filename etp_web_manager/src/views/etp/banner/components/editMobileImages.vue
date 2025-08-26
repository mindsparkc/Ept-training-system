<template>
  <div class="app-container">
    <div style="display: flex;">
      <div>
        <el-button  size="small" @click="back"> << 退出编辑 </el-button>
        <el-button type="primary" size="small" @click="save"> 保 存 </el-button>
      </div>
      <div style="flex: 2;">
          <div class="screen">
            <div class="navbar van-nav-bar van-hairline--bottom" style="z-index: 10;">
              <div class="van-nav-bar__left"></div>
              <div class="van-nav-bar__title van-ellipsis">首 页</div>
              <div class="van-nav-bar__right"></div>
            </div>

            <div v-if="fileArr.length <= 0">
              <img src="../../../../assets/etp/up.png">
            </div>
            <el-carousel v-else height="180px">
              <el-carousel-item v-for="(item,index) in fileArr" :key="index">
                <el-image
                  :src="item.url" style="width: 100%;height: 100%"
                  fit="cover"></el-image>
              </el-carousel-item>
            </el-carousel>
          </div>
      </div>

      <div style="flex: 2;padding-left: 20px;">
        <el-form label-width="80px" >
          <el-form-item label="模块名称">
            <el-tag size="small">手机端轮播图</el-tag>
          </el-form-item>
          <el-form-item label="轮播图片">
            <el-upload
              class="upload-demo"
              drag
              :action="baseUrl"
              :headers="headers"
              :on-success="handleUploadSuccess"
              :show-file-list="false"
              multiple>
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
            </el-upload>

            <br>
            <div v-if="fileArr.length > 0">
              <vuedraggable v-model="fileArr" animation="500">
                <transition-group>
                  <div v-for="(item,index) in fileArr" :key="index+1"
                       style="width: 150px; height: 120px;margin-right: 15px;position: relative;display: inline-block">
                    <el-image
                      style="width: 100%;height: 100%"
                      :src="item.url"
                      fit="cover" >
                    </el-image>
                    <span @click="clickImages(item,index)" class="actions"  :key="index+1"><i @click.stop="delImages(item,index)" title="删除" class="el-icon-delete icon"></i></span>
                  </div>
                </transition-group>
              </vuedraggable>
              <i class="el-icon-info input-tip">&nbsp;可拖拽图片进行排序，点击图片设置更多内容。</i>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <el-drawer
      title="设置图片信息"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose">
      <div style="margin: 0 30px">
        <el-form ref="URIform" :model="URIform" label-width="100px">
          <el-form-item label="轮播图Title">
            <el-input v-model="URIform.title" clearable></el-input>
          </el-form-item>
          <el-form-item label="输入地址">
            <el-input v-model="URIform.openUrl" clearable></el-input>
          </el-form-item>
          <el-form-item label="生效状态">
            <el-switch v-model="URIform.status"></el-switch>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
import vuedraggable from 'vuedraggable';
import { getNewBanners,addBanners } from "@/api/etp/banner";
export default {
  name: "editMobileImages",
  components:{
    vuedraggable
  },
  data(){
    return{
      baseUrl:process.env.VUE_APP_BASE_API + "/commom/upload/fastdfs",
      headers: {
        Authorization: "Bearer " + getToken(), 'x-platform': 'ADMIN'
      },
      fileArr:[],
      drawer: false,
      direction: 'rtl',
      uuid: undefined,
      URIform:{
        status:true
      },
      form:{},
    }
  },

  mounted(){
    this.getNewBanners();
  },

  watch:{
    fileArr:{
      handler(){
        this.$forceUpdate();
      },
      deep:true
    }
  },
  methods:{

    save(){
      this.form.source = 0;
      if (this.fileArr.length <= 0){
        this.$message.warning("暂未设置图片")
        return
      }
      this.form.imageurl = JSON.stringify(this.fileArr);
      addBanners(this.form).then(res => {
        this.$message.success("轮播图保存成功")
      })
    },

    // 回显操作
    getNewBanners(){
      getNewBanners({ source: 0}).then(res =>{
        if (res.code === 200){
          if (res.rows.length > 0){
              let rows = JSON.parse(res.rows[0].imageurl);
              this.fileArr = []
              rows.forEach(item => {
                this.fileArr.push({
                  uuid: item.uuid,
                  fileName:item.name,
                  url:item.url,
                  openUrl:item.openUrl,
                  title:item.title,
                  status:item.status
                })
              })
          }
        }
      })
    },

    handleClose(done){
      this.$confirm('确认关闭？')
        .then(_ => {
          this.fileArr.forEach(i => {
            if (this.uuid === i.uuid){
              i.title = this.URIform.title;
              i.openUrl = this.URIform.openUrl;
              i.status = this.URIform.status;
            }
          })
          done();
        })
        .catch(_ => {});
    },

    clickImages(item,index){
      this.drawer = true;
      this.URIform.title = item.title;
      this.URIform.openUrl = item.openUrl;
      this.URIform.status =  item.status;
      this.uuid = item.uuid;
    },


    delImages(item,index){
      this.$confirm('此操作将删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.fileArr.splice(index,1)
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {});
    },

    // 上传成功回调
    handleUploadSuccess(response, file, fileList) {
      if (response.code == 200 && response.msg == "操作成功"){
        this.uuid = Date.now()
        this.fileArr.push({
          uuid: this.uuid,
          fileName:file.name,
          url:response.url,
          openUrl:'',
          title:'',
          status:true
        })
        this.reset();
        this.drawer = true;
      }
    },

    // 表单重置
    reset() {
      this.URIform = {
        title: null,
        openUrl:null,
        status:true
      }
      this.resetForm("URIform");
    },

    /** 返回*/
    back() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({
        path:"/etp/banner/index/" ,
        query:{ t:Date.now() }
      });
    },
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 180px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.van-nav-bar{
  position: relative;
  height: 46px;
  line-height: 46px;
  text-align: center;
  background-color: #fff;
  -webkit-user-select: none;
  user-select: none;
  overflow: hidden;
}
.van-nav-bar__left, .van-nav-bar__right {
  position: absolute;
  bottom: 0;
  font-size: 14px;
}
.van-nav-bar__left {
  left: 16px;
}
.van-nav-bar__right{
  right: 16px;
}
.van-nav-bar__title {
  max-width: 60%;
  margin: 0 auto;
  color: #323233;
  font-weight: 500;
  font-size: 16px;
}
.van-ellipsis {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.screen{
  background: url("../../../../assets/etp/bg.png") 100% 100% no-repeat;
  position: relative;
  margin: 0 auto;
  padding: 50px 20px 0;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  width: 440px;
  height: 800px;
}

::v-deep .el-upload-dragger{
  width: 250px;
  height: 150px;
}
::v-deep .el-icon-info{
  color: #909399
}
::v-deep .actions{
  cursor: move;
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0,0,0,.5);
  -webkit-transition: opacity .3s;
  overflow: hidden;
  transition: opacity .3s;
}
::v-deep .actions:hover{
  opacity: 1;
}
::v-deep .el-icon-delete{
  margin-top: calc(50% - 25px);
}
</style>
