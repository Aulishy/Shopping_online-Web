<template>
  <div class="merchant-container">
    <!-- 侧边栏导航（使用封装的组件） -->
    <AdminSidebar 
      :adminId="$route.query.id"
      @logout="handleLogout"
    />

    <!-- 主内容区域 -->
    <main class="main-content">
      <!-- 标题与数量并排显示 -->
      <div class="title-count-group">
        <h2 class="page-title">产品上新审核</h2>
        <p class="audit-count-text">待审核总数：{{ pendingProducts.length }}</p>
      </div>

      <div v-if="loading" class="loading-tip">加载中...</div>
      
      <div v-else class="merchant-audit-list">
        <!-- 审核列表内容 -->
        <div v-for="product in pendingProducts" :key="product.id" class="audit-item">
          <!-- 审核信息表格区域 -->
          <div class="audit-table">
            <!-- 第一行（5列） -->
            <div class="table-row">
              <div class="table-cell"><strong>产品名称：</strong>{{ product.product_name }}</div>
              <div class="table-cell"><strong>价格：</strong>{{ product.price}}</div>
              <div class="table-cell"><strong>库存：</strong>{{ product.stock }}</div>
              <div class="table-cell"><strong>分类：</strong>{{ product.category }}</div>
              <div class="table-cell"><strong>描述：</strong>{{ product.description }}</div>
            </div>
            
            <!-- 图片展示 -->
            <div class="audit-images">
              <!-- 产品图片 -->
              <div class="image-group">
                <p><strong>产品图片：</strong></p>
                <img 
                  :src="product.image_url" 
                  alt="产品图片" 
                  class="audit-image" 
                  @click="handleImageClick(product.image_url)"
                >
              </div>
            </div>
          </div>
          <!-- 操作按钮 -->
          <div class="audit-actions">
            <button class="btn-approve" @click="handleApprove(product.paudit_id)">通过审核</button>
            <button class="btn-reject" @click="handleReject(product.paudit_id)">拒绝申请</button>
          </div>
        </div>
        <!-- 图片放大模态框 -->
        <div v-if="showImageModal" class="image-modal">
          <div class="modal-content">
            <span class="modal-close" @click="showImageModal = false">&times;</span>
            <img :src="currentImageUrl" alt="放大图片" class="modal-image">
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';
import AdminSidebar from '@/components/common/AdminSidebar.vue';

export default {
  name: 'ProductAudit',
  components: {
    AdminSidebar
  },
  data() {
    return {
      pendingProducts: [],
      loading: false,
      showImageModal: false, // 控制模态框显示
      currentImageUrl: ''   // 当前点击的图片URL
    };
  },
  async mounted() {
    await this.fetchPendingMerchants();
  },
  methods: {
    // 获取待审核商家列表
    async fetchPendingMerchants() {
      this.loading = true;
      try {
        const res = await axios.post('http://localhost:8081/admin/pending-products', {});
        // 过滤出未审核（audit_status=0）的商家
        this.pendingProducts = res.data.data
          .map(product => ({
            ...product,
            price: Number(product.price), // 字符串转数字
            stock: Number(product.stock)  // 字符串转数字
          }));
      } catch (error) {
        console.error('获取待审核商家失败:', error);
      } finally {
        this.loading = false;
      }
    },

    // 处理审核通过
    async handleApprove(pauditId) {
      if (!window.confirm('确认通过该产品上新申请？')) return;

      try {
        const response = await axios.post('http://localhost:8081/admin/approve-product', { 
          pauditId
        });

        if (response.data.code === 200) {
          this.pendingProducts = this.pendingProducts.filter(
            product => product.paudit_id !== pauditId
          );
          alert('审核通过成功！');
        } else {
          alert(`审核通过失败：${response.data.message}`);
        }
      } catch (error) {
        alert('网络请求失败，请稍后重试');
      }
    },

    // 处理审核拒绝
    async handleReject(pauditId) {
      const reason = window.prompt('请输入拒绝理由（必填）:', '');
      if (!reason?.trim()) {
        alert('拒绝理由不能为空');
        return;
      }
      try {
        await axios.post('http://localhost:8081/admin/reject-product', { 
          pauditId, 
          reason 
        });
        this.pendingProducts = this.pendingProducts.filter(
            product => product.paudit_id !== pauditId
          );
        alert('已记录拒绝理由');
      } catch (error) {
        alert('审核拒绝失败: ' + error.response?.data?.message || '服务器错误');
      }
    },

    // 处理图片点击事件
    handleImageClick(url) {
      this.currentImageUrl = url;
      this.showImageModal = true;
    },
    
    // 处理退出登录
    handleLogout() {
      // 添加退出登录的逻辑
      alert('退出登录');
      // 可以根据实际需求添加更多逻辑
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

.page-title {
  margin-bottom: 25px;
  color: #2c3e50;
  font-size: 1.5rem;
}

.merchant-audit-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.audit-item {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.audit-actions {
  display: flex;
  gap: 10px;
  position: absolute;
  right: 20px;
  bottom: 20px;
}

.btn-approve {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-reject {
  background: #f44336;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.loading-tip {
  color: #999;
  text-align: center;
  padding: 20px;
}

/* 图片展示样式 */
.audit-images {
  width: 100%;
  height: 120px;
  display: flex;
  flex-direction: row;
  gap: 15px;
  margin-top: 12px;
  overflow-x: auto;
  padding: 12px;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  background: #f8f9fa;
}

.image-group {
  width: 150px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.audit-image {
  max-width: 200px;
  height: auto;
  border-radius: 4px;
  border: 1px solid #eee;
}

/* 图片放大模态框 */
.image-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  position: relative;
  max-width: 80%;
  max-height: 80%;
}

.modal-close {
  position: absolute;
  top: -30px;
  right: -30px;
  color: white;
  font-size: 30px;
  cursor: pointer;
}

.modal-image {
  max-width: 100%;
  max-height: 100%;
  border-radius: 8px;
  border: 2px solid white;
}

/* 表格样式 */
.table-row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0;
  border-top: 1px solid #e9ecef;
  border-left: 1px solid #e9ecef;
}

.table-cell {
  border-right: 1px solid #e9ecef;
  border-bottom: 1px solid #e9ecef;
  font-size: 14px;
  text-align: left;
  padding: 8px;
}
</style>