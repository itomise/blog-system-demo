import Head from 'next/head'
import { Breadcrumbs, Button, Container, Group, Title } from '@mantine/core'
import { AdminTemplate } from '@/components/shared/layout/AdminTemplate'
import { AdminBreadcrumbs } from '@/components/shared/link/AdminBreadcrumbs'
import { InternalLink } from '@/components/shared/link/InternalLink'

export const PostsPage: React.FC = () => {
  const template = 'template'
  return (
    <>
      <Head>
        <title>ポスト一覧 | itomise admin</title>
      </Head>
      <AdminTemplate>
        <main>
          <AdminBreadcrumbs links={[{ title: 'ポスト一覧', href: null }]} />
          <Group mt="sm">
            <InternalLink href="/admin/posts/new">
              <Button>ポスト新規作成</Button>
            </InternalLink>
          </Group>
        </main>
      </AdminTemplate>
    </>
  )
}
