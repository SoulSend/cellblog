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
      component: () => import('../components/layout/index.vue'),
      children: [
        {
          path: 'index',
          component: () => import('../views/index/index.vue'),
          children: [
            {
              path: '',
              name: 'index',
              redirect: '/home/index/前端'
            },
            {
              path: ':category',
              name: 'category',
              component: () => import('../views/index/frontend/index.vue'),
              props: true
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
      component: () => import('@/views/writearticle/index.vue'),
    },
    {
      path: '/Profile',
      name: 'Profile',
      component: () => import('@/views/Profile/index.vue'),
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
