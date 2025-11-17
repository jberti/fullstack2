<template>
  <v-dialog v-model="dialog" max-width="500px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center">
        <v-icon class="me-3" :color="form.color">mdi-format-list-bulleted</v-icon>
        <span>{{ isEditing ? 'Editar Lista' : 'Nova Lista' }}</span>
      </v-card-title>

      <v-card-text>
        <v-form ref="formRef" v-model="isFormValid">
          <v-text-field
            v-model="form.name"
            label="Nome da Lista"
            :rules="nameRules"
            variant="outlined"
            density="comfortable"
            prepend-inner-icon="mdi-format-list-bulleted"
            required
            autofocus
          />

          <v-textarea
            v-model="form.description"
            label="Descrição (opcional)"
            variant="outlined"
            density="comfortable"
            prepend-inner-icon="mdi-text"
            rows="3"
            no-resize
          />

          <v-row>
            <v-col cols="12">
              <v-label class="text-body-2 text-medium-emphasis mb-2">
                Cor da Lista
              </v-label>
              <div class="d-flex flex-wrap ga-2">
                <v-btn
                  v-for="color in colorOptions"
                  :key="color.value"
                  :color="color.value"
                  :variant="form.color === color.value ? 'elevated' : 'tonal'"
                  size="small"
                  icon
                  @click="form.color = color.value"
                >
                  <v-icon v-if="form.color === color.value">mdi-check</v-icon>
                </v-btn>
              </div>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn
          variant="text"
          @click="closeDialog"
          :disabled="isLoading"
        >
          Cancelar
        </v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="saveTasklist"
          :loading="isLoading"
          :disabled="!isFormValid"
        >
          {{ isEditing ? 'Atualizar' : 'Criar' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useTasklistsStore } from '@/stores/tasklists'
import type { Tasklist } from '@/services/tasklistService'

interface Props {
  modelValue: boolean
  tasklist?: Tasklist | null
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'saved'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const tasklistsStore = useTasklistsStore()
const formRef = ref()
const isFormValid = ref(false)
const isLoading = ref(false)

const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const isEditing = computed(() => !!props.tasklist)

const form = ref({
  name: '',
  description: '',
  color: '#1976D2'
})

const colorOptions = [
  { name: 'Azul', value: '#1976D2' },
  { name: 'Verde', value: '#388E3C' },
  { name: 'Roxo', value: '#7B1FA2' },
  { name: 'Laranja', value: '#F57C00' },
  { name: 'Vermelho', value: '#D32F2F' },
  { name: 'Teal', value: '#00796B' },
  { name: 'Indigo', value: '#303F9F' },
  { name: 'Rosa', value: '#C2185B' },
]

const nameRules = [
  (v: string) => !!v || 'Nome é obrigatório',
  (v: string) => (v && v.length >= 1) || 'Nome deve ter pelo menos 1 caractere',
  (v: string) => (v && v.length <= 100) || 'Nome deve ter no máximo 100 caracteres',
]

watch(() => props.tasklist, (tasklist) => {
  if (tasklist) {
    form.value = {
      name: tasklist.name,
      description: tasklist.description || '',
      color: tasklist.color
    }
  } else {
    resetForm()
  }
}, { immediate: true })

watch(() => props.modelValue, (isOpen) => {
  if (!isOpen) {
    resetForm()
  }
})

function resetForm() {
  form.value = {
    name: '',
    description: '',
    color: '#1976D2'
  }
  if (formRef.value) {
    formRef.value.resetValidation()
  }
}

async function saveTasklist() {
  if (!isFormValid.value) return

  isLoading.value = true

  try {
    if (isEditing.value && props.tasklist) {
      await tasklistsStore.updateTasklist(props.tasklist.id, {
        name: form.value.name,
        description: form.value.description || undefined,
        color: form.value.color
      })
    } else {
      await tasklistsStore.createTasklist({
        name: form.value.name,
        description: form.value.description || undefined,
        color: form.value.color
      })
    }

    emit('saved')
    closeDialog()
  } catch (error) {
    // Error is handled by the store
  } finally {
    isLoading.value = false
  }
}

function closeDialog() {
  dialog.value = false
}
</script>

<style scoped>
.v-btn--icon {
  width: 40px;
  height: 40px;
}
</style>