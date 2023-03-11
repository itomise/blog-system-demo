export const PostStatus = {
  UN_PUBLISHED: 1,
  PUBLISHED: 2,
} as const

export type PostStatusType = typeof PostStatus[keyof typeof PostStatus]

export const PostStatusLabel: Record<PostStatusType, string> = {
  [PostStatus.UN_PUBLISHED]: '非公開中',
  [PostStatus.PUBLISHED]: '公開中',
}
