import request from '@/utils/request'

export function txt2Image(data) {
  return request({
    url: 'api/sd/txt2Image',
    method: 'get',
    params: {
      prompt: data.prompt,
      negativePrompt: data.negativePrompt,
      steps: data.steps
    }
  })
}

export default {
  txt2Image
}

