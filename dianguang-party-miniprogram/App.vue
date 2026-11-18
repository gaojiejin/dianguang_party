<script>
	import { jobid_login, queryUserByJobID,queryUserAvatarByJobID } from '@/api/api.js'
	export default {
		onLaunch: function() {
			let token = uni.getStorageSync('token')
			let job_id = uni.getStorageSync('job_id')
			let phone = uni.getStorageSync('phone')
			// 管理员端
			if (token != '' && job_id != ''){
				var jobId_data = {
					job_id: job_id
				} 
				queryUserByJobID(jobId_data).then((res)=>{
					console.log('获取用户信息成功', res);
					this.$store.state.user = res
					this.$store.state.role = res.role
					console.log(this.$store.state.user)
					if (res.role === "超级管理员"){
						uni.switchTab({
							url:'/pages/manager/home/home',
						})
						console.log("管理端")
					}else{
						uni.switchTab({
							url:'/pages/user/home/home',
						})
						console.log("用户端")
					}			
				}).catch((err) =>{
					console.error('获取用户信息失败', err);
				})
			}else{
				console.log('首次登录')
			}
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	page {
		background-color: #f7f7f7;
	}
</style>
