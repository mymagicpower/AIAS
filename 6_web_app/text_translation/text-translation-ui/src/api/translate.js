import request from '@/utils/request'

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
