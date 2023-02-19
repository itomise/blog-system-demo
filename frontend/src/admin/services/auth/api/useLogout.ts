import { useMutation } from '@tanstack/react-query'
import { MutationConfig } from '@/libs/react-query'
import { adminAppAxios } from '@/libs/axios'

const logout = async () => {
  await adminAppAxios.post('/auth/logout')
}

type Props = MutationConfig<typeof logout>

export const useLogout = (props: Props = {}) =>
  useMutation({
    ...props,
    mutationFn: logout,
  })
