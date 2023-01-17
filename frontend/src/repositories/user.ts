import { UUID } from '@/types'
import { User } from '@/services/user/types'
import { appAxios } from '@/libs/axios'

export type GetListUserResponse = {
  users: User[]
}

const getUserList = async (): Promise<GetListUserResponse> => {
  const { data } = await appAxios.get('/users')
  return data
}

const getUser = async (userId: string): Promise<User> => {
  const { data } = await appAxios.get(`/users/${userId}`)
  return data
}

export type CreateUserRequest = {
  email: string
}
export type CreateUserResponse = {
  userId: UUID
}

const createUser = async (payload: CreateUserRequest) => {
  const { data } = await appAxios.post<CreateUserResponse>('/users', payload)
  return data
}

export type EditUserRequest = {
  id: string
  name: string
}
const editUser = async ({ id, ...rest }: EditUserRequest) => {
  await appAxios.put(`/users/${id}`, rest)
}

export type DeleteUserRequest = {
  id: string
}
const deleteUser = async (request: DeleteUserRequest) => {
  await appAxios.delete(`/users/${request.id}`)
}

export const userRepository = {
  getUserList,
  getUser,
  createUser,
  editUser,
  deleteUser,
}
