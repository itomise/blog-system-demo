import Link from 'next/link'
import Head from 'next/head'
import { Anchor, Card, Center, Container } from '@mantine/core'

export const IndexPage: React.FC = () => (
  <>
    <Head>
      <title>TodoList</title>
    </Head>
    <Container>
      <main>
        <Center>
          <Card style={{ width: '100vw', height: '100vh' }}>
            <Link href="/login">
              <Anchor component="span">ログインはこちら</Anchor>
            </Link>
          </Card>
        </Center>
      </main>
    </Container>
  </>
)
