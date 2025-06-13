import { createRouter, createWebHistory } from 'vue-router';
import UserHome from '@/views/User/UserHome.vue';
import MerchantDashboard from '../views/Merchant/MerchantDashboard.vue';
import AuthComponent from '@/components/Auth/AuthComponent.vue';
import MerchantAudit from '@/views/Admin/MerchantAudit.vue';
import ProductAudit from '@/views/Admin/ProductAudit.vue';
import MerchantApply from '@/views/Merchant/MerchantApply.vue';
import OrderManagement from '@/views/Merchant/OrderManagement.vue';
import ProductPublish from '@/views/Merchant/ProductPublish.vue';

import ProductList from '@/views/User/ProductList.vue';
import UserProfile from '@/views/User/UserProfile.vue';

const routes = [
  {
    path: '/',
    name: 'AuthComponent',
    component: AuthComponent
  },  {
    path: '/userhome',
    name: 'UserHome',
    component: UserHome
  },
  {
    path: '/merchant-dashboard',
    name: 'MerchantDashboard',
    component: MerchantDashboard
  },
  {
   path:'/merchantAudit',
   name:'MerchantAudit',
   component: MerchantAudit
  },
  {
    path:'/productAudit',
    name:'ProductAudit',
    component: ProductAudit
   },
   {
    path:'/merchant-apply',
    name:'MerchantApply',
    component: MerchantApply
   },
   {
    path: '/order-management',
    name: 'OrderManagement',
    component: OrderManagement
  },
  {
    path: '/product-publish',
    name: 'ProductPublish',
    component: ProductPublish
  },
  
  {
    path: '/product-list',
    name: 'ProductList',
    component: ProductList 
  },
  {
    path: '/userProfile',
    name: 'UserProfile',
    component: UserProfile  
  },
 
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});



export default router;
