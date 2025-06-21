import Head from 'next/head'
import type { AppProps } from 'next/app'
import { QueryClientProvider } from '@tanstack/react-query'
import { NotificationsProvider } from '@mantine/notifications'
import { MantineProvider, MantineThemeOverride } from '@mantine/core'
import { queryClient } from '@/libs/react-query'

export const theme: MantineThemeOverride = {
  colorScheme: 'light',
  primaryColor: 'gray',
  headings: {
    fontFamily: 'inherit',
    fontWeight: '700',
  },
  globalStyles: () => ({
    a: {
      textDecoration: 'none',
    },
  }),
}

const MyApp = ({ Component, pageProps }: AppProps) => (
  <>
    <Head>
      <title>Todo App</title>
      <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width" />
    </Head>
    <QueryClientProvider client={queryClient}>
      <MantineProvider withGlobalStyles withNormalizeCSS theme={theme}>
        <NotificationsProvider position="bottom-left">
          <Component {...pageProps} />
        </NotificationsProvider>
      </MantineProvider>
    </QueryClientProvider>
  </>
)

export default MyApp
