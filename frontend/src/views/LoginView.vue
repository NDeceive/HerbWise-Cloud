<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Cellphone, ChatDotRound, Collection, Document, Lock, User } from '@element-plus/icons-vue'
import VisualBlock from '../components/VisualBlock.vue'
import { api } from '../services/api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const mode = ref<'login' | 'register'>('login')
const phone = ref('13800000000')
const code = ref('123456')
const agreed = ref(true)
const codeSent = ref(false)

async function submit() {
  auth.user =
    mode.value === 'login'
      ? await api.login({ phone: phone.value, code: code.value })
      : await api.register({ phone: phone.value, code: code.value })
  router.push('/')
}

async function guest() {
  await auth.loginAsGuest()
  router.push('/')
}

async function wechatLogin() {
  auth.user = await api.login({ phone: phone.value || '13800000000', code: code.value || '123456' })
  router.push('/')
}
</script>

<template>
  <div class="login-page">
    <section class="login-visual">
      <RouterLink class="brand login-brand" to="/">
        <span class="brand-mark"><span class="brand-mark-inner">膳</span></span>
        <span>
          <span class="brand-title">智膳坊</span>
          <span class="brand-subtitle">个性化药膳推荐与健康管理平台</span>
        </span>
      </RouterLink>

      <div class="login-copy">
        <div class="eyebrow">药食同源 · 智养有方</div>
        <h1 class="hero-title">药食同源，智养有方</h1>
        <p>根据体质与需求，定制专属药膳方案，吃出健康好状态。</p>
      </div>

      <div class="selling-points">
        <div class="selling-point">
          <span><el-icon><Document /></el-icon></span>
          <strong>个性化推荐</strong>
          <small>AI 定制专属药膳方案</small>
        </div>
        <div class="selling-point">
          <span><el-icon><Collection /></el-icon></span>
          <strong>收藏同步</strong>
          <small>多端同步，随时查看</small>
        </div>
        <div class="selling-point">
          <span><el-icon><Lock /></el-icon></span>
          <strong>健康档案</strong>
          <small>记录体质变化，科学管理</small>
        </div>
      </div>

      <div class="login-photo">
        <VisualBlock variant="hero" title="温润药膳" subtitle="Herbal Cuisine" />
      </div>
    </section>

    <section class="login-panel">
      <RouterLink class="return-home" to="/">返回首页</RouterLink>
      <div class="login-box card">
        <div class="login-tabs">
          <button :class="{ active: mode === 'login' }" type="button" @click="mode = 'login'">登录</button>
          <button :class="{ active: mode === 'register' }" type="button" @click="mode = 'register'">注册</button>
        </div>
        <p class="muted login-hint">{{ mode === 'login' ? '登录智膳坊，开启您的个性化健康之旅' : '注册账号，建立您的专属健康档案' }}</p>

        <label class="form-line">
          <span>手机号</span>
          <div class="input-with-icon">
            <el-icon><Cellphone /></el-icon>
            <input v-model="phone" placeholder="请输入手机号" />
          </div>
        </label>

        <label class="form-line">
          <span>验证码</span>
          <div class="code-row">
            <div class="input-with-icon">
              <el-icon><Lock /></el-icon>
              <input v-model="code" placeholder="请输入验证码" />
            </div>
            <button class="code-btn" type="button" @click="codeSent = true">{{ codeSent ? '已发送' : '获取验证码' }}</button>
          </div>
        </label>

        <label class="agreement">
          <input v-model="agreed" type="checkbox" />
          <span>我已阅读并同意《用户服务协议》和《隐私政策》</span>
        </label>

        <button class="primary-btn block-btn big-btn" type="button" :disabled="!agreed" @click="submit">
          <el-icon><User /></el-icon>
          {{ mode === 'login' ? '登录智膳坊' : '注册并登录' }}
        </button>

        <div class="divider"><span>或</span></div>

        <button class="wechat-btn" type="button" @click="wechatLogin">
          <el-icon><ChatDotRound /></el-icon>
          微信快捷登录
        </button>
        <button class="guest-btn" type="button" @click="guest">游客登录</button>
      </div>
    </section>
  </div>
</template>
