/* eslint-disable react/jsx-props-no-spreading */
import { QueryClientProvider } from '@tanstack/react-query'
import type { AppProps } from 'next/app'
import { queryClient } from '@/libs/react-query'
import Head from 'next/head'
import { MantineProvider } from '@mantine/core'

const MyApp = ({ Component, pageProps }: AppProps) => (
  <>
    <Head>
      <title>Todo App</title>
      <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width" />
    </Head>
    <QueryClientProvider client={queryClient}>
      <MantineProvider
        withGlobalStyles
        withNormalizeCSS
        theme={{
          colorScheme: 'light',
        }}
      >
        <Component {...pageProps} />
      </MantineProvider>
    </QueryClientProvider>
  </>
)

export default MyApp
