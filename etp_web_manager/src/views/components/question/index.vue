<template>
  <el-form ref="questionForm" :model="question" :rules="rules" label-width="80px">
    <el-card>
      <el-row :gutter="15">
        <el-col :span="8">
          <el-form-item label="题目类型" prop="type">
            <el-select v-model="question.type" placeholder="请选择题目类型" clearable :style="{width: '100%'}">
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
            <el-select v-model="question.level" placeholder="请选择难度等级" clearable :style="{width: '100%'}">
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
            <el-checkbox v-model="question.isTrain" true-label="1" false-label="0">练习</el-checkbox>
            <el-checkbox v-model="question.isExam" true-label="1" false-label="0">考试</el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="题目分类" prop="category" >
            <el-cascader v-model="question.category"
              clearable
              collapse-tags
              :style="{width: '100%'}"
              :options="categoryOptions"
              :props="{ checkStrictly: true, value: 'id', label: 'name', multiple: true }"
              filterable
              ></el-cascader>
          </el-form-item>
        </el-col>
      </el-row>


      <el-form-item label="题目" prop="content">
        <editor v-model="question.content" :min-height="192"/>
      </el-form-item>

      <el-form-item label="解析" prop="analysis">
        <el-input v-model="question.analysis" type="textarea" placeholder="请输入内容" />
      </el-form-item>
    </el-card>

    <!-- 6.组合题目 —— 子题目编辑区域 -->
    <template v-if="this.question.type === '6'" >
      <el-divider content-position="center"/>
<!--      <el-card v-if="childQuestions.length === 0" style="text-align: center">-->
<!--        <span>请点击下面的按钮添加子题目</span>-->
<!--        <el-button type="primary" style="width: 100%" icon="el-icon-plus" size="mini"-->
<!--                   @click="handleAddQuestionChild">添加子题目</el-button>-->
<!--      </el-card>-->

<!--      <div v-else  style="display: flex">-->
        <div style="display: flex">
        <!--子题目列表 -->
        <div class="qu_right" >
          <el-card v-for="(child, index) in childQuestions"
                   style="margin-bottom: 5px">
            <el-row :gutter="15">
              <el-col :span="8">
                <el-form-item label="题目类型" prop="type">
                  <el-select v-model="child.type" placeholder="请选择题目类型">
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

            <el-form-item label="题目" prop="content">
              <editor v-model="child.content" :min-height="192"/>
            </el-form-item>

            <el-form-item label="解析" prop="analysis">
              <el-input v-model="child.analysis" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-card>
        </div>

        <!-- 子题目导航 -->
        <el-card class="qu_left">
          <el-button type="primary" style="width: 100%" icon="el-icon-plus" size="mini"
                     @click="handleAddQuestionChild">添加子题目</el-button>
          <el-table :data="childQuestions" :show-header="false" style="width: 100%; max-height: 350px;">
            <el-table-column type="index" width="20" />
            <el-table-column label="题干内容" prop="content" min-width="90%">
              <template slot-scope="scope">
                <span style="font-style: normal">[{{typeFormat(scope.row)}}]</span>
                {{scope.row.content.replace(/\<[^>]*\>(([^<])*)/g, function() {return arguments[1];})}}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="50px">
              <template slot-scope="scope">
                <el-tooltip class="item" effect="dark" content="删除" placement="top">
                  <el-button
                    type="text"
                    @click.native.prevent="handleDeleteQuestionSub(scope.row)"
                    v-hasPermi="['etp:sign:remove']"
                  ><i class="el-icon-delete" style="background: white; color: red"></i></el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </template>
  </el-form>
</template>
<script>
import { categoryTreeselect } from "@/api/question/question";


export default {
  name: "EditQuestion",
  inheritAttrs: false,
  components: {},
  props: {
    data: {
      type: Object
    }
  },
  model: {
    prop: 'question',
    event: 'questionChange'
  },
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
        content: [
          { required: true, message: "题干内容不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "题目类型不能为空", trigger: "change" }
        ],
        level: [
          { required: true, message: "难度等级不能为空", trigger: "change" }
        ],
        category: [
          { required: true, message: "题目分类不能为空", trigger: "change", type: "array" }
        ]
      },
      question: this.data,
      childQuestions: this.data.questionChildList,
    }
  },
  computed: {

  },
  watch: {
    childQuestions: {
      handler: function (newVal, oldVal) {
        console.log(newVal)
      },
      deep: true
    }
  },
  created() {
    this.getDicts("question_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("question_sub_type").then(response => {
      this.subTypeOptions = response.data;
    });
    this.getDicts("question_level").then(response => {
      this.levelOptions = response.data;
    });
    categoryTreeselect().then(res => {
      this.categoryOptions = res.data
    })
  },
  mounted() {
    // window.addEventListener("scroll", function () {
    //   var styleNav = document.getElementById("nav")
    //
    //   if (scrollTop >= 350) {
    //
    //     document.getElementById("nav").style.position = "fixed"
    //
    //   }else {
    //     document.getElementById("nav").style.removeProperty("position")
    //   }
    // }, true)
  },
  methods: {
    onOpen() {
    },
    onClose() {
      this.$refs['elForm'].resetFields()
    },
    close() {
      this.$emit('update:visible', false)
    },
    // 题目类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    /** 子题目库添加按钮操作 */
    handleAddQuestionChild() {
      let obj = {};
      obj.content = null;
      obj.contentDisplay = null;
      obj.type = "1";
      obj.level = "1";
      obj.answerList = null;
      obj.analysis = null;
      this.childQuestions.push(obj);
    },
    /** 子题目库删除按钮操作 */
    handleDeleteQuestionSub(index) {
      this.$confirm('是否确认删除该子题目?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.childQuestions.splice(index, 1);
      }).catch(() => {});

    },
    /** 更改子题目 */
    handChange(index, child) {
      this.childQuestions[index] = child
    },
    validateHandle() {
      let status = null;
      this.$refs["questionForm"].validate((val) => {
        status = val
      });

      return status
    }
  }
}

</script>
<style>
.qu_right{
  width: calc(100% - 370px);
  flex-grow: 1;
  margin-bottom: 30px;
  padding-right: 20px;
}

.qu_left{
  width: 350px;
}

</style>
