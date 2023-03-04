import { BlogPostListResponse } from '@/blog/services/post/api/useGetListBlogPost'
import IndexPage from '@/pages'
import { render, screen, within } from '@testing-library/react'
import { expect, it } from 'vitest'
import { TestApp } from '../helper/TestApp'

const stub: BlogPostListResponse['posts'] = [
  {
    id: '595e3574-69eb-48cd-9f73-a5f330bf0bd5',
    title: 'テストタイトル1',
    content: '<p>コンテンツ1</p>',
    displayContent: 'コンテンツ1',
    publishedAt: '',
  },
  {
    id: 'c71b347e-8a92-472a-83c3-46d953670e3c',
    title: 'テストタイトル2',
    content: '<p>コンテンツ2</p>',
    displayContent: 'コンテンツ2',
    publishedAt: '',
  },
]

it('home', async () => {
  render(
    <TestApp>
      <IndexPage posts={stub} />
    </TestApp>
  )
  const main = within(screen.getByRole('main'))
  expect(main.getByRole('heading', { level: 1, name: /itomise Blog/i })).toBeDefined()

  expect((await main.findAllByRole('article')).length === 2)
  expect(await main.findByRole('heading', { level: 3, name: /テストタイトル1/i })).toBeDefined()
  expect(await main.findByText(/コンテンツ1/i)).toBeDefined()
})
