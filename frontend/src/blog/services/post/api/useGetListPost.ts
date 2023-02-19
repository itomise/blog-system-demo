import { blogAppAxios } from '@/libs/axios'
import { ExtractFnReturnType } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { useQuery } from '@tanstack/react-query'

type Response = {
  posts: {
    id: UUID
    title: string
    content: string
    displayContent: string
  }[]
}

const getListPostApi = async () => {
  const { data } = await blogAppAxios.get<string>(`/posts`)
  return data
}

export const useGetListPost = () => {
  const { data } = useQuery<ExtractFnReturnType<typeof getListPostApi>>({
    queryKey: ['/posts'],
    queryFn: getListPostApi,
  })
  return data
}
