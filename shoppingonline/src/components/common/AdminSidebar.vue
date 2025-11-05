<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <h3>管理员页面</h3>
    </div>
    <nav class="sidebar-menu">
      <router-link 
        :to="{ path: '/merchantAudit', query: { id: adminId } }" 
        class="menu-item"
        :class="{ active: currentRoute === '/merchantAudit' }"
      >
        商家入驻审核
      </router-link>
      <router-link 
        :to="{ path: '/productAudit', query: { id: adminId } }" 
        class="menu-item"
        :class="{ active: currentRoute === '/productAudit' }"
      >
        产品上新审核
      </router-link>

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
  name: 'AdminSidebar',
  props: {
    adminId: {
      type: String,
      required: true
    }
  },
  computed: {
    currentRoute() {
      return this.$route.path;
    }
  },
  methods: {
    handleLogout() {
      // 触发父组件的logout事件
      this.$emit('logout');
    }
  }
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 240px;
  background: #2c3e50;
  color: white;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 15px 0;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  margin-bottom: 20px;
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
  border: none;
  cursor: pointer;
  text-align: left;
}

.menu-item:hover {
  background: rgba(255,255,255,0.1);
  color: white;
}

.menu-item.active {
  background: #2196F3;
  color: white;
}
</style>