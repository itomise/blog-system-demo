import { UUID } from '@/shared/types'

export const UserLoginTypeValue = {
  INTERNAL: 1,
  EXTERNAL_GOOGLE: 2,
} as const
export type UserLoginType = typeof UserLoginTypeValue[keyof typeof UserLoginTypeValue]

export type User = {
  id: UUID
  name: string | null
  email: string
  isActive: boolean
}
