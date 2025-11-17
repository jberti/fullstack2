import api from './api'

export interface Tasklist {
  id: string
  name: string
  description?: string
  color: string
  taskCount: number
  completedTaskCount: number
  createdAt: string
  updatedAt: string
}

export interface TasklistRequest {
  name: string
  description?: string
  color?: string
}

class TasklistService {
  async getTasklists(): Promise<Tasklist[]> {
    return api.get<Tasklist[]>('/tasklists')
  }

  async createTasklist(data: TasklistRequest): Promise<Tasklist> {
    return api.post<Tasklist>('/tasklists', data)
  }

  async updateTasklist(id: string, data: TasklistRequest): Promise<Tasklist> {
    return api.put<Tasklist>(`/tasklists/${id}`, data)
  }

  async deleteTasklist(id: string): Promise<void> {
    await api.delete(`/tasklists/${id}`)
  }
}

export default new TasklistService()