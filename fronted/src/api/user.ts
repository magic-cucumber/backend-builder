import axios from "@/util/axios.ts";
import type { PageResponse } from "./dto";

export type User = {
    userId: number
    username: string
    password: string
    role: 'ADMIN' | 'USER'


    email: string
    phone: string
}

export function profile(): Promise<User> {
    return axios.get('/api/users/profile')
}

export function updateProfile(user: User): Promise<boolean> {
    return axios.put('/api/users/profile', user)
}

export function createUser(user: User): Promise<User> {
    return axios.post('/api/users', user)
}

export function updateUser(userId: number, user: User): Promise<boolean> {
    return axios.put(`/api/users/${userId}`, user)
}

export function deleteUser(userId: number): Promise<boolean> {
    return axios.delete(`/api/users/${userId}`)
}

export function getUser(userId: number): Promise<User> {
    return axios.get(`/api/users/${userId}`)
}

export function listUsers(page: number = 1, size: number = 10): Promise<PageResponse<User>> {
    return axios.get('/api/users', {
        params: {
            page,
            size
        }
    })
}
