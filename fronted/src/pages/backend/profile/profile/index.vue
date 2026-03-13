<script setup lang="ts">
import {onMounted, ref} from "vue";
import {profile, updateUser, type User} from "@/api/user.ts";
import {ElMessage} from "element-plus";

const user = ref<User | null>(null);
const loading = ref(true);
const editMode = ref(false);
const editForm = ref<Partial<User>>({});

function startEdit() {
  editForm.value = { ...user.value };
  editMode.value = true;
}

function cancelEdit() {
  editMode.value = false;
  editForm.value = {};
}

async function saveProfile() {
  if (!user.value || !editForm.value) return;

  try {
    const result = await updateUser(user.value.userId, editForm.value as User);
    if (result) {
      ElMessage.success('个人信息更新成功');
      // 更新本地user数据
      user.value = { ...user.value, ...editForm.value };
      editMode.value = false;
    } else {
      ElMessage.error('个人信息更新失败');
    }
  } catch (error) {
    ElMessage.error('个人信息更新失败');
    console.error(error);
  }
}

onMounted(async () => {
  try {
    loading.value = true;
    user.value = await profile();
  } catch (error) {
    ElMessage.error('获取用户信息失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
});

</script>

<template>
  <el-skeleton :loading="loading" animated>
    <template #default>
      <div v-if="user">
        <div v-if="!editMode" class="profile-info">
          <div class="info-item">
            <span class="label">用户名:</span>
            <span class="value">{{ user.username }}</span>
          </div>
          <div class="info-item">
            <span class="label">角色:</span>
            <span class="value">{{ user.role }}</span>
          </div>
          <div class="info-item">
            <span class="label">邮箱:</span>
            <span class="value">{{ user.email }}</span>
          </div>
          <div class="info-item">
            <span class="label">联系方式:</span>
            <span class="value">{{ user.phone }}</span>
          </div>

          <div class="action-buttons">
            <el-button type="primary" @click="startEdit">编辑资料</el-button>
          </div>
        </div>

        <el-form v-else label-position="top" :model="editForm">
          <el-form-item label="用户名">
            <el-input v-model="editForm.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="editForm.password" type="password" placeholder="留空则不修改"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="editForm.email"></el-input>
          </el-form-item>
          <el-form-item label="联系方式">
            <el-input v-model="editForm.phone"></el-input>
          </el-form-item>


          <div class="action-buttons">
            <el-button @click="cancelEdit">取消</el-button>
            <el-button type="primary" @click="saveProfile">保存</el-button>
          </div>
        </el-form>
      </div>
    </template>
  </el-skeleton>
</template>

<style scoped>

.profile-info {
  margin-top: 20px;
}

.info-item {
  display: flex;
  margin-bottom: 16px;
}

.label {
  font-weight: bold;
  width: 120px;
  color: #606266;
}

.value {
  flex: 1;
}

.action-buttons {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
