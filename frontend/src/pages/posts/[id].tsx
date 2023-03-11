import { GetServerSideProps } from 'next'
import { getBlogPostDetail } from '@/blog/services/post/api/useGetBlogPost'
import { BlogPostDetailPage } from '@/blog/components/pages/posts/detail/BlogPostDetailPage'

export default BlogPostDetailPage

export const getServerSideProps: GetServerSideProps = async ({ query: { id } }) => {
  const post = await getBlogPostDetail(id as string)

  return { props: { post } }
}
