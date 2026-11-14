<template><div class="dashboard"><section class="welcome-banner"><div><span class="eyebrow">初心如磐 · 使命在肩</span><h2>凝聚先锋力量，共建向光未来</h2><p>以组织建设为根基，以学习教育为动力。</p><el-button @click="$router.push('/publish')">发布新内容 <i class="el-icon-right"/></el-button></div><div class="banner-emblem">光<span>信仰有光 · 奋进有为</span></div></section><div class="quick-links"><router-link to="/manage"><i class="el-icon-document"/><div><strong>内容管理</strong><span>发布动态，传递组织声音</span></div><i class="el-icon-top-right"/></router-link><router-link to="/branchManage"><i class="el-icon-office-building"/><div><strong>支部管理</strong><span>夯实基础，凝聚支部力量</span></div><i class="el-icon-top-right"/></router-link><router-link to="/learnManager"><i class="el-icon-reading"/><div><strong>学习管理</strong><span>以学促行，筑牢信仰根基</span></div><i class="el-icon-top-right"/></router-link></div><div class="dashboard-grid"><el-card class="intro-card"><div slot="header"><span class="section-kicker">ABOUT THE PLATFORM</span><h3>数字赋能，党建同行</h3></div><p>{{ brief }}</p><div class="intro-signature"><img src="@/assets/电光logo.jpg" alt="电光学院标识"/><div>电光学院<small>党建工作管理平台</small></div></div></el-card><el-card class="news-card"><div slot="header"><span class="section-kicker">HIGHLIGHTS</span><h3>党建风采</h3></div><el-carousel height="290px" :interval="5000" indicator-position="outside"><el-carousel-item v-for="(item,index) in swiper" :key="index"><img :src="item.url" alt="党建风采" class="highlight-image"/></el-carousel-item><el-carousel-item v-if="!swiper.length"><div class="highlight-empty"><i class="el-icon-picture-outline"/><span>记录奋进足迹，展示支部风采</span></div></el-carousel-item></el-carousel></el-card></div></div></template>
<script>
import { getAllSwiper } from '@/api/swiper'

export default {
  data() {
    return {
      swiper: [],
      brief:'电光学院“云端党建”通过整合资源，用好大数据管理“一朵云”，通过动态采集，及时开展对日常数据的有效存储、深入分析和快速研判，及时跟踪了解基层党建工作情况。系统设有时政要闻、支部风采、微党课展示、学习站点等版块，助推党建治理现代化。',
      func: '本项目内容管理、支部管理和学习管理三大功能。内容管理功能包括内容发布、审核和轮播图管理功能，用于维护和更新网站或应用的内容。支部管理功能包括对支部、党员和积极分子的信息进行管理，学习管理功能，涵盖课程管理和章节发布，旨在促进组织内部的信息流通和学习资源的高效利用，并查看各类用户的学习信息',
    }
  },
  mounted() {
    getAllSwiper().then((res) => {
      this.swiper = res.data
    })
  }
}
</script>