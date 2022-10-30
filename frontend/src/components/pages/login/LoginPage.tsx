import { z } from 'zod'
import { Button, Container, Stack } from '@mantine/core'
import { useCreateUser } from '@/services/user/api/useCreateUser'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  name: z.string().min(5).max(255),
  email: z.string().email(),
})

type SignUpFormType = z.infer<typeof schema>

export const LoginPage: React.FC = () => {
  const createUserMutation = useCreateUser()

  return (
    <Container>
      <main>
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
              <Button type="submit">送信</Button>
            </Stack>
          )}
        </Form>
      </main>
    </Container>
  )
}
