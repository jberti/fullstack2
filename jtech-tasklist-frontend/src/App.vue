<template>
  <v-app>
    <!-- Modern App Bar with Glass Effect -->
    <v-app-bar 
      v-if="authStore.isAuthenticated" 
      :color="theme.global.current.value.dark ? 'surface' : 'surface'"
      flat
      class="app-bar-glass"
      style="backdrop-filter: blur(10px); -webkit-backdrop-filter: blur(10px);"
    >
      <template #prepend>
        <v-avatar size="32" class="me-3">
          <v-icon color="primary">mdi-check-circle</v-icon>
        </v-avatar>
      </template>
      
      <v-app-bar-title class="font-weight-bold">
        <span class="text-gradient">TaskList</span>
      </v-app-bar-title>
      
      <v-spacer />
      
      <!-- Dark Mode Toggle -->
      <v-btn
        :icon="theme.global.current.value.dark ? 'mdi-weather-sunny' : 'mdi-weather-night'"
        variant="text"
        @click="toggleTheme"
        class="me-2"
      />
      
      <!-- User Menu -->
      <v-menu location="bottom end">
        <template #activator="{ props }">
          <v-btn
            v-bind="props"
            variant="text"
            class="text-none"
          >
            <v-avatar size="32" class="me-2">
              <v-icon>mdi-account-circle</v-icon>
            </v-avatar>
            {{ authStore.user?.email?.split('@')[0] }}
            <v-icon end>mdi-chevron-down</v-icon>
          </v-btn>
        </template>
        
        <v-card min-width="200" class="elevation-8">
          <v-list>
            <v-list-item>
              <v-list-item-title class="text-caption text-medium-emphasis">
                Logado como
              </v-list-item-title>
              <v-list-item-subtitle class="font-weight-medium">
                {{ authStore.user?.email }}
              </v-list-item-subtitle>
            </v-list-item>
            
            <v-divider />
            
            <v-list-item @click="handleLogout" class="text-error">
              <template #prepend>
                <v-icon>mdi-logout</v-icon>
              </template>
              <v-list-item-title>Sair</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-card>
      </v-menu>
    </v-app-bar>

    <!-- Main Content with Smooth Transitions -->
    <v-main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useTheme } from 'vuetify'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const theme = useTheme()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}

function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>

<style scoped>
.text-gradient {
  background: linear-gradient(45deg, #6366F1, #8B5CF6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.app-bar-glass {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.main-content {
  background: linear-gradient(135deg, 
    rgba(99, 102, 241, 0.05) 0%, 
    rgba(139, 92, 246, 0.05) 100%);
  min-height: 100vh;
}

/* Page Transitions */
.page-enter-active,
.page-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>
