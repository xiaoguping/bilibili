import Vue from 'vue';
import VueRouter from 'vue-router'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/dist/home-page'
  },
  
  {
    path: '/dist/home-page',
    name: 'home-page',
    component: () => import(/* webpackChunkName: "about" */ '../views/home_page/home_page.vue')
  },
  {
    path: '/dist/register',
    name: 'register',
    component: () => import(/* webpackChunkName: "about" */ '../views/register/index.vue')
  },
  {
    path: '/dist/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "about" */ '../views/login/index.vue')
  },
  {
    path: '/dist/chapter',
    name: 'chapter',
    component: () => import(/* webpackChunkName: "about" */ '../views/chapter/index.vue')
  },
  {
    path: '/dist/personal',
    name: 'personal',
    component: () => import(/* webpackChunkName: "about" */ '../views/personal/index.vue')
  },
  {
    path: '/dist/search-result',
    name: 'search-result',
    component: () => import(/* webpackChunkName: "about" */ '../views/search/index.vue')
  },
  {
    path: '/dist/follows',
    name: 'follows',
    component: () => import(/* webpackChunkName: "about" */ '../views/follow/index.vue')
  },
  {
    path: '/dist/moments',
    name: 'moments',
    component: () => import(/* webpackChunkName: "about" */ '../views/moments/index.vue')
  },
  {
    path: '/dist/post-video',
    name: 'post-video',
    component: () => import(/* webpackChunkName: "about" */ '../views/moments/video.vue')
  },
  {
    path: '/dist/video-detail',
    name: 'video-detail',
    component: () => import(/* webpackChunkName: "about" */ '../views/video/video_detail.vue')
  },
  {
    path: '/dist/mask-test',
    name: 'mask-test',
    component: () => import(/* webpackChunkName: "about" */ '../views/maskTest.vue')
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.VUE_APP_BASE_ROUTE,
  routes
})

export default router
