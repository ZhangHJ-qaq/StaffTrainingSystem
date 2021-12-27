<template>
  <div>
    <el-button style="float: right;margin-left: 10px">查询</el-button>
    <el-input
        style="float: right"
        v-model="search"
        placeholder="输入关键字搜索"/>
    <el-button style="float: left" @click="addTableVisible = true">添加用户</el-button>
    <!-- 添加用户弹框 -->
    <el-dialog
        title="添加用户"
        @close="addDialogClose"
        :visible.sync="addTableVisible"
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
        <el-form-item prop="name" label="姓名">
          <el-input v-model="addUser.name"></el-input>
        </el-form-item>
        <el-form-item prop="department" label="所属部门">
          <el-select v-model="addUser.department" placeholder="请选择所属部门">
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
          <el-input v-model="addUser.employeeID"></el-input>
        </el-form-item>
        <el-form-item prop="gender" label="性别">
          <el-select v-model="addUser.gender" placeholder="请选择性别">
            <el-option label="女" value="shanghai"></el-option>
            <el-option label="男" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="role" label="角色">
          <el-select v-model="addUser.role" placeholder="请选择角色">
            <el-option label="系统管理员" :value="admin"></el-option>
            <el-option label="部门主管" :value="department_manager"></el-option>
            <el-option label="教员" :value="instructor"></el-option>
            <el-option label="员工" :value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="phoneNumber" label="电话号码">
          <el-input v-model="addUser.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="email">
          <el-input v-model="addUser.email"></el-input>
        </el-form-item>
        <el-form-item prop="arrivalTime" label="入职时间">
          <el-date-picker type="date" placeholder="选择日期" v-model="addUser.arrivalTime" style="width: 300px;"></el-date-picker>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input v-model="addUser.password"></el-input>
        </el-form-item>
        <el-form-item style="width: 400px;">
          <el-button @click="addTableVisible = false">取消</el-button>
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
      <!-- 添加用户的表单 -->
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
          <el-button type="primary" @click="onAddUser">确定</el-button>
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
              :data="courseData"
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
    // 自定义一个手机号验证规则
    var checkMoblie = (rule, value, callback) => {
      if (!value.trim()) {
        return callback(new Error('请输入手机号'))
      }
      if (!/^1[34578]\d{9}$/.test(value)) {
        return callback(new Error('手机号有误！请重填'))
      } else {
        callback()
      }
    }
    return {
      addTableVisible: false, // 添加用户弹框
      updateTableVisible: false, //编辑用户弹框
      //每个员工的培训信息
      courseData: [{
        courseID:1,
        startDate: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      },
        {
          courseID: 2,
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }],
      // 添加用户
      addUser: {
        name: '',
        department:'',
        employeeID:'',
        deptID:'',
        gender:'',
        password: '',
        email: '',
        phoneNumber: '',
        arrivalTime:'',
        role:''
      },
      updateUser: {},//编辑用户 用于暂时存储需要编辑的信息
      all_employee_info: [
        {
          employeeID:1,
          name:'sb',
          deptID: 3,
          gender: '女',
          arrivalTime:'2000-10-16'
        },
        {
          employeeID:2,
          name:'sss',
        }
      ],
      // 验证规则
      rulesAddUser: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入用户密码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        phoneNumber: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { validator: checkMoblie, trigger: 'blur' }
        ]
      }
    }
  },

  created() {
    //显示已有管理员列表
    this.axios.get('/admin/all_employee_info').then(resp => {
      if (resp.status === 200) {
        this.all_employee_info = resp.data.all_employee_info;
      } else {
        this.$message.error(resp.data.message);
      }
    })
  },
  methods: {
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // // 点击添加用户
    // onAddUser() {
    //   this.$refs.addFormRef.validate(async valid => {
    //     if (!valid) return null  // 如果验证失败就不往下继续执行
    //
    //     const { data: res } = await this.$http.post('users', this.addUser)
    //     if (res.meta.status !== 201) return this.$message.error(res.meta.msg)
    //     this.$message.success('添加成功')
    //     this.addTableVisible = false  // 关闭弹框
    //     this.$refs.addFormRef.resetFields() // 清空表单
    //     this.getUserList() // 重新调用，刷新表单
    //   })
    // },
    //新增操作
    click_add() {
      // if( !this.validateField('form') ) return;
      //模拟新增一条数据
      this.$axios.post('/admin/modify_employee', {
        employeeID:this.addUser.employeeID,
        name:this.addUser.name,
        password:this.addUser.password,
        gender:this.addUser.gender,
        arrivalTime: this.addUser.arrivalTime,
        phoneNumber:this.addUser.phoneNumber,
        role:this.addUser.role,
        deptID: this.addUser.deptID,
        email:this.addUser.email
      }).then(data => {
        if(data.status==200) {
          // let itemClone = JSON.parse(JSON.stringify(item));
          // this.all_employee_info.push(itemClone);
          this.$message.success('添加成功');
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    //删除操作
    click_delete(item, index) {
      this.$confirm("确定删除该条数据(员工ID" + item.employeeID + ")吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$axios.post('/admin/modify_employee', {
          employeeID:item.employeeID,
          name:item.name,
          password:item.password,
          gender:item.gender,
          arrivalTime:item.arrivalTime,
          phoneNumber:item.phoneNumber,
          role:item.role,
          deptID:item.deptID,
          email:item.email
        }).then(data => {
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
    click_update(item, index) {
      this.updateUser= item
      this.updateTableVisible=true;
      this.$message.success(index);

    }
  }
}
</script>


<style>
.el-select{
  width: 300px;
}
.el-input{
  width: 300px;
}
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
