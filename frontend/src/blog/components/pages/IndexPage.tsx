import Head from 'next/head'
import { Card, Container, Text, Title, Grid } from '@mantine/core'
import Link from 'next/link'
import { BlogPostListResponse, useGetListBlogPost } from '@/blog/services/post/api/useGetListBlogPost'
import { formatDate } from '@/shared/utils/dateUtil'
import { BlogTemplate } from '../shared/layout/BlogTemplate'

type PageProps = {
  posts: BlogPostListResponse['posts']
}

export const IndexPage: React.FC<PageProps> = ({ posts }) => {
  return (
    <>
      <Head>
        <title>TOP | TodoList</title>
      </Head>

      <BlogTemplate>
        <Title order={1} size="h2">
          itomise Blog.
        </Title>
        <Text mt="xs" size="sm">
          ソフトウェアエンジニアをやっているitomiseのブログです。
          <br />
          記事にならないようなカジュアルな内容のものを書いていきたいです。
        </Text>

        <Grid mt="lg">
          {posts.map((post) => (
            <Grid.Col span={5} key={post.id}>
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
      </BlogTemplate>
    </>
  )
}
