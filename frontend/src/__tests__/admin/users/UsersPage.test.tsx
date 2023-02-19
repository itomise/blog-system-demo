import UsersPage from '@/pages/admin/users'
import { TestApp } from '@/__tests__/helper/TestApp'
import { render, screen, waitFor, within } from '@testing-library/react'
import { expect, test, vi, describe, beforeEach } from 'vitest'
import userEvent from '@testing-library/user-event'
import { server } from '@/__tests__/testDouble/server'
import { rest } from 'msw'
import { GetListUserResponse } from '@/admin/services/user/api/useUserList'
import { queryClient } from '@/libs/react-query'
import { debug } from 'vitest-preview'

// モック用に追記
vi.mock('next/router', () => ({
  useRouter() {
    return {
      asPath: '/admin/users',
    }
  },
}))

const usersPageRender = () =>
  render(
    <TestApp>
      <UsersPage />
    </TestApp>
  )

describe('admin/users ページ', () => {
  beforeEach(() => {
    server.use(
      rest.get('http://localhost:8080/api/admin/users', (req, res, ctx) => {
        return res(
          ctx.status(200),
          ctx.json<GetListUserResponse>({
            users: [
              {
                id: '13c5909a-32b9-4edf-80c6-27225ce8238e',
                name: 'テスト太郎1',
                email: 'test1@test.test',
                isActive: true,
              },
              {
                id: '4e23c2cb-7470-4e2c-ab92-d25a1d7f08fd',
                name: 'テスト太郎2',
                email: 'test2@test.test',
                isActive: true,
              },
              {
                id: '4ef9a6fa-ed8c-40c2-a85d-45a64271ea79',
                name: null,
                email: 'test3@test.test',
                isActive: false,
              },
            ],
          })
        )
      }),
      rest.put('http://localhost:8080/api/admin/users/:userId', (req, res, ctx) => {
        return res(ctx.status(200))
      })
    )
  })
  test('正しく表示されること', async () => {
    usersPageRender()
    const main = within(await screen.findByRole('main'))

    expect(main.getByRole('heading', { level: 1, name: /ユーザー一覧/i })).toBeDefined()
  })

  test('ユーザーが編集できること', async () => {
    const { findAllByLabelText, findByLabelText, getByText } = usersPageRender()
    const userEditButtons = await findAllByLabelText('ユーザー編集ボタン')
    await userEvent.click(userEditButtons[0])
    const modal = await findByLabelText('ユーザー編集モーダル')
    const spy = vi.spyOn(queryClient, 'invalidateQueries').mockImplementation(async () => {})

    await userEvent.type(modal.querySelector('input')!!, 'test')
    await userEvent.click(getByText('送信'))

    expect(spy).toHaveBeenCalledTimes(1)
    expect(spy).toHaveBeenCalledWith(['/user'])
  })
  test('ユーザー編集の名前が空だった場合送信できないこと', async () => {
    usersPageRender()
    const userEditButtons = await screen.findAllByLabelText('ユーザー編集ボタン')
    await userEvent.click(userEditButtons[0])
    const modal = await screen.findByLabelText('ユーザー編集モーダル')
    const spy1 = vi.spyOn(queryClient, 'invalidateQueries').mockImplementation(async () => {})
    expect(spy1).toHaveBeenCalledTimes(0)

    await userEvent.clear(modal.querySelector('input')!!)
    await userEvent.click(screen.getByText('送信'))

    expect(spy1).toHaveBeenCalledTimes(0)
  })
})
