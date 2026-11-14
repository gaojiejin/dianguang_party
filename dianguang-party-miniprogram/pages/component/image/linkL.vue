<template>
	<view class="container">
		<web-view :src="webUrl"></web-view>
	</view>
</template>

<script>
import { addLearnDuration } from '@/api/api.js'
import { formatDate } from '@/utils/formatDate.js'

export default {
	data() {
		return {
			chapter: '',
			webUrl: '',
			class_id: '',
			time: '',
		}
	},
	onLoad(options) {
		this.chapter = JSON.parse(decodeURIComponent(options.chapter));	
		this.webUrl = this.chapter.content;
		this.time = formatDate(this.chapter.uploadTime);
		console.log(this.chapter);
	},
	methods: {

	},
	onUnload() {
		// 页面卸载时，清除计时器，避免内存泄漏
		if (this.timer) {
			clearInterval(this.timer);
		}
	}
}
</script>
