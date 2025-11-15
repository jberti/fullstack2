<template>
  <v-dialog :model-value="modelValue" max-width="600" @update:model-value="$emit('update:modelValue', $event)">
    <v-card>
      <v-card-title>
        {{ task ? 'Editar Tarefa' : 'Nova Tarefa' }}
      </v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid">
          <v-text-field
            v-model="title"
            label="Título"
            :rules="titleRules"
            required
            prepend-inner-icon="mdi-format-title"
          />
          <v-textarea
            v-model="description"
            label="Descrição"
            rows="3"
            prepend-inner-icon="mdi-text"
          />
          <v-checkbox
            v-model="completed"
            label="Concluída"
          />
        </v-form>
        <v-alert v-if="tasksStore.error" type="error" class="mt-4">
          {{ tasksStore.error }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="$emit('update:modelValue', false)">
          Cancelar
        </v-btn>
        <v-btn
          color="primary"
          :loading="tasksStore.isLoading"
          :disabled="!valid"
          @click="handleSave"
        >
          Salvar
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useTasksStore } from '@/stores/tasks'
import type { Task } from '@/services/taskService'

const props = defineProps<{
  modelValue: boolean
  task?: Task | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'task-saved': []
}>()

const tasksStore = useTasksStore()
const form = ref()
const valid = ref(false)
const title = ref('')
const description = ref('')
const completed = ref(false)

const titleRules = [
  (v: string) => !!v || 'Título é obrigatório',
  (v: string) => (v && v.length <= 200) || 'Título deve ter no máximo 200 caracteres',
]

watch(() => props.task, (newTask) => {
  if (newTask) {
    title.value = newTask.title
    description.value = newTask.description || ''
    completed.value = newTask.completed
  } else {
    title.value = ''
    description.value = ''
    completed.value = false
  }
}, { immediate: true })

watch(() => props.modelValue, (isOpen) => {
  if (!isOpen) {
    title.value = ''
    description.value = ''
    completed.value = false
    tasksStore.clearError()
  }
})

async function handleSave() {
  const { valid: isValid } = await form.value.validate()
  if (!isValid) return

  try {
    tasksStore.clearError()
    if (props.task?.id) {
      await tasksStore.updateTask(props.task.id, {
        title: title.value,
        description: description.value,
        completed: completed.value,
      })
    } else {
      await tasksStore.createTask({
        title: title.value,
        description: description.value,
        completed: completed.value,
      })
    }
    emit('update:modelValue', false)
    emit('task-saved')
  } catch (error) {
    // Error já é tratado no store
  }
}
</script>

