import { blogAppAxios } from '@/libs/axios'
import { ExtractFnReturnType } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { useQuery } from '@tanstack/react-query'

type SearchBlogPostRequest = {
  query: string | undefined
  limit: number
  offset: number
}

export type SearchBlogPostResponse = {
  posts: {
    id: UUID
    title: string
    content: string
    displayContent: string
    publishedAt: string
  }[]
}

const searchBlogPost = async (request: SearchBlogPostRequest) => {
  const { data } = await blogAppAxios.get<SearchBlogPostResponse>(
    `/posts/search?query=${request.query}&limit=${request.limit}&offset=${request.offset}`
  )
  return data
}

export const useFetchSearchBlogPost = (limit: number, offset: number, query: string | undefined) => {
  const { isFetching, data } = useQuery<ExtractFnReturnType<typeof searchBlogPost>>({
    queryKey: ['blog', 'posts', 'search', query, limit, offset],
    enabled: !!query,
    queryFn: () => searchBlogPost({ query: query!!, limit, offset }),
  })

  return {
    isLoading: isFetching,
    data,
  }
}
