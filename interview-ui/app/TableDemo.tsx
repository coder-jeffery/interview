'use client'
import { useState } from 'react'

type User = {
    id: number
    name: string
    email: string
    role: string
}

export default function TableDemo() {
    const [data, setData] = useState<User[]>([])
    const [loaded, setLoaded] = useState(false)
    const [loading, setLoading] = useState(false)

    const handleLoad = async () => {
        setLoading(true)
        // const res = await fetch("https://gateway.example.com/api/users")
        // const list = await res.json()
        const list: User[] = [
            { id: 1, name: 'Alice', email: 'alice@example.com', role: 'Admin' },
            { id: 2, name: 'Bob', email: 'bob@example.com', role: 'User' },
            { id: 3, name: 'Jeffery', email: 'jeffery@example.com', role: 'Developer' },
        ]
        setData(list)
        setLoaded(true)
        setLoading(false)
    }

    return (
        <div className="space-y-6 p-6">
            <button
                onClick={handleLoad}
                disabled={loading}
                className="rounded bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:opacity-60"
            >
                {loading ? '加载中...' : '加载表格数据'}
            </button>

            {loaded && (
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
