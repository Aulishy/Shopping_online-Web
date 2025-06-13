<template>
  <div class="merchant-onboarding" :style="{ backgroundImage: `url(/assets/v2-21cb8db22fb059184fc18ede2c75d05d_r.jpg)` }">
    <div class="onboarding-card">
      <h2>商家入驻申请</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="shopName">店铺名称:</label>
          <input id="shopName" v-model="form.shopName" type="text" placeholder="请输入店铺名称" required />
        </div>
        <div class="form-group">
          <label for="shopAddress">店铺详细地址:</label>
          <input id="shopAddress" v-model="form.shopAddress" type="text" placeholder="请输入店铺所在省市区及街道门牌号" required />
        </div>
        <div class="form-group">
          <label for="legalRepresentative">法定代表人姓名:</label>
          <input id="legalRepresentative" v-model="form.legalRepresentative" type="text" placeholder="请输入法定代表人姓名" required />
        </div>
        <div class="form-group">
          <label for="idNumber">身份证号:</label>
          <input id="idNumber" v-model="form.idNumber" type="text" placeholder="请输入身份证号" required />
        </div>
        <div class="form-group">
          <label for="contactNumber">联系方式:</label>
          <input id="contactNumber" v-model="form.contactNumber" type="text" placeholder="请输入联系方式" required />
        </div>
        <div class="form-group">
          <label for="businessLicense">营业执照上传:</label>
          <input id="businessLicense" type="file" @change="handleFileChange" accept="image/*" required />
        </div>
        <div class="form-group">
          <label for="shopCategory">店铺分类:</label>
          <select id="shopCategory" v-model="form.shopCategory"  required>
            <option value="电子产品">电子产品</option>
            <option value="服装鞋包">服装鞋帽</option>
            <option value="食品饮料">食品</option>
            <option value="家居日常">日常用品</option>
            
          </select>
        </div>
        <div class="form-group">
          <label for="shopDescription">店铺简介</label>
          <textarea id="shopDescription" v-model="form.shopDescription" placeholder="请输入店铺简介" rows="4" required></textarea>
        </div>
        <div class="form-group">
          <label for="idCardFront">身份证正面照片</label>
          <input id="idCardFront" type="file" @change="handleIdCardFrontChange" accept="image/*" required />
        </div>
        <div class="form-group">
          <label for="idCardBack">身份证背面照片</label>
          <input id="idCardBack" type="file" @change="handleIdCardBackChange" accept="image/*" required />
        </div>
        <button type="submit" class="submit-button">提交入驻申请</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'MerchantApply',
  data() {
    return {
      form: {
        shopName: '',
        shopAddress: '',
        legalRepresentative: '',
        idNumber: '',
        contactNumber: '',
        businessLicense: null,  // 存储压缩后的营业执照文件
        shopCategory: '',
        shopDescription: '',
        idCardFront: null,      // 存储压缩后的身份证正面文件
        idCardBack: null,       // 存储压缩后的身份证背面文件
        applyTime: ''
      }
    };
  },
  methods: {
    // 新增：图片压缩函数（最大宽度800px，质量0.8）
    async compressImage(file, maxWidth = 800, quality = 0.8) {
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
            // 调整尺寸（保持宽高比）
            if (width > maxWidth) {
              height = Math.round((height * maxWidth) / width);
              width = maxWidth;
            }
            canvas.width = width;
            canvas.height = height;
            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);
            // 转换为JPEG格式并压缩
            canvas.toBlob((blob) => {
              resolve(new File([blob], file.name, { 
                type: 'image/jpeg', 
                lastModified: Date.now() 
              }));
            }, 'image/jpeg', quality);
          };
        };
      });
    },

    // 修改：营业执照上传事件（添加压缩）
    async handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const compressedFile = await this.compressImage(file);  // 压缩图片
        this.form.businessLicense = compressedFile;  // 保存压缩后的文件
      }
    },

    // 修改：身份证正面上传事件（添加压缩）
    async handleIdCardFrontChange(event) {
      const file = event.target.files[0];
      if (file) {
        const compressedFile = await this.compressImage(file);  // 压缩图片
        this.form.idCardFront = compressedFile;  // 保存压缩后的文件
      }
    },

    // 修改：身份证背面上传事件（添加压缩）
    async handleIdCardBackChange(event) {
      const file = event.target.files[0];
      if (file) {
        const compressedFile = await this.compressImage(file);  // 压缩图片
        this.form.idCardBack = compressedFile;  // 保存压缩后的文件
      }
    },

    // 原有提交逻辑保持不变
    handleSubmit() {
      if (this.form.idNumber.length !== 18) {
        alert('身份证号必须为 18 位，请检查后重新输入！');
        return;
      }
      this.form.applyTime = new Date().toLocaleString('zh-CN');
      const formData = new FormData();
      for (const key in this.form) {
        if (this.form[key] !== null && this.form[key] !== '') {
          formData.append(key, this.form[key]);  // 上传压缩后的文件
        }
      }
      axios.post('http://localhost:8081/merchant/apply', formData)
        .then(response => {
          if (response.data.code === 200) {
            console.log('提交成功:', response.data);
            alert('提交成功！等待审核');
            this.form = {
              shopName: '',
              shopAddress: '',
              legalRepresentative: '',
              idNumber: '',
              contactNumber: '',
              businessLicense: null,
              shopCategory: '',
              shopDescription: '',
              idCardFront: null,
              idCardBack: null,
              applyTime: ''
            };
          } else {
            alert(`提交失败：${response.data.message}`);
          }
        })
        .catch(error => {
          alert('网络请求失败，请稍后再试');
          console.error('请求错误:', error);
        });
    }
  }
};
</script>

<style scoped>
.merchant-onboarding {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f8f9fa; /* 更浅的背景色 */
}

.onboarding-card {
  background-color: white;
  border-radius: 12px; /* 更大的圆角 */
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08); /* 更柔和的阴影 */
  padding: 40px; /* 增加内边距 */
  width: 700px; /* 适当加宽卡片 */
  max-width: 95%;
}

h2 {
  text-align: center;
  color: #2c3e50; /* 更深的标题颜色 */
  margin-bottom: 30px;
  font-size: 1.8rem; /* 增大标题字号 */
}

.form-group {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  margin-bottom: 25px; /* 增加表单组间距 */
  
}

label {
  display: flex;
  width:50%;
  margin-bottom: 10px;
  font-weight: 500; /* 更柔和的粗体 */
  color: #495057; /* 更柔和的标签颜色 */
  font-size: 15px;
}

input[type="text"],
input[type="file"],
select,
textarea {
  width: 100%;
  padding: 12px 15px; /* 增加输入框内边距 */
  border: 2px solid #e9ecef; /* 更粗的浅色边框 */
  border-radius: 6px; /* 输入框圆角 */
  font-size: 14px;
  transition: all 0.3s ease; /* 添加过渡效果 */
}

input[type="text"]:focus,
select:focus,
textarea:focus {
  outline: none;
  border-color: #2196F3; /* 聚焦时蓝色边框 */
  box-shadow: 0 0 0 3px rgba(33, 150, 243, 0.1); /* 聚焦阴影 */
}

textarea {
  resize: vertical;
  min-height: 100px; /* 文本域最小高度 */
}

.submit-button {
  width: 100%;
  padding: 14px;
  background-color: #2196F3; /* 主色调调整 */
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.1s ease; /* 增加按钮过渡 */
}

.submit-button:hover {
  background-color: #1976D2; /* 悬停加深颜色 */
  transform: translateY(-1px); /* 悬停微上浮 */
}

.submit-button:active {
  transform: translateY(0); /* 点击还原 */
}
/* 新增：预览图片样式 */
.preview-image {
  max-width: 100%;  /* 不超过父容器宽度 */
  max-height: 200px; /* 限制最大高度 */
  object-fit: contain; /* 等比例填充容器，不溢出 */
  border-radius: 8px;
  border: 1px solid #eee;
  margin-top: 10px; /* 与输入框的间距 */
}
</style>