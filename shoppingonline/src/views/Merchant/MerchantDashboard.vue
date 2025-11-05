<template>
  <div class="merchant-container">
    <!-- 侧边栏导航 -->
    <MerchantSidebar />
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
import MerchantSidebar from '@/components/common/MerchantSidebar.vue';
export default {
  name: 'MerchantDashboard',
  components: {
    MerchantSidebar
  },
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
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  margin-top: 15px;
}

.product-item {
  padding: 15px;
  border: 1px solid #f0f2f5;
  border-radius: 12px;
  background: white;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  transition: all 0.3s;
  overflow: hidden;
}

.product-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.1);
}

.product-image {
  width: 100%;
  height: 180px;
  object-fit: contain;
  border-radius: 8px;
  margin-bottom: 12px;
}

.product-card-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-height: 120px;
  justify-content: space-between;
}

.stock-sold-group {
  display: flex;
  justify-content: space-between;
  gap: 15px;
  align-items: center;
}

.product-stock, .product-sold {
  margin: 0;
  color: #495057;
  font-size: 14px;
}

.product-actions {
  display: flex;
  gap: 10px;
  margin-left: auto;
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
  background: #1976D2;
}

.btn-down {
  background: #ffe8e8;
  color: #e74c3c;
  border: 1px solid #ffe8e8;
}

.btn-down:hover {
  background: #ffe0e0;
}

.pagination-container {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 30px;
}

.page-btn {
  padding: 8px 16px;
  border: 1px solid #2196F3;
  background: #2196F3;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.page-btn:hover:not(:disabled) {
  background: #1976D2;
  border-color: #1976D2;
}

.page-btn:disabled {
  background: #e9ecef;
  border-color: #e9ecef;
  color: #adb5bd;
  cursor: not-allowed;
}

.page-info {
  color: #495057;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.product-name {
  margin: 0;
  color: #2c3e50;
  font-size: 16px;
}

.product-price {
  margin: 0;
  color: #e74c3c;
  font-weight: bold;
  font-size: 15px;
}

.shop-info-group {
  display: flex;
  justify-content: space-around;
  gap: 20px;
  align-items: center;
  margin-top: 15px;
}

.shop-logo {
  width: 120px;
  height: 120px;
  object-fit: contain;
  border-radius: 8px;
}

.shop-info-text {
  flex: 1;
  text-align: left;
}
</style>