<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入患者姓名查询" style="width: 200px" v-model="userName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

<!--    <div class="operation">-->
<!--&lt;!&ndash;      <el-button type="primary" plain @click="handleAdd">新增</el-button>&ndash;&gt;-->
<!--      <el-button type="danger" plain @click="delBatch">批量删除</el-button>-->
<!--    </div>-->

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
<!--        <el-table-column type="selection" width="55" align="center"></el-table-column>-->
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="患者姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="就诊时间"></el-table-column>
        <el-table-column prop="medicalRecord" label="医嘱病历">
          <template v-slot="scope">
            <el-button type="success" size="mini" @click="viewEditor(scope.row.medicalRecord)">查看病历</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="inhospital" label="是否住院">
        </el-table-column>
        <el-table-column prop="inhostpitalRecord" label="是否住院登记"></el-table-column>

        <el-table-column label="操作" width="180" align="center" v-if="user.role!=='USER'">
          <template v-slot="scope">
            <el-button plain type="primary" v-if="user.role==='DOCTOR'" @click="handleEdit(scope.row)" size="mini">
              填写医嘱病历
            </el-button>
            <el-button plain type="primary" v-if="user.role==='ADMIN' && scope.row.inhospital
==='是' && scope.row.inhostpitalRecord==='否'" @click="Registration(scope.row)" size="mini">住院登记
            </el-button>
            <el-button plain type="primary" @click="del(scope.row)" size="mini">删除
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

    <el-dialog title="填写病历单" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false"
               destroy-on-close @close="cancel">
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="medicalRecord" label="医嘱病历">
          <div id="editor">
          </div>
        </el-form-item>
        <el-form-itme prop="inhospital" label="是否住院">
          <el-select v-model="form.inhospital" placeholder="请选择是否住院" style="width: 100%;">
            <el-option label="是" value="是"></el-option>
            <el-option label="否" value="否"></el-option>
          </el-select>
        </el-form-itme>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="医嘱病历" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false"
               destroy-on-close>
      <div v-html="this.viewContent" class="w-e-text"></div>
    </el-dialog>
  </div>
</template>

<script>
import E from "wangeditor"
import registration from "@/views/manager/Registration.vue";

let editor

function initWangEditor(content) {
  setTimeout(() => {
    if (!editor) {
      editor = new E('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}

export default {
  name: "Record",
  computed: {
    registration() {
      return registration
    }
  },
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      userName: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        inhospital: [
          {required: true, message: '请输入是否住院', trigger: 'blur'},
        ],
      },
      ids: [],
      editorVisible: null,
      viewContent: null,
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    viewEditor(content) {
      this.viewContent = content
      this.editorVisible = true
    },
    Registration(row) {
      let data = JSON.parse(JSON.stringify(row))
      data.inhostpitalRecord = '是'
      this.$request.put('/record/update', data).then(res => {
        if (res.code === '200') {

          this.load(1)
          this.toRegistration(data.userId)
        }
      })
    },
    toRegistration(userId) {
      let data = {
        userId: userId,
        hosStatus: '住院中'
      }
      this.$request.post('/registration/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success("登记成功")
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      initWangEditor(this.form.medicalRecord || '')
      this.fromVisible = true   // 打开弹窗
    },
    cancel() {
      this.fromVisible = false
      location.href = '/record'
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.form.medicalRecord = editor.txt.html()
      this.$request.put('/record/update', this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    del(row) {   // 单个删除
     if(this.user.role!=='ADMIN' && row.inhospital
          !=='是' && row.inhostpitalRecord==='否') {
       this.$message.warning("您无法删除未登记或未确定的记录")
       return
     }
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/record/delete/' + row.id).then(res => {
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
        this.$request.delete('/record/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/record/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userName: this.userName,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.userName = null
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