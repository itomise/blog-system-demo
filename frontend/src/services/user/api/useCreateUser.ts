import { useMutation } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'
import { MutationConfig, queryClient } from '@/libs/react-query'

const { createUser, getUserList } = userRepository

type Options = {
  config?: MutationConfig<typeof createUser>
}

export const useCreateUser = ({ config }: Options = {}) =>
  useMutation({
    onSuccess: () => {
      queryClient.invalidateQueries(getUserList.queryKey)
    },
    ...config,
    mutationFn: createUser,
  })
