import { createRouter, createWebHistory } from "vue-router";
import route from "@/route/index.ts";

const router = createRouter({
    routes: route,
    history: createWebHistory(),
})

router.beforeEach((to, _, next) => {
    if (to.path.startsWith('/admin') && localStorage.getItem('token') == null) {
        next('/login')
        return
    }
    next()
})

export default router
