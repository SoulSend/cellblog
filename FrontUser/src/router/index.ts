import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home/index'
    },
    {
      path: '/login',//登录页
      name: 'login',
      component: () => import('../views/login/index.vue'),
    },
    {
      path: '/home',//布局页
      name: 'home',
      redirect:'/home/index/前端',
      component: () => import('../components/layout/index.vue'),
      children: [
        {
          path: 'index',
          name: 'index',
          redirect:'/home/index/前端',
          component: () => import('../views/index/index.vue'),
          children: [
            {
              path: '前端',
              name: 'frontend',
              component: () => import('../views/index/frontend/index.vue')
            }, 
            {
              path: '后端',
              name: 'rearend',
              component: () => import('../views/index/rearend/index.vue')
            },
            {
              path: '嵌入式开发',
              name: 'embedded',
              component: () => import('../views/index/embedded/index.vue')
            }, 
            {
              path: '开发工具',
              name: 'devtool',
              component: () => import('../views/index/devtool/index.vue')
            }, 
            {
              path: '数据库',
              name: 'database',
              component: () => import('../views/index/database/index.vue')
            }, 
            {
              path: '生活',
              name: 'life',
              component: () => import('../views/index/life/index.vue')
            }, 
            {
              path: '编程语言',
              name: 'proLanguage',
              component: () => import('../views/index/proLanguage/index.vue')
            }
          ]
        },
        {
          path: 'ArticleDetail/:id', 
          name: 'ArticleDetail',
          component: () => import('../views/ArticleDetail/index.vue'),
          props: true
        },
        {
          path: 'cellai',
          name: 'cellai',
          component: () => import('../views/cellAI/index.vue'),
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('../views/SearchPage/index.vue'),
        },
        {
          path: 'activity',
          name: 'activity',
          component: () => import('../views/activity/index.vue'),
        },
        {
          path: 'ActivityDetail/:id',
          name: 'ActivityDetail',
          component: () => import('../views/ActivityDetail/index.vue'),
          props: true
        },
      ]
    },
    {
      path: '/WriteArticle',
      name: 'WriteArticle',
      component: () => import('../views/writearticle/index.vue'),
    },
    {
      path: '/Profile',
      name: 'Profile',
      component: () => import('../views/Profile/index.vue'),
    },


  ]

})
/**
 * 路由守卫
 */
router.beforeEach((to, from, next) => {
  if (to.matched.length === 0) {
    next({ name: 'index' }) // 使用路由的 name 属性进行重定向
  } else {
    next()
  }
})

export default router
