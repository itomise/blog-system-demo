import { adminAppAxios } from '@/libs/axios'
import { MutationConfig } from '@/libs/react-query'
import { UUID } from '@/shared/types'
import { useMutation } from '@tanstack/react-query'
import { PostStatus } from '../types'

type Request = {
  id: UUID
  title: string
  content: string
}

const publishPostApi = async (request: Request) => {
  const { id, ...payload } = request
  await adminAppAxios.put(`/posts/${id}/publish`, payload)
}

type Props = MutationConfig<typeof publishPostApi>

export const usePublishPost = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: publishPostApi,
  })
