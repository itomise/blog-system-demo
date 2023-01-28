import { FC } from 'react'
import { useRouter } from 'next/router'
import { IconChevronRight, IconChevronLeft, IconLogout } from '@tabler/icons'
import { useDisclosure } from '@mantine/hooks'
import { Avatar, Box, Group, UnstyledButton, useMantineTheme, Text, Menu } from '@mantine/core'
import { useMe } from '@/services/auth/api/useMe'
import { useLogout } from '@/services/auth/api/useLogout'
import { queryClient } from '@/libs/react-query'

export const AdminBottomMenu: FC = () => {
  const theme = useMantineTheme()
  const me = useMe()
  const [opened, { close, open }] = useDisclosure(false)
  const { mutate, isLoading } = useLogout()
  const router = useRouter()

  const onLogout = async () => {
    mutate(undefined)
    await router.push('/')
    queryClient.clear()
  }
  if (!me) return null

  return (
    <Box
      sx={{
        paddingTop: theme.spacing.sm,
        borderTop: `1px solid ${theme.colorScheme === 'dark' ? theme.colors.dark[4] : theme.colors.gray[2]}`,
      }}
    >
      <Menu shadow="md" width={200} position="right-end" opened={opened} onClose={close}>
        <Menu.Target>
          <UnstyledButton
            sx={{
              display: 'block',
              width: '100%',
              padding: theme.spacing.xs,
              borderRadius: theme.radius.sm,
              '&:hover': {
                backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
              },
            }}
            onClick={open}
          >
            <Group>
              <Avatar
                src="https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=255&q=80"
                radius="xl"
              />
              <Box sx={{ flex: 1 }}>
                <Text size="sm" weight={500}>
                  {me.name}
                </Text>
                <Text color="dimmed" size="xs" sx={{ width: 110, overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  {me.email}
                </Text>
              </Box>

              {theme.dir === 'ltr' ? <IconChevronRight size={18} /> : <IconChevronLeft size={18} />}
            </Group>
          </UnstyledButton>
        </Menu.Target>

        <Menu.Dropdown>
          <Menu.Label>Application</Menu.Label>

          <Menu.Item
            icon={<IconLogout size={14} />}
            onClick={onLogout}
            color="red"
            sx={{ backgroundColor: isLoading ? theme.colors.gray[5] : undefined }}
          >
            Logout
          </Menu.Item>
        </Menu.Dropdown>
      </Menu>
    </Box>
  )
}
