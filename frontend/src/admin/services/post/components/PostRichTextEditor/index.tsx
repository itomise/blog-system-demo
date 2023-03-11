import dynamic from 'next/dynamic'
import { Skeleton } from '@mantine/core'

export const PostRichTextEditor = dynamic(
  () => import('@/admin/services/post/components/PostRichTextEditor/PostRichTextEditor'),
  {
    ssr: false,
    loading: () => <Skeleton visible height={150} />,
  }
)
