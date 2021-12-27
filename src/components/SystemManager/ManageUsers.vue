<template>
  <div>
    <el-button style="float: right;margin-left: 10px" @click="search">查询</el-button>
    <el-form :model="searchForm" :inline="true" style="float: right">
      <el-form-item>
        <el-input
            v-model="searchForm.employeeID"
            placeholder="输入ID搜索"/>
      </el-form-item>
      <el-form-item>
        <el-input
            v-model="searchForm.name"
            placeholder="输入姓名搜索"/>
      </el-form-item>
    </el-form>

    <el-button style="float: left" @click="dialogTableVisible = true">添加用户</el-button>
    <!-- 添加用户弹框 -->
    <el-dialog
        title="添加用户"
        @close="addDialogClose"
        :visible.sync="dialogTableVisible"
        :close-on-click-modal="false"
    >
      <!-- 添加用户的表单 -->
      <el-form
          style="margin: 50px"
          ref="addFormRef"
          :rules="rulesAddUser"
          :model="addUser"
          label-width="100px"
          label-position="left"
          inline class="demo-table-expand2">
        <el-form-item label="姓名">
          <el-input v-model="addUser.name"></el-input>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="addUser.deptID" placeholder="请选择所属部门">
            <el-option label="总裁办公室" :value="1"></el-option>
            <el-option label="研究院" :value="2"></el-option>
            <el-option label="人力资源部门" :value="3"></el-option>
            <el-option label="产品研发部门" :value="4"></el-option>
            <el-option label="营销部门" :value="5"></el-option>
            <el-option label="推广部门" :value="6"></el-option>
            <el-option label="人事管理部门" :value="7"></el-option>
            <el-option label="测试部门" :value="8"></el-option>
            <el-option label="策划部门" :value="9"></el-option>
            <el-option label="技术部门" :value="10"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工ID">
          <el-input v-model="addUser.employeeID"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="addUser.gender" placeholder="请选择性别">
            <el-option label="女" value="女"></el-option>
            <el-option label="男" value="男"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="addUser.role" placeholder="请选择角色">
            <el-option label="部门主管" value="dept"></el-option>
            <el-option label="教员" value="instructor"></el-option>
            <el-option label="员工" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model="addUser.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="email">
          <el-input v-model="addUser.email"></el-input>
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker type="date" placeholder="选择日期" v-model="addUser.arrivalTime" style="width: 300px;"></el-date-picker>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="addUser.password"></el-input>
        </el-form-item>
        <el-form-item style="width: 400px;">
          <el-button @click="dialogTableVisible = false">取消</el-button>
          <el-button type="primary" @click="click_add">确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>


    <el-dialog
        title="编辑用户"
        @close="updateDialogClose"
        :visible.sync="updateTableVisible"
        :close-on-click-modal="false"
    >
      <!-- 编辑用户的表单 -->
      <el-form
          style="margin: 50px"
          ref="addFormRef"
          :rules="rulesAddUser"
          :model="updateUser"
          label-width="100px"
          label-position="left"
          inline class="demo-table-expand2">
        <el-form-item prop="name" label="姓名">
          <el-input v-model="updateUser.name"></el-input>
        </el-form-item>
        <el-form-item prop="department" label="所属部门">
          <el-select v-model="updateUser.deptID" placeholder="请选择所属部门">
            <el-option label="总裁办公室" :value="1"></el-option>
            <el-option label="研究院" :value="2"></el-option>
            <el-option label="人力资源部门" :value="3"></el-option>
            <el-option label="产品研发部门" :value="4"></el-option>
            <el-option label="营销部门" :value="5"></el-option>
            <el-option label="推广部门" :value="6"></el-option>
            <el-option label="人事管理部门" :value="7"></el-option>
            <el-option label="测试部门" :value="8"></el-option>
            <el-option label="策划部门" :value="9"></el-option>
            <el-option label="技术部门" :value="10"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="employeeID" label="员工ID">
          <el-input v-model="updateUser.employeeID"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="updateUser.gender" placeholder="请选择性别">
            <el-option label="女" value="shanghai"></el-option>
            <el-option label="男" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="updateUser.role" placeholder="请选择角色">
            <el-option label="部门主管" value="shanghai"></el-option>
            <el-option label="教员" value="beijing"></el-option>
            <el-option label="员工" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="phoneNumber" label="电话号码">
          <el-input v-model="updateUser.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="email">
          <el-input v-model="updateUser.email"></el-input>
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker type="date" placeholder="选择日期" v-model="updateUser.arrivalTime" style="width: 300px;"></el-date-picker>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input v-model="updateUser.password"></el-input>
        </el-form-item>
        <el-form-item style="width: 400px;">
          <el-button @click="updateTableVisible = false">取消</el-button>
          <el-button type="primary" @click="submit_update">确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-table
        :data="all_employee_info"
        style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="姓名">
              <span>{{ scope.row.name }}</span>
            </el-form-item>
            <el-form-item label="所属部门">
              <span>{{ scope.row.department }}</span>
            </el-form-item>
            <el-form-item label="员工ID">
              <span>{{ scope.row.employeeID }}</span>
            </el-form-item>
            <el-form-item label="部门ID">
              <span>{{ scope.row.deptID }}</span>
            </el-form-item>
            <el-form-item label="性别">
              <span>{{ scope.row.gender }}</span>
            </el-form-item>
            <el-form-item label="角色">
              <span>{{ scope.row.role }}</span>
            </el-form-item>
            <el-form-item label="电话号码">
              <span>{{ scope.row.phoneNumber }}</span>
            </el-form-item>
            <el-form-item label="email">
              <span>{{ scope.row.email }}</span>
            </el-form-item>
            <el-form-item label="入职时间">
              <span>{{ scope.row.arrivalTime }}</span>
            </el-form-item>
            <el-form-item label="密码">
              <span>{{ scope.row.password }}</span>
            </el-form-item>
          </el-form>
          <el-table
              :data="scope.row.scores"
              border
              style="width: 98%;margin-top: 20px">
            <el-table-column
                prop="courseID"
                label="课程ID">
            </el-table-column>
            <el-table-column
                prop="courseName"
                label="课程名称">
            </el-table-column>
            <el-table-column
                prop="startDate"
                label="开始时间">
            </el-table-column>
            <el-table-column
                prop="finished"
                label="是否结束">
            </el-table-column>
            <el-table-column
                prop="instructorName"
                label="教员姓名">
            </el-table-column>
            <el-table-column
                prop="participateID"
                label="课程学号">
            </el-table-column>
            <el-table-column
                prop="score"
                label="培训成绩">
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column
          label="员工ID"
          prop="employeeID">
        <template slot-scope="scope">
          {{scope.row.employeeID}}
        </template>
      </el-table-column>
      <el-table-column
          label="姓名"
          prop="name">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column
          label="部门"
          prop="department">
        <template slot-scope="scope">
          {{scope.row.department}}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="danger"
              @click="click_update(scope.row,scope.$index)">编辑</el-button>
          <el-button
              size="mini"
              type="danger"
              @click="click_delete(scope.row,scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>


<script>
export default {
  data() {
    return {
      dialogTableVisible: false, // 添加用户弹框
      updateTableVisible: false, //编辑用户弹框
      // 添加用户
      addUser: [],
      updateUser: {},
      select:'',
      searchForm :{
        employeeID: '',
        name: ''
      },
      all_employee_info: [
        {
          employeeID:1,
          name:'sb',
          deptID: 3,
          gender: '女',
          scores: [{
            courseID:1,
            startDate: '2016-05-02',
          }]
        },
        {
          employeeID:2,
          name:'sss',
        }
      ]
    }
  },

  created() {
    //显示已有管理员列表
    this.axios.get('/admin/all_employee_info').then(resp => {
      if (resp.status === 200) {
        this.all_employee_info = resp.data.all_employee_info;
      } else {
        this.$message(resp.data.message);
      }
    })
  },
  methods: {
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击添加用户
    // onAddUser() {
    //   this.$refs.addFormRef.validate(async valid => {
    //     if (!valid) return null  // 如果验证失败就不往下继续执行
    //     // const { data: res } = await this.$http.post('users', this.addUser)
    //     // if (res.meta.status !== 201) return this.$message.error(res.meta.msg)
    //     this.all_employee_info.push(this.addUser);
    //     this.$message.success('添加成功')
    //     this.dialogTableVisible = false  // 关闭弹框
    //     this.$refs.addFormRef.resetFields() // 清空表单
    //     this.addUser = [];
    //     this.getUserList() // 重新调用，刷新表单
    //   })
    // },
    click_add() {
        this.$axios.post('/superadmin/addAdmin', this.addUser).then(data => {
          if(data.status==200) {
            this.all_employee_info.push(this.addUser);
            this.$message.success('添加成功')
            this.dialogTableVisible = false  // 关闭弹框
            this.addUser = [];
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
      // })
    },
    //删除操作
    click_delete(item, index) {
      this.$confirm("确定删除该条数据(员工ID" + item.employeeID + ")吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$axios.post('/admin/modify_employee', item).then(data => {
          if(data.status==200) {
            //模拟删除一条数据
            this.all_employee_info.splice(index,1);
            this.$message.success('删除成功');
          }
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      })
          .catch(() => {});
    },
    /**
     * 用于编辑用户
     * @param item 每行的数据
     * @param index 哪一行
     */
    click_update(item, index) {
      this.updateUser= item
      this.updateTableVisible=true;
      this.updateUser.index = index;
      this.$message.success(index);
    },
    submit_update(){
      this.$axios.post('/admin/modify_employee', this.updateUser).then(data => {
        if(data.status==200) {
          this.$set(this.all_employee_info,this.updateUser.index,this.updateUser);
          this.$message.success('编辑成功')
          this.updateTableVisible = false  // 关闭弹框
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    search(){
      this.axios.get('/admin/employee_info',
          {
            params:{
              employeeID: this.searchForm.employeeID,
              name: this.searchForm.name
            }
          }).then(resp => {
        if (resp.status === 200) {
          this.all_employee_info = resp.data;//此处可能会有错
          this.$message.success("查询成功")
        }
      }).then(err => {
        this.$message.error(err.response.data.message)
      })
    }
  }
}
</script>


<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.demo-table-expand2 {
  font-size: 0;
}
.demo-table-expand2.el-form-item {
  margin-bottom: 10px;
}
.demo-table-expand2 label {
  width: 90px;
}

</style>
