<template>
<div class="table-area">
  <el-form :model="queryParams" label-width="80px">
    <el-form-item label="难易程度:" prop="level" >
      <el-checkbox-group v-model="queryParams.level">
        <el-checkbox :label="item.dictValue" v-for="item in levelOptions">{{item.dictLabel}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="题目内容" prop="content" style="width: 380px">
      <el-input v-model="queryParams.content" placeholder="请输入题目内容" clearable size="small" >
      </el-input>
    </el-form-item>
    <el-button type="primary" @click="sureSel">确认 <i>{{datas.length}}</i> 项</el-button>
  </el-form>
  <el-table v-loading="loading"
            ref="multipleTable"
            :data="questionList"
            @select-all="selectAll"
            @select="handleSelect">
    <el-table-column type="selection" width="55" align="center" />
    <el-table-column label="题目内容" align="left" prop="display"  :show-overflow-tooltip="true" />
    <el-table-column label="题目类型" align="center" prop="type" :formatter="typeFormat" />
    <el-table-column label="难度等级" align="center" prop="level" :formatter="levelFormat" width="120"/>
  </el-table>

  <pagination v-show="total>0"
              :total="total"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
  />
</div>
</template>

<script>
import {listQuestion} from "@/api/question/question";

export default {
  name: "tmList",
  props:{
    typeOptions: {},
    levelOptions: {},
    type:{},
    strategy:{},
    categoryId:{
      default:[]
    },
    xz_data:{ //选中的数据
      default: []
    }
  },
  data(){
    return{
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      datas: [],// 选中数组
      // 总条数
      total: 0,
      // // 题目库表格数据
      questionList: [],
      queryParams:{
        pageNum: 1,
        pageSize: 10,
        type: null,
        categoryId: null,//题库
        // examFlag:'1',
        level:[],//难易程度
        content: "",
        quIds:null
      }

    }
  },
  created() {
    // this.handleQuery()
    this.queryParams.type = this.type
    this.queryParams.categoryId = this.categoryId

  },
  watch:{

    categoryId:{
      handler(val){
        this.queryParams.categoryId = val
      },
      deep:true
    },
    queryParams:{
      handler(val){
        this.datas = this.xz_data
        this.queryParams = val
        this.getList()
      },
      deep:true
    }
  },

  methods:{
    // 题目类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 难度等级字典翻译
    levelFormat(row, column) {
      return this.selectDictLabel(this.levelOptions, row.level);
    },
    /** 查询题目库列表 */
    async getList() {
      this.loading = true;
      await listQuestion(this.queryParams).then(response => {
        this.questionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      await this.toggleSelection(this.datas)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handleSelect(list,item){
      if(list.includes(item)){
        this.datas.push( {content:item.content,quId:item.quId,version:item.version,strategy:this.strategy,type:item.type,children:item.children,level:item.level})
      }else{
        this.datas = this.datas.filter((data) => {
          if(data.quId != item.quId){
            return data
          }
        });
      }
    },
    selectAll(selection){
      let data = this.questionList
      if(selection.length>0){
        //全选
        data.forEach(item =>{
          //查看选中的data中是否有这条数据
          let result = this.datas.find(xz => xz.quId == item.quId)
          if(result == undefined){
            this.datas.push( {content:item.content,quId:item.quId,version:item.version,strategy:this.strategy,type:item.type,children:item.children,level:item.level})
          }
        })
      }else{
        //取消全选
        data.forEach(item=>{
          let index = this.datas.findIndex(xz => xz.quId == item.quId)
          if(index>-1){
            this.datas.splice(index,1)
          }
        })
      }

    },
    // 多选框选中数据
    // handleSelectionChange(selection) {
    //   // this.ids = selection.map(item => item.quId)
    //   this.datas = selection.map(item=>{
    //     return {content:item.content,quId:item.quId,version:item.version,strategy:this.strategy,type:item.type,children:item.children}
    //   })
    // },
    //设置选中
    toggleSelection(rows) {
      let data_list =this.questionList
      if (rows) {
        this.$nextTick(()=>{
          rows.forEach((row) => {
            if(row != undefined){
              let data = data_list.find(item => row.quId == item.quId)
              if(data!= undefined){
                this.$refs.multipleTable.toggleRowSelection(data, true);
              }
            }
          });
        })
      }

    },
    sureSel(){
      this.$emit("sure",this.datas)
    },
    paramsReset(){
      this.queryParams ={
        pageNum: 1,
          pageSize: 10,
          categoryId: null,//题库
          level:[],//难易程度
          content: ""
      }
    }
  }
}
</script>

<style scoped lang="scss">
.table-area{
  position: relative;
  .el-button{
    position: absolute;
    right: 0px;
    top: 46px;
    i{
      font-style: italic;
    }

  }
}
</style>
