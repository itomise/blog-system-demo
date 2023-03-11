import { useQuery } from '@tanstack/react-query'
import { PostStatusType } from '../types'
import { UUID } from '@/shared/types'
import { ExtractFnReturnType } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

type Response = {
  id: UUID
  title: string
  content: string
  status: PostStatusType
  displayContent: string
}

const getPostApi = async (id: UUID) => {
  const { data } = await adminAppAxios.get<Response>(`/posts/${id}`)
  return data
}

export const useGetPost = (id: UUID | undefined) => {
  const { data } = useQuery<ExtractFnReturnType<typeof getPostApi>>({
    queryKey: ['admin', 'posts', id],
    enabled: !!id,
    queryFn: () => getPostApi(id!!),
  })
  return data
}
