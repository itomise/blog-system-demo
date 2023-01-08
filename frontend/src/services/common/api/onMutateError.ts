import { AxiosError } from 'axios'
import { NotificationProps, showNotification } from '@mantine/notifications'

export const onMutateError = (toastTitle: string) => (e: AxiosError<unknown, any>) => {
  const base: NotificationProps = {
    color: 'red',
    title: toastTitle,
    message: '',
  }

  if (e.response) {
    if (e.response.status === 400) {
      if (e.response?.data) {
        if (typeof e.response.data === 'string') {
          base.message = e.response.data
        }
      }
    }
  }

  showNotification(base)
}
