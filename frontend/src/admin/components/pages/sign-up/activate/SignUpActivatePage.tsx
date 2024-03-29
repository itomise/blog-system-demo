import { z } from 'zod'
import { useMemo } from 'react'
import { useRouter } from 'next/router'
import jwtDecode from 'jwt-decode'
import { showNotification } from '@mantine/notifications'
import { Button, Stack, Title } from '@mantine/core'
import { onMutateError } from '@/shared/api/onMutateError'
import { UserLoginType, UserLoginTypeValue } from '@/admin/services/user/types'
import { DecodedActivateTokenType, PasswordRegex } from '@/admin/services/auth/constant'
import { useActivateUser } from '@/admin/services/auth/api/useActivateUser'
import { SystemTemplate } from '@/admin/components/shared/layout/SystemTemplate'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'

const schema = z.object({
  name: z.string().min(1).max(50),
  password: z.string().min(4).max(100).regex(PasswordRegex).optional(),
})

type FormType = z.infer<typeof schema>

export const SignUpActivatePage: React.FC = () => {
  const router = useRouter()
  const token = router.query.token as string
  const { mutate, isLoading } = useActivateUser({
    onSuccess: () => {
      showNotification({
        message: 'ユーザー登録が完了しました。',
        color: 'green',
      })
      router.push('/admin/users')
    },
    onError: onMutateError('ユーザー登録が失敗しました。'),
  })
  const loginType = useMemo((): UserLoginType => {
    if (router.isReady) {
      const decodedJwt = jwtDecode<DecodedActivateTokenType>(token)
      if (decodedJwt.loginType === UserLoginTypeValue.EXTERNAL_GOOGLE.toString())
        return UserLoginTypeValue.EXTERNAL_GOOGLE
    }
    return UserLoginTypeValue.INTERNAL
  }, [router.isReady, token])

  return (
    <main>
      <SystemTemplate>
        <Title order={1} align="center">
          ユーザー初期設定
        </Title>
        <Form<FormType>
          onSubmit={(data) =>
            mutate({
              ...data,
              token,
            })
          }
          options={{
            shouldUnregister: true,
          }}
          schema={schema}
        >
          {({ register, formState: { errors } }) => (
            <Stack spacing="md" mt="md">
              <InputField label="名前" error={errors.name} registration={register('name')} required />
              {loginType === UserLoginTypeValue.INTERNAL && (
                <InputField
                  label="パスワード"
                  type="password"
                  error={errors.password}
                  registration={register('password')}
                  labelProps={{
                    autoComplete: 'new-password',
                    name: 'password',
                  }}
                  required
                  inputProps={{
                    autoComplete: 'new-password',
                  }}
                />
              )}
              <Button type="submit" mt="md" loading={isLoading} color="blue">
                送信
              </Button>
            </Stack>
          )}
        </Form>
      </SystemTemplate>
    </main>
  )
}
