// The old HBuilderX Babel pipeline loses the pages.json import in a default
// destructuring parameter. Pass it explicitly instead, preserving the config.
module.exports = function(source) {
  return source.replace('tabBar:r={}}=e){', 'tabBar:r={}}){')
    .replace('loginPageInTabBar:os(i,r)}}();', 'loginPageInTabBar:os(i,r)}}(e);');
};
