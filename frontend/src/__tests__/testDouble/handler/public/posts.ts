import { rest } from 'msw'

export const publicPostsHandler = [
  rest.get('http://localhost:8081/api/public/posts', (req, res, ctx) => {
    return res(ctx.text('OK'))
  }),
]
