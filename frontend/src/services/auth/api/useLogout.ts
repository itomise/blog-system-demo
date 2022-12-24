import { useMutation } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { MutationConfig } from '@/libs/react-query'

type Props = MutationConfig<typeof authRepository.logout>

export const useLogout = (props: Props = {}) =>
  useMutation({
    ...props,
    mutationFn: authRepository.logout,
  })
