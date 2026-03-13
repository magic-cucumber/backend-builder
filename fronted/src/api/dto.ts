export type BaseResponse<T> = {
    code: number
    message: string
    data: T
}

export type PageResponse<T> = {
    records: T[]
    total: number
    size: number
    current: number
    pages: number
}
