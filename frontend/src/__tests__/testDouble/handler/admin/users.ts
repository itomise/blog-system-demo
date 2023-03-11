import { rest } from 'msw'
import { GetListUserResponse } from '@/admin/services/user/api/useUserList'

export const adminUsersHandler = [
  rest.get('http://localhost:8080/api/admin/users', (req, res, ctx) =>
    res(
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
  ),
]
