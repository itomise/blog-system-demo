import { useMutation } from '@tanstack/react-query'
import { UUID } from '@/shared/types'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

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
