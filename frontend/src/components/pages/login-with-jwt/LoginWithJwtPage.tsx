import { z } from 'zod'
import { useState } from 'react'
import Head from 'next/head'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme, Text } from '@mantine/core'
import { useLoginWithJwt } from '@/services/auth/api/useLoginWithJwt'
import { useGetMeWithJwt } from '@/services/auth/api/useGetMeWithJwt'
import { appAxios } from '@/libs/axios'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().min(5).max(255).email(),
  password: z.string(),
})

type FormType = z.infer<typeof schema>

export const LoginWithJwtPage: React.FC = () => {
  const [jwtToken, setJwtToken] = useState<string | undefined>()
  const getMeRes = useGetMeWithJwt(jwtToken)
  const { mutate } = useLoginWithJwt({
    config: {
      onSuccess: (res) => setJwtToken(res.token),
    },
  })
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
                  mutate(data)
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
                    appAxios.get('/auth-jwt/logout')
                  }}
                >
                  ログアウト
                </Button>
              </Stack>

              <Text variant="text" size="sm" mt="lg">
                ログイン状態 : {getMeRes ?? '未ログイン'}
              </Text>
            </Paper>
          </Center>
        </main>
      </Container>
    </>
  )
}
