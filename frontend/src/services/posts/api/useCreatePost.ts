import { appAxios } from '@/libs/axios'
import { MutationConfig } from '@/libs/react-query'
import { useMutation } from '@tanstack/react-query'

type Request = {
  title: string
  content: string
}

const createPostApi = async (request: Request) => {
  await appAxios.post('/posts', request)
}

type Props = MutationConfig<typeof createPostApi>

export const useCreatePost = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: createPostApi,
  })
