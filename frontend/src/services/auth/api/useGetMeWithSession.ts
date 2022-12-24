import { useQuery } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { ExtractFnReturnType } from '@/libs/react-query'

export const useGetMeWithSession = () => {
  const { data } = useQuery<ExtractFnReturnType<typeof authRepository.getMeWithSession>>({
    queryKey: ['auth-session/me'],
    queryFn: authRepository.getMeWithSession,
  })
  return data
}
