import { AsyncReturnType } from 'type-fest'
import { AxiosError } from 'axios'
import { DefaultOptions, QueryClient, UseMutationOptions } from '@tanstack/react-query'

const queryConfig: DefaultOptions = {
  queries: {
    refetchOnWindowFocus: false,
    retry: false,
    useErrorBoundary: false, // TODO: 後で true にする
    suspense: false, // TODO: 後で true にする
    staleTime: 1000,
  },
}

export const queryClient = new QueryClient({ defaultOptions: queryConfig })

export type ExtractFnReturnType<FnType extends (...args: any) => any> = AsyncReturnType<FnType>

export type MutationConfig<MutationFnType extends (...args: any) => any> = UseMutationOptions<
  ExtractFnReturnType<MutationFnType>,
  AxiosError,
  Parameters<MutationFnType>[0]
>
