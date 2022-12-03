import { z } from 'zod'
import Head from 'next/head'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useLoginWithJwt } from '@/services/auth/api/useLoginWithJwt'
import { appAxios } from '@/libs/axios'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().min(5).max(255).email(),
  password: z.string(),
})

type FormType = z.infer<typeof schema>

export const LoginWithJwtPage: React.FC = () => {
  const loginMutation = useLoginWithJwt()
  const theme = useMantineTheme()

  return (
    <>
      <Head>
        <title>login with jwt</title>
      </Head>
      <Container sx={{ background: theme.colors.gray[2] }}>
        <main>
          <Center sx={{ width: '100%', height: '100vh' }}>
            <Paper p={80} radius="md" sx={{ maxWidth: 500, width: '100%' }}>
              <Title order={1} align="center">
                ログイン (Jwt)
              </Title>
              <Form<FormType>
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
                    const res = await appAxios.get('/hello', {
                      headers: {
                        Authorization:
                          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwOi8vMC4wLjAuMDo4MDgwLyIsImlzcyI6Imh0dHA6Ly8wLjAuMC4wOjgwODAvIiwiZXhwIjoxNjcwMDY2NzY4LCJ1c2VySWQiOiIxNDUyYmRmYy05YjI5LTRiNjItOTEyMy1mMTllNWQ3YjY4MTUifQ.TCc4RPfGFn2V2vO__xVy-iYl6yScGEooMeyv2GSKVXREoGIiko971Q88sJV58xsPwDsdX0Z0OcNI_SHYkNDZdg',
                      },
                    })
                    console.log(res.data)
                  }}
                >
                  get me api
                </Button>
                <Button
                  onClick={async () => {
                    appAxios.get('/auth-jwt/logout')
                  }}
                >
                  ログアウト
                </Button>
              </Stack>
            </Paper>
          </Center>
        </main>
      </Container>
    </>
  )
}
