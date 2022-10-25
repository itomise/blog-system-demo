import type { NextPage } from 'next'
import Head from 'next/head'
import { IndexPage } from '@/components/pages/index.page'

const Home: NextPage = () => (
  <>
    <Head>
      <title>todo list</title>
    </Head>
    <IndexPage />
  </>
)

export default Home
