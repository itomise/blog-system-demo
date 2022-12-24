import { z } from 'zod'
import { useRouter } from 'next/router'
import Head from 'next/head'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useLoginWithSession } from '@/services/auth/api/useLoginWithSession'
import { appAxios } from '@/libs/axios'
import { InternalLink } from '@/components/shared/link/InternalLink'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().min(5).max(255).email(),
  password: z.string(),
})

type FormType = z.infer<typeof schema>

export const LoginWithSessionPage: React.FC = () => {
  const router = useRouter()
  const { mutate, isLoading } = useLoginWithSession({
    config: {
      onSuccess: () => {
        router.push('/users')
      },
    },
  })
  const theme = useMantineTheme()

  return (
    <>
      <Head>
        <title>login with session</title>
      </Head>
      <Container sx={{ background: theme.colors.gray[2] }}>
        <main>
          <Center sx={{ width: '100%', height: '100vh' }}>
            <Paper p={80} radius="md" sx={{ maxWidth: 500, width: '100%' }}>
              <Title order={1} align="center">
                ログイン (Session)
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
                    <Button type="submit" mt="lg" loading={isLoading}>
                      ログイン
                    </Button>
                  </Stack>
                )}
              </Form>
              <Stack spacing={2} mt="lg">
                <Button
                  onClick={async () => {
                    appAxios.get('/auth-session/logout')
                  }}
                >
                  ログアウト
                </Button>
                <InternalLink href="/">
                  <Button fullWidth>Topへ戻る</Button>
                </InternalLink>
              </Stack>
            </Paper>
          </Center>
        </main>
      </Container>
    </>
  )
}
