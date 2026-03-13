<script setup lang="ts">
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import {useUserStore} from '@/store/user';
import {HomeFilled} from "@element-plus/icons-vue";

const router = useRouter();
const userStore = useUserStore();
const activeMenu = ref('home');

const handleMenuSelect = (key: string) => {
  activeMenu.value = key;
  if (key === 'logout') {
    userStore.logout();
    router.push('/login');
  }
  if (key === 'backend') {
    router.push('/admin');
  }
};
</script>

<template>
  <div class="frontend-layout">
    <header>
      <div class="logo">前台</div>

      <el-menu :default-active="activeMenu" mode="horizontal" router @select="handleMenuSelect" class="main-menu">
        <el-menu-item index="home" route="/">首页</el-menu-item>
      </el-menu>

      <div class="user-actions">
        <template v-if="userStore.token">
          <span>欢迎您，{{ userStore.user.username }}</span>
          <el-button-group>
            <el-button type="primary" size="small" @click="handleMenuSelect('backend')">后台</el-button>
            <el-button type="danger" size="small" @click="handleMenuSelect('logout')">退出</el-button>
          </el-button-group>
        </template>
        <template v-else>
          <router-link to="/login">
            <el-button size="small">登录</el-button>
          </router-link>
          <router-link to="/register">
            <el-button size="small" type="primary">注册</el-button>
          </router-link>
        </template>
      </div>
    </header>

    <!-- Main Content Area -->
    <main>
      <router-view/>
    </main>

    <!-- Enhanced Footer -->
    <footer class="enhanced-footer">
      <div class="footer-container">
        <!-- Company Information -->
        <div class="footer-section company-info">
          <h3>关于我们</h3>
          <p>无</p>
          <div class="social-links">
            <a href="#" title="微信">
              <el-icon>
                <HomeFilled/>
              </el-icon>
            </a>
            <a href="#" title="微博">
              <el-icon>
                <HomeFilled/>
              </el-icon>
            </a>
            <a href="#" title="抖音">
              <el-icon>
                <HomeFilled/>
              </el-icon>
            </a>
          </div>
        </div>

        <!-- Quick Links -->
        <div class="footer-section quick-links">
          <h3>快速链接</h3>
          <ul>
            <li>
              <router-link to="/">首页</router-link>
            </li>
          </ul>
        </div>

        <!-- Contact Information -->
        <div class="footer-section contact-info">
          <h3>联系我们</h3>
          <address>
            <p><i class="el-icon-location-information"></i>虚黑城</p>
            <p><i class="el-icon-phone"></i>114514</p>
            <p><i class="el-icon-message"></i>iveour@163.com</p>
          </address>
        </div>

        <!-- Advertisement Section -->
        <div class="footer-section advertisement-section">
          <h3>合作伙伴</h3>
          <div class="advertisement">
            <div>华立科技</div>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p>© 2024-2025 kagg886 版权所有 <br> <a href="#">隐私政策</a> · <a href="#">使用条款</a>
        </p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.frontend-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  display: flex;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #303133;
  margin-right: 30px;
}

.main-menu {
  flex-grow: 1;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-actions span {
  margin-right: 10px;
}

main {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
}

/* Enhanced Footer Styles */
.enhanced-footer {
  background-color: #303133;
  color: #e0e0e0;
  padding: 40px 0 20px;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  padding: 0 20px;
}

.footer-section h3 {
  color: #fff;
  margin-bottom: 20px;
  font-size: 18px;
  position: relative;
  padding-bottom: 10px;
}

.footer-section h3::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 2px;
  background-color: #409EFF;
}

.company-info p {
  margin-bottom: 20px;
  line-height: 1.6;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-links a {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.1);
  color: #fff;
  transition: all 0.3s ease;
}

.social-links a:hover {
  background-color: #409EFF;
  transform: translateY(-3px);
}

.quick-links ul {
  list-style: none;
  padding: 0;
}

.quick-links li {
  margin-bottom: 10px;
}

.quick-links a {
  color: #e0e0e0;
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-block;
}

.quick-links a:hover {
  color: #409EFF;
  transform: translateX(5px);
}

.contact-info address {
  font-style: normal;
}

.contact-info p {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.advertisement-section {
  overflow: hidden;
}

.advertisement-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.ad-item {
  padding: 8px 12px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  color: #e0e0e0;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.ad-item:hover {
  background-color: rgba(64, 158, 255, 0.3);
  color: #fff;
}

.ad-loading,
.no-ads {
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.footer-bottom {
  text-align: center;
  padding-top: 30px;
  margin-top: 30px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-bottom a {
  color: #e0e0e0;
  text-decoration: none;
}

.footer-bottom a:hover {
  color: #409EFF;
}

@media (max-width: 768px) {
  .footer-container {
    grid-template-columns: 1fr;
  }
}
</style>
