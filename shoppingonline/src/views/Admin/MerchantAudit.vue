<template>
  <div class="merchant-container">
    <AdminSidebar 
      :adminId="$route.query.id"
      @logout="handleLogout"
    />
    <main class="main-content">
      <!-- 标题与数量并排显示 -->
      <div class="title-count-group">
        <h2 class="page-title">商家入驻审核</h2>
        <p class="audit-count-text">待审核商家总数：{{ pendingMerchants.length }}</p>
      </div>

      <div v-if="loading" class="loading-tip">加载中...</div>
      
      <div v-else class="merchant-audit-list">
        <!-- 审核列表内容 -->
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
            <!-- 图片展示 -->
            <div class="audit-images">
              <!-- 营业执照组 -->
              <div class="image-group">
                <p><strong>营业执照：</strong></p>
                <img 
                  :src="merchant.business_license"  
                  alt="营业执照" 
                  class="audit-image"  
                  @click="handleImageClick(merchant.business_license)"
                >
              </div>
              
              <!-- 身份证正面组 -->
              <div class="image-group">
                <p><strong>身份证正面：</strong></p>
                <img 
                  :src="merchant.id_card_front" 
                  alt="身份证正面" 
                  class="audit-image" 
                  @click="handleImageClick(merchant.id_card_front)"
                >
              </div>
              
              <!-- 身份证背面组 -->
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
          <div class="audit-actions" v-if="merchant.audit_status === '0'">
            <button class="btn-approve" @click="handleApprove(merchant.id)">通过审核</button>
            <button class="btn-reject" @click="handleReject(merchant.id)">拒绝申请</button>
          </div>
        </div>
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
  name: 'MerchantAudit',
  components: {
    AdminSidebar
  },
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
    // 获取待审核商家列表
    async fetchPendingMerchants() {
      this.loading = true;
      try {
        // 修改点：将get改为post，并添加请求体（示例传空对象，实际按后端要求调整）
        const res = await axios.post('http://localhost:8080/admin/pending-merchants', {});
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
        await axios.post('http://localhost:8080/admin/approve-merchant', { merchantId });
        this.pendingMerchants = this.pendingMerchants.filter(m => m.id !== merchantId);
        alert('审核通过成功');
      } catch (error) {
        alert('审核通过失败: ' + error.response?.data?.message || '服务器错误');
      }
    },

    // 处理审核拒绝
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
  
  max-width: 1400px;
  margin-right: auto;
}

/* 针对表格区域也可以进行响应式优化 */
.audit-table {
  width: 100%;
  overflow-x: auto;
}

.table-row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0;
  border-top: 1px solid #e9ecef;
  border-left: 1px solid #e9ecef;
}

/* 图片区域也可以调整 */
.audit-images {
  width: 100%; /* 改为100%而不是固定的80% */
  height: 200px;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  border-radius: 4px;
  border: 1px solid #eee;
  overflow-x: auto; /* 添加横向滚动以防图片过多 */
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
  width: 80%;
  height: 200px;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  border-radius: 4px;
  border: 1px solid #eee;
}

.image-group {
  width: 200px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  overflow: hidden;
}

.audit-image {
  width: 100%;
  height: auto;
  object-fit: cover;
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