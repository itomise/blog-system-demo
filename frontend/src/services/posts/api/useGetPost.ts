import { appAxios } from '@/libs/axios'
import { ExtractFnReturnType, MutationConfig } from '@/libs/react-query'
import { UUID } from '@/types'
import { useQuery } from '@tanstack/react-query'
import { PostStatus } from '../types'

type Response = {
  id: UUID
  title: string
  content: string
  status: PostStatus
}

const getPostApi = async (id: UUID) => {
  const { data } = await appAxios.get<Response>(`/posts/${id}`)
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
