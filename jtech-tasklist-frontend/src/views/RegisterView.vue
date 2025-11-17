<template>
  <div class="register-container">
    <!-- Background with animated gradient -->
    <div class="background-gradient"></div>
    
    <!-- Floating shapes for visual interest -->
    <div class="floating-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>

    <v-container fluid class="fill-height">
      <v-row align="center" justify="center" class="fill-height">
        <!-- Left side - Branding -->
        <v-col cols="12" md="6" class="d-none d-md-flex">
          <div class="branding-section">
            <div class="brand-content">
              <div class="brand-icon">
                <v-avatar size="80" color="white" class="elevation-8">
                  <v-icon size="40" color="primary">mdi-account-plus</v-icon>
                </v-avatar>
              </div>
              
              <h1 class="brand-title">
                <span class="text-gradient">Junte-se a nós</span>
              </h1>
              
              <p class="brand-subtitle">
                Crie sua conta e comece a organizar suas tarefas hoje mesmo
              </p>
              
              <div class="features-list">
                <div class="feature-item">
                  <v-icon color="white" class="me-3">mdi-rocket-launch</v-icon>
                  <span>Comece em segundos</span>
                </div>
                <div class="feature-item">
                  <v-icon color="white" class="me-3">mdi-shield-check</v-icon>
                  <span>Dados seguros e protegidos</span>
                </div>
                <div class="feature-item">
                  <v-icon color="white" class="me-3">mdi-heart</v-icon>
                  <span>Gratuito para sempre</span>
                </div>
              </div>
            </div>
          </div>
        </v-col>

        <!-- Right side - Register Form -->
        <v-col cols="12" md="6" lg="4">
          <div class="register-form-container">
            <v-card class="register-card" elevation="24">
              <!-- Header -->
              <div class="register-header">
                <div class="d-flex align-center justify-center mb-4">
                  <v-avatar size="60" color="primary" class="elevation-4">
                    <v-icon size="30" color="white">mdi-account-plus</v-icon>
                  </v-avatar>
                </div>
                
                <h2 class="text-h4 font-weight-bold text-center mb-2">
                  Criar conta
                </h2>
                
                <p class="text-body-1 text-center text-medium-emphasis mb-6">
                  Preencha os dados para começar
                </p>
              </div>

              <!-- Form -->
              <v-card-text class="px-6 pb-6">
                <v-form ref="form" v-model="valid" @submit.prevent="handleRegister">
                  <v-text-field
                    v-model="name"
                    label="Nome completo"
                    :rules="nameRules"
                    required
                    variant="outlined"
                    density="comfortable"
                    prepend-inner-icon="mdi-account-outline"
                    class="mb-4"
                    @keydown.enter="handleRegister"
                  />
                  
                  <v-text-field
                    v-model="email"
                    label="Email"
                    type="email"
                    :rules="emailRules"
                    required
                    variant="outlined"
                    density="comfortable"
                    prepend-inner-icon="mdi-email-outline"
                    class="mb-4"
                    @keydown.enter="handleRegister"
                  />
                  
                  <v-text-field
                    v-model="password"
                    label="Senha"
                    :type="showPassword ? 'text' : 'password'"
                    :rules="passwordRules"
                    required
                    variant="outlined"
                    density="comfortable"
                    prepend-inner-icon="mdi-lock-outline"
                    :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                    class="mb-4"
                    @click:append-inner="showPassword = !showPassword"
                    @keydown.enter="handleRegister"
                  />
                  
                  <v-text-field
                    v-model="confirmPassword"
                    label="Confirmar senha"
                    :type="showConfirmPassword ? 'text' : 'password'"
                    :rules="confirmPasswordRules"
                    required
                    variant="outlined"
                    density="comfortable"
                    prepend-inner-icon="mdi-lock-check-outline"
                    :append-inner-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
                    class="mb-4"
                    @click:append-inner="showConfirmPassword = !showConfirmPassword"
                    @keydown.enter="handleRegister"
                  />

                  <v-alert 
                    v-if="authStore.error" 
                    type="error" 
                    variant="tonal"
                    class="mb-4"
                  >
                    {{ authStore.error }}
                  </v-alert>

                  <v-btn
                    color="primary"
                    size="large"
                    block
                    variant="elevated"
                    :loading="authStore.isLoading"
                    :disabled="!valid"
                    type="submit"
                    class="text-none mb-4"
                    @click="handleRegister"
                  >
                    <v-icon start>mdi-account-plus</v-icon>
                    Criar conta
                  </v-btn>
                </v-form>

                <!-- Divider -->
                <div class="text-center my-4">
                  <v-divider class="mb-4"></v-divider>
                  <span class="text-caption text-medium-emphasis">
                    Já tem uma conta?
                  </span>
                </div>

                <!-- Login Link -->
                <v-btn
                  variant="outlined"
                  size="large"
                  block
                  class="text-none"
                  to="/login"
                >
                  <v-icon start>mdi-login</v-icon>
                  Fazer login
                </v-btn>
              </v-card-text>
            </v-card>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
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
const showPassword = ref(false)
const showConfirmPassword = ref(false)

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

<style scoped>
.register-container {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
}

/* Animated gradient background */
.background-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    135deg,
    #667eea 0%,
    #764ba2 25%,
    #6366f1 50%,
    #8b5cf6 75%,
    #06b6d4 100%
  );
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
}

@keyframes gradientShift {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* Floating shapes */
.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  animation: float 20s infinite linear;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 120px;
  height: 120px;
  top: 60%;
  left: 80%;
  animation-delay: -7s;
}

.shape-3 {
  width: 60px;
  height: 60px;
  top: 80%;
  left: 20%;
  animation-delay: -14s;
}

@keyframes float {
  0% { transform: translateY(0px) rotate(0deg); }
  33% { transform: translateY(-30px) rotate(120deg); }
  66% { transform: translateY(30px) rotate(240deg); }
  100% { transform: translateY(0px) rotate(360deg); }
}

/* Branding section */
.branding-section {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 2rem;
}

.brand-content {
  text-align: center;
  color: white;
  max-width: 400px;
}

.brand-icon {
  margin-bottom: 2rem;
}

.brand-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.1;
}

.text-gradient {
  background: linear-gradient(45deg, #ffffff, #f0f9ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-subtitle {
  font-size: 1.25rem;
  margin-bottom: 3rem;
  opacity: 0.9;
  line-height: 1.6;
}

.features-list {
  text-align: left;
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  font-size: 1.1rem;
  opacity: 0.9;
}

/* Register form */
.register-form-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 2rem 1rem;
}

.register-card {
  width: 100%;
  max-width: 450px;
  border-radius: 24px !important;
  backdrop-filter: blur(20px);
  background: rgba(255, 255, 255, 0.95) !important;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.register-header {
  padding: 2rem 2rem 0;
  text-align: center;
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .brand-title {
    font-size: 2.5rem;
  }
  
  .brand-subtitle {
    font-size: 1.1rem;
  }
  
  .register-form-container {
    min-height: 100vh;
  }
}

@media (max-width: 600px) {
  .register-form-container {
    padding: 1rem 0.5rem;
  }
  
  .register-card {
    margin: 1rem;
    border-radius: 16px !important;
  }
  
  .register-header {
    padding: 1.5rem 1.5rem 0;
  }
}

/* Dark theme adjustments */
.v-theme--dark .register-card {
  background: rgba(30, 41, 59, 0.95) !important;
  border: 1px solid rgba(255, 255, 255, 0.1);
}
</style>

