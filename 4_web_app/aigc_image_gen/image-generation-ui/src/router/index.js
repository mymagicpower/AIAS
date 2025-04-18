import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    meta: { title: '图像预处理', icon: 'el-icon-picture' },
    children: [
      {
        path: '/canny',
        component: () => import('@/views/preprocess/canny'),
        name: 'canny',
        meta: { title: 'Canny 边缘检测', icon: 'el-icon-full-screen' }
      },
      {
        path: '/mlsd',
        component: () => import('@/views/preprocess/mlsd'),
        name: 'mlsd',
        meta: { title: 'MLSD 线条检测', icon: 'el-icon-full-screen' }
      },
      {
        path: '/scribbleHed',
        component: () => import('@/views/preprocess/scribbleHed'),
        name: 'scribbleHed',
        meta: { title: '涂鸦-Hed模型', icon: 'el-icon-full-screen' }
      },
      {
        path: '/scribblePidiNet',
        component: () => import('@/views/preprocess/scribblePidiNet'),
        name: 'scribblePidiNet',
        meta: { title: '涂鸦-PidiNet模型', icon: 'el-icon-full-screen' }
      },
      {
        path: '/softEdgeHed',
        component: () => import('@/views/preprocess/softEdgeHed'),
        name: 'softEdgeHed',
        meta: { title: '边缘检测-Hed模型', icon: 'el-icon-full-screen' }
      },
      {
        path: '/softEdgePidiNet',
        component: () => import('@/views/preprocess/softEdgePidiNet'),
        name: 'softEdgePidiNet',
        meta: { title: '边缘检测-PidiNet模型', icon: 'el-icon-full-screen' }
      },
      {
        path: '/openPose',
        component: () => import('@/views/preprocess/openPose'),
        name: 'openPose',
        meta: { title: '姿态检测', icon: 'el-icon-full-screen' }
      },
      {
        path: '/segUperNet',
        component: () => import('@/views/preprocess/segUperNet'),
        name: 'segUperNet',
        meta: { title: '语义分割', icon: 'el-icon-full-screen' }
      },
      {
        path: '/depthDpt',
        component: () => import('@/views/preprocess/depthDpt'),
        name: 'depthDpt',
        meta: { title: '深度估计-DPT模型', icon: 'el-icon-full-screen' }
      },
      {
        path: '/depthMidas',
        component: () => import('@/views/preprocess/depthMidas'),
        name: 'depthMidas',
        meta: { title: '深度估计-Midas模型', icon: 'el-icon-full-screen' }
      },
      {
        path: '/normalBae',
        component: () => import('@/views/preprocess/normalBae'),
        name: 'normalBae',
        meta: { title: '法线贴图', icon: 'el-icon-full-screen' }
      },
      {
        path: '/lineart',
        component: () => import('@/views/preprocess/lineart'),
        name: 'lineart',
        meta: { title: '生成线稿', icon: 'el-icon-full-screen' }
      },
      {
        path: '/lineartAnime',
        component: () => import('@/views/preprocess/lineartAnime'),
        name: 'lineartAnime',
        meta: { title: '卡通图像线稿', icon: 'el-icon-full-screen' }
      },
      {
        path: '/shuffle',
        component: () => import('@/views/preprocess/shuffle'),
        name: 'shuffle',
        meta: { title: '内容重洗', icon: 'el-icon-full-screen' }
      }
    ]
  },
  {
    path: '/sd',
    component: Layout,
    name: 'sd',
    meta: { title: '图像生成', icon: 'el-icon-picture' },
    children: [
      {
        path: '/text2img',
        component: () => import('@/views/sd/text2img'),
        name: 'text2img',
        meta: { title: '文生图', icon: 'el-icon-picture' }
      },
      {
        path: '/img2img',
        component: () => import('@/views/sd/img2img'),
        name: 'img2img',
        meta: { title: '图生图', icon: 'el-icon-picture' }
      },
      {
        path: '/sdCanny',
        component: () => import('@/views/sd/sdCanny'),
        name: 'sdCanny',
        meta: { title: 'Canny 辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdMlsd',
        component: () => import('@/views/sd/sdMlsd.vue'),
        name: 'sdMlsd',
        meta: { title: 'Mlsd 辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdScribble',
        component: () => import('@/views/sd/sdScribble.vue'),
        name: 'sdScribble',
        meta: { title: 'Scribble 辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdSoftEdge',
        component: () => import('@/views/sd/sdSoftEdge.vue'),
        name: 'sdSoftEdge',
        meta: { title: 'SoftEdge 辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdOpenPose',
        component: () => import('@/views/sd/sdOpenPose.vue'),
        name: 'sdOpenPose',
        meta: { title: 'OpenPose 辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdSeg',
        component: () => import('@/views/sd/sdSeg.vue'),
        name: 'sdSeg',
        meta: { title: '语义分割辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdDepth',
        component: () => import('@/views/sd/sdDepth.vue'),
        name: 'sdDepth',
        meta: { title: '深度估计辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdNormalBae',
        component: () => import('@/views/sd/sdNormalBae.vue'),
        name: 'sdNormalBae',
        meta: { title: '法线贴图辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdLineart',
        component: () => import('@/views/sd/sdLineart.vue'),
        name: 'sdLineart',
        meta: { title: '线稿提取辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdLineartAnime',
        component: () => import('@/views/sd/sdLineartAnime.vue'),
        name: 'sdLineartAnime',
        meta: { title: '卡通线稿辅助生成', icon: 'el-icon-picture' }
      },
      {
        path: '/sdShuffle',
        component: () => import('@/views/sd/sdShuffle.vue'),
        name: 'sdShuffle',
        meta: { title: '内容重洗辅助生成', icon: 'el-icon-picture' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
