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
        <h2 class="page-title">商家入驻审核</h2>
        <p class="audit-count-text">待审核商家总数：{{ pendingMerchants.length }}</p>
      </div>

      <div v-if="loading" class="loading-tip">加载中...</div>
      
      <div v-else class="merchant-audit-list">
        <!-- 原有审核列表内容保持不变 -->
        <div v-for="merchant in pendingMerchants" :key="merchant.id" class="audit-item">
          <!-- 审核信息表格区域 -->
          <div class="audit-table">
            <!-- 第一行（5列） -->
            <div class="table-row">
              <div class="table-cell"><strong>店铺名称：</strong>{{ merchant.shop_name }}</div>
              <div class="table-cell"><strong>法定代表人：</strong>{{ merchant.legal_representative }}</div>
              <div class="table-cell"><strong>身份证号：</strong>{{ merchant.id_number }}</div>
              <div class="table-cell"><strong>联系电话：</strong>{{ merchant.contact_number }}</div>
              <div class="table-cell"><strong>店铺地址：</strong>{{ merchant.shop_address }}</div>
            </div>
            <!-- 第二行（5列） -->
            <div class="table-row">
              <div class="table-cell"><strong>店铺类别：</strong>{{ merchant.shop_category }}</div>
              <div class="table-cell"><strong>店铺描述：</strong>{{ merchant.shop_description || '无' }}</div>
              <div class="table-cell"><strong>申请时间：</strong>{{ new Date(merchant.apply_time).toLocaleString() }}</div>
              <div class="table-cell"><strong>审核理由：</strong>{{ merchant.audit_reason || '无' }}</div>
              <div class="table-cell"><strong>审核状态：</strong>{{ merchant.audit_status === '0' ? '未审核' : merchant.audit_status === '1' ? '已通过' : '已拒绝' }}</div>
            </div>
            <!-- 图片展示（修改为水平排列） -->
            <div class="audit-images">
              <!-- 营业执照组（修改后） -->
              <div class="image-group">
                <p><strong>营业执照：</strong></p>
                <img 
                  :src="merchant.business_license"  
                  alt="营业执照" 
                  class="audit-image"  
                  @click="handleImageClick(merchant.business_license)"  
                >
              </div>
              
              <!-- 身份证正面组（修改后） -->
              <div class="image-group">
                <p><strong>身份证正面：</strong></p>
                <img 
                  :src="merchant.id_card_front" 
                  alt="身份证正面" 
                  class="audit-image" 
                  @click="handleImageClick(merchant.id_card_front)"
                >
              </div>
              
              <!-- 身份证背面组（修改后） -->
              <div class="image-group">
                <p><strong>身份证背面：</strong></p>
                <img 
                  :src="merchant.id_card_back" 
                  alt="身份证背面" 
                  class="audit-image" 
                  @click="handleImageClick(merchant.id_card_back)"
                >
              </div>
            </div>
          </div>
          <!-- 操作按钮（固定右下角） -->
          <div class="audit-actions" v-if="merchant.audit_status === '0'">
            <button class="btn-approve" @click="handleApprove(merchant.id)">通过审核</button>
            <button class="btn-reject" @click="handleReject(merchant.id)">拒绝申请</button>
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
  name: 'MerchantAudit',
  data() {
    return {
      pendingMerchants: [],
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
        const res = await axios.post('http://localhost:8081/admin/pending-merchants', {});
        // 过滤出未审核（audit_status=0）的商家
        this.pendingMerchants = res.data.data.filter(m => m.audit_status === '0');
      } catch (error) {
        console.error('获取待审核商家失败:', error);
      } finally {
        this.loading = false;
      }
    },

    // 处理审核通过
    async handleApprove(merchantId) {
      if (!window.confirm('确认通过该商家入驻申请？')) return;
      try {
        await axios.post('http://localhost:8081/admin/approve-merchant', { merchantId });
        this.pendingMerchants = this.pendingMerchants.filter(m => m.id !== merchantId);
        alert('审核通过成功');
      } catch (error) {
        alert('审核通过失败: ' + error.response?.data?.message || '服务器错误');
      }
    },

    // 处理审核拒绝（关键修改）
    async handleReject(merchantId) {
      const rawReason = window.prompt('请输入拒绝理由（必填）:', '');
      // 处理用户点击取消的情况（返回null）
      if (rawReason === null) {
        alert('已取消拒绝操作');
        return;
      }
      // 处理空输入（包括全空格）
      const reason = rawReason.trim();
      if (!reason) {
        alert('拒绝理由不能为空');
        return;
      }
      try {
        await axios.post('http://localhost:8081/admin/reject-merchant', { 
          merchantId, 
          reason 
        });
        this.pendingMerchants = this.pendingMerchants.filter(m => m.id !== merchantId);
        alert('已记录拒绝');
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
.details-image img {
  max-width: 100%;  /* 不超过父容器宽度 */
  max-height: 200px; /* 限制最大高度 */
  object-fit: contain; /* 保持比例，内容包含在容器内 */
  border-radius: 4px;
  border: 1px solid #eee;
}

/* 新增：图片展示样式 */
.audit-images {
  width:80%  ;  /* 固定宽度 */
  height:200px;   /* 固定高度（可根据需求调整） */
  display:flex;
  flex-direction: row; /* 垂直排列 */
  align-items: center; /* 水平居中 */
  gap: 8px; /* 图片之间的间距 */
  border-radius: 4px;
  border: 1px solid #eee;
}

/* 单个图片+标题组的样式（调整容器尺寸） */
.image-group {
  width: 200px;    /* 固定容器宽度 */
  height: 200px;  /* 固定容器高度 */
  display: flex;
  flex-direction: column; 
  align-items: center;
  gap: 8px; 
   overflow: hidden;
}

.table-cell {
  font-size: 14px; /* 缩小字体 */
  text-align: left; /* 文字左对齐 */
  padding: 8px; /* 增加内边距提升可读性 */
}
.audit-image {
 width: 100%;     /* 填满容器宽度（200px） */
  height: auto;   
  object-fit:cover;
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
