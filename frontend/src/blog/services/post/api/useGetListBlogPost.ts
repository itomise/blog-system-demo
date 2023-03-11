import { useQuery } from '@tanstack/react-query'
import { UUID } from '@/shared/types'
import { ExtractFnReturnType } from '@/libs/react-query'
import { blogAppAxios } from '@/libs/axios'

export type BlogPostListResponse = {
  posts: {
    id: UUID
    title: string
    content: string
    displayContent: string
    publishedAt: string
  }[]
}

export const getListBlogPost = async () => {
  const { data } = await blogAppAxios.get<BlogPostListResponse>(`/posts`)
  return data
}

export const useGetListBlogPost = () => {
  const { data } = useQuery<ExtractFnReturnType<typeof getListBlogPost>>({
    queryKey: ['public', 'posts'],
    queryFn: getListBlogPost,
  })
  return data
}
