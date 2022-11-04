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
      <Container sx={{ background: theme.colors.gray[2] }}>
        <main>
          <Center style={{ width: '100%', height: '100vh' }}>
            <Paper radius="md" p={80}>
              <Title order={1}>Todo List</Title>
              <Stack spacing="md" align="center" mt="lg">
                <InternalLink href="/login">
                  <Button>ログインはこちら</Button>
                </InternalLink>
                <InternalLink href="/sign-up">
                  <Button>登録はこちら</Button>
                </InternalLink>
              </Stack>
            </Paper>
          </Center>
        </main>
      </Container>
    </>
  )
}
