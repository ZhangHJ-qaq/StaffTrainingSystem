<template>
  <div>
    <el-table
        :data="currentCourseInfos"
        stripe
        style="width: 100%">
      <el-table-column
          prop="courseID"
          label="课程ID">
      </el-table-column>
      <el-table-column
          prop="courseID"
          label="课程名称">
      </el-table-column>
      <el-table-column
          prop="instructorName"
          label="教员姓名">
      </el-table-column>
      <el-table-column
          prop="startDate"
          label="开设时间">
      </el-table-column>
      <el-table-column
          prop="participateID"
          label="课程学号">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "ViewCourseInfos",
  data() {
    return {
      currentCourseInfos: [
        // {
        //   participateID:'',
        //   courseID:'',
        //   courseName:'',
        //   startDate:'',
        //   instructorName:''
        // }
      ]
    }
  },
  created() {//初始化操作
    this.axios.get('/student/my_current_courses').then(resp => {
      if (resp.status === 200) {
        this.currentCourseInfos.push(resp.data);
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