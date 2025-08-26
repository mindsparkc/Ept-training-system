<template>
  <div class="app-container" style="overflow-x: hidden;">
    <div class="font-size-xl font-color-black">我的考试</div>

    <div
      class="flex-row flex-col-center flex-between"
      style="
        margin-top: 28px;
        border-bottom: 1px solid hsla(0,0%,91.4%,.6);
      "
    >
      <div style="justify-content: flex-start">
        <span
          v-for="(item, index) in examTypes"
          :key="index"
          style="margin-right: 10px"
          :class="[item.isChecked ? 'tab-check' : 'tab-uncheck']"
          @click="examTypeOnClick(index)"
        >
          {{ item.name }}
        </span>
      </div>
      <div v-if="false">
        <el-dropdown @command="isActiveCheckOnClick">
          <span class="dropdown">
            {{ activeSort }}<i class="el-icon-caret-bottom el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <div v-for="(item, index) in isActiveSorts" :key="index">
              <el-dropdown-item :command="item">{{
                item.name
              }}</el-dropdown-item>
            </div>
          </el-dropdown-menu>
        </el-dropdown>

        <el-dropdown @command="timeCheckOnClick" style="margin-left: 10px">
          <span class="dropdown">
            {{ timeSort }}<i class="el-icon-caret-bottom el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <div v-for="(item, index) in timeSorts" :key="index">
              <el-dropdown-item :command="item">{{
                item.name
              }}</el-dropdown-item>
            </div>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <div
      v-for="(examItem, examIndex) in examinationList"
      :key="examIndex"
      class="exam-item"
    >
      <el-image
        v-if="examItem && examItem.cover"
        style="width: 180px; height: 90px; border-radius: 8px;flex-shrink: 0;"
        :src="examItem.cover"
        fit="cover"
      ></el-image>
      <img src="../../../assets/images/examine_cover.png"  style="width: 180px; height: 90px; border-radius: 8px" v-else />

      <div style="width: 100%; margin-left: 20px; cursor: pointer" >
        <div
          class="font-size-l font-color-black text-title"

        >
          {{ examItem.name }}
        </div>

        <div style="margin-top: 12px">
          <span class="font-size-m font-color-light-grey">考试时长：</span>
          <span class="font-color-black font-size-m">
            {{
              examItem.totalTime && examItem.totalTime != 0
                ? examItem.totalTime + " 分钟"
                : "不限时长"
            }}</span
          >
          <span
            class="font-size-m font-color-light-grey"
            style="margin-left: 20px"
            >总分：</span
          >
          <span class="font-color-black font-size-m"
            >{{ examItem.totalScore ? examItem.totalScore : "0" }}分</span
          >
          <span
            class="font-size-m font-color-light-grey"
            style="margin-left: 20px"
            >及格分：</span
          >
          <span class="font-color-black font-size-m"
            >{{ examItem.qualifyScore ? examItem.qualifyScore : "0" }}分</span
          >
        </div>
        <div style="margin-top: 12px">
          <span
            v-if="examItem.handMinTime != 0"
            style="margin-right: 20px"
            class="font-size-m font-color-light-grey"
            >考试<span class="font-color-red font-size-m"
              >开始{{ examItem.handMinTime }}分钟</span
            >后允许交卷
          </span>

          <span
            v-if="examItem.timeLimit == 1"
            class="font-size-m font-color-light-grey"
            >考试<span class="font-color-red font-size-m"
              >迟到{{ examItem.lateTime }}分钟</span
            >则不允许参加考试
          </span>
        </div>
        <div style="margin-top: 12px">
          <span class="font-size-m font-color-light-grey"
            >考试次数：
            <span class="font-color-black font-size-m">
              {{ examItem.usedChance }}次/</span
            >
            {{ examItem.chance == 0 ? "不限" : examItem.chance + "次" }}</span
          >
          <span
            class="font-size-m font-color-light-grey"
            style="margin-left: 20px"
            >考试时间：<span class="font-color-grey font-size-m"
              >{{
                examItem.timeLimit == 1
                  ? examItem.startTime
                  : examItem.publishTime
              }}{{ examItem.endTime ? " -- " + examItem.endTime : "" }}</span
            >
          </span>
        </div>
      </div>

      <el-button
        :type="judgeDate(examItem).stateIndex == 1 ? 'primary' : 'info'"
        style="position: absolute; bottom: 20%; right: 12px"
        @click="handleToExam(examItem)"
        >{{ judgeDate(examItem).stateName }}</el-button
      >
    </div>
    <p v-if="loading" class="text-p">下拉加载更多数据</p>
    <p v-if="noMore" class="text-p">暂无更多数据</p>

    <el-form
      @submit.native.prevent
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      v-if="false"
    >
      <el-form-item label="考试名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入考试名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考试名称" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="请输入考试描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="examinationList" v-if="false">
      <!-- <el-table-column label="封面" align="center" prop="cover">
        <template slot-scope="scope">
          <img :src="scope.row.cover" width="90" height="70" alt="X" />
        </template>
      </el-table-column> -->
      <el-table-column
        label="考试名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="考试描述"
        align="center"
        prop="content"
        show-overflow-tooltip
      />
      <el-table-column
        label="考试推送时间"
        align="center"
        prop="publishTime"
        width="180"
      />
      <el-table-column
        label="考试开始时间"
        align="center"
        prop="startTime"
        width="180"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.startTime == null">未限制</span>
          <span v-if="scope.row.startTime != null">{{
            scope.row.startTime
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="考试结束时间"
        align="center"
        prop="endTime"
        width="180"
      />
      <el-table-column
        label="考试机会"
        align="center"
        prop="chance"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="scope.row.chance == 0">不限次</span>
          <span v-else>{{ scope.row.chance }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="已使用机会"
        align="center"
        prop="usedChance"
        show-overflow-tooltip
      />
      <el-table-column
        label="考试时长"
        align="center"
        prop="totalTime"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ scope.row.totalTime }}分钟</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="总分数" align="center" prop="zfs" show-overflow-tooltip/>-->
      <!--      <el-table-column label="考试状态" align="center" prop="ksjd" show-overflow-tooltip>-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-tag v-if="scope.row.ksjd =='进行中'" type="success">{{scope.row.ksjd}}</el-tag>-->
      <!--          <el-tag v-else-if="scope.row.ksjd =='已结束'" type="info">{{scope.row.ksjd}}</el-tag>-->
      <!--          <el-tag v-else>{{scope.row.ksjd}}</el-tag>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.ksjd != '已结束'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleToExam(scope.row)"
            >考试</el-button
          >
          <!--          <el-button-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-tickets"-->
          <!--            @click="showInfo(scope.row)"-->
          <!--          >详情</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-if="false"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="查看成绩" :visible.sync="dialogTableVisible">
      <el-table :data="gridData" style="margin-bottom: 30px">
        <el-table-column
          property="createTime"
          align="center"
          label="考试时间"
          sortable
        />
        <el-table-column property="score" align="center" label="成绩" sortable>
          <template slot-scope="scope">
            {{ scope.row.score ? scope.row.score : 0 }}分
          </template>
        </el-table-column>
        <el-table-column property="zfs" align="center" label="总分数" sortable>
          <template slot-scope="scope">
            {{ scope.row.zfs ? scope.row.zfs : "——" }}分
          </template>
        </el-table-column>
        <!--        <el-table-column property="rate" align="center" label="正确率" sortable :formatter="rateFormate"/>-->
      </el-table>
      <pagination
        :autoScroll="false"
        v-show="gridData.length > 0"
        :limit.sync="gridForm.pageSize"
        :page.sync="gridForm.pageNum"
        :total="gridData2.length"
        @pagination="getGridData"
      />
    </el-dialog>
    <el-dialog
      :visible="djsTableVisible"
      width="700px"
      center
      :show-close="false"
      :close-on-click-modal="true"
    >
      <div class="djs-div">
        <div class="djs-ms"><span>距离开始考试还剩:</span></div>
        <div class="djs-time">
          <span v-if="day != '0'">{{ day }}天</span
          ><span>{{ hr }}时{{ min }}分{{ sec }}秒</span>
        </div>
        <div class="djs-button">
          <el-button @click="djsTableVisible = false" type="primary"
            >关闭</el-button
          >
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listExam,
  getExamInfo,
  startExam,
  submitTheExam,
  getHistoryExam,
} from "@/api/exam/exam";

export default {
  name: "Examination",
  data() {
    return {
      examTypes: [
        { name: "所有形式", type: "0", isChecked: true },
        // { name: "公开的", type: "1", isChecked: false },
        // { name: "指派的", type: "2", isChecked: false },
      ],
      isActiveSorts: [
        { name: "查看全部考试", type: "0" },
        { name: "只看有效考试", type: "1" },
        { name: "只看无效考试", type: "2" },
      ],
      timeSorts: [
        { name: "即将到期", type: "0" },
        { name: "刚刚开始", type: "1" },
      ],

      activeSort: "查看全部考试",
      timeSort: "即将到期",

      nowTime: "",
      // 遮罩层
      loading: true,
      noMore: false,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 考试表格数据
      examinationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 发布状态：0-暂存，1-发布字典
      fbOptions: [],
      // 开放程度：完全公开-0，限定用户-1字典
      kfcdOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        name: null,
        content: null,
        orders: null,
      },
      gridForm: {
        pageNum: 1,
        pageSize: 10,
      },
      gridData: [], // 页面使用的数据
      gridData2: [], // 总数据
      dialogTableVisible: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      //倒计时参数
      djsTableVisible: false,
      day: 0,
      hr: 0,
      min: 0,
      sec: 0,

      // 标签用到的
      tagValue: [],
      tagOptions: [],
      queryTag: [],
      isIndeterminate: true,
      checkAll: true,
      openFlag: true,
      tipFlag: false,
      height: "",

      djsFlag: false,
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  computed: {},

  methods: {
    handleScroll() {
      // 获取滚动的距离
      let scrollTop = document.documentElement.scrollTop;
      // 获取滚动的高度（获取整个html的高度）
      let scrollHeight = document.documentElement.scrollHeight;
      // 获取屏幕(浏览器)高度
      let clientHeight = document.documentElement.clientHeight;
      // 滚动的距离 + 屏幕高度 - 内容高度 >= 0 表示滚动到底部了      (下拉加载判断条件)
      if (scrollTop + clientHeight - scrollHeight >= 0) {
        //判断是否需要获取数据
        if (this.examinationList.length >= this.total) {
          this.loading = false;
          this.noMore = true;
        } else {
          this.loading = true;
          this.noMore = false;
          this.queryParams.pageNum++;
          this.getList();
        }
      }
    },

    //考试类型 所有形式  公开  指派
    examTypeOnClick(index) {
      this.examTypes[index].isChecked = true;
      this.examTypes.forEach((item, itemIndex) => {
        if (index == itemIndex) {
          item.isChecked = true;
        } else {
          item.isChecked = false;
        }
      });

      //重新发起请求 FIXME
      // this.queryParams.xx

      this.handleQuery();
    },

    //考试  有效  无效
    isActiveCheckOnClick(command) {
      console.log(command);
      this.activeSort = command.name;

      //重新发起请求 FIXME
      this.handleQuery();
    },
    //即将到期 刚刚开始
    timeCheckOnClick(command) {
      console.log(command);
      this.timeSort = command.name;

      //重新发起请求 FIXME
      this.handleQuery();
    },

    //新增 根据status判断 # 考试状态（1暂存 2发布 3正常结束 4强制结束），允许管理员强制结束已经开始的考试，学员只能搜索到已发布的考试  若status为4强制结束 显示已结束  当为发布状态时判断开始时间  未到考试时间显示未开始 已到开始时间未到结束时间显示 进行中
    judgeDate(item) {
      //返回值 stateIndex  0 未开始 1正在进行 2已结束
      let status = item.status;
      let obj = {};
      if (status == 3 || status == 4) {
        //强制结束
        obj.stateIndex = 2;
        obj.stateName = "考试结束";
        return obj;
      } else if (status == 2) {
        let startTime = new Date(item.startTime);

        let now = new Date().getTime();

        if (now < startTime) {
          //当前时间小于开始时间 则考试未开始
          obj.stateIndex = 0;
          obj.stateName = "尚未开始";
          return obj;
        } else {
          //当前时间大于开始时间
          if (item.endTime) {
            let endTime = new Date(item.endTime);
            if (now < endTime) {
              //当前时间小于结束时间 考试进行中
              obj.stateIndex = 1;
              obj.stateName = "开始考试";
              return obj;
            } else {
              //当前时间大于结束时间  考试已结束
              obj.stateIndex = 2;
              obj.stateName = "考试结束";
              return obj;
            }
          } else {
            //endTime为空代表不限制结束时间 为一个长期的考试
            obj.stateIndex = 1;
            obj.stateName = "开始考试";
            return obj;
          }
        }
      }
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
    openFop() {
      this.openFlag = !this.openFlag;
      if (!this.openFlag) {
        // 展开
        this.height = "auto";
      } else {
        this.height = 45 + "px";
      }
      // this.$forceUpdate()
    },
    //倒计时方法
    countdown(row) {
      const startTime = Date.parse(row.startTime);
      const nowTime = Date.parse(new Date());
      const msec = startTime - nowTime;

      let day = parseInt(msec / 1000 / 60 / 60 / 24);
      let hr = parseInt((msec / 1000 / 60 / 60) % 24);
      let min = parseInt((msec / 1000 / 60) % 60);
      let sec = parseInt((msec / 1000) % 60);
      this.day = day;
      this.hr = hr > 9 ? hr : "0" + hr;
      this.min = min > 9 ? min : "0" + min;
      this.sec = sec > 9 ? sec : "0" + sec;
      const that = this;
      if ((hr == 0) & (min == 0) & (sec == 0)) {
        this.handleToExam(row);
      } else {
        if (this.djsTableVisible) {
          setTimeout(function() {
            that.countdown(row);
          }, 1000);
        }
      }
    },

    getGridData() {
      let startIndex = (this.gridForm.pageNum - 1) * this.gridForm.pageSize;
      let endIndex =
        this.gridForm.pageSize * this.gridForm.pageNum < this.gridData2.length
          ? this.gridForm.pageSize * this.gridForm.pageNum
          : this.gridData2.length;
      this.gridData = [];
      for (let i = startIndex; i < endIndex; i++)
        this.gridData.push(this.gridData2[i]);
    },

    rateFormate(row) {
      // return  (row.rate * 100).toFixed(0) + "%"
      // parseFloat(row.rate) * 100 + '%'
      // (Number(row.rate).toFixed(2))
    },

    /** 查看成绩*/
    showInfo(row) {
      getHisInfo(row.id).then(res => {
        this.dialogTableVisible = true;
        console.log(res);
        this.gridData = [];
        this.gridData2 = res.data.tTestpaperStudent;
        for (
          let i = 0;
          i <
          (this.gridForm.pageSize > this.gridData2.length
            ? this.gridData2.length
            : this.gridForm.pageSize);
          i++
        ) {
          this.gridData.push(this.gridData2[i]);
          this.gridData[i]["zfs"] = res.data.zfs;
        }
      });
    },

    /** 查看详情 */
    handleToExam(row) {
      if (row.timeLimit == 1) {
        const startTime = Date.parse(row.startTime);
        const now = Date.parse(new Date());
        if (startTime > now) {
          this.djsTableVisible = true;
          this.countdown(row);
        } else if (row.chance != "0" && row.chance <= row.usedChance) {
          this.$message.error("您的考试次数已用完！");
        } else {
          this.djsTableVisible = false;
          let router = this.$router.resolve({
            path: "/examStart/detail/" + row.examId,
          });
          window.open(router.href, "_blank");
          // this.$router.push("/examManagement/detail/" + row.id);
        }
      } else if (row.chance != "0" && row.chance <= row.usedChance) {
        this.$message.error("您的考试次数已用完！");
      } else {
        this.djsTableVisible = false;
        let router = this.$router.resolve({
          path: "/examStart/detail/" + row.examId,
        });
        window.open(router.href, "_blank");
        // this.$router.push("/examManagement/detail/" + row.id);
      }
    },
    /** 查询考试列表 */
    getList() {
      this.loading = true;
      listExam(this.queryParams).then(response => {
        let list = response.rows;
        if (list && list.length > 0) {
          for (let i = 0; i < list.length; i++) {
            if (
              list[i].cover == null ||
              list[i].cover == "" ||
              list[i].cover == undefined
            ) {
              let cover = require("@/assets/images/examine_cover.png");
              list[i].cover = cover;
            }
          }
          this.examinationList = this.examinationList.concat(list);
          // console.log(this.examinationList);

          this.total = response.total;

          if (this.examinationList.length >= this.total) {
            //若本次请求列表长度大于等于列表总数 则认为没有更多数据了
            this.noMore = true;
          }
        } else {
          this.noMore = true;
        }
        this.loading = false;
      });
    },
    reset() {
      this.form = {
        id: null,
        mc: null,
        ms: null,
        cover: null,
        fb: null,
        kslj: null,
        kfcd: null,
        sjid: null,
        kskssj: null,
        ksjssj: null,
        djsc: null,
        dtcs: null,
        fsdj: null,
        zfs: null,
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
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryTag = [];
      this.handleQuery();
    },
  },
};
</script>
<style scoped lang="scss">
.djs-div {
  height: 200px;
  width: 100%;
  .djs-ms {
    font-size: 40px;
    flood-color: #121315;
    text-align: center;
    padding: 20px;
    font-weight: bold;
  }
  .djs-time {
    font-size: 30px;
    flood-color: #121315;
    text-align: center;
    padding: 10px;
  }
  .djs-button {
    position: absolute;
    right: 40px;
    bottom: 20px;
  }
}
.el-checkbox-group {
  margin-left: 30px;
}

.text-title-big {
  font-size: 24px;
  font-weight: bold;
  color: #333333;
}
.text-title-normal {
  font-size: 20px;
  font-weight: bold;
  color: #333333;
}
.font-size-xl {
  font-size: 24px;
  font-weight: 600;
}
.font-size-l {
  font-size: 16px;
}
.font-size-m {
  font-size: 14px;
}

.font-color-black {
  color: #333333;
}
.font-color-grey {
  color: #666666;
}
.font-color-light-grey {
  color: #999999;
}
.font-color-red {
  color: #ff0000;
}
.font-bold {
  font-weight: bold;
}

.tab-check {
  cursor: pointer;
  color: #0045c5;
  background-color: rgba(0, 69, 197, 0.05);
  border-radius: 2px;
  font-size: 14px;
  padding: 2px 10px;
  border-bottom: 1px solid hsla(0, 0%, 91.4%, 0.6);
}
.tab-uncheck {
  cursor: pointer;
  color: #333333;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-size: 14px;
  padding: 2px 10px;
}

.dropdown {
  cursor: pointer;
  color: #333333;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-size: 14px;
  padding: 5px 10px;
}
.dropdown:hover {
  color: #0045c5;
  background-color: rgba(0, 69, 197, 0.05);
}

.flex-row {
  display: flex;
  flex-direction: row;
}
.flex-between {
  justify-content: space-between;
}
.flex-col-center {
  align-items: center;
}

.cover-img {
  width: 180px;
  height: 90px;
  border-radius: 8px;
}

.exam-item {
  //cursor: pointer;
  display: flex;
  flex-direction: row;
  position: relative;
  margin-top: 18px;
  padding-bottom: 12px;

  border-bottom: 1px solid hsla(0, 0%, 91.4%, 0.6);
}

.text-p {
  text-align: center;
  color: #999999;
  font-size: 12px;
}

.exam-item:hover .text-title {
  color: #0045c5;
}

::v-deep .el-button--primary {
  background-color: #0045c5;
  border-color: #0045c5;
  border-radius: 6px;
}
</style>
