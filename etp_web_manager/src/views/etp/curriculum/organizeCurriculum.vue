<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="登记课程信息" name="basic">
        <baseInfoForm ref="baseInfoForm" :info="info"/>
      </el-tab-pane>
      <el-tab-pane label="组织课程内容" name="content">
        <div v-if="unitArr.length === 0">
          <el-alert
            title="温馨提示"
            type="warning"
            description="您还没有设置课程相关内容!"
            show-icon>
          </el-alert>
        </div>
        <el-collapse ref="coll" v-if="unitArr.length !== 0" style="border: 0">
          <el-collapse-item
            v-for="(item,index) in unitArr"
            :key="item.uuid"
            :name="item.uuid">
            <template slot="title" class="collapse-title">
              <el-input
                @focus="titleFocus"
                @blur="titleBlur"
                v-on:keyup.enter.native="enter(item)"
                v-model="item.uname"
                style="width: 30%"
                placeholder="请输入章节名称"
                maxlength="100"
                show-word-limit
                clearable>
              </el-input>

              <i style="margin-left:20px;"
                 class="el-icon-delete etp_font_color_red etp_font_size"
                 @click.stop @click="del(item)"
              />
            </template>
            <div>
              <el-button size="mini" type="primary" plain @click="addKJ(item)">+ 添加课件</el-button>
<!--              <el-button size="mini" type="primary" plain @click="addLx(item)">+ 添加练习</el-button>-->
            </div>

            <!--内容展示-->
            <el-divider content-position="left">课程章节内容</el-divider>
            <div>
              <el-table
                :data="item.sourceArr"
                style="width: 100%">

                <el-table-column
                  prop="source_name"
                  label="名称"
                  align="center"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="source_type"
                  align="center"
                  label="类型">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.source_type == 'kj'?'success':''">
                      {{ scope.row.source_type == 'kj' ? '课件' : '试题' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button type="text" style="padding:0"
                               :disabled="scope.$index == 0"
                               @click="moveUpward(item.sourceArr,scope.row, scope.$index)">
                      <i class="el-icon-top  etp_font_size"></i></el-button>
                    <el-button type="text" style="padding:0"
                               :disabled="(scope.$index + 1) == item.sourceArr.length"
                               @click="moveDown(item.sourceArr,scope.row, scope.$index)">
                      <i class="el-icon-bottom  etp_font_size"></i></el-button>
                    <el-button type="text" style="padding:0"
                               @click="delItem(item.sourceArr,scope.row, scope.$index)">
                      <i class="el-icon-delete etp_font_color_red etp_font_size"></i>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-collapse-item>
        </el-collapse>

        <div style="margin-top: 20px">
          <el-button size="small" type="primary" plain @click="addUNit">+ 增加章节</el-button>
        </div>

        <!--   表单提交     -->
        <el-form label-width="100px" style="float: right">
          <el-form-item style="margin-left:100px;margin-top:10px;">
            <el-button type="primary" @click="submitForm('0')" :disabled="disabled">提交</el-button>
            <el-button type="success" @click="submitForm('99')" :disabled="disabled">提交并发布</el-button>
            <el-button @click="close()">返回</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>


    <el-dialog
      title="增加课件"
      :visible.sync="dialogVisibleKJ"
      width="1000px"
      :close-on-click-modal="false">
      <addKJ ref="addKJ"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleKJ = false">取 消</el-button>
        <el-button type="primary" @click="addKJConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="增加练习"
      :visible.sync="dialogVisibleST"
      width="1000px"
      :close-on-click-modal="false">
      <addST ref="addST"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleST = false">取 消</el-button>
        <el-button type="primary" @click="addLXConfirm">确 定</el-button>
      </span>
    </el-dialog>

  </el-card>
</template>

<script>
import { getCurriculum, getCurriculumContent, organizatized } from "@/api/etp/curriculum/curriculum"
import baseInfoForm from "@/views/etp/curriculum/components/baseInfoForm";
import addKJ from "@/views/etp/curriculum/components/addKJ";
import addST from "@/views/etp/curriculum/components/addST";

export default {
  name: "OrganizeCurriculum",
  components: {
    baseInfoForm, addKJ, addST
  },
  data() {
    return {
      dialogVisibleKJ: false,
      dialogVisibleST: false,
      disabled: false,

      btnFlag: false,
      // 选中选项卡的 name
      activeName: "content",
      // 表信息
      tables: [],
      // 表列信息
      cloumns: [],
      // 字典信息
      dictOptions: [],
      // 菜单信息
      menus: [],
      // 表详细信息
      info: {},
      unitArr: [],
      idFlag: '',
      type: '',
    }
  },
  created() {
    const cid = this.$route.params && this.$route.params.cid;
    this.getCurriculum(cid);
    this.getCurriculumContent(cid);
  },
  methods: {

    /** 增加课件*/
    addKJ(item) {
      this.idFlag = item.uuid
      this.type = 'kj';
      this.dialogVisibleKJ = true;
      this.$nextTick(_ => {
        this.$refs.addKJ.getList();
        this.$refs.addKJ.getTags();
      })
    },
    /** 增加课件确认按钮 */
    addKJConfirm() {
      let arr = this.$refs.addKJ.collectDatasByIdsArr();
      if (arr.length > 0) {
        this.unitArr.forEach(unit => {
          if (unit.uuid == this.idFlag) {
            this.delRepeat(arr, unit);
            unit.sourceArr = [...unit.sourceArr, ...arr]
          }
        })
        this.dialogVisibleKJ = false;

      } else {
        this.$message.warning("未选择课件！")
      }
    },

    /** 增加练习*/
    addLx(item) {
      this.idFlag = item.uuid
      this.type = 'lx';
      this.dialogVisibleST = true;
      this.$nextTick(_ => {
        this.$refs.addST.getList();
        this.$refs.addST.getTags();
      })

    },
    /** 增加练习确认按钮 */
    addLXConfirm() {
      let arr = this.$refs.addST.collectDatasByIdsArr();
      if (arr.length > 0) {
        this.unitArr.forEach(unit => {
          if (unit.uuid == this.idFlag) {
            this.delRepeat(arr, unit);
            unit.sourceArr = [...unit.sourceArr, ...arr]
          }
        })

        this.dialogVisibleST = false
      } else {
        this.$message.warning("未选择练习！")
      }
    },

    /** 递归去重*/
    delRepeat(arr, unit) {
      arr.forEach((item, index) => {
        unit.sourceArr.forEach(source => {
          if (item.id == source.id) {
            setTimeout(() => {
              this.$message.warning(item.source_name + " 已存在！");
            }, 150)
            arr.splice(index, 1);
            this.delRepeat(arr, unit);
          }
        })
      })
    },

    /** 表单提交*/
    submitForm(states) {
      let flag = false;
      let baseInfoForm = this.$refs.baseInfoForm.info;
      let tagValue = this.$refs.baseInfoForm.tagValue;
      this.addTags(baseInfoForm, tagValue)
      //发布状态
      baseInfoForm.states = states;
      let unitFormArr = this.unitArr;
      if (unitFormArr.length === 0) {
        this.$message.error("未设置章节禁止提交！");
        return;
      }
      unitFormArr.forEach((item, index) => {
        if (item.uname === '' || item.uname == null || item.sourceArr.length === 0) {
          this.$message.error("请检查章节名称或者课程内容是否完整！");
          flag = true;
        } else {
          item.sourceArr.map((item, index) => {
            item['sort'] = index + 1
            return item;
          })
        }
      })
      if (flag) return;

      baseInfoForm['list'] = unitFormArr;
      organizatized(baseInfoForm).then(res => {
        if (res.code == 200) {
          if (states == '0') {
            this.$message.success("课程保存成功！");
          } else {
            this.$message.success("课程发布成功！");
          }
          this.disabled = true;
        }
      })
    },
    /** 获取课程基本信息*/
    getCurriculum(id) {
      getCurriculum(id).then(res => {
        this.info = res.data;
      })
    },
    /** 获取课程内容*/
    getCurriculumContent(id) {
      getCurriculumContent(id).then(res => {
        if (res.code == 200) {
          this.unitArr = res.data;
        }
      })
    },
    /** 增加章节目录*/
    addUNit() {
      let sort = this.unitArr.length + 1
      let uuid = this.guid();
      this.unitArr.push({uuid: uuid, uname: '', sort: sort, sourceArr: []})
    },
    /** 删除目录*/
    del(item) {
      this.$confirm(`是否移除此目录`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let uuid = item.uuid;
        this.unitArr = this.unitArr.filter(i => {
          return i.uuid != uuid;
        })
        this.$message({
          type: 'success',
          message: '移除成功！'
        });
      }).catch();
    },
    /** 用于生成uuid */
    guid() {
      return (this.S4() + this.S4() + "-" +
        this.S4() + "-" + this.S4() + "-" +
        this.S4() + "-" + this.S4() + this.S4() + this.S4());
    },
    S4() {
      return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    },
    /** 修改el折叠面板默认事件 */
    titleFocus() {
      this.collClick = this.$refs.coll.setActiveNames
      this.$refs.coll.setActiveNames = () => {
      }
    },
    titleBlur() {
      this.$refs.coll.setActiveNames = this.collClick
    },
    enter(item) {
      this.$notify({
        title: '操作成功',
        message: '请展开此目录设置相关内容',
        type: 'success',
        duration: 0
      });
    },
    /** 上移*/
    moveUpward(data, row, index) {
      if (index > 0) {
        let upData = data[index - 1];
        data.splice(index - 1, 1);
        data.splice(index, 0, upData);
      } else {
        this.$message({
          message: '已经是第一条，上移失败',
          type: 'warning'
        });
      }
    },
    /** 下移*/
    moveDown(data, row, index) {
      if ((index + 1) == data.length) {
        this.$message({
          message: '已经是最后一条，下移失败',
          type: 'warning'
        });
      } else {
        let downData = data[index + 1];
        data.splice(index + 1, 1);
        data.splice(index, 0, downData);
      }
    },
    /** 删除课件或者试题*/
    delItem(data, row, index) {
      let id = row.id;
      data.forEach((item, index) => {
        if (item.id == id) {
          data.splice(index, 1);
        }
      })
    },
    /** 返回按钮 */
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/etp/curriculum/curriculum", query: {t: Date.now()}})
    }
  }
}
</script>

<style scoped>
.etp_font_size {
  font-size: 16px;
}

.etp_font_color_red {
  color: red;
}
</style>
