import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import ListingView from '../views/ListingView.vue'
import AiEntryView from '../views/AiEntryView.vue'
import AiProfileView from '../views/AiProfileView.vue'
import AiResultView from '../views/AiResultView.vue'
import ArticlesView from '../views/ArticlesView.vue'
import FavoritesView from '../views/FavoritesView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView, meta: { title: '首页' } },
    { path: '/login', name: 'login', component: LoginView, meta: { title: '登录注册' } },
    {
      path: '/recipes',
      name: 'recipes',
      component: ListingView,
      meta: { title: '药膳精选', type: 'medicated_food', section: '药膳精选 · 为你推荐' },
    },
    {
      path: '/soups',
      name: 'soups',
      component: ListingView,
      meta: { title: '汤方专区', type: 'soup', section: '汤方专区 · 汤养全家' },
    },
    {
      path: '/teas',
      name: 'teas',
      component: ListingView,
      meta: { title: '茶方秘典 / 中药奶茶', type: 'tea', section: '茶方秘典 · 草本入茶' },
    },
    { path: '/ai', name: 'ai', component: AiEntryView, meta: { title: 'AI 药膳' } },
    {
      path: '/ai/profile',
      name: 'ai-profile',
      component: AiProfileView,
      meta: { title: 'AI 药膳健康档案' },
    },
    {
      path: '/ai/result',
      name: 'ai-result',
      component: AiResultView,
      meta: { title: 'AI 药膳推荐结果' },
    },
    { path: '/articles', name: 'articles', component: ArticlesView, meta: { title: '养生资讯' } },
    { path: '/favorites', name: 'favorites', component: FavoritesView, meta: { title: '收藏' } },
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

export default router
