import { AsyncReturnType } from 'type-fest'
import { AxiosError } from 'axios'
import { DefaultOptions, QueryClient, UseMutationOptions } from '@tanstack/react-query'

const queryConfig: DefaultOptions = {
  queries: {
    refetchOnWindowFocus: false,
    retry: false,
    staleTime: 1000,
  },
}

export const queryClient = new QueryClient({ defaultOptions: queryConfig })

export type ExtractFnReturnType<FnType extends (...args: unknown[]) => unknown> = AsyncReturnType<FnType>

export type MutationConfig<MutationFnType extends (...args: unknown[]) => unknown> = UseMutationOptions<
  ExtractFnReturnType<MutationFnType>,
  AxiosError,
  Parameters<MutationFnType>[0]
>
