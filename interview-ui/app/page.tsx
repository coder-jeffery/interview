'use client'
import { useState } from 'react'
import TableDemo from './TableDemo'

export default function Home() {
    const [count, setCount] = useState(0)
    return (
        <main className="p-10">
            <h1 className="mb-4 text-2xl font-semibold">用户列表</h1>
            <button onClick={() => setCount(count + 1)}>{count}</button>

            <TableDemo />
        </main>
    )
}
