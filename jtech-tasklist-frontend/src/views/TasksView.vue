<template>
  <div class="d-flex">
    <!-- Sidebar -->
    <TasklistSidebar />
    
    <!-- Main Content -->
    <v-main>
      <v-container fluid class="pa-6">
    <!-- Header Section with Modern Design -->
    <div class="d-flex align-center justify-space-between mb-8">
      <div>
        <h1 class="text-h4 font-weight-bold mb-2">
          <v-avatar 
            v-if="tasklistsStore.currentTasklist" 
            size="32" 
            :color="tasklistsStore.currentTasklist.color"
            class="me-3"
          >
            <v-icon color="white" size="18">mdi-format-list-bulleted</v-icon>
          </v-avatar>
          <v-icon v-else color="primary" class="me-3">mdi-check-circle-outline</v-icon>
          {{ tasklistsStore.currentTasklist?.name || 'Minhas Tarefas' }}
        </h1>
        <p class="text-body-1 text-medium-emphasis">
          {{ tasklistsStore.currentTasklist?.description || 'Gerencie suas tarefas de forma eficiente' }}
        </p>
      </div>
      
      <v-btn
        v-if="tasklistsStore.currentTasklist"
        color="primary"
        size="large"
        variant="elevated"
        class="text-none px-6"
        @click="dialog = true"
      >
        <v-icon start>mdi-plus</v-icon>
        Nova Tarefa
      </v-btn>
    </div>

    <!-- Stats Cards -->
    <v-row class="mb-6">
      <v-col cols="12" sm="4">
        <v-card class="stats-card" variant="tonal" color="primary">
          <v-card-text class="d-flex align-center">
            <v-avatar size="48" color="primary" class="me-4">
              <v-icon color="white">mdi-format-list-checks</v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold">{{ tasksStore.totalTasks }}</div>
              <div class="text-body-2 text-medium-emphasis">Total de Tarefas</div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
      
      <v-col cols="12" sm="4">
        <v-card class="stats-card" variant="tonal" color="warning">
          <v-card-text class="d-flex align-center">
            <v-avatar size="48" color="warning" class="me-4">
              <v-icon color="white">mdi-clock-outline</v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold">{{ tasksStore.pendingTasks.length }}</div>
              <div class="text-body-2 text-medium-emphasis">Pendentes</div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
      
      <v-col cols="12" sm="4">
        <v-card class="stats-card" variant="tonal" color="success">
          <v-card-text class="d-flex align-center">
            <v-avatar size="48" color="success" class="me-4">
              <v-icon color="white">mdi-check-circle</v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold">{{ tasksStore.completedTasks.length }}</div>
              <div class="text-body-2 text-medium-emphasis">Concluídas</div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Main Content Card -->
    <v-card class="main-card" elevation="2">
      <!-- Error Alert -->
      <v-alert 
        v-if="tasksStore.error" 
        type="error" 
        variant="tonal"
        closable
        class="ma-4"
        @click="tasksStore.clearError()"
      >
        {{ tasksStore.error }}
      </v-alert>

      <!-- Loading -->
      <v-progress-linear 
        v-if="tasksStore.isLoading" 
        indeterminate 
        color="primary"
        height="3"
      />

      <!-- Modern Tabs -->
      <v-tabs 
        v-model="tab" 
        color="primary"
        class="px-4 pt-4"
        slider-color="primary"
      >
        <v-tab value="all" class="text-none">
          <v-icon start>mdi-format-list-bulleted</v-icon>
          Todas
          <v-chip size="small" class="ms-2" variant="tonal">
            {{ tasksStore.totalTasks }}
          </v-chip>
        </v-tab>
        
        <v-tab value="pending" class="text-none">
          <v-icon start>mdi-clock-outline</v-icon>
          Pendentes
          <v-chip size="small" class="ms-2" variant="tonal" color="warning">
            {{ tasksStore.pendingTasks.length }}
          </v-chip>
        </v-tab>
        
        <v-tab value="completed" class="text-none">
          <v-icon start>mdi-check-circle</v-icon>
          Concluídas
          <v-chip size="small" class="ms-2" variant="tonal" color="success">
            {{ tasksStore.completedTasks.length }}
          </v-chip>
        </v-tab>
      </v-tabs>

      <v-divider />

      <!-- Tab Content -->
      <v-window v-model="tab" class="pa-4">
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

      <!-- Empty State - No Tasklist Selected -->
      <div
        v-if="!tasklistsStore.currentTasklist"
        class="text-center pa-12"
      >
        <v-avatar size="120" color="surface-variant" class="mb-6">
          <v-icon size="60" color="medium-emphasis">mdi-format-list-bulleted</v-icon>
        </v-avatar>
        
        <h3 class="text-h5 font-weight-medium mb-2">
          Selecione uma lista
        </h3>
        
        <p class="text-body-1 text-medium-emphasis mb-6">
          Escolha uma lista na barra lateral ou crie uma nova para começar
        </p>
      </div>

      <!-- Empty State - No Tasks -->
      <div
        v-else-if="!tasksStore.isLoading && tasksStore.tasks.length === 0"
        class="text-center pa-12"
      >
        <v-avatar size="120" color="surface-variant" class="mb-6">
          <v-icon size="60" color="medium-emphasis">mdi-clipboard-text-outline</v-icon>
        </v-avatar>
        
        <h3 class="text-h5 font-weight-medium mb-2">
          Nenhuma tarefa encontrada
        </h3>
        
        <p class="text-body-1 text-medium-emphasis mb-6">
          Comece criando sua primeira tarefa para organizar seu dia
        </p>
        
        <v-btn
          color="primary"
          variant="elevated"
          size="large"
          class="text-none"
          @click="dialog = true"
        >
          <v-icon start>mdi-plus</v-icon>
          Criar Primeira Tarefa
        </v-btn>
      </div>
    </v-card>

    <!-- Dialogs -->
    <TaskDialog 
      v-model="dialog" 
      :tasklist-id="tasklistsStore.currentTasklist?.id"
      @task-saved="handleTaskSaved" 
    />
    <TaskDialog 
      v-model="editDialog" 
      :task="selectedTask" 
      @task-saved="handleTaskSaved" 
    />
      </v-container>
    </v-main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useTasksStore } from '@/stores/tasks'
import { useTasklistsStore } from '@/stores/tasklists'
import TaskList from '@/components/TaskList.vue'
import TaskDialog from '@/components/TaskDialog.vue'
import TasklistSidebar from '@/components/TasklistSidebar.vue'
import type { Task } from '@/services/taskService'

const tasksStore = useTasksStore()
const tasklistsStore = useTasklistsStore()
const tab = ref('all')
const dialog = ref(false)
const editDialog = ref(false)
const selectedTask = ref<Task | null>(null)

onMounted(async () => {
  await tasklistsStore.fetchTasklists()
})

// Watch for changes in current tasklist and fetch tasks accordingly
watch(() => tasklistsStore.currentTasklist, async (currentTasklist) => {
  if (currentTasklist) {
    await tasksStore.fetchTasksByTasklist(currentTasklist.id)
  }
}, { immediate: true })

function handleTaskSaved() {
  dialog.value = false
  editDialog.value = false
  selectedTask.value = null
  
  // Refresh tasks for current tasklist
  if (tasklistsStore.currentTasklist) {
    tasksStore.fetchTasksByTasklist(tasklistsStore.currentTasklist.id)
  }
  
  // Refresh tasklists to update counters
  tasklistsStore.fetchTasklists()
}

function handleEdit(task: Task) {
  selectedTask.value = task
  editDialog.value = true
}
</script>

<style scoped>
.stats-card {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.main-card {
  border-radius: 16px;
  overflow: hidden;
}

/* Smooth animations for tab transitions */
.v-window-item {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
</style>

