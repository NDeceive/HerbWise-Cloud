<script setup lang="ts">
import { Collection, DocumentChecked, FirstAidKit, MagicStick, Warning } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import VisualBlock from '../components/VisualBlock.vue'

const flow = [
  { title: '填写健康档案', text: '录入基本信息、体质症状、饮食习惯。', icon: DocumentChecked },
  { title: 'AI 分析体质', text: '结合中医体质与现代营养学生成画像。', icon: MagicStick },
  { title: '生成药膳方案', text: '输出推荐理由、配方、做法与忌口建议。', icon: FirstAidKit },
  { title: '收藏/分享/咨询', text: '保存方案，后续查看或咨询专业医师。', icon: Collection },
]
</script>

<template>
  <AppShell>
    <div class="container ai-entry-page">
      <section class="ai-entry-hero">
        <div class="ai-entry-image">
          <VisualBlock variant="profile" title="体质辨识" subtitle="AI Herbal Plan" />
        </div>
        <div class="ai-entry-copy">
          <div class="eyebrow">AI 药膳 · 个性化定制</div>
          <h1 class="page-title">AI 智能定制您的专属药膳方案</h1>
          <p>根据您的体质、健康状况和饮食偏好，智能推荐适合您的药膳。</p>
          <RouterLink class="primary-btn big-btn" to="/ai/profile">开始定制</RouterLink>
        </div>
      </section>

      <section class="section-card flow-section">
        <div class="section-head">
          <div>
            <h2 class="section-title">定制流程</h2>
            <p class="muted">从健康档案到推荐方案，完整流程可用于比赛现场演示。</p>
          </div>
        </div>
        <div class="flow-grid">
          <article v-for="(item, index) in flow" :key="item.title" class="flow-card">
            <span class="flow-index">{{ index + 1 }}</span>
            <span class="flow-icon">
              <component :is="item.icon" />
            </span>
            <h3>{{ item.title }}</h3>
            <p>{{ item.text }}</p>
          </article>
        </div>
      </section>

      <section class="notice-card">
        <el-icon><Warning /></el-icon>
        <p>本平台建议仅供参考，不能替代专业医疗诊断。如有慢性疾病、孕期、正在服药或特殊身体情况，请咨询专业医师。</p>
      </section>
    </div>
  </AppShell>
</template>
