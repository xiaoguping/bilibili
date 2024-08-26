const path = require('path');
const debug = process.env.NODE_ENV !== 'production'

module.exports = {
  //基本路径
  // baseUrl: './',
  publicPath: process.env.VUE_APP_BASE_ROUTE,
  // publicPath: process.env.VUE_APP_FAB,
  //输出文件目录
  outputDir: 'dist',
  // eslint-loader 是否在保存的时候检查
  lintOnSave: true,
  //放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录。
  assetsDir: 'public', // 静态资源目录 (js, css, img, fonts)
  //是否使用包含运行时编译器的 Vue 构建版本
  runtimeCompiler: false,
  //生产环境是否生成 sourceMap 文件，一般情况不建议打开
  productionSourceMap: false,
  devServer: { //反向代理
    // proxy: {
    //   '/': {
    //     ws: false, // proxy websockets
    //     //  target: 'http://localhost:8081',
    //     target: 'https://szrt.boe.com/v1/dhealth-api', //测试
    //     changeOrigin: true,
    //     pathRewrite: {
    //       '^/': '/' //需要rewrite重写的,
    //     },
    //     cookieDomainRewrite: {
    //       '*': ''
    //     }
    //   }
    // }
  },
  // webpack配置，值位对象时会合并配置，为方法时会改写配置
  // configureWebpack: config => {
  //   require('vux-loader').merge(config, {
  //     options: {},
  //     plugins: ['vux-ui']
  //   })
  //   // if (process.env.NODE_ENV === 'production') {
  //   //   // Object.assign(config, {
  //   //   //   externals: externals
  //   //   // })
  //   //   //压缩图片体积
  //   //   config.module
  //   //     .rule("image-webpack-loader")
  //   //     .test(/\.(gif|png|jpe?g|svg)$/i)
  //   //     .use("file-loader")
  //   //     .loader("image-webpack-loader")
  //   //     .tap(() => ({
  //   //       disable: process.env.NODE_ENV !== 'production'
  //   //     }))
  //   //     .end()

  //   //   //取消console.log
  //   //   config.plugins.push(
  //   //     new UglifyJsPlugin({
  //   //       uglifyOptions: {
  //   //         warnings: false,
  //   //         compress: {
  //   //           drop_console: true,
  //   //           drop_debugger: false,
  //   //           pure_funcs: ['console.log']
  //   //         }
  //   //       },
  //   //       sourceMap: false,
  //   //       parallel: true
  //   //     })
  //   //   )
  //   // }

  // }
}
