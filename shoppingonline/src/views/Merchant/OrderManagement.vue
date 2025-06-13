<template>
  <div class="merchant-container">
    <!-- 侧边栏导航（保持不变） -->
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

    <!-- 主内容区域 -->
    <main class="main-content">
      <div class="order-list-card">
        <!-- 订单搜索/筛选栏（新增按钮） -->
        <div class="order-filter">
          <input 
            type="text" 
            placeholder="搜索订单号/商品名称" 
            class="search-input"
            v-model="searchKeyword"
          >
          <select 
            class="status-select"
            v-model="selectedStatus"  
          >
            <option value="">全部状态</option>
            <option value="待发货">待发货</option>
            <option value="已发货">已发货</option>
            <option value="已完成">已完成</option>
          </select>
          <!-- 新增搜索按钮 -->
          <button 
            class="btn-search" 
            @click="handleSearch"
          >
            搜索
          </button>
        </div>

        <!-- 订单列表 -->
        <div class="order-list">
          <div class="order-item" v-for="order in filteredOrders" :key="order.orderId" @click="showOrderDetail(order)"> <!-- 修改：key 改为 orderId -->
            <!-- 订单基础信息（修改订单号字段） -->
            <div class="order-basic">
              <span class="order-id">订单号：{{ order.orderId }}</span> <!-- 修改：id → orderId -->
              <span class="order-time">{{ formatTime(order.createTime) }}</span>
              <span class="order-amount">成交金额：¥{{ order.totalAmount }}</span>
            </div>

            <!-- 商品信息（修改为单个商品渲染） -->
            <div class="order-products">
              <div class="product-thumbnail"> <!-- 移除 v-for，直接渲染单个商品 -->
                <img :src="order.productImageUrl" alt="商品图" class="product-img"> <!-- 使用 productImageUrl -->
                <div class="product-info">
                  <p class="product-name">{{ order.productName }}</p> <!-- 使用 productName -->
                  <p class="product-quantity">x{{ order.productBuy }}</p> <!-- 使用 productBuy（购买数量） -->
                
                </div>
              </div>
            </div>

            <!-- 订单状态与操作（保持不变） -->
            <div class="order-actions">
              <span 
                class="status-tag" 
                :class="['status-' + order.status, order.status]"
              >
                {{ order.statusText }} <!-- 直接使用后端返回的 statusText -->
              </span>
              <button 
                class="btn-ship" 
                @click="handleShip(order, $event)" 
                :disabled="order.status !== 'pending'"
              >
                标记发货
              </button>
              <button 
                class="btn-delete" 
                @click="handleDelete(order, $event)" 
                :disabled="order.status !== 'completed'" 
              >
                删除订单
              </button>
            </div>
          </div>
        </div>
        <!-- 分页组件（新增） -->
       <div class="pagination">
    <button 
      @click="currentPage = Math.max(1, currentPage - 1)" 
      :disabled="currentPage === 1"
    >
      上一页
    </button>
    <span>第 {{ currentPage }} / {{ totalPages }} 页</span>
    <button 
      @click="currentPage = Math.min(totalPages, currentPage + 1)" 
      :disabled="currentPage === totalPages"
    >
      下一页
    </button>
  </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'OrderManagement',
  data() {
    return {
      searchKeyword: '',
      selectedStatus: '', // 若已删除状态筛选，可移除该字段
      orders: [],
      currentPage: 1, // 新增：初始化当前页码（默认第1页）
      pageSize: 10
    };
  },
  async mounted() {
    try {
      const response = await axios.post('http://localhost:8081/merchant/ordermanagement', {
         shopId: this.$route.query.id  // 使用路由参数获取商家ID
      });
      if (response.data.code === 200) {
        this.orders = response.data.data; // 直接使用后端返回的 data 数组
      } else {
        console.error('获取订单列表失败:', response.data.message);
        alert('获取订单列表失败，请稍后重试');
        }
      } catch (error) {
        console.error('请求订单列表失败:', error);
        alert('请求订单列表失败，请稍后重试');
      }
    },
  
  computed: {
    // 计算属性：根据搜索条件过滤订单
   totalPages() {
      return Math.ceil(this.filteredOrdersTotal.length / this.pageSize);
    },
    // 计算属性：过滤后的总订单数（用于计算总页数）
    filteredOrdersTotal() {
      return this.orders.filter(order => {
        const statusMatch = this.selectedStatus ? order.statusText === this.selectedStatus : true; // 修改：匹配 statusText
        const keywordMatch = 
          order.orderId.includes(this.searchKeyword) ||  // 修改：id → orderId
          order.productName.includes(this.searchKeyword); // 修改：product.name → productName
        return statusMatch && keywordMatch;
      });
    },
    // 计算属性：当前页显示的订单（关键分页逻辑）
    filteredOrders() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredOrdersTotal.slice(start, end);
    }
    
  },
  methods: {
    // 处理搜索按钮点击
    handleSearch() {
      this.currentPage = 1; // 重置到第一页
        // 计算过滤后的订单（触发计算属性重新计算）
        this.filteredOrders; // 触发计算属性更新
      },
    // 新增：处理订单状态筛选
    handleStatusFilter() {
      this.currentPage = 1; // 重置到第一页
      // 计算过滤后的订单（触发计算属性重新计算）
      this.filteredOrders; // 触发计算属性更新

    },
    // 原有标记发货逻辑（保持不变）
    async handleShip(order, event) {
      event.stopPropagation(); // 阻止事件冒泡
      if (order.status !== 'pending') return;

      const logisticsCompany = window.prompt('请输入物流公司名称：');
      if (!logisticsCompany) {
        alert('物流公司名称不能为空，请重新操作。');
        return;
      }

      try {
        // 调用后端接口更新订单状态和物流公司信息
        const response = await axios.post('http://localhost:8081/order/ship', {
          orderId: order.orderId, // 修改：使用 orderId
          logisticsCompany: logisticsCompany
        });

        if (response.data.code === 200) {
          order.status = 'shipped';
          order.statusText = '已发货'; // 同步更新 statusText
          alert('订单已成功标记为发货！');
        } else {
          alert(`订单发货失败：${response.data.message}`);
        }
      } catch (error) {
        console.error('订单发货请求失败:', error);
        alert('订单发货请求失败，请稍后重试');
      }
    },
    showBuyerInfo(order) {
      const info = `买家用户 ID: ${order.userId || '未提供'}\n收货人姓名: ${order.receiverName || '未提供'}\n收货地址: ${order.receiverAddress || '未提供'}\n收件人联系方式: ${order.receiverPhone || '未提供'}`;
      window.alert(info);
    },
    // 新增删除订单逻辑
     async handleDelete(order, event) {  // 改为 async 方法以使用 await
      event.stopPropagation(); // 阻止事件冒泡
      if (order.status !== 'completed') return; // 非已完成订单直接返回

      const confirmDelete = window.confirm('确定要删除该已完成订单吗？');
      if (!confirmDelete) return;

      try {
        // 调用后端删除订单接口（假设接口为 /order/delete，需根据实际调整）
        const response = await axios.post('http://localhost:8081/merchant/deleteorder', {
          orderId: order.orderId, // 传递订单ID
          shopId: this.$route.query.id // 传递商家ID（根据后端需求）
        });

        if (response.data.code === 200) {
          // 后端删除成功后，更新前端数组
          this.orders = this.orders.filter(item => item.orderId !== order.orderId);
          alert('订单删除成功！');
        } else {
          alert(`订单删除失败：${response.data.message}`);
        }
      } catch (error) {
        console.error('删除订单请求失败:', error);
        alert('删除订单请求失败，请稍后重试');
      }
    },
     //退出登录
  handleLogout() {
    const confirmLogout = window.confirm('确定要退出登录吗？');
    if (confirmLogout) {
      // 清除登录状态（如有）
      localStorage.removeItem('userToken');
      this.$router.push('/'); // 跳转到首页或登录页
    }
  },
  formatTime(utcTime) {
    const date = new Date(utcTime);
    return date.toLocaleString(); // 转换为本地时间格式（如 "2025/5/30 18:19:52"）
  },
    showOrderDetail(order) {
        // 显示订单详情逻辑（可根据实际需求实现）
        const detail = `订单号: ${order.orderId}\n收件人: ${order.receiverName}\n收件地址: ${order.receiverAddress}
收件人电话:${order.receiverPhone}\n下单用户:${order.userId}\n商品名称: ${order.productName}\n购买数量: ${order.productBuy}
总金额: ¥${order.totalAmount}\n购买详情:${order.buyInfo}\n状态: ${order.statusText}
创建时间: ${this.formatTime(order.createTime)}\n物流公司: ${order.logisticsCompany || '未发货'}`;
        
        window.alert(detail);
        
    }
}
}
</script>

<style scoped>
/* ... 原有 merchant-container/sidebar 等样式保持不变 ... */

.page-title {
  color: #2c3e50;
  font-size: 24px;
  margin-bottom: 25px;
}

.order-list-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}

.order-filter {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  padding: 8px 12px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  flex: 1;
}
.main-content {
  margin-left: 270px; 
  flex: 1;
  padding: 30px;
  background: #f8f9fa;
   
}
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

.status-select {
  padding: 8px 12px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  min-width: 120px;
}

.order-item {
  padding: 10px;
  border: 1px solid #f0f2f5;
  border-radius: 8px;
  margin-bottom: 15px;
  transition: all 0.3s;
  position: relative;  /* 新增：相对定位容器 */
  height:100px; /* 新增：设置固定高度，确保内容区域一致 */
  padding-bottom: 10px;  /* 新增：为底部操作按钮留出空间 */
}

.order-actions {
  display: flex;
  align-items: center;
  gap: 15px;
  /* 新增：绝对定位到右下角 */
  position: absolute;
  bottom: 15px;  /* 距离卡片底部15px（与卡片内边距一致） */
  right: 15px;   /* 距离卡片右侧15px（与卡片内边距一致） */
}

.order-item:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.order-basic {
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 14px;
  margin-bottom: 5px;
  gap: 5px; /* 新增：为元素间添加间距 */
  flex-wrap: wrap; /* 新增：允许换行 */
}

.order-amount {
  color: #e74c3c; /* 红色突出显示金额 */
  font-weight: bold; /* 加粗 */
}

.order-products {
  display: flex;
  gap: 5px;
  margin-bottom: 30px;  /* 新增：增大底部边距，避免被操作按钮遮挡 */
}

.product-thumbnail {
  display: flex;
  align-items: center;
  gap: 5px;
}

.product-img {
  width: 50px;
  height: 50px;
  object-fit: contain;
  border-radius: 4px;
}

.product-name {
  color: #2c3e50;
  font-size: 15px;
  min-width: 120px;
}

.product-quantity {
  color: #666;
  font-size: 14px;
}

.order-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.status-pending {
  background: #e3f2fd;
  color: #2196F3;
}

.status-shipped {
  background: #e8f5e9;
  color: #4caf50;
}

.status-completed {
  background: #fff3e0;
  color: #ff9800;
}

.btn-ship {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background: #2196F3;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-ship:disabled {
  background: #e9ecef;
  color: #adb5bd;
  cursor: not-allowed;
}
.btn-search {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  background: #2196F3;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-search:hover {
  background: #1976D2;
}

.btn-delete {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background: #e74c3c; 
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-delete:disabled {
  background: #e9ecef;
  color: #adb5bd;
  cursor: not-allowed;
}

.btn-delete:hover:not(:disabled) {
  background: #c0392b; /* 悬停加深红色 */
}


.pagination {
  margin-top: 20px;
  display: flex;
  gap: 20px;
  justify-content: center;
  align-items: center;
}

.pagination button {
  padding: 6px 12px;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
}

.pagination button:hover:not(:disabled) {
  background: #f8f9fa;
}

.pagination button:disabled {
  color: #adb5bd;
  cursor: not-allowed;
}
</style>

