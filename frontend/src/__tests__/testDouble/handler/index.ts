import { publicPostsHandler } from './public/posts'
import { adminUsersHandler } from './admin/users'
import { adminPostsHandler } from './admin/posts'
import { adminMeHandler } from './admin/me'

export const handlers = [...publicPostsHandler, ...adminMeHandler, ...adminPostsHandler, ...adminUsersHandler]
