import { request } from "@/utils/axios";

/** 获取所有学习信息 */
export function getLearnRateStatistics(data) {
    return request({
        url: 'learnRate/getLearnRateStatistics', // 自定义接口地址
        method: 'post', // 自定义请求方法
        data: data, // 用户登录信息
    })
}

/** 获取所有支部学习信息 */
export function getLearnRateBranchStatistics(data) {
    return request({
        url: 'learnRate/getLearnRateBranchStatistics', // 自定义接口地址
        method: 'post', // 自定义请求方法
        data: data, // 用户登录信息
    })
}