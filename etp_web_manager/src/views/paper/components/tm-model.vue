<template>
  <div>
    <el-card  class="card" shadow="always" v-for="(item,index) in paperUnit" :id="`visit_${index}`">
      <div class="unit-card">
        <el-row :gutter="20" class="top-area">
          <el-col :span="1" style="width: 54px;"><div class="type-tip">{{ typeFormat(item.quType) }}</div></el-col>
          <el-col :span="4"><el-input v-model="item.display"></el-input></el-col>
          <el-col :span="2">
            <el-button v-show="item.quType!=6" @click="addUnit(item.quType,index)"><i class="el-icon-plus"></i> 新增题目 / 规则</el-button>
            <el-button v-show="item.quType==6"@click="addUnit(item.quType,index)"><i class="el-icon-plus"></i> 新增题目</el-button>
          </el-col>

          <span> <el-checkbox v-model="item.quOrderFlag" true-label="1" false-label="0">试题乱序</el-checkbox></span>
          <span v-show="item.quType!=3&&item.quType!=4&&item.quType!=5"><el-checkbox v-model="item.answerOrderFlag" true-label="1" false-label="0">选项乱序</el-checkbox></span>
        </el-row>
        <el-row :gutter="20" class="show-area">
            <!--          展示题目-->
          <el-table
            :id="`tmxx-table${index}`"
            :data="item.unitRule"
            :ref="`table${index}`"
            :show-header="false"
            :default-expand-all="true"
            @cell-mouse-enter="handleMouseEnter(index)"
          >
            <el-table-column  align="center" type="expand" v-if="item.quType==6">
              <template slot-scope="props">
                <div class="xt-area" v-for="(child,i) in props.row.children">
                  <div class="xt-item">
                    <div>{{i+1}} </div>
                    <div>【{{typeFormat(child.type)}}】 </div>
                    <div> {{transText(child.content)}}</div>
                    <div> 本题  <el-input size="mini" style="width: 80px"
                                        :placeholder="item.eachScore"
                                        v-model="child.score" @input="validateScore(child,item)"></el-input> 分</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column  label="序号" width="55" align="center" >
              <template slot-scope="scope">
                <div class="xh">
                  <span>{{scope.$index +1}}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="题目内容" align="left" prop="content"  :show-overflow-tooltip="true" >
              <template slot-scope="scope">
                <span v-if="scope.row.strategy !=3">
                   <svg-icon icon-class="tm"></svg-icon>
                  {{transText(scope.row.content)}}
                </span>
                <span v-if="scope.row.strategy ==3">
                    <span>
                      <svg-icon icon-class="rule"></svg-icon>
                      <span style="font-weight: 600">【{{scope.row.rule.catDisplay}}】：</span>
                      <span v-for="rule in scope.row.rule.lc">
                        {{levelFormat(rule.level)}} ({{rule.count}}个) ；
                      </span>
                    </span>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="难度等级" align="center" prop="level"  width="120">
              <template slot-scope="scope">
                <el-tag size="small" v-show="scope.row.level!=null">{{levelFormat(scope.row.level)}}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="分数设置" align="left" prop="content" width="190px">
              <template slot-scope="scope" v-if="scope.row.type!=6">
                <span  v-if="scope.row.strategy!=3">本题 </span>
                <span  v-else>每题</span>
                 <el-input size="mini" style="width: 60px;font-size: 15px"
                            :placeholder="item.eachScore"
                            v-model="scope.row.score"
                           @blur="disBlur(scope.row)"
                           @input="validateScore(scope.row,item)"></el-input>
                 分
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="130">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="预览" placement="top-start" v-if="scope.row.strategy!=3">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-view"
                    @click="handleView(scope.row)"
                  ></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="编辑" placement="top-start" v-if="scope.row.strategy==3">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleEdit(scope.row,scope.$index,index)"
                  ></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    style="color: red"
                    @click="handleDelete(item,scope.$index)"
                  ></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="下移" placement="top-start">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-bottom"
                    @click="moveDown(item.unitRule,scope.$index)"
                  ></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="上移" placement="top-start">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-top"
                    @click="moveUpward(item.unitRule,scope.$index)"
                  ></el-button>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" content="拖拽" placement="top-start">
                  <el-button
                    size="mini"
                    type="text"
                    v-show="item.quType!=6"
                    style="color: #1c1c1c"
                    icon="el-icon-s-grid"
                    class="move"
                    @click=""
                  ></el-button>
                </el-tooltip>
              </template>

            </el-table-column>
          </el-table>

        </el-row>
      </div>
    </el-card>

    <el-dialog
      class="xt-dialog"
      :title="title"
      :visible.sync="open"
      :close-on-click-modal="false"
      width="1000px">
      <el-form  label-width="80px">
        <el-row>
          <el-col :span="14">
            <el-form-item label="组卷策略:" prop="strategy">
              <el-radio-group v-model="strategy">
                <el-radio :label="item.dictValue" v-for="item in zjclOptions" v-if="!(tm_type==6&&item.dictValue==3)">{{ item.dictLabel }}</el-radio>
              </el-radio-group>

              <el-tooltip class="item" effect="dark" content="抽题 / 随机组卷，选择题库之后显示各难度等级题目数量" placement="top-start">
                <i class="el-icon-warning" style="color: #fa9443;margin-left: 10px"></i>
              </el-tooltip>

            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="题目类型:" prop="tm_type">
              {{typeFormat(tm_type)}}
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="选择题库:" prop="categoryIds" >
          <el-cascader
            ref="cascader"
            style="width: 100%;"
            :options="categoryOptions"
            v-model="categoryIds"
            :props="{ checkStrictly: true ,value:'id'}"
            @change="changeTk()"
            clearable>
          </el-cascader>
        </el-form-item>
        <div v-show="strategy!=1 && categoryIds.length>0 &&factArray.length==0 " style="color: red">已选择的题库中暂无题目 ！</div>
<!--        =================选题组卷===================================================-->
       <div v-if="strategy==1">
         <tm-list
           ref="tmQuery"
           :type="tm_type"
           :strategy="strategy"
           :level-options="levelOptions"
           :type-options="typeOptions"
           :category-id="cateIdsTrans"
           @sure="sureSelTm"
           v-if="paperUnit!=null && unitIndex!=null"
           :xz_data="[]"
           >
         </tm-list>
       </div>
<!--        =================选题组卷 end================================================-->
        <div class="lx-item" v-if="strategy!=1">
          <el-row  v-for="(lx,k) in factArray">
            <el-col :span="6" style="padding-left: 100px;font-weight: 600">{{levelFormat(lx.level)}}</el-col>
            <el-col :span="10">抽取 &nbsp;<el-input-number size="mini" :min="0" :max="lx.count" v-model="rule.lc[k].count"></el-input-number> &nbsp;题 / 共 {{lx.count}} 题</el-col>
          </el-row>
        </div>
<!--      =================随机组卷 end================================================-->
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="strategy!=1">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="saveRule">确 定</el-button>
      </span>
    </el-dialog>

<!--    题目详情弹窗-->
    <el-dialog
      title=""
      :visible.sync="dialogVisible"
      width="60%">
      <div class="base-div" v-if="questionDetial!=null">
        <el-row>
          <el-col :span="6">
            题目类型：{{typeFormat(questionDetial.type)}}
          </el-col>
          <el-col :span="6">
            难易程度：{{levelFormat(questionDetial.level)}}
          </el-col>
          <el-col :span="6">
            更新时间：{{questionDetial.updateTime}}
          </el-col>
        </el-row>
      </div>
      <el-divider></el-divider>
      <div>
        <el-card style="margin-bottom: 20px;">
          <qusetion-view
            :data="questionDetial">
          </qusetion-view>
        </el-card>
        <el-card class="box-card" v-if="questionDetial!=null && questionDetial.children!=null&&questionDetial.children.length!=0">
          <div class="child-tm" v-for="(item,index) in questionDetial.children">
            <qusetion-view
              :bqShow="false"
              :xh="index+1"
              :child="true"
              :data="item"></qusetion-view>
          </div>
        </el-card>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>


<!--    编辑规则弹窗-->
    <el-dialog
      class="xt-dialog"
      title="编辑规则"
      :visible.sync="openEdit"
      :close-on-click-modal="false"
      width="1000px">
      <el-form  label-width="80px">
        <el-row>
          <el-col :span="14">
            <el-form-item label="组卷策略:" prop="strategy">
              随机组卷
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="题目类型:" prop="tm_type">
              {{typeFormat(tm_type)}}
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="选择题库:" prop="categoryIds" >
          <el-cascader
            ref="cascader"
            style="width: 100%;"
            :options="categoryOptions"
            v-model="ruleEdit.rule.catId"
            :props="{ checkStrictly: true ,value:'id'}"
            @change="changeTk()"
            clearable>
          </el-cascader>
        </el-form-item>

        <div class="lx-item">
          <el-row  v-for="(lx,k) in e_factArray">
            <el-col :span="6" style="padding-left: 100px;font-weight: 600">{{levelFormat(lx.level)}}</el-col>
            <el-col :span="10">抽取 &nbsp;<el-input-number size="mini" :min="0" :max="lx.count" v-model="ruleEdit.rule.lc[k].count"></el-input-number> &nbsp;题 / 共 {{lx.count}} 题</el-col>
          </el-row>
        </div>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="openEdit = false">取 消</el-button>
        <el-button type="primary" @click="sureEditRule">确 定</el-button>
      </span>
    </el-dialog>
  </div>


</template>

<script>
import {getTreeselect} from "@/api/category/category";
import tmList from "@/views/paper/components/tmList";
import Template from "@/views/etp/certificateTemplate";
import qusetionView from "@/views/components/question/questionView"
import {getQuestion,questionFact,questionRandom} from "@/api/question/question";
import Sortable from 'sortablejs'
import {updateStatus} from "@/api/paper/paper";


export default {
  name: "tm",
  components:{Template, tmList,qusetionView,Sortable},
  props:{
    unit:{
      default:[]
    }
  },
  data(){
    return{
      open: false,
      title: "新增",
      unitIndex:0,//第几个单元
      zjclOptions: [],
      categoryOptions: [],
      typeOptions: [],
      levelOptions: [],

      strategy: "1", //组卷策略
      categoryIds:[],//题库
      categoryLabel:"",//题库label
      cateIdsTrans:[],//选中的分类数据处理，只要最后一级

      tm_type :"1",
      questionDetial:{},
      dialogVisible:false,
      paperUnit:[],

      factArray:[],
      rule:{
        catId:[],
        lc:[]
      },

      //编辑规则弹窗===相关-======
      openEdit: false,
      ruleEdit:{
        index:0,//标记哪一条数据
        strategy:3,
        rule:{
          catId:[],
          catDisplay:"",
          lc:[]
        },
      },
      e_factArray:[],

      //展开行
      expands:[],
      xz_data:[]
    }
  },
  watch:{
    categoryIds:{
      handler(val){
        // this.cateIdsTrans =[]
        // val.forEach(item =>{
        //   this.cateIdsTrans.push(item[item.length-1])
        // })
        if(val.length>0){
          this.cateIdsTrans = val[val.length-1]
          if(this.strategy!=1){//不是选题组卷
            this.getQuestionFact()
          }
        }

      },
      deep:true
    },
    unit:{
      handler(val){
        this.paperUnit = val
      },
      deep:true
    },
    //组卷策略修改
    strategy:{
      handler(val){
        this.categoryIds =[]
        this.cateIdsTrans = null
        this.resetRule()
      },
      deep:true
    }
  },

  created() {
    this.paperUnit = this.unit
    this.getDicts("paper_type").then(response => {
      this.zjclOptions = response.data;
    });
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("question_level").then(response => {
      this.levelOptions = response.data;
    });
    getTreeselect().then(res => {
      this.categoryOptions = res.data
    })

  },
  methods:{
    // 题目类型字典翻译
    typeFormat(type) {
      return this.selectDictLabel(this.typeOptions, type);
    },
    // 难易程度：
    levelFormat(level, column) {
      return this.selectDictLabel(this.levelOptions, level);
    },
    addUnit(tm_type,index){
      this.resetParam()
      this.tm_type = tm_type
      this.unitIndex = index

      //题目弹窗 回显传值，去掉 规则数据，否则统计选中数据不正确
      this.xz_data=[]
      let quids =[]
      this.paperUnit[index].unitRule.forEach(item=>{
        if(item.quId!=undefined){
          this.xz_data.push(item)
          quids.push(item.quId)
        }
      })
      this.title = "新增"
      this.open = true
      //调用查询方法
      this.$nextTick(_ =>{
        this.$refs.tmQuery.queryParams.type = tm_type
        this.$refs.tmQuery.queryParams.quIds = quids
        this.$refs.tmQuery.handleQuery()
        this.dropRow()
      })
    },

    //编辑规则
    handleEdit(row,index,unit){
      this.resetRuleEdit()
      this.unitIndex = unit
      row = JSON.parse(JSON.stringify(row))
      this.ruleEdit.strategy =row.strategy
      let param = {
        categoryId: row.rule.catId[row.rule.catId.length-1],
        type:this.tm_type
      }
      questionFact(param).then(res =>{
        this.e_factArray = res.data
      })
      this.ruleEdit.rule  =row.rule
      this.ruleEdit.index = index
      this.openEdit = true
    },
    sureEditRule(){
      let rule = this.ruleEdit
      this.paperUnit[this.unitIndex].unitRule.splice(this.ruleEdit.index,1)
      this.paperUnit[this.unitIndex].unitRule.splice(this.ruleEdit.index,0,rule)
      this.openEdit = false
      //统计个数
      this.$emit("changeScore",this.paperUnit[this.unitIndex])
    },
    sureSelTm(value){
      let oldArr = this.paperUnit[this.unitIndex]["unitRule"]
      let allArr = [...oldArr,...value]
      //原来的选中题目 与新题目比较，新的取消选中的去掉，
      //主要 规则不要删掉
      // let newArr = this.unique(allArr,value)

      this.paperUnit[this.unitIndex]["unitRule"] =allArr

      this.open = false
      this.$emit("changeScore",this.paperUnit[this.unitIndex])
      this.$emit("sumCountByLevel",this.paperUnit[this.unitIndex])
    },

    unique(allArr,newArr){
      let arr =[]
      allArr.forEach(item=>{
        if(item.rule!=null&&item.rule!=undefined){
          //是规则
          arr.push(item)
        }else{
          let ret1 = newArr.find(data=>data.quId==item.quId)
          if(ret1 != undefined){
            //判断 arr 有没有这条数据
            let ret2 = arr.find(data=>data.quId ==item.quId)
            if(ret2 == undefined){
              arr.push(item)
            }
          }
        }
      })
      return arr;
    },
    resetParam(){
      this.strategy = '1'
    },
    transText(analy){
      return this.getText(analy)
    },
    handleView(row){
      const id = row.quId
      getQuestion(id).then(res => {
        this.questionDetial = res.data
      })
      this.dialogVisible = true
    },
    disBlur(row){
      let reg =/\.$/
      if(reg.test(row.score)){
        row.score = row.score+0
      }
    },
    validateScore(row,item){
      let num = row.score
        .replace(/[^\d.]/g, "") //将非数字和点以外的字符替换成空
        .replace(/^\./g, "") //验证第一个字符是数字而不是点
        .replace(/\.{2,}/g, ".") //出现多个点时只保留第一个
        .replace(".", "$#$") // 1、将数字的点替换成复杂字符$#$
        .replace(/\./g, "") // 2、将字符串的点直接清掉
        .replace("$#$", ".") // 3、将复杂字符再转换回点
        .replace(/^(\-)*(\d+)\.(\d).*$/, "$1$2.$3"); //只能输入1个小数
      row.score = num
      this.$emit("changeScore",item)
    },
    //题目删除
    handleDelete(item,index){
      this.$confirm('是否确认删除题目', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        item.unitRule.splice(index,1)
        this.$emit("changeScore",item)
      }).then(response => {

      }).catch(() => {});

    },
    // 行拖拽
    dropRow () {
      let tbody = document.querySelector(`#tmxx-table${this.unitIndex} .el-table__body-wrapper tbody`)
      let _this = this
      Sortable.create(tbody, {
        handle: '.move',
        // 元素未被选中的时候（从选中到未选中）
        // onMove(evt) {
        //   // same properties as onEnd
        //   debugger
        //   return false
        // },
        onChoose: function (/**Event*/evt) {
          // _this.closeRowExpansionAll()
        },
        onEnd (evt) { // 结束拖拽
          let {newIndex, oldIndex} = evt
          let list = _this.paperUnit[_this.unitIndex].unitRule
          let currRow = list.splice(oldIndex, 1)[0]
          list.splice(newIndex, 0, currRow)
          let newList = list.splice(0)
          _this.$nextTick(_ =>{
            _this.paperUnit[_this.unitIndex].unitRule = newList
          })
        }
      })
    },

    //获取题目难度列表
    getQuestionFact(){
      this.resetRule()
      let param = {
        categoryId: this.cateIdsTrans,
        type:this.tm_type,
        examFlag:'1'
      }
      questionFact(param).then(res =>{
        this.factArray = res.data
        this.rule.catId = this.categoryIds
        this.rule.catDisplay = this.categoryLabel
        this.factArray.forEach(item=>{
          this.rule.lc.push( {level:item.level,count:0})
        })
      })
    },
    //确定 --保存规则
    saveRule(){
      if(this.rule.lc.length==0){
        this.$message.warning("未选择题目！")
        return;
      }else{
        let count = 0
        this.rule.lc.forEach(c => {
          count += c.count
        })
        if(count==0){
          this.$message.warning("未选择题目！")
          return;
        }
      }
      //判断一下是抽题还是随机
      if(this.strategy ==3){ //随机组卷  添加规则
        this.paperUnit[this.unitIndex].unitRule.push({strategy:3,rule:this.rule})
        this.open = false
        //计算题目数量
        this.$emit("changeScore",this.paperUnit[this.unitIndex])
        this.$emit("sumCountByLevel",this.paperUnit[this.unitIndex])
      }else{
        //获取已选择题目id,传到后台排除，防止重复
        let quIds =[]
        this.paperUnit[this.unitIndex].unitRule.forEach(qu=>{
          quIds.push(qu.quId)
        })
        //抽题组卷 请求接口 获取题目
        let lc = this.rule.lc
        lc.forEach(tx =>{
          if(tx.count>0){
            let param = {
              categoryId : this.cateIdsTrans,
              type: this.tm_type,
              level: tx.level,
              n: tx.count,
              quIds:quIds,
              examFlag:'1'
            }
            questionRandom(param).then(res =>{

              let newarr = [];
              res.data.map((item,index)=>{
                newarr.push(Object.assign(item,{strategy:2}))
              })
              let old =  this.paperUnit[this.unitIndex].unitRule
              //添加题目
              this.paperUnit[this.unitIndex].unitRule =[...old,...newarr]
              this.open = false
              //计算题目数量
              this.$emit("changeScore",this.paperUnit[this.unitIndex])
              this.$emit("sumCountByLevel",this.paperUnit[this.unitIndex])
            })

          }
        })
      }

    },
    resetRule() {
      this.factArray =[]
      this.rule={
        catId:null,
        lc:[]
      }
    },
    resetRuleEdit(){
      this.ruleEdit={
          index:0,//标记哪一条数据
          strategy:3,
          rule:{
          catId:[],
            catDisplay:"",
            lc:[]
        },
      }
    },
    changeTk(){
      let label = this.$refs["cascader"].getCheckedNodes()[0].pathLabels
      this.categoryLabel = label.join("/")
    },
    handleMouseEnter(index){
      this.unitIndex = index
    },
    //回显 行拖拽
    backDropRow(unitIndex){
      this.unitIndex = unitIndex
      this.dropRow()
    },
    // //关闭所有行
    // closeRowExpansionAll() {
    //   this.paperUnit[this.unitIndex].unitRule.forEach((item) => {
    //     this.$refs[`table${this.unitIndex}`][0].toggleRowExpansion(item,false);
    //     if (item.children !== undefined && item.children !== null) {
    //       this.closeRowExpansionAll(item.children);
    //     }
    //   });
    // },
    /** 上移*/
    moveUpward(data,  index) {
      if (index > 0) {
        let upData = data[index - 1];
        data.splice(index - 1, 1);
        data.splice(index, 0, upData);
      } else {
        this.$message({
          message: '已经是第一条，不能上移',
          type: 'warning'
        });
      }
    },
    /** 下移*/
    moveDown(data, index) {
      if ((index + 1) == data.length) {
        this.$message({
          message: '已经是最后一条，不能下移',
          type: 'warning'
        });
      } else {
        let downData = data[index + 1];
        data.splice(index + 1, 1);
        data.splice(index, 0, downData);
      }
    },
  }
}
</script>

<style scoped lang="scss">
.card{
  ::v-deep .el-card__body{
    padding:0px;
  }
  .top-area{
    padding: 15px;
    background: #f1f1f1;
    position: relative;
    span{
      position: absolute;
      line-height: 36px;
      right: 30px;
    }
    span:nth-of-type(1){
      right: 134px!important;
    }
  }
  .show-area{
    padding: 15px;
  }
}

.card:not(:first-of-type){
  margin-top: 20px;
}
.xt-dialog{
  ::v-deep .el-form-item{
    margin-bottom: 5px;
  }
}
::v-deep .el-dialog__body{
  padding-top: 10px;
}
.xt-area{
  font-size: 14px;
  padding-left: 30px;
  .xt-item{
    display: flex;
    line-height: 40px;
    align-items: center;

    div:first-child{
      height: 20px;
      width: 20px;
      background: url("../../../assets/images/fang20.png") 100% 100% no-repeat;
      color: #fff;
      margin-right: 10px;
      text-align: center;
      line-height: 20px;
    }
    div:nth-of-type(4){
      margin-left: auto;
    }
  }
}
.xh{
  height: 30px;
  width: 30px;
  background: url("../../../assets/images/tag.png") 100% 100% no-repeat;
  color: #fff;
}
::v-deep .child-tm{
  margin-top: 50px;
  &:first-child{
    margin-top: 20px!important;
  }
}
.type-tip{
  height: 36px;
  line-height: 36px;
  font-size: 12px;
  border-radius: 4px;
  padding:0 4px;
  background-color: #f4f4f5;
  color: #909399;
  width: 50px;
}
.lx-item{
  margin-top: 30px;
  .el-row{
    padding: 10px 0;
  }
}
.svg-icon{
  color: #e35725;
  margin-right: 10px;
  font-size: 16px;
}
</style>
