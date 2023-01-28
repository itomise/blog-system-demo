import { z } from 'zod'
import { useRouter } from 'next/router'
import Head from 'next/head'
import { showNotification } from '@mantine/notifications'
import { Anchor, Button, Center, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useLoginWithSession } from '@/services/auth/api/useLoginWithSession'
import { authRepository } from '@/repositories/auth'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'
import { IconBrandGoogle } from '@tabler/icons'
import { useToggle } from '@mantine/hooks'
import { SystemTemplate } from '@/components/shared/layout/SystemTemplate'
import { InternalLink } from '@/components/shared/link/InternalLink'

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
    authRepository.googleOAuth2()
  }

  return (
    <>
      <Head>
        <title>login with session</title>
      </Head>

      <main>
        <SystemTemplate>
          <Title order={1} align="center">
            Login
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
                  label="Email"
                  type="email"
                  placeholder="example@example.com"
                  error={errors.email}
                  registration={register('email')}
                  required
                />
                <InputField
                  label="Password"
                  type="password"
                  error={errors.password}
                  registration={register('password')}
                  required
                />
                <Button type="submit" loading={isLoading} mt="sm">
                  Login
                </Button>

                <Center>
                  <InternalLink href="/admin/sign-up">Sign Up</InternalLink>
                </Center>
                {/* <Center>
                    <InternalLink href="">
                      I've forgotten my password
                    </InternalLink>
                  </Center> */}

                <Button
                  onClick={onClickGoogleLogin}
                  leftIcon={<IconBrandGoogle />}
                  color="blue"
                  mt="lg"
                  loading={isGoogleLoginStart}
                >
                  Login with Google
                </Button>
              </Stack>
            )}
          </Form>
        </SystemTemplate>
      </main>
    </>
  )
}
