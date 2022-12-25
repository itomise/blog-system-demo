import { FC, useRef } from 'react'
import { IconTrash } from '@tabler/icons'
import { showNotification } from '@mantine/notifications'
import { Popover, Button, Group, Text, ActionIcon } from '@mantine/core'
import { User } from '@/services/user/types'
import { useDeleteUser } from '@/services/user/api/useDeleteUser'
import { queryClient } from '@/libs/react-query'

type Props = {
  user: User
}

export const UsersDeleteUserButtonPopUp: FC<Props> = ({ user }) => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate: deleteUserMutate, isLoading: deleteUserLoading } = useDeleteUser({
    onSuccess: () => {
      showNotification({
        message: 'ユーザーを削除しました。',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/user'])
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'ユーザー削除に失敗しました。',
        message: e.message,
      })
    },
  })

  return (
    <Popover shadow="md" withArrow trapFocus position="bottom-end">
      <Popover.Target>
        <ActionIcon ref={buttonRef} color="red">
          <IconTrash size={14} />
        </ActionIcon>
      </Popover.Target>
      <Popover.Dropdown>
        <Text size="xs">本当に削除しますか？</Text>
        <Group mt="sm">
          <Button type="button" size="xs" onClick={() => buttonRef.current?.click()}>
            キャンセル
          </Button>
          <Button
            type="button"
            color="red"
            size="xs"
            onClick={() => deleteUserMutate({ id: user.id })}
            loading={deleteUserLoading}
          >
            削除
          </Button>
        </Group>
      </Popover.Dropdown>
    </Popover>
  )
}
