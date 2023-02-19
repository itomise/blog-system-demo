import { adminAppAxios } from '@/libs/axios'
import { ExtractFnReturnType, MutationConfig } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { useQuery } from '@tanstack/react-query'
import { PostStatus } from '../types'

export type AdminGetListPostsResponse = {
  posts: {
    id: UUID
    title: string
    content: string
    status: PostStatus
    displayContent: string
  }[]
}

const getListPostApi = async () => {
  const { data } = await adminAppAxios.get<AdminGetListPostsResponse>(`/posts`)
  return data
}

export const useGetListPost = () => {
  const { data } = useQuery<ExtractFnReturnType<typeof getListPostApi>>({
    queryKey: ['/posts'],
    queryFn: getListPostApi,
  })
  return data
}
