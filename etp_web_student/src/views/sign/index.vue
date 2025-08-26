<template>
  <div class="app-container" style="overflow-x: hidden">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="活动标题" prop="hdmc">
        <el-input
          v-model="queryParams.hdmc"
          placeholder="请输入活动标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动副标题" prop="hdfmc" label-width="88px">
        <el-input
          v-model="queryParams.hdfmc"
          placeholder="请输入活动副标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择活动状态" clearable size="small">
          <el-option
            v-for="dict in hdztOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="标签" class="tags-div" prop="queryTag" style="width: calc(100% - 256px);">
        <div style="display: flex">
          <template style="position: relative">
            <el-checkbox v-model="checkAll" @change="handleCheckAllChange" >全部数据</el-checkbox>
            <el-checkbox-group v-model="queryTag" ref="tag" :style="{height: height,overflow: 'hidden'}" @change="handleCheckedTagsChange">
              <el-checkbox :label="item.id" v-for="item in tagOptions" name="tag" @change="handleQuery">{{item.name}}</el-checkbox>
            </el-checkbox-group>
            <div @click="openFop" v-if="tipFlag"  style="position: absolute;z-index: 9999;right: -5%;width: 100px;top:0;cursor: pointer;color: #1890ff">
              {{ openFlag?'展开':'收起' }}
            </div>
          </template>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row v-for="(item,index) in tableData" style="border-bottom: 1px solid #EEEEEE">
      <el-col style="margin-top: 17px;margin-bottom: 17px" :span="4">
        <el-image style="width: 180px; height: 90px;border-radius: 8px" :src="item.hdfm" :fit="fit"/>
        <div class="tag-cir">
          <el-tag v-if="item.status == '-1'" type="info" size="mini"
                  effect="dark">
            {{ statusFormat(item) }}
          </el-tag>
          <el-tag v-if="item.status == '0'" type="success" size="mini"
                  effect="dark">
            {{ statusFormat(item) }}
          </el-tag>
          <el-tag v-if="item.status == '1'" type="warning" size="mini"
                  effect="dark">
            {{ statusFormat(item) }}
          </el-tag>
        </div>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col>
            <span class="col-top">
              {{item.hdmc}}
            </span>
          </el-col>
        </el-row>
        <el-row style="">
          <el-col :span="5">
            <span v-if="item.hdfmc!=null" class="col-center">
              {{item.hdfmc}}
            </span>
            <span v-else style="color: white">
              {{0}}
            </span>
          </el-col>
          <el-col :span="6">

          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="9" class="col-bottom">
            <span>
              活动开始~结束时间：{{item.kssj}}~{{item.jssj}}
            </span>
          </el-col>
          <el-col :span="9" class="col-bottom">
            <span>{{item.joinStatus}}</span>
          </el-col>
          <el-col :span="6">
            <el-button v-if="item.joinStatus == '未报名' && item.status == '0'" size="medium" type="primary"
                       @click="handleUpdate(item)" v-hasPermi="['etp:sign:edit']">报名</el-button>
            <el-button v-if="item.joinStatus == '已报名' && item.status == '0'" size="medium" type="primary"
                       @click="handleUpdate(item)" v-hasPermi="['etp:sign:edit']">取消报名</el-button>
            <el-button @click="handleView(item)" size="medium" type="primary" v-hasPermi="['etp:sign:query']">查看详情</el-button>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row>
      <el-col :offset="10">
        <p v-if="loading" style="color:#999999;">下拉加载更多数据</p>
        <p v-if="noMore" style="color:#999999;">暂无更多数据</p>
      </el-col>
    </el-row>

    <detail ref="signForm" :open="open" :form="form"  @submitForm="submitForm" @closeDialog="close"></detail>
  </div>
</template>

<script>
  import { listSign, getSign, studentSign, updateSign, exportSign } from "@/api/sign/sign";
  import tagsEdit from "@/components/tags/tagsEdit"
  import {listTagRelate} from "@/api/tag/tag";
  import detail from "@/views/sign/components/detail";
  import {delQuestionnaire} from "@/api/etp/questionnaire/questionnaire";
  export default {
    name: "Sign",
    components:{tagsEdit,detail},
    data() {
      return {
        fit: 'cover',
        // 遮罩层
        loading: true,
        noMore: false,
        // 导出遮罩层
        exportLoading: false,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // sign表格数据
        signList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 活动状态字典
        hdztOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          hdmc: null,
          hdfmc: null,
          hdnr: null,
          kfcd: null,
        },
        // 表单参数
        form: {},
        // 标签用到的
        tagValue: [],
        tagOptions: [],
        queryTag: [],
        checkAll: true,
        height:'',
        tipFlag:false,
        //-------------
        tableData:[],//当前查询条件分页累加数据信息

      };
    },
    created() {
      this.getTags();
      this.getList();
      this.getDicts("hdbm_status").then(res =>{
        this.hdztOptions = res.data;
      })
    },

    mounted() {
      window.addEventListener('scroll', this.handleScroll)
    },

    methods: {

      handleScroll(){
        var scrollTop = Math.floor(document.body.scrollTop || document.documentElement.scrollTop || window.pageYOffset)
        if(this.lastScrollTop == scrollTop){
          return
        }
        // console.log( '获取距离顶部的距离' + scrollTop )
        var clientHeight = Math.floor(document.body.clientHeight || document.documentElement.clientHeight)
        // console.log( '获取可视区的高度' + clientHeight )
        var scrollHeight = Math.floor(document.body.scrollHeight || document.documentElement.scrollHeight)
        // console.log( '获取滚动条的总高度' + scrollHeight )

        if(scrollTop + clientHeight >= scrollHeight){
          this.lastScrollTop = scrollTop;
          if(this.tableData.length>=this.total){
            this.content = '暂无更多数据'
            this.noMore = true;
            this.loading = false;
          }else{
            this.content = '下拉加载更多数据'
            this.loading = true;
            this.noMore = false;
            this.queryParams.pageNum += 1;
            this.getList();
          }
        }
      },

      handleCheckAllChange(val) {
        this.queryTag = []
        this.handleQuery()
      },

      handleCheckedTagsChange(value) {
        // let checkedCount = value.length;
        this.checkAll = false
        // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
      },
      /**标签相关开始*/
      getTags() {
        listTagRelate({modelType: "HD"}).then(response => {
          this.tagOptions = response.data;
        });
      },
      havaTags(val) {
        this.tagValue = val
      },
      // 标签相关结束

      /** 查询sign列表 */
      getList(flag) {
        listSign(this.queryParams).then(response => {
          let list = response.rows;
          for (let i = 0; i < list.length; i++) {
            if(list[i].hdfm == null || list[i].hdfm == '' || list[i].hdfm == undefined){
              list[i].hdfm = require("@/assets/images/cover.jpg");
            }
          }
          this.signList = list;
          if(this.signList.length>0){
            if(flag){
              this.tableData = [];
            }
            this.signList.forEach((item,index)=>{
              this.tableData.push(item)
            })
          }
          this.total = response.total;
          if(this.tableData.length<=this.total){
            this.noMore = true;
            this.loading = false;
          }
        });
      },

      statusFormat(row, column){
        return this.selectDictLabel(this.hdztOptions, row.status)
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.tableData = [];
        if (null != this.queryTag) {
          this.queryParams.tags = this.queryTag.join(',')
        }
        this.getList(true);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryTag = [];
        this.handleQuery();
      },

      /** 详细按钮操作 */
      handleView(row) {
        const id = row.id
        getSign(id).then(response => {
          this.form ={}
          this.form = response.data
          this.open = true

        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        if(row.status == '1'){
          this.$message.error("活动已结束！")
          return;
        }
        const id = row.id
        // getSign(id).then(response => {
        //   this.form ={}
        //   this.form = response.data
        //   this.open = true
        //
        //   if (null != response.data.tags) {
        //     this.tagValue = response.data.tags.split(",");
        //   } else {
        //     this.tagValue = [];
        //   }
        // });
        let tip ="报名"
        if(row.sfbm=="1"){
          tip ="取消报名"
        }
        this.$confirm('是否确认'+tip+"？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() =>{

          let params = {"id":row.id,"sfbm":row.sfbm=="1"?'0':'1',"kfcd":row.kfcd}
          console.log(params);
          studentSign(params).then(response => {
            console.log(response)
            if(response.data==0){
              this.$message.error("活动已结束，操作失败！")
            }else{
              this.msgSuccess(tip+"成功");
            }
            this.getList();
          });
        }).catch(() => {});

      },
      /** 提交按钮 */
      submitForm(type) {
        //活动报名校验
        console.log()
        let params = {"id":this.form.id,"sfbm":type,"kfcd":this.form.kfcd}
        if (this.form.id != null) {
          studentSign(params).then(response => {
            let message = type=='1'?'报名成功':'取消报名成功'
            this.msgSuccess(message);
            this.open = false
            this.getList();
          });
        }
      },
      close(){
        this.open = false
      }

    }
  };
</script>
<style scoped>
.el-checkbox-group {
  margin-left: 30px;
}

.col-bottom {
  height: 13px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 32px;
}
.col-center {
  height: 15px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #666666;
  line-height: 20px;
}
.col-top {
  height: 22px;
  font-size: 20px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 52px;
}

::v-deep .el-button--primary{
  background-color: #0045C5;
  border-radius: 6px;
}

.tag-cir {
  width: 44px;
  height: 20px;
  background-color: #f1580b;
  color: #ffffff;
  position: absolute;
  top: 15px;
  border-radius: 3px 4px 4px 0px;
  font-size: 8px;
  left: -1px;
}
</style>
