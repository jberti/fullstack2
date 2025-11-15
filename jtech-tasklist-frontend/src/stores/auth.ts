import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import authService, { type User } from '@/services/authService'
import type { RegisterRequest, LoginRequest } from '@/services/authService'

export const useAuthStore = defineStore(
  'auth',
  () => {
    const user = ref<User | null>(null)
    const token = ref<string | null>(localStorage.getItem('token'))
    const isLoading = ref(false)
    const error = ref<string | null>(null)

    const isAuthenticated = computed(() => !!token.value && !!user.value)

    async function register(data: RegisterRequest) {
      isLoading.value = true
      error.value = null
      try {
        const response = await authService.register(data)
        token.value = response.token
        authService.setToken(response.token)
        authService.setRefreshToken(response.refreshToken)
        
        // Decodificar token para obter informações do usuário
        const payload = JSON.parse(atob(response.token.split('.')[1]))
        user.value = {
          id: payload.sub,
          name: data.name,
          email: data.email,
        }
        return response
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Erro ao registrar usuário'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    async function login(data: LoginRequest) {
      isLoading.value = true
      error.value = null
      try {
        const response = await authService.login(data)
        token.value = response.token
        authService.setToken(response.token)
        authService.setRefreshToken(response.refreshToken)
        
        // Decodificar token para obter informações do usuário
        const payload = JSON.parse(atob(response.token.split('.')[1]))
        user.value = {
          id: payload.sub,
          email: data.email,
          name: '', // O nome não vem no login, precisaríamos buscar do backend
        }
        return response
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Email ou senha inválidos'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    function logout() {
      authService.logout()
      user.value = null
      token.value = null
      error.value = null
    }

    function clearError() {
      error.value = null
    }

    return {
      user,
      token,
      isLoading,
      error,
      isAuthenticated,
      register,
      login,
      logout,
      clearError,
    }
  },
  {
    persist: true,
  }
)


