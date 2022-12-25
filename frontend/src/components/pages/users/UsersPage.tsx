import { useRef } from 'react'
import Head from 'next/head'
import { Card, Title, Text, Group, Badge, Box, Grid, List } from '@mantine/core'
import { UsersEditUserButtonPopUp } from './_ui/UsersEditUserButtonPopUp'
import { UsersDeleteUserButtonPopUp } from './_ui/UsersDeleteUserButtonPopUp'
import { UsersCreateUserButtonPopUp } from './_ui/UsersCreateUserButtonPopUp'
import { useUserList } from '@/services/user/api/useUserList'
import { useCheckMe } from '@/services/auth/api/useCheckMe'
import { AdminLayout } from '@/components/shared/layout/AdminLayout'

export const UsersPage: React.FC = () => {
  const allUsers = useUserList()
  const rootRef = useRef<HTMLDivElement>(null)
  const { me } = useCheckMe()

  return (
    <>
      <Head>
        <title>users</title>
      </Head>
      <AdminLayout>
        <main ref={rootRef}>
          <Title order={1} size="h3">
            ユーザー一覧
          </Title>
          <Group mt="lg">
            <UsersCreateUserButtonPopUp />
          </Group>
          <List mt="lg" listStyleType="none" spacing="sm" styles={{ itemWrapper: { width: '100%' } }}>
            {allUsers?.map((user) => (
              <List.Item key={user.id}>
                <Card shadow="sm" p="md" radius="md" withBorder sx={{ width: '100%', overflow: 'initial' }}>
                  <Group position="apart" align="center" noWrap>
                    <Box>
                      <Group spacing="xs">
                        <Text weight={500} sx={{ wordBreak: 'break-all' }}>
                          {user.name}
                        </Text>
                        {me?.id === user.id && <Badge color="blue">Me</Badge>}
                      </Group>
                      <Text color="gray" size="xs">
                        {user.email}
                      </Text>
                    </Box>
                    <Group>
                      <UsersEditUserButtonPopUp user={user} />
                      <UsersDeleteUserButtonPopUp user={user} />
                    </Group>
                  </Group>
                </Card>
              </List.Item>
            ))}
          </List>
        </main>
      </AdminLayout>
    </>
  )
}
