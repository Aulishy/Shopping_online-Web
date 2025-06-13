<template>
  <div class="user-container">
    <!-- 顶部导航栏 -->
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

    <!-- 主内容区域 -->
    <main class="main-content">
      <!-- 搜索框 -->
      <div class="search-container">
        <input
          type="text"
          v-model="searchKeyword"
          placeholder="搜索商品名称/类别..."
          class="search-input"
          @input="handleSearch" 
          @keyup.enter="handleSearch"
        />
        <button class="search-btn" @click="handleSearch">搜索</button>
      </div>

      <!-- 商品分类导航 -->
      <nav class="category-nav">
        <button
          v-for="category in categories"
          :key="category"
          :class="{ active: currentCategory === category }"
         @click="handleCategoryChange(category)" 
          class="category-item"
        >
          {{ category }}
        </button>
      </nav>

      <!-- 热销商品列表 -->
       <!-- 热销商品列表 -->
      <div class="hot-products">
        <h3 class="section-title">热销商品</h3>
        <div class="product-grid">
          <!-- 修改：为商品卡片添加点击事件 -->
          <div
            v-for="product in hotProducts"
            :key="product.product_id" 
            class="product-card"
            @click="handleProductClick(product)"  
          >
            <img :src="product.imageUrl" alt="商品图片" class="product-img" />
            <div class="product-info">
              <p class="product-name">{{ product.product_name }}</p>
              <p class="product-price">¥{{ product.price }}</p>
              <p class="product-sold">已售：{{ product.sold }}</p>
            </div>
          </div>
        </div>
      </div>

       <!-- 新增：购买模态框 -->
      <div v-if="showPurchaseModal" class="purchase-modal">
        <div class="modal-overlay" @click="showPurchaseModal = false"></div>
        <div class="modal-content">
          <span class="modal-close" @click="showPurchaseModal = false">&times;</span>
          <h3>购买 {{ selectedProduct?.product_name }}</h3>
          <div class="modal-body">
            <img :src="selectedProduct?.imageUrl" alt="商品图片" class="modal-product-img" />
            <div class="purchase-info">
              <p>价格：¥{{ selectedProduct?.price }}</p>
              <p>库存：{{ selectedProduct?.stock }}</p>
              <div class="form-group">
            <label>选择收货信息：</label>
            <select 
              v-model="selectedAddress" 
              class="address-select"
              required
            >
              <option value="">请选择地址</option>
              <!-- 遍历用户地址列表 -->
              <option v-for="(addr, index) in userAddresses" :key="index" :value="addr">
                {{ addr }}
              </option>
            </select>
            </div>
                <div class="form-group">
            <label>购买数量：</label>
            <input
              type="number"
              v-model="purchaseQuantity"
              min="1"
              :max="selectedProduct?.stock"
              required
            />
          </div>
            <div class="form-group">
    <label>备注：</label>
    <input
      type="text"
      v-model="purchaseNote" 
      placeholder="选填：如配送时间要求等"
    />
  </div>
          
          </div>
          </div>
          <div class="modal-actions">
            <button class="btn-confirm" @click="handlePurchase">确认购买</button>
            <button class="btn-cancel" @click="showPurchaseModal = false">取消</button>
          </div>
        </div>
        
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'UserHome',
  data() {
    return {
      searchKeyword: '',
      currentCategory: '全部',
      categories: ['全部', '电子产品', '服装鞋包', '家居日用', '食品饮料'],
      hotProducts: [],
      originalProducts: [], // 新增：存储未过滤的原始商品数据
      userAddress: '', 
      showPurchaseModal: false,  // 控制购买模态框显示
      selectedProduct: null,     // 当前选中的商品
      purchaseQuantity: 1        // 购买数量
    };
  },
  async mounted() {
    try {
      const params = { currentCategory: this.currentCategory };  
      const response = await axios.post('http://localhost:8081/user/home', params);
      this.hotProducts = response.data.data;
      this.originalProducts = [...this.hotProducts]; // 在挂载时保存原始数据
    } catch (error) {
      console.error('获取热销商品失败:', error);
    }
    await this.fetchUserProfile();
  },
  methods: {
    handleCategoryChange(newCategory) {
      this.currentCategory = newCategory;
      this.fetchHotProducts();  // 触发数据请求
    },
    // 新增：将请求逻辑封装为独立方法（复用性更好）
    async fetchHotProducts() {
      try {
        const params = { currentCategory: this.currentCategory };
        const response = await axios.post('http://localhost:8081/user/home', params);
        this.originalProducts = response.data.data; // 保存原始数据
        this.hotProducts = this.originalProducts;   // 初始显示原始数据
        console.log('更新后的 hotProducts:', this.hotProducts);
        this.$forceUpdate();
      } catch (error) {
        console.error('获取热销商品失败:', error);
      }
    },
    handleSearch() {
      const keyword = this.searchKeyword.trim().toLowerCase();
      if (!keyword) {
        this.hotProducts = this.originalProducts;
        return;
      }
      // 仅保留商品名称的匹配逻辑（移除类别匹配）
      this.hotProducts = this.originalProducts.filter(product => {
        return product.product_name.toLowerCase().includes(keyword);
      });
    },
  
    // 退出登录（保持不变）
    handleLogout() {
      const confirmLogout = window.confirm('确定要退出登录吗？');
      if (confirmLogout) {
        localStorage.removeItem('userToken');
        this.$router.push('/');
      }
    },

     handleProductClick(product) {
      this.selectedProduct = product;  // 存储选中的商品信息
      this.showPurchaseModal = true;   // 显示模态框
      this.purchaseQuantity = 1;       // 重置数量
    },
    async fetchUserProfile() {
      try {
        const userId = this.$route.query.id;
        const response = await axios.post('http://localhost:8081/user/info', { userId });
        if (response.data.code === 200) {
          // 假设后端返回的地址数组在 response.data.addresses
          this.userAddresses = response.data.addresses;
        } else {
          alert(`获取地址失败：${response.data.message}`);
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
      }
    },
    
    // 修改：购买逻辑中传递完整地址信息
    async handlePurchase() {
      if (!this.selectedProduct) return;
      if (this.purchaseQuantity < 1 || this.purchaseQuantity > this.selectedProduct.stock) {
        alert('购买数量不合法');
        return;
      }
      if (!this.selectedAddress) { // 校验是否选择了地址
        alert('请选择收货地址');
        return;
      }

      try {
        const response = await axios.post('http://localhost:8081/user/purchase', {
          productId: this.selectedProduct.product_id,
          quantity: this.purchaseQuantity,
          userId: this.$route.query.id,
          // 传递完整地址信息或 addressId（根据后端需求）
          addressId: this.selectedAddress.addressId,
          
          note: this.purchaseNote  
        });

        if (response.data.code === 200) {
          alert('购买成功！');
          this.showPurchaseModal = false;
          this.selectedProduct.stock -= this.purchaseQuantity;
        } else {
          alert(`购买失败：${response.data.message}`);
        }
      } catch (error) {
        alert('网络请求失败，请稍后重试');
      }
    }
  }
};
</script>

<style scoped>
.user-container {
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* 顶部导航栏 */
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

/* 修改后的退出登录按钮 */
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

/* 搜索框 */
.search-container {
  max-width: 800px;
  margin: 30px auto;
  display: flex;
  gap: 0;
}

.search-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #e9ecef;
  border-radius: 8px 0 0 8px;
  font-size: 14px;
}

.search-btn {
  background-color: #ff6b6b;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 0 8px 8px 0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #ff5252;
}

/* 商品分类导航 */
.category-nav {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin: 30px auto;
  max-width: 1200px;
  flex-wrap: wrap;
}

.category-item {
  background-color: #fff5f5;
  color: #ff6b6b;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-item:hover, .category-item.active {
  background-color: #ff6b6b;
  color: white;
}

/* 热销商品 */
.hot-products {
  max-width: 1200px;
  margin: 30px auto;
}

.section-title {
  color: #333;
  font-size: 22px;
  margin-bottom: 20px;
  text-align: center;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 25px;
}

/* 热销商品卡片 */
.product-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden; /* 已有溢出隐藏 */
 
  transition: transform 0.3s;
  height: 350px; /* 新增：固定卡片整体高度 */
}

.product-info {
  padding: 10px;
  height: 120px; /* 新增：固定信息区域高度（总高度350px - 图片高度200px - padding 30px） */
  overflow: hidden; /* 新增：信息区域内容溢出时隐藏 */
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-img {
  width: 100%;
  height: 200px;
  object-fit: contain;
}


.product-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 1px;
  height: 20px;
  overflow: hidden;
  
}

.product-price {
  font-size: 18px;
  color: #ff6b6b;
  font-weight: bold;
  margin-bottom: 3px;
}

.product-sold {
  font-size: 14px;
  color: #6c757d;
  margin-top:2px;
}

/* 新增：购买模态框样式 */
.purchase-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
}

.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.modal-content {
  position: relative;
  background: white;
  max-width: 600px;
  margin: 100px auto;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.modal-close {
  position: absolute;
  top: 10px;
  right: 20px;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.modal-product-img {
  max-width: 200px;
  height: auto;
  margin-right: 20px;
  border-radius: 4px;
}

.modal-body {
  display: flex;
  align-items: center;
  margin: 20px 0;
}

.purchase-info {
  flex: 1;
}

.form-group {
  margin: 15px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.form-group input {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-confirm {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel {
  background: #f44336;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
.address-select {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100%;
  max-width: 300px;
}

.manage-address-link {
  display: block;
  margin-top: 8px;
  color: #2196F3;
  text-decoration: none;
  font-size: 14px;
}

.manage-address-link:hover {
  text-decoration: underline;
}

</style>