import { useMutation } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'
import { MutationConfig, queryClient } from '@/libs/react-query'

const { createUser } = userRepository

type Options = MutationConfig<typeof createUser>
export const useCreateUser = (props: Options = {}) =>
  useMutation({
    onSuccess: () => {
      queryClient.invalidateQueries(['/user'])
    },
    ...props,
    mutationFn: createUser,
  })
