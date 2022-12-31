import { useMutation } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { MutationConfig } from '@/libs/react-query'

type Props = MutationConfig<typeof authRepository.activateUser>

export const useActivateUser = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: authRepository.activateUser,
  })
