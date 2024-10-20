import request from '@/utils/request'

export function enAsrForAudioUrl(data) {
  return request({
    url: '/inference/enAsrForAudioUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function zhAsrForAudioUrl(data) {
  return request({
    url: '/inference/zhAsrForAudioUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// export function asrForAudioFile(lang, imageFile) {
//   return request({
//     url: '/inference/asrForAudioFile',
//     method: 'post',
//     params: {
//       name: lang,
//       imageFile: imageFile
//     }
//   })
// }

export default { enAsrForAudioUrl, zhAsrForAudioUrl }

