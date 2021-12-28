<template>
  <div>
    <el-form :model="ruleForm" status-icon label-width="100px" class="demo-ruleForm">
      <el-form-item label="学号" prop="participateID">
        <el-input v-model.number="ruleForm.participateID"></el-input>
      </el-form-item>
      <el-form-item label="分数" prop="score">
        <el-input v-model.number="ruleForm.score"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="resetForm()">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ruleForm: [{
        participateID: '',
        score:''
      }]
    };
  },
  methods: {
    submitForm() {
      this.$axios.post('/instructor/register_score',this.ruleForm).then(data => {
            if(data.status==200) {
              this.$message.success('录入成功')
            }else {
              this.$message.error('录入失败')
            }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    resetForm() {
      this.ruleForm=[]
    }
  }
}
</script>

<style scoped>
.el-form{
  width: 40%;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}
</style>