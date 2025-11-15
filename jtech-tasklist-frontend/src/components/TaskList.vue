<template>
  <div>
    <v-list>
      <v-list-item
        v-for="task in tasks"
        :key="task.id"
        :class="{ 'bg-grey-lighten-4': task.completed }"
      >
        <template #prepend>
          <v-checkbox
            :model-value="task.completed"
            @update:model-value="handleToggle(task)"
          />
        </template>

        <v-list-item-title
          :class="{ 'text-decoration-line-through': task.completed }"
        >
          {{ task.title }}
        </v-list-item-title>
        <v-list-item-subtitle v-if="task.description">
          {{ task.description }}
        </v-list-item-subtitle>

        <template #append>
          <v-btn
            icon="mdi-pencil"
            variant="text"
            size="small"
            @click="$emit('edit', task)"
          />
          <v-btn
            icon="mdi-delete"
            variant="text"
            size="small"
            color="error"
            @click="handleDelete(task)"
          />
        </template>
      </v-list-item>
    </v-list>
  </div>
</template>

<script setup lang="ts">
import { useTasksStore } from '@/stores/tasks'
import type { Task } from '@/services/taskService'

defineProps<{
  tasks: Task[]
}>()

defineEmits<{
  edit: [task: Task]
}>()

const tasksStore = useTasksStore()

async function handleToggle(task: Task) {
  if (task.id) {
    await tasksStore.toggleTask(task.id)
  }
}

async function handleDelete(task: Task) {
  if (task.id && confirm(`Deseja realmente excluir a tarefa "${task.title}"?`)) {
    await tasksStore.deleteTask(task.id)
  }
}
</script>


