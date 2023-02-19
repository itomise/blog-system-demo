import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

export type SignupRequest = {
  email: string
}
export type SignupResponse = {}

const signup = async (payload: SignupRequest) => {
  const { data } = await adminAppAxios.post<SignupResponse>('/auth/sign-up', payload)
  return data
}

type Props = MutationConfig<typeof signup>

export const useSignup = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: signup,
  })
