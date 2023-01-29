import { appAxios } from '@/libs/axios'
import { MutationConfig } from '@/libs/react-query'
import { UUID } from '@/types'
import { useMutation } from '@tanstack/react-query'

type Request = {
  id: UUID
  title: string
  content: string
}

const updatePostApi = async (request: Request) => {
  const { id, ...payload } = request
  await appAxios.put(`/posts/${id}`, payload)
}

type Props = MutationConfig<typeof updatePostApi>

export const useUpdatePost = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: updatePostApi,
  })
