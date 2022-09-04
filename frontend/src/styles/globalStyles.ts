import { css } from '@emotion/react'
import { theme } from './constants'

export const globalStyles = css({
  '*': {
    margin: '0',
    padding: '0',
    border: '0',
    fontSize: '100%',
    font: 'inherit',
    verticalAlign: 'baseline',
  },
  'article, aside, details, figcaption, figure, footer, header, hgroup, main, menu, nav, section': {
    display: 'block',
  },
  '*[hidden]': {
    display: 'none',
  },
  body: {
    lineHeight: '1',
    fontFamily: 'system-ui',
  },
  'ol, ul': {
    listStyle: 'none',
  },
  'blockquote, q': {
    quotes: 'none',
  },
  'blockquote:before, blockquote:after, q:before, q:after': {
    content: '',
    // @ts-ignore
    content: 'none',
  },
  table: {
    borderSpacing: '0',
  },
})
