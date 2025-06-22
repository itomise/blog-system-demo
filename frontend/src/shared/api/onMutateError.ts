import { AxiosError } from 'axios'
import { NotificationData, notifications } from '@mantine/notifications'

export const onMutateError = (toastTitle: string) => (e: AxiosError<unknown, unknown>) => {
  const base: NotificationData = {
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

  notifications.show(base)
}
