import api from './api'

export interface Task {
  id?: string
  title: string
  description?: string
  completed: boolean
  userId?: string
  createdAt?: string
  updatedAt?: string
}

export interface TaskRequest {
  title: string
  description?: string
  completed?: boolean
}

class TaskService {
  async getAll(): Promise<Task[]> {
    return api.get<Task[]>('/tasks')
  }

  async getById(id: string): Promise<Task> {
    return api.get<Task>(`/tasks/${id}`)
  }

  async create(task: TaskRequest): Promise<Task> {
    return api.post<Task>('/tasks', task)
  }

  async update(id: string, task: TaskRequest): Promise<Task> {
    return api.put<Task>(`/tasks/${id}`, task)
  }

  async delete(id: string): Promise<void> {
    return api.delete<void>(`/tasks/${id}`)
  }
}

export default new TaskService()

