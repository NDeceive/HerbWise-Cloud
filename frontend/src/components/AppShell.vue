<script setup lang="ts">
import { onMounted } from 'vue'
import AppHeader from './AppHeader.vue'
import { useFavoriteStore } from '../stores/favorites'

const favorites = useFavoriteStore()

onMounted(() => {
  if (!favorites.loaded) {
    favorites.load()
  }
})
</script>

<template>
  <div class="app-shell">
    <AppHeader />
    <main class="page-main">
      <slot />
    </main>
  </div>
</template>
