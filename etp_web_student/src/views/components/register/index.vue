
<template>
  <el-dialog
    title="企业注册"
    :visible.sync="registerDialog"
    width="43%"
    :before-close="handleClose">
    <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="企业名称" prop="tenantName">
            <el-input v-model="registerForm.tenantName" placeholder="请输入企业名称" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="linkman">
            <el-input v-model="registerForm.linkman" placeholder="请输入联系人" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电话" prop="phonenumber">
            <el-input v-model="registerForm.phonenumber" placeholder="请输入电话" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="E-mail" prop="email">
            <el-input v-model="registerForm.email" placeholder="请输入E-mail" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="登录账号" prop="userName">
            <el-input v-model="registerForm.userName" placeholder="请输入登录账号" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="registerForm.nickName" placeholder="请输入昵称" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerForm.password" placeholder="请输入密码" clearable show-password></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" placeholder="请再次确认密码" clearable show-password></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="验证码" prop="code" v-if="captchaOnOff">
            <el-input
              v-model="registerForm.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 63%"
            >
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="$emit('getcode')" class="login-code-img"/>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="说明" prop="remark">
            <el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="registerForm.remark" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="温馨提示">
            <div style="color:#1890FF;font-weight: bold;">注册成功后，即时开通免费版，如需要开通会员服务，请咨询客服或在说明中备注需求！</div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
         <el-button @click="close">取 消</el-button>
         <el-button type="primary" @click="submit">确 定</el-button>
      </span>
  </el-dialog>
</template>
<script>
import { register } from "@/api/login";

export default {
  name: "Register",
  props: {
    uuid: String,
    captchaOnOff:{
      type: Boolean,
      default: true
    },
    codeUrl: String
  },
  data() {
    return {
      registerDialog: false,
      registerForm: this.initForm(),
      registerRules: {
        tenantName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' },
        ],
        //电话合理性验证
        phonenumber: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3|4|5|7|8][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        //email合理性验证
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/, message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请输入确认密码', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    open() {
      this.registerDialog = true
    },
    close() {
      this.registerDialog = false
    },
    handleClose(done) {
      done()
    },
    initForm(){
      return {
        tenantName: '',
        linkman: '',
        phonenumber: '',
        email: '',
        userName: '',
        nickName: '',
        password: '',
        confirmPassword: '',
        code: '',
        uuid: this.uuid,
        remark: '',
      }
    },
    submit() {
      this.$refs.registerFormRef.validate(valid => {
        if (valid) {
          console.log('submit',this.registerForm)
          //如果密码和确认密码不一致，请重新输入
          if (this.registerForm.password !== this.registerForm.confirmPassword) {
            this.$message({
              message: '密码和确认密码不一致，请重新输入',
              type: 'warning',
            })
            return
          }
          register(this.registerForm).then((res) => {
            this.$message({
              message: '注册成功，请登录管理端发布课件试卷等资源',
              type: 'success',
            })
            this.close()
            this.registerForm = this.initForm()
          }).catch((err) => {
            this.$message({
              message: '注册失败，' + err.msg,
              type: 'error',
            })
          })
        }
      });
    },
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-dialog__body {
  padding: 20px 20px 0 20px;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
</style>
