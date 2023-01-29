import { appAxios } from '@/libs/axios'
import { MutationConfig } from '@/libs/react-query'
import { UUID } from '@/types'
import { useMutation } from '@tanstack/react-query'
import { PostStatus } from '../types'

type Request = {
  id: UUID
  title: string
  content: string
}

const unPublishPostApi = async (request: Request) => {
  const { id, ...payload } = request
  await appAxios.put(`/posts/${id}/un-publish`, payload)
}

type Props = MutationConfig<typeof unPublishPostApi>

export const useUnPublishPost = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: unPublishPostApi,
  })
