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
          <!-- 新增：按钮容器（右上角） -->
            <div class="publish-btn-container">
              <button class="btn-publish" @click="handleShowPublishForm">发布产品</button>
            </div>

          <!-- 原有审核记录卡片 -->
          <div class="audit-record-card">
            <h3>产品审核记录</h3>
            <div class="audit-list">
              <!-- 当前审核中 -->
              <div v-if="currentAudits.length" class="audit-section">
                <h4>当前审核中（{{ currentAudits.length }}条）</h4>
                <!-- 修改：key 改为 paudit_id，字段绑定改为后端返回的字段名 -->
                <!-- 当前审核中列表 -->
                <div class="audit-item" v-for="item in currentAudits" :key="item.paudit_id" @click="handleViewDetails(item)"> <!-- 新增点击事件 -->
                  <div class="audit-basic-info">
                    <p class="audit-product-name">{{ item.product_name }}</p>
                    <p class="audit-time">提交时间：{{ item.submit_time }}</p>
                    <p class="audit-price">价格：¥{{ item.price }}</p>
                    <p class="audit-stock">库存：{{ item.stock }}</p>
                  </div>
                  <div class="audit-status-group">
                    <span class="audit-status status-pending">审核中</span>
                    <button class="btn-cancel-audit" @click="handleCancelAudit(item)">撤销</button>
                  </div>
                </div>
                
                <!-- 历史记录列表 -->
                <div class="audit-item" v-for="item in historyAudits" :key="item.paudit_id" @click="handleViewDetails(item)"> <!-- 新增点击事件 -->
                  <div class="audit-basic-info">
                    <p class="audit-product-name">{{ item.product_name }}</p>
                    <p class="audit-time">提交时间：{{ item.submit_time }}</p>
                    <p class="audit-price">价格：¥{{ item.price }}</p>
                    <p class="audit-stock">库存：{{ item.stock }}</p>
                  </div>
                  <div class="audit-status-group">
                    <span :class="['audit-status', 'status-' + item.status]">
                      {{ item.status === 'approved' ? '通过' : '拒绝' }}
                    </span>
                    <p v-if="item.status === 'rejected'" class="reject-reason">拒绝原因：{{ item.reject_reason }}</p>
                  </div>
                </div>
                
                <!-- 新增：审核详情模态框 -->
               
                <div v-if="showAuditDetails" class="audit-details-modal">
                  <div class="modal-content">
                    <span class="modal-close" @click="showAuditDetails = false">&times;</span>
                    <h3>产品审核详情</h3>
                    <div class="details-content">
                      <p><strong>产品名称：</strong>{{ currentAudit.product_name }}</p>
                      <p><strong>价格：</strong>¥{{ currentAudit.price }}</p>
                      <p><strong>库存：</strong>{{ currentAudit.stock }}</p>
                      <p><strong>分类：</strong>{{ currentAudit.category }}</p>
                      <p><strong>描述：</strong>{{ currentAudit.description }}</p>
                      <div v-if="currentAudit.image_url" class="details-image">
                        <img :src="currentAudit.image_url" alt="产品图片" @click="handleZoomImage(currentAudit.image_url)">
                      </div>
                    </div>
                  </div>
                </div>
              </div>
  

            </div>
          </div>
          <!-- 新增：发布表单模态框 -->
        <div v-if="showPublishForm" class="publish-modal">
          <div class="modal-content">
            <h3>发布新产品</h3>
            <form class="publish-form" @submit.prevent="handlePublish">
              <!-- 产品名称与价格同行 -->
              <div class="form-group-row">
                <!-- 产品名称 -->
                <div class="form-group">
                  <label for="productName">产品名称：</label>
                  <input 
                    type="text" 
                    id="productName" 
                    v-model="productForm.productName" 
                    required
                  >
                </div>
              
              <!-- 价格 -->
              <div class="form-group">
                <label for="price">价格（元）：</label>
                <input 
                  type="number" 
                  id="price" 
                  v-model="productForm.price" 
                  min="0" 
                  step="0.01" 
                  required
                >
              </div>
            
            </div>

            <!-- 库存与分类同行 -->
            <div class="form-group-row">
              <!-- 库存 -->
              <div class="form-group">
                <label for="stock">库存：</label>
                <input 
                  type="number" 
                  id="stock" 
                  v-model="productForm.stock" 
                  min="0" 
                  required
                >
              </div>

              <!-- 分类 -->
              <div class="form-group">
                <label for="category">产品分类：</label>
                <select 
                  id="category" 
                  v-model="productForm.category" 
                  required
                >
                  <option value="">请选择分类</option>
                  <option value="电子产品">电子产品</option>
                  <option value="食品饮料">食品饮料</option>
                  <option value="家居日用">家居日用</option>
                  <option value="服装鞋包">服装鞋包</option>
                </select>
              </div>
            </div>

            <!-- 产品描述（保持不变） -->
            <div class="form-group">
              <label for="description">产品描述：</label>
              <textarea 
                id="description" 
                v-model="productForm.description" 
                rows="4" 
                required
              ></textarea>
            </div>

            <!-- 图片上传（保持不变） -->
            <div class="form-group">
              <label>产品图片：</label>
              <input 
                type="file" 
                accept="image/*" 
                @change="handleImageUpload"
              >
              
            </div>

            <!-- 提交与取消按钮（保持不变） -->
            <div class="form-actions">
              <button type="submit" class="btn-publish">提交审核</button>
              <button type="button" class="btn-cancel" @click="showPublishForm = false">取消</button>
            </div>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ProductPublish',
  data() {
    return {
       showPublishForm: false, // 控制表单显示
       productForm: {          // 表单数据对象
        productName: '',
        price: 0,
        stock: 0,
        category: '',
        description: '',
        imageFile: null
      },
      auditRecords: [], 
      showAuditDetails: false, // 控制审核详情模态框显示
      currentAudit: null, // 当前点击的审核记录
    };
  },
  
  async mounted() {
    try {
      const response = await axios.post('http://localhost:8081/merchant/audit-records', {
        shopId: this.$route.query.id
      });

      if (response.data.code === 200) {
        // Update: Use 'auditrecords' from the backend response instead of 'data'
        this.auditRecords = response.data.auditrecords; // <-- Fix here
      } else {
        alert(`获取审核记录失败：${response.data.message}`);
      }
    } catch (error) {
      console.error('请求审核记录失败:', error);
      alert('请求审核记录失败，请稍后重试');
    }
  },
  computed: {
    currentAudits() {
      // 过滤后端数据中状态为「审核中」的记录
      return this.auditRecords.filter(item => item.status === 'pending_audit');
    },
    historyAudits() {
      // 过滤后端数据中状态为「已通过」或「已拒绝」的记录
      return this.auditRecords.filter(item => item.status !== 'pending_audit');
    }
  },
  methods: {
    // 新增：图片压缩函数（与MerchantApply.vue共用，可提取为公共工具）
    async compressImage(file, maxWidth = 300, quality = 0.8) {
      return new Promise((resolve) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (e) => {
          const img = new Image();
          img.src = e.target.result;
          img.onload = () => {
            const canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;
            if (width > maxWidth) {
              height = Math.round((height * maxWidth) / width);
              width = maxWidth;
            }
            canvas.width = width;
            canvas.height = height;
            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);
            canvas.toBlob((blob) => {
              resolve(new File([blob], file.name, { type: 'image/jpeg', lastModified: Date.now() }));
            }, 'image/jpeg', quality);
          };
        };
      });
    },

    // 修改：产品图片上传事件（添加压缩）
    async handleImageUpload(e) {
      const file = e.target.files[0];
      if (file) {
        const compressedFile = await this.compressImage(file); // 压缩图片
        this.productForm.imageFile = compressedFile;
      }
    },
    async handlePublish() {
  const formData = new FormData();
  formData.append('product_name', this.productForm.productName);
  formData.append('image', this.productForm.imageFile);  // 上传文件
  formData.append('price', this.productForm.price);
  formData.append('stock', this.productForm.stock);
  formData.append('category', this.productForm.category);
  formData.append('description', this.productForm.description);
  //获取shopId
  const shopId = this.$route.query.id;
  //获取提交时间
  const submitTime = new Date().toLocaleString('zh-CN');
  formData.append('shopId', shopId);
  formData.append('submitTime', submitTime);
  //提交表格
  const response=await axios.post('http://localhost:8081/merchant/propublish', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
  if(response.data.code===200){
    alert('提交成功！等待审核');
    //移出表单界面
    this.showPublishForm = false;
    
}else{
  alert(`提交失败：${response.data.message}`);
  }},
     handleViewDetails(auditItem) {
      this.currentAudit = auditItem;  // 保存当前点击的审核记录
      this.showAuditDetails = true;   // 显示模态框
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
    // 新增：处理审核撤销
    async handleCancelAudit(auditItem) {
      const confirmCancel = window.confirm('确定要撤销该产品的审核申请吗？');
      if (!confirmCancel) return;

      try {
        const response = await axios.post('http://localhost:8081/merchant/cancel-audit', {
          auditId: auditItem.paudit_id,
          shopId: this.$route.query.id  // 新增：传递当前商家ID
        });

        if (response.data.code === 200) {
          this.auditRecords = this.auditRecords.filter(item => item.paudit_id !== auditItem.paudit_id);
          alert('审核申请已成功撤销！');  // 优化提示信息
        } else {
          alert(`撤销失败：${response.data.message || '无具体错误信息'}`);  // 增强错误提示鲁棒性
        }
      } catch (error) {
        console.error('撤销审核请求失败:', error);
        alert('网络请求异常，请检查网络连接后重试');  // 更友好的网络错误提示
      }
    },
     handleShowPublishForm() {
      this.showPublishForm = true;
    },
   
  }
};
</script>

<style scoped>
/* 保留必要的容器和侧边栏样式 */
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

.publish-container {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}

.page-title {
  color: #2c3e50;
  font-size: 24px;
  margin-bottom: 30px;
}

.audit-record-card {
  background: white;
  border-radius: 12px;
  padding: 20px; /* 补充卡片内边距 */
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  margin-top: 5px;
}

/* 新增：审核列表容器样式 */
.audit-list {
  padding: 10px; /* 列表内容与卡片边缘的间距 */
  background: white; /* 浅灰色背景区分内容区域 */
  border-radius: 8px; /* 与卡片圆角保持一致 */
  margin-top: 15px; /* 与卡片标题的间距 */
}

.audit-section {
  margin-top: 15px;
}

.audit-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border: 1px solid #f0f2f5;
  border-radius: 8px;
  margin-bottom: 10px;
  height: 120px; /* 固定卡片高度 */
  overflow:hidden; /* 超出内容隐藏 */
}
.audit-basic-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 均匀分布内容 */
  text-align: left;
  line-height: 0.5; /* 缩小行距 */
}

.audit-product-name {
 color: #2c3e50;
  font-size: 14px; /* 原15px → 减小标题字体 */
  font-weight: 500; /* 加粗标题突出 */
}

.audit-time {
  color: #666;
  font-size: 14px;
  min-width: 150px;
}

.audit-status {
  padding: 1px 2px;
  border-radius: 2px;
  font-size: 14px;
  min-width: 80px;
  text-align: center;
}

.status-pending {
  background: #e3f2fd;
  color: #2196F3;
}

.status-approved {
  background: #e8f5e9;
  color: #4caf50;
}

.status-rejected {
  background: #ffebee;
  color: #e53935;
}

.reject-reason {
  color: #e53935;
  font-size: 14px;
  
  margin-top: 5px; /* 调整拒绝原因与状态之间的间距 */

}
.audit-status-group {
  display: flex;
  flex-direction: column; /* 垂直排列状态和按钮 */
  align-items: flex-end; /* 左对齐状态和按钮 */
  gap: 5px; /* 状态标签与按钮间距 */
}

.btn-cancel-audit {
  margin-top: 5px; /* 调整按钮与状态标签间距 */
  padding: 4px 8px;
  border: none;
  border-radius: 4px;
  background: #2196F3;
  color: white;
  font-size: 14px;
  min-width: 80px; /* 按钮宽度保持一致 */
    text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-cancel-audit:hover {
  background: #1976D2;
}

/* 新增：按钮容器样式 */
.publish-btn-container {
  display: flex;
  justify-content: flex-end; /* 右对齐 */
  margin-bottom: 20px; /* 与下方审核卡片保持间距 */
  padding: 0 10px; /* 与容器内边距一致 */
}

/* 按钮样式（保持与页面主色一致） */
.btn-publish {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  background: #2196F3;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-publish:hover {
  background: #1976D2;
}
.publish-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 90%;
}
.form-group-row {
  display: flex;
  gap: 8px; /* 控制行内字段间距 */
}

.form-group-row .form-group {
  flex: 1; /* 子字段等宽 */
}
.form-group {
  display: flex;
  flex-direction: row;  /* 水平排列 */
  align-items: center; /* 垂直居中对齐 */
  gap: 15px;           /* 标签与输入框间距 */
  margin-bottom: 15px;
}

/* 为标签设置固定宽度（根据需求调整） */
.form-group label {
  min-width: 100px;    /* 标签固定宽度 */
  text-align: right;   /* 标签文字右对齐 */
}

/* 输入框/选择框/文本域占满剩余空间 */
.form-group input,
.form-group select,
.form-group textarea {
  flex: 1;  /* 自动填充剩余空间 */
}

.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 20px;
  justify-content: flex-end;
}

.btn-cancel {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  background: #e9ecef;
  color: #2c3e50;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-cancel:hover {
  background: #dee2e6;
}

/* 审核详情模态框 */
.audit-details-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001; /* 高于发布表单的z-index:1000 */
}

.audit-details-modal .modal-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 90%;
  position: relative;
}

.audit-details-modal .modal-close {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.details-content {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.details-image img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 8px;
  border: 1px solid #eee;
  margin-top: 10px;
  cursor: pointer;
}
</style>