import { BlogPostDetailPage } from '@/blog/components/pages/detail/BlogPostDetailPage'
import { getBlogPostDetail } from '@/blog/services/post/api/useGetBlogPost'
import { GetServerSideProps } from 'next'

export default BlogPostDetailPage

export const getServerSideProps: GetServerSideProps = async ({ query: { id } }) => {
  const post = await getBlogPostDetail(id as string)

  return { props: { post } }
}
