import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { NotificationsProvider } from '@mantine/notifications'
import { MantineProvider } from '@mantine/core'

type Props = {
  children: React.ReactNode
}

export const TestApp = ({ children }: Props) => (
  <QueryClientProvider client={new QueryClient()}>
    <MantineProvider withGlobalStyles withNormalizeCSS>
      <NotificationsProvider>{children}</NotificationsProvider>
    </MantineProvider>
  </QueryClientProvider>
)
