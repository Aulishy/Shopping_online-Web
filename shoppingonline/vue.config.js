const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  // 基础配置
  transpileDependencies: true, // 强制转译某些依赖包（如使用旧版本ES语法的第三方库）

  // 路径相关配置（关键）
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/', // 生产环境使用相对路径（避免部署后静态资源404），开发环境用绝对路径
  outputDir: 'dist', // 打包输出目录（默认dist）
  assetsDir: 'static', // 静态资源（js、css、img）存放的子目录（默认空）

  // 开发服务器配置（解决跨域等问题）
  devServer: {
    port: 8080, // 开发服务器端口（默认8080）
    open: true, // 启动后自动打开浏览器
    proxy: { // 代理配置（解决跨域问题）
      '/api': { // 匹配所有以/api开头的请求
        target: 'http://localhost:8081', // 后端接口地址
        changeOrigin: true, // 允许跨域
        pathRewrite: { '^/api': '' } // 将路径中的/api替换为空（如前端请求/api/user → 后端接收/user）
      }
    }
  },

  // CSS 配置
  css: {
    extract: process.env.NODE_ENV === 'production', // 生产环境提取CSS为独立文件（默认true），开发环境内联（提升热更新速度）
    sourceMap: false, // 是否为CSS生成source map（生产环境建议关闭）
    loaderOptions: { // CSS预处理器配置（如sass、less）
      scss: {
        additionalData: `@import "~@/styles/variables.scss";` // 自动注入全局SCSS变量
      }
    }
  },

  // 构建优化
  productionSourceMap: false, // 关闭生产环境source map（减少打包体积，提升安全性）
  parallel: require('os').cpus().length > 1, // 开启多线程编译（根据CPU核心数自动判断）

  // ESLint 配置（代码检查）
  lintOnSave: process.env.NODE_ENV === 'development', // 开发环境开启ESLint检查，生产环境关闭（避免影响构建速度）
});