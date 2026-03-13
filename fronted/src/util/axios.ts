import axios from "axios";
import type {BaseResponse} from "@/api/dto.ts";
import {ElMessage} from "element-plus";
import router from "@/route/router.ts";

const instance = axios.create()

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token != null) {
        config.headers.Authorization = token
    }
    return config
})

instance.interceptors.response.use(async (response) => {
    const data = response.data as BaseResponse<any>
    if (data.code === -2) {
        await router.replace('/login')
        return Promise.reject('')
    }
    if (data.code !== 0) {
        ElMessage.error(data.message)
        return Promise.reject(data.message)
    }
    return Promise.resolve(data.data)
})

export default instance
