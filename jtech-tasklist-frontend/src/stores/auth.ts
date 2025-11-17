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

    const isAuthenticated = computed(() => !!token.value)

    // Initialize user from token if available
    async function initializeAuth() {
      if (token.value && !user.value) {
        try {
          // Decode token to get user info or make a request to get user profile
          // For now, we'll assume the token is valid and set a basic user
          // In a real app, you'd make a request to /me endpoint
          const tokenPayload = JSON.parse(atob(token.value.split('.')[1]))
          user.value = {
            id: tokenPayload.sub,
            name: tokenPayload.name || 'Usuário',
            email: tokenPayload.email || 'user@example.com'
          }
        } catch (err) {
          // Token is invalid, clear it
          logout()
        }
      }
    }

    async function register(data: RegisterRequest) {
      isLoading.value = true
      error.value = null
      try {
        const response = await authService.register(data)
        token.value = response.token
        authService.setToken(response.token)
        user.value = response.user
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
        user.value = response.user
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
      initializeAuth,
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


