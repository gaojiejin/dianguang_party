<template>
	<view>
		<view class="page-body">
			<view class="page-section">
				<video id="myVideo" src="https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/wap2appvsnative.mp4" 
				style="width: 100%;"
				@play="videoPlay"
				@pause="videoPause"
				@error="videoErrorCallback" 
				@ended="videoEnded"
				:danmu-list="danmuList"
				show-mute-btn
				enable-play-gesture
				controls
				></video>
			</view>
		</view>
	</view>
</template>


<script>
	export default {
		data() {
			return {

			}
		},
		onReady: function (res) {
			this.videoContext = uni.createVideoContext('myVideo')
		},
		methods: {
			videoErrorCallback: function (e) {
				console.log('视频错误信息:')
				console.log(e.target.errMsg)
			},
			videoPlay(){
				console.log('视频播放');

			},
			videoPause(){
				console.log('视频暂停');
			},
			videoEnded() {
			  console.log('视频播放结束');
			  // 在此处添加视频结束后的处理逻辑
			},
		}
	}

</script>

<style>
.controls {
	display: flex;
	justify-content: center;
	margin-top: 10px;
}
button {
	margin: 0 5px;
	padding: 10px 20px;
	background-color: #007aff;
	color: #fff;
	border: none;
	border-radius: 5px;
}
button:hover {
	background-color: #005bb5;
}
</style>