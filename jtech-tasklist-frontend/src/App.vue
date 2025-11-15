<template>
  <v-app>
    <v-app-bar v-if="authStore.isAuthenticated" color="primary" dark>
      <v-app-bar-title>TaskList</v-app-bar-title>
      <v-spacer />
      <v-menu>
        <template #activator="{ props }">
          <v-btn icon="mdi-account" v-bind="props" />
        </template>
        <v-list>
          <v-list-item>
            <v-list-item-title>{{ authStore.user?.email }}</v-list-item-title>
          </v-list-item>
          <v-divider />
          <v-list-item @click="handleLogout">
            <v-list-item-title>Sair</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>
