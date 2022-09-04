import { css } from '@emotion/react'
import React from 'react'

export const IndexPage: React.FC = () => {
  return (
    <main css={style.root}>
      <p>テストページ</p>
    </main>
  )
}

const style = {
  root: css({
    position: 'relative',
  }),
  prefectureSelect: css({
    marginTop: '24px',
  }),
}
