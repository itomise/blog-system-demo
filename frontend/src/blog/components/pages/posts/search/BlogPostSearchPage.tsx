import { useEffect, useMemo } from 'react'
import { useRouter } from 'next/router'
import Link from 'next/link'
import Head from 'next/head'
import { IconSearch } from '@tabler/icons'
import { Box, Title, Text, Card, TextInput, Loader, Grid } from '@mantine/core'
import { BlogTemplate } from '../../../shared/layout/BlogTemplate'
import { usePostSearchState } from './BlogPostSearchPage.hooks'
import { formatDate } from '@/shared/utils/dateUtil'

export const BlogPostSearchPage: React.FC = () => {
  const { query, replace } = useRouter()
  const pageQuery = useMemo(() => query.query, [query.query])
  const { register, data, isLoading, searchedQuery } = usePostSearchState(pageQuery as string | undefined)
  const isBlankSearch = !searchedQuery

  useEffect(() => {
    if (searchedQuery) {
      replace(`/posts/search?query=${searchedQuery}`)
    } else if (searchedQuery === '') {
      replace('/posts/search')
    }
    // 無限ループになるため
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchedQuery])

  return (
    <>
      <Head>
        <title>Search | itomise blog demo</title>
      </Head>

      <BlogTemplate>
        <Title order={1} sx={{ visibility: 'hidden', position: 'absolute' }}>
          Search
        </Title>
        <Box>
          <TextInput
            {...register}
            icon={isLoading ? <Loader size={20} /> : <IconSearch size={20} />}
            placeholder="キーワードを入力..."
            size="lg"
          />
          <Box mt="md">
            <Grid grow>
              {data?.posts.map((post) => (
                <Grid.Col md={5} key={post.id}>
                  <Link href={`/posts/${post.id}`}>
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
            {data?.posts.length === 0 && !isBlankSearch && (
              <Text color="gray.8" size="sm" mt="md">
                検索結果がありません。
              </Text>
            )}
          </Box>
        </Box>
      </BlogTemplate>
    </>
  )
}
