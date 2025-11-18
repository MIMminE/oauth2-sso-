<template>
  <div class="google-wrapper my-3">
    <div
      class="inline-flex items-center gap-2 px-3 py-1 rounded-md bg-[#4285f4] text-white font-semibold text-sm cursor-pointer select-none"
      role="button"
      tabindex="0"
      @click="openConfirm"
      @keydown.enter.prevent="openConfirm"
    >
      <!-- Google SVG Icon -->
      <svg class="w-4 h-4" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
        <path fill="#EA4335" d="M12 11.5v3.5h5.2C16.6 17.9 14.5 19 12 19c-3.2 0-6-2.6-6-6s2.8-6 6-6c1.7 0 3.2.7 4.3 1.8l2.6-2.6C18.1 3.1 15.2 2 12 2 7 2 3 6 3 11s4 9 9 9c5 0 9-4 9-9 0-.6 0-1-.1-1.5H12z"/>
      </svg>
      구글
    </div>

    <!-- modal -->
    <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40" @click.self="close">
      <div class="bg-white rounded-md shadow-lg p-4 w-[90%] max-w-sm">
        <h3 class="text-lg font-medium mb-2 text-gray-900">로그인 확인</h3>
        <p class="text-sm text-gray-700 mb-4">구글 아이디로 접속을 시도합니다.</p>
        <div class="flex justify-end gap-2">
          <button class="px-3 py-1 rounded-md bg-blue-600 text-white" @click="confirm">확인</button>
          <button class="px-3 py-1 rounded-md border" @click="close">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { defineProps } from 'vue'

const props = defineProps<{ redirectUrl?: string, newWindow?: boolean }>()
const redirectUrl = props.redirectUrl ?? '/oauth2/authorization/google'
const newWindow = props.newWindow ?? false

const show = ref(false)

function openConfirm() {
  show.value = true
}
function close() {
  show.value = false
}
function confirm() {
  close()
  if (newWindow) window.open(redirectUrl, '_blank', 'noopener')
  else window.location.href = redirectUrl
}
</script>
