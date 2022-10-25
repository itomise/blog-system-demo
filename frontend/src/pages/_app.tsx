/* eslint-disable react/jsx-props-no-spreading */
import { Global } from '@emotion/react'
import { QueryClientProvider } from '@tanstack/react-query'
import type { AppProps } from 'next/app'
import { globalStyles } from '@/styles/globalStyles'
import { queryClient } from '@/libs/react-query'

const MyApp = ({ Component, pageProps }: AppProps) => (
  <QueryClientProvider client={queryClient}>
    <Global styles={globalStyles} />

    <Component {...pageProps} />
  </QueryClientProvider>
)

export default MyApp
