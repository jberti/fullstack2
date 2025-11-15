<template>
  <v-container fluid class="fill-height">
    <v-row align="center" justify="center" class="fill-height">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Registro</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form ref="form" v-model="valid" @submit.prevent="handleRegister">
              <v-text-field
                v-model="name"
                label="Nome"
                :rules="nameRules"
                required
                prepend-inner-icon="mdi-account"
              />
              <v-text-field
                v-model="email"
                label="Email"
                type="email"
                :rules="emailRules"
                required
                prepend-inner-icon="mdi-email"
              />
              <v-text-field
                v-model="password"
                label="Senha"
                type="password"
                :rules="passwordRules"
                required
                prepend-inner-icon="mdi-lock"
              />
              <v-text-field
                v-model="confirmPassword"
                label="Confirmar Senha"
                type="password"
                :rules="confirmPasswordRules"
                required
                prepend-inner-icon="mdi-lock-check"
              />
              <v-alert v-if="authStore.error" type="error" class="mt-4">
                {{ authStore.error }}
              </v-alert>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn color="primary" :loading="authStore.isLoading" @click="handleRegister">
              Registrar
            </v-btn>
          </v-card-actions>
          <v-card-actions>
            <v-spacer />
            <v-btn variant="text" to="/login">
              Já tem conta? Faça login
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const form = ref()
const valid = ref(false)
const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const nameRules = [
  (v: string) => !!v || 'Nome é obrigatório',
  (v: string) => (v && v.length >= 3) || 'Nome deve ter pelo menos 3 caracteres',
]

const emailRules = [
  (v: string) => !!v || 'Email é obrigatório',
  (v: string) => /.+@.+\..+/.test(v) || 'Email deve ser válido',
]

const passwordRules = [
  (v: string) => !!v || 'Senha é obrigatória',
  (v: string) => (v && v.length >= 6) || 'Senha deve ter pelo menos 6 caracteres',
]

const confirmPasswordRules = computed(() => [
  (v: string) => !!v || 'Confirmação de senha é obrigatória',
  (v: string) => v === password.value || 'Senhas não coincidem',
])

async function handleRegister() {
  const { valid: isValid } = await form.value.validate()
  if (!isValid) return

  try {
    authStore.clearError()
    await authStore.register({
      name: name.value,
      email: email.value,
      password: password.value,
    })
    router.push('/tasks')
  } catch (error) {
    // Error já é tratado no store
  }
}
</script>

