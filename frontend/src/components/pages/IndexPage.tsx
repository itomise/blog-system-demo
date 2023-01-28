import Head from 'next/head'
import { Card, Container, Image, Text, Title, useMantineTheme, Grid, Group, Badge } from '@mantine/core'
import Link from 'next/link'

const data: {
  image: string
  title: string
  tags: string[]
  date: string
}[] = [
  {
    image:
      'https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=255&q=80',
    title: 'タイトル',
    tags: ['technologies', 'next.js'],
    date: '2023.01.28',
  },
  {
    image:
      'https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=255&q=80',
    title: 'タイトル',
    tags: ['technologies', 'next.js'],
    date: '2023.01.28',
  },
  {
    image:
      'https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=255&q=80',
    title: 'タイトル',
    tags: ['technologies', 'next.js'],
    date: '2023.01.28',
  },
  {
    image:
      'https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=255&q=80',
    title: 'タイトル',
    tags: ['technologies', 'next.js'],
    date: '2023.01.28',
  },
]

export const IndexPage: React.FC = () => {
  const theme = useMantineTheme()

  return (
    <>
      <Head>
        <title>TOP | TodoList</title>
      </Head>

      <main>
        <Container py={36}>
          <Title order={1} size="h2">
            itomise Blog.
          </Title>
          <Text mt="xs" size="sm">
            ソフトウェアエンジニアをやっているitomiseのブログです。
            <br />
            記事にならないようなカジュアルな内容のものを書いていきたいです。
          </Text>

          <Grid mt="lg">
            {data.map((d) => (
              <Grid.Col span={5}>
                <Link href="/">
                  <Card shadow="lg" p="sm" component="article">
                    <Card.Section>
                      <Image src={d.image} height={160} alt="Norway" />
                    </Card.Section>
                    <Title order={3} size="h5" mt="xs">
                      {d.title}
                    </Title>
                    {d.tags.length > 0 && (
                      <Group mt="xs" spacing="xs">
                        {d.tags.map((t) => (
                          <Badge>{t}</Badge>
                        ))}
                      </Group>
                    )}
                    <Text color="gray.6" size="xs" mt="xs">
                      {d.date}
                    </Text>
                  </Card>
                </Link>
              </Grid.Col>
            ))}
          </Grid>
        </Container>
      </main>
    </>
  )
}
