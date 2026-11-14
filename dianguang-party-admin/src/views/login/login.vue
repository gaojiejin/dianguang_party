<template>
  <div class="login-scene">
    <section class="login-story"><div class="brand-lockup"><img src="@/assets/电光logo.jpg" alt="电光学院"/><span>电光党建<small>DIANGUANG · PARTY BUILDING</small></span></div><div class="story-copy"><span class="eyebrow">凝心聚力 · 向光而行</span><h1>让信仰扎根<br/>让先锋力量生长。</h1><p>连接每一个支部，凝聚每一份力量。<br/>以数字赋能党建，让组织与学习触手可及。</p><div class="story-line"></div><span class="story-note">组织建设 / 思想引领 / 学习教育</span></div><footer>电光学院 · 党建工作管理平台</footer></section>
    <section class="login-panel"><div class="login-box"><span class="eyebrow">WELCOME BACK</span><h2>欢迎登录</h2><p class="login-subtitle">电光党建管理工作台</p><el-form ref="loginFormRef" :model="loginFormData" @submit.native.prevent="handleLogin"><label>管理账号</label><el-form-item prop="username"><el-input v-model.trim="loginFormData.job_id" placeholder="请输入管理账号" prefix-icon="el-icon-user" autocomplete="username" /></el-form-item><label>登录密码</label><el-form-item prop="password"><el-input v-model.trim="loginFormData.password" placeholder="请输入登录密码" prefix-icon="el-icon-lock" type="password" show-password autocomplete="current-password" @keyup.enter.native="handleLogin" /></el-form-item><el-button class="login-submit" type="primary" @click.prevent="handleLogin">登录工作台 <i class="el-icon-right" /></el-button></el-form><div class="login-footnote"><i class="el-icon-lock"/> 电光党建 · 统一管理入口</div></div><span class="panel-footer">笃学 · 明德 · 求实 · 创新</span></section>
  </div>
</template>
<script>
import Owl from "@/views/login/Owl.vue";
import { login } from "@/api/login";
import { queryByJobID } from '@/api/user'
import { queryIDNameMapping } from '@/api/branch'
import axios from "axios"
export default {
  name: 'login',
  data() {
    return{
      loginFormData: {
        job_id: '',
        password: '',
      },
      isFocus: false
    }
  },
  components: {
    Owl,
  },

  methods: {
    handleLogin(){
      console.log('登录')
      login(this.loginFormData).then((res) => {
        if (res.status === 200 && res.data.token !== undefined) {
          this.$store.commit('setToken', res.data.token)
          const data = {
            job_id: this.loginFormData.job_id
          };
          console.log(data)
          // 获取支部id 和 支部名称的映射关系
          queryIDNameMapping().then((res)=>{
            console.log(res)
            this.$store.state.branchIDNameMapping = res.data
          })
          // 获取用户信息
          queryByJobID(data).then((res) => {
            if (res.data.role === '超级管理员'){
              this.$store.commit('setUser', res.data)
              this.$message.success('登录成功')
              this.$store.commit('setMenu', this.$router.options.routes[1].children)
              console.log(this.$store.state.user)
              this.$router.push({name: 'home'})
            }
            else {
              this.$message.warning('无权限登录')
            }
          })
        }
        else {
          this.$message.warning(res.data)
        }
      })

    },
    handleBlur() {
      this.isFocus = false; // 失去焦点时更新状态
    },
    handleFocus() {
      this.isFocus = true; // 获取焦点时更新状态
    }
  }
}
</script>