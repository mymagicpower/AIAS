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

export function enAsrForLongAudioUrl(data) {
  return request({
    url: 'api/asr/enAsrForLongAudioUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function zhAsrForLongAudioUrl(data) {
  return request({
    url: 'api/asr/zhAsrForLongAudioUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function imageSrForImageUrl(data) {
  return request({
    url: 'api/img/imageSrForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function imageHdForImageUrl(data) {
  return request({
    url: 'api/img/imageHdForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function faceResForImageUrl(data) {
  return request({
    url: 'api/img/faceResForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function faceGanForImageUrl(data) {
  return request({
    url: 'api/img/faceGanForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function generalSegBigForImageUrl(data) {
  return request({
    url: 'api/seg/generalSegBigForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function generalSegMidForImageUrl(data) {
  return request({
    url: 'api/seg/generalSegMidForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function generalSegSmallForImageUrl(data) {
  return request({
    url: 'api/seg/generalSegSmallForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function humanSegForImageUrl(data) {
  return request({
    url: 'api/seg/humanSegForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function animeSegForImageUrl(data) {
  return request({
    url: 'api/seg/animeSegForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function clothSegForImageUrl(data) {
  return request({
    url: 'api/seg/clothSegForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function getLabelData(data) {
  return request({
    url: 'api/seg/getLabelData',
    method: 'post',
    data
  })
}

export function uploadImage(name, imageFile) {
  return request({
    url: 'api/seg/uploadImage',
    method: 'post',
    params: {
      name: name,
      imageFile: imageFile
    }
  })
}

export function imageColorForImageUrl(data) {
  return request({
    url: 'api/color/imageColorForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export default {
  generalInfoForImageUrl,
  mlsdForImageUrl,
  translate,
  enAsrForAudioUrl,
  zhAsrForAudioUrl,
  enAsrForLongAudioUrl,
  zhAsrForLongAudioUrl,
  imageSrForImageUrl,
  imageHdForImageUrl,
  faceResForImageUrl,
  faceGanForImageUrl,
  generalSegBigForImageUrl,
  generalSegMidForImageUrl,
  generalSegSmallForImageUrl,
  humanSegForImageUrl,
  animeSegForImageUrl,
  clothSegForImageUrl,
  uploadImage,
  getLabelData,
  imageColorForImageUrl
}

