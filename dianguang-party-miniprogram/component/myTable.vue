<template><view class="m-article" @click="ToImageText"><view class="m-article-copy"><text class="m-article-title">{{article.title}}</text><view class="m-article-meta"><text class="u-article-type">{{article.type}}</text><text>{{time}}</text></view></view><image v-if="article.imagePath" :src="article.imagePath.split(',')[0]" mode="aspectFill" class="m-article-cover"/></view></template>
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
					url:`/pages/component/image/ImageText?article_id=${this.article.articleId}`
				})
			}
		},
		mounted() {
			this.time = formatDate2(this.article.uploadTime); // 使用 this.article 来获取上传时间
		},
	}
</script>
<style lang="scss">
.m-article{display:flex;flex-direction:row;align-items:center;gap:24rpx;background:#fff;border-radius:16rpx;padding:26rpx;margin:18rpx 0}.m-article-copy{flex:1;min-width:0}.m-article-title{display:-webkit-box;-webkit-box-orient:vertical;-webkit-line-clamp:2;overflow:hidden;font-size:29rpx;color:#282a2f;line-height:1.7}.m-article-meta{display:flex;flex-direction:row;gap:14rpx;flex-wrap:wrap;font-size:19rpx;color:#9398a1;margin-top:20rpx}.m-article-meta .u-article-type{color:#b1273c}.m-article-cover{width:190rpx;height:138rpx;flex-shrink:0;border-radius:12rpx}
</style>
