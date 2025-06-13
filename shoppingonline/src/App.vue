<template>
  <div id="app">
    <router-link to="/"></router-link>

    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
/* 设置全局样式 */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* 让页面内容占据整个视口高度 */
  min-height: 100vh; 
  margin: 0;
  padding: 0;
}

/* 可根据实际需求添加更多全局样式 */
</style>