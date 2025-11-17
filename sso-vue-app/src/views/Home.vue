<template>
  <div>
    <Header title="SSO Vue App">
      <template #nav>
        <a href="#/" class="text-gray-600 hover:text-gray-800">Home</a>
        <a href="#/profile" class="ml-4 text-gray-600 hover:text-gray-800">Profile</a>
      </template>
    </Header>

    <main class="p-6 max-w-4xl mx-auto">
      <p class="text-gray-700 mb-4">{{ message }}</p>
      <GoogleLabel />

      <div class="mt-4">
        <ApiTester @success="onSuccess" @error="onError" />
      </div>

      <div class="mt-6">
        <UserCard :user="user" />
      </div>
    </main>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import Header from '../components/Header.vue'
import ApiTester from '../components/ApiTester.vue'
import UserCard from '../components/UserCard.vue'
import GoogleLabel from '../components/GoogleLabel.vue'

const message = ref('Developer')
const user = ref<any>(null)

function onSuccess(data: any) {
  user.value = data
}

function onError(err: any) {
  user.value = null
  console.error('API error', err)
}
</script>
