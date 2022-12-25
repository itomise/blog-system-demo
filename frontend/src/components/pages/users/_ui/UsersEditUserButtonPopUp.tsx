import { z } from 'zod'
import { useRef } from 'react'
import { IconPencil } from '@tabler/icons'
import { showNotification } from '@mantine/notifications'
import { Popover, ActionIcon, Stack, Button, Divider, Text, Group } from '@mantine/core'
import { User } from '@/services/user/types'
import { useEditUser } from '@/services/user/api/useEditUser'
import { useDeleteUser } from '@/services/user/api/useDeleteUser'
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
    <Popover width={400} trapFocus position="bottom-start" withArrow shadow="md" zIndex={10}>
      <Popover.Target>
        <ActionIcon ref={buttonRef}>
          <IconPencil size={14} />
        </ActionIcon>
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
            name: user.name,
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
              <Button type="submit" mt="md" loading={isLoading}>
                送信
              </Button>
              <Divider />
              <Popover shadow="md" withArrow>
                <Popover.Target>
                  <Button type="button" size="sm" color="red">
                    ユーザーを削除
                  </Button>
                </Popover.Target>
                <Popover.Dropdown>
                  <Group>
                    <Text size="sm">本当に削除しますか？</Text>
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
            </Stack>
          )}
        </Form>
      </Popover.Dropdown>
    </Popover>
  )
}
