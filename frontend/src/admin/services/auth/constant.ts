import { UserLoginType } from '../user/types'

// eslint-disable-next-line prefer-regex-literals
export const PasswordRegex = new RegExp(
  '^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})'
)

export const AccountOperationType = {
  ACTIVATE: 1,
  PASSWORD_RESET: 2,
} as const

export type AccountOperationType = typeof AccountOperationType[keyof typeof AccountOperationType]

export type DecodedActivateTokenType = {
  userId: string
  operationType: `${AccountOperationType}`
  expires: string
  loginType: `${UserLoginType}`
}
