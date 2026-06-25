<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Bowl, CoffeeCup, Collection, Dish, Search, Star, User } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import RecipeCard from '../components/RecipeCard.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'
import { mockHome } from '../data/mock'
import { api } from '../services/api'
import { useAuthStore } from '../stores/auth'
import type { HomeData, Recipe } from '../types'

const auth = useAuthStore()
const home = ref<HomeData>(mockHome)
const query = ref('')

const entryCards = [
  {
    title: '汤方专区',
    subtitle: '靓汤温养，四时皆宜',
    to: '/soups',
    variant: 'soup' as const,
    image: contentImages.entries.soup,
    icon: Bowl,
    action: '探索汤方',
  },
  {
    title: '药膳精选',
    subtitle: '药食搭配，日常可行',
    to: '/recipes',
    variant: 'recipe' as const,
    image: contentImages.entries.recipe,
    icon: Dish,
    action: '查看药膳',
  },
  {
    title: '茶方秘典',
    subtitle: '草本入茶，温润轻养',
    to: '/teas',
    variant: 'tea' as const,
    image: contentImages.entries.tea,
    icon: CoffeeCup,
    action: '品味茶方',
  },
]

const categoryItems = ['养生', '减脂', '祛火', '祛湿', '安神', '助眠']

function variantFor(recipe: Recipe) {
  if (recipe.type === 'soup') return 'soup'
  if (recipe.type === 'tea') return 'tea'
  return 'recipe'
}

function hideBrokenImage(event: Event) {
  const image = event.target as HTMLImageElement
  image.hidden = true
}

onMounted(async () => {
  home.value = await api.home()
})
</script>

<template>
  <AppShell>
    <div class="home-page">
      <section class="home-hero">
        <div class="container home-hero-inner">
          <div class="home-hero-copy">
            <div class="eyebrow">药食同源 · AI 个性化药膳</div>
            <h1 class="hero-title">药食同源，智养有方</h1>
            <p class="hero-subtitle">以中医体质辨识为线索，结合日常饮食偏好，为您推荐温和、可执行、适合展示的药膳养生方案。</p>
            <div class="search-row hero-search">
              <el-icon><Search /></el-icon>
              <input v-model="query" aria-label="搜索" placeholder="搜索药膳、食材、功效、症状等" />
              <button class="primary-btn" type="button">搜索</button>
            </div>
          </div>
          <div class="hero-image-card">
            <VisualBlock variant="hero" title="草本膳养" subtitle="Herbal Cuisine" />
            <img class="hero-real-image visual-img" :src="contentImages.hero" alt="中式药膳宴席与草本食材" @error="hideBrokenImage" />
          </div>
        </div>
      </section>

      <div class="container">
        <section class="entry-grid">
          <RouterLink v-for="card in entryCards" :key="card.title" class="entry-card" :to="card.to">
            <VisualBlock :variant="card.variant" :title="card.title" :subtitle="card.subtitle" />
            <img class="entry-bg-image visual-img" :src="card.image" :alt="card.title" @error="hideBrokenImage" />
            <span class="entry-content">
              <span class="entry-icon">
                <component :is="card.icon" />
              </span>
              <strong>{{ card.title }}</strong>
              <small>{{ card.subtitle }}</small>
              <em>{{ card.action }}</em>
            </span>
          </RouterLink>
        </section>

        <div class="home-layout">
          <div class="main-column">
            <section class="section-card">
              <div class="section-head">
                <div>
                  <h2 class="section-title">为你推荐</h2>
                  <p class="muted">围绕气血、脾胃、睡眠与轻体管理，挑选更适合日常执行的药膳。</p>
                </div>
                <RouterLink class="text-link" to="/recipes">查看更多</RouterLink>
              </div>
              <div class="recipe-grid">
                <RecipeCard v-for="recipe in home.recommended.slice(0, 4)" :key="recipe.id" :recipe="recipe" />
              </div>
            </section>

            <section class="section-card seasonal-card">
              <div class="seasonal-poster">
                <span class="vertical-note">夏 · 清润养心</span>
                <h2>当季调养<br />清润为先</h2>
                <p>顺应节气，少油少燥，以汤、羹、茶饮温和调理。</p>
              </div>
              <div class="seasonal-list">
                <div class="section-head">
                  <h2 class="section-title">当季推荐</h2>
                  <RouterLink class="text-link" to="/recipes">查看全部</RouterLink>
                </div>
                <div class="compact-list">
                  <article v-for="recipe in home.seasonal.slice(0, 3)" :key="recipe.id" class="mini-recipe">
                    <div class="mini-visual">
                      <VisualBlock :variant="variantFor(recipe)" :title="recipe.name" compact />
                      <img v-if="recipe.imageUrl" class="content-image visual-img" :src="recipe.imageUrl" :alt="recipe.name" @error="hideBrokenImage" />
                    </div>
                    <div>
                      <strong>{{ recipe.name }}</strong>
                      <div class="tags">
                        <span v-for="tag in recipe.tags.slice(0, 2)" :key="tag" class="tag">{{ tag }}</span>
                      </div>
                    </div>
                    <RouterLink class="mini-action" :to="recipe.type === 'tea' ? '/teas' : recipe.type === 'soup' ? '/soups' : '/recipes'">查看</RouterLink>
                  </article>
                </div>
              </div>
            </section>

            <div class="bottom-grid">
              <section class="section-card hot-categories">
                <div class="section-head">
                  <h2 class="section-title">热门推荐分类</h2>
                </div>
                <div class="category-grid">
                  <RouterLink v-for="item in categoryItems" :key="item" class="category-tile" to="/recipes">
                    <span>{{ item }}</span>
                  </RouterLink>
                </div>
              </section>

              <section class="section-card articles-preview">
                <div class="section-head">
                  <h2 class="section-title">养生资讯</h2>
                  <RouterLink class="text-link" to="/articles">查看更多</RouterLink>
                </div>
                <div class="article-strip">
                  <article v-for="article in home.articles.slice(0, 4)" :key="article.id" class="article-mini">
                    <div class="mini-visual">
                      <VisualBlock variant="article" :title="article.category" compact />
                      <img v-if="article.cover" class="content-image visual-img" :src="article.cover" :alt="article.title" @error="hideBrokenImage" />
                    </div>
                    <div>
                      <strong>{{ article.title }}</strong>
                      <p class="muted">{{ article.summary }}</p>
                      <small>{{ article.views }} 阅读</small>
                    </div>
                  </article>
                </div>
              </section>
            </div>
          </div>

          <aside class="side-stack">
            <section class="side-card stats-card">
              <div class="side-title">
                <el-icon><Collection /></el-icon>
                <h2>我的收藏</h2>
              </div>
              <RouterLink class="more-link" to="/favorites">更多</RouterLink>
              <div class="stat-line">
                <span>收藏的药膳</span>
                <strong>{{ home.favoriteSummary.medicated_food ?? 0 }}</strong>
              </div>
              <div class="stat-line">
                <span>收藏的汤方</span>
                <strong>{{ home.favoriteSummary.soup ?? 0 }}</strong>
              </div>
              <div class="stat-line">
                <span>收藏的茶方</span>
                <strong>{{ home.favoriteSummary.tea ?? 0 }}</strong>
              </div>
            </section>

            <section class="side-card welcome-card">
              <span class="avatar">
                <el-icon><User /></el-icon>
              </span>
              <h2>{{ auth.user ? `欢迎您，${auth.user.nickname}` : '欢迎来到智膳坊' }}</h2>
              <p class="muted">{{ auth.user ? '继续查看您的个性化推荐与收藏' : '登录后可同步收藏、健康档案与 AI 推荐结果' }}</p>
              <RouterLink class="primary-btn block-btn" :to="auth.user ? '/favorites' : '/login'">
                {{ auth.user ? '查看收藏' : '登录 / 注册' }}
              </RouterLink>
            </section>

            <section class="side-card ai-side-card">
              <div>
                <div class="eyebrow">AI 药膳入口</div>
                <h2>定制专属药膳方案</h2>
                <p>填写健康档案，生成体质画像、食材配方与忌口建议。</p>
                <RouterLink class="brown-btn" to="/ai">开始定制</RouterLink>
              </div>
              <el-icon class="ai-side-icon"><Star /></el-icon>
            </section>
          </aside>
        </div>
      </div>
    </div>
  </AppShell>
</template>
