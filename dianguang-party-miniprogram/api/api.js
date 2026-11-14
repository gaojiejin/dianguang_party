// 引入 request 文件
import request from './index.js'
 
// 用户名登录
export const jobid_login = (params) => {
	return request({
		url: 'login/jobIDLogin',
		method: 'POST',
		data: params,
	})
}


// 验证码登录
export const smsLogin = (params) => {
	return request({
		url: 'sms/smsLogin',
		method: 'POST',
		data: params,
	})
}

//
export const getClassDuration = (classId) => {
  return request({
	url: 'classLearnInfo/getClassDuration',
    method: 'GET',
    params: { class_id: classId },
    headers: {
      'Content-Type': 'application/json',
    }
	
  });
}


// 改变支部学习状态
export const changeLearnDurationByBranch = (params) => {
	return request({
		url: 'classLearnInfo/changeLearnDurationByBranch',
		method: 'POST',
		data: params,
	})
}


// 获取验证码
export const getSmsCode = (params) => {
	return request({
		url: 'sms/sendSms',
		method: 'POST',
		data: params,
	})
}

// 获取全部课程
export const addUser = (params) => {
	return request({
		url: 'user/addUser',
		method: 'Post',
		data: params,
	})
}

// 通过ID获取用户信息
export const queryUserByJobID = (params) => {
	return request({
		url: 'user/queryByJobID',
		method: 'GET',
		data: params,
	})
}

// 通过手机号获取用户信息
export const queryByPhone = (params) => {
	return request({
		url: 'user/queryByPhone',
		method: 'GET',
		data: params,
	})
}

// 显示用户头像
export const queryUserAvatarByJobID = (params) => {
	return request({
		url: 'user/queryAvatar',
		method: 'GET',
		data: params,
	})
}


//  根据支部管理员获取支部信息
export const getBranchByManagerId = (params) => {
	return request({
		url: 'branch/getBranchByManagerId',
		method: 'POST',
		data: params,
	})
}
//  跟新密码
export const updatePasswordByPrePwd = (params) => {
	return request({
		url: 'user/updatePasswordByPrePwd',
		method: 'POST',
		data: params,
	})
}


// 获取所有用户信息
export const queryAllUser = (params) => {
	return request({
		url: 'user/queryAllUser',
		method: 'GET',
		data: params,
	})
}

// 获取支部ID与名称转化关系
export const queryIDNameMapping = (params) => {
	return request({
		url: 'branch/queryIDNameMapping',
		method: 'GET',
		data: params,
	})
}
// 获取所有支部
export const getBranches = (params) => {
	return request({
		url: 'branch/queryIDNameMapping',
		method: 'GET',
		data: params,
	})
}

// 获取所有支部
export const getBranches2 = (params) => {
	return request({
		url: 'branch/getBranches',
		method: 'POST',
		data: params,
	})
}

// 获取所有角色
export const getRoles = (params) => {
	return request({
		url: 'branch/queryIDNameMapping',
		method: 'GET',
		data: params,
	})
}
// 获取支部ID与名称转化关系
export const getBranchManage = (params) => {
	return request({
		url: 'branch/getBranchManage',
		method: 'POST',
		data: params,
	})
}

// 获取指定支部的所有用户系
export const getUsersByBranchID = (params) => {
	return request({
		url: 'branch/getUsersByBranchID',
		method: 'POST',
		data: params,
	})
}

// 获取学习统计情况
export const getLearnRateStatistics = (params) => {
	return request({
		url: 'learnRate/getLearnRateStatistics',
		method: 'Post',
		data: params,
	})
}

// 获取支部学习统计情况
export const getLearnRateBranchStatistics = (params) => {
	return request({
		url: 'learnRate/getLearnRateBranchStatistics',
		method: 'Post',
		data: params,
	})
}

// 获取全部课程
export const getClasses = (params) => {
	return request({
		url: 'class/getClasses',
		method: 'Post',
		data: params,
	})
}

// 获取全部课程和章节
export const getClassesAndChapter = (params) => {
	return request({
		url: 'class/getClassesAndChapter',
		method: 'Post',
		data: params,
	})
}

// 获取全部课程和章节
export const deleteClass = (params) => {
	return request({
		url: 'class/deleteClass',
		method: 'Post',
		data: params,
	})
}

// 获取全部课程和章节
export const deleteChapter = (params) => {
	return request({
		url: 'chapter/deleteChapter',
		method: 'Post',
		data: params,
	})
}

// 创建新的课程
export const addChapter = (params) => {
	return request({
		url: 'chapter/addChapter',
		method: 'Post',
		data: params,
	})
}

// 发布内容
export const createArticle = (params) => {
	return request({
		url: 'article/createArticle',
		method: 'Post',
		data: params,
	})
}

// 获取所有文章
export const queryAllArticle = (params) => {
	return request({
		url: 'article/queryAllArticle',
		method: 'Post',
		data: params,
	})
}

// 查看所有审核通过的文章
export const queryArticlePassed = (params) => {
	return request({
		url: 'article/queryArticlePassed',
		method: 'Post',
		data: params,
	})
}

// 获取用户学习详情
export const getUserLearnDetail = (params) => {
	return request({
		url: 'learnRate/getUserLearnDetail',
		method: 'Post',
		data: params,
	})
}


// 更新学习进度
export const updateLearnRate = (params) => {
	return request({
		url: 'learnRate/updateLearnRate',
		method: 'Post',
		data: params,
	})
}


// 删除OSS中的图片
export const deleteIm = (params) => {
	return request({
		url: 'class/delete',
		method: 'Post',
		data: params,
	})
}


// 获取所有的轮播图
export const getAllSwiper = (params) => {
	return request({
		url: 'swiper/getAllSwiper',
		method: 'Post',
		data: params,
	})
}

// 添加轮播图
export const addSwiper = (params) => {
	return request({
		url: 'swiper/addSwiper',
		method: 'Post',
		data: params,
	})
}

// 删除轮播图
export const deleteSwiperById = (params) => {
	return request({
		url: 'swiper/deleteSwiperById',
		method: 'Post',
		data: params,
	})
}

// 获取个人学习信息
export const getOwnLearnInfo = (params) => {
	return request({
		url: 'classLearnInfo/getOwnLearnInfo',
		method: 'GET',
		data: params,
	})
}

// 获取支部学习情况
export const getLearnRateStatisticsByBranchId = (params) => {
	return request({
		url: 'learnRate/getLearnRateStatisticsByBranchId',
		method: 'POST',
		data: params,
	})
}

// 添加支部
export const addBranch = (params) => {
	return request({
		url: 'branch/addBranch',
		method: 'POST',
		data: params,
	})
}

// 删除支部
export const deleteBranch = (params) => {
	return request({
		url: 'branch/deleteBranch',
		method: 'POST',
		data: params,
	})
}

// 修改支部信息
export const updateBranch = (params) => {
	return request({
		url: 'branch/updateBranch',
		method: 'POST',
		data: params,
	})
}

// 根据自定义条件查看文章
export const findArticlesByConditions = (params) => {
	return request({
		url: 'article/findArticlesByConditions',
		method: 'POST',
		data: params,
	})
}

// 根据支部管理元Id 查看管理员发布的文章
export const queryArticleById = (params) => {
	return request({
		url: 'article/queryArticleById',
		method: 'POST',
		data: params,
	})
}

// 查看驳回原因
export const getReviewList = (params) => {
	return request({
		url: 'articleReview/getReviewList',
		method: 'POST',
		data: params,
	})
}

//  重新提交文章
export const reSubmitArticle = (params) => {
	return request({
		url: 'article/reSubmitArticle',
		method: 'POST',
		data: params,
	})
}//  修改文章时间
export const editarticletime = (params) => {
	return request({
		url: 'article/changeArticleTime',
		method: 'POST',
		data: params,
	})
}


// 记录视频学习时长
export const addLearnDuration = (params) => {
	return request({
		url: 'classLearnInfo/addLearnDuration',
		method: 'POST',
		data: params,
	})
}

// 记录视频学习时长
export const uploadReviewState = (params) => {
	return request({
		url: 'articleReview/uploadReviewState',
		method: 'POST',
		data: params,
	})
}


// 删除文章
export const deleteArticle = (params) => {
	return request({
		url: 'article/deleteArticle',
		method: 'POST',
		data: params,
	})
}

// 上传文件到oss
export const upload = (params) => {
	return request({
		url: 'class/upload',
		method: 'POST',
		data: params,
		headers: {
			'Content-Type': 'multipart/form-data',
			'Authorization': uni.getStorageSync('token') || '',
		},
	})
}

// 查看章节名字是否重复
export const checkChapterName = (params) => {
	return request({
		url: 'chapter/checkChapterName',
		method: 'POST',
		data: params,
	})
}

// 获取所有党员
export const getPartyUser = (params) => {
	return request({
		url: 'user/getPartyUser',
		method: 'POST',
		data: params,
	})
}

// 获取所有积极分子
export const getActivistsUser = (params) => {
	return request({
		url: 'user/getActivistsUser',
		method: 'POST',
		data: params,
	})
}

// 获取批量导入的模板
export const getMoBan = (params) => {
	return request({
		url: 'user/getMoban',
		method: 'POST',
		data: params,
	})
}
// Export members using the local backend instead of a missing API function.
export const exportMembersData = (members) => request({
  url: 'user/exportMembers', method: 'POST', data: members,
  header: { 'Content-Type': 'application/json', 'Authorization': uni.getStorageSync('token') || '' }
});
