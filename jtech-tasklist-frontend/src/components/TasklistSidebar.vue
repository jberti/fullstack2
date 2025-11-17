<template>
  <v-navigation-drawer
    v-model="drawer"
    :rail="rail"
    permanent
    class="tasklist-sidebar"
  >
    <!-- Header -->
    <div class="pa-4 d-flex align-center">
      <v-btn
        :icon="rail ? 'mdi-menu' : 'mdi-menu-open'"
        variant="text"
        @click="rail = !rail"
      />
      <v-fade-transition>
        <h3 v-show="!rail" class="text-h6 font-weight-bold ms-3">
          Minhas Listas
        </h3>
      </v-fade-transition>
    </div>

    <v-divider />

    <!-- Create New List Button -->
    <div class="pa-3">
      <v-btn
        color="primary"
        variant="elevated"
        :block="!rail"
        :icon="rail"
        @click="showCreateDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
        <span v-if="!rail" class="ms-2">Nova Lista</span>
      </v-btn>
    </div>

    <v-divider />

    <!-- Lists -->
    <v-list density="compact" class="pa-2">
      <v-list-item
        v-for="tasklist in tasklistsStore.tasklists"
        :key="tasklist.id"
        :active="tasklistsStore.currentTasklist?.id === tasklist.id"
        :title="tasklist.name"
        :subtitle="rail ? undefined : `${tasklist.taskCount} tarefas`"
        @click="tasklistsStore.selectTasklist(tasklist)"
        class="tasklist-item"
        rounded="lg"
      >
        <template #prepend>
          <v-avatar size="32" :color="tasklist.color">
            <v-icon color="white" size="18">mdi-format-list-bulleted</v-icon>
          </v-avatar>
        </template>

        <template #append v-if="!rail">
          <v-menu>
            <template #activator="{ props }">
              <v-btn
                v-bind="props"
                icon="mdi-dots-vertical"
                variant="text"
                size="small"
                @click.stop
              />
            </template>
            
            <v-list>
              <v-list-item @click="editTasklist(tasklist)">
                <template #prepend>
                  <v-icon>mdi-pencil</v-icon>
                </template>
                <v-list-item-title>Editar</v-list-item-title>
              </v-list-item>
              
              <v-list-item 
                @click="confirmDelete(tasklist)"
                :disabled="tasklist.taskCount > 0"
                class="text-error"
              >
                <template #prepend>
                  <v-icon>mdi-delete</v-icon>
                </template>
                <v-list-item-title>Excluir</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </template>

        <!-- Progress indicator -->
        <template #subtitle v-if="!rail && tasklist.taskCount > 0">
          <div class="d-flex align-center mt-1">
            <span class="text-caption me-2">
              {{ tasklist.completedTaskCount }}/{{ tasklist.taskCount }}
            </span>
            <v-progress-linear
              :model-value="(tasklist.completedTaskCount / tasklist.taskCount) * 100"
              :color="tasklist.color"
              height="4"
              rounded
              class="flex-grow-1"
            />
          </div>
        </template>
      </v-list-item>
    </v-list>

    <!-- Empty State -->
    <div v-if="tasklistsStore.tasklists.length === 0 && !rail" class="pa-4 text-center">
      <v-icon size="48" color="grey-lighten-1">mdi-format-list-bulleted</v-icon>
      <p class="text-body-2 text-medium-emphasis mt-2">
        Nenhuma lista criada
      </p>
    </div>

    <!-- Create Dialog -->
    <TasklistDialog
      v-model="showCreateDialog"
      @saved="handleTasklistSaved"
    />

    <!-- Edit Dialog -->
    <TasklistDialog
      v-model="showEditDialog"
      :tasklist="selectedTasklist"
      @saved="handleTasklistSaved"
    />

    <!-- Delete Confirmation -->
    <v-dialog v-model="showDeleteDialog" max-width="400">
      <v-card>
        <v-card-title>Confirmar Exclusão</v-card-title>
        <v-card-text>
          <p>Tem certeza que deseja excluir a lista <strong>{{ selectedTasklist?.name }}</strong>?</p>
          <v-alert
            v-if="selectedTasklist?.taskCount && selectedTasklist.taskCount > 0"
            type="warning"
            variant="tonal"
            class="mt-3"
          >
            Esta lista contém {{ selectedTasklist.taskCount }} tarefa(s). 
            Exclua todas as tarefas antes de excluir a lista.
          </v-alert>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn variant="text" @click="showDeleteDialog = false">
            Cancelar
          </v-btn>
          <v-btn
            color="error"
            variant="elevated"
            @click="deleteTasklist"
            :disabled="Boolean(selectedTasklist?.taskCount && selectedTasklist.taskCount > 0)"
            :loading="isDeleting"
          >
            Excluir
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-navigation-drawer>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useTasklistsStore } from '@/stores/tasklists'
import TasklistDialog from './TasklistDialog.vue'
import type { Tasklist } from '@/services/tasklistService'

const tasklistsStore = useTasklistsStore()

const drawer = ref(true)
const rail = ref(false)
const showCreateDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const selectedTasklist = ref<Tasklist | null>(null)
const isDeleting = ref(false)

onMounted(() => {
  tasklistsStore.fetchTasklists()
})

function editTasklist(tasklist: Tasklist) {
  selectedTasklist.value = tasklist
  showEditDialog.value = true
}

function confirmDelete(tasklist: Tasklist) {
  selectedTasklist.value = tasklist
  showDeleteDialog.value = true
}

async function deleteTasklist() {
  if (!selectedTasklist.value) return

  isDeleting.value = true
  try {
    await tasklistsStore.deleteTasklist(selectedTasklist.value.id)
    showDeleteDialog.value = false
    selectedTasklist.value = null
  } catch (error) {
    // Error handled by store
  } finally {
    isDeleting.value = false
  }
}

function handleTasklistSaved() {
  showCreateDialog.value = false
  showEditDialog.value = false
  selectedTasklist.value = null
  tasklistsStore.fetchTasklists()
}
</script>

<style scoped>
.tasklist-sidebar {
  border-right: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
}

.tasklist-item {
  margin-bottom: 4px;
}

.tasklist-item:hover {
  background-color: rgba(var(--v-theme-primary), 0.08);
}
</style>