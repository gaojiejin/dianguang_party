import axios from "axios"
import { Message } from "element-ui"
import { get, merge } from "lodash-es"
import Cookie from "js-cookie";

/** 退出登录并强制刷新页面（会重定向到登录页） */
function logout() {
    Cookie.remove('token')
    window.location.hash = '/'
    Message.warning('登录已失效，请重新登录')
}

/** 创建请求实例 */
function createService() {
    // 创建一个 axios 实例
    const service = axios.create()
    // 请求拦截
    service.interceptors.request.use(
        (config) => config,
        // 发送失败
        (error) => Promise.reject(error)
    )
    // 响应拦截
    service.interceptors.response.use(
        (response) => {
            const apiData = response.data
            const responseType = response.request?.responseType
            if (response.status >= 200 && response.status < 300) {
                const failed = apiData && typeof apiData === 'object' && (apiData.success === false || apiData.code === 500);
                if (failed) {
                    const error = new Error(apiData.message || apiData.msg || '操作失败，请检查填写的信息');
                    Message.error(error.message);
                    return Promise.reject(error);
                }
                return response;
            }
        },
        (error) => {
            const status = get(error, "response.status")
            switch (status) {
                case 400:
                    error.message = "请求错误"
                    break
                case 401:
                    logout()
                    break
                case 403:
                    error.message = "拒绝访问"
                    break
                case 404:
                    error.message = "请求地址出错"
                    break
                case 408:
                    error.message = "请求超时"
                    break
                case 500:
                    error.message = "服务器内部错误"
                    break
                case 501:
                    error.message = "服务未实现"
                    break
                case 502:
                    error.message = "网关错误"
                    break
                case 503:
                    error.message = "服务不可用"
                    break
                case 504:
                    error.message = "网关超时"
                    break
                case 505:
                    error.message = "HTTP 版本不受支持"
                    break
                default:
                    break
            }
            const detail = error.response && error.response.data;
            if (typeof detail === 'string' && detail.length < 180) error.message = detail;
            else if (detail && (detail.message || detail.msg)) error.message = detail.message || detail.msg;
            if (!error.response) error.message = '网络连接失败，请检查本地后端是否启动';
            Message.error(error.message)
            return Promise.reject(error)
        }
    )
    return service
}

/** 创建请求方法 */
function createRequest(service) {
    return function(config) { // 这里使用普通函数
        const token = Cookie.get('token')
        const defaultConfig = {
            headers: {
                Authorization: token ? `Bearer ${token}` : undefined,
                "Content-Type": "application/x-www-form-urlencoded"
            },
            timeout: 60 * 5000,
            // baseURL: 'http://127.0.0.1:8080/',
            baseURL: process.env.VUE_APP_API_BASE_URL || 'http://127.0.0.1:8080/',
            // baseURL: 'https://www.dgdy123.cn/',
            data: {}
        };
        const mergeConfig = merge(defaultConfig, config);
        return service(mergeConfig);
    }
}


// 创建请求实例和请求方法
const service = createService()
export const request = createRequest(service)

