<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ChatDotRound, Collection, FirstAidKit, Refresh, Share, Warning } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import ContentImage from '../components/ContentImage.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'
import { mockRecommendation, mockRecipes } from '../data/mock'
import { api } from '../services/api'
import { useFavoriteStore } from '../stores/favorites'
import type { Recipe, RecommendationResult } from '../types'

const route = useRoute()
const router = useRouter()
const favorites = useFavoriteStore()
const result = ref<RecommendationResult>(mockRecommendation)
const saved = ref(false)
const saving = ref(false)
const resultImage = computed(() =>
  mockRecipes.find((recipe) => recipe.name === result.value.recipeName)?.imageUrl
  || contentImages.recipes[result.value.recipeName]
  || contentImages.recipes['当归黄芪乌鸡汤'],
)

const basicInfo = [
  ['功效', '补气养血、健脾益气、增强免疫'],
  ['适合体质', '气血两虚、体倦乏力、面色少华、易疲劳人群'],
  ['食用建议', '每周 1-2 次，建议午餐温热食用'],
  ['烹饪时长', '约 2.5 小时'],
  ['适宜人群', '成年人、亚健康调理、产后恢复人群'],
  ['注意事项', '感冒发热、口舌生疮者暂不宜食用'],
]

function variantFor(recipe: Recipe) {
  if (recipe.type === 'soup') return 'soup'
  if (recipe.type === 'tea') return 'tea'
  return 'recipe'
}

onMounted(async () => {
  result.value = await api.recommendation(String(route.query.id ?? 1001))
  await favorites.load()
  saved.value = favorites.items.some((item) => item.targetType === 'recommendation' && item.targetId === result.value.id)
})

async function saveResult() {
  if (saved.value || saving.value) return
  saving.value = true
  await favorites.addRecommendation(result.value)
  saved.value = true
  saving.value = false
}
</script>

<template>
  <AppShell>
    <div class="container result-page">
      <div class="breadcrumb">首页 <span>/</span> AI 药膳 <span>/</span> 个性化推荐结果</div>

      <section class="result-main-card">
        <div class="result-visual">
          <ContentImage :src="resultImage" :alt="result.recipeName" image-class="hero-real-image visual-img">
            <VisualBlock variant="result" :title="result.recipeName" subtitle="本次主推荐" />
          </ContentImage>
        </div>
        <div class="result-main-copy">
          <div class="eyebrow">本次为您推荐</div>
          <h1 class="page-title">{{ result.recipeName }}</h1>
          <div class="tags">
            <span v-for="tag in result.tags" :key="tag" class="tag large-tag">{{ tag }}</span>
          </div>
          <p>{{ result.reason }}</p>
          <div class="ai-note">AI 推荐结果仅供参考，具体请结合个人体质与专业医师建议。</div>
        </div>
      </section>

      <div class="content-layout result-layout">
        <div class="main-column">
          <section class="section-card">
            <div class="section-head">
              <h2 class="section-title">基本信息</h2>
            </div>
            <div class="info-grid">
              <div v-for="[label, value] in basicInfo" :key="label" class="info-cell">
                <span>{{ label }}</span>
                <strong>{{ value }}</strong>
              </div>
            </div>
          </section>

          <section class="section-card">
            <div class="section-head">
              <h2 class="section-title">为什么适合您</h2>
            </div>
            <p class="report-text">{{ result.bodyAnalysis }}</p>
            <p class="report-text">{{ result.reason }}</p>
          </section>

          <section class="section-card">
            <div class="section-head">
              <h2 class="section-title">药膳成分</h2>
            </div>
            <div class="ingredient-grid">
              <article v-for="item in result.ingredients" :key="item" class="ingredient-card">
                <span>{{ item.split(' ')[0] }}</span>
                <small>{{ item.includes(' ') ? item.split(' ').slice(1).join(' ') : '适量' }}</small>
              </article>
            </div>
          </section>

          <section class="section-card cooking-card">
            <div>
              <div class="section-head">
                <h2 class="section-title">食材搭配与烹制建议</h2>
              </div>
              <ol class="step-list">
                <li v-for="stepItem in result.steps" :key="stepItem">{{ stepItem }}</li>
              </ol>
            </div>
            <div class="cooking-visual">
              <ContentImage :src="resultImage" :alt="`${result.recipeName}烹饪示意`">
                <VisualBlock variant="soup" title="温火慢炖" compact />
              </ContentImage>
            </div>
          </section>

          <section class="section-card warning-card">
            <div class="section-head">
              <h2 class="section-title">忌口建议</h2>
            </div>
            <div class="warning-grid">
              <article v-for="warning in result.warnings" :key="warning">
                <el-icon><Warning /></el-icon>
                <span>{{ warning }}</span>
              </article>
            </div>
          </section>

          <div class="result-actions">
            <button class="brown-btn" type="button" :class="{ active: saved }" :disabled="saving" @click="saveResult">
              <el-icon><Collection /></el-icon>
              {{ saved ? '已收藏方案' : saving ? '收藏中...' : '收藏方案' }}
            </button>
            <button class="ghost-btn" type="button"><el-icon><Share /></el-icon>分享方案</button>
            <button class="ghost-btn" type="button"><el-icon><ChatDotRound /></el-icon>咨询医师</button>
            <button class="primary-btn" type="button" @click="router.push('/ai/profile')"><el-icon><Refresh /></el-icon>重新评估体质</button>
          </div>
        </div>

        <aside class="side-stack">
          <section class="side-card season-side">
            <div class="side-title">
              <el-icon><FirstAidKit /></el-icon>
              <h2>相关推荐</h2>
            </div>
            <div class="compact-list">
              <article v-for="recipe in result.relatedRecipes" :key="recipe.id" class="mini-recipe side-mini">
                <div class="mini-visual">
                  <ContentImage :src="recipe.imageUrl" :alt="recipe.name">
                    <VisualBlock :variant="variantFor(recipe)" :title="recipe.name" compact />
                  </ContentImage>
                </div>
                <div>
                  <strong>{{ recipe.name }}</strong>
                  <div class="tags">
                    <span v-for="tag in recipe.tags.slice(0, 2)" :key="tag" class="tag">{{ tag }}</span>
                  </div>
                </div>
              </article>
            </div>
          </section>

          <section class="side-card season-side">
            <div class="side-title">
              <el-icon><Collection /></el-icon>
              <h2>可搭配茶饮</h2>
            </div>
            <div class="compact-list">
              <article v-for="recipe in result.teaPairings" :key="recipe.id" class="mini-recipe side-mini">
                <div class="mini-visual">
                  <ContentImage :src="recipe.imageUrl" :alt="recipe.name">
                    <VisualBlock variant="tea" :title="recipe.name" compact />
                  </ContentImage>
                </div>
                <div>
                  <strong>{{ recipe.name }}</strong>
                  <div class="tags">
                    <span v-for="tag in recipe.tags.slice(0, 2)" :key="tag" class="tag">{{ tag }}</span>
                  </div>
                </div>
              </article>
            </div>
          </section>

          <section class="side-card portrait-card">
            <div class="side-title">
              <el-icon><FirstAidKit /></el-icon>
              <h2>您的体质画像</h2>
            </div>
            <ul>
              <li><strong>体质类型：</strong>气血两虚质（偏）</li>
              <li><strong>主要表现：</strong>容易疲劳、面色少华、睡眠质量一般。</li>
              <li><strong>调养建议：</strong>注重补气养血，规律作息，保持温和运动。</li>
            </ul>
            <button class="primary-btn block-btn" type="button" @click="router.push('/ai/profile')">重新评估体质</button>
          </section>
        </aside>
      </div>
    </div>
  </AppShell>
</template>
