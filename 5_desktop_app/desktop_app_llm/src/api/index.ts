import type { AxiosProgressEvent, GenericAbortSignal } from 'axios'
import { post } from '../utils/request'

export function fetchChatConfig<T = any>() {
  return post<T>({
    url: '/config',
  })
}

export function fetchChatAPIProcess<T = any>(
  params: {
    prompt: string
    memory: number
    top_p: number
    options?: { conversationId?: string; parentMessageId?: string }
    signal?: GenericAbortSignal
    onDownloadProgress?: (progressEvent: AxiosProgressEvent) => void
  },
) {
  return post<T>({
    url: '/chat-process',
    data: {
      prompt: params.prompt,
      options: params.options,
      memory: params.memory,
      top_p: params.top_p,
    },
    signal: params.signal,
    onDownloadProgress: params.onDownloadProgress,
  })
}

export function fetchAudioChatAPIProcess<T = any>(
  params: {
    formData: FormData
    options?: { conversationId?: string; parentMessageId?: string }
    onDownloadProgress?: (progressEvent: AxiosProgressEvent) => void
  },
) {
  return post<T>({
    url: '/audio-chat-process',
    data: params.formData,
    onDownloadProgress: params.onDownloadProgress,
  })
}
