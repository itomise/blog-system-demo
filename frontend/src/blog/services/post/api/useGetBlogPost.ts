import { useQuery } from '@tanstack/react-query'
import { UUID } from '@/shared/types'
import { ExtractFnReturnType } from '@/libs/react-query'
import { blogAppAxios } from '@/libs/axios'

export type GetBlogPostResponse = {
  id: UUID
  title: string
  content: string
  displayContent: string
  publishedAt: string
}

export const getBlogPostDetail = async (id: string) => {
  const { data } = await blogAppAxios.get<GetBlogPostResponse>(`/posts/${id}`)
  return data
}

export const useGetBlogPost = (id: string) => {
  const { data } = useQuery<ExtractFnReturnType<typeof getBlogPostDetail>>({
    queryKey: ['public', 'posts', id],
    enabled: !!id,
    queryFn: () => getBlogPostDetail(id),
  })
  return data
}
