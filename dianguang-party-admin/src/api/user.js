import { request } from "@/utils/axios";
import Cookie from "js-cookie";

const token = Cookie.get('token')

/** 获取用户信息 */
export function queryByJobID(data) {
    return request({
        url: 'user/queryByJobID',
        method: 'get',
        params: data,
    })
}

/** 获取用户信息 */
export function queryAllUser(data) {
    return request({
        url: 'user/queryAllUser',
        method: 'get',
        params: data,
    })
}

/** 添加单一用户 */
export function addUser(data) {
    return request({
        url: 'user/addUser',
        method: 'post',
        data: data,
    })
}

/** 批量添加用户 */
export function uploadAdminFile(data) {
    return request({
        url: 'user/uploadAdminFile',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token ? `Bearer ${token}` : undefined,
        },
    })
}

/** 修改用户信息 */
export function update(data) {
    return request({
        url: 'user/update',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json',
            Authorization: token ? `Bearer ${token}` : undefined,
        }
    })
}

/** 删除用户信息 */
export function deleteUser(data) {
    return request({
        url: 'user/delete',
        method: 'post',
        data: data,
    })
}

/** 获取批量导入用户的模板 */
export function getMoban(data) {
    return request({
        url: 'user/getMoban',
        method: 'post',
        data: data,
    })
}