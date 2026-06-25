<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Collection, Filter, MagicStick, PriceTag, WarningFilled } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import ContentImage from '../components/ContentImage.vue'
import RecipeCard from '../components/RecipeCard.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'
import { api } from '../services/api'
import type { Recipe, RecipeType } from '../types'

type PlanTabType = 'all' | RecipeType

const route = useRoute()
const router = useRouter()
const recipes = ref<Recipe[]>([])
const category = ref('全部')

const tabs: Array<{ label: string; type: PlanTabType }> = [
  { label: '全部推荐', type: 'all' },
  { label: '药膳', type: 'medicated_food' },
  { label: '汤方', type: 'soup' },
  { label: '茶饮', type: 'tea' },
]

const categories = ['全部', '养生', '减脂', '祛火', '祛湿', '安神', '助眠', '补气养血']
const hotTags = ['养生', '减脂', '祛火', '祛湿', '安神', '助眠', '补气养血', '健脾', '润肺', '清热']
const categoryAliases: Record<string, string[]> = {
  祛火: ['祛火', '清热', '清润', '上火'],
  助眠: ['助眠', '安神', '睡眠'],
  补气养血: ['补气养血', '气血', '补血', '补气'],
}

const activeType = computed<PlanTabType>(() => {
  const queryType = route.query.type
  if (queryType === 'medicated_food' || queryType === 'soup' || queryType === 'tea') return queryType
  return 'all'
})

const activeApiType = computed(() => activeType.value === 'all' ? undefined : activeType.value)
const typeLabel = computed(() => tabs.find((tab) => tab.type === activeType.value)?.label || '全部推荐')

const filteredRecipes = computed(() => {
  if (category.value === '全部') return recipes.value
  const keywords = categoryAliases[category.value] || [category.value]
  return recipes.value.filter((recipe) => {
    const text = [
      recipe.category,
      recipe.effects,
      recipe.summary,
      recipe.suitablePeople,
      ...recipe.tags,
    ].join(' ')
    return keywords.some((keyword) => text.includes(keyword))
  })
})

const recommendedRecipes = computed(() =>
  [...filteredRecipes.value].sort((a, b) => b.popularity - a.popularity).slice(0, 4),
)
const gridRecipes = computed(() => filteredRecipes.value.slice(0, 8))
const seasonalRecipes = computed(() => {
  const seasonal = filteredRecipes.value.filter((recipe) => recipe.season && recipe.season !== '四季')
  return (seasonal.length >= 3 ? seasonal : filteredRecipes.value).slice(0, 3)
})

function variantFor(recipe: Recipe) {
  if (recipe.type === 'soup') return 'soup'
  if (recipe.type === 'tea') return 'tea'
  return 'recipe'
}

function tabTo(type: PlanTabType) {
  const query: Record<string, string> = {}
  if (type !== 'all') query.type = type
  if (category.value !== '全部') query.category = category.value
  return { path: '/plans', query }
}

function setCategory(item: string) {
  category.value = item
  const query: Record<string, string> = {}
  if (activeType.value !== 'all') query.type = activeType.value
  if (item !== '全部') query.category = item
  router.replace({ path: '/plans', query })
}

async function load() {
  recipes.value = await api.recipes(activeApiType.value)
}

onMounted(load)
watch(activeApiType, load)
watch(
  () => route.query.category,
  (value) => {
    category.value = typeof value === 'string' && categories.includes(value) ? value : '全部'
  },
  { immediate: true },
)
</script>

<template>
  <AppShell>
    <div class="container listing-page plans-page">
      <header class="page-intro">
        <h1 class="page-title">膳养方案</h1>
        <p>整合药膳、汤方与草本茶饮，根据体质、功效和场景筛选适合的日常调理方案。</p>
      </header>

      <section class="listing-hero plans-hero">
        <div class="listing-copy">
          <div class="eyebrow">药食同源 · 膳养方案</div>
          <h2 class="page-title">一站式查看药膳、汤方与草本茶饮</h2>
          <p>从体质调理、季节养生和日常饮食场景出发，找到更适合自己的膳养方案。</p>
          <div class="sub-nav plan-tabs">
            <RouterLink
              v-for="tab in tabs"
              :key="tab.type"
              :to="tabTo(tab.type)"
              :class="{ active: activeType === tab.type }"
            >
              {{ tab.label }}
            </RouterLink>
          </div>
        </div>
        <div class="listing-hero-image">
          <ContentImage :src="contentImages.hero" alt="中式药膳汤品与草本食材" image-class="banner-real-image visual-img">
            <VisualBlock variant="hero" title="膳养方案" subtitle="Herbal Wellness" />
          </ContentImage>
        </div>
      </section>

      <div class="filter-bar">
        <div class="filter-left">
          <el-icon><Filter /></el-icon>
          <button v-for="item in categories" :key="item" :class="{ active: category === item }" type="button" @click="setCategory(item)">
            {{ item }}
          </button>
        </div>
        <span class="filter-summary">{{ typeLabel }} · {{ filteredRecipes.length }} 个方案</span>
      </div>

      <div class="content-layout listing-layout">
        <div class="main-column">
          <section class="section-card">
            <div class="section-head">
              <div>
                <h2 class="section-title">为你推荐</h2>
                <p class="muted">整合药膳、汤方与草本茶饮，优先展示热度高、适合日常调理的方案。</p>
              </div>
              <RouterLink class="text-link" to="/ai">让 AI 帮我选</RouterLink>
            </div>
            <div v-if="recommendedRecipes.length" class="recipe-grid">
              <RecipeCard v-for="recipe in recommendedRecipes" :key="recipe.id" :recipe="recipe" />
            </div>
            <div v-else class="plans-empty">当前筛选暂无方案，可切换标签或使用 AI 药膳生成专属建议。</div>
          </section>

          <section class="section-card landscape-section">
            <div class="section-head">
              <div>
                <h2 class="section-title">季节调养</h2>
                <p class="muted">顺应节气与身体状态，以汤、羹、茶饮做温和、可坚持的日常调理。</p>
              </div>
            </div>
            <div class="landscape-grid">
              <article v-for="recipe in seasonalRecipes" :key="recipe.id" class="landscape-card">
                <div class="landscape-visual">
                  <ContentImage :src="recipe.imageUrl" :alt="recipe.name">
                    <VisualBlock :variant="variantFor(recipe)" :title="recipe.name" compact />
                  </ContentImage>
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
                <h2 class="section-title">全部方案</h2>
                <p class="muted">按类型、功效和场景筛选，快速查看可收藏、可执行的膳养内容。</p>
              </div>
            </div>
            <div v-if="gridRecipes.length" class="recipe-grid">
              <RecipeCard v-for="recipe in gridRecipes" :key="`grid-${recipe.id}`" :recipe="recipe" compact />
            </div>
            <div v-else class="plans-empty">没有找到匹配内容，建议换一个功效标签继续浏览。</div>
          </section>
        </div>

        <aside class="side-stack">
          <section class="side-card ai-side-card">
            <div class="eyebrow">AI 药膳入口</div>
            <h2>生成专属膳养建议</h2>
            <p>填写健康档案，结合体质、偏好与忌口，生成更贴合个人状态的药膳建议。</p>
            <RouterLink class="brown-btn" to="/ai">开始定制</RouterLink>
            <el-icon class="ai-side-icon"><MagicStick /></el-icon>
          </section>

          <section class="side-card">
            <div class="side-title">
              <el-icon><PriceTag /></el-icon>
              <h2>热门标签</h2>
            </div>
            <div class="tag-cloud">
              <button v-for="tag in hotTags" :key="tag" type="button" @click="setCategory(categories.includes(tag) ? tag : '全部')">
                {{ tag }}
              </button>
            </div>
          </section>

          <section class="side-card stats-card">
            <div class="side-title">
              <el-icon><Collection /></el-icon>
              <h2>收藏概览</h2>
            </div>
            <div class="stat-line"><span>药膳方案</span><strong>12</strong></div>
            <div class="stat-line"><span>汤方</span><strong>8</strong></div>
            <div class="stat-line"><span>茶饮</span><strong>6</strong></div>
            <RouterLink class="text-link centered-link" to="/favorites">查看我的收藏</RouterLink>
          </section>

          <section class="side-card safety-card">
            <div class="side-title">
              <el-icon><WarningFilled /></el-icon>
              <h2>安全用膳提示</h2>
            </div>
            <ul class="safety-list">
              <li>药膳适合日常调理，不能替代诊疗或处方。</li>
              <li>孕期、慢病、用药中人群请先咨询专业医师。</li>
              <li>出现过敏或不适时，应立即停止食用。</li>
            </ul>
          </section>
        </aside>
      </div>
    </div>
  </AppShell>
</template>
