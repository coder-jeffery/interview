'use client'
import { useState } from 'react'
import TableDemo from './TableDemo'

export default function Home() {
    const [count, setCount] = useState(0)
    return (
        <main className="p-10">
            <button onClick={() => setCount(count + 1)}>{count}</button>

            <TableDemo />
        </main>
    )
}
