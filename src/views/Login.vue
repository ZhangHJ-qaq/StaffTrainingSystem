<template>
  <div id="login">
    <div class="loginToHome">
      <el-form
          ref="ruleForm"
          :model="ruleForm"
          :rules="rules"
          status-icon
          label-width="80px"
          class="loginForm">
        <h3>登录</h3>
        <el-form-item
            label="用户名"
            prop="username">
          <el-input
              type="text"
              v-model="ruleForm.username"
              auto-complete="off"
              placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item
            label="密码"
            prop="password"
        >
          <el-input
              type="password"
              v-model="ruleForm.password"
              auto-complete="off"
              placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="登陆身份" prop="identity">
          <el-radio-group v-model="ruleForm.identity">
            <el-radio label="admin">普通管理员</el-radio>
            <el-radio label="superadmin">超级管理员</el-radio>
            <el-radio label="reader">普通读者</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button
            class="homeBut"
            type="primary"
            plain
            @click="submitForm('ruleForm')"
            :loading="logining">登录</el-button>
        <el-button
            class="loginBut"
            type="primary"
            plain
            @click="resetForm('ruleForm')">重置</el-button>
       </el-form>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      logining: false,
      ruleForm: {
        username: '',
        password: '',
        identity: '',
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        identity: [
          { required: true, message: '请选择身份', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      let a = new Date().getTime() + 100000000;
      localStorage.setItem('exp', a.toString())
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/auth/login', {
            username: this.ruleForm.username,
            password: this.ruleForm.password,
          })
              .then(resp => {
                if (resp.status === 200 && resp.headers.hasOwn('token')) {
                  //更新 vuex 的 state的值, 必须通过 mutations 提供的方法才可以
                  // 通过 commit('方法名') 就可以出发 mutations 中的指定方法
                  if ((this.ruleForm.identity === 'reader' &&
                          (resp.data.message === 'undergraduate' || resp.data.message === 'postgraduate' ||
                              resp.data.message === 'teacher')) ||
                      this.ruleForm.identity === resp.data.message) {
                    this.$store.commit({
                      type: 'doLogin',
                      token: resp.headers.token
                    });
                    this.$router.push({path: '/home'});
                    this.$message.success('登陆成功')
                  } else {
                    this.$message.error('与所选身份不一致，请重新登陆')
                  }
                }
                else{
                  this.$message.success(resp.data.message);
                }
              }).catch(error => {
            this.$message.error(error.response.data.message)
          })
        } else {
          this.$message.error('请正确填写表单')
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>
<style>
#login{
  width: 30%;
  margin-left: 35%;
}
a {
  text-decoration: none;
  color: lightskyblue;
  font-weight: bold;
  cursor: pointer;
}
.reminder {
  margin-top: 10px;
  font-size: smaller;
  color: gray;
}
</style>
