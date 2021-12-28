<template>
  <div>
    <el-descriptions class="margin-top" title="个人信息" :column="3" :size="size" border>
      <template slot="extra">
        <el-button type="primary" size="small">编辑</el-button>
      </template>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          ID
        </template>
        1
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        kooriookami
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          性别
        </template>
        女
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          手机号
        </template>
        18100000000
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          部门
        </template>
        苏州市
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          职位
        </template>
        <el-tag size="small">学员</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          联系地址
        </template>
        江苏省苏州市吴中区吴中大道 1188 号
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          联系地址
        </template>
        江苏省苏州市吴中区吴中大道 1188 号
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          联系地址
        </template>
        江苏省苏州市吴中区吴中大道 1188 号
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  name: "EditUserInfos",
  data () {
    return {
      userInfos: [
        // {
        // employeeID:string,
        // name:string,
        // gender:string,
        // arrivalTime:string,
        // email:string,
        // phoneNumber:string,
        // role:string,
        // department:string,
        // deptID:int
        // }
      ]
    }
  },
  created() {//初始化操作
    this.axios.get('/student/my_info').then(resp => {
      if (resp.status === 200) {
        this.userInfos.push(resp.data);
      } else {
        this.$message.error(resp.data.message);
      }
    }).catch(err => {
      this.$message.error(err.response.data.message)
    })
  }
}
</script>

<style scoped>

</style>