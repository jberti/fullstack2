import api from './api'

export interface RegisterRequest {
  name: string
  email: string
  password: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface AuthResponse {
  token: string
  refreshToken: string
  type: string
}

export interface User {
  id: string
  name: string
  email: string
}

class AuthService {
  async register(data: RegisterRequest): Promise<AuthResponse> {
    return api.post<AuthResponse>('/auth/register', data)
  }

  async login(data: LoginRequest): Promise<AuthResponse> {
    return api.post<AuthResponse>('/auth/login', data)
  }

  logout(): void {
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }

  getToken(): string | null {
    return localStorage.getItem('token')
  }

  setToken(token: string): void {
    localStorage.setItem('token', token)
  }

  setRefreshToken(refreshToken: string): void {
    localStorage.setItem('refreshToken', refreshToken)
  }
}

export default new AuthService()

