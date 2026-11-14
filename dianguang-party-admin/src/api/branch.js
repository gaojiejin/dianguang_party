import { request } from "@/utils/axios";


/** 获取所有支部信息 */
export function getBranchManage(data) {
    return request({
        url: 'branch/getBranchManage',
        method: 'post',
        data: data,
    })
}

/** 查看支部名称和id的映射 */
export function queryIDNameMapping(data) {
    return request({
        url: 'branch/queryIDNameMapping',
        method: 'get',
        params: data,
    })
}

/** 添加支部 */
export function addBranch(data) {
    return request({
        url: 'branch/addBranch',
        method: 'post',
        data: data,
    })
}

/** 编辑支部 */
export function updateBranch(data) {
    return request({
        url: 'branch/updateBranch',
        method: 'post',
        data: data,
    })
}

/** 删除支部 */
export function deleteBranch(data) {
    return request({
        url: 'branch/deleteBranch',
        method: 'post',
        data: data,
    })
}