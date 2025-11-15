<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title class="d-flex justify-space-between align-center">
            <span>Minhas Tarefas</span>
            <v-btn color="primary" @click="dialog = true">
              <v-icon start>mdi-plus</v-icon>
              Nova Tarefa
            </v-btn>
          </v-card-title>
          <v-card-text>
            <v-alert v-if="tasksStore.error" type="error" class="mb-4" @click="tasksStore.clearError()">
              {{ tasksStore.error }}
            </v-alert>

            <v-progress-linear v-if="tasksStore.isLoading" indeterminate class="mb-4" />

            <v-tabs v-model="tab" class="mb-4">
              <v-tab value="all">Todas ({{ tasksStore.totalTasks }})</v-tab>
              <v-tab value="pending">Pendentes ({{ tasksStore.pendingTasks.length }})</v-tab>
              <v-tab value="completed">Concluídas ({{ tasksStore.completedTasks.length }})</v-tab>
            </v-tabs>

            <v-window v-model="tab">
              <v-window-item value="all">
                <TaskList :tasks="tasksStore.tasks" @edit="handleEdit" />
              </v-window-item>
              <v-window-item value="pending">
                <TaskList :tasks="tasksStore.pendingTasks" @edit="handleEdit" />
              </v-window-item>
              <v-window-item value="completed">
                <TaskList :tasks="tasksStore.completedTasks" @edit="handleEdit" />
              </v-window-item>
            </v-window>

            <v-card
              v-if="!tasksStore.isLoading && tasksStore.tasks.length === 0"
              class="text-center pa-8"
            >
              <v-card-text>
                <v-icon size="64" color="grey-lighten-1">mdi-clipboard-text-outline</v-icon>
                <div class="text-h6 mt-4">Nenhuma tarefa encontrada</div>
                <div class="text-body-2 mt-2">Crie sua primeira tarefa clicando no botão acima</div>
              </v-card-text>
            </v-card>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <TaskDialog v-model="dialog" @task-saved="handleTaskSaved" />
    <TaskDialog v-model="editDialog" :task="selectedTask" @task-saved="handleTaskSaved" />
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useTasksStore } from '@/stores/tasks'
import TaskList from '@/components/TaskList.vue'
import TaskDialog from '@/components/TaskDialog.vue'
import type { Task } from '@/services/taskService'

const tasksStore = useTasksStore()
const tab = ref('all')
const dialog = ref(false)
const editDialog = ref(false)
const selectedTask = ref<Task | null>(null)

onMounted(async () => {
  await tasksStore.fetchTasks()
})

function handleTaskSaved() {
  dialog.value = false
  editDialog.value = false
  selectedTask.value = null
  tasksStore.fetchTasks()
}

function handleEdit(task: Task) {
  selectedTask.value = task
  editDialog.value = true
}
</script>

