import { z } from 'zod'
import { useRef } from 'react'
import { IconPencil } from '@tabler/icons'
import { showNotification } from '@mantine/notifications'
import { Popover, ActionIcon, Stack, Button, Group, Box, Tooltip } from '@mantine/core'
import { User } from '@/services/user/types'
import { useEditUser } from '@/services/user/api/useEditUser'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  name: z.string().min(5).max(255),
})

type FormType = z.infer<typeof schema>

type Props = {
  user: User
}

export const UsersEditUserButtonPopUp: React.FC<Props> = ({ user }) => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate, isLoading } = useEditUser({
    onSuccess: () => {
      showNotification({
        message: 'ユーザー情報を更新しました。',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/user'])
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'ユーザー更新に失敗しました。',
        message: e.message,
      })
    },
  })

  return (
    <Popover width={400} trapFocus position="right-start" withArrow shadow="md">
      <Popover.Target>
        <Tooltip label="未設定のアカウントです。" disabled={user.isActive}>
          <Box>
            <ActionIcon ref={buttonRef} disabled={!user.isActive}>
              <IconPencil size={14} />
            </ActionIcon>
          </Box>
        </Tooltip>
      </Popover.Target>
      <Popover.Dropdown>
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
              <InputField
                label="名前"
                error={errors.name}
                placeholder="テスト太郎"
                registration={register('name')}
                required
                size="xs"
              />
              <Group position="right">
                <Button type="submit" loading={isLoading}>
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
