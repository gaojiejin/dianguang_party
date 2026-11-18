<template>
	<view v-if="article.imagePath === ''" style="width: 90%; height: 140rpx; margin-left: 5%; border-bottom: 1px solid #abbedb; display: flex; flex-direction: row;"
		@click="ToImageText">
		<view style="width: 100%;">
			<view class="title1">
				<text>{{article.title}}</text>
			</view>
			<view class="type1">
                <text v-if="article.type === '链接展示'" style="width: 40%;">微课堂展示</text>
                <text v-else style="width: 40%;">{{article.type}}</text>
				<text style="margin-left: 14rpx;">{{ time }}</text>
			</view>	
		</view>
	</view>
	<view v-else style="width: 90%; height: 196rpx; margin-left: 5%; border-bottom: 1px solid #abbedb; display: flex; flex-direction: row;"
		@click="ToImageText">
		<view style="width: 60%;">
			<view class="title2">
				<text>{{article.title}}</text>
			</view>
			<view class="type2">
                <text v-if="article.type === '链接展示'" style="width: 40%;">微课堂展示</text>
                <text v-else style="width: 40%;">{{article.type}}</text>
				<text style="margin-left: 14rpx;">{{ time }}</text>
			</view>	
		</view>
		<view style="width: 40%;">
			<image :src="article.imagePath.split(',')[0]" mode="scaleToFill" style="margin-top: 14rpx; margin-left: 10%; height: 168rpx; width: 90%;"></image>
		</view>
	</view>
</template>

<script>
	import { formatDate2 } from '@/utils/formatDate.js'
	export default {
		data(){
			return{
				time: '',
			}
		},
		props: {
			article: '',
			
		},
		methods: {
			ToImageText(){
				uni.navigateTo({
					url:`/pages/component/image/ImageTextReview?article=${encodeURIComponent(JSON.stringify(this.article))}`
				})
			}
		},
		mounted() {
			this.time = formatDate2(this.article.uploadTime); // 使用 this.article 来获取上传时间
		},
	}
</script>

<style>
.title1{
	height: 84rpx;
	font-size: 28rpx;
	line-height: 84rpx;
	color: #000;
	overflow: hidden;
	word-break: break-all; 
	text-overflow:ellipsis; 
	display: -webkit-box; 
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 1; 
}
.type1{
	width: 60%;
	height: 56rpx;
	line-height: 42rpx;
	display: flex;
	flex-direction: row;
	font-size: 25.2rpx;
	color: #abbedb;
	text-align: left;
	overflow: hidden;
}
.title2{
	height: 112rpx;
	font-size: 28rpx;
	line-height: 56rpx;
	margin-top: 14rpx;
	justify-content: center;
	align-items: center;
	color: #000;
	text-align: left;
	overflow: hidden;
	word-break: break-all; 
	text-overflow:ellipsis; 
	display: -webkit-box; 
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 2; 
	
}
.type2{
	height: 56rpx;
	line-height: 42rpx;
	margin-top: 14rpx;
	display: flex;
	flex-direction: row;
	font-size: 25.2rpx;
	color: #abbedb;
	text-align: left;
	overflow: hidden;
}
</style>