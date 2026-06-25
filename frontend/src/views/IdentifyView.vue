<script setup lang="ts">
import { ref } from 'vue'
import { Camera, Picture, UploadFilled } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import ContentImage from '../components/ContentImage.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'

const selectedFile = ref('')

function handleFile(event: Event) {
  const input = event.target as HTMLInputElement
  selectedFile.value = input.files?.[0]?.name || ''
}
</script>

<template>
  <AppShell>
    <div class="container listing-page identify-page">
      <section class="listing-hero">
        <div class="listing-copy">
          <div class="eyebrow">AI 药材识别</div>
          <h1 class="page-title">上传药材图片，辅助识别草本信息</h1>
          <p>用于比赛原型展示的前端识别入口，可承接后续药材图像识别能力。</p>
        </div>
        <div class="listing-hero-image">
          <ContentImage :src="contentImages.herbBasket" alt="中式草本药材与食疗食材" image-class="banner-real-image visual-img">
            <VisualBlock variant="recipe" title="药材识别" subtitle="Herbal ID" />
          </ContentImage>
        </div>
      </section>

      <div class="content-layout listing-layout">
        <div class="main-column">
          <section class="section-card identify-panel">
            <div class="section-head">
              <div>
                <h2 class="section-title">图片上传</h2>
                <p class="muted">支持上传单张药材图片，当前前端保留识别入口与结果展示区域，不新增后端接口。</p>
              </div>
            </div>
            <label class="identify-dropzone">
              <input type="file" accept="image/*" @change="handleFile" />
              <span class="identify-upload-icon">
                <el-icon><UploadFilled /></el-icon>
              </span>
              <strong>{{ selectedFile || '选择或拖拽药材图片' }}</strong>
              <small>建议上传清晰、无文字遮挡的中药材或食材图片</small>
            </label>
          </section>

          <section class="section-card">
            <div class="section-head">
              <div>
                <h2 class="section-title">识别结果预览</h2>
                <p class="muted">后续可展示药材名称、性味归经、常见搭配与禁忌提醒。</p>
              </div>
            </div>
            <div class="identify-preview-grid">
              <article class="identify-preview-card">
                <el-icon><Picture /></el-icon>
                <strong>候选药材</strong>
                <p>枸杞、红枣、陈皮、黄芪等药食同源食材。</p>
              </article>
              <article class="identify-preview-card">
                <el-icon><Camera /></el-icon>
                <strong>识别建议</strong>
                <p>请保持主体完整、光线柔和，并避免包装文字占据画面。</p>
              </article>
            </div>
          </section>
        </div>

        <aside class="side-stack">
          <section class="side-card safety-card">
            <div class="side-title">
              <h2>使用提示</h2>
            </div>
            <ul class="safety-list">
              <li>识别结果仅作草本知识参考。</li>
              <li>不确定药材来源时，请勿自行煎服。</li>
              <li>涉及疾病、用药或特殊体质，请咨询专业医师。</li>
            </ul>
          </section>
        </aside>
      </div>
    </div>
  </AppShell>
</template>
