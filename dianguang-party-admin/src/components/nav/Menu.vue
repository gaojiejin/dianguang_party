<template>
  <el-container class="workspace-shell" style="height: 100%;">
    <el-aside width="auto">
      <common-aside></common-aside>
    </el-aside>
    <el-container>
      <el-header>
        <common-header></common-header>
      </el-header>
      <!--            <common-tag></common-tag>-->
      <el-main><div class="page-heading"><div><span class="eyebrow">DIANGUANG WORKSPACE</span><h1>{{ pageTitle }}</h1></div><span class="workspace-status"><i></i> 党建管理工作台</span></div>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import CommonAside from "@/components/nav/CommonAside";
import CommonHeader from "@/components/nav/CommonHeader";
import CommonTag from "@/components/nav/CommonTag";

export default {
  name: 'homePage',
  components:{
    CommonHeader,
    CommonAside,
    CommonTag,
  },
  data () {
    return {}
  },
  computed: { pageTitle() { const find = (items) => { for (const item of items) { if (item.path === this.$route.path) return item.label; if (item.children) { const label = find(item.children); if (label) return label; } } }; return find(this.$router.options.routes[1].children) || '工作台'; } },
  mounted() {
    this.$store.commit('setMenu',  this.$router.options.routes[1].children)
  }
}
</script>

<style lang="less" scoped>
.el-aside,
.el-header,
.el-main {
  padding: 0;  /* 移除内边距 */
  margin: 0;   /* 移除外边距 */
}

.el-container {
  height: 100vh;  /* 使容器占满整个视口高度 */
}

.el-header,
.el-aside {
  height: auto;
}

</style>
