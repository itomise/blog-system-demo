import { z } from 'zod'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useLoginWithSession } from '@/services/auth/api/useLogin'
import { appAxios } from '@/libs/axios'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().min(5).max(255).email(),
  password: z.string(),
})

type SignUpFormType = z.infer<typeof schema>

export const LoginWithSessionPage: React.FC = () => {
  const loginMutation = useLoginWithSession()
  const theme = useMantineTheme()

  return (
    <Container sx={{ background: theme.colors.gray[2] }}>
      <main>
        <Center sx={{ width: '100%', height: '100vh' }}>
          <Paper p={80} radius="md" sx={{ maxWidth: 500, width: '100%' }}>
            <Title order={1} align="center">
              ログイン (Session)
            </Title>
            <Form<SignUpFormType>
              onSubmit={(data) => {
                loginMutation.mutate(data)
              }}
              schema={schema}
            >
              {({ register, formState: { errors } }) => (
                <Stack spacing="md" mt="md">
                  <InputField
                    label="メールアドレス"
                    type="email"
                    placeholder="example@example.com"
                    error={errors.email}
                    registration={register('email')}
                    required
                  />
                  <InputField
                    label="パスワード"
                    type="password"
                    error={errors.password}
                    registration={register('password')}
                    required
                  />
                  <Button type="submit" mt="lg">
                    送信
                  </Button>
                </Stack>
              )}
            </Form>
            <Stack spacing={2} mt="lg">
              <Button
                onClick={async () => {
                  const res = await appAxios.get('/auth-session/me')
                  console.log(res.data)
                }}
              >
                get me api
              </Button>
              <Button
                onClick={async () => {
                  appAxios.get('/auth-session/logout')
                }}
              >
                ログアウト
              </Button>
            </Stack>
          </Paper>
        </Center>
      </main>
    </Container>
  )
}
