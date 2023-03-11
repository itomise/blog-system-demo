import { useForm, UseFormRegisterReturn } from 'react-hook-form'
import { useEffect } from 'react'
import { useDebouncedValue } from '@mantine/hooks'
import { SearchBlogPostResponse, useFetchSearchBlogPost } from '@/blog/services/post/api/useSearchBlogPost'

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
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [queryParam])

  return {
    register: register('query'),
    data: debouncedQuery === '' ? { posts: [] } : data,
    isLoading,
    searchedQuery: debouncedQuery,
  }
}
