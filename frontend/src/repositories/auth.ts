import { UUID } from '@/types'
import { appAxios } from '@/libs/axios'

export type LoginWithSessionRequest = {
  email: string
  password: string
}

export type LoginWithSessionResponse = {
  userId: UUID
}

const loginWithSession = async (payload: LoginWithSessionRequest) => {
  const { data } = await appAxios.post<LoginWithSessionResponse>('/auth-session/login', payload)
  return data
}

export type LoginWithJwtRequest = {
  email: string
  password: string
}
export type LoginWithJwtResponse = {
  token: string
}

const loginWithJwt = async (payload: LoginWithJwtRequest) => {
  const { data } = await appAxios.post<LoginWithJwtResponse>('/auth-jwt/login', payload)
  return data
}

const logout = async () => {
  await appAxios.post('/auth-session/logout')
}

export type GetMeResponse = {
  id: string
  email: string
  name: string
}

const getMeWithJwt = async (jwtToken: string) => {
  const { data } = await appAxios.get<GetMeResponse>('/auth-jwt/me', {
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  })
  return data
}
const getMeWithSession = async () => {
  const { data } = await appAxios.get<GetMeResponse>('/auth-session/me')
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
  loginWithJwt,
  getMeWithJwt,
  getMeWithSession,
  signup,
  logout,
}
