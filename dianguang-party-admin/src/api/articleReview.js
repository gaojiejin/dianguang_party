import {request} from "@/utils/axios";
import Cookie from "js-cookie";

const token = Cookie.get('token')
/** 审核或驳回文章 */
export function uploadReviewState(data) {
    return request({
        url: 'articleReview/uploadReviewState',
        method: 'post',
        data: data,
    })
}

/** 查看驳回原因 */
export function getReviewList(data) {
    return request({
        url: 'articleReview/getReviewList',
        method: 'post',
        data: data,
    })
}


