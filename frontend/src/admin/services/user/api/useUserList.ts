import { useQuery } from '@tanstack/react-query'
import { User } from '../types'
import { adminAppAxios } from '@/libs/axios'

export type GetListUserResponse = {
  users: User[]
}

const getUserList = async (): Promise<GetListUserResponse> => {
  const { data } = await adminAppAxios.get('/users')
  return data
}

export const useUserList = () => {
  const { data } = useQuery({
    queryKey: ['/user'] as const,
    queryFn: getUserList,
  })

  return data?.users
}
