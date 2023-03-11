import Link from 'next/link'
import Head from 'next/head'
import { Card, Text, Title, Grid, Box } from '@mantine/core'
import { BlogTemplate } from '../shared/layout/BlogTemplate'
import { formatDate } from '@/shared/utils/dateUtil'
import { BlogPostListResponse } from '@/blog/services/post/api/useGetListBlogPost'

type PageProps = {
  posts: BlogPostListResponse['posts']
}

export const IndexPage: React.FC<PageProps> = ({ posts }) => (
  <>
    <Head>
      <title>TOP | TodoList</title>
    </Head>

    <BlogTemplate>
      <Title order={1} size="h2">
        itomise Blog.
      </Title>
      <Text mt="xs" size="sm">
        ソフトウェアエンジニアをやっている itomise のブログです。
        <br />
        記事にならないようなカジュアルな内容のものを書いていきたいです。
      </Text>

      <Box mt="lg">
        <Title order={2} size="h4">
          最近の記事
        </Title>
        <Grid mt="xs" grow>
          {posts.map((post) => (
            <Grid.Col md={5} key={post.id}>
              <Link href={`posts/${post.id}`}>
                <Card shadow="lg" p="md" component="article">
                  <Title order={3} size="h5">
                    {post.title}
                  </Title>
                  <Text lineClamp={2} size="xs" mt="xs">
                    {post.displayContent}
                  </Text>
                  <Text color="gray.6" size="xs" mt="xs">
                    {post.publishedAt && formatDate(post.publishedAt)}
                  </Text>
                </Card>
              </Link>
            </Grid.Col>
          ))}
        </Grid>
      </Box>
    </BlogTemplate>
  </>
)
