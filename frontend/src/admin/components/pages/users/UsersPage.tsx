import Head from 'next/head'
import { Card, Text, Group, Badge, Box, List } from '@mantine/core'
import { UsersEditUserButtonPopUp } from './_ui/UsersEditUserButtonPopUp'
import { UsersDeleteUserButtonPopUp } from './_ui/UsersDeleteUserButtonPopUp'
import { UsersCreateUserButtonPopUp } from './_ui/UsersCreateUserButtonPopUp'
import { useUserList } from '@/admin/services/user/api/useUserList'
import { useCheckMe } from '@/admin/services/auth/api/useMe'
import { AdminBreadcrumbs } from '@/admin/components/shared/link/AdminBreadcrumbs'
import { AdminTemplate } from '@/admin/components/shared/layout/AdminTemplate'

export const UsersPage: React.FC = () => {
  const allUsers = useUserList()
  const { me } = useCheckMe()

  return (
    <>
      <Head>
        <title>ユーザー一覧 | itomise blog admin</title>
      </Head>
      <AdminTemplate>
        <AdminBreadcrumbs links={[{ title: 'ユーザー一覧', href: null }]} />
        <Group mt="lg">
          <UsersCreateUserButtonPopUp />
        </Group>
        <List mt="md" listStyleType="none" spacing="sm" styles={{ itemWrapper: { width: '100%' } }}>
          {allUsers?.map((user) => (
            <List.Item key={user.id}>
              <Card shadow="sm" p="md" radius="md" withBorder sx={{ width: '100%', overflow: 'initial' }}>
                <Group position="apart" align="center" noWrap>
                  <Box>
                    <Group spacing="xs" aria-label="名前">
                      <Text weight={500} sx={{ wordBreak: 'break-all' }}>
                        {user.name ?? '( 未設定 )'}
                      </Text>
                      {me?.id === user.id && <Badge color="blue">Me</Badge>}
                    </Group>
                    <Text color="gray" size="xs">
                      {user.email}
                    </Text>
                  </Box>
                  <Group>
                    <UsersEditUserButtonPopUp user={user} />
                    <UsersDeleteUserButtonPopUp user={user} isMe={me?.id === user.id} />
                  </Group>
                </Group>
              </Card>
            </List.Item>
          ))}
        </List>
      </AdminTemplate>
    </>
  )
}
