import { z } from 'zod'
import { useRouter } from 'next/router'
import Head from 'next/head'
import { notifications } from '@mantine/notifications'
import { Box, Button, Stack, Group } from '@mantine/core'
import { onMutateError } from '@/shared/api/onMutateError'
import { PostRichTextEditor } from '@/admin/services/post/components/PostRichTextEditor'
import { useCreatePost } from '@/admin/services/post/api/useCreatePost'
import { AdminBreadcrumbs } from '@/admin/components/shared/link/AdminBreadcrumbs'
import { AdminTemplate } from '@/admin/components/shared/layout/AdminTemplate'
import { InputField } from '@/admin/components/shared/form/InputField'
import { Form } from '@/admin/components/shared/form/Form'

const schema = z.object({
  title: z.string().min(1).max(255),
  content: z.string(),
})

type FormType = z.infer<typeof schema>

export const PostsNewPage: React.FC = () => {
  const router = useRouter()
  const { mutate, isPending } = useCreatePost({
    onSuccess: () => {
      router.push('/admin/posts')
      notifications.show({
        color: 'green',
        message: 'ポストを作成しました。',
      })
    },
    onError: onMutateError('ポストの作成に失敗しました。'),
  })

  return (
    <>
      <Head>
        <title>ポスト新規作成 | itomise admin</title>
      </Head>
      <AdminTemplate>
        <Form<FormType>
          onSubmit={(data) => mutate(data)}
          schema={schema}
          defaultValues={{
            content: '',
          }}
        >
          {({ register, setValue, formState: { errors }, watch }) => (
            <>
              <Group justify="space-between" align="flex-start">
                <AdminBreadcrumbs
                  links={[
                    { title: 'ポスト一覧', href: '/admin/posts' },
                    { title: 'ポスト新規作成', href: null },
                  ]}
                />
                <Box>
                  <Button color="blue" type="submit" loading={isPending}>
                    保存する
                  </Button>
                </Box>
              </Group>
              <Stack gap="sm" mt="sm">
                <InputField
                  label="タイトル"
                  size="xs"
                  placeholder="タイトル"
                  error={errors.title}
                  registration={register('title')}
                  required
                />
              </Stack>
              <Box mt="md">
                <PostRichTextEditor value={watch('content')} onChange={(value) => setValue('content', value)} />
              </Box>
            </>
          )}
        </Form>
      </AdminTemplate>
    </>
  )
}
