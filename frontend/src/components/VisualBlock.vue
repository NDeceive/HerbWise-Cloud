<script setup lang="ts">
import { computed } from 'vue'
import { Bowl, CoffeeCup, Dish, FirstAidKit, Grape, Reading, Star } from '@element-plus/icons-vue'

const props = withDefaults(defineProps<{
  variant?: 'hero' | 'recipe' | 'soup' | 'tea' | 'article' | 'result' | 'profile' | 'herb'
  title?: string
  subtitle?: string
  compact?: boolean
}>(), {
  variant: 'herb',
  title: '',
  subtitle: '',
  compact: false,
})

const icon = computed(() => {
  if (props.variant === 'soup') return Bowl
  if (props.variant === 'tea') return CoffeeCup
  if (props.variant === 'article') return Reading
  if (props.variant === 'result' || props.variant === 'profile') return FirstAidKit
  if (props.variant === 'recipe') return Dish
  if (props.variant === 'hero') return Star
  return Grape
})
</script>

<template>
  <div class="visual-block" :class="[`visual-${variant}`, { compact }]">
    <span class="visual-orb visual-orb-one" />
    <span class="visual-orb visual-orb-two" />
    <span class="visual-line line-one" />
    <span class="visual-line line-two" />
    <span class="visual-icon">
      <component :is="icon" />
    </span>
    <span v-if="title || subtitle" class="visual-copy">
      <strong>{{ title }}</strong>
      <small>{{ subtitle }}</small>
    </span>
  </div>
</template>
