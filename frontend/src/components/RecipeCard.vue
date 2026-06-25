<script setup lang="ts">
import { computed } from 'vue'
import { Star, Timer } from '@element-plus/icons-vue'
import { useFavoriteStore } from '../stores/favorites'
import VisualBlock from './VisualBlock.vue'
import type { Recipe } from '../types'

const props = withDefaults(defineProps<{
  recipe: Recipe
  compact?: boolean
}>(), {
  compact: false,
})

const favorites = useFavoriteStore()
const isFavorite = computed(() => favorites.isRecipeFavorite(props.recipe))
const visualVariant = computed(() => {
  if (props.recipe.type === 'soup') return 'soup'
  if (props.recipe.type === 'tea') return 'tea'
  return 'recipe'
})
</script>

<template>
  <article class="recipe-card" :class="{ compact }">
    <div class="recipe-photo" role="img" :aria-label="recipe.name">
      <VisualBlock :variant="visualVariant" :title="recipe.name" :subtitle="recipe.category" compact />
      <span v-if="recipe.season" class="season-badge">{{ recipe.season }}</span>
    </div>
    <div class="recipe-body">
      <h3 class="recipe-title">{{ recipe.name }}</h3>
      <div class="tags">
        <span v-for="tag in recipe.tags.slice(0, 3)" :key="tag" class="tag">{{ tag }}</span>
      </div>
      <p class="muted">{{ recipe.summary }}</p>
      <div class="recipe-meta">
        <span class="muted">
          <el-icon><Timer /></el-icon>
          {{ recipe.cookingTime }}
        </span>
        <button class="favorite-btn" :class="{ active: isFavorite }" type="button" @click="favorites.toggleRecipe(recipe)">
          <el-icon><Star /></el-icon>
          {{ isFavorite ? '已收藏' : '收藏' }}
        </button>
      </div>
    </div>
  </article>
</template>
