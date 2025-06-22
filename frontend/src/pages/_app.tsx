import Head from 'next/head'
import type { AppProps } from 'next/app'
import { QueryClientProvider } from '@tanstack/react-query'
import { Notifications } from '@mantine/notifications'
import { MantineProvider, MantineThemeOverride } from '@mantine/core'
import '@mantine/core/styles.css'
import '@mantine/notifications/styles.css'
import '../styles/globals.css'
import { queryClient } from '@/libs/react-query'

export const theme: MantineThemeOverride = {
  primaryColor: 'gray',
  headings: {
    fontFamily: 'inherit',
    fontWeight: '700',
  },
}

const MyApp = ({ Component, pageProps }: AppProps) => (
  <>
    <Head>
      <title>Todo App</title>
      <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width" />
    </Head>
    <QueryClientProvider client={queryClient}>
      <MantineProvider theme={theme}>
        <Component {...pageProps} />
        <Notifications />
      </MantineProvider>
    </QueryClientProvider>
  </>
)

export default MyApp
