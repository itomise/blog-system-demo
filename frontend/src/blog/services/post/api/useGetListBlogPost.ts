import { blogAppAxios } from '@/libs/axios'
import { ExtractFnReturnType } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { useQuery } from '@tanstack/react-query'

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
