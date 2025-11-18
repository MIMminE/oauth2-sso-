<template>
  <header class="flex items-center justify-between px-6 py-4 bg-white border-b">
    <div class="font-semibold text-lg">{{ title }}</div>
    <nav class="space-x-4 text-sm text-gray-600">
      <slot name="nav"></slot>
    </nav>
  </header>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'
const props = defineProps<{ title?: string }>()
</script>
