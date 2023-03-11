import { z } from 'zod'
import { useRouter } from 'next/router'
import { showNotification } from '@mantine/notifications'
import { Button, Center, Stack, Title } from '@mantine/core'
import { useSignup } from '@/admin/services/auth/api/useSignup'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'
import { SystemTemplate } from '@/admin/components/shared/layout/SystemTemplate'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'

const schema = z.object({
  email: z.string().email(),
})

type FormType = z.infer<typeof schema>

export const SignUpPage: React.FC = () => {
  const router = useRouter()
  const { mutate, isLoading } = useSignup({
    onSuccess: () => {
      showNotification({
        message: '仮登録が完了しました。メールアドレスを確認して登録を完了してください。',
        color: 'green',
      })
      router.push('/admin/sign-up/sent')
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'Registration failed.',
        message: e.message,
      })
    },
  })

  return (
    <main>
      <SystemTemplate>
        <Title order={1} align="center">
          アカウント作成
        </Title>
        <Form<FormType> onSubmit={(data) => mutate(data)} schema={schema}>
          {({ register, formState: { errors } }) => (
            <Stack spacing="md" mt="md">
              <InputField
                label="メールアドレス"
                placeholder="example@example.com"
                type="email"
                error={errors.email}
                registration={register('email')}
                required
              />
              <Button type="submit" loading={isLoading} color="blue">
                アカウント作成
              </Button>
              <Center>
                <InternalLink href="/admin/login">ログインへ戻る</InternalLink>
              </Center>
            </Stack>
          )}
        </Form>
      </SystemTemplate>
    </main>
  )
}
