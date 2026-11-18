<template><view class="r-nav-host"><view class="r-nav-spacer"/><view class="r-bottom-nav"><view v-for="(item,index) in items" :key="index" :class="['r-nav-item',managerValue===index?'r-nav-active':'']" @click="navigate(index)"><party-icon :type="item.icon" size="25" :color="managerValue===index?'#C91010':'#8d99aa'"/><text class="r-nav-label">{{item.text}}</text></view></view></view></template>
<script>
	
	export default {data:()=>({items:[{text:'首页',icon:'home'},{text:'学习',icon:'list'},{text:'我的',icon:'person'}]}),
		props:{
			managerValue: {
				type: Number,
				required: true
			}
		},
		methods: {navigate(index){[this.ToHome,this.ToLearn,this.ToMy][index]()},
			ToHome(){
				if (this.managerValue !== 0){
					if (uni.getStorageSync('role') === '超级管理员'){
						uni.switchTab({
							url: '/pages/manager/home/home'
						})
					}
					else {
						uni.switchTab({
							url: '/pages/user/home/home'
						})
					}
					
				}			
			},
			ToLearn(){
				if (this.managerValue !== 1){
					if (uni.getStorageSync('role') === '超级管理员'){
						uni.switchTab({
							url: '/pages/manager/learn/learn'
						})
					}
					else {
						uni.switchTab({
							url: '/pages/user/learn/learn'
						})
					}		
				}	
			},
			ToMy(){
				if (this.managerValue !== 2){
					uni.switchTab({
						url: '/pages/my/my'
					})
				}				
			},
		}
	}
</script>
<style lang="scss">@import "../styles/responsive-navigation.scss";</style>
