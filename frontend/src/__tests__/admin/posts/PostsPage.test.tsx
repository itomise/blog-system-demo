import PostsPage from '@/pages/admin/posts'
import { TestApp } from '@/__tests__/helper/TestApp'
import { render, screen, within } from '@testing-library/react'
import { expect, vi, describe, it } from 'vitest'

// モック用に追記
vi.mock('next/router', () => ({
  useRouter() {
    return {
      asPath: '/admin/posts',
    }
  },
}))

describe.concurrent('admin/posts ページ', async () => {
  it.concurrent('正しく表示されること', async () => {
    render(
      <TestApp>
        <PostsPage />
      </TestApp>
    )
    const main = within(await screen.findByRole('main'))
    expect(main.getByRole('heading', { level: 1, name: /ポスト一覧/i })).toBeDefined()
  })
})
