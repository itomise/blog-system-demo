import { useMutation } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'
import { MutationConfig } from '@/libs/react-query'

type Props = MutationConfig<typeof userRepository.deleteUser>

export const useDeleteUser = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: userRepository.deleteUser,
  })
