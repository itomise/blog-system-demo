import { css } from '@emotion/react'
import type { NextPage } from 'next'
import Head from 'next/head'
import { IndexPage } from '@/components/pages/index.page'

const Home: NextPage = () => {
  return (
    <>
      <Head>
        <title>都道府県別の総人口推移グラフ</title>
      </Head>
      <IndexPage />
    </>
  )
}

export default Home
