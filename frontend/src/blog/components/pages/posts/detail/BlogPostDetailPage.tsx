import Head from 'next/head'
import { Box, Title, TypographyStylesProvider, Text, Card, Center } from '@mantine/core'
import { BlogTemplate } from '../../../shared/layout/BlogTemplate'
import { formatDate } from '@/shared/utils/dateUtil'
import { GetBlogPostResponse } from '@/blog/services/post/api/useGetBlogPost'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'

type PageProps = {
  post: GetBlogPostResponse
}

export const BlogPostDetailPage: React.FC<PageProps> = ({ post }) => (
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
            {/* eslint-disable-next-line react/no-danger */}
            <div dangerouslySetInnerHTML={{ __html: post.content }} className="postEditor" />
          </TypographyStylesProvider>
        </Card>
        <Center mt="md">
          <InternalLink href="/">TOP へ戻る</InternalLink>
        </Center>
      </Box>
    </BlogTemplate>
  </>
)
