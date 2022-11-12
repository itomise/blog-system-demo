import { UUID } from '@/types'
import { appAxios } from '@/libs/axios'

export type LoginRequest = {
  email: string
  password: string
}

export type LoginResponse = {
  userId: UUID
}

const loginWithSession = async (payload: LoginRequest) => {
  const { data } = await appAxios.post<LoginResponse>('/auth-session/login', payload)
  return data
}

export const authRepository = {
  loginWithSession,
}
