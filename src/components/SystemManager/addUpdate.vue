<template>
  <div id="app" style="width: 100%">
    <el-input size="small" style="width:100px" v-model="name"  placeholder="请输入姓名"></el-input>
    <el-date-picker
        v-model="date"
        type="date"
        placeholder="选择日期"
        value-format="yyyy-MM-dd"
    >
    </el-date-picker>
    <el-input size="small" style="width:100px" v-model="address"  placeholder="请输入地址"></el-input>
    <el-button size="small" type="primary" @click="handleCurrentChange">新增</el-button>
    <el-button size="small" type="primary" @click="delRow">删除</el-button>
    <el-table
        :data="tableData"
        style="width: 100%">
      <el-table-column
          prop="date"
          label="日期"
          width="180">
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名"
          width="180">
      </el-table-column>
      <el-table-column
          prop="address"
          label="地址">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          <el-button type="text" size="small"
                     @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button
              @click.native.prevent="deleteRow(scope.$index, tableData)"
              type="text"
              size="small">
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="编辑数据"
               :visible.sync="editFormVisible"
               :close-on-click-modal="false"
               class="edit-form">
      <el-form :model="editForm" label-width="80px"  ref="editForm">
        <el-form-item label="名称" prop="Name">
          <el-input v-model="editForm.name" auto-complete="off"></el-input>
          <el-input v-model="editForm.date" auto-complete="off"></el-input>
          <el-input v-model="editForm.address" auto-complete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="handleCancel('editForm')">取消</el-button>
        <el-button type="primary" @click.native="handleUpdate('editForm')">更新</el-button>
      </div>
    </el-dialog>
    <br>数据:{{tableData}}
  </div>
</template>

<script>
export default {
  name: "demo",
  data() {
    return{
      //编辑界面数据
      editForm: {
        name: '',
        date: '',
        address:'',
        index:0
      },
      //定义属性
      name:undefined,
      date:undefined,
      address:undefined,
      //默认dialog弹窗不打开（true打开，false为不打开）
      editFormVisible: false,
      //模拟表格数据
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '郑州市普陀区金沙江路 1518 弄'
      },{
        date: '2016-05-03',
        name: '王二虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }]
    }

  },
  methods:{
    //新增数据方法
    handleCurrentChange:function () {
      this.tableData.push({date:this.date,name:this.name,address:this.address});
    },
    //删除最后一行数据
    delRow(index, row){
      this.tableData.splice(this.tableData.indexOf(row),1);
    },
    //删除当前行数据
    deleteRow(index, rows) {
      rows.splice(index, 1);
    },
    //编辑
    handleEdit: function (index, row) {
      this.editFormVisible = true;//dialog对话窗口打开
      this.editForm = Object.assign({}, row);//将数据传入dialog页面
      this.editForm.index=index;//传递当前index
    },
    //取消
    handleCancel() {
      this.editFormVisible = false;
    },
    //更新
    handleUpdate() {
      //dialog页面数据写入到tableData页面上
      //this.$set(tableName,talbeIndex,data)
      this.$set(this.tableData,this.editForm.index,{date:this.editForm.date,name:this.editForm.name,address:this.editForm.address});
      this.editFormVisible = false;
    }
  }
}
</script>
