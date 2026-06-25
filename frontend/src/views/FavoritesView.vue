<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { Bowl, Collection, CoffeeCup, Delete, Dish, Reading } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import ContentImage from '../components/ContentImage.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'
import { useFavoriteStore } from '../stores/favorites'
import type { Favorite } from '../types'

const favorites = useFavoriteStore()
const active = ref('recipe')
const tabs = [
  { label: '药膳方案', value: 'recipe', icon: Dish },
  { label: '汤方', value: 'soup', icon: Bowl },
  { label: '茶方', value: 'tea', icon: CoffeeCup },
  { label: '养生资讯', value: 'article', icon: Reading },
]

onMounted(() => {
  favorites.load()
})

const visibleItems = computed(() => {
  if (active.value === 'recipe') {
    return favorites.items.filter((item) => item.targetType === 'recipe' || item.targetType === 'recommendation')
  }
  return favorites.items.filter((item) => item.targetType === active.value)
})

function variantFor(item: Favorite) {
  if (item.targetType === 'soup') return 'soup'
  if (item.targetType === 'tea') return 'tea'
  if (item.targetType === 'article') return 'article'
  if (item.targetType === 'recommendation') return 'result'
  return 'recipe'
}

function typeLabel(type: string) {
  return {
    recipe: '药膳方案',
    recommendation: 'AI 药膳方案',
    soup: '汤方',
    tea: '茶方',
    article: '养生资讯',
  }[type] || '收藏'
}

function imageFor(item: Favorite) {
  if (item.targetType === 'article') {
    return favorites.articleForFavorite(item)?.cover || contentImages.articles.default
  }
  if (item.targetType === 'recommendation') {
    return contentImages.recipes['当归黄芪乌鸡汤']
  }

  const recipe = favorites.recipeForFavorite(item)
  if (recipe?.imageUrl) return recipe.imageUrl
  if (item.targetType === 'soup') return contentImages.entries.soup
  if (item.targetType === 'tea') return contentImages.entries.tea
  return contentImages.entries.recipe
}

</script>

<template>
  <AppShell>
    <div class="container favorites-page">
      <section class="listing-hero favorites-hero">
        <div class="listing-copy">
          <div class="eyebrow">养生收藏馆</div>
          <h1 class="page-title">集中管理您收藏的药膳、汤方、茶方与资讯</h1>
          <p>把偶然发现的良方、茶饮和养生文章妥善保存，后续可一键查看与取消收藏。</p>
        </div>
        <div class="listing-hero-image">
          <ContentImage :src="contentImages.herbBasket" alt="草本药材与养生食材" image-class="banner-real-image visual-img">
            <VisualBlock variant="herb" title="我的养生册" subtitle="Saved Plans" />
          </ContentImage>
        </div>
      </section>

      <section class="section-card">
        <div class="favorite-tabs">
          <button v-for="tab in tabs" :key="tab.value" :class="{ active: active === tab.value }" type="button" @click="active = tab.value">
            <component :is="tab.icon" />
            {{ tab.label }}
          </button>
        </div>

        <div v-if="visibleItems.length" class="favorite-list">
          <article v-for="item in visibleItems" :key="item.id" class="favorite-item">
            <div class="favorite-visual">
              <ContentImage :src="imageFor(item)" :alt="item.title">
                <VisualBlock :variant="variantFor(item)" :title="typeLabel(item.targetType)" compact />
              </ContentImage>
            </div>
            <div>
              <span class="tag">{{ typeLabel(item.targetType) }}</span>
              <h3>{{ item.title }}</h3>
              <p>{{ item.summary }}</p>
              <small>收藏时间：{{ new Date(item.createdAt).toLocaleDateString() }}</small>
            </div>
            <button class="ghost-btn remove-btn" type="button" @click="favorites.remove(item.id)">
              <el-icon><Delete /></el-icon>
              取消收藏
            </button>
          </article>
        </div>

        <div v-else class="empty-state">
          <span><el-icon><Collection /></el-icon></span>
          <h2>暂无收藏内容</h2>
          <p class="muted">去药膳、汤方、茶方或资讯页面收藏您感兴趣的内容。</p>
        </div>
      </section>
    </div>
  </AppShell>
</template>
