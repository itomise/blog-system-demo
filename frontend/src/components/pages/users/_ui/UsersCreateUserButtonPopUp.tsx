import { z } from 'zod'
import { useRef } from 'react'
import { showNotification } from '@mantine/notifications'
import { Popover, Button, Stack } from '@mantine/core'
import { PasswordRegex } from '@/services/auth/constant'
import { useSignup } from '@/services/auth/api/useSignup'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  name: z.string().min(5).max(255),
  email: z.string().email(),
  password: z.string().min(6).max(100).regex(PasswordRegex),
})
type FormType = z.infer<typeof schema>

export const UsersCreateUserButtonPopUp: React.FC = () => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate, isLoading } = useSignup({
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
                label="名前"
                error={errors.name}
                placeholder="テスト太郎"
                registration={register('name')}
                required
                size="xs"
              />
              <InputField
                label="メールアドレス"
                type="email"
                placeholder="example@example.com"
                error={errors.email}
                registration={register('email')}
                required
                size="xs"
              />
              <InputField
                label="パスワード"
                type="password"
                error={errors.password}
                registration={register('password')}
                size="xs"
                required
              />
              <Button type="submit" mt="lg" loading={isLoading}>
                送信
              </Button>
            </Stack>
          )}
        </Form>
      </Popover.Dropdown>
    </Popover>
  )
}
