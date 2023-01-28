import { Center, Text, Loader, Skeleton } from '@mantine/core'
import dynamic from 'next/dynamic'

export const PostRichTextEditor = dynamic(
  () => import('@/services/posts/components/PostRichTextEditor/PostRichTextEditor'),
  {
    ssr: false,
    loading: () => <Skeleton visible height={150} />,
  }
)
