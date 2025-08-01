import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home/index'
    },

    {
      path: '/home',
      name: 'home',
      component: () => import('../components/layout/index.vue'),
      meta: {
        title: 'CELL Blog - 首页',
        requiresAuth: false
      },
      children: [
        {
          path: 'index',
          name: 'index',
          component: () => import('../views/index/index.vue'),
          meta: {
            title: '首页',
            keepAlive: true
          },
          children: [
            {
              path: '',
              redirect: '/home/index/前端'
            },
            {
              path: ':category',
              name: 'category',
              component: () => import('../views/index/frontend/index.vue'),
              props: true,
              meta: {
                title: '分类文章',
                keepAlive: true
              }
            }
          ]
        },
        {
          path: 'ArticleDetail/:id', 
          name: 'ArticleDetail',
          component: () => import('../views/ArticleDetail/index.vue'),
          props: true,
          meta: {
            title: '文章详情',
            keepAlive: false
          }
        },
        {
          path: 'cellai',
          name: 'cellai',
          component: () => import('../views/cellAI/index.vue'),
          meta: {
            title: 'CELL AI',
            keepAlive: false
          }
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('../views/SearchPage/index.vue'),
          meta: {
            title: '搜索',
            keepAlive: false
          }
        },
        {
          path: 'activity',
          name: 'activity',
          component: () => import('../views/activity/index.vue'),
          meta: {
            title: '活动',
            keepAlive: false
          }
        },
        {
          path: 'ActivityDetail/:id',
          name: 'ActivityDetail',
          component: () => import('../views/ActivityDetail/index.vue'),
          props: true,
          meta: {
            title: '活动详情',
            keepAlive: false
          }
        },
      ]
    },
    {
      path: '/WriteArticle',
      name: 'WriteArticle',
      component: () => import('@/views/writearticle/index.vue'),
      meta: {
        title: '写文章',
        requiresAuth: true,
        keepAlive: false
      }
    },
    {
      path: '/Profile',
      name: 'Profile',
      component: () => import('@/views/Profile/index.vue'),
      meta: {
        title: '个人中心',
        requiresAuth: true,
        keepAlive: false
      }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('../views/404.vue'),
      meta: {
        title: '页面未找到',
        keepAlive: false
      }
    }
  ]
})

/**
 * 路由守卫
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta?.title) {
    document.title = to.meta.title as string
  }
  
  // 检查是否需要登录
  if (to.meta?.requiresAuth) {
    // 这里可以添加登录状态检查逻辑
    // const isAuthenticated = checkAuthStatus()
    // if (!isAuthenticated) {
    //   next('/login')
    //   return
    // }
  }
  
  if (to.matched.length === 0) {
    next({ name: 'index' })
  } else {
    next()
  }
})

export default router
