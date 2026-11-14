<template><view><view class="m-bottom-spacer" style="height:150rpx;"></view><view class="m-bottom-nav" style="position:fixed;left:0;right:0;bottom:0;display:flex;flex-direction:row;z-index:9999;background:#fff;padding:16rpx 0;padding-bottom:calc(16rpx + env(safe-area-inset-bottom));border-top:1rpx solid #eee;"><view style="width:33.333%;display:flex;flex-direction:column;align-items:center;font-size:24rpx;" :class="managerValue===0?'m-nav-active':''" @click="ToHome"><view class="u-nav-icon u-icon-home"><view class="u-icon-roof"/></view><text>首页</text></view><view style="width:33.333%;display:flex;flex-direction:column;align-items:center;font-size:24rpx;" :class="managerValue===1?'m-nav-active':''" @click="ToLearn"><view class="u-nav-icon u-icon-book"><view class="u-icon-spine"/></view><text>学习</text></view><view style="width:33.333%;display:flex;flex-direction:column;align-items:center;font-size:24rpx;" :class="managerValue===2?'m-nav-active':''" @click="ToMy"><view class="u-nav-icon u-icon-user"><view class="u-icon-shoulders"/></view><text>我的</text></view></view></view></template>
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
					uni.switchTab({
						url: '/pages/manager/home/home'
					})
				}			
			},
			ToLearn(){
				if (this.managerValue !== 1){
					uni.switchTab({
						url: '/pages/manager/learn/learn'
					})
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


<style lang="scss">
@import "../styles/navigation.scss";
</style>
