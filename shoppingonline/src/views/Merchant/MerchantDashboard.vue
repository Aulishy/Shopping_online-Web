<template>
  <div class="merchant-container">
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

    <!-- 主内容区域 -->
    <main class="main-content">
      <!-- 商家主页信息（当前路由为/merchant-dashboard时显示） -->
      <div v-if="$route.path === '/merchant-dashboard'" class="dashboard-info">
        <div class="shop-card">
          <h3>店铺信息</h3>
          <!-- 新增：左右布局容器 -->
          <div class="shop-info-group">
            <!-- 左侧店铺图片 -->
            
            <!-- 右侧文字信息 -->
            <div class="shop-info-text">
              <p>店铺名称：{{ shopInfo.name }}</p>
              <p>商家地址：{{ shopInfo.address }}</p>
              <p>店铺分类：{{ shopInfo.category }}</p>
              <p>成立时间：{{ shopInfo.establishedDate  }}</p>
            </div>
          </div>
        </div>

        <!-- 修改：将原统计卡片替换为商品列表 -->
        <div class="product-list-card">
          <h3>我的商品</h3>
          <div class="product-list">
            <!-- 修改后的商品项结构 -->
            <div class="product-item" v-for="product in currentPageProducts" :key="product.id">
              <!-- 商品图片 -->
              <img :src="product.imageUrl" alt="商品图片" class="product-image">  <!-- 已有图片URL绑定 -->
              <!-- 卡片内容区域 -->
              <div class="product-card-content">
                <h4 class="product-name">{{ product.name }}</h4>
                <p class="product-price">¥{{ product.price }}</p>
                <!-- 新增容器包裹库存和已售 -->
                <div class="stock-sold-group">
                  <p 
                    class="product-stock" 
                    :style="{ color: product.stock < 20 ? '#e74c3c' : '#495057' }"
                  >
                    库存：{{ product.stock }}
                  </p>
                  <p class="product-sold">已售：{{ product.sold }}</p>
                </div>
                <div class="product-actions">
                  <button class="btn-add" @click="handleAddStock(product)">加库存</button>
                  <button class="btn-down" @click="handleTakeDown(product)">下架</button>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 新增分页控件 -->
          <div class="pagination-container">
            <button 
              class="page-btn" 
              @click="currentPage = Math.max(1, currentPage - 1)" 
              :disabled="currentPage === 1"
            >
              上一页
            </button>
            <span class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
            <button 
              class="page-btn" 
              @click="currentPage = Math.min(totalPages, currentPage + 1)" 
              :disabled="currentPage === totalPages"
            >
              下一页
            </button>
          </div>
        </div>

        <!-- 保留其他统计卡片 -->
        <div class="stats-container">
          <div class="stat-card">
            <p class="stat-title">总订单量</p>
            <p class="stat-value">{{ shopStats.totalOrders }}</p>
          </div>
          <div class="stat-card">
            <p class="stat-title">总收入</p>
            <p class="stat-value">¥{{ shopStats.totalIncome }}</p>
          </div>
        </div>
      </div>

      <!-- 其他页面内容会通过路由视图渲染（如订单管理/产品发布） -->
      <router-view v-else />
    </main>
  </div>
</template>

<script>
import axios from 'axios'; // 导入axios
export default {
  name: 'MerchantDashboard',
  data() {
    return {
      
      shopInfo: {}, // 动态数据（改为空对象）
      shopStats: {}, // 动态数据（改为空对象）
      shopProducts: [], // 动态数据（改为空数组）
      currentPage: 1, // 当前页码
      pageSize: 8, // 每页显示的商品数量
      
    };
  },
  async mounted() {
    
    const userId = this.$route.query.id;
    
    // 根据角色调用后端接口获取数据（示例）
    try {
      const response = await axios.post('http://localhost:8081/merchant/dashboard', {
        id:userId // 向后端传递角色参数
      });
      
      // 动态赋值数据
      this.shopInfo = response.data.shopInfo;
      this.shopStats = response.data.shopStats;
      this.shopProducts = response.data.shopProducts;
    } catch (error) {
      console.error('获取数据失败:', error);
    }
  },
  computed: {
    // 计算总页数
    totalPages() {
      return Math.ceil(this.shopProducts.length / this.pageSize);
    },
    // 计算当前页显示的商品
    currentPageProducts() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.shopProducts.slice(start, end);
    }
  },
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
  },
  handleAddStock(product) {
      // 弹出输入框（默认值为1，含确定/取消按钮）
      const input = window.prompt('请输入要增加的库存数量（需为正整数）：', '1');
      
      // 用户点击取消或输入为空
      if (input === null || input.trim() === '') return;
      
      // 验证输入是否为有效正整数
      const addAmount = parseInt(input, 10);
      if (isNaN(addAmount) || addAmount <= 0) {
        alert('错误：请输入有效的正整数');
        return;
      }
      
      // 发送更新请求到后端
      axios.post('http://localhost:8081/merchant/updateProducts', {
        productId: product.id,
        addAmount: addAmount
      })
        .then(response => {
          if (response.data.code === 200) {
            
            product.stock += addAmount;
            alert('库存更新成功！');
          } else {
            alert(`库存更新失败：${response.data.message}`);
          }
        })
        .catch(error => {
          console.error('库存更新请求失败:', error);
          alert('库存更新请求失败，请稍后重试');
        });
    },
    // 新增下架逻辑
    async handleTakeDown(product) {
      const confirmTakeDown = window.confirm('确定要下架该商品吗？');
      if (!confirmTakeDown) return;

      try {
        const response = await axios.post('http://localhost:8081/merchant/deleteProducts', {
          productId: product.id,
          shopId: this.$route.query.id
        });

        if (response.data.code === 200) {
          // 从商品列表中移除已下架的商品
          this.shopProducts = this.shopProducts.filter(item => item.id !== product.id);
          // 如果当前页没有商品了，自动跳转到上一页
          if (this.currentPageProducts.length === 0 && this.currentPage > 1) {
            this.currentPage--;
          }
          alert('商品下架成功！');
        } else {
          alert(`商品下架失败：${response.data.message}`);
        }
      } catch (error) {
        console.error('商品下架请求失败:', error);
        alert('商品下架请求失败，请稍后重试');
      }
    }

 }
};


</script>

<style scoped>
.merchant-container {
  display: flex;
  min-height: 100vh;
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

.sidebar-header {
  padding: 15px 0;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  margin-bottom: 20px;
  /* 头部内容自然高度，不额外设置高度 */
}

.sidebar-menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1; /* 新增：自动填充剩余空间 */
  /* 移除原 height:100%，避免与 Flex 布局冲突 */
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

.main-content {
  margin-left: 270px; 
  flex: 1;
  padding: 30px;
  background: #f8f9fa;
   
}

.dashboard-info {
  max-width: 1200px;
  margin: 0 auto;
}

.shop-card {
  background: white;
  border-radius: 12px;
  padding: 10px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  margin-bottom: 30px;
}

.shop-card h3 {
  color: #2c3e50;
  margin-top: 0;
  margin-bottom: 15px;
}

.shop-card p {
  color: #495057;
  margin: 8px 0;
  font-size: 15px;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 25px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  text-align: center;
}

.stat-title {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  color: #2c3e50;
  font-size: 24px;
  font-weight: bold;
}

.product-list-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  margin-bottom: 30px;
  margin-top: 10px;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 新增：每行4列 */
  gap: 15px;
  margin-top: 15px;
}

/* 新增商品卡片样式 */
.product-item {
  padding: 15px;
  border: 1px solid #f0f2f5;
  border-radius: 12px; /* 圆角卡片 */
  background: white;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05); /* 柔和阴影 */
  transition: all 0.3s;
  overflow: hidden; /* 防止图片圆角溢出 */
}

.product-item:hover {
  transform: translateY(-2px); /* 悬停微浮效果 */
  box-shadow: 0 6px 16px rgba(0,0,0,0.1);
}

.product-image {
  width: 100%;
  height: 180px; /* 固定图片高度 */
  object-fit: contain; /* 保持比例填充 */
  border-radius: 8px; /* 图片圆角 */
  margin-bottom: 12px;
}

.product-card-content {
  display: flex;
  flex-direction: column;
  gap: 2px; /* 减少整体行距（原 gap:8px 可根据需求调整） */
  min-height: 120px;
  justify-content: space-between;
}

.stock-sold-group {
  display: flex;          /* 水平排列 */
  justify-content: space-between; /* 两端对齐 */
  gap: 15px;             /* 库存和已售文字间的间距 */
  align-items: center;   /* 垂直居中对齐 */
}

.product-stock, .product-sold {
  margin: 0;  /* 清除 p 标签默认 margin，避免额外间距 */
  color: #495057;
  font-size: 14px;
}

.product-actions {
  display: flex;
  gap: 10px;
  margin-left: auto; /* 关键：按钮组右对齐 */
}

.btn-add, .btn-down {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-add {
  background: #2196F3;
  color: white;
}

.btn-add:hover {
  background: #1976D2; /* 悬停加深色 */
}

.btn-down {
  background: #ffe8e8;
  color: #e74c3c;
  border: 1px solid #ffe8e8;
}

.btn-down:hover {
  background: #ffe0e0;
}
/* 新增分页控件样式 */
.pagination-container {
  display: flex;
  justify-content: center;  /* 居中显示 */
  gap: 15px;                /* 按钮间间距 */
  margin-top: 30px;         /* 与商品列表的间距 */
}

.page-btn {
  padding: 8px 16px;        /* 按钮内边距 */
  border: 1px solid #2196F3; /* 蓝色边框 */
  background: #2196F3;      /* 主色调背景 */
  color: white;             /* 白色文字 */
  border-radius: 6px;       /* 圆角 */
  cursor: pointer;
  transition: all 0.3s ease; /* 过渡动画 */
  font-size: 14px;          /* 字体大小 */
}

.page-btn:hover:not(:disabled) {
  background: #1976D2;      /* 悬停加深色 */
  border-color: #1976D2;
}

.page-btn:disabled {
  background: #e9ecef;      /* 禁用背景色 */
  border-color: #e9ecef;
  color: #adb5bd;           /* 禁用文字色 */
  cursor: not-allowed;      /* 禁用光标 */
}

.page-info {
  color: #495057;           /* 信息文字色 */
  font-size: 14px;          /* 字体大小 */
  display: flex;
  align-items: center;      /* 与按钮垂直居中 */
}
.product-name {
  margin: 0; /* 清除 h4 默认的上下边距 */
  color: #2c3e50; /* 保持原有文字颜色 */
  font-size: 16px; /* 保持原有字号 */
}

.product-price {
  margin: 0; /* 清除 p 标签默认的上下边距 */
  color: #e74c3c; /* 保持原有价格颜色 */
  font-weight: bold; /* 保持原有字体粗细 */
  font-size: 15px; /* 保持原有字号 */
}

.shop-info-group {
  display: flex;          /* 左右排列 */
  justify-content: space-around; /* 两端对齐 */
  gap: 20px;             /* 图片与文字间距 */
  align-items: center;   /* 垂直居中对齐 */
  margin-top: 15px;       /* 与标题的间距 */
}

.shop-logo {
  width: 120px;           /* 图片宽度 */
  height: 120px;          /* 图片高度 */
  object-fit: contain;      /* 图片填充方式（覆盖容器，可能裁剪） */
  border-radius: 8px;     /* 图片圆角 */
}

.shop-info-text {
  flex: 1;                /* 文字区域占满剩余空间 */
  text-align: left; /* 文字左对齐 */
}

.product-card-content {
  display: flex;
  flex-direction: column;
  gap: 2px; /* 减少整体行距（原 gap:8px 可根据需求调整） */
  min-height: 120px;
  justify-content: space-between;
}

.stock-sold-group {
  display: flex;          /* 水平排列 */
  justify-content: space-between; /* 两端对齐 */
  gap: 15px;             /* 库存和已售文字间的间距 */
  align-items: center;   /* 垂直居中对齐 */
}

.product-stock, .product-sold {
  margin: 0;  /* 清除 p 标签默认 margin，避免额外间距 */
  color: #495057;
  font-size: 14px;
}

.product-actions {
  display: flex;
  gap: 10px;
  margin-left: auto; /* 关键：按钮组右对齐 */
}

.btn-add, .btn-down {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-add {
  background: #2196F3;
  color: white;
}

.btn-add:hover {
  background: #1976D2; /* 悬停加深色 */
}

.btn-down {
  background: #ffe8e8;
  color: #e74c3c;
  border: 1px solid #ffe8e8;
}

.btn-down:hover {
  background: #ffe0e0;
}
/* 新增分页控件样式 */
.pagination-container {
  display: flex;
  justify-content: center;  /* 居中显示 */
  gap: 15px;                /* 按钮间间距 */
  margin-top: 30px;         /* 与商品列表的间距 */
}

.page-btn {
  padding: 8px 16px;        /* 按钮内边距 */
  border: 1px solid #2196F3; /* 蓝色边框 */
  background: #2196F3;      /* 主色调背景 */
  color: white;             /* 白色文字 */
  border-radius: 6px;       /* 圆角 */
  cursor: pointer;
  transition: all 0.3s ease; /* 过渡动画 */
  font-size: 14px;          /* 字体大小 */
}

.page-btn:hover:not(:disabled) {
  background: #1976D2;      /* 悬停加深色 */
  border-color: #1976D2;
}

.page-btn:disabled {
  background: #e9ecef;      /* 禁用背景色 */
  border-color: #e9ecef;
  color: #adb5bd;           /* 禁用文字色 */
  cursor: not-allowed;      /* 禁用光标 */
}

.page-info {
  color: #495057;           /* 信息文字色 */
  font-size: 14px;          /* 字体大小 */
  display: flex;
  align-items: center;      /* 与按钮垂直居中 */
}
.product-name {
  margin: 0; /* 清除 h4 默认的上下边距 */
  color: #2c3e50; /* 保持原有文字颜色 */
  font-size: 16px; /* 保持原有字号 */
}

.product-price {
  margin: 0; /* 清除 p 标签默认的上下边距 */
  color: #e74c3c; /* 保持原有价格颜色 */
  font-weight: bold; /* 保持原有字体粗细 */
  font-size: 15px; /* 保持原有字号 */
}
</style>


