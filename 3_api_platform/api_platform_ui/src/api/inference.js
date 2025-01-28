import request from '@/utils/request'

export function generalInfoForImageUrl(data) {
  return request({
    url: 'api/ocr/generalInfoForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function mlsdForImageUrl(data) {
  return request({
    url: 'api/ocr/mlsdForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function translate(data) {
  return request({
    url: 'api/text/translate',
    method: 'get',
    params: {
      text: data.text,
      srcLangId: data.srcLangId,
      targetLangId: data.targetLangId
    }
  })
}

export function enAsrForAudioUrl(data) {
  return request({
    url: 'api/asr/enAsrForAudioUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function zhAsrForAudioUrl(data) {
  return request({
    url: 'api/asr/zhAsrForAudioUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}
export default { generalInfoForImageUrl, mlsdForImageUrl, translate, enAsrForAudioUrl, zhAsrForAudioUrl }

