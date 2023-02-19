import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

export type EditUserRequest = {
  id: string
  name: string
}
const editUser = async ({ id, ...rest }: EditUserRequest) => {
  await adminAppAxios.put(`/users/${id}`, rest)
}

type Props = MutationConfig<typeof editUser>

export const useUpdateUser = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: editUser,
  })
