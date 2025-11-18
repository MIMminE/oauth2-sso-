<template>
  <div class="space-y-2">
    <div class="flex items-center gap-2">
      <button class="px-3 py-1 rounded-md bg-indigo-600 text-white disabled:opacity-60" @click="callApi" :disabled="loading">{{ loading ? '로딩...' : label }}</button>
      <button class="px-3 py-1 rounded-md border" @click="clear">초기화</button>
    </div>
    <div>
      <div v-if="error" class="text-red-600 text-sm">에러: {{ error }}</div>
      <pre v-if="result" class="bg-gray-50 p-3 rounded-md overflow-auto text-sm">{{ pretty }}</pre>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import { defineEmits, defineProps } from 'vue'

const props = defineProps<{ endpoint?: string, label?: string }>()
const emit = defineEmits<{
  (e: 'success', data: any): void
  (e: 'error', err: any): void
}>()

const endpoint = props.endpoint ?? '/test'
const label = props.label ?? `API 호출: ${endpoint}`
const loading = ref(false)
const result = ref<any>(null)
const error = ref<string | null>(null)

async function callApi() {
  loading.value = true
  error.value = null
  result.value = null
  try {
    const res = await axios.get(endpoint)
    result.value = res.data
    emit('success', res.data)
  } catch (e: any) {
    let msg = ''
    if (e.response) msg = `Error ${e.response.status}: ${e.response.statusText}`
    else if (e.message) msg = e.message
    else msg = String(e)
    error.value = msg
    emit('error', { message: msg })
  } finally {
    loading.value = false
  }
}

function clear() {
  result.value = null
  error.value = null
}

const pretty = computed(() => (result.value ? JSON.stringify(result.value, null, 2) : ''))
</script>
