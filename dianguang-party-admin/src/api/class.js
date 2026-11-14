import { request } from "@/utils/axios";
import Cookie from "js-cookie";
const token = Cookie.get('token')

/** 获取所有课程 */
export function getClasses(data) {
    return request({
        url: 'class/getClasses',
        method: 'post',
        data: data,
    })
}

/** 添加课程 */
export function addClass(data) {
    return request({
        url: 'class/addClass',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token ? `Bearer ${token}` : undefined,
        },
    })
}

/** 删除课程 */
export function deleteClass(data) {
    return request({
        url: 'class/deleteClass',
        method: 'post',
        data: data,
    })
}

/** 修改课程时间 */
export function changeClassTime(data) {
    return request({
        url: 'class/changeClassTime',
        method: 'post',
        data: data,
    })
}

/** 上传文件到OSS */
export function upload(data) {
    return request({
        url: 'class/upload',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token ? `Bearer ${token}` : undefined,
        },
    })
}