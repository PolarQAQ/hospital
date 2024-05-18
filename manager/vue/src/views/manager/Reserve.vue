<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入挂号状态" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation" v-if="user.role==='ADMIN'">
      <!--      <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>

        <el-table-column type="selection" width="40" align="center"
                         v-if="user.role==='ADMIN' && status==='已叫号'"></el-table-column>

        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="患者姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="挂号时间"></el-table-column>
        <el-table-column prop="status" label="挂号状态"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" v-if="user.role === 'USER'"
                       @click=del(scope.row)>取消挂号
            </el-button>
            <el-button plain type="warning" size="mini" v-if="user.role === 'DOCTOR'" @click=call(scope.row)>叫号
            </el-button>
            <el-button plain type="danger" size="mini" v-if="user.role!=='USER'" @click=del(scope.row)>删除记录
            </el-button>
          </template>

        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "Reserve",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      status: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    call(row) {
      let reserveData = JSON.parse(JSON.stringify(row));
      reserveData.status = '已叫号'
      this.$request.put('/reserve/update', reserveData).then(res => {
        if (res.code === '200') {
          this.$message.success('叫号成功')
          this.load(1)
          // 往就诊记录里同步一条数据
          this.record(row)
        }
      })
    },
    record(row) {
      let data = {
        userId: row.userId,
        doctorId: row.doctorId,
      }
      this.$request.post('/record/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('数据同步成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(row) {   // 单个删除
      if (row.status === '未叫号' && this.user.role!=='USER') {
        this.$message.warning("您无法删除未叫号的患者")
        return
      }
      if (this.user.role === 'USER') {
        if(row.status === '已叫号') {
          this.$message.warning("已叫号，无法取消")
          return
        }
        this.$confirm('您确定取消挂号么', '操作确认', {type: "warning"}).then(response => {
          this.$request.delete('/reserve/delete/' + row.id).then(res => {
            if (res.code === '200') {   // 表示操作成功
              this.$message.success('操作成功')
              this.load(1)
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }).catch(() => {
        })
        return
      }
      this.$confirm('确定删除么，删除后无法恢复，请谨慎操作', '操作确认', {type: "warning"}).then(response => {
        this.$request.delete('/reserve/delete/' + row.id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/reserve/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/reserve/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>