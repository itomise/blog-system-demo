import { z } from 'zod'
import { useRef } from 'react'
import { IconPencil } from '@tabler/icons'
import { showNotification } from '@mantine/notifications'
import { Popover, ActionIcon, Stack, Button, Group, Box, Tooltip, Text } from '@mantine/core'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'
import { useMe } from '@/admin/services/auth/api/useMe'
import { useUpdateUser } from '@/admin/services/user/api/useUpdateUser'
import { User } from '@/admin/services/user/types'

const schema = z.object({
  name: z.string().min(5).max(255),
})

type FormType = z.infer<typeof schema>

type Props = {
  user: User
}

export const UsersEditUserButtonPopUp: React.FC<Props> = ({ user }) => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const me = useMe()
  const { mutate, isLoading } = useUpdateUser({
    onSuccess: () => {
      showNotification({
        message: 'ユーザー情報を更新しました。',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['admin', 'user'])
      if (me.id === user.id) {
        queryClient.invalidateQueries(['admin', 'auth', 'me'])
      }
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'ユーザー情報の更新に失敗しました。',
        message: e.message,
      })
    },
  })

  return (
    <Popover width={400} trapFocus position="right-start" withArrow shadow="md">
      <Popover.Target>
        <Tooltip label="未設定のユーザーは編集できません。" disabled={user.isActive}>
          <Box>
            <ActionIcon ref={buttonRef} disabled={!user.isActive} role="button" aria-label="ユーザー編集ボタン">
              <IconPencil size={14} />
            </ActionIcon>
          </Box>
        </Tooltip>
      </Popover.Target>
      <Popover.Dropdown aria-label="ユーザー編集モーダル">
        <Form<FormType>
          onSubmit={(data) => {
            mutate({
              id: user.id,
              ...data,
            })
          }}
          schema={schema}
          defaultValues={{
            name: user.name ?? '',
          }}
        >
          {({ register, formState: { errors } }) => (
            <Stack spacing="sm">
              <InputField label="名前" error={errors.name} registration={register('name')} required size="xs" />
              <Group position="right">
                <Button type="submit" size="xs" loading={isLoading}>
                  送信
                </Button>
              </Group>
            </Stack>
          )}
        </Form>
      </Popover.Dropdown>
    </Popover>
  )
}
