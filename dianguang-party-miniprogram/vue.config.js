const path = require('path');
const sass = require('D:/HBuilderX/HBuilderX/plugins/compile-dart-sass/node_modules/sass');
module.exports = {
  transpileDependencies: ['uview-ui'],
  configureWebpack: { optimization: { minimize: false } },
  chainWebpack(config) {
    config.module.rule('mp-runtime-compat').test(/uni-mp-weixin[\\/]dist[\\/]index\.js$/).enforce('pre').use('compat').loader(path.resolve(__dirname,'scripts/runtime-compat-loader.cjs'));
    config.module.rule('unicloud-compat').test(/uni-cloud[\\/]dist[\\/]index\.js$/).enforce('pre').use('compat').loader(path.resolve(__dirname,'scripts/unicloud-compat-loader.cjs'));
  },
  css: { loaderOptions: { scss: { implementation: sass, prependData: '@import "uview-ui/theme.scss";' }, sass: { implementation: sass } } }
};
