import {defineStore} from "pinia";
import {useLocalStorage} from "@vueuse/core";
import type {User} from "@/api/user.ts";
import {profile} from "@/api/user.ts";
import {login, register} from "@/api/auth.ts";
import type {Ref} from "vue";

export const useUserStore = defineStore('user', () => {
    const user: Ref<User> = useLocalStorage<User>('user', {} as User)
    const token: Ref<string> = useLocalStorage('token', '')

    const loginInternal = async (username: string, password: string) => {
        token.value = await login({username, password})
        user.value = await profile()
    }
    const registerInternal = async (username: string, password: string, name: string, contactInfo: string, address: string) => {
        await register({username, password, name, contactInfo, address})
    }

    const logout = () => {
        user.value = {} as User
        token.value = ''
    }

    return {
        user,
        token,
        login: loginInternal,
        register: registerInternal,
        logout
    }
})
