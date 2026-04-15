import axios from 'axios'

const API_BASE_URL = 'http://localhost:8098/api'

export interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
  timestamp: number
}

export function generateMemoryId(): string {
  return `chat_${Date.now()}_${Math.random().toString(36).substring(2, 15)}`
}

export async function chatStream(
  memoryId: string,
  message: string,
  onChunk: (chunk: string) => void
): Promise<void> {
  const url = `${API_BASE_URL}/ai/chat/stream?memoryId=${encodeURIComponent(memoryId)}&msg=${encodeURIComponent(message)}`

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'text/event-stream',
        'Cache-Control': 'no-cache'
      }
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const reader = response.body?.getReader()
    if (!reader) {
      throw new Error('Response body is not readable')
    }

    const decoder = new TextDecoder()
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()

      if (done) {
        break
      }

      buffer += decoder.decode(value, { stream: true })

      // SSE 格式：每行以 \n 分隔，数据以 "data:" 开头
      const lines = buffer.split('\n')
      buffer = lines.pop() || '' // 保留最后一个不完整的行

      for (const line of lines) {
        const trimmedLine = line.trim()
        if (trimmedLine.startsWith('data:')) {
          const data = trimmedLine.substring(5).trim()
          if (data) {
            onChunk(data)
          }
        } else if (trimmedLine && !trimmedLine.startsWith(':')) {
          // 如果不是注释行且有内容，直接处理
          onChunk(trimmedLine)
        }
      }
    }

    // 处理剩余的 buffer
    if (buffer.trim()) {
      const trimmedLine = buffer.trim()
      if (trimmedLine.startsWith('data:')) {
        const data = trimmedLine.substring(5).trim()
        if (data) {
          onChunk(data)
        }
      } else if (!trimmedLine.startsWith(':')) {
        onChunk(trimmedLine)
      }
    }
  } catch (error) {
    console.error('Chat stream error:', error)
    throw error
  }
}
