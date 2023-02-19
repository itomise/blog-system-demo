import { adminAppAxios } from '@/libs/axios'
import { ExtractFnReturnType, MutationConfig } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { useQuery } from '@tanstack/react-query'
import { PostStatus } from '../types'

type Response = {
  id: UUID
  title: string
  content: string
  status: PostStatus
  displayContent: string
}

const getPostApi = async (id: UUID) => {
  const { data } = await adminAppAxios.get<Response>(`/posts/${id}`)
  return data
}

export const useGetPost = (id: UUID | undefined) => {
  const { data } = useQuery<ExtractFnReturnType<typeof getPostApi>>({
    queryKey: ['/posts', id],
    enabled: !!id,
    queryFn: () => getPostApi(id!!),
  })
  return data
}
