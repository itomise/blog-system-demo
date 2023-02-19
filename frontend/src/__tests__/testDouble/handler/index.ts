import { adminMeHandler } from './admin/me'
import { adminPostsHandler } from './admin/posts'
import { adminUsersHandler } from './admin/users'
import { publicPostsHandler } from './public/posts'

export const handlers = [...publicPostsHandler, ...adminMeHandler, ...adminPostsHandler, ...adminUsersHandler]
