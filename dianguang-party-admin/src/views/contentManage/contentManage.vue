<template>
  <div>
    <el-menu :default-active="activeIndex" active-text-color="#2b947e" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">待审核文件</el-menu-item>
      <el-menu-item index="2">已审核文件</el-menu-item>
      <el-menu-item index="3">已驳回文件</el-menu-item>
    </el-menu>
    <Audit v-if="activeIndex === '1'"></Audit>
    <Audited v-if="activeIndex === '2'"></Audited>
    <Rejected v-if="activeIndex === '3'"></Rejected>

  </div>
</template>

<script>
import Audit from '@/views/contentManage/content/audit.vue'
import Audited from '@/views/contentManage/content/audited.vue'
import Rejected from '@/views/contentManage/content/rejected.vue'
export default {
  data() {
    return {
      activeIndex: '1'
    };
  },
  components:{
    Audit,
    Audited,
    Rejected
  },
  methods: {
    handleSelect(key, keyPath) {
      this.activeIndex = key;
    }
  }
};
</script>

<style scoped lang="less">

</style>