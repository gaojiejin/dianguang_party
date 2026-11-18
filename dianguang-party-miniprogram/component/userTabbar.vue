<template>
	<u-tabbar
	:value="managerValue"
	:fixed="true"
	:placeholder="false"
	activeColor="#d81e06"
	:safeAreaInsetBottom="true">
		<u-tabbar-item text="首页" icon="home" @click="ToHome()" ></u-tabbar-item>
		<u-tabbar-item text="学习" icon="grid" @click="ToLearn()" ></u-tabbar-item>
		<u-tabbar-item text="我的" icon="account" @click="ToMy()" ></u-tabbar-item>
	</u-tabbar>
</template>

<script>
	
	export default {
		props:{
			managerValue: {
				type: Number,
				required: true
			}
		},
		methods: {
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

<style>

</style>