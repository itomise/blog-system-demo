import { AdminGetListPostsResponse } from '@/admin/services/post/api/useGetListPost'
import { rest } from 'msw'

export const adminPostsHandler = [
  rest.get('http://localhost:8080/api/admin/posts', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json<AdminGetListPostsResponse>({
        posts: [
          {
            id: '7fe35b6c-32c5-4e3c-8d01-1ee9369e40da',
            title: 'test post1',
            content: '<p>test1 content</p>',
            status: 1,
            displayContent: 'test1 content',
          },
          {
            id: '0e24848a-5625-4198-a201-c7d53c9c4aab',
            title: 'test post2',
            content: '<p>test2 content</p>',
            status: 2,
            displayContent: 'test2 content',
          },
        ],
      })
    )
  }),
]
