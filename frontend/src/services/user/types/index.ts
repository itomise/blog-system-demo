import { UUID } from '../../../types'

export type User = {
  id: UUID
  name: string | null
  email: string
  isActive: boolean
}
