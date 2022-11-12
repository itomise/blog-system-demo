import { z } from 'zod'
import { Button, Center, Container, Paper, Stack, Title, useMantineTheme } from '@mantine/core'
import { useUserList } from '@/services/user/api/useUserList'
import { useCreateUser } from '@/services/user/api/useCreateUser'
import { PasswordRegex } from '@/services/auth/constant'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  name: z.string().min(5).max(255),
  email: z.string().email(),
  password: z.string().min(6).max(100).regex(PasswordRegex),
})

type SignUpFormType = z.infer<typeof schema>

export const SignUpPage: React.FC = () => {
  const { users } = useUserList()
  const createUserMutation = useCreateUser()
  const theme = useMantineTheme()

  return (
    <Container sx={{ background: theme.colors.gray[2] }}>
      <main>
        <Center sx={{ width: '100%', height: '100vh' }}>
          <Paper p={80} radius="md" sx={{ maxWidth: 500, width: '100%' }}>
            <Title order={1} align="center">
              新規登録
            </Title>
            <Form<SignUpFormType>
              onSubmit={(data) => {
                createUserMutation.mutate(data)
              }}
              schema={schema}
            >
              {({ register, formState: { errors } }) => (
                <Stack spacing="md" mt="md">
                  <InputField label="名前" error={errors.name} registration={register('name')} required />
                  <InputField
                    label="メールアドレス"
                    type="email"
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
                  <Button type="submit">送信</Button>
                </Stack>
              )}
            </Form>
          </Paper>
        </Center>
      </main>
    </Container>
  )
}
