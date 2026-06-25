<script setup lang="ts">
import { ref, watch } from 'vue'

const props = withDefaults(defineProps<{
  src?: string
  alt: string
  imageClass?: string
}>(), {
  src: '',
  imageClass: 'content-image visual-img',
})

const failed = ref(false)

watch(
  () => props.src,
  () => {
    failed.value = false
  },
)
</script>

<template>
  <img
    v-if="src && !failed"
    :class="imageClass"
    :src="src"
    :alt="alt"
    @error="failed = true"
  />
  <slot v-else />
</template>
