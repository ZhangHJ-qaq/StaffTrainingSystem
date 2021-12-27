<template>
  <div class="home">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" :router="true">
      <el-submenu index="2">
        <template slot="title">我的账户</template>
        <el-menu-item @click="logout" v-if="isLogin">登出</el-menu-item>
        <el-menu-item index="/login" v-else>登录</el-menu-item>
        <el-menu-item index="/home/user" v-if="isStudent">个人信息</el-menu-item>
      </el-submenu>
      <el-menu-item index="/home/admin" v-if="isAdmin">
        管理员
      </el-menu-item>
      <div class="userInfo">
        <span><i class="el-icon-user"></i>{{this.identity}}{{this.username}}</span>
        <br>
      </div>
    </el-menu>
    <router-view class="upload-form"/>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'Home',
  data() {
    return {
      activeIndex: '',
      isLogin: localStorage.getItem('login'),
      username: localStorage.getItem('login') ? localStorage.getItem('username') : '未登录',
      isAdmin: localStorage.getItem('login') &&
          (localStorage.getItem('role') === 'admin' || localStorage.getItem('role') === 'superadmin'),
      isStudent:localStorage.getItem('login') &&
          (localStorage.getItem('role') === 'undergraduate' ||
              localStorage.getItem('role') === 'teacher' ||
              localStorage.getItem('role') === 'postgraduate'),
      identity: localStorage.getItem('login') ? (localStorage.getItem('role') + ': ') : '',
    };
  },
  methods: {
    logout(){
      this.$store.commit("doLogout");
      this.$router.go(0)
    }
  },
  mounted: function () {
    if (/^\/home\/admin*/.test(this.$route.path)) {
      this.activeIndex = '/home/admin'
    } else if (/^\/home\/show*/.test(this.$route.path)) {
      this.activeIndex = '/home/show'
    } else if (/^\/home\/user*/.test(this.$route.path)) {
      this.activeIndex = '/home/user'
    }
    if (parseInt(localStorage.getItem('exp')) < ((new Date().getTime())/1000)) {
      this.$store.commit('doLogout')
      this.$router.go(0)
    }
  }
}
</script>

<style>
.home {
  margin: 20px 5%;
}

.home-link {
  text-decoration: none;
}

.upload-form {
  margin: 40px 0 0 0;
}

.is-active {
  font-weight: bold;
}
.userInfo {
  float: right;
  color: gray;
  font-size: x-small;
}
</style>
