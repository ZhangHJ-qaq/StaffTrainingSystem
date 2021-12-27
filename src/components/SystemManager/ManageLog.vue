<template>
  <div>
    <el-table
        :data="logInfo"
        style="width: 100%">
      <el-table-column
          prop="logID"
          label="日志ID">
      </el-table-column>
      <el-table-column
          prop="time"
          label="时间">
      </el-table-column>
      <el-table-column
          prop="operator"
          label="操作">
      </el-table-column>
      <el-table-column
          prop="content"
          label="内容">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "ManageLogs",
  data() {
    return {
      logInfo: []
    }
  },
  created() {//初始化操作
    this.axios.get('/admin/all_log').then(resp => {
      if (resp.status === 200) {
        this.logInfo.push(resp.data);
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