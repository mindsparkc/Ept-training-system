<template>
  <div class="app-container">
    <el-form ref="questionForm" :model="question" :rules="rules" label-width="80px">
      <el-card>
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="题目类型" prop="type">
              <el-select
                v-model="question.typeBy"
                placeholder="请选择题目类型"
                :disabled="question.quId!=null"
                @change="changeType"
                :style="{width: '100%'}"
              >
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="难度等级" prop="level">
              <el-select v-model="question.level" placeholder="请选择难度等级" :style="{width: '100%'}">
                <el-option
                  v-for="dict in levelOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="应用范围">
              <el-checkbox v-model="question.tarinFlag" >练习</el-checkbox>
              <el-checkbox v-model="question.examFlag" >考试</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="题目分类" prop="categoryIds" class="cate-select">
              <el-cascader
                :options="categoryOptions"
                v-model="question.categoryIds"
                :props="{ multiple: true, checkStrictly: true ,value:'id'}"
                clearable></el-cascader>
<!--              <div v-for="(item,index) in question.categoryIds" style="margin-right: 4px;">-->
<!--                <el-cascader-->
<!--                  v-model="question.categoryIds[index]"-->
<!--                  :options="categoryOptions"-->
<!--                  :props="{ checkStrictly: true,value:'id' }"-->
<!--                  @change="handleChange($event,index)"-->
<!--                >-->
<!--                </el-cascader>-->
<!--                <i class="el-icon-close" v-if="index!=0" @click="delete_cate(index)"></i>-->
<!--              </div>-->

<!--              <div><i class="el-icon-info"></i>最多可选择3个分类</div>-->
            </el-form-item>
<!--            <el-button class="add-btn" @click="add_cate()" type="primary" plain>+ 新增分类</el-button>-->
          </el-col>
        </el-row>
        <Tm :question="question"
            @addXx="add_xx(0)"
            @delXx="del_xx($event,0)"
            @addTk="add_tk(0)"
            @delTk="del_tk($event,0)"
            :edit_index="''"
            ref="main_tm"></Tm>
      </el-card>

      <!-- 6.组合题目 —— 子题目编辑区域 -->
      <template v-if="this.question.type === '6'" >
        <el-divider content-position="center"/>
        <div class="sub-title">子题目列表</div>
        <div>
          <!--子题目列表 -->
          <div class="qu_left" >
            <el-card v-for="(child, index) in question.children"
                     style="margin-bottom: 5px" :id="`visit_${index}`">
              <el-row :gutter="15" >
                <el-col :span="8">
                  <el-form-item label="题目类型" prop="type">
                    <el-select
                      v-model="child.type"
                      @change="changeChildType($event,index)"
                      :disabled="question.children[index].quId!=null" placeholder="请选择题目类型">
                      <el-option
                        v-for="dict in subTypeOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <Tm
                :ref="`child${index}`"
                :question="child"
                :edit_index="index+1"
                @addXx="add_xx(1,index)"
                @delXx="del_xx($event,1,index)"
                @addTk="add_tk(1,index)"
                @delTk="del_tk($event,1,index)"
              ></Tm>
            </el-card>
          </div>

          <!-- 子题目导航 -->
          <el-card :class="['qu_right',{'sub-tm':isScroll}]">
            <el-button type="primary" style="width: 100%" icon="el-icon-plus" size="mini"
                       @click="handleAddQuestionChild">添加子题目</el-button>
            <el-table
              class="childTable"
              :data="question.children"
              :show-header="false"
              :row-class-name="tableRowClassName"
              row-key="quId"
              @row-click="handleClick"
              style="width: 100%; max-height: 350px;"
            >
              <el-table-column type="index" width="20" />
              <el-table-column label="题干内容" prop="content" width="200" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <span style="font-style: normal">[{{typeFormat(scope.row)}}]</span>
                  {{scope.row.content.replace(/\<[^>]*\>(([^<])*)/g, function() {return arguments[1];})}}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="65px">
                <template slot-scope="scope">
                  <el-tooltip class="item" effect="dark" content="删除" placement="top">
                    <el-button
                      type="text"
                      @click.native.stop="handleDeleteQuestionSub(scope.$index,scope.row)"
                      v-hasPermi="['etp:sign:remove']"
                    ><i class="el-icon-delete" style="background: white; color: red"></i></el-button>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" content="拖拽" placement="top">
                    <el-button
                      type="text"
                    ><i class="el-icon-s-grid" style="color: #4c4c4c"></i></el-button>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </template>
    </el-form>
    <div class="footer-btn">
      <el-button type="primary" @click="submitForm" >保存</el-button>
      <el-button @click="cancel" >关闭</el-button>
    </div>
  </div>

</template>
<script>
import { getTreeselect } from "@/api/category/category";
import Tm  from "./tm";
import {addQuestion, updateQuestion, getQuestion} from "@/api/question/question";
import Sortable from 'sortablejs'

export default {
  name: "editquestion",
  inheritAttrs: false,
  components: {Tm,Sortable},
  data() {
    return {
      // 题目类型字典
      typeOptions: [],
      // 难度等级字典
      levelOptions: [],
      // 子题目类型字典
      subTypeOptions: [],
      // 分类列表
      categoryOptions: [],

      // 表单校验
      rules: {
        // content: [
        //   { required: true, message: "题干内容不能为空", trigger: "blur" }
        // ],
        typeBy: [
          { required: true, message: "题目类型不能为空", trigger: "change" }
        ],
        level: [
          { required: true, message: "难度等级不能为空", trigger: "change" }
        ],
        categoryIds: [
          { required: true, message: "题目分类不能为空", trigger: "change", type: "array" }
        ]
      },
      question: {
        type: "1",
        typeBy: "1", //备用 用来记录变化
        level: "1",
        content: "",
        tarinFlag: true,
        examFlag: true,
        categoryIds: [],
        children: [],
        answers:[
          {content:'',rightFlag:false,analysis:"",sort:1},
          {content:'',rightFlag:false,analysis:"",sort:2},
          {content:'',rightFlag:false,analysis:"",sort:3},
          {content:'',rightFlag:false,analysis:"",sort:4},
        ],
        analysis:null,//解析
      },

      letterArr:[],
      isScroll: false,//滚动条 固定
      scrolltop:0,
      quId: null,
      childFlag:true,
      allLoading:null,
    }
  },

  created() {
    this.allLoading = this.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("question_sub_type").then(response => {
      this.subTypeOptions = response.data;
    });
    this.getDicts("question_level").then(response => {
      this.levelOptions = response.data;
    });
    getTreeselect().then(res => {
      this.categoryOptions = res.data
    })
    this.quId = this.$route.query.quId
    let catIds = this.$route.query.catIds
    if(typeof catIds ==='string'){
      catIds = [catIds]
    }
    if(undefined != catIds && catIds.length>0){
      this.question.categoryIds = [catIds]
    }

    if(undefined != this.quId){
      this.getQuestionInfo()
    }else{
      this.allLoading.close()
    }
    this.letterArr="ABCDEFGHIJKLMNOPQRSTUVWXYZ".split('');
  },
  mounted() {
    window.addEventListener("scroll", this.scrollHandle); //绑定页面滚动事件
    this.$refs['main_tm'].initSubEdit();
  },
  methods: {
    scrollHandle(e) {
      this.scrolltop = e.srcElement.scrollingElement.scrollTop; // 获取页面滚动高度
      if ( this.scrolltop > 700) {
        this.isScroll = true;
      }else{
        this.isScroll = false;
      }
    },
    // 题目类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    changeType(value){
      let oldType = this.question.type
      if(value ==1||value==2){
        if(oldType ==1||oldType==2){
          this.question.answers.forEach(item=>{
            item.rightFlag = false
          })

        }else{
          this.question.answers = [
            {content:'',rightFlag:false,analysis:"",sort:1},
            {content:'',rightFlag:false,analysis:"",sort:2},
            {content:'',rightFlag:false,analysis:"",sort:3},
            {content:'',rightFlag:false,analysis:"",sort:4},
          ]
        }

      }else if(value ==3){
        this.question.answers = [
          {content:'正确',rightFlag:false,analysis:"",sort:1},
          {content:'错误',rightFlag:false,analysis:"",sort:2},
        ]
      }else if(value ==4){
        this.question.answers =[ {contents:[],sort:1}]
      }else if(value==6){
        this.question.answers = []
        this.$nextTick(()=>{
          //元素加载完成后执行的代码
          this.dropRow()
        });
      }else{
        this.question.answers = []
      }
      this.question.type = this.question.typeBy
    },
    changeChildType(value,index){
      if(value ==1||value==2){
        this.question.children[index].answers = [
          {content:'',rightFlag:false,analysis:"",sort:1},
          {content:'',rightFlag:false,analysis:"",sort:2},
          {content:'',rightFlag:false,analysis:"",sort:3},
          {content:'',rightFlag:false,analysis:"",sort:4},
        ]
      }else if(value ==3){
        this.question.children[index].answers = [
          {content:'正确',rightFlag:false,analysis:"",sort:1},
          {content:'错误',rightFlag:false,analysis:"",sort:2},
        ]
      }else if(value ==4){
        this.question.children[index].answers = [{contents:[],sort:1}]

      }else if(value==6){
        this.question.children[index].answers = []
        this.$nextTick(()=>{
          //元素加载完成后执行的代码
          this.dropRow()
        });
      }else{
        this.question.children[index].answers = []
      }
    },
    //题目回显
    getQuestionInfo(){
      getQuestion(this.quId).then(res => {
        this.question = res.data
        this.question.typeBy = this.question.type
        //题干信息回显
        this.$refs['main_tm'].editor['tg'].txt.html(this.question.content)
        this.$refs['main_tm'].editor['jx'].txt.html(this.question.analysis)
        this.allLoading.close()
        if(this.question.type==6){
          this.$nextTick(()=>{
            this.dropRow()
          });
        }
      }).catch(error=>{
        this.allLoading.close()
      })
    },
    /** 子题目库添加按钮操作 */
    handleAddQuestionChild() {
      let top = this.scrolltop
      let obj = {};
      obj.content = "";
      obj.analysis = null;
      obj.type='1';
      obj.editor={tg:null,jx:null};//富文本
      obj.answers = [
        {content:'',rightFlag:false,analysis:"",sort:1},
        {content:'',rightFlag:false,analysis:"",sort:2},
        {content:'',rightFlag:false,analysis:"",sort:3},
        {content:'',rightFlag:false,analysis:"",sort:4},
      ];
      if(null ==this.question.children){
        this.question.children=[]
      }
      this.question.children.push(obj);
      this.$nextTick(() => {
        window.scrollTo(0, top);  //滚动距离因为导航栏固定定位130px的高度
      });
    },
    /** 子题目库删除按钮操作 */
    handleDeleteQuestionSub(index,row) {
      this.$confirm('是否确认删除该子题目?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.question.children.splice(index, 1);
        //编辑器重新赋值
        this.question.children.forEach((item,i) =>{
          this.$refs[`child${i}`][0].editor[`tg${i+1}`].txt.html(item.content)
          this.$refs[`child${i}`][0].editor[`jx${i+1}`].txt.html(item.analysis)
        })
      }).catch(() => {});

    },
    /** 更改子题目 */
    handChange(index, child) {
      this.question.children[index] = child
    },

    //给el-table中的每个row对象里添加index属性
    tableRowClassName({ row, rowIndex }) {
      row.index = rowIndex;
    },
    //锚点
    handleClick(row, column, event){
      let index = row.index
      document.querySelector(`#visit_${index}`).scrollIntoView({
        behavior: "smooth"
      });
    },
    add_xx(flag,index){

      if(flag==1){//子题目
        let childLen = this.question.children[index].answers.length+1
        this.question.children[index].answers.push( {content:'',rightFlag:false,analysis:"",sort:childLen});
      }else{
        let len = 1
        if(this.question.answers==null){
          this.question.answers=[]
        }else{
          len = this.question.answers.length+1
        }
        this.question.answers.push({content:'',rightFlag:false,analysis:"",sort:len});
      }
    },

    //param 子参数，index子题目条数
    del_xx(param,flag,index){
      if(flag==1){
        this.question.children[index].answers.splice(param,1);
        this.question.children[index].answers.forEach(function (item, index) {
          item.sort = index+1
        })

      }else{
        this.question.answers.splice(param,1);
        this.question.answers.forEach( (item, index) =>{
          item.sort = index+1
        })
      }

    },

    add_tk(flag,index){
      if(flag==1){ //子题目
        if(this.question.children[index].answers==null||this.question.children[index].answers==undefined){
          this.question.children[index].answers = []
        }
        this.question.children[index].answers.push({contents:[]})
        this.$forceUpdate();
      }else{
        if(this.question.answers==null||this.question.answers==undefined){
          this.question.answers = []
        }
        this.question.answers.push({contents:[]})
        this.$forceUpdate();
      }

    },
    del_tk(el,flag,index){
      if(flag==1){
        this.question.children[index].answers.splice(el,1);
        this.$forceUpdate();
      }else{
        this.question.answers.splice(el,1);
        this.$forceUpdate();
      }

    },

    /** 提交按钮 */
    submitForm() {
      if(this.question.type == 6){//组合题
        let flag = this.validateChild()
        if(!flag){
          return;
        }
      }
      this.$refs["questionForm"].validate(valid => {
        if (valid) {
          this.$refs["main_tm"].$refs['form'].validate(valid => {
            if (valid) {
              if(this.question.type == 1||this.question.type == 2||this.question.type == 3){
                let res1 =  this.question.answers.filter(obj=>{
                  return obj.rightFlag
                })
                if(res1.length==0){
                  this.$message.error(`至少选中一个选项`)
                  return false;
                }
              }
              if (this.question.quId != null) {
                updateQuestion(this.question).then(response => {
                  this.msgSuccess("修改成功");
                  this.cancel()
                });
              } else {
                addQuestion(this.question).then(response => {
                  this.msgSuccess("新增成功");
                  this.cancel()
                });
              }
            }
          });

        }
      });

    },

    //校验子题目
    validateChild(){
      let data = this.question.children;
      if(data==null||data.length==0){
        this.$message.error("请至少添加一个子题目")
        return false;
      }
      for(let i in data){
        let item = data[i]
        if(item.content==null||item.content==""){
          this.$message.error("子题目"+(Number(i)+1)+",题目内容不能为空")
          return false;
        }
        let tg = this.getText(item.content)
        if(item.type==1||item.type==2||item.type == 3){
          //单选题||复选题||填空题
          let res =  item.answers.filter(obj=>{
            return obj.content==null||obj.content==""
           })
          if(res.length>0){
            this.$message.error(`子题目${Number(i)+1}【${tg}】，选项内容不能为空`)
            return false;
          }
          let res1 =  item.answers.filter(obj=>{
            return obj.rightFlag
          })
          if(res1.length==0){
            this.$message.error(`子题目${Number(i)+1}【${tg}】，至少选中一个选项`)
            return false;
          }
        }else if(item.type == 4){
          //填空题
          let res =  item.answers.filter((tk)=>{
            return tk.contents==null||tk.contents.length==0
          })
          if(res.length>0){
            this.$message.error(`子题目${Number(i)+1}【${tg}】，答案不能为空`)
            return false;
          }
        }
      }
      return true
    },
    cancel() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1);
    },
    // getText(content){
    //   return content.replace(/\<[^>]*\>(([^<])*)/g, function() {return arguments[1];})
    // },
    //-=====================子题目列表拖拽============================================
    // 行拖拽
    dropRow () {
      let tbody = document.querySelector('.childTable .el-table__body-wrapper tbody')
      let _this = this
      Sortable.create(tbody, {
        onEnd ({ newIndex, oldIndex }) { // 结束拖拽
          let list = _this.question.children
          let currRow = list.splice(oldIndex, 1)[0]
          list.splice(newIndex, 0, currRow)
          let newList = list.splice(0)
          //编辑器重新赋值
          _this.question.children.forEach((item,i) =>{
            _this.$refs[`child${i}`][0].editor[`tg${i+1}`].txt.html(item.content)
            _this.$refs[`child${i}`][0].editor[`jx${i+1}`].txt.html(item.analysis)
          })
          _this.$nextTick(_ =>{
            _this.question.children = newList
          })

        }
      })
    },
  }
}

</script>
<style lang="scss" scoped>
.qu_left{
  width: calc(100% - 370px);
  margin-bottom: 30px;
  padding-right: 20px;
  float: left;
}

.qu_right{
  width: 350px;
  float: right;
}

.el-table .cell {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap !important;
}
.sub-title{
  margin: 0 0 20px;
  font-weight: 600;
}
.sub-tm {
  top: 20px;
  z-index: 999;
  right: 20px;
  width: 350px;
  position: fixed;
  overflow-y: auto;
  max-height: calc(100vh - 40px);
}
.footer-btn{
  width: 160px;
  margin: 10px auto;
}
.cate-select {
  .el-cascader{
    width: 100%;
  }
}
.el-icon-close{
  color: red;
  margin-right: 10px;
  cursor: pointer;
}
.add-btn{
  margin: -10px 0px 22px 80px;
}

</style>
