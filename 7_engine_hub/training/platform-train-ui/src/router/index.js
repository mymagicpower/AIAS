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
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '训练可视化', icon: 'el-icon-data-line' }
    }]
  },
  {
    path: '/inference',
    component: Layout,
    name: 'inference',
    meta: { title: '在线推理', icon: 'el-icon-picture' },
    children: [
      {
        path: 'classification',
        component: () => import('@/views/inference/classification'),
        name: 'classification',
        meta: { title: '图像分类', icon: 'el-icon-s-grid' }
      },
      {
        path: 'feature',
        component: () => import('@/views/inference/feature'),
        name: 'feature',
        meta: { title: '特征提取', icon: 'el-icon-view' }
      },
      {
        path: 'comparison',
        component: () => import('@/views/inference/comparison'),
        name: 'comparison',
        meta: { title: '图像比对', icon: 'el-icon-copy-document' }
      }
    ]
  },
  {
    path: '/storage',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/storage/index'),
        name: 'storage',
        meta: { title: '数据管理', icon: 'el-icon-upload' }
      }
    ]
  },
  {
    path: '/argument',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/argument/index'),
        name: 'argument',
        meta: { title: '超参数设置', icon: 'el-icon-setting' }
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
