import { appAxios } from '@/libs/axios'
import { QueryRepositoryType } from '@/libs/react-query'
import { UUID } from '@/types'
import { User } from '@/services/user/types'

export type GetListUserResponse = {
  users: User[]
}

const getUserList: QueryRepositoryType<GetListUserResponse> = {
  queryKey: ['users'] as const,
  queryFn: async () => {
    const { data } = await appAxios.get('/users')
    return data
  },
}

export type CreateUserRequest = {
  name: string
  email: string
}
export type CreateUserResponse = {
  userId: UUID
}

const createUser = async (payload: CreateUserRequest) => {
  const { data } = await appAxios.post<CreateUserResponse>('/users', payload)
  return data
}

export const userRepository = {
  getUserList,
  createUser,
}
