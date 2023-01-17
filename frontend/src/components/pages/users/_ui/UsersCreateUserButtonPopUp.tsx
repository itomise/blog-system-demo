import { z } from 'zod'
import { useRef } from 'react'
import { showNotification } from '@mantine/notifications'
import { Popover, Button, Stack, Box, Group } from '@mantine/core'
import { useCreateUser } from '@/services/user/api/useCreateUser'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().email(),
})
type FormType = z.infer<typeof schema>

export const UsersCreateUserButtonPopUp: React.FC = () => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate, isLoading } = useCreateUser({
    onSuccess: () => {
      showNotification({
        message: '仮ユーザー作成が完了しました。メール認証でアカウントがアクティベートされます。',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/user'])
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
