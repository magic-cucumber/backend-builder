<script setup lang="ts">
import { computed } from "vue";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

// 导航项目
const navItems = [
  { name: '/admin/profile', label: '个人资料' },
];

// 计算当前活动的导航项
const activeNavPath = computed(() => {
  return route.path;
});

// 处理导航点击
const handleNavClick = (tab: string) => {
  router.push(tab);
};
</script>

<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="custom-nav">
        <div
            v-for="item in navItems"
            :key="item.name"
            :class="['nav-item', { active: activeNavPath === item.name }]"
            @click="handleNavClick(item.name)"
        >
          {{ item.label }}
        </div>
      </div>

      <div class="router-view-container">
        <router-view />
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.profile-card {
  max-width: 800px;
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.custom-nav {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 20px;
}

.nav-item {
  padding: 0 20px 12px;
  margin-right: 20px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  position: relative;
  transition: all 0.3s;
}

.nav-item:hover {
  color: #409EFF;
}

.nav-item.active {
  color: #409EFF;
  font-weight: 500;
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  height: 2px;
  width: 100%;
  background-color: #409EFF;
}

.router-view-container {
  margin-top: 20px;
}
</style>
