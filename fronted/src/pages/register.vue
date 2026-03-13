<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from '@/store/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  contactInfo: '',
  address: ''
});

const loading = ref(false);
const registerError = ref('');

const handleRegister = async () => {
  // Validate input
  if (!registerForm.value.username || !registerForm.value.password) {
    registerError.value = '用户名和密码不能为空';
    return;
  }
  
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    registerError.value = '两次输入的密码不一致';
    return;
  }
  
  try {
    loading.value = true;
    registerError.value = '';
    
    await userStore.register(
      registerForm.value.username,
      registerForm.value.password,
      registerForm.value.name,
      registerForm.value.contactInfo,
      registerForm.value.address
    );
    
    // Redirect to login page after successful registration
    router.push('/login');
  } catch (error: any) {
    registerError.value = error.message || '注册失败，请稍后再试';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="register-container">
    <div class="register-box">
      <h2>用户注册</h2>
      <el-form @submit.prevent="handleRegister">
        <el-alert v-if="registerError" type="error" :title="registerError" show-icon />
        
        <el-form-item>
          <el-input 
            v-model="registerForm.username" 
            placeholder="用户名" 
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item>
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="密码" 
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="确认密码" 
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-input 
            v-model="registerForm.name" 
            placeholder="姓名" 
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item>
          <el-input 
            v-model="registerForm.contactInfo" 
            placeholder="联系方式" 
            prefix-icon="Phone"
          />
        </el-form-item>
        
        <el-form-item>
          <el-input 
            v-model="registerForm.address" 
            placeholder="地址" 
            prefix-icon="Location"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            @click="handleRegister" 
            style="width: 100%"
          >
            注册
          </el-button>
        </el-form-item>
        
        <div class="register-actions">
          <router-link to="/login">已有账号？立即登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.register-box {
  width: 400px;
  padding: 30px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-box h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #303133;
}

.register-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
  font-size: 14px;
}

.register-actions a {
  color: #409EFF;
  text-decoration: none;
}
</style>
