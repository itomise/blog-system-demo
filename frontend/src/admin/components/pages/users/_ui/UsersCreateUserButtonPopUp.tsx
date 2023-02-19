import { z } from 'zod'
import { useRef } from 'react'
import { showNotification } from '@mantine/notifications'
import { Popover, Button, Stack, Group } from '@mantine/core'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'
import { useCreateUser } from '@/admin/services/user/api/useCreateUser'

const schema = z.object({
  email: z.string().email(),
})
type FormType = z.infer<typeof schema>

export const UsersCreateUserButtonPopUp: React.FC = () => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate, isLoading } = useCreateUser({
    onSuccess: () => {
      showNotification({
        message: '入力したメールアドレスに本登録のメールを送信しました。',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/admin/user'])
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'ユーザー作成に失敗しました。',
        message: e.message,
      })
    },
  })

  return (
    <Popover width={400} trapFocus position="bottom-start" withArrow shadow="md">
      <Popover.Target>
        <Button ref={buttonRef}>ユーザー作成</Button>
      </Popover.Target>
      <Popover.Dropdown>
        <Form<FormType>
          onSubmit={(data) => {
            mutate(data)
          }}
          schema={schema}
        >
          {({ register, formState: { errors } }) => (
            <Stack spacing="sm">
              <InputField
                label="メールアドレス"
                type="email"
                placeholder="example@example.com"
                error={errors.email}
                registration={register('email')}
                required
                size="xs"
              />
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
