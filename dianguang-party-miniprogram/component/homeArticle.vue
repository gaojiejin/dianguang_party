<template>
 <view class="feed-article" @click="open">
  <view class="feed-copy"><text class="feed-title">{{article.title}}</text><view class="feed-meta"><text class="feed-type">{{article.type==='链接展示'?'微课堂展示':article.type}}</text><text>{{date}}</text></view></view>
  <image v-if="cover&&!broken" class="feed-cover" :src="cover" mode="aspectFill" @error="broken=true"/>
 </view>
</template>
<script>
export default {
 props:{article:{type:Object,required:true}},data:()=>({broken:false}),
 computed:{cover(){return (this.article.imagePath||'').split(',')[0]},date(){return (this.article.uploadTime||'').replace('T',' ').slice(0,10)}},
 methods:{open(){uni.navigateTo({url:'/pages/component/image/ImageText?article='+encodeURIComponent(JSON.stringify(this.article))})}}
}
</script>
<style>
.feed-article { display:flex; flex-direction:row; align-items:center; padding:28rpx 0; margin:0 28rpx; border-bottom:1rpx solid #e3e7ed; }
.feed-copy { flex:1; min-width:0; }
.feed-title { display:-webkit-box; -webkit-box-orient:vertical; -webkit-line-clamp:2; overflow:hidden; word-break:break-all; font-size:29rpx; line-height:1.7; color:#262626; }
.feed-meta { display:flex; flex-direction:row; flex-wrap:wrap; margin-top:16rpx; gap:8rpx 20rpx; font-size:23rpx; color:#8f9db0; line-height:1.6; }
.feed-type { color:#9aabc0; }.feed-cover { width:194rpx; height:140rpx; flex-shrink:0; margin-left:24rpx; border-radius:8rpx; }
</style>
