import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { adminAppAxios } from '@/libs/axios'

export type LoginWithSessionRequest = {
  email: string
  password: string
}

export type LoginWithSessionResponse = {
  userId: UUID
}

const loginWithSession = async (payload: LoginWithSessionRequest) => {
  const { data } = await adminAppAxios.post<LoginWithSessionResponse>('/auth/login', payload)
  return data
}

type Options = {
  config?: MutationConfig<typeof loginWithSession>
}

export const useLoginWithSession = ({ config }: Options = {}) =>
  useMutation({
    ...config,
    mutationFn: loginWithSession,
  })
