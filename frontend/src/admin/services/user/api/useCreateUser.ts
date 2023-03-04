import { useMutation } from '@tanstack/react-query'
import { MutationConfig, queryClient } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'
import { UUID } from '@/shared/types'

export type CreateUserRequest = {
  email: string
}
export type CreateUserResponse = {
  userId: UUID
}

const createUser = async (payload: CreateUserRequest) => {
  const { data } = await adminAppAxios.post<CreateUserResponse>('/users', payload)
  return data
}

type Options = MutationConfig<typeof createUser>
export const useCreateUser = (props: Options = {}) =>
  useMutation({
    onSuccess: () => {
      queryClient.invalidateQueries(['admin', 'user'])
    },
    ...props,
    mutationFn: createUser,
  })
