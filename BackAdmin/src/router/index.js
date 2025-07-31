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
 * 动态路由
 */
export const asyncRoutes=[]

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

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
      meta: { title: '首页', icon: 'dashboard',affix:true }
    }]
  },

  {
    path: '/activity',
    component: Layout,
    redirect: '/activity/table',
    name: 'Activity',
    meta: { title: '活动管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'publish',
        name: 'Publish',
        component: () => import('@/views/ActivityPublish/index'),
        meta: { title: '发布活动', icon: 'table' }
      },
      {
        path: 'manage',
        name: 'Manage',
        component: () => import('@/views/ActivityManage/index'),
        meta: { title: '管理活动', icon: 'tree' }
      }
    ]
  },

  {
    path: '/comment',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Comment',
        component: () => import('@/views/comment/index'),
        meta: { title: '评论管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'User',
        component: () => import('@/views/user/index'),
        meta: { title: '用户管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/article',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Article',
        component: () => import('@/views/article/index'),
        meta: { title: '文章管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/category',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Category',
        component: () => import('@/views/category/index'),
        meta: { title: '文章类别管理', icon: 'form' }
      }
    ]
  },
  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://soulsend.github.io/',
        meta: { title: '点这里有惊喜~', icon: 'link' }
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
