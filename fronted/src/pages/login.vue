<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from '@/store/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

const loginForm = ref({
  username: '',
  password: ''
});

const loading = ref(false);
const loginError = ref('');

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    loginError.value = '用户名和密码不能为空';
    return;
  }
  
  try {
    loading.value = true;
    loginError.value = '';
    await userStore.login(loginForm.value.username, loginForm.value.password);
    router.push('/');
  } catch (error: any) {
    loginError.value = error.message || '登录失败，请检查用户名和密码';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <h2>用户登录</h2>
      <el-form @submit.prevent="handleLogin">
        <el-alert v-if="loginError" type="error" :title="loginError" show-icon />
        
        <el-form-item>
          <el-input 
            v-model="loginForm.username" 
            placeholder="用户名" 
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item>
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="密码" 
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            @click="handleLogin" 
            style="width: 100%"
          >
            登录
          </el-button>
        </el-form-item>
        
        <div class="login-actions">
          <router-link to="/register">没有账号？立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-box {
  width: 350px;
  padding: 30px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #303133;
}

.login-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
  font-size: 14px;
}

.login-actions a {
  color: #409EFF;
  text-decoration: none;
}
</style>
