import { useMutation } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { MutationConfig } from '@/libs/react-query'

type Options = {
  config?: MutationConfig<typeof authRepository.loginWithSession>
}

export const useLoginWithSession = ({ config }: Options = {}) =>
  useMutation({
    ...config,
    mutationFn: authRepository.loginWithSession,
  })
