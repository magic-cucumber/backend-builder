import axios from "@/util/axios.ts";

export type LoginRequest = {
    username: string
    password: string
}


export type RegisterRequest = {
    username: string
    password: string
    name: string
    contactInfo: string
    address: string
}

export function login(request: LoginRequest): Promise<string> {
    return axios.post('/api/auth/login', request)
}

export function register(request: RegisterRequest): Promise<boolean> {
    return axios.post('/api/auth/register', request)
}
