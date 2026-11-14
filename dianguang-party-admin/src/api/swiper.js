import {request} from "@/utils/axios";
import Cookie from "js-cookie";

const token = Cookie.get('token')

/** 获取所有轮播图 */
export function getAllSwiper(data) {
    return request({
        url: 'swiper/getAllSwiper',
        method: 'post',
        data: data,
    })
}

/** 添加轮播图 */
export function addSwiper(data) {
    return request({
        url: 'swiper/addSwiper',
        method: 'post',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token ? `Bearer ${token}` : undefined,
        },
    })
}

/** 根据id删除轮播图 */
export function deleteSwiperById(data) {
    return request({
        url: 'swiper/deleteSwiperById',
        method: 'post',
        data: data,
    })
}