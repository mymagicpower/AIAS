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
        component: () => import('@/views/generalBig/index'),
        name: 'generalBig',
        meta: { title: '通用一键抠图【大模型】', icon: 'el-icon-picture-outline-round' }
      }
    ]
  },
  {
    path: '/generalMid',
    component: Layout,
    children: [
      {
        path: '/generalMid',
        component: () => import('@/views/generalMid/index'),
        name: 'generalMid',
        meta: { title: '通用一键抠图【中模型】', icon: 'el-icon-picture-outline-round' }
      }
    ]
  },
  {
    path: '/generalSmall',
    component: Layout,
    children: [
      {
        path: '/generalSmall',
        component: () => import('@/views/generalSmall/index'),
        name: 'generalSmall',
        meta: { title: '通用一键抠图【小模型】', icon: 'el-icon-picture-outline-round' }
      }
    ]
  },
  {
    path: '/humanSeg',
    component: Layout,
    children: [
      {
        path: '/humanSeg',
        component: () => import('@/views/humanSeg/index'),
        name: 'humanSeg',
        meta: { title: '人体一键抠图', icon: 'el-icon-picture-outline-round' }
      }
    ]
  },
  {
    path: '/clothSeg',
    component: Layout,
    children: [
      {
        path: '/clothSeg',
        component: () => import('@/views/clothSeg/index'),
        name: 'clothSeg',
        meta: { title: '衣服一键抠图', icon: 'el-icon-picture-outline-round' }
      }
    ]
  },
  {
    path: '/animeSeg',
    component: Layout,
    children: [
      {
        path: '/animeSeg',
        component: () => import('@/views/animeSeg/index'),
        name: 'animeSeg',
        meta: { title: '动漫一键抠图', icon: 'el-icon-picture-outline-round' }
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
