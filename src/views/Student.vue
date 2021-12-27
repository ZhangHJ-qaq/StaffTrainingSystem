<template>
  <el-row>
    <el-col class="menu">
      <el-menu
          :default-active="$route.path"
          :router="true"
          class="el-menu-vertical-demo">
        <el-menu-item index="/student/editUserInfos">
          <i class="el-icon-menu"></i>
          <span slot="title">个人信息</span>
        </el-menu-item>
        <el-menu-item index="/student/viewCourseInfos">
          <i class="el-icon-document"></i>
          <span slot="title">课程信息</span>
        </el-menu-item>
        <el-menu-item index="/student/viewScores">
          <i class="el-icon-document-checked"></i>
          <span slot="title">培训信息</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col class="content">
      <router-view/>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "Student",
  // mounted() {
  //   if (!localStorage.getItem('login')) {
  //     this.$message.error('请先登录')
  //     this.$router.push('/login')
  //   } else if (localStorage.getItem('exp') < ((new Date().getTime())/1000)) {
  //     this.$message.error('登录过期，请先登录')
  //     this.$router.push('/login')
  //   }
  // }
}
</script>

<style>
.menu {
  width: 12%;
  float: left;
}
.content {
  float: right;
  width: 85%;
}
.router-link-active {
  color: deepskyblue;
}
.admin-link {
  text-decoration: none;
  color: inherit;
}
</style>
