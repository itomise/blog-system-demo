import Head from 'next/head'
import { Box, Button, Text, Group, Table, Badge } from '@mantine/core'
import { AdminTemplate } from '@/admin/components/shared/layout/AdminTemplate'
import { AdminBreadcrumbs } from '@/admin/components/shared/link/AdminBreadcrumbs'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'
import { useRouter } from 'next/router'
import { useGetListPost } from '@/admin/services/post/api/useGetListPost'
import { PostStatus, PostStatusLabel } from '@/admin/services/post/types'

export const PostsPage: React.FC = () => {
  const data = useGetListPost()
  const router = useRouter()

  return (
    <>
      <Head>
        <title>ポスト一覧 | itomise admin</title>
      </Head>
      <AdminTemplate>
        <AdminBreadcrumbs links={[{ title: 'ポスト一覧', href: null }]} />
        <Group mt="lg">
          <InternalLink href="/admin/posts/new">
            <Button>ポスト新規作成</Button>
          </InternalLink>
        </Group>
        <Box mt="md">
          <Table highlightOnHover horizontalSpacing="md" verticalSpacing="md">
            <thead>
              <tr>
                <th>ステータス</th>
                <th>タイトル</th>
                <th>コンテンツ</th>
              </tr>
            </thead>
            <tbody>
              {data?.posts.map((post) => (
                <tr onClick={() => router.push(`/admin/posts/${post.id}`)} style={{ cursor: 'pointer' }} key={post.id}>
                  <td>
                    <Badge color={post.status === PostStatus.PUBLISHED ? 'green' : 'gray'}>
                      {PostStatusLabel[post.status]}
                    </Badge>
                  </td>
                  <td>{post.title}</td>
                  <td>
                    <Text lineClamp={2}>{post.displayContent}</Text>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Box>
      </AdminTemplate>
    </>
  )
}
