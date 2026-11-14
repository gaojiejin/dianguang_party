<template>
	<view v-if="article.imagePath === ''" style="width: 90%; height: 10vh; margin-left: 5%; border-bottom: 1px solid #abbedb; display: flex; flex-direction: row;"
		@click="ToImageText">
		<view style="width: 100%;">
			<view class="title1">
				<text>{{article.title}}</text>
			</view>
			<view class="type1">
                <text v-if="article.type === '链接展示'" style="width: 40%;">微课堂展示</text>
                <text v-else style="width: 40%;">{{article.type}}</text>
				<text style="margin-left: 1vm;">{{ time }}</text>
			</view>	
		</view>
	</view>
	<view v-else style="width: 90%; height: 14vh; margin-left: 5%; border-bottom: 1px solid #abbedb; display: flex; flex-direction: row;"
		@click="ToImageText">
		<view style="width: 60%;">
			<view class="title2">
				<text>{{article.title}}</text>
			</view>
			<view class="type2">
                <text v-if="article.type === '链接展示'" style="width: 40%;">微课堂展示</text>
                <text v-else style="width: 40%;">{{article.type}}</text>
				<text style="margin-left: 1vm;">{{ time }}</text>
			</view>	
		</view>
		<view style="width: 40%;">
			<image :src="article.imagePath.split(',')[0]" mode="scaleToFill" style="margin-top: 1vh; margin-left: 10%; height: 12vh; width: 90%;"></image>
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
					url:`/pages/component/image/ImageTextReject?article=${encodeURIComponent(JSON.stringify(this.article))}`
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
	height: 6vh;
	font-size: 2vh;
	line-height: 6vh;
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
	height: 4vh;
	line-height: 3vh;
	display: flex;
	flex-direction: row;
	font-size: 1.8vh;
	color: #abbedb;
	text-align: left;
	overflow: hidden;
}
.title2{
	height: 8vh;
	font-size: 2vh;
	line-height: 4vh;
	margin-top: 1vh;
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
	height: 4vh;
	line-height: 3vh;
	margin-top: 1vh;
	display: flex;
	flex-direction: row;
	font-size: 1.8vh;
	color: #abbedb;
	text-align: left;
	overflow: hidden;
}
</style>