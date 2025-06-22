import React, { useEffect } from 'react'
import { useRouter } from 'next/router'
import { IconAlien } from '@tabler/icons-react'
import { AppShell, Box, Container, Group, Text, useMantineTheme } from '@mantine/core'
import { AdminMainLinks } from './AdminMainLinks'
import { AdminBottomMenu } from './AdminBottomMenu'
import { useCheckMe } from '@/admin/services/auth/api/useMe'

type Props = {
  children: React.ReactNode
}

export const AdminTemplate: React.FC<Props> = ({ children }) => {
  const theme = useMantineTheme()
  const { me, isLoading } = useCheckMe()
  const router = useRouter()

  useEffect(() => {
    if (!isLoading && !me) router.push('/')
  }, [isLoading, me, router])

  if (!me) return null

  return (
    <AppShell
      navbar={{ width: { base: 240 }, breakpoint: 'sm' }}
      styles={{
        main: { backgroundColor: theme.colors.gray[0] },
      }}
    >
      <AppShell.Navbar p="xs" zIndex={1}>
        <AppShell.Section>
          <Box
            style={{
              borderBottom: '1px solid',
              borderColor: theme.colors.gray[2],
            }}
            p="sm"
          >
            <Group>
              <IconAlien />
              <Text fw="bold">itomise admin</Text>
            </Group>
          </Box>
        </AppShell.Section>
        <AppShell.Section grow pt="md">
          <AdminMainLinks />
        </AppShell.Section>
        <AppShell.Section>
          <AdminBottomMenu />
        </AppShell.Section>
      </AppShell.Navbar>
      <AppShell.Main>
        <Container pt="xl" pb="xl">
          {children}
        </Container>
      </AppShell.Main>
    </AppShell>
  )
}
