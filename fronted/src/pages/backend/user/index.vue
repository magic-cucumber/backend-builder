<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { type User, listUsers, createUser, updateUser, deleteUser } from '@/api/user'

// 用户列表数据和分页
const userList = ref<User[]>([])
const total = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('添加用户')
const formData = ref<User>({
  userId: 0,
  username: '',
  email: '',
  phone: '',
  password: '',
  role: 'USER'
})
const isEdit = ref(false)

// 加载用户列表
const loadUsers = async () => {
  loading.value = true
  try {
    const res = await listUsers(currentPage.value, pageSize.value)
    userList.value = res.records
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取用户列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 页码变化处理
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadUsers()
}

// 每页条数变化处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadUsers()
}

// 打开添加用户对话框
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '添加用户'
  isEdit.value = false
  dialogVisible.value = true
}

// 打开编辑用户对话框
const handleEdit = (row: User) => {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  formData.value = { ...row }
  dialogVisible.value = true
}

// 删除用户
const handleDelete = (userId: number) => {
  ElMessageBox.confirm('确认删除该用户?', '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteUser(userId)
      ElMessage.success('删除成功')
      loadUsers()
    } catch (error) {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }).catch(() => {})
}

// 提交表单
const submitForm = async () => {
  try {
    if (isEdit.value) {
      await updateUser(formData.value.userId, formData.value)
      ElMessage.success('更新成功')
    } else {
      await createUser(formData.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '添加失败')
    console.error(error)
  }
}

// 重置表单
const resetForm = () => {
  formData.value = {
    userId: 0,
    username: '',
    email: '',
    phone: '',
    password: '',
    role: 'USER'
  }
}

// 取消表单
const cancelForm = () => {
  dialogVisible.value = false
  resetForm()
}

// 页面加载时获取用户列表
onMounted(() => {
  loadUsers()
})
</script>

<template>
  <div class="user-container">
    <div class="action-bar">
      <el-button type="primary" @click="handleAdd">添加用户</el-button>
    </div>

    <el-table :data="userList" border v-loading="loading" style="width: 100%">
      <el-table-column prop="userId" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="role" label="角色">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'success'">
            {{ row.role === 'ADMIN' ? '管理员' : '用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.userId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 用户表单对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="formData.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="formData.phone" />
        </el-form-item>
        <el-form-item label="密码" v-if="!isEdit">
          <el-input v-model="formData.password" type="password" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="formData.role" style="width: 100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelForm">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-container {
  padding: 20px;
}

.action-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
