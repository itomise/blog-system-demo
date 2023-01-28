import { z } from 'zod'
import { useRouter } from 'next/router'
import { showNotification } from '@mantine/notifications'
import { Anchor, Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useSignup } from '@/services/auth/api/useSignup'
import { InternalLink } from '@/components/shared/link/InternalLink'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'
import { SystemTemplate } from '@/components/shared/layout/SystemTemplate'
import Link from 'next/link'

const schema = z.object({
  email: z.string().email(),
})

type FormType = z.infer<typeof schema>

export const SignUpPage: React.FC = () => {
  const router = useRouter()
  const { mutate, isLoading } = useSignup({
    onSuccess: () => {
      showNotification({
        message: 'Your temporary registration has been completed. Please confirm your e-mail address.',
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
          Sign Up
        </Title>
        <Form<FormType> onSubmit={(data) => mutate(data)} schema={schema}>
          {({ register, formState: { errors } }) => (
            <Stack spacing="md" mt="md">
              <InputField label="email" type="email" error={errors.email} registration={register('email')} required />
              <Button type="submit" loading={isLoading}>
                Sign Up
              </Button>
              <Center>
                <InternalLink href="/admin/login">Back to Login</InternalLink>
              </Center>
            </Stack>
          )}
        </Form>
      </SystemTemplate>
    </main>
  )
}
