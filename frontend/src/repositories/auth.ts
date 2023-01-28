import { UUID } from '@/types'
import { BACKEND_ENDPOINT } from '@/services/common/api/constants'
import { appAxios } from '@/libs/axios'

export type LoginWithSessionRequest = {
  email: string
  password: string
}

export type LoginWithSessionResponse = {
  userId: UUID
}

const loginWithSession = async (payload: LoginWithSessionRequest) => {
  const { data } = await appAxios.post<LoginWithSessionResponse>('/auth/login', payload)
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
  await appAxios.post('/auth/logout')
}

export type GetMeResponse = {
  id: string
  email: string
  name: string | null
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
  const { data } = await appAxios.get<GetMeResponse>('/auth/me')
  return data
}

export type SignupRequest = {
  email: string
}
export type SignupResponse = {}

const signup = async (payload: SignupRequest) => {
  const { data } = await appAxios.post<SignupResponse>('/auth/sign-up', payload)
  return data
}

export type ActivateUserRequest = {
  name: string
  token: string
  password?: string
}

const activateUser = async (payload: ActivateUserRequest) => {
  await appAxios.post('/auth/sign-up/activate', payload)
}

const googleOAuth2 = () => {
  window.location.href = `${BACKEND_ENDPOINT}/auth/google_oauth2`
}

export const authRepository = {
  loginWithSession,
  loginWithJwt,
  getMeWithJwt,
  getMeWithSession,
  signup,
  logout,
  activateUser,
  googleOAuth2,
}
