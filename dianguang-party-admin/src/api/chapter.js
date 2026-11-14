import { request } from "@/utils/axios";
import Cookie from "js-cookie";
const token = Cookie.get('token')

/** 根据课程获取章节 */
export function getChapterByClassId(data) {
    return request({
        url: 'chapter/getChapterByClassId',
        method: 'post',
        data: data,
    })
}

/** 添加章节 */
export function addChapter(data) {
    return request({
        url: 'chapter/addChapter',
        method: 'post',
        data: data,
    })
}

/** 根据章节id 删除章节 */
export function deleteChapter(data) {
    return request({
        url: 'chapter/deleteChapter',
        method: 'post',
        data: data,
    })
}

/** 修改章节时间 */
export function changeChapterTime(data) {
    return request({
        url: 'chapter/changeChapterTime',
        method: 'post',
        data: data,
    })
}