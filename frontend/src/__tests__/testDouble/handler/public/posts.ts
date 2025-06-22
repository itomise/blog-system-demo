import { http, HttpResponse } from 'msw'

export const publicPostsHandler = [
  http.get('http://localhost:8081/api/public/posts', () => new HttpResponse('OK', {
      headers: {
        'Content-Type': 'text/plain',
      },
    })),
]
