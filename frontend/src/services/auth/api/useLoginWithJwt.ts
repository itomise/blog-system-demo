import { useMutation } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { MutationConfig } from '@/libs/react-query'

type Options = {
  config?: MutationConfig<typeof authRepository.loginWithJwt>
}

export const useLoginWithJwt = ({ config }: Options = {}) =>
  useMutation({
    ...config,
    mutationFn: authRepository.loginWithJwt,
  })
