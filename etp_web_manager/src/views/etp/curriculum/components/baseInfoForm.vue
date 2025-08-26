<template>
  <el-form ref="basicInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="24">
        <el-form-item label="课程名称" prop="mc">
          <el-input placeholder="请输入课程名称" v-model="info.mc" maxlength="100" show-word-limit/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="标签" prop="tag">
          <tagsEdit widthSty="100%"
                    :parentValue="tagValue"
                    :selectOptions="tagOptions"
                    @selectTags="havaTags">

          </tagsEdit>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="课程封面" prop="fmt">
          <imageUpload :limit="1" :uploadUrl="'/commom/upload/fastdfs'" v-model="info.fmt"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="开放程度" prop="kfcd">
          <el-radio-group v-model="kfcd">
            <el-radio :label="0">完全公开</el-radio>
            <el-radio :label="1">限定用户</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="课程描述" prop="ms">
          <el-input type="textarea" placeholder="请输入课程描述" :rows="2" v-model="info.ms" maxlength="300" show-word-limit></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="3" placeholder="请输入课程备注" v-model="info.remark" maxlength="300" show-word-limit></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
import imageUpload from "@/components/ImageUpload";
import tagsEdit from "@/components/tags/tagsEdit";
import { listTagRelate } from "@/api/etp/tag/tag";

export default {
  name: "baseInfoForm",
  components: {
    imageUpload,
    tagsEdit
  },
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  mounted() {
    this.getTags();
    setTimeout(() => {
      if (this.info.tags) {
        this.tagValue = this.info.tags.split(",");
      } else {
        this.tagValue = [];
      }
    }, 150)
  },
  watch: {
    info: {
      handler(newValue, oldValue) {
        this.kfcd = parseInt(newValue.kfcd);
      },
      deep: true // 深度监视，适用于当一个对象内部属性被改变时
    },
    kfcd(newValue, oldValue) {
      this.info.kfcd = newValue;
    }
  },
  data() {
    return {
      kfcd: 1,
      rules: {
        mc: [
          {required: true, message: "请输入课程名称", trigger: "blur"}
        ],
        fmt: [
          {required: true, message: "请设置封面图", trigger: "blur"}
        ],
        kfcd: [
          {required: true, message: "请设置开放程度", trigger: "blur"}
        ],
      },
      // 标签用到的
      tagValue: [],
      tagOptions: [],
    }
  },
  methods: {
    /** 标签相关 */
    getTags() {
      listTagRelate({modelType: "KC"}).then(response => {
        this.tagOptions = response.data;
      });
    },
    havaTags(val) {
      this.tagValue = val
    },
  }

}
</script>

<style scoped>

</style>
