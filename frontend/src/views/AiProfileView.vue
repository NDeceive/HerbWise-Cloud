<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Calendar, Check, DocumentChecked, Lock, MagicStick, Warning } from '@element-plus/icons-vue'
import AppShell from '../components/AppShell.vue'
import VisualBlock from '../components/VisualBlock.vue'
import { contentImages } from '../data/imageMap'
import { api } from '../services/api'
import type { HealthProfilePayload } from '../types'

const router = useRouter()
const step = ref(1)
const steps = ['基本信息', '健康状况', '生活习惯', '完成']
const isSubmitting = ref(false)

const options = {
  constitutions: ['平和质', '气虚质', '阳虚质', '阴虚质', '痰湿质', '湿热质', '血瘀质', '气郁质', '特禀质'],
  symptoms: ['失眠多梦', '疲劳乏力', '消化不良', '食欲不振', '慢性咳嗽', '容易上火', '便秘', '腹泻', '关节酸痛', '其他'],
  diseases: ['高血压', '糖尿病', '高血脂', '胃炎/胃溃疡', '脂肪肝', '痛风', '心脏病', '哮喘', '关节炎', '甲状腺疾病', '无慢性病'],
  tastes: ['清淡', '微甜', '偏咸', '偏辣', '无偏好'],
  diet: ['规律', '偶尔不规律', '经常不规律'],
  allergies: ['海鲜', '牛奶', '鸡蛋', '花生', '坚果', '大豆', '小麦', '无', '其他'],
  goals: ['增强免疫力', '改善睡眠', '调理脾胃', '减脂塑形', '缓解疲劳', '美容养颜', '补气养血', '调理三高', '改善便秘'],
}

const form = reactive<HealthProfilePayload>({
  name: '张女士',
  gender: '女',
  birthDate: '',
  age: 28,
  height: 165,
  weight: 55,
  constitutionTypes: ['气虚质'],
  symptoms: ['疲劳乏力'],
  diseases: ['无慢性病'],
  tastePreference: '清淡',
  dietRegularity: '规律',
  allergies: ['无'],
  sleepTime: '23:00',
  wakeTime: '07:00',
  waterIntake: '1500ml',
  goals: ['补气养血', '改善睡眠'],
  extraNote: '',
})

const progressWidth = computed(() => `${((step.value - 1) / 3) * 100}%`)

function toggle(list: string[], value: string) {
  const index = list.indexOf(value)
  index >= 0 ? list.splice(index, 1) : list.push(value)
}

async function submit() {
  isSubmitting.value = true
  await api.saveHealthProfile(form)
  const result = await api.recommend(form)
  isSubmitting.value = false
  router.push({ path: '/ai/result', query: { id: result.id } })
}

function hideBrokenImage(event: Event) {
  const image = event.target as HTMLImageElement
  image.hidden = true
}
</script>

<template>
  <AppShell>
    <div class="container profile-page">
      <div class="breadcrumb">首页 <span>/</span> AI 药膳 <span>/</span> AI 药膳健康问卷</div>

      <div class="profile-steps">
        <div class="steps-track"><span :style="{ width: progressWidth }" /></div>
        <button v-for="(item, index) in steps" :key="item" class="profile-step" :class="{ active: step === index + 1, done: step > index + 1 }" type="button" @click="step = index + 1">
          <span>{{ index + 1 }}</span>
          {{ item }}
        </button>
      </div>

      <div class="profile-layout">
        <aside class="profile-aside">
          <div class="profile-aside-hero">
            <VisualBlock variant="profile" title="健康问卷" subtitle="AI 药膳档案" />
            <img class="content-image visual-img" :src="contentImages.herbBasket" alt="草本食材与药膳健康档案" @error="hideBrokenImage" />
          </div>
          <div class="profile-aside-copy">
            <h1>AI 药膳健康问卷</h1>
            <p>填写健康信息，为您生成更贴合体质的药膳方案。</p>
          </div>
          <div class="tip-list">
            <article>
              <el-icon><DocumentChecked /></el-icon>
              <div><strong>请如实填写</strong><p>每一项信息都会帮助系统理解您的体质与调理需求。</p></div>
            </article>
            <article>
              <el-icon><Lock /></el-icon>
              <div><strong>仅用于生成方案</strong><p>健康档案服务于本次个性化推荐演示。</p></div>
            </article>
            <article>
              <el-icon><Calendar /></el-icon>
              <div><strong>建议定期更新</strong><p>身体状态会随时间变化，更新后可获得更合适的建议。</p></div>
            </article>
          </div>
          <div class="disclaimer">
            <el-icon><Warning /></el-icon>
            本平台提供的建议仅供参考，不能替代专业医疗诊断、治疗或建议。
          </div>
        </aside>

        <section class="questionnaire-card">
          <template v-if="step === 1">
            <div class="question-block">
              <h2><span>1</span> 基本资料</h2>
              <div class="form-grid">
                <label class="field"><span>姓名</span><input v-model="form.name" placeholder="请输入姓名" /></label>
                <label class="field"><span>性别</span><select v-model="form.gender"><option>男</option><option>女</option></select></label>
                <label class="field"><span>出生日期</span><input v-model="form.birthDate" type="date" /></label>
                <label class="field"><span>年龄</span><input v-model.number="form.age" type="number" placeholder="请输入年龄" /></label>
                <label class="field"><span>身高 cm</span><input v-model.number="form.height" type="number" /></label>
                <label class="field"><span>体重 kg</span><input v-model.number="form.weight" type="number" /></label>
              </div>
            </div>
          </template>

          <template v-else-if="step === 2">
            <div class="question-block">
              <h2><span>2</span> 健康状况</h2>
              <div class="choice-section">
                <h3>体质类型 <small>可多选</small></h3>
                <div class="choice-grid">
                  <button v-for="item in options.constitutions" :key="item" class="choice" :class="{ active: form.constitutionTypes.includes(item) }" type="button" @click="toggle(form.constitutionTypes, item)">{{ item }}</button>
                </div>
              </div>
              <div class="choice-section">
                <h3>主要健康问题 <small>可多选</small></h3>
                <div class="choice-grid">
                  <button v-for="item in options.symptoms" :key="item" class="choice" :class="{ active: form.symptoms.includes(item) }" type="button" @click="toggle(form.symptoms, item)">{{ item }}</button>
                </div>
              </div>
              <div class="choice-section">
                <h3>慢性病 / 病史 <small>可多选</small></h3>
                <div class="choice-grid">
                  <button v-for="item in options.diseases" :key="item" class="choice" :class="{ active: form.diseases.includes(item) }" type="button" @click="toggle(form.diseases, item)">{{ item }}</button>
                </div>
              </div>
              <label class="field full-field"><span>其他补充说明</span><textarea v-model="form.extraNote" placeholder="如有其他情况，请补充说明（选填）" /></label>
            </div>
          </template>

          <template v-else-if="step === 3">
            <div class="question-block">
              <h2><span>3</span> 生活习惯</h2>
              <div class="form-grid">
                <label class="field"><span>口味偏好</span><select v-model="form.tastePreference"><option v-for="item in options.tastes" :key="item">{{ item }}</option></select></label>
                <label class="field"><span>饮食规律</span><select v-model="form.dietRegularity"><option v-for="item in options.diet" :key="item">{{ item }}</option></select></label>
                <label class="field"><span>入睡时间</span><input v-model="form.sleepTime" type="time" /></label>
                <label class="field"><span>起床时间</span><input v-model="form.wakeTime" type="time" /></label>
                <label class="field"><span>平日饮水量</span><input v-model="form.waterIntake" placeholder="例如 1500ml" /></label>
              </div>
              <div class="choice-section">
                <h3>忌口食物</h3>
                <div class="choice-grid">
                  <button v-for="item in options.allergies" :key="item" class="choice" :class="{ active: form.allergies.includes(item) }" type="button" @click="toggle(form.allergies, item)">{{ item }}</button>
                </div>
              </div>
              <div class="choice-section">
                <h3>调理目标 <small>可多选</small></h3>
                <div class="choice-grid">
                  <button v-for="item in options.goals" :key="item" class="choice" :class="{ active: form.goals.includes(item) }" type="button" @click="toggle(form.goals, item)">{{ item }}</button>
                </div>
              </div>
            </div>
          </template>

          <template v-else>
            <div class="complete-panel">
              <span class="complete-icon">
                <el-icon><Check /></el-icon>
              </span>
              <h2>健康档案创建完成</h2>
              <p>正在为您生成个性化药膳方案，请稍候……</p>
              <el-icon class="complete-magic"><MagicStick /></el-icon>
            </div>
          </template>

          <div class="question-actions">
            <button class="ghost-btn" type="button" :disabled="step === 1 || isSubmitting" @click="step -= 1">上一步</button>
            <button v-if="step < 4" class="primary-btn" type="button" @click="step += 1">下一步</button>
            <button v-else class="primary-btn" type="button" :disabled="isSubmitting" @click="submit">
              {{ isSubmitting ? '生成中...' : '生成专属药膳方案' }}
            </button>
          </div>
        </section>
      </div>
    </div>
  </AppShell>
</template>
