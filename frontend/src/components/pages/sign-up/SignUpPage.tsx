import { z } from 'zod'
import { useRouter } from 'next/router'
import { showNotification } from '@mantine/notifications'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useSignup } from '@/services/auth/api/useSignup'
import { InternalLink } from '@/components/shared/link/InternalLink'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().email(),
})

type FormType = z.infer<typeof schema>

export const SignUpPage: React.FC = () => {
  const router = useRouter()
  const { mutate, isLoading } = useSignup({
    onSuccess: () => {
      showNotification({
        message: '仮登録が完了しました。メールアドレスをご確認ください。',
        color: 'green',
      })
      router.push('/sign-up/sent')
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: '登録に失敗しました。',
        message: e.message,
      })
    },
  })
  const theme = useMantineTheme()

  return (
    <Container sx={{ background: theme.colors.gray[2] }}>
      <main>
        <Center sx={{ width: '100%', height: '100vh' }}>
          <Paper p={80} radius="md" sx={{ maxWidth: 500, width: '100%' }}>
            <Title order={1} align="center">
              新規登録
            </Title>
            <Form<FormType> onSubmit={(data) => mutate(data)} schema={schema}>
              {({ register, formState: { errors } }) => (
                <Stack spacing="md" mt="md">
                  <InputField
                    label="メールアドレス"
                    type="email"
                    error={errors.email}
                    registration={register('email')}
                    required
                  />
                  <Button type="submit" loading={isLoading}>
                    送信
                  </Button>
                  <InternalLink href="/">
                    <Button fullWidth>Topへ戻る</Button>
                  </InternalLink>
                </Stack>
              )}
            </Form>
          </Paper>
        </Center>
      </main>
    </Container>
  )
}
