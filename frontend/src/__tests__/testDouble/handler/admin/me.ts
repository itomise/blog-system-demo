import { GetMeResponse } from '@/admin/services/auth/api/useMe'
import { rest } from 'msw'

export const adminMeHandler = [
  rest.get('http://localhost:8080/api/admin/auth/me', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json<GetMeResponse>({
        id: '118f310c-a327-4189-a9f6-864f01cf46e5',
        email: 'test@test.test',
        name: 'テスト太郎',
        isActive: true,
      })
    )
  }),
]
