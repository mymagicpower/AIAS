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
    path: '/iocr',
    component: Layout,
    name: 'iocr',
    meta: { title: '自定义模版识别', icon: 'el-icon-document-copy' },
    children: [
      {
        path: '/list',
        name: 'list',
        component: () => import('@/views/iocr/list'),
        meta: { title: '模版管理', icon: 'el-icon-document-copy' }
      },
      {
        path: 'iocrinfer',
        component: () => import('@/views/iocrinfer/index'),
        name: 'inference',
        meta: { title: '基于模版识别', icon: 'el-icon-document-copy' }
      }
    ]
  },
  {
    path: '/template',
    component: Layout,
    children: [
      {
        path: 'create',
        component: () => import('@/views/iocr/index'),
        name: 'CreateTemplate',
        meta: { title: '创建模版', icon: 'el-icon-picture' },
        hidden: true
      },
      {
        path: 'edit',
        component: () => import('@/views/iocr/edit'),
        name: 'EditTemplate',
        meta: { title: '编辑模版', noCache: true },
        hidden: true
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
