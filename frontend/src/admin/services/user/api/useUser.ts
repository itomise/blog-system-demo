import { useQuery } from '@tanstack/react-query'
import { User } from '../types'
import { adminAppAxios } from '@/libs/axios'

const getUser = async (userId: string): Promise<User> => {
  const { data } = await adminAppAxios.get(`/users/${userId}`)
  return data
}

export const useUser = (userId?: string) => {
  const { data } = useQuery({
    queryKey: ['admin', 'user', userId],
    enabled: !!userId,
    queryFn: () => getUser(userId!!),
  })

  return data
}
