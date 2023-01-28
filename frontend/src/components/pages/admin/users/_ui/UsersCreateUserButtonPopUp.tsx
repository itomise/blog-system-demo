import { z } from 'zod'
import { useRef } from 'react'
import { showNotification } from '@mantine/notifications'
import { Popover, Button, Stack, Box, Group } from '@mantine/core'
import { useCreateUser } from '@/services/user/api/useCreateUser'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'

const schema = z.object({
  email: z.string().email(),
})
type FormType = z.infer<typeof schema>

export const UsersCreateUserButtonPopUp: React.FC = () => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { mutate, isLoading } = useCreateUser({
    onSuccess: () => {
      showNotification({
        message: 'Temporary user creation is complete. Your account will be activated with email verification.',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/user'])
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'User creation failed.',
        message: e.message,
      })
    },
  })

  return (
    <Popover width={400} trapFocus position="bottom-start" withArrow shadow="md">
      <Popover.Target>
        <Button ref={buttonRef}>Create User</Button>
      </Popover.Target>
      <Popover.Dropdown>
        <Form<FormType>
          onSubmit={(data) => {
            mutate(data)
          }}
          schema={schema}
        >
          {({ register, formState: { errors } }) => (
            <Stack spacing="sm">
              <InputField
                label="Email"
                type="email"
                placeholder="example@example.com"
                error={errors.email}
                registration={register('email')}
                required
                size="xs"
              />
              <Group position="right">
                <Button type="submit" size="xs" loading={isLoading}>
                  Submit
                </Button>
              </Group>
            </Stack>
          )}
        </Form>
      </Popover.Dropdown>
    </Popover>
  )
}
