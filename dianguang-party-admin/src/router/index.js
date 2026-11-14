import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

//标记，避免重复进入相同路由时的报错
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch((err) => err);
};

const router = new VueRouter({
    mode: 'hash', // 使用 hash 模式
    routes: [
        {
            path: '/',
            name: 'login',
            component: () => import("@/views/login/login.vue"),
        },
        {
            path: '/',
            name: 'Main',
            redirect: "/home",
            component: () => import("@/components/nav/Menu.vue"),
            children: [
                {
                    path: '/home',
                    name: 'home',
                    component: () => import("@/views/home/home.vue"),
                    label: "首页",
                    icon: 'house',
                },
                {
                    path: '/content',
                    name: 'content',
                    component: () => import("@/components/nav/MenuChild.vue"),
                    label: "内容管理",
                    icon: 'document',
                    children: [{
                        path: '/publish',
                        name: 'publish',
                        component: () => import("@/views/contentManage/contentPublish.vue"),
                        label: "内容发布",
                    },{
                        path: '/manage',
                        name: 'manage',
                        component: () => import("@/views/contentManage/contentManage.vue"),
                        label: "内容管理",
                    },{
                        path: '/swiper',
                        name: 'swiper',
                        component: () => import("@/views/contentManage/swiperManage.vue"),
                        label: "轮播图管理",
                    }]
                },
                {
                    path: '/branch',
                    name: 'branch',
                    component: () => import("@/components/nav/MenuChild.vue"),
                    icon: 'collection',
                    label: "支部管理",
                    children: [{
                        path: '/branchManage',
                        name: 'branchManage',
                        component: () => import("@/views/branchManage/branch.vue"),
                        label: "支部管理",
                    },{
                        path: '/manageManage',
                        name: 'manageManage',
                        component: () => import("@/views/branchManage/member.vue"),
                        label: "党员管理",
                    },{
                        path: '/swiperManage',
                        name: 'swiperManage',
                        component: () => import("@/views/branchManage/activist.vue"),
                        label: "积极分子管理",
                    }]
                },
                {
                    path: '/learn',
                    name: 'learn',
                    component: () => import("@/components/nav/MenuChild.vue"),
                    icon: 'reading',
                    label: "学习管理",
                    children: [{
                        path: '/class',
                        name: 'class',
                        component: () => import("@/views/learnManage/classPublish.vue"),
                        label: "课程管理",
                    },{
                        path: '/chapter',
                        name: 'chapter',
                        component: () => import("@/views/learnManage/chapterPublish.vue"),
                        label: "章节发布",
                    },{
                        path: '/learnManager',
                        name: 'learnManager',
                        component: () => import("@/views/learnManage/learnManage.vue"),
                        label: "学习管理",
                    }]
                },
            ]
        }
    ]
});


export default router;