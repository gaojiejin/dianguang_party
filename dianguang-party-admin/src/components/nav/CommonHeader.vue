<template>
    <header>
<!--        左边-->
        <div class="l-content">
            <el-button @click="handleMenu" plain icon="el-icon-menu" size="mini"></el-button>
<!--            <h3 style="color: #fff">首页</h3>-->
            <el-breadcrumb separator="/">
<!--                <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ path: item.path }">{{item.label}}</el-breadcrumb-item>-->
               <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ }">{{item.label}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
<!--        右边-->
        <div class="r-content"><span class="header-identity">管理员 <small>ADMINISTRATOR</small></span>
            <el-dropdown trigger="click" size="mino">
                <span>
                    <img class="user" :src="userImg">
                </span>
                <el-dropdown-menu slot="dropdown">
<!--                    <el-dropdown-item>个人中心</el-dropdown-item>-->
                    <el-dropdown-item @click.native="logOut">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </header>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: "CommonHeader",
  data() {
    return {
      userImg: require('@/assets/电光logo.jpg')
    }
  },
  methods: {
    handleMenu() {
      this.$store.commit("collapseMenu")
    },
    logOut() {
      this.$store.commit('clearToken')
      this.$store.commit('clearMenu')
      this.$store.commit('clearUser')
      this.$router.push('/')
    },
  },
  computed: {
    ...mapState({
      tags: state => state.tab.tabsList,
    })
  }
}
</script>

<style lang="less" scoped>
header {
  display: flex;
  height: 100%;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  width: 100%;
}
.l-content {
  margin-left: 1vw;
  display: flex;
  align-items: center;
  .el-button {
      margin-right: 20px;
  }
}
.r-content {
  .user {
      width: 40px;
      height: 40px;
      border-radius: 50%;
  }
}
</style>
