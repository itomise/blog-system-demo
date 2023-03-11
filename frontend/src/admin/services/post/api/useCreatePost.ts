import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

type Request = {
  title: string
  content: string
}

const createPostApi = async (request: Request) => {
  await adminAppAxios.post('/posts', request)
}

type Props = MutationConfig<typeof createPostApi>

export const useCreatePost = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: createPostApi,
  })
