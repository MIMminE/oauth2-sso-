<template>
  <button @click="start">로그인 (OAuth)</button>
</template>

<script lang="ts" setup>
function start() {
  // 외부 URL로 리다이렉트
  window.location.assign('http://localhost:8080/test/1')
}
</script>