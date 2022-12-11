import { useQuery } from '@tanstack/react-query'
import { authRepository } from '@/repositories/auth'
import { ExtractFnReturnType } from '@/libs/react-query'

export const useGetMeWithJwt = (token: string | undefined) => {
  const { data } = useQuery<ExtractFnReturnType<typeof authRepository.getMeWithJwt>>({
    queryKey: token ? (['/hello', token] as const) : undefined,
    queryFn: () => authRepository.getMeWithJwt(token!!),
  })
  return data
}
