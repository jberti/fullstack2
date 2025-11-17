import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import taskService, { type Task, type TaskRequest } from '@/services/taskService'

export const useTasksStore = defineStore(
  'tasks',
  () => {
    const tasks = ref<Task[]>([])
    const isLoading = ref(false)
    const error = ref<string | null>(null)

    const completedTasks = computed(() => tasks.value.filter((task) => task.completed))
    const pendingTasks = computed(() => tasks.value.filter((task) => !task.completed))
    const totalTasks = computed(() => tasks.value.length)

    async function fetchTasks() {
      isLoading.value = true
      error.value = null
      try {
        tasks.value = await taskService.getAll()
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Erro ao carregar tarefas'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    async function fetchTasksByTasklist(tasklistId: string) {
      isLoading.value = true
      error.value = null
      try {
        tasks.value = await taskService.getByTasklist(tasklistId)
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Erro ao carregar tarefas'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    async function createTask(taskData: TaskRequest) {
      isLoading.value = true
      error.value = null
      try {
        const newTask = await taskService.create(taskData)
        tasks.value.push(newTask)
        return newTask
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Erro ao criar tarefa'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    async function updateTask(id: string, taskData: TaskRequest) {
      isLoading.value = true
      error.value = null
      try {
        const updatedTask = await taskService.update(id, taskData)
        const index = tasks.value.findIndex((task) => task.id === id)
        if (index !== -1) {
          tasks.value[index] = updatedTask
        }
        return updatedTask
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Erro ao atualizar tarefa'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    async function deleteTask(id: string) {
      isLoading.value = true
      error.value = null
      try {
        await taskService.delete(id)
        tasks.value = tasks.value.filter((task) => task.id !== id)
      } catch (err: any) {
        error.value = err.response?.data?.message || 'Erro ao excluir tarefa'
        throw err
      } finally {
        isLoading.value = false
      }
    }

    async function toggleTask(id: string) {
      const task = tasks.value.find((t) => t.id === id)
      if (task && task.tasklistId) {
        await updateTask(id, {
          title: task.title,
          description: task.description,
          completed: !task.completed,
          tasklistId: task.tasklistId,
        })
      }
    }

    function clearError() {
      error.value = null
    }

    return {
      tasks,
      isLoading,
      error,
      completedTasks,
      pendingTasks,
      totalTasks,
      fetchTasks,
      fetchTasksByTasklist,
      createTask,
      updateTask,
      deleteTask,
      toggleTask,
      clearError,
    }
  },
  {
    persist: true,
  }
)


