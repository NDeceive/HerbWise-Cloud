<script setup lang="ts">
import { onBeforeUnmount, ref } from 'vue'
import { Camera, Check, Picture, UploadFilled } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import ContentImage from '../components/ContentImage.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'

interface RecognitionResult {
  herbName: string
  confidenceLevel: string
  appearanceFeatures: string[]
  effects: string[]
  suitablePeople: string[]
  warnings: string[]
  recommendedRecipes: string[]
}

const mockRecognitionResult: RecognitionResult = {
  herbName: '黄芪',
  confidenceLevel: '较高',
  appearanceFeatures: [
    '切片呈淡黄色至黄白色',
    '表面可见细密放射状纹理',
    '质地较轻，边缘略不规则',
  ],
  effects: ['补气固表', '健脾益气', '利水消肿'],
  suitablePeople: ['气虚乏力人群', '易疲劳人群', '脾胃虚弱人群'],
  warnings: [
    '实热体质慎用',
    '感冒发热期间不建议自行食用',
    '慢性病或正在服药人群请咨询专业医师',
  ],
  recommendedRecipes: ['当归黄芪乌鸡汤', '黄芪党参乌鸡汤'],
}

const historyRecords = [
  { herbName: '黄芪', effect: '补气固表', confidenceLevel: '较高' },
  { herbName: '陈皮', effect: '理气健脾', confidenceLevel: '中等' },
  { herbName: '枸杞', effect: '滋补肝肾', confidenceLevel: '较高' },
]

const selectedFileName = ref('')
const previewUrl = ref('')
const identifying = ref(false)
const result = ref<RecognitionResult | null>(null)
let identifyingTimer: number | undefined

function handleFile(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return

  selectedFileName.value = file.name
  result.value = null
  identifying.value = false
  if (identifyingTimer) window.clearTimeout(identifyingTimer)

  if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
  previewUrl.value = URL.createObjectURL(file)
}

function startRecognition() {
  if (!selectedFileName.value || identifying.value) return

  identifying.value = true
  result.value = null

  if (identifyingTimer) window.clearTimeout(identifyingTimer)
  identifyingTimer = window.setTimeout(() => {
    result.value = mockRecognitionResult
    identifying.value = false
  }, 900)
}

onBeforeUnmount(() => {
  if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
  if (identifyingTimer) window.clearTimeout(identifyingTimer)
})
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
              <span v-if="!previewUrl" class="identify-upload-icon">
                <el-icon><UploadFilled /></el-icon>
              </span>
              <span v-else class="identify-image-preview">
                <img :src="previewUrl" :alt="selectedFileName" />
              </span>
              <strong>{{ selectedFileName || '选择或拖拽药材图片' }}</strong>
              <small>{{ previewUrl ? '已生成本地预览，可开始识别' : '建议上传清晰、无文字遮挡的中药材或食材图片' }}</small>
            </label>
            <div class="identify-actions">
              <button class="primary-btn big-btn" type="button" :disabled="!selectedFileName || identifying" @click="startRecognition">
                <el-icon><Camera /></el-icon>
                {{ identifying ? '识别中...' : '开始识别' }}
              </button>
            </div>
          </section>

          <section v-if="!result" class="section-card">
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

          <section v-else class="section-card identify-result-card">
            <div class="section-head">
              <div>
                <h2 class="section-title">识别结果</h2>
                <p class="muted">以下为前端演示用模拟结果，用于展示药材识别后的信息组织方式。</p>
              </div>
              <span class="confidence-pill">
                <el-icon><Check /></el-icon>
                {{ result.confidenceLevel }}
              </span>
            </div>

            <div class="result-summary">
              <span>药材名称</span>
              <strong>{{ result.herbName }}</strong>
              <small>识别置信度：{{ result.confidenceLevel }}</small>
            </div>

            <div class="identify-result-grid">
              <article>
                <h3>外观特征</h3>
                <ul>
                  <li v-for="item in result.appearanceFeatures" :key="item">{{ item }}</li>
                </ul>
              </article>
              <article>
                <h3>主要功效</h3>
                <div class="result-tags">
                  <span v-for="item in result.effects" :key="item">{{ item }}</span>
                </div>
              </article>
              <article>
                <h3>适用人群</h3>
                <div class="result-tags">
                  <span v-for="item in result.suitablePeople" :key="item">{{ item }}</span>
                </div>
              </article>
              <article class="warning-result">
                <h3>注意事项</h3>
                <ul>
                  <li v-for="item in result.warnings" :key="item">{{ item }}</li>
                </ul>
              </article>
            </div>

            <div class="recipe-recommend-block">
              <h3>相关推荐药膳</h3>
              <div class="recipe-recommend-list">
                <RouterLink
                  v-for="recipe in result.recommendedRecipes"
                  :key="recipe"
                  class="recipe-recommend-card"
                  to="/plans?category=补气养血"
                >
                  <span>{{ recipe }}</span>
                  <small>查看补气养血方案</small>
                </RouterLink>
              </div>
            </div>
          </section>
        </div>

        <aside class="side-stack">
          <section class="side-card history-card">
            <div class="side-title">
              <h2>最近识别记录</h2>
            </div>
            <div class="history-list">
              <article v-for="record in historyRecords" :key="record.herbName" class="history-item">
                <strong>{{ record.herbName }}</strong>
                <span>{{ record.effect }}</span>
                <small>{{ record.confidenceLevel }}</small>
              </article>
            </div>
          </section>

          <section class="side-card safety-card">
            <div class="side-title">
              <h2>安全提示</h2>
            </div>
            <p class="identify-disclaimer">
              AI 药材识别结果仅供健康管理、学习和辅助参考，不能替代专业药师鉴定、医疗诊断或用药建议。如涉及疾病治疗、孕期、慢性病、过敏体质或正在服药，请咨询专业医师。
            </p>
          </section>
        </aside>
      </div>
    </div>
  </AppShell>
</template>
