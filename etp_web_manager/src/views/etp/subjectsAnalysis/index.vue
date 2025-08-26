<template>
  <!-- 题库分析   -->
  <div class="app-container custom_box">
    <div style="height: 300px">
      <div>
        <el-row style="margin: 8px 15px 8px 15px">
          <el-col :span="24">
            <div style="text-align: center">
              <h3>试题使用分析</h3>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <div style="position: relative;left:15px;height: 40px;top: 10px">
              <div class="pre-desc">
                <span style="margin-left:10px;font-size: 14px">题型数量统计情况</span>
              </div>
            </div>
            <div>
              <div ref="chart"  style="height: 500px"/>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="6">
            <div style="border: 1px solid #eff2f6;padding: 15px 8px">
              <div style="position: relative;left:15px;height: 40px;top: 10px">
                <div class="pre-desc">
                  <span style="margin-left:10px;font-size: 14px">题型树形结构</span>
                </div>
              </div>
              <el-tree accordion :data="arr" ref="tree"
                       :props="defaultProps"
                       :highlight-current="true"
                       :default-expanded-keys="expanded"
                       :current-node-key="currentNodekey"
                       node-key="id"
                       @node-click="handleNodeClick">
                <span class="span-ellipsis custom-tree-node" slot-scope={node,data}>
                  <span>{{ data.tg }}</span>
                </span>
              </el-tree>
            </div>
            <div></div>
          </el-col>
          <el-col :span="18">
            <div style="border: 1px solid #eff2f6;margin-left: 15px">
              <div>
                <div style="position: relative;left:15px;height: 40px;top: 10px">
                  <div class="pre-desc">
                    <span style="margin-left:10px;font-size: 14px">课程引用情况</span>
                  </div>
                </div>
                <div>
                  <el-table
                    v-loading="courseloading"
                    :data="coursesTableData"
                    style="width: 100%">
                    <el-table-column align="center"
                      type="index"
                      label="ID"
                      width="50">
                    </el-table-column>
                    <el-table-column align="center"
                      prop="mc"
                      label="课程名称"
                      width="180">
                    </el-table-column>
                    <el-table-column align="center"
                      prop="states"
                      label="课程状态"
                      width="180" :formatter="courseStatesFormat">
                    </el-table-column>
                    <el-table-column align="center" sortable
                                     prop="createTime"
                                     label="创建时间">
                    </el-table-column>
                    <el-table-column align="center" sortable
                      prop="updateTime"
                      label="更新时间">
                    </el-table-column>
                  </el-table>
                  <pagination :autoScroll="false"
                    v-show="coursesTableData.length > 0"
                    :limit.sync="coursesGridForm.pageSize"
                    :page.sync="coursesGridForm.pageNum"
                    :total="coursesData.length"
                    @pagination="getCourseGridData"
                  />
                </div>
              </div>
              <div>
                <div style="position: relative;left:15px;height: 40px;top: 10px">
                  <div class="pre-desc">
                    <span style="margin-left:10px;font-size: 14px">试卷引用情况</span>
                  </div>
                </div>
                <div>
                  <el-table
                    v-loading="examloading"
                    :data="examsTableData"
                    style="width: 100%">
                    <el-table-column align="center"
                                     type="index"
                                     label="ID"
                                     width="50">
                    </el-table-column>
                    <el-table-column align="center"
                      prop="sjmc"
                      label="试卷名称"
                      width="180">
                    </el-table-column>
                    <el-table-column align="center"
                      prop="fb"
                      label="是否发布"
                      width="180" :formatter="paperStatesFormat">
                    </el-table-column>
                    <el-table-column align="center" sortable
                                     prop="createTime"
                                     label="创建时间">
                    </el-table-column>
                    <el-table-column align="center" sortable
                      prop="updateTime"
                      label="更新时间">
                    </el-table-column>
                  </el-table>
                  <pagination
                    :autoScroll="false"
                    v-show="examsTableData.length > 0"
                    :limit.sync="examsGridForm.pageSize"
                    :page.sync="examsGridForm.pageNum"
                    :total="examsData.length"
                    @pagination="getExamsGridData"
                  />
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>

</template>

<script>
import { listSubject } from "@/api/etp/subject/subject";
import {  subjectAnalyzeData } from "@/api/etp/analysis/subjectsAnalysis";
import LineChart from "@/views/dashboard/LineChart";
import echarts from "echarts";
export default {
  name: "subjectsAnalysis",
  components:{ LineChart },
  data() {
    return {
      isShowTooltip:false,
      arr: [],
      defaultProps: {
        children: 'children',
        label: 'tg'
      },

      courseloading:false,
      examloading:false,
      chart:null,
      subjectTypeData:[],
      currentNodekey: "", //默认选中的节点树
      expanded:['dx'],


      coursesTableData:[],
      coursesData:[],         // 课程总数据
      coursesGridForm:{
        pageNum: 1,
        pageSize: 10,
      },
      examsTableData:[],
      examsData:[],           // 考试总数据
      examsGridForm:{
        pageNum: 1,
        pageSize: 10,
      },
    }
  },
  mounted() {
    this.listSubject();

  },
  methods: {
    /** 格式化处理函数 */
    courseStatesFormat(data){
      return data.states == '0'? '暂存':'已发布'
    },
    paperStatesFormat(data){
      return data.fb == '0'? '暂存':'已发布'
    },

    /** 初始化图表 */
    initChart(){
      this.chart = echarts.init(this.$refs.chart, 'macarons');
      for (let i = 0; i < 5; i++) {
        this.subjectTypeData.push(this.arr[i].children.length)
      }
      this.chart.setOption({
        tooltip: {
          //鼠标悬停提示内容
          trigger: 'axis', // 触发类型，默认数据触发，可选为：'axis' item
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
            label:'cross',
            show:true
          },
        },
        xAxis: {
          type: 'category',
          data: ['单选题', '复选题', '判断题', '填空题', '问答题']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.subjectTypeData,
            type: 'line'
          }
        ]
      },true);
    },

    /** 获取试题列表 */
    listSubject() {
      listSubject().then(res => {
        let dxObject = {type: '1',id:'dx', tg: "单选题", children: []};
        let fxObject = {type: '2',id:'fx', tg: "复选题", children: []};
        let pdObject = {type: '3',id:'pd', tg: "判断题", children: []};
        let tkObject = {type: '4',id:'tk', tg: "填空题", children: []};
        let zgObject = {type: '5',id:'zg', tg: "问答题", children: []};
        let arr = [dxObject, fxObject, pdObject, tkObject, zgObject];
        let rows = res.rows;
        rows.forEach(item => { // 服务器返回的所有数据
          arr.forEach(i => {  // 五大类
            if (item.lx == i.type) {
              i.children.push(item)
            }
          })
        })
        this.arr = arr;
        this.$nextTick(_ =>{
          this.currentNodekey = this.arr[0].children[0].id;
          this.$refs.tree.setCurrentKey(this.currentNodekey);
          this.getAnalysisData(this.currentNodekey);
        })
        this.initChart()
      })
    },

    /** 树形组件节点点击监听 */
    handleNodeClick(data) {
      this.currentNodekey = "";
      this.expanded = [];
      if (data.id){
        this.getAnalysisData(data.id);
      }
    },

    getCourseGridData(){
      let startIndex = (this.coursesGridForm.pageNum - 1) * this.coursesGridForm.pageSize;
      let endIndex =
        (this.coursesGridForm.pageSize * this.coursesGridForm.pageNum < this.coursesData.length)
          ? this.coursesGridForm.pageSize * this.coursesGridForm.pageNum:this.coursesData.length;
      this.coursesTableData = [];
      for (let i = startIndex; i < endIndex; i++)
        this.coursesTableData.push(this.coursesData[i]);
    },

    getExamsGridData(){
      let startIndex = (this.examsGridForm.pageNum - 1) * this.examsGridForm.pageSize;
      let endIndex =
        (this.examsGridForm.pageSize * this.examsGridForm.pageNum < this.examsData.length)
          ? this.examsGridForm.pageSize * this.examsGridForm.pageNum:this.examsData.length;
      this.examsTableData = [];
      for (let i = startIndex; i < endIndex; i++)
        this.examsTableData.push(this.examsData[i]);
    },

    /** 获取分析数据 */
    getAnalysisData(id){
      this.courseloading = true;
      this.examloading = true;
      subjectAnalyzeData(id).then(res => {
        let courses = res.data.tCurriculaTable;
        let exams = res.data.tExaminationsTable;
        /*this.curriculumList = response.rows;
        this.total = response.total;
        this.loading = false;*/
        /*coursesTableData:[],
          coursesData:[],         // 课程总数据
          coursesGridForm:{
          pageNum: 1,
            pageSize: 10,
        },*/
        this.coursesData = courses.rows;
        this.coursesTableData = [];
        for (let i = 0; i < (this.coursesGridForm.pageSize > this.coursesData.length
          ? this.coursesData.length:this.coursesGridForm.pageSize); i++) {
          this.coursesTableData.push(this.coursesData[i]);
        }

        this.examsData = exams.rows;
        this.examsTableData = [];
        for (let i = 0; i < (this.examsGridForm.pageSize > this.examsData.length
          ? this.examsData.length:this.examsGridForm.pageSize); i++) {
          this.examsTableData.push(this.examsData[i]);
        }


        this.courseloading = false;
        this.examloading = false;
      })
    }

  }
}
</script>

<style scoped>
.span-ellipsis{
  display: block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.custom-tree-node {
  /*flex: 1;
  display: flex;
  align-items: center;*/
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.charts {
  width: 100%;
  height: 500px;
  border: 1px solid #ccc;
  margin-top: 10px;
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
</style>
