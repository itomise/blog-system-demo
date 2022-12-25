import { useQuery } from '@tanstack/react-query'
import { User } from '@/services/user/types'
import { authRepository } from '@/repositories/auth'
import { ExtractFnReturnType } from '@/libs/react-query'

export const useMe = (): User => {
  const { data } = useQuery<ExtractFnReturnType<typeof authRepository.getMeWithSession>>({
    queryKey: ['auth/me'],
    queryFn: authRepository.getMeWithSession,
  })
  if (!data) throw new Error('未ログインです')
  return data
}
