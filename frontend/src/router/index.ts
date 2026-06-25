import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import PlansView from '../views/PlansView.vue'
import AiEntryView from '../views/AiEntryView.vue'
import AiProfileView from '../views/AiProfileView.vue'
import AiResultView from '../views/AiResultView.vue'
import IdentifyView from '../views/IdentifyView.vue'
import ArticlesView from '../views/ArticlesView.vue'
import FavoritesView from '../views/FavoritesView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView, meta: { title: '首页' } },
    { path: '/login', name: 'login', component: LoginView, meta: { title: '登录注册' } },
    { path: '/plans', name: 'plans', component: PlansView, meta: { title: '膳养方案' } },
    {
      path: '/recipes',
      redirect: { path: '/plans', query: { type: 'medicated_food' } },
    },
    {
      path: '/soups',
      redirect: { path: '/plans', query: { type: 'soup' } },
    },
    {
      path: '/teas',
      redirect: { path: '/plans', query: { type: 'tea' } },
    },
    { path: '/ai', name: 'ai', component: AiEntryView, meta: { title: 'AI 药膳' } },
    { path: '/identify', name: 'identify', component: IdentifyView, meta: { title: '药材识别' } },
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
