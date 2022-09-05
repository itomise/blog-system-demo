import { css } from '@emotion/react'
import type { NextPage } from 'next'
import Head from 'next/head'
import { IndexPage } from '@/components/pages/index.page'

const Home: NextPage = () => {
  return (
    <>
      <Head>
        <title>itomise blog</title>
      </Head>
      <IndexPage />
    </>
  )
}

export default Home
