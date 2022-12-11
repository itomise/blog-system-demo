import { useQuery } from '@tanstack/react-query'
import { userRepository } from '@/repositories/user'

export const useUserList = () => {
  const { data } = useQuery(userRepository.getUserList)

  return { users: data?.users }
}
