import { GetBlogPostResponse } from '@/blog/services/post/api/useGetBlogPost'
import { formatDate } from '@/shared/utils/dateUtil'
import { Box, Container, Title, TypographyStylesProvider, Text, Card, useMantineTheme } from '@mantine/core'
import Head from 'next/head'
import { BlogTemplate } from '../../shared/layout/BlogTemplate'

type PageProps = {
  post: GetBlogPostResponse
}

export const BlogPostDetailPage: React.FC<PageProps> = ({ post }) => {
  const theme = useMantineTheme()

  return (
    <>
      <Head>
        <title>{post.title} | itomise blog</title>
      </Head>

      <BlogTemplate>
        <Title order={1} size="h2">
          {post.title}
        </Title>
        <Text mt="xs" size="sm" color="gray.6 ">
          投稿日 : {formatDate(post.publishedAt)}
        </Text>
        <Box mt="lg">
          <Card>
            <TypographyStylesProvider>
              <div dangerouslySetInnerHTML={{ __html: post.content }} className="postEditor" />
            </TypographyStylesProvider>
          </Card>
        </Box>
      </BlogTemplate>
    </>
  )
}
