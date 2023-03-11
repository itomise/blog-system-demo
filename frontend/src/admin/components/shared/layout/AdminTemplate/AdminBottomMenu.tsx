import { FC } from 'react'
import { useRouter } from 'next/router'
import { IconChevronRight, IconChevronLeft, IconLogout } from '@tabler/icons'
import { useDisclosure } from '@mantine/hooks'
import { Box, Group, UnstyledButton, useMantineTheme, Text, Menu } from '@mantine/core'
import { queryClient } from '@/libs/react-query'
import { useMe } from '@/admin/services/auth/api/useMe'
import { useLogout } from '@/admin/services/auth/api/useLogout'

export const AdminBottomMenu: FC = () => {
  const theme = useMantineTheme()
  const me = useMe()
  const [opened, { close, open }] = useDisclosure(false)
  const router = useRouter()
  const { mutate, isLoading } = useLogout({
    onSuccess: async () => {
      await router.push('/')
      queryClient.removeQueries({ queryKey: ['admin'] })
    },
  })

  const onLogout = async () => {
    mutate(undefined)
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
              {/* <Avatar
                src="https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=255&q=80"
                radius="xl"
              /> */}
              <Box sx={{ flex: 1 }}>
                <Text size="sm" weight={500}>
                  {me.name}
                </Text>
                <Text color="dimmed" size="xs" lineClamp={1}>
                  {me.email}
                </Text>
              </Box>

              {theme.dir === 'ltr' ? <IconChevronRight size={18} /> : <IconChevronLeft size={18} />}
            </Group>
          </UnstyledButton>
        </Menu.Target>

        <Menu.Dropdown>
          <Menu.Label>アプリケーション</Menu.Label>

          <Menu.Item
            icon={<IconLogout size={14} />}
            onClick={onLogout}
            color="red"
            sx={{ backgroundColor: isLoading ? theme.colors.gray[5] : undefined }}
          >
            ログアウト
          </Menu.Item>
        </Menu.Dropdown>
      </Menu>
    </Box>
  )
}
