/* eslint-disable import/named */
import useSWR, { SWRConfiguration, KeyedMutator, Key } from 'swr'

type AsyncReturnType<T extends (args: any) => Promise<any>> = ReturnType<T> extends Promise<infer U> ? U : never

// Suspenseでは loading と error は throw 先で制御するので data は defined で error は返さない
type SWRSuspenseResponse<Data> = {
  data: Data
  mutate: KeyedMutator<Data>
  isValidating: boolean
}

function useSuspenseSWR<T extends () => Promise<any>>(
  key: Key,
  api: T,
  options?: SWRConfiguration<AsyncReturnType<T>>
): SWRSuspenseResponse<AsyncReturnType<T>> {
  const _options: SWRConfiguration<AsyncReturnType<T>> = { ...options, suspense: true }
  const { data, mutate, isValidating } = useSWR(key, api, _options)

  return {
    data,
    mutate,
    isValidating,
  }
}

export default useSuspenseSWR
