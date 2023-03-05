import { SearchBlogPostResponse, useFetchSearchBlogPost } from '@/blog/services/post/api/useSearchBlogPost'
import { useDebouncedValue } from '@mantine/hooks'
import { useEffect } from 'react'
import { useForm, UseFormRegisterReturn } from 'react-hook-form'

type FormType = {
  query: string
}

export const usePostSearchState = (
  queryParam: string | undefined
): {
  register: UseFormRegisterReturn<'query'>
  data: SearchBlogPostResponse | undefined
  isLoading: boolean
  searchedQuery: string
} => {
  const { register, setValue, watch } = useForm<FormType>()
  const [debouncedQuery] = useDebouncedValue(watch('query'), 200)
  const { data, isLoading } = useFetchSearchBlogPost(100, 0, debouncedQuery)

  useEffect(() => {
    if (queryParam) setValue('query', queryParam)
  }, [queryParam])

  return {
    register: register('query'),
    data: debouncedQuery === '' ? { posts: [] } : data,
    isLoading,
    searchedQuery: debouncedQuery,
  }
}
