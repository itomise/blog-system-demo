import { IndexPage } from '@/blog/components/pages/IndexPage'
import { getListBlogPost } from '@/blog/services/post/api/useGetListBlogPost'

export default IndexPage

export async function getServerSideProps() {
  const { posts } = await getListBlogPost()

  return { props: { posts } }
}
