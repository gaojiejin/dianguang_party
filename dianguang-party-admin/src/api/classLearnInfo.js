import { request } from "@/utils/axios";
import Cookie from "js-cookie";

const token = Cookie.get('token')

/** 查看驳回原因 */
export function exportAllLearnStats(data) {
    return request({
        url: 'classLearnInfo/exportAllLearnStats',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json',
            Authorization: token ? `Bearer ${token}` : undefined,
        }
    })
}


/** 获取所有已经结业的积极分子 */
export function getRoleLearnedList(data) {
    return request({
        url: 'classLearnInfo/getRoleLearnedList',
        method: 'get',
        params: data,
    })
}

/** 积极分子结业 */
export function finishTrain(data) {
    return request({
        url: 'classLearnInfo/finishTrain',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'application/json',
            Authorization: token ? `Bearer ${token}` : undefined,
        }
    })
}

/** 导出培训证书 */
export function exportCertificate(data) {
    return request({
        url: 'classLearnInfo/exportCertificate',
        method: 'post',
        data: data,
    })
}

/** 删除培训证书 */
export function deleteCertificate(data) {
    return request({
        url: 'classLearnInfo/deleteCertificate',
        method: 'post',
        data: data,
    })
}

/** 根据支部修改学习信息 */
export function changeLearnDurationByBranch(data) {
    return request({
        url: 'classLearnInfo/changeLearnDurationByBranch',
        method: 'post',
        data: data,
    })
}