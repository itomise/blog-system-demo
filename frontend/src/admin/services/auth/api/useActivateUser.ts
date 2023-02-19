import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

export type ActivateUserRequest = {
  name: string
  token: string
  password?: string
}

const activateUser = async (payload: ActivateUserRequest) => {
  await adminAppAxios.post('/auth/sign-up/activate', payload)
}

type Props = MutationConfig<typeof activateUser>

export const useActivateUser = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: activateUser,
  })
