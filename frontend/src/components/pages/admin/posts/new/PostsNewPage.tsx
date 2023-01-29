import Head from 'next/head'
import { Box, Button, Stack, Group } from '@mantine/core'
import { AdminTemplate } from '@/components/shared/layout/AdminTemplate'
import { AdminBreadcrumbs } from '@/components/shared/link/AdminBreadcrumbs'
import { PostRichTextEditor } from '@/services/posts/components/PostRichTextEditor'
import { Form } from '@/components/shared/form/Form'
import { InputField } from '@/components/shared/form/InputField'
import { z } from 'zod'
import { useCreatePost } from '@/services/posts/api/useCreatePost'
import { useRouter } from 'next/router'
import { showNotification } from '@mantine/notifications'
import { onMutateError } from '@/services/common/api/onMutateError'

const schema = z.object({
  title: z.string().min(1).max(255),
  content: z.string(),
})

type FormType = z.infer<typeof schema>

export const PostsNewPage: React.FC = () => {
  const router = useRouter()
  const { mutate, isLoading } = useCreatePost({
    onSuccess: () => {
      router.push('/admin/posts')
      showNotification({
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
        <main>
          <Form<FormType>
            onSubmit={(data) => mutate(data)}
            schema={schema}
            defaultValues={{
              content: '',
            }}
          >
            {({ register, setValue, formState: { errors }, watch }) => (
              <>
                <Group position="apart" align="flex-start">
                  <AdminBreadcrumbs
                    links={[
                      { title: 'ポスト一覧', href: '/admin/posts' },
                      { title: 'ポスト新規作成', href: null },
                    ]}
                  />
                  <Box>
                    <Button color="blue" type="submit" loading={isLoading}>
                      保存する
                    </Button>
                  </Box>
                </Group>
                <Stack spacing="sm" mt="sm">
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
        </main>
      </AdminTemplate>
    </>
  )
}
