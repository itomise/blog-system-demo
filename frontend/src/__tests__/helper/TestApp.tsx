import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Notifications } from '@mantine/notifications'
import { MantineProvider } from '@mantine/core'
import { RouterContext } from 'next/dist/shared/lib/router-context.shared-runtime'

type Props = {
  children: React.ReactNode
}

const mockRouter = {
  push: () => {},
  replace: () => {},
  prefetch: () => {},
  back: () => {},
  forward: () => {},
  refresh: () => {},
  pathname: '/',
  route: '/',
  query: {},
  asPath: '/',
  events: {
    on: () => {},
    off: () => {},
    emit: () => {},
  },
  beforePopState: () => {},
  isFallback: false,
  isLocaleDomain: false,
  isReady: true,
  defaultLocale: 'en',
  domainLocales: [],
  isPreview: false
}

export const TestApp = ({ children }: Props) => (
  <QueryClientProvider client={new QueryClient()}>
    <RouterContext.Provider value={mockRouter as any}>
      <MantineProvider>
        {children}
        <Notifications />
      </MantineProvider>
    </RouterContext.Provider>
  </QueryClientProvider>
)
