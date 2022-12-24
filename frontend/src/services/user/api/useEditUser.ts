import { useMutation } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'
import { MutationConfig, queryClient } from '@/libs/react-query'

type Props = MutationConfig<typeof userRepository.editUser>

export const useEditUser = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: userRepository.editUser,
  })
