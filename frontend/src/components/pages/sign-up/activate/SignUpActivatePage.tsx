import { z } from 'zod'
import { useEffect } from 'react'
import { useRouter } from 'next/router'
import { showNotification } from '@mantine/notifications'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { PasswordRegex } from '@/services/auth/constant'
import { useActivateUser } from '@/services/auth/api/useActivateUser'
import { InternalLink } from '@/components/shared/link/InternalLink'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  password: z.string().min(6).max(100).regex(PasswordRegex),
})

type FormType = z.infer<typeof schema>

export const SignUpActivatePage: React.FC = () => {
  const router = useRouter()
  const token = router.query.token as string
  const { mutate, isLoading } = useActivateUser({
    onSuccess: () => {
      showNotification({
        message: 'ユーザー登録が完了しました。',
        color: 'green',
      })
      router.push('/login')
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'ユーザー登録に失敗しました。',
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
              パスワード設定
            </Title>
            <Form<FormType>
              onSubmit={(data) =>
                mutate({
                  password: data.password,
                  token,
                })
              }
              schema={schema}
            >
              {({ register, formState: { errors } }) => (
                <Stack spacing="md" mt="md">
                  <InputField
                    label="パスワード"
                    type="password"
                    error={errors.password}
                    registration={register('password')}
                    labelProps={{
                      autoComplete: 'new-password',
                      name: 'password',
                    }}
                    required
                    inputProps={{
                      autoComplete: 'new-password',
                    }}
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
