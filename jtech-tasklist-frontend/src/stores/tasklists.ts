import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import tasklistService, { type Tasklist, type TasklistRequest } from '@/services/tasklistService'

export const useTasklistsStore = defineStore('tasklists', () => {
  const tasklists = ref<Tasklist[]>([])
  const currentTasklist = ref<Tasklist | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const totalTasklists = computed(() => tasklists.value.length)
  
  const totalTasks = computed(() => 
    tasklists.value.reduce((sum, list) => sum + list.taskCount, 0)
  )
  
  const totalCompletedTasks = computed(() => 
    tasklists.value.reduce((sum, list) => sum + list.completedTaskCount, 0)
  )

  async function fetchTasklists() {
    isLoading.value = true
    error.value = null
    
    try {
      tasklists.value = await tasklistService.getTasklists()
      
      // Se não há lista atual selecionada, selecionar a primeira
      if (!currentTasklist.value && tasklists.value.length > 0) {
        currentTasklist.value = tasklists.value[0]
      }
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao carregar listas'
    } finally {
      isLoading.value = false
    }
  }

  async function createTasklist(data: TasklistRequest) {
    isLoading.value = true
    error.value = null
    
    try {
      const newTasklist = await tasklistService.createTasklist(data)
      tasklists.value.push(newTasklist)
      
      // Selecionar a nova lista criada
      currentTasklist.value = newTasklist
      
      return newTasklist
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao criar lista'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function updateTasklist(id: string, data: TasklistRequest) {
    isLoading.value = true
    error.value = null
    
    try {
      const updatedTasklist = await tasklistService.updateTasklist(id, data)
      
      const index = tasklists.value.findIndex(list => list.id === id)
      if (index !== -1) {
        tasklists.value[index] = updatedTasklist
        
        // Atualizar lista atual se for a mesma
        if (currentTasklist.value?.id === id) {
          currentTasklist.value = updatedTasklist
        }
      }
      
      return updatedTasklist
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao atualizar lista'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function deleteTasklist(id: string) {
    isLoading.value = true
    error.value = null
    
    try {
      await tasklistService.deleteTasklist(id)
      
      const index = tasklists.value.findIndex(list => list.id === id)
      if (index !== -1) {
        tasklists.value.splice(index, 1)
        
        // Se a lista excluída era a atual, selecionar outra
        if (currentTasklist.value?.id === id) {
          currentTasklist.value = tasklists.value.length > 0 ? tasklists.value[0] : null
        }
      }
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Erro ao excluir lista'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  function selectTasklist(tasklist: Tasklist) {
    currentTasklist.value = tasklist
  }

  function clearError() {
    error.value = null
  }

  return {
    tasklists,
    currentTasklist,
    isLoading,
    error,
    totalTasklists,
    totalTasks,
    totalCompletedTasks,
    fetchTasklists,
    createTasklist,
    updateTasklist,
    deleteTasklist,
    selectTasklist,
    clearError
  }
}, {
  persist: {
    paths: ['currentTasklist']
  }
})