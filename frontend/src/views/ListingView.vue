<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Bowl, CoffeeCup, Collection, Dish, Filter, MagicStick, PriceTag } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import RecipeCard from '../components/RecipeCard.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { api } from '../services/api'
import type { Recipe, RecipeType } from '../types'

const route = useRoute()
const recipes = ref<Recipe[]>([])
const category = ref('全部')
const categories = ['全部', '养生', '减脂', '祛火', '祛湿', '安神']
const pageType = computed(() => route.meta.type as RecipeType)
const hotTags = ['养生', '减脂', '祛火', '祛湿', '安神', '助眠', '补气养血', '调理脾胃']

const pageMeta = computed(() => {
  if (pageType.value === 'soup') {
    return {
      eyebrow: '汤方专区',
      title: '汤方专区 · 汤养全家',
      subtitle: '从经典汤方到当季滋补，按体质、季节与功效找到适合的一碗汤。',
      variant: 'soup' as const,
      icon: Bowl,
      popularTitle: '人气汤品',
    }
  }
  if (pageType.value === 'tea') {
    return {
      eyebrow: '茶方秘典 / 中药奶茶',
      title: '茶方秘典 · 草本入茶',
      subtitle: '精选草本茶饮与中药奶茶，兼顾口味、调理目标与日常轻养生。',
      variant: 'tea' as const,
      icon: CoffeeCup,
      popularTitle: '人气茶饮',
    }
  }
  return {
    eyebrow: '药膳精选',
    title: '药膳精选 · 为你推荐',
    subtitle: '分类展示食膳配方，置顶个性化推荐，适合比赛展示的内容门户。',
    variant: 'recipe' as const,
    icon: Dish,
    popularTitle: '人气推荐',
  }
})

const recommendedRecipes = computed(() => recipes.value.slice(0, 4))
const seasonalRecipes = computed(() => recipes.value.slice(1, 4).length >= 3 ? recipes.value.slice(1, 4) : recipes.value.slice(0, 3))
const popularRecipes = computed(() => [...recipes.value].sort((a, b) => b.popularity - a.popularity).slice(0, 4))

function variantFor(recipe: Recipe) {
  if (recipe.type === 'soup') return 'soup'
  if (recipe.type === 'tea') return 'tea'
  return 'recipe'
}

async function load() {
  const selectedCategory = category.value === '全部' ? undefined : category.value
  const typed = await api.recipes(pageType.value, selectedCategory)
  let merged = [...typed]
  if (merged.length < 4) {
    const fallback = await api.recipes(pageType.value)
    merged = [...merged, ...fallback.filter((item) => !merged.some((recipe) => recipe.id === item.id))]
  }
  if (merged.length < 4) {
    const all = await api.recipes()
    merged = [...merged, ...all.filter((item) => !merged.some((recipe) => recipe.id === item.id))]
  }
  recipes.value = merged
}

onMounted(load)
watch(() => route.path, () => {
  category.value = '全部'
  load()
})
watch(category, load)
</script>

<template>
  <AppShell>
    <div class="container listing-page">
      <section class="listing-hero">
        <div class="listing-copy">
          <div class="eyebrow">{{ pageMeta.eyebrow }}</div>
          <h1 class="page-title">{{ pageMeta.title }}</h1>
          <p>{{ pageMeta.subtitle }}</p>
          <div class="sub-nav">
            <RouterLink to="/recipes" :class="{ active: pageType === 'medicated_food' }">药膳精选</RouterLink>
            <RouterLink to="/soups" :class="{ active: pageType === 'soup' }">汤方专区</RouterLink>
            <RouterLink to="/teas" :class="{ active: pageType === 'tea' }">茶方秘典 / 中药奶茶</RouterLink>
          </div>
        </div>
        <div class="listing-hero-image">
          <VisualBlock :variant="pageMeta.variant" :title="pageMeta.eyebrow" subtitle="Herbal Wellness" />
        </div>
      </section>

      <div class="filter-bar">
        <div class="filter-left">
          <el-icon><Filter /></el-icon>
          <button v-for="item in categories" :key="item" :class="{ active: category === item }" type="button" @click="category = item">
            {{ item }}
          </button>
        </div>
        <button class="sort-btn" type="button">综合排序</button>
      </div>

      <div class="content-layout listing-layout">
        <div class="main-column">
          <section class="section-card">
            <div class="section-head">
              <div>
                <h2 class="section-title">为你推荐</h2>
                <p class="muted">根据体质偏好、功效标签和人气热度，优先展示适合日常调养的内容。</p>
              </div>
              <RouterLink class="text-link" :to="route.path">查看全部</RouterLink>
            </div>
            <div class="recipe-grid">
              <RecipeCard v-for="recipe in recommendedRecipes" :key="recipe.id" :recipe="recipe" />
            </div>
          </section>

          <section class="section-card landscape-section">
            <div class="section-head">
              <div>
                <h2 class="section-title">季节限定</h2>
                <p class="muted">应时而食，顺应节气，温和调理更适合长期坚持。</p>
              </div>
            </div>
            <div class="landscape-grid">
              <article v-for="recipe in seasonalRecipes" :key="recipe.id" class="landscape-card">
                <div class="landscape-visual">
                  <VisualBlock :variant="variantFor(recipe)" :title="recipe.name" compact />
                </div>
                <div>
                  <h3>{{ recipe.name }}</h3>
                  <div class="tags">
                    <span v-for="tag in recipe.tags.slice(0, 2)" :key="tag" class="tag">{{ tag }}</span>
                  </div>
                  <p>{{ recipe.summary }}</p>
                </div>
              </article>
            </div>
          </section>

          <section class="section-card">
            <div class="section-head">
              <div>
                <h2 class="section-title">{{ pageMeta.popularTitle }}</h2>
                <p class="muted">大家都在浏览和收藏的健康好味道。</p>
              </div>
            </div>
            <div class="popular-strip">
              <RecipeCard v-for="recipe in popularRecipes" :key="`popular-${recipe.id}`" :recipe="recipe" compact />
            </div>
          </section>
        </div>

        <aside class="side-stack">
          <section class="side-card stats-card">
            <div class="side-title">
              <el-icon><Collection /></el-icon>
              <h2>收藏概览</h2>
            </div>
            <div class="stat-line"><span>我的收藏</span><strong>12</strong></div>
            <div class="stat-line"><span>药膳方案</span><strong>8</strong></div>
            <div class="stat-line"><span>汤方</span><strong>2</strong></div>
            <div class="stat-line"><span>茶方</span><strong>2</strong></div>
          </section>

          <section class="side-card season-side">
            <div class="side-title">
              <component :is="pageMeta.icon" />
              <h2>当季养生推荐</h2>
            </div>
            <div class="compact-list">
              <article v-for="recipe in recipes.slice(0, 3)" :key="`side-${recipe.id}`" class="mini-recipe side-mini">
                <div class="mini-visual">
                  <VisualBlock :variant="variantFor(recipe)" :title="recipe.name" compact />
                </div>
                <div>
                  <strong>{{ recipe.name }}</strong>
                  <div class="tags">
                    <span v-for="tag in recipe.tags.slice(0, 2)" :key="tag" class="tag">{{ tag }}</span>
                  </div>
                </div>
              </article>
            </div>
            <RouterLink class="text-link centered-link" :to="route.path">更多当季推荐</RouterLink>
          </section>

          <section class="side-card">
            <div class="side-title">
              <el-icon><PriceTag /></el-icon>
              <h2>热门标签</h2>
            </div>
            <div class="tag-cloud">
              <span v-for="tag in hotTags" :key="tag">{{ tag }}</span>
            </div>
          </section>

          <section class="side-card ai-side-card">
            <div class="eyebrow">AI 药膳入口</div>
            <h2>定制您的专属方案</h2>
            <p>填写健康档案，快速生成体质画像与药膳推荐。</p>
            <RouterLink class="brown-btn" to="/ai">开始定制</RouterLink>
            <el-icon class="ai-side-icon"><MagicStick /></el-icon>
          </section>
        </aside>
      </div>
    </div>
  </AppShell>
</template>
