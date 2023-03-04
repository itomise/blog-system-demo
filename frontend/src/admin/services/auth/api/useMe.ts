import { useQuery } from '@tanstack/react-query'
import { ExtractFnReturnType } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'
import { User } from '../../user/types'

export type GetMeResponse = {
  id: string
  email: string
  name: string | null
  isActive: boolean
}

const getMeWithJwt = async (jwtToken: string) => {
  const { data } = await adminAppAxios.get<GetMeResponse>('/auth-jwt/me', {
    headers: {
      Authorization: `Bearer ${jwtToken}`,
    },
  })
  return data
}
export const useGetMeWithJwt = (token: string | undefined) => {
  const { data } = useQuery<ExtractFnReturnType<typeof getMeWithJwt>>({
    queryKey: ['admin', 'auth-jwt', 'me', token],
    enabled: !!token,
    queryFn: () => getMeWithJwt(token!!),
  })
  return data
}

const getMeWithSession = async () => {
  const { data } = await adminAppAxios.get<GetMeResponse>('/auth/me')
  return data
}

export const useCheckMe = () => {
  const { data, isLoading } = useQuery<ExtractFnReturnType<typeof getMeWithSession>>({
    queryKey: ['admin', 'auth', 'me'],
    queryFn: getMeWithSession,
  })
  return { me: data, isLoading }
}

export const useMe = (): User => {
  const { data } = useQuery<ExtractFnReturnType<typeof getMeWithSession>>({
    queryKey: ['admin', 'auth', 'me'],
    queryFn: getMeWithSession,
  })
  if (!data) throw new Error('未ログインです')
  return data
}
