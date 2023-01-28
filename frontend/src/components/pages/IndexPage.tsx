import Head from 'next/head'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { InternalLink } from '../shared/link/InternalLink'

export const IndexPage: React.FC = () => {
  const theme = useMantineTheme()

  return (
    <>
      <Head>
        <title>TOP | TodoList</title>
      </Head>

      <main>
        <Center sx={{ background: theme.colors.gray[2], width: '100%', height: '100vh' }}>
          <Paper radius="md" p={80}>
            <Title order={1}>Admin</Title>
            <Stack spacing="md" align="center" mt="lg">
              <InternalLink href="/admin/login">
                <Button>ログインはこちら (Session)</Button>
              </InternalLink>
              <InternalLink href="/admin/login-with-jwt">
                <Button>ログインはこちら (Jwt)</Button>
              </InternalLink>
              <InternalLink href="/admin/sign-up">
                <Button>登録はこちら</Button>
              </InternalLink>
            </Stack>
          </Paper>
        </Center>
      </main>
    </>
  )
}
