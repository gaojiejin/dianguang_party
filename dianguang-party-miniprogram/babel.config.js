const compiler = 'D:/HBuilderX/HBuilderX/plugins/uniapp-cli';
const base = require(compiler + '/babel.config.js');
function resolvePlugin(plugin) {
 if (typeof plugin !== 'string') return plugin;
 const name = plugin === 'import' ? 'babel-plugin-import' : plugin;
 return require.resolve(name, { paths: [compiler] });
}
module.exports = {
 ...base,
 presets: base.presets.map(([name,options])=>[require.resolve('@vue/babel-preset-app',{paths:[compiler]}),options]),
 plugins: [...base.plugins.map(p=>Array.isArray(p)?[resolvePlugin(p[0]),...p.slice(1)]:resolvePlugin(p)),
  require.resolve('../dianguang-party-admin/node_modules/@babel/plugin-transform-optional-chaining'),
  require.resolve('../dianguang-party-admin/node_modules/@babel/plugin-transform-nullish-coalescing-operator')
 ]
};
