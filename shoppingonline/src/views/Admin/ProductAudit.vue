<template>
  <div class="merchant-container">
    <!-- 侧边栏导航（补充实际内容） -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h3>管理员页面</h3>
      </div>
      <nav class="sidebar-menu">
        <router-link 
          :to="{ path: '/merchantAudit', query: { id: $route.query.id } }" 
          class="menu-item"
          :class="{ active: $route.path === '/merchantAudit' }"
        >
          商家入驻审核
        </router-link>
        <router-link 
          :to="{ path: '/productAudit', query: { id: $route.query.id } }" 
          class="menu-item"
          :class="{ active: $route.path === '/productAudit' }"
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

    <!-- 主内容区域保持不变 -->
    <main class="main-content">
      <!-- 调整：标题与数量并排显示 -->
      <div class="title-count-group">
        <h2 class="page-title">产品上新审核</h2>
        <p class="audit-count-text">待审核总数：{{ pendingProducts.length }}</p>
      </div>

      <div v-if="loading" class="loading-tip">加载中...</div>
      
      <div v-else class="merchant-audit-list">
        <!-- 原有审核列表内容保持不变 -->
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
           
            <!-- 图片展示（修改为水平排列） -->
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
          <!-- 操作按钮（固定右下角） -->
          <div class="audit-actions" >
            <button class="btn-approve" @click="handleApprove(product.paudit_id)">通过审核</button>
            <button class="btn-reject" @click="handleReject(product.paudit_id)">拒绝申请</button>
          </div>
        </div>
        <!-- 新增：图片放大模态框 -->
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
export default {
  name: 'ProductAudit',
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
    // 获取待审核商家列表（修改请求方式为POST）
    async fetchPendingMerchants() {
      this.loading = true;
      try {
        // 修改点：将get改为post，并添加请求体（示例传空对象，实际按后端要求调整）
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

    // 处理审核通过（关键修改）
    async handleApprove(pauditId) {  // 参数改为 paudit_id
      // 修正提示信息为产品审核场景
      if (!window.confirm('确认通过该产品上新申请？')) return;

      try {
        // 传递 paudit_id 给后端（匹配后端唯一标识）
        const response = await axios.post('http://localhost:8081/admin/approve-product', { 
          pauditId  // 使用 paudit_id 替代 productId
        });

        if (response.data.code === 200) {
          // 从 pendingProducts 中移除已通过的产品（根据 paudit_id 过滤）
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
  position: relative; /* 关键：为子元素提供绝对定位参考 */
  display: flex;
  flex-direction: column; /* 内容垂直排列，避免按钮覆盖信息 */
  gap: 15px; /* 内容与按钮的间距 */
}


.audit-actions {
  display: flex;
  gap: 10px;
  position: absolute; /* 绝对定位 */
  right: 20px; /* 右侧距离卡片内边距（与卡片padding一致） */
  bottom: 20px; /* 底部距离卡片内边距（与卡片padding一致） */
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

.no-data-tip {
  color: #666;
  text-align: center;
  padding: 20px;
}

.loading-tip {
  color: #999;
  text-align: center;
  padding: 20px;
}

/* 新增：统计卡片样式 */
.audit-stats-card {
  background: white;
  padding: 5px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin-bottom: 5px;
}

.stats-title {
  color: #666;
  font-size: 1rem;
  margin-bottom: 8px;
}

.stats-value {
  color: #2c3e50;
  font-size: 1.8rem;
  font-weight: bold;
}

/* 新增：图片展示样式 */
.audit-images {
  width: 100%;          /* 占满父容器宽度 */
  height: 120px;        /* 固定容器高度 */
  display: flex;
  flex-direction: row;
  gap: 15px;            /* 图片组间距 */
  margin-top: 12px;
  overflow-x: auto;     /* 水平滚动（内容超出时） */
  padding: 12px;        /* 内边距提升美观 */
  border: 1px solid #e9ecef;  /* 浅色边框 */
  border-radius: 6px;   /* 圆角 */
  background: #f8f9fa;  /* 浅灰色背景 */
}

/* 新增/修改：固定单个图片组尺寸 */
.image-group {
  width: 150px;         /* 单个图片组宽度 */
  flex-shrink: 0;       /* 不允许收缩 */
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;             /* 标题与图片间距 */
}

/* 修改：调整图片显示方式 */
.audit-image {
  width: 100%;          /* 填满图片组宽度 */
  height: 120px;        /* 固定图片高度 */
  object-fit: contain;  /* 保持比例填充 */
  border-radius: 4px;
  border: 1px solid #eee;
  background: white;    /* 图片背景色 */
}
.table-cell {
  font-size: 14px; /* 缩小字体 */
  text-align: left; /* 文字左对齐 */
  padding: 8px; /* 增加内边距提升可读性 */
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


/* 表格行样式（外边框） */
.table-row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0; /* 取消列间距，避免边框断裂 */
  border-top: 1px solid #e9ecef; /* 顶部边框 */
  border-left: 1px solid #e9ecef; /* 左侧边框 */
}

/* 表格单元格样式（内边框） */
.table-cell {
  border-right: 1px solid #e9ecef; /* 右侧边框 */
  border-bottom: 1px solid #e9ecef; /* 底部边框 */
}
</style>
