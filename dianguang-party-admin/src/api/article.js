import { request } from "@/utils/axios";
import Cookie from "js-cookie";

const token = Cookie.get('token')

/** 查看所有待审核的文章 */
export function queryArticleNeedReview(data) {
    return request({
        url: 'article/queryArticleNeedReview',
        method: 'post',
        data: data,
    })
}

/** 查看所有已审核的文章 */
export function queryArticlePassed(data) {
    return request({
        url: 'article/queryArticlePassed',
        method: 'post',
        data: data,
    })
}

/** 查看所有被驳回的文章 */
export function queryArticleRejected(data) {
    return request({
        url: 'article/queryArticleRejected',
        method: 'post',
        data: data,
    })
}

/** 根据文章id修改内容 */
export function changeArticleTime(data) {
    return request({
        url: 'article/changeArticleTime',
        method: 'post',
        data: data,
    })
}

/** 添加文章 */
export function createArticle(data) {
    return request({
        url: 'article/createArticle',
        method: 'post',
        data: data,
    })
}

/** 添加文章 */
export function deleteArticle(data) {
    return request({
        url: 'article/deleteArticle',
        method: 'post',
        data: data,
    })
}