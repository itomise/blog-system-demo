import { appAxios } from '@/libs/axios'
import { ExtractFnReturnType, MutationConfig } from '@/libs/react-query'
import { UUID } from '@/types'
import { useQuery } from '@tanstack/react-query'
import { PostStatus } from '../types'

type Response = {
  posts: {
    id: UUID
    title: string
    content: string
    status: PostStatus
  }[]
}

const getListPostApi = async () => {
  const { data } = await appAxios.get<Response>(`/posts`)
  return data
}

export const useGetListPost = () => {
  const { data } = useQuery<ExtractFnReturnType<typeof getListPostApi>>({
    queryKey: ['/posts'],
    queryFn: getListPostApi,
  })
  return data
}
