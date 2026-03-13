import {type RouteRecordRaw} from "vue-router";

export default [
    {
        path: '/',
        component: () => import('@/pages/fronted/index.vue'),
        children: [
            {
                path: '/',
                name: 'fronted-home',
                component: () => import('@/pages/fronted/home/index.vue')
            },
        ]
    },
    {
        path: '/admin',
        component: () => import('@/pages/backend/index.vue'),
        children: [
            {
                path: '/',
                name: 'backend-home',
                component: () => import('@/pages/backend/home/index.vue')
            },
            {
                path: 'profile',
                name: 'backend-profile',
                component: () => import('@/pages/backend/profile/index.vue'),
                children: [
                    {
                        path: '',
                        name: 'backend-profile-index',
                        component: () => import('@/pages/backend/profile/profile/index.vue')
                    },
                ]
            },
            {
                path: 'user',
                name: 'backend-user',
                component: () => import('@/pages/backend/user/index.vue'),
            }
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/pages/login.vue')
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('@/pages/register.vue')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('@/pages/404.vue')
    },
    // 所有未定义路由，全部重定向到404页
    {
        path: '/:pathMatch(.*)',
        redirect: '/404'
    }
] as RouteRecordRaw[]
