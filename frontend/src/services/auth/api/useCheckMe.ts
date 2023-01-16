import { useQuery } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { ExtractFnReturnType } from '@/libs/react-query'

export const useCheckMe = () => {
  const { data, isLoading } = useQuery<ExtractFnReturnType<typeof authRepository.getMeWithSession>>({
    queryKey: ['auth/me'],
    queryFn: authRepository.getMeWithSession,
  })
  return { me: data, isLoading }
}
