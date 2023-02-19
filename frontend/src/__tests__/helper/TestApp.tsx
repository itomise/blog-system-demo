import { MantineProvider } from '@mantine/core'
import { NotificationsProvider } from '@mantine/notifications'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

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
