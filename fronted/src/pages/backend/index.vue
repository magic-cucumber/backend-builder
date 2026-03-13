<script setup lang="ts">
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {useUserStore} from '@/store/user';
import {HomeFilled, UserFilled} from "@element-plus/icons-vue";

const router = useRouter();
const route = useRoute()
const userStore = useUserStore();
const isCollapse = ref(false);

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value;
};

const logout = () => {
  userStore.logout();
  router.push('/login');
};

const initialPath = () => window.location.pathname
</script>

<template>
  <div class="backend-layout">
    <!-- Sidebar -->
    <div class="sidebar" :class="{ 'is-collapsed': isCollapse }">
      <div class="sidebar-header">
        <div class="logo">后台管理</div>
        <el-button
            type="text"
            @click="toggleSidebar"
            :icon="isCollapse ? 'Expand' : 'Fold'"
            class="collapse-btn"
        />
      </div>

      <el-menu
          :default-active="initialPath()"
          background-color="#304156"
          text-color="#fff"
          :collapse="isCollapse"
          router
      >
        <el-menu-item index="/admin">
          <el-icon>
            <HomeFilled/>
          </el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/admin/user">
          <el-icon>
            <UserFilled/>
          </el-icon>
          <template #title>用户管理</template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- Main Content Area -->
    <div class="main-container">
      <!-- Top Header -->
      <div class="header">
        <div class="breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="i in route.fullPath.split('/')">{{i}}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="user-info">
          <span>欢迎您，{{ userStore.user.username }}</span>
          <el-dropdown trigger="click">
            <el-avatar size="small" icon="UserFilled"/>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/admin/profile/')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="router.push('/')">回到主站</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- Page Content -->
      <div class="content">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.backend-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 220px;
  height: 100%;
  background-color: #304156;
  transition: width 0.3s;
  display: flex;
  flex-direction: column;
}

.sidebar.is-collapsed {
  width: 64px;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
}

.logo {
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  color: #fff;
}


.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.breadcrumb {
  font-size: 14px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.content {
  flex: 1;
  padding: 20px;
  overflow: auto;
  background-color: #f0f2f5;
}
</style>
