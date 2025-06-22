import { http, HttpResponse } from 'msw'
import { GetMeResponse } from '@/admin/services/auth/api/useMe'

export const adminMeHandler = [
  http.get('http://localhost:8080/api/admin/auth/me', () => HttpResponse.json<GetMeResponse>({
      id: '118f310c-a327-4189-a9f6-864f01cf46e5',
      email: 'test@test.test',
      name: 'テスト太郎',
      isActive: true,
    }, { status: 200 })),
]
