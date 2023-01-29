import React, { useEffect } from 'react'
import { useRouter } from 'next/router'
import { IconAlien } from '@tabler/icons'
import { AppShell, Box, Container, Group, Navbar, Text, useMantineTheme } from '@mantine/core'
import { AdminMainLinks } from './AdminMainLinks'
import { AdminBottomMenu } from './AdminBottomMenu'
import { useCheckMe } from '@/services/auth/api/useCheckMe'

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
      navbar={
        <Navbar width={{ base: 240 }} p="xs" zIndex={1}>
          <Navbar.Section>
            <Box
              sx={{
                borderBottom: '1px solid',
                borderColor: theme.colors.gray[2],
              }}
              p="sm"
            >
              <Group>
                <IconAlien />
                <Text weight="bold">itomise admin</Text>
              </Group>
            </Box>
          </Navbar.Section>
          <Navbar.Section grow pt="md">
            <AdminMainLinks />
          </Navbar.Section>
          <Navbar.Section>
            <AdminBottomMenu />
          </Navbar.Section>
        </Navbar>
      }
      header={<Box sx={{ display: 'none' }} />}
      styles={{
        main: { backgroundColor: theme.colors.gray[0] },
      }}
    >
      <Container pt="xl" pb="xl">
        {children}
      </Container>
    </AppShell>
  )
}
