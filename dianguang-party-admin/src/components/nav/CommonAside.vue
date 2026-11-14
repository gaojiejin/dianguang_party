<template>
  <el-menu :default-active="activeIndex" class="el-menu-vertical-demo" active-text-color="#ffd04b"
           :collapse="isCollapse" text-color="#fff" background-color="rgb(40,44,55)">
    <h3 class="menu-title"><span class="brand-symbol">光</span><span v-if="!isCollapse">电光党建<small>管理工作台</small></span></h3>

    <!-- 渲染无子菜单的菜单 -->
    <el-menu-item @click="clickMenu(item)" v-for="item in noChildren" :index="item.path" :key="item.path">
      <i :class="'el-icon-' + item.icon"></i>
      <span style="font-size: 16px; margin-left: 2px" slot="title">{{ item.label }}</span>
    </el-menu-item>

    <!-- 渲染有子菜单的菜单 -->
    <el-submenu v-for="item in hasChildren" :index="item.path" :key="item.path">
      <template slot="title">
        <i :class="'el-icon-' + item.icon"></i>
        <span style="font-size: 16px; margin-left: 2px" slot="title">{{ item.label }}</span>
      </template>
      <el-menu-item-group v-for="(subItem) in item.children" :key="subItem.path">
        <el-menu-item style="font-size: 15px;" @click="clickMenu(subItem)" :index="subItem.path">{{ subItem.label }}</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<script>
import Cookie from "js-cookie";
import Vue from "vue";
export default {
  data() {
    return {
      menu: [],
      activeIndex: '/home'  // 默认首页
    };
  },
  watch: {
    $route() {
      this.setCurrentRoute();
    }
  },
  mounted() {
    this.setCurrentRoute();  // 页面加载时调用
  },
  methods: {
    clickMenu(item) {
      this.$router.push({
        name: item.name
      });
      this.$store.commit('selectMenu', item);
    },
    setCurrentRoute() {
      this.activeIndex = this.$route.path;
      const menu = Cookie.get('menu');

      if (menu) {
        try {
          // 解析菜单项
          const parsedMenu = JSON.parse(menu);
          // 在解析的菜单中查找与当前路由 path 匹配的 item
          const currentItem = this.findMenuItemByPath(parsedMenu, this.$route.path);
          // 如果找到了当前的菜单项，提交到 Vuex 中
          if (currentItem) {
            this.clickMenu(Vue.observable(currentItem))
          } else {
            console.log('当前路由未找到对应的菜单项');
          }
        } catch (error) {
          console.error('解析菜单时出错:', error);
        }
      } else {
        console.log('Cookie 中未找到菜单');
      }
    },
    // 辅助方法：递归查找菜单项
    findMenuItemByPath(menu, path) {
      for (const item of menu) {
        if (item.path === path) {
          return item;
        }
        // 如果当前 item 有子菜单，递归查找子菜单
        if (item.children && item.children.length > 0) {
          const foundItem = this.findMenuItemByPath(item.children, path);
          if (foundItem) {
            return foundItem;
          }
        }
      }
      return null;  // 如果没有找到对应的菜单项，返回 null
    }
  },
  computed: {
    noChildren() {
      return this.asyncMenu.filter(item => !item.children);
    },
    hasChildren() {
      return this.asyncMenu.filter(item => item.children);
    },
    isCollapse() {
      return this.$store.state.tab.isCollapse;
    },
    asyncMenu() {
      return this.$store.state.tab.menu;
    }
  }
};
</script>

<style lang="less" scoped>
.el-menu-vertical-demo:not(.el-menu--collapse)  {
  height: 100vh;   /* 确保菜单占满视口高度 */
  width: 15vw;
}

.el-menu {
  height: 100vh;  /* 菜单折叠和展开时都占满整个高度 */
  border: none;
  background-color: #001428;  /* 深灰色背景 */
}

.menu-title {
  height: 4vh;
  line-height: 4vh;
  color: #ffffff;
  text-align: center;
  font-size: 23px;
}

.el-menu-item {
  transition: background-color 0.3s ease;
}

.el-menu-item:hover,
.el-menu-item:focus {
  background-color: rgba(255, 255, 255, 0.1);  /* 悬停效果 */
}

.el-menu-item.is-active {
  background-color: rgba(255, 208, 75, 0.2);  /* 激活状态背景色 */
  border-right: 4px solid #ffd04b;  /* 激活状态的边框颜色 */
}

.el-menu-item-group {
  margin: 0;
}
.el-submenu .el-submenu__title {
  font-size: 16px;
  transition: all 0.3s ease;
}

.el-submenu .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);  /* 子菜单悬停效果 */
}

.el-menu-item-group {
  padding-left: 10px;  /* 子菜单缩进 */
}

/* 调整折叠时的宽度 */
.el-menu--collapse .el-menu-item,
.el-menu--collapse .el-submenu__title {
  justify-content: center;
  text-align: center;
  padding: 0;
}

.el-menu-item:focus,
.el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.15) !important;
}

.el-submenu .el-submenu__title:hover {
  color: #ffd04b;
  background-color: rgba(255, 255, 255, 0.1);
}
</style>
