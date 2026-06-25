<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Collection, Fold, User } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const route = useRoute()
const menuOpen = ref(false)

const navItems = [
  { path: '/', label: '首页' },
  { path: '/plans', label: '膳养方案' },
  { path: '/ai', label: 'AI 药膳' },
  { path: '/identify', label: '药材识别' },
  { path: '/articles', label: '养生资讯' },
  { path: '/favorites', label: '我的收藏' },
]

const displayName = computed(() => auth.user?.nickname || '登录 / 注册')

function isActive(path: string) {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}
</script>

<template>
  <header class="topbar">
    <div class="container topbar-inner">
      <RouterLink class="brand" to="/" aria-label="膳本云鉴首页">
        <span class="brand-mark">
          <span class="brand-mark-inner">膳</span>
        </span>
        <span>
          <span class="brand-title">膳本云鉴</span>
          <span class="brand-subtitle">AI 药食同源个性化调理与健康管理平台</span>
        </span>
      </RouterLink>

      <nav class="nav" aria-label="主导航">
        <RouterLink v-for="item in navItems" :key="item.path" :class="{ active: isActive(item.path) }" :to="item.path">
          {{ item.label }}
        </RouterLink>
      </nav>

      <div class="header-action">
        <RouterLink v-if="!auth.user" class="primary-btn header-login" to="/login">
          <el-icon><User /></el-icon>
          登录 / 注册
        </RouterLink>
        <button v-else class="primary-btn header-login" type="button">
          <el-icon><Collection /></el-icon>
          {{ displayName }}
        </button>
        <button class="menu-btn" type="button" aria-label="打开导航" @click="menuOpen = !menuOpen">
          <el-icon><Fold /></el-icon>
        </button>
      </div>
    </div>
    <nav v-if="menuOpen" class="mobile-nav container" aria-label="移动导航">
      <RouterLink v-for="item in navItems" :key="item.path" :class="{ active: isActive(item.path) }" :to="item.path" @click="menuOpen = false">
        {{ item.label }}
      </RouterLink>
    </nav>
  </header>
</template>
