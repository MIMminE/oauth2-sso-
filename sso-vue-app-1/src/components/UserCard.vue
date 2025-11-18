<template>
  <div v-if="user" class="border rounded-md p-4 max-w-md bg-white shadow-sm">
    <div class="text-sm text-gray-500">현재 사용자</div>
    <div class="mt-1 text-lg font-semibold">{{ user.name }}</div>
    <div class="mt-3 text-sm text-gray-700">
      <div class="font-medium">권한</div>
      <ul class="list-disc list-inside mt-1">
        <li v-for="(a, i) in user.authorities" :key="i">{{ a }}</li>
      </ul>
    </div>
  </div>
  <div v-else class="text-gray-500">사용자 정보 없음</div>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'
const props = defineProps<{ user?: { name?: string, authorities?: string[] } }>()
</script>
