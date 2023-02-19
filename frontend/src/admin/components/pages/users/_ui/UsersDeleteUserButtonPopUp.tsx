import { FC, useRef } from 'react'
import { IconTrash } from '@tabler/icons'
import { showNotification } from '@mantine/notifications'
import { Popover, Button, Group, Text, ActionIcon, Tooltip, Box } from '@mantine/core'
import { queryClient } from '@/libs/react-query'
import { useDeleteUser } from '@/admin/services/user/api/useDeleteUser'
import { User } from '@/admin/services/user/types'

type Props = {
  user: User
  isMe: boolean
}

export const UsersDeleteUserButtonPopUp: FC<Props> = ({ user, isMe }) => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate: deleteUserMutate, isLoading: deleteUserLoading } = useDeleteUser({
    onSuccess: () => {
      showNotification({
        message: 'User deleted.',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/admin/user'])
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'User deletion failed.',
        message: e.message,
      })
    },
  })

  return (
    <Popover shadow="md" withArrow trapFocus position="bottom-end" disabled={isMe}>
      <Popover.Target>
        <Tooltip label="自分自身を削除することはできません。" disabled={!isMe}>
          <Box>
            <ActionIcon ref={buttonRef} color="red" disabled={isMe}>
              <IconTrash size={14} />
            </ActionIcon>
          </Box>
        </Tooltip>
      </Popover.Target>
      <Popover.Dropdown>
        <Text size="xs">本当に削除しますか？</Text>
        <Group mt="sm" position="right">
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
