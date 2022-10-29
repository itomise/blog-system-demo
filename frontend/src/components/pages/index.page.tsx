import { useCreateUser } from '@/services/user/api/useCreateUser'
import { useUserList } from '@/services/user/api/useUserList'
import { Button, Card, Container, Grid, Stack, Text } from '@mantine/core'
import React from 'react'
import { z } from 'zod'
import { Form } from '../shared/form/Form'
import { InputField } from '../shared/form/InputField'

const schema = z.object({
  name: z.string().min(5).max(255),
  email: z.string().email(),
})

type RegisterFormType = z.infer<typeof schema>

export const IndexPage: React.FC = () => {
  const { users } = useUserList()
  const createUserMutation = useCreateUser()

  return (
    <Container>
      <main>
        <Form<RegisterFormType>
          onSubmit={(data) => {
            createUserMutation.mutate(data)
          }}
          schema={schema}
        >
          {({ register, formState: { errors } }) => (
            <Stack spacing={10} sx={{ marginTop: 10 }}>
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
                <Text>{user.id}</Text>
                <Text>{user.name}</Text>
                <Text>{user.email}</Text>
              </Card>
            </Grid.Col>
          ))}
        </Grid>
      </main>
    </Container>
  )
}
