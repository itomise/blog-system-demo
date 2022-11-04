import Head from 'next/head'
import type { AppProps } from 'next/app'
import { QueryClientProvider } from '@tanstack/react-query'
import { MantineProvider } from '@mantine/core'
import { theme } from '@/styles/theme'
import { queryClient } from '@/libs/react-query'

const MyApp = ({ Component, pageProps }: AppProps) => (
  <>
    <Head>
      <title>Todo App</title>
      <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width" />
    </Head>
    <QueryClientProvider client={queryClient}>
      <MantineProvider withGlobalStyles withNormalizeCSS theme={theme}>
        <Component {...pageProps} />
      </MantineProvider>
    </QueryClientProvider>
  </>
)

export default MyApp
