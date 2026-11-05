'use client'

import { useEffect } from 'react'

type ErrorProps = {
    error: Error & { digest?: string }
    reset: () => void
}

export default function Error({ error, reset }: ErrorProps) {
    useEffect(() => {
        console.error(error)
    }, [error])

    return (
        <div className="p-10">
            <h2>出错啦！</h2>
            <p>{error.message}</p>
            <button onClick={() => reset()}>重试</button>
        </div>
    )
}

