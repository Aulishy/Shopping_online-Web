<template>
  <div class="user-container">
    <!-- 顶部导航栏（保持不变） -->
    <header class="top-navbar">
      <div class="navbar-content">
        <div class="logo">
          <router-link to="/">购物商城</router-link>
        </div>
        <nav class="user-nav">
           <router-link :to="{ path: '/userHome', query: { id: $route.query.id } }" class="nav-item">首页</router-link>
          <router-link :to="{ path: '/userProfile', query: { id: $route.query.id } }" class="nav-item">个人信息</router-link>
          
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
            <span class="label">用户ID:</span>
            <span class="value">{{  this.$route.query.id}}</span>
          </div>
          


          <!-- 收货地址列表（修改展示内容） -->
          <div class="profile-item">
            <span class="label">收货地址：</span>
            <div class="address-list">
              <div v-for="(addr, index) in userInfo.addresses" :key="index" class="address-item">

                <!-- 补充展示收件人和联系电话 -->
                <span class="address-detail">
                  {{ addr.detail }}<br>
                  收件人：{{ addr.contact }}<br>
                  电话：{{ addr.phone }}
                </span>
                <button class="delete-btn" @click="deleteAddress(addr.id)">删除</button>
              </div>
              <button 
                class="add-address-btn" 
                @click="showAddAddress = true"
                v-if="userInfo.addresses.length < 5"  
              >+ 添加新地址</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加地址模态框（新增） -->
      <div v-if="showAddAddress" class="address-modal">
        <div class="modal-content">
          <span class="modal-close" @click="showAddAddress = false">&times;</span>
          <h4>添加新地址</h4>
          <form @submit.prevent="addAddress">
            <div class="form-group">
              <label>详细地址：</label>
              <input v-model="newAddress.detail" required>
            </div>
            <div class="form-group">
              <label>收件人：</label>
              <input v-model="newAddress.contact" required>
            </div>
            <div class="form-group">
              <label>联系电话：</label>
              <input v-model="newAddress.phone" type="tel" required>
            </div>
            <button type="submit" class="btn-confirm">保存地址</button>
            <button type="button" class="btn-cancel" @click="showAddAddress = false">取消</button>
          </form>
        </div>
      </div>

              <!-- 订单列表 -->
        <div class="order-list-card">  <!-- 修改：使用统一容器类 -->
          <div class="order-list">
            <!-- 修改：将 filteredOrders 替换为已定义的 userOrders -->
            <div class="order-item" v-for="order in userOrders" :key="order.orderId">
              <!-- 订单基础信息（保持不变） -->
              <div class="order-basic">
                <span class="order-id">订单号：{{ order.orderId }}</span>
                <span class="order-time">{{ formatTime(order.createTime) }}</span>
                <span class="order-amount">成交金额：¥{{ order.totalAmount }}</span>
              </div>

              <!-- 商品信息（保持不变） -->
              <div class="order-products">
                <div class="product-thumbnail">
                  <img :src="order.productImage" alt="商品图" class="product-img">
                  <div class="product-info">
                    <p class="product-name">{{ order.productName }}</p>
                    <p class="product-quantity">x{{ order.quantity }}</p>
                  </div>
                </div>
              </div>

              <!-- 订单状态与操作（保持原有绑定逻辑） -->
              <!-- 订单操作按钮部分 -->
              <div class="order-actions">
                <span class="status-tag" :class="['status-' + order.status]">
                  {{ order.status}}
                </span>
                <button 
                  class="btn-ship" 
                  @click="handleShip(order, $event)" 
                  :disabled="order.status !== '已发货'">  <!-- 修正判断条件 -->
                  确认收货
                </button>
                <button 
                  class="btn-delete" 
                  @click="handleDelete(order, $event)" 
                  :disabled="order.status !== '已完成'">
                  删除订单
                </button>
              </div>
            </div>
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
      userInfo: {
        username: '购物小达人',
        phone: '138-1234-5678',
        addresses: [  // 最多5个地址（符合v-if限制）
        ]
      },
      newAddress: { detail: '', contact: '', phone: '' },  // 保持空初始值
      userOrders: [  // 包含不同状态的订单（pending/completed）
      ],
      showAddAddress: false  // 默认隐藏模态框
    };
  },
  async mounted() {
    try {
      const response = await axios.post('http://localhost:8081/user/info', 
      { userId: this.$route.query.id }, // 将参数移到请求体
      {
        headers: {
          'Content-Type': 'application/json' // 明确指定请求头
        }
      }
    );
      // 修改：假设接口返回addresses数组（原address字段改为addresses）
      this.userInfo = {
        username: response.data.username || '未知用户',
      phone: response.data.phone || '未绑定手机',
      addresses: response.data.addresses?.map(addr => ({
        id: addr.addressId,      // 根据实际情况调整ID字段
        detail: addr.shippingAddress,
        contact: addr.recipientName,
        phone: addr.recipientPhone
      })) || []
      
    };
      // 加载订单数据（保持不变）
      this.userOrders = response.data.orders?.map(order => ({
       orderId: order.order_id,
      productName: order.product_name,
      productImage: order.product_imageUrl, // 使用新的图片URL字段
      price: parseFloat(order.total_amount), // 转换金额为数字类型
      quantity: order.product_buy,
      totalAmount: order.total_amount,
      status: order.status,
      createTime: order.create_time
    })) || [];
    } catch (error) {
      console.error('获取用户信息失败:', error);
    }
  },
  methods: {
   
    handleLogout() {
      const confirmLogout = window.confirm('确定要退出登录吗？');
      if (confirmLogout) {
        localStorage.removeItem('userToken');
        this.$router.push('/');
      }
    },

    // 新增：删除地址方法
    async deleteAddress(addrId) {
      const confirmDel = window.confirm('确定要删除该地址吗？');
      if (!confirmDel) return;
     try {
    await axios.post('http://localhost:8081/user/deleteaddress', 
      { 
        userId: this.$route.query.id,  // 用户ID
        addressId: addrId
      }, 
      {
        headers: {
          'Content-Type': 'application/json' // 明确指定JSON格式
        }
      }
    );
    // 从数组中移除已删除的地址
    this.userInfo.addresses = this.userInfo.addresses.filter(addr => addr.id !== addrId);
    alert('地址删除成功');
  } catch (error) {
    alert('删除失败，请稍后重试');
  }
    },

    // 新增：添加地址方法
    async addAddress() {
      try {
        const response = await axios.post('http://localhost:8081/user/insertaddress', {
          userId: this.$route.query.id,
          ...this.newAddress
        });
        // 将新地址添加到数组
        this.userInfo.addresses.push({ id: response.data.addressId, ...this.newAddress });
        // 清空表单并关闭模态框
        this.newAddress = { detail: '', contact: '', phone: '' };
        this.showAddAddress = false;
        alert('地址添加成功');
      } catch (error) {
        alert('添加失败，请检查信息是否完整');
      }
    },
     formatTime(timeStr) {
      const date = new Date(timeStr);
      
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    },
    
   async handleShip(order) {
      const confirmReceive = window.confirm('确定要确认收货吗？');
      if (!confirmReceive) return;

      try {
        // 调用后端确认收货接口（假设接口路径为 /order/confirmReceive）
        const response = await axios.post('http://localhost:8081/user/confirmReceive', {
          orderId: order.orderId  // 传递订单ID
        }, {
          headers: { 'Content-Type': 'application/json' }
        });

        if (response.data.code === 200) {
          // 成功后更新前端订单状态为「已完成」
          const targetOrder = this.userOrders.find(item => item.orderId === order.orderId);
          if (targetOrder) {
            targetOrder.status = '已完成';  // 修改状态
          }
          alert('确认收货成功！');
        } else {
          alert(`操作失败：${response.data.message}`);
        }
      } catch (error) {
        alert('网络请求失败，请稍后重试');
      }
    },
     async handleDelete(order) {
      const confirmDelete = window.confirm('确定要删除该订单吗？');
      if (!confirmDelete) return;

      try {
        // 调用后端删除订单接口（假设接口路径为 /order/delete）
        const response = await axios.post('http://localhost:8081/user/deleteorder', {
          orderId: order.orderId,  // 订单ID
          userId: this.$route.query.id  // 用户ID（根据后端需求添加）
        }, {
          headers: { 'Content-Type': 'application/json' }
        });

        if (response.data.code === 200) {
          // 从前端列表中移除已删除的订单
          this.userOrders = this.userOrders.filter(item => item.orderId !== order.orderId);
          alert('订单删除成功！');
        } else {
          alert(`删除失败：${response.data.message}`);
        }
      } catch (error) {
        alert('网络请求失败，请稍后重试');
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

/* 个人信息展示样式 */
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

/* 地址相关样式 */
.address-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 4px;
}

.address-detail {
  flex: 1;
  color: #333;
  font-size: 14px;
  line-height: 1.5;
}

.delete-btn {
  background: #ff6b6b;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;  /* 新增过渡动画 */
}

/* 悬停时加深橙色 */
.address-item .delete-btn:hover {
  background: #f57c00;  /* 橙色加深 */
}
.add-address-btn {
  background: none;
  border: 1px dashed #ff6b6b;
  color: #ff6b6b;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

/* 模态框样式 */
.address-list{
  display: flex;
  flex-direction: column;
  gap: 10px;
  width:100%
}
.address-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px 30px;
  border-radius: 8px;
  min-width: 350px;
}

.modal-close {
  float: right;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.form-group {
  margin: 15px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.btn-confirm, .btn-cancel {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.btn-confirm {
  background: #ff6b6b;
  color: white;
}

.btn-cancel {
  background: #f0f0f0;
}

/* 订单区域样式 */
.order-list-card {
  max-width: 1200px;  /* 固定最大宽度与页面主内容区一致 */
  margin: 30px auto;
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.order-item {
  padding: 15px;
  margin-bottom: 15px;
  background: #fff;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  border: 1px solid #f0f0f0;
}

.order-basic {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.order-id {
  color: #666;
  font-size: 14px;
}

.order-time {
  color: #666;
  font-size: 14px;
}

.order-amount {
  color: #ff6b6b;
  font-size: 14px;
}

.product-thumbnail {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

/* 修正：将product-image改为product-img（匹配模板中的类名） */
.product-img {
  width: 60px;  /* 适当增大图片尺寸 */
  height: 60px;
  object-fit: contain;  /* 保持图片比例并填充 */
  border-radius: 4px;
  border: 1px solid #eee;  /* 添加浅色边框 */
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);  /* 轻微阴影提升质感 */
}

/* 新增：为商品信息区域添加分隔线 */
.order-products {
  padding: 12px 0;  /* 增加上下内边距 */
  border-top: 1px solid #f5f5f5;  /* 顶部浅色分隔线 */
  border-bottom: 1px solid #f5f5f5;  /* 底部浅色分隔线 */
  margin: 8px 0;  /* 增加与上下内容的间距 */
}

/* 优化：调整商品名称最大宽度适配更大屏幕 */
.product-name {
  max-width: 300px;  /* 从200px调整为300px */
  font-size: 14px;
  color: #2c3e50;  /* 加深文字颜色提升可读性 */
}


.product-info {
  display: flex;
  flex-direction: column;
}


.product-quantity {
  font-size: 12px;
  color: #999;
}

.order-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-待发货 {  /* 添加待发货状态样式 */
  background: #e3f2fd;
  color: #2196f3;
}

.status-已发货 {  /* 确保已发货样式存在 */
  background: #fff3e0;
  color: #ff9800;
}

.status-已完成 {
  background: #e8f5e9;
  color: #4caf50;
}

/* 修改删除按钮有效状态样式 */
.btn-delete {
  padding: 6px 12px;
  border: 1px solid #ff6b6b;  /* 红色边框 */
  border-radius: 4px;
  background-color: #ff6b6b;  /* 主色红色背景 */
  color: white;  /* 白色文字 */
  cursor: pointer;
  transition: all 0.3s;  /* 动画过渡 */
}

/* 悬停时加深红色 */
.btn-delete:hover:not(:disabled) {
  background-color: #ff4444;  /* 加深红色 */
  border-color: #ff4444;
}

/* 禁用状态保持原有样式（无需修改） */
.btn-delete:disabled {
  background: #e9ecef;
  color: #adb5bd;
  cursor: not-allowed;
  border-color: #e9ecef;
}

/* 修改：标记完成按钮有效状态样式 */
.btn-ship {
  padding: 6px 12px;
  border: 1px solid #2196f3;  /* 蓝色边框 */
  border-radius: 4px;
  background-color: #2196f3;  /* 蓝色背景 */
  color: white;  /* 白色文字 */
  cursor: pointer;
  transition: all 0.3s;  /* 动画过渡 */
}

/* 悬停时加深蓝色 */
.btn-ship:hover:not(:disabled) {
  background-color: #1976d2;  /* 蓝色加深 */
  border-color: #1976d2;
}

/* 禁用状态保持原有样式（无需修改） */
.btn-ship:disabled {
  background: #e9ecef;
  color: #adb5bd;
  cursor: not-allowed;
  border-color: #e9ecef;
}
</style>