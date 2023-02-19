import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

export type DeleteUserRequest = {
  id: string
}
const deleteUser = async (request: DeleteUserRequest) => {
  await adminAppAxios.delete(`/users/${request.id}`)
}

type Props = MutationConfig<typeof deleteUser>

export const useDeleteUser = (props: Props) =>
  useMutation({
    ...props,
    mutationFn: deleteUser,
  })
