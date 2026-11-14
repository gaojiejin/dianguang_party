// Avoid the legacy uni Babel root-mount rewrite inside the platform runtime.
module.exports = source => source.replace('this.$vm.$mount()', () => "this.$vm['$' + 'mount']()");
