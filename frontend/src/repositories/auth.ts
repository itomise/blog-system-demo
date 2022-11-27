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

export type SignupRequest = {
  name: string
  email: string
  password: string
}
export type SignupResponse = {}

const signup = async (payload: SignupRequest) => {
  const { data } = await appAxios.post<SignupResponse>('/auth-session/sign-up', payload)
  return data
}

export const authRepository = {
  loginWithSession,
  signup,
}
