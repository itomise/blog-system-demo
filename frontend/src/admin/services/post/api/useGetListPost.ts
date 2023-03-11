import { useQuery } from '@tanstack/react-query'
import { PostStatusType } from '../types'
import { UUID } from '@/shared/types'
import { ExtractFnReturnType } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

export type AdminGetListPostsResponse = {
  posts: {
    id: UUID
    title: string
    content: string
    status: PostStatusType
    displayContent: string
  }[]
}

const getListPostApi = async () => {
  const { data } = await adminAppAxios.get<AdminGetListPostsResponse>(`/posts`)
  return data
}

export const useGetListPost = () => {
  const { data } = useQuery<ExtractFnReturnType<typeof getListPostApi>>({
    queryKey: ['admin', 'posts'],
    queryFn: getListPostApi,
  })
  return data
}
