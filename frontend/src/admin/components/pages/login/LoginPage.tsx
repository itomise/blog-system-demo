import { z } from 'zod'
import { useRouter } from 'next/router'
import Head from 'next/head'
import { showNotification } from '@mantine/notifications'
import { Anchor, Button, Center, Divider, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useLoginWithSession } from '@/admin/services/auth/api/useLoginWithSession'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'
import { IconBrandGoogle } from '@tabler/icons'
import { useToggle } from '@mantine/hooks'
import { SystemTemplate } from '@/admin/components/shared/layout/SystemTemplate'
import { InternalLink } from '@/admin/components/shared/link/InternalLink'
import { onGoogleOAuth2Request } from '@/admin/services/auth/api/onGoogleOAuth2Request'

const schema = z.object({
  email: z.string().min(5).max(255).email(),
  password: z.string(),
})

type FormType = z.infer<typeof schema>

export const LoginPage: React.FC = () => {
  const router = useRouter()
  const [isGoogleLoginStart, setIsGoogleLoginStart] = useToggle()
  const { mutate, isLoading } = useLoginWithSession({
    config: {
      onSuccess: () => {
        router.push('/admin/users')
      },
      onError: () => {
        showNotification({
          color: 'red',
          message: 'ユーザー名かパスワードが間違っています。',
        })
      },
    },
  })
  const theme = useMantineTheme()

  const onClickGoogleLogin = () => {
    setIsGoogleLoginStart(true)
    onGoogleOAuth2Request()
  }

  return (
    <>
      <Head>
        <title>login with session</title>
      </Head>

      <main>
        <SystemTemplate>
          <Title order={1} align="center">
            ログイン
          </Title>
          <Button
            onClick={onClickGoogleLogin}
            leftIcon={<IconBrandGoogle />}
            color="blue"
            mt="lg"
            loading={isGoogleLoginStart}
            variant="outline"
            fullWidth
          >
            Google でログイン
          </Button>
          <Divider mt="md" />
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
                <Button type="submit" loading={isLoading} mt="sm" color="blue">
                  ログイン
                </Button>

                <Center>
                  <InternalLink href="/admin/sign-up">アカウント作成はこちら</InternalLink>
                </Center>
                {/* <Center>
                    <InternalLink href="">
                      I've forgotten my password
                    </InternalLink>
                  </Center> */}
              </Stack>
            )}
          </Form>
        </SystemTemplate>
      </main>
    </>
  )
}
