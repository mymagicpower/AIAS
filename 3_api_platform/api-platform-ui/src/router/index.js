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
    children: [
      {
        path: '/',
        component: () => import('@/views/general/index'),
        name: 'inference',
        meta: { title: '通用文字识别', icon: 'el-icon-full-screen' }
      }
    ]
  },
  {
    path: '/mlsd',
    component: Layout,
    children: [
      {
        path: '/mlsd',
        component: () => import('@/views/mlsd/index'),
        name: 'inference',
        meta: { title: '文本转正', icon: 'el-icon-c-scale-to-original' }
      }
    ]
  },
  {
    path: '/trans',
    component: Layout,
    children: [
      {
        path: '/trans',
        component: () => import('@/views/translate/index'),
        name: 'inference',
        meta: { title: '文本翻译', icon: 'el-icon-c-scale-to-original' }
      }
    ]
  },
  {
    path: '/enasr',
    component: Layout,
    meta: { title: '语音识别', icon: 'el-icon-picture' },
    children: [
      {
        path: '/enasr',
        component: () => import('@/views/english/index'),
        name: 'enasr',
        meta: { title: '英文语音识别', icon: 'el-icon-mic' }
      },
      {
        path: '/zhasr',
        component: () => import('@/views/chinese/index'),
        name: 'zhasr',
        meta: { title: '中文语音识别', icon: 'el-icon-mic' }
      }
    ]
  },
  {
    path: '/imageSr',
    component: Layout,
    meta: { title: '一键高清', icon: 'el-icon-picture' },
    children: [
      {
        path: '/imageSr',
        component: () => import('@/views/imageSr/index'),
        name: 'imageSr',
        meta: { title: '图片一键高清', icon: 'el-icon-picture' }
      },
      {
        path: '/faceGan',
        component: () => import('@/views/faceGan/index'),
        name: 'faceGan',
        meta: { title: '头像一键高清', icon: 'el-icon-picture' }
      },
      {
        path: '/faceRes',
        component: () => import('@/views/faceRes/index'),
        name: 'faceRes',
        meta: { title: '人脸一键修复', icon: 'el-icon-picture' }
      }
    ]
  },
  {
    path: '/seg',
    component: Layout,
    name: 'seg',
    meta: { title: '一键抠图', icon: 'el-icon-picture' },
    children: [
      {
        path: '/generalBig',
        component: () => import('@/views/generalBig/index'),
        name: 'generalBig',
        meta: { title: '通用一键抠图【大】', icon: 'el-icon-picture' }
      },
      {
        path: '/generalMid',
        component: () => import('@/views/generalMid/index'),
        name: 'generalMid',
        meta: { title: '通用一键抠图【中】', icon: 'el-icon-picture' }
      },
      {
        path: '/generalSmall',
        component: () => import('@/views/generalSmall/index'),
        name: 'generalSmall',
        meta: { title: '通用一键抠图【小】', icon: 'el-icon-picture' }
      },
      {
        path: '/humanSeg',
        component: () => import('@/views/humanSeg/index'),
        name: 'humanSeg',
        meta: { title: '人体一键抠图', icon: 'el-icon-picture' }
      },
      {
        path: '/clothSeg',
        component: () => import('@/views/clothSeg/index'),
        name: 'clothSeg',
        meta: { title: '衣服一键抠图', icon: 'el-icon-picture' }
      },
      {
        path: '/animeSeg',
        component: () => import('@/views/animeSeg/index'),
        name: 'animeSeg',
        meta: { title: '动漫一键抠图', icon: 'el-icon-picture' }
      },
      {
        path: '/manualSeg',
        component: () => import('@/views/manualSeg/index'),
        name: 'manualSeg',
        meta: { title: '框选一键抠图', icon: 'el-icon-picture' }
      }
    ]
  },
  {
    path: '/ddcolor',
    component: Layout,
    children: [
      {
        path: '/ddcolor',
        component: () => import('@/views/ddcolor/index'),
        name: 'ddcolor',
        meta: { title: '黑白照片上色', icon: 'el-icon-picture' }
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
