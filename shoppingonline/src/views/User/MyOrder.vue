<template>
  <div class="user-container">
    <!-- 顶部导航栏（与 UserHome 一致） -->
    <header class="top-navbar">
      <div class="navbar-content">
        <div class="logo">
          <router-link to="/">购物商城</router-link>
        </div>
        <nav class="user-nav">
           <router-link :to="{ path: '/userHome', query: { id: $route.query.id } }" class="nav-item">首页</router-link>
          <router-link :to="{ path: '/userProfile', query: { id: $route.query.id } }" class="nav-item">个人信息</router-link>
          <router-link :to="{ path: '/user-orders', query: { id: $route.query.id } }" class="nav-item">我的订单</router-link>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </nav>
      </div>
    </header>

    <!-- 主内容区域（用户信息展示） -->
    <main class="main-content">
      <div class="profile-info">
        <h3 class="section-title">个人信息</h3>
        <div class="profile-card">
          <div class="profile-item">
            <span class="label">用户名：</span>
            <span class="value">{{ userInfo.username }}</span>
          </div>
          <div class="profile-item">
            <span class="label">手机号：</span>
            <span class="value">{{ userInfo.phone }}</span>
          </div>
          <div class="profile-item">
            <span class="label">收货地址：</span>
            <span class="value">{{ userInfo.address }}</span>
          </div>
          <!-- 可根据需求添加更多信息字段 -->
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserProfile',
  data() {
    return {
      userInfo: {} // 用户信息数据（从接口获取）
    };
  },
  async mounted() {
    // 模拟获取用户信息（需替换为实际接口）
    try {
      const response = await axios.get('http://localhost:8081/user/info', {
        params: { userId: this.$route.query.id }
      });
      this.userInfo = response.data;
    } catch (error) {
      console.error('获取用户信息失败:', error);
    }
  },
  methods: {
    // 退出登录（与 UserHome 一致）
    handleLogout() {
      const confirmLogout = window.confirm('确定要退出登录吗？');
      if (confirmLogout) {
        localStorage.removeItem('userToken');
        this.$router.push('/login');
      }
    }
  }
};
</script>

<style scoped>
/* 复用 UserHome 的核心样式（保持风格一致） */
.user-container {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.top-navbar {
  background-color: #ff6b6b;
  padding: 15px 0;
}

.navbar-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo a {
  color: white;
  font-size: 20px;
  font-weight: bold;
  text-decoration: none;
}

.user-nav {
  display: flex;
  gap: 20px;
}

.nav-item {
  color: white;
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.nav-item:hover, .nav-item.active {
  background-color: rgba(255, 255, 255, 0.2);
}

.logout-btn {
  background-color: white;
  color: #ff6b6b;
  border: 1px solid #ff6b6b;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.3s;
}

.logout-btn:hover {
  background-color: #f8f9fa;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

/* 新增：个人信息展示样式（与 UserHome 的商品卡片风格一致） */
.main-content {
  max-width: 1200px;
  margin: 30px auto;
}

.section-title {
  color: #333;
  font-size: 22px;
  margin-bottom: 20px;
  text-align: center;
}

.profile-card {
  background-color: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.profile-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.profile-item:last-child {
  border-bottom: none;
}

.label {
  color: #6c757d;
  font-size: 16px;
  width: 100px;
}

.value {
  color: #333;
  font-size: 16px;
  flex: 1;
}
</style>
