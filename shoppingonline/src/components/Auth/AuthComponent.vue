<template>
  <!-- 修改背景图片引用方式 -->
  <div class="auth-container" :style="{ backgroundImage: `url(/assets/v2-21cb8db22fb059184fc18ede2c75d05d_r.jpg)` }"> <!-- 关键修改 -->
    <div class="auth-card">
      <!-- 原有内容保持不变 -->
      <h2 style="color: white;">欢迎登录</h2>
      <div v-if="activeTab === 'login'">
        <form @submit.prevent="handleLogin">
          <div class="form-container">
            <label>身份：</label>
            <select v-model="loginForm.role">
              <option value="user">用户</option>
              <option value="merchant">商家</option>
              <option value="admin">管理员</option>
            </select>
          </div>
          <div class="form-group">
            <label for="phone">手机号</label>
            <input v-model="loginForm.phone" type="text" placeholder="请输入您的手机号" required />
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input v-model="loginForm.password" type="password" placeholder="请输入您的密码" required />
          </div>
          
          <button type="submit" class="login-button">安全登录</button>
        </form>
      </div>
      <div v-if="activeTab === 'register'">
        <form @submit.prevent="handleRegister">
          <div class="form-container">
            <label>身份：</label>
            <select v-model="registerForm.role">
              <option value="user">用户</option>
              <option value="merchant">商家</option>
            </select>
          </div>
          <div class="form-group">
            <label for="username">用户名：</label>
            <input v-model="registerForm.username" type="text" placeholder="用户名" required />
          </div>
          <div class="form-group">
            <label for="phone">手机号：</label>
            <input v-model="registerForm.phone" type="text" placeholder="手机号" required />
          </div>
          <div class="form-group">
            <label for="password">密码：</label>
            <input v-model="registerForm.password" type="password" placeholder="密码" required />
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认密码：</label>
            <input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" required />
          </div>
          <button type="submit" class="login-button">注册</button>
        </form>
      </div>
      <div class="tabs">
        <button :class="{ active: activeTab === 'login' }" @click="activeTab = 'login'">登录</button>
        <button :class="{ active: activeTab === 'register' }" @click="activeTab = 'register'">注册</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AuthComponent',
  data() {
    return {
      activeTab: 'login',
      loginForm: {
        
        phone: '',
        password: '',
        role: 'user'
      },
      registerForm: {
        
        username: '',
        password: '',
        confirmPassword: '',
        phone: '',
        role: 'user'
      }
    };
  },
  methods: {
    async handleLogin() {
      // 登录表单验证
      if (!this.loginForm.role || !this.loginForm.phone || !this.loginForm.password) {
        alert('请填写完整的登录信息');
        return;
      }
      try {
        const response = await axios.post('http://localhost:8081/login', this.loginForm);
        if (response.data.status === 200) {
          const { role, id} = response.data.pdata; // 从后端数据中解构id
          switch (role) {
            case 'user':
              this.$router.push({ path: '/userhome', query: { id } }); // 传递phone到用户主页
              break;
            case 'merchant':
              this.$router.push({ path: '/merchant-dashboard', query: {id } }); // 传递phone到商家后台
              break;
            case 'admin':
              this.$router.push({ path: '/merchantAudit', query: { id } }); // 传递phone到审核页
              break;
            default:
                alert('未知角色，请联系管理员！');
                // 可以选择跳转到错误页面或首页
                this.$router.push('/');
        }
       }else{
        alert('登录失败，请检查');
       }
      } catch (error) {
        alert('登录失败，请检查');
      }
    },
    async handleRegister() {
      // 注册表单验证
      if (
        !this.registerForm.role ||
        !this.registerForm.username ||
        !this.registerForm.phone ||
        !this.registerForm.password ||
        !this.registerForm.confirmPassword
      ) {
        alert('请填写完整的注册信息');
        return;
      }
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        alert('两次输入的密码不一致');
        return;
      }
      try {
        const response = await axios.post('http://localhost:8081/register', this.registerForm);
        if (response.data.status === 200) {
          const { role, phone } = response.data.pdata; // 从后端数据中解构phone
          switch (role) {
            case 'user':
              this.$router.push({ path: '/userhome', query: { phone } });
              break;
            case 'merchant':
              this.$router.push({ path: '/merchant-apply', query: { phone } }); // 传递phone到入驻申请页
              break;
            case 'admin':
                alert('登录成功');
                this.$router.push('/merchantAudit');
                break;
            default:
                alert('未知角色，请联系管理员！');
                // 可以选择跳转到错误页面或首页
                this.$router.push('/');
        }
        } else {
          alert(response.data.message);
        }
      } catch (error) {
        alert('注册失败，请稍后重试');
      }
    }
  }
};
</script>


<style scoped>
/* 重置默认边距 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%; 
}

.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh; 
  
}

.auth-card {
  /* 保持原有样式 */
  background-color: rgba(123, 31, 31, 0.3);
  backdrop-filter: blur(10px);
  border-radius: 10px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
  text-align: center;
  padding: 40px;
  width: 400px;
}

h2 {
  color: white;
  margin-top: 0;
  margin-bottom: 30px;
}

.form-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
  gap: 10px; /* 添加元素间距 */
}

.form-group label {
  width: 90px; /* 增大标签宽度适配文字 */
  text-align: right;
  margin-right: 0; /* 移除原有 margin-right，用 gap 替代 */
  color: white;
  flex-shrink: 0; /* 防止标签被压缩 */
}

.form-group input,
.form-group select {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 200px; /* 设置输入框最小宽度 */
}

/* 同步调整身份选择区域的布局 */
.form-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
  gap: 10px; /* 添加间距 */
}

.form-container label {
  width: 90px; /* 与表单标签宽度统一 */
  text-align: right;
  margin-right: 0;
  color: white;
  flex-shrink: 0;
}

.form-container select {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: white;
  min-width: 200px; /* 同步设置最小宽度 */
}
.form-options {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}
.form-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
}
.login-button {
  width: 100%;
  padding: 10px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.tabs {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.tabs button {
  margin: 0 10px;
  padding: 8px 15px;
  width:50%;
  background-color: transparent;
  color: white;
  border: 1px solid white;
  cursor: pointer;
}


.tabs button.active {
  background-color: white;
  color: #3498db;
}
</style>