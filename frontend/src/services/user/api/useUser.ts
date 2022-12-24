import { useQuery } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'

export const useUser = (userId?: string) => {
  const { data } = useQuery({
    queryKey: ['/user', userId],
    enabled: !!userId,
    queryFn: () => userRepository.getUser(userId!!),
  })

  return data
}
