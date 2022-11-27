import { useMutation } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { MutationConfig } from '@/libs/react-query'

type Props = MutationConfig<typeof authRepository.signup>

export const useSignup = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: authRepository.signup,
  })
