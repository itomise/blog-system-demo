import { UUID } from '@/shared/types'

export const UserLoginType = {
  INTERNAL: 1,
  EXTERNAL_GOOGLE: 2,
} as const
export type UserLoginType = typeof UserLoginType[keyof typeof UserLoginType]

export type User = {
  id: UUID
  name: string | null
  email: string
  isActive: boolean
}
