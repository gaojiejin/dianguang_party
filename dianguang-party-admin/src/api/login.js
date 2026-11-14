import { request } from "@/utils/axios";

/** 用户登录 */
export function login(data) {
    return request({
        url: 'login/jobIDLogin', // 自定义接口地址
        method: 'post', // 自定义请求方法
        data: data, // 用户登录信息
    })
}