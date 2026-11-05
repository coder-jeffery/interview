'use client'
import { useCallback, useEffect, useState } from 'react'

type User = {
    id: number
    name: string
    email: string
    role: string
}

/** 默认同源代理（见 next.config rewrites），避免浏览器 CORS 拦截 */
const USER_LIST_URL =
    process.env.NEXT_PUBLIC_USER_LIST_URL ?? '/interview-api/user/list'

export default function TableDemo() {
    const [data, setData] = useState<User[]>([])
    const [loaded, setLoaded] = useState(false)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const loadUsers = useCallback(async () => {
        setLoading(true)
        setError(null)
        try {
            const res = await fetch(USER_LIST_URL, { cache: 'no-store' })
            if (!res.ok) {
                throw new Error(`请求失败 (${res.status})`)
            }
            const list = (await res.json()) as User[]
            setData(list)
            setLoaded(true)
        } catch (e) {
            const msg = e instanceof Error ? e.message : '加载失败'
            setError(
                msg === 'Failed to fetch'
                    ? '无法连接后端：请确认 interview-api 已在 8888 端口启动；若直连跨域地址，需后端 CORS 或改用同源 /interview-api/user/list'
                    : msg,
            )
            setLoaded(true)
        } finally {
            setLoading(false)
        }
    }, [])

    useEffect(() => {
        void loadUsers()
    }, [loadUsers])

    return (
        <div className="space-y-6 p-6">
            <div className="flex flex-wrap items-center gap-3">
                <button
                    type="button"
                    onClick={() => void loadUsers()}
                    disabled={loading}
                    className="rounded bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:opacity-60"
                >
                    {loading ? '加载中...' : '刷新表格数据'}
                </button>
                <span className="text-sm text-gray-500">数据源：{USER_LIST_URL}</span>
            </div>

            {error && (
                <p className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
                    {error}
                </p>
            )}

            {loaded && !error && (
                <table className="w-full border-collapse border border-gray-300">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="border border-gray-300 p-2 text-left">ID</th>
                        <th className="border border-gray-300 p-2 text-left">姓名</th>
                        <th className="border border-gray-300 p-2 text-left">邮箱</th>
                        <th className="border border-gray-300 p-2 text-left">角色</th>
                    </tr>
                    </thead>
                    <tbody>
                    {data.length === 0 ? (
                        <tr>
                            <td colSpan={4} className="border p-2 text-center">暂无数据</td>
                        </tr>
                    ) : (
                        data.map((row) => (
                            <tr key={row.id}>
                                <td className="border p-2">{row.id}</td>
                                <td className="border p-2">{row.name}</td>
                                <td className="border p-2">{row.email}</td>
                                <td className="border p-2">{row.role}</td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            )}
        </div>
    )
}
