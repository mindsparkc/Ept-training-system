<template>
  <div class="app-container">
    <div class="parent">
      <div class="div1" @click="toImagePage('mobile')">
        <div class="cover">
          <img src="../../../assets/etp/entrance_home_mobile.png">
        </div>
        <div class="text">用于设计学员手机访问学员端时首页的页面风格，自由定义轮播图</div>
      </div>
      <div class="div2" @click="toImagePage('PC')">
        <div class="cover">
          <img src="../../../assets/etp/entrance_home_pc.png">
        </div>
        <div class="text">用于设计学员电脑访问学员端时首页的页面风格，自由定义轮播图</div>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "index",

  methods:{
    toImagePage(type){
      if (type == 'PC'){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({
          path:"/etp/banner/components/editPCImages/" ,
        });
      }else {
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({
          path:"/etp/banner/components/editMobileImages/" ,
        });
      }
    }
  }
}
</script>

<style>
.parent {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: 1fr;
  grid-column-gap: 50px;
  grid-row-gap: 0px;
  margin-left: 50px;
  margin-right: 50px;
}

.div1 { grid-area: 1 / 1 / 2 / 2;}
.div2 { grid-area: 1 / 2 / 2 / 3;}
.div3 { grid-area: 1 / 3 / 2 / 4;}

.div1,.div2,.div3{
  height: 280px;padding: 15px;
  margin-bottom: 45px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  -webkit-transition: .2s ease;
  transition: .2s ease;
}
.div1:hover,.div2:hover,.div3:hover{
  box-shadow: 5px 5px 5px #ccc;
  z-index: 10;
}

.text{
  padding: 12px 18px;
  line-height: 30px;
  font-size: 16px;
  color: #666;
}
</style>
