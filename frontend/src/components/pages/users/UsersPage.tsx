import { useRef } from 'react'
import Head from 'next/head'
import { Card, Container, Title, Text, Group, Badge, Box, Grid } from '@mantine/core'
import { UsersEditUserButtonPopUp } from './_ui/UsersEditUserButtonPopUp'
import { UsersCreateUserButtonPopUp } from './_ui/UsersCreateUserButtonPopUp'
import { useUserList } from '@/services/user/api/useUserList'
import { useGetMeWithSession } from '@/services/auth/api/useGetMeWithSession'

export const UsersPage: React.FC = () => {
  const allUsers = useUserList()
  const rootRef = useRef<HTMLDivElement>(null)
  const me = useGetMeWithSession()

  return (
    <>
      <Head>
        <title>users</title>
      </Head>
      <Container p="lg" ref={rootRef}>
        <main>
          <Title order={1} size="h3">
            ユーザー一覧
          </Title>
          <Group mt="lg">
            <UsersCreateUserButtonPopUp />
          </Group>
          <Grid mt="lg">
            {allUsers?.map((user) => (
              <Grid.Col key={user.id} span="content">
                <Card shadow="sm" p="lg" radius="md" withBorder sx={{ width: 280, overflow: 'initial' }}>
                  <Group position="apart" align="center" noWrap>
                    <Group spacing="xs">
                      <Text weight={500} sx={{ wordBreak: 'break-all' }}>
                        {user.name}
                      </Text>
                      {me?.id === user.id && <Badge color="blue">Me</Badge>}
                    </Group>
                    <UsersEditUserButtonPopUp user={user} />
                  </Group>
                  <Text color="gray" size="xs">
                    {user.email}
                  </Text>

                  <Box mt="sm">
                    <Text color="gray" size={10}>
                      {user.id}
                    </Text>
                  </Box>
                </Card>
              </Grid.Col>
            ))}
          </Grid>
        </main>
      </Container>
    </>
  )
}
