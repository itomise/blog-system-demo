import { useQuery } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'

export const useUserList = () => {
  const { data } = useQuery({
    queryKey: ['/user'] as const,
    queryFn: userRepository.getUserList,
  })

  return data?.users
}
