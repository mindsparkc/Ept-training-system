<template>
  <div class="app-container app-container-custome">
    <el-row :gutter="24">
      <el-col :span="24">
        <el-steps :active="active" simple finish-status="success">
          <el-step>
            <slot slot="title">第一步：登记试卷信息</slot>
          </el-step>
          <el-step>
            <slot slot="title">第二步：组织试卷内容</slot>
          </el-step>
          <el-step>
            <slot slot="title">第三步：设置分值</slot>
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
<!--    试卷基本信息-->
    <div v-show="active==0" style="margin-top: 30px">
      <baseform ref="baseForm"></baseform>
    </div>
<!--    第二步 选择试题-->
    <div v-show="active==1" style="margin-top: 20px;display: flex">
<!--      左侧序号 根据右侧题目总数渲染题号、选题个数标绿-->
      <div class="left-div">
        <div v-for="(item,key) in txjs">
          <span>{{selectDictLabel(lxOptions,key)}}
            <span v-if="paperBaseFrom.zjcl != '2'">
              （{{xz_js[key]==undefined?0:xz_js[key]}}/{{init_txjs[key]==undefined?0:init_txjs[key].length}}）
            </span>
<!--            随机生成考卷-->
            <span v-else>
              （{{init_txjs[key].length}}）
            </span>
          </span>

          <ul class="xh-ul" v-if="paperBaseFrom.zjcl != '2'">
            <!--根据选择的实际个数来渲染-->
            <li v-for="(data,index) in item" v-show="data!=undefined" :class="getColor(data,index+1,init_txjs[key].length)" >
              <a :href="`#${key}&${index+1}`">{{index+1}}</a>
            </li>
          </ul>
        </div>
      </div>

<!--      右侧选题区-->
      <div class="right-div">
        <div class="mc-title">{{paperBaseFrom.sjmc}}</div>
        <div style="padding: 0px 20px;">
          <div v-for="(item,key) in txjs">
            <!--            题目类型-->
            <div class="tm-title" >
              <span style="font-weight: 600;">{{ selectDictLabel(lxOptions,key) }}</span>
              <el-button type="primary" size="mini" plain @click="getTm(key)" class="sel-plus">批量选择题目</el-button>
            </div>


            <!-- 题目区域--非随机生成考卷展示 -->
            <div class="tm-div" v-if="paperBaseFrom.zjcl != '2'" v-for="(data,j) in item" >
              <div class="tg-div" :id="`${key}&${j+1}`" v-if="undefined != data">
                <p>{{j+1}}、<el-tag size="mini" type="success" style="margin-left: 5px">{{levelFormat(data.level)}}</el-tag>
                  <span class="tg-span" v-if="undefined != data" v-html="data.tg"></span><!--{{data.tg}}-->
                </p>
                <div v-if="undefined != data">
                  <div v-if="data.xx != '' && undefined != data.xx"  >
                    <p v-for="xx_item in JSON.parse(data.xx)" class="xx-div">
                      {{ xx_item.key }}、{{xx_item.value}}
                    </p>
                  </div>
                  <div class="da-div">
                    答案：<span v-if="data.lx == '3'">{{ data.da=='1'?'对':'错' }}</span>
                    <span v-else-if="data.lx == '5'">{{ data.jx }}</span>
                    <span v-else>{{data.da}}</span>
                  </div>
                </div>
              </div>
              <!--操作区域-->
              <div class="oper-div">
                <el-tooltip class="item" v-show="item.length==xz_js[key]" effect="dark" content="上移" placement="top-end">
                  <i class="el-icon-top  etp_font_size" style="color: blue;" @click="moveUp(item,data, j)"></i>
                </el-tooltip>
                <el-tooltip class="item" v-show="item.length==xz_js[key]" effect="dark" content="上移" placement="top-end">
                  <i class="el-icon-bottom  etp_font_size" style="color: blue;" @click="moveDown(item,data, j)"></i>
                </el-tooltip>
                <el-tooltip class="item" v-show="data!=undefined" effect="dark" content="删除" placement="top-end">
                  <i class="el-icon-delete etp_font_color_red etp_font_size" @click="delData(item, j)" style="color: red;"></i>
                </el-tooltip>
              </div>
            </div>

            <div class="sj-div" v-if="paperBaseFrom.zjcl=='2'">
              <p>已选题目：{{xz_js[key]}}</p>
              <p>题目标签：
                <el-tag type="success" v-for="tag in sj_tags[key]">{{ tag }}</el-tag></p>
            </div>
          </div>
        </div>
      </div>
    </div>

<!-----------------第三步------------------------->
    <div v-show="active==2" style="margin-top: 30px">
      <div v-for="(item,key) in init_txjs" class="fen-div" v-if="item.length>0">
        <p>{{selectDictLabel(lxOptions,key)}}</p>
        <span>共{{item.length}}题，共{{isNaN(item.length*sz_fz[key])?0:item.length*sz_fz[key]}}分</span><br>
        <span>每题 <el-input class="fen-input" v-model="sz_fz[key]"></el-input> 分</span>
      </div>
      <div class="sum-item">
        <span>总题数：{{all_tm_num}}</span><br>
        <span>当前总分：{{all_score}}</span>
      </div>
    </div>

    <div class="btns">
      <div class="btns-bt">
        <el-button @click="qxBtn">取 消</el-button>
        <el-button @click="saveTm()" type="primary" v-if="active == 1" :disabled="btn_dis">暂 存</el-button>
        <el-button @click="preStep" v-if="active > 0">上一步</el-button>
        <el-button @click="nextStep" v-if="active < 2">下一步</el-button>
        <el-button v-if="active == 2" type="primary" @click="saveFz('0')" >暂 存</el-button>
        <el-button v-if="active == 2" type="primary" @click="saveFz('99')" >发 布</el-button>
      </div>
    </div>

  <el-dialog
    :title="`${zjclFormat(this.paperBaseFrom.zjcl)}选题`"
    :visible.sync="st_open"
    :close-on-click-modal="false"
    width="1000px" append-to-body class="st_dialog">
    <addSTHand ref="addSTHand" :tm_type="tm_type"
               :lxOptions="lxOptions"
               :zjcl="paperBaseFrom.zjcl"
               :szgs="init_txjs[tm_type]==undefined?0:init_txjs[tm_type].length"></addSTHand>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="saveHandST">确 定</el-button>
      <el-button @click="cancelHandST">取 消</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
import baseform from "./components/baseForm";
import addSTHand from "./components/addSTHand";
import { getTestpaper, addTestpaper, updateTestpaper } from "@/api/etp/testpaper/testpaper";
import { getSubjectRand } from "@/api/etp/subject/subject";
import { savePaperSubject,getSubjectByPaper,getPaperSubjectTags,updateScore,selectScoreByType } from "@/api/etp/testpaper/testpapersubject";

export default {
  name: "paper",
  components: {
    baseform,
    addSTHand
  },
  data() {
    return {
      active:0,

      //组卷策略
      zjclOptions:[],
      paperBaseFrom:{
        tx: null,
      },
      //设置的题型及个数
      sztx:[], //[{key:1,value:12}]
      tagValues:[],
      txgs:[],//题型个数，用于记录变化之前的个数

      //第二步
      btn_dis: true,
      txjs:{},//右侧题型计数,新增之后会填充数据,可能会大于设置的数： {1:[1,2,3],2:[data],3:[]}
      init_txjs:{}, //txjs，原始值，不填充数据，等于设置的数，不会变化
      xz_js:{},//记录选中的个数（根据txjs）:{"1":3,"2":3}

      //题目列表
      loading:false,
      // 非多个禁用
      multiple: true,
      // tList:[],
      lxOptions:[],
      st_open:false,
      tm_type:null,
      st_params:{
        params: {ids:null}
      },

      sj_num:0,
      sj_tags:{},
      sz_fz:{},//设置分值
      all_score:0,
      all_tm_num:0,
      // 难易程度
      levelOptions: [],
    };
  },
  created() {
    this.getDicts("testpaper_zjcl").then(response => {
      this.zjclOptions = response.data;
    });
    this.getDicts("subject_lx").then(response => {
      this.lxOptions = response.data;

      let txg = []//用于记录题型个数
      for(let i in this.lxOptions){
        txg.push({key:this.lxOptions[i].dictValue,value:0})
      }
      this.$nextTick(()=>{
        this.$refs.baseForm.txgs = txg
      })
    });

    const id = this.$route.params.paperId;

    if("" != id){
      this.getInfo(id);
    }
    this.getDicts("subject_level").then(response => {
      this.levelOptions = response.data;
    });

  },
  watch:{
    //监控变化，统计选择了各个题型的个数
    txjs:{
      handler(val,old){
        let lx_count = {}
        for(let key in val ){
          let num = 0;
          for(let i in val[key]){
            if(val[key][i] != undefined){
              num++;
            }
          }
          lx_count[key] =num
        }
        this.xz_js = lx_count
      },
      // immediate:true,
      deep:true
    },
    sz_fz:{
      handler(val,old){
        this.all_score = 0
        for(let key in val){
          this.all_score += val[key] * this.init_txjs[key].length
        }
      },
      deep:true
    },
  },
  methods: {
    // 难易程度：
    levelFormat(level, column) {
      return this.selectDictLabel(this.levelOptions, level);
    },
    qxBtn(){
      if (this.$route.query.from && this.$route.query.from === 'index'){
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({ path: "/index", query: { t: Date.now() }});
        return
      }
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/etp/testpaper", query: { t: Date.now() }});

    },
    zjclFormat(zjcl, column) {
      return this.selectDictLabel(this.zjclOptions, zjcl);
    },
    //下一步
    nextStep(){
      //第一步
      if(this.active ==0){
        //校验
        this.$refs.baseForm.$refs["form"].validate(valid => {
          if (valid) {
            this.paperBaseFrom = this.$refs.baseForm.form
            //校验题目设置
            if(this.checkTx()){
              //暂存试卷基本信息
              this.paperBaseFrom.tx = JSON.stringify(this.sztx)
              let tags = this.$refs.baseForm.tagValue
              if(tags.sort().toString() ==this.tagValues.sort().toString()){
                this.paperBaseFrom.tagFlag ="0"
              }else{
                this.paperBaseFrom.tagFlag ="1"
                this.tagValues = tags
              }

              //判断题型设置个数是否改变
              let tips = []
              const h=this.$createElement
              if(this.txgs.length>0 && this.sztx.length>0){
                this.sztx.forEach(item=>{
                  if(item.value != this.txgs[item.key]){
                    tips.push(h('p',null,`【${this.selectDictLabel(this.lxOptions,item.key)}】个数由${this.txgs[item.key]}个变成${item.value}个`))
                  }
                })

              }
              if(tips.length>0){
                this.$confirm('提示',{message:h('div',null, tips),
                  title:'提示',
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  //暂存第一步的信息
                  addTestpaper(this.addTags(this.paperBaseFrom,this.tagValues)).then(response => {
                    this.paperBaseFrom.id = response.data;
                    //查询是否暂存了题目数据，暂存了，回显
                    this.subjectBack();
                    this.active++
                  });
                }).catch(() => {});
              }else{
                //暂存第一步的信息
                addTestpaper(this.addTags(this.paperBaseFrom,this.tagValues)).then(response => {
                  this.paperBaseFrom.id = response.data;
                  //查询是否暂存了题目数据，暂存了，回显
                  this.subjectBack();
                  this.active++
                });
              }

            }
          }
        })
      }else if(this.active ==1){

        if(this.paperBaseFrom.zjcl !="2"){
          let flag = true;//校验标记
          //比较选中的数据是否超过设置的数据，超过，给用户提示
          for(let key in this.xz_js){
            if(this.xz_js[key] != this.init_txjs[key].length){
              this.$message("选择题目数量与设置数量不符，请检查修改！")
              flag = false
            }
          }
          if(flag){
            //判断
            if(!this.btn_dis){//未暂存
              this.$confirm('是否需要暂存数据?【确定】，自动暂存；【取消】，不暂存', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                //暂存数据
                this.saveTm("1")
                this.active=2
              }).catch(() => {
                this.active =2
              });
            }else{
              this.active =2
            }
          }
        }else{
          this.active=2
        }
        //回显分值
        selectScoreByType(this.paperBaseFrom.id).then(response => {
          this.sz_fz = response.data
        })
        if(null != this.paperBaseFrom.fm && this.paperBaseFrom.fm.indexOf("/img/cover")>-1){
          this.paperBaseFrom.fm = null
        }
      }
    },
    //上一步
    preStep(){
      if (this.active > 0) {
        this.active--
      }
      if(this.active ==0){
        this.getInfo(this.paperBaseFrom.id)
      }
      if(null== this.paperBaseFrom.fm){
        this.paperBaseFrom.fm =require("@/assets/images/cover.jpg") ;
      }

    },
    //校验选择的题型及个数
    checkTx(){
      let tx = this.$refs.baseForm.txgs
      this.sztx=[]
      this.all_tm_num = 0
      let txjs_tmp = {}
      for(let i in tx){
        this.sztx.push({key:tx[i].key,value:tx[i].value})

        //第二步右侧 选择题目计数
        let cnt= parseInt(tx[i].value)
        this.init_txjs[tx[i].key] = new Array(cnt)
        this.all_tm_num += cnt
        if(cnt>0){
          if(this.paperBaseFrom.zjcl !="2"){
            txjs_tmp[tx[i].key]=new Array(cnt)
          }else{
            txjs_tmp[tx[i].key]=new Array()
          }
        }
      }
      this.txjs = txjs_tmp
      console.log(this.init_txjs)
      // this.init_txjs = JSON.parse(JSON.stringify(txjs_tmp))
      return true;
    },

    //暂存选择的题目
    saveTm(param) {
      let data = Object.assign({},this.txjs)
      data["sjid"] = this.paperBaseFrom.id;
      savePaperSubject(data).then(response => {
        this.btn_dis = true
        // if(param=="1"){
        //   this.active++
        // }
        this.msgSuccess("暂存成功");
      });

    },
    //保存分值设置
    saveFz(fb){
      let param = {};
      param["sjid"] = this.paperBaseFrom.id;
      param["fenMap"] = this.sz_fz
      param["fb"] = fb
      if(fb=='0'){
        updateScore(param).then(response =>{
          this.msgSuccess("暂存成功");
        })
      }else{
        this.$confirm('请确认是否发布当前试卷信息？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //暂存数据
          updateScore(param).then(response =>{
              this.msgSuccess("发布成功");
            this.$store.dispatch("tagsView/delView", this.$route);
            this.$router.push({path: "/etp/testpaper", query: {t: Date.now()}})
          })
        }).catch(() => {

        });
      }



    },
    //试卷信息回显 第一步
    getInfo(id){
      getTestpaper(id).then(response => {
        this.paperBaseFrom = response.data;
        this.$refs.baseForm.form = response.data;
        this.$refs.baseForm.form.fm = response.data.fm == ""|| response.data.fm == null? require("@/assets/images/cover.jpg") : response.data.fm;
        this.tagValues = [];
        //标签回显
        if (null != response.data.tags) {
          this.tagValues = response.data.tags.split(",");
        }
        //题型设置回显
        let txsz = JSON.parse(response.data.tx)//设置的题型数据
        let xztx =[]//选中题型
        let txg =[]//选中题型个数
        for(let i in txsz){
          let cnt = parseInt(txsz[i].value)
          if(cnt>0){
            xztx.push(txsz[i].key)

          }
          this.$set(this.txgs,txsz[i].key,cnt)
          txg.push({key:txsz[i].key,value:cnt})
        }
        this.$nextTick(()=>{
          this.$refs.baseForm.tagValue = this.tagValues
          this.$refs.baseForm.xztx = xztx
          this.$refs.baseForm.txgs = txg

        })
      });
    },

    //第二步 查询暂存数据，并回显
    subjectBack(){
      getSubjectByPaper(this.paperBaseFrom.id).then(response => {
        let list = response.data;
        let txjs_data = this.txjs;
        for(let key in list){
          for(let i in list[key]) {
            let len = txjs_data[key] != undefined ? txjs_data[key].length : 0
            //获取的数据比设置的数据多
            if (i < len) {
              this.$set(txjs_data[key], i, list[key][i])
            } else {
              if(txjs_data[key] == undefined){
                txjs_data[key] = new Array();
              }
              txjs_data[key].push(list[key][i])
            }
          }
        }
        this.txjs = Object.assign({},txjs_data)
      })
      //标签回显
      if(this.paperBaseFrom.zjcl =="2"){
        // this.sj_tags[this.tm_type] = new Array();
        getPaperSubjectTags(this.paperBaseFrom.id).then(response => {
          let tag_list = response.data
          for(let key in tag_list){
            this.sj_tags[key] = new Array();
            for(let i in tag_list[key]){
              this.$set(this.sj_tags[key],i,tag_list[key][i].name)
            }
          }
          this.$forceUpdate()
        })
      }
    },
    moveUp(data, row, index) {
      if (index > 0) {
        let upData = data[index - 1];
        data.splice(index - 1, 1);
        data.splice(index, 0, upData);
        this.btn_dis = false
      } else {
        this.$message({
          message: '已经是第一条，上移失败',
          type: 'warning'
        });
      }
    },
    moveDown(data, row, index){
      if ((index + 1) == data.length) {
        this.$message({
          message: '已经是最后一条，下移失败',
          type: 'warning'
        });
      } else {
        let downData = data[index + 1];
        data.splice(index + 1, 1);
        data.splice(index, 0, downData);
        this.btn_dis = false
      }
    },
    delData(data, index){
      this.$set(data,index,undefined)
      this.btn_dis = false
    },


    //*********题目列表*********
    getTm(type){

      this.tm_type = type
      this.$nextTick(()=>{
        this.$refs.addSTHand.xz_data = this.txjs[type].filter(item=>{

          return item !=undefined
        });
        this.$refs.addSTHand.resetQuery()
      })
      this.st_open = true
    },
    // 类型：
    lxFormat(row, column) {
      return this.selectDictLabel(this.lxOptions, row.lx);
    },


    //确定选择的试题
    saveHandST(){
      let xz_data = this.$refs.addSTHand.xz_data
      let xz_ids = []
      xz_data.forEach( item => {
        xz_ids.push(item.id)
      })

      //比较选中的数据是否超过设置的数据，超过，给用户提示
      let tm_num = this.init_txjs[this.tm_type].length
      if(this.paperBaseFrom.zjcl == "0") {
        if (xz_data.length > tm_num) {
          this.$message(`选中题目数量超过设置数量${xz_data.length - tm_num}条，请修改`)
        } else {
          this.st_open = false;
          this.setXzData(xz_data)
          this.btn_dis = false//暂存按钮可用
        }
      }else if(this.paperBaseFrom.zjcl == "1"){
        let param = {ids:xz_ids.join(','),num:tm_num}
        getSubjectRand(param).then(response =>{
          let data = response.data
          this.setXzData(data)
          this.st_open = false;
          this.btn_dis = false//暂存按钮可用
        })
      }else{
        //随机生成考卷
        if(xz_ids.length<1){
          this.$message(`请选择题目`)
        }else{
          this.sj_num = xz_data.length

          this.txjs[this.tm_type] = new Array();
          this.sj_tags[this.tm_type] = new Array();
          xz_data.forEach(item=>{
            //执行代码
            this.txjs[this.tm_type].push(item)
            if(null != item.tags){
              let tagsArr = item.tags.split(',')
              tagsArr.forEach(tag=>{
                if(this.sj_tags[this.tm_type].indexOf(tag) < 0){
                  this.sj_tags[this.tm_type].push(tag)
                }
              })
            }
          })
          this.st_open = false;
          this.btn_dis = false//暂存按钮可用
        }
      }
    },
  /*  saveHandST(){
      let xz_ids = this.$refs.addSTHand.ids;

      //比较选中的数据是否超过设置的数据，超过，给用户提示
      let tm_num = this.init_txjs[this.tm_type].length
      //自选题组卷
      if(this.paperBaseFrom.zjcl == "0"){
        if(xz_ids.length>tm_num){
          this.$message(`选中题目数量超过设置数量${xz_ids.length-tm_num}条，请修改`)
        }else{
          this.st_open = false;

          this.st_params.params['ids'] = xz_ids
          listSubject(this.st_params).then(response => {
            let data = response.rows
            this.setXzData(data)
            this.btn_dis = false//暂存按钮可用
          });
        }
      }else if(this.paperBaseFrom.zjcl == "1"){
        if(xz_ids.length<1){
          this.$message(`请选择题目`)
        }else{
          let param = {ids:xz_ids.join(','),num:tm_num}
          getSubjectRand(param).then(response =>{
            let data = response.data
            this.setXzData(data)
            this.st_open = false;
            this.btn_dis = false//暂存按钮可用
          })
        }
      }else{
        //随机生成考卷
        if(xz_ids.length<1){
          this.$message(`请选择题目`)
        }else{
          this.sj_num = xz_ids.length
          this.st_params.params['ids'] = xz_ids
          listSubject(this.st_params).then(response => {
            let data = response.rows
            this.txjs[this.tm_type] = new Array();
            this.sj_tags[this.tm_type] = new Array();

            data.forEach(item=>{
              //执行代码
              this.txjs[this.tm_type].push(item)
              if(null != item.tags){
                let tagsArr = item.tags.split(',')
                tagsArr.forEach(tag=>{
                  if(this.sj_tags[this.tm_type].indexOf(tag) < 0){
                    this.sj_tags[this.tm_type].push(tag)
                  }
                })
              }
            })
            this.st_open = false;
            this.btn_dis = false//暂存按钮可用
          });
        }
      }
    },*/

    setXzData(data){
      let txjs_temp = this.txjs[this.tm_type];
      //给题型计数txjs 赋值  = table 选中的数据
      let len = txjs_temp.length;
      for(let i =0 ;i <len;i++){
        if(i<data.length){
          this.$set(this.txjs[this.tm_type],i,data[i])
        }else{
          this.$set(this.txjs[this.tm_type],i,undefined)
        }
      }
    },
    cancelHandST(){
      this.st_open = false
      this.$nextTick(()=>{
        this.$refs.addSTHand.resetQuery();
        //取消选中
        this.$refs.addSTHand.$refs.multipleTable.clearSelection();
      })
    },
    getColor(data,index,allLength){

      if(undefined != data && index>allLength){
        return 'red'
      }else if(undefined != data){
        return 'active'
      }
    }
  }



}
</script>

<style scoped lang="scss">
.app-container-custome {
  margin: 20px 30px;
  border-radius: 8px;
  border: 1px solid rgba(167, 171, 183, 0.25);
  background: rgba(241, 245, 252, 0.3);
}
.btns {
  margin-top: 30px;
  position: relative;
  height: 36px;
}
.btns-bt{
  position: absolute;
  right: 20px;
}
.left-div{
  width: 200px;
  height: calc(100vh - 84px - 46px - 166px);
  border: 1px solid #eeeeee;
  padding: 12px;
  overflow-y: scroll;
  div{
    padding: 5px;
  }
}
.right-div{
  width: calc(100% - 200px - 20px);
  margin-left: 20px;
  height: calc(100vh - 84px - 46px - 166px);
  border: 1px solid #eeeeee;
  overflow-y: auto;
}
.mc-title{
  width: 100%;
  height: 32px;
  text-align: center;
  line-height: 32px;
  background: #eee;
  color: #1e1e1e;
}
.tm-title{
  color: #6f6969;
  width: 200px;
  height: 36px;
  margin-top: 20px;
  line-height: 36px;
}
.tm-div{
  min-height: 20px;
  display: flex;
}
.sj-div{
  min-height: 100px;
  p{
    padding-left: 30px;
    font-size: 14px;
  }
}
.tg-div{
  width: calc(100% - 20px - 64px - 20px);
  margin-left: 20px;
  border-bottom: 1px solid #eeeeee;
}
.oper-div{

}
.item{
  padding: 5px;
}
.sel-plus{
  margin-left: 10px;
}
.xh-ul{
  padding-left: 0px;
  li{
    width: 20px;
    height: 20px;
    border: 1px solid #a6a6a6;
    text-align: center;
    line-height: 20px;
    display: inline-block;
    margin-right: 5px;
  }
  .active{
    background: #1ab394;
  }
  .red{
    background: #e20b24;
  }
}
.xx-div{
  color: #a7a6a6;
  padding-left: 20px;
}
.da-div{
  margin-bottom: 10px;
  padding-left: 20px;
  color: #a7a6a6;
}
.el-tag.el-tag--success {
  margin-right: 5px;
}
::v-deep .el-dialog__body {
  padding: 0px 20px;
}
.fen-input{
  width: 50px;
}
.fen-input ::v-deep .el-input__inner {
  height: 26px;
  line-height: 26px;
}
.fen-div{
  border-bottom: 1px solid #eeeeee;
  padding: 5px 0px;
  span{
    font-size: 14px;
    padding: 8px 20px;
    color: #6e6c6c;
  }
}
.sum-item{
  padding-top: 15px;
  span{
    font-size: 14px;
    font-weight: 600;
    color: #4e4e4e;
  }
}
::v-deep .tg-span{
  p:first-child{
    display: inline;
  }
}
</style>
