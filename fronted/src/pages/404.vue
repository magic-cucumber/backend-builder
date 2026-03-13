<script setup lang="ts">

</script>

<template>
  <div class="error-container">
    <h1>404</h1>
    <h2>页面不存在</h2>
    <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
  </div>
</template>

<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

h1 {
  font-size: 6rem;
  margin-bottom: 0;
  color: #409EFF;
}

h2 {
  margin-bottom: 2rem;
  color: #606266;
}
</style>
