<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { Collection, Reading, Search } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import ContentImage from '../components/ContentImage.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'
import { mockArticles } from '../data/mock'
import { api } from '../services/api'
import { useFavoriteStore } from '../stores/favorites'
import type { Article } from '../types'

const favorites = useFavoriteStore()
const articles = ref<Article[]>(mockArticles)
const active = ref('全部')
const categories = ['全部', '养生指南', '调理方法', '药膳食疗', '节气养生']

const filtered = computed(() =>
  active.value === '全部' ? articles.value : articles.value.filter((article) => article.category === active.value),
)

onMounted(async () => {
  articles.value = await api.articles()
})

</script>

<template>
  <AppShell>
    <div class="container articles-page">
      <section class="listing-hero articles-hero">
        <div class="listing-copy">
          <div class="eyebrow">养生资讯</div>
          <h1 class="page-title">当季养生与药膳食疗知识</h1>
          <p>热门帖子、调理方法、药膳食疗与节气养生，让用户在浏览中建立健康认知。</p>
          <div class="search-row compact-search">
            <el-icon><Search /></el-icon>
            <input aria-label="搜索资讯" placeholder="搜索节气、体质、食疗方法" />
          </div>
        </div>
        <div class="listing-hero-image">
          <ContentImage :src="contentImages.banners.articles" alt="草本养生与四时食材" image-class="banner-real-image visual-img">
            <VisualBlock variant="article" title="四时养生" subtitle="Wellness Notes" />
          </ContentImage>
        </div>
      </section>

      <div class="filter-bar">
        <div class="filter-left">
          <el-icon><Reading /></el-icon>
          <button v-for="item in categories" :key="item" :class="{ active: active === item }" type="button" @click="active = item">
            {{ item }}
          </button>
        </div>
      </div>

      <section class="section-card">
        <div class="section-head">
          <div>
            <h2 class="section-title">精选资讯</h2>
            <p class="muted">以节气、体质、食疗和日常习惯为线索，沉淀可读可用的养生内容。</p>
          </div>
        </div>
        <div class="article-grid">
          <article v-for="article in filtered" :key="article.id" class="article-card">
            <div class="article-visual">
              <ContentImage :src="article.cover" :alt="article.title">
                <VisualBlock variant="article" :title="article.category" compact />
              </ContentImage>
            </div>
            <div class="article-body">
              <span class="tag">{{ article.category }}</span>
              <h3>{{ article.title }}</h3>
              <p>{{ article.summary }}</p>
              <div class="article-meta">
                <small>{{ article.views }} 阅读</small>
                <button class="favorite-btn" type="button" @click="favorites.toggleArticle(article.id)">
                  <el-icon><Collection /></el-icon>
                  收藏
                </button>
              </div>
            </div>
          </article>
        </div>
      </section>
    </div>
  </AppShell>
</template>
