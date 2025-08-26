<template>
<div style="background:#f5f5f5; min-height:93vh; ">
  <div class="top_bg" :class="fixed == true ? 'fixed' : ''">
      <el-row style="padding-top:10px; padding-left:20px;width: 1240px; margin: 0 auto;">
        <el-col>
          <!-- <div :class="{'icon-after':activeName=='1','icon-before':activeName!='1'}" @click="handleClick('1')">
            <img src="../../../../assets/home/icon_gkk_rm.png" class="icon-img">
            <div>
              <span class="icon-span">热门</span>
            </div>
          </div> -->
          <!-- <div :class="{'icon-after':activeName=='2','icon-before':activeName!='2'}" @click="handleClick('2')">
            <img src="../../../../assets/home/icon_gkk_jp.png" class="icon-img">
            <div>
              <span class="icon-span">精品</span>
            </div>
          </div> -->
          
           
        
         <div class="tagOptions" :class="{tagOptions1 : istagOption1}"  ref="tagOptions" :style="{ 'max-height': tagOptionsOpenFlag ? tagOptionsHeight : '' }">
                <div :class="{'icon-after':activeName=='3','icon-before':activeName!='3'}" @click="handleClick('3')">
                  <!-- <img src="../../../../assets/home/icon_gkk_tj.png" class="icon-img"> -->
                  <div>
                    <span class="icon-span">推荐</span>
                  </div>
                </div>
          
          <!-- </el-col> -->
          <!-- <el-col> -->
                
                <el-tag v-for="item in tagOptions"  :key="'tagOptions_' + item.id" 
         :id="item.id" @click="tagAdd(item.id)"  class="tag-top-before" :class=" tagOptionsId == item.id ? 'active' : ''">
                  {{item.name}}
                </el-tag>
                <!-- <el-tabs v-model="editableTabsValue" type="card" editable @edit="handleTabsEdit">
                  <el-tab-pane v-for="item in tagOptions"  :key="'tagOptions_' + item.id" 
         :id="item.id" @click="tagAdd(item.id)"  class="tag-top-before" :class=" tagOptionsId == item.id ? 'active' : ''">
                  </el-tab-pane>
                </el-tabs> -->
                <div v-if="showtagOptionsBtnFlag" @click="tagOptionsOpenFlag = !tagOptionsOpenFlag ,istagOption1 = !istagOption1" class="btn">          
                  {{ tagOptionsOpenFlag ? "展开" : "收起" }}
                </div>
          </div>
      </el-col>
    </el-row>
  </div>
  <div class="app-container-all">
      <el-row style="margin-bottom: 10px">
        <el-col :span="16">
          <span style="font-size: 24px;font-weight: bold;line-height: 50px">
            推荐课程
          </span>
        </el-col>
  <!--      <el-col :span="4">-->
  <!--        <el-select v-model="curriculumType" placeholder="请选择" size="small" style="line-height: 52px;margin-right: 5px">-->
  <!--          <el-option-->
  <!--            v-for="item in curriculumTypes"-->
  <!--            :key="item.value"-->
  <!--            :label="item.label"-->
  <!--            :value="item.value">-->
  <!--          </el-option>-->
  <!--        </el-select>-->
  <!--      </el-col>-->
  <!--      <el-col :span="4">-->
  <!--        <el-select v-model="queryParams.order" placeholder="请选择" size="small" style="line-height: 52px" @change="getList(true)">-->
  <!--          <el-option-->
  <!--            v-for="item in curriculumStatuses"-->
  <!--            :key="item.value"-->
  <!--            :label="item.label"-->
  <!--            :value="item.value">-->
  <!--          </el-option>-->
  <!--        </el-select>-->
  <!--      </el-col>-->
      </el-row>

<!-- 
    <el-row :gutter="20">
      <el-col :span="6" v-for="(item,index) in tableData" style="margin-bottom: 20px;box-shadow: 4px 4px 10px 0 rgb(205 216 228 / 60%)">
        <div class="fm" @click="rowClick(item)">
          <el-image :src="item.fmt" :fit="fit" style="width: 285px;height: 142px;border-radius: 8px;"/>
          <div :class="{'span-status-a':item.getStatus=='1','span-status-b':activeName!='1'}">
            <span v-if="item.getStatus =='1'">已领取</span>
          </div>
          <div class="tag-cir">
           <img src="../../../../assets/home/icon_gkk_rm.png" width="20" height="20" style="margin-right: 10px">
           <img src="../../../../assets/home/icon_gkk_jp.png" width="20" height="20" style="margin-right: 10px">
            <img src="../../../../assets/home/icon_gkk_tj.png" width="20" height="20" v-if="item.sftj==='1'">
          </div>
        </div>

        <div class="item-one">
          <span>{{item.mc}}</span>
        </div>
        <div style="width: 285px">
          <span class="item-two" style="float:left; width:220px;">
            {{item.createBy}}
          </span>
          <span class="item-two" style="text-align: right">
            {{item.num}}人已学习
          </span>
        </div>
      </el-col>
    </el-row> -->
    <div class="model-c">
      <div class="model-item" v-for="(item,index) in tableData" style="background:#fff;" >
        <div class="fm" @click="rowClick(item)">
          <img :src="item.fmt" :fit="fit" @error="e => { e.target.src = defaultImg }">
          <div :class="{'span-status-a':item.getStatus=='1','span-status-b':activeName!='1'}">
              <span v-if="item.getStatus =='1'">已领取</span>
          </div>
            <div class="tag-cir">
  <!--            <img src="../../../../assets/home/icon_gkk_rm.png" width="20" height="20" style="margin-right: 10px">-->
  <!--            <img src="../../../../assets/home/icon_gkk_jp.png" width="20" height="20" style="margin-right: 10px">-->
              <img src="../../../../assets/home/icon_gkk_tj.png" width="20" height="20" v-if="item.sftj==='1'">
            </div>
        </div>
        <div class="item-one">
          <span>{{item.mc}}</span>
        </div>
        <div style="width: 265px">
            <span class="item-two" style="float:left;  margin-left:10px;">
              {{item.createBy}}
            </span>
            <span class="item-two" style=" text-align: right; margin-right:10px;">
              {{item.num}}人已学习
            </span>
          </div>
        <div>
        </div>
      </div>
    </div>
    <el-row>
      <el-col :offset="10">
        <p v-if="loading" style="color:#999999;">下拉加载更多数据</p>
        <p v-if="noMore" style="color:#999999;">暂无更多数据</p>
      </el-col>
    </el-row>

  </div>
</div>
</template>

<script>
import imageUpload from "@/components/ImageUpload";
import tagsEdit from "@/components/tags/tagsEdit";
import {
  receiveCurriculum,
  getKnowledgeInfo,
  getCurriculum,
} from "@/api/etp/curriculum/curriculum";
import { listTagRelate } from "@/api/etp/tag/tag";
import { clickSendRedis } from "@/api/buryPoint";
import defaultImg from "@/assets/home/fm.png";

export default {
  name: "Curriculum",
  components: {
    imageUpload,
    tagsEdit,
    listTagRelate,
  },
  data() {
    return {
      // editableTabsValue: "2",
      fixed: false,
      fit: "cover",
      // 遮罩层
      loading: true,
      noMore: false,
      // 选中数组
      ids: [],
      open: false,
      title: "新增课程",
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // curriculum表格数据
      curriculumList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 16,
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        tags: [],
        sftj: null,
        order: "",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        mc: [{ required: true, message: "请输入表名称", trigger: "blur" }],
        fmt: [{ required: true, message: "请设置封面图", trigger: "blur" }],
        kfcd: [{ required: true, message: "请设置开放程度", trigger: "blur" }],
      },
      // 标签用到的
      tagValue: [],
      brandFold: false,
      tagOptions: [],
      istagOption1: true,
      // AlltagOptions: [],
      tagOptionsId: "", //当前选择的id
      tagOptionsHeight: null, //列表区域的显示高度
      tagOptionsOpenFlag: false, //为true时，展开；为false时，收起
      showtagOptionsBtnFlag: false, //为true时，出现展开-收起；为false,不出行展开-收起
      queryTag: [],
      isIndeterminate: true,
      checkAll: true,
      openFlag: true,
      tipFlag: false,
      height: "",
      // -----以下是测试滚动table参数------
      tableData: [], //当前查询条件分页累加数据信息
      lastScrollTop: 0, //防止抖动
      //  ------tabs页参数------
      activeName: "",
      content: "",
      defaultImg,

      curriculumTypes: [{ value: "", label: "查看全部课程" }],
      curriculumType: "",
      curriculumStatuses: [
        { value: "0", label: "最近更新" },
        { value: "1", label: "最近领取" },
      ],
      curriculumStatus: "",
    };
  },
  created() {
    let categoryId = this.$route.query.categoryId;
    // let tab = this.$route.query.tab
    // if(tab == 'first'){
    //   this.activeName = '1'
    // }else if(tab == 'second'){
    //   this.activeName = '3'
    //   this.queryParams.sftj = '1'
    // }

    this.getList();
    this.getTags();
  },
  activated() {
    // this.getList();
    this.getTags();
  },

  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    window.addEventListener("scroll", this.fixedActiveBtn);
    this.$nextTick(() => {
      setTimeout(() => {
        this.calculatetagOptions();
      }, 300);
    });
  },

  methods: {
    calculatetagOptions() {
      // 这是默认一行数据的高度
      let oneHeight = 42;
      this.tagOptionsHeight = `${oneHeight}px`;
      let curHeight = this.$refs.tagOptions.offsetHeight;
      if (curHeight > oneHeight) {
        this.tagOptionsOpenFlag = true;
        this.showtagOptionsBtnFlag = true;
      } else {
        this.tagOptionsOpenFlag = false;
        this.showtagOptionsBtnFlag = false;
      }
    },

    // handleChange(val) {
    //   // console.log(val);
    // },

    // tagAdd(id) {
    //   debugger;
    //   let result = this.queryTag.findIndex(ele => ele === id);
    //   if (result > -1) {
    //     this.queryTag.splice(result, 1);
    //     let tag = document.getElementById(id);
    //     console.log(tag);
    //     let a = document
    //       .getElementById(id)
    //       .setAttribute("class", "tag-top-before");
    //   } else {
    //     // this.queryTag.push(id);
    //     let tag = document.getElementById(id);
    //     console.log(tag);
    //     document.getElementById(id).setAttribute("class", "tag-top-after");
    //     let a = document.getElementById(id).getAttribute("type");
    //     console.log(a);
    //   }
    //   if (this.queryTag != null) {
    //     // this.queryParams.tags = this.queryTag.join(",");
    //     this.queryParams.tags = id;
    //   } else {
    //   }
    //   this.getList(true);
    // },
    tagAdd(id) {
      // debugger;
      if (this.queryTag.length > 0) {
        // let tag = document.getElementById(this.queryTag[0]);

        let a = document
          .getElementById(this.queryTag[0])
          .setAttribute("class", "tag-top-before");
        this.queryTag = [];
        this.queryTag.push(id);
        document.getElementById(id).setAttribute("class", "tag-top-after");
      } else {
        this.queryTag.push(id);
        // let tag = document.getElementById(id);

        document.getElementById(id).setAttribute("class", "tag-top-after");
        let a = document.getElementById(id).getAttribute("type");
      }
      this.queryParams.tags = id;
      this.getList(true);
    },
    // tagAdd(id) {
    // debugger;
    // if (this.queryTag.length > 0) {
    //   let tag = document.getElementById(this.queryTag[0]);

    //     .setAttribute("class", "tag-top-before");
    //   this.queryTag = [];
    //   this.queryTag.push(id);
    //   document.getElementById(id).setAttribute("class", "tag-top-after");
    // } else {
    //   this.queryTag.push(id);

    //   document.getElementById(id).setAttribute("class", "tag-top-after");
    //   let a = document.getElementById(id).getAttribute("type");
    // }
    //   let a = document.getElementById(id);
    //   let tag = document.getElementById(this.queryTag[0]);
    //   if (tag.classList.contains("tag-top-after") == true) {
    //     tag.classList.remove("tag-top-after");
    //     this.queryTag = [];
    //     this.queryTag.push(id);
    //   } else if (a.classList.contains("tag-top-after") == false) {
    //     this.queryTag.push(id);
    //     a.classList.add("tag-top-after");
    //   }
    //   this.queryParams.tags = id;
    //   this.getList(true);
    // },

    fixedActiveBtn() {
      var scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      scrollTop >= 56 ? (this.fixed = true) : (this.fixed = false);
    },

    //判断滚动条是否到底
    handleScroll() {
      var scrollTop = Math.floor(
        document.body.scrollTop ||
          document.documentElement.scrollTop ||
          window.pageYOffset
      );
      var clientHeight = Math.floor(
        document.body.clientHeight || document.documentElement.clientHeight
      );
      var scrollHeight = Math.floor(
        document.body.scrollHeight || document.documentElement.scrollHeight
      );

      if (scrollTop + clientHeight >= scrollHeight) {
        this.lastScrollTop = scrollTop;
        if (this.tableData.length >= this.total) {
          this.content = "暂无更多数据";
          this.noMore = true;
          this.loading = false;
        } else {
          this.content = "下拉加载更多数据";
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum += 1;
          this.getList();
        }
      }
    },

    //tab页点击事件
    handleClick(tab) {
      this.tableData = [];
      if (tab == "1" && this.activeName != "1") {
        this.activeName = "1";
        // this.queryParams.sftj = null;
        // this.queryParams.pageNum = 1;
        // if(this.noMore) this.noMore = false;
        // this.getList(true)
        this.tableData = [];
        this.noMore = true;
      } else if (tab == "1" && this.activeName == "1") {
        this.activeName = "";
        this.queryParams.sftj = null;
        this.queryParams.pageNum = 1;
        if (this.noMore) this.noMore = false;
        this.getList(true);
      }

      if (tab == "2" && this.activeName != "2") {
        this.activeName = "2";
        // this.queryParams.sftj = null;
        // this.queryParams.pageNum = 1;
        // if(this.noMore) this.noMore = false;
        // this.getList(true)
        this.tableData = [];
        this.noMore = true;
      } else if (tab == "2" && this.activeName == "2") {
        this.activeName = "";
        this.queryParams.sftj = null;
        this.queryParams.pageNum = 1;
        if (this.noMore) this.noMore = false;
        this.getList(true);
      }

      if (tab == "3" && this.activeName != "3") {
        this.activeName = "3";
        this.queryParams.sftj = "1";
        this.queryParams.pageNum = 1;
        if (this.noMore) this.noMore = false;
        this.getList(true);
      } else if (tab == "3" && this.activeName == "3") {
        this.activeName = "";
        this.queryParams.sftj = null;
        this.queryParams.pageNum = 1;
        if (this.noMore) this.noMore = false;
        this.getList(true);
      }
    },

    //强制重新加载子组件
    rebuileComponents() {
      // 销毁子标签
      this.hackReset = false;
      // 重新创建子标签
      this.$nextTick(() => {
        this.hackReset = true;
      });
    },
    //重置
    async handleReset() {},

    rowClick(row) {
      let jd = null;
      getCurriculum(row.id).then(res => {
        jd = res.data.kcjd;
        if (jd) {
          if (JSON.parse(jd).lastStudy == undefined) {
            jd = null;
          } else {
            jd = JSON.parse(jd)["lastStudy"]["current"];
          }
        }
        let params = {};
        params.streamName = "stream";
        params.eventType = "CLI_CURR";
        params.id = row.id;

        //点击埋点
        clickSendRedis(params).then(response => {
          console.log("点击课程埋点", response);
        });

        // 打开新的浏览器标签页进行学习
        let router = this.$router.resolve({
          path: "/etp/curriculum/previewCurriculum/",
          query: { cid: row.id, jd: jd },
        });
        window.open(router.href, "_blank");
      });
    },

    //---------------------------------------------

    /** 预览课件 */
    // rowClick(row){
    //   let router = this.$router.resolve({
    //     path: "/etp/curriculum/previewCurriculum/",
    //     query: {cid: row.id,getStatus:row.getStatus}
    //   })
    //   window.open(router.href,'_blank')
    // },
    /** 领取课程 */
    getCurriculum(row) {
      let json = { kcid: row.id };
      receiveCurriculum(json).then(res => {
        if (res.code == "200") {
          this.$message.success("课程领取成功！");
          this.getList();
        }
      });
    },

    handleCheckAllChange(val) {
      this.queryTag = [];
      this.handleQuery();
    },

    handleCheckedTagsChange(value) {
      // let checkedCount = value.length;
      this.checkAll = false;
      // this.isIndeterminate = checkedCount > 0 && checkedCount < this.tagOptions.length;
    },

    /** 查询curriculum列表 */
    getList(flag) {
      getKnowledgeInfo(this.queryParams).then(response => {
        // debugger;
        let list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].fmt == null ||
            list[i].fmt == "" ||
            list[i].fmt == undefined
          ) {
            list[i].fmt = require("@/assets/images/cover.jpg");
          }
        }
        this.curriculumList = list;
        if (flag) {
          this.tableData = [];
        }
        if (this.curriculumList.length > 0) {
          this.curriculumList.forEach((item, index) => {
            this.tableData.push(item);
          });
        } else {
          this.noMore = true;
        }
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        mc: null,
        ms: null,
        fmt: null,
        kfcd: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      if (null != this.queryTag) {
        this.queryParams.tags = this.queryTag.join(",");
      }
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path: "/etp/curriculum/addOrUpdateCurriculum/",
        query: {
          id: row.id,
        },
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addCurriculum(this.addTags(this.form, this.tagValue)).then(
            response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              //更新一下标签列表
              this.getTags();
            }
          );
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除curriculum编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function() {
          return delCurriculum(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
          //更新一下标签列表
          this.getTags();
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有curriculum数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.exportLoading = true;
          return exportCurriculum(queryParams);
        })
        .then(response => {
          this.download(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /**标签相关开始*/
    getTags() {
      listTagRelate({ modelType: "KC" }).then(response => {
        this.tagOptions = response.data;
        this.$nextTick(_ => {
          if (this.$refs.tag.$el.clientHeight > 44) {
            this.height = 45 + "px";
            this.tipFlag = true;
          }
        });
      });
    },
  },
};
</script>

<style scoped lang="scss" src="../../../../assets/styles/home-css.scss"></style>
<style type="scss" scoped >
.top_bg {
  /* min-height: 56px; */
  background: #fff;
  width: 100%;
  /* position: absolute; */
  box-shadow: 0px 4px 18px 0px rgba(0, 0, 0, 0.1);
  padding-bottom: 25px;
}
.fixed {
  position: fixed;
  top: 0;
  z-index: 21;
}
.icon-before {
  /* width: 64px; */
  /* height: 75px; */

  /* width: 80px; */
  padding: 0 15px;
  color: #333333;
  background-color: #f5f5f5;
  margin-right: 10px;
  height: 36px;
  line-height: 36px;

  float: left;
  cursor: pointer;
  border-radius: 18px;
  margin-right: 15px;
}
.icon-before:hover {
  color: #0045c5;
  background-color: #e5ecf9;
}
.icon-after {
  /* width: 64px; */
  /* height: 75px; */

  /* width: 80px; */
  padding: 0 15px;
  /* margin-bottom: 10px; */
  margin-right: 15px;
  height: 36px;
  line-height: 36px;

  float: left;
  cursor: pointer;
  color: #0045c5;
  background-color: #e5ecf9;
  border-radius: 18px;
  margin-right: 15px;
}
.icon-span {
  font-size: 16px;
  font-weight: 500;
  text-align: center;
  display: block;
}
.icon-img {
  clear: both;
  display: block;
  margin: auto;
  padding-top: 10px;
}

.item-one {
  width: 265px;
  height: 44px;
  margin-left: 10px;
  margin-right: 15px;
  font-size: 16px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #333333;
  line-height: 20px;
  margin-top: 16px;
  margin-bottom: 4px;
}

.item-two {
  width: 142px;
  height: 15px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 20px;
}

.tag-top-before {
  color: #333333;
  background-color: #f5f5f5;
  /* margin-bottom: 10px; */
  margin-right: 15px;
  cursor: pointer;
  /* width: 80px; */
  height: 36px;
  text-align: center;
  font-size: 16px;
  border-color: #f5f5f5;
  display: inline-block;
  padding: 0 15px;
  line-height: 36px;
  border-width: 1px;
  border-style: solid;
  border-radius: 18px;
  box-sizing: border-box;
  white-space: nowrap;
  margin-bottom: 10px;
}
.tag-top-before:hover {
  color: #0045c5;
  background-color: #e5ecf9;
  border-color: #e5ecf9;
}
.tag-top-after {
  /* margin-bottom: 10px; */
  margin-right: 15px;
  margin-bottom: 10px;
  color: #0045c5;
  background-color: #e5ecf9;
  border-color: #e5ecf9;
  cursor: pointer;
  /* width: 80px; */
  height: 36px;
  text-align: center;
  font-size: 16px;
  line-height: 29px;
  display: inline-block;
  padding: 0 15px;
  line-height: 36px;
  border-width: 1px;
  border-style: solid;
  border-radius: 18px;
  box-sizing: border-box;
  white-space: nowrap;
}
.el-col-offset-10 {
  margin: 10px auto;
  text-align: center;
  font-size: 16px;
}
.fm {
  position: relative;
  height: 142px;
  width: 285px;
  cursor: pointer;
}

.tag-cir {
  height: 23px;
  position: absolute;
  top: 114px;
  border-radius: 3px 4px 4px 0px;
  font-size: 8px;
  left: 255px;
}
.model-item .fm .tag-cir img {
  width: 23px;
  height: 23px;
}

.span-status-a {
  width: 72px;
  height: 23px;
  border-radius: 0px 8px 0px 8px;
  color: #ffffff;
  position: absolute;
  top: 0px;
  font-size: 14px;
  left: 213px;
  text-align: center;
  background-color: #009944;
  line-height: 25px;
}

.span-status-a {
  width: 72px;
  height: 23px;
  border-radius: 0px 8px 0px 8px;
  color: #ffffff;
  position: absolute;
  top: 0px;
  font-size: 14px;
  left: 213px;
  text-align: center;
  line-height: 25px;
}

.desc {
  white-space: nowrap !important;
  text-overflow: ellipsis !important;
  overflow: hidden !important;
}
.tags-div ::v-deep .el-form-item__content {
  width: calc(100% - 100px) !important;
}
.el-checkbox-group {
  margin-left: 30px;
}

.app-container-all {
  padding: 20px;
  width: 1240px;
  margin: 0 auto;
  overflow-x: hidden;
}

.col-bottom {
  height: 13px;
  font-size: 14px;
  font-family: PingFang SC;
  font-weight: 500;
  color: #999999;
  line-height: 38px;
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

.tagOptions {
  overflow: hidden;
  max-height: 184px;
  overflow-y: auto;
}
.tagOptions1::-webkit-scrollbar {
  display: none;
}
.tagOptions1 {
  scrollbar-width: none;
  -ms-overflow-style: none;
}
.tagOptions::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}

.tagOptions::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 6px;
  height: 6px;
}

.tagOptions::-webkit-scrollbar-thumb {
  cursor: pointer;
  border-radius: 10px;
  background: rgba(0, 0, 0, 0.1);
  transition: color 0.2s ease;
}

.btn {
  font-size: 14px;
  color: #999999;
  position: absolute;
  right: 50%;
  margin-right: -28px;
  bottom: -25px;
  /* background-color: #fff; */
  background-color: #f5f5f5;
  border-top-left-radius: 15px;
  border-top-right-radius: 15px;
  padding: 1px 15px;
  &:hover {
    color: #0045c5;
    background: #e8f3ff;
  }
}
::v-deep .el-button--primary {
  background-color: #0045c5;
  border-radius: 6px;
}

::v-deep .el-tabs__nav-wrap::after {
  /*el-tabs组件隐藏下划线*/
  position: static !important;
}

::v-deep .el-tabs__item {
  font-size: 18px;
}
</style>
