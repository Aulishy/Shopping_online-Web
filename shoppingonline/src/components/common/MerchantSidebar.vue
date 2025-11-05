<template>
  <!-- 侧边栏导航 -->
  <aside class="sidebar">
    <div class="sidebar-header">
      <h3>商家中心</h3>
    </div>
    <nav class="sidebar-menu">
      <router-link 
        :to="{ path: '/merchant-dashboard', query: { id: $route.query.id } }"  
        class="menu-item"
        :class="{ active: $route.path === '/merchant-dashboard' }"  
      >
        商家主页
      </router-link>
      <router-link 
        :to="{ path: '/order-management', query: { id: $route.query.id } }" 
        class="menu-item"
        :class="{ active: $route.path === '/order-management' }"
      >
        订单管理
      </router-link>
      <router-link 
        :to="{ path: '/product-publish', query: { id: $route.query.id } }"
        class="menu-item"
        :class="{ active: $route.path === '/product-publish' }"
      >
        产品发布
      </router-link>
      <!-- 修改：替换为按钮并绑定确认逻辑 -->
      <button 
        class="menu-item" 
        @click="handleLogout"
        style="margin-bottom:50px;margin-top: auto;"
      >
        退出登录
      </button>
    </nav>
  </aside>
</template>

<script>
export default {
  name: 'MerchantSidebar',
  methods: {
    handleLogout() {
      // 弹出确认对话框
      const confirmLogout = window.confirm('确定要退出登录吗？');
      if (confirmLogout) {
        // 清除用户状态（示例：本地存储的token）
        localStorage.removeItem('userToken');
        // 跳转到登录页（需确保路由配置了 /login 路径）
        this.$router.push('/');
      }
    }
  }
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh; /* 保持占满视口高度 */
  width: 240px;
  background: #2c3e50;
  color: white;
  padding: 20px;
  display: flex;          /* 新增：启用 Flex 布局 */
  flex-direction: column; /* 新增：垂直排列子元素 */
}
.sidebar-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.menu-item {
  padding: 12px 15px;
  border-radius: 6px;
  color: rgba(255,255,255,0.8);
  text-decoration: none;
  transition: all 0.3s;
  background: transparent;
}

.menu-item:hover {
  background: rgba(255,255,255,0.1);
  color: white;
}

.menu-item.active {
  background: #2196F3;
  color: white;
}
.sidebar-header {
  padding: 15px 0;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  margin-bottom: 20px;
  /* 头部内容自然高度，不额外设置高度 */
}
</style>