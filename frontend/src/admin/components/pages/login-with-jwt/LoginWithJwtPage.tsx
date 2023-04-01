import { z } from 'zod'
import { useState } from 'react'
import Head from 'next/head'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme, Text, List, Box } from '@mantine/core'
import { useGetMeWithJwt } from '@/admin/services/auth/api/useMe'
import { useLoginWithJwt } from '@/admin/services/auth/api/useLoginWithSession'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'

const schema = z.object({
  email: z.string().min(5).max(255).email(),
  password: z.string(),
})

type FormType = z.infer<typeof schema>

export const LoginWithJwtPage: React.FC = () => {
  const [jwtToken, setJwtToken] = useState<string | undefined>()
  const getMeRes = useGetMeWithJwt(jwtToken)
  const { mutate, isLoading } = useLoginWithJwt({
    config: {
      onSuccess: (res) => setJwtToken(res.token),
    },
  })
  const theme = useMantineTheme()

  return (
    <>
      <Head>
        <title>login | itomise admin</title>
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
                    <Button type="submit" mt="lg" loading={isLoading}>
                      送信
                    </Button>
                  </Stack>
                )}
              </Form>
              <Stack spacing={2} mt="lg">
                <Button onClick={async () => setJwtToken(undefined)}>ログアウト</Button>
                <InternalLink href="/">
                  <Button fullWidth>Topへ戻る</Button>
                </InternalLink>
              </Stack>

              <Box mt="lg">
                {getMeRes ? (
                  <List>
                    <List.Item>ユーザーID : {getMeRes.id}</List.Item>
                    <List.Item>メールアドレス : {getMeRes.email}</List.Item>
                    <List.Item>名前 : {getMeRes.name}</List.Item>
                  </List>
                ) : (
                  <Text variant="text" size="sm">
                    未ログイン
                  </Text>
                )}
              </Box>
            </Paper>
          </Center>
        </main>
      </Container>
    </>
  )
}
