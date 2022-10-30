import { z } from 'zod'
import { Button, Card, Container, Grid, Group, Stack, Text } from '@mantine/core'
import { useUserList } from '@/services/user/api/useUserList'
import { useCreateUser } from '@/services/user/api/useCreateUser'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  name: z.string().min(5).max(255),
  email: z.string().email(),
})

type SignUpFormType = z.infer<typeof schema>

export const SignUpPage: React.FC = () => {
  const { users } = useUserList()
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
        <Grid mt={20}>
          {users?.map((user) => (
            <Grid.Col key={user.id} span={4}>
              <Card shadow="sm" radius="md">
                <Group noWrap>
                  <Text sx={{ width: 40, flexShrink: 0 }}>id:</Text>
                  <Text>{user.id}</Text>
                </Group>
                <Group>
                  <Text sx={{ width: 40, flexShrink: 0 }}>name:</Text>
                  <Text>{user.name}</Text>
                </Group>
                <Group>
                  <Text sx={{ width: 40, flexShrink: 0 }}>email:</Text>
                  <Text>{user.email}</Text>
                </Group>
              </Card>
            </Grid.Col>
          ))}
        </Grid>
      </main>
    </Container>
  )
}