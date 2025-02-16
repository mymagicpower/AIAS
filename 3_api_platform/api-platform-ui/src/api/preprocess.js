import request from '@/utils/request'

// 1. Canny 边缘检测 - URL
export function imageCannyForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageCannyForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 2. MLSD 线条检测 - URL
export function imageMlsdForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageMlsdForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 3.1 Scribble 涂鸦 hed模型 - URL
export function imageScribbleHedForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageScribbleHedForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 3.2 Scribble 涂鸦 Pidinet模型 - URL
export function imageScribblePidiForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageScribblePidiForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 4.1 SoftEdge 边缘检测hed模型 - URL
export function imageSoftEdgeHedForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageSoftEdgeHedForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 4.2 SoftEdge 边缘检测 Pidinet模型 - URL
export function imageSoftEdgePidiForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageSoftEdgePidiForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 5. OpenPose 姿态检测 - URL
export function imagePoseForImageUrl(data) {
  return request({
    url: 'api/preprocess/imagePoseForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 6. Segmentation 语义分割 - URL
export function imageSegForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageSegForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 7.1 Depth 深度检测 DPT模型 - URL
export function imageDepthDptForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageDepthDptForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 7.2 Depth 深度检测 Midas模型 - URL
export function imageDepthMidasForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageDepthMidasForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 8. Normal Map 法线贴图 - URL
export function imageNormalForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageNormalForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 9. Lineart 生成线稿 - URL
export function imageLineartForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageLineartForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 10. Lineart Anime 生成线稿 - URL
export function imageLineartAnimeForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageLineartAnimeForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

// 11. Content Shuffle - URL
export function imageShuffleForImageUrl(data) {
  return request({
    url: 'api/preprocess/imageShuffleForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export default {
  imageCannyForImageUrl,
  imageMlsdForImageUrl,
  imageScribbleHedForImageUrl,
  imageScribblePidiForImageUrl,
  imageSoftEdgeHedForImageUrl,
  imageSoftEdgePidiForImageUrl,
  imagePoseForImageUrl,
  imageSegForImageUrl,
  imageDepthDptForImageUrl,
  imageDepthMidasForImageUrl,
  imageNormalForImageUrl,
  imageLineartForImageUrl,
  imageLineartAnimeForImageUrl,
  imageShuffleForImageUrl
}

