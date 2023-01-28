import { z } from 'zod'
import { useRef } from 'react'
import { IconPencil } from '@tabler/icons'
import { showNotification } from '@mantine/notifications'
import { Popover, ActionIcon, Stack, Button, Group, Box, Tooltip } from '@mantine/core'
import { User } from '@/services/user/types'
import { useEditUser } from '@/services/user/api/useEditUser'
import { queryClient } from '@/libs/react-query'
import { InputField } from '@/components/shared/form/InputField'
import { Form } from '@/components/shared/form/Form'
import { useMe } from '@/services/auth/api/useMe'

const schema = z.object({
  name: z.string().min(5).max(255),
})

type FormType = z.infer<typeof schema>

type Props = {
  user: User
}

export const UsersEditUserButtonPopUp: React.FC<Props> = ({ user }) => {
  const buttonRef = useRef<HTMLButtonElement>(null)
  const me = useMe()
  const { mutate, isLoading } = useEditUser({
    onSuccess: () => {
      showNotification({
        message: 'Updated user information.',
        color: 'green',
      })
      buttonRef.current?.click()
      queryClient.invalidateQueries(['/user'])
      if (me.id === user.id) {
        queryClient.invalidateQueries(['auth/me'])
      }
    },
    onError: (e) => {
      showNotification({
        color: 'red',
        title: 'User update failed.',
        message: e.message,
      })
    },
  })

  return (
    <Popover width={400} trapFocus position="right-start" withArrow shadow="md">
      <Popover.Target>
        <Tooltip label="Unsettled accounts." disabled={user.isActive}>
          <Box>
            <ActionIcon ref={buttonRef} disabled={!user.isActive}>
              <IconPencil size={14} />
            </ActionIcon>
          </Box>
        </Tooltip>
      </Popover.Target>
      <Popover.Dropdown>
        <Form<FormType>
          onSubmit={(data) => {
            mutate({
              id: user.id,
              ...data,
            })
          }}
          schema={schema}
          defaultValues={{
            name: user.name ?? '',
          }}
        >
          {({ register, formState: { errors } }) => (
            <Stack spacing="sm">
              <InputField
                label="Name"
                error={errors.name}
                placeholder="John Smith"
                registration={register('name')}
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
