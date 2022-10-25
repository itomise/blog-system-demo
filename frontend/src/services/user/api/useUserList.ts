import { userRepository } from '@/repositories/user'
import { useQuery } from '@tanstack/react-query'

export const useUserList = () => {
  const { data } = useQuery(userRepository.getUserList)

  return { users: data?.users }
}
